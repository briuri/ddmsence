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
import nu.xom.Element;
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
	
	public void testIsVersion() {
		Element element = Util.buildDDMSElement("test", null);
		assertTrue(DDMSVersion.isVersion("3.1", element));
		assertFalse(DDMSVersion.isVersion("3.0.1", element));
		assertFalse(DDMSVersion.isVersion("3.0", element));
		assertFalse(DDMSVersion.isVersion("2.0", element));
	}
	
	public void testGetVersionFor() {
		assertEquals("2.0", 
			DDMSVersion.getVersionForNamespace("http://metadata.dod.mil/mdr/ns/DDMS/2.0/").getVersion());
		assertFalse("3.0".equals( 
			DDMSVersion.getVersionForNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/").getVersion()));
		assertFalse("3.0.1".equals( 
			DDMSVersion.getVersionForNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/").getVersion()));
		assertEquals("3.1", 
			DDMSVersion.getVersionForNamespace("http://metadata.dod.mil/mdr/ns/DDMS/3.0/").getVersion());
		assertEquals(null, DDMSVersion.getVersionForNamespace("TEST"));
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
	
	public void testGetSupportedVersions() {
		assertFalse(DDMSVersion.getSupportedVersions().isEmpty());
		assertTrue(DDMSVersion.getSupportedVersions().contains("3.0"));
	}
	
	public void testGetCurrentSchema() {
		assertEquals("/schemas/3.1/DDMS-v3_1.xsd", DDMSVersion.getCurrentVersion().getSchema());
	}
	
	public void testGetCurrentNamespace() {
		assertEquals("http://metadata.dod.mil/mdr/ns/DDMS/3.0/", DDMSVersion.getCurrentVersion().getNamespace());
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
		DDMSVersion.setCurrentVersion("2.0");
		assertEquals("/schemas/2.0/DDMS-v2_0.xsd", DDMSVersion.getCurrentVersion().getSchema());
	}
	
	public void testGmlNamespaceInvalid() {
		assertNull(DDMSVersion.getVersionForGmlNamespace("unknown"));
	}
	
	public void testGmlNamespaceValid() {
		assertEquals("2.0", DDMSVersion.getVersionForGmlNamespace("http://www.opengis.net/gml").getVersion());
	}
	
	public void testIsCurrentVersion() {
		DDMSVersion.setCurrentVersion("2.0");
		assertTrue(DDMSVersion.isCurrentVersion("2.0"));
		assertFalse(DDMSVersion.isCurrentVersion("3.0"));
	}
	public void testToString() {
		assertEquals(DDMSVersion.getCurrentVersion().getVersion(), DDMSVersion.getCurrentVersion().toString());
	}
	
	public void testAccessors() {
		DDMSVersion.setCurrentVersion("3.0");
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		assertEquals("3.0", version.getVersion());
		assertEquals("http://metadata.dod.mil/mdr/ns/DDMS/3.0/", version.getNamespace());
		assertEquals("/schemas/3.0/DDMS-v3_0.xsd", version.getSchema());
		assertEquals("http://www.opengis.net/gml/3.2", version.getGmlNamespace());
		assertEquals("/schemas/3.0/DDMS-GML-Profile.xsd", version.getGmlSchema());
		assertEquals("urn:us:gov:ic:ism", version.getIcismNamespace());
	}
}
