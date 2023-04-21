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
 * 04.09.2009 - [HM] - creation  
 * 21.07.2020 - [JR] - #2329: notifyVisible, notifyDestroy events     
 * 19.08.2023 - [JR] - more unique root name          
 */
package javax.rad.application.genui;

import javax.rad.application.IApplication;
import javax.rad.application.IContent;
import javax.rad.application.genui.event.ContentHandler;
import javax.rad.application.genui.event.type.content.INotifyDestroyListener;
import javax.rad.application.genui.event.type.content.INotifyVisibleListener;
import javax.rad.genui.UILayout;
import javax.rad.genui.container.UIPanel;
import javax.rad.ui.IComponent;
import javax.rad.ui.container.IInternalFrame;

import com.sibvisions.util.type.StringUtil;
import com.sibvisions.util.type.StringUtil.TextType;

/**
 * The <code>Content</code> is the default {@link IContent} implementation and extends
 * an {@link UIPanel}.
 *  
 * @author Martin Handsteiner
 */
public class Content extends UIPanel 
					 implements IContent
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the opener. */
	private transient Object opener;
	
	/** the "notify visible" event. */
	private transient ContentHandler<INotifyVisibleListener> eventNotifyVisible;
	/** the "notify destroy" event. */
	private transient ContentHandler<INotifyDestroyListener> eventNotifyDestroy;
	
	/** whether the content is already destroyed. */
	private transient boolean bDestroyed;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>Content</code>.
	 */
	public Content()
	{
	}
	
	/**
     * Creates a new instance of <code>Content</code>.
     *
     * @param pLayout the layout.
     */
	public Content(UILayout pLayout)
	{
		setLayout(pLayout);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public <OP> void setOpener(OP pOpener)
	{
		opener = pOpener;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <OP> OP getOpener()
	{
		return (OP)opener;
	}

    /**
     * {@inheritDoc}
     */
    public void notifyVisible()
    {
    	setDestroyed(false);
        
        if (eventNotifyVisible != null)
        {
        	eventNotifyVisible.dispatchEvent(this);
        }
    }

    /**
	 * {@inheritDoc}
	 */
	public void notifyDestroy()
	{
		setDestroyed(true);
	    
	    if (eventNotifyDestroy != null)
	    {
	    	eventNotifyDestroy.dispatchEvent(this);
	    }
	}
	
    /**
     * {@inheritDoc}
     */
    public boolean isDestroyed()
    {
        return bDestroyed;
    }

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addNotify()
	{
		// Only set a new root name if there wasn't already one set.
		// This is to make sure that extending classes are able to set
		// their own rootname before addNotify is called.
		// Under normal circumstances, the root name should not be set
		// until addNotify is called.
		if (isRootContainer() && getRootName() == null)
		{
			setRootName(createComponentName());
		}
		
		super.addNotify();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String createComponentName()
	{
		if (isRootContainer())
		{
		    return createComponentNameRoot();
		}
		else
		{
		    return createComponentNameOriginal();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sets the destroyed state of this content.
	 * 
	 * @param pDestroyed <code>true</code> to set destroyed, <code>false</code> otherwise
	 */
	protected void setDestroyed(boolean pDestroyed)
	{
		bDestroyed = pDestroyed;
	}
	
	/**
	 * Gets whether this content should get a root name.
	 * 
	 * @return whether this content should get a root name.
	 */
	protected boolean isRootContainer()
	{
	    return true;
	}
	
    /**
     * The original component name of UIComponent.
     * 
     * @return The original component name of UIComponent.
     */
    protected String createComponentNameRoot()
    {
        String sSimpleName = StringUtil.getText(getClass().getSimpleName(), TextType.UpperCase);

        IComponent cp = getParent();
        
        while (cp != null && !(cp instanceof IContent))
        {
            cp = cp.getParent();
        }
        
        String sName;
        
        if (cp != null && !(cp instanceof IApplication))
        {
            sName = cp.getName() + "_" + sSimpleName;
        }
        else
        {
            sName = sSimpleName;
        }
        
        cp = getParent();
        
        while (cp != null && !(cp instanceof IInternalFrame))
        {
            cp = cp.getParent();
        }
        
        if (cp != null)
        {
        	String sTitle = ((IInternalFrame)cp).getTitle();
        	
        	//use a title, if available -> this makes the name more unique
        	if (sTitle != null)
        	{
				int hashCode = sTitle.hashCode();
				if (hashCode == Integer.MIN_VALUE)
				{
					// Math.abs(INTEGER.MIN_VALUE) yields Integer.MIN_VALUE).
					// Let us just add 1 and be done with it.
					hashCode = hashCode + 1;
				}
				
				int checkDigit = Math.abs(hashCode) % 1296;
				String checkDigitString = Integer.toString(checkDigit, 36);
				
				if (checkDigitString.length() < 2)
				{
					checkDigitString += "0" + checkDigitString;
				}
				
				sName += "_" + checkDigitString.toUpperCase();
        	}
        }
        
        
        if (opener instanceof IComponent)
        {
        	sName = ((IComponent)opener).getName() + "_" + sName;
        }
		
		return sName;        
    }
    
	/**
	 * The original component name of UIComponent.
	 * 
	 * @return The original component name of UIComponent.
	 */
    protected String createComponentNameOriginal()
    {
        return super.createComponentName();
    }
    
	/**
	 * Gets the event handler for {@link #notifyVisible()}.
	 * 
	 * @return the event handler
	 */
	public ContentHandler<INotifyVisibleListener> eventNotifyVisible()
	{
		if (eventNotifyVisible == null)
		{
			eventNotifyVisible = new ContentHandler<INotifyVisibleListener>(INotifyVisibleListener.class);
		}
		
		return eventNotifyVisible;
	}
	
	/**
	 * Gets the event handler for {@link #notifyDestroy()}.
	 * 
	 * @return the event handler
	 */
	public ContentHandler<INotifyDestroyListener> eventNotifyDestroy()
	{
		if (eventNotifyDestroy == null)
		{
			eventNotifyDestroy = new ContentHandler<INotifyDestroyListener>(INotifyDestroyListener.class);
		}
		
		return eventNotifyDestroy;
	}
	
}	// Content
