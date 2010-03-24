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
package buri.ddmsence.ddms.summary;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:virtualCoverage.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p><ul>
 * <li>If address is specified, protocol must not be empty.</li>
 * </ul>
 * 
 * <p>
 * DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A virtualCoverage element can be used with no attributes.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:address</u>: a computer or telecommunications network address, or a network name or locale. (optional).<br />
 * <u>ddms:protocol</u>: the type of rules for data transfer that apply to the Virtual Address (can stand alone, but
 * should be used if address is provided)<br />
 * This class is also decorated with ICISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#VirtualCoverage<br />
 * <u>Description</u>: The subject-matter coverage of a publication in terms of one or more virtual addresses.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2003-05-06<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class VirtualCoverage extends AbstractBaseComponent {

	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/** The element name of this component */
	public static final String NAME = "virtualCoverage";
	
	private static final String ADDRESS_NAME = "address";
	private static final String PROTOCOL_NAME = "protocol";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public VirtualCoverage(Element element) throws InvalidDDMSException {
		_cachedSecurityAttributes = new SecurityAttributes(element);
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param address the virtual address (optional)
	 * @param protocol the network protocol (optional, should be used if address is provided)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public VirtualCoverage(String address, String protocol, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		Element element = Util.buildDDMSElement(VirtualCoverage.NAME, null);
		Util.addDDMSAttribute(element, ADDRESS_NAME, address);
		Util.addDDMSAttribute(element, PROTOCOL_NAME, protocol);
		_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null) : securityAttributes);
		_cachedSecurityAttributes.addTo(element);
		setXOMElement(element, true);
	}

	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>If an address is provided, the protocol is required and must not be empty.</li>
	 * <li>The SecurityAttributes are valid.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	public void validate() throws InvalidDDMSException {
		super.validate();
		if (!Util.isEmpty(getAddress()))
			Util.requireDDMSValue(PROTOCOL_NAME, getProtocol());
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("virtual.address", getAddress(), false));
		html.append(buildHTMLMeta("virtual.networkProtocol", getProtocol(), false));
		html.append(getSecurityAttributes().toHTML("virtual"));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Virtual address", getAddress(), false));
		text.append(buildTextLine("Network Protocol", getProtocol(), false));
		text.append(getSecurityAttributes().toText("Virtual"));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof VirtualCoverage))
			return (false);
		VirtualCoverage test = (VirtualCoverage) obj;
		return (getAddress().equals(test.getAddress()) &&
			getProtocol().equals(test.getProtocol()) &&
			getSecurityAttributes().equals(test.getSecurityAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getAddress().hashCode();
		result = 7 * result + getProtocol().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the address attribute (optional)
	 */
	public String getAddress() {
		return (getAttributeValue(ADDRESS_NAME)); 
	}
	
	/**
	 * Accessor for the protocol attribute (optional, should be used if address is supplied)
	 */
	public String getProtocol() {
		return (getAttributeValue(PROTOCOL_NAME)); 
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
} 