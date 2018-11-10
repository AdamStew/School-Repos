//
//  GameTableViewCell.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/25/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

class GameTableViewCell: UITableViewCell {

    @IBOutlet weak var gameBackgroundView: AlarmView!
    @IBOutlet weak var gameLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
