/*
 * Copyright 2022 SIB Visions GmbH
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
 * 29.03.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa;

import java.util.Map.Entry;

import javax.rad.remote.IConnectionConstants;
import javax.rad.remote.MasterConnection;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.rad.remote.mfa.IMFAConstants;
import com.sibvisions.rad.remote.mfa.MFAException;
import com.sibvisions.rad.server.DirectServerConnection;
import com.sibvisions.util.ThreadHandler;

/**
 * Test the functionality of {@link MFASecurityManager}.
 * 
 * @author René Jahn
 */
public class TestMFASecurityManager
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** the wait authorization check thread. */
	private Thread thWaitCheck;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Tests authentication.
	 * 
	 * @throws Throwable if the bug is still present
	 */
	@Test
	public void testAuthentication() throws Throwable
	{
		MasterConnection macon = new MasterConnection(new DirectServerConnection());
		
		macon.setApplicationName("mfa");
		macon.setUserName("admin");
		macon.setPassword("admin");
		
		try
		{
			macon.open();
			
			Assert.fail("Multi-factor authentication not initialized!");
		}
		catch (MFAException mfae)
		{
			//CHECK for init
			Object oToken = mfae.getToken();
			
			Assert.assertNotNull("Token not set!", oToken);
			
			int iState = mfae.getState();
			
			Assert.assertEquals(IMFAConstants.STATE_INIT, iState);
			
			int iType = mfae.getType();
			
			Assert.assertEquals(IMFAConstants.TYPE_TEXTINPUT, iType);
			
			macon.setProperty(IConnectionConstants.MFA_TOKEN, oToken);
			
			// send back "communication properties"
			for (Entry<String, Object> props : mfae.properties())
			{
				macon.setProperty(IConnectionConstants.PREFIX_MFA_PROPERTY + props.getKey(), props.getValue());
			}
			
			macon.setProperty(IConnectionConstants.MFA_PAYLOAD, "JVx123");
			
			macon.open();
			macon.close();
		}
	}
	
	/**
	 * Tests authentication.
	 * 
	 * @throws Throwable if the bug is still present
	 */
	@Test
	public void testAuthenticationWithWait() throws Throwable
	{
		final MasterConnection macon = new MasterConnection(new DirectServerConnection());
		
		macon.setApplicationName("mfa_wait");
		macon.setUserName("admin");
		macon.setPassword("admin");
		
		try
		{
			macon.open();
			
			Assert.fail("Multi-factor authentication not initialized!");
		}
		catch (final MFAException mfae)
		{
			//CHECK for init
			Object oToken = mfae.getToken();
			
			Assert.assertNotNull("Token not set!", oToken);
			
			int iState = mfae.getState();
			
			Assert.assertEquals(IMFAConstants.STATE_INIT, iState);
			
			int iType = mfae.getType();
			
			Assert.assertEquals(IMFAConstants.TYPE_WAIT, iType);
			
			macon.setProperty(IConnectionConstants.MFA_TOKEN, oToken);
			macon.setProperty(IConnectionConstants.MFA_PAYLOAD, mfae.get(IMFAConstants.PROP_PAYLOAD));
			
			// send back "communication properties"
			for (Entry<String, Object> props : mfae.properties())
			{
				macon.setProperty(IConnectionConstants.PREFIX_MFA_PROPERTY + props.getKey(), props.getValue());
			}
			
			thWaitCheck = ThreadHandler.start(new Runnable() 
			{
				@Override
				public void run() 
				{
					try
					{
						while (thWaitCheck == Thread.currentThread() && !macon.isOpen())
						{
							Thread.sleep(1500);
							
							try
							{
								macon.open();
							}
							catch (MFAException mfaex)
							{
								Assert.assertEquals(mfae.getToken(), mfaex.getToken());
								Assert.assertEquals(IMFAConstants.TYPE_WAIT, mfaex.getType());
								Assert.assertEquals(IMFAConstants.STATE_RETRY, mfaex.getState());
							}
							catch (Throwable th)
							{
								th.printStackTrace();
								
								thWaitCheck = ThreadHandler.stop(thWaitCheck);
							}
						}
					}
					catch (InterruptedException ie)
					{
						//done
					}
					
					synchronized(TestMFASecurityManager.this)
					{
						TestMFASecurityManager.this.notifyAll();
					}
				}
			});
			
			synchronized(this)
			{
				wait(mfae.getTimeout());
			}
			
			ThreadHandler.stop(thWaitCheck);
			
			if (!macon.isOpen())
			{
				Assert.fail("MFA with wait didn't work!");
			}
			else
			{
				macon.close();
			}
		}
	}	

	/**
	 * Tests authentication with multi wait.
	 * 
	 * @throws Throwable if the bug is still present
	 */
	@Test
	public void testMultiWait() throws Throwable
	{
		final MasterConnection macon = new MasterConnection(new DirectServerConnection());
		
		macon.setApplicationName("mfa_multiwait");
		macon.setUserName("admin");
		macon.setPassword("admin");
		
		try
		{
			macon.open();
			
			Assert.fail("Multi-factor authentication not initialized!");
		}
		catch (final MFAException mfae)
		{
			//CHECK for init
			Object oToken = mfae.getToken();
			
			Assert.assertNotNull("Token not set!", oToken);
			
			int iState = mfae.getState();
			
			Assert.assertEquals(IMFAConstants.STATE_INIT, iState);
			
			int iType = mfae.getType();
			
			Assert.assertEquals(IMFAConstants.TYPE_WAIT, iType);
			
			macon.setProperty(IConnectionConstants.MFA_TOKEN, oToken);
			macon.setProperty(IConnectionConstants.MFA_PAYLOAD, mfae.get(IMFAConstants.PROP_PAYLOAD));
			
			// send back "communication properties"
			for (Entry<String, Object> props : mfae.properties())
			{
				macon.setProperty(IConnectionConstants.PREFIX_MFA_PROPERTY + props.getKey(), props.getValue());
			}

			thWaitCheck = ThreadHandler.start(new Runnable() 
			{
				@Override
				public void run() 
				{
					try
					{
						while (thWaitCheck == Thread.currentThread() && !macon.isOpen())
						{
							Thread.sleep(1500);
							
							try
							{
								macon.open();
							}
							catch (MFAException mfaex)
							{
								Assert.assertEquals(mfae.getToken(), mfaex.getToken());
								
								Assert.assertEquals(IMFAConstants.TYPE_WAIT, mfaex.getType());

								System.out.println("Received MFA state: " + mfaex.getState() + 
										           " and payload: " + mfaex.get(IMFAConstants.PROP_PAYLOAD));								
								
								if (mfaex.getState() == IMFAConstants.STATE_INIT)
								{
									try
									{
										macon.setProperty(IConnectionConstants.MFA_PAYLOAD, mfaex.get(IMFAConstants.PROP_PAYLOAD));
									}
									catch (Throwable th)
									{
										th.printStackTrace();
									}
								}
								else if (mfaex.getState() != IMFAConstants.STATE_RETRY)
								{
									Assert.fail("Wrong MFA state!");
								}
							}
							catch (Throwable th)
							{
								th.printStackTrace();
								
								thWaitCheck = ThreadHandler.stop(thWaitCheck);
							}
						}
					}
					catch (InterruptedException ie)
					{
						//done
					}
					
					synchronized(TestMFASecurityManager.this)
					{
						TestMFASecurityManager.this.notifyAll();
					}
				}
			});
			
			synchronized(this)
			{
				wait(mfae.getTimeout());
			}
			
			ThreadHandler.stop(thWaitCheck);
			
			if (!macon.isOpen())
			{
				Assert.fail("MFA with wait didn't work!");
			}
			else
			{
				macon.close();
			}
		}
	}	
	
}	// TestMFASecurityManager
