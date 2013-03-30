/* Copyright 2010 - 2013 by Brian Uri!
   
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
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractQualifierValue;
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
 * {@table.header Strictness}
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A non-empty qualifier value is required. This rule is codified in the schema, starting in DDMS 5.0.</li>
 * <li>A non-empty value attribute is required. This rule is codified in the schema, starting in DDMS 5.0.</li>
 * <li>Although qualifier and value are optional in DDMS 2.0, this is considered invalid data. DDMSence requires
 * a non-empty qualifier and value.</li>
 * </ul>
 * {@table.footer}
 * 
 * <p>In DDMS 5.0, the "qualifier" and "value" attributes are renamed to "codespace" and "code".</p>
 * 
 * {@table.header Attributes}
 * <u>ddms:qualifier</u>: a domain vocabulary (required)<br />
 * <u>ddms:value</u>: a permissible value (required)<br />
 * {@table.footer}
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
		super(element, !DDMSVersion.getVersionForNamespace(element.getNamespaceURI()).isAtLeast("5.0"));
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param qualifier the value of the qualifier attribute
	 * @param value the value of the value attribute
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public CountryCode(String qualifier, String value) throws InvalidDDMSException {
		super(CountryCode.getName(DDMSVersion.getCurrentVersion()), qualifier, value, true,
			!DDMSVersion.getCurrentVersion().isAtLeast("5.0"));
	}

	/**
	 * Validates the component.
	 * 
	 * {@table.header Rules}
	 * <li>The qualified name of the element is correct.</li>
	 * <li>The qualifier exists and is not empty.</li>
	 * <li>The value exists and is not empty.</li>
	 * <li>Does not validate that the value is valid against the qualifier's vocabulary.</li>
	 * {@table.footer}
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), CountryCode.getName(getDDMSVersion()));
		Util.requireDDMSValue(getQualifierName() + " attribute", getQualifier());
		Util.requireDDMSValue(getValueName() + " attribute", getValue());
		super.validate();
	}

	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix + ".");
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix + getQualifierName(), getQualifier()));
		text.append(buildOutput(isHTML, localPrefix + getValueName(), getValue()));
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