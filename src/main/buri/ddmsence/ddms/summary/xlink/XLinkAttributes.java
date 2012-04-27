/* Copyright 2010 - 2012 by Brian Uri!
   
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
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.resource.RevisionRecall;
import buri.ddmsence.ddms.resource.TaskID;
import buri.ddmsence.ddms.summary.Link;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Attribute group for the XLINK attributes.
 * 
 * <p>
 * This class only models the subset of attributes and values that are employed by the DDMS specification. 
 * Determinations about whether an attribute is optional or required depend on the decorated class 
 * ({@link Link}, {@link RevisionRecall}, or {@link TaskID}).
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Locator Attributes (for ddms:link)</th></tr><tr><td class="infoBody">
 * <u>xlink:type</u>: the type of link (optional, but fixed as "locator" if set)<br />
 * <u>xlink:href</u>: A target URL (optional)<br />
 * <u>xlink:role</u>: The URI reference identifies some resource that describes the intended property. (optional)<br />
 * <u>xlink:title</u>: Used to describe the meaning of a link or resource in a human-readable fashion, along the same
 * lines as the role or arcrole attribute. (optional)<br />
 * <u>xlink:label</u>: The label attribute provides a name for the link (optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Resource Attributes (for ddms:revisionRecall)</th></tr><tr>
 * <td class="infoBody">
 * <u>xlink:type</u>: (optional, but fixed as "resource" if set)<br />
 * <u>xlink:role</u>: The URI reference identifies some resource that describes the intended property. When no value is
 * supplied, no particular role value is to be inferred. (optional, but must be non-empty if set)<br />
 * <u>xlink:title</u>: Used to describe the meaning of a link or resource in a human-readable fashion, along the same
 * lines as the role or arcrole attribute. (optional)<br />
 * <u>xlink:label</u>: The label attribute provides a name for the link (optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Simple Attributes (for ddms:taskID)</th></tr><tr><td class="infoBody">
 * <u>xlink:type</u>: (optional, but fixed as "simple" if set)<br />
 * <u>xlink:href</u>: A URL (optional, must be a URI)<br />
 * <u>xlink:role</u>: The URI reference identifies some resource that describes the intended property. When no value is
 * supplied, no particular role value is to be inferred. (optional, but must be non-empty if set)<br />
 * <u>xlink:title</u>: Used to describe the meaning of a link or resource in a human-readable fashion, along the same
 * lines as the role or arcrole attribute. (optional)<br />
 * <u>xlink:arcrole</u>: A URI reference describing an arc role (optional)<br />
 * <u>xlink:show</u>: A token which signifies the behavior intentions for traversal (optional)<br />
 * <u>xlink:actuate</u>: A token which signifies the behavior intentions for traversal (optional)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class XLinkAttributes extends AbstractAttributeGroup {
	
	private String _type = null;
	private String _href = null;
	private String _role = null;
	private String _title = null;	
	private String _label = null;	
	private String _arcrole = null;
	private String _show = null;
	private String _actuate = null;

	private static final String TYPE_NAME = "type";
	private static final String HREF_NAME = "href";
	private static final String ROLE_NAME = "role";
	private static final String TITLE_NAME = "title";
	private static final String LABEL_NAME = "label";
	private static final String ARC_ROLE_NAME = "arcrole";
	private static final String SHOW_NAME = "show";
	private static final String ACTUATE_NAME = "actuate";
	
	private static final String TYPE_LOCATOR = "locator";
	private static final String TYPE_SIMPLE = "simple";
	private static final String TYPE_RESOURCE = "resource";
	
	private static Set<String> TYPE_TYPES = new HashSet<String>();
	static {
		TYPE_TYPES.add(TYPE_LOCATOR);
		TYPE_TYPES.add(TYPE_SIMPLE);
		TYPE_TYPES.add(TYPE_RESOURCE);
	}
	private static Set<String> SHOW_TYPES = new HashSet<String>();
	static {
		SHOW_TYPES.add("new");
		SHOW_TYPES.add("replace");
		SHOW_TYPES.add("embed");
		SHOW_TYPES.add("other");
		SHOW_TYPES.add("none");		
	}
	
	private static Set<String> ACTUATE_TYPES = new HashSet<String>();
	static {
		ACTUATE_TYPES.add("onLoad");
		ACTUATE_TYPES.add("onRequest");
		ACTUATE_TYPES.add("other");
		ACTUATE_TYPES.add("none");
	}
	
	/**
	 * Returns a non-null instance of XLink attributes. If the instance passed in is not null, it will be returned.
	 * 
	 * @param xlinkAttributes the attributes to return by default
	 * @return a non-null attributes instance
	 * @throws InvalidDDMSException if there are problems creating the empty attributes instance
	 */
	public static XLinkAttributes getNonNullInstance(XLinkAttributes xlinkAttributes) throws InvalidDDMSException {
		return (xlinkAttributes == null ? new XLinkAttributes() : xlinkAttributes);
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
		_arcrole = element.getAttributeValue(ARC_ROLE_NAME, xlinkNamespace);
		_show = element.getAttributeValue(SHOW_NAME, xlinkNamespace);
		_actuate = element.getAttributeValue(ACTUATE_NAME, xlinkNamespace);
		validate();
	}

	/**
	 * Constructor which builds from raw data for an unknown type.
	 * 
	 * @throws InvalidDDMSException
	 */
	public XLinkAttributes() throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion().getNamespace());
		validate();
	}
	
	/**
	 * Constructor which builds from raw data for a resource link.
	 * 
	 * @param role	the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public XLinkAttributes(String role, String title, String label) throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion().getNamespace());
		_type = TYPE_RESOURCE;
		_role = role;
		_title = title;
		_label = label;
		validate();
	}

	/**
	 * Constructor which builds from raw data for a locator link.
	 * 
	 * @param href the link href (optional)
	 * @param role the role attribute (optional)
	 * @param title the link title (optional)
	 * @param label the name of the link (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public XLinkAttributes(String href, String role, String title, String label) throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion().getNamespace());
		_type = TYPE_LOCATOR;
		_href = href;
		_role = role;
		_title = title;
		_label = label;
		validate();
	}
	
	/**
	 * Constructor which builds from raw data for a simple link.
	 * 
	 * @param href	the link href (optional)
	 * @param role	the role attribute (optional)
	 * @param title the link title (optional)
	 * @param arcrole the arcrole (optional)
	 * @param show the show token (optional)
	 * @param actuate the actuate token (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public XLinkAttributes(String href, String role, String title, String arcrole, String show, String actuate)
		throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion().getNamespace());
		_type = TYPE_SIMPLE;
		_href = href;
		_role = role;
		_title = title;
		_arcrole = arcrole;
		_show = show;
		_actuate = actuate;
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
		Util.addAttribute(element, xlinkPrefix, ARC_ROLE_NAME, xlinkNamespace, getArcrole());
		Util.addAttribute(element, xlinkPrefix, SHOW_NAME, xlinkNamespace, getShow());
		Util.addAttribute(element, xlinkPrefix, ACTUATE_NAME, xlinkNamespace, getActuate());
	}
	 
	/**
	 * Validates the attribute group.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>If the href attribute is set, it is a valid URI.</li>
	 * <li>If the role attribute is set, it is a valid URI, starting in DDMS 4.0.1.</li>
	 * <li>If the label attribute is set, it is a valid NCName, starting in DDMS 4.0.1.</li>
	 * <li>If the arcrole attribute is set, it is a valid URI.</li>
	 * <li>If the show attribute is set, it is a valid token.</li>
	 * <li>If the actuate attribute is set, it is a valid token.</li>
	 * <li>Does not validate the required nature of any attribute. It is the parent class'
	 * responsibility to do that.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		if (!Util.isEmpty(getHref()))
			Util.requireDDMSValidURI(getHref());

		// Should be reviewed as additional versions of DDMS are supported.
		if (getDDMSVersion().isAtLeast("4.0.1")) {
			if (!Util.isEmpty(getRole()))
				Util.requireDDMSValidURI(getRole());
			if (!Util.isEmpty(getLabel()))
				Util.requireValidNCName(getLabel());
		}
		if (!Util.isEmpty(getArcrole()))
			Util.requireDDMSValidURI(getArcrole());
		if (!Util.isEmpty(getShow()) && !SHOW_TYPES.contains(getShow()))
			throw new InvalidDDMSException("The show attribute must be one of " + SHOW_TYPES);
		if (!Util.isEmpty(getActuate()) && !ACTUATE_TYPES.contains(getActuate()))
			throw new InvalidDDMSException("The actuate attribute must be one of " + ACTUATE_TYPES);		
		super.validate();
	}
	
	/**
	 * @see AbstractAttributeGroup#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		String localPrefix = Util.getNonNullString(prefix);		
		StringBuffer text = new StringBuffer();
		text.append(Resource.buildOutput(isHTML, localPrefix + TYPE_NAME, getType()));
		text.append(Resource.buildOutput(isHTML, localPrefix + HREF_NAME, getHref()));
		text.append(Resource.buildOutput(isHTML, localPrefix + ROLE_NAME, getRole()));
		text.append(Resource.buildOutput(isHTML, localPrefix + TITLE_NAME, getTitle()));
		text.append(Resource.buildOutput(isHTML, localPrefix + LABEL_NAME, getLabel()));
		text.append(Resource.buildOutput(isHTML, localPrefix + ARC_ROLE_NAME, getArcrole()));
		text.append(Resource.buildOutput(isHTML, localPrefix + SHOW_NAME, getShow()));
		text.append(Resource.buildOutput(isHTML, localPrefix + ACTUATE_NAME, getActuate()));
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
			&& getLabel().equals(test.getLabel())
			&& getArcrole().equals(test.getArcrole())
			&& getShow().equals(test.getShow())
			&& getActuate().equals(test.getActuate()));		
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
		result = 7 * result + getArcrole().hashCode();
		result = 7 * result + getShow().hashCode();
		result = 7 * result + getActuate().hashCode();
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
	 * Accessor for the arcrole
	 */
	public String getArcrole() {
		return (Util.getNonNullString(_arcrole));
	}
	
	/**
	 * Accessor for the show
	 */
	public String getShow() {
		return (Util.getNonNullString(_show));
	}
	
	/**
	 * Accessor for the actuate
	 */
	public String getActuate() {
		return (Util.getNonNullString(_actuate));
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
		private String _arcrole;
		private String _show;
		private String _actuate;
		
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
			setArcrole(attributes.getArcrole());
			setShow(attributes.getShow());
			setActuate(attributes.getActuate());
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. Will always return an empty instance instead of a null
		 * one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public XLinkAttributes commit() throws InvalidDDMSException {
			if (TYPE_LOCATOR.equals(getType()))
				return (new XLinkAttributes(getHref(), getRole(), getTitle(), getLabel()));
			if (TYPE_SIMPLE.equals(getType()))
				return (new XLinkAttributes(getHref(), getRole(), getTitle(), getArcrole(), getShow(), getActuate()));
			if (TYPE_RESOURCE.equals(getType()))
				return (new XLinkAttributes(getRole(), getTitle(), getLabel()));
			return (new XLinkAttributes());
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
				&& Util.isEmpty(getLabel())
				&& Util.isEmpty(getArcrole())
				&& Util.isEmpty(getShow())
				&& Util.isEmpty(getActuate()));				
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

		/**
		 * Builder accessor for the arcrole
		 */
		public String getArcrole() {
			return _arcrole;
		}

		/**
		 * Builder accessor for the arcrole
		 */
		public void setArcrole(String arcrole) {
			_arcrole = arcrole;
		}

		/**
		 * Builder accessor for the show
		 */
		public String getShow() {
			return _show;
		}

		/**
		 * Builder accessor for the show
		 */
		public void setShow(String show) {
			_show = show;
		}

		/**
		 * Builder accessor for the actuate
		 */
		public String getActuate() {
			return _actuate;
		}

		/**
		 * Builder accessor for the actuate
		 */
		public void setActuate(String actuate) {
			_actuate = actuate;
		}
	}
}