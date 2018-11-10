//
//  RepeatTableViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/6/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

protocol RepeatTableViewControllerDelegate {
    func repeatDelegation(repeats: [String])
}


class RepeatTableViewController: AlarmTableViewController {
    
    @IBOutlet var repeatTable: UITableView!
    var delegate : RepeatTableViewControllerDelegate?
    var repeatDays : [String]?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        if let days = self.repeatDays {
            self.tableView.reloadData()
            setUpdatedResults(days: days)
        } else {
            print("Nothing transfered to the repeat section, somehow.")
        }
        
        // This will remove extra separators from tableview
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        let newResults : [String] = getUpdatedResults()
        self.delegate?.repeatDelegation(repeats: newResults)
    }
    
    @IBAction func backButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func triggerCheckmark(index: Int) {
        let i = NSIndexPath(row: index, section: 0)
        let cell = self.repeatTable.cellForRow(at: i as IndexPath)
        cell?.backgroundColor = BACKGROUND_COLOR
        cell?.tintColor = FOREGROUND_COLOR
        cell?.accessoryType = .checkmark
    }
    
    func setUpdatedResults(days: [String]) {
        
        for (_, element) in days.enumerated() {
            if element == "Sun" {
                triggerCheckmark(index: 0)
            } else if element == "Mon" {
                triggerCheckmark(index: 1)
            } else if element == "Tues" {
                triggerCheckmark(index: 2)
            } else if element == "Wed" {
                triggerCheckmark(index: 3)
            } else if element == "Thur" {
                triggerCheckmark(index: 4)
            } else if element == "Fri" {
                triggerCheckmark(index: 5)
            } else if element == "Sat" {
                triggerCheckmark(index: 6)
            }
        }
        
    }
    
    func getUpdatedResults() -> [String] {
        var newResults : [String] = []
        var index = NSIndexPath(row: 0, section: 0)
        if self.repeatTable.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
            newResults.append("Sun")
        }
        index = NSIndexPath(row: 1, section: 0)
        if self.repeatTable.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
            newResults.append("Mon")
        }
        index = NSIndexPath(row: 2, section: 0)
        if self.repeatTable.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
            newResults.append("Tues")
        }
        index = NSIndexPath(row: 3, section: 0)
        if self.repeatTable.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
            newResults.append("Wed")
        }
        index = NSIndexPath(row: 4, section: 0)
        if self.repeatTable.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
            newResults.append("Thur")
        }
        index = NSIndexPath(row: 5, section: 0)
        if self.repeatTable.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
            newResults.append("Fri")
        }
        index = NSIndexPath(row: 6, section: 0)
        if self.repeatTable.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
            newResults.append("Sat")
        }
        return newResults
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let cell = tableView.cellForRow(at: indexPath) {
            if cell.accessoryType.rawValue == 0 {
                cell.backgroundColor = BACKGROUND_COLOR
                cell.tintColor = FOREGROUND_COLOR
                cell.accessoryType = .checkmark
            } else if cell.accessoryType.rawValue == 3 {
                cell.accessoryType = .none
            }
        }
        tableView.deselectRow(at: indexPath, animated: true)
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 7
    }

}
