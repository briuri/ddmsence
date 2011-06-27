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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractAttributeGroup;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Attribute group for the ICISM markings used throughout DDMS.
 * 
 * <p>In DDMS 3.0, this attribute group can decorate {@link buri.ddmsence.ddms.summary.Description}, 
 * {@link buri.ddmsence.ddms.summary.GeospatialCoverage}, {@link Organization},
 * {@link Person}, {@link buri.ddmsence.ddms.summary.RelatedResources}, {@link Security}, {@link Service}, 
 * {@link buri.ddmsence.ddms.resource.Source}, {@link buri.ddmsence.ddms.summary.SubjectCoverage}, {@link buri.ddmsence.ddms.resource.Subtitle},
 * {@link buri.ddmsence.ddms.summary.TemporalCoverage}, {@link buri.ddmsence.ddms.resource.Title}, {@link Unknown}, 
 * {@link buri.ddmsence.ddms.summary.VirtualCoverage}, or the {@link Resource} itself. In DDMS 2.0, this 
 * attribute group can only decorate {@link buri.ddmsence.ddms.summary.Description}, {@link Organization}, 
 * {@link Person}, {@link buri.ddmsence.ddms.summary.RelatedResources}, 
 * {@link Security}, {@link Service}, {@link buri.ddmsence.ddms.resource.Subtitle}, 
 * {@link buri.ddmsence.ddms.resource.Title}, {@link Unknown}, or the {@link Resource} itself.</p>		
 * 				
 * <p>The DDMS documentation does not provide sample HTML/Text output for every attribute, so a best guess was taken. 
 * In general, the HTML/Text output of security attributes will be prefixed with the name of the element being marked.
 * For example:</p>
 * <ul><code>
 * Title Owner Producer: US<br />
 * &lt;meta name="security.classification" content="U" /&gt;<br />
 * </code></ul></p>
 * <p>
 * When validating this attribute group, the required/optional nature of the classification and ownerProducer
 * attributes are not checked. Because that limitation depends on the parent element (for example, ddms:title
 * requires them, but ddms:creator does not), the parent element should be responsible for checking, via
 * <code>requireClassification()</code>.
 * </p>
 * 
 * <p>
 * At this time, logical validation is only done on the data types of the various attributes, and the controlled
 * vocabulary enumerations behind some of the attributes. Comparisons against the CVEs can be toggled between
 * warnings and errors with the configurable property, <code>icism.cve.validationAsErrors</code>, and a different
 * set of CVEs can be loaded with the configurable property, <code>icism.cve.enumLocation</code>, which should
 * point to a classpath accessible directory containing your enumeration XML files.</p>
 * 
 * <p>I would like to add the complete constraints set from the "XML Data Encoding Specification for Information 
 * Security Marking Metadata" document in the future.
 * </p>
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
public final class SecurityAttributes extends AbstractAttributeGroup {
	
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
	
	private static final Set<String> ALL_NAMES = new HashSet<String>();
	static {
		ALL_NAMES.add(CLASSIFICATION_NAME);
		ALL_NAMES.add(OWNER_PRODUCER_NAME);
		ALL_NAMES.add(SCI_CONTROLS_NAME);
		ALL_NAMES.add(SAR_IDENTIFIER_NAME);
		ALL_NAMES.add(DISSEMINATION_CONTROLS_NAME);
		ALL_NAMES.add(FGI_SOURCE_OPEN_NAME);
		ALL_NAMES.add(FGI_SOURCE_PROTECTED_NAME);
		ALL_NAMES.add(RELEASABLE_TO_NAME);
		ALL_NAMES.add(NON_IC_MARKINGS_NAME);
		ALL_NAMES.add(CLASSIFIED_BY_NAME);
		ALL_NAMES.add(COMPILATION_REASON_NAME);
		ALL_NAMES.add(DERIVATIVELY_CLASSIFIED_BY_NAME);
		ALL_NAMES.add(CLASSIFICATION_REASON_NAME);
		ALL_NAMES.add(DERIVED_FROM_NAME);
		ALL_NAMES.add(DECLASS_DATE_NAME);
		ALL_NAMES.add(DECLASS_EVENT_NAME);
		ALL_NAMES.add(DECLASS_EXCEPTION_NAME);
		ALL_NAMES.add(TYPE_OF_EXEMPTED_SOURCE_NAME);
		ALL_NAMES.add(DATE_OF_EXEMPTED_SOURCE_NAME);
		ALL_NAMES.add(DECLASS_MANUAL_REVIEW_NAME);
	}
	
