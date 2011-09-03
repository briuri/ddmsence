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
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:keyword.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The keyword value must not be empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:value</u>: The keyword itself (required)<br />
 * Starting in DDMS 3.0, this component can also be decorated with optional {@link ExtensibleAttributes}.<br />
 * This class is also decorated with ICISM {@link SecurityAttributes}, starting in DDMS 4.0. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#subjectCoverage_Subject_keyword<br />
 * <u>Description</u>: Subject keyword(s) that characterize the subject matter of a resource.<br />
 * <u>Obligation</u>: At least 1 category or keyword is required in a Subject.<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Keyword extends AbstractBaseComponent {

	private SecurityAttributes _cachedSecurityAttributes = null;
	private ExtensibleAttributes _cachedExtensibleAttributes = null;
	
	/** The element name of this component */
	public static final String NAME = "keyword";
	
	private static final String VALUE_NAME = "value";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(Element element) throws InvalidDDMSException {
		try {
			_cachedSecurityAttributes = new SecurityAttributes(element);
			_cachedExtensibleAttributes = new ExtensibleAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data. Provided for backwards compatibility to pre-DDMS 4.0 elements.
	 *  
	 * @param value the value attribute (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(String value) throws InvalidDDMSException {
		this(value, null, null);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param value the value attribute (required)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(String value, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		this(value, securityAttributes, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * <p>This constructor will throw an InvalidDDMSException if the extensible attributes uses the reserved
	 * attribute "ddms:value".</p>
	 *  
	 * @param value the value attribute (required)
	 * @param securityAttributes any security attributes (optional)
	 * @param extensions extensible attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Keyword(String value, SecurityAttributes securityAttributes, ExtensibleAttributes extensions) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Keyword.NAME, null);
			Util.addDDMSAttribute(element, VALUE_NAME, value);
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);
			_cachedExtensibleAttributes = (extensions == null ? new ExtensibleAttributes((List<Attribute>) null)
				: extensions);
			_cachedExtensibleAttributes.addTo(element);
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
	 * <li>The keyword value exists and is not empty.</li>
	 * <li>The SecurityAttributes do not exist until DDMS 4.0 or later.</li>
	 * <li>No extensible attributes can exist until DDMS 3.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
		Util.requireDDMSValue("value attribute", getValue());
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException("Security attributes cannot be applied to this component until DDMS 4.0 or later.");
		}
		if (!getDDMSVersion().isAtLeast("3.0") && !getExtensibleAttributes().isEmpty())
			throw new InvalidDDMSException("xs:anyAttribute cannot be applied to ddms:keyword until DDMS 3.0 or later.");
		
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
		String prefix = SubjectCoverage.NAME + ".Subject." + NAME;
		html.append(buildHTMLMeta(prefix, getValue(), false));
		html.append(getSecurityAttributes().toHTML(prefix));
		html.append(getExtensibleAttributes().toHTML(prefix));
		return (html.toString());

	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine(NAME, getValue(), false));
		text.append(getSecurityAttributes().toText(NAME));
		text.append(getExtensibleAttributes().toText(NAME));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Keyword))
			return (false);
		Keyword test = (Keyword) obj;
		return (getValue().equals(test.getValue())
			&& getSecurityAttributes().equals(test.getSecurityAttributes())
			&& getExtensibleAttributes().equals(test.getExtensibleAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getValue().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		result = 7 * result + getExtensibleAttributes().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the value attribute.
	 */
	public String getValue() {
		return (getAttributeValue(VALUE_NAME));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Accessor for the extensible attributes. Will always be non-null, even if not set.
	 */
	public ExtensibleAttributes getExtensibleAttributes() {
		return (_cachedExtensibleAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 5165722850252388155L;
		private String _value;
		private SecurityAttributes.Builder _securityAttributes;
		private ExtensibleAttributes.Builder _extensibleAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Keyword keyword) {
			setValue(keyword.getValue());
			setSecurityAttributes(new SecurityAttributes.Builder(keyword.getSecurityAttributes()));
			setExtensibleAttributes(new ExtensibleAttributes.Builder(keyword.getExtensibleAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Keyword commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Keyword(getValue(), getSecurityAttributes().commit(),
				getExtensibleAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getValue())
				&& getSecurityAttributes().isEmpty()
				&& getExtensibleAttributes().isEmpty());
		}

		/**
		 * Builder accessor for the value attribute
		 */
		public String getValue() {
			return _value;
		}

		/**
		 * Builder accessor for the value attribute
		 */
		public void setValue(String value) {
			_value = value;
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
		
		/**
		 * Builder accessor for the Extensible Attributes
		 */
		public ExtensibleAttributes.Builder getExtensibleAttributes() {
			if (_extensibleAttributes == null)
				_extensibleAttributes = new ExtensibleAttributes.Builder();
			return _extensibleAttributes;
		}
		
		/**
		 * Builder accessor for the Extensible Attributes
		 */
		public void setExtensibleAttributes(ExtensibleAttributes.Builder extensibleAttributes) {
			_extensibleAttributes = extensibleAttributes;
		}
	}
} 