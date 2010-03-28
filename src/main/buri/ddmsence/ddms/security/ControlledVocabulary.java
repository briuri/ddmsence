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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

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
 * <ul>
 * <li>CVEnumISM25X.xml</li>
 * <li>CVEnumISMClassificationAll.xml</li>
 * <li>CVEnumISMClassificationUS.xml</li>
 * <li>CVEnumISMDissem.xml</li>
 * <li>CVEnumISMFGIOpen.xml</li>
 * <li>CVEnumISMFGIProtected.xml</li>
 * <li>CVEnumISMNonIC.xml</li>
 * <li>CVEnumISMOwnerProducer.xml</li>
 * <li>CVEnumISMRelTo.xml</li>
 * <li>CVEnumISMSAR.xml</li>
 * <li>CVEnumISMSCIControls.xml</li>
 * <li>CVEnumISMSourceMarked.xml</li>
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
	
	/** Filename for the enumerations allowed in a declassException attribute */
	public static final String CVE_DECLASS_EXCEPTION = "CVEnumISM25X.xml";
	
	/** Filename for the enumerations allowed in a classification attribute */	
	public static final String CVE_ALL_CLASSIFICATIONS = "CVEnumISMClassificationAll.xml";
	
	/** Filename for the enumerations allowed in a classification attribute (US only) */
	public static final String CVE_US_CLASSIFICATIONS = "CVEnumISMClassificationUS.xml";
	
	/** Filename for the enumerations allowed in a disseminationControls attribute */
	public static final String CVE_DISSEMINATION_CONTROLS = "CVEnumISMDissem.xml";
	
	/** Filename for the enumerations allowed in a FGIsourceOpen attribute */
	public static final String CVE_FGI_SOURCE_OPEN = "CVEnumISMFGIOpen.xml";
	
	/** Filename for the enumerations allowed in a FGIsourceProtected attribute */
	public static final String CVE_FGI_SOURCE_PROTECTED = "CVEnumISMFGIProtected.xml";
	
	/** Filename for the enumerations allowed in a nonICmarkings attribute */
	public static final String CVE_NON_IC_MARKINGS = "CVEnumISMNonIC.xml";
	
	/** Filename for the enumerations allowed in an ownerProducer attribute */
	public static final String CVE_OWNER_PRODUCERS = "CVEnumISMOwnerProducer.xml";
	
	/** Filename for the enumerations allowed in a releasableTo attribute */
	public static final String CVE_RELEASABLE_TO = "CVEnumISMRelTo.xml";
	
	/** Filename for the enumerations allowed in a SARIdentifier attribute */
	public static final String CVE_SAR_IDENTIFIER = "CVEnumISMSAR.xml";
	
	/** Filename for the enumerations allowed in a SCIcontrols attribute */
	public static final String CVE_SCI_CONTROLS = "CVEnumISMSCIControls.xml";
	
	/** Filename for the enumerations allowed in a typeOfExemptedSource attribute */
	public static final String CVE_TYPE_EXEMPTED_SOURCE = "CVEnumISMSourceMarked.xml";
	
	private static final List<String> ORDERED_US_CLASSIFICATION_TYPES = new ArrayList<String>();
	static {
		ORDERED_US_CLASSIFICATION_TYPES.add("U");
		ORDERED_US_CLASSIFICATION_TYPES.add("C");
		ORDERED_US_CLASSIFICATION_TYPES.add("S");
		ORDERED_US_CLASSIFICATION_TYPES.add("TS");
	}
	
	private static final List<String> ORDERED_NATO_CLASSIFICATION_TYPES = new ArrayList<String>();
	static {		
		ORDERED_NATO_CLASSIFICATION_TYPES.add("NU");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("R");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("NR");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("NC");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("NCA");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("NS");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("NSAT");	
		ORDERED_NATO_CLASSIFICATION_TYPES.add("CTS");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("CTS-B");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("CTS-BALK");
		ORDERED_NATO_CLASSIFICATION_TYPES.add("CTSA");
	}
	
	private static final Set<String> ALL_CLASSIFICATION_TYPES = new HashSet<String>();
	static {
		ALL_CLASSIFICATION_TYPES.addAll(ORDERED_US_CLASSIFICATION_TYPES);
		ALL_CLASSIFICATION_TYPES.addAll(ORDERED_NATO_CLASSIFICATION_TYPES);
	}	
	
	private static final Map<String, Set<String>> ENUMERATIONS = new HashMap<String, Set<String>>();
	
	private static final String ENUMERATION_NAME = "Enumeration";
	private static final String TERM_NAME = "Term";
	private static final String VALUE_NAME = "Value";
	private static final String XML_READER_CLASS = PropertyReader.getProperty("xmlReader.class");
	private static final String CVE_NAMESPACE = PropertyReader.getProperty("icism.cve.xmlNamespace");
	private static final String CVE_LOCATION = PropertyReader.getProperty("icism.cve.enumLocation");
	
	/**
	 * Private to prevent instantiation
	 */
	private ControlledVocabulary() throws IOException {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader(XML_READER_CLASS);
			Builder builder = new Builder(reader, false);
			loadEnumeration(builder, CVE_DECLASS_EXCEPTION);
			loadEnumeration(builder, CVE_ALL_CLASSIFICATIONS);
			loadEnumeration(builder, CVE_US_CLASSIFICATIONS);
			loadEnumeration(builder, CVE_DISSEMINATION_CONTROLS);
			loadEnumeration(builder, CVE_FGI_SOURCE_OPEN);
			loadEnumeration(builder, CVE_FGI_SOURCE_PROTECTED);
			loadEnumeration(builder, CVE_NON_IC_MARKINGS);
			loadEnumeration(builder, CVE_OWNER_PRODUCERS);
			loadEnumeration(builder, CVE_RELEASABLE_TO);
			loadEnumeration(builder, CVE_SAR_IDENTIFIER);
			loadEnumeration(builder, CVE_SCI_CONTROLS);
			loadEnumeration(builder, CVE_TYPE_EXEMPTED_SOURCE);
		}
		catch (SAXException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Opens the enumeration file and extracts a Set of String token values
	 * based on the Term elements in the file. Stores them in the ENUMERATIONS
	 * map with the key.
	 * 
	 * @param builder the XOM Builder to read the file with
	 * @param enumerationKey the key for the enumeration, which doubles as the filename.
	 */
	private void loadEnumeration(Builder builder, String enumerationKey) throws IOException {
		InputStream stream = null;
		try {
			stream = getClass().getResourceAsStream(CVE_LOCATION + enumerationKey);
			Document doc = builder.build(stream);
			Set<String> values = new TreeSet<String>();
			Element enumerationElement = doc.getRootElement().getFirstChildElement(ENUMERATION_NAME, CVE_NAMESPACE);
			Elements terms = enumerationElement.getChildElements(TERM_NAME, CVE_NAMESPACE);
			for (int i = 0; i < terms.size(); i++) {
				Element value = terms.get(i).getFirstChildElement(VALUE_NAME, CVE_NAMESPACE);
				if (value != null)
					values.add(value.getValue());
			}
			ENUMERATIONS.put(enumerationKey, values);
		}
		catch (ParsingException e) {
			throw new IOException(e);
		}
		finally {
			if (stream != null)
				stream.close();
		}
	}
	
	/**
	 * Checks if a value exists in the controlled vocabulary identified by the key. 
	 * 
	 * @param enumerationKey the key of the enumeration
	 * @param value the test value
	 * @throws InvalidDDMSException if the value is not.
	 */
	public static void validateEnumeration(String enumerationKey, String value) throws InvalidDDMSException {
		if (!enumerationContains(enumerationKey, value))
			throw new InvalidDDMSException(value + " is not a valid enumeration token for this attribute, as specified in " + enumerationKey + ".");
	}
	
	/**
	 * Checks if a value exists in the controlled vocabulary identified by the key
	 * 
	 * @param enumerationKey the key of the enumeration
	 * @param value the test value
	 * @return true if the value exists in the enumeration, false otherwise
	 */
	public static boolean enumerationContains(String enumerationKey, String value) {
		Util.requireValue("key", enumerationKey);
		return (ENUMERATIONS.get(enumerationKey).contains(value));
	}
	
	/**
	 * Returns an index which can be used to determine how restrictive a marking is (with lower numbers being less
	 * restrictive).
	 * 
	 * <p> The ordering for US markings (from least to most restrictive) is [U, C, S, TS]. The ordering for NATO
	 * markings (from least to most restrictive) is [NU, NR, NC, NCA, NS, NSAT, CTS, CTSA]. </p>
	 * 
	 * <p>CTS-B, CTS-BALK, and R are not included in the ordering, so they return -1. </p>
	 * 
	 * @param classification the classification to test
	 * @return an index, or -1 if the marking does not belong to any known systems.
	 */
	public static int getMarkingIndex(String classification) {
		if (!Util.isEmpty(classification)) {
			if (enumerationContains(CVE_US_CLASSIFICATIONS, classification))
				return (ORDERED_US_CLASSIFICATION_TYPES.indexOf(classification));
			if (!needsManualReview(classification))
				return (ORDERED_NATO_CLASSIFICATION_TYPES.indexOf(classification));
		}
		return (-1);
	}
	
	/**
	 * Checks if the classification is CTS-B, CTS-BALK, or R.
	 * 
	 * <p>
	 * I was not sure how these 3 markings are ordered when compared to other NATO markings. Need to research further.
	 * </p>
	 * 
	 * @param classification the classification to test
	 * @return true if it is, false otherwise
	 */
	public static boolean needsManualReview(String classification) {
		Util.requireValue("classification", classification);
		return ("CTS-B".equals(classification) || "CTS-BALK".equals(classification) || "R".equals(classification));
	}
}
