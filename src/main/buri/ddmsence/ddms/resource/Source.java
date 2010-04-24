/* Copyright 2010 by Brian Uri!
   
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
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.SecurityAttributes;
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
 * This class is also decorated with ICISM {@link SecurityAttributes}, starting in DDMS v3.0. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Source<br />
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
	
	/** The element name of this component */
	public static final String NAME = "source";
	
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
		super(Source.NAME, qualifier, value, false);
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
	 * <li>The SecurityAttributes do not exist in DDMS v2.0.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		if (!Util.isEmpty(getSchemaHref())) {
			Util.requireDDMSValidURI(getSchemaHref());
		}		
		if (DDMSVersion.isVersion("2.0", getXOMElement()) && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes can only be applied to this component in DDMS v3.0.");
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
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("source.qualifier", getQualifier(), false));
		html.append(buildHTMLMeta("source.value", getValue(), false));
		html.append(buildHTMLMeta("source.schema.qualifier", getSchemaQualifier(), false));
		html.append(buildHTMLMeta("source.schema.href", getSchemaHref(), false));
		html.append(getSecurityAttributes().toHTML(Source.NAME));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Source Qualifier", getQualifier(), false));
		text.append(buildTextLine("Source Value", getValue(), false));
		text.append(buildTextLine("Source Schema Qualifier", getSchemaQualifier(), false));
		text.append(buildTextLine("Source Schema href", getSchemaHref(), false));
		text.append(getSecurityAttributes().toText("Source"));
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
} 