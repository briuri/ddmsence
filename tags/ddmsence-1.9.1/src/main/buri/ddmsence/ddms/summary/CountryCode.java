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

import java.util.HashSet;
import java.util.Set;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.AbstractQualifierValue;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:countryCode.
 * 
 * <p>
 * The Text/HTML output of this class depends on the enclosing element of this country code. For example,
 * if the country code is used in a GeographicIdentifier.NAME, the HTML meta tags will prefix each field
 * with "geospatial.identifier.". See the DDMS category descriptions for other examples:
 * http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#CountryCodeQualifier
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A non-empty qualifier value is required.</li>
 * <li>A non-empty value attribute is required.</li>
 * <li>(v2.0) Although qualifier and value are optional in v2.0, this is considered invalid data. DDMSence requires
 * a non-empty qualifier and value.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:qualifier</u>: a domain vocabulary (required)<br />
 * <u>ddms:value</u>: a permissable value (required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#CountryCodeQualifier<br />
 * <u>Description</u>: A standards-based abbreviation of a country name.<br />
 * <u>Obligation</u>: Mandatory in postalAddresses, Optional in geographicIdentifiers<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class CountryCode extends AbstractQualifierValue {

	private String _parentType;
	
	private static Set<String> PARENT_TYPES = new HashSet<String>();
	static {
		PARENT_TYPES.add(GeographicIdentifier.NAME);
		PARENT_TYPES.add(PostalAddress.NAME);
	}
	
	/** The element name of this component */
	public static final String NAME = "countryCode";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param parentType either geographicIdentifier or postalAddress
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public CountryCode(String parentType, Element element) throws InvalidDDMSException {
		try {
			_parentType = parentType;
			Util.requireDDMSValue("countryCode element", element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param parentType either geographicIdentifier or postalAddress
	 * @param qualifier	the value of the qualifier attribute
	 * @param value	the value of the value attribute 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public CountryCode(String parentType, String qualifier, String value) throws InvalidDDMSException {
		super(CountryCode.NAME, qualifier, value, false);
		try {
			_parentType = parentType;
			setXOMElement(getXOMElement(), true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Validates a parent type against the allowed types.
	 * 
	 * @param parentType the type to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateParentType(String parentType) throws InvalidDDMSException {
		Util.requireDDMSValue("parent type", parentType);
		if (!PARENT_TYPES.contains(parentType))
			throw new InvalidDDMSException("The parent type must be one of " + PARENT_TYPES);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>The parent type is either geographicIdentifier or postalAddress.</li>
	 * <li>The qualifier exists and is not empty.</li>
	 * <li>The value exists and is not empty.</li>
	 * <li>Does not validate that the value is valid against the qualifier's vocabulary.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		validateParentType(getParentType());
		Util.requireDDMSValue("qualifier attribute", getQualifier());
		Util.requireDDMSValue("value attribute", getValue());
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		String parentHtml = (getParentType().equals(GeographicIdentifier.NAME)
			? "geospatial.identifier" : "geospatial.address");
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta(parentHtml + ".country.qualifier", getQualifier(), true));
		html.append(buildHTMLMeta(parentHtml + ".country", getValue(), true));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		String parentText = (getParentType().equals(GeographicIdentifier.NAME) 
			? "Geographic Identifier" : "Postal Address");
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine(parentText + " Country Qualifier", getQualifier(), true));
		text.append(buildTextLine(parentText + " Country", getValue(), true));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof CountryCode))
			return (false);
		CountryCode test = (CountryCode) obj;
		return (getParentType().equals(test.getParentType()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getParentType().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the name of the parent element
	 */
	public String getParentType() {
		return (Util.getNonNullString(_parentType));
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractQualifierValue.Builder {
		private static final long serialVersionUID = 2136329013144660166L;
		private String _parentType;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(CountryCode code) {
			super(code);
			setParentType(code.getParentType());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public CountryCode commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new CountryCode(getParentType(), getQualifier(), getValue()));
		}
		
		/**
		 * Builder accessor for the parentType
		 */
		public String getParentType() {
			return _parentType;
		}

		/**
		 * Builder accessor for the parentType
		 */
		public void setParentType(String parentType) {
			_parentType = parentType;
		}
	}
} 