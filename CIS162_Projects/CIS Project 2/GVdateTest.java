
public class GVdateTest
{ public static void main (String[] args) {
    GVdate d1 = new GVdate(10,5,2014); //Regular date
    GVdate d2 = new GVdate(10,5,2014); //Testing if two dates are equal
    GVdate d3 = new GVdate(2,28,2000); //Testing Leap Year Rollover
    GVdate d4 = new GVdate(1,31,2014); //Testing Month Rollover
    GVdate d5 = new GVdate(12,31,2014); //Testing Year Rollover
    GVdate d6 = new GVdate(2,28,2001); //Testing Invalid Leap Year Rollover
    GVdate d7 = new GVdate(1,15,2018); //Testing isBirthday
    GVdate d8 = new GVdate(1,15,2020);
    GVdate d9 = new GVdate(1,32,2014); //Testing isDayValid
    GVdate d10 = new GVdate(13,1,2014); //Testing isMonthValid
    GVdate d11 = new GVdate(1,1,0000); //Testing isYearValid
    
    
    System.out.println("Fall 2014 – CIS162 Project 2 – Adam Stewart");
    System.out.println("==========================================");
   
    System.out.println("Today: " + d1);
    d1.nextDay();
    System.out.println("Tomorrow: " + d1);
    d1.nextDay();
    System.out.println("Next Day: " + d1);
    System.out.println("");
    
    System.out.println("Testing Leap Year");
    System.out.println("================="); 
    System.out.println("Is 2014 a leap year? " + d1.isLeapYear(2014));
    System.out.println("Is 2000 a leap year? " + d3.isLeapYear(2000));
    System.out.println("");
    
    System.out.println("Testing Month Rollover");
    System.out.println("======================");
    System.out.println("Today is: " + d4);
    d4.nextDay();
    System.out.println("Tomorrow is: " + d4);
    System.out.println("");
   
    System.out.println("Testing Year Rollover");
    System.out.println("=====================");
    System.out.println("Today is: " + d5);
    d5.nextDay();
    System.out.println("Tomorrow is: " + d5);
    System.out.println("");
    
    System.out.println("Testing Non-Leap-Year Rollover");
    System.out.println("==============================");
    System.out.println("Today is: " + d6);
    d6.nextDay();
    System.out.println("Tomorrow is: " + d6);
    System.out.println("");
    
    System.out.println("Testing Leap Year Rollover");
    System.out.println("==========================");
    System.out.println("Today is: " + d3);
    d3.nextDay();
    System.out.println("Tomorrow is: " + d3);
    System.out.println("");
    
    System.out.println("Testing My Birthday");
    System.out.println("===================");
    System.out.println("Is 1/15/2018 my birthday? " + d7.isMyBirthday());
    System.out.println("Is 1/15/2020 my birthday? " + d8.isMyBirthday());
    System.out.println("Is 12/31/2014 my birthday? " + d5.isMyBirthday());
    System.out.println("");
    
    System.out.println("Testing Equal Dates");
    System.out.println("====================");
    System.out.print("Is 10/5/2014 equal to 1/15/2018? ");
       if(d1.equals(d7)){
        System.out.println("They're equal!");
    }else{
        System.out.println("They're not equal.");
    }
    System.out.print("Is 10/5/2014 equal to 10/5/2014? ");
    if(d1.equals(d2)){
        System.out.println("They're equal!");
    }else{
        System.out.println("They're not equal.");
    }
    System.out.println("");
    
    System.out.println("Testing if Dates are Valid");
    System.out.println("==========================");
    System.out.println("Is 1/32/2014 a valid day? " + d9.isDayValid(1,32,2014));
    System.out.println("Is 13/1/2014 a valid month? " + d10.isMonthValid(13));
    System.out.println("Is 1/1/0000 a valid year? " + d11.isYearValid(0000));
 }
}
