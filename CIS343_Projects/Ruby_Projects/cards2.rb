=begin
Name: Adam Stewart
Assignment: Ruby Programming Project Three
Due Date: November 18, 2016
=end


=begin
Card class, has a number, which is a number value, and 
a suit which is a string value.
=end
class Card
	def initialize(num, suit)
		@num = num
		@suit = suit
	end
	
	def setNum(num)
		@num = num
	end
	
	def setSuit(suit)
		@suit = suit
	end
	
	def getNum()
		return @num
	end
	
	def getSuit()
		return @suit
	end
	
	def equalNum(cardNum)
		#Compares two numbers from two different cards to see if equal.
		if @num.eql? cardNum
			return true
		else
			return false
		end
	end
	
	def equalSuit(cardSuit)
		#Compares two strings from two different cards to see if equal.
		if @suit.eql? cardSuit
			return true
		else
			return false
		end
	end
	
	def printCard()
		#Returns a string to be easily printed.  Formats so that if
		#the number value is 1, it reads 'Ace', 11 reads 'Jack' 12
		#read 'Queen', and 13 reads 'King'.
		if @num == 1
			return print = "an Ace of #{@suit}. \n"
		elsif @num == 11
			return print = "a Jack of #{@suit}. \n"
		elsif @num == 12
			return print = "a Queen of #{@suit}. \n"
		elsif @num == 13
			return print = "a King of #{@suit}. \n"
		else
			return print = "a #{@num} of #{@suit}. \n"
		end
	end
end

=begin
Deck class that creates a queue of all the possible 52 cards
inside a real deck.
=end
class Deck
	def initialize()
		cardNum = 1 #This will keep track of the possible number per suit.
		index = 0 #Index for the array.
		@deck = Queue.new #This is our "deck"
		cards = Array.new(52) #Creating all 52 cards.
		while cardNum < 14
			#Making all of the spade cards.
			temp = Card.new(cardNum, "Spades")
			cards[index] = temp
			index += 1
			#Making all of the club cards.
			temp = Card.new(cardNum, "Clubs")
			cards[index] = temp
			index += 1
			#Making all of the diamond cards.
			temp = Card.new(cardNum, "Diamonds")
			cards[index] = temp
			index += 1
			#Making al of the heart cards.
			temp = Card.new(cardNum, "Hearts")
			cards[index] = temp
			index += 1
			cardNum += 1
		end
		
		while !cards.empty?()
			#Putting our cards randomly into our deck.
			shuffle = Random.rand(cards.length)
			@deck.push(cards.slice!(shuffle))
		end
	end
	
	def getDeck()
		return @deck
	end
	
	def draw()
		#This will make it so we draw a random card in our deck.
		if(!@deck.empty?())
			return @deck.pop() #Card drawn.
		end
	end
	
	def empty?()
		#Checks to see if the deck is empty.
		if @deck.empty?()
			puts "The deck is empty."
			return true
		else
			return false
		end
	end
	
	def printDeck()
		index = 0
		result = "DECK: "
		while index < @deck.length
			temp = @deck.pop()
			@deck.push(temp)
			result = result + temp.printCard().chomp() + "-> " 
			index += 1
		end
		return result;
	end
end

