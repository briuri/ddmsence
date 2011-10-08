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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
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
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class PostalAddress extends AbstractBaseComponent {
	
	private List<String> _streets = null;
	private String _city = null;
	private String _state = null;
	private String _province = null;
	private String _postalCode = null;
	private CountryCode _countryCode = null;
	
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
		try {
			setXOMElement(element, false);
			_streets = Util.getDDMSChildValues(element, STREET_NAME);
			Element cityElement = element.getFirstChildElement(CITY_NAME, getNamespace());
			if (cityElement != null)
				_city = cityElement.getValue();
			Element stateElement = element.getFirstChildElement(STATE_NAME, getNamespace());
			if (stateElement != null)
				_state = stateElement.getValue();
			Element provinceElement = element.getFirstChildElement(PROVINCE_NAME, getNamespace());
			if (provinceElement != null)
				_province = provinceElement.getValue();
			Element postalCodeElement = element.getFirstChildElement(POSTAL_CODE_NAME, getNamespace());
			if (postalCodeElement != null)
				_postalCode = postalCodeElement.getValue();
			Element countryCodeElement = element.getFirstChildElement(CountryCode.getName(getDDMSVersion()),
				getNamespace());
			if (countryCodeElement != null)
				_countryCode = new CountryCode(countryCodeElement);
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
	 * @param streets the street address lines (0-6)
	 * @param city the city (optional)
	 * @param stateOrProvince the state or province (optional)
	 * @param postalCode the postal code (optional)
	 * @param countryCode the country code (optional)
	 * @param hasState true if the stateOrProvince is a state, false if it is a province (only 1 of state or province 
	 * can exist in a postalAddress)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public PostalAddress(List<String> streets, String city, String stateOrProvince, String postalCode,
		CountryCode countryCode, boolean hasState) throws InvalidDDMSException {
		try {
			if (streets == null)
				streets = Collections.emptyList();
			Element element = Util.buildDDMSElement(PostalAddress.getName(DDMSVersion.getCurrentVersion()), null);
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
			_streets = streets;
			_city = city;
			_state = hasState ? stateOrProvince : "";
			_province = hasState ? "" : stateOrProvince;
			_postalCode = postalCode;
			_countryCode = countryCode;
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
	 * <li>Either a state or a province can exist, but not both.</li>
	 * <li>0-6 streets, 0-1 cities, 0-1 states, 0-1 provinces, 0-1 postal codes, and 0-1 country codes exist.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), PostalAddress.getName(getDDMSVersion()));
		if (!Util.isEmpty(getState()) && !Util.isEmpty(getProvince())) {
			throw new InvalidDDMSException("Only 1 of state or province can be used.");
		}
		Util.requireBoundedChildCount(getXOMElement(), STREET_NAME, 0, 6);
		Util.requireBoundedChildCount(getXOMElement(), CITY_NAME, 0, 1);
		Util.requireBoundedChildCount(getXOMElement(), STATE_NAME, 0, 1);
		Util.requireBoundedChildCount(getXOMElement(), PROVINCE_NAME, 0, 1);
		Util.requireBoundedChildCount(getXOMElement(), POSTAL_CODE_NAME, 0, 1);
		Util.requireBoundedChildCount(getXOMElement(), CountryCode.getName(getDDMSVersion()), 0, 1);
		
		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A completely empty ddms:postalAddress element was found.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (getStreets().isEmpty() && Util.isEmpty(getCity()) && Util.isEmpty(getState())
			&& Util.isEmpty(getProvince()) && Util.isEmpty(getPostalCode()) && getCountryCode() == null) {
			addWarning("A completely empty ddms:postalAddress element was found.");
		}
		super.validateWarnings();
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		prefix = Util.getNonNullString(prefix) + getName() + Util.getNonNullString(suffix) + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + STREET_NAME, getStreets(), false));
		text.append(buildOutput(isHTML, prefix + CITY_NAME, getCity(), false));
		text.append(buildOutput(isHTML, prefix + STATE_NAME, getState(), false));
		text.append(buildOutput(isHTML, prefix + PROVINCE_NAME, getProvince(), false));
		text.append(buildOutput(isHTML, prefix + POSTAL_CODE_NAME, getPostalCode(), false));
		if (getCountryCode() != null)
			text.append(getCountryCode().getOutput(isHTML, prefix, ""));
		return (text.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.add(getCountryCode());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof PostalAddress))
			return (false);
		PostalAddress test = (PostalAddress) obj;
		return (Util.listEquals(getStreets(), test.getStreets()) 
			&& getCity().equals(test.getCity())
			&& getState().equals(test.getState()) 
			&& getProvince().equals(test.getProvince())
			&& getPostalCode().equals(test.getPostalCode()));
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
		return ("postalAddress");
	}
	
	/**
	 * Accessor for the street addresses (max 6)
	 */
	public List<String> getStreets() {
		return (Collections.unmodifiableList(_streets));
	}
	
	/**
	 * Accessor for the city
	 */
	public String getCity() {
		return (Util.getNonNullString(_city));
	}
	
	/**
	 * Accessor for the state
	 */
	public String getState() {
		return (Util.getNonNullString(_state));
	}
	
	/**
	 * Accessor for the province
	 */
	public String getProvince() {
		return (Util.getNonNullString(_province));
	}
	
	/**
	 * Accessor for the postalCode
	 */
	public String getPostalCode() {
		return (Util.getNonNullString(_postalCode));
	}

	/**
	 * Accessor for the country code
	 */
	public CountryCode getCountryCode() {
		return (_countryCode);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 6887962646280796652L;
		private List<String> _streets;
		private String _city;
		private String _state;
		private String _province;
		private String _postalCode;
		private CountryCode.Builder _countryCode;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(PostalAddress address) {
			setStreets(address.getStreets());
			setCity(address.getCity());
			setState(address.getState());
			setProvince(address.getProvince());
			setPostalCode(address.getPostalCode());
			if (address.getCountryCode() != null)
				setCountryCode(new CountryCode.Builder(address.getCountryCode()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public PostalAddress commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			boolean hasStateAndProvince = (!Util.isEmpty(getState()) && !Util.isEmpty(getProvince()));
			if (hasStateAndProvince)
				throw new InvalidDDMSException("Only 1 of state or province can be used.");
			boolean hasState = !Util.isEmpty(getState());
			String stateOrProvince = hasState ? getState() : getProvince();
			return (new PostalAddress(getStreets(), getCity(), stateOrProvince, getPostalCode(), 
				getCountryCode().commit(), hasState));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.containsOnlyEmptyValues(getStreets())
				&& Util.isEmpty(getCity())
				&& Util.isEmpty(getState())
				&& Util.isEmpty(getProvince())
				&& Util.isEmpty(getPostalCode())
				&& getCountryCode().isEmpty());
		}
		
		/**
		 * Builder accessor for the streets
		 */
		public List<String> getStreets() {
			if (_streets == null)
				_streets = new LazyList(String.class);
			return _streets;
		}
		
		/**
		 * Builder accessor for the streets
		 */
		public void setStreets(List<String> streets) {
			_streets = new LazyList(streets, String.class);
		}
		
		/**
		 * Builder accessor for the city
		 */
		public String getCity() {
			return _city;
		}
		
		/**
		 * Builder accessor for the city
		 */
		public void setCity(String city) {
			_city = city;
		}
		
		/**
		 * Builder accessor for the state
		 */
		public String getState() {
			return _state;
		}
		
		/**
		 * Builder accessor for the state
		 */
		public void setState(String state) {
			_state = state;
		}
		
		/**
		 * Builder accessor for the province
		 */
		public String getProvince() {
			return _province;
		}
		
		/**
		 * Builder accessor for the province
		 */
		public void setProvince(String province) {
			_province = province;
		}
		
		/**
		 * Builder accessor for the postalCode
		 */
		public String getPostalCode() {
			return _postalCode;
		}
		
		/**
		 * Builder accessor for the postalCode
		 */
		public void setPostalCode(String postalCode) {
			_postalCode = postalCode;
		}
		
		/**
		 * Builder accessor for the countryCode
		 */
		public CountryCode.Builder getCountryCode() {
			if (_countryCode == null) {
				_countryCode = new CountryCode.Builder();
			}
			return _countryCode;
		}
		
		/**
		 * Builder accessor for the countryCode
		 */
		public void setCountryCode(CountryCode.Builder countryCode) {
			_countryCode = countryCode;
		}		
	}
} 