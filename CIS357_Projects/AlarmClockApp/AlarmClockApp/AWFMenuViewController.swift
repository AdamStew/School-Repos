//
//  AWFMenuViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 3/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import FirebaseDatabase
import FirebaseAuth

class AWFMenuViewController: AlarmViewController, UITableViewDataSource, UITableViewDelegate, CreateGroupAlarmViewControllerDelegate {

    @IBOutlet weak var welcomeLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    fileprivate var ref : FIRDatabaseReference?
    var userEmail : String?
    var userEmailNoTag : String?
    var validEntries : [AlarmKeyed]?
    var hostEntries : [AlarmKeyed]?
    var plebEntries : [AlarmKeyed]?
    var edittedAlarmIndex : Int?
    var gameAlarmIndex : Int?
    var cancelledAlarm : Alarm?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let email = self.userEmail {
            var slice = email.components(separatedBy: "@")
            self.welcomeLabel.text = "Welcome " + (slice[0]) + "!"
            slice = email.components(separatedBy: ".")
            self.userEmailNoTag = slice[0]
        }else{
            print("No username, somehow.")
        }
        
        self.validEntries = [AlarmKeyed]()
        self.hostEntries = [AlarmKeyed]()
        self.plebEntries = [AlarmKeyed]()
        self.ref = FIRDatabase.database().reference()
        self.registerForFireBaseUpdates()
        
