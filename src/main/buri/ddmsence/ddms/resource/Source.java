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
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.AbstractQualifierValue;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:source.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A source element can be used with none of the attributes set.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:qualifier</u>: specifies the source of the type vocabulary (optional)<br />
 * <u>ddms:value</u>: includes terms describing general categories, functions, genres, or aggregation levels
 * (optional)<br />
 * <u>ddms:schemaQualifier</u>: the schema type (optional)<br />
 * <u>ddms:schemaHref</u>: a resolvable reference to the schema (optional)<br />
 * This class is also decorated with ISM {@link SecurityAttributes}, starting in DDMS 3.0. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: References to assets or resources from which the tagged data asset is derived.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2004-07-01<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Source extends AbstractQualifierValue {

	private SecurityAttributes _cachedSecurityAttributes = null;
	
	private static final String SCHEMA_QUALIFIER_NAME = "schemaQualifier";
	private static final String SCHEMA_HREF_NAME = "schemaHref";

	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Source(Element element) throws InvalidDDMSException {
		try {
			_cachedSecurityAttributes = new SecurityAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param qualifier	the value of the qualifier attribute
	 * @param value	the value of the value attribute 
	 * @param schemaQualifier the value of the schemaQualifier attribute
	 * @param schemaHref the value of the schemaHref attribute
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Source(String qualifier, String value, String schemaQualifier, String schemaHref,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		super(Source.getName(DDMSVersion.getCurrentVersion()), qualifier, value, false);
		try {
			Element element = getXOMElement();
			Util.addDDMSAttribute(element, SCHEMA_QUALIFIER_NAME, schemaQualifier);
			Util.addDDMSAttribute(element, SCHEMA_HREF_NAME, schemaHref);
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
	 * <li>If a schemaHref is present, it is a valid URI.</li>
	 * <li>The SecurityAttributes do not exist until DDMS 3.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), Source.getName(getDDMSVersion()));
		if (!Util.isEmpty(getSchemaHref())) {
			Util.requireDDMSValidURI(getSchemaHref());
		}		
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("3.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes cannot be applied to this component until DDMS 3.0 or later.");
		}
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A completely empty ddms:source element was found.</li>
	 * <li>Include any validation warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getQualifier()) && Util.isEmpty(getValue()) && Util.isEmpty(getSchemaQualifier())
			&& Util.isEmpty(getSchemaHref())) {
			addWarning("A completely empty ddms:source element was found.");
		}
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + QUALIFIER_NAME, getQualifier(), false));
		text.append(buildOutput(isHTML, prefix + VALUE_NAME, getValue(), false));
		text.append(buildOutput(isHTML, prefix + SCHEMA_QUALIFIER_NAME, getSchemaQualifier(), false));
		text.append(buildOutput(isHTML, prefix + SCHEMA_HREF_NAME, getSchemaHref(), false));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
			
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Source))
			return (false);
		Source test = (Source) obj;
		return (getSchemaQualifier().equals(test.getSchemaQualifier()) 
			&& getSchemaHref().equals(test.getSchemaHref()) 
			&& getSecurityAttributes().equals(test.getSecurityAttributes()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getSchemaQualifier().hashCode();
		result = 7 * result + getSchemaHref().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
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
		return ("source");
	}
	
	/**
	 * Accessor for the value of the schema qualifier
	 */
	public String getSchemaQualifier() {
		return (getAttributeValue(SCHEMA_QUALIFIER_NAME));
	}
	
	/**
	 * Accessor for the value of the schema href
	 */
	public String getSchemaHref() {
		return (getAttributeValue(SCHEMA_HREF_NAME));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractQualifierValue.Builder {
		private static final long serialVersionUID = -514632949760329348L;
		private String _schemaQualifier;
		private String _schemaHref;
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
		public Builder(Source source) {
			super(source);
			setSchemaQualifier(source.getSchemaQualifier());
			setSchemaHref(source.getSchemaHref());
			setSecurityAttributes(new SecurityAttributes.Builder(source.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Source commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Source(getQualifier(), getValue(), getSchemaQualifier(), getSchemaHref(),
				getSecurityAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (super.isEmpty()
				&& Util.isEmpty(getSchemaQualifier())
				&& Util.isEmpty(getSchemaHref())
				&& getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the schema qualifier
		 */
		public String getSchemaQualifier() {
			return _schemaQualifier;
		}

		/**
		 * Builder accessor for the schema qualifier
		 */
		public void setSchemaQualifier(String schemaQualifier) {
			_schemaQualifier = schemaQualifier;
		}

		/**
		 * Builder accessor for the schema href
		 */
		public String getSchemaHref() {
			return _schemaHref;
		}

		/**
		 * Builder accessor for the schemah ref
		 */
		public void setSchemaHref(String schemaHref) {
			_schemaHref = schemaHref;
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