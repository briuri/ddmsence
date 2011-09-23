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

import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractSimpleString;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ISM:NoticeText.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A NoticeText element can be used without any child text.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ISM:pocType</u>: indicates that the element specifies a POC for particular notice type. (optional)<br />
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class NoticeText extends AbstractSimpleString {
	
	private List<String> _pocTypes = null;
	
	private static final String POC_TYPE_NAME = "pocType";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public NoticeText(Element element) throws InvalidDDMSException {
		super(element, false);	
		try {
			String pocTypes = element.getAttributeValue(POC_TYPE_NAME, getDDMSVersion().getIsmNamespace());
			_pocTypes = Util.getXsListAsList(pocTypes);
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
	 * @param value the value of the description child text
	 * @param pocTypes the value of the pocType attribute (optional)
	 * @param securityAttributes any security attributes (classification and ownerProducer are required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public NoticeText(String value, List<String> pocTypes, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(PropertyReader.getPrefix("ism"), DDMSVersion.getCurrentVersion().getIsmNamespace(), NoticeText
			.getName(DDMSVersion.getCurrentVersion()), value, securityAttributes, false);
		try {
			if (pocTypes == null)
				pocTypes = Collections.emptyList();
			if (!pocTypes.isEmpty())
				Util.addAttribute(getXOMElement(), PropertyReader.getPrefix("ism"), POC_TYPE_NAME, DDMSVersion
					.getCurrentVersion().getIsmNamespace(), Util.getXsList(pocTypes));
			_pocTypes = pocTypes;
			validate();
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
	 * <li>This component cannot be used until DDMS 4.0 or later.</li>
	 * <li>If set, the pocTypes must each be a valid token.</li>
	 * </td></tr></table>
	 *  
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getDDMSVersion().getIsmNamespace(), NoticeText.getName(getDDMSVersion()));
		
		// TBD: Validate against CVE.
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");
		
		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>An ISM:NoticeText element was found with no value.</li>
	 * <li>Include any validation warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getValue()))
			addWarning("An ISM:" + getName() + " element was found with no value.");
		super.validateWarnings();		
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + "noticeText";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix, getValue(), true));
		text.append(buildOutput(isHTML, prefix +  "."  + POC_TYPE_NAME, Util.getXsList(getPocTypes()), false));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix + "."));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof NoticeText))
			return (false);
		NoticeText test = (NoticeText) obj;
		return (Util.listEquals(getPocTypes(), test.getPocTypes()));		
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getPocTypes().hashCode();
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
		return ("NoticeText");
	}
	
	/**
	 * Accessor for the pocType attribute.
	 */
	public List<String> getPocTypes() {
		return (_pocTypes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder extends AbstractSimpleString.Builder {
		private static final long serialVersionUID = 7750664735441105296L;
		private List<String> _pocTypes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(NoticeText text) {
			super(text);
			setPocTypes(text.getPocTypes());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public NoticeText commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new NoticeText(getValue(), getPocTypes(), getSecurityAttributes().commit()));
		}

		/**
		 * Builder accessor for the pocTypes
		 */
		public List<String> getPocTypes() {
			if (_pocTypes == null)
				_pocTypes = new LazyList(String.class);
			return _pocTypes;
		}

		/**
		 * Builder accessor for the pocTypes
		 */
		public void setPocTypes(List<String> pocTypes) {
			_pocTypes = pocTypes;
		}
	}
} 