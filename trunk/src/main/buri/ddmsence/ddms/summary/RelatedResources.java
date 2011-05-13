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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:relatedResources.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p><ul>
 * <li>A non-empty relationship attribute is required.</li>
 * <li>(v2.0) Although 0 RelatedResource elements can exist in v2.0, this is considered invalid data. DDMSence requires
 * at least 1 RelatedResource.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:RelatedResource</u>: the resource (1-many required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:relationship</u>: A URI representing a relationship of some relationship type between the resource being
 * described and other resources. (required)<br />
 * <u>ddms:direction</u>: Used to indicate the direction of the relationship between the resource being described and
 * the target related resource. Valid values are "inbound," "outbound," and "bidirectional". (optional)
 * This class is also decorated with ICISM {@link SecurityAttributes}. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#RelatedResources<br />
 * <u>Description</u>: A set of resources related by a specified relationship and direction to the resource being 
 * described.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class RelatedResources extends AbstractBaseComponent {

	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<RelatedResource> _cachedResources;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/** The element name of this component */
	public static final String NAME = "relatedResources";
	
	/** The value for an inbound direction. */
	public static final String INBOUND_DIRECTION = "inbound";
	
	/** The value for an outbound direction. */
	public static final String OUTBOUND_DIRECTION = "outbound";
	
	/** The value for an bidirectional direction. */
	public static final String BIDRECTIONAL_DIRECTION = "bidirectional";
	
	private static Set<String> RELATIONSHIP_DIRECTIONS = new HashSet<String>();
	static {
		RELATIONSHIP_DIRECTIONS.add(INBOUND_DIRECTION);
		RELATIONSHIP_DIRECTIONS.add(OUTBOUND_DIRECTION);
		RELATIONSHIP_DIRECTIONS.add(BIDRECTIONAL_DIRECTION);
	}
	
	private static final String RELATIONSHIP_NAME = "relationship";
	private static final String DIRECTION_NAME = "direction";
		
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public RelatedResources(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("RelatedResources element", element);
			_cachedResources = new ArrayList<RelatedResource>();
			Elements resources = element.getChildElements(RelatedResource.NAME, element.getNamespaceURI());
			for (int i = 0; i < resources.size(); i++) {
				_cachedResources.add(new RelatedResource(resources.get(i)));
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
	 * @param resources the related resources (1 required)
	 * @param relationship the relationship attribute (required)
	 * @param direction the relationship direction (optional)
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public RelatedResources(List<RelatedResource> resources, String relationship, String direction,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(RelatedResources.NAME, null);
			Util.addDDMSAttribute(element, RELATIONSHIP_NAME, relationship);
			Util.addDDMSAttribute(element, DIRECTION_NAME, direction);
			if (resources == null)
				resources = Collections.emptyList();
			for (RelatedResource resource : resources) {
				element.appendChild(resource.getXOMElementCopy());
			}
			_cachedResources = resources;
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Asserts that a direction is valid.
	 * 
	 * @param direction	the string to check
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateRelationshipDirection(String direction) throws InvalidDDMSException {
		Util.requireDDMSValue("relationship direction", direction);
		if (!RELATIONSHIP_DIRECTIONS.contains(direction))
			throw new InvalidDDMSException("The direction attribute must be one of " + RELATIONSHIP_DIRECTIONS);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>A relationship exists and is not empty.</li>
	 * <li>The relationship is a valid URI.</li>
	 * <li>If set, the direction has a valid value.</li>
	 * <li>At least 1 RelatedResource exists.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		Util.requireDDMSValue("relationship attribute", getRelationship());
		Util.requireDDMSValidURI(getRelationship());
		if (!Util.isEmpty(getDirection()))
			validateRelationshipDirection(getDirection());
		if (getChild(RelatedResource.NAME) == null)
			throw new InvalidDDMSException("At least 1 RelatedResource must exist.");
		for (RelatedResource related : getRelatedResources())
			Util.requireSameVersion(this, related);

		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any validation warnings from the related resources or the security attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		for (RelatedResource related : getRelatedResources())
			addWarnings(related.getValidationWarnings(), false);
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("RelatedResources.relationship", getRelationship(), true));
		html.append(buildHTMLMeta("RelatedResources.direction", getDirection(), false));
		for (RelatedResource resource : getRelatedResources()) {
			html.append(resource.toHTML());
		}
		html.append(getSecurityAttributes().toHTML("RelatedResources"));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Related Resources relationship", getRelationship(), true));
		text.append(buildTextLine("Related Resources direction", getDirection(), false));
		for (RelatedResource resource : getRelatedResources()) {
			text.append(resource.toText());
		}
		text.append(getSecurityAttributes().toText("Related Resources"));
		return (text.toString());
	}
			 
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof RelatedResources))
			return (false);
		RelatedResources test = (RelatedResources) obj;
		return (getRelationship().equals(test.getRelationship()) 
			&& getDirection().equals(test.getDirection())
			&& Util.listEquals(getRelatedResources(), test.getRelatedResources()) 
			&& getSecurityAttributes().equals(test.getSecurityAttributes()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getRelationship().hashCode();
		result = 7 * result + getDirection().hashCode();
		result = 7 * result + getRelatedResources().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the related resources (1 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<RelatedResource> getRelatedResources() {
		return (Collections.unmodifiableList(_cachedResources));
	}
	
	/**
	 * Accessor for the relationship attribute
	 */
	public String getRelationship() {
		return (getAttributeValue(RELATIONSHIP_NAME));
	}
	
	/**
	 * Accessor for the direction attribute (may be empty)
	 */
	public String getDirection() {
		return (getAttributeValue(DIRECTION_NAME));
	}
	
	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
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
	public static class Builder implements IBuilder {
		private String _relationship;
		private String _direction;
		private List<RelatedResource.Builder> _relatedResources;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(RelatedResources resources) {
			setRelationship(resources.getRelationship());
			setDirection(resources.getDirection());
			for (RelatedResource resource : resources.getRelatedResources()) {
				getRelatedResources().add(new RelatedResource.Builder(resource));
			}
			setSecurityAttributes(new SecurityAttributes.Builder(resources.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public RelatedResources commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<RelatedResource> resources = new ArrayList<RelatedResource>();
			for (RelatedResource.Builder builder : getRelatedResources()) {
				RelatedResource resource = builder.commit();
				if (resource != null)
					resources.add(resource);
			}
			return (new RelatedResources(resources, getRelationship(), getDirection(), getSecurityAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getRelatedResources()) {
				hasValueInList = hasValueInList || !builder.isEmpty();
			}
			return (Util.isEmpty(getRelationship())
				&& Util.isEmpty(getDirection())
				&& !hasValueInList && getSecurityAttributes().isEmpty());
		}
		
		/**
		 * Builder accessor for the relationship attribute
		 */
		public String getRelationship() {
			return _relationship;
		}
		
		/**
		 * Builder accessor for the relationship attribute
		 */
		public void setRelationship(String relationship) {
			_relationship = relationship;
		}
		
		/**
		 * Builder accessor for the direction attribute
		 */
		public String getDirection() {
			return _direction;
		}
		
		/**
		 * Builder accessor for the direction attribute
		 */
		public void setDirection(String direction) {
			_direction = direction;
		}
		
		/**
		 * Builder accessor for the related resources
		 */
		public List<RelatedResource.Builder> getRelatedResources() {
			if (_relatedResources == null)
				_relatedResources = new ArrayList<RelatedResource.Builder>();
			return _relatedResources;
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