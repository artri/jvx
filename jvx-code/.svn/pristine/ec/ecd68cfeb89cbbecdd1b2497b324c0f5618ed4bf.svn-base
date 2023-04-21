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
 * 29.11.2008 - [JR] - creation
 *                   - used WindowsDesktopManager as template (only needed for bugfixing)
 * 28.01.2009 - [JR] - don't transfer the maximized state between frames                   
 */
package com.sibvisions.rad.ui.swing.ext;

import java.beans.PropertyVetoException;
import java.lang.ref.WeakReference;

import javax.swing.DefaultDesktopManager;
import javax.swing.JInternalFrame;

/**
 * This class extends the {@link DefaultDesktopManager} to follow the MDI
 * model more than the DefaultDesktopManager. Unlike the DefaultDesktopManager
 * policy, MDI requires that the selected and activated child frames are the
 * same, and that that frame always be the top-most window.
 * <p>
 * The maximized state is managed by the DesktopManager with MDI, instead of
 * just being a property of the individual child frame. This means that if the
 * currently selected window is maximized and another window is selected, that
 * new window will be maximized.
 *
 * @author René Jahn
 * @see javax.swing.DefaultDesktopManager
 */
public class JVxDesktopManager extends DefaultDesktopManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** the reference to the current selected frame. */
	private WeakReference<JInternalFrame>	wrSelectedFrame;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activateFrame(JInternalFrame pFrame)
	{
		JInternalFrame currentFrame = wrSelectedFrame != null ? wrSelectedFrame.get() : null;
		
		try
		{
			super.activateFrame(pFrame);
			
			if (currentFrame != null && pFrame != currentFrame)
			{
/*				
				// If the current frame is maximized, transfer that
				// attribute to the frame being activated.
				if (currentFrame.isMaximum() && (pFrame.getClientProperty("JInternalFrame.frameType") != "optionDialog"))
				{
					// Special case. If key binding was used to select next
					// frame instead of minimizing the icon via the minimize
					// icon.
					if (!currentFrame.isIcon())
					{
						currentFrame.setMaximum(false);
						if (pFrame.isMaximizable())
						{
							if (!pFrame.isMaximum())
							{
								pFrame.setMaximum(true);
							}
							else if (pFrame.isMaximum() && pFrame.isIcon())
							{
								pFrame.setIcon(false);
							}
							else
							{
								pFrame.setMaximum(false);
							}
						}
					}
				}
*/				
				if (currentFrame.isSelected())
				{
					currentFrame.setSelected(false);
				}
			}

			if (!pFrame.isSelected())
			{
				pFrame.setSelected(true);
			}
		}
		catch (PropertyVetoException e)
		{
			//nothing to be done!
		}
		
		if (pFrame != currentFrame)
		{
			wrSelectedFrame = new WeakReference(pFrame);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeFrame(JInternalFrame pFrame)
	{
		super.closeFrame(pFrame);
		
		JInternalFrame currentFrame = wrSelectedFrame != null ? wrSelectedFrame.get() : null;

		if (pFrame == currentFrame)
		{
			wrSelectedFrame = null;
		}
	}
	
}	// JVxDesktopManager
