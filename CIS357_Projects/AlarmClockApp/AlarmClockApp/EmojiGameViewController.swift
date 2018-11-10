//
//  EmojiGameViewController.swift
//  AlarmClockApp
//
//  Created by X Code User on 4/23/17.
//  Copyright © 2017 Kristian Trevino. All rights reserved.
//

import UIKit
import Foundation
import Social
import AVFoundation

class EmojiGameViewController: AlarmViewController, UITextFieldDelegate {
    
    //outlets
    @IBOutlet weak var timer: UILabel!
    @IBOutlet weak var emojiLabel: UILabel!
    @IBOutlet weak var input: UITextField!
    @IBOutlet weak var statusLabel: UILabel!
    @IBOutlet weak var answerCounter: UILabel!
    @IBOutlet weak var backButton: UIBarButtonItem!
    var allowTyping : Bool!
    var audioPlayer : AVAudioPlayer!
    var gameAlarm : Alarm?
    
    //for one second timer
    var myTimer: Timer? = nil
    
    //for emoji timer
    var emojiTimer: Timer? = nil
    
    //used as a buffer
    var bufferTime: Timer? = nil
    
    //for timer
    var startTime = TimeInterval()
    var time = Timer()
    
    //points earned from game
    var pointsEarned: String = ""
    
    
    //holds the number in a row that are correct
    var answersCorrect: Int = 3
    
    //encoded words
    var emojis: [String] = ["😇", "😎", "😍", "😜", "😂", "🤓", "😘"]
    
    //keeps track if the game has started
    var gameHasStarted: Bool = false
    
    var correctPattern: String = ""
    
    /*    //random numbers for game
     var num1: Int = 0
     var num2: Int = 0
     var num3: Int = 0
     var num4: Int = 0
     */
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.emojiLabel.textColor = BACKGROUND_COLOR
        self.backButton.isEnabled = false
        self.backButton.title = ""
        self.allowTyping = false
        
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
    
    override func viewDidAppear(_ animated: Bool) {
        
        
        //intialized gameplay
        //playGame()
        
        //tells the user to get ready
        setStatusToInitializer()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //function that dismises keyboard
    func dismissKeyboard(){
        self.view.endEditing(true)
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool{
        if textField == self.input {
            self.submitButton(self)
        }
        return true
    }
    
    func startTimer(){
        let aSelector : Selector = #selector(EmojiGameViewController.updateTime)
        time = Timer.scheduledTimer(timeInterval: 0.01, target: self, selector: aSelector, userInfo: nil, repeats: true)
        startTime = NSDate.timeIntervalSinceReferenceDate
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
    
    //generates a new game
    @IBAction func newGameButton(_ sender: Any) {
        self.allowTyping = false
        answersCorrect = 3
        input.text = ""
        setStatusToInitializer()
        answerCounter.text = "\(answersCorrect)"
    }
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if self.allowTyping! {
            return true
        } else {
            return false
        }
    }
    
    //plays the game
    func playGame(){
        input.text = ""
        let num1: Int = randNum()
        let num2: Int = randNum()
        let num3: Int = randNum()
        //num3 = randNum()
    
        setEmojiLabel(index: num1)
        DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(2), execute: {
            self.setEmojiLabel(index: num2)
        })
        DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(4), execute: {
            self.setEmojiLabel(index: num3)
        })
        DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(5), execute: {
            self.allowTyping = true
            self.statusLabel.text = "What was the pattern?"
        })
        correctPattern = emojis[num1] + emojis[num2] + emojis[num3]
        print("correctPatter =" + correctPattern)
        
