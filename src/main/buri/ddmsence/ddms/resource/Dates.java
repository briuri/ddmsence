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
package buri.ddmsence.ddms.resource;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:dates.
 * 
 * <p>
 * Date formats must adhere to one of: xs:dateTime, xs:date, xs:gYearMonth, or xs:gYear, and the
 * <code>XMLGregorianCalendar</code> class is used to enforce these restrictions.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A dates element can be used with none of the four date attributes set.</li>
 * </ul>
 * </td></tr></table>
 *  
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:created</u>: creation date (optional)<br />
 * <u>ddms:posted</u>: posting date (optional)<br />
 * <u>ddms:validTil</u>: expiration date (optional)<br />
 * <u>ddms:infoCutOff</u>: info cutoff date (optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Date<br />
 * <u>Description</u>: Calendar dates associated with an event in the life cycle of the resource.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2004-11-23<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Dates extends AbstractBaseComponent {

	// Values are cached upon instantiation, so Calendars do not have to be built when calling getters.
	private XMLGregorianCalendar _cachedCreated = null;
	private XMLGregorianCalendar _cachedPosted = null;
	private XMLGregorianCalendar _cachedValidTil = null;
	private XMLGregorianCalendar _cachedInfoCutOff = null;
	
	/** The element name of this component */
	public static final String NAME = "dates";
	
	private static final String CREATED_NAME = "created";
	private static final String POSTED_NAME = "posted";
	private static final String VALID_TIL_NAME = "validTil";
	private static final String INFO_CUT_OFF_NAME = "infoCutOff";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Dates(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			String created = getAttributeValue(CREATED_NAME);
			if (!Util.isEmpty(created))
				_cachedCreated = getFactory().newXMLGregorianCalendar(created);
			String posted = getAttributeValue(POSTED_NAME);
			if (!Util.isEmpty(posted))
				_cachedPosted = getFactory().newXMLGregorianCalendar(posted);
			String validTil = getAttributeValue(VALID_TIL_NAME);
			if (!Util.isEmpty(validTil))
				_cachedValidTil = getFactory().newXMLGregorianCalendar(validTil);
			String infoCutOff = getAttributeValue(INFO_CUT_OFF_NAME);
			if (!Util.isEmpty(infoCutOff))
				_cachedInfoCutOff = getFactory().newXMLGregorianCalendar(infoCutOff);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
		
	/**
	 * Constructor for creating a component from raw data. The string-based inputs must conform to one of
	 * the XML date types: xs:dateTime, xs:date, xs:gYearMonth, or xs:gYear. 
	 * 
	 * @param created the creation date (optional)
	 * @param posted the posting date (optional)
	 * @param validTil the expiration date (optional)
	 * @param infoCutOff the info cutoff date (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Dates(String created, String posted, String validTil, String infoCutOff) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Dates.NAME, null);
			if (!Util.isEmpty(created)) {
				_cachedCreated = getFactory().newXMLGregorianCalendar(created);
				Util.addDDMSAttribute(element, CREATED_NAME, getCreated().toXMLFormat());
			}
			if (!Util.isEmpty(posted)) {
				_cachedPosted = getFactory().newXMLGregorianCalendar(posted);
				Util.addDDMSAttribute(element, POSTED_NAME, getPosted().toXMLFormat());
			}
			if (!Util.isEmpty(validTil)) {
				_cachedValidTil = getFactory().newXMLGregorianCalendar(validTil);
				Util.addDDMSAttribute(element, VALID_TIL_NAME, getValidTil().toXMLFormat());
			}
			if (!Util.isEmpty(infoCutOff)) {
				_cachedInfoCutOff = getFactory().newXMLGregorianCalendar(infoCutOff);
				Util.addDDMSAttribute(element, INFO_CUT_OFF_NAME, getInfoCutOff().toXMLFormat());
			}
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
	 * <li>If set, each date attribute adheres to an acceptable date format.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		if (getCreated() != null)
			Util.requireDDMSDateFormat(getCreated().getXMLSchemaType());
		if (getPosted() != null)
			Util.requireDDMSDateFormat(getPosted().getXMLSchemaType());
		if (getValidTil() != null)
			Util.requireDDMSDateFormat(getValidTil().getXMLSchemaType());
		if (getInfoCutOff() != null)
			Util.requireDDMSDateFormat(getInfoCutOff().getXMLSchemaType());
		
		if (getCreated() == null && getPosted() == null && getValidTil() == null && getInfoCutOff() == null) {
			addWarning("A completely empty ddms:dates element was found.");
		}
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		if (getCreated() != null)
			html.append(buildHTMLMeta("date.created", getCreated().toXMLFormat(), true));
		if (getPosted() != null)
			html.append(buildHTMLMeta("date.posted", getPosted().toXMLFormat(), true));
		if (getValidTil() != null)
			html.append(buildHTMLMeta("date.validtil", getValidTil().toXMLFormat(), true));
		if (getInfoCutOff() != null)
			html.append(buildHTMLMeta("date.infocutoff", getInfoCutOff().toXMLFormat(), true));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		if (getCreated() != null)
			text.append(buildTextLine("Date Created", getCreated().toXMLFormat(), true));
		if (getPosted() != null)
			text.append(buildTextLine("Date Posted", getPosted().toXMLFormat(), true));
		if (getValidTil() != null)
			text.append(buildTextLine("Date Valid Til", getValidTil().toXMLFormat(), true));
		if (getInfoCutOff() != null)
			text.append(buildTextLine("Date Info Cut Off", getInfoCutOff().toXMLFormat(), true));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Dates))
			return (false);
		Dates test = (Dates) obj;
		boolean isEqual = true;
		if (getCreated() != null)
			isEqual = isEqual && getCreated().equals(test.getCreated());
		if (getPosted() != null)
			isEqual = isEqual && getPosted().equals(test.getPosted());
		if (getValidTil() != null)
			isEqual = isEqual && getValidTil().equals(test.getValidTil());
		if (getInfoCutOff() != null)
			isEqual = isEqual && getInfoCutOff().equals(test.getInfoCutOff());
		return (isEqual);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		if (getCreated() != null)
			result = 7 * result + getCreated().hashCode();
		if (getPosted() != null)
			result = 7 * result + getPosted().hashCode();
		if (getValidTil() != null)
			result = 7 * result + getValidTil().hashCode();
		if (getInfoCutOff() != null)
			result = 7 * result + getInfoCutOff().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the created date (optional). Returns a copy.
	 */
	public XMLGregorianCalendar getCreated() {
		return (_cachedCreated == null ? null : getFactory().newXMLGregorianCalendar(_cachedCreated.toXMLFormat())); 
	}
	
	/**
	 * Accessor for the posted date (optional). Returns a copy.
	 */
	public XMLGregorianCalendar getPosted() {
		return (_cachedPosted == null ? null : getFactory().newXMLGregorianCalendar(_cachedPosted.toXMLFormat())); 
	}
	
	/**
	 * Accessor for the expiration date (optional). Returns a copy.
	 */
	public XMLGregorianCalendar getValidTil() {
		return (_cachedValidTil == null ? null : getFactory().newXMLGregorianCalendar(_cachedValidTil.toXMLFormat()));  
	}
	
	/**
	 * Accessor for the cutoff date (optional). Returns a copy.
	 */
	public XMLGregorianCalendar getInfoCutOff() {
		return (_cachedInfoCutOff == null ? null : getFactory().newXMLGregorianCalendar(_cachedInfoCutOff.toXMLFormat())); 
	}
	
	/**
	 * Accesor for the datatype factory
	 */
	private static DatatypeFactory getFactory() {
		return (Util.getDataTypeFactory());
	}
} 