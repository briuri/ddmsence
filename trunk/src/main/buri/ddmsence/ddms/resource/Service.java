/* Copyright 2010 - 2013 by Brian Uri!
   
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

import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractRoleEntity;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a ddms:service element.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>At least 1 name value must be non-empty. This rule is codified in the schema, starting in DDMS 5.0.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A phone number can be set with no value. This loophole goes away in DDMS 5.0.</li>
 * <li>An email can be set with no value. This loophole goes away in DDMS 5.0.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <p>The name of this component was changed from "Service" to "service" in DDMS 4.0.1.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:name</u>: names of the producer entity (1-many, at least 1 required)<br />
 * <u>ddms:phone</u>: phone numbers of the producer entity (0-many optional)<br />
 * <u>ddms:email</u>: email addresses of the producer entity (0-many optional)<br />
 * <u>ddms:affiliation</u>: organizational affiliation (0-many optional starting in DDMS 5.0)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link ExtensibleAttributes}</u>: Custom attributes
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Service extends AbstractRoleEntity {
	
	private List<String> _affiliations = null;
	
	private static final String AFFILIATION_NAME = "affiliation";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Service(Element element) throws InvalidDDMSException {
		super(element, true);
		_affiliations = Util.getDDMSChildValues(element, AFFILIATION_NAME);
	}

	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @deprecated A new constructor was added for DDMS 5.0 to support ddms:affiliation. This constructor is preserved for 
	 * backwards compatibility, but may disappear in the next major release.
	 * 
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 */
	public Service(List<String> names, List<String> phones, List<String> emails)
		throws InvalidDDMSException {
		this(names, phones, emails, null, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param affiliations the affiliations of the service
	 * @param extensions extensible attributes (optional)
	 */
	public Service(List<String> names, List<String> phones, List<String> emails, List<String> affiliations,
		ExtensibleAttributes extensions) throws InvalidDDMSException {
		super(Service.getName(DDMSVersion.getCurrentVersion()), names, phones, emails, extensions);
		try {
			if (affiliations == null)
				affiliations = Collections.emptyList();
			Element element = getXOMElement();
			for (String affiliation : affiliations)
				element.appendChild(Util.buildDDMSElement(AFFILIATION_NAME, affiliation));
			_affiliations = affiliations;
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}			
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractRoleEntity#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Service.getName(getDDMSVersion()));
		super.validate();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, "", suffix);
		StringBuffer text = new StringBuffer(super.getOutput(isHTML, localPrefix, ""));
		text.append(buildOutput(isHTML, localPrefix + AFFILIATION_NAME, getAffiliations()));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Service))
			return (false);
		Service test = (Service) obj;
		return (Util.listEquals(getAffiliations(), test.getAffiliations()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getAffiliations().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the affiliations of the person
	 */
	public List<String> getAffiliations() {
		return (Collections.unmodifiableList(_affiliations));
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return (version.isAtLeast("4.0.1") ? "service" : "Service");
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractRoleEntity.Builder {
		private static final long serialVersionUID = 7653534173085296283L;
		private List<String> _affiliations;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Service service) {
			super(service);
			setAffiliations(service.getAffiliations());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Service commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Service(getNames(), getPhones(), getEmails(), getAffiliations(),
				getExtensibleAttributes().commit()));
		}
		
		/**
		 * Helper method to determine if any values have been entered for this Person.
		 * 
		 * @return true if all values are empty
		 */
		public boolean isEmpty() {
			return (super.isEmpty()
				&& Util.containsOnlyEmptyValues(getAffiliations()));
		}
		
		/**
		 * Builder accessor for the affiliations
		 */
		public List<String> getAffiliations() {
			if (_affiliations == null)
				_affiliations = new LazyList(String.class);
			return _affiliations;
		}

		/**
		 * Builder accessor for the affiliations
		 */
		public void setAffiliations(List<String> affiliations) {
			_affiliations = new LazyList(affiliations, String.class);
		}
	}
} 