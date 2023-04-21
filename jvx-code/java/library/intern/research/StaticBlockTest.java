package research;

import com.sibvisions.util.log.LoggerFactory;
import com.sibvisions.util.log.ILogger.LogLevel;

public class StaticBlockTest
{
	
	public static void main(String[] pArgs)
	{
		//System.out.println(DerivedClass.DEFAULT_COLOR1);

		System.out.println(DerivedClass.DEFAULT_COLOR2);
		
		LoggerFactory.getInstance("com.sibvisions.rad.server").setLevel(LogLevel.ALL);
		
		//System.out.println(SuperClass.DEFAULT_COLOR1);

		System.exit(0);
	}
	
	
}
