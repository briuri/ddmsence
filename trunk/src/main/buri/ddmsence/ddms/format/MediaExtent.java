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
package buri.ddmsence.ddms.format;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.AbstractQualifierValue;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:extent.
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
 * <li>An extent can be set without a qualifier or value.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:qualifier</u>: a URI-based vocabulary (required if value is set)<br />
 * <u>ddms:value</u>: a related data size, compression rate, or pixel size (optional)<br />
 * </td></tr></table>
 *  
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Extent<br />
 * <u>Description</u>: An extent provides further details about the format of a resource, such as sizes or 
 * dimensions.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2003-05-16 (parent ddms:Format element)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class MediaExtent extends AbstractQualifierValue {

	/** The element name of this component */
	public static final String NAME = "extent";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public MediaExtent(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param qualifier the value of the qualifier attribute
	 * @param value the value of the value attribute
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public MediaExtent(String qualifier, String value) throws InvalidDDMSException {
		super(MediaExtent.NAME, qualifier, value, true);
	}

	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>If set, the qualifier is a valid URI.</li>
	 * <li>If the value is set, a non-empty qualifier is required.</li>
	 * <li>Does NOT validate that the value is valid against the qualifier's vocabulary.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		if (!Util.isEmpty(getValue()))
			Util.requireDDMSValue("qualifier attribute", getQualifier());
		if (!Util.isEmpty(getQualifier())) {
			Util.requireDDMSValidURI(getQualifier());
		}
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A qualifier has been set without an accompanying value attribute.</li>
	 * <li>A completely empty ddms:extent element was found.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (!Util.isEmpty(getQualifier()) && Util.isEmpty(getValue()))
			addWarning("A qualifier has been set without an accompanying value attribute.");
		if (Util.isEmpty(getQualifier()) && Util.isEmpty(getValue()))
			addWarning("A completely empty ddms:extent element was found.");
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("format.extentqualifier", getQualifier(), false));
		html.append(buildHTMLMeta("format.extent", getValue(), false));
		return (html.toString());
	}

	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Extent qualifier", getQualifier(), false));
		text.append(buildTextLine("Extent", getValue(), false));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		return (super.equals(obj) && (obj instanceof MediaExtent));
	}
} 