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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Attribute group for the ICISM markings used throughout DDMS.
 * 
 * <p>The DDMS documentation does not provide sample HTML/Text output for every attribute, so a best guess was taken. In general, 
 * the HTML/Text output of security attributes will be prefixed with the name of the element being marked. For example:</p>
 * <ul><code>
 * Title Owner Producer: US<br />
 * &lt;meta name="security.classification" content="U" /&gt;<br />
 * </code></ul></p>
 * 
 * <p>When validating this attribute group, the required/optional nature of the classification and ownerProducer attributes
 * are not checked. Because that limitation depends on the parent element (for example, ddms:title requires them, but ddms:creator does not),
 * the parent element should be responsible for checking, via <code>requireClassification()</code>.</p>
 * 
 * <p>At this time, logical validation is only done on the data types of the various attributes, and the controlled vocabulary enumerations
 * behind some of the attributes. I would like to add the complete constraints set from the "XML Data Encoding Specification
 * for Information Security Marking Metadata Version 2 (Pre-Release)" document in the future.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ICISM:classification</u>: (optional)<br />
 * <u>ICISM:ownerProducer</u>: (optional)<br />
 * <u>ICISM:SCIcontrols</u>: (optional)<br />
 * <u>ICISM:SARIdentifier</u>: (optional)<br />
 * <u>ICISM:disseminationControls</u>: (optional)<br />
 * <u>ICISM:FGIsourceOpen</u>: (optional)<br />
 * <u>ICISM:FGIsourceProtected</u>: (optional)<br />
 * <u>ICISM:releasableTo</u>: (optional)<br />
 * <u>ICISM:nonICmarkings</u>: (optional)<br />
 * <u>ICISM:classifiedBy</u>: (optional)<br />
 * <u>ICISM:compilationReason</u>: (optional, v3.0 only)<br />
 * <u>ICISM:derivativelyClassifiedBy</u>: (optional)<br />
 * <u>ICISM:classificationReason</u>: (optional)<br />
 * <u>ICISM:derivedFrom</u>: (optional)<br />
 * <u>ICISM:declassDate</u>: (optional)<br />
 * <u>ICISM:declassEvent</u>: (optional)<br />
 * <u>ICISM:declassException</u>: (optional)<br />
 * <u>ICISM:typeOfExemptedSource</u>: (optional)<br />
 * <u>ICISM:dateOfExemptedSource</u>: (optional)<br />
 * <u>ICISM:declassManualReview</u>: (optional, v2.0 only)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class SecurityAttributes {
	
	private List<ValidationMessage> _warnings;
	
	private String _cachedClassification = null;
	private List<String> _cachedOwnerProducers = null;
	private List<String> _cachedSCIcontrols = null;
	private List<String> _cachedSARIdentifier = null;
	private List<String> _cachedDisseminationControls = null;
	private List<String> _cachedFGIsourceOpen = null;
	private List<String> _cachedFGIsourceProtected = null;
	private List<String> _cachedReleasableTo = null;
	private List<String> _cachedNonICmarkings = null;
	private String _cachedClassifiedBy = null;
	private String _cachedCompilationReason = null;
	private String _cachedDerivativelyClassifiedBy = null;
	private String _cachedClassificationReason = null;
	private String _cachedDerivedFrom = null;	
	private XMLGregorianCalendar _cachedDeclassDate = null;
	private String _cachedDeclassEvent = null;
	private String _cachedDeclassException = null;
	private String _cachedTypeOfExemptedSource = null;
	private XMLGregorianCalendar _cachedDateOfExemptedSource = null;
	private Boolean _cachedDeclassManualReview = null;
	
	/** The ICISM prefix */
	public static final String ICISM_PREFIX = PropertyReader.getProperty("icism.prefix");

	/** Attribute name */
	public static final String CLASSIFICATION_NAME = "classification";
	
	/** Attribute name */
	public static final String OWNER_PRODUCER_NAME = "ownerProducer";
	
	/** Attribute name */
	public static final String SCI_CONTROLS_NAME = "SCIcontrols";
	
	/** Attribute name */
	public static final String SAR_IDENTIFIER_NAME = "SARIdentifier";
	
	/** Attribute name */
	public static final String DISSEMINATION_CONTROLS_NAME = "disseminationControls";
	
	/** Attribute name */
	public static final String FGI_SOURCE_OPEN_NAME = "FGIsourceOpen";
	
	/** Attribute name */
	public static final String FGI_SOURCE_PROTECTED_NAME = "FGIsourceProtected";
	
	/** Attribute name */
	public static final String RELEASABLE_TO_NAME = "releasableTo";
	
	/** Attribute name */
	public static final String NON_IC_MARKINGS_NAME = "nonICmarkings";
	
	/** Attribute name */
	public static final String CLASSIFIED_BY_NAME = "classifiedBy";
	
	/** Attribute name */
	public static final String COMPILATION_REASON_NAME = "compilationReason";
	
	/** Attribute name */
	public static final String DERIVATIVELY_CLASSIFIED_BY_NAME = "derivativelyClassifiedBy";
	
	/** Attribute name */
	public static final String CLASSIFICATION_REASON_NAME = "classificationReason";
	
	/** Attribute name */
	public static final String DERIVED_FROM_NAME = "derivedFrom";
	
	/** Attribute name */
	public static final String DECLASS_DATE_NAME = "declassDate";
	
	/** Attribute name */
	public static final String DECLASS_EVENT_NAME = "declassEvent";
	
	/** Attribute name */
	public static final String DECLASS_EXCEPTION_NAME = "declassException";	

	/** Attribute name */
	public static final String TYPE_OF_EXEMPTED_SOURCE_NAME = "typeOfExemptedSource";
	
	/** Attribute name */
	public static final String DATE_OF_EXEMPTED_SOURCE_NAME = "dateOfExemptedSource";
	
	/** Attribute name */
	public static final String DECLASS_MANUAL_REVIEW_NAME = "declassManualReview";
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element which is decorated with these attributes.
	 */
	public SecurityAttributes(Element element) throws InvalidDDMSException {
		_cachedClassification = element.getAttributeValue(CLASSIFICATION_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		_cachedOwnerProducers = getAsList(element.getAttributeValue(OWNER_PRODUCER_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));
		_cachedSCIcontrols = getAsList(element.getAttributeValue(SCI_CONTROLS_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));
		_cachedSARIdentifier = getAsList(element.getAttributeValue(SAR_IDENTIFIER_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));
		_cachedDisseminationControls = getAsList(element.getAttributeValue(DISSEMINATION_CONTROLS_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));
		_cachedFGIsourceOpen = getAsList(element.getAttributeValue(FGI_SOURCE_OPEN_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));
		_cachedFGIsourceProtected = getAsList(element.getAttributeValue(FGI_SOURCE_PROTECTED_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));
		_cachedReleasableTo = getAsList(element.getAttributeValue(RELEASABLE_TO_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));
		_cachedNonICmarkings = getAsList(element.getAttributeValue(NON_IC_MARKINGS_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace()));		
		_cachedClassifiedBy = element.getAttributeValue(CLASSIFIED_BY_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		_cachedCompilationReason = element.getAttributeValue(COMPILATION_REASON_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		_cachedDerivativelyClassifiedBy = element.getAttributeValue(DERIVATIVELY_CLASSIFIED_BY_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		_cachedClassificationReason = element.getAttributeValue(CLASSIFICATION_REASON_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		_cachedDerivedFrom = element.getAttributeValue(DERIVED_FROM_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());			
		String declassDate = element.getAttributeValue(DECLASS_DATE_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		if (!Util.isEmpty(declassDate))
			_cachedDeclassDate = getFactory().newXMLGregorianCalendar(declassDate);		
		_cachedDeclassEvent = element.getAttributeValue(DECLASS_EVENT_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		_cachedDeclassException = element.getAttributeValue(DECLASS_EXCEPTION_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());	
		_cachedTypeOfExemptedSource = element.getAttributeValue(TYPE_OF_EXEMPTED_SOURCE_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		String dateOfExemptedSource = element.getAttributeValue(DATE_OF_EXEMPTED_SOURCE_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		if (!Util.isEmpty(dateOfExemptedSource))
			_cachedDateOfExemptedSource = getFactory().newXMLGregorianCalendar(dateOfExemptedSource);
		String manualReview = element.getAttributeValue(DECLASS_MANUAL_REVIEW_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		if (!Util.isEmpty(manualReview))
			_cachedDeclassManualReview = Boolean.valueOf(manualReview);
		validate();
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * <p>The classification and ownerProducer exist as parameters, and any other security markings are passed in as a mapping
	 * of local attribute names to String values. This approach is a compromise between a constructor with over seventeen parameters,
	 * and the added complexity of a step-by-step factory/builder approach. If any name-value pairing does not correlate with a
	 * valid ICISM attribute, it will be ignored.
	 * </p>
	 * 
	 * <p>
	 * If an attribute mapping appears more than once, the last one in the list will be the one used. If classification and ownerProducer
	 * are included in the Map of other attributes, they will be ignored.
	 * </p>
	 * 
	 * @param classification the classification level, which must be a legal classification type (optional)
	 * @param ownerProducers a list of ownerProducers (optional)
	 * @param otherAttributes a name/value mapping of other ICISM attributes. The value will be a String value, as it appears in XML.
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SecurityAttributes(String classification, List<String> ownerProducers, Map<String, String> otherAttributes) throws InvalidDDMSException {
		if (ownerProducers == null)
			ownerProducers = Collections.emptyList();
		if (otherAttributes == null)
			otherAttributes = Collections.emptyMap();
				
		_cachedClassification = classification;
		_cachedOwnerProducers = ownerProducers;
		_cachedSCIcontrols = getAsList(otherAttributes.get(SCI_CONTROLS_NAME));
		_cachedSARIdentifier = getAsList(otherAttributes.get(SAR_IDENTIFIER_NAME));
		_cachedDisseminationControls = getAsList(otherAttributes.get(DISSEMINATION_CONTROLS_NAME));
		_cachedFGIsourceOpen = getAsList(otherAttributes.get(FGI_SOURCE_OPEN_NAME));
		_cachedFGIsourceProtected = getAsList(otherAttributes.get(FGI_SOURCE_PROTECTED_NAME));
		_cachedReleasableTo = getAsList(otherAttributes.get(RELEASABLE_TO_NAME));
		_cachedNonICmarkings = getAsList(otherAttributes.get(NON_IC_MARKINGS_NAME));		
		_cachedClassifiedBy = otherAttributes.get(CLASSIFIED_BY_NAME);
		_cachedCompilationReason = otherAttributes.get(COMPILATION_REASON_NAME);
		_cachedDerivativelyClassifiedBy = otherAttributes.get(DERIVATIVELY_CLASSIFIED_BY_NAME);
		_cachedClassificationReason = otherAttributes.get(CLASSIFICATION_REASON_NAME);
		_cachedDerivedFrom = otherAttributes.get(DERIVED_FROM_NAME);			
		String declassDate = otherAttributes.get(DECLASS_DATE_NAME);
		if (!Util.isEmpty(declassDate))
			_cachedDeclassDate = getFactory().newXMLGregorianCalendar(declassDate);		
		_cachedDeclassEvent = otherAttributes.get(DECLASS_EVENT_NAME);
		_cachedDeclassException = otherAttributes.get(DECLASS_EXCEPTION_NAME);	
		_cachedTypeOfExemptedSource = otherAttributes.get(TYPE_OF_EXEMPTED_SOURCE_NAME);
		String dateOfExemptedSource = otherAttributes.get(DATE_OF_EXEMPTED_SOURCE_NAME);
		if (!Util.isEmpty(dateOfExemptedSource))
			_cachedDateOfExemptedSource = getFactory().newXMLGregorianCalendar(dateOfExemptedSource);
		String manualReview = otherAttributes.get(DECLASS_MANUAL_REVIEW_NAME);
		if (!Util.isEmpty(manualReview))
			_cachedDeclassManualReview = Boolean.valueOf(manualReview);
		validate();
	}
	
	/**
	 * Helper method to convert an xs:NMTOKENS data type into a List of Strings.
	 * 
	 * @param element the element with the attribute
	 * @param name the name of the attribute to parse
	 * @return a List (never null)
	 */
	private List<String> getAsList(String value) {
		if (Util.isEmpty(value))
			return Collections.emptyList();
		return (Arrays.asList(value.split(" ")));
	}
		
	/**
	 * Convenience method to add these attributes onto an existing XOM Element
	 * 
	 * @param element the element to decorate
	 */
	public void addTo(Element element) {
		String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
		Util.addAttribute(element, ICISM_PREFIX, CLASSIFICATION_NAME, icNamespace, getClassification());
		Util.addAttribute(element, ICISM_PREFIX, OWNER_PRODUCER_NAME, icNamespace, Util.getXsList(getOwnerProducers()));
		Util.addAttribute(element, ICISM_PREFIX, SCI_CONTROLS_NAME, icNamespace, Util.getXsList(getSCIcontrols()));
		Util.addAttribute(element, ICISM_PREFIX, SAR_IDENTIFIER_NAME, icNamespace, Util.getXsList(getSARIdentifier()));
		Util.addAttribute(element, ICISM_PREFIX, DISSEMINATION_CONTROLS_NAME, icNamespace, Util.getXsList(getDisseminationControls()));
		Util.addAttribute(element, ICISM_PREFIX, FGI_SOURCE_OPEN_NAME, icNamespace, Util.getXsList(getFGIsourceOpen()));
		Util.addAttribute(element, ICISM_PREFIX, FGI_SOURCE_PROTECTED_NAME, icNamespace, Util.getXsList(getFGIsourceProtected()));
		Util.addAttribute(element, ICISM_PREFIX, RELEASABLE_TO_NAME, icNamespace, Util.getXsList(getReleasableTo()));
		Util.addAttribute(element, ICISM_PREFIX, NON_IC_MARKINGS_NAME, icNamespace, Util.getXsList(getNonICmarkings()));
		Util.addAttribute(element, ICISM_PREFIX, CLASSIFIED_BY_NAME, icNamespace, getClassifiedBy());
		Util.addAttribute(element, ICISM_PREFIX, COMPILATION_REASON_NAME, icNamespace, getCompilationReason());
		Util.addAttribute(element, ICISM_PREFIX, DERIVATIVELY_CLASSIFIED_BY_NAME, icNamespace, getDerivativelyClassifiedBy());
		Util.addAttribute(element, ICISM_PREFIX, CLASSIFICATION_REASON_NAME, icNamespace, getClassificationReason());
		Util.addAttribute(element, ICISM_PREFIX, DERIVED_FROM_NAME, icNamespace, getDerivedFrom());
		if (getDeclassDate() != null)
			Util.addAttribute(element, ICISM_PREFIX, DECLASS_DATE_NAME, icNamespace, getDeclassDate().toXMLFormat());
		Util.addAttribute(element, ICISM_PREFIX, DECLASS_EVENT_NAME, icNamespace, getDeclassEvent());
		Util.addAttribute(element, ICISM_PREFIX, DECLASS_EXCEPTION_NAME, icNamespace, getDeclassException());
		Util.addAttribute(element, ICISM_PREFIX, TYPE_OF_EXEMPTED_SOURCE_NAME, icNamespace, getTypeOfExemptedSource());
		if (getDateOfExemptedSource() != null)
			Util.addAttribute(element, ICISM_PREFIX, DATE_OF_EXEMPTED_SOURCE_NAME, icNamespace, getDateOfExemptedSource().toXMLFormat());
		if (getDeclassManualReview() != null)
			Util.addAttribute(element, ICISM_PREFIX, DECLASS_MANUAL_REVIEW_NAME, icNamespace, getDeclassManualReview().toString());
	}
	
	/**
	 * Checks if any attributes have been set.
	 * @return true if no attributes have values, false otherwise
	 */
	public boolean isEmpty() {
		return (Util.isEmpty(getClassification()) &&
			getOwnerProducers().isEmpty() &&
			getSCIcontrols().isEmpty() &&
			getSARIdentifier().isEmpty() &&
			getDisseminationControls().isEmpty() &&
			getFGIsourceOpen().isEmpty() &&
			getFGIsourceProtected().isEmpty() &&
			getReleasableTo().isEmpty() &&
			getNonICmarkings().isEmpty() &&
			Util.isEmpty(getClassifiedBy()) &&
			Util.isEmpty(getCompilationReason()) &&
			Util.isEmpty(getDerivativelyClassifiedBy()) &&
			Util.isEmpty(getClassificationReason()) &&
			Util.isEmpty(getDerivedFrom()) &&
			getDeclassDate() == null &&
			Util.isEmpty(getDeclassEvent()) &&
			Util.isEmpty(getTypeOfExemptedSource()) &&
			getDateOfExemptedSource() == null &&
			getDeclassManualReview() == null);
	}

	/**
	 * Validates the attribute group. Where appropriate the {@link ISMVocabulary} enumerations are validated.
	 * 
	 * <table class="info">
	 * <tr class="infoHeader">
	 * <th>Rules</th>
	 * </tr>
	 * <tr>
	 * <td class="infoBody">
	 * <li>If set, the classification must be a valid token.</li>
	 * <li>If set, the ownerProducers must be valid tokens.</li>
	 * <li>If set, the SCIcontrols must be valid tokens.</li>
	 * <li>If set, the SARIdentifiers must be valid tokens.</li>
	 * <li>If set, the disseminationControls must be valid tokens.</li>
	 * <li>If set, the FGIsourceOpen must be valid tokens.</li>
	 * <li>If set, the FGIsourceProtected must be valid tokens.</li>
	 * <li>If set, the releasableTo must be valid tokens.</li>
	 * <li>If set, the nonICmarkings must be valid tokens.</li>
	 * <li>(v2.0) The compilationReason cannot be used until DDMS 3.0.</li>
	 * <li>If set, the declassDate is a valid xs:date value.</li>
	 * <li>If set, the declassException must be a valid token.</li>
	 * <li>If set, the typeOfExemptedSource must be a valid token.</li>
	 * <li>If set, the dateOfExemptedSource is a valid xs:date value.</li>
	 * <li>(v3.0) The declassManualReview cannot be used after DDMS 2.0.</li> 
	 * <li>Does NOT do any validation on the constraints described in the DES ISM specification.</li></td>
	 * </tr>
	 * </table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	private void validate() throws InvalidDDMSException {
		boolean isDDDMS20 = DDMSVersion.isCurrentVersion("2.0");
		if (!Util.isEmpty(getClassification())) {
			if (!isDDDMS20 || !ISMVocabulary.usingOldClassification(getClassification()))
				ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_ALL_CLASSIFICATIONS, getClassification());
		}
		for (String op : getOwnerProducers())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_OWNER_PRODUCERS, op);
		for (String sciControls : getSCIcontrols())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_SCI_CONTROLS, sciControls);
		for (String sarId : getSARIdentifier())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_SAR_IDENTIFIER, sarId);
		for (String dissemination : getDisseminationControls())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, dissemination);
		for (String fgiSourceOpen : getFGIsourceOpen())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_FGI_SOURCE_OPEN, fgiSourceOpen);
		for (String fgiSourceProtected : getFGIsourceProtected())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_FGI_SOURCE_PROTECTED, fgiSourceProtected);
		for (String releasableTo : getReleasableTo())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_RELEASABLE_TO, releasableTo);
		for (String nonIC : getNonICmarkings())
			ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_NON_IC_MARKINGS, nonIC);
		if (isDDDMS20 && !Util.isEmpty(getCompilationReason()))
			throw new InvalidDDMSException("The compilationReason attribute cannot be used in DDMS 2.0.");		
		if (getDeclassDate() != null && !getDeclassDate().getXMLSchemaType().equals(DatatypeConstants.DATE))
			throw new InvalidDDMSException("The declassDate must be in the xs:date format (YYYY-MM-DD).");
		if (!Util.isEmpty(getDeclassException())) {
			if (isDDDMS20) {
				// In DDMS 2.0, this can be a list of tokens.
				List<String> values = Arrays.asList(getDeclassException().split(" "));
				for (String value : values)
					ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_DECLASS_EXCEPTION, value);
			}
			else
				ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_DECLASS_EXCEPTION, getDeclassException());
		}
		if (!Util.isEmpty(getTypeOfExemptedSource())) {
			if (isDDDMS20) {
				// In DDMS 2.0, this can be a list of tokens.
				List<String> values = Arrays.asList(getTypeOfExemptedSource().split(" "));
				for (String value : values)
					ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, value);
		
			}
			else
				ISMVocabulary.validateEnumeration(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, getTypeOfExemptedSource());
		}
		if (!isDDDMS20 && getDeclassManualReview() != null)
			throw new InvalidDDMSException("The declassManualReview attribute can only be used in DDMS 2.0.");
		if (getDateOfExemptedSource() != null && !getDateOfExemptedSource().getXMLSchemaType().equals(DatatypeConstants.DATE))
			throw new InvalidDDMSException("The dateOfExemptedSource must be in the xs:date format (YYYY-MM-DD).");		
	}
	
	/**
	 * Returns a list of any warning messages that occurred during validation. Warnings
	 * do not prevent a valid component from being formed.
	 * 
	 * @return a list of warnings
	 */
	public List<ValidationMessage> getValidationWarnings() {
		return (Collections.unmodifiableList(getWarnings()));
	}
	
	/**
	 * Standalone validation method for components which require a classification and ownerProducer.
	 * 
	 * @throws InvalidDDMSException if there is no classification.
	 */
	public void requireClassification() throws InvalidDDMSException {
		Util.requireDDMSValue(CLASSIFICATION_NAME, getClassification());			

		if (getOwnerProducers().size() == 0)
			throw new InvalidDDMSException("At least 1 ownerProducer must be set.");
	}
	
	/**
	 * Creates an HTML representation of these attributes.
	 * 
	 * @param prefix the parent prefix to place in each Meta tag
	 * @return a string representation of the HTML.
	 */
	public String toHTML(String prefix) {
		if (!Util.isEmpty(prefix))
			prefix = prefix + ".";
		StringBuffer html = new StringBuffer();
		html.append(Security.buildHTMLMeta(prefix + CLASSIFICATION_NAME, getClassification(), false));
		html.append(Security.buildHTMLMeta(prefix + OWNER_PRODUCER_NAME, Util.getXsList(getOwnerProducers()), false));
		html.append(Security.buildHTMLMeta(prefix + SCI_CONTROLS_NAME, Util.getXsList(getSCIcontrols()), false));
		html.append(Security.buildHTMLMeta(prefix + SAR_IDENTIFIER_NAME, Util.getXsList(getSARIdentifier()), false));
		html.append(Security.buildHTMLMeta(prefix + DISSEMINATION_CONTROLS_NAME, Util.getXsList(getDisseminationControls()), false));
		html.append(Security.buildHTMLMeta(prefix + FGI_SOURCE_OPEN_NAME, Util.getXsList(getFGIsourceOpen()), false));
		html.append(Security.buildHTMLMeta(prefix + FGI_SOURCE_PROTECTED_NAME, Util.getXsList(getFGIsourceProtected()), false));
		html.append(Security.buildHTMLMeta(prefix + RELEASABLE_TO_NAME, Util.getXsList(getReleasableTo()), false));
		html.append(Security.buildHTMLMeta(prefix + NON_IC_MARKINGS_NAME, Util.getXsList(getNonICmarkings()), false));
		html.append(Security.buildHTMLMeta(prefix + CLASSIFIED_BY_NAME, getClassifiedBy(), false));
		html.append(Security.buildHTMLMeta(prefix + COMPILATION_REASON_NAME, getCompilationReason(), false));
		html.append(Security.buildHTMLMeta(prefix + DERIVATIVELY_CLASSIFIED_BY_NAME, getDerivativelyClassifiedBy(), false));
		html.append(Security.buildHTMLMeta(prefix + CLASSIFICATION_REASON_NAME, getClassificationReason(), false));
		html.append(Security.buildHTMLMeta(prefix + DERIVED_FROM_NAME, getDerivedFrom(), false));
		if (getDeclassDate() != null)
			html.append(Security.buildHTMLMeta(prefix + DECLASS_DATE_NAME, getDeclassDate().toXMLFormat(), false));
		html.append(Security.buildHTMLMeta(prefix + DECLASS_EVENT_NAME, getDeclassEvent(), false));
		html.append(Security.buildHTMLMeta(prefix + DECLASS_EXCEPTION_NAME, getDeclassException(), false));
		html.append(Security.buildHTMLMeta(prefix + TYPE_OF_EXEMPTED_SOURCE_NAME, getTypeOfExemptedSource(), false));
		if (getDateOfExemptedSource() != null)
			html.append(Security.buildHTMLMeta(prefix + DATE_OF_EXEMPTED_SOURCE_NAME, getDateOfExemptedSource().toXMLFormat(), false));
		if (getDeclassManualReview() != null)
			html.append(Security.buildHTMLMeta(prefix + DECLASS_MANUAL_REVIEW_NAME, getDateOfExemptedSource().toXMLFormat(), false));
		return (html.toString());
	}
	
	/**
	 * Creates a Text representation of these attributes.
	 * 
	 * @param prefix the parent prefix to place in each line of text
	 * @return a string representation of the Text
	 */
	public String toText(String prefix) {
		if (!Util.isEmpty(prefix))
			prefix = prefix + " ";
		StringBuffer text = new StringBuffer();
		text.append(Security.buildTextLine(prefix + "Classification", getClassification(), false));
		text.append(Security.buildTextLine(prefix + "ownerProducer", Util.getXsList(getOwnerProducers()), false));
		text.append(Security.buildTextLine(prefix + "SCI Controls", Util.getXsList(getSCIcontrols()), false));
		text.append(Security.buildTextLine(prefix + "SAR Identifier", Util.getXsList(getSARIdentifier()), false));
		text.append(Security.buildTextLine(prefix + "Dissemination Controls", Util.getXsList(getDisseminationControls()), false));
		text.append(Security.buildTextLine(prefix + "FGI Source Open", Util.getXsList(getFGIsourceOpen()), false));
		text.append(Security.buildTextLine(prefix + "FGI Source Protected", Util.getXsList(getFGIsourceProtected()), false));
		text.append(Security.buildTextLine(prefix + "Releasable To", Util.getXsList(getReleasableTo()), false));
		text.append(Security.buildTextLine(prefix + "Non-IC Markings", Util.getXsList(getNonICmarkings()), false));
		text.append(Security.buildTextLine(prefix + "Classified By", getClassifiedBy(), false));
		text.append(Security.buildTextLine(prefix + "Compilation Reason", getCompilationReason(), false));
		text.append(Security.buildTextLine(prefix + "Derivatively Classified By", getDerivativelyClassifiedBy(), false));
		text.append(Security.buildTextLine(prefix + "Classification Reason", getClassificationReason(), false));
		text.append(Security.buildTextLine(prefix + "Derived From", getDerivedFrom(), false));
		if (getDeclassDate() != null)
			text.append(Security.buildTextLine(prefix + "Declass Date", getDeclassDate().toXMLFormat(), false));
		text.append(Security.buildTextLine(prefix + "Declass Event", getDeclassEvent(), false));
		text.append(Security.buildTextLine(prefix + "Declass Exception", getDeclassException(), false));
		text.append(Security.buildTextLine(prefix + "Type Of Exempted Source", getTypeOfExemptedSource(), false));
		if (getDateOfExemptedSource() != null)
			text.append(Security.buildTextLine(prefix + "Date Of Exempted Source", getDateOfExemptedSource().toXMLFormat(), false));
		if (getDeclassManualReview() != null)
			text.append(Security.buildTextLine(prefix + "Declass Manual Review", getDeclassManualReview().toString(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SecurityAttributes))
			return (false);		
		SecurityAttributes test = (SecurityAttributes) obj;
		return (getClassification().equals(test.getClassification()) &&
			Util.listEquals(getOwnerProducers(), test.getOwnerProducers()) &&
			Util.listEquals(getSCIcontrols(), test.getSCIcontrols()) &&
			Util.listEquals(getSARIdentifier(), test.getSARIdentifier()) &&
			Util.listEquals(getDisseminationControls(), test.getDisseminationControls()) &&
			Util.listEquals(getFGIsourceOpen(), test.getFGIsourceOpen()) &&
			Util.listEquals(getFGIsourceProtected(), test.getFGIsourceProtected()) &&
			Util.listEquals(getReleasableTo(), test.getReleasableTo()) &&
			Util.listEquals(getNonICmarkings(), test.getNonICmarkings()) &&
			getClassifiedBy().equals(test.getClassifiedBy()) &&
			getCompilationReason().equals(test.getCompilationReason()) &&
			getDerivativelyClassifiedBy().equals(test.getDerivativelyClassifiedBy()) &&
			getClassificationReason().equals(test.getClassificationReason()) &&
			getDerivedFrom().equals(test.getDerivedFrom()) &&
			Util.nullEquals(getDeclassDate(), test.getDeclassDate()) &&
			getDeclassEvent().equals(test.getDeclassEvent()) &&
			getDeclassException().equals(test.getDeclassException()) &&
			getTypeOfExemptedSource().equals(test.getTypeOfExemptedSource()) &&
			Util.nullEquals(getDateOfExemptedSource(), test.getDateOfExemptedSource()) &&
			Util.nullEquals(getDeclassManualReview(), test.getDeclassManualReview()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = getClassification().hashCode();
		result = 7 * result + getOwnerProducers().hashCode();
		result = 7 * result + getSCIcontrols().hashCode();
		result = 7 * result + getSARIdentifier().hashCode();
		result = 7 * result + getDisseminationControls().hashCode();
		result = 7 * result + getFGIsourceOpen().hashCode();
		result = 7 * result + getFGIsourceProtected().hashCode();
		result = 7 * result + getReleasableTo().hashCode();
		result = 7 * result + getNonICmarkings().hashCode();
		result = 7 * result + getClassifiedBy().hashCode();
		result = 7 * result + getCompilationReason().hashCode();
		result = 7 * result + getDerivativelyClassifiedBy().hashCode();
		result = 7 * result + getClassificationReason().hashCode();
		result = 7 * result + getDerivedFrom().hashCode();
		if (getDeclassDate() != null)
			result = 7 * result + getDeclassDate().hashCode();
		result = 7 * result + getDeclassEvent().hashCode();
		result = 7 * result + getDeclassException().hashCode();
		result = 7 * result + getTypeOfExemptedSource().hashCode();
		if (getDateOfExemptedSource() != null)
			result = 7 * result + getDateOfExemptedSource().hashCode();
		if (getDeclassManualReview() != null)
			result = 7 * result + getDeclassManualReview().hashCode();
		return (result);
	}	

	/**
	 * Accessor for the list of validation warnings.
	 * 
	 * <p>
	 * This is the private copy that should be manipulated during validation.
	 * Lazy initialization.
	 * </p>
	 * 
	 * @return an editable list of warnings
	 */
	protected List<ValidationMessage> getWarnings() {
		if (_warnings == null)
			_warnings = new ArrayList<ValidationMessage>();
		return (_warnings);
	}
	
	/**
	 * Accessor for the classification attribute.
	 */
	public String getClassification() {
		return (Util.getNonNullString(_cachedClassification));
	}

	/**
	 * Accessor for the ownerProducers attribute. Returns a copy.
	 */
	public List<String> getOwnerProducers() {
		return (Collections.unmodifiableList(_cachedOwnerProducers));
	}

	/**
	 * Accessor for the SCIcontrols attribute. Returns a copy.
	 */
	public List<String> getSCIcontrols() {
		return (Collections.unmodifiableList(_cachedSCIcontrols));
	}

	/**
	 * Accessor for the SARIdentifier attribute. Returns a copy.
	 */
	public List<String> getSARIdentifier() {
		return (Collections.unmodifiableList(_cachedSARIdentifier));
	}

	/**
	 * Accessor for the disseminationControls attribute. Returns a copy.
	 */
	public List<String> getDisseminationControls() {
		return (Collections.unmodifiableList(_cachedDisseminationControls));
	}

	/**
	 * Accessor for the FGIsourceOpen attribute. Returns a copy.
	 */
	public List<String> getFGIsourceOpen() {
		return (Collections.unmodifiableList(_cachedFGIsourceOpen));
	}

	/**
	 * Accessor for the FGIsourceProtected attribute. Returns a copy.
	 */
	public List<String> getFGIsourceProtected() {
		return (Collections.unmodifiableList(_cachedFGIsourceProtected));
	}

	/**
	 * Accessor for the releasableTo attribute. Returns a copy.
	 */
	public List<String> getReleasableTo() {
		return (Collections.unmodifiableList(_cachedReleasableTo));
	}

	/**
	 * Accessor for the nonICmarkings attribute. Returns a copy.
	 */
	public List<String> getNonICmarkings() {
		return (Collections.unmodifiableList(_cachedNonICmarkings));
	}

	/**
	 * Accessor for the classifiedBy attribute.
	 */
	public String getClassifiedBy() {
		return (Util.getNonNullString(_cachedClassifiedBy));
	}

	/**
	 * Accessor for the compilationReason attribute.
	 */
	public String getCompilationReason() {
		return (Util.getNonNullString(_cachedCompilationReason));
	}

	/**
	 * Accessor for the derivativelyClassifiedBy attribute.
	 */
	public String getDerivativelyClassifiedBy() {
		return (Util.getNonNullString(_cachedDerivativelyClassifiedBy));
	}

	/**
	 * Accessor for the classificationReason attribute.
	 */
	public String getClassificationReason() {
		return (Util.getNonNullString(_cachedClassificationReason));
	}
	
	/**
	 * Accessor for the derivedFrom attribute.
	 */
	public String getDerivedFrom() {
		return (Util.getNonNullString(_cachedDerivedFrom));
	}

	/**
	 * Accessor for the declassDate attribute. May return null if not set.
	 */
	public XMLGregorianCalendar getDeclassDate() {
		return (_cachedDeclassDate == null ? null : getFactory().newXMLGregorianCalendar(_cachedDeclassDate.toXMLFormat()));
	}

	/**
	 * Accessor for the declassEvent attribute.
	 */
	public String getDeclassEvent() {
		return (Util.getNonNullString(_cachedDeclassEvent));
	}

	/**
	 * Accessor for the declassException attribute. In DDMS 2.0, this could be a list of tokens. This is represented here as a space-delimited
	 * string.
	 */
	public String getDeclassException() {
		return (Util.getNonNullString(_cachedDeclassException));
	}

	/**
	 * Accessor for the typeOfExemptedSource attribute. In DDMS 2.0, this could be a list of tokens. This is represented here as a space-delimited
	 * string.
	 */
	public String getTypeOfExemptedSource() {
		return (Util.getNonNullString(_cachedTypeOfExemptedSource));
	}

	/**
	 * Accessor for the dateOfExemptedSource attribute. May return null if not set.
	 */
	public XMLGregorianCalendar getDateOfExemptedSource() {
		return (_cachedDateOfExemptedSource == null ? null : getFactory().newXMLGregorianCalendar(_cachedDateOfExemptedSource.toXMLFormat()));
	}
	
	/**
	 * Accessor for the declassManualReview attribute. Will be null in DDMS 3.0.
	 */
	public Boolean getDeclassManualReview() {
		return (_cachedDeclassManualReview);
	}
	
	/**
	 * Accesor for the datatype factory
	 */
	private static DatatypeFactory getFactory() {
		return (Util.getDataTypeFactory());
	}
}