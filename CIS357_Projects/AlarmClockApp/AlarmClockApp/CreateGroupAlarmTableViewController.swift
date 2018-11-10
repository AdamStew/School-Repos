//
//  CreateGroupAlarmTableViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/15/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

protocol CreateGroupAlarmTableViewControllerDelegate {
    func infoDelegation(name: String, daysOfWeek: [String], ringtone: String, gameType: String, friends: [String])
}

class CreateGroupAlarmTableViewController: UITableViewController, AlarmNameViewControllerDelegate, RepeatTableViewControllerDelegate, GameTypeTableViewControllerDelegate, RingtoneViewControllerDelegate, FriendsViewControllerDelegate {
    @IBOutlet weak var alarmNameLabel: UILabel!
    @IBOutlet weak var repeatLabel: UILabel!
    @IBOutlet weak var ringtoneLabel: UILabel!
    @IBOutlet weak var gameLabel: UILabel!
    @IBOutlet weak var friendsLabel: UILabel!
    var alarmNameStr : String?
    var repeatStr : String?
    var ringtoneStr : String?
    var gameStr : String?
    var friendsStr : String?
    var repeatDays : [String]?
    var delegate : CreateGroupAlarmTableViewControllerDelegate?

    override func viewDidLoad() {
        super.viewDidLoad()

        if self.alarmNameStr == nil {
            self.alarmNameLabel.text = "Alarm"
            self.repeatLabel.text = "Never"
            self.ringtoneLabel.text = "Hail"
            self.gameLabel.text = "Caesar Cypher"
            self.friendsLabel.text = "None"
        } else {
            self.alarmNameLabel.text = alarmNameStr
            self.repeatLabel.text = repeatStr
            self.ringtoneLabel.text = ringtoneStr
            self.gameLabel.text = gameStr
            self.friendsLabel.text = friendsStr
        }
        
        
        // This will remove extra separators from tableview
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        updateData()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "alarmNameSegue" {
            if let dest = segue.destination.childViewControllers[0] as? AlarmNameViewController {
                dest.delegate = self
                dest.alarmName = self.alarmNameLabel.text
            }
        } else if segue.identifier == "repeatSegue" {
            if let dest = segue.destination.childViewControllers[0] as? RepeatTableViewController {
                appendDays()
                dest.delegate = self
                dest.repeatDays = self.repeatDays
            }
        } else if segue.identifier == "ringtoneSegue" {
            if let dest = segue.destination.childViewControllers[0] as? RingtoneTableViewController {
                dest.delegate = self
                dest.ringtoneStr = self.ringtoneLabel.text
            }
        } else if segue.identifier == "gameTypeSegue" {
            if let dest = segue.destination.childViewControllers[0] as? GameTypeTableViewController {
                dest.delegate = self
                dest.gameSelected = self.gameLabel.text
            }
        } else if segue.identifier == "friendsSegue" {
            if let dest = segue.destination.childViewControllers[0] as? FriendsViewController {
                dest.delegate = self
                dest.friends = self.friendsLabel.text
            }
        }
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func updateData() {
        
        if self.repeatLabel!.text == "Everyday" {
            self.repeatDays = ["Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"]
        } else {
            self.repeatDays = (self.repeatLabel!.text?.components(separatedBy: ", "))!
        }
        
        let dest = CreateGroupAlarmViewController()
        dest.alarmName = self.alarmNameLabel.text
        dest.daysOfWeek = self.repeatDays
        dest.ringtone = self.ringtoneLabel.text
        dest.gameType = self.gameLabel.text
        dest.friends = self.friendsLabel.text?.components(separatedBy: ", ")
        
        self.delegate?.infoDelegation(name: self.alarmNameLabel.text!, daysOfWeek: self.repeatDays!, ringtone: self.ringtoneLabel.text!, gameType: self.gameLabel.text!, friends: self.friendsLabel.text!.components(separatedBy: ", "))
    }
    
    func appendDays() {
        if self.repeatLabel!.text == "Everyday" {
            self.repeatDays = ["Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"]
        } else if self.repeatLabel!.text == "Never" {
            self.repeatDays = self.repeatDays?.filter() { $0 == "Never"}
        } else {
            print("Got valid days in the array.")
            self.repeatDays = self.repeatLabel!.text?.components(separatedBy: ", ")
        }
    }
    
    func nameDelegation(name: String) {
        self.alarmNameLabel!.text = name
    }
    
    func repeatDelegation(repeats: [String]) {
        if repeats.count == 7 {
            self.repeatLabel!.text = "Everyday"
        } else if repeats.count > 0 {
            var allDays: String = ""
            for day in repeats {
                allDays += day
                allDays += ", "
            }
            allDays = allDays.substring(to: allDays.index(before: allDays.endIndex)) //Gets rid of last space.
            allDays = allDays.substring(to: allDays.index(before: allDays.endIndex)) //Gets rid of last comma.
            self.repeatLabel!.text = allDays
        } else {
            self.repeatLabel!.text = "Never"
        }
    }
    
    func gameTypeDelegation(game: String) {
        self.gameLabel!.text = game
    }
    
    func ringtoneDelegation(ringtone: String) {
        self.ringtoneLabel!.text = ringtone
    }
    
    func friendsDelegation(friends: String) {
        self.friendsLabel!.text = friends
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 6
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    @IBAction func openCalendarApplication(_ sender: Any) {
        print("Calendar app is open")
        if let url = URL(string: "calshow://"){
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
    

}
