//
//  RingtoneTableViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/6/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import MediaPlayer


protocol RingtoneViewControllerDelegate {
    func ringtoneDelegation(ringtone: String)
}

class RingtoneTableViewController: AlarmTableViewController {

    var ringtones : [String]?
    var delegate : RingtoneViewControllerDelegate?
    var ringtoneStr : String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        ringtones = [String]()
        let myPath = Bundle.main.resourcePath!
        let fileManager = FileManager.default
        
        do {
            let docsArray = try fileManager.contentsOfDirectory(atPath: myPath)
            for i in 0...docsArray.count-1 {
                let m = docsArray[i]
                if m.characters.count > 4 && m.substring(from:m.index(m.endIndex, offsetBy: -4)) == ".wav" {
                    self.ringtones?.append(m.substring(to: m.index(m.endIndex, offsetBy: -4)))
                }
            }
            
        } catch {
            print(error)
        }
        
        if let ringtone = self.ringtoneStr {
            if ringtone != "None" {
                self.tableView.reloadData()
                setUpdatedResults(ringtone: ringtone)
            }
        } else {
            print("Nothing transfered to the repeat section, somehow.")
        }

        
        // This will remove extra separators from tableview
        self.tableView.tableFooterView = UIView(frame: CGRect.zero)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        print(getResults())
        self.delegate?.ringtoneDelegation(ringtone: getResults())
    }

    @IBAction func backButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }

    func setUpdatedResults(ringtone: String) {
        for (i, _) in (ringtones?.enumerated())! {
            let index = NSIndexPath(row: i, section: 0)
            let cell = self.tableView.cellForRow(at: index as IndexPath) as! RingtoneTableViewCell
            if cell.ringtoneLabel?.text == ringtone {
                cell.backgroundColor = BACKGROUND_COLOR
                cell.tintColor = FOREGROUND_COLOR
                cell.accessoryType = .checkmark
            }
        }
    }
    
    func getResults() -> String {
        for (i, element) in (ringtones?.enumerated())! {
            let index = NSIndexPath(row: i, section: 0)
            if (self.tableView.cellForRow(at: index as IndexPath)?.accessoryType.rawValue)! == 3 {
                return element
            }
        }
        return "None"
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let cell = tableView.cellForRow(at: indexPath) {
            if cell.accessoryType.rawValue == 0 {
                cell.backgroundColor = BACKGROUND_COLOR
                cell.tintColor = FOREGROUND_COLOR
                cell.accessoryType = .checkmark
            }
            if let songs = self.ringtones {
                for i in 0...songs.count {
                    if i != indexPath.row {
                        let oldCell = tableView.cellForRow(at: NSIndexPath(row: i, section: 0) as IndexPath)
                        oldCell?.accessoryType = .none
                    }
                }
            }
        }
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ringtoneCell", for: indexPath) as! RingtoneTableViewCell
        
        cell.ringtoneLabel?.text = self.ringtones?[indexPath.row]
        
        return cell
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let songs = self.ringtones {
            return songs.count
        } else {
            return 0
        }
    }

}
