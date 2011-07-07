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
package buri.ddmsence.ddms.security;

import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;

/**
 * <p>Tests related to the ICISM Controlled Vocabularies</p>
 * 
 * @author Brian Uri!
 * @since 0.9.d
 */
public class ISMVocabularyTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public ISMVocabularyTest() {
		super(null);
	}

	/**
	 * Resets the enumLocation property.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		PropertyReader.setProperty("icism.cve.customEnumLocation", "");
	}
	
	public void testBadKey() {
		try {
			ISMVocabulary.getEnumerationTokens("unknownKey");
			fail("Allowed invalid key.");
		} catch (IllegalArgumentException e) {
			// Good
		}
		try {
			ISMVocabulary.getEnumerationPatterns("unknownKey");
			fail("Allowed invalid key.");
		} catch (IllegalArgumentException e) {
			// Good
		}
	}

	public void testEnumerationTokens() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			ISMVocabulary.setIsmVersion(DDMSVersion.getVersionFor(version));
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_ALL_CLASSIFICATIONS, "C"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_ALL_CLASSIFICATIONS, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_OWNER_PRODUCERS, "AUS"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_OWNER_PRODUCERS, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_SCI_CONTROLS, "HCS"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_SCI_CONTROLS, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FOUO"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_FGI_SOURCE_OPEN, "ABW"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_FGI_SOURCE_OPEN, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_FGI_SOURCE_PROTECTED, "ABW"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_FGI_SOURCE_PROTECTED, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_RELEASABLE_TO, "ABW"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_RELEASABLE_TO, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_NON_IC_MARKINGS, "SINFO"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_NON_IC_MARKINGS, "unknown"));
	
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DECLASS_EXCEPTION, "25X1"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_DECLASS_EXCEPTION, "unknown"));
	
			if (!"3.1".equals(version)) {
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, "X1"));
				assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, "unknown"));
			}
			
			if ("3.1".equals(version)) {
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_ATOMIC_ENERGY_MARKINGS, "RD"));
				assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_ATOMIC_ENERGY_MARKINGS, "unknown"));
				
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_COMPLIES_WITH, "ICD-710"));
				assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_COMPLIES_WITH, "unknown"));
				
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISPLAY_ONLY_TO, "ABW"));
				assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISPLAY_ONLY_TO, "unknown"));
				
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_NON_US_CONTROLS, "ATOMAL"));
				assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_NON_US_CONTROLS, "unknown"));
			}
		}
	}

	public void testEnumerationPatterns() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			ISMVocabulary.setIsmVersion(DDMSVersion.getVersionFor(version));
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_SCI_CONTROLS, "SI-G-ABCD"));
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_SCI_CONTROLS, "SI-ECI-ABC"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_SCI_CONTROLS, "SI-G-ABCDE"));

			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_SAR_IDENTIFIER, "SAR-ABC"));
			assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_SAR_IDENTIFIER, "SAR-AB"));
			assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_SAR_IDENTIFIER, "SAR-ABCD"));
			
			if (!"3.1".equals(version)) {
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "RD-SG-1"));
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "RD-SG-12"));
				assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "RD-SG-100"));
	
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FRD-SG-1"));
				assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FRD-SG-12"));
				assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, "FRD-SG-100"));
			}
		}
	}

	public void testIsUSMarking() {
		ISMVocabulary.setIsmVersion(DDMSVersion.getVersionFor("2.0"));
		assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_US_CLASSIFICATIONS, "TS"));
		assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_US_CLASSIFICATIONS, "CTS"));
	}

	public void testGetClassificationIndex() {
		assertEquals(-1, ISMVocabulary.getClassificationIndex("SuperSecret"));
		assertEquals(1, ISMVocabulary.getClassificationIndex("R"));
		assertEquals(7, ISMVocabulary.getClassificationIndex("CTS-BALK"));
		assertEquals(7, ISMVocabulary.getClassificationIndex("CTS-B"));
		assertTrue(ISMVocabulary.getClassificationIndex("TS") > ISMVocabulary.getClassificationIndex("C"));
		assertTrue(ISMVocabulary.getClassificationIndex("CTS") > ISMVocabulary.getClassificationIndex("NU"));
	}
	
	public void testInvalidMessage() {
		assertEquals("Dog is not a valid enumeration token for this attribute, as specified in Cat.",
			ISMVocabulary.getInvalidMessage("Cat", "Dog"));
	}
	
	public void testChangedCVELocation() {
		assertFalse(ISMVocabulary.enumContains(ISMVocabulary.CVE_DECLASS_EXCEPTION, "25X0"));
		PropertyReader.setProperty("icism.cve.customEnumLocation", "/customCVEnumISM/");
		assertTrue(ISMVocabulary.enumContains(ISMVocabulary.CVE_DECLASS_EXCEPTION, "25X0"));
		
		// Unknown Location
		PropertyReader.setProperty("icism.cve.customEnumLocation", "/doesNotExist/");
		try {
			ISMVocabulary.enumContains(ISMVocabulary.CVE_DECLASS_EXCEPTION, "25X0");
			fail("Evaluated an enumeration with no enumeration files.");
		}
		catch (RuntimeException e) {
			// Good
		}
	}
}
