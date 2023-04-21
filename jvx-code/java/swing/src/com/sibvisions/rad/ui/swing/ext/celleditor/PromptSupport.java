/*
 * Copyright 2020 SIB Visions GmbH
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
 * 02.06.2020 - [JR] - creation
 */
package com.sibvisions.rad.ui.swing.ext.celleditor;

import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.JTextComponent;

import com.sibvisions.rad.ui.swing.ext.JVxConstants;
import com.sibvisions.util.type.CommonUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * The <code>PromptSupport</code> is a reflective wrapper for org.jdesktop prompt support.
 * 
 * @author René Jahn
 */
public final class PromptSupport implements DocumentListener, ComponentListener, PropertyChangeListener
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /** The text component. */
    private JTextComponent textComponent;
    
    /** The prompt renderer. */
    private DefaultTableCellRenderer promptRenderer;
    
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * The text component for configuring the prompt.
	 * 
	 * @param pComponent the text component.
	 */
	private PromptSupport(JTextComponent pComponent)
	{
	    textComponent = pComponent;
	    
	    promptRenderer = new DefaultTableCellRenderer();
        promptRenderer.setFont(null);
	    promptRenderer.setForeground(pComponent.getDisabledTextColor());
        promptRenderer.setBackground(null);
        promptRenderer.setOpaque(false);
	    
	    Insets insets;
	    if (textComponent.getBorder() instanceof EmptyBorder)
	    {
	        insets = (Insets)UIManager.get("TextField.margin");
	    }
	    else
	    {
            insets = textComponent.getInsets();
	    }
	    promptRenderer.setBorder(new EmptyBorder(insets));
	    
	    textComponent.addComponentListener(this);
        textComponent.addPropertyChangeListener(this);
	    textComponent.getDocument().addDocumentListener(this);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
    /**
     * Uninstall the prompt.
     */
    public void uninstall()
    {
        textComponent.remove(promptRenderer);
        
        textComponent.removeComponentListener(this);
        textComponent.removePropertyChangeListener(this);
        textComponent.getDocument().removeDocumentListener(this);
    }
    
	/**
	 * Gets the prompt.
	 * 
	 * @return the prompt.
	 */
	public String getPrompt()
	{
	    return promptRenderer.getText();
	}
	
    /**
     * Gets the prompt.
     * 
     * @param pPrompt the prompt.
     */
    public void setPrompt(String pPrompt)
    {
        promptRenderer.setText(pPrompt);
        
        layoutPromptRenderer();
    }
    
    /**
     * Layouts the prompt renderer.
     */
    public void layoutPromptRenderer()
    {
        if (textComponent instanceof JTextField)
        {
            promptRenderer.setHorizontalAlignment(((JTextField)textComponent).getHorizontalAlignment());
        }
        else
        {
            promptRenderer.setVerticalAlignment(JVxConstants.TOP);
        }

        String currentText = textComponent.getText();
        
        if (currentText == null || currentText.length() == 0)
        {
            if (promptRenderer.getParent() == null)
            {
                textComponent.add(promptRenderer);
            }
        }
        else 
        {
            if (promptRenderer.getParent() != null)
            {
                textComponent.remove(promptRenderer);
            }
        }
        promptRenderer.setSize(textComponent.getSize());
    }
    
	/**
	 * Gets the prompt support.
	 * 
	 * @param pComponent the text component
	 * @return the prompt support 
	 */
	private static PromptSupport getPromptSupport(JTextComponent pComponent)
	{
	    return (PromptSupport)pComponent.getClientProperty("promptSupport");
	}
	
    /**
     * Gets the prompt support.
     * 
     * @param pComponent the text component
     * @param pPromptSupport the prompt support 
     */
    private static void setPromptSupport(JTextComponent pComponent, PromptSupport pPromptSupport)
    {
        pComponent.putClientProperty("promptSupport", pPromptSupport);
    }
    
    /**
     * Gets the prompt support.
     * 
     * @param pComponent the text component
     * @return the prompt support 
     */
    private static PromptSupport installPromptSupport(JTextComponent pComponent)
    {
        PromptSupport promptSupport = getPromptSupport(pComponent);
        
        if (promptSupport == null)
        {
            promptSupport = new PromptSupport(pComponent);
            
            setPromptSupport(pComponent, promptSupport);
        }
        
        return promptSupport;
    }
    
    /**
     * Gets the prompt support.
     * 
     * @param pComponent the text component
     */
    private static void uninstallPromptSupport(JTextComponent pComponent)
    {
        PromptSupport promptSupport = getPromptSupport(pComponent);
        
        if (promptSupport != null)
        {
            promptSupport.uninstall();
            
            setPromptSupport(pComponent, null);
        }
    }
    
	/**
	 * Gets the prompt of the text component.
	 * 
	 * @param pComponent the text component
	 * @return the prompt
	 */
	public static String getPrompt(JTextComponent pComponent)
	{
	    PromptSupport promptSupport = getPromptSupport(pComponent);
	    
	    if (promptSupport != null)
	    {
	        return promptSupport.getPrompt();
	    }
	    
	    return null;
	}
	
	/**
	 * Sets the prompt for given component.
	 * 
	 * @param pComponent the component
	 * @param pText the prompt or <code>null</code> to remove prompt
	 */
	public static void setPrompt(JTextComponent pComponent, String pText)
	{
	    if (StringUtil.isEmpty(pText))
	    {
	        pText = null;
	    }
	    if (!CommonUtil.equals(pText, getPrompt(pComponent)))
	    {
            if (pText == null)
            {
                uninstallPromptSupport(pComponent);
            }
            else
            {
                PromptSupport promptSupport = installPromptSupport(pComponent);

                promptSupport.setPrompt(pText);
            }
	    }
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void componentResized(ComponentEvent pEvent)
    {
        layoutPromptRenderer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentMoved(ComponentEvent pEvent)
    {
        layoutPromptRenderer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentShown(ComponentEvent pEvent)
    {
        layoutPromptRenderer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentHidden(ComponentEvent pEvent)
    {
        layoutPromptRenderer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUpdate(DocumentEvent pEvent)
    {
        layoutPromptRenderer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate(DocumentEvent pEvent)
    {
        layoutPromptRenderer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedUpdate(DocumentEvent pEvent)
    {
        layoutPromptRenderer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent pEvent)
    {
        layoutPromptRenderer();
    }
	
}	// PromptSupport
