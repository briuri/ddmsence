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

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractQualifierValue;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:type.
 * 
 * <p>Beginning in DDMS 4.0, a ddms:type element can contain child text. The intent of this text is to provide further
 * context when the ddms:type element references an IC activity.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A non-empty qualifier value is required when the value attribute is set.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A qualifier can be set with no value.</li>
 * <li>A type can be set without a qualifier or value.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:qualifier</u>: a URI-based qualifier (required if value is set)<br />
 * <u>ddms:value</u>: includes terms describing general categories, functions, genres, or aggregation levels 
 * (optional)<br />
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are optional. 
 * (starting in DDMS 4.0)
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: The nature, genre, or discipline of the content of the resource.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Type extends AbstractQualifierValue {

	private SecurityAttributes _securityAttributes;
		
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Type(Element element) throws InvalidDDMSException {
		try {
			_securityAttributes = new SecurityAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param description the child text describing an IC activity, if this component is used to reference an IC
	 * activity
	 * @param qualifier the value of the qualifier attribute
	 * @param value the value of the value attribute
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Type(String description, String qualifier, String value, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(Type.getName(DDMSVersion.getCurrentVersion()), qualifier, value, false);
		try {
			Element element = getXOMElement();
			if (!Util.isEmpty(description))
				element.appendChild(description);
			_securityAttributes = SecurityAttributes.getNonNullInstance(securityAttributes);
			_securityAttributes.addTo(element);
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
	 * <li>The description child text cannot exist until DDMS 4.0 or later.</li>
	 * <li>If a value is set, a qualifier must exist and be non-empty.</li>
	 * <li>Does NOT validate that the value is valid against the qualifier's vocabulary.</li>
	 * <li>The SecurityAttributes do not exist until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Type.getName(getDDMSVersion()));
		if (!Util.isEmpty(getValue()))
			Util.requireDDMSValue("qualifier attribute", getQualifier());
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0") && !Util.isEmpty(getDescription())) {
			throw new InvalidDDMSException(
				"This component cannot contain description child text until DDMS 4.0 or later.");
		}
		if (!getDDMSVersion().isAtLeast("4.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes cannot be applied to this component until DDMS 4.0 or later.");
		}

		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A qualifier has been set without an accompanying value attribute.</li>
	 * <li>Neither a qualifier nor a value was set on this type.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (!Util.isEmpty(getQualifier()) && Util.isEmpty(getValue()))
			addWarning("A qualifier has been set without an accompanying value attribute.");
		if (Util.isEmpty(getQualifier()) && Util.isEmpty(getValue()))
			addWarning("Neither a qualifier nor a value was set on this type.");
		super.validateWarnings();
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + "description", getDescription(), false));
		text.append(buildOutput(isHTML, prefix + QUALIFIER_NAME, getQualifier(), false));
		text.append(buildOutput(isHTML, prefix + VALUE_NAME, getValue(), false));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Type))
			return (false);
		Type test = (Type) obj;
		return (getDescription().equals(test.getDescription()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getDescription().hashCode();
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
		return ("type");
	}
	
	/**
	 * Accessor for the description child text, which provides additional context to the qualifier/value pairing of this
	 * component. The underlying XOM method which retrieves the child text returns an empty string if not found.
	 */
	public String getDescription() {
		return (getXOMElement().getValue());
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_securityAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractQualifierValue.Builder {
		private static final long serialVersionUID = 4388694130954618393L;
		private String _description;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Type type) {
			super(type);
			setDescription(type.getDescription());
			setSecurityAttributes(new SecurityAttributes.Builder(type.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Type commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Type(getDescription(), getQualifier(), getValue(), 
				getSecurityAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (super.isEmpty() && Util.isEmpty(getDescription()) && getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the description
		 */
		public String getDescription() {
			return _description;
		}

		/**
		 * Builder accessor for the description
		 */
		public void setDescription(String description) {
			_description = description;
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
	}
} 