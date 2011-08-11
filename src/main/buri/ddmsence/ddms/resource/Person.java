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
package buri.ddmsence.ddms.resource;

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractProducer;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a producer element containing a ddms:Person element.
 * 
 * <p>Producers are modeled as "entities fulfilling a role", rather than a "role filled by
 * an entity". Please see {@link AbstractProducer} for details.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>At least 1 name value must be non-empty.</li>
 * <li>The surname must be non-empty.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A phone number can be set with no value.</li>
 * <li>An email can be set with no value.</li>
 * <li>A userID can be set with no value.</li>
 * <li>An affiliation can be set with no value.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:name</u>: names of the producer (1-many, at least 1 required)<br />
 * <u>ddms:surname</u>: surname of the producer (exactly 1 required)<br />
 * <u>ddms:userID</u>: userId of the producer (0-1 optional)<br />
 * <u>ddms:affiliation</u>: organizational affiliation (0-1 optional)<br />
 * <u>ddms:phone</u>: phone numbers of the producer (0-many optional)<br />
 * <u>ddms:email</u>: email addresses of the producer (0-many optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * In both DDMS 2.0 and DDMS 3.0, this component can be decorated with optional {@link ExtensibleAttributes}.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Person<br />
 * <u>Description</u>: Information about a person fulfilling some producer role.<br />
 * <u>Obligation</u>: At least one of the four producerTypes is required.<br />
 * <u>Schema Modification Date</u>: 2004-08-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Person extends AbstractProducer {
	
	/** The element name of this component */
	public static final String NAME = "Person";
	
	private static final String AFFILIATION_NAME = "affiliation";
	private static final String USERID_NAME = "userID";
	private static final String SURNAME_NAME = "surname";

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Person(Element element) throws InvalidDDMSException {
		super(element);
	}

	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param producerType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param surname the surname of the person
	 * @param names an ordered list of names
	 * @param userID optional unique identifier within an organization
	 * @param affiliation organizational affiliation of the person
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param securityAttributes any security attributes (optional)
	 */
	public Person(String producerType, String surname, List<String> names, String userID, String affiliation,
		List<String> phones, List<String> emails, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		this(producerType, surname, names, userID, affiliation, phones, emails, securityAttributes, null);
	}

	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param producerType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param surname the surname of the person
	 * @param names an ordered list of names
	 * @param userID optional unique identifier within an organization
	 * @param affiliation organizational affiliation of the person
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param securityAttributes any security attributes (optional)
	 * @param extensions extensible attributes (optional)
	 */
	public Person(String producerType, String surname, List<String> names, String userID, String affiliation,
		List<String> phones, List<String> emails, SecurityAttributes securityAttributes, ExtensibleAttributes extensions)
		throws InvalidDDMSException {
		super(producerType, Person.NAME, names, phones, emails, securityAttributes, extensions, false);
		try {
			int insertIndex = (names == null ? 0 : names.size());
			insertElements(insertIndex, surname, userID, affiliation);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Inserts additional elements into the existing ProducerEntity. Because the personType contains a sequence,
	 * additional fields must be inserted among the name, phone, and email elements.
	 * 
	 * @param insertIndex the index of the position after the last names element
	 * @param surname the surname of the person
	 * @param userID optional unique identifier within an organization
	 * @param affiliation organizational affiliation of the person
	 * @throws InvalidDDMSException if the result is an invalid component
	 */
	private void insertElements(int insertIndex, String surname, String userID, String affiliation)
		throws InvalidDDMSException {
		Element element = getEntityElement();
		// Inserting in reverse order allow the same index to be reused. Later inserts will "push" the early ones
		// forward.
		if (!Util.isEmpty(affiliation))
			element.insertChild(Util.buildDDMSElement(AFFILIATION_NAME, affiliation), insertIndex);
		if (!Util.isEmpty(userID))
			element.insertChild(Util.buildDDMSElement(USERID_NAME, userID), insertIndex);
		element.insertChild(Util.buildDDMSElement(SURNAME_NAME, surname), insertIndex);
		setXOMElement(getXOMElement(), true);
	}

	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>Surname exists and is not empty.</li>
	 * <li>Exactly 1 surname, 0-1 userIDs, 0-1 affiliations exist.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractProducer#validate()
	 * @throws InvalidDDMSException
	 *             if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), getProducerType());
		Util.requireDDMSQName(getEntityElement(), NAME);
		Util.requireDDMSValue(SURNAME_NAME, getSurname());
		Util.requireBoundedDDMSChildCount(getEntityElement(), SURNAME_NAME, 1, 1);
		Util.requireBoundedDDMSChildCount(getEntityElement(), USERID_NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getEntityElement(), AFFILIATION_NAME, 0, 1);
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A ddms:userID element was found with no value.</li>
	 * <li>A ddms:affiliation element was found with no value.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		String ddmsNamespace = getEntityElement().getNamespaceURI();
		if (Util.isEmpty(getUserID()) && getEntityElement().getChildElements(USERID_NAME, ddmsNamespace).size() == 1)
			addWarning("A ddms:userID element was found with no value.");
		if (Util.isEmpty(getAffiliation())
			&& getEntityElement().getChildElements(AFFILIATION_NAME, ddmsNamespace).size() == 1)
			addWarning("A ddms:affiliation element was found with no value.");
	}

	/**
	 * Because ordering is not important in HTML output, this method merely appends the additional Person fields to the
	 * end of the AbstractProducer output. All fields will still be underneath a line identifying the entity type.
	 * 
	 * @see AbstractProducer#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer(super.toHTML());
		html.append(buildHTMLMeta(getProducerType() + ".surname", getSurname(), true));
		html.append(buildHTMLMeta(getProducerType() + ".userID", getUserID(), false));
		html.append(buildHTMLMeta(getProducerType() + ".affiliation", getAffiliation(), false));
		return (html.toString());
	}

	/**
	 * Because ordering is not important in Text output, this method merely appends the additional Person fields to the
	 * end of the AbstractProducer output. All fields will still be underneath a line identifying the entity type.
	 * 
	 * @see AbstractProducer#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer(super.toText());
		text.append(buildTextLine("surname", getSurname(), true));
		text.append(buildTextLine("userID", getUserID(), false));
		text.append(buildTextLine("affiliation", getAffiliation(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Person))
			return (false);
		Person test = (Person) obj;
		return (getSurname().equals(test.getSurname()) 
			&& getUserID().equals(test.getUserID()) 
			&& getAffiliation().equals(test.getAffiliation()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getSurname().hashCode();
		result = 7 * result + getUserID().hashCode();
		result = 7 * result + getAffiliation().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the surname of the person
	 */
	public String getSurname() {
		return (Util.getFirstDDMSChildValue(getEntityElement(), SURNAME_NAME));
	}
	
	/**
	 * Accessor for the userID of the person
	 */
	public String getUserID() {
		return (Util.getFirstDDMSChildValue(getEntityElement(), USERID_NAME));
	}
	
	/**
	 * Accessor for the affiliation of the person
	 */
	public String getAffiliation() {
		return (Util.getFirstDDMSChildValue(getEntityElement(), AFFILIATION_NAME));
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractProducer.Builder {
		private static final long serialVersionUID = -2933889158864177338L;
		private String _surname;
		private String _userID;
		private String _affliation;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Person person) {
			super(person);
			setSurname(person.getSurname());
			setUserID(person.getUserID());
			setAffliation(person.getAffiliation());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Person commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Person(getProducerType(), getSurname(), getNames(), getUserID(), getAffliation(), 
				getPhones(), getEmails(), getSecurityAttributes().commit(), getExtensibleAttributes().commit()));
		}

		/**
		 * Helper method to determine if any values have been entered for this Person.
		 * 
		 * @return true if all values are empty
		 */
		public boolean isEmpty() {
			return (super.isEmpty()
				&& Util.isEmpty(getSurname())
				&& Util.isEmpty(getUserID())
				&& Util.isEmpty(getAffliation()));
		}
		
		/**
		 * Builder accessor for the surname
		 */
		public String getSurname() {
			return _surname;
		}

		/**
		 * Builder accessor for the surname
		 */
		public void setSurname(String surname) {
			_surname = surname;
		}

		/**
		 * Builder accessor for the userID
		 */
		public String getUserID() {
			return _userID;
		}

		/**
		 * Builder accessor for the userID
		 */
		public void setUserID(String userID) {
			_userID = userID;
		}

		/**
		 * Builder accessor for the affliation
		 */
		public String getAffliation() {
			return _affliation;
		}

		/**
		 * Builder accessor for the affliation
		 */
		public void setAffliation(String affliation) {
			_affliation = affliation;
		}
	}
} 