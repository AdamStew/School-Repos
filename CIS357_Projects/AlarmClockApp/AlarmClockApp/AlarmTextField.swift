//
//  AlarmTextField.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

class AlarmTextField: UITextField {

    override func awakeFromNib() {
        self.tintColor = UIColor.white
        self.layer.borderWidth = 1.0
        self.layer.borderColor = FOREGROUND_COLOR.cgColor
        self.layer.cornerRadius = 5.0
        
        self.textColor = FOREGROUND_COLOR
        self.backgroundColor = UIColor.clear
        self.borderStyle = .roundedRect
        
        guard let ph = self.placeholder else {
            return
        }
        
        self.attributedPlaceholder = NSAttributedString(string: ph, attributes: [NSForegroundColorAttributeName : FOREGROUND_COLOR])
    }

}
