package research;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.rad.model.IDataPage;
import javax.rad.model.IRowDefinition;
import javax.rad.model.event.DataRowHandler;
import javax.rad.model.ui.IControl;

import com.sibvisions.util.ArrayUtil;

/**
 * Test the Difference between an Object[] and an ArrayList<>.<br>
 * <br> 
 * -> more 80KB, for 1 Mio rows.
 * 
 * @author Roland Hörmann
 */
public class MemOverheadTest
{
	/** Runtime for accessing the memory information. */
	private static Runtime rt = Runtime.getRuntime(); 

	public static class DataRow 
	{
		/** Dummy Data for Memorytest. */
		protected Object[] data;
		/** Dummy Data for Memorytest. */
		protected IRowDefinition rdRowDefinition = null;
		/** Dummy Data for Memorytest. */
		protected DataRowHandler	eventValuesChanged = null;
		/** Dummy Data for Memorytest. */
		protected ArrayUtil<IControl> auControls = null;
		/** Dummy Data for Memorytest. */
		protected IDataPage page = null;
		/** Dummy Data for Memorytest. */
		protected int index = 2;
		
		public DataRow(Object[] pData)
		{
			data = pData;
		}
	}
	
	private static double[] roundPostKomma = new double[20];
	private static double[] roundPreKomma = new double[20];
	
	static
	{
		for (int i = 0; i < roundPostKomma.length; i++)
		{
			roundPostKomma[i] = Math.pow(10d, i);
			roundPreKomma[i] = Math.pow(10d, -i);
		}
	}
	
