//
//  SwipeableCell.swift
//  AlarmClockApp
//
//  Created by X Code User on 3/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit



class AlarmCell: UITableViewCell {
    
    @IBOutlet weak var alarmTime: UILabel!
    @IBOutlet weak var daysList: UILabel!
    @IBOutlet weak var alarmBackgroundView: UIView!
    @IBOutlet weak var alarmSwitch: UISwitch!
    
    var status : Bool?
    
    @IBAction func alarmSwitch(_ sender: Any) {
        self.status = (sender as AnyObject).isOn
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        //self.backgroundColor = BACKGROUND_COLOR
        //self.contentView.backgroundColor = BACKGROUND_COLOR
        self.status = true
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}
