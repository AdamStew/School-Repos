//
//  alarm.swift
//  AlarmClockApp
//
//  Created by X Code User on 3/25/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import Foundation
struct Alarm {
    var hour: Int?
    var minute: Int?
    var timeOfDay: String?
    var gameType: String?
    var daysOfWeek: [String]?
    var repeating: Bool?
    var name: String?
    var ringtone: String?
    var active: Bool?
    var friends: [String]?
    
    init(hour: Int, minute: Int, timeOfDay: String, gameType: String, daysOfWeek: [String], repeating: Bool, name: String, ringtone: String, active: Bool){
        self.hour = hour
        self.minute = minute
        self.timeOfDay = timeOfDay
        self.gameType = gameType
        self.daysOfWeek = daysOfWeek
        self.repeating = repeating
        self.name = name
        self.ringtone = ringtone
        self.active = active
    }
    
    init(hour: Int, minute: Int, timeOfDay: String, gameType: String, daysOfWeek: [String], repeating: Bool, name: String, ringtone: String, active: Bool, friends: [String]){
        self.hour = hour
        self.minute = minute
        self.timeOfDay = timeOfDay
        self.gameType = gameType
        self.daysOfWeek = daysOfWeek
        self.repeating = repeating
        self.name = name
        self.ringtone = ringtone
        self.active = active
        self.friends = friends
    }
}
