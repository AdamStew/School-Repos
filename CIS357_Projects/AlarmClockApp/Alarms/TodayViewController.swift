//
//  TodayViewController.swift
//  Alarms
//
//  Created by X Code User on 4/20/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import NotificationCenter

class TodayViewController: UIViewController, NCWidgetProviding {
        
    @IBOutlet weak var alarmLabel: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        //print("SOMETHING")
        //let defaults = UserDefaults(suiteName: "group.trevikri.AlarmClockApp.Alarms")
        //let entries : String = defaults?.object(forKey: "key") as! String
        //print(entries)
        //self.alarmLabel.text! = "Something new."
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func widgetPerformUpdate(completionHandler: (@escaping (NCUpdateResult) -> Void)) {
        completionHandler(NCUpdateResult.newData)
    }
    
    //func setExtensionLabel(alarms: [AlarmKeyed]){
    //    self.alarmLabel.text! = (alarms[0].alarm?.gameType)!
    //    self.alarmLabel.text! = "PIE"
    //}
    
}
