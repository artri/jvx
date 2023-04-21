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
 * 02.04.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.mfa.auth;

import javax.rad.server.ISession;

import com.sibvisions.rad.remote.mfa.IMFAConstants;
import com.sibvisions.rad.remote.mfa.MFAException;
import com.sibvisions.rad.server.security.AbstractSecurity;
import com.sibvisions.rad.server.security.UserInfo;
import com.sibvisions.rad.server.security.mfa.IMFAuthenticator;
import com.sibvisions.rad.server.security.mfa.MFAHandler;
import com.sibvisions.rad.server.security.mfa.MFAHandler.AccessToken;
import com.sibvisions.util.xml.XmlNode;

/**
 * The <code>WaitMFAuthenticator</code> is a multi-factor authentication implementation for
 * a wait process. It's necessary to confirm the authentication with a different mechanism.
 * 
 * @author René Jahn
 */
public abstract class AbstractURLMFAuthenticator extends AbstractSecurity
                                                 implements IMFAuthenticator
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * {@inheritDoc}
	 */
	public void init(ISession pSession, UserInfo pUser) 
	{
		AccessToken token = MFAHandler.createToken(pSession, TYPE_URL);
		
		if (token != null)
		{
			sendLink(token, pSession, pUser);
			
			try
			{
				MFAHandler.init(token);
			}
			catch (SecurityException se)
			{
				throw (SecurityException)prepareException(se);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean validate(ISession pSession) 
	{
		AccessToken token;
		
		try
		{
			token = MFAHandler.getToken(pSession);
		}
		catch (SecurityException se)
		{
			throw (SecurityException)prepareException(se);
		}
		
		if (token != null)
		{
			checkConfirmation(token, pSession);
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Creates a new {@link Link}.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 * @param pUser the user information
	 * @return the URL
	 */
	protected abstract Link createLink(AccessToken pToken, ISession pSession, UserInfo pUser);
	
	/**
	 * Gets whether the confirmation is finished.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 * @return <code>true</code> if confirmation is successful, <code>false</code> otherwise
	 */
	protected abstract boolean isConfirmed(AccessToken pToken, ISession pSession);

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Sends a newly created link.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 * @param pUser the user information
	 */
	private void sendLink(AccessToken pToken, ISession pSession, UserInfo pUser)
	{
		Link link = createLink(pToken, pSession, pUser);

		if (link != null)
		{
			pToken.setAttribute(IMFAConstants.PROP_PAYLOAD, link.toString());
		}
		
		pToken.put("LINK", link);
	}
	
	/**
	 * Checks if authentication is confirmed.
	 * 
	 * @param pToken the access token
	 * @param pSession the session
	 */
	private void checkConfirmation(AccessToken pToken, ISession pSession) 
	{
		if (isConfirmed(pToken, pSession))
		{
			MFAHandler.destroy(pToken);

			return;
		}
		
		MFAException mfae = new MFAException();
		mfae.setToken(pToken.getIdentifier());
		mfae.setType(MFAException.TYPE_URL);
		mfae.setState(MFAException.STATE_RETRY);
				
		throw mfae;
	}
	
	//****************************************************************
    // Subclass definition
    //****************************************************************
	
	/**
	 * The <code>Link</code> class is a simple object for web link definition.
	 * 
	 * @author René Jahn
	 */
	public static final class Link
	{
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Class members
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** the targets. */
		public enum Target
		{
			/** _self. */
			Self,
			/** _blank. */
			Blank
		};
		
		/** the internal node. */
		private XmlNode node;
		
		/** the URL. */
		private String sUrl;
		/** the target. */
		private Target target;
		
		/** the width. */
		private int width = -1;
		/** the height. */
		private int height = -1;

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Initialization
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		/**
		 * Creates a new instance of <code>Link</code>.
		 */
		public Link()
		{
			node = new XmlNode("xml");
		}
		
		/**
		 * Creates a new instance of <code>Link</code> for an URL.
		 * 
		 * @param pUrl the URL
		 */
		public Link(String pUrl)
		{
			this();
			
			setUrl(pUrl);
		}
		
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // Overwritten methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			if (target == null
				&& width < 0
				&& height < 0)
			{
				return sUrl;
			}
			else
			{
				return node.toString();
			}
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	    // User-defined methods
	    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Sets the URL.
		 * 
		 * @param pUrl the URL
		 */
		public void setUrl(String pUrl)
		{
			sUrl = pUrl;

			if (pUrl == null)
			{
				node.removeNode("/url");
			}
			else
			{
				node.setNode("/url", pUrl);
			}
		}
		
		/**
		 * Gets the URL.
		 * 
		 * @return the URL
		 */
		public String getUrl()
		{
			return sUrl;
		}
		
		/**
		 * Sets the target.
		 * 
		 * @param pTarget the target
		 */
		public void setTarget(Target pTarget)
		{
			target = pTarget;
			
			if (pTarget == null)
			{
				node.removeNode("target");
			}
			else
			{
				switch (pTarget)
				{
					case Self:
						node.setNode("target", "_self");
						break;
					case Blank:
						node.setNode("target", "_blank");
						break;
					default:
						throw new IllegalArgumentException("Target not supported: " + pTarget);
				}
			}
		}
		
		/**
		 * Gets the target.
		 * 
		 * @return the target
		 */
		public Target getTarget()
		{
			return target;
		}
		
		/**
		 * Sets the width.
		 * 
		 * @param pWidth the width
		 */
		public void setWidth(int pWidth)
		{
			width = pWidth;
			
			if (pWidth < 0)
			{
				node.removeNode("width");
			}
			else
			{
				node.setNode("width", Integer.valueOf(pWidth));
			}
		}

		/**
		 * Gets the width.
		 * 
		 * @return the width
		 */
		public int getWidth()
		{
			return width;
		}

		/**
		 * Sets the height.
		 * 
		 * @param pHeight the height
		 */
		public void setHeight(int pHeight)
		{
			height = pHeight;
			
			if (pHeight < 0)
			{
				node.removeNode("height");
			}
			else
			{
				node.setNode("height", Integer.valueOf(pHeight));
			}
		}
		
		/**
		 * Gets the height.
		 * 
		 * @return the height
		 */
		public int getHeight()
		{
			return height;
		}		
		
	}	// Link

}	// WaitMFAuthenticator
