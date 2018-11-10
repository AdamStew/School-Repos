#  exampleCIT-3.s
#  data movement instruction

	.data
val1:	.word 42		# data variable and value
val2:	.word 43
	.text
	.globl main
main:	


####################################
# Test file for CIS 451 Project 2a #
# Adam Stewart                     #
# Josh Getter                      #
####################################	

addi $8, $8, 15
add $9, $9, $8
li $10, 16

jal function #Testing jal.

bne $8, $10, test #Works.

anotherTest:    addi $11, $11, 3

filler:    addi $14, $14, -7 #Won't reach.

test:    addi $15, $15, 72 #Reaches.

bne $8, $9, end #Won't work.

addi $16, $16, 42 #Reaches

end:    addi $17, $17, 24

li $v0, 10		# syscall to exit
syscall   		# execute syscall	

function:    addi $12, $12, 1600
addu $13, $13, $31 # Testing $ra.
jr $31 # Testing jr .



