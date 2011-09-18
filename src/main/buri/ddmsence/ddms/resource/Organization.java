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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractRoleEntity;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a ddms:organization element.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>At least 1 name value must be non-empty.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A phone number can be set with no value.</li>
 * <li>An email can be set with no value.</li>
 * <li>An acronym can be set with no value.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <p>The name of this component was changed from "Organization" to "organization" in DDMS 4.0.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:name</u>: names of the producer entity (1-many, at least 1 required)<br />
 * <u>ddms:phone</u>: phone numbers of the producer entity (0-many optional)<br />
 * <u>ddms:email</u>: email addresses of the producer entity (0-many optional)<br />
 * <u>ddms:subOrganization</u>: suborganization (0-many optional, starting in DDMS 4.0)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:acronym</u>: an acronym for the organization (optional, starting in DDMS 4.0)<br />
 * <u>{@link ExtensibleAttributes}</u>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: Information about an organization.<br />
 * <u>Obligation</u>: At least one of the four producerTypes is required.<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Organization extends AbstractRoleEntity {
	
	private List<SubOrganization> _cachedSubOrganizations;
	
	private static final String ACRONYM_NAME = "acronym";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Organization(Element element) throws InvalidDDMSException {
		super(element, false);
		try {
			_cachedSubOrganizations = new ArrayList<SubOrganization>();
			String namespace = element.getNamespaceURI();
			Elements components = element.getChildElements(SubOrganization.getName(getDDMSVersion()), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedSubOrganizations.add(new SubOrganization(components.get(i)));
			}
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
		
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param subOrganizations an ordered list of suborganizations
	 * @param acronym the organization's acronym
	 */
	public Organization(List<String> names, List<String> phones, List<String> emails,
		List<SubOrganization> subOrganizations, String acronym)
		throws InvalidDDMSException {
		this(names, phones, emails, subOrganizations, acronym, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param subOrganizations an ordered list of suborganizations
	 * @param acronym the organization's acronym
	 * @param extensions extensible attributes (optional)
	 */
	public Organization(List<String> names, List<String> phones, List<String> emails,
		List<SubOrganization> subOrganizations, String acronym, ExtensibleAttributes extensions)
		throws InvalidDDMSException {
		super(Organization.getName(DDMSVersion.getCurrentVersion()), names, phones, emails, extensions, false);
		try {
			if (subOrganizations == null)
				subOrganizations = Collections.emptyList();
			if (!Util.isEmpty(acronym))
				Util.addDDMSAttribute(getXOMElement(), ACRONYM_NAME, acronym);
			for (SubOrganization subOrganization : subOrganizations) {
				getXOMElement().appendChild(subOrganization.getXOMElementCopy());
			}
			_cachedSubOrganizations = subOrganizations;
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>Acronyms cannot exist until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractRoleEntity#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Organization.getName(getDDMSVersion()));
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0")) {
			if (!Util.isEmpty(getAcronym()))
				throw new InvalidDDMSException("An organization cannot have an acronym until DDMS 4.0 or later.");
		}
		super.validate();
	}
		
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A ddms:acronym attribute was found with no value.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (getDDMSVersion().isAtLeast("4.0")) {
			if (Util.isEmpty(getAcronym())
				&& getXOMElement().getAttribute(ACRONYM_NAME, getNamespace()) != null)
			addWarning("A ddms:acronym attribute was found with no value.");
		}
		super.validateWarnings();
	}

	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix);
		StringBuffer text = new StringBuffer(super.getOutput(isHTML, prefix));
		for (SubOrganization subOrg : getSubOrganizations())
			text.append(subOrg.getOutput(isHTML, prefix));	
		text.append(buildOutput(isHTML, prefix + ACRONYM_NAME, getAcronym(), false));
		return (text.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.addAll(getSubOrganizations());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Organization))
			return (false);
		Organization test = (Organization) obj;
		return (getAcronym().equals(test.getAcronym()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getAcronym().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return (version.isAtLeast("4.0") ? "organization" : "Organization");
	}
	
	/**
	 * Accessor for the suborganizations (0-many)
	 */
	public List<SubOrganization> getSubOrganizations() {
		return (Collections.unmodifiableList(_cachedSubOrganizations));
	}
	
	/**
	 * Accessor for the acronym
	 */
	public String getAcronym() {
		return (getAttributeValue(ACRONYM_NAME));
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractRoleEntity.Builder {
		private static final long serialVersionUID = 4565840434345629470L;
		private List<SubOrganization.Builder> _subOrganizations;
		private String _acronym;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Organization organization) {
			super(organization);
			for (SubOrganization subOrg : organization.getSubOrganizations())
				getSubOrganizations().add(new SubOrganization.Builder(subOrg));
			setAcronym(organization.getAcronym());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Organization commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<SubOrganization> subOrgs = new ArrayList<SubOrganization>();
			for (IBuilder builder : getSubOrganizations()) {
				SubOrganization component = (SubOrganization) builder.commit();
				if (component != null)
					subOrgs.add(component);
			}
			return (new Organization(getNames(), getPhones(), getEmails(), subOrgs, getAcronym(),
				getExtensibleAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getSubOrganizations())
				hasValueInList = hasValueInList || !builder.isEmpty();
			return (super.isEmpty()
				&& !hasValueInList
				&& Util.isEmpty(getAcronym()));
		}
		
		/**
		 * Builder accessor for suborganizations
		 */
		public List<SubOrganization.Builder> getSubOrganizations() {
			if (_subOrganizations == null)
				_subOrganizations = new LazyList(SubOrganization.Builder.class);
			return _subOrganizations;
		}
		
		/**
		 * Builder accessor for the acronym
		 */
		public String getAcronym() {
			return _acronym;
		}

		/**
		 * Builder accessor for the acronym
		 */
		public void setAcronym(String acronym) {
			_acronym = acronym;
		}
	}
} 