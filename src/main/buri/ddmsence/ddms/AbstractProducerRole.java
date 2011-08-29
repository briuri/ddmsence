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
import buri.ddmsence.ddms.resource.OrganizationX;
import buri.ddmsence.ddms.resource.PersonX;
import buri.ddmsence.ddms.resource.ServiceX;
import buri.ddmsence.ddms.resource.UnknownX;
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
				if (OrganizationX.NAME.equals(entityType))
					_producerEntity = new OrganizationX(producerType, entityElement);
				if (PersonX.NAME.equals(entityType))
					_producerEntity = new PersonX(producerType, entityElement);
				if (ServiceX.NAME.equals(entityType))
					_producerEntity = new ServiceX(producerType, entityElement);
				if (UnknownX.NAME.equals(entityType))
					_producerEntity = new UnknownX(producerType, entityElement);
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
	 * @param securityAttributes any security attributes (optional)
	 */
	protected AbstractProducerRole(String producerType, IProducerEntity producerEntity, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("producer type", producerType);
			Util.requireDDMSValue("producer entity", producerEntity);
			_producerEntity = producerEntity;
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);			
			Element element = Util.buildDDMSElement(producerType, null);
			element.appendChild(producerEntity.getXOMElementCopy());
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
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();	
		Util.requireDDMSValue("producer entity", getProducerEntity());
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
			&& getSecurityAttributes().equals(test.getSecurityAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getProducerEntity().hashCode();
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
		text.append(getSecurityAttributes().toText(getName()));
		return (text.toString());		
	}
		
	/**
	 * Accessor for the producer entity
	 */
	protected IProducerEntity getProducerEntity() {
		return _producerEntity;
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
		private OrganizationX.Builder _organization;
		private PersonX.Builder _person;
		private ServiceX.Builder _service;
		private UnknownX.Builder _unknown;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		protected Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		protected Builder(AbstractProducerRole producer) {
			setEntityType(producer.getProducerEntity().getName());
			if (OrganizationX.NAME.equals(getEntityType()))
				setOrganization(new OrganizationX.Builder((OrganizationX) producer.getProducerEntity()));
			if (PersonX.NAME.equals(getEntityType()))
				setPerson(new PersonX.Builder((PersonX) producer.getProducerEntity()));
			if (ServiceX.NAME.equals(getEntityType()))
				setService(new ServiceX.Builder((ServiceX) producer.getProducerEntity()));
			if (UnknownX.NAME.equals(getEntityType()))
				setUnknown(new UnknownX.Builder((UnknownX) producer.getProducerEntity()));
			setSecurityAttributes(new SecurityAttributes.Builder(producer.getSecurityAttributes()));
		}
		
		/**
		 * Commits the entity which is active in this builder, based on the entityType.
		 * @return the entity
		 */
		protected IProducerEntity commitSelectedEntity() throws InvalidDDMSException {
			if (OrganizationX.NAME.equals(getEntityType()))
				return (getOrganization().commit());
			if (PersonX.NAME.equals(getEntityType()))
				return (getPerson().commit());
			if (ServiceX.NAME.equals(getEntityType()))
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
		public OrganizationX.Builder getOrganization() {
			if (_organization == null)
				_organization = new OrganizationX.Builder();
			return _organization;
		}

		/**
		 * Builder accessor for the organization builder
		 */
		public void setOrganization(OrganizationX.Builder organization) {
			_organization = organization;
		}

		/**
		 * Builder accessor for the person builder
		 */
		public PersonX.Builder getPerson() {
			if (_person == null)
				_person = new PersonX.Builder();
			return _person;
		}

		/**
		 * Builder accessor for the person builder
		 */
		public void setPerson(PersonX.Builder person) {
			_person = person;
		}

		/**
		 * Builder accessor for the service builder
		 */
		public ServiceX.Builder getService() {
			if (_service == null)
				_service = new ServiceX.Builder();
			return _service;
		}

		/**
		 * Builder accessor for the service builder
		 */
		public void setService(ServiceX.Builder service) {
			_service = service;
		}

		/**
		 * Builder accessor for the unknown builder
		 */
		public UnknownX.Builder getUnknown() {
			if (_unknown == null)
				_unknown = new UnknownX.Builder();
			return _unknown;
		}

		/**
		 * Builder accessor for the unknown builder
		 */
		public void setUnknown(UnknownX.Builder unknown) {
			_unknown = unknown;
		}
	}
}