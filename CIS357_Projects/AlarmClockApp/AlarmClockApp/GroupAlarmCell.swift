//
//  GroupAlarmCell.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/22/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

class GroupAlarmCell: UITableViewCell {

    @IBOutlet weak var alarmTime: UILabel!
    @IBOutlet weak var daysList: UILabel!
    @IBOutlet weak var alarmSwitch: UISwitch!
    @IBOutlet weak var groupAlarmBackgroundView: UIView!
    
    
    
    var status : Bool?
    
    @IBAction func alarmSwitch(_ sender: Any) {
        print((sender as AnyObject).isOn)
        self.status = (sender as AnyObject).isOn
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.status = true
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
}
