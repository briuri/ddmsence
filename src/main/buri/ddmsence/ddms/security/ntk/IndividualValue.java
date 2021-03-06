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
package buri.ddmsence.ddms.security.ntk;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractNtkString;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

import com.google.gson.JsonObject;

/**
 * An immutable implementation of ntk:AccessIndividualValue.
 * <br /><br />
 * {@ddms.versions 00010}
 * 
 * <p></p>
 * 
 * {@table.header History}
 * 		<p>This class was introduced to support NTK components in DDMS 4.1. Those components are
 * 		no longer a part of DDMS 5.0.</p>
 * {@table.footer}
 * {@table.header Nested Elements}
 * 		None.
 * {@table.footer}
 * {@table.header Attributes}
 * 		{@child.info ntk:id|0..1|00010}
 * 		{@child.info ntk:IDReference|0..1|00010}
 * 		{@child.info ntk:qualifier|0..1|00010}
 * 		{@child.info ism:classification|1|00010}
 * 		{@child.info ism:ownerProducer|1..*|00010}
 * 		{@child.info ism:&lt;<i>securityAttributes</i>&gt;|0..*|00010}
 * {@table.footer}
 * {@table.header Validation Rules}
 * 		{@ddms.rule Component must not be used before the DDMS version in which it was introduced.|Error|11111}
 * 		{@ddms.rule The qualified name of this element must be correct.|Error|11111}
 * 		{@ddms.rule ism:classification must exist.|Error|11111}
 * 		{@ddms.rule ism:ownerProducer must exist.|Error|11111}
 * 		{@ddms.rule This component can be used with no values set.|Warning|11111}
 * {@table.footer}
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class IndividualValue extends AbstractNtkString {

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public IndividualValue(Element element) throws InvalidDDMSException {
		super(false, element);
	}

	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param value the value of the element's child text
	 * @param id the NTK ID
	 * @param idReference a reference to an NTK ID
	 * @param qualifier an NTK qualifier
	 * @param securityAttributes the security attributes
	 */
	public IndividualValue(String value, String id, String idReference, String qualifier,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		super(false, IndividualValue.getName(DDMSVersion.getCurrentVersion()), value, id, idReference, qualifier,
			securityAttributes, true);
	}

	/**
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getNamespace(), IndividualValue.getName(getDDMSVersion()));
		super.validate();
	}

	/**
	 * @see AbstractBaseComponent#validateWarnings()
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getValue()))
			addWarning("A ntk:" + getName() + " element was found with no value.");
		super.validateWarnings();
	}

	/**
	 * @see AbstractBaseComponent#getJSONObject()
	 */
	public JsonObject getJSONObject() {
		JsonObject object = new JsonObject();
		addJson(object, "individualValue", getValue());
		addJson(object, "id", getID());
		addJson(object, "idReference", getIDReference());
		addJson(object, "qualifier", getQualifier());
		addJson(object, getSecurityAttributes());
		return (object);
	}
	
	/**
	 * @see AbstractBaseComponent#getHTMLTextOutput(OutputFormat, String, String)
	 */
	public String getHTMLTextOutput(OutputFormat format, String prefix, String suffix) {
		Util.requireHTMLText(format);
		String localPrefix = buildPrefix(prefix, "individualValue", suffix);
		StringBuffer text = new StringBuffer();
		text.append(buildHTMLTextOutput(format, localPrefix, getValue()));
		text.append(buildHTMLTextOutput(format, localPrefix + ".id", getID()));
		text.append(buildHTMLTextOutput(format, localPrefix + ".idReference", getIDReference()));
		text.append(buildHTMLTextOutput(format, localPrefix + ".qualifier", getQualifier()));
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
		return ("AccessIndividualValue");
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof IndividualValue))
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
		public Builder(IndividualValue value) {
			super(value);
		}

		/**
		 * @see IBuilder#commit()
		 */
		public IndividualValue commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new IndividualValue(getValue(), getID(), getIDReference(), getQualifier(),
				getSecurityAttributes().commit()));
		}
	}
}