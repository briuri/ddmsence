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
package buri.ddmsence.ddms;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.Util;

/**
 * Base class for DDMS producer elements, such as ddms:creator and ddms:contributor.
 * 
 * <p>
 * In DDMS, producers are modeled as a producerType (creator, contributor, pointOfContact, publisher) containing a
 * producerEntity (Person, Organization, Service, Unknown). DDMSence flattens this hierarchy to reduce complexity. In
 * DDMSence terms, a Producer is "an entity who fills some producer role", rather than "a role which is filled by some
 * entity". The object itself models the entity, and the producer role is a property on the object.
 * </p>
 * 
 * <p>
 * The HTML output of this class depends on the producer type which the producer entity is associated with. For example,
 * if the producer entity represented by this class is a "pointOfContact", the HTML meta tags will prefix each field
 * with "pointOfContact.". See the DDMS category descriptions for other examples:
 * http://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Person
 * </p>
 * 
 * <p>
 * Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set before
 * the component is used.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ICISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public abstract class AbstractProducer extends AbstractBaseComponent implements IProducer {
	
	private String _producerType;
	private ExtensibleAttributes _cachedExtensibleAttributes = null;
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<String> _cachedNames;
	private List<String> _cachedPhones;
	private List<String> _cachedEmails;
	private SecurityAttributes _cachedSecurityAttributes = null;
		
	/** The name of a producer element which is a contributor */
	public static final String CONTRIBUTOR_NAME = "contributor";
	
	/** The name of a producer element which is a creator */
	public static final String CREATOR_NAME = "creator";
	
	/** The name of a producer element which is a point of contact */
	public static final String POC_NAME = "pointOfContact";
	
	/** The name of a producer element which is a publisher */
	public static final String PUBLISHER_NAME = "publisher";
	
	/** The element name for ddms:name */
	protected static final String NAME_NAME = "name";
	
	/** The element name for phone numbers */
	protected static final String PHONE_NAME = "phone";
	
	/** The element name for email addresses */
	protected static final String EMAIL_NAME = "email";	
	
	private static Set<String> PRIVATE_PRODUCER_TYPES = new HashSet<String>();
	static {
		PRIVATE_PRODUCER_TYPES.add(CONTRIBUTOR_NAME);
		PRIVATE_PRODUCER_TYPES.add(CREATOR_NAME);
		PRIVATE_PRODUCER_TYPES.add(POC_NAME);
		PRIVATE_PRODUCER_TYPES.add(PUBLISHER_NAME);
	}
	private static Set<String> PRODUCER_ENTITY_TYPES = new HashSet<String>();
	static {
		PRODUCER_ENTITY_TYPES.add(Person.NAME);
		PRODUCER_ENTITY_TYPES.add(Organization.NAME);
		PRODUCER_ENTITY_TYPES.add(Service.NAME);
		PRODUCER_ENTITY_TYPES.add(Unknown.NAME);
	}
	
	/** Set of all valid producer types */
	public static Set<String> PRODUCER_TYPES = Collections.unmodifiableSet(PRIVATE_PRODUCER_TYPES);

	/**
	 * Base constructor
	 * 
	 * @param element the XOM element representing this component
	 */
	protected AbstractProducer(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("producer element", element);
			Element entityElement = element.getChildElements().get(0);
			if (entityElement != null) {
				_cachedNames = Util.getDDMSChildValues(entityElement, NAME_NAME);
				_cachedPhones = Util.getDDMSChildValues(entityElement, PHONE_NAME);
				_cachedEmails = Util.getDDMSChildValues(entityElement, EMAIL_NAME);
			}
			_producerType = element.getLocalName();
			_cachedSecurityAttributes = new SecurityAttributes(element);
			_cachedExtensibleAttributes = new ExtensibleAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
		
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param producerType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param entityType the type of the entity (i.e. Organization, Person)
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param securityAttributes any security attributes (optional)
	 * @param extensions extensible attributes (optional)
	 * @param validateNow true to validate the component immediately. Because Person entities have additional fields
	 * they should not be validated in the superconstructor.
	 */
	protected AbstractProducer(String producerType, String entityType, List<String> names, List<String> phones,
		List<String> emails, SecurityAttributes securityAttributes, ExtensibleAttributes extensions, boolean validateNow)
		throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("entity type", entityType);
			if (names == null)
				names = Collections.emptyList();
			if (phones == null)
				phones = Collections.emptyList();
			if (emails == null)
				emails = Collections.emptyList();
			Element entityElement = Util.buildDDMSElement(entityType, null);
			for (String name : names) {
				entityElement.appendChild(Util.buildDDMSElement(NAME_NAME, name));
			}
			for (String phone : phones) {
				entityElement.appendChild(Util.buildDDMSElement(PHONE_NAME, phone));
			}
			for (String email : emails) {
				entityElement.appendChild(Util.buildDDMSElement(EMAIL_NAME, email));
			}
			Element element = Util.buildDDMSElement(producerType, null);
			element.appendChild(entityElement);

			_cachedNames = names;
			_cachedPhones = phones;
			_cachedEmails = emails;
			_producerType = producerType;
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);
			_cachedExtensibleAttributes = (extensions == null ? new ExtensibleAttributes((List<Attribute>) null)
				: extensions);
			_cachedExtensibleAttributes.addTo(entityElement);
			setXOMElement(element, validateNow);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Validates a producer type against the allowed types.
	 * 
	 * @param producerType the type to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateProducerType(String producerType) throws InvalidDDMSException {
		Util.requireDDMSValue("producer type", producerType);
		if (!PRODUCER_TYPES.contains(producerType))
			throw new InvalidDDMSException("The producer type must be one of " + PRODUCER_TYPES);
	}
	
	/**
	 * Asserts that some string is a valid producer entity type in DDMS (Person, Organization, Service, Unknown).
	 * 
	 * @param entityType		the type to check
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateProducerEntityType(String entityType) throws InvalidDDMSException {
		Util.requireDDMSValue("entity type", entityType);
		if (!PRODUCER_ENTITY_TYPES.contains(entityType))
			throw new InvalidDDMSException("The producer entity type must be one of " + PRODUCER_ENTITY_TYPES);
	}
		
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The producer type is valid.</li>
	 * <li>A producer entity exists and has a valid element name.</li>
	 * <li>The producer entity has at least 1 non-empty name.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();	
		validateProducerType(getProducerType());
		Elements elements = getXOMElement().getChildElements();
		if (!Util.isBounded(elements.size(), 1, 1))
			throw new InvalidDDMSException("A producer element can only have 1 child producer entity.");
		Element entityElement = getEntityElement();
		validateProducerEntityType(entityElement.getLocalName());		
		if (entityElement.getChildElements(NAME_NAME, entityElement.getNamespaceURI()).size() == 0)
			throw new InvalidDDMSException("At least 1 name element must exist.");
		
		boolean foundNonEmptyName = false;
		for (String name : getNames()) {
			foundNonEmptyName = foundNonEmptyName || !Util.isEmpty(name);
		}
		if (!foundNonEmptyName)
			throw new InvalidDDMSException("At least 1 name element must have a non-empty value.");

		validateSharedWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A ddms:phone element was found with no value.</li>
	 * <li>A ddms:email element was found with no value.</li>
	 * <li>Include any validation warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateSharedWarnings() {
		Element entityElement = getEntityElement();
		Elements phoneElements = entityElement.getChildElements(PHONE_NAME, entityElement.getNamespaceURI());
		for (int i = 0; i < phoneElements.size(); i++) {
			if (Util.isEmpty(phoneElements.get(i).getValue())) {
				addWarning("A ddms:phone element was found with no value.");
				break;
			}
		}
		Elements emailElements = entityElement.getChildElements(EMAIL_NAME, entityElement.getNamespaceURI());
		for (int i = 0; i < emailElements.size(); i++) {
			if (Util.isEmpty(emailElements.get(i).getValue())) {
				addWarning("A ddms:email element was found with no value.");
				break;
			}
		}
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
		
	/**
	 * @see AbstractBaseComponent#getLocatorSuffix()
	 */
	protected String getLocatorSuffix() {
		return (ValidationMessage.ELEMENT_PREFIX + getXOMElement().getNamespacePrefix() + ":" + getEntityType());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractProducer))
			return (false);
		AbstractProducer test = (AbstractProducer) obj;
		return (getProducerType().equals(test.getProducerType())
			&& getEntityType().equals(test.getEntityType())
			&& Util.listEquals(getNames(), test.getNames())
			&& Util.listEquals(getPhones(), test.getPhones())
			&& Util.listEquals(getEmails(), test.getEmails())
			&& getSecurityAttributes().equals(test.getSecurityAttributes())
			&& getExtensibleAttributes().equals(test.getExtensibleAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getProducerType().hashCode();
		result = 7 * result + getEntityType().hashCode();
		result = 7 * result + getNames().hashCode();
		result = 7 * result + getPhones().hashCode();
		result = 7 * result + getEmails().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		result = 7 * result + getExtensibleAttributes().hashCode();
		return (result);
	}
	
	/**
	 * The DDMS website examples are not clear on what to do if there are multiple names, phone numbers, or emails. This
	 * method merely creates a separate HTML meta tag for each value.
	 * 
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta(getProducerType() + ".entityType", getEntityType(), true));
		for (String name : getNames())
			html.append(buildHTMLMeta(getProducerType() + ".name", name, true));
		for (String phone : getPhones())
			html.append(buildHTMLMeta(getProducerType() + ".phone", phone, true));
		for (String email : getEmails())
			html.append(buildHTMLMeta(getProducerType() + ".email", email, true));
		html.append(getSecurityAttributes().toHTML(getProducerType()));
		html.append(getExtensibleAttributes().toHTML(getProducerType()));
		return (html.toString());
	}

	/**
	 * The DDMS website examples are not clear on what to do if there are multiple names, phone numbers, or emails. This
	 * method merely creates a separate Text line for each value.
	 * 
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine(Util.capitalize(getProducerType()) + " EntityType", getTextOutputName(), true));
		for (String name : getNames())
			text.append(buildTextLine("Name", name, true));
		for (String phone : getPhones())
			text.append(buildTextLine("Phone Number", phone, true));
		for (String email : getEmails())
			text.append(buildTextLine("Email", email, true));
		text.append(getSecurityAttributes().toText(Util.capitalize(getProducerType())));
		text.append(getExtensibleAttributes().toText(Util.capitalize(getProducerType())));
		return (text.toString());		
	}
	
	/**
	 * Returns the producer entity name for Text output. This is overridden for Service entities, which are called
	 * Service.NAME in XML and HTML, but "Web Service" in Text.
	 * 
	 * @return the name
	 */
	protected String getTextOutputName() {
		return (getEntityType());
	}
	
	/**
	 * Accessor for the names of the entity (1 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<String> getNames() {
		return (Collections.unmodifiableList(_cachedNames));
	}
	
	/**
	 * Accessor for the phone numbers of the entity (0 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<String> getPhones() {
		return (Collections.unmodifiableList(_cachedPhones));
	}
	
	/**
	 * Accessor for the emails of the entity (0 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<String> getEmails() {
		return (Collections.unmodifiableList(_cachedEmails));
	}
	
	/**
	 * Accessor for the element representing the producer entity. Will return null if not set, but this cannot happen
	 * after instantiation.
	 */
	protected Element getEntityElement() {
		return (getXOMElement().getChildElements().get(0));
	}
	
	/**
	 * Accessor for the entityType (i.e. Person, Organization, etc.)
	 */
	public String getEntityType() {
		return (getEntityElement().getLocalName());
	}
	
	/**
	 * Accessor for the producerType (i.e. creator, contributor, etc.)
	 */
	protected String getProducerType() {
		return _producerType;
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Accessor for the extensible attributes. Will always be non-null, even if not set.
	 */
	public ExtensibleAttributes getExtensibleAttributes() {
		return (_cachedExtensibleAttributes);
	}
}