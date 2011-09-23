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

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractSimpleString;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:nonStateActor.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A nonStateActor element can be used without any child text.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:order</u>: specifies a user-defined order of an element within the given document (optional)<br />
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class NonStateActor extends AbstractSimpleString {
	
	private static final String ORDER_NAME = "order";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public NonStateActor(Element element) throws InvalidDDMSException {
		super(element, true);
		
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param value the value of the description child text
	 * @param order the order of this actor
	 * @param securityAttributes any security attributes (classification and ownerProducer are optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public NonStateActor(String value, Integer order, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(NonStateActor.getName(DDMSVersion.getCurrentVersion()), value, securityAttributes, false);
		try {
			if (order != null)
				Util.addDDMSAttribute(getXOMElement(), ORDER_NAME, order.toString());
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
	 * <li>Does not validate the value of the order attribute (this is done at the Resource level).</li>
	 * </td></tr></table>
	 *  
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		// Do not call super.validate(), because securityAttributes are optional.
		Util.requireDDMSQName(getXOMElement(), NonStateActor.getName(getDDMSVersion()));
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A ddms:nonStateActor element was found with no value.</li>
	 * <li>Include any validation warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getValue()))
			addWarning("A ddms:" + getName() + " element was found with no value.");
		super.validateWarnings();		
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + "value", getValue(), true));
		text.append(buildOutput(isHTML, prefix + ORDER_NAME, String.valueOf(getOrder()), false));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof NonStateActor))
			return (false);
		NonStateActor test = (NonStateActor) obj;
		return (getOrder().equals(test.getOrder()));		
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getOrder().hashCode();
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
		return ("nonStateActor");
	}
	
	/**
	 * Accessor for the order attribute.
	 */
	public Integer getOrder() {
		String order = getAttributeValue(ORDER_NAME);
		return (Util.isEmpty(order) ? null : Integer.valueOf(order));
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
		private Integer _order;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(NonStateActor actor) {
			super(actor);
			setOrder(actor.getOrder());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public NonStateActor commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new NonStateActor(getValue(), getOrder(), getSecurityAttributes().commit()));
		}

		/**
		 * Builder accessor for the order
		 */
		public Integer getOrder() {
			return _order;
		}

		/**
		 * Builder accessor for the order
		 */
		public void setOrder(Integer order) {
			_order = order;
		}
	}
} 