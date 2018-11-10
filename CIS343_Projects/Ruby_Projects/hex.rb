=begin
Name: Adam Stewart
Assignment: Ruby Programming Project One
Due Date: October 26, 2016

Referenced code for String and Range methods found
at ruby-doc.org: https://ruby-doc.org/core-2.2.0/String.html
and https://ruby-doc.org/core-1.9.3/Range.html .
Sole purpose was to see the library of methods, and what they do.

As far as integer-overflow is concerned.  It's not something I
needed to worry about, since Ruby automatically converts 
integers into larger integers, preventing it from overflowing.
=end

count = 0, total = 0, i = 0
broke = false
temp = ""

#Get's the inputed value.
print "Give a hexadecimal number: "
temp = gets

#Puts all of the chars to uppercase and deletes spaces.
#Doesn't need the temp string anymore, now that it's formatted correctly.
str = temp.upcase.delete(' ')
size = str.length - 1
#This keeps track of what power to raise each section of the conversion.
exponent = size - 1

while i < size
	#Checks to see if valid hexadecimal was entered.
	#For some reason, === wasn't working out, so I changed it to use the method .include?.
	if ('A'..'F').include?(str[i]) || ('0'..'9').include?(str[i])
		if str[i]=='A'
			count = 10 * 16**(exponent)
		elsif str[i]=='B'
			count = 11 * 16**(exponent)
		elsif str[i]=='C'
			count = 12 * 16**(exponent)
		elsif str[i]=='D'
			count = 13 * 16**(exponent)
		elsif str[i]=='E'
			count = 14 * 16**(exponent)
		elsif str[i]=='F'
			count = 15 * 16**(exponent)
		else
			count = str[i].to_i * 16**(exponent) #For some reason += isn't working here.
		end										 #So instead, it's totaled after the if-stmt.
	else
		broke = true #Keeps track if an error was found.
		messup = str[i] #Keeps track of the char that screwed it up.
		break
	end
	total += count #This is my total variable, to keep the total value, isntead of in the if-stmt.
	i+=1 #Increments loop, i++ doesn't work, so I assume it's not allowed in Ruby.
	exponent-=1 #Decrements what exponent value we're looking at.
end

#Results.
if broke
	puts "Hex: #{str}**Error - #{messup} is not a legal hexadecimal digit."
else
	puts "Hex: #{str}Decimal: #{total}"
end