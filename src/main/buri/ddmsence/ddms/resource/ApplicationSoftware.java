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
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractSimpleString;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:applicationSoftware.
 * 
 * {@table.header Strictness}
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>An applicationSoftware element can be used without any child text. This loophole goes away in DDMS 5.0.</li>
 * </ul>
 * {@table.footer}
 * 
 * {@table.header Attributes}
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are required.
 * {@table.footer}
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class ApplicationSoftware extends AbstractSimpleString {

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ApplicationSoftware(Element element) throws InvalidDDMSException {
		super(element, true);
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param value the value of the child text
	 * @param securityAttributes any security attributes (classification and ownerProducer are required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ApplicationSoftware(String value, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		super(ApplicationSoftware.getName(DDMSVersion.getCurrentVersion()), value, securityAttributes, true);
	}

	/**
	 * Validates the component.
	 * 
	 * {@table.header Rules}
	 * <li>The qualified name of the element is correct.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot be used until DDMS 4.0.1 or later.</li>
	 * {@table.footer}
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), ApplicationSoftware.getName(getDDMSVersion()));

		// Should be reviewed as additional versions of DDMS are supported.
		requireAtLeastVersion("4.0.1");
		super.validate();
	}

	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * {@table.header Rules}
	 * <li>A ddms:applicationSoftware element was found with no child text, through DDMS 4.1.</li>
	 * {@table.footer}
	 */
	protected void validateWarnings() {
		if (!getDDMSVersion().isAtLeast("5.0") && Util.isEmpty(getValue()))
			addWarning("A ddms:applicationSoftware element was found with no value.");
		super.validateWarnings();
	}

	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix);
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix, getValue()));
		text.append(getSecurityAttributes().getOutput(isHTML, localPrefix + "."));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof ApplicationSoftware))
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
		return ("applicationSoftware");
	}

	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder extends AbstractSimpleString.Builder {
		private static final long serialVersionUID = -7348511606867959470L;

		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(ApplicationSoftware software) {
			super(software);
		}

		/**
		 * @see IBuilder#commit()
		 */
		public ApplicationSoftware commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new ApplicationSoftware(getValue(), getSecurityAttributes().commit()));
		}
	}
}