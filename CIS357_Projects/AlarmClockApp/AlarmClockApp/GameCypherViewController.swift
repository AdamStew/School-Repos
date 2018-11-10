//
//  GameCypherViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/4/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import Social
import AVFoundation


class GameCypherViewController: AlarmViewController, UITextFieldDelegate {
    
    @IBOutlet weak var timer: UILabel!
    @IBOutlet weak var encodedWord: UILabel!
    @IBOutlet weak var input: UITextField!
    @IBOutlet weak var hintLabel1: AlarmLabel!
    @IBOutlet weak var hintLabel2: AlarmLabel!
    @IBOutlet weak var hintLabel3: AlarmLabel!
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var backButton: UIBarButtonItem!
    var audioPlayer : AVAudioPlayer!
    var gameAlarm : Alarm?
    
    
    //for timer
    var startTime = TimeInterval()
    var time = Timer()
    
    //controlls when hint button is pressed
    var hintIsShowing: Bool = false
    
    //points earned from game
    var pointsEarned: String = ""
    
    //encoded words
    var listOfDecodedWords: [String] = ["odnhuv", "olrqv", "frpsxwhuv", "kdfnhu", "preloh", "dssoh","wljhuv", "idfherrn" ]
    
    
    //holds all the letters of the alphabet
    var alphabetIntString: [Int:String] = [0:"A", 1:"B", 2:"C", 3:"D",
                                           4:"E", 5:"F", 6:"G", 7:"H",
                                           8:"I", 9:"J", 10:"K", 11:"L",
                                           12:"M", 13:"N", 14:"O", 15:"P",
                                           16:"Q", 17:"R", 18:"S", 19:"T",
                                           20:"U", 21:"V", 22:"W", 23:"X",
                                           24:"Y", 25:"Z"]
    
    //holds all the letters of the alphabet
    var alphabet: [String:Int] = ["A":0, "B":1, "C":2, "D":3,
                                  "E":4, "F":5, "G":6, "H":7,
                                  "I":8, "J":9, "K":10, "L":11,
                                  "M":12, "N":13, "O":14, "P":15,
                                  "Q":16, "R":17, "S":18, "T":19,
                                  "U":20, "V":21, "W":22, "X":23,
                                  "Y":24, "Z":25]
    
    //holds value of current decoded word
    var decodedWord: String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.hintLabel1.textColor = BACKGROUND_COLOR
        self.hintLabel2.textColor = BACKGROUND_COLOR
        self.hintLabel3.textColor = BACKGROUND_COLOR
        
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
        let aSelector : Selector = #selector(GameCypherViewController.updateTime)
        time = Timer.scheduledTimer(timeInterval: 0.01, target: self, selector: aSelector, userInfo: nil, repeats: true)
        startTime = NSDate.timeIntervalSinceReferenceDate
        
        //load a new word
        newWordButton(self)
    }
    
    @IBAction func backButon(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
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
    
    //loads a new decoded word from array
    @IBAction func newWordButton(_ sender: Any) {
        let randomNumber: UInt32 = arc4random_uniform(6)
        let randomNum: Int = Int(randomNumber)
        
        encodedWord.text = "\(listOfDecodedWords[randomNum].uppercased())"
        input.text = ""
        statusLabel.text = "Decypher the Following Word"
    }
    
    //compares input word with the decoded word
    @IBAction func submitButton(_ sender: Any) {
        var inputString: String = ""
        
        inputString = input.text!
        
        decodedWord = decodeTheWord(encodedString: encodedWord.text!)
        print("The decoded word is \(decodedWord)")
        
        if (inputString.uppercased() == decodedWord.uppercased()){
            statusLabel.text = "Correct!"
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
            
        }
        else{
            statusLabel.text = "Incorrect. Please try Again."
        }
    }
    
    //shows a hint for the user
    @IBAction func hintButton(_ sender: Any) {
        
        if(hintIsShowing == false){
            self.hintLabel1.textColor = FOREGROUND_COLOR
            self.hintLabel2.textColor = FOREGROUND_COLOR
            self.hintLabel3.textColor = FOREGROUND_COLOR
            hintIsShowing = true
        }else {
            self.hintLabel1.textColor = BACKGROUND_COLOR
            self.hintLabel2.textColor = BACKGROUND_COLOR
            self.hintLabel3.textColor = BACKGROUND_COLOR
            hintIsShowing = false
        }
    }
    
    //decodes the currently encoded word
    func decodeTheWord(encodedString: String) -> String{
        var decodedString: String = ""
        let encodedWord = Array(encodedString.characters)
        
        print("decodeTheWord method \(encodedWord)")
        
        for i in encodedWord{
            
            
            var indexOfEncodedLetter: Int = alphabet["\(i)"]!
            var decodedLetter: String = ""
            let encodedLetter: String = "\(i)"
            
            if encodedLetter == "C"
            {
                indexOfEncodedLetter = 28
                
            }else if encodedLetter == "B"
            {
                indexOfEncodedLetter = 27
                
            }else if encodedLetter == "A"
            {
                indexOfEncodedLetter = 26
            }
            
            decodedLetter = alphabetIntString[indexOfEncodedLetter - 3]!
            decodedString += decodedLetter
            
        }
        return decodedString
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
        
        //concatenate minutes, seconds and milliseconds as assign it to the UILabel
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
    
    
}
