/* Copyright 2010 - 2014 by Brian Uri!
   
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
package buri.ddmsence.ddms.security.ism;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Representation of the Controlled Vocabulary enumerations used by ISM attributes.
 * <br /><br />
 * {@ddms.versions 11111}
 * 
 * <p>
 * Token values are read from the CVEnumISM.xml files accompanying the "XML Data Encoding Specification for Information
 * Security Marking Metadata". They can then be used to validate the contents of the attributes
 * in a {@link SecurityAttributes} or {@link NoticeAttributes} instance.
 * </p>
 * 
 * <ul>
 * <li>CVEnumISM25X.xml: tokens allowed in the "declassException" attribute</li>
 * <li>CVEnumISMAtomicEnergyMarkings.xml: tokens allowed in the "atomicEnergyMarkings" attribute (starting in DDMS
 * 3.1)</li>
 * <li>CVEnumISMClassificationAll.xml: tokens allowed in the "classification" attribute</li>
 * <li>CVEnumISMClassificationUS.xml: subset of the tokens allowed in the "classification" attribute</li>
 * <li>CVEnumISMCompliesWith.xml: tokens allowed in the "compliesWith" attribute (starting in DDMS 3.1)</li>
 * <li>CVEnumISMDissem.xml: tokens allowed in the "disseminationControls" attribute</li>
 * <li>CVEnumISMFGIOpen.xml: tokens allowed in the "FGIsourceOpen" attribute</li>
 * <li>CVEnumISMFGIProtected.xml: tokens allowed in the "FGIsourceProtected" attribute</li>
 * <li>CVEnumISMNonIC.xml: tokens allowed in the "nonICmarkings" attribute</li>
 * <li>CVEnumISMNonUSControls.xml: tokens allowed in the "nonUSControls" attribute (starting in DDMS 3.1)</li>
 * <li>CVEnumISMNotice.xml: tokens allowed in the "noticeType" attribute (starting in DDMS 4.0.1)</li>
 * <li>CVEnumISMOwnerProducer.xml: tokens allowed in the "ownerProducer" attribute</li>
 * <li>CVEnumISMPocType.xml: tokens allowed in the "pocType" attribute</li>
 * <li>CVEnumISMRelTo.xml: tokens allowed in the "displayOnlyTo" (starting in DDMS 3.1) and "releasableTo"
 * attribute</li>
 * <li>CVEnumISMSAR.xml: tokens allowed in the "SARIdentifier" attribute</li>
 * <li>CVEnumISMSCIControls.xml: tokens allowed in the "SCIcontrols" attribute</li>
 * <li>CVEnumISMSourceMarked.xml: tokens allowed in the "typeOfExemptedSource" attribute (DDMS 2.0 and DDMS 3.0
 * only)</li>
 * </ul>
 * 
 * <p>Some of these vocabularies include regular expression patterns.</p>
 * 
 * @author Brian Uri!
 * @since 0.9.d
 */
public class ISMVocabulary {

	/** Filename for the enumerations allowed in a declassException attribute */
	public static final String CVE_DECLASS_EXCEPTION = "CVEnumISM25X.xml";

	/** Filename for the enumerations allowed in an atomicEnergyMarkings attribute */
	public static final String CVE_ATOMIC_ENERGY_MARKINGS = "CVEnumISMAtomicEnergyMarkings.xml";

	/** Filename for the enumerations allowed in a classification attribute */
	public static final String CVE_ALL_CLASSIFICATIONS = "CVEnumISMClassificationAll.xml";

	/** Filename for the enumerations allowed in a classification attribute (US only) */
	public static final String CVE_US_CLASSIFICATIONS = "CVEnumISMClassificationUS.xml";

	/** Filename for the enumerations allowed in a compliesWith attribute */
	public static final String CVE_COMPLIES_WITH = "CVEnumISMCompliesWith.xml";

	/** Filename for the enumerations allowed in a displayOnlyTo attribute */
	public static final String CVE_DISPLAY_ONLY_TO = "CVEnumISMRelTo.xml";

	/** Filename for the enumerations allowed in a disseminationControls attribute */
	public static final String CVE_DISSEMINATION_CONTROLS = "CVEnumISMDissem.xml";

	/** Filename for the enumerations allowed in a FGIsourceOpen attribute */
	public static final String CVE_FGI_SOURCE_OPEN = "CVEnumISMFGIOpen.xml";

	/** Filename for the enumerations allowed in a FGIsourceProtected attribute */
	public static final String CVE_FGI_SOURCE_PROTECTED = "CVEnumISMFGIProtected.xml";

