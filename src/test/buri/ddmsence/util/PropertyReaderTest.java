/* Copyright 2010 - 2016 by Brian Uri!
   
   This file is part of DDMSence.
   
   This library is free software; you can redistribute it and/or modify
   it under the terms of version 3.0 of the GNU Lesser General Public 
   License as published by the Free Software Foundation.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
   GNU Lesser General Public License for more details.
   
   You should have received a copy of the GNU Lesser General Public 
   License along with DDMSence. If not, see <http://www.gnu.org/licenses/>.

   You can contact the author at ddmsence@urizone.net. The DDMSence
   home page is located at https://ddmsence.urizone.net/
 */
package buri.ddmsence.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.AccessorRunnable;

/**
 * A collection of PropertyReader tests.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PropertyReaderTest extends AbstractBaseTestCase {

	public PropertyReaderTest() {
		super(null);
	}

	/**
	 * Resets the in-use prefix for DDMS.
	 */
	@Before
	public void setUp() throws Exception {
		PropertyReader.setProperty("ddms.prefix", "ddms");
	}

	/**
	 * Resets the in-use prefix for DDMS.
	 */
	@After
	public void tearDown() throws Exception {
		PropertyReader.setProperty("ddms.prefix", "ddms");
	}

	@Test
	public void testGetPropertyInvalid() {
		try {
			PropertyReader.getProperty("unknown.property");
			fail("Did not prevent invalid property.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "Undefined Property");
		}
	}

	@Test
	public void testGetListPropertyValid() {
		List<String> properties = PropertyReader.getListProperty("ddms.supportedVersions");
		assertEquals(5, properties.size());
	}

	@Test
	public void testSetPropertyInvalidName() {
		// This also handles unconfigurable properties.
		try {
			PropertyReader.setProperty("unknown.property", "value");
			fail("Did not prevent invalid property name.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "unknown.property is not a configurable property.");
		}
	}

	@Test
	public void testSetPropertyValid() {
		PropertyReader.setProperty("ddms.prefix", "DDMS");
		assertEquals("DDMS", PropertyReader.getPrefix("ddms"));
	}
		
	@Test
	public void testMultithreaded() throws InterruptedException {
		List<Thread> threads = new ArrayList<Thread>();
		List<AccessorRunnable> runnables = new ArrayList<AccessorRunnable>();
		for (int i = 0; i < AccessorRunnable.NUM_THREADS; i++) {
			runnables.add(new PropertyReaderRunnable(i));
			threads.add(new Thread(runnables.get(i), runnables.get(i).getThreadName()));
			threads.get(i).start();
		}
		for (int i = 0; i < AccessorRunnable.NUM_THREADS; i++) {
			threads.get(i).join();
		}
		
		int numFailures = 0;
		for (int i = 0; i < AccessorRunnable.NUM_THREADS; i++) {
			if (!runnables.get(i).getMatch())
				numFailures++;
		}
		if (numFailures > 0) {
			fail(numFailures + " threads contained an incorrect property value.");
		}
	}
	
	/**
	 * Helper class to test PropertyReader in multiple threads.
	 */
	public static class PropertyReaderRunnable extends AccessorRunnable {

		public PropertyReaderRunnable(int threadNum) {
			super("Thread" + threadNum);
		}
		
		@Override
		public String getExpectedValue() {
			return (getThreadName());
		}
		
		@Override
		public void callSet() {
			PropertyReader.setProperty("ddms.prefix", getThreadName());
		}
		
		@Override
		public String callGet() {
			return (PropertyReader.getProperty("ddms.prefix"));
		}
	}
}
