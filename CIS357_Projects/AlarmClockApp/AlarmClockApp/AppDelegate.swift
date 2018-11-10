//
//  AppDelegate.swift
//  AlarmClockApp
//
//  Created by Kristian Trevino on 3/21/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import UserNotifications
import AVFoundation
import Firebase

let BACKGROUND_COLOR = UIColor.init(colorLiteralRed:0.05, green:0.31, blue:0.48, alpha:1.0)
let FOREGROUND_COLOR = UIColor.init(colorLiteralRed:1.00, green:0.81, blue:0.56, alpha:1.0)
let GREEN = UIColor.init(colorLiteralRed:0.00, green:0.71, blue:0.15, alpha:1.0)

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    var soundPlayer: AVAudioPlayer?
    
    func scheduleNotification(at date: Date, title: String, ringtone: String) {
        let calendar = Calendar(identifier: .gregorian)
        let components = calendar.dateComponents(in: .current, from: date)
        let newComponents = DateComponents(calendar: calendar, timeZone: .current, month: components.month, day: components.day, hour: components.hour, minute: components.minute)
        
        let trigger = UNCalendarNotificationTrigger(dateMatching: newComponents, repeats: false)
        let content = UNMutableNotificationContent()
        content.title = title
        content.body = "Click on the alarm to play game!"
        content.sound = UNNotificationSound.init(named: ringtone + ".wav")
        
        
        
        let request = UNNotificationRequest(identifier: "alarmNotification", content: content, trigger: trigger)
        UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
        UNUserNotificationCenter.current().add(request) {(error) in
            if let error = error {
                print("Uh oh! We had an error: \(error)")
            }
        }
    }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
        application.statusBarStyle = UIStatusBarStyle.lightContent
        let navAppearance = UINavigationBar.appearance()
        navAppearance.barTintColor = BACKGROUND_COLOR
        navAppearance.tintColor = FOREGROUND_COLOR
        navAppearance.isTranslucent = false
        navAppearance.titleTextAttributes = [NSForegroundColorAttributeName : FOREGROUND_COLOR]
        
        // Use Firebase library to configure APIs
        FIRApp.configure()
        
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound]) {(accepted, error) in
            if !accepted {
                print("Notification access denied.")
            }
        }
        
        return true
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


}

