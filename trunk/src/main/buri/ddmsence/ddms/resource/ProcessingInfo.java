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

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.AbstractSimpleString;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:processingInfo.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A processingInfo element can be used without any child text.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:dateProcessed</u>: date when this processing occurred (required)<br />
 * <u>{@link SecurityAttributes}</u>:  The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class ProcessingInfo extends AbstractSimpleString {

	private XMLGregorianCalendar _dateProcessed = null;
	
	private static final String DATE_PROCESSED_NAME = "dateProcessed";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ProcessingInfo(Element element) throws InvalidDDMSException {
		super(element, false);
		try {
			String processed = getAttributeValue(DATE_PROCESSED_NAME);
			if (!Util.isEmpty(processed))
				_dateProcessed = getFactory().newXMLGregorianCalendar(processed);
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param value the value of the child text
	 * @param dateProcessed the processing date (required)
	 * @param securityAttributes any security attributes (classification and ownerProducer are required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ProcessingInfo(String value, String dateProcessed, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		super(ProcessingInfo.getName(DDMSVersion.getCurrentVersion()), value, securityAttributes, false);
		try {
			try {
				if (!Util.isEmpty(dateProcessed)) {
					_dateProcessed = getFactory().newXMLGregorianCalendar(dateProcessed);
					Util.addDDMSAttribute(getXOMElement(), DATE_PROCESSED_NAME, getDateProcessed().toXMLFormat());
				}
			} catch (IllegalArgumentException e) {
				throw new InvalidDDMSException("The ddms:dateProcessed attribute is not in a valid date format.");
			}
			validate();
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
	 * <li>The dateProcessed exists, and is an acceptable date format.</li>
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot be used until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), ProcessingInfo.getName(getDDMSVersion()));
		Util.requireDDMSValue("dateProcessed", getDateProcessed());
		if (getDateProcessed() != null)
			Util.requireDDMSDateFormat(getDateProcessed().getXMLSchemaType());
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");
		
		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A ddms:processingInfo element was found with no child text.</li>
	 * <li>Include any warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getValue()))
			addWarning("A ddms:processingInfo element was found with no value.");
		super.validateWarnings();
	}
			
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName();
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix, getValue(), false));
		text.append(buildOutput(isHTML, prefix + "." + DATE_PROCESSED_NAME, getDateProcessed().toXMLFormat(), true));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix + "."));
		return (text.toString());
	}
		
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof ProcessingInfo))
			return (false);
		ProcessingInfo test = (ProcessingInfo) obj;
		return (Util.nullEquals(getDateProcessed(), test.getDateProcessed()));
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
	 * Accessor for the processing date (required). Returns a copy.
	 */
	public XMLGregorianCalendar getDateProcessed() {
		return (_dateProcessed == null ? null : getFactory().newXMLGregorianCalendar(
			_dateProcessed.toXMLFormat()));
	}
	
	/**
	 * Accesor for the datatype factory
	 */
	private static DatatypeFactory getFactory() {
		return (Util.getDataTypeFactory());
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
			if (info.getDateProcessed() != null)
				setDateProcessed(info.getDateProcessed().toXMLFormat());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public ProcessingInfo commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new ProcessingInfo(getValue(), getDateProcessed(), getSecurityAttributes()
				.commit()));
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