	/** A set of all SecurityAttribute names which should not be converted into ExtensibleAttributes */
	public static final Set<String> NON_EXTENSIBLE_NAMES = Collections.unmodifiableSet(ALL_NAMES);
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element which is decorated with these attributes.
	 */
	public SecurityAttributes(Element element) throws InvalidDDMSException {
		super(DDMSVersion.getVersionForNamespace(element.getNamespaceURI()));
		String icNamespace = DDMSVersion.getVersionFor(getDDMSVersion()).getIcismNamespace();
		_cachedClassification = element.getAttributeValue(CLASSIFICATION_NAME, icNamespace);
		_cachedOwnerProducers = Util.getXsListAsList(element.getAttributeValue(OWNER_PRODUCER_NAME, icNamespace));
		_cachedSCIcontrols = Util.getXsListAsList(element.getAttributeValue(SCI_CONTROLS_NAME, icNamespace));
		_cachedSARIdentifier = Util.getXsListAsList(element.getAttributeValue(SAR_IDENTIFIER_NAME, icNamespace));
		_cachedDisseminationControls = Util.getXsListAsList(element.getAttributeValue(DISSEMINATION_CONTROLS_NAME, icNamespace));
		_cachedFGIsourceOpen = Util.getXsListAsList(element.getAttributeValue(FGI_SOURCE_OPEN_NAME, icNamespace));
		_cachedFGIsourceProtected = Util.getXsListAsList(element.getAttributeValue(FGI_SOURCE_PROTECTED_NAME, icNamespace));
		_cachedReleasableTo = Util.getXsListAsList(element.getAttributeValue(RELEASABLE_TO_NAME, icNamespace));
		_cachedNonICmarkings = Util.getXsListAsList(element.getAttributeValue(NON_IC_MARKINGS_NAME, icNamespace));		
		_cachedClassifiedBy = element.getAttributeValue(CLASSIFIED_BY_NAME, icNamespace);
		_cachedCompilationReason = element.getAttributeValue(COMPILATION_REASON_NAME, icNamespace);
		_cachedDerivativelyClassifiedBy = element.getAttributeValue(DERIVATIVELY_CLASSIFIED_BY_NAME, icNamespace);
		_cachedClassificationReason = element.getAttributeValue(CLASSIFICATION_REASON_NAME, icNamespace);
		_cachedDerivedFrom = element.getAttributeValue(DERIVED_FROM_NAME, icNamespace);			
		String declassDate = element.getAttributeValue(DECLASS_DATE_NAME, icNamespace);
		if (!Util.isEmpty(declassDate))
			_cachedDeclassDate = getFactory().newXMLGregorianCalendar(declassDate);		
		_cachedDeclassEvent = element.getAttributeValue(DECLASS_EVENT_NAME, icNamespace);
		_cachedDeclassException = element.getAttributeValue(DECLASS_EXCEPTION_NAME, icNamespace);	
		_cachedTypeOfExemptedSource = element.getAttributeValue(TYPE_OF_EXEMPTED_SOURCE_NAME, icNamespace);
		String dateOfExemptedSource = element.getAttributeValue(DATE_OF_EXEMPTED_SOURCE_NAME, icNamespace);
		if (!Util.isEmpty(dateOfExemptedSource))
			_cachedDateOfExemptedSource = getFactory().newXMLGregorianCalendar(dateOfExemptedSource);
		String manualReview = element.getAttributeValue(DECLASS_MANUAL_REVIEW_NAME, icNamespace);
		if (!Util.isEmpty(manualReview))
			_cachedDeclassManualReview = Boolean.valueOf(manualReview);
		validate();
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * <p>
	 * The classification and ownerProducer exist as parameters, and any other security markings are passed in as a
	 * mapping of local attribute names to String values. This approach is a compromise between a constructor with over
	 * seventeen parameters, and the added complexity of a step-by-step factory/builder approach. If any name-value
	 * pairing does not correlate with a valid ICISM attribute, it will be ignored.
	 * </p>
	 * 
	 * <p>
	 * If an attribute mapping appears more than once, the last one in the list will be the one used. If classification
	 * and ownerProducer are included in the Map of other attributes, they will be ignored.
	 * </p>
	 * 
	 * @param classification the classification level, which must be a legal classification type (optional)
	 * @param ownerProducers a list of ownerProducers (optional)
	 * @param otherAttributes a name/value mapping of other ICISM attributes. The value will be a String value, as it
	 *            appears in XML.
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SecurityAttributes(String classification, List<String> ownerProducers, Map<String, String> otherAttributes) throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion());
		if (ownerProducers == null)
			ownerProducers = Collections.emptyList();
		if (otherAttributes == null)
			otherAttributes = Collections.emptyMap();
		_cachedClassification = classification;
		_cachedOwnerProducers = ownerProducers;
		_cachedSCIcontrols = Util.getXsListAsList(otherAttributes.get(SCI_CONTROLS_NAME));
		_cachedSARIdentifier = Util.getXsListAsList(otherAttributes.get(SAR_IDENTIFIER_NAME));
		_cachedDisseminationControls = Util.getXsListAsList(otherAttributes.get(DISSEMINATION_CONTROLS_NAME));
		_cachedFGIsourceOpen = Util.getXsListAsList(otherAttributes.get(FGI_SOURCE_OPEN_NAME));
		_cachedFGIsourceProtected = Util.getXsListAsList(otherAttributes.get(FGI_SOURCE_PROTECTED_NAME));
		_cachedReleasableTo = Util.getXsListAsList(otherAttributes.get(RELEASABLE_TO_NAME));
		_cachedNonICmarkings = Util.getXsListAsList(otherAttributes.get(NON_IC_MARKINGS_NAME));		
		_cachedClassifiedBy = otherAttributes.get(CLASSIFIED_BY_NAME);
		_cachedCompilationReason = otherAttributes.get(COMPILATION_REASON_NAME);
		_cachedDerivativelyClassifiedBy = otherAttributes.get(DERIVATIVELY_CLASSIFIED_BY_NAME);
		_cachedClassificationReason = otherAttributes.get(CLASSIFICATION_REASON_NAME);
		_cachedDerivedFrom = otherAttributes.get(DERIVED_FROM_NAME);			
		String declassDate = otherAttributes.get(DECLASS_DATE_NAME);
		if (!Util.isEmpty(declassDate)) {
			try {
				_cachedDeclassDate = getFactory().newXMLGregorianCalendar(declassDate);
			}
			catch (IllegalArgumentException e) {
				throw new InvalidDDMSException("The ICISM:declassDate attribute is not in a valid date format.");
			}
		}
		_cachedDeclassEvent = otherAttributes.get(DECLASS_EVENT_NAME);
		_cachedDeclassException = otherAttributes.get(DECLASS_EXCEPTION_NAME);	
		_cachedTypeOfExemptedSource = otherAttributes.get(TYPE_OF_EXEMPTED_SOURCE_NAME);
		String dateOfExemptedSource = otherAttributes.get(DATE_OF_EXEMPTED_SOURCE_NAME);
		if (!Util.isEmpty(dateOfExemptedSource)) {
			try {
				_cachedDateOfExemptedSource = getFactory().newXMLGregorianCalendar(dateOfExemptedSource);
			}
			catch (IllegalArgumentException e) {
				throw new InvalidDDMSException("The ICISM:dateOfExemptedSource attribute is not in a valid date format.");
			}
		}
		String manualReview = otherAttributes.get(DECLASS_MANUAL_REVIEW_NAME);
		if (!Util.isEmpty(manualReview))
			_cachedDeclassManualReview = Boolean.valueOf(manualReview);
		validate();
	}
			
	/**
	 * Convenience method to add these attributes onto an existing XOM Element
	 * 
	 * @param element the element to decorate
	 * @throws InvalidDDMSException if the DDMS version of the element is different
	 */
	public void addTo(Element element) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getVersionFor(getDDMSVersion());
		DDMSVersion elementVersion = DDMSVersion.getVersionForNamespace(element.getNamespaceURI());
		if (!version.getNamespace().equals(elementVersion.getNamespace())) {
			throw new InvalidDDMSException("These security attributes cannot decorate a DDMS component with"
				+ " a different DDMS version.");
		}			
		String icNamespace = version.getIcismNamespace();
		String icPrefix = PropertyReader.getProperty("icism.prefix");
		Util.addAttribute(element, icPrefix, CLASSIFICATION_NAME, icNamespace, getClassification());
		Util.addAttribute(element, icPrefix, OWNER_PRODUCER_NAME, icNamespace, Util.getXsList(getOwnerProducers()));
		Util.addAttribute(element, icPrefix, SCI_CONTROLS_NAME, icNamespace, Util.getXsList(getSCIcontrols()));
		Util.addAttribute(element, icPrefix, SAR_IDENTIFIER_NAME, icNamespace, Util.getXsList(getSARIdentifier()));
		Util.addAttribute(element, icPrefix, DISSEMINATION_CONTROLS_NAME, icNamespace, 
			Util.getXsList(getDisseminationControls()));
		Util.addAttribute(element, icPrefix, FGI_SOURCE_OPEN_NAME, icNamespace, Util.getXsList(getFGIsourceOpen()));
		Util.addAttribute(element, icPrefix, FGI_SOURCE_PROTECTED_NAME, icNamespace, 
			Util.getXsList(getFGIsourceProtected()));
		Util.addAttribute(element, icPrefix, RELEASABLE_TO_NAME, icNamespace, Util.getXsList(getReleasableTo()));
		Util.addAttribute(element, icPrefix, NON_IC_MARKINGS_NAME, icNamespace, Util.getXsList(getNonICmarkings()));
		Util.addAttribute(element, icPrefix, CLASSIFIED_BY_NAME, icNamespace, getClassifiedBy());
		Util.addAttribute(element, icPrefix, COMPILATION_REASON_NAME, icNamespace, getCompilationReason());
		Util.addAttribute(element, icPrefix, DERIVATIVELY_CLASSIFIED_BY_NAME, icNamespace,
			getDerivativelyClassifiedBy());
		Util.addAttribute(element, icPrefix, CLASSIFICATION_REASON_NAME, icNamespace, getClassificationReason());
		Util.addAttribute(element, icPrefix, DERIVED_FROM_NAME, icNamespace, getDerivedFrom());
		if (getDeclassDate() != null)
			Util.addAttribute(element, icPrefix, DECLASS_DATE_NAME, icNamespace, getDeclassDate().toXMLFormat());
		Util.addAttribute(element, icPrefix, DECLASS_EVENT_NAME, icNamespace, getDeclassEvent());
		Util.addAttribute(element, icPrefix, DECLASS_EXCEPTION_NAME, icNamespace, getDeclassException());
		Util.addAttribute(element, icPrefix, TYPE_OF_EXEMPTED_SOURCE_NAME, icNamespace, getTypeOfExemptedSource());
		if (getDateOfExemptedSource() != null)
			Util.addAttribute(element, icPrefix, DATE_OF_EXEMPTED_SOURCE_NAME, icNamespace,
				getDateOfExemptedSource().toXMLFormat());
		if (getDeclassManualReview() != null) {
			Util.addAttribute(element, icPrefix, DECLASS_MANUAL_REVIEW_NAME, icNamespace, 
				getDeclassManualReview().toString());
		}
	}

	/**
	 * Checks if any attributes have been set.
	 * 
	 * @return true if no attributes have values, false otherwise
	 */
	public boolean isEmpty() {
		return (Util.isEmpty(getClassification()) 
			&& getOwnerProducers().isEmpty() 
			&& getSCIcontrols().isEmpty()
			&& getSARIdentifier().isEmpty() 
			&& getDisseminationControls().isEmpty() 
			&& getFGIsourceOpen().isEmpty()
			&& getFGIsourceProtected().isEmpty() 
			&& getReleasableTo().isEmpty() 
			&& getNonICmarkings().isEmpty()
			&& Util.isEmpty(getClassifiedBy()) 
			&& Util.isEmpty(getCompilationReason())
			&& Util.isEmpty(getDerivativelyClassifiedBy()) 
			&& Util.isEmpty(getClassificationReason())
			&& Util.isEmpty(getDerivedFrom()) 
			&& getDeclassDate() == null && Util.isEmpty(getDeclassEvent())
			&& Util.isEmpty(getTypeOfExemptedSource()) 
			&& getDateOfExemptedSource() == null 
			&& getDeclassManualReview() == null);
	}

	/**
	 * Validates the attribute group. Where appropriate the {@link ISMVocabulary} enumerations are validated.
	 * For any validation rule in which the value "must be a valid token", the configurable property,
	 * <code>icism.cve.validationAsErrors</code> determines whether the results of these checks are returned
	 * as errors or warnings. The default behavior is to return errors when a value is not found in a controlled
	 * vocabulary. Note that this property does not affect other types of rules -- for example, using 
	 * "compliationReason" on a DDMS 2.0 component will always result in an error.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
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
	 * <li>Does NOT do any validation on the constraints described in the DES ISM specification.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		boolean isDDDMS20 = "2.0".equals(getDDMSVersion());
		if (!Util.isEmpty(getClassification())) {
			if (!isDDDMS20 || !ISMVocabulary.usingOldClassification(getClassification()))
				validateEnumeration(ISMVocabulary.CVE_ALL_CLASSIFICATIONS, getClassification());
		}
		for (String op : getOwnerProducers())
			validateEnumeration(ISMVocabulary.CVE_OWNER_PRODUCERS, op);
		for (String sciControls : getSCIcontrols())
			validateEnumeration(ISMVocabulary.CVE_SCI_CONTROLS, sciControls);
		for (String sarId : getSARIdentifier())
			validateEnumeration(ISMVocabulary.CVE_SAR_IDENTIFIER, sarId);
		for (String dissemination : getDisseminationControls())
			validateEnumeration(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, dissemination);
		for (String fgiSourceOpen : getFGIsourceOpen())
			validateEnumeration(ISMVocabulary.CVE_FGI_SOURCE_OPEN, fgiSourceOpen);
		for (String fgiSourceProtected : getFGIsourceProtected())
			validateEnumeration(ISMVocabulary.CVE_FGI_SOURCE_PROTECTED, fgiSourceProtected);
		for (String releasableTo : getReleasableTo())
			validateEnumeration(ISMVocabulary.CVE_RELEASABLE_TO, releasableTo);
		for (String nonIC : getNonICmarkings())
			validateEnumeration(ISMVocabulary.CVE_NON_IC_MARKINGS, nonIC);
		if (isDDDMS20 && !Util.isEmpty(getCompilationReason()))
			throw new InvalidDDMSException("The compilationReason attribute cannot be used in DDMS 2.0.");		
		if (getDeclassDate() != null && !getDeclassDate().getXMLSchemaType().equals(DatatypeConstants.DATE))
			throw new InvalidDDMSException("The declassDate must be in the xs:date format (YYYY-MM-DD).");
		if (!Util.isEmpty(getDeclassException())) {
			if (isDDDMS20) {
				// In DDMS 2.0, this can be a list of tokens.
				for (String value : Util.getXsListAsList(getDeclassException()))
					validateEnumeration(ISMVocabulary.CVE_DECLASS_EXCEPTION, value);
			}
			else
				validateEnumeration(ISMVocabulary.CVE_DECLASS_EXCEPTION, getDeclassException());
		}
		if (!Util.isEmpty(getTypeOfExemptedSource())) {
			if (isDDDMS20) {
				// In DDMS 2.0, this can be a list of tokens.
				for (String value : Util.getXsListAsList(getTypeOfExemptedSource()))
					validateEnumeration(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, value);
			}
			else
				validateEnumeration(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, getTypeOfExemptedSource());
		}
		if (!isDDDMS20 && getDeclassManualReview() != null)
			throw new InvalidDDMSException("The declassManualReview attribute cannot be used in DDMS 3.0.");
		if (getDateOfExemptedSource() != null
			&& !getDateOfExemptedSource().getXMLSchemaType().equals(DatatypeConstants.DATE))
			throw new InvalidDDMSException("The dateOfExemptedSource must be in the xs:date format (YYYY-MM-DD).");
	}
	
	/**
	 * Helper method to validate a value from a controlled vocabulary. This is the delegate that handles whether a
	 * bad validation should result in a warning or error, based on the configurable property, "icism.cve.validationAsErrors".
	 * 
	 * @param enumerationKey the key of the enumeration
	 * @param value the test value
	 * @throws InvalidDDMSException if the value is not and validation should result in errors
	 */
	private void validateEnumeration(String enumerationKey, String value) throws InvalidDDMSException {
		boolean validationAsErrors = Boolean.valueOf(PropertyReader.getProperty("icism.cve.validationAsErrors")).booleanValue();
		if (validationAsErrors)
			ISMVocabulary.validateEnumeration(enumerationKey, value);
		else {
			if (!ISMVocabulary.enumContains(enumerationKey, value))
				getWarnings().add(ValidationMessage.newWarning(ISMVocabulary.getInvalidMessage(enumerationKey, value), null));
		}
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
		html.append(Security.buildHTMLMeta(prefix + DISSEMINATION_CONTROLS_NAME, 
			Util.getXsList(getDisseminationControls()), false));
		html.append(Security.buildHTMLMeta(prefix + FGI_SOURCE_OPEN_NAME, Util.getXsList(getFGIsourceOpen()), false));
		html.append(Security.buildHTMLMeta(prefix + FGI_SOURCE_PROTECTED_NAME, Util.getXsList(getFGIsourceProtected()),
			false));
		html.append(Security.buildHTMLMeta(prefix + RELEASABLE_TO_NAME, Util.getXsList(getReleasableTo()), false));
		html.append(Security.buildHTMLMeta(prefix + NON_IC_MARKINGS_NAME, Util.getXsList(getNonICmarkings()), false));
		html.append(Security.buildHTMLMeta(prefix + CLASSIFIED_BY_NAME, getClassifiedBy(), false));
		html.append(Security.buildHTMLMeta(prefix + COMPILATION_REASON_NAME, getCompilationReason(), false));
		html.append(Security.buildHTMLMeta(prefix + DERIVATIVELY_CLASSIFIED_BY_NAME, getDerivativelyClassifiedBy(),
			false));
		html.append(Security.buildHTMLMeta(prefix + CLASSIFICATION_REASON_NAME, getClassificationReason(), false));
		html.append(Security.buildHTMLMeta(prefix + DERIVED_FROM_NAME, getDerivedFrom(), false));
		if (getDeclassDate() != null)
			html.append(Security.buildHTMLMeta(prefix + DECLASS_DATE_NAME, getDeclassDate().toXMLFormat(), false));
		html.append(Security.buildHTMLMeta(prefix + DECLASS_EVENT_NAME, getDeclassEvent(), false));
		html.append(Security.buildHTMLMeta(prefix + DECLASS_EXCEPTION_NAME, getDeclassException(), false));
		html.append(Security.buildHTMLMeta(prefix + TYPE_OF_EXEMPTED_SOURCE_NAME, getTypeOfExemptedSource(), false));
		if (getDateOfExemptedSource() != null) {
			html.append(Security.buildHTMLMeta(prefix + DATE_OF_EXEMPTED_SOURCE_NAME, 
				getDateOfExemptedSource().toXMLFormat(), false));
		}
		if (getDeclassManualReview() != null) {
			html.append(Security.buildHTMLMeta(prefix + DECLASS_MANUAL_REVIEW_NAME, 
				getDeclassManualReview().toString(), false));
		}
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
		text.append(Security.buildTextLine(prefix + "Dissemination Controls", 
			Util.getXsList(getDisseminationControls()), false));
		text.append(Security.buildTextLine(prefix + "FGI Source Open", Util.getXsList(getFGIsourceOpen()), false));
		text.append(Security.buildTextLine(prefix + "FGI Source Protected", Util.getXsList(getFGIsourceProtected()),
			false));
		text.append(Security.buildTextLine(prefix + "Releasable To", Util.getXsList(getReleasableTo()), false));
		text.append(Security.buildTextLine(prefix + "Non-IC Markings", Util.getXsList(getNonICmarkings()), false));
		text.append(Security.buildTextLine(prefix + "Classified By", getClassifiedBy(), false));
		text.append(Security.buildTextLine(prefix + "Compilation Reason", getCompilationReason(), false));
		text.append(Security.buildTextLine(prefix + "Derivatively Classified By", getDerivativelyClassifiedBy(), 
			false));
		text.append(Security.buildTextLine(prefix + "Classification Reason", getClassificationReason(), false));
		text.append(Security.buildTextLine(prefix + "Derived From", getDerivedFrom(), false));
		if (getDeclassDate() != null)
			text.append(Security.buildTextLine(prefix + "Declass Date", getDeclassDate().toXMLFormat(), false));
		text.append(Security.buildTextLine(prefix + "Declass Event", getDeclassEvent(), false));
		text.append(Security.buildTextLine(prefix + "Declass Exception", getDeclassException(), false));
		text.append(Security.buildTextLine(prefix + "Type Of Exempted Source", getTypeOfExemptedSource(), false));
		if (getDateOfExemptedSource() != null) {
			text.append(Security.buildTextLine(prefix + "Date Of Exempted Source", 
				getDateOfExemptedSource().toXMLFormat(), false));
		}
		if (getDeclassManualReview() != null) {
			text.append(Security.buildTextLine(prefix + "Declass Manual Review", getDeclassManualReview().toString(),
				false));
		}
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof SecurityAttributes))
			return (false);		
		SecurityAttributes test = (SecurityAttributes) obj;
		return (getClassification().equals(test.getClassification())
			&& Util.listEquals(getOwnerProducers(), test.getOwnerProducers())
			&& Util.listEquals(getSCIcontrols(), test.getSCIcontrols())
			&& Util.listEquals(getSARIdentifier(), test.getSARIdentifier())
			&& Util.listEquals(getDisseminationControls(), test.getDisseminationControls())
			&& Util.listEquals(getFGIsourceOpen(), test.getFGIsourceOpen())
			&& Util.listEquals(getFGIsourceProtected(), test.getFGIsourceProtected())
			&& Util.listEquals(getReleasableTo(), test.getReleasableTo())
			&& Util.listEquals(getNonICmarkings(), test.getNonICmarkings())
			&& getClassifiedBy().equals(test.getClassifiedBy())
			&& getCompilationReason().equals(test.getCompilationReason())
			&& getDerivativelyClassifiedBy().equals(test.getDerivativelyClassifiedBy())
			&& getClassificationReason().equals(test.getClassificationReason())
			&& getDerivedFrom().equals(test.getDerivedFrom())
			&& Util.nullEquals(getDeclassDate(), test.getDeclassDate())
			&& getDeclassEvent().equals(test.getDeclassEvent())
			&& getDeclassException().equals(test.getDeclassException())
			&& getTypeOfExemptedSource().equals(test.getTypeOfExemptedSource())
			&& Util.nullEquals(getDateOfExemptedSource(), test.getDateOfExemptedSource()) 
			&& Util.nullEquals(getDeclassManualReview(), test.getDeclassManualReview()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getClassification().hashCode();
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
		return (_cachedDeclassDate == null ? null 
			: getFactory().newXMLGregorianCalendar(_cachedDeclassDate.toXMLFormat()));
	}

	/**
	 * Accessor for the declassEvent attribute.
	 */
	public String getDeclassEvent() {
		return (Util.getNonNullString(_cachedDeclassEvent));
	}

	/**
	 * Accessor for the declassException attribute. In DDMS 2.0, this could be a list of tokens. This is represented
	 * here as a space-delimited string.
	 */
	public String getDeclassException() {
		return (Util.getNonNullString(_cachedDeclassException));
	}

	/**
	 * Accessor for the typeOfExemptedSource attribute. In DDMS 2.0, this could be a list of tokens. This is represented
	 * here as a space-delimited string.
	 */
	public String getTypeOfExemptedSource() {
		return (Util.getNonNullString(_cachedTypeOfExemptedSource));
	}

	/**
	 * Accessor for the dateOfExemptedSource attribute. May return null if not set.
	 */
	public XMLGregorianCalendar getDateOfExemptedSource() {
		return (_cachedDateOfExemptedSource == null ? null 
			: getFactory().newXMLGregorianCalendar(_cachedDateOfExemptedSource.toXMLFormat()));
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
	
	/**
	 * Builder for these attributes.
	 * 
	 * <p>This class does not implement the IBuilder interface, because the behavior of commit() is at odds with the standard
	 * commit() method. As an attribute group, an empty attribute group will always be returned instead of null.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements Serializable {
		private static final long serialVersionUID = 279072341662308051L;
		private String _classification = null;
		private List<String> _ownerProducers = null;
		private List<String> _SCIcontrols = null;
		private List<String> _SARIdentifier = null;
		private List<String> _disseminationControls = null;
		private List<String> _FGIsourceOpen = null;
		private List<String> _FGIsourceProtected = null;
		private List<String> _releasableTo = null;
		private List<String> _nonICmarkings = null;
		private String _classifiedBy = null;
		private String _compilationReason = null;
		private String _derivativelyClassifiedBy = null;
		private String _classificationReason = null;
		private String _derivedFrom = null;	
		private String _declassDate = null;
		private String _declassEvent = null;
		private String _declassException = null;
		private String _typeOfExemptedSource = null;
		private String _dateOfExemptedSource = null;
		private Boolean _declassManualReview = null;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(SecurityAttributes attributes) {
			setClassification(attributes.getClassification());
			setOwnerProducers(attributes.getOwnerProducers());
			setSCIcontrols(attributes.getSCIcontrols());
			setSARIdentifier(attributes.getSARIdentifier());
			setDisseminationControls(attributes.getDisseminationControls());
			setFGIsourceOpen(attributes.getFGIsourceOpen());
			setFGIsourceProtected(attributes.getFGIsourceProtected());
			setReleasableTo(attributes.getReleasableTo());
			setNonICmarkings(attributes.getNonICmarkings());
			setClassifiedBy(attributes.getClassifiedBy());
			setCompilationReason(attributes.getCompilationReason());
			setDerivativelyClassifiedBy(attributes.getDerivativelyClassifiedBy());
			setClassificationReason(attributes.getClassificationReason());
			setDerivedFrom(attributes.getDerivedFrom());
			if (attributes.getDeclassDate() != null)
				setDeclassDate(attributes.getDeclassDate().toXMLFormat());
			setDeclassEvent(attributes.getDeclassEvent());
			setDeclassException(attributes.getDeclassException());
			setTypeOfExemptedSource(attributes.getTypeOfExemptedSource());
			if (attributes.getDateOfExemptedSource() != null)
				setDateOfExemptedSource(attributes.getDateOfExemptedSource().toXMLFormat());
			if (attributes.getDeclassManualReview() != null)
				setDeclassManualReview(attributes.getDeclassManualReview());
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. Will always return an empty instance instead of
		 * a null one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public SecurityAttributes commit() throws InvalidDDMSException {
			Map<String, String> otherAttributes = new HashMap<String, String>();
			otherAttributes.put(SCI_CONTROLS_NAME, Util.getXsList(getSCIcontrols()));
			otherAttributes.put(SAR_IDENTIFIER_NAME, Util.getXsList(getSARIdentifier()));
			otherAttributes.put(DISSEMINATION_CONTROLS_NAME, Util.getXsList(getDisseminationControls()));
			otherAttributes.put(FGI_SOURCE_OPEN_NAME, Util.getXsList(getFGIsourceOpen()));
			otherAttributes.put(FGI_SOURCE_PROTECTED_NAME, Util.getXsList(getFGIsourceProtected()));
			otherAttributes.put(RELEASABLE_TO_NAME, Util.getXsList(getReleasableTo()));
			otherAttributes.put(NON_IC_MARKINGS_NAME, Util.getXsList(getNonICmarkings()));
			otherAttributes.put(CLASSIFIED_BY_NAME, getClassifiedBy());
			otherAttributes.put(COMPILATION_REASON_NAME, getCompilationReason());
			otherAttributes.put(DERIVATIVELY_CLASSIFIED_BY_NAME, getDerivativelyClassifiedBy());
			otherAttributes.put(CLASSIFICATION_REASON_NAME, getClassificationReason());
			otherAttributes.put(DERIVED_FROM_NAME, getDerivedFrom());
			otherAttributes.put(DECLASS_DATE_NAME, getDeclassDate());
			otherAttributes.put(DECLASS_EVENT_NAME, getDeclassEvent());
			otherAttributes.put(DECLASS_EXCEPTION_NAME, getDeclassException());
			otherAttributes.put(TYPE_OF_EXEMPTED_SOURCE_NAME, getTypeOfExemptedSource());
			otherAttributes.put(DATE_OF_EXEMPTED_SOURCE_NAME, getDateOfExemptedSource());
			if (getDeclassManualReview() != null)
				otherAttributes.put(DECLASS_MANUAL_REVIEW_NAME, getDeclassManualReview().toString());
			return (new SecurityAttributes(getClassification(), getOwnerProducers(), otherAttributes));
		}
		
		/**
		 * Checks if any values have been provided for this Builder.
		 * 
		 * @return true if every field is empty
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getClassification())
				&& Util.containsOnlyEmptyValues(getOwnerProducers())
				&& Util.containsOnlyEmptyValues(getSCIcontrols())
				&& Util.containsOnlyEmptyValues(getSARIdentifier())
				&& Util.containsOnlyEmptyValues(getDisseminationControls())
				&& Util.containsOnlyEmptyValues(getFGIsourceOpen())
				&& Util.containsOnlyEmptyValues(getFGIsourceProtected())
				&& Util.containsOnlyEmptyValues(getReleasableTo())
				&& Util.containsOnlyEmptyValues(getNonICmarkings())
				&& Util.isEmpty(getClassifiedBy())
				&& Util.isEmpty(getCompilationReason())
				&& Util.isEmpty(getDerivativelyClassifiedBy())
				&& Util.isEmpty(getClassificationReason())
				&& Util.isEmpty(getDerivedFrom())
				&& Util.isEmpty(getDeclassDate())
				&& Util.isEmpty(getDeclassEvent())
				&& Util.isEmpty(getDeclassException())
				&& Util.isEmpty(getTypeOfExemptedSource())
				&& Util.isEmpty(getDateOfExemptedSource())
				&& getDeclassManualReview() == null);				
		}
		
		/**
		 * Builder accessor for the classification attribute
		 */
		public String getClassification() {
			return _classification;
		}
		
		/**
		 * Builder accessor for the classification attribute
		 */
		public void setClassification(String classification) {
			_classification = classification;
		}
		
		/**
		 * Builder accessor for the ownerProducers attribute
		 */
		public List<String> getOwnerProducers() {
			if (_ownerProducers == null)
				_ownerProducers = new LazyList(String.class);
			return _ownerProducers;
		}
		
		/**
		 * Builder accessor for the ownerProducers attribute
		 */
		public void setOwnerProducers(List<String> ownerProducers) {
			_ownerProducers = new LazyList(ownerProducers, String.class);
		}
		
		/**
		 * Builder accessor for the SCIcontrols attribute
		 */
		public List<String> getSCIcontrols() {
			if (_SCIcontrols == null)
				_SCIcontrols = new LazyList(String.class);
			return _SCIcontrols;
		}
		
		/**
		 * Builder accessor for the SCIcontrols attribute
		 */
		public void setSCIcontrols(List<String> SCIcontrols) {
			_SCIcontrols = new LazyList(SCIcontrols, String.class);
		}
		
		/**
		 * Builder accessor for the SARIdentifier attribute
		 */
		public List<String> getSARIdentifier() {
			if (_SARIdentifier == null)
				_SARIdentifier = new LazyList(String.class);
			return _SARIdentifier;
		}
		
		/**
		 * Builder accessor for the SARIdentifier attribute
		 */
		public void setSARIdentifier(List<String> SARIdentifier) {
			_SARIdentifier = new LazyList(SARIdentifier, String.class);
		}
		
		/**
		 * Builder accessor for the disseminationControls attribute
		 */
		public List<String> getDisseminationControls() {
			if (_disseminationControls == null)
				_disseminationControls = new LazyList(String.class);
			return _disseminationControls;
		}
		
		/**
		 * Builder accessor for the disseminationControls attribute
		 */
		public void setDisseminationControls(List<String> disseminationControls) {
			_disseminationControls = new LazyList(disseminationControls, String.class);
		}
		
		/**
		 * Builder accessor for the FGIsourceOpen attribute
		 */
		public List<String> getFGIsourceOpen() {
			if (_FGIsourceOpen == null)
				_FGIsourceOpen = new LazyList(String.class);
			return _FGIsourceOpen;
		}
		
		/**
		 * Builder accessor for the FGIsourceOpen attribute
		 */
		public void setFGIsourceOpen(List<String> FGIsourceOpen) {
			_FGIsourceOpen = new LazyList(FGIsourceOpen, String.class);
		}
		
		/**
		 * Builder accessor for the FGIsourceProtected attribute
		 */
		public List<String> getFGIsourceProtected() {
			if (_FGIsourceProtected == null)
				_FGIsourceProtected = new LazyList(String.class);			
			return _FGIsourceProtected;
		}
		
		/**
		 * Builder accessor for the FGIsourceProtected attribute
		 */
		public void setFGIsourceProtected(List<String> FGIsourceProtected) {
			_FGIsourceProtected = new LazyList(FGIsourceProtected, String.class);
		}
		
		/**
		 * Builder accessor for the releasableTo attribute
		 */
		public List<String> getReleasableTo() {
			if (_releasableTo == null)
				_releasableTo = new LazyList(String.class);
			return _releasableTo;
		}
		
		/**
		 * Builder accessor for the releasableTo attribute
		 */
		public void setReleasableTo(List<String> releasableTo) {
			_releasableTo = new LazyList(releasableTo, String.class);
		}
		
		/**
		 * Builder accessor for the nonICmarkings attribute
		 */
		public List<String> getNonICmarkings() {
			if (_nonICmarkings == null)
				_nonICmarkings = new LazyList(String.class);
			return _nonICmarkings;
		}
		
		/**
		 * Builder accessor for the nonICmarkings attribute
		 */
		public void setNonICmarkings(List<String> nonICmarkings) {
			_nonICmarkings = new LazyList(nonICmarkings, String.class);
		}
		
		/**
		 * Builder accessor for the classifiedBy attribute
		 */
		public String getClassifiedBy() {
			return _classifiedBy;
		}
		
		/**
		 * Builder accessor for the classifiedBy attribute
		 */
		public void setClassifiedBy(String classifiedBy) {
			_classifiedBy = classifiedBy;
		}
		
		/**
		 * Builder accessor for the compilationReason attribute
		 */
		public String getCompilationReason() {
			return _compilationReason;
		}
		
		/**
		 * Builder accessor for the compilationReason attribute
		 */
		public void setCompilationReason(String compilationReason) {
			_compilationReason = compilationReason;
		}
		
		/**
		 * Builder accessor for the derivativelyClassifiedBy attribute
		 */
		public String getDerivativelyClassifiedBy() {
			return _derivativelyClassifiedBy;
		}
		
		/**
		 * Builder accessor for the derivativelyClassifiedBy attribute
		 */
		public void setDerivativelyClassifiedBy(String derivativelyClassifiedBy) {
			_derivativelyClassifiedBy = derivativelyClassifiedBy;
		}
		
		/**
		 * Builder accessor for the classificationReason attribute
		 */
		public String getClassificationReason() {
			return _classificationReason;
		}
		
		/**
		 * Builder accessor for the classificationReason attribute
		 */
		public void setClassificationReason(String classificationReason) {
			_classificationReason = classificationReason;
		}
		
		/**
		 * Builder accessor for the derivedFrom attribute
		 */
		public String getDerivedFrom() {
			return _derivedFrom;
		}
		
		/**
		 * Builder accessor for the derivedFrom attribute
		 */
		public void setDerivedFrom(String derivedFrom) {
			_derivedFrom = derivedFrom;
		}
		
		/**
		 * Builder accessor for the declassDate attribute
		 */
		public String getDeclassDate() {
			return _declassDate;
		}
		
		/**
		 * Builder accessor for the declassDate attribute
		 */
		public void setDeclassDate(String declassDate) {
			_declassDate = declassDate;
		}
		
		/**
		 * Builder accessor for the declassEvent attribute
		 */
		public String getDeclassEvent() {
			return _declassEvent;
		}
		
		/**
		 * Builder accessor for the declassEvent attribute
		 */
		public void setDeclassEvent(String declassEvent) {
			_declassEvent = declassEvent;
		}
		
		/**
		 * Builder accessor for the declassException attribute
		 */
		public String getDeclassException() {
			return _declassException;
		}
		
		/**
		 * Builder accessor for the declassException attribute
		 */
		public void setDeclassException(String declassException) {
			_declassException = declassException;
		}
		
		/**
		 * Builder accessor for the typeOfExemptedSource attribute
		 */
		public String getTypeOfExemptedSource() {
			return _typeOfExemptedSource;
		}
		
		/**
		 * Builder accessor for the typeOfExemptedSource attribute
		 */
		public void setTypeOfExemptedSource(String typeOfExemptedSource) {
			_typeOfExemptedSource = typeOfExemptedSource;
		}
		
		/**
		 * Builder accessor for the dateOfExemptedSource attribute
		 */
		public String getDateOfExemptedSource() {
			return _dateOfExemptedSource;
		}
		
		/**
		 * Builder accessor for the dateOfExemptedSource attribute
		 */
		public void setDateOfExemptedSource(String dateOfExemptedSource) {
			_dateOfExemptedSource = dateOfExemptedSource;
		}
		
		/**
		 * Builder accessor for the declassManualReview attribute
		 */
		public Boolean getDeclassManualReview() {
			return _declassManualReview;
		}
		
		/**
		 * Builder accessor for the declassManualReview attribute
		 */
		public void setDeclassManualReview(Boolean declassManualReview) {
			_declassManualReview = declassManualReview;
		}
	}
}