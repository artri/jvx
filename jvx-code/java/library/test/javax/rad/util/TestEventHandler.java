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
 * 24.09.2008 - [JR] - creation
 */
package javax.rad.util;

import java.io.Serializable;

import javax.rad.genui.UIComponent;
import javax.rad.ui.IComponent;
import javax.rad.ui.event.ActionHandler;
import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.ui.event.UIWindowEvent;
import javax.rad.ui.event.WindowHandler;
import javax.rad.ui.event.type.window.IWindowActivatedListener;
import javax.rad.ui.event.type.window.IWindowDeactivatedListener;

import org.junit.Assert;
import org.junit.Test;

import com.sibvisions.util.ArrayUtil;
import com.sibvisions.util.type.StringUtil;

/**
 * Tests the functionality of {@link EventHandler}.
 * 
 * @author René Jahn
 */
public class TestEventHandler implements Serializable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/** a simple count member. */
	private transient int iCount = 0;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * A simple action method.
	 */
	public void doAction()
	{
	}
	
	/**
	 * A simple action method.
	 */
	public void doAnotherAction()
	{
	}

	/**
	 * A simple event method.
	 */
	public void doWindowEvent()
	{
	}
	
	/**
	 * Actionmethod with super class.
	 * @param pComponent the component.
	 */
	public void doSomething1(IComponent pComponent)
	{
	}
	
	/**
	 * Actionmethod with sub class.
	 * @param pComponent the component.
	 */
	public void doSomething2(UIComponent<?> pComponent)
	{
	}

	/**
	 * Actionmethod with no class.
	 */
	public void doSomething3()
	{
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Test methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Tests the {@link EventHandler#addListener(Object)}, {@link EventHandler#removeListener(Object)} methods. 
	 */
	@Test
	public void testAddRemove()
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		// Test object with method.
		ehAction.addListener(this, "doAction");
		ehAction.removeListener(this);
		
		Assert.assertEquals(0, ehAction.getListeners().length);
		
		// Test interface listener.
		IActionListener listener = new CalledActionListener();
		
		ehAction.addListener(listener);
		ehAction.removeListener(listener);
		
		Assert.assertEquals(0, ehAction.getListeners().length);
		
		// Test IRunnable listener.
		IRunnable runnable = new CalledRunnable();
		
		ehAction.addListener(runnable);
		ehAction.removeListener(runnable);
		
		Assert.assertEquals(0, ehAction.getListeners().length);
	}
	
	/**
	 * Tests the {@link EventHandler#addListener(Object)}, {@link EventHandler#removeListener(Object)} methods. 
	 * On add remove of the proxy listener the proxy handler information is lost.
	 * see Ticket: 882
	 */
	@Test
	public void testAddRemoveAdvanced()
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		ehAction.addListener(this, "doAction");
		
		IActionListener[] listeners = ehAction.getListeners();
		ehAction.removeAllListeners();
		
		Assert.assertEquals(0, ehAction.getListeners().length);
		
		for (IActionListener listener : listeners)
		{
			ehAction.addListener(listener);
		}
		
		ehAction.removeListener(this);
		
		Assert.assertEquals(0, ehAction.getListeners().length);
	}
	
	/**
	 * Tests the {@link EventHandler#addListener(Object, String)} method.
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testAddObjectListener()
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		//throws an IllegalArgumentException
		ehAction.addListener(this, "doUnknownAction");
	}

	/**
	 * Tests the new add remove behavior.
	 */
	@Test
	public void testAdvancedAddRemoveListener()
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		ehAction.addListener(this, "doAction");
		
		IActionListener firstListener = ehAction.getListener(0);
		
		ehAction.addListener(this, "doAnotherAction");
		
		IActionListener secondListener = ehAction.getListener(1);
		
		ehAction.addListener(this, "doAnotherAction", 0);
		
		Assert.assertTrue(ehAction.getListenerCount() == 2 && ehAction.getListener(1) == firstListener);
		
		secondListener = ehAction.getListener(0);
		
		ehAction.removeListener(this, "doAction");
		
		Assert.assertTrue(ehAction.getListenerCount() == 1 && ehAction.getListener(0) == secondListener);
		
		ehAction.addListener(this, "doAction", 0);
		
		firstListener = ehAction.getListener(0);
		
		Assert.assertTrue(ehAction.getListenerCount() == 2 && ehAction.getListener(1) == secondListener);
		
		ehAction.removeListener(this);
		
		Assert.assertTrue(ehAction.getListenerCount() == 0);
	}
	
	/**
	 * Tests the default listener functionality.
	 * 
	 * @throws Throwable if dispatching the event failed. Shouldn't happen.
	 */
	@Test
	public void testDefaultListener() throws Throwable
	{
		EventHandler<IActionListener> eventHandler = new EventHandler<IActionListener>(IActionListener.class);
		
		CalledActionListener defaultListener = new CalledActionListener();
		
		eventHandler.setDefaultListener(defaultListener);
		
		eventHandler.dispatchEvent(new DummyActionEvent());
		
		Assert.assertTrue(defaultListener.isCalled());
		
		defaultListener.reset();
		
		CalledActionListener listener = new CalledActionListener();
		
		eventHandler.addListener(listener);
		
		eventHandler.dispatchEvent(new DummyActionEvent());
		
		Assert.assertFalse(defaultListener.isCalled());
		Assert.assertTrue(listener.isCalled());
	}

	/**
	 * Tests dispatching the events.
	 * 
	 * @throws Throwable if dispatching the event failed. Shouldn't happen.
	 */
	@Test
	public void testDispatch() throws Throwable
	{
		EventHandler<IActionListener> eventHandler = new EventHandler<IActionListener>(IActionListener.class);
		
		CalledActionListener actionListener = new CalledActionListener();
		CalledRunnable runnable = new CalledRunnable();
		CalledRunnable runnableB = new CalledRunnable();
		
		eventHandler.addListener(actionListener);
		eventHandler.addListener(runnable);
		eventHandler.addListener(runnableB, "run");
		
		eventHandler.dispatchEvent(new DummyActionEvent());
		
		Assert.assertTrue(actionListener.isCalled());
		Assert.assertTrue(runnable.isCalled());
		Assert.assertTrue(runnableB.isCalled());
	}
	
	/**
	 * Tests the {@link EventHandler#removeListener(int)} function.
	 */
	@Test
	public void testRemoveListenerByIndex()
	{
		EventHandler<IActionListener> eventHandler = new EventHandler<IActionListener>(IActionListener.class);
		
		IActionListener listenerA = new CalledActionListener();
		IActionListener listenerB = new CalledActionListener();
		IActionListener listenerC = new CalledActionListener();
		
		eventHandler.addListener(listenerA);
		eventHandler.addListener(listenerB);
		eventHandler.addListener(listenerC);
		
		Assert.assertEquals(3, eventHandler.getListenerCount());
		Assert.assertSame(listenerA, eventHandler.getListener(0));
		Assert.assertSame(listenerB, eventHandler.getListener(1));
		Assert.assertSame(listenerC, eventHandler.getListener(2));
		
		eventHandler.removeListener(1);
		
		Assert.assertEquals(2, eventHandler.getListenerCount());
		Assert.assertSame(listenerA, eventHandler.getListener(0));
		Assert.assertSame(listenerC, eventHandler.getListener(1));
		
		eventHandler.removeListener(1);
		
		Assert.assertEquals(1, eventHandler.getListenerCount());
		Assert.assertSame(listenerA, eventHandler.getListener(0));
		
		eventHandler.removeListener(0);
		
		Assert.assertEquals(0, eventHandler.getListenerCount());
	}
	
	/**
	 * Tests ticket 451.
	 */
	@Test
	public void testTicket451()
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		ehAction.addListener(new TestEventHandler(), "doAnotherAction");
		ehAction.addListener(this, "doAction");

		Assert.assertEquals(1, ArrayUtil.indexOf(ehAction.getListeners(), this));
	}
	
	/**
	 * Tests remove listeners.
	 */
	@Test
	public void testTicket451Second()
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		ehAction.addListener(new TestEventHandler(), "doAnotherAction");
		ehAction.addListener(this, "doAction");

		IActionListener[] listeners = ehAction.getListeners();
		
		for (int i = 0; i < listeners.length; i++)
		{
			ehAction.removeListener(listeners[i]);
		}
		
		Assert.assertEquals(0, ehAction.getListenerCount());
	}
	
	/**
	 * Tests en-/disable event dispatching.
	 * 
	 * @throws Throwable if dispatching an event fails
	 */
	@Test
	public void testTicket454() throws Throwable
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		iCount = 0;
		
		ehAction.addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				iCount++;
			}
		});
		
		UIActionEvent event = new UIActionEvent(null, UIActionEvent.ACTION_PERFORMED, System.currentTimeMillis(), 0, null);
		
		ehAction.dispatchEvent(event);
		
		Assert.assertEquals(1, iCount);
		
		ehAction.setDispatchEventsEnabled(false);

		try
		{
			ehAction.dispatchEvent(event);
			
			Assert.assertEquals(1, iCount);
		}
		finally
		{
			ehAction.setDispatchEventsEnabled(true);
		}
		
		ehAction.dispatchEvent(event);
		
		Assert.assertEquals(2, iCount);
	}
	
	/**
	 * Tests get dispatched object(s).
	 */
	@Test
	public void testGetDispatchedObject()
	{
		WindowHandler<IWindowActivatedListener> whActivated = new WindowHandler<IWindowActivatedListener>(IWindowActivatedListener.class);
		whActivated.addListener(this, "doWindowEvent");
		
		WindowHandler<IWindowDeactivatedListener> whDeactivated = new WindowHandler<IWindowDeactivatedListener>(IWindowDeactivatedListener.class);
		whDeactivated.addListener(this, "doWindowEvent");
		
		whActivated.dispatchEvent(new UIWindowEvent(null, UIWindowEvent.WINDOW_ACTIVATED, 0, 0));
		whDeactivated.dispatchEvent(new UIWindowEvent(null, UIWindowEvent.WINDOW_DEACTIVATED, 0, 0));
		
		Assert.assertNotNull(EventHandler.getLastDispatchedObject());
	}

	/**
	 * Tests get dispatched object(s).
	 */
	@Test
	public void testSimilarMethod()
	{
		EventHandler<TestListener> testHandler1 = new EventHandler<TestListener>(TestListener.class, "componentListener1");

		testHandler1.addListener(this, "doSomething1");
		testHandler1.addListener(this, "doSomething2");
		testHandler1.addListener(this, "doSomething3");

		EventHandler<TestListener> testHandler2 = new EventHandler<TestListener>(TestListener.class, "componentListener2");

		testHandler2.addListener(this, "doSomething1");
		try
		{
			testHandler2.addListener(this, "doSomething2");
			// Ok, Method with UIComponent should work with IComponent parameter in listener
		}
		catch (Exception ex)
		{
			Assert.fail("Method with UIComponent should work with IComponent parameter in listener");
		}
		
		testHandler2.addListener(this, "doSomething3");
	}

	/**
	 * Test null as parameter.
	 * 
	 * @throws Throwable if dispatching an event fails
	 */
	@Test
	public void testNullAsParameter() throws Throwable
	{
		EventHandler<IActionListener> ehAction = new EventHandler<IActionListener>(IActionListener.class);
		
		iCount = 0;
		
		ehAction.addListener(new IActionListener()
		{
			public void action(UIActionEvent pActionEvent)
			{
				iCount++;
			}
		});
		
		UIActionEvent event = new UIActionEvent(null, UIActionEvent.ACTION_PERFORMED, System.currentTimeMillis(), 0, null);
		
		ehAction.dispatchEvent(event);
		
		Assert.assertEquals(1, iCount);

		ehAction.dispatchEvent(null);
		
		Assert.assertEquals(2, iCount);
	}
	

	/**
	 * Tests user defined parameter types.
	 */
	@Test
	public void testUserDefinedParameterTypes()
	{
		TestListener testListener = new TestListener() 
		{
			public void componentListener4(Object... pObjects) 
			{
				iCount++;
				System.out.println("Called!!!! " + StringUtil.toString(pObjects));
			}
			public void componentListener3(String pComponent, Object... pObjects) 
			{
				iCount++;
				System.out.println("Called!!!! " + pComponent + "  " + StringUtil.toString(pObjects));
			}
			public void componentListener2(IComponent pComponent) 
			{
			}
			public void componentListener1(UIComponent pComponent) 
			{
			}
		};
		
		EventHandler test1 = new EventHandler(TestListener.class, "componentListener3", String.class);
		test1.addListener(testListener);
		test1.addListener(this, "doComponentListener3");
		
		iCount = 0;
		
		try 
		{
			test1.dispatchEvent("UILabel", "Hallo", Integer.valueOf(15));
			
			Assert.assertEquals(2, iCount);
		} 
		catch (AssertionError e)
		{
			// It was already an assert
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		try 
		{
			test1.dispatchEvent(null);
			
			Assert.assertEquals(4, iCount);
		} 
		catch (AssertionError e)
		{
			// It was already an assert
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		try 
		{
			test1.dispatchEvent(null, null);
			
			Assert.assertEquals(6, iCount);
		} 
		catch (AssertionError e)
		{
			// It was already an assert
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		try 
		{
			EventHandler test2 = new EventHandler(TestListener.class, "componentListener3", Integer.class);

			Assert.fail("Integer is not assignable to String");
		} 
		catch (Exception e) 
		{
			// As intended to be
		}

		try 
		{
			EventHandler test3 = new EventHandler(TestListener.class, "componentListener3", String.class, Integer.class);
		} 
		catch (Exception e) 
		{
			Assert.fail("It should be assignable!");
		}

		EventHandler test2 = new EventHandler(TestListener.class, "componentListener4", String.class);
		test2.addListener(testListener);
		test2.addListener(this, "doComponentListener4");
		
		try 
		{
			test2.dispatchEvent("UILabel", "Hallo", Integer.valueOf(15));
			
			Assert.assertEquals(8, iCount);
		} 
		catch (AssertionError e)
		{
			// It was already an assert
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		try 
		{
			test2.dispatchEvent();
			
			Assert.assertEquals(10, iCount);
		} 
		catch (AssertionError e)
		{
			// It was already an assert
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		try 
		{
			test2.dispatchEvent(null);
			
			Assert.assertEquals(12, iCount);
		} 
		catch (AssertionError e)
		{
			// It was already an assert
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		try 
		{
			test2.dispatchEvent(new Object[] {"Hallo Pferd!", "Hallo Esel!"});
			
			Assert.assertEquals(12, iCount);
		} 
		catch (AssertionError e)
		{
			// It was already an assert
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 * Tests the ability of {@link EventHandler} to handle primitive parametes.
	 */
	@Test
	public void testPrimitiveParameter()
	{
		CallableHandler handler = new CallableHandler(boolean.class);
		
		iCount = 0;
		
		handler.addListener(this, "doPrimitiveParameterListener");
		handler.dispatchEvent(true);
		
		if (iCount != 1)
		{
			Assert.fail("Primitive handler was not called!");
		}

		// Test also Ticket 450, parameter search like Reflective.
		handler = new CallableHandler(Boolean.class);
		
		iCount = 0;
		
		handler.addListener(this, "doPrimitiveParameterListener");
		handler.dispatchEvent(true);
		
		if (iCount != 1)
		{
			Assert.fail("Primitive handler was not called!");
		}
	}

    /**
     * Tests the ability of {@link EventHandler} to handle primitive parametes.
     */
    @Test
    public void testExtractInvokeMethod()
    {
        ActionHandler handler = new ActionHandler();

        IActionListener listener1 = handler.createListener(this, "doAction");
        IRunnable listener2 = this::doAction;
        IActionListener listener3 = handler.createListener(listener2, "run");

        System.out.println(listener1);
        System.out.println(listener2);
        System.out.println(listener3);
        
        Assert.assertEquals("doAction", EventHandler.extractInvokedMethodName(listener1));
        Assert.assertEquals("doAction", EventHandler.extractInvokedMethodName(listener2));
        Assert.assertEquals("doAction", EventHandler.extractInvokedMethodName(listener3));
    }
	
	/**
	 * Test var args.
	 * @param pObjects the parameter.
	 */
	public void doComponentListener4(Object... pObjects) 
	{
		iCount++;
		System.out.println("Called!!!! " + StringUtil.toString(pObjects));
	}
	/**
	 * Test var args.
	 * @param pComponent the parameter.
	 * @param pObjects the parameter.
	 */
	public void doComponentListener3(String pComponent, Object... pObjects) 
	{
		iCount++;
		System.out.println("Called!!!! " + pComponent + "  " + StringUtil.toString(pObjects));
	}
	
	/**
	 * Tests a primitive parameter.
	 * 
	 * @param pValue the primitive parameter.
	 */
	public void doPrimitiveParameterListener(boolean pValue)
	{
		iCount++;
	}
	
	//****************************************************************
	// Subinterface definition
	//****************************************************************
	
	/**
	 * The TestListener.
	 */
	public static interface TestListener
	{
		/**
		 * The test method.
		 * @param pComponent the parameter.
		 */
		public void componentListener1(UIComponent pComponent);

		/**
		 * The test method.
		 * @param pComponent the parameter.
		 */
		public void componentListener2(IComponent pComponent);

		/**
		 * The test method.
		 * @param pComponent the parameter.
		 * @param pObjects the parameter.
		 */
		public void componentListener3(String pComponent, Object... pObjects);

		/**
		 * The test method.
		 * @param pObjects the parameter.
		 */
		public void componentListener4(Object... pObjects);
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * A simple abstract base class for classes which need to know of they've
	 * been called or not.
	 * 
	 * @author Robert Zenz
	 */
	private abstract static class AbstractCalled
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Class members
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** If the class has been called. */
		protected boolean called = false;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// User-defined methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Gets if this class has been called.
		 * 
		 * @return {@code true} if this class has been called.
		 */
		public boolean isCalled()
		{
			return called;
		}
		
		/**
		 * Resets if this class has been called.
		 */
		public void reset()
		{
			called = false;
		}
		
	}	// AbstractCalled
	
	/**
	 * {@link CalledActionListener} is an {@link IActionListener} implementation
	 * which is aware if it was called or not.
	 * 
	 * @author Robert Zenz
	 */
	private static final class CalledActionListener extends AbstractCalled implements IActionListener
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void action(UIActionEvent pActionEvent) throws Throwable
		{
			called = true;
		}
		
	}	// CalledActionListener
	
	/**
	 * {@link CalledRunnable} is an {@link IRunnable} implementation which is
	 * aware if it was called or not.
	 * 
	 * @author Robert Zenz
	 */
	private static final class CalledRunnable extends AbstractCalled implements IRunnable
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Interface implementation
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		public void run() throws Throwable
		{
			called = true;
		}
		
	}	// CalledRunnable
	
	/**
	 * The {@link DummyActionEvent} is a dummy extension of {@link UIActionEvent}.
	 * 
	 * @author Robert Zenz
	 */
	private static final class DummyActionEvent extends UIActionEvent
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link DummyActionEvent}.
		 */
		public DummyActionEvent()
		{
			super(null, 0, 0, 0, "dummy");
		}

		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void checkId(int pId)
		{
			// Do nothing.
		}
		
	}	// DummyActionEvent
	
}	// TestEventHandler
