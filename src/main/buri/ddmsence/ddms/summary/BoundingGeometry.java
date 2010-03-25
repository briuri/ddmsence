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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:boundingGeometry.
 *
 * <p>The DDMS documentation has no Text/HTML examples for the output of this component. However, the component has no additional attributes or elements besides
 * the nested Polygon/Point components, so no additional output is needed.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>gml:Polygon</u>: a polygon (0-many optional)<br />
 * <u>gml:Point</u>: a point (0-many optional)<br />
 * <p>At least 1 of Polygon or Point must be used.</p>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#GeographicBoundingGeometry<br />
 * <u>Description</u>: A wrapper for child elements used to express a geographic location as a point or polygon.<br />
 * <u>Obligation</u>: Optional in a geospatialCoverage element<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class BoundingGeometry extends AbstractBaseComponent {

	// Values are cached upon instantiation, so objects are only created once
	private List<Polygon> _cachedPolygons;
	private List<Point> _cachedPoints;
		
	/** The element name of this component */
	public static final String NAME = "boundingGeometry";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingGeometry(Element element) throws InvalidDDMSException {
		Util.requireDDMSValue("boundingGeometry element", element);
		_cachedPolygons = new ArrayList<Polygon>();
		_cachedPoints = new ArrayList<Point>();
		Elements polygons = element.getChildElements(Polygon.NAME, GML_NAMESPACE);
		for (int i = 0; i < polygons.size(); i++) {
			_cachedPolygons.add(new Polygon(polygons.get(i)));
		}
		Elements points = element.getChildElements(Point.NAME, GML_NAMESPACE);
		for (int i = 0; i < points.size(); i++) {
			_cachedPoints.add(new Point(points.get(i)));
		}
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param polygons an ordered list of the polygons used in this geometry
	 * @param points an ordered list of the points used in this geometry
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingGeometry(List<Polygon> polygons, List<Point> points) throws InvalidDDMSException {
		if (polygons == null)
			polygons = Collections.emptyList();
		if (points == null)
			points = Collections.emptyList();
		Element element = Util.buildDDMSElement(BoundingGeometry.NAME, null);
		for (Polygon polygon : polygons)
			element.appendChild(polygon.getXOMElementCopy());
		for (Point point : points)
			element.appendChild(point.getXOMElementCopy());
		_cachedPolygons = polygons;
		_cachedPoints = points;
		setXOMElement(element, true);
	}
		
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>At least 1 polygon or point exists.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */	
	protected void validate() throws InvalidDDMSException {
		super.validate();
		if (getPolygons().size() + getPoints().size() == 0) {
			throw new InvalidDDMSException("At least 1 of Polygon or Point must be used.");
		}
		
		for (Polygon polygon : getPolygons())
			addWarnings(polygon.getValidationWarnings());
		for (Point point : getPoints())
			addWarnings(point.getValidationWarnings());		
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		for (Polygon polygon : getPolygons())
			html.append(polygon.toHTML());
		for (Point point : getPoints())
			html.append(point.toHTML());
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		for (Polygon polygon : getPolygons())
			text.append(polygon.toText());
		for (Point point : getPoints())
			text.append(point.toText());
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof BoundingGeometry))
			return (false);
		BoundingGeometry test = (BoundingGeometry) obj;
		return (Util.listEquals(getPolygons(), test.getPolygons())) &&
			Util.listEquals(getPoints(), test.getPoints());
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getPolygons().hashCode();
		result = 7 * result + getPoints().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the polygons in this geometry.
	 */
	public List<Polygon> getPolygons() {
		return (Collections.unmodifiableList(_cachedPolygons)); 
	}
	
	/**
	 * Accessor for the points in this geometry.
	 */
	public List<Point> getPoints() {
		return (Collections.unmodifiableList(_cachedPoints)); 
	}
} 