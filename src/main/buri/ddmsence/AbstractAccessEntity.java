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
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ntk.SystemName;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Base class for NTK elements which describe system access rules for an individual, group, or profile.
 * 
 * <p> Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set
 * before the component is used. </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public abstract class AbstractAccessEntity extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private SystemName _cachedSystemName;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/**
	 * Constructor for creating a component from a XOM Element. Does not validate.
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public AbstractAccessEntity(Element element) throws InvalidDDMSException {
		Util.requireDDMSValue("element", element);
		setXOMElement(element, false);
		Element systemElement = element.getFirstChildElement(SystemName.getName(getDDMSVersion()), getNamespace());
		if (systemElement != null)
			_cachedSystemName = new SystemName(systemElement);
		_cachedSecurityAttributes = new SecurityAttributes(element);
	}
	
	/**
	 * Constructor for creating a component from raw data. Does not validate.
	 *  
	 * @param systemName the system name (required)
	 * @param securityAttributes security attributes (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public AbstractAccessEntity(String name, SystemName systemName, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildElement(PropertyReader.getPrefix("ntk"), name,
			version.getNtkNamespace(), null);
		setXOMElement(element, false);
		if (systemName != null)
			element.appendChild(systemName.getXOMElementCopy());
		_cachedSystemName = systemName;
		_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
			: securityAttributes);
		_cachedSecurityAttributes.addTo(element);
	}
			
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A systemName is required.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot exist until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSValue("systemName", getSystemName());
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");

		super.validate();
	}
				
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.add(getSystemName());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractAccessEntity))
			return (false);
		return (true);		
	}
	
	/**
	 * Accessor for the system name
	 */
	public SystemName getSystemName() {
		return _cachedSystemName;
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
	public abstract static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 7851044806424206976L;
		private SystemName.Builder _systemName;
		private SecurityAttributes.Builder _securityAttributes;		
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(AbstractAccessEntity group) {
			if (group.getSystemName() != null)
				setSystemName(new SystemName.Builder(group.getSystemName()));
			setSecurityAttributes(new SecurityAttributes.Builder(group.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (getSystemName().isEmpty() && getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the systemName
		 */
		public SystemName.Builder getSystemName() {
			if (_systemName == null)
				_systemName = new SystemName.Builder();
			return _systemName;
		}

		/**
		 * Builder accessor for the systemName
		 */
		public void setSystemName(SystemName.Builder systemName) {
			_systemName = systemName;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}
	}
} 