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
package buri.ddmsence.ddms.summary;

import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
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
 * In DDMS 3.0, this component can also be decorated with optional {@link ExtensibleAttributes}.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Subject<br />
 * <u>Description</u>: Subject keyword(s) that characterize the subject matter of a resource.<br />
 * <u>Obligation</u>: At least 1 category or keyword is required in a Subject.<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Keyword extends AbstractBaseComponent {

	private ExtensibleAttributes _cachedExtensibleAttributes = null;
	
	/** The element name of this component */
	public static final String NAME = "keyword";
	
	private static final String VALUE_NAME = "value";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(Element element) throws InvalidDDMSException {
		_cachedExtensibleAttributes = new ExtensibleAttributes(element);
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param value the value attribute (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(String value) throws InvalidDDMSException {
		this(value, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * <p>This constructor will throw an InvalidDDMSException if the extensible attributes uses the reserved
	 * attribute "ddms:value".</p>
	 *  
	 * @param value the value attribute (required)
	 * @param extensions extensible attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(String value, ExtensibleAttributes extensions) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Keyword.NAME, null);
			Util.addDDMSAttribute(element, VALUE_NAME, value);
			_cachedExtensibleAttributes = (extensions == null ? new ExtensibleAttributes((List<Attribute>) null)
				: extensions);
			_cachedExtensibleAttributes.addTo(element);
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
	 * <li>The keyword value exists and is not empty.</li>
	 * <li>(v2.0) No extensible attributes can exist.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		Util.requireDDMSValue("value attribute", getValue());
		if ("2.0".equals(getDDMSVersion()) && !getExtensibleAttributes().isEmpty())
			throw new InvalidDDMSException("xs:anyAttribute can only be applied to ddms:keyword in DDMS 3.0.");
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("subject.keyword", getValue(), false));
		html.append(getExtensibleAttributes().toHTML("subject.keyword"));
		return (html.toString());

	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Keyword", getValue(), false));
		text.append(getExtensibleAttributes().toText("Keyword"));
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
	 * Accessor for the value attribute.
	 */
	public String getValue() {
		return (getAttributeValue(VALUE_NAME));
	}
	
	/**
	 * Accessor for the extensible attributes. Will always be non-null, even if not set.
	 */
	public ExtensibleAttributes getExtensibleAttributes() {
		return (_cachedExtensibleAttributes);
	}
} 