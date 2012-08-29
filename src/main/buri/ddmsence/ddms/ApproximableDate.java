/* Copyright 2010 - 2012 by Brian Uri!
   
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
package buri.ddmsence.ddms;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.util.Util;

/**
 * Base class for DDMS elements which are an approximable date, such as ddms:dates/ddms:acquiredOn.
 * 
 * <p>
 * The structure of this class diverges from the usual DDMSence approach of selecting which DDMS components are
 * implemented as Java classes. The ApproximableDateType, introduced in DDMS 4.1, is directly reused in three locations
 * in the DDMS schema, so it is implemented as a final class rather than an Abstract class. It contains one wrapper
 * element, ddms:searchableDate, which is not implemented as a Java class.
 * </p>
 * 
 * <p>
 * This type also contains one element, ddms:approximableDate, which should be implemented as a Java class, since it
 * contains an attribute. To simplify the class structure, this element and its attribute are collapsed into this Java
 * class.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The searchable end date cannot be before the searchable start date.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>This component can be used with no description, approximableDate, or searchableDate values.</li>
 * <li>A ddms:description element can be used without child text.</li> 
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:description</u>: A description of this date (0-1, optional)<br />
 * <u>ddms:approximableDate</u>: The value of this date, associated with an optional approximation decorator (0-1,
 * optional)<br />
 * <u>ddms:searchableDate/ddms:start</u>: The exact date which is the lower bound for this approximable date in searches
 * (0-1, optional)<br />
 * <u>ddms:searchableDate/ddms:end</u>: The exact date which is the upper bound for this approximable date in searches
 * (0-1, optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:approximableDate/ddms:approximation</u>: An attribute that decorates the approximableDate with terms such as
 * "early" or "late" (0-1, optional)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.1.0
 */
public final class ApproximableDate extends AbstractBaseComponent {

	private XMLGregorianCalendar _approximableDate = null;
	private XMLGregorianCalendar _searchableStartDate = null;
	private XMLGregorianCalendar _searchableEndDate = null;

	private static final String DESCRIPTION_NAME = "description";
	private static final String APPROXIMABLE_DATE_NAME = "approximableDate";
	private static final String APPROXIMATION_NAME = "approximation";
	private static final String SEARCHABLE_DATE_NAME = "searchableDate";
	private static final String START_NAME = "start";
	private static final String END_NAME = "end";

	private static Set<String> APPROXIMATION_TYPES = new HashSet<String>();
	static {
		APPROXIMATION_TYPES.add("1st qtr");
		APPROXIMATION_TYPES.add("2nd qtr");
		APPROXIMATION_TYPES.add("3rd qtr");
		APPROXIMATION_TYPES.add("4th qtr");
		APPROXIMATION_TYPES.add("circa");
		APPROXIMATION_TYPES.add("early");
		APPROXIMATION_TYPES.add("mid");
		APPROXIMATION_TYPES.add("late");
	}

