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
package buri.ddmsence;

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Base class for NTK elements which consist of simple child text decorated with NTK attributes, and security attributes.
 * 
 * <p> Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set
 * before the component is used. </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ntk:id</u>: A unique XML identifier (optional)<br />
 * <u>ntk:IDReference</u>: A cross-reference to a unique identifier (optional)<br />
 * <u>ntk:qualifier</u>: A user-defined property within an element for general purpose processing used with block 
 * objects to provide supplemental information over and above that conveyed by the element name (optional)<br />
 * This class is decorated with ISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public abstract class AbstractNtkString extends AbstractBaseComponent {
	
	private boolean _isTokenBased;
	private SecurityAttributes _cachedSecurityAttributes = null;
		
	private static final String ID_NAME = "id";
	private static final String ID_REFERENCE_NAME = "IDReference";
	private static final String QUALIFIER_NAME = "qualifier";
	
	/**
	 * Base constructor which works from a XOM element.
	 * 
	 * @param isTokenBased true if the child text is an NMTOKEN, false if it's just a string
	 * @param element the XOM element
	 */
	protected AbstractNtkString(boolean isTokenBased, Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			_isTokenBased = isTokenBased;
			_cachedSecurityAttributes = new SecurityAttributes(element);
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);	
		}
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param isTokenBased true if the child text is an NMTOKEN, false if it's just a string
	 * @param name the name of the element without a prefix
	 * @param value the value of the element's child text
	 * @param id the NTK ID (optional)
	 * @param idReference a reference to an NTK ID (optional)
	 * @param qualifier an NTK qualifier (optional)
	 * @param attributes the security attributes
	 * @param validateNow whether to validate immediately
	 */
	protected AbstractNtkString(boolean isTokenBased, String name, String value, String id, String idReference,
		String qualifier, SecurityAttributes attributes, boolean validateNow) throws InvalidDDMSException {
		try {
			String ntkPrefix = PropertyReader.getProperty("ntk.prefix");
			String ntkNamespace = DDMSVersion.getCurrentVersion().getNtkNamespace();
			
			Element element = Util.buildElement(ntkPrefix, name, ntkNamespace, value);
			_isTokenBased = isTokenBased;
			_cachedSecurityAttributes = attributes;
			if (!Util.isEmpty(id))
				Util.addAttribute(element, ntkPrefix, ID_NAME, ntkNamespace, id);
			if (!Util.isEmpty(idReference))
				Util.addAttribute(element, ntkPrefix, ID_REFERENCE_NAME, ntkNamespace, idReference);
			if (!Util.isEmpty(qualifier))
				Util.addAttribute(element, ntkPrefix, QUALIFIER_NAME, ntkNamespace, qualifier);
			if (attributes != null)
				attributes.addTo(element);
			setXOMElement(element, validateNow);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>If this is an NMTOKEN-based string, and the child text is not empty, the child text is an NMTOKEN.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot be used until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		if (isTokenBased())
			Util.requireValidNMToken(getValue());
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");
		
		super.validate();
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractNtkString))
			return (false);
		AbstractNtkString test = (AbstractNtkString) obj;
		return (getValue().equals(test.getValue())
			&& getID().equals(test.getID())
			&& getIDReference().equals(test.getIDReference())
			&& getQualifier().equals(test.getQualifier()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getValue().hashCode();
		result = 7 * result + getID().hashCode();
		result = 7 * result + getIDReference().hashCode();
		result = 7 * result + getQualifier().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the child text of the description. The underlying XOM method which retrieves the child text returns
	 * an empty string if not found.
	 */
	public String getValue() {
		return (getXOMElement().getValue());
	}
	
	/**
	 * Accessor for the ID
	 */
	public String getID() {
		return (getAttributeValue(ID_NAME, getDDMSVersion().getNtkNamespace()));
	}
	
	/**
	 * Accessor for the IDReference
	 */
	public String getIDReference() {
		return (getAttributeValue(ID_REFERENCE_NAME, getDDMSVersion().getNtkNamespace()));
	}
	
	/**
	 * Accessor for the qualifier
	 */
	public String getQualifier() {
		return (getAttributeValue(QUALIFIER_NAME, getDDMSVersion().getNtkNamespace()));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Accessor for whether this is an NMTOKEN-based string
	 */
	private boolean isTokenBased() {
		return (_isTokenBased);
	}
	
	/**
	 * Abstract Builder for this DDMS component.
	 * 
	 * <p>Builders which are based upon this abstract class should implement the commit() method, returning the
	 * appropriate concrete object type.</p>
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static abstract class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 7824644958681123708L;
		private String _value;
		private String _id;
		private String _idReference;
		private String _qualifier;
		private SecurityAttributes.Builder _securityAttributes;
		
		
		/**
		 * Empty constructor
		 */
		protected Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		protected Builder(AbstractNtkString string) {
			setValue(string.getValue());
			setID(string.getID());
			setIDReference(string.getIDReference());
			setQualifier(string.getQualifier());
			setSecurityAttributes(new SecurityAttributes.Builder(string.getSecurityAttributes()));
		}
		
		/**
		 * Helper method to determine if any values have been entered for this producer.
		 * 
		 * @return true if all values are empty
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getValue())
				&& Util.isEmpty(getID())
				&& Util.isEmpty(getIDReference())
				&& Util.isEmpty(getQualifier())
				&& getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the child text.
		 */
		public String getValue() {
			return _value;
		}

		/**
		 * Builder accessor for the child text.
		 */
		public void setValue(String value) {
			_value = value;
		}

		/**
		 * Builder accessor for the id
		 */
		public String getID() {
			return _id;
		}

		/**
		 * Builder accessor for the id
		 */
		public void setID(String id) {
			_id = id;
		}

		/**
		 * Builder accessor for the idReference
		 */
		public String getIDReference() {
			return _idReference;
		}

		/**
		 * Builder accessor for the idReference
		 */
		public void setIDReference(String idReference) {
			_idReference = idReference;
		}

		/**
		 * Builder accessor for the qualifier
		 */
		public String getQualifier() {
			return _qualifier;
		}

		/**
		 * Builder accessor for the qualifier
		 */
		public void setQualifier(String qualifier) {
			_qualifier = qualifier;
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