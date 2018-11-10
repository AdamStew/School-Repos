//
//  FirstViewController.swift
//  AlarmClockApp
//
//  Created by Kristian Trevino on 3/21/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import FirebaseDatabase


//TODO: AFTER AN ALARM THAT NEVER REPEATS GOES OFF, TURN KNOB TO FALSE.
//TODO: ALSO MAKE SURE WHEN KNOB IS FALSE, THE ALARM DOESN'T TRIGGER.
//TODO: MAKE IT SO ALARMS GO OFF FOR TOMORROW, WITHOUT CRAZY STUFF.
class FirstViewController: AlarmViewController, UITableViewDataSource, UITableViewDelegate, CreateAlarmViewControllerDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    fileprivate var ref : FIRDatabaseReference?
    var validEntries : [AlarmKeyed]?
    var edittedAlarmIndex : Int?
    var gameAlarmIndex : Int?
    var cancelledAlarm : Alarm?
    var uniqueVendorStr : String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.uniqueVendorStr = UIDevice.current.identifierForVendor!.uuidString
        self.validEntries = [AlarmKeyed]()
        self.ref = FIRDatabase.database().reference()
        self.registerForFireBaseUpdates()
        
        // This will remove extra separators from tableview
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        updateActiveStatus()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        checkAlarms()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    fileprivate func registerForFireBaseUpdates() {
        self.ref!.child("alarm").child(self.uniqueVendorStr!).observe(.value, with: { snapshot in
            if let postDict = snapshot.value as? [String : AnyObject] {
                var tmpItems = [AlarmKeyed]()
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
                    let active = entry["active"] as! Bool?
                    let alarm = (Alarm(hour: hour!, minute: minute!, timeOfDay: timeOfDay!, gameType: gameType!, daysOfWeek: daysOfWeek!, repeating: repeating!, name: alarmName!, ringtone: ringtone!, active: active!))
                    let key = entry["key"] as! String?
                    tmpItems.append(AlarmKeyed(alarm: alarm, key: key!))
                }
                self.validEntries = tmpItems
                self.tableView.reloadData()
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
            "key" : NSString(string: key),
        ]
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "addSegue" {
            if let dest = segue.destination.childViewControllers[0] as? CreateAlarmViewController {
                print("Adding a new alarm.")
                dest.delegate = self
            }
        } else if segue.identifier == "editSegue" {
            if let dest = segue.destination.childViewControllers[0] as? CreateAlarmViewController {
                print("Editting an old alarm.")
                dest.delegate = self
                dest.vcStatus = "Edit"
                dest.editAlarm = self.validEntries?[self.edittedAlarmIndex!].alarm
                self.cancelledAlarm = self.validEntries?[self.edittedAlarmIndex!].alarm
                self.ref?.child("alarm").child(self.uniqueVendorStr!).child((self.validEntries?[edittedAlarmIndex!].key)!).removeValue()
                self.validEntries?.remove(at: self.edittedAlarmIndex!)
            }
        } else if segue.identifier == "cypherSegue" {
            if let dest = segue.destination.childViewControllers[0] as? GameCypherViewController {
                if !(self.validEntries?[self.gameAlarmIndex!].alarm?.repeating!)! {
                    let index = NSIndexPath(row: self.gameAlarmIndex!, section: 0)
                    let cell = self.tableView.cellForRow(at: index as IndexPath) as! AlarmCell
                    cell.alarmSwitch.setOn(false, animated: true)
                    self.validEntries?[self.gameAlarmIndex!].alarm?.active = false
                }
                dest.gameAlarm = self.validEntries?[self.gameAlarmIndex!].alarm
            }
        } else if segue.identifier == "mathSegue" {
            if let dest = segue.destination.childViewControllers[0] as? MathGameViewController {
                if !(self.validEntries?[self.gameAlarmIndex!].alarm?.repeating!)! {
                    let index = NSIndexPath(row: self.gameAlarmIndex!, section: 0)
                    let cell = self.tableView.cellForRow(at: index as IndexPath) as! AlarmCell
                    cell.alarmSwitch.setOn(false, animated: true)
                    self.validEntries?[self.gameAlarmIndex!].alarm?.active = false
                }
                dest.gameAlarm = self.validEntries?[self.gameAlarmIndex!].alarm
            }
        } else if segue.identifier == "emojiSegue" {
            if let dest = segue.destination.childViewControllers[0] as? EmojiGameViewController {
                if !(self.validEntries?[self.gameAlarmIndex!].alarm?.repeating!)! {
                    let index = NSIndexPath(row: self.gameAlarmIndex!, section: 0)
                    let cell = self.tableView.cellForRow(at: index as IndexPath) as! AlarmCell
                    cell.alarmSwitch.setOn(false, animated: true)
                    self.validEntries?[self.gameAlarmIndex!].alarm?.active = false
                }
                dest.gameAlarm = self.validEntries?[self.gameAlarmIndex!].alarm
            }
        }
    }
    
    @IBAction func cancelFromCreateAlarmButton(_ segue: UIStoryboardSegue) {
        print("Cancelled.")
        if cancelledAlarm != nil {
            let newChild = ref?.child("alarm").child(self.uniqueVendorStr!).childByAutoId()
            newChild?.setValue(self.toDictionary(vals: cancelledAlarm!, key: (newChild?.key)!))
            self.validEntries?.append(AlarmKeyed(alarm: cancelledAlarm!, key: (newChild?.key)!))
            cancelledAlarm = nil
        }
        self.tableView.reloadData()
    }
    
    func updateActiveStatus() {
        for (i, _) in (self.validEntries?.enumerated())! {
            let index = NSIndexPath(row: i, section: 0)
            let cell = self.tableView.cellForRow(at: index as IndexPath) as! AlarmCell
            if self.validEntries?[i].alarm?.active != cell.status {
                self.validEntries?[i].alarm?.active = cell.status
                let newChild = ref?.child("alarm").child(self.uniqueVendorStr!).child((self.validEntries?[i].key!)!).child("active")
                newChild?.setValue(cell.status)
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
            let cell = self.tableView.cellForRow(at: index as IndexPath) as! AlarmCell
            
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
    
    func alarmDelegation(alarm: Alarm) {
        print("Got to the delegation.")
        let newChild = ref?.child("alarm").child(self.uniqueVendorStr!).childByAutoId()
        newChild?.setValue(self.toDictionary(vals: alarm, key: (newChild?.key)!))
        let validAlarm = AlarmKeyed(alarm: alarm, key: (newChild?.key)!)
        self.validEntries?.append(validAlarm)
        self.tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        
        let editAction = UITableViewRowAction(style: .normal, title: "Edit") { (rowAction, indexPath) in
            self.edittedAlarmIndex = indexPath.row
            self.performSegue(withIdentifier: "editSegue", sender: self.validEntries?[indexPath.row].alarm)
        }
        editAction.backgroundColor = .orange
        
        let deleteAction = UITableViewRowAction(style: .normal, title: "Delete") { (rowAction, indexPath) in
            print("THE KEY: \(self.validEntries?[indexPath.row].key))")
            self.ref?.child("alarm").child(self.uniqueVendorStr!).child((self.validEntries?[indexPath.row].key)!).removeValue()
            self.validEntries?.remove(at: indexPath.row)
            tableView.setEditing(false, animated: true)
            tableView.deleteRows(at: [indexPath], with: UITableViewRowAnimation.automatic)
            self.tableView.reloadData()
        }
        deleteAction.backgroundColor = .red
        
        return [deleteAction, editAction]
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
        let cell = self.tableView.dequeueReusableCell(withIdentifier: "timeCell", for: indexPath) as! AlarmCell
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
            print("ROW: \(indexPath.row) ALARM STATUS: \(alarms.active!) CELL STATUS: \(cell.status!)")
            if !(alarms.active!) && cell.status! {
                cell.alarmSwitch.setOn(false, animated: true)
            } else if !(cell.status!) && alarms.active! {
                cell.alarmSwitch.setOn(true, animated: true)
            }
        }
        return cell
    }
}