	/** Filename for the enumerations allowed in a nonICmarkings attribute */
	public static final String CVE_NON_IC_MARKINGS = "CVEnumISMNonIC.xml";

	/** Filename for the enumerations allowed in a nonUSControls attribute */
	public static final String CVE_NON_US_CONTROLS = "CVEnumISMNonUSControls.xml";

	/** Filename for the enumerations allowed in a noticeType attribute */
	public static final String CVE_NOTICE_TYPE = "CVEnumISMNotice.xml";

	/** Filename for the enumerations allowed in an ownerProducer attribute */
	public static final String CVE_OWNER_PRODUCERS = "CVEnumISMOwnerProducer.xml";

	/** Filename for the enumerations allowed in a pocType attribute */
	public static final String CVE_POC_TYPE = "CVEnumISMPocType.xml";

	/** Filename for the enumerations allowed in a releasableTo attribute */
	public static final String CVE_RELEASABLE_TO = "CVEnumISMRelTo.xml";

	/** Filename for the enumerations allowed in a SARIdentifier attribute */
	public static final String CVE_SAR_IDENTIFIER = "CVEnumISMSAR.xml";

	/** Filename for the enumerations allowed in a SCIcontrols attribute */
	public static final String CVE_SCI_CONTROLS = "CVEnumISMSCIControls.xml";

	/** Filename for the enumerations allowed in a typeOfExemptedSource attribute */
	public static final String CVE_TYPE_EXEMPTED_SOURCE = "CVEnumISMSourceMarked.xml";

	private static final Set<String> COMMON_NETWORK_TYPES = new HashSet<String>();
	static {
		COMMON_NETWORK_TYPES.add("NIPRNet");
		COMMON_NETWORK_TYPES.add("SIPRNet");
		COMMON_NETWORK_TYPES.add("JWICS");
		COMMON_NETWORK_TYPES.add("ADSN");
		COMMON_NETWORK_TYPES.add("StoneGhost");
		COMMON_NETWORK_TYPES.add("LOCE");
		COMMON_NETWORK_TYPES.add("CRONOS");
		COMMON_NETWORK_TYPES.add("other");
	}

	private static final List<String> ALL_ENUMS = new ArrayList<String>();
	static {
		ALL_ENUMS.add(CVE_DECLASS_EXCEPTION);
		ALL_ENUMS.add(CVE_ATOMIC_ENERGY_MARKINGS);
		ALL_ENUMS.add(CVE_ALL_CLASSIFICATIONS);
		ALL_ENUMS.add(CVE_US_CLASSIFICATIONS);
		ALL_ENUMS.add(CVE_COMPLIES_WITH);
		ALL_ENUMS.add(CVE_DISSEMINATION_CONTROLS);
		ALL_ENUMS.add(CVE_DISPLAY_ONLY_TO);
		ALL_ENUMS.add(CVE_FGI_SOURCE_OPEN);
		ALL_ENUMS.add(CVE_FGI_SOURCE_PROTECTED);
		ALL_ENUMS.add(CVE_NON_IC_MARKINGS);
		ALL_ENUMS.add(CVE_NON_US_CONTROLS);
		ALL_ENUMS.add(CVE_NOTICE_TYPE);
		ALL_ENUMS.add(CVE_OWNER_PRODUCERS);
		ALL_ENUMS.add(CVE_POC_TYPE);
		ALL_ENUMS.add(CVE_RELEASABLE_TO);
		ALL_ENUMS.add(CVE_SAR_IDENTIFIER);
		ALL_ENUMS.add(CVE_SCI_CONTROLS);
		ALL_ENUMS.add(CVE_TYPE_EXEMPTED_SOURCE);
	}

	private static final String ENUMERATION_NAME = "Enumeration";
	private static final String TERM_NAME = "Term";
	private static final String VALUE_NAME = "Value";
	private static final String REG_EXP_NAME = "regularExpression";

	/**
	 * A thread-local instance of the last location where enumeration files were cached from. This is used to cache the
	 * most recently used set of enumeration files for performance.
	 */
	private static final ThreadLocal<String> LAST_ENUM_HOLDER = new ThreadLocal<String>();
	
	/**
	 * A thread-local cache of any enumeration tokens mapped to the <code>LAST_ENUM_HOLDER</code> location. This cache
	 * is cleared whenever the <code>LAST_ENUM_HOLDER</code> changes.
	 */
	private static final ThreadLocal<Map<String, Set<String>>> ENUM_TOKEN_HOLDER = new ThreadLocal<Map<String, Set<String>>>() {
		@Override
		protected Map<String, Set<String>> initialValue() {
			return new HashMap<String, Set<String>>();
		}
	};
	
