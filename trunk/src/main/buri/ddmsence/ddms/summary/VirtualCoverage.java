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
package buri.ddmsence.ddms.summary;

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
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
 * <p>Starting in DDMS 5.0, the ddms attributes have moved into the virt namespace.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:address</u>: a computer or telecommunications network address, or a network name or locale. (optional).<br />
 * <u>ddms:protocol</u>: the type of rules for data transfer that apply to the Virtual Address (can stand alone, but
 * should be used if address is provided)<br />
 * <u>virt:network</u>
 * <u>ntk:access</u>
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are optional. (starting in DDMS
 * 3.0)
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class VirtualCoverage extends AbstractBaseComponent {

	private SecurityAttributes _securityAttributes = null;

	private static final String ADDRESS_NAME = "address";
	private static final String PROTOCOL_NAME = "protocol";
	private static final String ACCESS_NAME = "access";
	private static final String NETWORK_NAME = "network";

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public VirtualCoverage(Element element) throws InvalidDDMSException {
		try {
			_securityAttributes = new SecurityAttributes(element);
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @deprecated A new constructor was added for DDMS 5.0 to support ntk:access and virt:network. This constructor is
	 *             preserved for backwards compatibility, but may disappear in the next major release.
	 * 
	 * @param address the virtual address (optional)
	 * @param protocol the network protocol (optional, should be used if address is provided)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public VirtualCoverage(String address, String protocol, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		this(address, protocol, null, null, securityAttributes);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param address the virtual address (optional)
	 * @param protocol the network protocol (optional, should be used if address is provided)
	 * @param access an NTK portion access pattern (optional)
	 * @param network a VIRT network name (optional)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public VirtualCoverage(String address, String protocol, String access, String network, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			Element element = Util.buildDDMSElement(VirtualCoverage.getName(version), null);
			if (version.isAtLeast("5.0")) {
				String ntkPrefix = PropertyReader.getPrefix("ntk");
				String ntkNamespace = version.getNtkNamespace();
				String virtPrefix = PropertyReader.getPrefix("virt");
				String virtNamespace = version.getVirtNamespace();
				Util.addAttribute(element, virtPrefix, ADDRESS_NAME, virtNamespace, address);
				Util.addAttribute(element, virtPrefix, PROTOCOL_NAME, virtNamespace, protocol);
				Util.addAttribute(element, ntkPrefix, ACCESS_NAME, ntkNamespace, access);
				Util.addAttribute(element, virtPrefix, NETWORK_NAME, virtNamespace, network);
			}
			else {
				Util.addDDMSAttribute(element, ADDRESS_NAME, address);
				Util.addDDMSAttribute(element, PROTOCOL_NAME, protocol);
			}
			_securityAttributes = SecurityAttributes.getNonNullInstance(securityAttributes);
			_securityAttributes.addTo(element);
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
	 * <li>If an address is provided, the protocol is required and must not be empty.</li>
	 * <li>The SecurityAttributes do not exist until DDMS 3.0 or later.</li>
	 * <li>The access and network attributes cannot be used until DDMS 5.0.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), VirtualCoverage.getName(getDDMSVersion()));
		if (!Util.isEmpty(getAddress()))
			Util.requireDDMSValue(PROTOCOL_NAME, getProtocol());
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("3.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes cannot be applied to this component until DDMS 3.0 or later.");
		}
		if (!getDDMSVersion().isAtLeast("5.0") && !getAccess().isEmpty()) {
			throw new InvalidDDMSException(
				"The ntk:access attribute cannot be applied to this component until DDMS 5.0 or later.");
		}
		if (!getDDMSVersion().isAtLeast("5.0") && !getNetwork().isEmpty()) {
			throw new InvalidDDMSException(
				"The virt:network attribute cannot be applied to this component until DDMS 5.0 or later.");
		}

		super.validate();
	}

	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A completely empty ddms:virtualCoverage element was found.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getAddress()) && Util.isEmpty(getProtocol()) && Util.isEmpty(getAccess())
			&& Util.isEmpty(getNetwork()))
			addWarning("A completely empty ddms:virtualCoverage element was found.");
		super.validateWarnings();
	}

	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix + ".");
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix + ADDRESS_NAME, getAddress()));
		text.append(buildOutput(isHTML, localPrefix + PROTOCOL_NAME, getProtocol()));
		text.append(buildOutput(isHTML, localPrefix + ACCESS_NAME, getAccess()));
		text.append(buildOutput(isHTML, localPrefix + NETWORK_NAME, getNetwork()));
		text.append(getSecurityAttributes().getOutput(isHTML, localPrefix));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof VirtualCoverage))
			return (false);
		VirtualCoverage test = (VirtualCoverage) obj;
		return (getAddress().equals(test.getAddress()) 
			&& getProtocol().equals(test.getProtocol())
			&& getAccess().equals(test.getAccess())
			&& getNetwork().equals(test.getNetwork()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getAddress().hashCode();
		result = 7 * result + getProtocol().hashCode();
		result = 7 * result + getAccess().hashCode();
		result = 7 * result + getNetwork().hashCode();
		return (result);
	}

	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("virtualCoverage");
	}

	/**
	 * Accessor for the address attribute (optional)
	 */
	public String getAddress() {
		if (getDDMSVersion().isAtLeast("5.0"))
			return (getAttributeValue(ADDRESS_NAME, getDDMSVersion().getVirtNamespace()));
		return (getAttributeValue(ADDRESS_NAME));
	}

	/**
	 * Accessor for the protocol attribute (optional, should be used if address is supplied)
	 */
	public String getProtocol() {
		if (getDDMSVersion().isAtLeast("5.0"))
			return (getAttributeValue(PROTOCOL_NAME, getDDMSVersion().getVirtNamespace()));
		return (getAttributeValue(PROTOCOL_NAME));
	}

	/**
	 * Accessor for the access attribute (optional)
	 */
	public String getAccess() {
		return (getAttributeValue(ACCESS_NAME, getDDMSVersion().getNtkNamespace()));
	}
	
	/**
	 * Accessor for the network attribute (optional)
	 */
	public String getNetwork() {
		return (getAttributeValue(NETWORK_NAME, getDDMSVersion().getVirtNamespace()));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_securityAttributes);
	}

	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 2986952678400201045L;
		private String _access;
		private String _address;
		private String _protocol;
		private String _network;
		private SecurityAttributes.Builder _securityAttributes;

		/**
		 * Empty constructor
		 */
		public Builder() {}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(VirtualCoverage coverage) {
			setAddress(coverage.getAddress());
			setProtocol(coverage.getProtocol());
			setAccess(coverage.getAccess());
			setNetwork(coverage.getNetwork());
			setSecurityAttributes(new SecurityAttributes.Builder(coverage.getSecurityAttributes()));
		}

		/**
		 * @see IBuilder#commit()
		 */
		public VirtualCoverage commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new VirtualCoverage(getAddress(), getProtocol(), getAccess(), getNetwork(),
				getSecurityAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getAddress()) && Util.isEmpty(getProtocol()) && Util.isEmpty(getAccess())
				&& Util.isEmpty(getNetwork()) && getSecurityAttributes().isEmpty());
		}

		/**
		 * Builder accessor for the address attribute
		 */
		public String getAddress() {
			return _address;
		}

		/**
		 * Builder accessor for the address attribute
		 */
		public void setAddress(String address) {
			_address = address;
		}

		/**
		 * Builder accessor for the protocol attribute
		 */
		public String getProtocol() {
			return _protocol;
		}

		/**
		 * Builder accessor for the protocol attribute
		 */
		public void setProtocol(String protocol) {
			_protocol = protocol;
		}
		
		/**
		 * Builder accessor for the access attribute
		 */
		public String getAccess() {
			return _access;
		}

		/**
		 * Builder accessor for the access attribute
		 */
		public void setAccess(String access) {
			_access = access;
		}

		/**
		 * Builder accessor for the network attribute
		 */
		public String getNetwork() {
			return _network;
		}

		/**
		 * Builder accessor for the network attribute
		 */
		public void setNetwork(String network) {
			_network = network;
		}
		
		/**
		 * Builder accessor for the Security Attributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}

		/**
		 * Builder accessor for the Security Attributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}
	}
}