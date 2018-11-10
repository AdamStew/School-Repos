//
//  FriendsViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/21/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

protocol FriendsViewControllerDelegate {
    func friendsDelegation(friends: String)
}

class FriendsViewController: AlarmViewController, UITextFieldDelegate {

    @IBOutlet weak var friendsText: UITextField!
    var delegate : FriendsViewControllerDelegate?
    var friends : String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.friendsText.delegate = self
        self.friendsText.becomeFirstResponder()
        
        if let name = self.friends {
            if name != "None" {
                self.friendsText.text = name
            }
        }else{
            print("No alarm friends, somehow.")
        }
        
        let detectTouch = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        self.view.addGestureRecognizer(detectTouch)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        self.dismissKeyboard()
        let friendsList = self.friendsText.text!.trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
        if friendsList.characters.count != 0 {
            self.delegate?.friendsDelegation(friends: friendsList)
        } else {
            self.delegate?.friendsDelegation(friends: "None")
        }
    }
    
    @IBAction func backButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        if textField == self.friendsText {
            print("Back friends section.")
            self.dismiss(animated: true, completion: nil)
        }
        return true
    }
    
    func dismissKeyboard() {
        self.view.endEditing(true)
    }
    
}
