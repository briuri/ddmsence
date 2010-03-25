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
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Top-level base class for all DDMS elements and attributes modeled as Java objects.
 * 
 * <p>
 * Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set before
 * the component is used. It is assumed that after the constructor on a component has been called, the component will be
 * well-formed and valid.
 * </p>
 *  
 * @author Brian Uri!
 * @since 0.9.b
 */
public abstract class AbstractBaseComponent implements IDDMSComponent {

	private List<ValidationMessage> _warnings;
	private Element _element;
	
	/** The GML prefix */
	public static final String GML_PREFIX = PropertyReader.getProperty("gml.prefix");
	
	/** The GML namespace */
	public static final String GML_NAMESPACE = PropertyReader.getProperty("gml.xmlNamespace");
	
	/** The ICISM prefix */
	public static final String ICISM_PREFIX = PropertyReader.getProperty("icism.prefix");
	
	/** The ICISM namespace */
	public static final String ICISM_NAMESPACE = PropertyReader.getProperty("icism.xmlNamespace");
	
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
		setXOMElement(element, true);
	}
	
	/**
	 * Will return an empty string if the name is not set, but this cannot occur after instantiation.
	 * 
	 * @see IDDMSComponent#getName()
	 */
	public String getName() {
		return (getXOMElement() == null ? "" : getXOMElement().getLocalName());
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
	 * Convenience method to get the first child element with a given name in the 
	 * same namespace as the parent element
	 * 
	 * @param name the local name to search for
	 * @return the element, or null if it does not exist
	 */
	protected Element getChild(String name) {
		Util.requireValue("name", name);
		return (getXOMElement().getFirstChildElement(name, getXOMElement().getNamespaceURI()));
	}

	/**
	 * Convenience method to convert one of the lat/lon fields into a Double. Returns null
	 * if the field does not exist or cannot be converted into a Double.
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
	 * <p>
	 * The base case tests against the name value. Extending classes may require additional 
	 * rules for equality.
	 * </p>
	 * 
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return (true);
		if (!(obj instanceof AbstractBaseComponent))
			return (false);
		AbstractBaseComponent test = (AbstractBaseComponent) obj;
		return (getName().equals(test.getName()));
	}
	
	/**
	 * Returns a hashcode for the component.
	 * 
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = getName().hashCode();
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
	 * Convenience method to create a warning and add it to the list of validation warnings.
	 * 
	 * @param text the description text
	 */
	protected void addWarning(String text) {
		getWarnings().add(ValidationMessage.newWarning(text));
	}
	
	/**
	 * Convenience method to add multiple warnings to the list of validation warnings.
	 * 
	 * @param warnings the list of validation messages to add
	 */
	protected void addWarnings(List<ValidationMessage> warnings) {
		getWarnings().addAll(warnings);
	}
	
	/**
	 * Accessor for the list of validation warnings.
	 * 
	 * <p>
	 * This is the private copy that should be manipulated during validation.
	 * Lazy initialization.
	 * </p>
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
	 * Accessor for the XOM element representing this component. When the element is set, 
	 * the component is validated again with <code>validate</code>.
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
