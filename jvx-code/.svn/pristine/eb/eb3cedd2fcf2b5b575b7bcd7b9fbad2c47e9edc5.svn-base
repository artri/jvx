/*
 * Copyright 2021 SIB Visions GmbH
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
 * 08.04.2021 - [ST] - creation
 * 13.04.2021 - [ST] - use working directory to download application.jar
 */
package com.sibvisions.rad.ui.swing.impl;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.rad.ui.IAlignmentConstants;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import com.sibvisions.rad.ui.swing.ext.JVxIcon;
import com.sibvisions.rad.ui.swing.ext.JVxScrollPane;
import com.sibvisions.rad.ui.swing.ext.JVxUtil;
import com.sibvisions.rad.ui.swing.ext.layout.JVxFormLayout;
import com.sibvisions.util.Execute;
import com.sibvisions.util.type.ExceptionUtil;
import com.sibvisions.util.type.FileUtil;
import com.sibvisions.util.type.HttpUtil;
import com.sibvisions.util.type.ResourceUtil;
import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.xml.XmlNode;
import com.sibvisions.util.xml.XmlWorker;

/**
 * The <code>SwingStarter</code> is a class to start swing applications from data provided from an application.jnlp File.
 * 
 * @author Schoenberger Thomas
 */
public final class SwingStarter
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Initializes and runs the application.
	 * 
	 * @param pArguments the launcher arguments: main-class [config.xml key1=value1 key2=value2]
	 * @throws Exception if an error occurs
	 */
    public static void main(String[] pArguments) throws Exception
    {
    	SwingStarter starter = new SwingStarter();
        starter.startApplication();
    }

	/**
	 * Creates a new instance of <code>SwingStarter</code>.
	 */
	protected SwingStarter()
	{
	}
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    /**
	 * Starts a swing application from data provided from an application.jnlp file.
	 * 
	 * @throws Exception if an error occurs
	 */
    private void startApplication() throws Exception
    {
    	try
    	{
    		File fiBaseDir = new File("").getCanonicalFile();
    		
	    	File fiJnlp = new File(fiBaseDir, "/app/application.jnlp");
	    	
	    	if (!fiJnlp.exists())
	    	{
	    		fiJnlp = new File(fiBaseDir, "/installation/application.jnlp");
	    	}

	    	if (!fiJnlp.exists())
	    	{
	    		//e.g. MacOS
	    		fiBaseDir = new File(ResourceUtil.getPathForClass(ResourceUtil.getFqClassName(SwingStarter.class)));
	    		
	    		fiJnlp = new File(fiBaseDir, "application.jnlp");
	    	}

	        XmlNode xmn = XmlWorker.readNode(new FileInputStream(fiJnlp));
	
	        String codeBase = xmn.getNodeValue("/jnlp/codebase/");
	        StringBuilder classPath = new StringBuilder();
	        List<XmlNode> libraryNodes = xmn.getNodes("/jnlp/resources/jar/");
	
	        File dirCache = new File(System.getProperty("user.home") + File.separator + "japps" + File.separator + xmn.getNodeValue("/jnlp/application-desc/main-class/"));
	        dirCache.mkdirs();
	        
	        for (XmlNode libraryNode : libraryNodes)
	        {
	            String jar = libraryNode.getNodeValue("/href/");
	
	            // create a folder in user.home to store the jar file
	            File jarFile = new File(dirCache, jar);
	                        
	            if (classPath.length() > 0)
	            {
	            	classPath.append(File.pathSeparator);
	            }
	            
	            if (jarFile.exists())
	            {
	                URL url = new URL(codeBase + jar);
	                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	                long date = httpCon.getLastModified();
	
	                if (date > jarFile.lastModified())
	                {
	                    if (jarFile.delete())
	                    {
	                        FileUtil.save(jarFile, HttpUtil.get(codeBase + jar));
	                    }
	                }
	            }
	            else
	            {
	                FileUtil.save(jarFile, HttpUtil.get(codeBase + jar));
	            }
	            
	            classPath.append(jarFile.getAbsolutePath());
	        }
	
	        Execute processExecution = new Execute();
	        processExecution.setProgram(System.getProperty("java.home") + File.separatorChar + "bin" + File.separatorChar + "java");
	        processExecution.addParameter("-cp");
	        processExecution.addParameter(classPath.toString());
	        processExecution.addParameter(xmn.getNodeValue("/jnlp/application-desc/main-class/"));
	
	        List<XmlNode> applicationArguments = xmn.getNodes("/jnlp/application-desc/argument/");
	        if (applicationArguments != null && applicationArguments.size() > 0)
	        {
		        for (XmlNode applicationArgument : applicationArguments)
		        {
		            processExecution.addParameter(applicationArgument.getValue());
		        }
	        }
	
	        for (String vmArg : xmn.getNodeValue("/jnlp/resources/j2se/java-vm-args/").split(" "))
	        {
	            processExecution.addParameter(vmArg);
	        }
	
	        processExecution.execute(false);
	
	        //wait a little bit...
	        Thread.sleep(250);
	        
	        String sError = processExecution.getError();
	        
	        if (!StringUtil.isEmpty(sError))
	        {
	        	new ErrorDialog(sError);
	        	
	        	return;
	        }
		}
	    catch (Throwable th)
		{
			new ErrorDialog(ExceptionUtil.dump(th, true));
		}
    	finally
    	{
    		System.exit(0);
    	}
    }

	//****************************************************************
	// Subclass definition
	//****************************************************************

    /**
     * The <code>ErrorDialog</code> is a {@link JDialog} that shows a message as modal dialog.
     * 
     * @author Schoenberger Thomas
     */
    private static final class ErrorDialog extends JDialog
    {
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Class members
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/** the layout. */
    	private JVxFormLayout folDialog;

    	/** the OK button. */
    	private JButton butOK;
    	
    	/** the message scroll pane. */
    	private JScrollPane jscMessage;
    	
    	/** the error message. */
    	private String sMessage;
    	
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    	// Initialization
    	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    	/**
    	 * Creates and shows the dialog.
    	 * 
    	 * @param pMessage the error message
    	 */
    	public ErrorDialog(String pMessage)
    	{
    		super(new JFrame());
    		
    		super.setAlwaysOnTop(true);
    		
    		sMessage = pMessage;
    		
    		init();
    	}
    	
    	/**
    	 * Initializes the UI.
    	 */
    	private void init()
    	{
    		try
    		{
    			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    		}
    		catch (Exception e)
    		{
    			//nothing to be done
    		}
    		
    		JVxIcon icoError = new JVxIcon(JVxUtil.getImage("/javax/rad/genui/images/32x32/error.png"));
    		
    		JTextArea taMessage = new JTextArea();
    		taMessage.setMinimumSize(new Dimension(0, 70));
            taMessage.setPreferredSize(new Dimension(470, 70));
    		taMessage.setWrapStyleWord(true);
            taMessage.setLineWrap(true);
    		taMessage.setEditable(false);
    		taMessage.setText(sMessage);
    		
    		jscMessage = new JVxScrollPane(taMessage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    		
    		butOK = new JButton();
    		butOK.addActionListener
    		(
    			new ActionListener() 
    			{
    				
    				@Override
    				public void actionPerformed(ActionEvent e) 
    				{
    					dispose();
    				}
    			}
    		);
    		butOK.setText("OK");
    		
    		//----------------------------------------------------------------
    		// Standard
    		//----------------------------------------------------------------
    		
    		folDialog = new JVxFormLayout();
    		folDialog.setVerticalAlignment(IAlignmentConstants.ALIGN_TOP);
    		folDialog.setMargins(new Insets(7, 7, 7, 7));

    		setLayout(folDialog);
    		getRootPane().setDefaultButton(butOK);

    		add(icoError);
    		add(jscMessage,
    			new JVxFormLayout.Constraint(folDialog.getTopMarginAnchor(),
    										 new JVxFormLayout.Anchor(folDialog.getConstraint(icoError).getRightAnchor(), 5),
    									     null,
    									     folDialog.getRightMarginAnchor()));
    		add(butOK,
    			new JVxFormLayout.Constraint(null,
    		    		                     null,
    		    		                     folDialog.getBottomMarginAnchor(),
    		    		                     folDialog.getRightMarginAnchor()));
    		
    		//make the message resizable
    		folDialog.getConstraint(jscMessage).setBottomAnchor(new JVxFormLayout.Anchor(folDialog.getConstraint(butOK).getTopAnchor(), -7));
    		
    		setTitle("Error");
    		setModal(true);
    		((JFrame)getOwner()).setIconImage(JVxUtil.getImage("/com/sibvisions/visionx/images/visionx_icon_32x32.png"));
    		pack();
    		setLocationRelativeTo(null);
    		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		((JFrame)getOwner()).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		setVisible(true);
    		
    		((JFrame)getOwner()).dispose();
    	}
    	
    }	// ErrorDialog
    
}	// SwingStarter

