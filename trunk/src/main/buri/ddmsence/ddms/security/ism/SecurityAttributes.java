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
package buri.ddmsence.ddms.security.ism;

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
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Attribute group for the ISM markings used throughout DDMS.
 * 
 * <p>The DDMS documentation does not provide sample HTML/Text output for every attribute, so a best guess was taken. 
 * In general, the HTML/Text output of security attributes will be prefixed with the name of the element being marked.
 * For example:</p>
 * <ul><code>
 * title ownerProducer: US<br />
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
 * set of CVEs can be loaded with the configurable property, <code>icism.cve.customEnumLocation</code>, which should
 * point to a classpath accessible directory containing your enumeration XML files.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ISM:atomicEnergyMarkings</u>: (optional, starting in DDMS 3.1)<br />
 * <u>ISM:classification</u>: (optional)<br />
 * <u>ISM:classificationReason</u>: (optional)<br />
 * <u>ISM:classifiedBy</u>: (optional)<br />
 * <u>ISM:compilationReason</u>: (optional, starting in DDMS 3.0)<br />
 * <u>ISM:compliesWith</u>: (optional, v3.1 only)<br />
 * <u>ISM:dateOfExemptedSource</u>: (optional, DDMS 2.0 and 3.0 only)<br />
 * <u>ISM:declassDate</u>: (optional)<br />
 * <u>ISM:declassEvent</u>: (optional)<br />
 * <u>ISM:declassException</u>: (optional)<br />
 * <u>ISM:declassManualReview</u>: (optional, DDMS 2.0 only)<br />
 * <u>ISM:derivativelyClassifiedBy</u>: (optional)<br />
 * <u>ISM:derivedFrom</u>: (optional)<br />
 * <u>ISM:displayOnlyTo</u>: (optional, starting in DDMS 3.1)<br />
 * <u>ISM:disseminationControls</u>: (optional)<br />
 * <u>ISM:FGIsourceOpen</u>: (optional)<br />
 * <u>ISM:FGIsourceProtected</u>: (optional)<br />
 * <u>ISM:nonICmarkings</u>: (optional)<br />
 * <u>ISM:nonUSControls</u>: (optional, starting in DDMS 3.1)<br />
 * <u>ISM:ownerProducer</u>: (optional)<br />
 * <u>ISM:releasableTo</u>: (optional)<br />
 * <u>ISM:SARIdentifier</u>: (optional)<br />
 * <u>ISM:SCIcontrols</u>: (optional)<br />
 * <u>ISM:typeOfExemptedSource</u>: (optional, DDMS 2.0 and 3.0 only)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class SecurityAttributes extends AbstractAttributeGroup {
	
	private String _ddmsNamespace = null;

	private List<String> _cachedAtomicEnergyMarkings = null;
	private String _cachedClassification = null;
	private String _cachedClassificationReason = null;
	private String _cachedClassifiedBy = null;
	private String _cachedCompilationReason = null;
	private List<String> _cachedCompliesWith = null;
	private XMLGregorianCalendar _cachedDateOfExemptedSource = null;
	private XMLGregorianCalendar _cachedDeclassDate = null;
	private String _cachedDeclassEvent = null;
	private String _cachedDeclassException = null;
	private Boolean _cachedDeclassManualReview = null;
	private String _cachedDerivativelyClassifiedBy = null;
	private String _cachedDerivedFrom = null;
	private List<String> _cachedDisplayOnlyTo = null;
	private List<String> _cachedDisseminationControls = null;
	private List<String> _cachedFGIsourceOpen = null;
	private List<String> _cachedFGIsourceProtected = null;
	private List<String> _cachedNonICmarkings = null;
	private List<String> _cachedNonUSControls = null;
	private List<String> _cachedOwnerProducers = null;
	private List<String> _cachedReleasableTo = null;
	private List<String> _cachedSARIdentifier = null;	
	private List<String> _cachedSCIcontrols = null;
	private String _cachedTypeOfExemptedSource = null;
		
	/** Attribute name */
	public static final String ATOMIC_ENERGY_MARKINGS_NAME = "atomicEnergyMarkings";
	
	/** Attribute name */
	public static final String CLASSIFICATION_NAME = "classification";
	
	/** Attribute name */
	public static final String CLASSIFICATION_REASON_NAME = "classificationReason";
	
	/** Attribute name */
	public static final String CLASSIFIED_BY_NAME = "classifiedBy";
	
	/** Attribute name */
	public static final String COMPILATION_REASON_NAME = "compilationReason";
	
	/** Attribute name */
	public static final String COMPLIES_WITH_NAME = "compliesWith";
	
	/** Attribute name */
	public static final String DATE_OF_EXEMPTED_SOURCE_NAME = "dateOfExemptedSource";
	
	/** Attribute name */
	public static final String DECLASS_DATE_NAME = "declassDate";
	
	/** Attribute name */
	public static final String DECLASS_EVENT_NAME = "declassEvent";
	
	/** Attribute name */
	public static final String DECLASS_EXCEPTION_NAME = "declassException";	
	
	/** Attribute name */
	public static final String DECLASS_MANUAL_REVIEW_NAME = "declassManualReview";
	
	/** Attribute name */
	public static final String DERIVATIVELY_CLASSIFIED_BY_NAME = "derivativelyClassifiedBy";
	
	/** Attribute name */
	public static final String DERIVED_FROM_NAME = "derivedFrom";

	/** Attribute name */
	public static final String DISPLAY_ONLY_TO_NAME = "displayOnlyTo";
	
	/** Attribute name */
	public static final String DISSEMINATION_CONTROLS_NAME = "disseminationControls";
	
	/** Attribute name */
	public static final String FGI_SOURCE_OPEN_NAME = "FGIsourceOpen";
	
	/** Attribute name */
	public static final String FGI_SOURCE_PROTECTED_NAME = "FGIsourceProtected";
	
	/** Attribute name */
	public static final String NON_IC_MARKINGS_NAME = "nonICmarkings";
	
	/** Attribute name */
	public static final String NON_US_CONTROLS_NAME = "nonUSControls";
	
	/** Attribute name */
	public static final String OWNER_PRODUCER_NAME = "ownerProducer";
	
	/** Attribute name */
	public static final String RELEASABLE_TO_NAME = "releasableTo";
	
	/** Attribute name */
	public static final String SAR_IDENTIFIER_NAME = "SARIdentifier";
	
	/** Attribute name */
	public static final String SCI_CONTROLS_NAME = "SCIcontrols";

	/** Attribute name */
	public static final String TYPE_OF_EXEMPTED_SOURCE_NAME = "typeOfExemptedSource";
			
	private static final Set<String> ALL_NAMES = new HashSet<String>();
	static {
		ALL_NAMES.add(ATOMIC_ENERGY_MARKINGS_NAME);
		ALL_NAMES.add(CLASSIFICATION_NAME);
		ALL_NAMES.add(CLASSIFICATION_REASON_NAME);
		ALL_NAMES.add(CLASSIFIED_BY_NAME);
		ALL_NAMES.add(COMPILATION_REASON_NAME);
		ALL_NAMES.add(COMPLIES_WITH_NAME);
		ALL_NAMES.add(DATE_OF_EXEMPTED_SOURCE_NAME);
		ALL_NAMES.add(DECLASS_DATE_NAME);
		ALL_NAMES.add(DECLASS_EVENT_NAME);
		ALL_NAMES.add(DECLASS_EXCEPTION_NAME);
		ALL_NAMES.add(DECLASS_MANUAL_REVIEW_NAME);
		ALL_NAMES.add(DERIVATIVELY_CLASSIFIED_BY_NAME);
		ALL_NAMES.add(DERIVED_FROM_NAME);
		ALL_NAMES.add(DISPLAY_ONLY_TO_NAME);
		ALL_NAMES.add(DISSEMINATION_CONTROLS_NAME);		
		ALL_NAMES.add(FGI_SOURCE_OPEN_NAME);
		ALL_NAMES.add(FGI_SOURCE_PROTECTED_NAME);
		ALL_NAMES.add(NON_IC_MARKINGS_NAME);
		ALL_NAMES.add(NON_US_CONTROLS_NAME);
		ALL_NAMES.add(OWNER_PRODUCER_NAME);
		ALL_NAMES.add(RELEASABLE_TO_NAME);
		ALL_NAMES.add(SAR_IDENTIFIER_NAME);
		ALL_NAMES.add(SCI_CONTROLS_NAME);
		ALL_NAMES.add(TYPE_OF_EXEMPTED_SOURCE_NAME);
	}
	
	/** A set of all SecurityAttribute names which should not be converted into ExtensibleAttributes */
	public static final Set<String> NON_EXTENSIBLE_NAMES = Collections.unmodifiableSet(ALL_NAMES);
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element which is decorated with these attributes.
	 */
	public SecurityAttributes(Element element) throws InvalidDDMSException {
		_ddmsNamespace = element.getNamespaceURI();
		String icNamespace = DDMSVersion.getVersionForDDMSNamespace(getDDMSNamespace()).getIsmNamespace();
		_cachedAtomicEnergyMarkings = Util.getXsListAsList(element.getAttributeValue(ATOMIC_ENERGY_MARKINGS_NAME,
			icNamespace));
		_cachedClassification = element.getAttributeValue(CLASSIFICATION_NAME, icNamespace);
		_cachedClassificationReason = element.getAttributeValue(CLASSIFICATION_REASON_NAME, icNamespace);
		_cachedClassifiedBy = element.getAttributeValue(CLASSIFIED_BY_NAME, icNamespace);
		_cachedCompilationReason = element.getAttributeValue(COMPILATION_REASON_NAME, icNamespace);
		_cachedCompliesWith = Util.getXsListAsList(element.getAttributeValue(COMPLIES_WITH_NAME, icNamespace));
		String dateOfExemptedSource = element.getAttributeValue(DATE_OF_EXEMPTED_SOURCE_NAME, icNamespace);
		if (!Util.isEmpty(dateOfExemptedSource))
			_cachedDateOfExemptedSource = getFactory().newXMLGregorianCalendar(dateOfExemptedSource);
		String declassDate = element.getAttributeValue(DECLASS_DATE_NAME, icNamespace);
		if (!Util.isEmpty(declassDate))
			_cachedDeclassDate = getFactory().newXMLGregorianCalendar(declassDate);		
		_cachedDeclassEvent = element.getAttributeValue(DECLASS_EVENT_NAME, icNamespace);
		_cachedDeclassException = element.getAttributeValue(DECLASS_EXCEPTION_NAME, icNamespace);	
		String manualReview = element.getAttributeValue(DECLASS_MANUAL_REVIEW_NAME, icNamespace);
		if (!Util.isEmpty(manualReview))
			_cachedDeclassManualReview = Boolean.valueOf(manualReview);
		_cachedDerivativelyClassifiedBy = element.getAttributeValue(DERIVATIVELY_CLASSIFIED_BY_NAME, icNamespace);
		_cachedDerivedFrom = element.getAttributeValue(DERIVED_FROM_NAME, icNamespace);			
		_cachedDisplayOnlyTo = Util.getXsListAsList(element.getAttributeValue(DISPLAY_ONLY_TO_NAME, icNamespace));
		_cachedDisseminationControls = Util.getXsListAsList(element.getAttributeValue(DISSEMINATION_CONTROLS_NAME,
			icNamespace));
		_cachedFGIsourceOpen = Util.getXsListAsList(element.getAttributeValue(FGI_SOURCE_OPEN_NAME, icNamespace));
		_cachedFGIsourceProtected = Util.getXsListAsList(element.getAttributeValue(FGI_SOURCE_PROTECTED_NAME,
			icNamespace));
		_cachedNonICmarkings = Util.getXsListAsList(element.getAttributeValue(NON_IC_MARKINGS_NAME, icNamespace));
	_cachedNonUSControls = Util.getXsListAsList(element.getAttributeValue(NON_US_CONTROLS_NAME, icNamespace));		
		_cachedOwnerProducers = Util.getXsListAsList(element.getAttributeValue(OWNER_PRODUCER_NAME, icNamespace));
		_cachedReleasableTo = Util.getXsListAsList(element.getAttributeValue(RELEASABLE_TO_NAME, icNamespace));
		_cachedSARIdentifier = Util.getXsListAsList(element.getAttributeValue(SAR_IDENTIFIER_NAME, icNamespace));
		_cachedSCIcontrols = Util.getXsListAsList(element.getAttributeValue(SCI_CONTROLS_NAME, icNamespace));
		_cachedTypeOfExemptedSource = element.getAttributeValue(TYPE_OF_EXEMPTED_SOURCE_NAME, icNamespace);
		validate();
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * <p>
	 * The classification and ownerProducer exist as parameters, and any other security markings are passed in as a
	 * mapping of local attribute names to String values. This approach is a compromise between a constructor with over
	 * seventeen parameters, and the added complexity of a step-by-step factory/builder approach. If any name-value
	 * pairing does not correlate with a valid ISM attribute, it will be ignored.
	 * </p>
	 * 
	 * <p>
	 * If an attribute mapping appears more than once, the last one in the list will be the one used. If classification
	 * and ownerProducer are included in the Map of other attributes, they will be ignored.
	 * </p>
	 * 
	 * @param classification the classification level, which must be a legal classification type (optional)
	 * @param ownerProducers a list of ownerProducers (optional)
	 * @param otherAttributes a name/value mapping of other ISM attributes. The value will be a String value, as it
	 *            appears in XML.
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SecurityAttributes(String classification, List<String> ownerProducers, Map<String, String> otherAttributes)
		throws InvalidDDMSException {
		_ddmsNamespace = DDMSVersion.getCurrentVersion().getNamespace();
		if (ownerProducers == null)
			ownerProducers = Collections.emptyList();
		if (otherAttributes == null)
			otherAttributes = Collections.emptyMap();
		
		_cachedAtomicEnergyMarkings = Util.getXsListAsList(otherAttributes.get(ATOMIC_ENERGY_MARKINGS_NAME));
		_cachedClassification = classification;
		_cachedClassificationReason = otherAttributes.get(CLASSIFICATION_REASON_NAME);
		_cachedClassifiedBy = otherAttributes.get(CLASSIFIED_BY_NAME);
		_cachedCompilationReason = otherAttributes.get(COMPILATION_REASON_NAME);
		_cachedCompliesWith = Util.getXsListAsList(otherAttributes.get(COMPLIES_WITH_NAME));
		String dateOfExemptedSource = otherAttributes.get(DATE_OF_EXEMPTED_SOURCE_NAME);
		if (!Util.isEmpty(dateOfExemptedSource)) {
			try {
				_cachedDateOfExemptedSource = getFactory().newXMLGregorianCalendar(dateOfExemptedSource);
			}
			catch (IllegalArgumentException e) {
				throw new InvalidDDMSException("The ISM:dateOfExemptedSource attribute is not in a valid date format.");
			}
		}
		String declassDate = otherAttributes.get(DECLASS_DATE_NAME);
		if (!Util.isEmpty(declassDate)) {
			try {
				_cachedDeclassDate = getFactory().newXMLGregorianCalendar(declassDate);
			}
			catch (IllegalArgumentException e) {
				throw new InvalidDDMSException("The ISM:declassDate attribute is not in a valid date format.");
			}
		}
		_cachedDeclassEvent = otherAttributes.get(DECLASS_EVENT_NAME);
		_cachedDeclassException = otherAttributes.get(DECLASS_EXCEPTION_NAME);
		String manualReview = otherAttributes.get(DECLASS_MANUAL_REVIEW_NAME);
		if (!Util.isEmpty(manualReview))
			_cachedDeclassManualReview = Boolean.valueOf(manualReview);		
		_cachedDerivativelyClassifiedBy = otherAttributes.get(DERIVATIVELY_CLASSIFIED_BY_NAME);
		_cachedDerivedFrom = otherAttributes.get(DERIVED_FROM_NAME);			
		_cachedDisplayOnlyTo = Util.getXsListAsList(otherAttributes.get(DISPLAY_ONLY_TO_NAME));
		_cachedDisseminationControls = Util.getXsListAsList(otherAttributes.get(DISSEMINATION_CONTROLS_NAME));
		_cachedFGIsourceOpen = Util.getXsListAsList(otherAttributes.get(FGI_SOURCE_OPEN_NAME));
		_cachedFGIsourceProtected = Util.getXsListAsList(otherAttributes.get(FGI_SOURCE_PROTECTED_NAME));
		_cachedNonICmarkings = Util.getXsListAsList(otherAttributes.get(NON_IC_MARKINGS_NAME));		
		_cachedNonUSControls = Util.getXsListAsList(otherAttributes.get(NON_US_CONTROLS_NAME));		
		_cachedOwnerProducers = ownerProducers;
		_cachedReleasableTo = Util.getXsListAsList(otherAttributes.get(RELEASABLE_TO_NAME));
		_cachedSARIdentifier = Util.getXsListAsList(otherAttributes.get(SAR_IDENTIFIER_NAME));
		_cachedSCIcontrols = Util.getXsListAsList(otherAttributes.get(SCI_CONTROLS_NAME));		
		_cachedTypeOfExemptedSource = otherAttributes.get(TYPE_OF_EXEMPTED_SOURCE_NAME);		
		validate();
	}
			
	/**
	 * Convenience method to add these attributes onto an existing XOM Element
	 * 
	 * @param element the element to decorate
	 */
	public void addTo(Element element) throws InvalidDDMSException {		
		if (!getDDMSNamespace().equals(element.getNamespaceURI())) {
			throw new InvalidDDMSException("These security attributes cannot decorate a DDMS component with"
				+ " a different DDMS version.");
		}			
		String icNamespace = DDMSVersion.getVersionForDDMSNamespace(element.getNamespaceURI()).getIsmNamespace();
		String icPrefix = PropertyReader.getProperty("ism.prefix");
		
		Util.addAttribute(element, icPrefix, ATOMIC_ENERGY_MARKINGS_NAME, icNamespace,
			Util.getXsList(getAtomicEnergyMarkings()));
		Util.addAttribute(element, icPrefix, CLASSIFICATION_NAME, icNamespace, getClassification());
		Util.addAttribute(element, icPrefix, CLASSIFICATION_REASON_NAME, icNamespace, getClassificationReason());
		Util.addAttribute(element, icPrefix, CLASSIFIED_BY_NAME, icNamespace, getClassifiedBy());
		Util.addAttribute(element, icPrefix, COMPILATION_REASON_NAME, icNamespace, getCompilationReason());
		Util.addAttribute(element, icPrefix, COMPLIES_WITH_NAME, icNamespace, Util.getXsList(getCompliesWith()));
		if (getDateOfExemptedSource() != null)
			Util.addAttribute(element, icPrefix, DATE_OF_EXEMPTED_SOURCE_NAME, icNamespace,
				getDateOfExemptedSource().toXMLFormat());
		if (getDeclassDate() != null)
			Util.addAttribute(element, icPrefix, DECLASS_DATE_NAME, icNamespace, getDeclassDate().toXMLFormat());
		Util.addAttribute(element, icPrefix, DECLASS_EVENT_NAME, icNamespace, getDeclassEvent());
		Util.addAttribute(element, icPrefix, DECLASS_EXCEPTION_NAME, icNamespace, getDeclassException());
		if (getDeclassManualReview() != null) {
			Util.addAttribute(element, icPrefix, DECLASS_MANUAL_REVIEW_NAME, icNamespace, 
				getDeclassManualReview().toString());
		}
		Util.addAttribute(element, icPrefix, DERIVATIVELY_CLASSIFIED_BY_NAME, icNamespace,
			getDerivativelyClassifiedBy());
		Util.addAttribute(element, icPrefix, DERIVED_FROM_NAME, icNamespace, getDerivedFrom());
		Util.addAttribute(element, icPrefix, DISPLAY_ONLY_TO_NAME, icNamespace, Util.getXsList(getDisplayOnlyTo()));
		Util.addAttribute(element, icPrefix, DISSEMINATION_CONTROLS_NAME, icNamespace, 
			Util.getXsList(getDisseminationControls()));
		Util.addAttribute(element, icPrefix, FGI_SOURCE_OPEN_NAME, icNamespace, Util.getXsList(getFGIsourceOpen()));
		Util.addAttribute(element, icPrefix, FGI_SOURCE_PROTECTED_NAME, icNamespace, 
			Util.getXsList(getFGIsourceProtected()));
		Util.addAttribute(element, icPrefix, NON_IC_MARKINGS_NAME, icNamespace, Util.getXsList(getNonICmarkings()));
		Util.addAttribute(element, icPrefix, NON_US_CONTROLS_NAME, icNamespace, Util.getXsList(getNonUSControls()));	
		Util.addAttribute(element, icPrefix, OWNER_PRODUCER_NAME, icNamespace, Util.getXsList(getOwnerProducers()));
		Util.addAttribute(element, icPrefix, RELEASABLE_TO_NAME, icNamespace, Util.getXsList(getReleasableTo()));
		Util.addAttribute(element, icPrefix, SAR_IDENTIFIER_NAME, icNamespace, Util.getXsList(getSARIdentifier()));
		Util.addAttribute(element, icPrefix, SCI_CONTROLS_NAME, icNamespace, Util.getXsList(getSCIcontrols()));
		Util.addAttribute(element, icPrefix, TYPE_OF_EXEMPTED_SOURCE_NAME, icNamespace, getTypeOfExemptedSource());
	}

	/**
	 * Checks if any attributes have been set.
	 * 
	 * @return true if no attributes have values, false otherwise
	 */
	public boolean isEmpty() {
		return (getAtomicEnergyMarkings().isEmpty()
			&& Util.isEmpty(getClassification()) 
			&& Util.isEmpty(getClassificationReason())
			&& Util.isEmpty(getClassifiedBy()) 
			&& Util.isEmpty(getCompilationReason())
			&& getCompliesWith().isEmpty()
			&& getDateOfExemptedSource() == null 
			&& getDeclassDate() == null
			&& Util.isEmpty(getDeclassEvent())
			&& Util.isEmpty(getDeclassException())
			&& getDeclassManualReview() == null
			&& Util.isEmpty(getDerivativelyClassifiedBy()) 
			&& Util.isEmpty(getDerivedFrom()) 
			&& getDisplayOnlyTo().isEmpty()
			&& getDisseminationControls().isEmpty() 
			&& getFGIsourceOpen().isEmpty()
			&& getFGIsourceProtected().isEmpty() 
			&& getNonICmarkings().isEmpty()
			&& getNonUSControls().isEmpty()
			&& getOwnerProducers().isEmpty()
			&& getReleasableTo().isEmpty()
			&& getSARIdentifier().isEmpty()
			&& getSCIcontrols().isEmpty()
			&& Util.isEmpty(getTypeOfExemptedSource()));
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
	 * <li>The atomicEnergyMarkings cannot be used until DDMS 3.1 or later.</li>
	 * <li>If set, the atomicEnergyMarkings attribute must be valid tokens.</li>
	 * <li>If set, the classification attribute must be a valid token.</li>
	 * <li>The compliesWith attribute cannot be used until DDMS 3.1 or later.</li>
	 * <li>If set, the compliesWith attribute must be valid tokens.</li>
	 * <li>The compilationReason attribute cannot be used until DDMS 3.0 or later.</li>
	 * <li>The dateOfExemptedSource attribute can only be used until DDMS 3.1 or later.</li>
	 * <li>If set, the dateOfExemptedSource attribute is a valid xs:date value.</li>
	 * <li>If set, the declassDate attribute is a valid xs:date value.</li>
	 * <li>If set, the declassException attribute must be a valid token.</li>
	 * <li>The declassManualReview attribute cannot be used after DDMS 2.0.</li>
	 * <li>The displayOnlyTo attribute cannot be used until DDMS 3.1 or later.</li>
	 * <li>If set, the displayOnlyTo attribute must be valid tokens.</li>
	 * <li>If set, the disseminationControls attribute must be valid tokens.</li>
	 * <li>If set, the FGIsourceOpen attribute must be valid tokens.</li>
	 * <li>If set, the FGIsourceProtected attribute must be valid tokens.</li>
	 * <li>If set, the nonICmarkings attribute must be valid tokens.</li>
	 * <li>The nonUSControls attribute cannot be used until DDMS 3.1 or later.</li>
	 * <li>If set, the nonUSControls attribute must be valid tokens.</li>	 
	 * <li>If set, the ownerProducers attribute must be valid tokens.</li>
	 * <li>If set, the releasableTo attribute must be valid tokens.</li>
	 * <li>If set, the SARIdentifiers attribute must be valid tokens.</li>
	 * <li>If set, the SCIcontrols attribute must be valid tokens.</li>
	 * <li>The typeOfExemptedSource attribute can only be used in DDMS 2.0 or DDMS 3.0.</li>
	 * <li>If set, the typeOfExemptedSource attribute must be a valid token.</li>
	 *  
	 * <li>Does NOT do any validation on the constraints described in the DES ISM specification.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		// Should be reviewed as additional versions of DDMS are supported.
		DDMSVersion version = DDMSVersion.getVersionForDDMSNamespace(getDDMSNamespace());
		boolean isDDMS20 = "2.0".equals(version.getVersion());
		ISMVocabulary.setDDMSVersion(version);
		
		if (!version.isAtLeast("3.1") && !getAtomicEnergyMarkings().isEmpty())
			throw new InvalidDDMSException("The atomicEnergyMarkings attribute cannot be used until DDMS 3.1 or later.");
		for (String atomic : getAtomicEnergyMarkings())
			validateEnumeration(ISMVocabulary.CVE_ATOMIC_ENERGY_MARKINGS, atomic);
		if (!Util.isEmpty(getClassification())) {
			if (version.isAtLeast("3.0") || !ISMVocabulary.usingOldClassification(getClassification()))
				validateEnumeration(ISMVocabulary.CVE_ALL_CLASSIFICATIONS, getClassification());
		}
		if (!version.isAtLeast("3.1") && !getCompliesWith().isEmpty())
			throw new InvalidDDMSException("The compliesWith attribute cannot be used until DDMS 3.1 or later.");
		for (String with : getCompliesWith())
			validateEnumeration(ISMVocabulary.CVE_COMPLIES_WITH, with);	
		if (!version.isAtLeast("3.0") && !Util.isEmpty(getCompilationReason()))
			throw new InvalidDDMSException("The compilationReason attribute cannot be used until DDMS 3.0 or later.");		
		if (!!version.isAtLeast("3.1") && getDateOfExemptedSource() != null)
			throw new InvalidDDMSException("The dateOfExemptedSource attribute can only be used until DDMS 3.1 or later.");		
		if (getDateOfExemptedSource() != null
			&& !getDateOfExemptedSource().getXMLSchemaType().equals(DatatypeConstants.DATE))
			throw new InvalidDDMSException(
				"The dateOfExemptedSource attribute must be in the xs:date format (YYYY-MM-DD).");
		if (getDeclassDate() != null && !getDeclassDate().getXMLSchemaType().equals(DatatypeConstants.DATE))
			throw new InvalidDDMSException("The declassDate must be in the xs:date format (YYYY-MM-DD).");
		if (!Util.isEmpty(getDeclassException())) {
			if (isDDMS20) {
				// In DDMS 2.0, this can be a list of tokens.
				for (String value : Util.getXsListAsList(getDeclassException()))
					validateEnumeration(ISMVocabulary.CVE_DECLASS_EXCEPTION, value);
			}
			else
				validateEnumeration(ISMVocabulary.CVE_DECLASS_EXCEPTION, getDeclassException());
		}
		if (!isDDMS20 && getDeclassManualReview() != null)
			throw new InvalidDDMSException("The declassManualReview attribute can only be used in DDMS 2.0.");
		if (!version.isAtLeast("3.1") && !getDisplayOnlyTo().isEmpty())
			throw new InvalidDDMSException("The displayOnlyTo attribute cannot be used until DDMS 3.1 or later.");
		for (String display : getDisplayOnlyTo())
			validateEnumeration(ISMVocabulary.CVE_DISPLAY_ONLY_TO, display);
		for (String dissemination : getDisseminationControls())
			validateEnumeration(ISMVocabulary.CVE_DISSEMINATION_CONTROLS, dissemination);
		for (String fgiSourceOpen : getFGIsourceOpen())
			validateEnumeration(ISMVocabulary.CVE_FGI_SOURCE_OPEN, fgiSourceOpen);
		for (String fgiSourceProtected : getFGIsourceProtected())
			validateEnumeration(ISMVocabulary.CVE_FGI_SOURCE_PROTECTED, fgiSourceProtected);
		for (String nonIC : getNonICmarkings())
			validateEnumeration(ISMVocabulary.CVE_NON_IC_MARKINGS, nonIC);
		if (!version.isAtLeast("3.1") && !getNonUSControls().isEmpty())
			throw new InvalidDDMSException("The nonUSControls attribute cannot be used until DDMS 3.1 or later.");
		for (String nonUS : getNonUSControls())
			validateEnumeration(ISMVocabulary.CVE_NON_US_CONTROLS, nonUS);	
		for (String op : getOwnerProducers())
			validateEnumeration(ISMVocabulary.CVE_OWNER_PRODUCERS, op);
		for (String releasableTo : getReleasableTo())
			validateEnumeration(ISMVocabulary.CVE_RELEASABLE_TO, releasableTo);
		for (String sarId : getSARIdentifier())
			validateEnumeration(ISMVocabulary.CVE_SAR_IDENTIFIER, sarId);
		for (String sciControls : getSCIcontrols())
			validateEnumeration(ISMVocabulary.CVE_SCI_CONTROLS, sciControls);
		if (!Util.isEmpty(getTypeOfExemptedSource())) {
			if (isDDMS20) {
				// In DDMS 2.0, this can be a list of tokens.
				for (String value : Util.getXsListAsList(getTypeOfExemptedSource()))
					validateEnumeration(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, value);
			}
			else if ("3.0".equals(version.getVersion())) {
				validateEnumeration(ISMVocabulary.CVE_TYPE_EXEMPTED_SOURCE, getTypeOfExemptedSource());
			}
			else {
				throw new InvalidDDMSException("The typeOfExemptedSource attribute can only be used in DDMS 2.0 or 3.0.");
			}
		}
	}
	
	/**
	 * Helper method to validate a value from a controlled vocabulary. This is the delegate that handles whether a bad
	 * validation should result in a warning or error, based on the configurable property,
	 * "icism.cve.validationAsErrors".
	 * 
	 * @param enumerationKey the key of the enumeration
	 * @param value the test value
	 * @throws InvalidDDMSException if the value is not and validation should result in errors
	 */
	private void validateEnumeration(String enumerationKey, String value) throws InvalidDDMSException {
		boolean validationAsErrors = Boolean.valueOf(PropertyReader.getProperty("ism.cve.validationAsErrors"))
			.booleanValue();
		if (!ISMVocabulary.enumContains(enumerationKey, value)) {
			String message = ISMVocabulary.getInvalidMessage(enumerationKey, value);
			if (validationAsErrors)
				throw new InvalidDDMSException(message);
			else
				getWarnings().add(ValidationMessage.newWarning(message, null));
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
		html.append(Security.buildHTMLMeta(prefix + ATOMIC_ENERGY_MARKINGS_NAME,
			Util.getXsList(getAtomicEnergyMarkings()), false));
		html.append(Security.buildHTMLMeta(prefix + CLASSIFICATION_NAME, getClassification(), false));
		html.append(Security.buildHTMLMeta(prefix + CLASSIFICATION_REASON_NAME, getClassificationReason(), false));
		html.append(Security.buildHTMLMeta(prefix + CLASSIFIED_BY_NAME, getClassifiedBy(), false));
		html.append(Security.buildHTMLMeta(prefix + COMPILATION_REASON_NAME, getCompilationReason(), false));
		html.append(Security.buildHTMLMeta(prefix + COMPLIES_WITH_NAME, Util.getXsList(getCompliesWith()), false));
		if (getDateOfExemptedSource() != null) {
			html.append(Security.buildHTMLMeta(prefix + DATE_OF_EXEMPTED_SOURCE_NAME, 
				getDateOfExemptedSource().toXMLFormat(), false));
		}		
		if (getDeclassDate() != null)
			html.append(Security.buildHTMLMeta(prefix + DECLASS_DATE_NAME, getDeclassDate().toXMLFormat(), false));
		html.append(Security.buildHTMLMeta(prefix + DECLASS_EVENT_NAME, getDeclassEvent(), false));
		html.append(Security.buildHTMLMeta(prefix + DECLASS_EXCEPTION_NAME, getDeclassException(), false));
		if (getDeclassManualReview() != null) {
			html.append(Security.buildHTMLMeta(prefix + DECLASS_MANUAL_REVIEW_NAME, 
				getDeclassManualReview().toString(), false));
		}
		html.append(Security.buildHTMLMeta(prefix + DERIVATIVELY_CLASSIFIED_BY_NAME, getDerivativelyClassifiedBy(),
			false));
		html.append(Security.buildHTMLMeta(prefix + DERIVED_FROM_NAME, getDerivedFrom(), false));
		html.append(Security.buildHTMLMeta(prefix + DISPLAY_ONLY_TO_NAME, Util.getXsList(getDisplayOnlyTo()), false));
		html.append(Security.buildHTMLMeta(prefix + DISSEMINATION_CONTROLS_NAME, 
			Util.getXsList(getDisseminationControls()), false));
		html.append(Security.buildHTMLMeta(prefix + FGI_SOURCE_OPEN_NAME, Util.getXsList(getFGIsourceOpen()), false));
		html.append(Security.buildHTMLMeta(prefix + FGI_SOURCE_PROTECTED_NAME, Util.getXsList(getFGIsourceProtected()),
			false));
		html.append(Security.buildHTMLMeta(prefix + NON_IC_MARKINGS_NAME, Util.getXsList(getNonICmarkings()), false));
		html.append(Security.buildHTMLMeta(prefix + NON_US_CONTROLS_NAME, Util.getXsList(getNonUSControls()), false));
		html.append(Security.buildHTMLMeta(prefix + OWNER_PRODUCER_NAME, Util.getXsList(getOwnerProducers()), false));
		html.append(Security.buildHTMLMeta(prefix + RELEASABLE_TO_NAME, Util.getXsList(getReleasableTo()), false));
		html.append(Security.buildHTMLMeta(prefix + SAR_IDENTIFIER_NAME, Util.getXsList(getSARIdentifier()), false));
		html.append(Security.buildHTMLMeta(prefix + SCI_CONTROLS_NAME, Util.getXsList(getSCIcontrols()), false));
		html.append(Security.buildHTMLMeta(prefix + TYPE_OF_EXEMPTED_SOURCE_NAME, getTypeOfExemptedSource(), false));
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
		text.append(Security.buildTextLine(prefix + ATOMIC_ENERGY_MARKINGS_NAME,
			Util.getXsList(getAtomicEnergyMarkings()), false));
		text.append(Security.buildTextLine(prefix + CLASSIFICATION_NAME, getClassification(), false));
		text.append(Security.buildTextLine(prefix + CLASSIFICATION_REASON_NAME, getClassificationReason(), false));
		text.append(Security.buildTextLine(prefix + CLASSIFIED_BY_NAME, getClassifiedBy(), false));
		text.append(Security.buildTextLine(prefix + COMPILATION_REASON_NAME, getCompilationReason(), false));
		text.append(Security.buildTextLine(prefix + COMPLIES_WITH_NAME, Util.getXsList(getCompliesWith()), false));
		if (getDateOfExemptedSource() != null) {
			text.append(Security.buildTextLine(prefix + DATE_OF_EXEMPTED_SOURCE_NAME, 
				getDateOfExemptedSource().toXMLFormat(), false));
		}
		if (getDeclassDate() != null)
			text.append(Security.buildTextLine(prefix + DECLASS_DATE_NAME, getDeclassDate().toXMLFormat(), false));
		text.append(Security.buildTextLine(prefix + DECLASS_EVENT_NAME, getDeclassEvent(), false));
		text.append(Security.buildTextLine(prefix + DECLASS_EXCEPTION_NAME, getDeclassException(), false));
		if (getDeclassManualReview() != null) {
			text.append(Security.buildTextLine(prefix + DECLASS_MANUAL_REVIEW_NAME, getDeclassManualReview().toString(),
				false));
		}
		text.append(Security.buildTextLine(prefix + DERIVATIVELY_CLASSIFIED_BY_NAME, getDerivativelyClassifiedBy(), 
			false));
		text.append(Security.buildTextLine(prefix + DERIVED_FROM_NAME, getDerivedFrom(), false));
		text.append(Security.buildTextLine(prefix + DISPLAY_ONLY_TO_NAME, Util.getXsList(getDisplayOnlyTo()), false));
		text.append(Security.buildTextLine(prefix + DISSEMINATION_CONTROLS_NAME, 
			Util.getXsList(getDisseminationControls()), false));
		text.append(Security.buildTextLine(prefix + FGI_SOURCE_OPEN_NAME, Util.getXsList(getFGIsourceOpen()), false));
		text.append(Security.buildTextLine(prefix + FGI_SOURCE_PROTECTED_NAME, Util.getXsList(getFGIsourceProtected()),
			false));
		text.append(Security.buildTextLine(prefix + NON_IC_MARKINGS_NAME, Util.getXsList(getNonICmarkings()), false));
		text.append(Security.buildTextLine(prefix + NON_US_CONTROLS_NAME, Util.getXsList(getNonUSControls()), false));
		text.append(Security.buildTextLine(prefix + OWNER_PRODUCER_NAME, Util.getXsList(getOwnerProducers()), false));
		text.append(Security.buildTextLine(prefix + RELEASABLE_TO_NAME, Util.getXsList(getReleasableTo()), false));
		text.append(Security.buildTextLine(prefix + SAR_IDENTIFIER_NAME, Util.getXsList(getSARIdentifier()), false));
		text.append(Security.buildTextLine(prefix + SCI_CONTROLS_NAME, Util.getXsList(getSCIcontrols()), false));
		text.append(Security.buildTextLine(prefix + TYPE_OF_EXEMPTED_SOURCE_NAME, getTypeOfExemptedSource(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof SecurityAttributes))
			return (false);		
		SecurityAttributes test = (SecurityAttributes) obj;
		return (Util.listEquals(getAtomicEnergyMarkings(), test.getAtomicEnergyMarkings())
			&& getClassification().equals(test.getClassification())
			&& getClassificationReason().equals(test.getClassificationReason())
			&& getClassifiedBy().equals(test.getClassifiedBy())
			&& getCompilationReason().equals(test.getCompilationReason())
			&& Util.listEquals(getCompliesWith(), test.getCompliesWith())
			&& Util.nullEquals(getDateOfExemptedSource(), test.getDateOfExemptedSource())
			&& Util.nullEquals(getDeclassDate(), test.getDeclassDate())
			&& getDeclassEvent().equals(test.getDeclassEvent())
			&& getDeclassException().equals(test.getDeclassException())
			&& Util.nullEquals(getDeclassManualReview(), test.getDeclassManualReview())
			&& getDerivativelyClassifiedBy().equals(test.getDerivativelyClassifiedBy())
			&& getDerivedFrom().equals(test.getDerivedFrom())
			&& Util.listEquals(getDisplayOnlyTo(), test.getDisplayOnlyTo())
			&& Util.listEquals(getDisseminationControls(), test.getDisseminationControls())
			&& Util.listEquals(getFGIsourceOpen(), test.getFGIsourceOpen())
			&& Util.listEquals(getFGIsourceProtected(), test.getFGIsourceProtected())
			&& Util.listEquals(getNonICmarkings(), test.getNonICmarkings())
			&& Util.listEquals(getNonUSControls(), test.getNonUSControls())
			&& Util.listEquals(getOwnerProducers(), test.getOwnerProducers())
			&& Util.listEquals(getReleasableTo(), test.getReleasableTo())
			&& Util.listEquals(getSARIdentifier(), test.getSARIdentifier())
			&& Util.listEquals(getSCIcontrols(), test.getSCIcontrols())
			&& getTypeOfExemptedSource().equals(test.getTypeOfExemptedSource()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = 0;
		result = 7 * result + getAtomicEnergyMarkings().hashCode();
		result = 7 * result + getClassification().hashCode();
		result = 7 * result + getClassificationReason().hashCode();
		result = 7 * result + getClassifiedBy().hashCode();
		result = 7 * result + getCompilationReason().hashCode();
		result = 7 * result + getCompliesWith().hashCode();
		if (getDateOfExemptedSource() != null)
			result = 7 * result + getDateOfExemptedSource().hashCode();
		if (getDeclassDate() != null)
			result = 7 * result + getDeclassDate().hashCode();
		result = 7 * result + getDeclassEvent().hashCode();
		result = 7 * result + getDeclassException().hashCode();
		if (getDeclassManualReview() != null)
			result = 7 * result + getDeclassManualReview().hashCode();
		result = 7 * result + getDerivativelyClassifiedBy().hashCode();
		result = 7 * result + getDerivedFrom().hashCode();
		result = 7 * result + getDisplayOnlyTo().hashCode();
		result = 7 * result + getDisseminationControls().hashCode();
		result = 7 * result + getFGIsourceOpen().hashCode();
		result = 7 * result + getFGIsourceProtected().hashCode();
		result = 7 * result + getNonICmarkings().hashCode();
		result = 7 * result + getNonUSControls().hashCode();
		result = 7 * result + getOwnerProducers().hashCode();
		result = 7 * result + getReleasableTo().hashCode();
		result = 7 * result + getSARIdentifier().hashCode();
		result = 7 * result + getSCIcontrols().hashCode();
		result = 7 * result + getTypeOfExemptedSource().hashCode();
		return (result);
	}	
	
	/**
	 * Accessor for the DDMS namespace on the enclosing element.
	 */
	public String getDDMSNamespace() {
		return (Util.getNonNullString(_ddmsNamespace));
	}
	
	/**
	 * Accessor for the atomicEnergyMarkings attribute. Returns a copy.
	 */
	public List<String> getAtomicEnergyMarkings() {
		return (Collections.unmodifiableList(_cachedAtomicEnergyMarkings));
	}
	
	/**
	 * Accessor for the classification attribute.
	 */
	public String getClassification() {
		return (Util.getNonNullString(_cachedClassification));
	}

	/**
	 * Accessor for the classificationReason attribute.
	 */
	public String getClassificationReason() {
		return (Util.getNonNullString(_cachedClassificationReason));
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
	 * Accessor for the compliesWith attribute. Returns a copy.
	 */
	public List<String> getCompliesWith() {
		return (Collections.unmodifiableList(_cachedCompliesWith));
	}

	/**
	 * Accessor for the dateOfExemptedSource attribute. May return null if not set.
	 */
	public XMLGregorianCalendar getDateOfExemptedSource() {
		return (_cachedDateOfExemptedSource == null ? null 
			: getFactory().newXMLGregorianCalendar(_cachedDateOfExemptedSource.toXMLFormat()));
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
	 * Accessor for the declassManualReview attribute. Will be null in DDMS 3.0.
	 */
	public Boolean getDeclassManualReview() {
		return (_cachedDeclassManualReview);
	}

	/**
	 * Accessor for the derivativelyClassifiedBy attribute.
	 */
	public String getDerivativelyClassifiedBy() {
		return (Util.getNonNullString(_cachedDerivativelyClassifiedBy));
	}
	
	/**
	 * Accessor for the derivedFrom attribute.
	 */
	public String getDerivedFrom() {
		return (Util.getNonNullString(_cachedDerivedFrom));
	}

	/**
	 * Accessor for the displayOnlyTo attribute. Returns a copy.
	 */
	public List<String> getDisplayOnlyTo() {
		return (Collections.unmodifiableList(_cachedDisplayOnlyTo));
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
	 * Accessor for the nonICmarkings attribute. Returns a copy.
	 */
	public List<String> getNonICmarkings() {
		return (Collections.unmodifiableList(_cachedNonICmarkings));
	}
	
	/**
	 * Accessor for the nonUSControls attribute. Returns a copy.
	 */
	public List<String> getNonUSControls() {
		return (Collections.unmodifiableList(_cachedNonUSControls));
	}
	
	/**
	 * Accessor for the ownerProducers attribute. Returns a copy.
	 */
	public List<String> getOwnerProducers() {
		return (Collections.unmodifiableList(_cachedOwnerProducers));
	}

	/**
	 * Accessor for the releasableTo attribute. Returns a copy.
	 */
	public List<String> getReleasableTo() {
		return (Collections.unmodifiableList(_cachedReleasableTo));
	}
	
	/**
	 * Accessor for the SARIdentifier attribute. Returns a copy.
	 */
	public List<String> getSARIdentifier() {
		return (Collections.unmodifiableList(_cachedSARIdentifier));
	}

	/**
	 * Accessor for the SCIcontrols attribute. Returns a copy.
	 */
	public List<String> getSCIcontrols() {
		return (Collections.unmodifiableList(_cachedSCIcontrols));
	}

	/**
	 * Accessor for the typeOfExemptedSource attribute. In DDMS 2.0, this could be a list of tokens. This is represented
	 * here as a space-delimited string.
	 */
	public String getTypeOfExemptedSource() {
		return (Util.getNonNullString(_cachedTypeOfExemptedSource));
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
	 * <p>This class does not implement the IBuilder interface, because the behavior of commit() is at odds with the
	 * standard commit() method. As an attribute group, an empty attribute group will always be returned instead of
	 * null.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements Serializable {
		private static final long serialVersionUID = 279072341662308051L;
		
		private Map<String, String> _stringAttributes = new HashMap<String, String>();
		private Map<String, List<String>> _listAttributes = new HashMap<String, List<String>>();		
		private Boolean _declassManualReview = null;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(SecurityAttributes attributes) {
			setAtomicEnergyMarkings(attributes.getAtomicEnergyMarkings());
			setClassification(attributes.getClassification());
			setClassificationReason(attributes.getClassificationReason());
			setClassifiedBy(attributes.getClassifiedBy());
			setCompilationReason(attributes.getCompilationReason());
			setCompliesWith(attributes.getCompliesWith());
			if (attributes.getDateOfExemptedSource() != null)
				setDateOfExemptedSource(attributes.getDateOfExemptedSource().toXMLFormat());
			if (attributes.getDeclassDate() != null)
				setDeclassDate(attributes.getDeclassDate().toXMLFormat());
			setDeclassEvent(attributes.getDeclassEvent());
			setDeclassException(attributes.getDeclassException());
			if (attributes.getDeclassManualReview() != null)
				setDeclassManualReview(attributes.getDeclassManualReview());
			setDerivativelyClassifiedBy(attributes.getDerivativelyClassifiedBy());
			setDerivedFrom(attributes.getDerivedFrom());
			setDisplayOnlyTo(attributes.getDisplayOnlyTo());
			setDisseminationControls(attributes.getDisseminationControls());
			setFGIsourceOpen(attributes.getFGIsourceOpen());
			setFGIsourceProtected(attributes.getFGIsourceProtected());
			setNonICmarkings(attributes.getNonICmarkings());			
			setNonUSControls(attributes.getNonUSControls());		
			setOwnerProducers(attributes.getOwnerProducers());
			setReleasableTo(attributes.getReleasableTo());
			setSARIdentifier(attributes.getSARIdentifier());
			setSCIcontrols(attributes.getSCIcontrols());
			setTypeOfExemptedSource(attributes.getTypeOfExemptedSource());
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. Will always return an empty instance instead of
		 * a null one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public SecurityAttributes commit() throws InvalidDDMSException {
			Map<String, String> otherAttributes = new HashMap<String, String>();
			otherAttributes.put(ATOMIC_ENERGY_MARKINGS_NAME, Util.getXsList(getAtomicEnergyMarkings()));
			otherAttributes.put(CLASSIFICATION_REASON_NAME, getClassificationReason());
			otherAttributes.put(CLASSIFIED_BY_NAME, getClassifiedBy());
			otherAttributes.put(COMPILATION_REASON_NAME, getCompilationReason());
			otherAttributes.put(COMPLIES_WITH_NAME, Util.getXsList(getCompliesWith()));
			otherAttributes.put(DATE_OF_EXEMPTED_SOURCE_NAME, getDateOfExemptedSource());
			otherAttributes.put(DECLASS_DATE_NAME, getDeclassDate());
			otherAttributes.put(DECLASS_EVENT_NAME, getDeclassEvent());
			otherAttributes.put(DECLASS_EXCEPTION_NAME, getDeclassException());
			if (getDeclassManualReview() != null)
				otherAttributes.put(DECLASS_MANUAL_REVIEW_NAME, getDeclassManualReview().toString());
			otherAttributes.put(DERIVATIVELY_CLASSIFIED_BY_NAME, getDerivativelyClassifiedBy());
			otherAttributes.put(DERIVED_FROM_NAME, getDerivedFrom());
			otherAttributes.put(DISPLAY_ONLY_TO_NAME, Util.getXsList(getDisplayOnlyTo()));
			otherAttributes.put(DISSEMINATION_CONTROLS_NAME, Util.getXsList(getDisseminationControls()));
			otherAttributes.put(FGI_SOURCE_OPEN_NAME, Util.getXsList(getFGIsourceOpen()));
			otherAttributes.put(FGI_SOURCE_PROTECTED_NAME, Util.getXsList(getFGIsourceProtected()));
			otherAttributes.put(NON_IC_MARKINGS_NAME, Util.getXsList(getNonICmarkings()));			
			otherAttributes.put(NON_US_CONTROLS_NAME, Util.getXsList(getNonUSControls()));			
			otherAttributes.put(RELEASABLE_TO_NAME, Util.getXsList(getReleasableTo()));
			otherAttributes.put(SAR_IDENTIFIER_NAME, Util.getXsList(getSARIdentifier()));
			otherAttributes.put(SCI_CONTROLS_NAME, Util.getXsList(getSCIcontrols()));
			otherAttributes.put(TYPE_OF_EXEMPTED_SOURCE_NAME, getTypeOfExemptedSource());
			return (new SecurityAttributes(getClassification(), getOwnerProducers(), otherAttributes));
		}
		
		/**
		 * Checks if any values have been provided for this Builder.
		 * 
		 * @return true if every field is empty
		 */
		public boolean isEmpty() {
			boolean isEmpty = true;
			for (String value : getStringAttributes().values()) {
				isEmpty = isEmpty && Util.isEmpty(value);
			}
			for (List<String> list : getListAttributes().values()) {
				isEmpty = isEmpty && Util.containsOnlyEmptyValues(list);
			}
			return (isEmpty && getDeclassManualReview() == null);				
		}
		
		/**
		 * Builder accessor for the atomicEnergyMarkings attribute
		 */
		public List<String> getAtomicEnergyMarkings() {
			return (getListAttribute(ATOMIC_ENERGY_MARKINGS_NAME));
		}

		/**
		 * Builder accessor for the atomicEnergyMarkings attribute
		 */
		public void setAtomicEnergyMarkings(List<String> atomicEnergyMarkings) {
			setListAttribute(ATOMIC_ENERGY_MARKINGS_NAME, atomicEnergyMarkings);
		}
		
		/**
		 * Builder accessor for the classification attribute
		 */
		public String getClassification() {
			return (getStringAttributes().get(CLASSIFICATION_NAME));
		}
		
		/**
		 * Builder accessor for the classification attribute
		 */
		public void setClassification(String classification) {
			getStringAttributes().put(CLASSIFICATION_NAME, classification);
		}
		
		/**
		 * Builder accessor for the classificationReason attribute
		 */
		public String getClassificationReason() {
			return (getStringAttributes().get(CLASSIFICATION_REASON_NAME));
		}
		
		/**
		 * Builder accessor for the classificationReason attribute
		 */
		public void setClassificationReason(String classificationReason) {
			getStringAttributes().put(CLASSIFICATION_REASON_NAME, classificationReason);
		}
		
		/**
		 * Builder accessor for the classifiedBy attribute
		 */
		public String getClassifiedBy() {
			return (getStringAttributes().get(CLASSIFIED_BY_NAME));
		}
		
		/**
		 * Builder accessor for the classifiedBy attribute
		 */
		public void setClassifiedBy(String classifiedBy) {
			getStringAttributes().put(CLASSIFIED_BY_NAME, classifiedBy);
		}
		
		/**
		 * Builder accessor for the compilationReason attribute
		 */
		public String getCompilationReason() {
			return (getStringAttributes().get(COMPILATION_REASON_NAME));
		}
		
		/**
		 * Builder accessor for the compilationReason attribute
		 */
		public void setCompilationReason(String compilationReason) {
			getStringAttributes().put(COMPILATION_REASON_NAME, compilationReason);
		}

		/**
		 * Builder accessor for the compliesWith attribute
		 */
		public List<String> getCompliesWith() {
			return (getListAttribute(COMPLIES_WITH_NAME));
		}

		/**
		 * Builder accessor for the compliesWith attribute
		 */
		public void setCompliesWith(List<String> compliesWith) {
			setListAttribute(COMPLIES_WITH_NAME, compliesWith);
		}
		
		/**
		 * Builder accessor for the dateOfExemptedSource attribute
		 */
		public String getDateOfExemptedSource() {
			return (getStringAttributes().get(DATE_OF_EXEMPTED_SOURCE_NAME));
		}
		
		/**
		 * Builder accessor for the dateOfExemptedSource attribute
		 */
		public void setDateOfExemptedSource(String dateOfExemptedSource) {
			getStringAttributes().put(DATE_OF_EXEMPTED_SOURCE_NAME, dateOfExemptedSource);
		}
		
		/**
		 * Builder accessor for the declassDate attribute
		 */
		public String getDeclassDate() {
			return (getStringAttributes().get(DECLASS_DATE_NAME));
		}
		
		/**
		 * Builder accessor for the declassDate attribute
		 */
		public void setDeclassDate(String declassDate) {
			getStringAttributes().put(DECLASS_DATE_NAME, declassDate);
		}
		
		/**
		 * Builder accessor for the declassEvent attribute
		 */
		public String getDeclassEvent() {
			return (getStringAttributes().get(DECLASS_EVENT_NAME));
		}
		
		/**
		 * Builder accessor for the declassEvent attribute
		 */
		public void setDeclassEvent(String declassEvent) {
			getStringAttributes().put(DECLASS_EVENT_NAME, declassEvent);
		}
		
		/**
		 * Builder accessor for the declassException attribute
		 */
		public String getDeclassException() {
			return (getStringAttributes().get(DECLASS_EXCEPTION_NAME));
		}
		
		/**
		 * Builder accessor for the declassException attribute
		 */
		public void setDeclassException(String declassException) {
			getStringAttributes().put(DECLASS_EXCEPTION_NAME, declassException);
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
		
		/**
		 * Builder accessor for the derivativelyClassifiedBy attribute
		 */
		public String getDerivativelyClassifiedBy() {
			return (getStringAttributes().get(DERIVATIVELY_CLASSIFIED_BY_NAME));
		}
		
		/**
		 * Builder accessor for the derivativelyClassifiedBy attribute
		 */
		public void setDerivativelyClassifiedBy(String derivativelyClassifiedBy) {
			getStringAttributes().put(DERIVATIVELY_CLASSIFIED_BY_NAME, derivativelyClassifiedBy);
		}
		
		/**
		 * Builder accessor for the derivedFrom attribute
		 */
		public String getDerivedFrom() {
			return (getStringAttributes().get(DERIVED_FROM_NAME));
		}
		
		/**
		 * Builder accessor for the derivedFrom attribute
		 */
		public void setDerivedFrom(String derivedFrom) {
			getStringAttributes().put(DERIVED_FROM_NAME, derivedFrom);
		}
		
		/**
		 * Builder accessor for the displayOnlyTo attribute
		 */
		public List<String> getDisplayOnlyTo() {
			return (getListAttribute(DISPLAY_ONLY_TO_NAME));
		}

		/**
		 * Builder accessor for the displayOnlyTo attribute
		 */
		public void setDisplayOnlyTo(List<String> displayOnlyTo) {
			setListAttribute(DISPLAY_ONLY_TO_NAME, displayOnlyTo);
		}
		
		/**
		 * Builder accessor for the disseminationControls attribute
		 */
		public List<String> getDisseminationControls() {
			return (getListAttribute(DISSEMINATION_CONTROLS_NAME));
		}
		
		/**
		 * Builder accessor for the disseminationControls attribute
		 */
		public void setDisseminationControls(List<String> disseminationControls) {
			setListAttribute(DISSEMINATION_CONTROLS_NAME, disseminationControls);
		}

		/**
		 * Builder accessor for the FGIsourceOpen attribute
		 */
		public List<String> getFGIsourceOpen() {
			return (getListAttribute(FGI_SOURCE_OPEN_NAME));
		}
		
		/**
		 * Builder accessor for the FGIsourceOpen attribute
		 */
		public void setFGIsourceOpen(List<String> FGIsourceOpen) {
			setListAttribute(FGI_SOURCE_OPEN_NAME, FGIsourceOpen);
		}
		
		/**
		 * Builder accessor for the FGIsourceProtected attribute
		 */
		public List<String> getFGIsourceProtected() {
			return (getListAttribute(FGI_SOURCE_PROTECTED_NAME));
		}
		
		/**
		 * Builder accessor for the FGIsourceProtected attribute
		 */
		public void setFGIsourceProtected(List<String> FGIsourceProtected) {
			setListAttribute(FGI_SOURCE_PROTECTED_NAME, FGIsourceProtected);
		}
		
		/**
		 * Builder accessor for the nonICmarkings attribute
		 */
		public List<String> getNonICmarkings() {
			return (getListAttribute(NON_IC_MARKINGS_NAME));
		}
		
		/**
		 * Builder accessor for the nonICmarkings attribute
		 */
		public void setNonICmarkings(List<String> nonICmarkings) {
			setListAttribute(NON_IC_MARKINGS_NAME, nonICmarkings);
		}

		/**
		 * Builder accessor for the nonUSControls attribute
		 */
		public List<String> getNonUSControls() {
			return (getListAttribute(NON_US_CONTROLS_NAME));
		}

		/**
		 * Builder accessor for the nonUSControls attribute
		 */
		public void setNonUSControls(List<String> nonUSControls) {
			setListAttribute(NON_US_CONTROLS_NAME, nonUSControls);
		}
				
		/**
		 * Builder accessor for the ownerProducers attribute
		 */
		public List<String> getOwnerProducers() {
			return (getListAttribute(OWNER_PRODUCER_NAME));
		}
		
		/**
		 * Builder accessor for the ownerProducers attribute
		 */
		public void setOwnerProducers(List<String> ownerProducers) {
			setListAttribute(OWNER_PRODUCER_NAME, ownerProducers);
		}
		
		/**
		 * Builder accessor for the releasableTo attribute
		 */
		public List<String> getReleasableTo() {
			return (getListAttribute(RELEASABLE_TO_NAME));
		}
		
		/**
		 * Builder accessor for the releasableTo attribute
		 */
		public void setReleasableTo(List<String> releasableTo) {
			setListAttribute(RELEASABLE_TO_NAME, releasableTo);
		}
		
		/**
		 * Builder accessor for the SARIdentifier attribute
		 */
		public List<String> getSARIdentifier() {
			return (getListAttribute(SAR_IDENTIFIER_NAME));
		}
		
		/**
		 * Builder accessor for the SARIdentifier attribute
		 */
		public void setSARIdentifier(List<String> SARIdentifier) {
			setListAttribute(SAR_IDENTIFIER_NAME, SARIdentifier);
		}
		
		/**
		 * Builder accessor for the SCIcontrols attribute
		 */
		public List<String> getSCIcontrols() {
			return (getListAttribute(SCI_CONTROLS_NAME));
		}
		
		/**
		 * Builder accessor for the SCIcontrols attribute
		 */
		public void setSCIcontrols(List<String> SCIcontrols) {
			setListAttribute(SCI_CONTROLS_NAME, SCIcontrols);
		}
		
		/**
		 * Builder accessor for the typeOfExemptedSource attribute
		 */
		public String getTypeOfExemptedSource() {
			return (getStringAttributes().get(TYPE_OF_EXEMPTED_SOURCE_NAME));
		}
		
		/**
		 * Builder accessor for the typeOfExemptedSource attribute
		 */
		public void setTypeOfExemptedSource(String typeOfExemptedSource) {
			getStringAttributes().put(TYPE_OF_EXEMPTED_SOURCE_NAME, typeOfExemptedSource);
		}
		
		/**
		 * Helper method to look up a key in the map of attribute. Lazily creates a new list if null.
		 * 
		 * @param key the attribute name
		 * @return the list of strings mapped to that attribute name
		 */
		private List<String> getListAttribute(String key) {
			if (getListAttributes().get(key) == null)
				getListAttributes().put(key, new LazyList(String.class));
			return (getListAttributes().get(key));
		}
		
		/**
		 * Helper method to initialize a new lazy list for some attribute.
		 * 
		 * @param key the attribute name
		 * @param value the list to save, which will be wrapped in a lazy list
		 */
		private void setListAttribute(String key, List<String> value)  {
			getListAttributes().put(key, new LazyList(value, String.class));
		}
		
		/**
		 * Accessor for the map of attribute names to list values
		 */
		private Map<String, List<String>> getListAttributes()  {
			return (_listAttributes);
		}
		
		/**
		 * Accessor for the map of attribute names to string values
		 */
		private Map<String, String> getStringAttributes()  {
			return (_stringAttributes);
		}
	}
}