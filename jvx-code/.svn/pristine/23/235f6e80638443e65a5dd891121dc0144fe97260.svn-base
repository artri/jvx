package research;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sibvisions.util.type.ResourceUtil;

import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.CombinedSelector;
import cz.vutbr.web.css.RuleBlock;
import cz.vutbr.web.css.RuleSet;
import cz.vutbr.web.css.StyleSheet;
import cz.vutbr.web.css.Term;

public class FontAwesomeParser
{
    @Test
    public void parseIcons() throws Exception
    {
        StyleSheet css =  CSSFactory.parse(new File(ResourceUtil.getLocationForClass(ResourceUtil.getFqClassName(FontAwesomeParser.class)), 
                                                    "/research/fontawesome/font-awesome.css").getAbsolutePath(), 
                                           "ISO-8859-15");
        
        int iFoundUnique = 0;
        int iFoundNames  = 0;
        
        List<String> liCharacter = new ArrayList<String>();
        
        for (RuleBlock block : css.asList())
        {
            if (block instanceof RuleSet)
            {
                RuleSet set = (RuleSet)block;

                for (CombinedSelector selector : set.getSelectors())
                {
                    String sSelector = selector.toString(); 
                    
                    if (sSelector.startsWith(".fa-") && sSelector.endsWith("::before"))
                    {
                        if (set.size() == 1 && set.get(0).getProperty().equals("content"))
                        {
                            Term term = set.get(0).get(0);
                            
                            String sName = sSelector.substring(4, sSelector.length() - "::before".length());
                            String sChar = Integer.toHexString((int)((String)term.getValue()).toCharArray()[0]);

                            if (!liCharacter.contains(sChar))
                            {
                                liCharacter.add(sChar);
                                
                                iFoundUnique++;
                            }
                            
                            iFoundNames++;
                            
                            String sDisplayName = sName.toUpperCase().replace("-", "_");
                            
                            //replace invalid java name
                            if ("500PX".equals(sDisplayName))
                            {
                                sDisplayName = "FIVEHUNDRED_PX";
                            }
                            
                            //FontAwesome enum (Swing)
                            System.out.println("/** " + sName +" icon. */");
                            System.out.println(sDisplayName +"(\"" + sName + "\", '\\u" + sChar + "'),");
                            
                            //FontAwesome constants (genUI)
//                            System.out.println();
//                            System.out.println("/** the name of the small(default: 16x16 px) " + sName +" image (used for menuitems or buttons). */");
//                            System.out.println("public static final String " + sDisplayName +"_SMALL = \"FontAwesome." + sName + "\";");
//                            System.out.println("/** the name of the large(default: 24x24 px) " + sName +" image (used for toolbar buttons or icons). */");
//                            System.out.println("public static final String " + sDisplayName +"_LARGE = \"FontAwesome." + sName + ";size=24\";");
                            
                        }
                    }
                }
            }
        }
        
        System.out.println();
        System.out.println("=> Found icon names: " + iFoundNames);
        System.out.println("=> Found unique icons: " + iFoundUnique);
    }

}
