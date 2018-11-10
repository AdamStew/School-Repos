//
//  AlarmModel.swift
//  AlarmClockApp
//
//  Created by X Code User on 3/25/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import Foundation
class AlarmModel {
    fileprivate var items : [Alarm] = [Alarm]()
    init(){
        createAlarms()
    }
    
    func getAlarms() -> [Alarm] {
        return self.items
    }
    
    fileprivate func createAlarms(){
        items.append(Alarm(hour: 8, minute: 45, timeOfDay: "AM", gameType: "Cypher Game", daysOfWeek: ["Mon", "Wed", "Fri"], repeating: true, name: "Classes Start", ringtone: "Default", active: true))
        items.append(Alarm(hour: 7, minute: 30, timeOfDay: "AM", gameType: "Cypher Game", daysOfWeek: ["Tues", "Thur"], repeating: true, name: "Classes Start", ringtone: "Default", active: true))
        items.append(Alarm(hour: 12, minute: 00, timeOfDay: "PM", gameType: "Cypher Game", daysOfWeek: ["Sat"], repeating: true, name: "none", ringtone: "Default", active: true))
    }
}
