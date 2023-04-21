package research;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class TimestampTest
{

	/**
	 * 
	 * @param pObject
	 */
	public static void main(String[] pObject)
	{
		TimeZone CET = TimeZone.getTimeZone("Europe/Berlin");
		TimeZone Eding = TimeZone.getTimeZone("Pacific/Enderbury");
		TimeZone GMT = TimeZone.getTimeZone("GMT");

		Date time = new Date();
		
		GregorianCalendar gregorGMT = new GregorianCalendar(GMT);
		GregorianCalendar gregorCET = new GregorianCalendar(CET);
		GregorianCalendar gregorEding = new GregorianCalendar(Eding);
		
		gregorGMT.setTimeInMillis(time.getTime());
		gregorCET.setTimeInMillis(time.getTime());
		gregorEding.setTimeInMillis(time.getTime());
		
		System.out.println(gregorGMT.getTime());
		System.out.println(gregorCET.getTime());
		System.out.println(gregorEding.getTime());
		
		Timestamp stamp = new Timestamp(time.getTime());
		
		System.out.println(stamp);
		
		TimeZone.setDefault(GMT);
		
		System.out.println(stamp);
		
		System.out.println(Calendar.getInstance(Locale.US).getTimeZone());
		System.out.println(Calendar.getInstance(Locale.CHINA).getTimeZone());
		//Date timeDate = new Date()
		
	}
	
	
}
