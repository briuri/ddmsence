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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Representation of the Controlled Vocabulary enumerations used by ICISM attributes.
 * 
 * <p>
 * Token values are read from the CVEnumISM.xml files accompanying the "XML Data Encoding Specification
 * for Information Security Marking Metadata Version 2 (Pre-Release)".
 * </p>
 * 
 * <p>
 * Separate Java lists of Classification values are maintained to calculate the ordering of 
 * classifications from least to most restrictive.
 * </p>
 * 
 * @author Brian Uri!
 * @since 1.0.0
 */
public class ControlledVocabulary {

	
	private static List<String> US_CLASSIFICATION_TYPES = new ArrayList<String>();
	static {
		US_CLASSIFICATION_TYPES.add("U");
		US_CLASSIFICATION_TYPES.add("C");
		US_CLASSIFICATION_TYPES.add("R");
		US_CLASSIFICATION_TYPES.add("S");
		US_CLASSIFICATION_TYPES.add("TS");
	}
	
	private static List<String> NATO_CLASSIFICATION_TYPES = new ArrayList<String>();
	static {		
		NATO_CLASSIFICATION_TYPES.add("NU");
		NATO_CLASSIFICATION_TYPES.add("NR");
		NATO_CLASSIFICATION_TYPES.add("NC");
		NATO_CLASSIFICATION_TYPES.add("NCA");
		NATO_CLASSIFICATION_TYPES.add("NS");
		NATO_CLASSIFICATION_TYPES.add("NSAT");	
		NATO_CLASSIFICATION_TYPES.add("CTS");
		NATO_CLASSIFICATION_TYPES.add("CTS-B");
		NATO_CLASSIFICATION_TYPES.add("CTS-BALK");
		NATO_CLASSIFICATION_TYPES.add("CTSA");
	}
	
	private static Set<String> ALL_CLASSIFICATION_TYPES = new HashSet<String>();
	static {
		ALL_CLASSIFICATION_TYPES.addAll(US_CLASSIFICATION_TYPES);
		ALL_CLASSIFICATION_TYPES.addAll(NATO_CLASSIFICATION_TYPES);
	}	
	
	private static final String CVE_LOCATION = PropertyReader.getProperty("icism.cveLocation");
	private static final String CVE_DECLASS_EXCEPTION = "CVEnumISM25X.xml";
	private static final String CVE_ALL_CLASSIFICATIONS = "CVEnumISMClassificationAll.xml";
	private static final String CVE_US_CLASSIFICATIONS = "CVEnumISMClassificationUS.xml";
	private static final String CVE_DISSEMINATION_CONTROLS = "CVEnumISMDissem.xml";
	private static final String CVE_FGI_SOURCE_OPEN = "CVEnumISMFGIOpen.xml";
	private static final String CVE_FGI_SOURCE_PROTECTED = "CVEnumISMFGIProtected.xml";
	private static final String CVE_NON_IC_MARKINGS = "CVEnumISMNonIC.xml";
	private static final String CVE_OWNER_PRODUCERS = "CVEnumISMOwnerProducer.xml";
	private static final String CVE_RELEASABLE_TO = "CVEnumISMRelTo.xml";
	private static final String CVE_SAR_IDENTIFIER = "CVEnumISMSAR.xml";
	private static final String CVE_SCI_CONTROLS = "CVEnumISMSCIControls.xml";
	private static final String CVE_TYPE_EXEMPTED_SOURCE = "CVEnumISMSourceMarked.xml";

	/**
	 * Private to prevent instantiation
	 */
	private ControlledVocabulary() {}
	
	/**
	 * Validates a classification against the allowed types.
	 * 
	 * @param classification the type to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateClassification(String classification) throws InvalidDDMSException {
		Util.requireDDMSValue("classification", classification);
		if (!ALL_CLASSIFICATION_TYPES.contains(classification))
			throw new InvalidDDMSException("The classification must be one of " + ALL_CLASSIFICATION_TYPES);
	}
	
	/**
	 * Checks whether a marking is a US or NATO marking.
	 * 
	 * @param classification the classification to test
	 * @return true for US, false for NATO
	 */
	public static boolean isUSMarking(String classification) {
		return (US_CLASSIFICATION_TYPES.contains(classification));
	}
	
	/**
	 * Returns an index which can be used to determine how restrictive a marking is (with lower numbers being less
	 * restrictive).
	 * 
	 * <p> The ordering for US markings (from least to most restrictive) is [U, C, R, S, TS]. The ordering for NATO
	 * markings (from least to most restrictive) is [NU, NR, NC, NCA, NS, NSAT, CTS, CTSA]. </p>
	 * 
	 * <p>CTS-B and CTS-BALK will return the same index as CTS. </p>
	 * 
	 * @param classification the classification to test
	 * @return an index, or -1 if the marking does not belong to any known systems.
	 */
	public static int getMarkingIndex(String classification) {
		if (!Util.isEmpty(classification)) {
			if (isUSMarking(classification))
				return (US_CLASSIFICATION_TYPES.indexOf(classification));
			if (hasSharingCaveat(classification)) {
				classification = "CTS";
			}
			return (NATO_CLASSIFICATION_TYPES.indexOf(classification));
		}
		return (-1);
	}
	
	/**
	 * Checks if the classification is CTS-B or CTS-BALK.
	 * 
	 * @param classification the classification to test
	 * @return true if it is, false otherwise
	 */
	public static boolean hasSharingCaveat(String classification) {
		Util.requireValue("classification", classification);
		return ("CTS-B".equals(classification) || "CTS-BALK".equals(classification));
	}
}
