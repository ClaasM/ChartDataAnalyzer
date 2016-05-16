package test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import other.Utils;

public class UtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseBooleanArray() {
		boolean[][] test = {{true, false, true},{false, true, true},{true, true, false}};
		String asString = Arrays.deepToString(test);
		System.out.println(asString);
		boolean[][] parsed = Utils.parseBooleanArray(asString);
		System.out.println(Arrays.deepToString(parsed));
		assertEquals(test, parsed);
	}

}
