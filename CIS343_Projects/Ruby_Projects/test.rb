=begin
Name: Adam Stewart
Assignment: Ruby Programming Project Two
Due Date: October 31, 2016
=end


#Create two variables with similar properties.
x = 2
y = 3
puts "x = #{x}"
puts "y = #{y}"

#Checking to make sure the classes are the same.
puts "The class of x is a: #{x.class}"
puts "The class of y is a: #{y.class}"

z = x + y
#Checking to see if they can be added.
if z == 5
	puts "Ruby has loose equivalence!  x + y = #{z}"
else
	puts "Ruby has strict equivalence!  x + y = #{z}"
end