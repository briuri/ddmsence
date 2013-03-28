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
package buri.ddmsence.ddms.resource;

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractRoleEntity;
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
 * <li>At least 1 name value must be non-empty. This rule is codified in the schema, starting in DDMS 5.0.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A phone number can be set with no value. This loophole goes away in DDMS 5.0.</li>
 * <li>An email can be set with no value. This loophole goes away in DDMS 5.0.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <p>The ddms:unknown element is new in DDMS 3.0. Attempts to use it with DDMS 2.0 will result in an 
 * UnsupportedVersionException. Its name was changed from "Unknown" to "unknown" in DDMS 4.0.1.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:name</u>: names of the producer (1-many, at least 1 required)<br />
 * <u>ddms:phone</u>: phone numbers of the producer (0-many optional)<br />
 * <u>ddms:email</u>: email addresses of the producer (0-many optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link ExtensibleAttributes}</u>
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Unknown extends AbstractRoleEntity {
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Unknown(Element element) throws InvalidDDMSException {
		super(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 */
	public Unknown(List<String> names, List<String> phones, List<String> emails)
		throws InvalidDDMSException {
		this(names, phones, emails, null);
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 * 
	 * @param names an ordered list of names
	 * @param phones an ordered list of phone numbers
	 * @param emails an ordered list of email addresses
	 * @param extensions extensible attributes (optional)
	 */
	public Unknown(List<String> names, List<String> phones, List<String> emails,
		ExtensibleAttributes extensions) throws InvalidDDMSException {
		super(Unknown.getName(DDMSVersion.getCurrentVersion()), names, phones, emails, extensions, true);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>This component cannot be used until DDMS 3.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractRoleEntity#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Unknown.getName(getDDMSVersion()));
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("3.0");
		
		super.validate();
	}
		
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		return (super.equals(obj) && (obj instanceof Unknown));
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return (version.isAtLeast("4.0.1") ? "unknown" : "Unknown");
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractRoleEntity.Builder {
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
		public Builder(Unknown unknown) {
			super(unknown);
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Unknown commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Unknown(getNames(), getPhones(), getEmails(), 
				getExtensibleAttributes().commit()));
		}
	}
} 