	private static Set<String> NAME_TYPES = new HashSet<String>();
	static {
		NAME_TYPES.add("acquiredOn");
		NAME_TYPES.add("approximableStart");
		NAME_TYPES.add("approximableEnd");
	}
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ApproximableDate(Element element) throws InvalidDDMSException {
		super.setXOMElement(element, false);
		try {
			String approximableDate = "";
			String startString = "";
			String endString = "";
			Element approximableDateElement = getXOMElement().getFirstChildElement(APPROXIMABLE_DATE_NAME,
				getNamespace());
			if (approximableDateElement != null) {
				approximableDate = approximableDateElement.getValue();
			}
			Element searchableElement = getXOMElement().getFirstChildElement(SEARCHABLE_DATE_NAME,
				getNamespace());
			if (searchableElement != null) {
				Element startElement = searchableElement.getFirstChildElement(START_NAME, getNamespace());
				startString = startElement.getValue();
				Element endElement = searchableElement.getFirstChildElement(END_NAME, getNamespace());
				endString = endElement.getValue();
			}
			loadDateCaches(approximableDate, startString, endString);
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param name the name of the element
	 * @param description the description of this approximable date (optional)
	 * @param approximableDate the value of the approximable date (optional)
	 * @param approximation an attribute that decorates the date (optional)
	 * @param searchableStartDate the lower bound for this approximable date (optional)
	 * @param searchableEndDate the upper bound for this approximable date (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ApproximableDate(String name, String description, String approximableDate, String approximation,
		String searchableStartDate, String searchableEndDate) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(name, null);
			setXOMElement(element, false);

			if (!Util.isEmpty(description)) {
				Util.addDDMSChildElement(getXOMElement(), DESCRIPTION_NAME, description);
			}
			if (!Util.isEmpty(approximableDate) || Util.isEmpty(approximation)) {
				Element approximableElment = Util.buildDDMSElement(APPROXIMABLE_DATE_NAME, approximableDate);
				Util.addDDMSAttribute(approximableElment, APPROXIMATION_NAME, approximation);
				getXOMElement().appendChild(approximableElment);
			}
			if (!Util.isEmpty(searchableStartDate) || Util.isEmpty(searchableEndDate)) {
				Element searchableElement = Util.buildDDMSElement(SEARCHABLE_DATE_NAME, null);
				Util.addDDMSChildElement(searchableElement, START_NAME, searchableStartDate);
				Util.addDDMSChildElement(searchableElement, END_NAME, searchableEndDate);
				getXOMElement().appendChild(searchableElement);
			}
			loadDateCaches(approximableDate, searchableStartDate, searchableEndDate);
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Helper method to populate cached date variables.
	 * 
	 * @param approximableDate the approximable date string
	 * @param startString the start string
	 * @param endString the end string
	 */
	private void loadDateCaches(String approximableDate, String startString, String endString) {
		try {
			_approximableDate = getFactory().newXMLGregorianCalendar(Util.getNonNullString(approximableDate));
		}
		catch (IllegalArgumentException e) {
			// Was not a valid date. validate() will catch this later.
		}
		try {
			_searchableStartDate = getFactory().newXMLGregorianCalendar(Util.getNonNullString(startString));
		}
		catch (IllegalArgumentException e) {
			// Was not a valid date. validate() will catch this later.
		}
		try {
			_searchableEndDate = getFactory().newXMLGregorianCalendar(Util.getNonNullString(endString));
		}
		catch (IllegalArgumentException e) {
			// Was not a valid date. validate() will catch this later.
		}
	}
	
	/**
	 * Validates an approximation against the allowed values.
	 * 
	 * @param approximation the value to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateApproximation(String approximation) throws InvalidDDMSException {
		Util.requireDDMSValue("approximation", approximation);
		if (!APPROXIMATION_TYPES.contains(approximation))
			throw new InvalidDDMSException("The approximation must be one of " + APPROXIMATION_TYPES);
	}

	/**
	 * Validates an element name against the allowed values.
	 * 
	 * @param name the value to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateElementName(String name) throws InvalidDDMSException {
		Util.requireDDMSValue("name", name);
		if (!NAME_TYPES.contains(name))
			throw new InvalidDDMSException("The element name must be one of " + NAME_TYPES);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The name of the element has an appropriate value.</li>
	 * <li>If the approximableDate exists, it is an acceptable date format.</li>
	 * <li>If an approximation exists, it has an appropriate value.</li>
	 * <li>If start exists, it is a valid date format.</li>
	 * <li>If end exists, it is a valid date format.</li>
	 * <li>The start date is before the end date.</li>
	 * <li>This component cannot be used until DDMS 4.1 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		validateElementName(getName());
		if (getApproximableDate() != null)
			Util.requireDDMSDateFormat(getApproximableDate().getXMLSchemaType());
		if (!Util.isEmpty(getApproximation())) {
			validateApproximation(getApproximation());
		}
		if (getSearchableStartDate() != null)
			Util.requireDDMSDateFormat(getSearchableStartDate().getXMLSchemaType());
		if (getSearchableEndDate() != null)
			Util.requireDDMSDateFormat(getSearchableEndDate().getXMLSchemaType());
		if (getSearchableStartDate() != null && getSearchableEndDate() != null) {
			if (getSearchableStartDate().toGregorianCalendar().after(getSearchableEndDate().toGregorianCalendar())) {
				throw new InvalidDDMSException("The start date is after the end date.");
			}
		}
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.1");

		super.validate();
	}

	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A completely empty element was found.</li>
	 * <li>A description element can be used without any child text.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getDescription()) && getApproximableDate() == null && Util.isEmpty(getApproximation())) {
			addWarning("A completely empty " + getQualifiedName() + " element was found.");
		}
		if (getChild(DESCRIPTION_NAME) != null && Util.isEmpty(getDescription()))
			addWarning("A completely empty ddms:description element was found.");
		super.validateWarnings();
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix);
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix + "." + DESCRIPTION_NAME, getDescription()));
		if (getApproximableDate() != null)
			text.append(buildOutput(isHTML, localPrefix + "." + APPROXIMABLE_DATE_NAME, getApproximableDate().toXMLFormat()));
		text.append(buildOutput(isHTML, localPrefix + "." + APPROXIMABLE_DATE_NAME + "." + APPROXIMATION_NAME, getApproximation()));
		if (getSearchableStartDate() != null)
			text.append(buildOutput(isHTML, localPrefix + "." + SEARCHABLE_DATE_NAME + "." + START_NAME, getSearchableStartDate().toXMLFormat()));
		if (getSearchableEndDate() != null)
			text.append(buildOutput(isHTML, localPrefix + "." + SEARCHABLE_DATE_NAME + "." + END_NAME, getSearchableEndDate().toXMLFormat()));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof ApproximableDate))
			return (false);
		ApproximableDate test = (ApproximableDate) obj;
		return (getDescription().equals(test.getDescription())
			&& Util.nullEquals(getApproximableDate(), test.getApproximableDate()) 
			&& getApproximation().equals(test.getApproximation())
			&& Util.nullEquals(getSearchableStartDate(), test.getSearchableStartDate())
			&& Util.nullEquals(getSearchableEndDate(), test.getSearchableEndDate()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getDescription().hashCode();
		result = 7 * result + getApproximableDate().hashCode();
		result = 7 * result + getApproximation().hashCode();
		result = 7 * result + getSearchableStartDate().hashCode();
		result = 7 * result + getSearchableEndDate().hashCode();		
		return (result);
	}
	
	/**
	 * Accessor for the description.
	 */
	public String getDescription() {
		String description = null;
		Element descriptionElement = getChild(DESCRIPTION_NAME);
		if (descriptionElement != null) {
			description = descriptionElement.getValue();
		}
		return (Util.getNonNullString(description));
	}
	
	/**
	 * Accessor for the approximableDate (optional). Returns a copy.
	 */
	public XMLGregorianCalendar getApproximableDate() {
		return (_approximableDate == null ? null : getFactory().newXMLGregorianCalendar(_approximableDate.toXMLFormat()));
	}

	/**
	 * Accessor for the value of the approximation attribute
	 */
	public String getApproximation() {
		String approximation = null;
		Element approximableDateElement = getChild(APPROXIMABLE_DATE_NAME);
		if (approximableDateElement != null) {
			approximation = approximableDateElement.getAttributeValue(APPROXIMATION_NAME, getNamespace());
		}
		return (Util.getNonNullString(approximation));
	}

	/**
	 * Accessor for the searchableStartDate (optional). Returns a copy.
	 */
	public XMLGregorianCalendar getSearchableStartDate() {
		return (_searchableStartDate == null ? null : getFactory().newXMLGregorianCalendar(_searchableStartDate.toXMLFormat()));
	}
	
	/**
	 * Accessor for the searchableEndDate (optional). Returns a copy.
	 */
	public XMLGregorianCalendar getSearchableEndDate() {
		return (_searchableEndDate == null ? null : getFactory().newXMLGregorianCalendar(_searchableEndDate.toXMLFormat()));
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
	 * @since 2.1.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -7348511606867959470L;
		private String _name;
		private String _description;
		private String _approximableDate;
		private String _approximation;
		private String _searchableStartDate;
		private String _searchableEndDate;

		/**
		 * Empty constructor
		 */
		public Builder() {}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(ApproximableDate approximableDate) {
			setName(approximableDate.getName());
			setDescription(approximableDate.getDescription());
			if (approximableDate.getApproximableDate() != null)
				setApproximableDate(approximableDate.getApproximableDate().toXMLFormat());
			setApproximation(approximableDate.getApproximation());
			setSearchableStartDate(approximableDate.getSearchableStartDate().toXMLFormat());
			setSearchableEndDate(approximableDate.getSearchableEndDate().toXMLFormat());
		}

		/**
		 * @see IBuilder#commit()
		 */
		public ApproximableDate commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new ApproximableDate(getName(), getDescription(), getApproximableDate(),
				getApproximation(), getSearchableStartDate(), getSearchableEndDate()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getName()) && Util.isEmpty(getDescription()) && Util.isEmpty(getApproximableDate())
				&& Util.isEmpty(getApproximation()) && Util.isEmpty(getSearchableStartDate())
				&& Util.isEmpty(getSearchableEndDate()));
		}

		/**
		 * Builder accessor for the name of the element
		 */
		public String getName() {
			return _name;
		}

		/**
		 * Builder accessor for the name of the element
		 */
		public void setName(String name) {
			_name = name;
		}

		/**
		 * Builder accessor for the description
		 */
		public String getDescription() {
			return _description;
		}

		/**
		 * Builder accessor for the description
		 */
		public void setDescription(String description) {
			_description = description;
		}
		
		/**
		 * Builder accessor for the approximableDate
		 */
		public String getApproximableDate() {
			return _approximableDate;
		}

		/**
		 * Builder accessor for the approximableDate
		 */
		public void setApproximableDate(String approximableDate) {
			_approximableDate = approximableDate;
		}

		/**
		 * Builder accessor for the approximation
		 */
		public String getApproximation() {
			return _approximation;
		}

		/**
		 * Builder accessor for the approximation
		 */
		public void setApproximation(String approximation) {
			_approximation = approximation;
		}

		/**
		 * Builder accessor for the searchableStartDate
		 */
		public String getSearchableStartDate() {
			return _searchableStartDate;
		}

		/**
		 * Builder accessor for the searchableStartDate
		 */
		public void setSearchableStartDate(String searchableStartDate) {
			_searchableStartDate = searchableStartDate;
		}
		
		/**
		 * Builder accessor for the searchableEndDate
		 */
		public String getSearchableEndDate() {
			return _searchableEndDate;
		}

		/**
		 * Builder accessor for the searchableEndDate
		 */
		public void setSearchableEndDate(String searchableEndDate) {
			_searchableEndDate = searchableEndDate;
		}
	}
}