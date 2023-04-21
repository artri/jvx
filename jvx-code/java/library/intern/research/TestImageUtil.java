/*
 * Copyright 2009 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 * History
 *
 * 22.12.2022 - [HM] - creation
 */
package research;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.junit.Test;

import com.sibvisions.util.type.ImageUtil;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests {@link ImageUtil} methods.
 * 
 * @author Martin Handsteiner
 * @see ImageUtil
 */
public class TestImageUtil
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests loading a special image.
	 */
	@Test
	public void testReadJpg() throws Exception
	{
	    try
	    {
	        ImageIO.read(ResourceUtil.getResourceAsStream("/research/grauerkreis_5.jpg"));
	    }
	    catch (Exception ex)
	    {
	        ex.printStackTrace(); // Without twelve monkeys, this image cannot be read by java. 
	    }
	}

    /**
     * Tests loading a special image.
     */
    @Test
    public void testGetScaledImage() throws Exception
    {
        ImageIcon pIcon = new ImageIcon(ResourceUtil.getResource("/research/cwlogo-rotate.png"));
        
        System.out.println(pIcon.getIconWidth() + "x" + pIcon.getIconHeight());

        pIcon = ImageUtil.getScaledIcon(pIcon, pIcon.getIconWidth(), 16, true);
        
        System.out.println(pIcon.getIconWidth() + "x" + pIcon.getIconHeight());
    }
	
}	// TestStringUtil
