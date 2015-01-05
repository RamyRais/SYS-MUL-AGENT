package usine;

import org.junit.Assert;
import org.junit.Test;

public class test {

	@Test
	public void test() {
		String t = "table 50";
		String[] c = t.split(",");
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i]);
			String[] z = c[i].split(" ");
			int x = Integer.parseInt(z[1]);
			Assert.assertEquals(x, 50);
			
		}
		
	
	}

}
