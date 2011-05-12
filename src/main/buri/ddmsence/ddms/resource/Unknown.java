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
package buri.ddmsence.ddms.resource;

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractProducer;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a producer element containing a ddms:Unknown element.
 * 
 * <p>Producers are modeled as "entities fulfilling a role", rather than a "role filled by
 * an entity". Please see {@link AbstractProducer} for details.</p>
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
public final class Unknown extends AbstractProducer {

	/** The element name of this component */
	public static final String NAME = "Unknown";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Unknown(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param producerType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param securityAttributes any security attributes (optional)
	 */
	public Unknown(String producerType, List<String> names, List<String> phones, List<String> emails,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		this(producerType, names, phones, emails, securityAttributes, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param producerType the type of producer this producer entity is fulfilling (i.e. creator or contributor)
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param securityAttributes any security attributes (optional)
	 * @param extensions extensible attributes (optional)
	 */
	public Unknown(String producerType, List<String> names, List<String> phones, List<String> emails,
		SecurityAttributes securityAttributes, ExtensibleAttributes extensions) throws InvalidDDMSException {
		super(producerType, Unknown.NAME, names, phones, emails, securityAttributes, extensions, true);
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
		if (this.getDDMSVersion().equals("2.0"))
			throw new InvalidDDMSException("The ddms:Unknown element cannot be used in DDMS 2.0.");
		String ddmsNamespace = DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace();
		Util.requireDDMSQName(getXOMElement(), ddmsNamespace, getProducerType());
		Util.requireDDMSQName(getEntityElement(), ddmsNamespace, NAME);
	}
		
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		return (super.equals(obj) && (obj instanceof Unknown));
	}
	
	/**
	 * Builder for this DDMS component. The builder should be used when a DDMS record needs to be built up over time,
	 * but validation should not occur until the end. The commit() method attempts to finalize the immutable object
	 * based on the values gathered.
	 * 
	 * <p>The builder approach differs from calling the immutable constructor directly because it treats a Builder
	 * instance with no values provided as "no component" instead of "a component with missing values". For example,
	 * calling a constructor directly with an empty string for a required parameter might throw an InvalidDDMSException,
	 * while calling commit() on a Builder without setting any values would just return null.</p>
	 * 
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractProducer.Builder {
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Unknown unknown) {
			super(unknown);
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. If no values have been provided, a null
		 * instance will be returned instead of a possibly invalid one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public Unknown commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Unknown(getProducerType(), getNames(), getPhones(), getEmails(), 
				getSecurityAttributes().commit(), getExtensibleAttributes().commit()));
		}
	}
} 