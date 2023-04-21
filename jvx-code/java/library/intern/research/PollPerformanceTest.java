package research;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.sibvisions.util.type.FileUtil;

public class PollPerformanceTest
{
	
	public static void main(String[] pArgs)
	{
		try
		{
			Class.forName("oracle.jdbc.OracleDriver");
			
		    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.35:1521:xe", "system", "subclub2");
		
			
			PreparedStatement stat = conn.prepareStatement("select * from dual");
			
			long max = 0;
			long min = Integer.MAX_VALUE;
			long sum = 0;
			int count = 0;
			long glob = System.currentTimeMillis();
			for (int i = 0; i < 10000; i++)
			{
				long start = System.currentTimeMillis();
				ResultSet result = stat.executeQuery();
				
				result.next();
				result.getString(1);
				
				long diff = System.currentTimeMillis() - start;
				
				max = Math.max(max, diff);
				min = Math.min(min, diff);
				sum += diff;
				count++;
			}
			long gesamt = System.currentTimeMillis() - glob;

			System.out.println("DB poll:\n--------");
			System.out.println("Max: " + max);
			System.out.println("Min: " + min);
			System.out.println("Avg: " + (sum/count));
			System.out.println("Sum: " + sum + "  " + gesamt);
			
			
			File file = File.createTempFile("SpeedTest", ".tmp");
			
			FileOutputStream out = new FileOutputStream(file);
			out.write("DUMMY\nX".getBytes());
			out.close();
			
			max = 0;
			min = Integer.MAX_VALUE;
			sum = 0;
			count = 0;
			glob = System.currentTimeMillis();

			for (int i = 0; i < 10000; i++)
			{
				long start = System.currentTimeMillis();
				FileInputStream in = new FileInputStream(file);
				
				new String(FileUtil.getContent(in)); 
				
				long diff = System.currentTimeMillis() - start;
				
				max = Math.max(max, diff);
				min = Math.min(min, diff);
				sum += diff;
				count++;
			}
			gesamt = System.currentTimeMillis() - glob;

			System.out.println("\nFile poll:\n----------");
			System.out.println("Max: " + max);
			System.out.println("Min: " + min);
			System.out.println("Avg: " + (sum/count));
			System.out.println("Sum: " + sum + "  " + gesamt);
			
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
}
