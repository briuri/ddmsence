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

import nu.xom.Element;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.RequestorInfo;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * Base class for DDMS tasking role elements, including ddms:requestorInfo and ddms:addressee.
 * 
 * <p>
 * Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set before
 * the component is used.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:person</u>: the person entity in this role (0-1, optional)<br />
 * <u>ddms:organization</u>: The organization entity in this role (0-1, optional)<br />
 * Only one of the nested entities can appear.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public abstract class AbstractTaskingRole extends AbstractBaseComponent {
	
	private IRoleEntity _entity;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element representing this component
	 */
	protected AbstractTaskingRole(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("producer element", element);
			setXOMElement(element, false);
			if (element.getChildElements().size() > 0) {
				Element entityElement = element.getChildElements().get(0);
				String entityType = entityElement.getLocalName();
				if (Organization.getName(getDDMSVersion()).equals(entityType))
					_entity = new Organization(entityElement);
				if (Person.getName(getDDMSVersion()).equals(entityType))
					_entity = new Person(entityElement);
			}
			_cachedSecurityAttributes = new SecurityAttributes(element);
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}		
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param roleType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param entity the actual entity fulfilling this role
	 * @param securityAttributes any security attributes (optional)
	 */
	protected AbstractTaskingRole(String roleType, IRoleEntity entity, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("entity", entity);
			_entity = entity;
			Element element = Util.buildDDMSElement(roleType, null);
			element.appendChild(entity.getXOMElementCopy());
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);			
			_cachedSecurityAttributes.addTo(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
			
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The entity exists and is either a Person or an Organization.</li>
	 * <li>The entity uses the same version of DDMS as this element.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot exist until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSValue("entity", getEntity());
		if (!(getEntity() instanceof Organization)
			&& !(getEntity() instanceof Person)) {
			throw new InvalidDDMSException("The entity must be a person or an organization.");
		}
		Util.requireCompatibleVersion(this, getEntity());
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();
		
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0"))
			throw new InvalidDDMSException("The ddms:" + RequestorInfo.getName(getDDMSVersion()) + " element cannot be used until DDMS 4.0 or later.");
		
		validateSharedWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the person or organization.</li>
	 * <li>Include any validation warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateSharedWarnings() {
		if (getEntity() != null)
			addWarnings(getEntity().getValidationWarnings(), false);
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
			
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractTaskingRole))
			return (false);
		AbstractTaskingRole test = (AbstractTaskingRole) obj;
		return (getEntity().equals(test.getEntity())
			&& getSecurityAttributes().equals(test.getSecurityAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getEntity().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		return (result);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		return (toHTML(""));
	}

	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		return (toText(""));
	}
	
	/**
	 * Outputs to HTML with a prefix at the beginning of each meta tag.
	 * 
	 * @param prefix the prefix to add
	 * @return the HTML output
	 */
	public String toHTML(String prefix) {
		prefix = Util.getNonNullString(prefix) + getName();
		StringBuffer html = new StringBuffer();
		html.append(getEntity().toHTML(prefix + "."));
		html.append(getSecurityAttributes().toHTML(prefix));
		return (html.toString());
	}
	
	/**
	 * Outputs to Text with a prefix at the beginning of each line.
	 * 
	 * @param prefix the prefix to add
	 * @return the Text output
	 */
	public String toText(String prefix) {
		prefix = Util.getNonNullString(prefix) + getName();
		StringBuffer text = new StringBuffer();
		text.append(getEntity().toText(prefix + "."));
		text.append(getSecurityAttributes().toText(prefix));
		return (text.toString());	
	}
		
	/**
	 * Accessor for the producer entity
	 */
	public IRoleEntity getEntity() {
		return _entity;
	}

	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
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
		private String _entityType;
		private Organization.Builder _organization;
		private Person.Builder _person;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Base constructor
		 */
		protected Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		protected Builder(AbstractTaskingRole role) {
			DDMSVersion version = role.getDDMSVersion();
			setEntityType(role.getEntity().getName());
			if (Organization.getName(version).equals(getEntityType()))
				setOrganization(new Organization.Builder((Organization) role.getEntity()));
			if (Person.getName(version).equals(getEntityType()))
				setPerson(new Person.Builder((Person) role.getEntity()));
			setSecurityAttributes(new SecurityAttributes.Builder(role.getSecurityAttributes()));
		}
		
		/**
		 * Commits the entity which is active in this builder, based on the entityType.
		 * @return the entity
		 */
		protected IRoleEntity commitSelectedEntity() throws InvalidDDMSException {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			if (Organization.getName(version).equals(getEntityType()))
				return (getOrganization().commit());
			return (getPerson().commit());
		}
		
		/**
		 * Helper method to determine if any values have been entered for this producer.
		 * 
		 * @return true if all values are empty
		 */
		public boolean isEmpty() {	
			return (getOrganization().isEmpty()
				&& getPerson().isEmpty()
				&& getSecurityAttributes().isEmpty());
		}
				
		/**
		 * Builder accessor for the Security Attributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the Security Attributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}

		/**
		 * Builder accessor for the entityType, which determines which of the 4 entity builders are used.
		 */
		public String getEntityType() {
			return _entityType;
		}

		/**
		 * Builder accessor for the entityType, which determines which of the 4 entity builders are used.
		 */
		public void setEntityType(String entityType) {
			_entityType = entityType;
		}

		/**
		 * Builder accessor for the organization builder
		 */
		public Organization.Builder getOrganization() {
			if (_organization == null) {
				_organization = new Organization.Builder();
			}
			return _organization;
		}

		/**
		 * Builder accessor for the organization builder
		 */
		public void setOrganization(Organization.Builder organization) {
			_organization = organization;
		}

		/**
		 * Builder accessor for the person builder
		 */
		public Person.Builder getPerson() {
			if (_person == null) {
				_person = new Person.Builder();
			}
			return _person;
		}

		/**
		 * Builder accessor for the person builder
		 */
		public void setPerson(Person.Builder person) {
			_person = person;
		}
	}
}