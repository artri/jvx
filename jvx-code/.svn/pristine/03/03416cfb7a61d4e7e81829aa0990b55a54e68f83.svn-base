package research;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationForIPTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			String ip = "193.186.185.72";
			
			long start = System.currentTimeMillis();
			
			URL url = new URL("http://freegeoip.net/csv/" + ip);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
	
			InputStream is = connection.getInputStream();
	
			int status = connection.getResponseCode();
			if (status != 200) {
			    return;
			}
	
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF8"));
			String line = reader.readLine();
			while (line != null)
			{
				System.out.println(line);
				
				line = reader.readLine();
			}
			
			System.out.println("Duration: " + (System.currentTimeMillis() - start) + "ms");
		
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		System.exit(0);
	}

}
