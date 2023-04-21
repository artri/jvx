package research;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.sibvisions.util.type.ResourceUtil;

public class ColorParser
{
    @Test
    public void parseCodes() throws Exception
    {
        //InputStream is = ResourceUtil.getResourceAsStream("/research/color/colorcodes.csv");
        InputStream is = ResourceUtil.getResourceAsStream("/research/color/colorcodes_www.csv");
        
        InputStreamReader reader = new InputStreamReader(is);
        
        BufferedReader buffer = new BufferedReader(reader);
        
        String line;
        
        while ((line = buffer.readLine()) != null)
        {
            String[] sElements = new String[2];
            
            int iPos = line.indexOf(";");
            sElements[0] = line.substring(0, iPos);
            sElements[1] = line.substring(iPos + 1);
            
            String[] sRGB;
            
            if (sElements[1].indexOf(";") >= 0)
            {
                //Format: "R;G;B"
                sRGB = sElements[1].substring(1, sElements.length - 1).split(";"); 
            }
            else
            {
                //Format: #FFCCDD
                sRGB = new String[3];
                
                sRGB[0] = "" + Integer.parseInt(sElements[1].substring(1, 3), 16);
                sRGB[1] = "" + Integer.parseInt(sElements[1].substring(3, 5), 16);
                sRGB[2] = "" + Integer.parseInt(sElements[1].substring(5, 7), 16);
            }
            
            System.out.println("hmpColor.put(\"" + sElements[0].trim().toLowerCase() +"\", new int[] {"+ sRGB[0] +", " + sRGB[1] +", " + sRGB[2] +"});");
        }
    }
}
