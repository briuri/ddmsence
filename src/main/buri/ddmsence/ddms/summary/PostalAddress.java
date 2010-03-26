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

import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:postalAddress.
 *  
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A postalAddress element can be used with no child elements.</li>
 * </ul>
 * </td></tr></table>
 * 	
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:street</u>: the street address (0-6 optional)<br />
 * <u>ddms:city</u>: the city (0-1 optional)<br />
 * <u>ddms:state</u>: the state (0-1 optional)<br />
 * <u>ddms:province</u>: the province (0-1 optional)<br />
 * <u>ddms:postalCode</u>: the postal code (0-1 optional)<br />
 * <u>ddms:countryCode</u>: the country code (0-1 optional), implemented as a {@link CountryCode}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#PostalAddress<br />
 * <u>Description</u>: A wrapper for postal address elements including street, city, state or province, postal code and country code.<br />
 * <u>Obligation</u>: Optional in a geospatialCoverage element<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class PostalAddress extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<String> _cachedStreets;
	private String _cachedCity;
	private String _cachedState;
	private String _cachedProvince;
	private String _cachedPostalCode;
	private CountryCode _cachedCountryCode;
	
	/** The element name of this component */
	public static final String NAME = "postalAddress";
	
	private static final String STREET_NAME = "street";
	private static final String CITY_NAME = "city";
	private static final String STATE_NAME = "state";
	private static final String PROVINCE_NAME = "province";
	private static final String POSTAL_CODE_NAME = "postalCode";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public PostalAddress(Element element) throws InvalidDDMSException {
		Util.requireDDMSValue("postalAddress element", element);
		_cachedStreets = Util.getDDMSChildValues(element, STREET_NAME);
		Element cityElement = element.getFirstChildElement(CITY_NAME, element.getNamespaceURI());
		if (cityElement != null)
			_cachedCity = cityElement.getValue();
		Element stateElement = element.getFirstChildElement(STATE_NAME, element.getNamespaceURI());
		if (stateElement != null)
			_cachedState = stateElement.getValue();
		Element provinceElement = element.getFirstChildElement(PROVINCE_NAME, element.getNamespaceURI());
		if (provinceElement != null)
			_cachedProvince = provinceElement.getValue();
		Element postalCodeElement = element.getFirstChildElement(POSTAL_CODE_NAME, element.getNamespaceURI());
		if (postalCodeElement != null)
			_cachedPostalCode = postalCodeElement.getValue();
		Element countryCodeElement = element.getFirstChildElement(CountryCode.NAME, element.getNamespaceURI());
		if (countryCodeElement != null)
			_cachedCountryCode = new CountryCode(PostalAddress.NAME, countryCodeElement);
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param streets the street address lines (0-6)
	 * @param city the city (optional)
	 * @param stateOrProvince the state or province (optional)
	 * @param postalCode the postal code (optional)
	 * @param countryCode the country code (optional)
	 * @param hasState true if the stateOrProvince is a state, false if it is a province (only 1 of state or province can exist in a postalAddress)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public PostalAddress(List<String> streets, String city, String stateOrProvince, String postalCode, CountryCode countryCode, boolean hasState) throws InvalidDDMSException {
		if (streets == null)
			streets = Collections.emptyList();
		Element element = Util.buildDDMSElement(PostalAddress.NAME, null);
		for (String street : streets) {
			element.appendChild(Util.buildDDMSElement(STREET_NAME, street));
		}
		Util.addDDMSChildElement(element, CITY_NAME, city);
		if (hasState)
			Util.addDDMSChildElement(element, STATE_NAME, stateOrProvince);
		else
			Util.addDDMSChildElement(element, PROVINCE_NAME, stateOrProvince);
		Util.addDDMSChildElement(element, POSTAL_CODE_NAME, postalCode);
		if (countryCode != null)
			element.appendChild(countryCode.getXOMElementCopy());
		_cachedStreets = streets;
		_cachedCity = city;
		_cachedState = hasState ? stateOrProvince : ""; 
		_cachedProvince = hasState ? "" : stateOrProvince;
		_cachedPostalCode = postalCode;
		_cachedCountryCode = countryCode;
		setXOMElement(element, true);
	}

	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Either a state or a province can exist, but not both.</li>
	 * <li>0-6 streets, 0-1 cities, 0-1 states, 0-1 provinces, 0-1 postal codes, and 0-1 country codes exist.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		if (!Util.isEmpty(getState()) && !Util.isEmpty(getProvince())) {
			throw new InvalidDDMSException("Only 1 of state or province can be used.");
		}
		Util.requireBoundedDDMSChildCount(getXOMElement(), STREET_NAME, 0, 6);
		Util.requireBoundedDDMSChildCount(getXOMElement(), CITY_NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), STATE_NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), PROVINCE_NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), POSTAL_CODE_NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), CountryCode.NAME, 0, 1);
		
		if (getCountryCode() != null)
			addWarnings(getCountryCode().getValidationWarnings(), false);
		if (getStreets().isEmpty() && Util.isEmpty(getCity()) && Util.isEmpty(getState()) && Util.isEmpty(getProvince()) &&
				Util.isEmpty(getPostalCode()) && getCountryCode() == null) {
			addWarning("A completely empty ddms:postalAddress element was found.");
		}
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		for (String street : getStreets())
			html.append(buildHTMLMeta("geospatial.address.street", street, false));
		html.append(buildHTMLMeta("geospatial.address.city", getCity(), false));
		html.append(buildHTMLMeta("geospatial.address.state", getState(), false));
		html.append(buildHTMLMeta("geospatial.address.province", getProvince(), false));
		html.append(buildHTMLMeta("geospatial.address.postalcode", getPostalCode(), false));
		if (getCountryCode() != null)
			html.append(getCountryCode().toHTML());
		return (html.toString());
	}

	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		for (String street : getStreets())
			text.append(buildTextLine("Postal Address Street", street, false));
		text.append(buildTextLine("Postal Address City", getCity(), false));
		text.append(buildTextLine("Postal Address State", getState(), false));
		text.append(buildTextLine("Postal Address Province", getProvince(), false));
		text.append(buildTextLine("Postal Address Postal Code", getPostalCode(), false));
		if (getCountryCode() != null)
			text.append(getCountryCode().toText());
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof PostalAddress))
			return (false);
		PostalAddress test = (PostalAddress) obj;
		boolean isEqual = Util.listEquals(getStreets(), test.getStreets()) &&
			getCity().equals(test.getCity()) &&
			getState().equals(test.getState()) &&
			getProvince().equals(test.getProvince()) &&
			getPostalCode().equals(test.getPostalCode()) &&
			Util.nullEquals(getCountryCode(), test.getCountryCode());
		return (isEqual);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getStreets().hashCode();
		result = 7 * result + getCity().hashCode();
		result = 7 * result + getState().hashCode();
		result = 7 * result + getProvince().hashCode();
		result = 7 * result + getPostalCode().hashCode();
		if (getCountryCode() != null)
			result = 7 * result + getCountryCode().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the street addresses (max 6)
	 */
	public List<String> getStreets() {
		return (Collections.unmodifiableList(_cachedStreets));
	}
	
	/**
	 * Accessor for the city
	 */
	public String getCity() {
		return (Util.getNonNullString(_cachedCity));
	}
	
	/**
	 * Accessor for the state
	 */
	public String getState() {
		return (Util.getNonNullString(_cachedState));
	}
	
	/**
	 * Accessor for the province
	 */
	public String getProvince() {
		return (Util.getNonNullString(_cachedProvince));
	}
	
	/**
	 * Accessor for the postalCode
	 */
	public String getPostalCode() {
		return (Util.getNonNullString(_cachedPostalCode));
	}

	/**
	 * Accessor for the country code
	 */
	public CountryCode getCountryCode() {
		return (_cachedCountryCode);
	}
} 