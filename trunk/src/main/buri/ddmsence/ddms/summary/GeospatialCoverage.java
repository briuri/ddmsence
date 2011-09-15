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
import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:geospatialCoverage.
 * 
 * <p>
 * Before DDMS 4.0, a geospatialCoverage element contains a nested GeospatialExtent element. Because
 * DDMS does not decorate this element with any special attributes, it is not implemented as a Java object.
 * Starting in DDMS 4.0, the GeospatialExtent wrapper has been removed.
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
 * <u>ddms:precedence</u>: priority claimed or received as a result of preeminence. Used with country codes (optional, 
 * starting in DDMS 4.0)
 * <u>ddms:order</u>: specifies a user-defined order of an element within the given document (optional, starting in 
 * DDMS 4.0)<br />
 * This class is decorated with ISM {@link SecurityAttributes}, starting in DDMS 3.0. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: Geographic place names or coordinates that relate to the resource.<br />
 * <u>Obligation</u>: Mandatory unless Not Applicable<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
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
	
	private static final String GEOSPATIAL_EXTENT_NAME = "GeospatialExtent";
	private static final String PRECEDENCE_NAME = "precedence";
	private static final String ORDER_NAME = "order";
	
	private static final List<String> VALID_PRECEDENCE_VALUES = new ArrayList<String>();
	static {
		VALID_PRECEDENCE_VALUES.add("Primary");
		VALID_PRECEDENCE_VALUES.add("Secondary");
	}
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public GeospatialCoverage(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("geographicIdentifier element", element);
			setXOMElement(element, false);
			String namespace = element.getNamespaceURI();
			Element extElement = getExtentElement();
			if (extElement != null) {
				DDMSVersion version = DDMSVersion.getVersionForNamespace(element.getNamespaceURI());
				Element geographicIdentifierElement = extElement.getFirstChildElement(
					GeographicIdentifier.getName(version), namespace);
				if (geographicIdentifierElement != null)
					_cachedGeographicIdentifier = new GeographicIdentifier(geographicIdentifierElement);
				Element boundingBoxElement = extElement.getFirstChildElement(BoundingBox.getName(version), namespace);
				if (boundingBoxElement != null)
					_cachedBoundingBox = new BoundingBox(boundingBoxElement);
				Element boundingGeometryElement = extElement.getFirstChildElement(BoundingGeometry.getName(version), namespace);
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
			validate();
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
	 * @param precedence the precedence attribute (optional, starting in DDMS 4.0)
	 * @param order the order attribute (optional, starting in DDMS 4.0)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public GeospatialCoverage(GeographicIdentifier geographicIdentifier, BoundingBox boundingBox,
		BoundingGeometry boundingGeometry, PostalAddress postalAddress, VerticalExtent verticalExtent,
		String precedence, Integer order, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			Element coverageElement = Util.buildDDMSElement(GeospatialCoverage.getName(DDMSVersion.getCurrentVersion()), null);
			
			Element element = DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? coverageElement
				: Util.buildDDMSElement(GEOSPATIAL_EXTENT_NAME, null);
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
			if (!Util.isEmpty(precedence))
				Util.addDDMSAttribute(coverageElement, PRECEDENCE_NAME, precedence);
			if (order != null)
				Util.addDDMSAttribute(coverageElement, ORDER_NAME, order.toString());

			if (!DDMSVersion.getCurrentVersion().isAtLeast("4.0"))
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
	 * <li>The order and precedence cannot be used until DDMS 4.0 or later.</li>
	 * <li>If set, the precedence must be "Primary" or "Secondary".</li>
	 * <li>If a precedence is set, this coverage must contain a geographicIdentifier with a countryCode.</li>
	 * <li>Does not validate the value of the order attribute (this is done at the Resource level).</li>
	 * <li>The SecurityAttributes do not exist until DDMS 3.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), GeospatialCoverage.getName(getDDMSVersion()));
		Element extElement = getExtentElement();
		Util.requireDDMSValue("GeospatialExtent element", extElement);
		
		Util.requireBoundedDDMSChildCount(extElement, GeographicIdentifier.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, BoundingBox.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, BoundingGeometry.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, PostalAddress.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(extElement, VerticalExtent.getName(getDDMSVersion()), 0, 1);
			
		int validComponents = 0;
		if (getGeographicIdentifier() != null)
			validComponents++;
		if (getBoundingBox() != null)
			validComponents++;
		if (getBoundingGeometry() != null)
			validComponents++;
		if (getPostalAddress() != null)
			validComponents++;
		if (getVerticalExtent() != null)
			validComponents++;
		if (validComponents == 0) {
			throw new InvalidDDMSException("At least 1 of geographicIdentifier, boundingBox, boundingGeometry, "
				+ "postalAddress, or verticalExtent must be used.");
		}
		if (hasFacilityIdentifier() && validComponents > 1) {
			throw new InvalidDDMSException("A geographicIdentifier containing a facilityIdentifier cannot be used in "
				+ "tandem with any other coverage elements.");
		}	
		
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("4.0") && !Util.isEmpty(getPrecedence())) {
			throw new InvalidDDMSException("The ddms:precedence attribute cannot be used until DDMS 4.0 or later.");
		}
		if (!getDDMSVersion().isAtLeast("4.0") && getOrder() != null) {
			throw new InvalidDDMSException("The ddms:order attribute cannot be used until DDMS 4.0 or later.");
		}
		if (!getDDMSVersion().isAtLeast("3.0") && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException(
				"Security attributes cannot be applied to this component until DDMS 3.0 or later.");
		}
		if (!Util.isEmpty(getPrecedence())) {
			if (!VALID_PRECEDENCE_VALUES.contains(getPrecedence())) {
				throw new InvalidDDMSException("The ddms:precedence attribute must have a value from: "
					+ VALID_PRECEDENCE_VALUES);
			}
			if (getGeographicIdentifier() == null || getGeographicIdentifier().getCountryCode() == null) {
				throw new InvalidDDMSException("The ddms:precedence attribute should only be applied to a "
					+ "geospatialCoverage containing a country code.");
			}				
		}
		
		super.validate();
	}
	
	/**
	 * @see AbstractBaseComponent#getLocatorSuffix()
	 */
	protected String getLocatorSuffix() {
		return (getDDMSVersion().isAtLeast("4.0") ? "" : ValidationMessage.ELEMENT_PREFIX
			+ getXOMElement().getNamespacePrefix() + ":" + GEOSPATIAL_EXTENT_NAME);
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		if (!getDDMSVersion().isAtLeast("4.0"))
			prefix += GEOSPATIAL_EXTENT_NAME + ".";
		StringBuffer text = new StringBuffer();
		if (getGeographicIdentifier() != null)
			text.append(getGeographicIdentifier().getOutput(isHTML, prefix));
		if (getBoundingBox() != null)
			text.append(getBoundingBox().getOutput(isHTML, prefix));
		if (getBoundingGeometry() != null)
			text.append(getBoundingGeometry().getOutput(isHTML, prefix));
		if (getPostalAddress() != null)
			text.append(getPostalAddress().getOutput(isHTML, prefix));
		if (getVerticalExtent() != null)
			text.append(getVerticalExtent().getOutput(isHTML, prefix));
		text.append(buildOutput(isHTML, prefix + PRECEDENCE_NAME, getPrecedence(), false));
		if (getOrder() != null)
			text.append(buildOutput(isHTML, prefix + ORDER_NAME, String.valueOf(getOrder()), false));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}

	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.add(getBoundingBox());
		list.add(getBoundingGeometry());
		list.add(getGeographicIdentifier());
		list.add(getPostalAddress());
		list.add(getVerticalExtent());		
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof GeospatialCoverage))
			return (false);
		GeospatialCoverage test = (GeospatialCoverage) obj;
		return (getPrecedence().equals(test.getPrecedence())
			&& Util.nullEquals(getOrder(), test.getOrder()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getPrecedence().hashCode();
		if (getOrder() != null)
			result = 7 * result + getOrder().hashCode();
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
		return ("geospatialCoverage");
	}
	
	/**
	 * Accessor for the element which contains the child elementsnt. Before DDMS 4.0, this is a wrapper element called
	 * ddms:GeospatialExtent. Starting in DDMS 4.0, it is the ddms:geospatialCoverage element itself.
	 */
	private Element getExtentElement() {
		return (getDDMSVersion().isAtLeast("4.0") ? getXOMElement() : getChild(GEOSPATIAL_EXTENT_NAME));
	}
	
	/**
	 * Accessor for the precedence attribute.
	 */
	public String getPrecedence() {
		return (getAttributeValue(PRECEDENCE_NAME));
	}
	
	/**
	 * Accessor for the order attribute.
	 */
	public Integer getOrder() {
		String order = getAttributeValue(ORDER_NAME);
		return (Util.isEmpty(order) ? null : Integer.valueOf(order));
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
		private String _precedence;
		private Integer _order;
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
			setPrecedence(coverage.getPrecedence());
			setOrder(coverage.getOrder());
			setSecurityAttributes(new SecurityAttributes.Builder(coverage.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public GeospatialCoverage commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new GeospatialCoverage(getGeographicIdentifier().commit(), 
				getBoundingBox().commit(), getBoundingGeometry().commit(), getPostalAddress().commit(), 
				getVerticalExtent().commit(), getPrecedence(), getOrder(), getSecurityAttributes().commit()));
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
				&& Util.isEmpty(getPrecedence())
				&& getOrder() == null
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
		 * Builder accessor for the precedence
		 */
		public String getPrecedence() {
			return _precedence;
		}

		/**
		 * Builder accessor for the precedence
		 */
		public void setPrecedence(String precedence) {
			_precedence = precedence;
		}

		/**
		 * Builder accessor for the order
		 */
		public Integer getOrder() {
			return _order;
		}

		/**
		 * Builder accessor for the order
		 */
		public void setOrder(Integer order) {
			_order = order;
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