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

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.AbstractProducerRole;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of a ddms:recordKeeper element.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The recordKeeperID must not be empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:recordKeeperID</u>: A unique identifier for the Record Keeper (exactly 1 required)<br />
 * <u>ddms:organization</u>: The organization which acts as the record keeper (exactly 1 required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: The administrative entity, unit, office, responsible for the custody and ongoing 
 * management of the records during their active business use.<br />
 * <u>Obligation</u>: Optional.<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RecordKeeper extends AbstractBaseComponent {

	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private Organization _cachedOrganization;
	
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
					_cachedOrganization = new Organization(organizationElement);
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
	public RecordKeeper(String recordKeeperID, Organization organization)
		throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(RecordKeeper.getName(DDMSVersion.getCurrentVersion()), null);
			if (!Util.isEmpty(recordKeeperID))
				element.appendChild(Util.buildDDMSElement(RECORD_KEEPER_ID_NAME, recordKeeperID));
			if (organization != null)
				element.appendChild(organization.getXOMElementCopy());
			_cachedOrganization = organization;
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
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
	 * <li>The organization uses the same version of DDMS as this element.</li>
	 * <li>This component cannot exist until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractProducerRole#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), RecordKeeper.getName(getDDMSVersion()));
		Util.requireDDMSValue("record keeper ID", getRecordKeeperID());
		Util.requireDDMSValue("organization", getOrganization());
		Util.requireCompatibleVersion(this, getOrganization());
		
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0"))
			throw new InvalidDDMSException("The ddms:" + RecordKeeper.getName(getDDMSVersion()) + " element cannot be used until DDMS 4.0 or later.");
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof RecordKeeper))
			return (false);
		RecordKeeper test = (RecordKeeper) obj;
		return (getRecordKeeperID().equals(test.getRecordKeeperID())
			&& getOrganization().equals(test.getOrganization()));		
	}	
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getRecordKeeperID().hashCode();
		result = 7 * result + getOrganization().hashCode();
		return (result);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		return (toHTML(""));
	}

	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		return (toText(""));
	}
	
	/**
	 * Outputs to HTML with a prefix at the beginning of each meta tag.
	 * 
	 * @param prefix the prefix to add
	 * @return the HTML output
	 */
	public String toHTML(String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta(prefix + RECORD_KEEPER_ID_NAME, getRecordKeeperID(), true));
		html.append(getOrganization().toHTML(prefix));
		return (html.toString());
	}
	
	/**
	 * Outputs to Text with a prefix at the beginning of each line.
	 * 
	 * @param prefix the prefix to add
	 * @return the Text output
	 */
	public String toText(String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine(prefix + RECORD_KEEPER_ID_NAME, getRecordKeeperID(), true));
		text.append(getOrganization().toText(prefix));
		return (text.toString());	
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
		return (_cachedOrganization);
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
