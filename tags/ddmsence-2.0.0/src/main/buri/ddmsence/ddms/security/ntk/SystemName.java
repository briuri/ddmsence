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
package buri.ddmsence.ddms.security.ntk;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractNtkString;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ntk:AccessSystemName.
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ntk:id</u>: A unique XML identifier (optional)<br />
 * <u>ntk:IDReference</u>: A cross-reference to a unique identifier (optional)<br />
 * <u>ntk:qualifier</u>: A user-defined property within an element for general purpose processing used with block 
 * objects to provide supplemental information over and above that conveyed by the element name (optional)<br />
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class SystemName extends AbstractNtkString {
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SystemName(Element element) throws InvalidDDMSException {
		super(true, element);
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param value the value of the element's child text
	 * @param id the NTK ID (optional)
	 * @param idReference a reference to an NTK ID (optional)
	 * @param qualifier an NTK qualifier (optional)
	 * @param securityAttributes the security attributes
	 */
	public SystemName(String value, String id, String idReference, String qualifier,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		super(true, SystemName.getName(DDMSVersion.getCurrentVersion()), value, id, idReference, qualifier,
			securityAttributes, true);
	}
		
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * </td></tr></table>
	 *  
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getNamespace(), SystemName.getName(getDDMSVersion()));
		super.validate();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, "systemName", suffix);
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix, getValue()));
		text.append(buildOutput(isHTML, localPrefix + ".id", getID()));
		text.append(buildOutput(isHTML, localPrefix + ".idReference", getIDReference()));
		text.append(buildOutput(isHTML, localPrefix + ".qualifier", getQualifier()));
		text.append(getSecurityAttributes().getOutput(isHTML, localPrefix + "."));
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
		return ("AccessSystemName");
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof SystemName))
			return (false);
		return (true);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder extends AbstractNtkString.Builder {
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
		public Builder(SystemName systemName) {
			super(systemName);
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public SystemName commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new SystemName(getValue(), getID(), getIDReference(), getQualifier(),
				getSecurityAttributes().commit()));
		}
	}
} 