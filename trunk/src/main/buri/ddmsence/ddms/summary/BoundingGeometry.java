/* Copyright 2010 - 2013 by Brian Uri!
   
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
import nu.xom.Elements;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.summary.gml.Point;
import buri.ddmsence.ddms.summary.gml.Polygon;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:boundingGeometry.
 * <br /><br />
 * {@ddms.versions 11111}
 * 
 * <p></p>
 * 
 *  {@table.header History}
 *  	<p>The GML shapes were replaced by TSPI shapes in DDMS 5.0.</p>
 * {@table.footer}
 * {@table.header Nested Elements}
 * 		{@child.info gml:Polygon|0..1|11110}
 * 		{@child.info gml:Point|0..1|11110}
 * 		{@child.info tspi:Polygon|0..1|00001} 
 * 		{@child.info tspi:Point|0..1|00001}
 * 		{@child.info tspi:Envelope|0..1|00001}
 *  	{@child.info tspi:Circle|0..1|00001}
 *   	{@child.info tspi:Ellipse|0..1|00001}
 * {@table.footer}
 * {@table.header Attributes}
 * 		None.
 * {@table.footer}
 * {@table.header Validation Rules}
 * 		{@ddms.rule The qualified name of this element is correct.|Error|11111}
 * 		{@ddms.rule At least 1 shape is required.|Error|11111}
 * 		{@ddms.rule TSPI shapes are not used before the DDMS version in which they were introduced.|Error|11111}
 * 		{@ddms.rule GML shapes are not used after the DDMS version in which they were removed.|Error|11111}
 * {@table.footer}
 *  
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class BoundingGeometry extends AbstractBaseComponent {

	private List<Polygon> _polygons = null;
	private List<Point> _points = null;

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingGeometry(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("boundingGeometry element", element);
			setXOMElement(element, false);
			String gmlNamespace = getDDMSVersion().getGmlNamespace();
			_polygons = new ArrayList<Polygon>();
			_points = new ArrayList<Point>();
			Elements polygons = element.getChildElements(Polygon.getName(getDDMSVersion()), gmlNamespace);
			for (int i = 0; i < polygons.size(); i++) {
				_polygons.add(new Polygon(polygons.get(i)));
			}
			Elements points = element.getChildElements(Point.getName(getDDMSVersion()), gmlNamespace);
			for (int i = 0; i < points.size(); i++) {
				_points.add(new Point(points.get(i)));
			}
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
	 * @param polygons an ordered list of the polygons used in this geometry
	 * @param points an ordered list of the points used in this geometry
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingGeometry(List<Polygon> polygons, List<Point> points) throws InvalidDDMSException {
		try {
			if (polygons == null)
				polygons = Collections.emptyList();
			if (points == null)
				points = Collections.emptyList();
			Element element = Util.buildDDMSElement(BoundingGeometry.getName(DDMSVersion.getCurrentVersion()), null);
			for (Polygon polygon : polygons)
				element.appendChild(polygon.getXOMElementCopy());
			for (Point point : points)
				element.appendChild(point.getXOMElementCopy());
			_polygons = polygons;
			_points = points;
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), BoundingGeometry.getName(getDDMSVersion()));
		if (getPolygons().size() + getPoints().size() == 0) {
			throw new InvalidDDMSException("At least 1 of Polygon or Point must be used.");
		}
		// TODO: TSPI vs GML Checks #193
		super.validate();
	}

	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix + ".");
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix, getPolygons()));
		text.append(buildOutput(isHTML, localPrefix, getPoints()));
		return (text.toString());
	}

	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.addAll(getPoints());
		list.addAll(getPolygons());
		return (list);
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof BoundingGeometry))
			return (false);
		return (true);
	}

	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("boundingGeometry");
	}

	/**
	 * Accessor for the polygons in this geometry.
	 */
	public List<Polygon> getPolygons() {
		return (Collections.unmodifiableList(_polygons));
	}

	/**
	 * Accessor for the points in this geometry.
	 */
	public List<Point> getPoints() {
		return (Collections.unmodifiableList(_points));
	}

	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -5734267242408462644L;
		private List<Polygon.Builder> _polygons;
		private List<Point.Builder> _points;

		/**
		 * Empty constructor
		 */
		public Builder() {}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(BoundingGeometry geometry) {
			for (Polygon polygon : geometry.getPolygons())
				getPolygons().add(new Polygon.Builder(polygon));
			for (Point point : geometry.getPoints())
				getPoints().add(new Point.Builder(point));
		}

		/**
		 * @see IBuilder#commit()
		 */
		public BoundingGeometry commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<Polygon> polygons = new ArrayList<Polygon>();
			for (Polygon.Builder builder : getPolygons()) {
				Polygon polygon = builder.commit();
				if (polygon != null)
					polygons.add(polygon);
			}
			List<Point> points = new ArrayList<Point>();
			for (Point.Builder builder : getPoints()) {
				Point point = builder.commit();
				if (point != null)
					points.add(point);
			}
			return (new BoundingGeometry(polygons, points));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getChildBuilders()) {
				hasValueInList = hasValueInList || !builder.isEmpty();
			}
			return (!hasValueInList);
		}

		/**
		 * Convenience method to get every child Builder in this Builder.
		 * 
		 * @return a list of IBuilders
		 */
		private List<IBuilder> getChildBuilders() {
			List<IBuilder> list = new ArrayList<IBuilder>();
			list.addAll(getPolygons());
			list.addAll(getPoints());
			return (list);
		}

		/**
		 * Builder accessor for the polygons in this geometry.
		 */
		public List<Polygon.Builder> getPolygons() {
			if (_polygons == null)
				_polygons = new LazyList(Polygon.Builder.class);
			return _polygons;
		}

		/**
		 * Builder accessor for the points in this geometry.
		 */
		public List<Point.Builder> getPoints() {
			if (_points == null)
				_points = new LazyList(Point.Builder.class);
			return _points;
		}
	}
}