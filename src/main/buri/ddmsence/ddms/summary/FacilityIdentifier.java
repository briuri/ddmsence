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

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:facilityIdentifier.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The beNumber value must be non-empty.</li>
 * <li>The osuffix value must be non-empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:beNumber</u>: uniquely identifies the installation of the facility (required).<br />
 * <u>ddms:osuffix</u>: identifies a facility in conjunction with a beNumber (required if beNumber is set).<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#geospatialCoverage_GeospatialExtent_geographicIdentifier_facilityIdentifier<br />
 * <u>Description</u>: Information about rights held in and over the resource<br />
 * <u>Obligation</u>: Optional nested in a geospatialCoverage element, but if used, must be used alone<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 *  
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class FacilityIdentifier extends AbstractBaseComponent {

	/** The element name of this component */
	public static final String NAME = "facilityIdentifier";
	
	private static final String BE_NUMBER_NAME = "beNumber";
	private static final String OSUFFIX_NAME = "osuffix";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public FacilityIdentifier(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param beNumber the beNumber (required)
	 * @param osuffix the Osuffix (required, because beNumber is required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public FacilityIdentifier(String beNumber, String osuffix) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(FacilityIdentifier.NAME, null);
			Util.addDDMSAttribute(element, BE_NUMBER_NAME, beNumber);
			Util.addDDMSAttribute(element, OSUFFIX_NAME, osuffix);
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
	 * <li>The qualified name of the element is correct.</li>
	 * <li>A beNumber exists and is non-empty.</li>
	 * <li>An osuffix exists and is non-empty.</li>
	 * <li>Does not validate whether the attributes have logical values.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
		Util.requireDDMSValue(BE_NUMBER_NAME, getBeNumber());
		Util.requireDDMSValue(OSUFFIX_NAME, getOsuffix());
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("geospatial.identifier.facility.BEnumber", getBeNumber(), true));
		html.append(buildHTMLMeta("geospatial.identifier.facility.Osuffix", getOsuffix(), true));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Geographic Identifier BEnumber", getBeNumber(), true));
		text.append(buildTextLine("Geographic Identifier Osuffix", getOsuffix(), true));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof FacilityIdentifier))
			return (false);
		FacilityIdentifier test = (FacilityIdentifier) obj;
		return (getBeNumber().equals(test.getBeNumber()) 
			&& getOsuffix().equals(test.getOsuffix()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getBeNumber().hashCode();
		result = 7 * result + getOsuffix().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the beNumber attribute.
	 */
	public String getBeNumber() {
		return (getAttributeValue(BE_NUMBER_NAME)); 
	}
	
	/**
	 * Accessor for the osuffix attribute.
	 */
	public String getOsuffix() {
		return (getAttributeValue(OSUFFIX_NAME)); 
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 4781523669271343048L;
		private String _beNumber;
		private String _osuffix;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(FacilityIdentifier facilityIdentifier) {
			setBeNumber(facilityIdentifier.getBeNumber());
			setOsuffix(facilityIdentifier.getOsuffix());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public FacilityIdentifier commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new FacilityIdentifier(getBeNumber(), getOsuffix()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getBeNumber()) && Util .isEmpty(getOsuffix()));
		}
		
		/**
		 * Builder accessor for the beNumber attribute.
		 */
		public String getBeNumber() {
			return _beNumber;
		}

		/**
		 * Builder accessor for the beNumber attribute.
		 */
		public void setBeNumber(String beNumber) {
			_beNumber = beNumber;
		}

		/**
		 * Builder accessor for the osuffix attribute.
		 */
		public String getOsuffix() {
			return _osuffix;
		}

		/**
		 * Builder accessor for the osuffix attribute.
		 */
		public void setOsuffix(String osuffix) {
			_osuffix = osuffix;
		}
	}
} 