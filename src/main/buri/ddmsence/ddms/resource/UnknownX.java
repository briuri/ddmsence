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

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractProducerEntity;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a ddms:unknown element.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>At least 1 name value must be non-empty.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A phone number can be set with no value.</li>
 * <li>An email can be set with no value.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <p>The ddms:Unknown element is new in v3.0. Attempts to use it with DDMS v2.0 will result in an 
 * UnsupportedVersionException.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:name</u>: names of the producer (1-many, at least 1 required)<br />
 * <u>ddms:phone</u>: phone numbers of the producer (0-many optional)<br />
 * <u>ddms:email</u>: email addresses of the producer (0-many optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * In DDMS 3.0, this component can be decorated with optional {@link ExtensibleAttributes}.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Unknown<br />
 * <u>Description</u>: Information about an unknown producer entity fulfilling some producer role.<br />
 * <u>Obligation</u>: At least one of the four producerTypes is required.<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class UnknownX extends AbstractProducerEntity {

	/** The element name of this component */
	public static final String NAME = "Unknown";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param parentType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public UnknownX(String parentType, Element element) throws InvalidDDMSException {
		super(parentType, element);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param parentType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 */
	public UnknownX(String parentType, List<String> names, List<String> phones, List<String> emails)
		throws InvalidDDMSException {
		this(parentType, names, phones, emails, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param parentType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param extensions extensible attributes (optional)
	 */
	public UnknownX(String parentType, List<String> names, List<String> phones, List<String> emails,
		ExtensibleAttributes extensions) throws InvalidDDMSException {
		super(parentType, UnknownX.NAME, names, phones, emails, extensions, true);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>The DDMS Version must be 3.0 or higher.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractProducer#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
		// Should be reviewed as additional versions of DDMS are supported.
		if (DDMSVersion.isCompatibleWithVersion("2.0", getXOMElement()))
			throw new InvalidDDMSException("The ddms:Unknown element cannot be used in DDMS 2.0.");
	}
		
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		return (super.equals(obj) && (obj instanceof UnknownX));
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractProducerEntity.Builder {
		private static final long serialVersionUID = -2278534009019179572L;

		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(UnknownX unknown) {
			super(unknown);
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public UnknownX commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new UnknownX(getParentType(), getNames(), getPhones(), getEmails(), 
				getExtensibleAttributes().commit()));
		}
	}
} 