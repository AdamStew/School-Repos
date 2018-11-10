//
//  SignUpViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 3/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import FirebaseAuth

class SignUpViewController: AlarmViewController, UITextFieldDelegate {

    @IBOutlet weak var userField: UITextField!
    @IBOutlet weak var passField: UITextField!
    @IBOutlet weak var verifyField: UITextField!
    var validationErrors = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        userField.delegate = self
        passField.delegate = self
        verifyField.delegate = self
        
        let detectTouch = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        self.view.addGestureRecognizer(detectTouch)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "signUpSegue" {
            if let destVC = segue.destination.childViewControllers[1] as? UINavigationController {
                if let destVC = destVC.childViewControllers[0] as? AWFMenuViewController {
                    destVC.userEmail = self.userField.text!
                }
            }
        }
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        if textField == self.userField {
            self.passField.becomeFirstResponder()
        }else if textField == self.passField{
            self.verifyField.becomeFirstResponder()
        }else if textField == self.verifyField && self.validateFields(){
            signUpButton(self)
            performSegue(withIdentifier: "signUpSegue", sender: nil)
        }
        return true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        print("\(identifier)")
        if validateFields() {
            return true
        } else {
            return false
        }
    }
    
    func dismissKeyboard() {
        self.view.endEditing(true)
    }
    
    func validateFields() -> Bool {
        let pwOk = self.isEmptyOrNil(self.passField.text)
        if !pwOk {
            self.validationErrors += "Pasword cannot be blank."
        }
        
        var verifyOk = true
        if self.passField.text != self.verifyField.text {
            verifyOk = false
            self.validationErrors += "Passwords don't match."
        }
        
        
        let emailOk = self.isValidEmail(self.userField.text)
        if !emailOk {
            self.validationErrors += "Invalid email address."
        }
        
        return emailOk && pwOk && verifyOk
    }
    
    @IBAction func signUpButton(_ sender: Any) {
        if self.validateFields() {
            FIRAuth.auth()?.createUser(withEmail: userField.text!, password: passField.text!) { (user, error) in
                if let _ = user {
                    self.performSegue(withIdentifier: "signUpSegue", sender: self)
                } else {
                    self.reportError((error?.localizedDescription)!)
                }
            }
        } else {
            self.passField.text = ""
            self.verifyField.text = ""
            self.passField.becomeFirstResponder()
            self.reportError(self.validationErrors)
        }
    }

    @IBAction func cancelButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }

}
