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

import buri.ddmsence.util.Util;

/**
 * Base class for DDMS elements which consist of simple child text, possibly decorated with attributes, such as
 * ddms:description, ddms:title, and ddms:subtitle.
 * 
 * <p> Extensions of this class are generally expected to be immutable, and the underlying XOM element MUST be set
 * before the component is used. </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public abstract class AbstractSimpleString extends AbstractBaseComponent {
	
	/**
	 * This implicit superconstructor does nothing.
	 */
	protected AbstractSimpleString() throws InvalidDDMSException {}
	
	/**
	 * Constructor which builds from raw data. The object is not validated at this point -- attributes may be required
	 * which have not been created yet.
	 * 
	 * @param name the name of the element without a prefix
	 * @param value the value of the element's child text
	 */
	protected AbstractSimpleString(String name, String value) throws InvalidDDMSException {
		try {
			setXOMElement(Util.buildDDMSElement(name, value), false);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof AbstractSimpleString))
			return (false);
		AbstractSimpleString test = (AbstractSimpleString) obj;
		return (getValue().equals(test.getValue()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getValue().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the child text of the description. The underlying XOM method which retrieves the child text returns
	 * an empty string if not found.
	 */
	public String getValue() {
		return (getXOMElement().getValue()); 
	}
}