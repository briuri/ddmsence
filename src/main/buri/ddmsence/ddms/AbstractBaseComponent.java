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
package buri.ddmsence.ddms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Top-level base class for all DDMS elements and attributes modeled as Java objects.
 * 
 * <p>Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set
 * before the component is used. It is assumed that after the constructor on a component has been called, the component
 * will be well-formed and valid.</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public abstract class AbstractBaseComponent implements IDDMSComponent {

	private List<ValidationMessage> _warnings;
	private Element _element;

	/** The default DDMS prefix used to create components from scratch */
	public static final String DDMS_PREFIX = PropertyReader.getProperty("ddms.prefix");
	
	/** The GML prefix used to create components from scratch */
	public static final String GML_PREFIX = PropertyReader.getProperty("gml.prefix");
	
	/** The ICISM prefix used to create components from scratch */
	public static final String ICISM_PREFIX = PropertyReader.getProperty("icism.prefix");
	
	/**
	 * This implicit superconstructor does nothing.
	 */
	protected AbstractBaseComponent() throws InvalidDDMSException {}
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element representing this component
	 */
	protected AbstractBaseComponent(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Will return an empty string if the element is not set, but this cannot occur after instantiation.
	 * 
	 * @see IDDMSComponent#getPrefix()
	 */
	public String getPrefix() {
		return (getXOMElement() == null ? "" : getXOMElement().getNamespacePrefix());
	}
	
	/**
	 * Will return an empty string if the element is not set, but this cannot occur after instantiation.
	 * 
	 * @see IDDMSComponent#getName()
	 */
	public String getName() {
		return (getXOMElement() == null ? "" : getXOMElement().getLocalName());
	}
	
	/**
	 * Will return an empty string if the element is not set, but this cannot occur after instantiation.
	 * 
	 * @see IDDMSComponent#getQualifiedName()
	 */
	public String getQualifiedName() {
		return (getXOMElement() == null ? "" : getXOMElement().getQualifiedName());
	}
	
	/**
	 * @see IDDMSComponent#getDDMSVersion()
	 */
	public String getDDMSVersion() {
		return (DDMSVersion.getVersionForNamespace(getXOMElement().getNamespaceURI()).getVersion());
	}
	
	/**
	 * The base implementation of a DDMS component assumes that there are no security attributes. Components
	 * with attributes should override this.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (null);
	}
	
	/**
	 * @see IDDMSComponent#getValidationWarnings()
	 */
	public List<ValidationMessage> getValidationWarnings() {
		return (Collections.unmodifiableList(getWarnings()));
	}
	
	/**
	 * Base case for validation. This method can be overridden for more in-depth validation. It is always assumed
	 * that the subcomponents of a component are already valid.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A name exists and is not empty.</li>
	 * </td></tr></table>
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSValue("name", getName());
	}
	
	/**
	 * @see IDDMSComponent#toHTML()
	 */
	public abstract String toHTML();
	
	/**
	 * @see IDDMSComponent#toText()
	 */
	public abstract String toText();
	
	/**
	 * Will return an empty string if the name is not set, but this cannot occur after
	 * instantiation.
	 * 
	 * @see IDDMSComponent#toXML()
	 */
	public String toXML() {
		return (getXOMElement() == null ? "" : getXOMElement().toXML());
	}
	
	/**
	 * Convenience method to build a meta tag for HTML output
	 * 
	 * @param name the name value of the meta tag (will be escaped)
	 * @param content the content value of the meta tag (will be escaped)
	 * @param alwaysPrint if true, will print the tag even if the content is empty or null.
	 * @return an HTML meta tag as a String
	 */
	public static String buildHTMLMeta(String name, String content, boolean alwaysPrint) {
		if (Util.isEmpty(content) && !alwaysPrint)
			return ("");
		StringBuffer tag = new StringBuffer();
		tag.append("<meta name=\"").append(Util.xmlEscape(name)).append("\" content=\"")
			.append(Util.xmlEscape(content)).append("\" />\n");
		return (tag.toString());
	}

	/**
	 * Convenience method to build a DDMS text line, used to output a component as plain-text. Text is not escaped.
	 * 
	 * @param name the name value of the text line
	 * @param content the content value of the text line
	 * @param alwaysPrint if true, will print the tag even if the content is empty or null.
	 * @return text output of a DDMS element
	 */
	public static String buildTextLine(String name, String content, boolean alwaysPrint) {
		if (Util.isEmpty(content) && !alwaysPrint)
			return ("");
		StringBuffer tag = new StringBuffer();
		tag.append(name).append(": ").append(content).append("\n");
		return (tag.toString());		
	}
	
	/**
	 * Convenience method to look up an attribute which is in the same namespace as the enclosing element
	 * 
	 * @param name the local name of the attribute
	 * @return attribute value, or an empty string if it does not exist
	 */
	protected String getAttributeValue(String name) {
		return (getAttributeValue(name, getXOMElement().getNamespaceURI()));
	}
	
	/**
	 * Convenience method to look up an attribute
	 * 
	 * @param name the local name of the attribute
	 * @param namespaceURI the namespace of the attribute
	 * @return attribute value, or an empty string if it does not exist
	 */
	protected String getAttributeValue(String name, String namespaceURI) {
		Util.requireValue("name", name);
		String attrValue = getXOMElement().getAttributeValue(name, Util.getNonNullString(namespaceURI));
		return (Util.getNonNullString(attrValue));
	}
	
	/**
	 * Convenience method to get the first child element with a given name in the same namespace as the parent element
	 * 
	 * @param name the local name to search for
	 * @return the element, or null if it does not exist
	 */
	protected Element getChild(String name) {
		Util.requireValue("name", name);
		return (getXOMElement().getFirstChildElement(name, getXOMElement().getNamespaceURI()));
	}

	/**
	 * Convenience method to convert one of the lat/lon fields into a Double. Returns null if the field does not exist
	 * or cannot be converted into a Double.
	 * 
	 * @param element the parent element
	 * @param name the local name of the child
	 * @return a Double, or null if it cannot be created
	 */
	protected static Double getChildTextAsDouble(Element element, String name) {
		Util.requireValue("element", element);
		Util.requireValue("name", name);
		Element childElement = element.getFirstChildElement(name, element.getNamespaceURI());
		if (childElement == null)
			return (null);
		try {
			return (Double.valueOf(childElement.getValue()));
		}
		catch (NumberFormatException e) {
			return (null);
		}
	}
	
	/**
	 * Test for logical equality.
	 * 
	 * <p> The base case tests against the name value and namespaceURI. Extending classes may require additional rules
	 * for equality. </p>
	 * 
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return (true);
		if (!(obj instanceof AbstractBaseComponent))
			return (false);
		AbstractBaseComponent test = (AbstractBaseComponent) obj;
		return (getName().equals(test.getName())
			&& getXOMElement().getNamespaceURI().equals(test.getXOMElement().getNamespaceURI()));
	}
	
	/**
	 * Returns a hashcode for the component.
	 * 
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = getName().hashCode();
		result = 7 * result + getXOMElement().getNamespaceURI().hashCode();
		return (result);
	}
	
	/**
	 * Returns the XML representation of the component
	 * 
	 * @see Object#toString()
	 */
	public String toString() {
		return (toXML());
	}
	
	/**
	 * Can be overridden to change the locator string used in warnings and errors.
	 * 
	 * <p>For components such as Format, there are wrapper elements that are not implemented as Java objects. These
	 * elements should be included in the XPath string used to identify the source of the error.</p>
	 * 
	 * <p>For example, if a ddms:extent element has a warning and the ddms:format element reports it, the locator
	 * information should be "/ddms:format/ddms:Media/ddms:extent" and not the default of "/ddms:format/ddms:extent"</p>
	 * 
	 * @return an empty string, unless overridden.
	 */
	protected String getLocatorSuffix() {
		return ("");
	}
	
	/**
	 * Convenience method to create a warning and add it to the list of validation warnings.
	 * 
	 * @param text the description text
	 */
	protected void addWarning(String text) {
		getWarnings().add(ValidationMessage.newWarning(text, getQualifiedName() + getLocatorSuffix()));
	}
	
	/**
	 * Convenience method to add multiple warnings to the list of validation warnings.
	 * 
	 * <p>Child locator information will be prefixed with the parent (this) locator information. This does not overwrite
	 * the original warning -- it creates a new copy.</p>
	 * 
	 * @param warnings the list of validation messages to add
	 * @param forAttributes if true, the locator suffix is not used, because the attributes will be for the topmost
	 * element (for example, warnings for gml:Polygon's security attributes should not end up with a locator of
	 * /gml:Polygon/gml:exterior/gml:LinearRing).
	 */
	protected void addWarnings(List<ValidationMessage> warnings, boolean forAttributes) {
		for (ValidationMessage warning : warnings) {
			String newLocator = getQualifiedName() + (forAttributes ? "" : getLocatorSuffix()) + warning.getLocator();
			getWarnings().add(ValidationMessage.newWarning(warning.getText(), newLocator));
		}
	}

	/**
	 * Accessor for the list of validation warnings.
	 * 
	 * <p>This is the private copy that should be manipulated during validation. Lazy initialization.</p>
	 * 
	 * @return an editable list of warnings
	 */
	private List<ValidationMessage> getWarnings() {
		if (_warnings == null)
			_warnings = new ArrayList<ValidationMessage>();
		return (_warnings);
	}
	
	/**
	 * Accessor for the XOM element representing this component
	 */
	protected Element getXOMElement() {
		return _element;
	}
	
	/**
	 * Accessor for a copy of the underlying XOM element
	 */
	public Element getXOMElementCopy() {
		return (new Element(_element));
	}
	
	/**
	 * Accessor for the XOM element representing this component. When the element is set, the component is validated
	 * again with <code>validate</code>.
	 * 
	 * @param element the XOM element to use
	 * @param validateNow whether to validate the component immediately after setting
	 */
	protected void setXOMElement(Element element, boolean validateNow) throws InvalidDDMSException {
		Util.requireDDMSValue("XOM Element", element);
		_element = element;
		if (validateNow)
			validate();
	}
}
