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
}
