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
package buri.ddmsence.ddms;

import nu.xom.Element;
import buri.ddmsence.util.Util;

/**
 * Base class for DDMS elements which have a qualifier/value attribute, such as ddms:Identifier and ddms:source.
 * 
 * <p> Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set
 * before the component is used. </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public abstract class AbstractQualifierValue extends AbstractBaseComponent {

	protected static final String QUALIFIER_NAME = "qualifier";
	protected static final String VALUE_NAME = "value";

	/**
	 * This implicit superconstructor does nothing.
	 */
	protected AbstractQualifierValue() throws InvalidDDMSException {
	}

	/**
	 * Base constructor
	 * 
	 * @param element the XOM element representing this component
	 */
	protected AbstractQualifierValue(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
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
	 * they should not be validated in the superconstructor.
	 */
	protected AbstractQualifierValue(String name, String qualifier, String value, boolean validateNow) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(name, null);
			Util.addDDMSAttribute(element, QUALIFIER_NAME, qualifier);
			Util.addDDMSAttribute(element, VALUE_NAME, value);
			setXOMElement(element, validateNow);
		} catch (InvalidDDMSException e) {
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
	 * Accessor for the value of the qualifier attribute
	 */
	public String getQualifier() {
		return (getAttributeValue(QUALIFIER_NAME));
	}
	
	/**
	 * Accessor for the value of the value attribute
	 */
	public String getValue() {
		return (getAttributeValue(VALUE_NAME));
	}
}