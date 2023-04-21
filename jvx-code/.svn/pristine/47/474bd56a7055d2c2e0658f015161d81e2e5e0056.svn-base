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
 * 04.10.2022 - [JR] - creation
 */
package com.sibvisions.rad.server.security.reset;



import org.junit.Test;

import com.sibvisions.rad.server.PreconfiguredSession;
import com.sibvisions.rad.server.security.UserInfo;
import com.sibvisions.util.xml.XmlNode;

/**
 * Tests functionality of {@link DefaultOneTimePasswordHandler}.
 * 
 * @author René Jahn
 */
public class TestDefaultOneTimePasswordHandler 
{
	/**
	 * Tests sending password.
	 */
	@Test
	public void sendPassword() throws Exception
	{
		XmlNode ndEmail = new XmlNode("smtp");
		ndEmail.setNode("host", "mail.sibvisions.org");
		ndEmail.setNode("port", "587");
		ndEmail.setNode("username", "");
		ndEmail.setNode("password", "");
		ndEmail.setNode("tlsenabled", "true");
		ndEmail.setNode("defaultFrom", "SIB Visions <demo@sibvisions.org>");
		
		PreconfiguredSession session = new PreconfiguredSession();
		session.setNodeValue("/application/mail/smtp", ndEmail);
		
		UserInfo user = new UserInfo();
		user.setUserName("JVx");
		user.setEmailAddress("jvx@sibvisions.com");
		
		DefaultOneTimePasswordHandler pwh = new DefaultOneTimePasswordHandler();
		pwh.sendOneTimePassword(session, user, "Pwd12345");
	}
	
}	// TestDefaultOneTimePasswordHandler
