/*
 * Copyright 2016 SIB Visions GmbH
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
 * 09.09.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.rad.genui.UIFactoryManager;
import javax.rad.genui.UIImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.ui.swing.impl.SwingFactory;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.ResourceUtil;

/**
 * Tests functionality of {@link JVxUtil}.
 * 
 * @author René Jahn
 */
public class TestJVxUtil
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
     * Prints all available readers.
     */
    @Test
    public void printReaders()
    {
        for (String sReader : ImageIO.getReaderFileSuffixes()) 
        {
            System.out.println(sReader);
        }   
    }
    
    /**
     * Tests icon creation with different Image APIs.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testCreateIcon() throws Exception
    {
        UIFactoryManager.getFactoryInstance(SwingFactory.class);
        
        MediaTracker mt = new MediaTracker(new JLabel());

        int iLoops = 5;
        
        System.out.println("Count = " + (UIImage.getImageMappingNames().length * iLoops));

        long l = System.currentTimeMillis();
        
        for (int i = 1; i <= iLoops; i++)
        {
            for (String sName : UIImage.getImageMappingNames())
            {
                String sRealName = UIImage.getImageMapping(sName);
                
                Toolkit.getDefaultToolkit().createImage(FileUtil.getContent(ResourceUtil.getResourceAsStream(sRealName)));
            }
        }

        System.out.println("Toolkit: " + (System.currentTimeMillis() - l));

        l = System.currentTimeMillis();
        
        for (int i = 1; i <= iLoops; i++)
        {
            for (String sName : UIImage.getImageMappingNames())
            {
                String sRealName = UIImage.getImageMapping(sName);
                
                Image image = Toolkit.getDefaultToolkit().createImage(FileUtil.getContent(ResourceUtil.getResourceAsStream(sRealName)));
                
                Assert.assertTrue(image.getWidth(null) == -1);
                
                mt.addImage(image, 0);
                mt.waitForID(0);
                
                Assert.assertTrue(image.getWidth(null) > 0);
            }
        }

        System.out.println("Toolkit-pre: " + (System.currentTimeMillis() - l));
        
        l = System.currentTimeMillis();
        
        for (int i = 1; i <= iLoops; i++)
        {
            for (String sName : UIImage.getImageMappingNames())
            {
                String sRealName = UIImage.getImageMapping(sName);
                
                BufferedImage image = ImageIO.read(ResourceUtil.getResourceAsStream(sRealName));
                
                Assert.assertTrue(image.getWidth(null) > 0);
            }
        }

        System.out.println("ImageIO: " + (System.currentTimeMillis() - l));

        String[] sExt = new String[] {"gif", "png", "jpg", "tif", "tiff", "bmp"};
        int[] iWidth = new int[] {110, 110, 110, -1, -1, -1};
        
        for (int i = 0; i < sExt.length; i++)
        {
            Image image = Toolkit.getDefaultToolkit().createImage(FileUtil.getContent(ResourceUtil.getResourceAsStream("/com/sibvisions/rad/ui/swing/ext/images/jvx." + sExt[i])));

            mt.addImage(image, 0);
            mt.waitForID(0);
            
            Assert.assertEquals(iWidth[i], image.getWidth(null));
            
            BufferedImage bimage = ImageIO.read(ResourceUtil.getResourceAsStream("/com/sibvisions/rad/ui/swing/ext/images/jvx." + sExt[i]));

            Assert.assertNotNull("Couldn't load image type: " + sExt[i], bimage);

            if (bimage != null)
            {
                ImageIcon icon = new ImageIcon(bimage);
                
                Assert.assertNotNull("Can't load image with type " + sExt[i], icon);
                
                /*
                JFrame frame = new JFrame();
                frame.add(new JLabel(icon));
                frame.setTitle(sExt[i]);
                frame.pack();
                frame.setVisible(true);
                */
            }
        }
    }
    
}   // TestJVxUtil
