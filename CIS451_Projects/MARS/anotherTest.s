#  exampleCIT-3.s
#  data movement instruction

	.data
val1:	.word 42		# data variable and value
val2:	.word 43
	.text
	.globl main
main:	



	addi $8, $8, 65535
	addi $9, $9, 35000
	
	or $10, $9, $8
	
	ori $11, $9, 35000

	
	li $v0, 10		# syscall to exit
	syscall   		# execute syscall	

