package research;

import java.sql.Timestamp;

import javax.rad.model.ColumnDefinition;
import javax.rad.model.RowDefinition;

import com.sibvisions.rad.model.mem.DataRow;

/**
 * Test if all Memory get release, when storing the ArrayList<> of the DataRows
 * (result of DBAccess) in the DataPage.
 * 
 * @author Roland Hörmann
 */
public class MemberReferenceTest
{

	public static void main(String[] args) throws Exception
	{
		System.out.println(new Timestamp(7676));
		
		// 128080
		RowDefinition rdRowDefinition = new RowDefinition();

		ColumnDefinition cd1 = new ColumnDefinition("LALA1");
		ColumnDefinition cd2 = new ColumnDefinition("LALA2");
		ColumnDefinition cd3 = new ColumnDefinition("LALA3");
		ColumnDefinition cd4 = new ColumnDefinition("LALA4");
		rdRowDefinition.addColumnDefinition(cd1);
		rdRowDefinition.addColumnDefinition(cd2);
		rdRowDefinition.addColumnDefinition(cd3);
		rdRowDefinition.addColumnDefinition(cd4);

		DataRow[] aDataRows = new DataRow[10000]; // 852000 B = 832K (168680
		// ->Array + Overhead)
		//ArrayList<Object> alRows = new ArrayList<Object>(10000);

		for (int i = 0; i < aDataRows.length; i++)
		{
			aDataRows[i] = new DataRow(rdRowDefinition);
			//alRows.add(aDataRows[i].getData());
		}

		long mem3 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		rdRowDefinition = null;
		cd1 = null;
		cd2 = null;
		cd3 = null;
		cd4 = null;
		for (int i = 0; i < aDataRows.length; i++)
		{
			aDataRows[i] = null;
		}
		aDataRows = null;

		// alRows=null;

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

		System.out.println("1xDataRow with 4xCellDef. 1xRowDefinition = " + (mem3 - mem4));
	}

}
