package package1;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSimpleDatabase {
	
	//All methods already tested in TestLinkedList, except save and 
	//load
	
	//Testing save/load when loading empty file
	@Test
	public void testSaveLoadEmpty() {
		SimpleDatabase data = new SimpleDatabase();
		data.saveDB("testemptyload");
		Student a = new Student("Aaa", "G123", 3);
		data.insert(a);
		data.loadDB("testemptyload");
		assertEquals(data.list.getHead(),null);
	}
	
	//Testing save/load with data in file
	@Test
	public void testSaveLoad() {
		SimpleDatabase data = new SimpleDatabase();
		Student a = new Student("Aaa", "G123", 3);
		Student b = new Student("Bbb", "G123", 3);
		Student c = new Student("Ccc", "G123", 3);
		Student d = new Student("Ddd", "G123", 3);
		data.insert(a);
		data.insert(b);
		data.insert(c);
		data.saveDB("testloaddata");
		data.insert(d);
		data.loadDB("testloaddata");
		assertTrue(data.list.getTail().getData().equals(c));
	}
	
}
