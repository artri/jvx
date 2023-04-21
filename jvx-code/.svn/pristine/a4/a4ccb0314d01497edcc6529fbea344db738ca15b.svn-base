/*
 * Copyright 2013 SIB Visions GmbH
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
 * 02.10.2013 - [JR] - creation
 */
package javax.rad.application;

import javax.rad.application.genui.UILauncher;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.ui.IComponent;
import javax.rad.ui.container.IInternalFrame;

import com.sibvisions.rad.application.Dialog;
import com.sibvisions.rad.application.Dialog.ButtonMode;
import com.sibvisions.rad.ui.swing.impl.SwingApplication;

/**
 * The <code>DialogTestApplication</code> class simply tests {@link Dialog}.
 * 
 * @author René Jahn
 */
public class DialogTestApplication extends DesktopApplication
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Startup
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Starts the application.
	 * 
	 * @param pArgs arguments
	 */
	public static void main(String[] pArgs)
	{
		SwingApplication sapp = new SwingApplication();
		sapp.startup(DialogTestApplication.class.getName(), null, null);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new instance of <code>DialogTestApplication</code>.
	 * 
	 * @param pLauncher the UI launcher
	 */
	public DialogTestApplication(UILauncher pLauncher)
	{
		super(pLauncher);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public IComponent getDefaultComponent() throws Exception
	{
		return null;
	}

	@Override
	protected void showDefaultFrame()
	{
		UIFormLayout folContent = new UIFormLayout();
		
		UIPanel panContent = new UIPanel(folContent);
		panContent.add(new UILabel("Name"));
		panContent.add(new UITextField(), folContent.getConstraints(1, 0, -1, 0));
		panContent.add(new UILabel("Value"));
		panContent.add(new UITextField(), folContent.getConstraints(1, 1, -1, 1));
				
		UIButton butOK = new UIButton("OK");
		
		Dialog dlg = new Dialog(panContent);
		dlg.setButtonMode(ButtonMode.Custom);
		dlg.addRightButton(butOK);

		IInternalFrame iframe = Dialog.openInternalFrame(this, "Dialog test", true, dlg);

		butOK.eventAction().addListener(iframe, "dispose");
	}
	
}	// DialogTestApplication
