package research;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.DateUtil;

/**
 * Test the Difference between an Object[] and an ArrayList<>.<br>
 * <br> 
 * -> more 80KB, for 1 Mio rows.
 * 
 * @author Roland Hörmann
 */
public class MemTest
{
	@SuppressWarnings({"unused"})
	public static void main(String[] args)
	{
		
		ArrayUtil<String> au= new ArrayUtil<String>();
		
		au.add("11");
		au.add("22");
		au.add("33");
		au.add("44");
		
		System.out.println(au.get(2));
		au.remove(2);
		for (int i = 0; i < au.size(); i++)
		{
			System.out.println("," + au.get(i));
		}
		
		
		
		BigDecimal bgTest = new BigDecimal("0.44");
		System.out.println(bgTest.precision() + "," + bgTest.scale() + "," + bgTest.unscaledValue() +
				"," + bgTest.toPlainString());
		
		bgTest = bgTest.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		System.out.println(bgTest.precision() + "," + bgTest.scale() + "," + bgTest.unscaledValue() +
				"," + bgTest.toPlainString());
		
		Timestamp ts = DateUtil.getTimestamp(2010, 06, 07, 10, 31, 12);
		ts.setNanos(777);
		
		System.out.println(ts);
		
		ArrayList<Object> als1 = new ArrayList<Object>(10000);
		ArrayList<Object> als2 = new ArrayList<Object>(10000);
		ArrayList<Object> als3 = new ArrayList<Object>(10000);
		ArrayList<Object> als4 = new ArrayList<Object>(10000);
		ArrayList<Object> als5 = new ArrayList<Object>(10000);
		ArrayList<Object> als6 = new ArrayList<Object>(10000);
		ArrayList<Object> als7 = new ArrayList<Object>(10000);
		ArrayList<Object> als8 = new ArrayList<Object>(10000);
		ArrayList<Object> als9 = new ArrayList<Object>(10000);
		ArrayList<Object> als10 = new ArrayList<Object>(10000);

		// ArrayList<ArrayList<Object>> al = new
		// ArrayList<ArrayList<Object>>(1000);

		long mem3 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		// al = null;
		als1 = null;
		als2 = null;
		als3 = null;
		als4 = null;
		als5 = null;
		als6 = null;
		als7 = null;
		als8 = null;
		als9 = null;
		als10 = null;
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();

		long mem4 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		Object[] oa1 = new Object[10000];
		Object[] oa2 = new Object[10000];
		Object[] oa3 = new Object[10000];
		Object[] oa4 = new Object[10000];
		Object[] oa5 = new Object[10000];
		Object[] oa6 = new Object[10000];
		Object[] oa7 = new Object[10000];
		Object[] oa8 = new Object[10000];
		Object[] oa9 = new Object[10000];
		Object[] oa10 = new Object[10000];
		// Object[][] oa = new Object[1000][20];

		long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		oa1 = null;
		oa2 = null;
		oa3 = null;
		oa4 = null;
		oa5 = null;
		oa6 = null;
		oa7 = null;
		oa8 = null;
		oa9 = null;
		oa10 = null;
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();
		System.gc();

		long mem2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		System.out.println("Object[1000]" + (mem1 - mem2));
		System.out.println("ArrayList<Object>(1000)" + (mem3 - mem4));
	}

}
