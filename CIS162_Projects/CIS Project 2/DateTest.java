import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
/*****************************************************
 * The test class GVdate.
 *
 * @author  Scott Grissom
 * @version (a version number or a date)
 * Updated September 2014 - Ana Posada
 * To include:
 * 1. contructor GVdate (String date) testing 
 * 2. printing formats testing 
 * 3. invalid date testing for constructors and setters.
 * 4. deleted testSkipAhead
 * 5. setMonth, setDay and setYear testing
 ****************************************************/
public class DateTest{
    private GVdate d;

    /************************************************
     * Sets up the test fixture.
     *
     * Called before every test case method.
     ***********************************************/
    @Before
    public void setUp()
    {
        d = new GVdate(4,20,1963);         
    }

    /*********************************************************
     *  Test Date Constructor(int m, int d, int y)
     ********************************************************/
    @Test
    public void testConstructor(){
        GVdate d = new GVdate(4,20,1963);        
        Assert.assertEquals("GVdate() month not set correctly", 
            4, d.getMonth());                
        Assert.assertEquals("GVdate() day not set correctly", 
            20, d.getDay());   
        Assert.assertEquals("GVdate() year not set correctly", 
            1963, d.getYear());      
    } 

    /*********************************************************
     *  Test Date Constructor GVdate (String date)
     ********************************************************/
    @Test
    public void testConstructor2(){
        GVdate d = new GVdate("4/20/1963");        
        Assert.assertEquals("GVdate() month not set correctly", 
            4, d.getMonth());                
        Assert.assertEquals("GVdate() day not set correctly", 
            20, d.getDay());   
        Assert.assertEquals("GVdate() year not set correctly", 
            1963, d.getYear()); 
    } 

    /*********************************************************
     *  Test Invalid date constructor1(int m, int d, int y)
     ********************************************************/
    @Test
    public void testInvalidDateConstructor(){
        GVdate d = new GVdate(13,20,1963);   
        Assert.assertEquals("GVdate() invalid month", 
            0, d.getMonth());  
        GVdate d1 = new GVdate(2,29,1963); 
        Assert.assertEquals("GVdate() invalid day", 
            0, d1.getDay()); 
        GVdate d2 = new GVdate(4,20,-4); 
        Assert.assertEquals("GVdate() invalid year", 
            0, d2.getYear());      
    } 

    /*********************************************************
     *  Test Invalid date constructor2(String date)
     ********************************************************/
    @Test
    public void testInvalidDateConstructor2(){
        GVdate d1 = new GVdate("13/20/1963");   
        Assert.assertEquals("GVdate() invalid month", 
            0, d1.getMonth());  
        GVdate d2 = new GVdate("2/29/1963"); 
        Assert.assertEquals("GVdate() invalid day", 
            0, d2.getDay()); 
        GVdate d3 = new GVdate("4/20/-4"); 
        Assert.assertEquals("GVdate() invalid year", 
            0, d3.getYear());      
    } 

    /*********************************************************
     * Test setDate 
     ********************************************************/
    @Test
    public void testSetDate(){
        GVdate d = new GVdate(4,20,1963);    
        d.setDate(3,21,1965);
        Assert.assertEquals("setDate() month not set correctly", 
            3, d.getMonth());                
        Assert.assertEquals("setDate() day not set correctly", 
            21, d.getDay());   
        Assert.assertEquals("setDate() year not set correctly", 
            1965, d.getYear());
    } 

    /*********************************************************
     * Test invalid date setDate 
     ********************************************************/
    @Test
    public void testInvalidDateSetDate(){
        GVdate d = new GVdate(4,20,1963);    
        d.setDate(3,21,1965);
        d.setDate(13,21,1965);
        Assert.assertEquals("setDate() invalid month", 
            3, d.getMonth()); 
        d.setDate(2,29,2014);
        Assert.assertEquals("setDate() invalid day", 
            21, d.getDay()); 
        d.setDate(3,21,-3);
        Assert.assertEquals("setDate() invalid year", 
            1965, d.getYear()); 
    } 

