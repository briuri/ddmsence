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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:geographicIdentifier.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>No more than 1 countryCode or facilityIdentifier can be used. The schema seems to support this assertion with
 * explicit restrictions on those elements, but the enclosing xs:choice element allows multiples.</li>
 * <li>At least 1 of name, region, countryCode, or facilityIdentifier must be present. Once again, the xs:choice 
 * restrictions create a loophole which could allow a completely empty geographicIdentifier to be valid.</li>
 * </ul>
 * </td></tr></table>
 * 				
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:name</u>: geographic name (0-many optional)<br />
 * <u>ddms:region</u>: geographic region (0-many optional)<br />
 * <u>ddms:countryCode</u>: the country code (0-1 optional), implemented as a {@link CountryCode}<br />
 * <u>ddms:facilityIdentifier</u>: the facility identifier (0-1 optional), implemented as a 
 * {@link FacilityIdentifier}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#GeographicIdentifier<br />
 * <u>Description</u>: A wrapper for an identifier or reference to an identifier that describes a geographic extent 
 * using a name or other identifier.<br />
 * <u>Obligation</u>: Mandatory unless Not Applicable<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class GeographicIdentifier extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<String> _cachedNames = null;
	private List<String> _cachedRegions = null;
	private CountryCode _cachedCountryCode = null;
	private FacilityIdentifier _cachedFacilityIdentifier = null;
	
	/** The element name of this component */
	public static final String NAME = "geographicIdentifier";
	
	private static final String NAME_NAME = "name";
	private static final String REGION_NAME = "region";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public GeographicIdentifier(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("geographicIdentifier element", element);
			_cachedNames = Util.getDDMSChildValues(element, NAME_NAME);
			_cachedRegions = Util.getDDMSChildValues(element, REGION_NAME);
			Element countryCodeElement = element.getFirstChildElement(CountryCode.NAME, element.getNamespaceURI());
			if (countryCodeElement != null)
				_cachedCountryCode = new CountryCode(GeographicIdentifier.NAME, countryCodeElement);
			Element facilityIdentifierElement = element.getFirstChildElement(FacilityIdentifier.NAME, element
				.getNamespaceURI());
			if (facilityIdentifierElement != null)
				_cachedFacilityIdentifier = new FacilityIdentifier(facilityIdentifierElement);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data. Note that the facilityIdentifier component cannot be used
	 * with the components in this constructor.
	 * 
	 * @param names the names (optional)
	 * @param regions the region names (optional)
	 * @param countryCode the country code (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public GeographicIdentifier(List<String> names, List<String> regions, CountryCode countryCode)
		throws InvalidDDMSException {
		try {
			if (names == null)
				names = Collections.emptyList();
			if (regions == null)
				regions = Collections.emptyList();
			Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
			for (String name : names) {
				element.appendChild(Util.buildDDMSElement(NAME_NAME, name));
			}
			for (String region : regions) {
				element.appendChild(Util.buildDDMSElement(REGION_NAME, region));
			}
			if (countryCode != null)
				element.appendChild(countryCode.getXOMElementCopy());
			_cachedNames = names;
			_cachedRegions = regions;
			_cachedCountryCode = countryCode;
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param facilityIdentifier the facility identifier (required in this constructor)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public GeographicIdentifier(FacilityIdentifier facilityIdentifier) throws InvalidDDMSException {
		Element element = Util.buildDDMSElement(GeographicIdentifier.NAME, null);
		if (facilityIdentifier != null)
			element.appendChild(facilityIdentifier.getXOMElementCopy());
		_cachedNames = Collections.emptyList();
		_cachedRegions = Collections.emptyList();
		_cachedFacilityIdentifier = facilityIdentifier;
		setXOMElement(element, true);
	}
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>At least 1 of name, region, countryCode, or facilityIdentifier must exist.</li>
	 * <li>No more than 1 countryCode or facilityIdentifier can exist.</li>
	 * <li>If facilityIdentifier is used, no other components can exist.</li>
	 * <li>If a countryCode or facilityIdentifier exists, it is using the same version of DDMS.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		if (getNames().isEmpty() && getRegions().isEmpty() && getCountryCode() == null
			&& getFacilityIdentifier() == null) {
			throw new InvalidDDMSException("At least 1 of name, region, countryCode, or facilityIdentifier must exist.");
		}
		Util.requireBoundedDDMSChildCount(getXOMElement(), CountryCode.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), FacilityIdentifier.NAME, 0, 1);
		if (hasFacilityIdentifier()) {
			Util.requireSameVersion(this, getFacilityIdentifier());
			if (!getNames().isEmpty() || !getRegions().isEmpty() || getCountryCode() != null)
				throw new InvalidDDMSException("facilityIdentifier cannot be used in tandem with other components.");
		}		
		if (getCountryCode() != null)
			Util.requireSameVersion(this, getCountryCode());
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the facility identifier or country code.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (hasFacilityIdentifier())
			addWarnings(getFacilityIdentifier().getValidationWarnings(), false);
		if (getCountryCode() != null)
			addWarnings(getCountryCode().getValidationWarnings(), false);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		for (String name : getNames())
			html.append(buildHTMLMeta("geospatial.identifier.name", name, false));
		for (String region : getRegions())
			html.append(buildHTMLMeta("geospatial.identifier.region", region, false));
		if (getCountryCode() != null)
			html.append(getCountryCode().toHTML());
		if (hasFacilityIdentifier())
			html.append(getFacilityIdentifier().toHTML());
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		for (String name : getNames())
			text.append(buildTextLine("Geographic Identifier Name", name, false));
		for (String region : getRegions())
			text.append(buildTextLine("Geographic Identifier Region", region, false));
		if (getCountryCode() != null)
			text.append(getCountryCode().toText());
		if (hasFacilityIdentifier())
			text.append(getFacilityIdentifier().toText());
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof GeographicIdentifier))
			return (false);
		GeographicIdentifier test = (GeographicIdentifier) obj;
		boolean isEqual = Util.listEquals(getNames(), test.getNames())
			&& Util.listEquals(getRegions(), test.getRegions())
			&& Util.nullEquals(getCountryCode(), test.getCountryCode())
			&& Util.nullEquals(getFacilityIdentifier(), test.getFacilityIdentifier());
		return (isEqual);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getNames().hashCode();
		result = 7 * result + getRegions().hashCode();
		if (getCountryCode() != null)
			result = 7 * result + getCountryCode().hashCode();
		if (hasFacilityIdentifier())
			result = 7 * result + getFacilityIdentifier().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the names
	 */
	public List<String> getNames() {
		return (Collections.unmodifiableList(_cachedNames));
	}
	
	/**
	 * Accessor for the regions
	 */
	public List<String> getRegions() {
		return (Collections.unmodifiableList(_cachedRegions));
	}
	
	/**
	 * Accessor for the country code. May return null if no code was used.
	 */
	public CountryCode getCountryCode() {
		return (_cachedCountryCode);
	}
	
	/**
	 * Accessor for the facility identifier. May return null if no identifier was used.
	 */
	public FacilityIdentifier getFacilityIdentifier() {
		return (_cachedFacilityIdentifier);
	}
	
	/**
	 * Accessor for whether this geographic identifier is using a facility identifier.
	 */
	public boolean hasFacilityIdentifier() {
		return (getFacilityIdentifier() != null);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder {
		private List<String> _names = null;
		private List<String> _regions = null;
		private CountryCode.Builder _countryCode = null;
		private FacilityIdentifier.Builder _facilityIdentifier = null;
				
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(GeographicIdentifier identifier) {
			if (identifier.hasFacilityIdentifier())
				setFacilityIdentifier(new FacilityIdentifier.Builder(identifier.getFacilityIdentifier()));
			else {
				setNames(identifier.getNames());
				setRegions(identifier.getRegions());
				setCountryCode(new CountryCode.Builder(identifier.getCountryCode()));
			}
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public GeographicIdentifier commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			FacilityIdentifier identifier = getFacilityIdentifier().commit();
			if (identifier != null)
				return (new GeographicIdentifier(identifier));
			return (new GeographicIdentifier(getNames(), getRegions(), getCountryCode().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.containsOnlyEmptyValues(getNames())
				&& Util.containsOnlyEmptyValues(getRegions())
				&& getCountryCode().isEmpty()
				&& getFacilityIdentifier().isEmpty());
		}
		
		/**
		 * Builder accessor for the names
		 */
		public List<String> getNames() {
			if (_names == null)
				_names = new ArrayList<String>();
			return _names;
		}

		/**
		 * Builder accessor for the names
		 */
		public void setNames(List<String> names) {
			_names = names;
		}

		/**
		 * Builder accessor for the regions
		 */
		public List<String> getRegions() {
			if (_regions == null)
				_regions = new ArrayList<String>();
			return _regions;
		}

		/**
		 * Builder accessor for the regions
		 */
		public void setRegions(List<String> regions) {
			_regions = regions;
		}

		/**
		 * Builder accessor for the country code
		 */
		public CountryCode.Builder getCountryCode() {
			if (_countryCode == null) {
				_countryCode = new CountryCode.Builder();
				_countryCode.setParentType(GeographicIdentifier.NAME);
			}
			return _countryCode;
		}

		/**
		 * Builder accessor for the country code
		 */
		public void setCountryCode(CountryCode.Builder countryCode) {
			if (countryCode != null)
				countryCode.setParentType(GeographicIdentifier.NAME);
			_countryCode = countryCode;
		}

		/**
		 * Builder accessor for the facility identifier
		 */
		public FacilityIdentifier.Builder getFacilityIdentifier() {
			if (_facilityIdentifier == null)
				_facilityIdentifier = new FacilityIdentifier.Builder();
			return _facilityIdentifier;
		}

		/**
		 * Builder accessor for the facility identifier
		 */
		public void setFacilityIdentifier(FacilityIdentifier.Builder facilityIdentifier) {
			_facilityIdentifier = facilityIdentifier;
		}
	}
} 