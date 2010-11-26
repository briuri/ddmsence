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
 * An immutable implementation of ddms:category.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The label value must be non-empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:qualifier</u>: A URI-based qualifier (optional)<br />
 * <u>ddms:code</u>: The machine readable description of a concept represented within the scope of the category
 * qualifier (optional)<br />
 * <u>ddms:label</u>: The human readable representation of the concept that corresponds to the category qualifier and
 * the category code, if they exist. (required)<br />
 * In DDMS 3.0, this component can also be decorated with optional {@link ExtensibleAttributes}.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Subject<br />
 * <u>Description</u>: A category describes the subject area of a resource using a controlled vocabulary.<br />
 * <u>Obligation</u>: At least 1 category or keyword is required in a Subject<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Category extends AbstractBaseComponent {

	private ExtensibleAttributes _cachedExtensibleAttributes = null;
	
	/** The element name of this component */
	public static final String NAME = "category";
	
	private static final String QUALIFIER_NAME = "qualifier";
	private static final String CODE_NAME = "code";
	private static final String LABEL_NAME = "label";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Category(Element element) throws InvalidDDMSException {
		_cachedExtensibleAttributes = new ExtensibleAttributes(element);
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param qualifier the qualifier (optional)
	 * @param code the code (optional)
	 * @param label the label (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Category(String qualifier, String code, String label) throws InvalidDDMSException {
		this(qualifier, code, label, null);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * 
	 * <p>This constructor will throw an InvalidDDMSException if the extensible attributes uses the reserved
	 * attributes, "ddms:qualifier", "ddms:code", or "ddms:label".</p>
	 * 
	 * @param qualifier the qualifier (optional)
	 * @param code the code (optional)
	 * @param label the label (required)
	 * @param extensions extensible attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Category(String qualifier, String code, String label, ExtensibleAttributes extensions) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Category.NAME, null);
			Util.addDDMSAttribute(element, QUALIFIER_NAME, qualifier);
			Util.addDDMSAttribute(element, CODE_NAME, code);
			Util.addDDMSAttribute(element, LABEL_NAME, label);
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
	 * <li>A label exists and is not empty.</li>
	 * <li>If a qualifier exists, it is a valid URI.</li>
	 * <li>(v2.0) No extensible attributes can exist.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		Util.requireDDMSValue("label attribute", getLabel());
		if (!Util.isEmpty(getQualifier())) {
			Util.requireDDMSValidURI(getQualifier());
		}			
		if ("2.0".equals(getDDMSVersion()) && !getExtensibleAttributes().isEmpty())
			throw new InvalidDDMSException("xs:anyAttribute can only be applied to ddms:category in DDMS 3.0.");
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("subject.category.qualifier", getQualifier(), false));
		html.append(buildHTMLMeta("subject.category.code", getCode(), false));
		html.append(buildHTMLMeta("subject.category.label", getLabel(), true));
		html.append(getExtensibleAttributes().toHTML("subject.category"));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Category Qualifier", getQualifier(), false));
		text.append(buildTextLine("Category Code", getCode(), false));
		text.append(buildTextLine("Category Label", getLabel(), true));
		text.append(getExtensibleAttributes().toText("Category"));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Category))
			return (false);
		Category test = (Category) obj;
		return (getQualifier().equals(test.getQualifier()) 
			&& getCode().equals(test.getCode()) 
			&& getLabel().equals(test.getLabel())
			&& getExtensibleAttributes().equals(test.getExtensibleAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getQualifier().hashCode();
		result = 7 * result + getCode().hashCode();
		result = 7 * result + getLabel().hashCode();
		result = 7 * result + getExtensibleAttributes().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the qualifier attribute.
	 */
	public String getQualifier() {
		return (getAttributeValue(QUALIFIER_NAME));
	}
	
	/**
	 * Accessor for the code attribute.
	 */
	public String getCode() {
		return (getAttributeValue(CODE_NAME));
	}
	
	/**
	 * Accessor for the label attribute.
	 */
	public String getLabel() {
		return (getAttributeValue(LABEL_NAME));
	}
	
	/**
	 * Accessor for the extensible attributes. Will always be non-null, even if not set.
	 */
	public ExtensibleAttributes getExtensibleAttributes() {
		return (_cachedExtensibleAttributes);
	}
} 