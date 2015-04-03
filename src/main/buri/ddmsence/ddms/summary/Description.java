/* Copyright 2010 - 2015 by Brian Uri!
   
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
package buri.ddmsence.ddms.summary;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractSimpleString;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

import com.google.gson.JsonObject;

/**
 * An immutable implementation of ddms:description.
 *  * <br /><br />
 * {@ddms.versions 11111}
 * 
 * <p></p>
 * 
 * {@table.header History}
 *  	None.
 * {@table.footer}
 * {@table.header Nested Elements}
 * 		None.
 * {@table.footer}
 * {@table.header Attributes}
 * 		{@child.info ism:classification|1|11111}
 * 		{@child.info ism:ownerProducer|1..*|11111}
 * 		{@child.info ism:&lt;<i>securityAttributes</i>&gt;|0..*|11111}
 * {@table.footer}
 * {@table.header Validation Rules}
 * 		{@ddms.rule The qualified name of this element must be correct.|Error|11111}
 * 		{@ddms.rule ism:classification must exist.|Error|11111}
 * 		{@ddms.rule ism:ownerProducer must exist.|Error|11111}
 * 		{@ddms.rule This component can be used with no values set.|Warning|11111}
 * {@table.footer}
 *  
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Description extends AbstractSimpleString {

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Description(Element element) throws InvalidDDMSException {
		super(element, true);
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param description the value of the description child text
	 * @param securityAttributes any security attributes
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Description(String description, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		super(Description.getName(DDMSVersion.getCurrentVersion()), description, securityAttributes, true);
	}

	/**
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Description.getName(getDDMSVersion()));
		super.validate();
	}

	/**
	 * @see AbstractBaseComponent#validateWarnings()
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getValue()))
			addWarning("A ddms:" + getName() + " element was found with no description value.");
		super.validateWarnings();
	}

	/**
	 * @see AbstractBaseComponent#getJSONObject()
	 */
	public JsonObject getJSONObject() {
		JsonObject object = new JsonObject();
		addJson(object, getName(), getValue());
		addJson(object, getSecurityAttributes());
		return (object);
	}
	
	/**
	 * @see AbstractBaseComponent#getHTMLTextOutput(OutputFormat, String, String)
	 */
	public String getHTMLTextOutput(OutputFormat format, String prefix, String suffix) {
		Util.requireHTMLText(format);
		String localPrefix = buildPrefix(prefix, getName(), suffix);
		StringBuffer text = new StringBuffer();
		text.append(buildHTMLTextOutput(format, localPrefix, getValue()));
		text.append(getSecurityAttributes().getHTMLTextOutput(format, localPrefix + "."));
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
		return ("description");
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Description))
			return (false);
		return (true);
	}

	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder extends AbstractSimpleString.Builder {
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
		public Builder(Description description) {
			super(description);
		}

		/**
		 * @see IBuilder#commit()
		 */
		public Description commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Description(getValue(), getSecurityAttributes().commit()));
		}
	}
}