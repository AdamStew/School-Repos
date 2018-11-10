//
//  GameTypeTableViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/8/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

protocol GameTypeTableViewControllerDelegate {
    func gameTypeDelegation(game: String)
}

class GameTypeTableViewController: AlarmTableViewController {

    @IBOutlet var table: UITableView!
    var delegate : GameTypeTableViewControllerDelegate?
    var gameTypes : [String]?
    var gameSelected : String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.gameTypes = ["Caesar Cypher", "Math Challenger", "Emoji Memory"]
        
        if let game = self.gameSelected {
            self.tableView.reloadData()
            setUpdatedResults(gameSelected: game, gameTypes: gameTypes!)
        } else {
            print("No game selected, somehow.")
        }
        
        // This will remove extra separators from tableview
        self.table.tableFooterView = UIView(frame: CGRect.zero)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        let newResult : String = getUpdatedResults()
        self.delegate?.gameTypeDelegation(game: newResult)
    }
    
    @IBAction func backButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    func setUpdatedResults(gameSelected : String, gameTypes : [String]) {
        for (i, _) in (self.gameTypes?.enumerated())! {
            let index = NSIndexPath(row: i, section: 0)
            let cell = self.tableView.cellForRow(at: index as IndexPath) as! GameTableViewCell
            if cell.gameLabel.text == gameSelected {
                cell.backgroundColor = BACKGROUND_COLOR
                cell.tintColor = FOREGROUND_COLOR
                cell.accessoryType = .checkmark
            }
        }
    }
    
    func getUpdatedResults() -> String {
        for (i, element) in (self.gameTypes?.enumerated())! {
            let index = NSIndexPath(row: i, section: 0)
            if self.table.cellForRow(at: index as IndexPath)?.accessoryType.rawValue == 3 {
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
            if let games = self.gameTypes {
                for i in 0...games.count {
                    if i != indexPath.row {
                        let oldCell = tableView.cellForRow(at: NSIndexPath(row: i, section: 0) as IndexPath)
                        oldCell?.accessoryType = .none
                    }
                }
            }
        }
        tableView.deselectRow(at: indexPath, animated: true)
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let games = self.gameTypes {
            return games.count
        } else {
            return 0
        }
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "gameCell", for: indexPath) as! GameTableViewCell
        
        cell.gameLabel?.text = self.gameTypes?[indexPath.row]

        return cell
    }
 

}
