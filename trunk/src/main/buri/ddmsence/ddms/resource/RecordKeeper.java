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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractProducerRole;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:recordKeeper.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The recordKeeperID must not be empty. This rule is codified in the schema, starting in DDMS 5.0.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:recordKeeperID</u>: A unique identifier for the Record Keeper (exactly 1 required)<br />
 * <u>ddms:organization</u>: The organization which acts as the record keeper (exactly 1 required), implemented as an
 * {@link Organization}<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RecordKeeper extends AbstractBaseComponent {

	private Organization _organization = null;
	
	private static final String RECORD_KEEPER_ID_NAME = "recordKeeperID";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public RecordKeeper(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("element", element);
			if (element.getChildElements().size() > 1) {
				Element organizationElement = element.getChildElements().get(1);
				if (organizationElement != null)
					_organization = new Organization(organizationElement);
			}
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param recordKeeperID a unique ID for the organization (required)
	 * @param organization the organization acting as record keeper (required)
	 */
	public RecordKeeper(String recordKeeperID, Organization organization) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(RecordKeeper.getName(DDMSVersion.getCurrentVersion()), null);
			if (!Util.isEmpty(recordKeeperID))
				element.appendChild(Util.buildDDMSElement(RECORD_KEEPER_ID_NAME, recordKeeperID));
			if (organization != null)
				element.appendChild(organization.getXOMElementCopy());
			_organization = organization;
			setXOMElement(element, true);
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
	 * <li>The recordKeeperID exists.</li>
	 * <li>The organization exists.</li>
	 * <li>Exactly 1 organization exists.</li>
	 * <li>This component cannot exist until DDMS 4.0.1 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractProducerRole#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), RecordKeeper.getName(getDDMSVersion()));
		Util.requireDDMSValue("record keeper ID", getRecordKeeperID());
		Util.requireDDMSValue("organization", getOrganization());
		Util.requireBoundedChildCount(getXOMElement(), Organization.getName(getDDMSVersion()), 1, 1);

		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0.1");
		
		super.validate();
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof RecordKeeper))
			return (false);
		RecordKeeper test = (RecordKeeper) obj;
		return (getRecordKeeperID().equals(test.getRecordKeeperID()));
	}	
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getRecordKeeperID().hashCode();
		return (result);
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix + ".");
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix + RECORD_KEEPER_ID_NAME, getRecordKeeperID()));
		text.append(getOrganization().getOutput(isHTML, localPrefix, ""));
		return (text.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.add(getOrganization());
		return (list);
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("recordKeeper");
	}
	
	/**
	 * Accessor for the recordKeeperID
	 */
	public String getRecordKeeperID() {
		return (Util.getFirstDDMSChildValue(getXOMElement(), RECORD_KEEPER_ID_NAME));
	}
	
	/**
	 * Accessor for the organization
	 */
	public Organization getOrganization() {
		return (_organization);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 4565840434345629470L;
		private String _recordKeeperID;
		private Organization.Builder _organization;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(RecordKeeper keeper) {
			setRecordKeeperID(keeper.getRecordKeeperID());
			setOrganization(new Organization.Builder(keeper.getOrganization()));			
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public RecordKeeper commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new RecordKeeper(getRecordKeeperID(), getOrganization().commit()));
		}

		/**
		 * Helper method to determine if any values have been entered.
		 * 
		 * @return true if all values are empty
		 */
		public boolean isEmpty() {	
			return (getOrganization().isEmpty()
				&& Util.isEmpty(getRecordKeeperID()));
		}
		
		/**
		 * Builder accessor for the recordKeeperID
		 */
		public String getRecordKeeperID() {
			return _recordKeeperID;
		}

		/**
		 * Builder accessor for the recordKeeperID
		 */
		public void setRecordKeeperID(String recordKeeperID) {
			_recordKeeperID = recordKeeperID;
		}
		
		/**
		 * Builder accessor for the organization builder
		 */
		public Organization.Builder getOrganization() {
			if (_organization == null) {
				_organization = new Organization.Builder();
			}
			return _organization;
		}

		/**
		 * Builder accessor for the organization builder
		 */
		public void setOrganization(Organization.Builder organization) {
			_organization = organization;
		}
	}
}
