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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.AbstractQualifierValue;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:RelatedResource.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A non-empty qualifier value is required.</li>
 * <li>A non-empty value attribute is required.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:link</u>: a link for the resource (1-many required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:qualifier</u>: A URI specifying the formal identification system or encoding scheme by which the identifier
 * value is to be interpreted. (required)<br />
 * <u>ddms:value</u>: an unambiguous reference to the resource within a given context. An internal, external, and/or
 * universal identification number for a data asset or resource. (required)
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#RelatedResources<br />
 * <u>Description</u>: An identifier for the resource being related to the resource described by the containing DDMS record.<br />
 * <u>Obligation</u>: At least 1 RelatedResource is required in a RelatedResources element.<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class RelatedResource extends AbstractQualifierValue {

	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<Link> _cachedLinks;
	
	/** The element name of this component */
	public static final String NAME = "RelatedResource";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public RelatedResource(Element element) throws InvalidDDMSException {
		Util.requireDDMSValue("RelatedResource element", element);
		_cachedLinks = new ArrayList<Link>();
		Elements links = element.getChildElements(Link.NAME, element.getNamespaceURI());
		for (int i = 0; i < links.size(); i++) {
			_cachedLinks.add(new Link(links.get(i)));
		}
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * @param links the xlinks
	 * @param qualifier	the value of the qualifier attribute
	 * @param value	the value of the value attribute 
	 *  
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public RelatedResource(List<Link> links, String qualifier, String value) throws InvalidDDMSException {
		super(RelatedResource.NAME, qualifier, value, false);
		Element element = getXOMElement();
		if (links == null)
			links = Collections.emptyList();
		for (Link link : links) {
			element.appendChild(link.getXOMElementCopy());
		}
		_cachedLinks = links;
		setXOMElement(element, true);		
	}

	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A qualifier exists and is not empty.</li>
	 * <li>A value exists and is not empty.</li>
	 * <li>The qualifier is a valid URI.</li>
	 * <li>At least 1 link exists.</li>
	 * <li>Does NOT validate that the value is valid against the qualifier's vocabulary.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException  if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSValue("qualifier attribute", getQualifier());
		Util.requireDDMSValue("value attribute", getValue());
		Util.requireDDMSValidURI(getQualifier());
		if (getChild(Link.NAME) == null)
			throw new InvalidDDMSException("At least 1 link must exist.");
		
		for (Link link : getLinks())
			addWarnings(link.getValidationWarnings());
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("RelatedResource.qualifier", getQualifier(), true));
		html.append(buildHTMLMeta("RelatedResource.value", getValue(), true));
		for (Link link : getLinks()) {
			html.append(link.toHTML());
		}
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Related Resource qualifier", getQualifier(), true));
		text.append(buildTextLine("Related Resource value", getValue(), true));
		for (Link link : getLinks()) {
			text.append(link.toText());
		}
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof RelatedResource))
			return (false);
		RelatedResource test = (RelatedResource) obj;
		return (Util.listEquals(getLinks(), test.getLinks()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getLinks().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the links (1 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<Link> getLinks() {
		return (Collections.unmodifiableList(_cachedLinks));
	}
} 