/* Copyright 2010 - 2011 by Brian Uri!
   
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
	
	/**
	 * Resets the in-use version of DDMS.
	 */
	protected void setUp() throws Exception {
		DDMSVersion.clearCurrentVersion();
	}
	
	/**
	 * Resets the in-use version of DDMS.
	 */
	protected void tearDown() throws Exception {
		DDMSVersion.clearCurrentVersion();
	}
	
	public void testGetVersionForInvalid() {
		try {
			DDMSVersion.getVersionFor("1.4");
			fail("Allowed unsupported version.");
		}
		catch (UnsupportedVersionException e) {
			// Good
		}
	}
	
	public void testGetVersionForDDMSNamespace() {
		assertEquals(DDMSVersion.getVersionFor("3.0"),
			DDMSVersion.getVersionForDDMSNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/"));
		try {
			DDMSVersion.getVersionForDDMSNamespace("http://metadata.dod.mil/mdr/ns/DDMS/1.4/");
			fail("Allowed unsupported version.");
		} catch (UnsupportedVersionException e) {
			// Good
		}
	}
	
	public void testGetSupportedVersions() {
		assertFalse(DDMSVersion.getSupportedVersions().isEmpty());
		assertTrue(DDMSVersion.getSupportedVersions().contains("3.0"));
	}
	
	public void testIsSupportedXmlNamespace() {
		assertTrue(DDMSVersion.isSupportedDDMSNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/"));
		assertFalse(DDMSVersion.isSupportedDDMSNamespace("http://metadata.dod.mil/mdr/ns/DDMS/1.4/"));
	}
	
	public void testGetCurrentSchema() {
		assertEquals("/schemas/3.1/DDMS/DDMS-v3_1.xsd", DDMSVersion.getCurrentVersion().getSchema());
	}
	
	public void testGetCurrentNamespace() {
		assertEquals("http://metadata.dod.mil/mdr/ns/DDMS/3.1/", DDMSVersion.getCurrentVersion().getNamespace());
	}
	
	public void testGetNamespaceForValid() {
		DDMSVersion.setCurrentVersion("2.0");
		assertEquals("http://metadata.dod.mil/mdr/ns/DDMS/2.0/", DDMSVersion.getCurrentVersion().getNamespace());
	}
	
	public void testSetCurrentVersionInvalid() {
		try {
			DDMSVersion.setCurrentVersion("1.4");
			fail("Allowed unsupported version.");
		}
		catch (UnsupportedVersionException e) {
			// Good
		}
	}
	
	public void testGetSchemaForValid() {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		assertEquals("/schemas/2.0/DDMS/DDMS-v2_0.xsd", DDMSVersion.getCurrentVersion().getSchema());
		assertEquals("2.0", version.getVersion());
	}

	public void testToString() {
		assertEquals(DDMSVersion.getCurrentVersion().getVersion(), DDMSVersion.getCurrentVersion().toString());
	}
	
	public void testAccessors() {
		DDMSVersion.setCurrentVersion("3.0");
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		assertEquals("3.0", version.getVersion());
		assertEquals("http://metadata.dod.mil/mdr/ns/DDMS/3.0/", version.getNamespace());
		assertEquals("/schemas/3.0/DDMS/DDMS-v3_0.xsd", version.getSchema());
		assertEquals("http://www.opengis.net/gml/3.2", version.getGmlNamespace());
		assertEquals("/schemas/3.0/DDMS/DDMS-GML-Profile.xsd", version.getGmlSchema());
		assertEquals("urn:us:gov:ic:ism", version.getIsmNamespace());
		assertEquals("/schemas/3.0/ISM/CVE/", version.getIsmCveLocation());
	}
	
	public void testAliasVersion() {
		DDMSVersion.setCurrentVersion("3.0.1");
		assertEquals("3.0", DDMSVersion.getCurrentVersion().getVersion());		
		assertEquals("3.0", DDMSVersion.getVersionFor("3.0.1").getVersion());
		assertTrue(DDMSVersion.getCurrentVersion().getVersion().equals("3.0"));
	}
	
	public void testIsNewerThan() {
		assertTrue(DDMSVersion.getVersionFor("2.0").isAtLeast("2.0"));
		assertFalse(DDMSVersion.getVersionFor("2.0").isAtLeast("3.0"));
		assertFalse(DDMSVersion.getVersionFor("2.0").isAtLeast("3.0.1"));
		assertFalse(DDMSVersion.getVersionFor("2.0").isAtLeast("3.1"));
		assertFalse(DDMSVersion.getVersionFor("2.0").isAtLeast("4.0"));
		
		assertTrue(DDMSVersion.getVersionFor("3.0").isAtLeast("2.0"));
		assertTrue(DDMSVersion.getVersionFor("3.0").isAtLeast("3.0"));
		assertTrue(DDMSVersion.getVersionFor("3.0").isAtLeast("3.0.1"));
		assertFalse(DDMSVersion.getVersionFor("3.0").isAtLeast("3.1"));
		assertFalse(DDMSVersion.getVersionFor("3.0").isAtLeast("4.0"));
		
		assertTrue(DDMSVersion.getVersionFor("3.1").isAtLeast("2.0"));
		assertTrue(DDMSVersion.getVersionFor("3.1").isAtLeast("3.0"));
		assertTrue(DDMSVersion.getVersionFor("3.0").isAtLeast("3.0.1"));
		assertTrue(DDMSVersion.getVersionFor("3.1").isAtLeast("3.1"));
		assertFalse(DDMSVersion.getVersionFor("3.1").isAtLeast("4.0"));
		
		assertTrue(DDMSVersion.getVersionFor("4.0").isAtLeast("2.0"));
		assertTrue(DDMSVersion.getVersionFor("4.0").isAtLeast("3.0"));
		assertTrue(DDMSVersion.getVersionFor("3.0").isAtLeast("3.0.1"));
		assertTrue(DDMSVersion.getVersionFor("4.0").isAtLeast("3.1"));
		assertTrue(DDMSVersion.getVersionFor("4.0").isAtLeast("4.0"));
		
		try {
			DDMSVersion.getCurrentVersion().isAtLeast("dog");
			fail("Allowed invalid data.");
		}
		catch (UnsupportedVersionException e) {
			// Good
		}
	}
}