=begin
Hand class that is a queue of cards drawn from the 
deck class.
=end
class Hand
	def initialize()
		@hand = Queue.new #This is our "hand".
	end
	
	def getHand()
		return @hand
	end
	
	def getLast(hand)
		i = 0 #Index.
		temp = Card.new(0, "") #Placeholder.
		while i < hand.length #Finds the rightmost/last card in hand.
			temp = hand.shift()
			hand.push(temp) #Puts every card back in your hand.
			i += 1
		end
		return temp #Card we want.
	end
	
	def getForthLast(hand)
		i = 3 #Index to get to the forth from last card.
		j = 0 #Index to return the hand back to the correct order.
		temp = Card.new(0, "")
		#This loop finds the card fourth from the right/last.
		while i < hand.length
			temp = hand.shift()
			hand.push(temp) #Putting all of the cards back in the deck.
			final = Card.new(temp.getNum(), temp.getSuit()) #This is the card we want.
			i += 1
		end
		#This loop restores your hand to the correct order.
		while j < 3
			temp = hand.shift()
			hand.push(temp) #Putting the cards back in the deck.
			j += 1
		end
		return final #Card we want.
	end
	
	def checkSize()
		#Returns true if your hand has at least 4 cards.
		if @hand.length >= 4
			return true
		else
			return false
		end
	end
	
	def checkMatchNum()
		#Uses methods to check if the last and forth from last cards have equal numbers.
		if getLast(@hand).equalNum(getForthLast(@hand).getNum())
			return true
		else
			return false
		end
	end
	
	def checkMatchSuit()
		#Uses methods to check if the last and forth from last cards have equal suits.
		if getLast(@hand).equalSuit(getForthLast(@hand).getSuit())	
			return true
		else
			return false
		end
	end
	
	
	def discardCards?()
		#Checks if we have at least 4 cards in our hand.
		if checkSize()
			i = 0 #Index to get to the last 4 cards of your hand.
			j = 0 #Index to discard all 4 cards.
			temp = Card.new(0, "")
			#Checking to see if the 1st and 4th cards match numbers.
			if checkMatchNum()
				puts "Your two cards have matching numbers!"
				#Iterating to the 4 cards we want to discard.
				while i < @hand.length - 4
					temp = @hand.shift()
					@hand.push(temp) #Putting cards we don't want back in our deck.
					i += 1
				end
				#Discards 4 cards.
				while j < 4
					puts "Discarding #{@hand.pop().printCard()}" #Discarding cards.
					j += 1 
				end
				discardCards?() #Now that you discarded cards, have to check again if there's new stuff to discard.
			end
		end
		if checkSize()
			i = 0 #Index to get to the last 4 cards of your hand.
			j = 0 #Index to skip two 'middle' cards of the last four.
			#Checking to see if the 1st and 4th card match suits.
			if checkMatchSuit()
				puts "Your two cards have matching suits!"
				#Iterating to the middle two cards
				while i < @hand.length - 3
					temp = @hand.shift()
					@hand.push(temp)
					i += 1
				end
				puts "Discarding #{@hand.pop().printCard()}" #Discards the middle two cards.
				puts "Discarding #{@hand.pop().printCard()}" 
				@hand.push(@hand.pop())
				discardCards?() #Now that you discarded cards, have to check again if there's new stuff to discard.
			end
		end
	end

	def discardHiddenCards?()
		#Checks if we have at least 4 cards in our hand.
		if checkSize()
			i = 0 #Index to get to the last 4 cards of your hand.
			j = 0 #Index to discard all 4 cards.
			temp = Card.new(0, "")
			#Checking to see if the 1st and 4th cards match numbers.
			if checkMatchNum()
				#Iterating to the 4 cards we want to discard.
				while i < @hand.length - 4
					temp = @hand.shift()
					@hand.push(temp) #Putting cards we don't want back in our deck.
					i += 1
				end
				#Discards 4 cards.
				while j < 4
					@hand.pop().printCard() #Discarding cards.
					j += 1 
				end
				discardCards?() #Now that you discarded cards, have to check again if there's new stuff to discard.
			end
		end
		if checkSize()
			i = 0 #Index to get to the last 4 cards of your hand.
			j = 0 #Index to skip two 'middle' cards of the last four.
			#Checking to see if the 1st and 4th card match suits.
			if checkMatchSuit()
				#Iterating to the middle two cards
				while i < @hand.length - 3
					temp = @hand.shift()
					@hand.push(temp)
					i += 1
				end
				@hand.pop().printCard() #Discards the middle two cards.
				@hand.pop().printCard()
				@hand.push(@hand.pop())
				discardCards?() #Now that you discarded cards, have to check again if there's new stuff to discard.
			end
		end
	end
		
	def getScore()
		i = 0 #Index for all of the cards in someone's hand.
		temp = 0 #Placeholder for cards.
		score = 0 #Keeps track of score.
		while i < @hand.length
			temp = @hand.pop().getNum()
			if temp > 10 #This checks to see if it's a Jack, Queen, or King.
				score += 10 #Adds 10, since it's face class.
			else
				score += temp #Otherwise, just add its' value.
			end
		end
		puts "Congratulations, you have a score of #{score}."
		return score
	end
		
	def printPrimaryHand()
		i = 0
		j = 0
		k = 0
		result = "HAND: "
		#This will be printed if we have less than 4 cards in our hand.
		if @hand.length < 4
			while k < @hand.length
				temp = @hand.shift()
				result = result + temp.printCard().chomp() #Collecting the cards.
				@hand.push(temp)
				k += 1
			end
			
			return result
		end
		#Iterating to the 4 cards we want to display.
		while i < @hand.length - 4
			temp = @hand.shift()
			@hand.push(temp) #Putting cards we don't want back in our deck.
			i += 1
		end
		#Found the 4 cards we want..
		while j < 4
			temp = @hand.shift()
			result = result + temp.printCard().chomp() #Collecting the cards.
			@hand.push(temp)
			j += 1 
		end
		return result
	end
		
	def printEntireHand()
		i = 0
		result = "HAND: "
		#Iterating through our hand.
		while i < @hand.length
			temp = @hand.shift()
			result = result + temp.printCard().chomp() #Collecting ALL cards.
			@hand.push(temp)
			i += 1
		end
		return result
	end
