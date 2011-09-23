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
package buri.ddmsence.ddms.resource;

import nu.xom.Element;
import buri.ddmsence.AbstractProducerRole;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:publisher.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:organization</u>: The organization who is in this role (0-1, optional)<br />
 * <u>ddms:person</u>: the person who is in this role (0-1, optional)<br />
 * <u>ddms:service</u>: The web service who is in this role (0-1, optional)<br />
 * <u>ddms:unknown</u>: The unknown entity who is in this role (0-1, optional)<br />
 * Only one of the nested entities can appear in this element.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class Publisher extends AbstractProducerRole {

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Publisher(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param entity the actual entity fulfilling this role
	 * @param pocType the ISM POCType for this producer (optional, starting in DDMS 4.0)
	 * @param securityAttributes any security attributes (optional)
	 */
	public Publisher(IRoleEntity entity, String pocType, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(Publisher.getName(DDMSVersion.getCurrentVersion()), entity, pocType, securityAttributes);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractProducerRole#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Publisher.getName(getDDMSVersion()));
		super.validate();
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		return (super.equals(obj) && (obj instanceof Publisher));
	}	
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("publisher");
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder extends AbstractProducerRole.Builder {
		private static final long serialVersionUID = 4565840434345629470L;

		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Publisher producer) {
			super(producer);
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Publisher commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Publisher(commitSelectedEntity(), getPocType(), 
				getSecurityAttributes().commit()));
		}
	}
}
