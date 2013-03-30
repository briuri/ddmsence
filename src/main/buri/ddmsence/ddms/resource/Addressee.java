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

import nu.xom.Element;
import buri.ddmsence.AbstractTaskingRole;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:addressee.
 * 
 * {@table.header Nested Elements}
 * <u>ddms:organization</u>: The organization who is the addressee (0-1, optional), implemented as an
 * {@link Organization}<br />
 * <u>ddms:person</u>: the person who is the addressee (0-1, optional), implemented as a {@link Person}<br />
 * Only one of the nested entities can appear in an addressee element.
 * {@table.footer}
 * 
 * {@table.header Attributes}
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are required.
 * {@table.footer}
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class Addressee extends AbstractTaskingRole {

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Addressee(Element element) throws InvalidDDMSException {
		super(element);
	}

	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param entity the actual entity who is the addressee (required)
	 * @param securityAttributes any security attributes (required)
	 */
	public Addressee(IRoleEntity entity, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		super(Addressee.getName(DDMSVersion.getCurrentVersion()), entity, securityAttributes);
	}

	/**
	 * Validates the component.
	 * 
	 * {@table.header Rules}
	 * <li>The qualified name of the element is correct.</li>
	 * <li>Only 0-1 persons or organizations exist.</li>
	 * {@table.footer}
	 * 
	 * @see AbstractTaskingRole#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Addressee.getName(getDDMSVersion()));
		Util.requireBoundedChildCount(getXOMElement(), Organization.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedChildCount(getXOMElement(), Person.getName(getDDMSVersion()), 0, 1);
		super.validate();
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Addressee))
			return (false);
		return (true);
	}

	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("addressee");
	}

	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder extends AbstractTaskingRole.Builder {
		private static final long serialVersionUID = 4565840434345629470L;

		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Addressee info) {
			super(info);
		}

		/**
		 * @see IBuilder#commit()
		 */
		public Addressee commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Addressee(commitSelectedEntity(), getSecurityAttributes().commit()));
		}
	}
}