	/**
	 * A thread-local cache of any enumeration patterns mapped to the <code>LAST_ENUM_HOLDER</code> location. This cache
	 * is cleared whenever the <code>LAST_ENUM_HOLDER</code> changes.
	 */
	private static final ThreadLocal<Map<String, Set<String>>> ENUM_PATTERN_HOLDER = new ThreadLocal<Map<String, Set<String>>>() {
		@Override
		protected Map<String, Set<String>> initialValue() {
			return new HashMap<String, Set<String>>();
		}
	};

	/**
	 * Private to prevent instantiation
	 */
	private ISMVocabulary() {}

	/**
	 * Helper method to validate a value from a controlled vocabulary.
	 * 
	 * @param version the DDMS Version which maps to the version of ISM you wish to use
	 * @param enumerationKey the key of the enumeration
	 * @param value the test value
	 * @throws InvalidDDMSException if the value is not and validation should result in errors
	 */
	public static void validateEnumeration(DDMSVersion version, String enumerationKey, String value) throws InvalidDDMSException {
		System.out.println("validation for v" + version);
		if (!enumContains(version, enumerationKey, value)) {
			String message = getInvalidMessage(enumerationKey, value);
			throw new InvalidDDMSException(message);
		}
	}
	
	/**
	 * Returns an unmodifiable set of controlled vocabulary tokens. This method is publicly available
	 * so that these tokens can be used as reference data (for example, a select box on a web form).
	 * 
	 * <p>
	 * If you wish to use these tokens in that way, you must explicitly call
	 * <code>setDDMSVersion()</code> in advance, to ensure that the appropriate set of CVE files is used
	 * to look up the tokens, OR you may use the configurable property, <code>icism.cve.customEnumLocation</code>, to
	 * force the use of a custom set of CVE files. If neither option is used, the default set of tokens returned will be
	 * based on the current value of <code>DDMSVersion.getCurrentVersion()</code>.</p>
	 * 
	 * @param version the DDMS Version which maps to the version of ISM you wish to use
	 * @param enumerationKey the key of the enumeration
	 * @return an unmodifiable set of Strings
	 * @throws IllegalArgumentException if the key does not match a controlled vocabulary
	 */
	public static Set<String> getEnumerationTokens(DDMSVersion version, String enumerationKey) {
		updateEnumLocation(version);
		Set<String> vocabulary = ENUM_TOKEN_HOLDER.get().get(enumerationKey);
		if (vocabulary == null) {
			throw new IllegalArgumentException("No controlled vocabulary could be found for this key: "
				+ enumerationKey);
		}
		return (Collections.unmodifiableSet(vocabulary));
	}


	/**
	 * Checks if a value exists in the controlled vocabulary identified by the key. If the value does not match the
	 * tokens, but the CVE also contains patterns, the regular expression pattern is checked next. If neither tokens or
	 * patterns returns a match, return false.
	 * 
	 * @param version the DDMS Version which maps to the version of ISM you wish to use
	 * @param enumerationKey the key of the enumeration
	 * @param value the test value
	 * @return true if the value exists in the enumeration, false otherwise
	 * @throws IllegalArgumentException on an invalid key
	 */
	protected static boolean enumContains(DDMSVersion version, String enumerationKey, String value) {
		Util.requireValue("key", enumerationKey);
		boolean isValidToken = getEnumerationTokens(version, enumerationKey).contains(value);
		if (!isValidToken) {
			for (String patternString : getEnumerationPatterns(version, enumerationKey)) {
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(value);
				if (matcher.matches()) {
					isValidToken = true;
					break;
				}
			}
		}
		return (isValidToken);
	}

	/**
	 * Returns an unmodifiable set of controlled vocabulary regular expression patterns.
	 * 
	 * @param version the DDMS Version which maps to the version of ISM you wish to use
	 * @param enumerationKey the key of the enumeration
	 * @return an unmodifiable set of Strings
	 */
	protected static Set<String> getEnumerationPatterns(DDMSVersion version, String enumerationKey) {
		updateEnumLocation(version);
		Set<String> vocabulary = ENUM_PATTERN_HOLDER.get().get(enumerationKey);
		return (Collections.unmodifiableSet(vocabulary));
	}

