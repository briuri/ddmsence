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
package buri.ddmsence.ddms.summary.gml;

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
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of gml:Polygon.
 * 
 * <p>
 * A Polygon element contains a nested gml:exterior element, which itself contains a nested gml:LinearRing element. 
 * The points which mark the boundaries of the polygon should be provided in counter-clockwise order.
 * Because DDMS does not decorate these elements with any special attributes, they are not implemented as Java objects.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The srsName must also be non-empty.</li>
 * </ul>
 * </td></tr></table>
 *  
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>gml:pos</u>: the positions which comprise the LinearRing in this Polygon (at least 4 required)<br />
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
 * <u>Description</u>: Specifies a position using a list of coordinates.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Polygon extends AbstractBaseComponent {

	// Values are cached upon instantiation, so objects are only created once
	private SRSAttributes _cachedSrsAttributes;
	private List<Position> _cachedPositions;
	
	private static final String EXTERIOR_NAME = "exterior";
	private static final String LINEAR_RING_NAME = "LinearRing";
	private static final String ID_NAME = "id";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Polygon(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			_cachedPositions = new ArrayList<Position>();
			Element extElement = element.getFirstChildElement(EXTERIOR_NAME, element.getNamespaceURI());
			if (extElement != null) {
				Element ringElement = extElement.getFirstChildElement(LINEAR_RING_NAME, element.getNamespaceURI());
				if (ringElement != null) {
					Elements positions = ringElement.getChildElements(Position.getName(getDDMSVersion()), element.getNamespaceURI());
					for (int i = 0; i < positions.size(); i++) {
						_cachedPositions.add(new Position(positions.get(i)));
					}
				}
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
	 * @param positions the positions of the Polygon (required)
	 * @param srsAttributes the attribute group containing srsName, srsDimension, axisLabels, and uomLabels (srsName 
	 * required)
	 * @param id the id value (required)
	 *  
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Polygon(List<Position> positions, SRSAttributes srsAttributes, String id) throws InvalidDDMSException {
		try {
			if (positions == null)
				positions = Collections.emptyList();
			_cachedPositions = positions;
			_cachedSrsAttributes = srsAttributes;
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			String gmlPrefix = PropertyReader.getPrefix("gml");
			String gmlNamespace = version.getGmlNamespace();
			Element ringElement = Util.buildElement(gmlPrefix, LINEAR_RING_NAME, gmlNamespace, null);
			for (Position pos : positions) {
				ringElement.appendChild(pos.getXOMElementCopy());
			}
			Element extElement = Util.buildElement(gmlPrefix, EXTERIOR_NAME, gmlNamespace, null);
			extElement.appendChild(ringElement);
			Element element = Util.buildElement(gmlPrefix, Polygon.getName(version), gmlNamespace, null);
			if (srsAttributes != null)
				srsAttributes.addTo(element);
			Util.addAttribute(element, gmlPrefix, ID_NAME, gmlNamespace, id);
			element.appendChild(extElement);
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
	 * <li>The srsName is required.</li>
	 * <li>If the position has an srsName, it matches the srsName of this Polygon.</li>
	 * <li>The ID is required, and must be a valid NCName.</li>
	 * <li>The first and last position coordinates must be identical (a closed polygon).</li>
	 * <li>Does not perform any special validation on the third coordinate (height above ellipsoid).</li>
	 * </td></tr></table>
	 *
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getNamespace(), Polygon.getName(getDDMSVersion()));
		Util.requireDDMSValue("srsAttributes", getSRSAttributes());
		Util.requireDDMSValue("srsName", getSRSAttributes().getSrsName());
		Util.requireDDMSValue(ID_NAME, getId());
		Util.requireValidNCName(getId());
		
		Element extElement = getXOMElement().getFirstChildElement(EXTERIOR_NAME, getNamespace());
		Util.requireDDMSValue("exterior element", extElement);
		if (extElement != null) {
			Util.requireDDMSValue("LinearRing element", extElement.getFirstChildElement(LINEAR_RING_NAME,
				getNamespace()));
		}
		List<Position> positions = getPositions();
		for (Position pos : positions) {
			if (pos.getSRSAttributes() != null) {
				String srsName = pos.getSRSAttributes().getSrsName();
				if (!Util.isEmpty(srsName) && !srsName.equals(getSRSAttributes().getSrsName()))
					throw new InvalidDDMSException("The srsName of each position must match the srsName of the Polygon.");
			}
		}
		if (positions.size() < 4)
			throw new InvalidDDMSException("At least 4 positions are required for a valid Polygon.");
		if (!positions.isEmpty() && !positions.get(0).equals(positions.get(positions.size() - 1))) {
			throw new InvalidDDMSException("The first and last position in the Polygon must be the same.");
		}

		super.validate();
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
		super.validateWarnings();
	}
	
	/**
	 * @see AbstractBaseComponent#getLocatorSuffix()
	 */
	protected String getLocatorSuffix() {
		String gmlPrefix = PropertyReader.getPrefix("gml");
		return (ValidationMessage.ELEMENT_PREFIX + gmlPrefix + ":" + EXTERIOR_NAME
			+ ValidationMessage.ELEMENT_PREFIX + gmlPrefix + ":" + LINEAR_RING_NAME);
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + ID_NAME, getId(), true));
		text.append(getSRSAttributes().getOutput(isHTML, prefix));
		for (Position pos : getPositions())
			text.append(pos.getOutput(isHTML, prefix));
		return (text.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.addAll(getPositions());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Polygon))
			return (false);
		Polygon test = (Polygon) obj;
		return (getSRSAttributes().equals(test.getSRSAttributes())
			&& getId().equals(test.getId()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getSRSAttributes().hashCode();
		result = 7 * result + getId().hashCode();
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
		return ("Polygon");
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
		return (getAttributeValue(ID_NAME, getNamespace()));
	}
	
	/**
	 * Accessor for the coordinates. May return null, but cannot happen after instantiation.
	 */
	public List<Position> getPositions() {
		return (Collections.unmodifiableList(_cachedPositions));
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -4324741146353401634L;
		private SRSAttributes.Builder _srsAttributes;
		private List<Position.Builder> _positions;
		private String _id;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Polygon polygon) {
			setSrsAttributes(new SRSAttributes.Builder(polygon.getSRSAttributes()));
			for (Position position : polygon.getPositions()) {
				getPositions().add(new Position.Builder(position));
			}
			setId(polygon.getId());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Polygon commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<Position> positions = new ArrayList<Position>();
			for (Position.Builder builder : getPositions()) {
				Position position = builder.commit();
				if (position != null)
					positions.add(position);
			}
			return (new Polygon(positions, getSrsAttributes().commit(), getId()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getPositions()) {
				hasValueInList = hasValueInList || !builder.isEmpty();
			}
			return (Util.isEmpty(getId()) && !hasValueInList && getSrsAttributes().isEmpty());
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
		 * Builder accessor for the coordinates
		 */
		public List<Position.Builder> getPositions() {
			if (_positions == null)
				_positions = new LazyList(Position.Builder.class);
			return _positions;
		}

		/**
		 * Accessor for the ID
		 */
		public String getId() {
			return _id;
		}

		/**
		 * Accessor for the ID
		 */
		public void setId(String id) {
			_id = id;
		}			
	}
} 