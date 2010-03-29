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
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:geospatialCoverage.
 * 
 * <p>
 * A geospatialCoverage element contains a nested GeospatialExtent element. Because
 * DDMS does not decorate this element with any special attributes, it is not implemented as a Java object.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>No more than 1 each of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent can be used.
 * The schema seems to support this assertion with explicit restrictions on those elements, but the enclosing xs:choice
 * element allows multiples.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:geographicIdentifier</u>: an identifier (0-1 optional) implemented as a {@link GeographicIdentifier}<br />
 * <u>ddms:boundingBox</u>: a bounding box (0-1 optional) implemented as a {@link BoundingBox}<br />
 * <u>ddms:boundingGeometry</u>: a set of bounding geometry (0-1 optional) implemented as a {@link BoundingGeometry}<br />
 * <u>ddms:postalAddress</u>: an address (0-1 optional), implemented as a (@link PostalAddress)<br />
 * <u>ddms:verticalExtent</u>: an extent (0-1 optional), implemented as a (@link VerticalExtent)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ICISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#GeospatialCoverage<br />
 * <u>Description</u>: Geographic place names or coordinates that relate to the resource.<br />
 * <u>Obligation</u>: Mandatory unless Not Applicable<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class GeospatialCoverage extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private GeographicIdentifier _cachedGeographicIdentifier;
	private BoundingBox _cachedBoundingBox;
	private BoundingGeometry _cachedBoundingGeometry;
	private PostalAddress _cachedPostalAddress;
	private VerticalExtent _cachedVerticalExtent;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/** The element name of this component */
	public static final String NAME = "geospatialCoverage";
	
	private static final String GEOSPATIAL_EXTENT_NAME = "GeospatialExtent";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public GeospatialCoverage(Element element) throws InvalidDDMSException {
		Util.requireDDMSValue("geographicIdentifier element", element);
		Element extElement = element.getFirstChildElement(GEOSPATIAL_EXTENT_NAME, element.getNamespaceURI());
		if (extElement != null) {
			Element geographicIdentifierElement = extElement.getFirstChildElement(GeographicIdentifier.NAME, extElement.getNamespaceURI());
			if (geographicIdentifierElement != null)
				_cachedGeographicIdentifier = new GeographicIdentifier(geographicIdentifierElement);
			Element boundingBoxElement = extElement.getFirstChildElement(BoundingBox.NAME, extElement.getNamespaceURI());
			if (boundingBoxElement != null)
				_cachedBoundingBox = new BoundingBox(boundingBoxElement);
			Element boundingGeometryElement = extElement.getFirstChildElement(BoundingGeometry.NAME, extElement.getNamespaceURI());
			if (boundingGeometryElement != null)
				_cachedBoundingGeometry = new BoundingGeometry(boundingGeometryElement);
			Element postalAddressElement = extElement.getFirstChildElement(PostalAddress.NAME, extElement.getNamespaceURI());
			if (postalAddressElement != null)
				_cachedPostalAddress = new PostalAddress(postalAddressElement);
			Element verticalExtentElement = extElement.getFirstChildElement(VerticalExtent.NAME, extElement.getNamespaceURI());
			if (verticalExtentElement != null)
				_cachedVerticalExtent = new VerticalExtent(verticalExtentElement);
		}
		_cachedSecurityAttributes = new SecurityAttributes(element);
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param geographicIdentifier an identifier (0-1 optional)
	 * @param boundingBox a bounding box (0-1 optional)
	 * @param boundingGeometry a set of bounding geometry (0-1 optional)
	 * @param postalAddress an address (0-1 optional)
	 * @param verticalExtent an extent (0-1 optional)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public GeospatialCoverage(GeographicIdentifier geographicIdentifier, BoundingBox boundingBox, BoundingGeometry boundingGeometry,
			PostalAddress postalAddress, VerticalExtent verticalExtent, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		Element element = Util.buildDDMSElement(GEOSPATIAL_EXTENT_NAME, null);
		if (geographicIdentifier != null)
			element.appendChild(geographicIdentifier.getXOMElementCopy());
		if (boundingBox != null)
			element.appendChild(boundingBox.getXOMElementCopy());
		if (boundingGeometry != null)
			element.appendChild(boundingGeometry.getXOMElementCopy());
		if (postalAddress != null)
			element.appendChild(postalAddress.getXOMElementCopy());
		if (verticalExtent != null)
			element.appendChild(verticalExtent.getXOMElementCopy());
		Element coverageElement = Util.buildDDMSElement(GeospatialCoverage.NAME, null);
		coverageElement.appendChild(element);
		_cachedGeographicIdentifier = geographicIdentifier;
		_cachedBoundingBox = boundingBox;
		_cachedBoundingGeometry = boundingGeometry;
		_cachedPostalAddress = postalAddress;
		_cachedVerticalExtent = verticalExtent;
		_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null) : securityAttributes);
		_cachedSecurityAttributes.addTo(element);
		setXOMElement(coverageElement, true);
	}

	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent must be used.</li>
	 * <li>No more than 1 geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent can be used.</li>
	 * <li>If a geographicIdentifer is used and contains a facilityIdentifier, no other subcomponents can be used.</li>
	 * <li>The SecurityAttributes are valid.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Element extElement = getChild(GEOSPATIAL_EXTENT_NAME);
		Util.requireDDMSValue("GeospatialExtent element", extElement);
		
		Util.requireBoundedDDMSChildCount(extElement, GeographicIdentifier.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, BoundingBox.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, BoundingGeometry.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, PostalAddress.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, VerticalExtent.NAME, 0, 1);
		
		int validComponents = 0;
		if (getGeographicIdentifier() != null) {
			addWarnings(getGeographicIdentifier().getValidationWarnings());
			validComponents++;
		}
		if (getBoundingBox() != null) {
			addWarnings(getBoundingBox().getValidationWarnings());
			validComponents++;
		}
		if (getBoundingGeometry() != null) {
			addWarnings(getBoundingGeometry().getValidationWarnings());
			validComponents++;
		}
		if (getPostalAddress() != null) {
			addWarnings(getPostalAddress().getValidationWarnings());
			validComponents++;
		}
		if (getVerticalExtent() != null) {
			addWarnings(getVerticalExtent().getValidationWarnings());
			validComponents++;
		}
		if (validComponents == 0) {
			throw new InvalidDDMSException("At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent must be used.");
		}
		if (hasFacilityIdentifier() && validComponents > 1) {
			throw new InvalidDDMSException("A geographicIdentifier containing a facilityIdentifier cannot be used in tandem with any other coverage elements.");
		}	
		
		addWarnings(getSecurityAttributes().getValidationWarnings());
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		if (getGeographicIdentifier() != null)
			html.append(getGeographicIdentifier().toHTML());
		if (getBoundingBox() != null)
			html.append(getBoundingBox().toHTML());
		if (getBoundingGeometry() != null)
			html.append(getBoundingGeometry().toHTML());
		if (getPostalAddress() != null)
			html.append(getPostalAddress().toHTML());
		if (getVerticalExtent() != null)
			html.append(getVerticalExtent().toHTML());
		html.append(getSecurityAttributes().toHTML("geospatial"));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		if (getGeographicIdentifier() != null)
			text.append(getGeographicIdentifier().toText());
		if (getBoundingBox() != null)
			text.append(getBoundingBox().toText());
		if (getBoundingGeometry() != null)
			text.append(getBoundingGeometry().toText());
		if (getPostalAddress() != null)
			text.append(getPostalAddress().toText());
		if (getVerticalExtent() != null)
			text.append(getVerticalExtent().toText());
		text.append(getSecurityAttributes().toText("Geospatial"));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof GeospatialCoverage))
			return (false);
		GeospatialCoverage test = (GeospatialCoverage) obj;
		boolean isEqual = Util.nullEquals(getGeographicIdentifier(), test.getGeographicIdentifier()) &&
			Util.nullEquals(getBoundingBox(), test.getBoundingBox()) &&
			Util.nullEquals(getBoundingGeometry(), test.getBoundingGeometry()) &&
			Util.nullEquals(getPostalAddress(), test.getPostalAddress()) &&
			Util.nullEquals(getVerticalExtent(), test.getVerticalExtent()) &&
					getSecurityAttributes().equals(test.getSecurityAttributes());
		return (isEqual);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		if (getGeographicIdentifier() != null)
			result = 7 * result + getGeographicIdentifier().hashCode();
		if (getBoundingBox() != null)
			result = 7 * result + getBoundingBox().hashCode();
		if (getBoundingGeometry() != null)
			result = 7 * result + getBoundingGeometry().hashCode();
		if (getPostalAddress() != null)
			result = 7 * result + getPostalAddress().hashCode();
		if (getVerticalExtent() != null)
			result = 7 * result + getVerticalExtent().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		return (result);
	}

	/**
	 * Accessor for whether this geospatialCoverage is using a facility identifier.
	 */
	public boolean hasFacilityIdentifier() {
		return (getGeographicIdentifier() != null && getGeographicIdentifier().hasFacilityIdentifier());
	}

	/**
	 * Accessor for the geographicIdentifier. May return null if not used.
	 */
	public GeographicIdentifier getGeographicIdentifier() {
		return _cachedGeographicIdentifier;
	}

	/**
	 * Accessor for the boundingBox. May return null if not used.
	 */
	public BoundingBox getBoundingBox() {
		return _cachedBoundingBox;
	}

	/**
	 * Accessor for the boundingGeometry. May return null if not used.
	 */
	public BoundingGeometry getBoundingGeometry() {
		return _cachedBoundingGeometry;
	}

	/**
	 * Accessor for the postalAddress. May return null if not used.
	 */
	public PostalAddress getPostalAddress() {
		return (_cachedPostalAddress);
	}

	/**
	 * Accessor for the verticalExtent. May return null if not used.
	 */
	public VerticalExtent getVerticalExtent() {
		return (_cachedVerticalExtent);
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
} 