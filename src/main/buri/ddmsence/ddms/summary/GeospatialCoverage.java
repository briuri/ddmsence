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
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
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
 * <li>No more than 1 each of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent can 
 * be used. The schema seems to support this assertion with explicit restrictions on those elements, but the enclosing 
 * xs:choice element allows multiples. From the specification, "The intent of Geospatial Coverage is to provide 
 * logically and semantically consistent information.  Flexibility in the specification does not absolve end users 
 * using Geospatial Coverage from expressing information in a meaningful manner.  Users should ensure that combinations 
 * of elements are appropriately relatable, consistent, meaningful, and useful for enterprise discovery."</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:geographicIdentifier</u>: an identifier (0-1 optional) implemented as a {@link GeographicIdentifier}<br />
 * <u>ddms:boundingBox</u>: a bounding box (0-1 optional) implemented as a {@link BoundingBox}<br />
 * <u>ddms:boundingGeometry</u>: a set of bounding geometry (0-1 optional) implemented as a 
 * {@link BoundingGeometry}<br />
 * <u>ddms:postalAddress</u>: an address (0-1 optional), implemented as a (@link PostalAddress)<br />
 * <u>ddms:verticalExtent</u>: an extent (0-1 optional), implemented as a (@link VerticalExtent)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ISM {@link SecurityAttributes}, starting in DDMS 3.0. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#geospatialCoverage<br />
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
		try {
			Util.requireDDMSValue("geographicIdentifier element", element);
			String namespace = element.getNamespaceURI();
			Element extElement = element.getFirstChildElement(GEOSPATIAL_EXTENT_NAME, namespace);
			if (extElement != null) {
				DDMSVersion version = DDMSVersion.getVersionForDDMSNamespace(element.getNamespaceURI());
				Element geographicIdentifierElement = extElement.getFirstChildElement(
					GeographicIdentifier.getName(version), namespace);
				if (geographicIdentifierElement != null)
					_cachedGeographicIdentifier = new GeographicIdentifier(geographicIdentifierElement);
				Element boundingBoxElement = extElement.getFirstChildElement(BoundingBox.getName(version), namespace);
				if (boundingBoxElement != null)
					_cachedBoundingBox = new BoundingBox(boundingBoxElement);
				Element boundingGeometryElement = extElement.getFirstChildElement(BoundingGeometry.NAME, namespace);
				if (boundingGeometryElement != null)
					_cachedBoundingGeometry = new BoundingGeometry(boundingGeometryElement);
				Element postalAddressElement = extElement.getFirstChildElement(PostalAddress.getName(version),
					namespace);
				if (postalAddressElement != null)
					_cachedPostalAddress = new PostalAddress(postalAddressElement);
				Element verticalExtentElement = extElement.getFirstChildElement(VerticalExtent.getName(version),
					namespace);
				if (verticalExtentElement != null)
					_cachedVerticalExtent = new VerticalExtent(verticalExtentElement);
			}
			_cachedSecurityAttributes = new SecurityAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
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
	public GeospatialCoverage(GeographicIdentifier geographicIdentifier, BoundingBox boundingBox,
		BoundingGeometry boundingGeometry, PostalAddress postalAddress, VerticalExtent verticalExtent,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
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
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(coverageElement);
			setXOMElement(coverageElement, true);
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
	 * <li>At least 1 of geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent must 
	 * be used.</li>
	 * <li>No more than 1 geographicIdentifier, boundingBox, boundingGeometry, postalAddress, or verticalExtent can 
	 * be used.</li>
	 * <li>If a geographicIdentifer is used and contains a facilityIdentifier, no other subcomponents can be used.</li>
	 * <li>The SecurityAttributes do not exist until DDMS 3.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
		Element extElement = getChild(GEOSPATIAL_EXTENT_NAME);
		Util.requireDDMSValue("GeospatialExtent element", extElement);
		
		Util.requireBoundedDDMSChildCount(extElement, GeographicIdentifier.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, BoundingBox.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, BoundingGeometry.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, PostalAddress.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, VerticalExtent.getName(getDDMSVersion()), 0, 1);
			
		int validComponents = 0;
		if (getGeographicIdentifier() != null) {
			Util.requireCompatibleVersion(this, getGeographicIdentifier());
			validComponents++;
		}
		if (getBoundingBox() != null) {
			Util.requireCompatibleVersion(this, getBoundingBox());
			validComponents++;
		}
		if (getBoundingGeometry() != null) {
			Util.requireCompatibleVersion(this, getBoundingGeometry());
			validComponents++;
		}
		if (getPostalAddress() != null) {
			Util.requireCompatibleVersion(this, getPostalAddress());
			validComponents++;
		}
		if (getVerticalExtent() != null) {
			Util.requireCompatibleVersion(this, getVerticalExtent());
			validComponents++;
		}
		if (validComponents == 0) {
			throw new InvalidDDMSException("At least 1 of geographicIdentifier, boundingBox, boundingGeometry, "
				+ "postalAddress, or verticalExtent must be used.");
		}
		if (hasFacilityIdentifier() && validComponents > 1) {
			throw new InvalidDDMSException("A geographicIdentifier containing a facilityIdentifier cannot be used in "
				+ "tandem with any other coverage elements.");
		}	
		
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("3.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes cannot be applied to this component until DDMS 3.0 or later.");
		}
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the security attributes and any child components.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (getGeographicIdentifier() != null)
			addWarnings(getGeographicIdentifier().getValidationWarnings(), false);
		if (getBoundingBox() != null)
			addWarnings(getBoundingBox().getValidationWarnings(), false);
		if (getBoundingGeometry() != null)
			addWarnings(getBoundingGeometry().getValidationWarnings(), false);
		if (getPostalAddress() != null)
			addWarnings(getPostalAddress().getValidationWarnings(), false);
		if (getVerticalExtent() != null)
			addWarnings(getVerticalExtent().getValidationWarnings(), false);
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
	
	/**
	 * @see AbstractBaseComponent#getLocatorSuffix()
	 */
	protected String getLocatorSuffix() {
		return (ValidationMessage.ELEMENT_PREFIX + getXOMElement().getNamespacePrefix() + ":" + GEOSPATIAL_EXTENT_NAME);
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
		html.append(getSecurityAttributes().toHTML(NAME));
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
		text.append(getSecurityAttributes().toText(NAME));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof GeospatialCoverage))
			return (false);
		GeospatialCoverage test = (GeospatialCoverage) obj;
		boolean isEqual = Util.nullEquals(getGeographicIdentifier(), test.getGeographicIdentifier())
			&& Util.nullEquals(getBoundingBox(), test.getBoundingBox())
			&& Util.nullEquals(getBoundingGeometry(), test.getBoundingGeometry())
			&& Util.nullEquals(getPostalAddress(), test.getPostalAddress())
			&& Util.nullEquals(getVerticalExtent(), test.getVerticalExtent())
			&& getSecurityAttributes().equals(test.getSecurityAttributes());
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
	 * Accessor for the Security Attributes.  Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 2895705456552847432L;
		private BoundingBox.Builder _boundingBox;
		private BoundingGeometry.Builder _boundingGeometry;
		private GeographicIdentifier.Builder _geographicIdentifier;
		private PostalAddress.Builder _postalAddress;
		private VerticalExtent.Builder _verticalExtent;
		private SecurityAttributes.Builder _securityAttributes;	
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(GeospatialCoverage coverage) {
			if (coverage.getBoundingBox() != null)
				setBoundingBox(new BoundingBox.Builder(coverage.getBoundingBox()));
			if (coverage.getBoundingGeometry() != null)
				setBoundingGeometry(new BoundingGeometry.Builder(coverage.getBoundingGeometry()));
			if (coverage.getGeographicIdentifier() != null)
				setGeographicIdentifier(new GeographicIdentifier.Builder(coverage.getGeographicIdentifier()));
			if (coverage.getPostalAddress() != null)
				setPostalAddress(new PostalAddress.Builder(coverage.getPostalAddress()));
			if (coverage.getVerticalExtent() != null)
				setVerticalExtent(new VerticalExtent.Builder(coverage.getVerticalExtent()));
			setSecurityAttributes(new SecurityAttributes.Builder(coverage.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public GeospatialCoverage commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new GeospatialCoverage(getGeographicIdentifier().commit(), 
				getBoundingBox().commit(), getBoundingGeometry().commit(), getPostalAddress().commit(), 
				getVerticalExtent().commit(), getSecurityAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (getGeographicIdentifier().isEmpty()
				&& getBoundingBox().isEmpty()
				&& getBoundingGeometry().isEmpty()
				&& getPostalAddress().isEmpty()
				&& getVerticalExtent().isEmpty()
				&& getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the boundingBox
		 */
		public BoundingBox.Builder getBoundingBox() {
			if (_boundingBox == null)
				_boundingBox = new BoundingBox.Builder();
			return _boundingBox;
		}

		/**
		 * Builder accessor for the boundingBox
		 */
		public void setBoundingBox(BoundingBox.Builder boundingBox) {
			_boundingBox = boundingBox;
		}

		/**
		 * Builder accessor for the boundingGeometry
		 */
		public BoundingGeometry.Builder getBoundingGeometry() {
			if (_boundingGeometry == null)
				_boundingGeometry = new BoundingGeometry.Builder();
			return _boundingGeometry;
		}

		/**
		 * Builder accessor for the boundingGeometry
		 */
		public void setBoundingGeometry(BoundingGeometry.Builder boundingGeometry) {
			_boundingGeometry = boundingGeometry;
		}

		/**
		 * Builder accessor for the geographicIdentifier
		 */
		public GeographicIdentifier.Builder getGeographicIdentifier() {
			if (_geographicIdentifier == null)
				_geographicIdentifier = new GeographicIdentifier.Builder();
			return _geographicIdentifier;
		}

		/**
		 * Builder accessor for the geographicIdentifier
		 */
		public void setGeographicIdentifier(GeographicIdentifier.Builder geographicIdentifier) {
			_geographicIdentifier = geographicIdentifier;
		}

		/**
		 * Builder accessor for the postalAddress
		 */
		public PostalAddress.Builder getPostalAddress() {
			if (_postalAddress == null)
				_postalAddress = new PostalAddress.Builder();
			return _postalAddress;
		}

		/**
		 * Builder accessor for the postalAddress
		 */
		public void setPostalAddress(PostalAddress.Builder postalAddress) {
			_postalAddress = postalAddress;
		}

		/**
		 * Builder accessor for the verticalExtent
		 */
		public VerticalExtent.Builder getVerticalExtent() {
			if (_verticalExtent == null)
				_verticalExtent = new VerticalExtent.Builder();
			return _verticalExtent;
		}

		/**
		 * Builder accessor for the verticalExtent
		 */
		public void setVerticalExtent(VerticalExtent.Builder verticalExtent) {
			_verticalExtent = verticalExtent;
		}

		/**
		 * Builder accessor for the Security Attributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the Security Attributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}
	}
} 