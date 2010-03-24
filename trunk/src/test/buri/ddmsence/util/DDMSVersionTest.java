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

import junit.framework.TestCase;
import buri.ddmsence.ddms.UnsupportedVersionException;

/**
 * A collection of DDMSVersion tests.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DDMSVersionTest extends TestCase {
	
	public void testGetSupportedVersions() {
		assertFalse(DDMSVersion.getSupportedVersions().isEmpty());
		assertTrue(DDMSVersion.getSupportedVersions().contains(DDMSVersion.DEFAULT_VERSION));
	}
	
	public void testIsSupportedVersionSuccess() {
		assertTrue(DDMSVersion.isSupported(DDMSVersion.DEFAULT_VERSION));
	}
	
	public void testIsSupportedVersionFailure() {
		assertFalse(DDMSVersion.isSupported("99"));
	}
	
	public void testGetDefaultSchema() {
		assertEquals("/schemas/3.0/DDMS/3.0/DDMS-v3_0.xsd", DDMSVersion.getDefaultSchema());
	}
	
	public void testGetDefaultNamespace() {
		assertEquals("http://metadata.dod.mil/mdr/ns/DDMS/3.0/", DDMSVersion.getDefaultNamespace());
	}
	
	public void testGetNamespaceForValid() {
		assertEquals("http://metadata.dod.mil/mdr/ns/DDMS/3.0/", DDMSVersion.getNamespaceFor("3.0"));
	}
	
	public void testGetNamespaceForInvalid() {
		try {
			DDMSVersion.getNamespaceFor("99");
			fail("Allowed unsupported version.");
		}
		catch (UnsupportedVersionException e) {
			// Good
		}
	}
	
	public void testGetSchemaForValid() {
		assertEquals("/schemas/3.0/DDMS/3.0/DDMS-v3_0.xsd", DDMSVersion.getSchemaFor("3.0"));
	}
	
	public void testGetSchemaForInvalid() {
		try {
			DDMSVersion.getSchemaFor("99");
			fail("Allowed unsupported version.");
		}
		catch (UnsupportedVersionException e) {
			// Good
		}
	}
}
