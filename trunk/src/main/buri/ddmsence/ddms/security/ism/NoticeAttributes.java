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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
 * Attribute group for the ISM notice markings used on a ddms:resource and ISM:Notice, starting in DDMS 4.0.
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ISM:noticeType</u>: (optional)<br />
 * <u>ISM:noticeReason</u>: (optional)<br />
 * <u>ISM:noticeDate</u>: (optional)<br />
 * <u>ISM:unregisteredNoticeType</u>: (optional)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class NoticeAttributes extends AbstractAttributeGroup {
	private String _cachedNoticeType = null;
	private String _cachedNoticeReason = null;
	private XMLGregorianCalendar _cachedNoticeDate = null;
	private String _cachedUnregisteredNoticeType = null;
		
	/** Attribute name */
	public static final String NOTICE_TYPE_NAME = "noticeType";
	
	/** Attribute name */
	public static final String NOTICE_REASON_NAME = "noticeReason";
	
	/** Attribute name */
	public static final String NOTICE_DATE_NAME = "noticeDate";
	
	/** Attribute name */
	public static final String UNREGISTERED_NOTICE_TYPE_NAME = "unregisteredNoticeType";
			
	/** Maximum length of reason and unregistered notice type attributes. */
	public static int MAX_LENGTH = 2048;
	
	private static final Set<String> ALL_NAMES = new HashSet<String>();
	static {
		ALL_NAMES.add(NOTICE_TYPE_NAME);
		ALL_NAMES.add(NOTICE_REASON_NAME);
		ALL_NAMES.add(NOTICE_DATE_NAME);
		ALL_NAMES.add(UNREGISTERED_NOTICE_TYPE_NAME);
	}
	
	/** A set of all SecurityAttribute names which should not be converted into ExtensibleAttributes */
	public static final Set<String> NON_EXTENSIBLE_NAMES = Collections.unmodifiableSet(ALL_NAMES);
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element which is decorated with these attributes.
	 */
	public NoticeAttributes(Element element) throws InvalidDDMSException {
		super(element.getNamespaceURI());
		String icNamespace = getDDMSVersion().getIsmNamespace();

		_cachedNoticeType = element.getAttributeValue(NOTICE_TYPE_NAME, icNamespace);;
		_cachedNoticeReason = element.getAttributeValue(NOTICE_REASON_NAME, icNamespace);;
		_cachedUnregisteredNoticeType = element.getAttributeValue(UNREGISTERED_NOTICE_TYPE_NAME, icNamespace);
		String noticeDate = element.getAttributeValue(NOTICE_DATE_NAME, icNamespace);
		if (!Util.isEmpty(noticeDate))
			_cachedNoticeDate = getFactory().newXMLGregorianCalendar(noticeDate);
		validate();
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param noticeType the notice type (with a value from the CVE)
	 * @param noticeReason the reason associated with a notice
	 * @param noticeDate the date associated with a notice
	 * @param unregisteredNoticeType a notice type not in the CVE
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public NoticeAttributes(String noticeType, String noticeReason, String noticeDate, String unregisteredNoticeType)
		throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion().getNamespace());
		_cachedNoticeType = noticeType;
		_cachedNoticeReason = noticeReason;
		_cachedUnregisteredNoticeType = unregisteredNoticeType;
		if (!Util.isEmpty(noticeDate)) {
			try {
				_cachedNoticeDate = getFactory().newXMLGregorianCalendar(noticeDate);
			}
			catch (IllegalArgumentException e) {
				throw new InvalidDDMSException("The ISM:noticeDate attribute is not in a valid date format.");
			}
		}
		validate();
	}
			
	/**
	 * Convenience method to add these attributes onto an existing XOM Element
	 * 
	 * @param element the element to decorate
	 */
	public void addTo(Element element) throws InvalidDDMSException {
		DDMSVersion elementVersion = DDMSVersion.getVersionForNamespace(element.getNamespaceURI());
		validateSameVersion(elementVersion);
		String icNamespace = elementVersion.getIsmNamespace();
		String icPrefix = PropertyReader.getPrefix("ism");

		Util.addAttribute(element, icPrefix, NOTICE_TYPE_NAME, icNamespace, getNoticeType());
		Util.addAttribute(element, icPrefix, NOTICE_REASON_NAME, icNamespace, getNoticeReason());
		if (getNoticeDate() != null)
			Util.addAttribute(element, icPrefix, NOTICE_DATE_NAME, icNamespace, getNoticeDate().toXMLFormat());
		Util.addAttribute(element, icPrefix, UNREGISTERED_NOTICE_TYPE_NAME, icNamespace, getUnregisteredNoticeType());
	}

	/**
	 * Checks if any attributes have been set.
	 * 
	 * @return true if no attributes have values, false otherwise
	 */
	public boolean isEmpty() {
		return (Util.isEmpty(getNoticeType()) 
			&& Util.isEmpty(getNoticeReason())
			&& Util.isEmpty(getUnregisteredNoticeType()) 
			&& getNoticeDate() == null);
	}

	/**
	 * Validates the attribute group. Where appropriate the {@link ISMVocabulary} enumerations are validated.
	 * For any validation rule in which the value "must be a valid token", the configurable property,
	 * <code>icism.cve.validationAsErrors</code> determines whether the results of these checks are returned
	 * as errors or warnings. The default behavior is to return errors when a value is not found in a controlled
	 * vocabulary. Note that this property does not affect other types of rules -- for example, a
	 * noticeReason which is too long will always result in an error.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>If set, the noticeType attribute must be a valid token.</li>
	 * <li>The noticeReason must be shorter than 2048 characters.</li>
	 * <li>The unregisteredNoticeType must be shorter than 2048 characters.</li>
	 * <li>If set, the noticeDate attribute is a valid xs:date value.</li>
	 * <li>These attributes cannot be used until DDMS 4.0 or later.</li>
	 * <li>Does NOT do any validation on the constraints described in the DES ISM specification.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		// Should be reviewed as additional versions of DDMS are supported.
		DDMSVersion version = getDDMSVersion();
		ISMVocabulary.setDDMSVersion(version);
//		if (!Util.isEmpty(getNoticeType()))
//			validateEnumeration(ISMVocabulary.CVE_NOTICE_TYPE, getNoticeType());
		if (!Util.isEmpty(getNoticeReason()) && getNoticeReason().length() > MAX_LENGTH)
			throw new InvalidDDMSException("The noticeReason attribute must be shorted than " + MAX_LENGTH
				+ " characters.");
		if (!Util.isEmpty(getUnregisteredNoticeType()) && getUnregisteredNoticeType().length() > MAX_LENGTH)
			throw new InvalidDDMSException("The unregisteredNoticeType attribute must be shorted than "
				+ MAX_LENGTH + " characters.");
		if (getNoticeDate() != null && !getNoticeDate().getXMLSchemaType().equals(DatatypeConstants.DATE))
			throw new InvalidDDMSException("The noticeDate attribute must be in the xs:date format (YYYY-MM-DD).");
		if (!version.isAtLeast("4.0") && !isEmpty())
			throw new InvalidDDMSException("Notice attributes cannot be used until DDMS 4.0 or later.");

		super.validate();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix);
		StringBuffer text = new StringBuffer();
		text.append(Resource.buildOutput(isHTML, prefix + NOTICE_TYPE_NAME, getNoticeType(), false));
		text.append(Resource.buildOutput(isHTML, prefix + NOTICE_REASON_NAME, getNoticeReason(), false));
		if (getNoticeDate() != null) {
			text.append(Resource.buildOutput(isHTML, prefix + NOTICE_DATE_NAME, getNoticeDate().toXMLFormat(), false));
		}
		text.append(Resource.buildOutput(isHTML, prefix + UNREGISTERED_NOTICE_TYPE_NAME, getUnregisteredNoticeType(),
			false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof NoticeAttributes))
			return (false);		
		NoticeAttributes test = (NoticeAttributes) obj;
		return (getNoticeType().equals(test.getNoticeType())
			&& getNoticeReason().equals(test.getNoticeReason())
			&& getUnregisteredNoticeType().equals(test.getUnregisteredNoticeType())
			&& Util.nullEquals(getNoticeDate(), test.getNoticeDate()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = 0;
		result = 7 * result + getNoticeType().hashCode();
		result = 7 * result + getNoticeReason().hashCode();
		result = 7 * result + getUnregisteredNoticeType().hashCode();
		if (getNoticeDate() != null)
			result = 7 * result + getNoticeDate().hashCode();
		return (result);
	}	
	
	/**
	 * Accessor for the noticeType attribute.
	 */
	public String getNoticeType() {
		return (Util.getNonNullString(_cachedNoticeType));
	}

	/**
	 * Accessor for the noticeReason attribute.
	 */
	public String getNoticeReason() {
		return (Util.getNonNullString(_cachedNoticeReason));
	}
	
	/**
	 * Accessor for the unregisteredNoticeType attribute.
	 */
	public String getUnregisteredNoticeType() {
		return (Util.getNonNullString(_cachedUnregisteredNoticeType));
	}
	
	/**
	 * Accessor for the noticeDate attribute. May return null if not set.
	 */
	public XMLGregorianCalendar getNoticeDate() {
		return (_cachedNoticeDate == null ? null : getFactory()
			.newXMLGregorianCalendar(_cachedNoticeDate.toXMLFormat()));
	}

	/**
	 * Accesor for the datatype factory
	 */
	private static DatatypeFactory getFactory() {
		return (Util.getDataTypeFactory());
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
		private static final long serialVersionUID = 279072341662308051L;		
		private Map<String, String> _stringAttributes = new HashMap<String, String>();
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(NoticeAttributes attributes) {
			setNoticeType(attributes.getNoticeType());
			setNoticeReason(attributes.getNoticeReason());
			if (attributes.getNoticeDate() != null)
				setNoticeDate(attributes.getNoticeDate().toXMLFormat());
			setUnregisteredNoticeType(attributes.getUnregisteredNoticeType());
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. Will always return an empty instance instead of
		 * a null one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public NoticeAttributes commit() throws InvalidDDMSException {
			return (new NoticeAttributes(getNoticeType(), getNoticeReason(), getNoticeDate(), getUnregisteredNoticeType()));
		}
		
		/**
		 * Checks if any values have been provided for this Builder.
		 * 
		 * @return true if every field is empty
		 */
		public boolean isEmpty() {
			boolean isEmpty = true;
			for (String value : getStringAttributes().values()) {
				isEmpty = isEmpty && Util.isEmpty(value);
			}
			return (isEmpty);				
		}
		
		/**
		 * Builder accessor for the noticeType attribute
		 */
		public String getNoticeType() {
			return (getStringAttributes().get(NOTICE_TYPE_NAME));
		}

		/**
		 * Builder accessor for the noticeType attribute
		 */
		public void setNoticeType(String noticeType) {
			getStringAttributes().put(NOTICE_TYPE_NAME, noticeType);
		}
		
		/**
		 * Builder accessor for the noticeReason attribute
		 */
		public String getNoticeReason() {
			return (getStringAttributes().get(NOTICE_REASON_NAME));
		}

		/**
		 * Builder accessor for the noticeReason attribute
		 */
		public void setNoticeReason(String noticeReason) {
			getStringAttributes().put(NOTICE_REASON_NAME, noticeReason);
		}
		
		/**
		 * Builder accessor for the noticeDate attribute
		 */
		public String getNoticeDate() {
			return (getStringAttributes().get(NOTICE_DATE_NAME));
		}

		/**
		 * Builder accessor for the noticeDate attribute
		 */
		public void setNoticeDate(String noticeDate) {
			getStringAttributes().put(NOTICE_DATE_NAME, noticeDate);
		}
		
		/**
		 * Builder accessor for the unregisteredNoticeType attribute
		 */
		public String getUnregisteredNoticeType() {
			return (getStringAttributes().get(UNREGISTERED_NOTICE_TYPE_NAME));
		}

		/**
		 * Builder accessor for the unregisteredNoticeType attribute
		 */
		public void setUnregisteredNoticeType(String unregisteredNoticeType) {
			getStringAttributes().put(UNREGISTERED_NOTICE_TYPE_NAME, unregisteredNoticeType);
		}
				
		/**
		 * Accessor for the map of attribute names to string values
		 */
		private Map<String, String> getStringAttributes()  {
			return (_stringAttributes);
		}
	}
}