	public static double round(double pValue, int pPrecision)
	{
		if (pValue == Double.POSITIVE_INFINITY || pValue == Double.NEGATIVE_INFINITY)
		{
			return pValue;
		}
		else if (pPrecision == 0)
		{
			return Math.round(pValue);
		}
		else if (pPrecision > 0)
		{
			return Math.round(pValue * roundPostKomma[pPrecision]) / roundPostKomma[pPrecision];
		}
		else
		{
			return Math.round(pValue * roundPreKomma[pPrecision]) / roundPreKomma[pPrecision];
		}
	}
	
	
	public static void main(String[] args)
	{
		try
		{
			Iterable<String> iter = new Iterable<String>()
			{
				public Iterator<String> iterator()
				{
					ArrayList<String> list = new ArrayList<String>();
					list.add("Hallo");
					list.add("Martin");
					list.add("wie");
					list.add("Gehts?");
					return list.iterator();
				}
			};
			
			for (String value : iter)
			{
				System.out.println(value);
			}
			
			
/*			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "visionx", "visionx");

			PreparedStatement stat = conn.prepareStatement("insert into test (value) values (?)"); 
			
			stat.setDouble(1, 0.1234567890123456789012345678901234567890123456789d);
			
			stat.executeUpdate();
			
			System.exit(0);
			
			//			double test1 = Double.NaN;
			double test2 = 0.3;
			double test3 = 0.2;
			double test4 = 0.4;
			double test5 = 0.1;
			double test6 = 0.15;
			double result1 = 0;
			double result2 = 0;
//			double result3 = 0;
			
			long start1 = System.currentTimeMillis();
			
			for (int i = 0; i < 1000000; i++)
			{
				result1 = round(result1 + test2 * test3 / test4, 2);
				result2 = round(result2 + test2 / test3 / 0d, 2);
//				result3 += test1 * test5 / test6;
			}

			System.out.println(-1d / 0d);
			System.out.println(1d / 0d);
			System.out.println(0d / 0d);
			System.out.println(System.currentTimeMillis() - start1);
			
			System.out.println(result1);
			System.out.println(result2);
//			System.out.println(result3);
			
//			BigDecimal btest1 = new BigDecimal(Double.NaN);
			BigDecimal btest2 = new BigDecimal("0.3");
			BigDecimal btest3 = new BigDecimal("0.2");
			BigDecimal btest4 = new BigDecimal("0.4");
			BigDecimal btest5 = new BigDecimal("0.1");
			BigDecimal btest6 = new BigDecimal("0.15");
			BigDecimal bresult1 = new BigDecimal("0");
			BigDecimal bresult2 = new BigDecimal("0");
//			BigDecimal bresult3 = new BigDecimal(0);
			
			long start2 = System.currentTimeMillis();
			
			for (int i = 0; i < 1000000; i++)
			{
				bresult1 = bresult1.add(btest2.multiply(btest3).divide(btest4, BigDecimal.ROUND_HALF_UP));
				bresult2 = bresult2.add(btest2.divide(btest3, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(10)));
//				bresult3 = bresult3.add(btest1.multiply(btest5).divide(btest6));
			}
			
			System.out.println(System.currentTimeMillis() - start2);
			
			System.out.println(bresult1);
			System.out.println(bresult2);
//			System.out.println(bresult3);
			
			System.exit(0);
*/			
/*			
			Hashtable<String, Method> testString = new Hashtable<String, Method>();
			Hashtable<String, Hashtable<String, Method[]>> testHash = new Hashtable<String, Hashtable<String, Method[]>>();
			
			Method method = MemOverheadTest.class.getMethod("main", String[].class);
			String methodName = method.getName();
			Object[] params = new Object[1];
			
			testString.put(MemOverheadTest.class.hashCode() + "|" + methodName + "|" + 1, method);
			Hashtable<String, Method[]> sub = new Hashtable<String, Method[]>();
			sub.put(methodName, new Method[] {null, null, method});
			testHash.put(MemOverheadTest.class.getName(), sub);
			
			System.out.println(rt.totalMemory() / 1024 + "  " + rt.freeMemory() / 1024);

			long mem = usedMemory();

			long start = System.currentTimeMillis();

			int count = 0;
//			StringBuilder build = new StringBuilder();
			
			for (int i = 0; i < 10000000; i++)
			{
//				build.setLength(0);
//				build.append(MemOverheadTest.class.hashCode());
//				build.append("|");
//				build.append(methodName);
//				build.append("|");
//				build.append(params.length);
//				Method x = testString.get(build.toString());
//				Method x = testString.get(MemOverheadTest.class.hashCode() + "|" + methodName + "|" + params.length);
//				Method x = MemOverheadTest.class.getMethod("main", String[].class);
				Method x = testHash.get(MemOverheadTest.class.getName()).get(methodName)[params.length + 1];

				if (x == method)
				{
					count++;
				}
			}
			
			System.out.println("Dauer: " + (System.currentTimeMillis() - start));
			System.out.println("Memory Usage: " + (usedMemory() - mem) / 1024 + "  " + rt.totalMemory() / 1024 + "  " + rt.freeMemory() / 1024);
			System.out.println(count);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		
		System.exit(0);
*/		
		long mem = usedMemory();
		long start = System.currentTimeMillis();
		
		ArrayUtil<DataRow> rows = new ArrayUtil<DataRow>();
		
		for (int i = 0; i < 300000; i++)
		{
			rows.add(new DataRow(new Object[] {BigDecimal.valueOf(i), "Name" + i, "Walter"}));
		}
/*		
		ArrayUtil<Object[]> rows = new ArrayUtil<Object[]>();
		
		for (int i = 0; i < 300000; i++)
		{
			rows.add(new Object[] {BigDecimal.valueOf(i), "Name" + i, "Walter"});
		}
*/		
//		MemoryInfo.freeMem();
		
		System.out.println("Memory Usage: " + (usedMemory() - mem) / 1024 / 1024 + " MB Time: " + (System.currentTimeMillis() - start));
		
		
			Thread.sleep(50000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Returns the used memory.
	 * 
	 * @return total memory - free memory
	 */
	public static long usedMemory()
	{
		return rt.totalMemory() - rt.freeMemory();
	}

	
}
