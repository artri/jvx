package research;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DurationAddSubMulDivBigDecimal
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			System.out.println("BigDecimal 36:");
			
			BigDecimal[] values = new BigDecimal[1000000];
			
			for (int i = 0; i < values.length; i++)
			{
				values[i] = new BigDecimal(Math.random() + 0.5d).setScale(36, RoundingMode.HALF_UP);
			}
			
			BigDecimal result = new BigDecimal(1);
			long start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].add(values[i-1]);
			}
			
			System.out.println("Duration " + values.length + " add: " + (System.currentTimeMillis() - start) + "ms " + result);

			result = new BigDecimal(1);
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].subtract(values[i-1]);
			}
			
			System.out.println("Duration " + values.length + " sub: " + (System.currentTimeMillis() - start) + "ms " + result);

			result = new BigDecimal(1);
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].multiply(values[i-1]).setScale(36, RoundingMode.HALF_UP);
			}
			
			System.out.println("Duration " + values.length + " mul: " + (System.currentTimeMillis() - start) + "ms " + result);
			
			result = new BigDecimal(1);
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].divide(values[i-1], 36, RoundingMode.HALF_UP);
			}
			
			System.out.println("Duration " + values.length + " div: " + (System.currentTimeMillis() - start) + "ms " + result);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		try
		{
			System.out.println("BigDecimal 16:");
			
			BigDecimal[] values = new BigDecimal[1000000];
			
			for (int i = 0; i < values.length; i++)
			{
				values[i] = new BigDecimal(Math.random() + 0.5d).setScale(16, RoundingMode.HALF_UP);
			}
			
			BigDecimal result = new BigDecimal(1);
			long start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].add(values[i-1]);
			}
			
			System.out.println("Duration " + values.length + " add: " + (System.currentTimeMillis() - start) + "ms " + result);

			result = new BigDecimal(1);
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].subtract(values[i-1]);
			}
			
			System.out.println("Duration " + values.length + " sub: " + (System.currentTimeMillis() - start) + "ms " + result);

			result = new BigDecimal(1);
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].multiply(values[i-1]).setScale(16, RoundingMode.HALF_UP);
			}
			
			System.out.println("Duration " + values.length + " mul: " + (System.currentTimeMillis() - start) + "ms " + result);
			
			result = new BigDecimal(1);
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i].divide(values[i-1], 16, RoundingMode.HALF_UP);
			}
			
			System.out.println("Duration " + values.length + " div: " + (System.currentTimeMillis() - start) + "ms " + result);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		try
		{
			System.out.println("double:");
			
			double[] values = new double[10000000];
			
			for (int i = 0; i < values.length; i++)
			{
				values[i] = Math.random() + 0.5d;
			}
			
			double result = 1;
			long start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i] + values[i-1];
			}
			
			System.out.println("Duration " + values.length + " add: " + (System.currentTimeMillis() - start) + "ms " + result);

			result = 1;
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i] - values[i-1];
			}
			
			System.out.println("Duration " + values.length + " sub: " + (System.currentTimeMillis() - start) + "ms " + result);

			result = 1;
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i] * values[i-1];
			}
			
			System.out.println("Duration " + values.length + " mul: " + (System.currentTimeMillis() - start) + "ms " + result);
			
			result = 1;
			start = System.currentTimeMillis();

			for (int i = 1; i < values.length; i++)
			{
				result = values[i] / values[i-1];
			}
			
			System.out.println("Duration " + values.length + " div: " + (System.currentTimeMillis() - start) + "ms " + result);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		System.exit(0);
	}

}
