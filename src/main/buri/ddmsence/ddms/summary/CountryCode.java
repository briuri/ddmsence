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
 * if the country code is used in a geographicIdentifier, the HTML meta tags will prefix each field
 * with "geospatialCoverage.GeospatialExtent.geographicIdentifier.". 
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A non-empty qualifier value is required.</li>
 * <li>A non-empty value attribute is required.</li>
 * <li>Although qualifier and value are optional in DDMS 2.0, this is considered invalid data. DDMSence requires
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
 * <u>Description</u>: A standards-based abbreviation of a country name.<br />
 * <u>Obligation</u>: Mandatory in postalAddresses, Optional in geographicIdentifiers<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class CountryCode extends AbstractQualifierValue {
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public CountryCode(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param qualifier	the value of the qualifier attribute
	 * @param value	the value of the value attribute 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public CountryCode(String qualifier, String value) throws InvalidDDMSException {
		super(CountryCode.getName(DDMSVersion.getCurrentVersion()), qualifier, value, true);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
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
		Util.requireDDMSQName(getXOMElement(), CountryCode.getName(getDDMSVersion()));
		Util.requireDDMSValue("qualifier attribute", getQualifier());
		Util.requireDDMSValue("value attribute", getValue());
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + QUALIFIER_NAME, getQualifier(), true));
		text.append(buildOutput(isHTML, prefix + VALUE_NAME, getValue(), true));
		return (text.toString());
	}
		
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof CountryCode))
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
		return ("countryCode");
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
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public CountryCode commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new CountryCode(getQualifier(), getValue()));
		}
	}
} 