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
package buri.ddmsence.ddms.security;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:security.
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ICISM:excludeFromRollup</u>: (required, fixed as "true", starting in v3.0)<br />
 * This class is also decorated with ICISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Security<br />
 * <u>Description</u>: The highest level of classification, dissemination controls, and declassification rules applicable to a data asset.<br />
 * <u>Obligation</u>: Mandatory<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Security extends AbstractBaseComponent {

	private SecurityAttributes _cachedSecurityAttributes = null;
	
	private static final String FIXED_ROLLUP = "true";
	
	/** The element name of this component */
	public static final String NAME = "security";
	
	/** Attribute name */
	public static final String EXCLUDE_FROM_ROLLUP_NAME = "excludeFromRollup";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Security(Element element) throws InvalidDDMSException {
		try {
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
	 * @param securityAttributes any security attributes (classification and ownerProducer are required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Security(SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Security.NAME, null);
			if (!DDMSVersion.isCurrentVersion("2.0"))
				Util.addAttribute(element, PropertyReader.getProperty("icism.prefix"), EXCLUDE_FROM_ROLLUP_NAME, 
					DDMSVersion.getCurrentVersion().getIcismNamespace(), FIXED_ROLLUP);
			_cachedSecurityAttributes = securityAttributes;
			if (securityAttributes != null)
				securityAttributes.addTo(element);
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
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>(v3.0) The excludeFromRollup is set and has a value of "true".</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		if (!"2.0".equals(getDDMSVersion())) {
			if (getExcludeFromRollup() == null)
				throw new InvalidDDMSException("The excludeFromRollup attribute is required.");
			if (!FIXED_ROLLUP.equals(String.valueOf(getExcludeFromRollup())))
				throw new InvalidDDMSException("The excludeFromRollup attribute must have a fixed value of \""
					+ FIXED_ROLLUP + "\".");
		} else if (getExcludeFromRollup() != null)
			throw new InvalidDDMSException("The excludeFromRollup attribute cannot be used in DDMS 2.0.");
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();

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
		StringBuffer html = new StringBuffer();
		if (getExcludeFromRollup() != null)
			html.append(buildHTMLMeta("security.excludeFromRollup", String.valueOf(getExcludeFromRollup()), true));
		html.append(getSecurityAttributes().toHTML(Security.NAME));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		if (getExcludeFromRollup() != null)
			text.append(buildTextLine("Exclude From Rollup", String.valueOf(getExcludeFromRollup()), true));
		text.append(getSecurityAttributes().toText(""));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Security))
			return (false);
		Security test = (Security) obj;
		// ExcludeFromRollup is not included in equality, becuase it is fixed at TRUE.
		return (getSecurityAttributes().equals(test.getSecurityAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		// ExcludeFromRollup is not included in hashCode, becuase it is fixed at TRUE.
		result = 7 * result + getSecurityAttributes().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the excludeFromRollup attribute. This may be null for DDMS v2.0 components.
	 */
	public Boolean getExcludeFromRollup() {
		String value = getAttributeValue(EXCLUDE_FROM_ROLLUP_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		if ("true".equals(value))
			return (Boolean.TRUE);
		if ("false".equals(value))
			return (Boolean.FALSE);
		return (null);
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
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
	public static class Builder implements IBuilder {
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Security security) {
			setSecurityAttributes(new SecurityAttributes.Builder(security.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Security commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Security(getSecurityAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the Security Attributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the Security Attributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}
	}
} 