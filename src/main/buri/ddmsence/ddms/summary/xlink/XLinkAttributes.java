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
package buri.ddmsence.ddms.summary.xlink;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import nu.xom.Element;
import buri.ddmsence.AbstractAttributeGroup;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Attribute group for the XLINK attributes.
 * 
 * <p>
 * This class only models the subset of attributes and values that are employed by the DDMS specification. Determinations
 * about whether an attribute is optional or required depend on the decorated class (ddms:link or ddms:taskID).
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>xlink:type</u>: the type of link<br />
 * <u>xlink:href</u>: A target URL<br />
 * <u>xlink:role</u>: The URI reference identifies some resource that describes the intended property.<br />
 * <u>xlink:title</u>: Used to describe the meaning of a link or resource in a human-readable fashion, along the same
 * lines as the role or arcrole attribute.<br />
 * <u>xlink:label</u>: The label attribute provides a name for the link<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class XLinkAttributes extends AbstractAttributeGroup {	
	private String _type;
	private String _href;
	private String _role;
	private String _title;
	private String _label;
	
	private static final String TYPE_NAME = "type";
	private static final String HREF_NAME = "href";
	private static final String ROLE_NAME = "role";
	private static final String TITLE_NAME = "title";
	private static final String LABEL_NAME = "label";
	
	private static Set<String> TYPE_TYPES = new HashSet<String>();
	static {
		TYPE_TYPES.add("simple");
		TYPE_TYPES.add("locator");	
	}
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element which is decorated with these attributes.
	 */
	public XLinkAttributes(Element element) throws InvalidDDMSException {
		super(element.getNamespaceURI());
		String xlinkNamespace = getDDMSVersion().getXlinkNamespace();
		_type = element.getAttributeValue(TYPE_NAME, xlinkNamespace);
		_href = element.getAttributeValue(HREF_NAME, xlinkNamespace);
		_role = element.getAttributeValue(ROLE_NAME, xlinkNamespace);
		_title = element.getAttributeValue(TITLE_NAME, xlinkNamespace);
		_label = element.getAttributeValue(LABEL_NAME, xlinkNamespace);
		validate();
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param href	the link href (optional)
	 * @param role	the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public XLinkAttributes(String type, String href, String role, String title, String label)
		throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion().getNamespace());
		_type = type;
		_href = href;
		_role = role;
		_title = title;
		_label = label;
		validate();
	}
	
	/**
	 * Convenience method to add these attributes onto an existing XOM Element
	 * 
	 * @param element the element to decorate
	 * @throws InvalidDDMSException if the DDMS version of the element is different
	 */
	public void addTo(Element element) throws InvalidDDMSException {
		DDMSVersion elementVersion = DDMSVersion.getVersionForNamespace(element.getNamespaceURI());
		validateSameVersion(elementVersion);
		String xlinkNamespace = elementVersion.getXlinkNamespace();
		String xlinkPrefix = PropertyReader.getPrefix("xlink");
		
		Util.addAttribute(element, xlinkPrefix, TYPE_NAME, xlinkNamespace, getType());
		Util.addAttribute(element, xlinkPrefix, HREF_NAME, xlinkNamespace, getHref());
		Util.addAttribute(element, xlinkPrefix, ROLE_NAME, xlinkNamespace, getRole());
		Util.addAttribute(element, xlinkPrefix, TITLE_NAME, xlinkNamespace, getTitle());
		Util.addAttribute(element, xlinkPrefix, LABEL_NAME, xlinkNamespace, getLabel());
	}
	
	/**
	 * Validates the attribute group.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>If the type is set, it is either "locator" or "simple".</li>
	 * <li>If the href is set, it is a valid URI.</li>
	 * <li>If the role is set, it is a valid URI, starting in DDMS 4.0.</li>
	 * <li>If the label is set, it is a valid NCName, starting in DDMS 4.0.</li>
	 * <li>Does not validate the required nature of any attribute. It is the parent class'
	 * responsibility to do that.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		if (!Util.isEmpty(getType()) && !TYPE_TYPES.contains(getType()))
			throw new InvalidDDMSException("The type attribute must be one of " + TYPE_TYPES);
		if (!Util.isEmpty(getHref()))
			Util.requireDDMSValidURI(getHref());

		// Should be reviewed as additional versions of DDMS are supported.
		if (getDDMSVersion().isAtLeast("4.0")) {
			if (!Util.isEmpty(getRole()))
				Util.requireDDMSValidURI(getRole());
			if (!Util.isEmpty(getLabel()))
				Util.requireValidNCName(getLabel());
		}
		
		super.validate();
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix);		
		StringBuffer text = new StringBuffer();
		text.append(Resource.buildOutput(isHTML, prefix + TYPE_NAME, getType(), false));
		text.append(Resource.buildOutput(isHTML, prefix + HREF_NAME, getHref(), false));
		text.append(Resource.buildOutput(isHTML, prefix + ROLE_NAME, getRole(), false));
		text.append(Resource.buildOutput(isHTML, prefix + TITLE_NAME, getTitle(), false));
		text.append(Resource.buildOutput(isHTML, prefix + LABEL_NAME, getLabel(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof XLinkAttributes))
			return (false);
		XLinkAttributes test = (XLinkAttributes) obj;
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
		int result = 0; 
		result = 7 * result + getType().hashCode();
		result = 7 * result + getHref().hashCode();
		result = 7 * result + getRole().hashCode();
		result = 7 * result + getTitle().hashCode();
		result = 7 * result + getLabel().hashCode();		
		return (result);
	}
	
	/**
	 * Accessor for the type
	 */
	public String getType() {
		return (Util.getNonNullString(_type));
	}

	/**
	 * Accessor for the href
	 */
	public String getHref() {
		return (Util.getNonNullString(_href));
	}

	/**
	 * Accessor for the role
	 */
	public String getRole() {
		return (Util.getNonNullString(_role));
	}

	/**
	 * Accessor for the title
	 */
	public String getTitle() {
		return (Util.getNonNullString(_title));
	}

	/**
	 * Accessor for the label
	 */
	public String getLabel() {
		return (Util.getNonNullString(_label));
	}
	
	/**
	 * Builder for these attributes.
	 * 
	 * <p>This class does not implement the IBuilder interface, because the behavior of commit() is at odds with the
	 * standard commit() method. As an attribute group, an empty attribute group will always be returned instead of
	 * null.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder implements Serializable {
		private static final long serialVersionUID = 6071979027185230870L;
		private String _type;
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
		public Builder(XLinkAttributes attributes) {
			setType(attributes.getType());
			setHref(attributes.getHref());
			setRole(attributes.getRole());
			setTitle(attributes.getTitle());
			setLabel(attributes.getLabel());
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. Will always return an empty instance instead of a null
		 * one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public XLinkAttributes commit() throws InvalidDDMSException {
			return (new XLinkAttributes(getType(), getHref(), getRole(), getTitle(), getLabel()));
		}
		
		/**
		 * Checks if any values have been provided for this Builder.
		 * 
		 * @return true if every field is empty
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getType())
				&& Util.isEmpty(getHref())
				&& Util.isEmpty(getRole())
				&& Util.isEmpty(getTitle())
				&& Util.isEmpty(getLabel()));				
		}
		
		/**
		 * Builder accessor for the type
		 */
		public String getType() {
			return _type;
		}

		/**
		 * Builder accessor for the type
		 */
		public void setType(String type) {
			_type = type;
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