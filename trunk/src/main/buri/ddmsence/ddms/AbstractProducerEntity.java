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
package buri.ddmsence.ddms;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.resource.Contributor;
import buri.ddmsence.ddms.resource.Creator;
import buri.ddmsence.ddms.resource.PointOfContact;
import buri.ddmsence.ddms.resource.Publisher;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * Base class for entities which fulfill a producer role, such as ddms:person and ddms:organization.
 * 
 * <p>
 * The HTML output of this class depends on the producer type which the producer entity is associated with. For example,
 * if the producer entity represented by this class is a "pointOfContact", the HTML meta tags will prefix each field
 * with "pointOfContact.". See the DDMS category descriptions for other examples:
 * http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Person
 * </p>
 * 
 * <p>
 * Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set before
 * the component is used.
 * </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public abstract class AbstractProducerEntity extends AbstractBaseComponent implements IProducerEntity {
	
	private String _parentType;
	private ExtensibleAttributes _cachedExtensibleAttributes = null;
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<String> _cachedNames;
	private List<String> _cachedPhones;
	private List<String> _cachedEmails;
	
	/** The element name for ddms:name */
	protected static final String NAME_NAME = "name";
	
	/** The element name for phone numbers */
	protected static final String PHONE_NAME = "phone";
	
	/** The element name for email addresses */
	protected static final String EMAIL_NAME = "email";	
	
	private static Set<String> PARENT_TYPES = new HashSet<String>();
	static {
		PARENT_TYPES.add(Contributor.NAME);
		PARENT_TYPES.add(Creator.NAME);
		PARENT_TYPES.add(PointOfContact.NAME);
		PARENT_TYPES.add(Publisher.NAME);
	}

	/**
	 * Base constructor
	 * 
	 * @param parentType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param element the XOM element representing this component
	 */
	protected AbstractProducerEntity(String parentType, Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("producerEntity element", element);
			_cachedNames = Util.getDDMSChildValues(element, NAME_NAME);
			_cachedPhones = Util.getDDMSChildValues(element, PHONE_NAME);
			_cachedEmails = Util.getDDMSChildValues(element, EMAIL_NAME);
			_parentType = parentType;
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
	 * @param parentType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param entityName the element name of this entity (i.e. organization, person)
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param extensions extensible attributes (optional)
	 * @param validateNow true to validate the component immediately. Because Person entities have additional fields
	 * they should not be validated in the superconstructor.
	 */
	protected AbstractProducerEntity(String parentType, String entityName, List<String> names, List<String> phones,
		List<String> emails, ExtensibleAttributes extensions, boolean validateNow)
		throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("entityName", entityName);
			if (names == null)
				names = Collections.emptyList();
			if (phones == null)
				phones = Collections.emptyList();
			if (emails == null)
				emails = Collections.emptyList();
			Element element = Util.buildDDMSElement(entityName, null);
			for (String name : names) {
				element.appendChild(Util.buildDDMSElement(NAME_NAME, name));
			}
			for (String phone : phones) {
				element.appendChild(Util.buildDDMSElement(PHONE_NAME, phone));
			}
			for (String email : emails) {
				element.appendChild(Util.buildDDMSElement(EMAIL_NAME, email));
			}

			_cachedNames = names;
			_cachedPhones = phones;
			_cachedEmails = emails;
			_parentType = parentType;
			_cachedExtensibleAttributes = (extensions == null ? new ExtensibleAttributes((List<Attribute>) null)
				: extensions);
			_cachedExtensibleAttributes.addTo(element);
			setXOMElement(element, validateNow);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Validates a parent type against the allowed types.
	 * 
	 * @param parentType the type to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateParentType(String parentType) throws InvalidDDMSException {
		Util.requireDDMSValue("parent type", parentType);
		if (!PARENT_TYPES.contains(parentType))
			throw new InvalidDDMSException("The parent type must be one of " + PARENT_TYPES);
	}
		
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The parent type is valid.</li>
	 * <li>The producer entity has at least 1 non-empty name.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();	
		validateParentType(getParentType());
		if (getXOMElement().getChildElements(NAME_NAME, getNamespace()).size() == 0)
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
	 * </td></tr></table>
	 */
	protected void validateSharedWarnings() {
		Elements phoneElements = getXOMElement().getChildElements(PHONE_NAME, getNamespace());
		for (int i = 0; i < phoneElements.size(); i++) {
			if (Util.isEmpty(phoneElements.get(i).getValue())) {
				addWarning("A ddms:phone element was found with no value.");
				break;
			}
		}
		Elements emailElements = getXOMElement().getChildElements(EMAIL_NAME, getNamespace());
		for (int i = 0; i < emailElements.size(); i++) {
			if (Util.isEmpty(emailElements.get(i).getValue())) {
				addWarning("A ddms:email element was found with no value.");
				break;
			}
		}
	}
			
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractProducerEntity))
			return (false);
		AbstractProducerEntity test = (AbstractProducerEntity) obj;
		return (getParentType().equals(test.getParentType())
			&& Util.listEquals(getNames(), test.getNames())
			&& Util.listEquals(getPhones(), test.getPhones())
			&& Util.listEquals(getEmails(), test.getEmails())
			&& getExtensibleAttributes().equals(test.getExtensibleAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getParentType().hashCode();
		result = 7 * result + getNames().hashCode();
		result = 7 * result + getPhones().hashCode();
		result = 7 * result + getEmails().hashCode();
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
		html.append(buildHTMLMeta(getParentType() + ".entityType", getName(), true));
		for (String name : getNames())
			html.append(buildHTMLMeta(getParentType() + ".name", name, true));
		for (String phone : getPhones())
			html.append(buildHTMLMeta(getParentType() + ".phone", phone, true));
		for (String email : getEmails())
			html.append(buildHTMLMeta(getParentType() + ".email", email, true));
		html.append(getExtensibleAttributes().toHTML(getParentType()));
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
		text.append(buildTextLine(getParentType() + " EntityType", getName(), true));
		for (String name : getNames())
			text.append(buildTextLine("name", name, true));
		for (String phone : getPhones())
			text.append(buildTextLine("phone", phone, true));
		for (String email : getEmails())
			text.append(buildTextLine("email", email, true));
		text.append(getExtensibleAttributes().toText(getParentType()));
		return (text.toString());		
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
	 * Accessor for the parentType (i.e. creator, contributor, etc.)
	 */
	protected String getParentType() {
		return _parentType;
	}
	
	/**
	 * Accessor for the extensible attributes. Will always be non-null, even if not set.
	 */
	public ExtensibleAttributes getExtensibleAttributes() {
		return (_cachedExtensibleAttributes);
	}
	
	/**
	 * Abstract Builder for this DDMS component.
	 * 
	 * <p>Builders which are based upon this abstract class should implement the commit() method, returning the appropriate
	 * concrete object type.</p>
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static abstract class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -1694935853087559491L;
		private String _parentType;
		private List<String> _names;
		private List<String> _phones;
		private List<String> _emails;
		private ExtensibleAttributes.Builder _extensibleAttributes;
		
		/**
		 * Empty constructor
		 */
		protected Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		protected Builder(AbstractProducerEntity entity) {
			setParentType(entity.getParentType());
			setNames(entity.getNames());
			setPhones(entity.getPhones());
			setEmails(entity.getEmails());
			setExtensibleAttributes(new ExtensibleAttributes.Builder(entity.getExtensibleAttributes()));
		}
		
		/**
		 * Helper method to determine if any values have been entered for this producer.
		 * 
		 * @return true if all values are empty
		 */
		public boolean isEmpty() {
			return (Util.containsOnlyEmptyValues(getNames())
				&& Util.containsOnlyEmptyValues(getPhones())
				&& Util.containsOnlyEmptyValues(getEmails())
				&& getExtensibleAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the parentType
		 */
		public String getParentType() {
			return _parentType;
		}

		/**
		 * Builder accessor for the parentType
		 */
		public void setParentType(String parentType) {
			_parentType = parentType;
		}

		/**
		 * Builder accessor for the names
		 */
		public List<String> getNames() {
			if (_names == null)
				_names = new LazyList(String.class);
			return _names;
		}

		/**
		 * Builder accessor for the names
		 */
		public void setNames(List<String> names) {
			_names = new LazyList(names, String.class);
		}

		/**
		 * Builder accessor for the phones
		 */
		public List<String> getPhones() {
			if (_phones == null)
				_phones = new LazyList(String.class);
			return _phones;
		}

		/**
		 * Builder accessor for the phones
		 */
		public void setPhones(List<String> phones) {
			_phones = new LazyList(phones, String.class);
		}

		/**
		 * Builder accessor for the emails
		 */
		public List<String> getEmails() {
			if (_emails == null)
				_emails = new LazyList(String.class);
			return _emails;
		}

		/**
		 * Builder accessor for the emails
		 */
		public void setEmails(List<String> emails) {
			_emails = new LazyList(emails, String.class);
		}
		
		/**
		 * Builder accessor for the Extensible Attributes
		 */
		public ExtensibleAttributes.Builder getExtensibleAttributes() {
			if (_extensibleAttributes == null)
				_extensibleAttributes = new ExtensibleAttributes.Builder();
			return _extensibleAttributes;
		}
		
		/**
		 * Builder accessor for the Extensible Attributes
		 */
		public void setExtensibleAttributes(ExtensibleAttributes.Builder extensibleAttributes) {
			_extensibleAttributes = extensibleAttributes;
		}
	}
}