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
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of gml:pos.
 * 
 * <p>
 * The DDMS documentation has no Text/HTML examples for the output of this component, so a best guess was taken
 * (suggestions are welcome, as this is probably not an optimal solution):
 * </p>
 * <ul>
 * <p>
 * <b>Suggested Text Output</b><br />
 * <code>
 * Geospatial Geometry Position: value<br />
 * Geospatial Geometry Position SRS Name: value<br />
 * Geospatial Geometry Position SRS Dimension: value<br />
 * Geospatial Geometry Position Axis Labels: value<br />
 * Geospatial Geometry Position Unit of Measure Labels: value<br />
 * </code>
 * </p>
 * 
 * <p>
 * <b>Suggested HTML Output</b><br />
 * <code>
 * &lt;meta name="geospatial.boundingGeometry.position" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.position.srsName" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.position.srsDimension" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.position.axisLabels" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.position.uomLabels" content="value" /&gt;<br />
 * </code>
 * </p>
 * </ul>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A position must either have 2 coordinates (to comply with WGS84E_2D) or 3 coordinates (to comply with WGS84E_3D).
 * </li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>srsName</u>: A URI-based name (optional)<br />
 * <u>srsDimension</u>: A positive integer dimension (optional)<br />
 * <u>axisLabels</u>: Ordered list of labels for the axes, as a space-delimited list of NCNames (valid XML names without
 * colons) (optional, but if no srsName is set, this should be omitted too)<br />
 * <u>uomLabels</u>: Ordered list of unit of measure (uom) labels for all the axes, as a space-delimited list of NCNames
 * (valid XML names without colons) (required when axisLabels is set)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#Point<br />
 * <u>Description</u>: This component is used to build gml:Polygon or gml:Point.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Position extends AbstractBaseComponent {

	// Values are cached upon instantiation, so objects are only created once
	private SRSAttributes _cachedSrsAttributes;
	private List<Double> _cachedCoordinates;	
	
	/** The element name of this component */
	public static final String NAME = "pos";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Position(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			List<String> tuple = Util.getAsList(getCoordinatesAsXsList());
			_cachedCoordinates = new ArrayList<Double>();
			for (String coordinate : tuple) {
				_cachedCoordinates.add(getStringAsDouble(coordinate));
			}
			_cachedSrsAttributes = new SRSAttributes(element);
			setXOMElement(element, true);

		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * @param coordinates a list of either 2 or 3 coordinate Double values
	 * @param srsAttributes the attribute group containing srsName, srsDimension, axisLabels, and uomLabels
	 *  
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Position(List<Double> coordinates, SRSAttributes srsAttributes) throws InvalidDDMSException {
		try {
			if (coordinates == null)
				coordinates = Collections.emptyList();
			_cachedSrsAttributes = (srsAttributes == null ? new SRSAttributes(null, null, null, null) : srsAttributes);
			_cachedCoordinates = coordinates;
			Element element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Position.NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), Util.getXsList(coordinates));
			if (srsAttributes != null)
				srsAttributes.addTo(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Helper method to assist with string to double conversion
	 * 
	 * @param string the double as a string
	 * @return a Double if possible, or null if the string cannot be converted
	 */
	private Double getStringAsDouble(String string) {
		if (Util.isEmpty(string))
			return (null);
		try {
			return (Double.valueOf(string));
		}
		catch (NumberFormatException e) {
			return (null);
		}
	}
		
	/**
	 * @see IDDMSComponent#getDDMSVersion()
	 */
	public String getDDMSVersion() {
		return (DDMSVersion.getVersionForGmlNamespace(getXOMElement().getNamespaceURI()).getVersion());
	}
			
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>Each coordinate is a valid Double value.</li>
	 * <li>The position is represented by 2 or 3 coordinates.</li>
	 * <li>The first coordinate is a valid latitude.</li>
	 * <li>The second coordinate is a valid longitude.</li>
	 * <li>Does not perform any special validation on the third coordinate (height above ellipsoid).</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getGmlNamespace(), NAME);
		for (Double coordinate : getCoordinates())
			Util.requireDDMSValue("coordinate", coordinate);
		if (!Util.isBounded(getCoordinates().size(), 2, 3))
			throw new InvalidDDMSException("A position must be represented by either 2 or 3 coordinates.");
		Util.requireValidLatitude(getCoordinates().get(0));
		Util.requireValidLongitude(getCoordinates().get(1));
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the SRS attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		addWarnings(getSRSAttributes().getValidationWarnings(), true);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("geospatial.boundingGeometry.position", getCoordinatesAsXsList(), true));
		html.append(buildHTMLMeta("geospatial.boundingGeometry.position.srsName", getSRSAttributes().getSrsName(),
			false));
		if (getSRSAttributes().getSrsDimension() != null) {
			html.append(buildHTMLMeta("geospatial.boundingGeometry.position.srsDimension", 
				String.valueOf(getSRSAttributes().getSrsDimension()), false));
		}
		html.append(buildHTMLMeta("geospatial.boundingGeometry.position.axisLabels", 
			getSRSAttributes().getAxisLabelsAsXsList(), false));
		html.append(buildHTMLMeta("geospatial.boundingGeometry.position.uomLabels", 
			getSRSAttributes().getUomLabelsAsXsList(), false));
		return (html.toString());
	}

	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Geospatial Geometry Position", getCoordinatesAsXsList(), true));
		text.append(buildTextLine("Geospatial Geometry Position SRS Name", 
			getSRSAttributes().getSrsName(), false));
		if (getSRSAttributes().getSrsDimension() != null) {
			text.append(buildTextLine("Geospatial Geometry Position SRS Dimension", 
				String.valueOf(getSRSAttributes().getSrsDimension()), false));
		}
		text.append(buildTextLine("Geospatial Geometry Position Axis Labels", 
			getSRSAttributes().getAxisLabelsAsXsList(), false));
		text.append(buildTextLine("Geospatial Geometry Position Unit of Measure Labels", 
			getSRSAttributes().getUomLabelsAsXsList(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Position))
			return (false);
		Position test = (Position) obj;
		return (getSRSAttributes().equals(test.getSRSAttributes()) 
			&& getCoordinatesAsXsList().equals(test.getCoordinatesAsXsList()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getSRSAttributes().hashCode();
		result = 7 * result + getCoordinatesAsXsList().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the SRS Attributes. Will always be non-null, even if the attributes inside are not set.
	 */
	public SRSAttributes getSRSAttributes() {
		return (_cachedSrsAttributes);
	}
	
	/**
	 * Accessor for the coordinates of the position. May return null, but cannot happen after instantiation.
	 */
	public List<Double> getCoordinates() {
		return (Collections.unmodifiableList(_cachedCoordinates));
	}
		
	/**
	 * Accessor for the String representation of the coordinates
	 */
	public String getCoordinatesAsXsList() {
		return (getXOMElement().getValue());
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder {
		private SRSAttributes.Builder _srsAttributes;
		private List<Double> _coordinates;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Position position) {
			setSrsAttributes(new SRSAttributes.Builder(position.getSRSAttributes()));
			setCoordinates(position.getCoordinates());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Position commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<Double> coordinates = new ArrayList<Double>();
			for (Double coord : getCoordinates()) {
				if (coord != null)
					coordinates.add(coord);
			}	
			return (new Position(coordinates, getSrsAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasCoordinate = false;
			for (Double coord : getCoordinates()) {
				hasCoordinate = hasCoordinate || (coord != null);
			}
			return (!hasCoordinate && getSrsAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the SRS Attributes
		 */
		public SRSAttributes.Builder getSrsAttributes() {
			if (_srsAttributes == null)
				_srsAttributes = new SRSAttributes.Builder();
			return _srsAttributes;
		}
		
		/**
		 * Builder accessor for the SRS Attributes
		 */
		public void setSrsAttributes(SRSAttributes.Builder srsAttributes) {
			_srsAttributes = srsAttributes;
		}
		
		/**
		 * Builder accessor for the coordinates of the position
		 */
		public List<Double> getCoordinates() {
			if (_coordinates == null)
				_coordinates = new ArrayList<Double>();
			return _coordinates;
		}
		
		/**
		 * Builder accessor for the coordinates of the position
		 */
		public void setCoordinates(List<Double> coordinates) {
			_coordinates = coordinates;
		}			
	}
} 