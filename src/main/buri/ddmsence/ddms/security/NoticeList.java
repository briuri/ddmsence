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
package buri.ddmsence.ddms.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.Notice;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:noticeList.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ISM:Notice</u>: A collection of IC notices (1-to-many required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link SecurityAttributes}</u>:  The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: A collection of IC notices.<br />
 * <u>Obligation</u>: Optional.<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class NoticeList extends AbstractBaseComponent {
	
	private List<Notice> _cachedNotices;
	private SecurityAttributes _securityAttributes;
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public NoticeList(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			_cachedNotices = new ArrayList<Notice>();			
			Elements notices = element.getChildElements(Notice.getName(getDDMSVersion()), getDDMSVersion().getIsmNamespace());
			for (int i = 0; i < notices.size(); i++) {
				_cachedNotices.add(new Notice(notices.get(i)));
			}
			_securityAttributes = new SecurityAttributes(element);
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param notices the notices (at least 1 required)
	 * @param securityAttributes any security attributes (classification and ownerProducer are optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public NoticeList(List<Notice> notices, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		try {
			if (notices == null)
				notices = Collections.emptyList();
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			Element element = Util.buildDDMSElement(NoticeList.getName(version), null);
			for (Notice noticeText : notices)
				element.appendChild(noticeText.getXOMElementCopy());
			_cachedNotices = notices;
			_securityAttributes = SecurityAttributes.getNonNullInstance(securityAttributes);
			_securityAttributes.addTo(element);
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
	 * <li>At least 1 Notice exists.</li>
	 * <li>This component cannot be used until DDMS 4.0 or later.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * </td></tr></table>
	 *  
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), NoticeList.getName(getDDMSVersion()));
		
		if (getNotices().isEmpty())
			throw new InvalidDDMSException("At least one ISM:Notice must exist within a ddms:noticeList element.");
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");
		
		super.validate();
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		for (Notice notice : getNotices())
			text.append(notice.getOutput(isHTML, prefix));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.addAll(getNotices());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof NoticeList))
			return (false);
		return (true);		
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("noticeList");
	}
	
	/**
	 * Accessor for the list of Notices.
	 */
	public List<Notice> getNotices() {
		return (Collections.unmodifiableList(_cachedNotices));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_securityAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 7750664735441105296L;
		private List<Notice.Builder> _notices;
		private SecurityAttributes.Builder _securityAttributes = null;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(NoticeList notice) {
			for (Notice noticeText : notice.getNotices())
				getNotices().add(new Notice.Builder(noticeText));
			setSecurityAttributes(new SecurityAttributes.Builder(notice.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public NoticeList commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<Notice> notices = new ArrayList<Notice>();
			for (IBuilder builder : getNotices()) {
				Notice component = (Notice) builder.commit();
				if (component != null)
					notices.add(component);
			}
			return (new NoticeList(notices, getSecurityAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getNotices())
				hasValueInList = hasValueInList || !builder.isEmpty();
			return (!hasValueInList && getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the notices
		 */
		public List<Notice.Builder> getNotices() {
			if (_notices == null)
				_notices = new LazyList(Notice.Builder.class);
			return _notices;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}
	}
} 