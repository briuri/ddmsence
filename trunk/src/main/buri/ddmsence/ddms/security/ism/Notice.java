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
package buri.ddmsence.ddms.security.ism;

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
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ISM:Notice.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ISM:NoticeText</u>: The text associated with this Notice (1-to-many required), implemented as a {@link NoticeText}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are optional.<br />
 * <u>{@link NoticeAttributes}</u>
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class Notice extends AbstractBaseComponent {
	
	private List<NoticeText> _noticeTexts = null;
	private SecurityAttributes _securityAttributes = null;
	private NoticeAttributes _noticeAttributes = null;
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Notice(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			_noticeTexts = new ArrayList<NoticeText>();
			Elements noticeTexts = element.getChildElements(NoticeText.getName(getDDMSVersion()), getDDMSVersion()
				.getIsmNamespace());
			for (int i = 0; i < noticeTexts.size(); i++) {
				_noticeTexts.add(new NoticeText(noticeTexts.get(i)));
			}
			_noticeAttributes = new NoticeAttributes(element);
			_securityAttributes = new SecurityAttributes(element);
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param noticeTexts the notice texts (at least 1 required)
	 * @param securityAttributes any security attributes (classification and ownerProducer are optional)
	 * @param noticeAttributes any notice attributes
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Notice(List<NoticeText> noticeTexts, SecurityAttributes securityAttributes, NoticeAttributes noticeAttributes)
		throws InvalidDDMSException {
		try {
			if (noticeTexts == null)
				noticeTexts = Collections.emptyList();
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			Element element = Util.buildElement(PropertyReader.getPrefix("ism"), Notice.getName(version), version
				.getIsmNamespace(), null);
			for (NoticeText noticeText : noticeTexts)
				element.appendChild(noticeText.getXOMElementCopy());
			_noticeTexts = noticeTexts;
			_noticeAttributes = NoticeAttributes.getNonNullInstance(noticeAttributes);
			_noticeAttributes.addTo(element);
			_securityAttributes = SecurityAttributes.getNonNullInstance(securityAttributes);
			_securityAttributes.addTo(element);
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
		
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>At least 1 NoticeText exists.</li>
	 * <li>This component cannot be used until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 *  
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getDDMSVersion().getIsmNamespace(), Notice.getName(getDDMSVersion()));
		if (getNoticeTexts().isEmpty())
			throw new InvalidDDMSException("At least one ISM:NoticeText must exist within an ISM:Notice element.");
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");
		
		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the notice attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (!getNoticeAttributes().isEmpty()) {
			addWarnings(getNoticeAttributes().getValidationWarnings(), true);
		}
		super.validateWarnings();		
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, "notice", suffix + ".");
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix, getNoticeTexts()));
		text.append(getSecurityAttributes().getOutput(isHTML, localPrefix));
		text.append(getNoticeAttributes().getOutput(isHTML, localPrefix));
		return (text.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.addAll(getNoticeTexts());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Notice))
			return (false);
		Notice test = (Notice) obj;
		return (getNoticeAttributes().equals(test.getNoticeAttributes()));		
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getNoticeAttributes().hashCode();
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
		return ("Notice");
	}
	
	/**
	 * Accessor for the list of NoticeTexts.
	 */
	public List<NoticeText> getNoticeTexts() {
		return (Collections.unmodifiableList(_noticeTexts));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_securityAttributes);
	}
	
	/**
	 * Accessor for the Notice Attributes. Will always be non-null even if the attributes are not set.
	 */
	public NoticeAttributes getNoticeAttributes() {
		return (_noticeAttributes);
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
		private List<NoticeText.Builder> _noticeTexts;
		private SecurityAttributes.Builder _securityAttributes = null;
		private NoticeAttributes.Builder _noticeAttributes = null;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Notice notice) {
			for (NoticeText noticeText : notice.getNoticeTexts())
				getNoticeTexts().add(new NoticeText.Builder(noticeText));
			setSecurityAttributes(new SecurityAttributes.Builder(notice.getSecurityAttributes()));
			setNoticeAttributes(new NoticeAttributes.Builder(notice.getNoticeAttributes()));			
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Notice commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<NoticeText> noticeTexts = new ArrayList<NoticeText>();
			for (IBuilder builder : getNoticeTexts()) {
				NoticeText component = (NoticeText) builder.commit();
				if (component != null)
					noticeTexts.add(component);
			}
			return (new Notice(noticeTexts, getSecurityAttributes().commit(), getNoticeAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getNoticeTexts())
				hasValueInList = hasValueInList || !builder.isEmpty();
			return (!hasValueInList && getSecurityAttributes().isEmpty() && getNoticeAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the noticeTexts
		 */
		public List<NoticeText.Builder> getNoticeTexts() {
			if (_noticeTexts == null)
				_noticeTexts = new LazyList(NoticeText.Builder.class);
			return _noticeTexts;
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
		
		/**
		 * Builder accessor for the noticeAttributes
		 */
		public NoticeAttributes.Builder getNoticeAttributes() {
			if (_noticeAttributes == null)
				_noticeAttributes = new NoticeAttributes.Builder();
			return _noticeAttributes;
		}
		
		/**
		 * Builder accessor for the noticeAttributes
		 */
		public void setNoticeAttributes(NoticeAttributes.Builder noticeAttributes) {
			_noticeAttributes = noticeAttributes;
		}	
	}
} 