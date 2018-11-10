
//Adam Stewart

import java.text.DecimalFormat;

public class GVdate

{       private int month;
        private int day;
        private int year;
        private int birthDay = 15;
        private int birthMonth = 1;
        private String monthNumbers = ("312831303130313130313031");
        private String leapNumbers = ("312931303130313130313031");
        private String monthShorts = ("JanFebMarAprMayJunJulAugSepOctNovDec");
        
    public GVdate (int m, int d, int y) {
        month = m;
        day = d;
        year = y;
    }

    public GVdate (String pDate) {
        int backslash1 = pDate.indexOf("/");
        int backslash2 = pDate.indexOf("/", backslash1+1);
        
        month = Integer.parseInt (pDate.substring(0, backslash1));
        
        day = Integer.parseInt (pDate.substring(backslash1 + 1, backslash2));
        
        year = Integer.parseInt (pDate.substring(backslash2 + 1));
    }
    
    public void setMonth(int pMonth) {
        if(pMonth<=12){
            month = pMonth;
        }else{
            System.out.println("Month is invalid.  ");
        }
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int pDay) {
        if(day>=1 && day<=31){
            day = pDay;
        }else{
            System.out.println("Day is invalid.  ");
        }
    }
    
    public int getDay() {
        return day;
    }
    
    public void setYear(int pYear) {
        if(pYear>0){
            year = pYear;
        }else{
            System.out.println("Year is invalid.  ");
        }
    }
    
    public int getYear() {
        return year;
    }
    
    public String toString() {
        return month + "/" + getDay() + "/" + getYear();
    }
    
    public void setDate(int m, int d, int y) {
        month = m;
        day = d;
        year = y;
        
    }
    
    public boolean isMyBirthday() {
        if (getMonth() == birthMonth && getDay() == birthDay){
            return true;
        }else{ 
            return false;
        }    
    }
    
    public void nextDay() {
        if (month == 2 && (year%4 == 0 && year%100 != 0 || year%400 == 0) && day!=29 && day==28)    {
            day=29;
        }else if (day<(Integer.parseInt(monthNumbers.substring ((month-1)*2, month*2)))){
                day++;
            }else  if(day>=(Integer.parseInt(monthNumbers.substring ((month-1)*2, month*2)))&&month!=12){
                    day=1; month++;
                }else if(day==31 && month==12){
                        day=1; month=1; year++;
                    }
                }
  
    public boolean equals(GVdate otherDate){
        if(month==otherDate.month && day==otherDate.day && year==otherDate.year){  
            return true;
        }else{
            return false;
        }
    }
    
    public boolean isLeapYear(int y) {
        if (y%4 == 0 && y%100 != 0 || y%400 == 0){
            return true;
        }else{
            return false;
        }
    }
  
    public boolean isDayValid(int pMonth, int pDay, int pYear){
        if ((month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12) && day <=31){
            return true;
        }else if((month==4 || month==6 || month==9 || month==11) && day<=30){
                return true;
            }else if(month==2 && day<=28){
                    return true;
                }else if((month==2 && day<=29) && (year%4 == 0 && year%100 != 0 || year%400 == 0)){
                        return true;
                    }else{
                            return false;
                }
    }
    
    public boolean isMonthValid(int pMonth){
        if (pMonth < 13){
            return true;
        }else{
            return false;
        } 
    }
    
    public boolean isYearValid (int pYear){
        if (pYear > 0){
            return true;
        }else{
            return false;
        }
    }
    
    public String toString(int format){
        if(format==1){
            return month + "/" + getDay() + "/" + getYear();
        }    
        
        
        DecimalFormat df = new DecimalFormat ("##00");
        if(format==2){
            return df.format(month) + "/" + df.format(getDay()) + "/" + getYear();
        }
 
        if(format==3){
            return monthShorts.substring ((month-1)*3, month*3) + " " + df.format(getDay()) + ", " + 
        df.format(getYear());
        }
        
        if (format==4){
         if (month == 1)
            return "January " + getDay() + ", " + getYear();
         else if (month == 2)
            return "Februrary " + getDay() + ", " + getYear();
         else if (month == 3)
            return "March " + getDay() + ", " + getYear();
         else if (month == 4)
            return "April " + getDay() + ", " + getYear();
         else if (month == 5)
            return "May " + getDay() + ", " + getYear();
         else if (month == 6) 
            return "June " + getDay() + ", " + getYear();
         else if (month == 7) 
            return "July " + getDay() + ", " + getYear();
         else if (month == 8) 
            return "August " + getDay() + ", " + getYear();
         else if (month == 9) 
            return "September " + getDay() + ", " + getYear();
         else if (month == 10) 
            return "October " + getDay() + ", " + getYear();
         else if (month == 11) 
            return "November " + getDay() + ", " + getYear();
         else if (month == 12) 
            return "December " + getDay() + ", " + getYear();
        }   
        return "";
    }
}
