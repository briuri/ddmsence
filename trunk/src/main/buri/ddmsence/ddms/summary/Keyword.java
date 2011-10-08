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
package buri.ddmsence.ddms.summary;

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:keyword.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The keyword value must not be empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:value</u>: The keyword itself (required)<br />
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are optional. (starting in DDMS 
 * 4.0)<br />
 * <u>{@link ExtensibleAttributes}</u>: (optional, starting in DDMS 3.0).
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Keyword extends AbstractBaseComponent {

	private SecurityAttributes _securityAttributes = null;
	private ExtensibleAttributes _extensibleAttributes = null;
	
	private static final String VALUE_NAME = "value";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(Element element) throws InvalidDDMSException {
		try {
			_securityAttributes = new SecurityAttributes(element);
			_extensibleAttributes = new ExtensibleAttributes(element);
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
		
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param value the value attribute (required)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(String value, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		this(value, securityAttributes, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * <p>This constructor will throw an InvalidDDMSException if the extensible attributes uses the reserved
	 * attribute "ddms:value".</p>
	 *  
	 * @param value the value attribute (required)
	 * @param securityAttributes any security attributes (optional)
	 * @param extensibleAttributes extensible attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(String value, SecurityAttributes securityAttributes, ExtensibleAttributes extensibleAttributes)
		throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Keyword.getName(DDMSVersion.getCurrentVersion()), null);
			Util.addDDMSAttribute(element, VALUE_NAME, value);
			_securityAttributes = SecurityAttributes.getNonNullInstance(securityAttributes);
			_securityAttributes.addTo(element);
			_extensibleAttributes = ExtensibleAttributes.getNonNullInstance(extensibleAttributes);
			_extensibleAttributes.addTo(element);
			setXOMElement(element, true);
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
	 * <li>The keyword value exists and is not empty.</li>
	 * <li>The SecurityAttributes do not exist until DDMS 4.0 or later.</li>
	 * <li>No extensible attributes can exist until DDMS 3.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Keyword.getName(getDDMSVersion()));
		Util.requireDDMSValue("value attribute", getValue());
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes cannot be applied to this component until DDMS 4.0 or later.");
		}
		if (!getDDMSVersion().isAtLeast("3.0") && !getExtensibleAttributes().isEmpty())
			throw new InvalidDDMSException("xs:anyAttribute cannot be applied to ddms:keyword until DDMS 3.0 or later.");

		super.validate();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		prefix = Util.getNonNullString(prefix) + getName() + Util.getNonNullString(suffix);
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix, getValue(), false));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix + "."));
		text.append(getExtensibleAttributes().getOutput(isHTML, prefix + "."));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Keyword))
			return (false);
		Keyword test = (Keyword) obj;
		return (getValue().equals(test.getValue())
			&& getExtensibleAttributes().equals(test.getExtensibleAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getValue().hashCode();
		result = 7 * result + getExtensibleAttributes().hashCode();
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
		return ("keyword");
	}
	
	/**
	 * Accessor for the value attribute.
	 */
	public String getValue() {
		return (getAttributeValue(VALUE_NAME));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_securityAttributes);
	}
	
	/**
	 * Accessor for the extensible attributes. Will always be non-null, even if not set.
	 */
	public ExtensibleAttributes getExtensibleAttributes() {
		return (_extensibleAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 5165722850252388155L;
		private String _value;
		private SecurityAttributes.Builder _securityAttributes;
		private ExtensibleAttributes.Builder _extensibleAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Keyword keyword) {
			setValue(keyword.getValue());
			setSecurityAttributes(new SecurityAttributes.Builder(keyword.getSecurityAttributes()));
			setExtensibleAttributes(new ExtensibleAttributes.Builder(keyword.getExtensibleAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Keyword commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Keyword(getValue(), getSecurityAttributes().commit(),
				getExtensibleAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getValue())
				&& getSecurityAttributes().isEmpty()
				&& getExtensibleAttributes().isEmpty());
		}

		/**
		 * Builder accessor for the value attribute
		 */
		public String getValue() {
			return _value;
		}

		/**
		 * Builder accessor for the value attribute
		 */
		public void setValue(String value) {
			_value = value;
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
		 * Builder accessor for the Extensible Attributes
		 */
		public ExtensibleAttributes.Builder getExtensibleAttributes() {
			if (_extensibleAttributes == null)
				_extensibleAttributes = new ExtensibleAttributes.Builder();
			return _extensibleAttributes;
		}
		
		/**
		 * Builder accessor for the Extensible Attributes
		 */
		public void setExtensibleAttributes(ExtensibleAttributes.Builder extensibleAttributes) {
			_extensibleAttributes = extensibleAttributes;
		}
	}
} 