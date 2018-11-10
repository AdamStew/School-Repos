//
//  MathGameViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import Foundation
import Social
import AVFoundation

class MathGameViewController: AlarmViewController, UITextFieldDelegate {
    
    //for one second timer
    var myTimer: Timer? = nil
    
    //outlets
    @IBOutlet weak var timer: UILabel!
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var equationLabel: UILabel!
    @IBOutlet weak var input: UITextField!
    @IBOutlet weak var answerCounter: UILabel!
    @IBOutlet weak var backButton: UIBarButtonItem!
    
    
    var audioPlayer : AVAudioPlayer!
    var gameAlarm : Alarm?
    
    //for timer
    var startTime = TimeInterval()
    var time = Timer()
    
    //points earned from game
    var pointsEarned: String = ""
    
    //holds calculation answer
    var answer: Int = 0
    
    //holds the number of correct answers
    var answersCorrect: Int = 3
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.backButton.isEnabled = false
        self.backButton.title = ""
        
        self.input.delegate = self
        let detectTouch = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard))
        self.view.addGestureRecognizer(detectTouch)
        
        let audioFilePath = Bundle.main.path(forResource: self.gameAlarm?.ringtone!, ofType: "wav")
        
        if audioFilePath != nil {
            
            let audioFileUrl = NSURL.fileURL(withPath: audioFilePath!)
            
            self.audioPlayer = try! AVAudioPlayer(contentsOf: audioFileUrl)
            self.audioPlayer.numberOfLoops = -1
            self.audioPlayer.play()
            
        } else {
            print("Audio file was not found, for some reason.")
        }
    }
    
    //starts the timer
    override func viewDidAppear(_ animated: Bool) {
        let aSelector : Selector = #selector(MathGameViewController.updateTime)
        time = Timer.scheduledTimer(timeInterval: 0.01, target: self, selector: aSelector, userInfo: nil, repeats: true)
        startTime = NSDate.timeIntervalSinceReferenceDate
        
        //load a new word
        newProblemButton(self)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func dismissKeyboard() {
        self.view.endEditing(true)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        if textField == self.input {
            self.submitButton(self)
        }
        return true
    }
    
    //stops the timer
    func stopTimer(){
        time.invalidate()
        //timerTicks = nil
    }
    
    //set up for the timer
    func updateTime(){
        
        let currentTime = NSDate.timeIntervalSinceReferenceDate
        
        //Find the difference between current time and start time.
        var elapsedTime: TimeInterval = currentTime - startTime
        
        //calculate the minutes in elapsed time.
        let minutes = UInt8(elapsedTime / 60.0)
        elapsedTime -= (TimeInterval(minutes) * 60)
        
        //calculate the seconds in elapsed time.
        let seconds = UInt8(elapsedTime)
        elapsedTime -= TimeInterval(seconds)
        
        //find out the fraction of milliseconds to be displayed.
        //let fraction = UInt8(elapsedTime * 100)
        
        //add the leading zero for minutes, seconds and millseconds and store them as string constants
        
        let strMinutes = String(format: "%02d", minutes)
        let strSeconds = String(format: "%02d", seconds)
        //let strFraction = String(format: "%02d", fraction)
        
        //concatenate minuets, seconds and milliseconds as assign it to the UILabel
        timer.text = " \(strMinutes):\(strSeconds)"
        
        //calculate the points earned
        pointsEarned = calculatePointsEarned(minutes: strMinutes, seconds: strSeconds)
        
    }
    
    //calculates the points earned based on the ammount of time
    func calculatePointsEarned(minutes: String, seconds: String ) -> String{
        let mins: Int = Int(minutes)!
        var secs: Int = Int(seconds)!
        var pointsEquivalent = 0
        
        let minutesToSeconds: Int = mins*60
        secs += minutesToSeconds
        
        pointsEquivalent = 300 - secs
        
        if (pointsEquivalent < 0 )
        {
            return "0"
        }else
        {
            return "\(pointsEquivalent)"
        }
        
    }
    
    
    @IBAction func backButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    //generates new a new problem
    @IBAction func newProblemButton(_ sender: Any) {
        switch (answersCorrect){
        case 3:
            let randomX: UInt32 = arc4random_uniform(101)
            let randomY: UInt32 = arc4random_uniform(11)
            
            let x: Int = Int(randomX)
            let y: Int = Int(randomY)
            
            equationLabel.text = "\(x) * \(y)"
            answer = x * y
            input.text = ""
            break
        case 2:
            let randomX: UInt32 = arc4random_uniform(1001)
            let randomY: UInt32 = arc4random_uniform(1001)
            
            let x: Int = Int(randomX)
            let y: Int = Int(randomY)
            
            equationLabel.text = "\(x) + \(y)"
            answer = x + y
            input.text = ""
            break
        case 1:
            let randomX: UInt32 = arc4random_uniform(1001)
            let randomY: UInt32 = arc4random_uniform(1001)
            
            let x: Int = Int(randomX)
            let y: Int = Int(randomY)
            
            answer = x - y
            if answer < 0 {
                equationLabel.text = "\(y) - \(x)"
                answer = y - x
            } else {
                equationLabel.text = "\(x) - \(y)"
            }
            input.text = ""
            break
        default:
            break
            
        }
    }
    
    //sets the status label to correct for two seconds
    func setStatusToCorrect(){
        statusLabel.textColor = GREEN
        statusLabel.text = "Correct!"
        myTimer = Timer.scheduledTimer(timeInterval: 2, target: self, selector:#selector(MathGameViewController.resetStatusLabel), userInfo: nil, repeats: true)
        
    }
    
    //sets the status label incorrect for two seconds
    func setSatusToFalse(){
        statusLabel.textColor = UIColor.red
        statusLabel.text = "Incorrect. Please try Again."
        myTimer = Timer.scheduledTimer(timeInterval: 2, target: self, selector:#selector(MathGameViewController.resetStatusLabel), userInfo: nil, repeats: true)
    }
    
    //resets the status label
    func resetStatusLabel(){
        statusLabel.textColor = FOREGROUND_COLOR
        statusLabel.text = "Calculate the following problem"
        myTimer?.invalidate()
    }
    
    @IBAction func submitButton(_ sender: Any) {
        var inputString: String = ""
        
        inputString = input.text!
        
        if (inputString == "\(answer)"){
            answersCorrect -= 1
            answerCounter.text = "\(answersCorrect)"
            //statusLabel.text = "Correct!"
            
            if answersCorrect == 0 {
                
                input.text = ""
                stopTimer()
                audioPlayer.volume = 0 //Mute noise.
                self.backButton.isEnabled = true
                self.backButton.title = "Back"
                self.dismissKeyboard()
                
                //for alertbox
                // create the alert
                let alert = UIAlertController(title: "Final Time \(timer.text!)", message: "You earned \(pointsEarned) points! \n\nYour alarm clock is now turned off.", preferredStyle: UIAlertControllerStyle.alert)
                
                // add the actions (buttons)
                alert.addAction(UIAlertAction(title: "Return to Menu", style: UIAlertActionStyle.default, handler: { (action: UIAlertAction!) in
                    self.dismiss(animated: true, completion: nil)
                }))
                
                
                alert.addAction(UIAlertAction(title: "Share", style: UIAlertActionStyle.cancel, handler: { (action: UIAlertAction!) in
                    let actionSheet = UIAlertController(title: "", message: "Share your results!", preferredStyle: UIAlertControllerStyle.actionSheet)
                    let tweetAction = UIAlertAction(title: "Share on Twitter", style: UIAlertActionStyle.default) { (action) -> Void in
                        // Check if sharing to Twitter is possible.
                        if SLComposeViewController.isAvailable(forServiceType: SLServiceTypeTwitter) {
                            let twitterComposeVC = SLComposeViewController(forServiceType: SLServiceTypeTwitter)
                            twitterComposeVC?.setInitialText("My \(self.gameAlarm!.name!) notification went off, and I just got a score of \(self.pointsEarned) in \(self.gameAlarm!.gameType!)!")
                            // Display the compose view controller.
                            self.present(twitterComposeVC!, animated: true, completion: nil)
                        }
                        else {
                            self.reportError("You are not logged in to your Twitter account.")
                        }
                    }
                    actionSheet.addAction(tweetAction)
                    self.present(actionSheet, animated: true, completion: nil)
                }))

                
                // show the alert
                self.present(alert, animated: true, completion: nil)
            }else{
                setStatusToCorrect()
                newProblemButton(self)
            }
            
            
        }
        else{
            setSatusToFalse()
        }

        
    }
}
