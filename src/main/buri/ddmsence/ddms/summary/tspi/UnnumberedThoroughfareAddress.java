/* Copyright 2010 - 2019 by Brian Uri!
   
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
   home page is located at https://ddmsence.urizone.net/
 */
package buri.ddmsence.ddms.summary.tspi;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractTspiAddress;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of tspi:UnnumberedThoroughfareAddress.
 * <br /><br />
 * {@ddms.versions 00001}
 * 
 * <p>For the initial support of DDMS 5.0 TSPI addresses, the DDMSence component will only return the raw XML of
 * an address. The addressing specification is incredibly complex and multi-layered, and it is unclear how much
 * value full-fledged classes would have. As use cases refine and more organizations adopt DDMS 5.0, the components
 * can be revisited to provide more value-add.</p>
 *   
 * {@table.header History}
 * 		None.
 * {@table.footer}
 * {@table.header Nested Elements}
 * 		None.
 * {@table.footer}
 * {@table.header Attributes}
 * 		{@child.info action|0..1|00001}
 * {@table.footer}
 * {@table.header Validation Rules}
 * 		{@ddms.rule Component must not be used before the DDMS version in which it was introduced.|Error|11111}
 * 		{@ddms.rule The qualified name of this element must be correct.|Error|11111}
 * 		{@ddms.rule If set, action attribute must be a valid token.|Error|11111}
 * 		<p>No additional validation is done on the TSPI address at this time.</p>
 * {@table.footer}
 * 
 * @author Brian Uri!
 * @since 2.2.0
 */
public final class UnnumberedThoroughfareAddress extends AbstractTspiAddress {

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public UnnumberedThoroughfareAddress(Element element) throws InvalidDDMSException {
		super(element);
	}

	/**
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getNamespace(), UnnumberedThoroughfareAddress.getName(getDDMSVersion()));
		super.validate();
	}

	/**
	 * Builder for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("UnnumberedThoroughfareAddress");
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof UnnumberedThoroughfareAddress))
			return (false);
		return (true);
	}

	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.2.0
	 */
	public static class Builder extends AbstractTspiAddress.Builder {
		private static final long serialVersionUID = 7750664735441105296L;

		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(UnnumberedThoroughfareAddress address) {
			super(address);
		}

		/**
		 * @see IBuilder#commit()
		 */
		public UnnumberedThoroughfareAddress commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			return (new UnnumberedThoroughfareAddress(commitXml()));
		}
	}
}