        /** SIMON GAME FUNCTIONALITY (has to start at 0 isntead) **/
        /*    switch (answersCorrect){
         case 5:
         input.text = ""
         break
         case 4:
         input.text = "Congratulations! The alarm is now turned off"
         break
         case 3:
         input.text = ""
         //let num1: Int = randNum()
         //let num2: Int = randNum()
         //let num3: Int = randNum()
         //let num4: Int = randNum()
         num4 = randNum()
         setEmojiLabel(index: num1)
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(2), execute: {
         self.setEmojiLabel(index: self.num2)
         })
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(4), execute: {
         self.setEmojiLabel(index: self.num3)
         })
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(6), execute: {
         self.setEmojiLabel(index: self.num4)
         })
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(7), execute: {
         self.statusLabel.text = "What was the pattern?"
         })
         correctPattern = emojis[num1] + emojis[num2] + emojis[num3] + emojis[num4]
         print("CASE3: correctPatter =" + correctPattern)
         break
         case 2:
         input.text = ""
         //let num1: Int = randNum()
         //let num2: Int = randNum()
         //let num3: Int = randNum()
         num3 = randNum()
         setEmojiLabel(index: num1)
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(2), execute: {
         self.setEmojiLabel(index: self.num2)
         })
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(4), execute: {
         self.setEmojiLabel(index: self.num3)
         })
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(5), execute: {
         self.statusLabel.text = "What was the pattern?"
         })
         correctPattern = emojis[num1] + emojis[num2] + emojis[num3]
         print("CASE2: correctPatter =" + correctPattern)
         break
         case 1:
         input.text = ""
         //let num1: Int = randNum()
         //let num2: Int = randNum()
         num2 = randNum()
         setEmojiLabel(index: num1)
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(2), execute: {
         self.setEmojiLabel(index: self.num2)
         
         })
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(4), execute: {
         self.statusLabel.text = "What was the pattern?"
         })
         correctPattern = emojis[num1] + emojis[num2]
         print("CASE1: correctPatter =" + correctPattern)
         break
         default:
         //let num1: Int = randNum()
         num1 = randNum()
         setEmojiLabel(index: num1)
         correctPattern = emojis[num1]
         print("DEFAULT: correctPatter =" + correctPattern)
         DispatchQueue.main.asyncAfter(deadline: .now() + .seconds(1), execute: {
         self.statusLabel.text = "What was the pattern?"
         })
         
         break
         
         }
         */
    }
    
    func buffer(){
        bufferTime = Timer.scheduledTimer(timeInterval: 3, target: self, selector:#selector(EmojiGameViewController.endBuffer), userInfo: nil, repeats: true)
    }
    
    func endBuffer(){
        bufferTime?.invalidate()
    }
    
    //generates a random number from 0 to 6
    func randNum()-> Int{
        let randomNum: UInt32 = arc4random_uniform(7)
        
        let num: Int = Int(randomNum)
        return num
    }
    
    func setStatusToInitializer(){
        statusLabel.textColor = FOREGROUND_COLOR
        statusLabel.text = "Get Ready!!!"
        myTimer = Timer.scheduledTimer(timeInterval: 2, target: self, selector:#selector(EmojiGameViewController.resetStatusLabel), userInfo: nil, repeats: false)
    }
    //sets the status label to correct for two seconds
    func setStatusToCorrect(){
        self.input.resignFirstResponder()
        self.allowTyping = false
        statusLabel.textColor = GREEN
        statusLabel.text = "Correct!"
        myTimer = Timer.scheduledTimer(timeInterval: 2, target: self, selector:#selector(EmojiGameViewController.setStatusToInitializer), userInfo: nil, repeats: false)
        
    }
    
    //sets the status label incorrect for two seconds
    func setSatusToFalse(){
        self.allowTyping = false
        statusLabel.textColor = UIColor.red
        statusLabel.text = "Incorrect. Game will restart"
        answersCorrect = 3
        input.text = ""
        answerCounter.text = "\(answersCorrect)"
        myTimer = Timer.scheduledTimer(timeInterval: 2, target: self, selector:#selector(EmojiGameViewController.setStatusToInitializer), userInfo: nil, repeats: false)
    }
    
    /*   func setStatusToGetReady(){
     statusLabel.textColor = UIColor.blue
     statusLabel.text = "Get Ready!"
     myTimer = Timer.scheduledTimer(timeInterval: 3, target: self, selector:#selector(ViewController.resetStatusLabel), userInfo: nil, repeats: true)
     }*/
    
    //resets the status label
    func resetStatusLabel(){
        statusLabel.text = ""
        myTimer?.invalidate()
        if gameHasStarted == false{
            startTimer()
            gameHasStarted = true
        }
        
        playGame()
        //plays the game
        //setEmojiLabel(index: 0)
    }
    
    
    //--------------------------------------------------
    //for emojis
    
    //sets emoji label
    func setEmojiLabel(index: Int){
        emojiLabel.text = "\(emojis[index])"
        statusLabel.textColor = FOREGROUND_COLOR
        statusLabel.text = "Remember the pattern"
        emojiTimer = Timer.scheduledTimer(timeInterval: 1, target: self, selector:#selector(EmojiGameViewController.resetEmojiLabel), userInfo: nil, repeats: false)
    }
    
    //resets the emoji label
    func resetEmojiLabel(){
        emojiLabel.textColor = BACKGROUND_COLOR
        emojiLabel.text = "Label"
        
        emojiTimer?.invalidate()
    }
    
    
    //--------------------------------------------------
    
    @IBAction func submitButton(_ sender: Any) {
        var inputString: String = ""
        
        inputString = input.text!
        print("User input:" + inputString)
        if (inputString == correctPattern){
            answersCorrect -= 1
            answerCounter.text = "\(answersCorrect)"
            input.text = ""
            
            if answersCorrect == 0 {
                
                answerCounter.text = "0"
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
                //playGame()
            }
            
            
        }
        else{
            setSatusToFalse()
        }
        
    }
    
}
