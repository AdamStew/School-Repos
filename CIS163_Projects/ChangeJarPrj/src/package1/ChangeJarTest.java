package package1;

import org.junit.*;
import static junit.framework.Assert.*;

public class ChangeJarTest {

	/**
	 * ***  Your assignment is to write many test cases  *****
	 */
	/* some examples provided to help you get started */

	@Test
	public void ALStestStringConstructor() {
		ChangeJar s1 = new ChangeJar(5.5);
		assertEquals (s1, new ChangeJar("5.5"));

		ChangeJar s2 = new ChangeJar(0.5);
		assertEquals (s2, new ChangeJar(".5"));

		ChangeJar s3 = new ChangeJar(0.05);
		assertEquals (s3, new ChangeJar(".05"));

		ChangeJar s4 = new ChangeJar(5.16);
		assertEquals (s4.getQuarters(), 20);
		assertEquals (s4.getDimes(), 1);
		assertEquals (s4.getNickels(), 1);
		assertEquals (s4.getPennies(), 1);


	}


	@Test(expected = IllegalArgumentException.class)
	public void ALStestConstructorNegDollars() {
		ChangeJar s = new ChangeJar(-300.0);
	}

	@Test(expected = NumberFormatException.class)
	public void ALStestConstructorbadFormat() {
		ChangeJar s = new ChangeJar("500.Q");
	}
	
	@Test(expected = NumberFormatException.class)
	public void ALStestConstructorbadFormat2() {
		ChangeJar s = new ChangeJar("What's UP");
	}

	@Test(expected = IllegalArgumentException.class)
	public void ALStestConstructorBadPennies() {
		ChangeJar s = new ChangeJar("-30");
	}

	@Test
	public void ALStestSub() {
		ChangeJar s1 = new ChangeJar(0);
		s1.add(2,2,2,2);
		s1.subtract(1,1,1,1);
		assertEquals (s1.getQuarters(), 1);
		assertEquals (s1.getDimes(), 1);
		assertEquals (s1.getNickels(), 1);
		assertEquals (s1.getPennies(), 1);
		
		ChangeJar s2 = new ChangeJar(0.75);
		s1.add(2,0,0,0);
		s1.subtract(s2);
		assertEquals (s1.getQuarters(), 0);
		assertEquals (s1.getDimes(), 1);
		assertEquals (s1.getNickels(), 1);
		assertEquals (s1.getPennies(), 1);
		
		ChangeJar s3 = new ChangeJar(0.16);
		s1.subtract(s3);
		assertEquals (s1.getQuarters(), 0);
		assertEquals (s1.getDimes(), 0);
		assertEquals (s1.getNickels(), 0);
		assertEquals (s1.getPennies(), 1);
	}

	@Test
	public void ALStestAdd() {
		ChangeJar s1 = new ChangeJar();
		ChangeJar s2 = new ChangeJar(1.00);
		s1.add(0,0,0,2000);
		s1.add(1,2,3,4);
		assertEquals (s1.getQuarters(), 1);
		assertEquals (s1.getDimes(), 2);
		assertEquals (s1.getNickels(), 3);
		assertEquals (s1.getPennies(), 2004);
		s1.add(s2);
		assertEquals (s1.getQuarters(), 5);
		assertEquals (s1.getDimes(), 2);
		assertEquals (s1.getNickels(), 3);
		assertEquals (s1.getPennies(), 2004);
	}


	@Test
	public void ALStestDec () {

		ChangeJar s1 = new ChangeJar(41.99);
		for (int i = 0; i < 2; i++) {
			s1.dec();
			// System.out.println (s1);
		}
		assertEquals (s1.getQuarters(), 167);
		assertEquals (s1.getDimes(), 2);
		assertEquals (s1.getNickels(), 0);
		assertEquals (s1.getPennies(), 2);
		
		ChangeJar s2 = new ChangeJar(1.01);
		s2.dec();
		assertEquals (s2.getPennies(), 0);
		
		ChangeJar s3 = new ChangeJar (0.01);
		s3.dec();
		assertEquals (s3.getPennies(), 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void ALStestBadDec() {
		ChangeJar s = new ChangeJar("1.00");
		s.dec();
	}

	@Test
	public void ALStestEqual () {
		ChangeJar s1 = new ChangeJar(55.2);
		ChangeJar s2 = new ChangeJar(60100);
		ChangeJar s3 = new ChangeJar(55.20);
		ChangeJar s4 = new ChangeJar("55.2");

		assertFalse(s1.equals(s2));
		assertTrue(s1.equals(s4));
	}

	@Test
	public void testCompareTo () {
		ChangeJar s1 = new ChangeJar(500);
		ChangeJar s2 = new ChangeJar(600);
		ChangeJar s3 = new ChangeJar(420);
		ChangeJar s4 = new ChangeJar("500.0");
		ChangeJar s5 = new ChangeJar(104.00);
		ChangeJar s6 = new ChangeJar(1.05);
		ChangeJar s7 = new ChangeJar(1.00);
		s7.add(0,0,1,0);

		assertTrue(s2.compareTo(s1) > 0);
		assertTrue(s3.compareTo(s1) < 0);
		assertTrue(s1.compareTo(s4) == 0);
		assertTrue(s3.compareTo(s5) > 0);
		assertTrue(s6.compareTo(s7) == 0);
		
	}

	@Test
	public void ALStestLoadSave() {
		ChangeJar s1 = new ChangeJar(55930);
		ChangeJar s2 = new ChangeJar("55930");
		ChangeJar s3 = new ChangeJar(5.34);
		ChangeJar s4 = new ChangeJar("5.34");

		s1.save("file1");
		s1 = new ChangeJar();  // resets to zero

		s1.load("file1");
		assertTrue(s1.equals(s2));
		
		s3.save("file2");
		s3 = new ChangeJar();
		
		s3.load("file2");
		assertTrue(s3.equals(s4));
	}
	
	@Test
	public void ALStestMutate(){
		ChangeJar s1 = new ChangeJar (1.00);
		ChangeJar s2 = new ChangeJar (1.00);
		ChangeJar s3 = new ChangeJar (1.00);
		ChangeJar s4 = new ChangeJar (2.56);
		ChangeJar s5 = new ChangeJar (2.56);
		
		s1.add(s2);
		assertEquals (s1.getQuarters(), 8);
		assertEquals (s1.getDimes(), 0);
		assertEquals (s1.getNickels(), 0);
		assertEquals (s1.getPennies(), 0);
		
		ChangeJar.mutate(false);
		s1.add(s3);
		assertEquals (s1.getQuarters(), 8);
		assertEquals (s1.getDimes(), 0);
		assertEquals (s1.getNickels(), 0);
		assertEquals (s1.getPennies(), 0);
		
		ChangeJar.mutate(true);
		s1.add(s4);
		assertEquals (s1.getQuarters(), 18);
		assertEquals (s1.getDimes(), 0);
		assertEquals (s1.getNickels(), 1);
		assertEquals (s1.getPennies(), 1);
		
		ChangeJar.mutate(false);
		assertEquals (s1.getQuarters(), 18);
		assertEquals (s1.getDimes(), 0);
		assertEquals (s1.getNickels(), 1);
		assertEquals (s1.getPennies(), 1);
	}
	
}