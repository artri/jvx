/*
 * Copyright 2014 SIB Visions GmbH
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
 * 18.08.2016 - [RZ] - creation
 */
package com.sibvisions.rad.ui.swing.ext;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.junit.Test;

/**
 * Test cases for {@link JVxTabbedPane}.
 * 
 * @author René Jahn
 */
public class TestJVxTabbedPane
{
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Test methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Test case for {@link https://oss.sibvisions.com/index.php?do=details&task_id=1670}.
     * 
     * @throws Exception if test fails
     */
    @Test
    public void testScrollBug1670() throws Exception
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JVxTabbedPane pane = new JVxTabbedPane();
        pane.setDraggable(true);
//        pane = new JTabbedPane();

        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        for (int i = 0; i <= 20; i++) 
        {
          pane.addTab(String.format("%d--------------------", Integer.valueOf(i)), new JLabel("" + i));
        }
        
        frame.getContentPane().add(pane, BorderLayout.CENTER);
        frame.setSize(500, 200);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent e) 
            {
                synchronized (TestJVxTabbedPane.this)
                {
                    TestJVxTabbedPane.this.notify();
                }
            }
        });
        
        frame.setVisible(true);      

        synchronized (this)
        {
            wait();
        }
    }
    
}   // TestJVxTabbedPane
