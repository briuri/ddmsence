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
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of gml:Point.
 * 
 * <p>
 * The DDMS documentation has no Text/HTML examples for the output of this component, so a best guess was taken
 * (suggestions are welcome, as this is probably not an optimal solution):
 * </p>
 * 
 * <ul>
 * <p>
 * <b>Suggested Text Output</b><br />
 * <code>
 * Geospatial Geometry ID: value<br />
 * Geospatial Geometry Type: Point<br />
 * Geospatial Geometry SRS Name: value<br />
 * Geospatial Geometry SRS Dimension: value<br />
 * Geospatial Geometry Axis Labels: value<br />
 * Geospatial Geometry Unit of Measure Labels: value<br />
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
 * &lt;meta name="geospatial.boundingGeometry.id" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.type" content="Point" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.srsName" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.srsDimension" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.axisLabels" content="value" /&gt;<br />
 * &lt;meta name="geospatial.boundingGeometry.uomLabels" content="value" /&gt;<br />
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
 * <li>The srsName must also be non-empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>gml:pos</u>: the position (exactly 1 required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>srsName</u>: A URI-based name (required)<br />
 * <u>srsDimension</u>: A positive integer dimension (optional)<br />
 * <u>axisLabels</u>: Ordered list of labels for the axes, as a space-delimited list of NCNames (valid XML names without
 * colons) (optional, but if no srsName is set, this should be omitted too)<br />
 * <u>uomLabels</u>: Ordered list of unit of measure (uom) labels for all the axes, as a space-delimited list of NCNames
 * (valid XML names without colons) (required when axisLabels is set)<br />
 * <u>gml:id</u>: unique ID (required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Point<br />
 * <u>Description</u>: Specifies a position using a single coordinate tuple.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Point extends AbstractBaseComponent {

	// Values are cached upon instantiation, so objects are only created once
	private SRSAttributes _cachedSrsAttributes;
	private Position _cachedPosition;
	
	/** The element name of this component */
	public static final String NAME = "Point";
	
	private static final String ID_NAME = "id";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Point(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			Element posElement = element.getFirstChildElement(Position.NAME, 
				element.getNamespaceURI());
			if (posElement != null)
				_cachedPosition = new Position(posElement);
			_cachedSrsAttributes = new SRSAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param position the position of the Point (required)
	 * @param srsAttributes the attribute group containing srsName, srsDimension, axisLabels, and uomLabels (srsName
	 * required)
	 * @param id the id value (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Point(Position position, SRSAttributes srsAttributes, String id) throws InvalidDDMSException {
		try {
			_cachedPosition = position;
			_cachedSrsAttributes = srsAttributes;
			Element element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Point.NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), null);
			if (position != null) {
				element.appendChild(position.getXOMElementCopy());
			}
			if (srsAttributes != null)
				srsAttributes.addTo(element);
			Util.addAttribute(element, PropertyReader.getProperty("gml.prefix"), ID_NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), id);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
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
	 * <li>The srsName is required.</li>
	 * <li>The ID is required, and must be a valid NCName.</li>
	 * <li>If the position has an srsName, it matches the srsName of this Point.</li>
	 * <li>Does not perform any special validation on the third coordinate (height above ellipsoid).</li>
	 * </td></tr></table>
	 *
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getGmlNamespace(), NAME);
		Util.requireDDMSValue("srsAttributes", getSRSAttributes());
		Util.requireDDMSValue("srsName", getSRSAttributes().getSrsName());
		Util.requireDDMSValue(ID_NAME, getId());
		Util.requireValidNCName(getId());
		Util.requireDDMSValue("position", getPosition());
		Util.requireSameVersion(this, getPosition());
		String srsName = getPosition().getSRSAttributes().getSrsName();
		if (!Util.isEmpty(srsName) && !srsName.equals(getSRSAttributes().getSrsName()))
			throw new InvalidDDMSException("The srsName of the position must match the srsName of the Point.");
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the SRS attributes and the child position.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		addWarnings(getPosition().getValidationWarnings(), false);
		addWarnings(getSRSAttributes().getValidationWarnings(), true);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("geospatial.boundingGeometry.id", getId(), true));
		html.append(buildHTMLMeta("geospatial.boundingGeometry.type", Point.NAME, true));
		html.append(buildHTMLMeta("geospatial.boundingGeometry.srsName", getSRSAttributes().getSrsName(), true));
		if (getSRSAttributes().getSrsDimension() != null) {
			html.append(buildHTMLMeta("geospatial.boundingGeometry.srsDimension", 
				String.valueOf(getSRSAttributes().getSrsDimension()), false));
		}
		html.append(buildHTMLMeta("geospatial.boundingGeometry.axisLabels", getSRSAttributes().getAxisLabelsAsXsList(),
			false));
		html.append(buildHTMLMeta("geospatial.boundingGeometry.uomLabels", getSRSAttributes().getUomLabelsAsXsList(),
			false));
		html.append(getPosition().toHTML());
		return (html.toString());
	}

	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Geospatial Geometry ID", getId(), true));
		text.append(buildTextLine("Geospatial Geometry Type", Point.NAME, true));		
		text.append(buildTextLine("Geospatial Geometry SRS Name", getSRSAttributes().getSrsName(), true));
		if (getSRSAttributes().getSrsDimension() != null) {
			text.append(buildTextLine("Geospatial Geometry SRS Dimension", 
				String.valueOf(getSRSAttributes().getSrsDimension()), false));
		}
		text.append(buildTextLine("Geospatial Geometry Axis Labels", getSRSAttributes().getAxisLabelsAsXsList(), 
			false));
		text.append(buildTextLine("Geospatial Geometry Unit of Measure Labels", 
			getSRSAttributes().getUomLabelsAsXsList(), false));
		text.append(getPosition().toText());
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Point))
			return (false);
		Point test = (Point) obj;
		return (getSRSAttributes().equals(test.getSRSAttributes()) 
			&& getPosition().equals(test.getPosition())
			&& getId().equals(test.getId()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getSRSAttributes().hashCode();
		result = 7 * result + getId().hashCode();
		result = 7 * result + getPosition().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the SRS Attributes. Will always be non-null.
	 */
	public SRSAttributes getSRSAttributes() {
		return (_cachedSrsAttributes);
	}
	
	/**
	 * Accessor for the ID
	 */
	public String getId() {
		return (getAttributeValue(ID_NAME, getXOMElement().getNamespaceURI()));
	}
	
	/**
	 * Accessor for the coordinates of the position. May return null, but cannot happen after instantiation.
	 */
	public Position getPosition() {
		return (_cachedPosition);
	}
} 