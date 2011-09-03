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
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
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
 * to contact them (optional, starting in DDMS 4.0).
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ICISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public abstract class AbstractProducerRole extends AbstractBaseComponent {
	
	private IProducerEntity _producerEntity;
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
			if (element.getChildElements().size() > 0) {
				Element entityElement = element.getChildElements().get(0);
				String producerType = element.getLocalName();
				String entityType = entityElement.getLocalName();
				if (Organization.NAME.equals(entityType))
					_producerEntity = new Organization(producerType, entityElement);
				if (Person.NAME.equals(entityType))
					_producerEntity = new Person(producerType, entityElement);
				if (Service.NAME.equals(entityType))
					_producerEntity = new Service(producerType, entityElement);
				if (Unknown.NAME.equals(entityType))
					_producerEntity = new Unknown(producerType, entityElement);
			}
			_cachedSecurityAttributes = new SecurityAttributes(element);
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
	 * @param producerEntity the actual entity fulfilling this role
	 * @param pocType the POCType attribute (starting in DDMS 4.0)
	 * @param securityAttributes any security attributes (optional)
	 */
	protected AbstractProducerRole(String producerType, IProducerEntity producerEntity, String pocType,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("producer type", producerType);
			Util.requireDDMSValue("producer entity", producerEntity);
			_producerEntity = producerEntity;
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);			
			Element element = Util.buildDDMSElement(producerType, null);
			element.appendChild(producerEntity.getXOMElementCopy());
			if (!Util.isEmpty(pocType))
				Util.addDDMSAttribute(element, POC_TYPE_NAME, pocType);
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
	 * <li>The producer entity is using the same version of DDMS as this producer role.</li>
	 * <li>The POCType cannot be used before DDMS 4.0.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();	
		Util.requireDDMSValue("producer entity", getProducerEntity());
		Util.requireCompatibleVersion(this, getProducerEntity());
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0") && !Util.isEmpty(getPOCType())) {
			throw new InvalidDDMSException("This component cannot have a POCType until DDMS 4.0 or later.");
		}
		validateSharedWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateSharedWarnings() {
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
			
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractProducerRole))
			return (false);
		AbstractProducerRole test = (AbstractProducerRole) obj;
		return (getProducerEntity().equals(test.getProducerEntity())
			&& getPOCType().equals(test.getPOCType())
			&& getSecurityAttributes().equals(test.getSecurityAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getProducerEntity().hashCode();
		result = 7 * result + getPOCType().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
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
		html.append(getProducerEntity().toHTML());
		html.append(buildHTMLMeta(getName() + "." + POC_TYPE_NAME, getPOCType(), false));
		html.append(getSecurityAttributes().toHTML(getName()));
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
		text.append(getProducerEntity().toText());
		text.append(buildTextLine(POC_TYPE_NAME, getPOCType(), false));
		text.append(getSecurityAttributes().toText(getName()));
		return (text.toString());		
	}
		
	/**
	 * Accessor for the producer entity
	 */
	public IProducerEntity getProducerEntity() {
		return _producerEntity;
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

		private String _producerType;
		private String _entityType;
		private Organization.Builder _organization;
		private Person.Builder _person;
		private Service.Builder _service;
		private Unknown.Builder _unknown;
		private String _pocType;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Base constructor
		 * 
		 * @param producerType the producer type for this role
		 */
		protected Builder(String producerType) {
			setProducerType(producerType);
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		protected Builder(AbstractProducerRole producer) {
			setProducerType(producer.getName());
			setEntityType(producer.getProducerEntity().getName());
			if (Organization.NAME.equals(getEntityType()))
				setOrganization(new Organization.Builder((Organization) producer.getProducerEntity()));
			if (Person.NAME.equals(getEntityType()))
				setPerson(new Person.Builder((Person) producer.getProducerEntity()));
			if (Service.NAME.equals(getEntityType()))
				setService(new Service.Builder((Service) producer.getProducerEntity()));
			if (Unknown.NAME.equals(getEntityType()))
				setUnknown(new Unknown.Builder((Unknown) producer.getProducerEntity()));
			setPocType(producer.getPOCType());
			setSecurityAttributes(new SecurityAttributes.Builder(producer.getSecurityAttributes()));
		}
		
		/**
		 * Commits the entity which is active in this builder, based on the entityType.
		 * @return the entity
		 */
		protected IProducerEntity commitSelectedEntity() throws InvalidDDMSException {
			if (Organization.NAME.equals(getEntityType()))
				return (getOrganization().commit());
			if (Person.NAME.equals(getEntityType()))
				return (getPerson().commit());
			if (Service.NAME.equals(getEntityType()))
				return (getService().commit());
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
				_organization.setParentType(getProducerType());
			}
			return _organization;
		}

		/**
		 * Builder accessor for the organization builder
		 */
		public void setOrganization(Organization.Builder organization) {
			_organization = organization;
			_organization.setParentType(getProducerType());
		}

		/**
		 * Builder accessor for the person builder
		 */
		public Person.Builder getPerson() {
			if (_person == null) {
				_person = new Person.Builder();
				_person.setParentType(getProducerType());
			}
			return _person;
		}

		/**
		 * Builder accessor for the person builder
		 */
		public void setPerson(Person.Builder person) {
			_person = person;
			_person.setParentType(getProducerType());
		}

		/**
		 * Builder accessor for the service builder
		 */
		public Service.Builder getService() {
			if (_service == null) {
				_service = new Service.Builder();
				_service.setParentType(getProducerType());
			}
			return _service;
		}

		/**
		 * Builder accessor for the service builder
		 */
		public void setService(Service.Builder service) {
			_service = service;
			_service.setParentType(getProducerType());
		}

		/**
		 * Builder accessor for the unknown builder
		 */
		public Unknown.Builder getUnknown() {
			if (_unknown == null) {
				_unknown = new Unknown.Builder();
				_unknown.setParentType(getProducerType());
			}
			return _unknown;
		}

		/**
		 * Builder accessor for the unknown builder
		 */
		public void setUnknown(Unknown.Builder unknown) {
			_unknown = unknown;
			_unknown.setParentType(getProducerType());
		}

		/**
		 * Builder accessor for the producerType
		 */
		public String getProducerType() {
			return _producerType;
		}

		/**
		 * Builder accessor for the producerType
		 */
		public void setProducerType(String producerType) {
			_producerType = producerType;
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