end


class Game
	def initialize()
		puts "Welcome to 'Car-Solitaire'.  Please input 'Q' to quit the application" +
		", 'L' to display the leaderboard, 'P' to play a user-watched game, and 'H'" + 
		"to play a hidden game."
		
		@result = gets.chomp().upcase()
	end
	
	def setResult()
		@result = gets
	end
	
	def getResult()
		return @result
	end
	
	def userWatchGame()
		puts "Welcome to a user-watched game.  Please input 'C' to start/continue " +
		"the game, 'D' to display the current deck, and 'X' to exit the game."
		
		deck = Deck.new()
		hand = Hand.new()
		
		broke = false
		
		#Kicks out if the game is over.
		while !(deck.empty?())
			setResult() #Checks to see what we want to do.
			if @result.chomp().upcase() == "C" || @result.chomp() == ""
				if !hand.checkSize() #Checking if we have at least 4 cards.
					puts "Less than 4 cards found in your hand.  Drawing until reached 4 cards."
					while (hand.checkSize() == false) && (deck.empty?() == false)
						#Draws.
						card = deck.draw()
						puts "You drew #{card.printCard()}"
						hand.getHand().push(card)
					end
					puts "#{hand.printPrimaryHand()}"
					hand.discardCards?()
				else
					#Draws.
					card = deck.draw()
					puts "You drew #{card.printCard()}"
					hand.getHand().push(card)
					hand.discardCards?()
					puts "#{hand.printPrimaryHand()}"
				end
			elsif @result.chomp().upcase() == "D" #If we picked 'D' display the remaining deck.
				puts "#{deck.printDeck()}"
			elsif @result.chomp().upcase() == "X" #If we picked 'X', quit out of the game.
				puts "Quitting the game."
				broke = true
				break
			else #If nothing matched above, then we didn't input a valid character.
				puts "Invalid user-watched game command.  Please input 'C' to " + 
				"start/continue the game, 'D' to display the current deck, and " +
				"'X' to exit the game."
			end
		end
		if !broke
			puts "#{hand.printEntireHand()}"
			return hand.getScore()
		end
		return broke
	end
	
	def userHiddenGame()
		puts "Welcome to a user-hidden game.  Please enjoy the ride."
		
		deck = Deck.new()
		hand = Hand.new()
		
		while !(deck.empty?())
			if !hand.checkSize()
				#Makes sure you have at least 4 cards.
				while(hand.checkSize() == false) && (deck.empty?() == false)
					#Draws.
					card = deck.draw()
					hand.getHand().push(card)
				end
				hand.discardHiddenCards?()
			end
			#Draws card if you have more than 4 cards, but no more matches.
			if hand.getHand().length >= 4 && (deck.empty?() == false)
				#Draws.
				card = deck.draw()
				hand.getHand().push(card)
				hand.discardHiddenCards?()
			end
		end
		return hand.getScore()
	end
	
	
	def getRankLength(file)
		
	end