    /*********************************************************
     * Test setMonth 
     ********************************************************/
    @Test
    public void testSetMonth(){
        GVdate d = new GVdate(4,20,1963);    
        d.setMonth(12);
        Assert.assertEquals("setMonth() month not set correctly", 
            12, d.getMonth());                
    } 

    /*********************************************************
     * Test setYear
     ********************************************************/
    @Test
    public void testSetYear(){
        GVdate d = new GVdate(4,20,1963);    
        d.setYear(2013);
        Assert.assertEquals("setYear() year not set correctly", 
            2013, d.getYear());                
    } 

    /*********************************************************
     * Test setDay
     ********************************************************/
    @Test
    public void testSetDay(){
        GVdate d = new GVdate(4,20,1963);    
        d.setDay(28);
        Assert.assertEquals("setDay() day not set correctly", 
            28, d.getDay());   
        d.setDate(2,28,2004);
        d.setDay(29);
        Assert.assertEquals("setDay() day not set correctly", 
            29, d.getDay());   

    } 

    /*********************************************************
     * Test InvalidDaysetDay
     ********************************************************/
    @Test
    public void testInvalidDaySetDay(){
        GVdate d = new GVdate(4,20,1963);    
        d.setDay(32);
        Assert.assertEquals("setDay() invalid day", 
            20, d.getDay());   
        GVdate d1 = new GVdate(2,20,2014);    
        d1.setDay(29);
        Assert.assertEquals("setDay() invalid day", 
            20, d.getDay());   
        GVdate d2 = new GVdate(2,20,2004);    
        d1.setDay(32);
        Assert.assertEquals("setDay() invalid day", 
            20, d.getDay());   
    } 

    /*********************************************************
     * Test invalid month setMonth 
     ********************************************************/
    @Test
    public void testInvalidMonthSetMonth(){
        GVdate d = new GVdate(4,20,1963);    
        d.setMonth(13);
        Assert.assertEquals("setMonth() month not set correctly", 
            4, d.getMonth());                
    } 

    /*********************************************************
     * Test setYear
     ********************************************************/
    @Test
    public void testInvalidYearSetYear(){
        GVdate d = new GVdate(4,20,1963);    
        d.setYear(0);
        Assert.assertEquals("setYear() year not set correctly", 
            1963, d.getYear());                
    } 

    /*********************************************************
     * Test Tomorrow
     ********************************************************/
    @Test
    public void testTomorrow(){
        d.setDate(4,20,1963);
        d.nextDay();
        Assert.assertEquals("Problems with nextDay when it is NOT the end of the month", 
            21, d.getDay());  
        Assert.assertEquals("Problems with nextDay when it is NOT the end of the month", 
            4, d.getMonth());  

        d.setDate(3,31,1963);
        d.nextDay();
        Assert.assertEquals("Problems with nextDay when IT IS the end of the month", 
            1, d.getDay());   

        Assert.assertEquals("Problems with nextDay when IT IS the end of the month", 
            4, d.getMonth());

        d.setDate(12,31,1963);
        d.nextDay();
        Assert.assertEquals("Dec 31 should go to Jan 1 of next year", 
            1964, d.getYear());  
        Assert.assertEquals("Dec 31 should go to Jan 1 of next year", 
            1, d.getDay());   
        Assert.assertEquals("Dec 31 should go to Jan 1 of next year", 
            1, d.getMonth());   
    }   

