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

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:link.
 * 
 * <p>This element is not a global component, but is being implemented because it has attributes.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The href value must not be empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>xlink:type</u>: (required, fixed as "locator")<br />
 * <u>xlink:href</u>: A URL to the target related resource. (required, must be a URI)<br />
 * <u>xlink:role</u>: The URI reference identifies some resource that describes the intended property. When no value is
 * supplied, no particular role value is to be inferred. (optional)<br />
 * <u>xlink:title</u>: Used to describe the meaning of a link or resource in a human-readable fashion, along the same
 * lines as the role or arcrole attribute.(optional)<br />
 * <u>xlink:label</u>: The label attribute provides a name for the locator link providing a way for an XLink arc-type
 * element to refer to it in creating a traversal arc.(optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#relatedResources_RelatedResource_link<br />
 * <u>Description</u>: An XLink locator element for the resource being related.<br />
 * <u>Obligation</u>: At least 1 link is required in a RelatedResource.<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Link extends AbstractBaseComponent {

	private static final String FIXED_TYPE = "locator";
	
	/** The element name of this component */
	public static final String NAME = "link";
	
	private static final String TYPE_NAME = "type";
	private static final String HREF_NAME = "href";
	private static final String ROLE_NAME = "role";
	private static final String TITLE_NAME = "title";
	private static final String LABEL_NAME = "label";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Link(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param href	the link href (required)
	 * @param role	the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Link(String href, String role, String title, String label) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Link.NAME, null);
			String xlinkPrefix = PropertyReader.getProperty("xlink.prefix");
			String xlinkNamespace = PropertyReader.getProperty("xlink.xmlNamespace");
			Util.addAttribute(element, xlinkPrefix, TYPE_NAME, xlinkNamespace, FIXED_TYPE);
			Util.addAttribute(element, xlinkPrefix, HREF_NAME, xlinkNamespace, href);
			Util.addAttribute(element, xlinkPrefix, ROLE_NAME, xlinkNamespace, role);
			Util.addAttribute(element, xlinkPrefix, TITLE_NAME, xlinkNamespace, title);
			Util.addAttribute(element, xlinkPrefix, LABEL_NAME, xlinkNamespace, label);
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
	 * <li>The type is set and has a value of "locator"</li>
	 * <li>The href is set and non-empty.</li>
	 * <li>The href is a valid URI.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
		Util.requireDDMSValue("type attribute", getType());
		Util.requireDDMSValue("href attribute", getHref());
		Util.requireDDMSValidURI(getHref());
		if (!getType().equals(FIXED_TYPE))
			throw new InvalidDDMSException("The type attribute must have a fixed value of \"" + FIXED_TYPE + "\".");
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("Link.type", getType(), true));
		html.append(buildHTMLMeta("Link.href", getHref(), true));
		html.append(buildHTMLMeta("Link.role", getRole(), false));
		html.append(buildHTMLMeta("Link.title", getTitle(), false));
		html.append(buildHTMLMeta("Link.label", getLabel(), false));
		return (html.toString());

	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Link type", getType(), true));
		text.append(buildTextLine("Link href", getHref(), true));
		text.append(buildTextLine("Link role", getRole(), false));
		text.append(buildTextLine("Link title", getTitle(), false));
		text.append(buildTextLine("Link label", getLabel(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Link))
			return (false);
		Link test = (Link) obj;
		return (getType().equals(test.getType()) 
			&& getHref().equals(test.getHref())
			&& getRole().equals(test.getRole()) 
			&& getTitle().equals(test.getTitle()) 
			&& getLabel().equals(test.getLabel()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getType().hashCode();
		result = 7 * result + getHref().hashCode();
		result = 7 * result + getRole().hashCode();
		result = 7 * result + getTitle().hashCode();
		result = 7 * result + getLabel().hashCode();
		return (result);
	}
	
	/**
	 * Builder accessor for the type attribute.
	 */
	public String getType() {
		return (getAttributeValue(TYPE_NAME, PropertyReader.getProperty("xlink.xmlNamespace")));
	}
	
	/**
	 * Builder accessor for the href attribute.
	 */
	public String getHref() {
		return (getAttributeValue(HREF_NAME, PropertyReader.getProperty("xlink.xmlNamespace")));
	}
	
	/**
	 * Builder accessor for the role attribute.
	 */
	public String getRole() {
		return (getAttributeValue(ROLE_NAME, PropertyReader.getProperty("xlink.xmlNamespace")));
	}
	
	/**
	 * Builder accessor for the title attribute.
	 */
	public String getTitle() {
		return (getAttributeValue(TITLE_NAME, PropertyReader.getProperty("xlink.xmlNamespace")));
	}
	
	/**
	 * Builder accessor for the label attribute.
	 */
	public String getLabel() {
		return (getAttributeValue(LABEL_NAME, PropertyReader.getProperty("xlink.xmlNamespace")));
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 4325950371570699184L;
		private String _href;
		private String _role;
		private String _title;
		private String _label;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Link link) {
			setHref(link.getHref());
			setRole(link.getRole());
			setTitle(link.getTitle());
			setLabel(link.getLabel());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Link commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Link(getHref(), getRole(), getTitle(), getLabel()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getHref())
				&& Util.isEmpty(getRole())
				&& Util.isEmpty(getTitle())
				&& Util.isEmpty(getLabel()));				
		}
		
		/**
		 * Builder accessor for the href
		 */
		public String getHref() {
			return _href;
		}

		/**
		 * Builder accessor for the href
		 */
		public void setHref(String href) {
			_href = href;
		}

		/**
		 * Builder accessor for the role
		 */
		public String getRole() {
			return _role;
		}

		/**
		 * Builder accessor for the role
		 */
		public void setRole(String role) {
			_role = role;
		}

		/**
		 * Builder accessor for the title
		 */
		public String getTitle() {
			return _title;
		}

		/**
		 * Builder accessor for the title
		 */
		public void setTitle(String title) {
			_title = title;
		}

		/**
		 * Builder accessor for the label
		 */
		public String getLabel() {
			return _label;
		}

		/**
		 * Builder accessor for the label
		 */
		public void setLabel(String label) {
			_label = label;
		}
	}
} 