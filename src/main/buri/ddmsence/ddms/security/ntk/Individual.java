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
package buri.ddmsence.ddms.security.ntk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ntk:AccessIndividual.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ntk:AccessSystemName</u>: The system described by this access record (exactly 1 required), implemented as a {@link SystemName}<br />
 * <u>ntk:AccessIndividualValue</u>: The value used to describe the individual (1-to-many required), implemented as an {@link IndividualValue}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 *  
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: An access record describing system access for indivdiuals.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class Individual extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private SystemName _cachedSystemName;
	private List<IndividualValue> _cachedIndividualValues;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Individual(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("element", element);
			setXOMElement(element, false);
			Element systemElement = element.getFirstChildElement(SystemName.getName(getDDMSVersion()), getNamespace());
			if (systemElement != null)
				_cachedSystemName = new SystemName(systemElement);
			Elements values = element.getChildElements(IndividualValue.getName(getDDMSVersion()), getNamespace());
			_cachedIndividualValues = new ArrayList<IndividualValue>();
			for (int i = 0; i < values.size(); i++) {
				_cachedIndividualValues.add(new IndividualValue(values.get(i)));
			}			
			_cachedSecurityAttributes = new SecurityAttributes(element);
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param systemName the system name (required)
	 * @param individualValues the list of values (at least 1 required)
	 * @param attributes security attributes (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Individual(SystemName systemName, List<IndividualValue> individualValues,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			if (individualValues == null)
				individualValues = Collections.emptyList();
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			Element element = Util.buildElement(PropertyReader.getProperty("ntk.prefix"), Individual.getName(version),
				version.getNtkNamespace(), null);
			setXOMElement(element, false);
			if (systemName != null)
				element.appendChild(systemName.getXOMElementCopy());
			for (IndividualValue value : individualValues) {
				element.appendChild(value.getXOMElementCopy());
			}

			_cachedSystemName = systemName;
			_cachedIndividualValues = individualValues;
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);
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
	 * <li>A systemName is required.</li>
	 * <li>At least 1 individual value is required.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot exist until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getNamespace(), Individual.getName(getDDMSVersion()));
		Util.requireDDMSValue("systemName", getSystemName());
		if (getIndividualValues().isEmpty())
			throw new InvalidDDMSException("At least one individual value is required.");
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");

		super.validate();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + "individual.";
		StringBuffer text = new StringBuffer();
		if (getSystemName() != null)
			text.append(getSystemName().getOutput(isHTML, prefix));
		for (IndividualValue value : getIndividualValues())
			text.append(value.getOutput(isHTML, prefix));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.add(getSystemName());
		list.addAll(getIndividualValues());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Individual))
			return (false);
		return (true);		
	}

	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("AccessIndividual");
	}
	
	/**
	 * Accessor for the system name
	 */
	public SystemName getSystemName() {
		return _cachedSystemName;
	}

	/**
	 * Accessor for the list of individual values (1-many)
	 */
	public List<IndividualValue> getIndividualValues() {
		return (Collections.unmodifiableList(_cachedIndividualValues));
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
		private static final long serialVersionUID = 7851044806424206976L;
		private SystemName.Builder _systemName;
		private List<IndividualValue.Builder> _individualValues;
		private SecurityAttributes.Builder _securityAttributes;		
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Individual individual) {
			if (individual.getSystemName() != null)
				setSystemName(new SystemName.Builder(individual.getSystemName()));
			for (IndividualValue value : individual.getIndividualValues())
				getIndividualValues().add(new IndividualValue.Builder(value));
			setSecurityAttributes(new SecurityAttributes.Builder(individual.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Individual commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<IndividualValue> values = new ArrayList<IndividualValue>();
			for (IBuilder builder : getIndividualValues()) {
				IndividualValue component = (IndividualValue) builder.commit();
				if (component != null)
					values.add(component);
			}
			return (new Individual(getSystemName().commit(), values, getSecurityAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getIndividualValues())
				hasValueInList = hasValueInList || !builder.isEmpty();
			return (!hasValueInList && getSystemName().isEmpty() && getSecurityAttributes().isEmpty());
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
		 * Builder accessor for the values
		 */
		public List<IndividualValue.Builder> getIndividualValues() {
			if (_individualValues == null)
				_individualValues = new LazyList(IndividualValue.Builder.class);
			return _individualValues;
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