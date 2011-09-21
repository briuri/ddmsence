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
import buri.ddmsence.AbstractTaskingRole;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a ddms:requesterInfo element.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:person</u>: the person who requested the production of this resource (0-1, optional)<br />
 * <u>ddms:organization</u>: The organization who requested the production of this resource (0-1, optional)<br />
 * Only one of the nested entities can appear in a requesterInfo element.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: The person or organization who requested the production of this resource.<br />
 * <u>Obligation</u>: At least 1 requesterInfo is mandatory.<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RequesterInfo extends AbstractTaskingRole {
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public RequesterInfo(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param entity the actual entity who is the requester (required)
	 * @param securityAttributes any security attributes (required)
	 */
	public RequesterInfo(IRoleEntity entity, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(RequesterInfo.getName(DDMSVersion.getCurrentVersion()), entity, securityAttributes);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractTaskingRole#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), RequesterInfo.getName(getDDMSVersion()));
		super.validate();
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof RequesterInfo))
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
		return ("requesterInfo");
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
		public Builder(RequesterInfo info) {
			super(info);		
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public RequesterInfo commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new RequesterInfo(commitSelectedEntity(), getSecurityAttributes().commit()));
		}
	}
}
