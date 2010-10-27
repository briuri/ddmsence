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

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:boundingBox.
 *
 * <p>The DDMS documentation has no Text/HTML examples for the output of this component, so a best guess was taken:</p>
 * <ul>
 * <p><b>Suggested Text Output</b><br /><code>
 * Bounding Box Westbound Longitude: value<br />
 * Bounding Box Eastbound Longitude: value<br />
 * Bounding Box Southbound Latitude: value<br />
 * Bounding Box Northbound Latitude: value<br />
 * </code></p>
 * 
 * <p><b>Suggested HTML Output</b><br /><code>
 * &lt;meta name="geospatial.boundingBox.WestBL" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingBox.EastBL" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingBox.SouthBL" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingBox.NorthBL" content="value" /&gt;<br />
 * </code>
 * </ul></p>
 *
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:WestBL</u>: westbound longitude (required)<br />
 * <u>ddms:EastBL</u>: eastbound longitude (required)<br />
 * <u>ddms:SouthBL</u>: northbound latitude (required)<br />
 * <u>ddms:NorthBL</u>: southbound latitude (required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#GeographicBoundingBox<br />
 * <u>Description</u>: A wrapper for elements containing the bounding longitudes and latitudes for describing a 
 * geographic extent.<br />
 * <u>Obligation</u>: Optional in a geospatialCoverage element<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class BoundingBox extends AbstractBaseComponent {

	// Values are cached upon instantiation, so doubles are only generated once.
	private Double _cachedWestBL = null;
	private Double _cachedEastBL = null;
	private Double _cachedSouthBL = null;
	private Double _cachedNorthBL = null;
		
	/** The element name of this component */
	public static final String NAME = "boundingBox";
	
	private static final String WEST_BL_NAME = "WestBL";
	private static final String EAST_BL_NAME = "EastBL";
	private static final String SOUTH_BL_NAME = "SouthBL";
	private static final String NORTH_BL_NAME = "NorthBL";
		
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingBox(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("boundingBox element", element);
			_cachedWestBL = getChildTextAsDouble(element, WEST_BL_NAME);
			_cachedEastBL = getChildTextAsDouble(element, EAST_BL_NAME);
			_cachedSouthBL = getChildTextAsDouble(element, SOUTH_BL_NAME);
			_cachedNorthBL = getChildTextAsDouble(element, NORTH_BL_NAME);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param westBL the westbound longitude
	 * @param eastBL the eastbound longitude
	 * @param southBL the southbound latitude
	 * @param northBL the northbound latitude
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingBox(double westBL, double eastBL, double southBL, double northBL) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(BoundingBox.NAME, null);
			element.appendChild(Util.buildDDMSElement(WEST_BL_NAME, String.valueOf(westBL)));
			element.appendChild(Util.buildDDMSElement(EAST_BL_NAME, String.valueOf(eastBL)));
			element.appendChild(Util.buildDDMSElement(SOUTH_BL_NAME, String.valueOf(southBL)));
			element.appendChild(Util.buildDDMSElement(NORTH_BL_NAME, String.valueOf(northBL)));
			_cachedWestBL = Double.valueOf(westBL);
			_cachedEastBL = Double.valueOf(eastBL);
			_cachedSouthBL = Double.valueOf(southBL);
			_cachedNorthBL = Double.valueOf(northBL);
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
	 * <li>A westBL exists.</li>
	 * <li>An eastBL exists.</li>
	 * <li>A southBL exists.</li>
	 * <li>A northBL exists.</li>
	 * <li>westBL and eastBL must be between -180 and 180 degrees.</li>
	 * <li>southBL and northBL must be between -90 and 90 degrees.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */	
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		Util.requireDDMSValue("westbound longitude", getWestBL());
		Util.requireDDMSValue("eastbound longitude", getEastBL());
		Util.requireDDMSValue("southbound latitude", getSouthBL());
		Util.requireDDMSValue("northbound latitude", getNorthBL());
		Util.requireValidLongitude(getWestBL());
		Util.requireValidLongitude(getEastBL());
		Util.requireValidLatitude(getSouthBL());
		Util.requireValidLongitude(getNorthBL());
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("geospatial.boundingBox.WestBL", String.valueOf(getWestBL()), true));
		html.append(buildHTMLMeta("geospatial.boundingBox.EastBL", String.valueOf(getEastBL()), true));
		html.append(buildHTMLMeta("geospatial.boundingBox.SouthBL", String.valueOf(getSouthBL()), true));
		html.append(buildHTMLMeta("geospatial.boundingBox.NorthBL", String.valueOf(getNorthBL()), true));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Bounding Box Westbound Longitude", String.valueOf(getWestBL()), true));
		text.append(buildTextLine("Bounding Box Eastbound Longitude", String.valueOf(getEastBL()), true));
		text.append(buildTextLine("Bounding Box Southbound Latitude", String.valueOf(getSouthBL()), true));
		text.append(buildTextLine("Bounding Box Northbound Latitude", String.valueOf(getNorthBL()), true));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof BoundingBox))
			return (false);
		BoundingBox test = (BoundingBox) obj;
		return (getWestBL().equals(test.getWestBL()) 
			&& getEastBL().equals(test.getEastBL())
			&& getSouthBL().equals(test.getSouthBL()) 
			&& getNorthBL().equals(test.getNorthBL()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getWestBL().hashCode();
		result = 7 * result + getEastBL().hashCode();
		result = 7 * result + getSouthBL().hashCode();
		result = 7 * result + getNorthBL().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the westbound longitude.
	 */
	public Double getWestBL() {
		return (_cachedWestBL); 
	}
	
	/**
	 * Accessor for the eastbound longitude.
	 */
	public Double getEastBL() {
		return (_cachedEastBL); 
	}
	
	/**
	 * Accessor for the southbound latitude.
	 */
	public Double getSouthBL() {
		return (_cachedSouthBL); 
	}
	
	/**
	 * Accessor for the northbound latitude.
	 */
	public Double getNorthBL() {
		return (_cachedNorthBL); 
	}
} 