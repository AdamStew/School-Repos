//
//  UIViewController+Validation.swift
//  AlarmClockApp
//
//  Created by X Code User on 3/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit

extension AlarmViewController{
    
    func isEmptyOrNil(_ str: String?) -> Bool
    {
        guard let s = str, !s.isEmpty else {
            return false
        }
        return true
    }
    
    func isValidEmail(_ emailStr: String?) -> Bool
    {
        var emailOk = false
        if let email = emailStr {
            let regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
            let emailPredicate = NSPredicate(format: "SELF MATCHES %@", regex)
            emailOk = emailPredicate.evaluate(with: email)
        }
        return emailOk
    }

    func reportError(_ msg: String) {
        let alert = UIAlertController(title: "Failed", message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}
