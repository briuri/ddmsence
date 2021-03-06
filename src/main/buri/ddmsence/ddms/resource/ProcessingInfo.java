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
package buri.ddmsence.ddms.resource;

import javax.xml.datatype.XMLGregorianCalendar;

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
 * An immutable implementation of ddms:processingInfo.
 * <br /><br />
 * {@ddms.versions 00011}
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
 * 		{@child.info ddms:dateProcessed|1|00011}
 * 		{@child.info ism:classification|1|00011}
 * 		{@child.info ism:ownerProducer|1..*|00011}
 * 		{@child.info ism:&lt;<i>securityAttributes</i>&gt;|0..*|00011}
 * {@table.footer}
 * {@table.header Validation Rules}
 * 		{@ddms.rule Component must not be used before the DDMS version in which it was introduced.|Error|11111}
 * 		{@ddms.rule The qualified name of this element must be correct.|Error|11111}
 * 		{@ddms.rule ddms:dateProcessed must exist, and adheres to a valid date format.|Error|11111}
 * 		{@ddms.rule ism:classification must exist.|Error|11111}
 * 		{@ddms.rule ism:ownerProducer must exist.|Error|11111}
 * {@table.footer}
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class ProcessingInfo extends AbstractSimpleString {

	private static final String DATE_PROCESSED_NAME = "dateProcessed";

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ProcessingInfo(Element element) throws InvalidDDMSException {
		super(element, true);
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param value the value of the child text
	 * @param dateProcessed the processing date
	 * @param securityAttributes any security attributes
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ProcessingInfo(String value, String dateProcessed, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(ProcessingInfo.getName(DDMSVersion.getCurrentVersion()), value, securityAttributes, false);
		try {
			Util.addDDMSAttribute(getXOMElement(), DATE_PROCESSED_NAME, dateProcessed);
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		requireAtLeastVersion("4.0.1");
		Util.requireDDMSQName(getXOMElement(), ProcessingInfo.getName(getDDMSVersion()));
		Util.requireDDMSValue(DATE_PROCESSED_NAME, getDateProcessedString());
		Util.requireDDMSDateFormat(getDateProcessedString(), getNamespace());
		super.validate();
	}

	/**
	 * @see AbstractBaseComponent#validateWarnings()
	 */
	protected void validateWarnings() {
		super.validateWarnings();
	}

	/**
	 * @see AbstractBaseComponent#getJSONObject()
	 */
	public JsonObject getJSONObject() {
		JsonObject object = new JsonObject();
		addJson(object, getName(), getValue());
		addJson(object, DATE_PROCESSED_NAME, getDateProcessedString());
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
		text.append(buildHTMLTextOutput(format, localPrefix + "." + DATE_PROCESSED_NAME, getDateProcessedString()));
		text.append(getSecurityAttributes().getHTMLTextOutput(format, localPrefix + "."));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof ProcessingInfo))
			return (false);
		ProcessingInfo test = (ProcessingInfo) obj;
		return (Util.nullEquals(getDateProcessedString(), test.getDateProcessedString()));
	}

	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("processingInfo");
	}

	/**
	 * Accessor for the processing date. Returns a copy.
	 * 
	 * <p>DDMS 4.1 added a new allowable date format (ddms:DateHourMinType). This method will convert values of that
	 * type into an XMLGregorianCalendar, using intelligent defaults for any missing information (e.g. 00 for seconds).
	 * <code>getDateProcessedString()</code> will return the raw XML format without any assumptions.</p>
	 */
	public XMLGregorianCalendar getDateProcessed() {
		return (Util.toXMLGregorianCalendar(getDateProcessedString()));
	}

	/**
	 * Accessor for the processing date.
	 */
	public String getDateProcessedString() {
		return (getAttributeValue(DATE_PROCESSED_NAME));
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
		private String _dateProcessed;

		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(ProcessingInfo info) {
			super(info);
			setDateProcessed(info.getDateProcessedString());
		}

		/**
		 * @see IBuilder#commit()
		 */
		public ProcessingInfo commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new ProcessingInfo(getValue(), getDateProcessed(),
				getSecurityAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (super.isEmpty() && Util.isEmpty(getDateProcessed()));
		}

		/**
		 * Builder accessor for the dateProcessed
		 */
		public String getDateProcessed() {
			return _dateProcessed;
		}

		/**
		 * Builder accessor for the dateProcessed
		 */
		public void setDateProcessed(String dateProcessed) {
			_dateProcessed = dateProcessed;
		}
	}
}