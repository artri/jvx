/*
 * Copyright 2017 SIB Visions GmbH
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
 * 13.03.2017 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.junit.Test;

import com.sibvisions.rad.ui.swing.ext.JVxDesktopPane.DisplayMode;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;

/**
 * Test cases for {@link JVxDesktopPane}.
 * 
 * @author René Jahn
 */
public class TestJVxDesktopPane
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Tests desktop mode switching.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testFrameMode() throws Exception
    {
    	System.setProperty("apple.laf.useScreenMenuBar", "true");
    	
        //UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setSize(1024, 768);

        JMenu mExit = new JMenu("Exit");
        
        JMenuBar menu = new JMenuBar();
        menu.add(mExit);
        
        frame.setJMenuBar(menu);
        
        final JVxDesktopPane pane = new JVxDesktopPane();
        
        JVxFormLayout flFrame = new JVxFormLayout();
        flFrame.setVerticalAlignment(JVxFormLayout.BOTTOM);
        flFrame.setHorizontalAlignment(JVxFormLayout.CENTER);
        
        final JVxInternalFrame iframe = new JVxInternalFrame();
        iframe.setTitle("Internal frame 1");
        iframe.setSize(300, 400);
        iframe.setClosable(true);
        iframe.setResizable(true);
        iframe.setMaximizable(true);
        iframe.setLayout(flFrame);
        iframe.addComponentListener(new ComponentListener() 
        {
			@Override
			public void componentShown(ComponentEvent arg0) 
			{
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) 
			{
				System.out.println("Resized: " + ((JVxInternalFrame)arg0.getSource()).getSize());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) 
			{
				System.out.println("Moved: " + ((JVxInternalFrame)arg0.getSource()).getLocation());
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) 
			{
			}
		});

        JButton butSwitch = new JButton("Switch Mode");
        butSwitch.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (pane.getDisplayMode() == DisplayMode.InternalFrame)
                {
                    pane.setDisplayMode(DisplayMode.Frame);
                }
                else
                {
                    pane.setDisplayMode(DisplayMode.InternalFrame);
                }
            }
        });
        
        JButton butNew = new JButton("New iframe");
        butNew.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JVxInternalFrame iframe2 = new JVxInternalFrame();
                iframe2.setTitle("Internal frame 2");
                iframe2.setSize(150, 200);
                iframe2.setClosable(true);
                iframe2.setResizable(true);
                iframe2.setMaximizable(true);

                iframe2.setVisible(true);

                pane.add(iframe2);
                
                iframe2.setLocation(10, 10);
            }
        });

        JButton butSize = new JButton("New size");
        butSize.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	iframe.setSize(new Dimension(iframe.getSize().width + 1, iframe.getSize().height + 1));
            }
        });
        
        JButton butBounds = new JButton("New bounds");
        butBounds.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	iframe.setBounds(iframe.getLocation().x + 1, iframe.getLocation().y + 1, iframe.getSize().width + 1, iframe.getSize().height + 1);
            }
        });

        
        iframe.add(butSwitch);
        iframe.add(butNew);
        iframe.add(butSize);
        iframe.add(butBounds);

        pane.add(iframe);
        
        iframe.setVisible(true);
        
        frame.add(pane);
        
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent e) 
            {
                synchronized (TestJVxDesktopPane.this)
                {
                    TestJVxDesktopPane.this.notify();
                }
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        iframe.setLocationRelativeTo(pane);

        iframe.setSize(400, 600);
        
        synchronized (this)
        {
            wait();
        }
    }    
    
}   // TestJVxTabbedPane
