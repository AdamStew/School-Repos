//
//  AlarmNameViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/6/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

protocol AlarmNameViewControllerDelegate {
    func nameDelegation(name: String)
}

class AlarmNameViewController: AlarmViewController, UITextFieldDelegate {

    @IBOutlet weak var alarmNameField: UITextField!
    var delegate : AlarmNameViewControllerDelegate?
    var alarmName : String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.alarmNameField.delegate = self
        self.alarmNameField.becomeFirstResponder()
        
        if let name = self.alarmName {
            self.alarmNameField.text = name
        }else{
            print("No alarm name, somehow.")
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
        self.delegate?.nameDelegation(name: alarmNameField.text!)
    }
    
    @IBAction func backButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        if textField == self.alarmNameField {
            print("Back alarm name section.")
            self.dismiss(animated: true, completion: nil)
        }
        return true
    }
    
    func dismissKeyboard() {
        self.view.endEditing(true)
    }
    
    

}
