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
package buri.ddmsence.ddms.summary;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:temporalCoverage.
 * 
 * <p>
 * Before DDMS 4.0, a temporalCoverage element contains a locally defined TimePeriod construct. 
 * This TimePeriod construct is a container for the name, start, and end values of a time period.
 * It exists only inside of a ddms:temporalCoverage parent, so it is not implemented as a Java object.
 * Starting in DDMS 4.0, the TimePeriod wrapper has been removed.
 * </p>
 * 
 * <p>To avoid confusion between the name of the temporalCoverage element and the name of the specified time period,
 * the latter is referred to as the "time period name".
 * </p>
 * 
 * <p>
 * If not "Not Applicable" or "Unknown", date formats must adhere to one of: xs:dateTime, xs:date, xs:gYearMonth, or
 * xs:gYear, and the <code>XMLGregorianCalendar</code> class is used to enforce these restrictions.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The end date cannot be before the start date.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A time period name element can be used with no child text.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:name</u>: An interval of time, which can be expressed as a named era (0-1 optional, default=Unknown). Zero or
 * 1 of these elements may appear.<br />
 * <u>ddms:start</u>: The start date of a period of time (exactly 1 required, default=Unknown).<br />
 * <u>ddms:end</u>: The end date of a period of time (exactly 1 required, default=Unknown).
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are optional. (starting in DDMS 3.0)
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: Subject matter coverage expressed in terms of one or more periods of time.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class TemporalCoverage extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so Calendars do not have to be built when calling getters.
	private String _cachedName = DEFAULT_VALUE;
	private String _cachedStartString;
	private String _cachedEndString;
	private XMLGregorianCalendar _cachedStart = null;
	private XMLGregorianCalendar _cachedEnd = null;
	private SecurityAttributes _securityAttributes;
	
	private static final String DEFAULT_VALUE = "Unknown";
		
	private static Set<String> EXTENDED_DATE_TYPES = new HashSet<String>();
	static {
		EXTENDED_DATE_TYPES.add("Not Applicable");
		EXTENDED_DATE_TYPES.add("Unknown");
	}
	
	// The name of the TimePeriod element itself
	private static final String TIME_PERIOD_NAME = "TimePeriod";
	
	// The name of the "name" element nested inside the temporalCoverage element 
	private static final String TIME_PERIOD_NAME_NAME = "name";
	
	private static final String START_NAME = "start";
	private static final String END_NAME = "end";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public TemporalCoverage(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("temporalCoverage element", element);
			setXOMElement(element, false);
			Element periodElement = getTimePeriodElement();
			if (periodElement != null) {
				Element nameElement = periodElement.getFirstChildElement(TIME_PERIOD_NAME_NAME, element
					.getNamespaceURI());
				if (nameElement != null && !Util.isEmpty(nameElement.getValue()))
					_cachedName = nameElement.getValue();
				Element startElement = periodElement.getFirstChildElement(START_NAME, element.getNamespaceURI());
				Element endElement = periodElement.getFirstChildElement("end", element.getNamespaceURI());
				String startString = (startElement == null ? "" : startElement.getValue());
				String endString = (endElement == null ? "" : endElement.getValue());
				loadDateCaches(startString, endString);
			}
			_securityAttributes = new SecurityAttributes(element);
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param timePeriodName the time period name (optional) (if empty, defaults to Unknown)
	 * @param startString a string representation of the date (required) (if empty, defaults to Unknown)
	 * @param endString a string representation of the end date (required) (if empty, defaults to Unknown)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public TemporalCoverage(String timePeriodName, String startString, String endString,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(TemporalCoverage.getName(DDMSVersion.getCurrentVersion()), null);
			
			Element periodElement = DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? element 
				: Util.buildDDMSElement(TIME_PERIOD_NAME, null);
			if (!Util.isEmpty(timePeriodName))
				_cachedName = timePeriodName;
			startString = (Util.isEmpty(startString) ? DEFAULT_VALUE : startString);
			endString = (Util.isEmpty(endString) ? DEFAULT_VALUE : endString);
			Util.addDDMSChildElement(periodElement, TIME_PERIOD_NAME_NAME, timePeriodName);
			periodElement.appendChild(Util.buildDDMSElement(START_NAME, startString));
			periodElement.appendChild(Util.buildDDMSElement(END_NAME, endString));
			loadDateCaches(startString, endString);

			if (!DDMSVersion.getCurrentVersion().isAtLeast("4.0"))
				element.appendChild(periodElement);

			_securityAttributes = SecurityAttributes.getNonNullInstance(securityAttributes);
			_securityAttributes.addTo(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
			
	/**
	 * Helper method to populate cached date variables.
	 * 
	 * @param startString the start string. Defaults to "Unknown" if empty.
	 * @param endString the end string. Defaults to "Unknown" if empty.
	 */
	private void loadDateCaches(String startString, String endString) {
		_cachedStartString = startString;
		try {
			_cachedStart = getFactory().newXMLGregorianCalendar(_cachedStartString);
		}
		catch (IllegalArgumentException e) {
			// Was not a valid date. validate() will catch this later. If we throw an InvalidDDMSException,
			// we will prevent the use of the extended date types like Unknown.
		}
		_cachedEndString = endString;
		try {
			_cachedEnd = getFactory().newXMLGregorianCalendar(_cachedEndString);
		}
		catch (IllegalArgumentException e) {
			// Was not a valid date. validate() will catch this later. If we throw an InvalidDDMSException,
			// we will prevent the use of the extended date types like Unknown.
		}
	}
	
	/**
	 * Asserts that an extended date is either "Not Applicable" or "Unknown".
	 * 
	 * @param dateString	the string to check
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateExtendedDateType(String dateString) throws InvalidDDMSException {
		Util.requireDDMSValue("extended date type", dateString);
		if (!EXTENDED_DATE_TYPES.contains(dateString))
			throw new InvalidDDMSException("If no date is specified, the value must be one of " + EXTENDED_DATE_TYPES);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>start is a valid date format.</li>
	 * <li>end is a valid date format.</li>
	 * <li>0-1 names, exactly 1 start, and exactly 1 end exist.</li>
	 * <li>The start date is before the end date.</li>
	 * <li>The SecurityAttributes do not exist until DDMS 3.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException  if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), TemporalCoverage.getName(getDDMSVersion()));
		Element periodElement = getTimePeriodElement();
		Util.requireDDMSValue("TimePeriod element", periodElement);
		Util.requireBoundedDDMSChildCount(periodElement, TIME_PERIOD_NAME_NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(periodElement, START_NAME, 1, 1);
		Util.requireBoundedDDMSChildCount(periodElement, END_NAME, 1, 1);
		if (getStart() != null)
			Util.requireDDMSDateFormat(getStart().getXMLSchemaType());
		else
			validateExtendedDateType(getStartString());
		if (getEnd() != null)
			Util.requireDDMSDateFormat(getEnd().getXMLSchemaType());
		else
			validateExtendedDateType(getEndString());
		if (getStart() != null && getEnd() != null) {
			if (getStart().toGregorianCalendar().after(getEnd().toGregorianCalendar())) {
				throw new InvalidDDMSException("The start date is after the end date.");
			}
		}

		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("3.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes cannot be applied to this component until DDMS 3.0 or later.");
		}
		
		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A ddms:name element was found with no value.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		Element periodElement = getTimePeriodElement();
		Element timePeriodName = periodElement.getFirstChildElement(TIME_PERIOD_NAME_NAME,
			periodElement.getNamespaceURI());
		if (timePeriodName != null && Util.isEmpty(timePeriodName.getValue()))
			addWarning("A ddms:name element was found with no value. Defaulting to \"" + DEFAULT_VALUE + "\".");
		super.validateWarnings();
	}
	
	/**
	 * @see AbstractBaseComponent#getLocatorSuffix()
	 */
	protected String getLocatorSuffix() {
		return (getDDMSVersion().isAtLeast("4.0") ? "" : ValidationMessage.ELEMENT_PREFIX
			+ getXOMElement().getNamespacePrefix() + ":" + TIME_PERIOD_NAME);
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		if (!getDDMSVersion().isAtLeast("4.0"))
			prefix += TIME_PERIOD_NAME + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + TIME_PERIOD_NAME_NAME, getTimePeriodName(), false));
		text.append(buildOutput(isHTML, prefix + START_NAME, getStartString(), true));
		text.append(buildOutput(isHTML, prefix + END_NAME, getEndString(), true));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
	 
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof TemporalCoverage))
			return (false);
		TemporalCoverage test = (TemporalCoverage) obj;
		return (getTimePeriodName().equals(test.getTimePeriodName()) 
			&& getStartString().equals(test.getStartString())
			&& getEndString().equals(test.getEndString()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getTimePeriodName().hashCode();
		result = 7 * result + getStartString().hashCode();
		result = 7 * result + getEndString().hashCode();
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
		return ("temporalCoverage");
	}
	
	/**
	 * Accessor for the element which contains the time period name, start date, and end date. Before DDMS 4.0,
	 * this is a wrapper element called ddms:TimePeriod. Starting in DDMS 4.0, it is the ddms:temporalCoverage
	 * element itself.
	 */
	private Element getTimePeriodElement() {
		return (getDDMSVersion().isAtLeast("4.0") ? getXOMElement() : getChild(TIME_PERIOD_NAME));
	}
	
	/**
	 * Accessor for the TimePeriod name element child text. Note that the getName() accessor will
	 * return the local name of the temporal coverage element (temporalCoverage).
	 */
	public String getTimePeriodName() {
		return (_cachedName);
	}
	
	/**
	 * Accessor for the XML calendar representing the start date. If the start date is "Not Applicable" or "Unknown"
	 * will return null. Use <code>getStartString</code> to retrieve the string representation.
	 */
	public XMLGregorianCalendar getStart() {
		return (_cachedStart == null ? null : getFactory().newXMLGregorianCalendar(_cachedStart.toXMLFormat()));
	}

	/**
	 * Accessor for the start date as a string. If the value of start cannot be represented by an XML calendar, this
	 * will return "Not Applicable" or "Unknown". Use <code>getStart</code> to work with this value as a calendar date.
	 */
	public String getStartString() {
		if (getStart() != null)
			return (getStart().toXMLFormat());
		return (_cachedStartString);
	}
	
	/**
	 * Accessor for the XML calendar representing the end date. If the end date is "Not Applicable" or "Unknown"
	 * will return null. Use <code>getEndString</code> to retrieve the string representation.
	 */
	public XMLGregorianCalendar getEnd() {
		return (_cachedEnd == null ? null : getFactory().newXMLGregorianCalendar(_cachedEnd.toXMLFormat()));
	}

	/**
	 * Accessor for the end date as a string. If the value of end cannot be represented by an XML calendar, this will
	 * return "Not Applicable" or "Unknown". Use <code>getEnd</code> to work with this value as a calendar date.
	 */
	public String getEndString() {
		if (getEnd() != null)
			return (getEnd().toXMLFormat());
		return (_cachedEndString);
	}
	
	/**
	 * Accessor for the Security Attributes.  Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_securityAttributes);
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
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -3187482277963663663L;
		private String _timePeriodName;
		private String _startString;
		private String _endString;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(TemporalCoverage coverage) {
			setTimePeriodName(coverage.getTimePeriodName());
			setStartString(coverage.getStartString());
			setEndString(coverage.getEndString());
			setSecurityAttributes(new SecurityAttributes.Builder(coverage.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public TemporalCoverage commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new TemporalCoverage(getTimePeriodName(), getStartString(), getEndString(), 
				getSecurityAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getTimePeriodName())
				&& Util.isEmpty(getStartString())
				&& Util.isEmpty(getEndString())
				&& getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the TimePeriod name element child text.
		 */
		public String getTimePeriodName() {
			return _timePeriodName;
		}

		/**
		 * Builder accessor for the TimePeriod name element child text.
		 */
		public void setTimePeriodName(String timePeriodName) {
			_timePeriodName = timePeriodName;
		}

		/**
		 * Builder accessor for the start date as a string.
		 */
		public String getStartString() {
			return _startString;
		}

		/**
		 * Builder accessor for the start date as a string.
		 */
		public void setStartString(String startString) {
			_startString = startString;
		}

		/**
		 * Builder accessor for the end date as a string.
		 */
		public String getEndString() {
			return _endString;
		}

		/**
		 * Builder accessor for the end date as a string.
		 */
		public void setEndString(String endString) {
			_endString = endString;
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