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
 * An immutable implementation of ddms:subDivisionCode.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A non-empty qualifier value is required.</li>
 * <li>A non-empty value attribute is required.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:qualifier</u>: a domain vocabulary (required)<br />
 * <u>ddms:value</u>: a permissable value (required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#geospatialCoverage_GeospatialExtent_geographicIdentifier_subDivisionCode<br />
 * <u>Description</u>: A standards-based abbreviation of an administrative subdivision of a country.<br />
 * <u>Obligation</u>: Optional in geographicIdentifiers<br />
 * <u>Schema Modification Date</u>: 2011-09-01<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class SubDivisionCode extends AbstractQualifierValue {
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SubDivisionCode(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param qualifier	the value of the qualifier attribute
	 * @param value	the value of the value attribute 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SubDivisionCode(String qualifier, String value) throws InvalidDDMSException {
		super(SubDivisionCode.getName(DDMSVersion.getCurrentVersion()), qualifier, value, true);
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
		Util.requireDDMSQName(getXOMElement(), SubDivisionCode.getName(getDDMSVersion()));
		Util.requireDDMSValue("qualifier attribute", getQualifier());
		Util.requireDDMSValue("value attribute", getValue());
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
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta(prefix + QUALIFIER_NAME, getQualifier(), true));
		html.append(buildHTMLMeta(prefix + VALUE_NAME, getValue(), true));
		return (html.toString());
	}
	
	/**
	 * Outputs to Text with a prefix at the beginning of each line.
	 * 
	 * @param prefix the prefix to add
	 * @return the Text output
	 */
	public String toText(String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine(prefix + QUALIFIER_NAME, getQualifier(), true));
		text.append(buildTextLine(prefix + VALUE_NAME, getValue(), true));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof SubDivisionCode))
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
		return ("subDivisionCode");
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
		public Builder(SubDivisionCode code) {
			super(code);
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public SubDivisionCode commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new SubDivisionCode(getQualifier(), getValue()));
		}
	}
} 