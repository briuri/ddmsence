/* Copyright 2010 by Brian Uri!
   
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
   home page is located at http://ddmsence.urizone.net/
*/
package buri.ddmsence.util;

import java.util.List;

import junit.framework.TestCase;

/**
 * A collection of PropertyReader tests.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PropertyReaderTest extends TestCase {

	/**
	 * Resets the in-use prefix for DDMS.
	 */
	protected void setUp() throws Exception {
		PropertyReader.setProperty("ddms.prefix", "ddms");
	}

	/**
	 * Resets the in-use prefix for DDMS.
	 */
	protected void tearDown() throws Exception {
		PropertyReader.setProperty("ddms.prefix", "ddms");
	}
	
	public void testGetPropertyInvalid() {
		try {
			PropertyReader.getProperty("unknown.property");
			fail("Did not prevent invalid property.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testGetListPropertyValid() {
		List<String> properties = PropertyReader.getListProperty("ddms.supportedVersions");
		assertEquals(2, properties.size());
	}
	
	public void testSetPropertyInvalidName() {
		// This also handles unconfigurable properties.
		try {
			PropertyReader.setProperty("unknown.property", "value");
			fail("Did not prevent invalid property name.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testSetPropertyInvalidValue() {
		try {
			PropertyReader.setProperty("ddms.prefix", "");
			fail("Did not prevent invalid property value.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testSetPropertyValid() {
		PropertyReader.setProperty("ddms.prefix", "DDMS");
		assertEquals("DDMS", PropertyReader.getProperty("ddms.prefix"));
	}
}
