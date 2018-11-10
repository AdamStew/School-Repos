//
//  CreateAlarmViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 3/26/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import FirebaseDatabase

protocol CreateAlarmViewControllerDelegate {
    func alarmDelegation(alarm: Alarm)
}

class CreateAlarmViewController: AlarmViewController, CreateAlarmTableViewControllerDelegate {
    
    
    @IBOutlet weak var timePicker: UIDatePicker!
    var status: String?
    var vcStatus : String?
    var editAlarm : Alarm?
    var newAlarm : Alarm?
    var alarmName : String?
    var daysOfWeek : [String]?
    var ringtone : String?
    var gameType : String?
    var delegate : CreateAlarmViewControllerDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        timePicker.setValue(FOREGROUND_COLOR, forKey: "textColor")
        timePicker.datePickerMode = UIDatePickerMode.time
        status = "Cancel"
        
        if vcStatus == "Edit" {
            let timeFormatter = DateFormatter()
            timeFormatter.dateFormat = "HH:mm"
            let time = timeFormatter.string(from: timePicker.date)
            let sepTime = time.components(separatedBy: ":")
            
            var hour = self.editAlarm?.hour
            var minute = self.editAlarm?.minute
            let timeOfDay = self.editAlarm?.timeOfDay
            if timeOfDay == "PM" {
                hour = hour! + 12
            }
            hour = (24 + (hour! - Int(sepTime[0])!)) * 60 * 60
            minute = (60 + (minute! - Int(sepTime[1])!)) * 60
            let total = hour! + minute! - 3600 //Total time in seconds.
            timePicker.setDate(NSDate(timeIntervalSinceNow: Double(total)) as Date, animated: false)
        }
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "alarmInfoSegue" {
            if let dest = segue.destination as? CreateAlarmTableViewController {
                dest.delegate = self
                if vcStatus == "Edit" {
                    var temp : String
                    if self.editAlarm?.daysOfWeek?.count == 7 {
                        temp = "Everyday"
                    } else if (self.editAlarm?.daysOfWeek?.count)! > 0 {
                        var allDays: String = ""
                        for day in (self.editAlarm?.daysOfWeek)! {
                            allDays += day
                            allDays += ", "
                        }
                        allDays = allDays.substring(to: allDays.index(before: allDays.endIndex)) //Gets rid of last space.
                        allDays = allDays.substring(to: allDays.index(before: allDays.endIndex)) //Gets rid of last comma.
                        temp = allDays
                    } else {
                        temp = "Never"
                    }
                    
                    dest.alarmNameStr = self.editAlarm?.name
                    dest.repeatStr = temp
                    dest.ringtoneStr = self.editAlarm?.ringtone
                    dest.gameStr = self.editAlarm?.gameType
                }
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func saveAlarmButton(_ sender: Any) {
        print("Saved.")
        status = "Save"
        self.dismiss(animated: true, completion: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if status == "Save" && vcStatus != "Edit" {
            let timeFormatter = DateFormatter()
            timeFormatter.dateFormat = "HH:mm"
            let time = timeFormatter.string(from: timePicker.date)
            let sepTime = time.components(separatedBy: ":")
            var timeOfDay = "AM"
            var hour = Int(sepTime[0])!
            let minute = Int(sepTime[1])!
            if hour >= 12 {
                timeOfDay = "PM"
                if hour != 12 {
                    hour -= 12
                }
            } else if hour == 0 {
                hour += 12
            }
            
            var repeating = true
            if daysOfWeek?[0] == "Never" {
                repeating = false
            }
            
            self.newAlarm = Alarm(hour: hour, minute: minute, timeOfDay: timeOfDay, gameType: gameType!, daysOfWeek: daysOfWeek!, repeating: repeating, name: alarmName!, ringtone: ringtone!, active: true)
            self.delegate?.alarmDelegation(alarm: self.newAlarm!)
        } else if status == "Save" && vcStatus == "Edit" {
            let timeFormatter = DateFormatter()
            timeFormatter.dateFormat = "HH:mm"
            let time = timeFormatter.string(from: timePicker.date)
            let sepTime = time.components(separatedBy: ":")
            var timeOfDay = "AM"
            var hour = Int(sepTime[0])!
            let minute = Int(sepTime[1])!
            if Int(sepTime[0])! > 12 {
                timeOfDay = "PM"
                hour -= 12
            }
            var repeating = true
            if daysOfWeek?[0] == "Never" {
                repeating = false
            }
            
            self.editAlarm = Alarm(hour: hour, minute: minute, timeOfDay: timeOfDay, gameType: gameType!, daysOfWeek: daysOfWeek!, repeating: repeating, name: alarmName!, ringtone: ringtone!, active: true)
            self.delegate?.alarmDelegation(alarm: self.editAlarm!)
        }
    }
    
    func infoDelegation(name: String, daysOfWeek: [String], ringtone: String, gameType: String) {
        self.alarmName = name
        self.daysOfWeek = daysOfWeek
        self.ringtone = ringtone
        self.gameType = gameType
    }
}
