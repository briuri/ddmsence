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

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.AbstractProducerRole;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IProducerEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a ddms:requestorInfo element.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:person</u>: the person who requested the production of this resource (0-1, optional)<br />
 * <u>ddms:organization</u>: The organization who requested the production of this resource (0-1, optional)<br />
 * Only one of the nested entities can appear in a requestorInfo element.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: The person or organization who requested the production of this resource.<br />
 * <u>Obligation</u>: At least 1 requestorInfo is mandatory.<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RequestorInfo extends AbstractBaseComponent {

	private IProducerEntity _producerEntity;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public RequestorInfo(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("element", element);
			setXOMElement(element, false);
			if (element.getChildElements().size() > 0) {
				Element entityElement = element.getChildElements().get(0);
				String entityType = entityElement.getLocalName();
				if (Organization.getName(getDDMSVersion()).equals(entityType))
					_producerEntity = new Organization(entityElement);
				if (Person.getName(getDDMSVersion()).equals(entityType))
					_producerEntity = new Person(entityElement);
			}
			_cachedSecurityAttributes = new SecurityAttributes(element);
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param producerEntity the actual entity who is the requestor (required)
	 * @param securityAttributes any security attributes (required)
	 */
	public RequestorInfo(IProducerEntity producerEntity, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("producer entity", producerEntity);
			_producerEntity = producerEntity;
			Element element = Util.buildDDMSElement(RequestorInfo.getName(DDMSVersion.getCurrentVersion()), null);
			element.appendChild(producerEntity.getXOMElementCopy());
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
	 * <li>The qualified name of the element is correct.</li>
	 * <li>The entity exists and is either a Person or an Organization.</li>
	 * <li>The entity uses the same version of DDMS as this element.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot exist until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractProducerRole#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), RequestorInfo.getName(getDDMSVersion()));
		Util.requireDDMSValue("entity", getProducerEntity());
		if (!(getProducerEntity() instanceof Organization)
			&& !(getProducerEntity() instanceof Person)) {
			throw new InvalidDDMSException("The requestor must be a person or an organization.");
		}
		Util.requireCompatibleVersion(this, getProducerEntity());
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();
		
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0"))
			throw new InvalidDDMSException("The ddms:" + RequestorInfo.getName(getDDMSVersion()) + " element cannot be used until DDMS 4.0 or later.");
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof RequestorInfo))
			return (false);
		RequestorInfo test = (RequestorInfo) obj;
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
		html.append(getProducerEntity().toHTML(prefix + "."));
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
		text.append(getProducerEntity().toText(prefix + "."));
		text.append(getSecurityAttributes().toText(prefix));
		return (text.toString());	
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("requestorInfo");
	}
	
	/**
	 * Accessor for the producer entity
	 */
	public IProducerEntity getProducerEntity() {
		return _producerEntity;
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 4565840434345629470L;
		private String _entityType;
		private Organization.Builder _organization;
		private Person.Builder _person;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(RequestorInfo info) {
			DDMSVersion version = info.getDDMSVersion();
			setEntityType(info.getProducerEntity().getName());
			if (Organization.getName(version).equals(getEntityType()))
				setOrganization(new Organization.Builder((Organization) info.getProducerEntity()));
			if (Person.getName(version).equals(getEntityType()))
				setPerson(new Person.Builder((Person) info.getProducerEntity()));
			setSecurityAttributes(new SecurityAttributes.Builder(info.getSecurityAttributes()));
		
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public RequestorInfo commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new RequestorInfo(commitSelectedEntity(), getSecurityAttributes().commit()));
		}
		
		/**
		 * Commits the entity which is active in this builder, based on the entityType.
		 * @return the entity
		 */
		protected IProducerEntity commitSelectedEntity() throws InvalidDDMSException {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			if (Organization.getName(version).equals(getEntityType()))
				return (getOrganization().commit());
			return (getPerson().commit());
		}


		/**
		 * Helper method to determine if any values have been entered.
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
