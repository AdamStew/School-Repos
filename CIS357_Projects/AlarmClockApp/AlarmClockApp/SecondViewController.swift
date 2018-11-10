//
//  SecondViewController.swift
//  AlarmClockApp
//
//  Created by Kristian Trevino on 3/21/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import FirebaseAuth

class SecondViewController: AlarmViewController, UITextFieldDelegate {

    @IBOutlet weak var userField: UITextField!
    @IBOutlet weak var passField: UITextField!
    var validationErrors = ""
   
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        userField.delegate = self
        passField.delegate = self
        
        let detectTouch = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        self.view.addGestureRecognizer(detectTouch)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "signInSegue" {
            if let dest = segue.destination.childViewControllers[1] as? UINavigationController {
                if let dest = dest.childViewControllers[0] as? AWFMenuViewController {
                    dest.userEmail = self.userField.text
                }
            }
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        if textField == self.userField {
            self.passField.becomeFirstResponder()
        }else if textField == self.passField && self.validateFields() {
            signInButton(self)
        }
        return true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if validateFields() || identifier == "signUpSegueFromMain" {
            return true
        } else {
            return false
        }
    }
    
    func dismissKeyboard() {
        self.view.endEditing(true)
    }

    func validateFields() -> Bool {
        self.validationErrors = ""
        let pwOk = self.isEmptyOrNil(self.passField.text)
        if !pwOk {
            self.validationErrors += "Pasword cannot be blank."
        }
        
        let emailOk = self.isValidEmail(self.userField.text)
        if !emailOk {
            self.validationErrors += "Invalid email address."
        }
        
        return emailOk && pwOk
    }
    
    @IBAction func signOutButton(_ segue: UIStoryboardSegue) {
        do {
            try FIRAuth.auth()?.signOut()
            print("Logged out.")
        }catch let signOutError as NSError {
            print("Error signing out: %@", signOutError)
        }
        
        self.passField.text = ""
    }
    
    @IBAction func signUpButton(_ sender: Any) {
        print("Signing up.")
    }
    
    @IBAction func signInButton(_ sender: Any) {
        if self.validateFields() {
            print("Congratulations!  You entered correct values.")
            FIRAuth.auth()?.signIn(withEmail: self.userField.text!, password: self.passField.text!) { (user, error) in
                if let _ = user {
                    self.performSegue(withIdentifier: "signInSegue", sender: nil)
                } else {
                    print("DIDN'T EXIST.")
                    self.reportError((error?.localizedDescription)!)
                    self.passField.text = ""
                    self.passField.becomeFirstResponder()
                }
            }
        } else {
            self.reportError(self.validationErrors)
        }
    }
}
