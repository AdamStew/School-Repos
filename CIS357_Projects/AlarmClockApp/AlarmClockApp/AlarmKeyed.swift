//
//  AlarmKeyed.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/15/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import Foundation
struct AlarmKeyed {
    var alarm: Alarm?
    var key: String?
    var status: String?
    
    init(alarm: Alarm, key: String) {
        self.alarm = alarm
        self.key = key
    }
    
    init(alarm: Alarm, key: String, status: String) {
        self.alarm = alarm
        self.key = key
        self.status = status
    }
}
