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
package buri.ddmsence.ddms.security;

import buri.ddmsence.ddms.AbstractComponentTestCase;

/**
 * <p>Tests related to the ICISM Controlled Vocabularies</p>
 * 
 * @author Brian Uri!
 * @since 1.0.0
 */
public class ControlledVocabularyTest extends AbstractComponentTestCase {
	
	/**
	 * Constructor
	 */
	public ControlledVocabularyTest() {
		super(null);
	}
	
	public void testIsUSMarking() {
		assertTrue(ControlledVocabulary.enumerationContains(ControlledVocabulary.CVE_US_CLASSIFICATIONS, "TS"));
		assertFalse(ControlledVocabulary.enumerationContains(ControlledVocabulary.CVE_US_CLASSIFICATIONS, "CTS"));
	}
	
	public void testGetMarkingIndex() {
		assertEquals(-1, ControlledVocabulary.getMarkingIndex("SuperSecret"));
		assertEquals(-1, ControlledVocabulary.getMarkingIndex("R"));
		assertEquals(-1, ControlledVocabulary.getMarkingIndex("CTS-BALK"));
		assertEquals(-1, ControlledVocabulary.getMarkingIndex("CTS-B"));
		assertTrue(ControlledVocabulary.getMarkingIndex("TS") > ControlledVocabulary.getMarkingIndex("C"));
		assertTrue(ControlledVocabulary.getMarkingIndex("CTS") > ControlledVocabulary.getMarkingIndex("NU"));
	}
		
	public void testHasSharingCaveat() {
		assertTrue(ControlledVocabulary.needsManualReview("CTS-B"));
		assertTrue(ControlledVocabulary.needsManualReview("CTS-BALK"));
		assertFalse(ControlledVocabulary.needsManualReview("CTS"));
	}
}




