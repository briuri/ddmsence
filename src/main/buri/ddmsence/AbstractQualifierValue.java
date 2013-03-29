/* Copyright 2010 - 2013 by Brian Uri!
   
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
import buri.ddmsence.util.Util;

/**
 * Base class for DDMS elements which have a qualifier/value attribute, such as ddms:Identifier and ddms:source.
 * 
 * <p>In DDMS 5.0, ddms:countryCode and ddms:subdivisionCode have new attribute names, codespace/code. These are
 * treated as aliases to qualifier/value.</p>
 * 
 * <p> Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set
 * before the component is used. </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public abstract class AbstractQualifierValue extends AbstractBaseComponent {

	private boolean _useClassicAttributeNames = true;

	private static final String QUALIFIER_NAME = "qualifier";
	private static final String VALUE_NAME = "value";

	private static final String CODESPACE_NAME = "codespace";
	private static final String CODE_NAME = "code";

	/**
	 * This implicit superconstructor does nothing.
	 */
	protected AbstractQualifierValue() throws InvalidDDMSException {}

	/**
	 * Base constructor
	 * 
	 * @param element the XOM element representing this component
	 * @param useClassicAttributeNames true for "qualifier/value" and false for "codespace/code".
	 */
	protected AbstractQualifierValue(Element element, boolean useClassicAttributeNames) throws InvalidDDMSException {
		_useClassicAttributeNames = useClassicAttributeNames;
		try {
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param name the name of the element without a prefix
	 * @param qualifier the value of the qualifier attribute
	 * @param value the value of the value attribute
	 * @param validateNow true to validate the component immediately. Because Source entities have additional fields
	 *        they should not be validated in the superconstructor.
	 * @param useClassicAttributeNames true for "qualifier/value" and false for "codespace/code".
	 */
	protected AbstractQualifierValue(String name, String qualifier, String value, boolean validateNow,
		boolean useClassicAttributeNames) throws InvalidDDMSException {
		_useClassicAttributeNames = useClassicAttributeNames;
		try {
			Element element = Util.buildDDMSElement(name, null);
			Util.addDDMSAttribute(element, getQualifierName(), qualifier);
			Util.addDDMSAttribute(element, getValueName(), value);
			setXOMElement(element, validateNow);

		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractQualifierValue))
			return (false);
		AbstractQualifierValue test = (AbstractQualifierValue) obj;
		return (getQualifier().equals(test.getQualifier()) && getValue().equals(test.getValue()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getQualifier().hashCode();
		result = 7 * result + getValue().hashCode();
		return (result);
	}

	/**
	 * Accessor for the name of the qualifier attribute
	 */
	public String getQualifierName() {
		return (_useClassicAttributeNames ? QUALIFIER_NAME : CODESPACE_NAME);
	}

	/**
	 * Accessor for the nameof the value attribute
	 */
	public String getValueName() {
		return (_useClassicAttributeNames ? VALUE_NAME : CODE_NAME);
	}

	/**
	 * Accessor for the value of the qualifier attribute (referred to in some DDMS 5.0 components as a "codespace")
	 */
	public String getQualifier() {
		return (getAttributeValue(getQualifierName()));
	}

	/**
	 * Accessor for the value of the value attribute (referred to in some DDMS 5.0 components as a "code")
	 */
	public String getValue() {
		return (getAttributeValue(getValueName()));
	}

	/**
	 * Abstract Builder for this DDMS component.
	 * 
	 * <p>Builders which are based upon this abstract class should implement the commit() method, returning the
	 * appropriate concrete object type.</p>
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static abstract class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 5630463057657652800L;
		private String _qualifier;
		private String _value;

		/**
		 * Empty constructor
		 */
		protected Builder() {}

		/**
		 * Constructor which starts from an existing component.
		 */
		protected Builder(AbstractQualifierValue qualifierValue) {
			setQualifier(qualifierValue.getQualifier());
			setValue(qualifierValue.getValue());
		}

		/**
		 * Builder accessor for the qualifier attribute.
		 */
		public String getQualifier() {
			return _qualifier;
		}

		/**
		 * Builder accessor for the qualifier attribute.
		 */
		public void setQualifier(String qualifier) {
			_qualifier = qualifier;
		}

		/**
		 * Builder accessor for the value attribute.
		 */
		public String getValue() {
			return _value;
		}

		/**
		 * Builder accessor for the value attribute.
		 */
		public void setValue(String value) {
			_value = value;
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getQualifier()) && Util.isEmpty(getValue()));
		}
	}
}