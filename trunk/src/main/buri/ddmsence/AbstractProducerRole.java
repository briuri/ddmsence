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
package buri.ddmsence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * Base class for DDMS producer elements, such as ddms:creator and ddms:contributor.
 * 
 * <p>
 * Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set before
 * the component is used.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:POCType</u>: Indicates that the element specifies a point-of-contact (POC) and the methods with which
 * to contact them (optional, starting in DDMS 4.0).<br />
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public abstract class AbstractProducerRole extends AbstractBaseComponent {
	
	private IRoleEntity _entity;
	private SecurityAttributes _cachedSecurityAttributes = null;

	private static final String POC_TYPE_NAME = "POCType";
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element representing this component
	 */
	protected AbstractProducerRole(Element element) throws InvalidDDMSException {
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
				if (Service.getName(getDDMSVersion()).equals(entityType))
					_entity = new Service(entityElement);
				if (Unknown.getName(getDDMSVersion()).equals(entityType))
					_entity = new Unknown(entityElement);
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
	 * @param producerType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param entity the actual entity fulfilling this role
	 * @param pocType the POCType attribute (starting in DDMS 4.0)
	 * @param securityAttributes any security attributes (optional)
	 */
	protected AbstractProducerRole(String producerType, IRoleEntity entity, String pocType,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("producer type", producerType);
			Util.requireDDMSValue("entity", entity);
			_entity = entity;
			Element element = Util.buildDDMSElement(producerType, null);
			element.appendChild(entity.getXOMElementCopy());
			Util.addDDMSAttribute(element, POC_TYPE_NAME, pocType);
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
	 * <li>A producer entity exists.</li>
	 * <li>The POCType cannot be used before DDMS 4.0.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSValue("entity", getEntity());
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0") && !Util.isEmpty(getPOCType())) {
			throw new InvalidDDMSException("This component cannot have a POCType until DDMS 4.0 or later.");
		}
		super.validate();	
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractProducerRole))
			return (false);
		AbstractProducerRole test = (AbstractProducerRole) obj;
		return (getPOCType().equals(test.getPOCType()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getPOCType().hashCode();
		return (result);
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(getEntity().getOutput(isHTML, prefix));
		text.append(buildOutput(isHTML, prefix + POC_TYPE_NAME, getPOCType(), false));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.add(getEntity());
		return (list);
	}
	
	/**
	 * Accessor for the entity fulfilling this producer role
	 */
	public IRoleEntity getEntity() {
		return _entity;
	}
		
	/**
	 * Accessor for the POCType attribute.
	 */
	public String getPOCType() {
		return (getAttributeValue(POC_TYPE_NAME)); 
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
		private Service.Builder _service;
		private Unknown.Builder _unknown;
		private String _pocType;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Base constructor
		 */
		protected Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		protected Builder(AbstractProducerRole producer) {
			setEntityType(producer.getEntity().getName());
			DDMSVersion version = producer.getDDMSVersion();
			if (Organization.getName(version).equals(getEntityType()))
				setOrganization(new Organization.Builder((Organization) producer.getEntity()));
			if (Person.getName(version).equals(getEntityType()))
				setPerson(new Person.Builder((Person) producer.getEntity()));
			if (Service.getName(version).equals(getEntityType()))
				setService(new Service.Builder((Service) producer.getEntity()));
			if (Unknown.getName(version).equals(getEntityType()))
				setUnknown(new Unknown.Builder((Unknown) producer.getEntity()));
			setPocType(producer.getPOCType());
			setSecurityAttributes(new SecurityAttributes.Builder(producer.getSecurityAttributes()));
		}
		
		/**
		 * Commits the entity which is active in this builder, based on the entityType.
		 * @return the entity
		 */
		protected IRoleEntity commitSelectedEntity() throws InvalidDDMSException {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			if (Organization.getName(version).equals(getEntityType())) {
				return (getOrganization().commit());
			}
			if (Person.getName(version).equals(getEntityType())) {
				return (getPerson().commit());
			}
			if (Service.getName(version).equals(getEntityType())) {
				return (getService().commit());
			}
			return (getUnknown().commit());
		}
		
		/**
		 * Helper method to determine if any values have been entered for this producer.
		 * 
		 * @return true if all values are empty
		 */
		public boolean isEmpty() {	
			return (getOrganization().isEmpty()
				&& getPerson().isEmpty()
				&& getService().isEmpty()
				&& getUnknown().isEmpty()
				&& Util.isEmpty(getPocType())
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

		/**
		 * Builder accessor for the service builder
		 */
		public Service.Builder getService() {
			if (_service == null) {
				_service = new Service.Builder();
			}
			return _service;
		}

		/**
		 * Builder accessor for the service builder
		 */
		public void setService(Service.Builder service) {
			_service = service;
		}

		/**
		 * Builder accessor for the unknown builder
		 */
		public Unknown.Builder getUnknown() {
			if (_unknown == null) {
				_unknown = new Unknown.Builder();
			}
			return _unknown;
		}

		/**
		 * Builder accessor for the unknown builder
		 */
		public void setUnknown(Unknown.Builder unknown) {
			_unknown = unknown;
		}

		/**
		 * Builder accessor for the pocType attribute
		 */
		public String getPocType() {
			return _pocType;
		}

		/**
		 * Builder accessor for the pocType attribute
		 */
		public void setPocType(String pocType) {
			_pocType = pocType;
		}
	}
}