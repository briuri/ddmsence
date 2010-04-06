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
package buri.ddmsence.ddms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.format.Format;
import buri.ddmsence.ddms.resource.Dates;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.resource.Language;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Source;
import buri.ddmsence.ddms.resource.Subtitle;
import buri.ddmsence.ddms.resource.Title;
import buri.ddmsence.ddms.resource.Type;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.ISMVocabulary;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.GeospatialCoverage;
import buri.ddmsence.ddms.summary.RelatedResources;
import buri.ddmsence.ddms.summary.SubjectCoverage;
import buri.ddmsence.ddms.summary.TemporalCoverage;
import buri.ddmsence.ddms.summary.VirtualCoverage;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:Resource (the top-level element of a DDMS record).
 * 
 * <p>
 * DDMS 3.0 Resources have additional ICISM attributes which did not exist in 2.0. However, the 2.0 schema still allows 
 * "any" attributes on the Resource, so the 3.0 attribute values will be loaded if present.
 * </p>
 *  
 * <p>When generating HTML/Text output for a Resource, additional tags are generated listing the version of DDMSence 
 * used to create the record (to help identify formatting bugs), and the version of DDMS. These lines are not required 
 * (and may be removed). For example:</p>
 * 
 * <ul><code>
 * DDMS Generator: DDMSence 1.0.0<br />
 * DDMS Version: 3.0<br /><br />
 * &lt;meta name="ddms.generator" content="DDMSence 1.0.0" /&gt;<br />
 * &lt;meta name="ddms.version" content="3.0" /&gt;<br />
 * </code></ul></p>
 * 
 * This implementation does yet do anything to the extensible attributes or elements (<code>&lt;xs:anyAttribute 
 * namespace="##other"/&gt;</code> on the ResourceType complex type).
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:identifier</u>: (1-many required), implemented as an {@link Identifier}<br />
 * <u>ddms:title</u>: (1-many required), implemented as a {@link Title}<br />
 * <u>ddms:subtitle</u>: (0-many optional), implemented as a {@link Subtitle}<br />
 * <u>ddms:description</u>: (0-1 optional), implemented as a {@link Description}<br />
 * <u>ddms:language</u>: (0-many optional), implemented as a {@link Language}<br />
 * <u>ddms:dates</u>: (0-1 optional), implemented as a {@link Dates}<br />
 * <u>ddms:rights</u>: (0-1 optional), implemented as a {@link Rights}<br />
 * <u>ddms:source</u>: (0-many optional), implemented as a {@link Source}<br />
 * <u>ddms:type</u>: (0-many optional), implemented as a {@link Type}<br />
 * <u>ddms:creator</u>: (0-many optional), implemented as a {@link Person}, {@link Organization}, {@link Service}, or
 * {@link Unknown}<br />
 * <u>ddms:publisher</u>: (0-many optional), implemented as a {@link Person}, {@link Organization}, {@link Service}, or
 * {@link Unknown}<br />
 * <u>ddms:contributor</u>: (0-many optional), implemented as a {@link Person}, {@link Organization}, {@link Service}, 
 * or {@link Unknown}<br />
 * <u>ddms:pointOfContact</u>: (0-many optional), implemented as a {@link Person}, {@link Organization}, 
 * {@link Service}, or {@link Unknown}<br />
 * <u>ddms:format</u>: (0-1 optional), implemented as a {@link Format}<br />
 * <u>ddms:subjectCoverage</u>: (exactly 1 required), implemented as a {@link SubjectCoverage}<br />
 * <u>ddms:virtualCoverage</u>: (0-many optional), implemented as a {@link VirtualCoverage}<br />
 * <u>ddms:temporalCoverage</u>: (0-many optional), implemented as a {@link TemporalCoverage}<br />
 * <u>ddms:geospatialCoverage</u>: (0-many optional), implemented as a {@link GeospatialCoverage}<br />
 * <u>ddms:relatedResources</u>: (0-many optional), implemented as a {@link RelatedResources}<br />
 * <u>ddms:security</u>: (exactly 1 required), implemented as a {@link Security}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ICISM:resourceElement</u>: Identifies whether this tag sets the classification for the xml file as a whole
 * (required, starting in DDMS v3.0)<br />
 * <u>ICISM:createDate</u>: Specifies the creation or latest modification date (YYYY-MM-DD) (required, starting in 
 * DDMS v3.0)<br />
 * <u>ICISM:DESVersion</u>: Specifies the version of the Digital Encrpytion Schema version used for the security
 * markings on this record. (required, starting in DDMS v3.0)<br />
 * This class is also decorated with ICISM {@link SecurityAttributes}, starting in DDMS v3.0. The classification and
 * ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#ResourceHeader<br />
 * <u>Description</u>: The header marking of the DDMS card with amplifying attributes.<br />
 * <u>Obligation</u>: Mandatory<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Resource extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<Identifier> _cachedIdentifiers = new ArrayList<Identifier>();
	private List<Title> _cachedTitles = new ArrayList<Title>();
	private List<Subtitle> _cachedSubtitles = new ArrayList<Subtitle>();
	private Description _cachedDescription = null;
	private List<Language> _cachedLanguages = new ArrayList<Language>();
	private Dates _cachedDates = null;
	private Rights _cachedRights = null;
	private List<Source> _cachedSources = new ArrayList<Source>();
	private List<Type> _cachedTypes = new ArrayList<Type>();
	private List<IProducer> _cachedCreators = new ArrayList<IProducer>();
	private List<IProducer> _cachedPublishers = new ArrayList<IProducer>();
	private List<IProducer> _cachedContributors = new ArrayList<IProducer>();
	private List<IProducer> _cachedPointOfContacts = new ArrayList<IProducer>();
	private Format _cachedFormat = null;
	private SubjectCoverage _cachedSubjectCoverage = null;
	private List<VirtualCoverage> _cachedVirtualCoverages = new ArrayList<VirtualCoverage>();
	private List<TemporalCoverage> _cachedTemporalCoverages = new ArrayList<TemporalCoverage>();
	private List<GeospatialCoverage> _cachedGeospatialCoverages = new ArrayList<GeospatialCoverage>();
	private List<RelatedResources> _cachedRelatedResources = new ArrayList<RelatedResources>();
	private Security _cachedSecurity = null;
	private List<IDDMSComponent> _orderedList = new ArrayList<IDDMSComponent>();

	private XMLGregorianCalendar _cachedCreateDate = null;
	private Integer _cachedDESVersion = null;
	private SecurityAttributes _cachedSecurityAttributes = null;

	private static final String DDMSENCE_VERSION = PropertyReader.getProperty("version");
	
	/** The element name of this component */
	public static final String NAME = "Resource";
	
	/** The attribute name for resource element flag */
	protected static final String RESOURCE_ELEMENT_NAME = "resourceElement";
		
	/** The attribute name for create date */
	protected static final String CREATE_DATE_NAME = "createDate";
	
	/** The attribute name for DES version */
	protected static final String DES_VERSION_NAME = "DESVersion";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * <p> DDMS 3.0 Resources have additional ICISM attributes which did not exist in 2.0. However, the 2.0 schema still
	 * allows "any" attributes on the Resource, so the 3.0 attribute values will be loaded if present. </p>
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Resource(Element element) throws InvalidDDMSException {
		try {
			String namespace = element.getNamespaceURI();
			setXOMElement(element, false);

			String createDate = getAttributeValue(CREATE_DATE_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
			if (!Util.isEmpty(createDate))
				_cachedCreateDate = getFactory().newXMLGregorianCalendar(createDate);
			String desVersion = element.getAttributeValue(DES_VERSION_NAME, 
				DDMSVersion.getCurrentVersion().getIcismNamespace());
			if (!Util.isEmpty(desVersion)) {
				try {
					_cachedDESVersion = Integer.valueOf(desVersion);
				} catch (NumberFormatException e) {
					// 	This will be thrown as an InvalidDDMSException during validation
				}
			}
			_cachedSecurityAttributes = new SecurityAttributes(element);
			
			// Resource Layer
			Elements components = element.getChildElements(Identifier.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedIdentifiers.add(new Identifier(components.get(i)));
			}
			components = element.getChildElements(Title.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedTitles.add(new Title(components.get(i)));
			}
			components = element.getChildElements(Subtitle.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedSubtitles.add(new Subtitle(components.get(i)));
			}
			Element component = getChild(Description.NAME);
			if (component != null)
				_cachedDescription = new Description(component);
			components = element.getChildElements(Language.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedLanguages.add(new Language(components.get(i)));
			}
			component = getChild(Dates.NAME);
			if (component != null)
				_cachedDates = new Dates(component);
			component = getChild(Rights.NAME);
			if (component != null)
				_cachedRights = new Rights(component);
			components = element.getChildElements(Source.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedSources.add(new Source(components.get(i)));
			}
			components = element.getChildElements(Type.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedTypes.add(new Type(components.get(i)));
			}
			components = element.getChildElements(AbstractProducer.CREATOR_NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedCreators.add(getEntityFor(components.get(i)));
			}
			components = element.getChildElements(AbstractProducer.PUBLISHER_NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedPublishers.add(getEntityFor(components.get(i)));
			}
			components = element.getChildElements(AbstractProducer.CONTRIBUTOR_NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedContributors.add(getEntityFor(components.get(i)));
			}
			components = element.getChildElements(AbstractProducer.POC_NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedPointOfContacts.add(getEntityFor(components.get(i)));
			}

			// Format layer
			component = getChild(Format.NAME);
			if (component != null)
				_cachedFormat = new Format(component);

			// Summary layer
			component = getChild(SubjectCoverage.NAME);
			if (component != null)
				_cachedSubjectCoverage = new SubjectCoverage(component);
			components = element.getChildElements(VirtualCoverage.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedVirtualCoverages.add(new VirtualCoverage(components.get(i)));
			}
			components = element.getChildElements(TemporalCoverage.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedTemporalCoverages.add(new TemporalCoverage(components.get(i)));
			}
			components = element.getChildElements(GeospatialCoverage.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedGeospatialCoverages.add(new GeospatialCoverage(components.get(i)));
			}
			components = element.getChildElements(RelatedResources.NAME, namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedRelatedResources.add(new RelatedResources(components.get(i)));
			}

			// Security layer
			component = getChild(Security.NAME);
			if (component != null)
				_cachedSecurity = new Security(component);

			populatedOrderedList();
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Constructor for creating a DDMS v2.0 component from raw data
	 * 
	 * <p> Passing the top-level components in as a list is a compromise between a constructor with over twenty
	 * parameters, and the added complexity of a step-by-step factory/builder approach. If any component is not a
	 * top-level component, an InvalidDDMSException will be thrown. </p>
	 * 
	 * <p> The order of different types of components does not matter here (a security component could be the first
	 * component in the list). However, if multiple instances of the same component type exist in the list (such as
	 * multiple identifier components), those components will be stored and output in the order of the list. If only 1
	 * intance can be supported, the last one in the list will be the one used. </p>
	 * 
	 * <p> DDMS 3.0 Resources have additional ICISM attributes which did not exist in 2.0. However, the 2.0 schema still
	 * allows "any" attributes on the Resource, so the 3.0 attribute values will be loaded if present. </p>
	 * 
	 * @param topLevelComponents a list of top level components
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed, or if one of the components
	 * does not belong at the top-level of the Resource.
	 */
	public Resource(List<IDDMSComponent> topLevelComponents) throws InvalidDDMSException {
		this(topLevelComponents, null, null, null, null);
	}

	/**
	 * Constructor for creating a DDMS v3.0 component from raw data
	 * 
	 * <p> Passing the top-level components in as a list is a compromise between a constructor with over twenty
	 * parameters, and the added complexity of a step-by-step factory/builder approach. If any component is not a
	 * top-level component, an InvalidDDMSException will be thrown. </p>
	 * 
	 * <p> The order of different types of components does not matter here (a security component could be the first
	 * component in the list). However, if multiple instances of the same component type exist in the list (such as
	 * multiple identifier components), those components will be stored and output in the order of the list. If only 1
	 * intance can be supported, the last one in the list will be the one used. </p>
	 * 
	 * <p> DDMS 3.0 Resources have additional ICISM attributes which did not exist in 2.0. However, the 2.0 schema still
	 * allows "any" attributes on the Resource, so the 3.0 attribute values will be loaded if present. </p>
	 * 
	 * @param topLevelComponents a list of top level components
	 * @param resourceElement value of the resourceElement attribute (required in v3.0)
	 * @param createDate the create date as an xs:date (YYYY-MM-DD) (required in v3.0)
	 * @param desVersion the DES Version as an Integer (required in v3.0)
	 * @param securityAttributes any security attributes (classification and ownerProducer are required in v3.0)
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed, or if one of the components
	 * does not belong at the top-level of the Resource.
	 */
	public Resource(List<IDDMSComponent> topLevelComponents, Boolean resourceElement, String createDate,
		Integer desVersion, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			if (topLevelComponents == null)
				topLevelComponents = Collections.emptyList();
			Element element = Util.buildDDMSElement(Resource.NAME, null);

			// Attributes
			if (resourceElement != null) {
				Util.addAttribute(element, ICISM_PREFIX, RESOURCE_ELEMENT_NAME, 
					DDMSVersion.getCurrentVersion().getIcismNamespace(), String.valueOf(resourceElement));
			}
			if (desVersion != null) {
				_cachedDESVersion = desVersion;
				Util.addAttribute(element, ICISM_PREFIX, DES_VERSION_NAME, 
					DDMSVersion.getCurrentVersion().getIcismNamespace(), desVersion.toString());
			}
			if (!Util.isEmpty(createDate)) {
				_cachedCreateDate = getFactory().newXMLGregorianCalendar(createDate);
				Util.addAttribute(element, ICISM_PREFIX, CREATE_DATE_NAME, 
					DDMSVersion.getCurrentVersion().getIcismNamespace(), getCreateDate().toXMLFormat());
			}
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);

			for (IDDMSComponent component : topLevelComponents) {
				// Resource Layer
				if (component instanceof Identifier)
					_cachedIdentifiers.add((Identifier) component);
				else if (component instanceof Title)
					_cachedTitles.add((Title) component);
				else if (component instanceof Subtitle)
					_cachedSubtitles.add((Subtitle) component);
				else if (component instanceof Description)
					_cachedDescription = (Description) component;
				else if (component instanceof Language)
					_cachedLanguages.add((Language) component);
				else if (component instanceof Dates)
					_cachedDates = (Dates) component;
				else if (component instanceof Rights)
					_cachedRights = (Rights) component;
				else if (component instanceof Source)
					_cachedSources.add((Source) component);
				else if (component instanceof Type)
					_cachedTypes.add((Type) component);
				else if (component instanceof IProducer) {
					IProducer producer = (IProducer) component;
					if (AbstractProducer.CREATOR_NAME.equals(producer.getName())) {
						_cachedCreators.add(producer);
					} else if (AbstractProducer.PUBLISHER_NAME.equals(producer.getName())) {
						_cachedPublishers.add(producer);
					} else if (AbstractProducer.CONTRIBUTOR_NAME.equals(producer.getName())) {
						_cachedContributors.add(producer);
					} else if (AbstractProducer.POC_NAME.equals(producer.getName())) {
						_cachedPointOfContacts.add(producer);
					}
				}
				// Format layer
				else if (component instanceof Format)
					_cachedFormat = (Format) component;
				// Summary layer
				else if (component instanceof SubjectCoverage)
					_cachedSubjectCoverage = (SubjectCoverage) component;
				else if (component instanceof VirtualCoverage)
					_cachedVirtualCoverages.add((VirtualCoverage) component);
				else if (component instanceof TemporalCoverage)
					_cachedTemporalCoverages.add((TemporalCoverage) component);
				else if (component instanceof GeospatialCoverage)
					_cachedGeospatialCoverages.add((GeospatialCoverage) component);
				else if (component instanceof RelatedResources)
					_cachedRelatedResources.add((RelatedResources) component);
				// Security Layer
				else if (component instanceof Security)
					_cachedSecurity = (Security) component;
				else
					throw new InvalidDDMSException(component.getName()
						+ " is not a valid top-level component in a ddms:Resource.");
			}
			populatedOrderedList();
			for (IDDMSComponent component : getTopLevelComponents()) {
				element.appendChild(component.getXOMElementCopy());
			}
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Helper method to convert a XOM Element into a producer entity, based on the producer entity type.
	 * 
	 * @param element the producerRole element (creator, contributor, etc.)
	 * @return an IProducer
	 */
	private IProducer getEntityFor(Element element) throws InvalidDDMSException {
		Element entityElement = element.getChildElements().get(0);
		Util.requireDDMSValue("entity element", entityElement);
		if (Person.NAME.equals(entityElement.getLocalName()))
			return (new Person(element));
		else if (Organization.NAME.equals(entityElement.getLocalName()))
			return (new Organization(element));
		else if (Service.NAME.equals(entityElement.getLocalName()))
			return (new Service(element));
		else	// Unknown
			return (new Unknown(element));
	}
	
	/**
	 * Creates an ordered list of all the top-level components in this Resource, for ease of traversal.
	 */
	private void populatedOrderedList() {
		_orderedList.addAll(getIdentifiers());
		_orderedList.addAll(getTitles());
		_orderedList.addAll(getSubtitles());
		if (getDescription() != null)
			_orderedList.add(getDescription());
		_orderedList.addAll(getLanguages());
		if (getDates() != null)
			_orderedList.add(getDates());
		if (getRights() != null)
			_orderedList.add(getRights());
		_orderedList.addAll(getSources());
		_orderedList.addAll(getTypes());
		_orderedList.addAll(getCreators());
		_orderedList.addAll(getPublishers());
		_orderedList.addAll(getContributors());
		_orderedList.addAll(getPointOfContacts());
		if (getFormat() != null)
			_orderedList.add(getFormat());
		if (getSubjectCoverage() != null)
			_orderedList.add(getSubjectCoverage());
		_orderedList.addAll(getVirtualCoverages());
		_orderedList.addAll(getTemporalCoverages());
		_orderedList.addAll(getGeospatialCoverages());
		_orderedList.addAll(getRelatedResources());
		if (getSecurity() != null)
			_orderedList.add(getSecurity());
	}

	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>(v3.0) resourceElement attribute must exist.</li>
	 * <li>(v3.0) createDate attribute must exist and conform to the xs:date date type (YYYY-MM-DD).</li>
	 * <li>(v3.0) DESVersion must exist and be a valid Integer.</li>
	 * <li>(v3.0) A classification is required.</li>
	 * <li>(v3.0) At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>1-many identifiers, 1-many titles, 0-1 descriptions, 0-1 dates, 0-1 rights, 0-1 formats, exactly 1
	 * subjectCoverage, and exactly 1 security element must exist.</li>
	 * <li>At least 1 of creator, publisher, contributor, or pointOfContact must exist.</li>
	 * <li>If this resource has security attributes, the SecurityAttributes on any subcomponents are valid according 
	 * to rollup rules (security attributes are not required in DDMS 2.0).</li>
	 * <li>All child components are using the same version of DDMS.</li>
	 * </td></tr></table>
	 * 
	 * @see Resource#validateRollup(SecurityAttributes, Set)
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMS_PREFIX, NAME);
		if (!"2.0".equals(getDDMSVersion())) {
			Util.requireDDMSValue(RESOURCE_ELEMENT_NAME, isResourceElement());
			Util.requireDDMSValue(CREATE_DATE_NAME, getCreateDate());
			Util.requireDDMSValue(DES_VERSION_NAME, getDESVersion());
			Util.requireDDMSValue("security attributes", getSecurityAttributes());
			getSecurityAttributes().requireClassification();
			if (isResourceElement() == null)
				throw new InvalidDDMSException("The resourceElement attribute must have a boolean value.");
			if (!getCreateDate().getXMLSchemaType().equals(DatatypeConstants.DATE))
				throw new InvalidDDMSException("The createDate must be in the xs:date format (YYYY-MM-DD).");
		}

		if (getIdentifiers().size() < 1)
			throw new InvalidDDMSException("At least 1 identifier is required.");
		if (getTitles().size() < 1)
			throw new InvalidDDMSException("At least 1 title is required.");
		if (getCreators().size() + getContributors().size() + getPublishers().size() + getPointOfContacts().size() == 0)
			throw new InvalidDDMSException(
				"At least 1 producer (creator, contributor, publisher, or pointOfContact) is required.");
		Util.requireBoundedDDMSChildCount(getXOMElement(), Description.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Dates.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Rights.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Format.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), SubjectCoverage.NAME, 1, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Security.NAME, 1, 1);	
		if (!getSecurityAttributes().isEmpty()) {
			Set<SecurityAttributes> childAttributes = new HashSet<SecurityAttributes>();
			for (IDDMSComponent component : getTopLevelComponents()) {
				if (component.getSecurityAttributes() != null && !(component instanceof Security))
					childAttributes.add(component.getSecurityAttributes());
			}
			validateRollup(getSecurityAttributes(), childAttributes);
		}
		for (IDDMSComponent component : getTopLevelComponents()) {
			Util.requireSameVersion(this, component);
		}
		
		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>(v2.0) If ddms:Resource has no classification, warn about ignoring rollup validation.</li>
	 * <li>If the markings [R, CTS-B, or CTS-BALK are used, a validation warning will be generated, asking
	 * the user to review the markings manually. I'm not sure how these compare to other markings in the
	 * NATO scheme.</li>
	 * <li>Include all child component validation warnings, and any warnings from the security attributes.</li>
	 * </td></tr></table>
	 */
	private void validateWarnings() {
		if (getSecurityAttributes().isEmpty()) {
			addWarning("Security rollup validation is being skipped, because no classification exists "
				+ "on the ddms:Resource itself.");
		}
		
		boolean needsManualReview = ISMVocabulary.classificationNeedsReview(getSecurityAttributes().getClassification());
		for (IDDMSComponent component : getTopLevelComponents()) {
			if (component.getSecurityAttributes() != null)
				needsManualReview = needsManualReview
					|| ISMVocabulary.classificationNeedsReview(component.getSecurityAttributes().getClassification());
		}
		if (needsManualReview) {
			addWarning("A security classification from the set [R, CTS-B, or CTS-BALK] is being used. "
				+ "Please review your ddms:Resource and confirm that security rollup is being handled correctly.");
		}
		
		for (IDDMSComponent component : getTopLevelComponents()) {
			addWarnings(component.getValidationWarnings(), false);
		}
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
	
	/**
	 * Validates that the security attributes of any subcomponents are no more restrictive than
	 * the parent attributes. Does not include the ddms:security tag which has a fixed
	 * excludeFromRollup="true" attribute.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>For any subcomponent's security attributes:</li>
	 * <ul>
	 * <li>The classification must belong to the same classification system as the parent's
	 * classification (US or NATO markings).</li>
	 * <li>The classification cannot be more restrictive than the parent classification. The ordering
	 * for US markings (from least to most restrictive) is [U, C, S, TS]. The ordering for NATO
	 * markings (from least to most restrictive) is [NU, NR, NC, NCA, NS, NSAT, CTS, CTSA].</li>
	 * </ul>
	 * </td></tr></table>
	 * 	
	 * @param parentAttributes the master attributes to compare to
	 * @param childAttributes a set of all nested attributes
	 */
	private void validateRollup(SecurityAttributes parentAttributes, Set<SecurityAttributes> childAttributes)
		throws InvalidDDMSException {
		Util.requireValue("parent classification", parentAttributes.getClassification());

		String parentClass = parentAttributes.getClassification();
		boolean isParentUS = ISMVocabulary.enumContains(ISMVocabulary.CVE_US_CLASSIFICATIONS, parentClass);
		int parentIndex = ISMVocabulary.getClassificationIndex(parentClass);
		
		for (SecurityAttributes childAttr : childAttributes) {
			String childClass = childAttr.getClassification();
			if (Util.isEmpty(childClass))
				continue;
			boolean isChildUS = ISMVocabulary.enumContains(ISMVocabulary.CVE_US_CLASSIFICATIONS, childClass);
			int childIndex = ISMVocabulary.getClassificationIndex(childClass);
			if (isParentUS != isChildUS) {
				throw new InvalidDDMSException("The security classification of a nested component is using a "
					+ "different marking system than the ddms:Resource itself.");
			}
			if (childIndex > parentIndex) {
				throw new InvalidDDMSException("The security classification of a nested component is more "
					+ "restrictive than the ddms:Resource itself.");
			}			
		}
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		if (isResourceElement() != null)
			html.append(buildHTMLMeta("security.resourceElement", String.valueOf(isResourceElement()), true));
		if (getCreateDate() != null)
			html.append(buildHTMLMeta("security.createDate", getCreateDate().toXMLFormat(), true));
		if (getDESVersion() != null)
			html.append(buildHTMLMeta("security.DESVersion", String.valueOf(getDESVersion()), true));
		html.append(getSecurityAttributes().toHTML(Security.NAME));
		for (IDDMSComponent component : getTopLevelComponents())
			html.append(component.toHTML());
		html.append(buildHTMLMeta("ddms.generator", "DDMSence " + DDMSENCE_VERSION, true));
		html.append(buildHTMLMeta("ddms.version", getDDMSVersion(), true));
		return (html.toString());
	}

	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		if (isResourceElement() != null)
			text.append(buildTextLine("ResourceElement", String.valueOf(isResourceElement()), true));
		if (getCreateDate() != null)
			text.append(buildTextLine("Create Date", getCreateDate().toXMLFormat(), true));
		if (getDESVersion() != null)
			text.append(buildTextLine("DES Version", String.valueOf(getDESVersion()), true));
		text.append(getSecurityAttributes().toText(""));
		for (IDDMSComponent component : getTopLevelComponents())
			text.append(component.toText());
		text.append(buildTextLine("DDMS Generator", "DDMSence " + DDMSENCE_VERSION, true));
		text.append(buildTextLine("DDMS Version", getDDMSVersion(), true));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Resource))
			return (false);
		Resource test = (Resource) obj;
		return (Util.nullEquals(isResourceElement(), test.isResourceElement())
			&& Util.nullEquals(getCreateDate(), test.getCreateDate())
			&& Util.nullEquals(getDESVersion(), test.getDESVersion())
			&& getSecurityAttributes().equals(test.getSecurityAttributes())
			&& Util.listEquals(getTopLevelComponents(),	test.getTopLevelComponents()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		if (isResourceElement() != null)
			result = 7 * result + isResourceElement().hashCode();
		if (getCreateDate() != null)
			result = 7 * result + getCreateDate().hashCode();
		if (getDESVersion() != null)
			result = 7 * result + getDESVersion().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		for (IDDMSComponent component : getTopLevelComponents())
			result = 7 * result + component.hashCode();
		return (result);
	}

	/**
	 * Accessor for the identifier components. There will always be at least one.
	 */
	public List<Identifier> getIdentifiers() {
		return (Collections.unmodifiableList(_cachedIdentifiers));
	}

	/**
	 * Accessor for the title components. There will always be at least one.
	 */
	public List<Title> getTitles() {
		return (Collections.unmodifiableList(_cachedTitles));
	}

	/**
	 * Accessor for the subtitle components (0-many)
	 */
	public List<Subtitle> getSubtitles() {
		return (Collections.unmodifiableList(_cachedSubtitles));
	}

	/**
	 * Accessor for the description component (0-1)
	 */
	public Description getDescription() {
		return (_cachedDescription);
	}

	/**
	 * Accessor for the language components (0-many)
	 */
	public List<Language> getLanguages() {
		return (Collections.unmodifiableList(_cachedLanguages));
	}

	/**
	 * Accessor for the dates component (0-1). May return null.
	 */
	public Dates getDates() {
		return _cachedDates;
	}

	/**
	 * Accessor for the rights component (0-1). May return null.
	 */
	public Rights getRights() {
		return _cachedRights;
	}

	/**
	 * Accessor for the source components (0-many)
	 */
	public List<Source> getSources() {
		return (Collections.unmodifiableList(_cachedSources));
	}

	/**
	 * Accessor for the type components (0-many)
	 */
	public List<Type> getTypes() {
		return (Collections.unmodifiableList(_cachedTypes));
	}

	/**
	 * Accessor for a list of all Creator entities (0-many)
	 */
	public List<IProducer> getCreators() {
		return (Collections.unmodifiableList(_cachedCreators));
	}

	/**
	 * Accessor for a list of all Publisher entities (0-many)
	 */
	public List<IProducer> getPublishers() {
		return (Collections.unmodifiableList(_cachedPublishers));
	}

	/**
	 * Accessor for a list of all Contributor entities (0-many)
	 */
	public List<IProducer> getContributors() {
		return (Collections.unmodifiableList(_cachedContributors));
	}

	/**
	 * Accessor for a list of all PointOfContact entities (0-many)
	 */
	public List<IProducer> getPointOfContacts() {
		return (Collections.unmodifiableList(_cachedPointOfContacts));
	}

	/**
	 * Accessor for the Format component (0-1). May return null.
	 */
	public Format getFormat() {
		return (_cachedFormat);
	}

	/**
	 * Accessor for the subjectCoverage component (exactly 1). May return null, but this cannot happen after
	 * instantiation.
	 */
	public SubjectCoverage getSubjectCoverage() {
		return _cachedSubjectCoverage;
	}

	/**
	 * Accessor for the virtualCoverage components (0-many)
	 */
	public List<VirtualCoverage> getVirtualCoverages() {
		return (Collections.unmodifiableList(_cachedVirtualCoverages));
	}

	/**
	 * Accessor for the temporalCoverage components (0-many)
	 */
	public List<TemporalCoverage> getTemporalCoverages() {
		return (Collections.unmodifiableList(_cachedTemporalCoverages));
	}

	/**
	 * Accessor for the geospatialCoverage components (0-many)
	 */
	public List<GeospatialCoverage> getGeospatialCoverages() {
		return (Collections.unmodifiableList(_cachedGeospatialCoverages));
	}

	/**
	 * Accessor for the RelatedResources component (0-many)
	 */
	public List<RelatedResources> getRelatedResources() {
		return (Collections.unmodifiableList(_cachedRelatedResources));
	}

	/**
	 * Accessor for the security component (exactly 1). May return null but this cannot happen after instantiation.
	 */
	public Security getSecurity() {
		return (_cachedSecurity);
	}

	/**
	 * Accessor for the resourceElement attribute. This may be null for v2.0 Resource components.
	 */
	public Boolean isResourceElement() {
		String value = getAttributeValue(RESOURCE_ELEMENT_NAME, DDMSVersion.getCurrentVersion().getIcismNamespace());
		if ("true".equals(value))
			return (Boolean.TRUE);
		if ("false".equals(value))
			return (Boolean.FALSE);
		return (null);
	}

	/**
	 * Accessor for the createDate date (optional). Returns a copy. This may be null for v2.0 Resource components.
	 */
	public XMLGregorianCalendar getCreateDate() {
		return (_cachedCreateDate == null ? null : getFactory()
			.newXMLGregorianCalendar(_cachedCreateDate.toXMLFormat()));
	}

	/**
	 * Accessor for the DESVersion attribute. Because this attribute does not exist in DDMS v2.0, the accessor
	 * will return null for v2.0 Resource elements.
	 */
	public Integer getDESVersion() {
		return (_cachedDESVersion);
	}

	/**
	 * Accessor for an ordered list of the components in this Resource. Components which are missing are not represented
	 * in this list (no null entries).
	 */
	public List<IDDMSComponent> getTopLevelComponents() {
		return (Collections.unmodifiableList(_orderedList));
	}

	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Accesor for the datatype factory
	 */
	private static DatatypeFactory getFactory() {
		return (Util.getDataTypeFactory());
	}
}