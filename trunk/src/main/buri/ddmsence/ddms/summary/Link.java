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

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
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
 * supplied, no particular role value is to be inferred. (optional, required starting in DDMS 4.0)<br />
 * <u>xlink:title</u>: Used to describe the meaning of a link or resource in a human-readable fashion, along the same
 * lines as the role or arcrole attribute.(optional)<br />
 * <u>xlink:label</u>: The label attribute provides a name for the locator link providing a way for an XLink arc-type
 * element to refer to it in creating a traversal arc.(optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: An XLink locator element for the resource being related.<br />
 * <u>Obligation</u>: At least 1 link is required in a RelatedResource.<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Link extends AbstractBaseComponent {

	private String _xlinkNamespace;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	private static final String FIXED_TYPE = "locator";
	
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
		try {
			for (int i = 0; i < element.getAttributeCount(); i++) {
				Attribute attr = element.getAttribute(i);
				if (TYPE_NAME.equals(attr.getLocalName()))
					_xlinkNamespace = attr.getNamespaceURI();
			}
			if (Util.isEmpty(getXlinkNamespace()))
				throw new InvalidDDMSException("Could not find the xlink namespace URI on this ddms:link element.");
			_cachedSecurityAttributes = new SecurityAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
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
		this(href, role, title, label, null);
	}

	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param href	the link href (required)
	 * @param role	the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @param securityAttributes attributes, which are only allowed on links within a ddms:revisionRecall
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Link(String href, String role, String title, String label, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Link.getName(DDMSVersion.getCurrentVersion()), null);
			String xlinkPrefix = PropertyReader.getProperty("xlink.prefix");
			_xlinkNamespace = DDMSVersion.getCurrentVersion().getXlinkNamespace();
			Util.addAttribute(element, xlinkPrefix, TYPE_NAME, getXlinkNamespace(), FIXED_TYPE);
			Util.addAttribute(element, xlinkPrefix, HREF_NAME, getXlinkNamespace(), href);
			Util.addAttribute(element, xlinkPrefix, ROLE_NAME, getXlinkNamespace(), role);
			Util.addAttribute(element, xlinkPrefix, TITLE_NAME, getXlinkNamespace(), title);
			Util.addAttribute(element, xlinkPrefix, LABEL_NAME, getXlinkNamespace(), label);
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);
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
	 * <li>The role is non-empty and a valid URI, starting in DDMS 4.0.</li>
	 * <li>The label is a valid NCName, starting in DDMS 4.0.</li>
	 * <li>Does not validate the security attributes. It is the parent class' responsibility
	 * to do that.
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), Link.getName(getDDMSVersion()));
		Util.requireDDMSValue("type attribute", getType());
		Util.requireDDMSValue("href attribute", getHref());
		Util.requireDDMSValidURI(getHref());
		if (!getType().equals(FIXED_TYPE))
			throw new InvalidDDMSException("The type attribute must have a fixed value of \"" + FIXED_TYPE + "\".");
		
		// Should be reviewed as additional versions of DDMS are supported.
		if (getDDMSVersion().isAtLeast("4.0")) {
			Util.requireDDMSValidURI(getRole());
			if (!Util.isEmpty(getLabel()))
				Util.requireValidNCName(getLabel());
		}
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
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
		html.append(buildHTMLMeta(prefix + TYPE_NAME, getType(), true));
		html.append(buildHTMLMeta(prefix + HREF_NAME, getHref(), true));
		html.append(buildHTMLMeta(prefix + ROLE_NAME, getRole(), false));
		html.append(buildHTMLMeta(prefix + TITLE_NAME, getTitle(), false));
		html.append(buildHTMLMeta(prefix + LABEL_NAME, getLabel(), false));
		html.append(getSecurityAttributes().toHTML(prefix));
		return (html.toString());
	}
	
	/**
	 * Outputs to Text with a prefix at the beginning of each line.
	 * 
	 * @param prefix the prefix to add
	 * @return the Text output
	 */
	public String toText(String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + " ";
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine(prefix + TYPE_NAME, getType(), true));
		text.append(buildTextLine(prefix + HREF_NAME, getHref(), true));
		text.append(buildTextLine(prefix + ROLE_NAME, getRole(), false));
		text.append(buildTextLine(prefix + TITLE_NAME, getTitle(), false));
		text.append(buildTextLine(prefix + LABEL_NAME, getLabel(), false));
		text.append(getSecurityAttributes().toText(prefix));
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
			&& getLabel().equals(test.getLabel())
			&& getSecurityAttributes().equals(test.getSecurityAttributes()));
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
		result = 7 * result + getSecurityAttributes().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("link");
	}
	
	/**
	 * Accessor for the type attribute.
	 */
	public String getType() {
		return (getAttributeValue(TYPE_NAME, getXlinkNamespace()));
	}
	
	/**
	 * Accessor for the href attribute.
	 */
	public String getHref() {
		return (getAttributeValue(HREF_NAME, getXlinkNamespace()));
	}
	
	/**
	 * Accessor for the role attribute.
	 */
	public String getRole() {
		return (getAttributeValue(ROLE_NAME, getXlinkNamespace()));
	}
	
	/**
	 * Accessor for the title attribute.
	 */
	public String getTitle() {
		return (getAttributeValue(TITLE_NAME, getXlinkNamespace()));
	}
	
	/**
	 * Accessor for the label attribute.
	 */
	public String getLabel() {
		return (getAttributeValue(LABEL_NAME, getXlinkNamespace()));
	}
	
	/**
	 * Accessor for the xlink namespace
	 */
	private String getXlinkNamespace() {
		return _xlinkNamespace;
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
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