    /*********************************************************
     * Test Month Rollover
     ********************************************************/
    @Test
    public void testMonthRollover(){
        d.setDate(4,30,1963);
        d.nextDay();
        Assert.assertEquals("Problems with nextDay when IT IS the end of the month", 
            1, d.getDay()); 
        Assert.assertEquals("Problems with nextDay when IT IS the end of the month", 
            5, d.getMonth()); 
        d.setDate(1,31,1963);
        d.nextDay();
        Assert.assertEquals("Problems with nextDay when IT IS the end of the month", 
            1, d.getDay());  
        Assert.assertEquals("Problems with nextDay when IT IS the end of the month", 
            2, d.getMonth());  
        d.setDate(2,28,1963);
        d.nextDay();
        Assert.assertEquals("Feb 28 for a non leap year should become March 1", 
            1, d.getDay());   
        Assert.assertEquals("Feb 28 for a non leap year should become March 1", 
            3, d.getMonth());  
        d.setDate(2,28,1964);
        d.nextDay();
        Assert.assertEquals("Feb 28 should become Feb 29 in a leap year", 
            29, d.getDay());   
        Assert.assertEquals("Feb 28 should become Feb 29 in a leap year", 
            2, d.getMonth()); 
        d.setDate(2,29,1964);
        d.nextDay();
        Assert.assertEquals("Feb 29 should become March 1 in a leap year", 
            1, d.getDay()); 
        Assert.assertEquals("Feb 29 should become March 1 in a leap year", 
            3, d.getMonth()); 

    }  

    /*********************************************************
     * Test Year Rollover
     ********************************************************/
    @Test
    public void testYearRollover(){
        d.setDate(12,31,1963);
        d.nextDay();
        Assert.assertEquals("December 31 should become Jan 1", 
            1964, d.getYear());   
        Assert.assertEquals("December 31 should become Jan 1", 
            1, d.getDay());   
        Assert.assertEquals("December 31 should become Jan 1", 
            1, d.getMonth());   
    }  

    /*********************************************************
     * Test toString
     *********************************************************/
    @Test
    public void testToString(){
        d.setDate(12,31,1963);
        Assert.assertEquals("Problems with formatting the date as required: "
            + "mm/dd/yyyy" , "12/31/1963", d.toString());                            
    }  

    /*********************************************************
     * Test toString - Testing the different formats
     *********************************************************/
    @Test
    public void testToString2(){    
        //Testing format 1
        d.setDate(7,31,1963);
        Assert.assertEquals("Problems with formatting the date as required: "
            + "mm/dd/yyyy","7/31/1963", d.toString(1));   

        //Testing format 2
        d.setDate(7,31,1963);
        Assert.assertEquals("Problems with formatting the date as required: " 
            + "mm/dd/yyyy - 2 digits for month","07/31/1963", d.toString(2));  

        //Testing format 3
        d.setDate(12,31,1963);
        Assert.assertEquals("Problems with formatting the date as required: "
            + "MMM dd, yyyy","Dec 31, 1963", d.toString(3)); 

        //Testing format 4
        d.setDate(12,31,1963);
        Assert.assertEquals("Problems with formatting the date as required: " 
            + "Month dd, yyyy","December 31, 1963", d.toString(4)); 
    }  

    /*********************************************************
     * Test Leap Year
     ********************************************************/
    @Test
    public void testLeapYear(){
        d.setDate(4,20,1963);
        Assert.assertTrue("1963 is NOT a leap year", 
            !d.isLeapYear(1963));                
        d.setDate(4,20,2000);
        Assert.assertTrue("2000 is a leap year", 
            d.isLeapYear(2000));   
        d.setDate(4,20,2004);
        Assert.assertTrue("2004 is a leap year", 
            d.isLeapYear(2004));            
        d.setDate(4,20,1900);
        Assert.assertTrue("1900 is NOT a leap year", 
            !d.isLeapYear(1900));    
    }      

    /*********************************************************
     * Test Equals
     ********************************************************/
    @Test
    public void testEquals(){
        GVdate d1 = new GVdate(4,20,1963);
        GVdate d2 = new GVdate(4,20,1963);       
        Assert.assertTrue("Two dates should be equal", 
            d1.equals(d2));                
        d2.setDate(4,21,1963);        
        Assert.assertTrue("Two dates should NOT be equal", 
            !d1.equals(d2));  

    }     
}