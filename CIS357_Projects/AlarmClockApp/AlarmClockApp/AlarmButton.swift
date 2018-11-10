//
//  AlarmButton.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

class AlarmButton: UIButton {

    override func awakeFromNib() {
        self.backgroundColor = FOREGROUND_COLOR
        self.tintColor = BACKGROUND_COLOR
        self.layer.borderWidth = 1.0
        self.layer.borderColor = FOREGROUND_COLOR.cgColor
        self.layer.cornerRadius = 5.0
        
    }

}