	/**
	 * Maintains a DDMSVersion which will be used to look up the CVE files. If the version has changed from its previous
	 * value, the new set of CVEs will be loaded and cached.
	 * 
	 * @param version the DDMS version
	 */
	private static void updateEnumLocation(DDMSVersion version) {
		String enumLocation = PropertyReader.getProperty(version.getVersion() + ".ism.cveLocation");
		if (LAST_ENUM_HOLDER.get() == null || !LAST_ENUM_HOLDER.get().equals(enumLocation)) {
			System.out.println("Updating enumLocation from " + LAST_ENUM_HOLDER.get() + " to " + enumLocation);
			LAST_ENUM_HOLDER.set(enumLocation);
			try {
				ENUM_TOKEN_HOLDER.get().clear();
				ENUM_PATTERN_HOLDER.get().clear();
				XMLReader reader = XMLReaderFactory.createXMLReader(PropertyReader.getProperty("xml.reader.class"));
				Builder builder = new Builder(reader, false);
				for (String cve : ALL_ENUMS) {
					try {
						String cveNamespace = PropertyReader.getProperty(version.getVersion() + ".ism.cve.xmlNamespace");
						loadEnumeration(enumLocation, cveNamespace, builder, cve);
					}
					catch (Exception e) {
						continue;
					}
				}
			}
			catch (SAXException e) {
				throw new RuntimeException("Could not load controlled vocabularies: " + e.getMessage());
			}
		}
	}

	/**
	 * Opens the enumeration file and extracts a Set of String token values based on the Term elements in the file.
	 * Stores them in the ENUM_TOKENS map with the key. If a pattern is discovered, it is stored in a separate mapping.
	 * 
	 * @param enumLocation the classpath resource location for the enumeration files
	 * @param cveNamespace the XML namespace of entries in this enumeration
	 * @param builder the XOM Builder to read the file with
	 * @param enumerationKey the key for the enumeration, which doubles as the filename.
	 */
	private static void loadEnumeration(String enumLocation, String cveNamespace, Builder builder, String enumerationKey)
		throws ParsingException, IOException {
		InputStream stream = new ISMVocabulary().getClass().getResourceAsStream(enumLocation + enumerationKey);
		Document doc = builder.build(stream);
		Set<String> tokens = new TreeSet<String>();
		Set<String> patterns = new HashSet<String>();
		Element enumerationElement = doc.getRootElement().getFirstChildElement(ENUMERATION_NAME, cveNamespace);
		Elements terms = enumerationElement.getChildElements(TERM_NAME, cveNamespace);
		for (int i = 0; i < terms.size(); i++) {
			Element value = terms.get(i).getFirstChildElement(VALUE_NAME, cveNamespace);
			boolean isPattern = Boolean.valueOf(value.getAttributeValue(REG_EXP_NAME)).booleanValue();
			if (value != null) {
				if (isPattern)
					patterns.add(value.getValue());
				else
					tokens.add(value.getValue());
			}
		}
		ENUM_TOKEN_HOLDER.get().put(enumerationKey, tokens);
		ENUM_PATTERN_HOLDER.get().put(enumerationKey, patterns);
	}
	
	/**
	 * Checks if one of the classifications that existed in DDMS 2.0 but was removed for DDMS 3.0 is being used.
	 * 
	 * @param classification the classification to test
	 * @return true if it is one of the removed enums, false otherwise
	 */
	public static boolean usingOldClassification(String classification) {
		return ("NS-S".equals(classification) || "NS-A".equals(classification));
	}

	/**
	 * Generates a message for an invalid value.
	 * 
	 * @param enumerationKey the key of the enumeration
	 * @param value the test value which was invalid
	 * @return a String
	 */
	public static String getInvalidMessage(String enumerationKey, String value) {
		return (value + " is not a valid enumeration token for this attribute, as specified in " + enumerationKey + ".");
	}

	/**
	 * Validates the value of a network attribute from the IC-COMMON schema. This check is only relevant in DDMS 4.1, which
	 * uses IC-COMMON to create a no-namespace "network" attribute. In DDMS 5.0, "network" is moved into the "virt"
	 * XML namespace, and validation of the token is implicit in the schema.
	 * 
	 * @param network the network token to test
	 * @throws InvalidDDMSException if the network is not a valid token
	 */
	public static void requireValidNetwork(String network) throws InvalidDDMSException {
		if (!COMMON_NETWORK_TYPES.contains(network))
			throw new InvalidDDMSException("The network attribute must be one of " + COMMON_NETWORK_TYPES);
	}
}