        // This will remove extra separators from tableview
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillAppear(animated)
        updateActiveStatus()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.validEntries?.removeAll()
        self.validEntries!.append(contentsOf: self.hostEntries!)
        self.validEntries!.append(contentsOf: self.plebEntries!)
        self.tableView.reloadData()
        checkAlarms()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "addGroupAlarmSegue" {
            if let dest = segue.destination.childViewControllers[0] as? CreateGroupAlarmViewController {
                print("Adding a new alarm.")
                dest.delegate = self
            }
        } else if segue.identifier == "editGroupAlarmSegue" {
            if let dest = segue.destination.childViewControllers[0] as? CreateGroupAlarmViewController {
                print("Editting an old alarm.")
                dest.delegate = self
                dest.vcStatus = "Edit"
                dest.editAlarm = self.validEntries?[self.edittedAlarmIndex!].alarm
                self.cancelledAlarm = self.validEntries?[self.edittedAlarmIndex!].alarm
                self.ref?.child("users").child(self.userEmailNoTag!).child("groupalarms").child((self.validEntries?[self.edittedAlarmIndex!].key)!).removeValue() //Delete's host's.
                for i in 0...(self.validEntries?[self.edittedAlarmIndex!].alarm?.friends?.count)!-1 {
                    let tmpArray = self.validEntries?[self.edittedAlarmIndex!].alarm?.friends?[i].components(separatedBy: ".")
                    self.ref?.child("users").child((tmpArray?[0])!).child("shared").child((self.validEntries?[self.edittedAlarmIndex!].key)!).removeValue() //Delete's plebs'.
                }
                self.validEntries?.remove(at: self.edittedAlarmIndex!)
            }
        } else if segue.identifier == "cypherSegue" {
            if let dest = segue.destination.childViewControllers[0] as? GameCypherViewController {
                if !(self.validEntries?[self.gameAlarmIndex!].alarm?.repeating!)! {
                    let index = NSIndexPath(row: self.gameAlarmIndex!, section: 0)
                    let cell = self.tableView.cellForRow(at: index as IndexPath) as! GroupAlarmCell
                    cell.alarmSwitch.setOn(false, animated: true)
                }
                dest.gameAlarm = self.validEntries?[self.gameAlarmIndex!].alarm
            }
        } else if segue.identifier == "mathSegue" {
            if let dest = segue.destination.childViewControllers[0] as? MathGameViewController {
                if !(self.validEntries?[self.gameAlarmIndex!].alarm?.repeating!)! {
                    let index = NSIndexPath(row: self.gameAlarmIndex!, section: 0)
                    let cell = self.tableView.cellForRow(at: index as IndexPath) as! GroupAlarmCell
                    cell.alarmSwitch.setOn(false, animated: true)
                }
                dest.gameAlarm = self.validEntries?[self.gameAlarmIndex!].alarm
            }
        } else if segue.identifier == "emojiSegue" {
            if let dest = segue.destination.childViewControllers[0] as? EmojiGameViewController {
                if !(self.validEntries?[self.gameAlarmIndex!].alarm?.repeating!)! {
                    let index = NSIndexPath(row: self.gameAlarmIndex!, section: 0)
                    let cell = self.tableView.cellForRow(at: index as IndexPath) as! GroupAlarmCell
                    cell.alarmSwitch.setOn(false, animated: true)
                }
                dest.gameAlarm = self.validEntries?[self.gameAlarmIndex!].alarm
            }
        }
    }
    
    @IBAction func cancelFromCreateGroupAlarmButton(_ segue: UIStoryboardSegue) {
        print("Cancelled.")
        if cancelledAlarm != nil {
            
            let newChild = self.ref!.child("users").child(self.userEmailNoTag!).child("groupalarms").childByAutoId()
            newChild.setValue(self.toDictionary(vals: self.cancelledAlarm!, key: (newChild.key)))
            self.validEntries?.append(AlarmKeyed(alarm: self.cancelledAlarm!, key: (newChild.key), status: "host"))
            for i in 0...(self.cancelledAlarm?.friends!.count)!-1 {
                let splitFriend = self.cancelledAlarm?.friends![i].components(separatedBy: ".")
                let shareChild = self.ref!.child("users").child((splitFriend?[0])!).child("shared").child(newChild.key)
                shareChild.setValue(self.pathDictionary(path: "users|" + self.userEmailNoTag! + "|groupalarms|" + newChild.key))
            }
            cancelledAlarm = nil
        }
        self.tableView.reloadData()
    }
    
    fileprivate func registerForFireBaseUpdates() {
        
        //Get group alarms where YOU are the host.
        self.ref!.child("users").child(self.userEmailNoTag!).child("groupalarms").observe(.value, with: { snapshot in
            if let postDict = snapshot.value as? [String : AnyObject] {
                self.hostEntries?.removeAll()
                for (_,val) in postDict.enumerated() {
                    let entry = val.1 as! Dictionary<String,AnyObject>
                    let alarmName = entry["alarmName"] as! String?
                    let daysOfWeek = entry["daysOfWeek"] as! [String]?
                    let ringtone = entry["ringtone"] as! String?
                    let gameType = entry["gameType"] as! String?
                    let hour = entry["hour"] as! Int?
                    let minute = entry["minute"] as! Int?
                    let timeOfDay = entry["timeOfDay"] as! String?
                    let repeating = entry["repeating"] as! Bool?
                    let friends = entry["friends"] as! [String]?
                    let active = entry["active"] as! Bool?
                    let alarm = (Alarm(hour: hour!, minute: minute!, timeOfDay: timeOfDay!, gameType: gameType!, daysOfWeek: daysOfWeek!, repeating: repeating!, name: alarmName!, ringtone: ringtone!, active: active!, friends: friends!))
                    let key = entry["key"] as! String?
                    self.hostEntries!.append(AlarmKeyed(alarm: alarm, key: key!, status: "host"))
                }
                self.tableView.reloadData()
            }
        })
        
        //Get the group alarms where you are NOT the host.
        self.ref!.child("users").child(self.userEmailNoTag!).child("shared").observe(.value, with: { snapshot in
            if let postDict = snapshot.value as? [String : AnyObject] {
                for (_,val) in postDict.enumerated() {
                    let entry = val.1 as! Dictionary<String,AnyObject>
                    let path = entry["path"] as! String?
                    
                    
                    let pathArray = path!.components(separatedBy: "|")
                    self.ref!.child(pathArray[0]).child(pathArray[1]).child(pathArray[2]).observe(.value, with: { snapshot in
                        if let postDict = snapshot.value as? [String : AnyObject] {
                            self.plebEntries?.removeAll()
                            for (_,val) in postDict.enumerated() {
                                let entry = val.1 as! Dictionary<String,AnyObject>
                                let alarmName = entry["alarmName"] as! String?
                                let daysOfWeek = entry["daysOfWeek"] as! [String]?
                                let ringtone = entry["ringtone"] as! String?
                                let gameType = entry["gameType"] as! String?
                                let hour = entry["hour"] as! Int?
                                let minute = entry["minute"] as! Int?
                                let timeOfDay = entry["timeOfDay"] as! String?
                                let repeating = entry["repeating"] as! Bool?
                                let friends = entry["friends"] as! [String]?
                                let active = entry["active"] as! Bool?
                                let alarm = (Alarm(hour: hour!, minute: minute!, timeOfDay: timeOfDay!, gameType: gameType!, daysOfWeek: daysOfWeek!, repeating: repeating!, name: alarmName!, ringtone: ringtone!, active: active!, friends: friends!))
                                let key = entry["key"] as! String?
                                print("ALARM?: \(alarm)")
                                self.plebEntries!.append(AlarmKeyed(alarm: alarm, key: key!, status: "pleb"))
                            }
                            self.tableView.reloadData()
                        }
                    })
                    
                    
                }
            }
        })
    }
    
    func toDictionary(vals: Alarm, key: String) -> NSDictionary {
        return [
            "alarmName" : NSString(string: vals.name!),
            "daysOfWeek" : NSArray(array: vals.daysOfWeek!),
            "ringtone" : NSString(string: vals.ringtone!),
            "gameType" : NSString(string: vals.gameType!),
            "hour" : NSNumber(value: vals.hour!),
            "minute" : NSNumber(value: vals.minute!),
            "timeOfDay" : NSString(string: vals.timeOfDay!),
            "repeating" : NSNumber(value: vals.repeating!),
            "active" : NSNumber(value: vals.active!),
            "friends" : NSArray(array: vals.friends!),
            "key" : NSString(string: key),
        ]
    }
    
    func pathDictionary(path: String) -> NSDictionary {
        return ["path" : NSString(string: path)]
    }
    
    //For some reason.. this works without appending..
    func groupAlarmDelegation(alarm: Alarm) {
        print("Got to the delegation.")
        let newChild = self.ref!.child("users").child(self.userEmailNoTag!).child("groupalarms").childByAutoId()
        newChild.setValue(self.toDictionary(vals: alarm, key: (newChild.key)))
        //let validAlarm = AlarmKeyed(alarm: alarm, key: (newChild.key), status: "host")
        for i in 0...alarm.friends!.count-1 {
            let splitFriend = alarm.friends![i].components(separatedBy: ".")
            let shareChild = self.ref!.child("users").child(splitFriend[0]).child("shared").child(newChild.key)
            shareChild.setValue(self.pathDictionary(path: "users|" + self.userEmailNoTag! + "|groupalarms|" + newChild.key))
        }
        print("DELEGATION COUNT: \(self.validEntries?.count)")
        //self.validEntries?.append(validAlarm)
        //self.tableView.reloadData()
    }
    
    func updateActiveStatus() {
        for (i, _) in (self.validEntries?.enumerated())! {
            let index = NSIndexPath(row: i, section: 0)
            let cell = self.tableView.cellForRow(at: index as IndexPath) as! GroupAlarmCell
            if self.validEntries?[i].alarm?.active != cell.status {
                self.validEntries?[i].alarm?.active = cell.status
                if self.validEntries?[i].status == "host" {
                    let newChild = ref?.child("users").child(self.userEmailNoTag!).child("groupalarms").child((self.validEntries?[i].key!)!).child("active")
                    newChild?.setValue(cell.status)
                }
            }
        }
    }
    
    func checkAlarms() {
        print("Checking our alarms.. \(self.validEntries?.count)")
        for (i, _) in (validEntries?.enumerated())! {
            let date: Date = Date()
            let cal: Calendar = Calendar(identifier: .gregorian)
            let weekDay = cal.component(.weekday, from: date)
            var today = false
            let days : [String] = (self.validEntries?[i].alarm?.daysOfWeek)!
            
            if days[0] == "Never"  {
                today = true
            } else {
                for (j, _) in (days.enumerated()) {
                    if (days[j] == "Sun" && weekDay == 1) || (days[j] == "Mon" && weekDay == 2) || (days[j] == "Tues" && weekDay == 3) || (days[j] == "Wed" && weekDay == 4) || (days[j] == "Thur" && weekDay == 5) || (days[j] == "Fri" && weekDay == 6) || (days[j] == "Sat" && weekDay == 7) {
                        today = true
                    }
                }
            }
            
            let index = NSIndexPath(row: i, section: 0)
            let cell = self.tableView.cellForRow(at: index as IndexPath) as! GroupAlarmCell
            
            if cell.status! && today {
                
                var hour : Int
                if self.validEntries?[i].alarm?.timeOfDay == "PM" && self.validEntries?[i].alarm?.hour != 12 {
                    hour = (self.validEntries?[i].alarm?.hour)! + 12
                } else if self.validEntries?[i].alarm?.hour == 12 && self.validEntries?[i].alarm?.timeOfDay == "AM" {
                    hour = 0
                } else {
                    hour = (self.validEntries?[i].alarm?.hour)!
                }
                
                let minute = self.validEntries?[i].alarm?.minute
                let newDate: Date = cal.date(bySettingHour: hour, minute: minute!, second: 0, of: date)!
                let delegate = UIApplication.shared.delegate as? AppDelegate
                delegate?.scheduleNotification(at: newDate, title: (self.validEntries?[i].alarm?.name)!, ringtone: (self.validEntries?[i].alarm?.ringtone)!)
                
            }
        }
        
    }
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        
        if self.validEntries?[indexPath.row].status == "host" {
            let editAction = UITableViewRowAction(style: .normal, title: "Edit") { (rowAction, indexPath) in
                self.edittedAlarmIndex = indexPath.row
                self.performSegue(withIdentifier: "editGroupAlarmSegue", sender: self.validEntries?[indexPath.row].alarm)
            }
            editAction.backgroundColor = .orange
            
            let deleteAction = UITableViewRowAction(style: .normal, title: "Delete") { (rowAction, indexPath) in
                self.ref?.child("users").child(self.userEmailNoTag!).child("groupalarms").child((self.validEntries?[indexPath.row].key)!).removeValue() //Delete's host's.
                for i in 0...(self.validEntries?[indexPath.row].alarm?.friends?.count)!-1 {
                    let tmpArray = self.validEntries?[indexPath.row].alarm?.friends?[i].components(separatedBy: ".")
                    self.ref?.child("users").child((tmpArray?[0])!).child("shared").child((self.validEntries?[indexPath.row].key)!).removeValue() //Delete's plebs'.
                }
                self.validEntries?.remove(at: indexPath.row)
                tableView.setEditing(false, animated: true)
                tableView.deleteRows(at: [indexPath], with: UITableViewRowAnimation.automatic)
                self.tableView.reloadData()
            }
            deleteAction.backgroundColor = .red
            
            return [deleteAction, editAction]
        } else {
            let deleteAction = UITableViewRowAction(style: .normal, title: "Delete") { (rowAction, indexPath) in
                self.ref?.child("users").child(self.userEmailNoTag!).child("shared").child((self.validEntries?[indexPath.row].key)!).removeValue()
                self.validEntries?.remove(at: indexPath.row)
                tableView.setEditing(false, animated: true)
                tableView.deleteRows(at: [indexPath], with: UITableViewRowAnimation.automatic)
                self.tableView.reloadData()
            }
            deleteAction.backgroundColor = .red
            
            return [deleteAction]
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) ->
        CGFloat {
            return 75.0
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let alarms = self.validEntries {
            return alarms.count
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if self.validEntries?[indexPath.row].alarm?.gameType == "Caesar Cypher" {
            gameAlarmIndex = indexPath.row
            self.performSegue(withIdentifier: "cypherSegue", sender: self)
        } else if self.validEntries?[indexPath.row].alarm?.gameType == "Math Challenger" {
            gameAlarmIndex = indexPath.row
            self.performSegue(withIdentifier: "mathSegue", sender: self)
        } else if self.validEntries?[indexPath.row].alarm?.gameType == "Emoji Memory" {
            gameAlarmIndex = indexPath.row
            self.performSegue(withIdentifier: "emojiSegue", sender: self)
        }
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = self.tableView.dequeueReusableCell(withIdentifier: "timeCell", for: indexPath) as! GroupAlarmCell
        if let alarms = self.validEntries?[indexPath.row].alarm {
            cell.alarmTime.text = "\(alarms.hour!):\(String(format: "%02d", alarms.minute!)) \(alarms.timeOfDay!)"
            if alarms.repeating! {
                var allDays: String = ""
                if alarms.daysOfWeek!.count != 7 {
                    for day in alarms.daysOfWeek! {
                        allDays += day
                        allDays += ", "
                    }
                    allDays = allDays.substring(to: allDays.index(before: allDays.endIndex)) //Gets rid of last space.
                    allDays = allDays.substring(to: allDays.index(before: allDays.endIndex)) //Gets rid of last comma.
                    cell.daysList.text = "\(alarms.name!): \(allDays)"
                } else {
                    cell.daysList.text = "\(alarms.name!): Everyday"
                }
            } else {
                cell.daysList.text = "\(alarms.name!)"
            }
            
            cell.alarmSwitch.onTintColor = FOREGROUND_COLOR
            cell.alarmSwitch.tintColor = FOREGROUND_COLOR
            
            if !(alarms.active!) && cell.status! {
                cell.alarmSwitch.setOn(false, animated: true)
            } else if !(cell.status!) && alarms.active! {
                cell.alarmSwitch.setOn(true, animated: true)
            }
        }
        return cell
    }
}
