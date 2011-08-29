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
import buri.ddmsence.ddms.AbstractProducerRole;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IProducerEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a ddms:publisher element.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:organization, ddms:person, ddms:service, or ddms:unknown</u>: the entity which fulfills this role (exactly 1 required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#publisher<br />
 * <u>Description</u>: This category is used to tag the identification of the entity responsible for releasing the data 
 * asset---the entity primarily responsible for the intellectual content of the product. It is intended that this category 
 * apply whenever applicable to an organization, as opposed to a person.<br />
 * <u>Obligation</u>: At least one of the four producerTypes is required.<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ICISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class Publisher extends AbstractProducerRole {
	
	/** The element name of this component */
	public static final String NAME = "publisher";

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
	 * @param producerEntity the actual entity fulfilling this role
	 * @param securityAttributes any security attributes (optional)
	 */
	public Publisher(IProducerEntity producerEntity, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(NAME, producerEntity, securityAttributes);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractProducer#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		return (super.equals(obj) && (obj instanceof Publisher));
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
		public Builder() {
			super(Publisher.NAME);
		}
		
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
			return (isEmpty() ? null : new Publisher(commitSelectedEntity(), getSecurityAttributes().commit()));
		}
	}
}
