lw r3, 4(r1) ;item1
lw r4, 8(r1) ;item2
add r6, r3, r4 ;item1 + item2
sw 0(r1), r6 ;storing sum
lw r5, 0(r2) ;tax
add r7, r6, r5 ;sum + tax
sw 12(r1), r7 ;storing total
lw r8, 8(r2) ;item3
lw r9, 12(r2) ;item4
add r10, r9, r8 ;item3 + item4
sw 4(r2), r10 ;storing sum2
nop
nop
nop
nop
trap	#0