end


=begin
Both of these references found in the code below.

This was used to reference a current date format:
http://stackoverflow.com/questions/7415982/how-do-i-get-the-current-date-time-in-dd-mm-yyyy-hhmm-format

This was used to print multiple lines/strings to a file:
http://alvinalexander.com/blog/post/ruby/how-write-text-to-file-ruby-example
=end

game = Game.new()

#The brains of the program

#Program will run unless you type 'Q' to quit.
while game.getResult.chomp().upcase != "Q"

	#If the user types 'L' while not in a game, will display leaderboard, or create
	#a leaderboard if one does not exist.
	if game.getResult.chomp().upcase == "L"
	
		#Checking if it exists.
		if File.exist?("Leaders.txt")
			#If it does exist, display on contents of the file.
			File.open("Leaders.txt", "r").each do |line|
				puts line
			end
		else
			#If it does not exist, tell the user, and generate a blank file.
			puts "Leaderboard file not found.  Generating Leaders.txt."
			leaders = File.new("Leaders.txt", "w+")
		end
		
	#If the user types 'P' while not in a game, it will start a user-watch game.
	elsif game.getResult.chomp().upcase == "P"
	
		#This runs the actual game.  See Game, Hand, Deck, and Card classes.
		newScore = game.userWatchGame #Stores our score here.
		
		#If we ragequit, then all the leaderboard junk will get skipped.
		if newScore != true
			#After the game is over, let's see where we lie ont he leaderboards, again
			#if the file doesn't exist, it'll generate one.
			if File.exist?("Leaders.txt")
			
				#File does exist.
				
				i = 0; #This is an index that will keep track of how many lines are in the file.
				
				broke = false #Just a regular boolean that will tell me if I've broken out of a loop.
				
				fewRanks = false #This boolean will keep track of we currently have a ranking with 4 or less. 
				
				rank = 0 #Keeps track of what rank our score is among the leaderboard
						#Rank 1 being best, rank 5 being worst (that can be on the board)
						
				name = "" #String that will keep track of the user's name, if they get high score.
				
				#This opens our file.
				File.open("Leaders.txt", "r+").each do |line|
					#This skips to a certin area on a line and gathers the score on that line of the leader.
					oldScore = line[21..24].chomp().to_i #Making it an int.
					i += 1
					rank += 1 #Increments what rank we are among the top 5.
					#If we have a score low enough for top 5, it'll tell the user.
					if newScore < oldScore
						puts "Congrats on a high score!  Please enter your name (less than 20 total" + 
						" characters long)."
						
						#Here we change the score back into a string, so that we can add
						#leading zeroes, that way our columns line up.
						newScore = newScore.to_s
						newScore = "%03d" % newScore
						
						#User inputed name.
						name = gets.chomp
						
						#Unfornunantly have a name length cap, so that columns line up and look nice.
						if name.length > 20
							#If the name surpasses 20 characters, it tells the user and cuts it off.
							puts "Name too long!  Chopped it."
							name = name[0..19] + " "
						else	
							#Otherwise, it'll try and add spaces if too short, again for columns line up.
							while name.length <= 20
								name = name + " "
							end	
						end
						#Here, we're breaking, since we found a high school rank.  We don't want to 
						#increment our rank any higher, so we notify our program that we've broken out.
						broke = true
						break
					end	
				end
			
				#So.. if we didn't break, that means we didn't break in on the rankings.
				if !broke
					fewRanks = true
				end
			
				#However, we want to see if we didn't make it in the rankings because our rankings
				#have less than 4 high scores.
				if i < 5 && fewRanks
					#It'll only enter this loop if we have less than 5 high scores AND the score we
					#just got is a worse score than all the previous ones.
					
					rank = 5 #Since our score is worse than the others, we can just put it at default #5
					
					puts "Congrats on a high score!  Please enter your name (less than 20 total" + 
					" characters long."
					
					#Here we change the score back into a string, so that we can add
					#leading zeroes, that way our columns line up.
					newScore = newScore.to_s
					newScore = "%03d" % newScore
					
					#User inputed name.
					name = gets.chomp
					
					#Unfornunantly have a name length cap, so that columns line up and look nice.
					if name.length > 20
						#If the name surpasses 20 characters, it tells the user and cuts it off.
						puts "Name too long!  Chopped it."
						name = name[0..19] + " "
					else	
						#Otherwise, it'll try and add spaces if too short, again for columns line up.
						while name.length <= 20
							name = name + " "
						end	
					end
					#Here, we're breaking, since we found a high school rank.  We don't want to 
					#increment our rank any higher, so we notify our program that we've broken out.
					broke = true
					break
				end				
		
				#Well this is kinda a mess, but it was the only way off the top of my head to
				#do this, and it works, will definitely fix if a better idea comes to mind.
				
				#So since we found a high score, we want to come right to here.
				if broke
					i = 0 #This index will keep track of what line in the old leaderboard we're reading.
					
					#Since we have a high score, that means the previous top 4 of the 5 scores will 
					#carry over.  I decided to keep this lines into strings, denoted short for
					#"rank A", "rank B", "rank C", and "rank D", below.
					ra = ""
					rb = ""
					rc = ""
					rd = ""
					
					#Here I'm gather the top 4 scores, and putting them in their respective variables.
					File.open("Leaders.txt", "r+").each do |line|
						if i == 0
							ra = line
						elsif i == 1
							rb = line
						elsif i == 2
							rc = line
						elsif i == 3
							rd = line
						end
						
						i += 1
					end
					
					#If it wasn't sloppy before, it sure is now.  The only way I could think of getting
					#the lines printed in the right order was having 5 different 'if stmts' depending on 
					#what rank you got.  I'm sure there is a much better way..
					
					#So if you're rank 1, print our just received score on the very top.
					if rank == 1
						open("Leaders.txt", "w") { |f|
							f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
							f << "#{ra}"
							f << "#{rb}"
							f << "#{rc}"
							f << "#{rd}"
						}	
					
					#If you're rank 2, print it after the original best score.
					elsif rank == 2
						open("Leaders.txt", "w") { |f|
							f << "#{ra}"
							f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
							f << "#{rb}"
							f << "#{rc}"
							f << "#{rd}"
						}	
						
					#And so on..
					elsif rank == 3
						open("Leaders.txt", "w") { |f|
							f << "#{ra}"
							f << "#{rb}"
							f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
							f << "#{rc}"
							f << "#{rd}"
						}
					
					#And so on..
					elsif rank == 4
						open("Leaders.txt", "w") { |f|
							f << "#{ra}"
							f << "#{rb}"
							f << "#{rc}"
							f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
							f << "#{rd}"
						}	
					
					#And so forth..
					else
						open("Leaders.txt", "w") { |f|
							f << "#{ra}"
							f << "#{rb}"
							f << "#{rc}"
							f << "#{rd}"
							f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
						}	
					end
				end
			
			#NOW.. it'll come here if the file doesn't exist.  Notifies the user that it's creating
			#a new leaderboard file.
			else
				puts "Leaderboard file not found.  Generating Leaders.txt."
				leaders = File.new("Leaders.txt", "w+") #New file.
				
				puts "Congrats on a high score!  Please enter your name (less than 20 total" + 
				"characters long."
				
				#Should be the exact same format as above, just now with a blank document.
				newScore = newScore.to_s
				newScore = "%03d" % newScore
				name = gets.chomp
				if name.length > 20
					puts "Name too long!  Chopped it."
					name = name[0..19]
				else #Adding filler.
					while name.length <= 20
						name = name + " "
					end
				end
				
				open("Leaders.txt", "w") { |f|
					f << "#{name}#{newScore}  #{Time.now.strftime("%d/%m/%Y %H:%M")} \n"
				}		
			end
		end
	#If the user types 'H' while not in a game, it will start a user-hidden game.
	elsif game.getResult.chomp().upcase == "H"
		#This runs the actual game.  See Game, Hand, Deck, and Card classes.
		newScore = game.userHiddenGame() #Stores our score here.
		
		
		if File.exist?("Leaders.txt")
		
			#File does exist.
			
			i = 0; #This is an index that will keep track of how many lines are in the file.
			
			broke = false #Just a regular boolean that will tell me if I've broken out of a loop.
			
			fewRanks = false #This boolean will keep track of we currently have a ranking with 4 or less. 
			
			rank = 0 #Keeps track of what rank our score is among the leaderboard
					 #Rank 1 being best, rank 5 being worst (that can be on the board)
					 
			name = "" #String that will keep track of the user's name, if they get high score.
			
			#This opens our file.
			File.open("Leaders.txt", "r+").each do |line|
				#This skips to a certin area on a line and gathers the score on that line of the leader.
				oldScore = line[21..24].chomp().to_i #Making it an int.
				i += 1
				rank += 1 #Increments what rank we are among the top 5.
				#If we have a score low enough for top 5, it'll tell the user.
				if newScore < oldScore
					puts "Congrats on a high score!  Please enter your name (less than 20 total" + 
					" characters long)."
					
					#Here we change the score back into a string, so that we can add
					#leading zeroes, that way our columns line up.
					newScore = newScore.to_s
					newScore = "%03d" % newScore
					
					#User inputed name.
					name = gets.chomp
					
					#Unfornunantly have a name length cap, so that columns line up and look nice.
					if name.length > 20
						#If the name surpasses 20 characters, it tells the user and cuts it off.
						puts "Name too long!  Chopped it."
						name = name[0..19] + " "
					else
						#Otherwise, it'll try and add spaces if too short, again for columns line up.
						while name.length <= 20
							name = name + " "
						end
					end
					#Here, we're breaking, since we found a high school rank.  We don't want to 
					#increment our rank any higher, so we notify our program that we've broken out.
					broke = true
					break
				end
			end
			
			#So.. if we didn't break, that means we didn't break in on the rankings.
			if !broke
				fewRanks = true
			end
			
			#However, we want to see if we didn't make it in the rankings because our rankings
			#have less than 4 high scores.
			if i < 5 && fewRanks
				#It'll only enter this loop if we have less than 5 high scores AND the score we
				#just got is a worse score than all the previous ones.
				
				rank = 5 #Since our score is worse than the others, we can just put it at default #5
				
				puts "Congrats on a high score!  Please enter your name (less than 20 total" + 
				" characters long."
				
				#Here we change the score back into a string, so that we can add
				#leading zeroes, that way our columns line up.
				newScore = newScore.to_s
				newScore = "%03d" % newScore
				
				#User inputed name.
				name = gets.chomp
				
				#Unfornunantly have a name length cap, so that columns line up and look nice.
				if name.length > 20
					#If the name surpasses 20 characters, it tells the user and cuts it off.
					puts "Name too long!  Chopped it."
					name = name[0..19] + " "
				else
					#Otherwise, it'll try and add spaces if too short, again for columns line up.
					while name.length <= 20
						name = name + " "
					end
				end
				#Here, we're breaking, since we found a high school rank.  We don't want to 
				#increment our rank any higher, so we notify our program that we've broken out.
				broke = true
				break
			end			
		
			#Well this is kinda a mess, but it was the only way off the top of my head to
			#do this, and it works, will definitely fix if a better idea comes to mind.
			
			#So since we found a high score, we want to come right to here.
			if broke
				i = 0 #This index will keep track of what line in the old leaderboard we're reading.
				
				#Since we have a high score, that means the previous top 4 of the 5 scores will 
				#carry over.  I decided to keep this lines into strings, denoted short for
				#"rank A", "rank B", "rank C", and "rank D", below.
				ra = ""
				rb = ""
				rc = ""
				rd = ""
				
				#Here I'm gather the top 4 scores, and putting them in their respective variables.
				File.open("Leaders.txt", "r+").each do |line|
					if i == 0
						ra = line
					elsif i == 1
						rb = line
					elsif i == 2
						rc = line
					elsif i == 3
						rd = line
					end
					
					i += 1
				end
				
				#If it wasn't sloppy before, it sure is now.  The only way I could think of getting
				#the lines printed in the right order was having 5 different 'if stmts' depending on 
				#what rank you got.  I'm sure there is a much better way..
				
				#So if you're rank 1, print our just received score on the very top.
				if rank == 1
					open("Leaders.txt", "w") { |f|
						f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
						f << "#{ra}"
						f << "#{rb}"
						f << "#{rc}"
						f << "#{rd}"
					}
				
				#If you're rank 2, print it after the original best score.
				elsif rank == 2
					open("Leaders.txt", "w") { |f|
						f << "#{ra}"
						f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
						f << "#{rb}"
						f << "#{rc}"
						f << "#{rd}"
					}	
					
				#And so on..
				elsif rank == 3
					open("Leaders.txt", "w") { |f|
						f << "#{ra}"
						f << "#{rb}"
						f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
						f << "#{rc}"
						f << "#{rd}"
					}
					
				#And so on..
				elsif rank == 4
					open("Leaders.txt", "w") { |f|
						f << "#{ra}"
						f << "#{rb}"
						f << "#{rc}"
						f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
						f << "#{rd}"
					}
					
				#And so forth..
				else
					open("Leaders.txt", "w") { |f|
						f << "#{ra}"
						f << "#{rb}"
						f << "#{rc}"
						f << "#{rd}"
						f << "#{name}#{newScore}  #{Time.now.strftime("%m/%d/%Y %H:%M")} \n"
					}
				end
			end
			
		#NOW.. it'll come here if the file doesn't exist.  Notifies the user that it's creating
		#a new leaderboard file.
		else
			puts "Leaderboard file not found.  Generating Leaders.txt."
			leaders = File.new("Leaders.txt", "w+") #New file.
			
			puts "Congrats on a high score!  Please enter your name (less than 20 total" + 
			"characters long."
			
			#Should be the exact same format as above, just now with a blank document.
			newScore = newScore.to_s
			newScore = "%03d" % newScore
			name = gets.chomp
			if name.length > 20
				puts "Name too long!  Chopped it."
				name = name[0..19]
			else #Adding filler.
				while name.length <= 20
					name = name + " "
				end
			end
			
			open("Leaders.txt", "w") { |f|
				f << "#{name}#{newScore}  #{Time.now.strftime("%d/%m/%Y %H:%M")} \n"
			}
		end
	end
	
	#Once that giant if-stmt is over, a new input can be given (being quit, 
	#leaderboard, user-watch game, and user-hidden game).
	game.setResult()
end	