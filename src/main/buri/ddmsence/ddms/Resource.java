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
package buri.ddmsence.ddms;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;
import nu.xom.XPathContext;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleElement;
import buri.ddmsence.ddms.format.Format;
import buri.ddmsence.ddms.resource.Contributor;
import buri.ddmsence.ddms.resource.Creator;
import buri.ddmsence.ddms.resource.Dates;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.resource.Language;
import buri.ddmsence.ddms.resource.PointOfContact;
import buri.ddmsence.ddms.resource.Publisher;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.resource.Source;
import buri.ddmsence.ddms.resource.Subtitle;
import buri.ddmsence.ddms.resource.Title;
import buri.ddmsence.ddms.resource.Type;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.ddms.security.ism.ISMVocabulary;
import buri.ddmsence.ddms.security.ism.NoticeAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.GeospatialCoverage;
import buri.ddmsence.ddms.summary.RelatedResource;
import buri.ddmsence.ddms.summary.SubjectCoverage;
import buri.ddmsence.ddms.summary.TemporalCoverage;
import buri.ddmsence.ddms.summary.VirtualCoverage;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:resource (the top-level element of a DDMS record).
 * 
 * <p>
 * Starting in DDMS 3.0, resources have additional ISM attributes which did not exist in 2.0. However, the 2.0 schema still allows 
 * "any" attributes on the Resource, so the 3.0 attribute values will be loaded if present.
 * </p>
 *  
 * <p>When generating HTML/Text output for a Resource, additional tags are generated listing the version of DDMSence 
 * used to create the record (to help identify formatting bugs), and the version of DDMS. These lines are not required 
 * (and may be removed). For example:</p>
 * 
 * <ul><code>
 * DDMSGenerator: DDMSence 1.0.0<br />
 * DDMSVersion: 3.0<br /><br />
 * &lt;meta name="ddms.generator" content="DDMSence 1.0.0" /&gt;<br />
 * &lt;meta name="ddms.version" content="3.0" /&gt;<br />
 * </code></ul></p>
 * 
 * <p>The name of this component was changed from "Resource" to "resource" in DDMS 4.0.</p>
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
 * <u>ddms:creator</u>: (0-many optional), implemented as a {@link Creator}<br />
 * <u>ddms:publisher</u>: (0-many optional), implemented as a {@link Publisher}<br />
 * <u>ddms:contributor</u>: (0-many optional), implemented as a {@link Contributor}<br />
 * <u>ddms:pointOfContact</u>: (0-many optional), implemented as a {@link PointOfContact}<br />
 * <u>ddms:format</u>: (0-1 optional), implemented as a {@link Format}<br />
 * <u>ddms:subjectCoverage</u>: (1-many required, starting in DDMS 4.0), implemented as a {@link SubjectCoverage}<br />
 * <u>ddms:virtualCoverage</u>: (0-many optional), implemented as a {@link VirtualCoverage}<br />
 * <u>ddms:temporalCoverage</u>: (0-many optional), implemented as a {@link TemporalCoverage}<br />
 * <u>ddms:geospatialCoverage</u>: (0-many optional), implemented as a {@link GeospatialCoverage}<br />
 * <u>ddms:relatedResources</u>: (0-many optional), implemented as a {@link RelatedResource}<br />
 * <u>ddms:security</u>: (exactly 1 required), implemented as a {@link Security}<br />
 * <u>Extensible Layer</u>: (0-many optional), implemented as a {@link ExtensibleElement}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ISM:resourceElement</u>: Identifies whether this tag sets the classification for the xml file as a whole
 * (required, starting in DDMS 3.0)<br />
 * <u>ISM:createDate</u>: Specifies the creation or latest modification date (YYYY-MM-DD) (required, starting in 
 * DDMS 3.0)<br />
 * <u>ISM:DESVersion</u>: Specifies the version of the Data Encoding Specification used for the security
 * markings on this record. (required, starting in DDMS 3.0)<br />
 * <u>ntk:DESVersion</u>: Specifies the version of the Data Encoding Specification used for Need-To-Know markings
 * on this record. (required, starting in DDMS 4.0 with a fixed value)<br />
 * <u>{@link SecurityAttributes}</u>: The classification and ownerProducer attributes are required. (starting in DDMS 3.0)<br />
 * <u>{@link NoticeAttributes}</u>: (optional, starting in DDMS 4.0)<br /><br />
 * <u>{@link ExtensibleAttributes}</u>: (optional)<br />
 * 
 * Starting in DDMS 3.0, the ISM attributes explicitly defined in the schema should appear in the SecurityAttributes, not
 * the ExtensibleAttributes. Attempts to load them as ExtensibleAttributes will throw an InvalidDDMSException.
 * In DDMS 2.0, there are no ISM attributes explicitly defined in the schema, so you can load them in any way you 
 * want. It is recommended that you load them as SecurityAttributes anyhow, for consistency with newer DDMS resources. 
 * Please see the "Power Tips" on the Extensible Layer (on the DDMSence home page) for details. 
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: The top-level element of a DDMS Metacard with its amplifying attributes.<br />
 * <u>Obligation</u>: Mandatory<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
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
	private List<Creator> _cachedCreators = new ArrayList<Creator>();
	private List<Publisher> _cachedPublishers = new ArrayList<Publisher>();
	private List<Contributor> _cachedContributors = new ArrayList<Contributor>();
	private List<PointOfContact> _cachedPointOfContacts = new ArrayList<PointOfContact>();
	private Format _cachedFormat = null;
	private List<SubjectCoverage> _cachedSubjectCoverages = new ArrayList<SubjectCoverage>();
	private List<VirtualCoverage> _cachedVirtualCoverages = new ArrayList<VirtualCoverage>();
	private List<TemporalCoverage> _cachedTemporalCoverages = new ArrayList<TemporalCoverage>();
	private List<GeospatialCoverage> _cachedGeospatialCoverages = new ArrayList<GeospatialCoverage>();
	private List<RelatedResource> _cachedRelatedResources = new ArrayList<RelatedResource>();
	private Security _cachedSecurity = null;
	private List<ExtensibleElement> _cachedExtensibleElements = new ArrayList<ExtensibleElement>();
	private List<IDDMSComponent> _orderedList = new ArrayList<IDDMSComponent>();

	private XMLGregorianCalendar _cachedCreateDate = null;
	private Integer _cachedIsmDESVersion = null;
	private Integer _cachedNtkDESVersion = null;
	private NoticeAttributes _cachedNoticeAttributes = null;
	private SecurityAttributes _cachedSecurityAttributes = null;
	private ExtensibleAttributes _cachedExtensibleAttributes = null;
	
	/** The attribute name for resource element flag */
	public static final String RESOURCE_ELEMENT_NAME = "resourceElement";
		
	/** The attribute name for create date */
	public static final String CREATE_DATE_NAME = "createDate";
	
	/** The attribute name for DES version */
	public static final String DES_VERSION_NAME = "DESVersion";
	
	private static final Set<String> ALL_IC_ATTRIBUTES = new HashSet<String>();
	static {
		ALL_IC_ATTRIBUTES.add(RESOURCE_ELEMENT_NAME);
		ALL_IC_ATTRIBUTES.add(CREATE_DATE_NAME);
		ALL_IC_ATTRIBUTES.add(DES_VERSION_NAME);
	}
	
	/** A set of all Resource attribute names which should not be converted into ExtensibleAttributes */
	public static final Set<String> NON_EXTENSIBLE_NAMES = Collections.unmodifiableSet(ALL_IC_ATTRIBUTES);
	
	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * <p>Starting in DDMS 3.0, resources have additional ISM attributes which did not exist in 2.0. However, the 2.0
	 * schema still allows "any" attributes on the Resource, so the 3.0 attribute values will be loaded if present.</p>
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Resource(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			String namespace = element.getNamespaceURI();
			String createDate = getAttributeValue(CREATE_DATE_NAME, getDDMSVersion().getIsmNamespace());
			if (!Util.isEmpty(createDate))
				_cachedCreateDate = getFactory().newXMLGregorianCalendar(createDate);
			String ismDESVersion = element.getAttributeValue(DES_VERSION_NAME, 
				getDDMSVersion().getIsmNamespace());
			if (!Util.isEmpty(ismDESVersion)) {
				try {
					_cachedIsmDESVersion = Integer.valueOf(ismDESVersion);
				} catch (NumberFormatException e) {
					// 	This will be thrown as an InvalidDDMSException during validation
				}
			}
			if (getDDMSVersion().isAtLeast("4.0")) {
				String ntkDESVersion = element.getAttributeValue(DES_VERSION_NAME, 
					getDDMSVersion().getNtkNamespace());
				if (!Util.isEmpty(ntkDESVersion)) {
					try {
						_cachedNtkDESVersion = Integer.valueOf(ntkDESVersion);
					} catch (NumberFormatException e) {
						// This will be thrown as an InvalidDDMSException during validation
					}
				}
			}
			_cachedNoticeAttributes = new NoticeAttributes(element);
			_cachedSecurityAttributes = new SecurityAttributes(element);
			_cachedExtensibleAttributes = new ExtensibleAttributes(element);
			
			DDMSVersion version = getDDMSVersion();
			// Resource Set
			Elements components = element.getChildElements(Identifier.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedIdentifiers.add(new Identifier(components.get(i)));
			}
			components = element.getChildElements(Title.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedTitles.add(new Title(components.get(i)));
			}
			components = element.getChildElements(Subtitle.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedSubtitles.add(new Subtitle(components.get(i)));
			}
			Element component = getChild(Description.getName(version));
			if (component != null)
				_cachedDescription = new Description(component);
			components = element.getChildElements(Language.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedLanguages.add(new Language(components.get(i)));
			}
			component = getChild(Dates.getName(version));
			if (component != null)
				_cachedDates = new Dates(component);
			component = getChild(Rights.getName(version));
			if (component != null)
				_cachedRights = new Rights(component);
			components = element.getChildElements(Source.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedSources.add(new Source(components.get(i)));
			}
			components = element.getChildElements(Type.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedTypes.add(new Type(components.get(i)));
			}
			components = element.getChildElements(Creator.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedCreators.add(new Creator(components.get(i)));
			}
			components = element.getChildElements(Publisher.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedPublishers.add(new Publisher(components.get(i)));
			}
			components = element.getChildElements(Contributor.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedContributors.add(new Contributor(components.get(i)));
			}
			components = element.getChildElements(PointOfContact.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedPointOfContacts.add(new PointOfContact(components.get(i)));
			}

			// Format Set
			component = getChild(Format.getName(version));
			if (component != null)
				_cachedFormat = new Format(component);

			// Summary Set
			components = element.getChildElements(SubjectCoverage.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedSubjectCoverages.add(new SubjectCoverage(components.get(i)));
			}
			components = element.getChildElements(VirtualCoverage.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedVirtualCoverages.add(new VirtualCoverage(components.get(i)));
			}
			components = element.getChildElements(TemporalCoverage.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedTemporalCoverages.add(new TemporalCoverage(components.get(i)));
			}
			components = element.getChildElements(GeospatialCoverage.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				_cachedGeospatialCoverages.add(new GeospatialCoverage(components.get(i)));
			}
			components = element.getChildElements(RelatedResource.getName(version), namespace);
			for (int i = 0; i < components.size(); i++) {
				loadRelatedResource(components.get(i));
			}

			// Security Set
			component = getChild(Security.getName(version));
			if (component != null) {
				_cachedSecurity = new Security(component);

				// Extensible Layer
				
				// We use the security component to locate the extensible layer. If it is null, this resource is going
				// to fail validation anyhow, so we skip the extensible layer.
				int index = 0;
				Elements allElements = element.getChildElements();
				while (allElements.get(index) != component) {
					index++;
				}
				for (int i = index + 1; i < allElements.size(); i++) {
					_cachedExtensibleElements.add(new ExtensibleElement(allElements.get(i)));
				}	
			}
			populatedOrderedList();
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Helper method to convert element-based related resources into components. In DDMS 4.0, there is a
	 * one-to-one correlation between the two. In DDMS 2.0, 3.0, or 3.1, the top-level ddms:RelatedResources
	 * element might contain more than 1 ddms:relatedResource. In the latter case, each ddms:relatedResource
	 * must be mediated into a separate RelatedResource instance.
	 * 
	 * @param resource the top-level element
	 */
	private void loadRelatedResource(Element resource) throws InvalidDDMSException {
		Elements children = resource.getChildElements(RelatedResource.OLD_INNER_NAME, getNamespace());
		if (children.size() <= 1) {
			_cachedRelatedResources.add(new RelatedResource(resource));
		}
		else {
			for (int i = 0; i < children.size(); i++) {
				Element copy = new Element(resource);
				copy.removeChildren();
				copy.appendChild(new Element(children.get(i)));
				_cachedRelatedResources.add(new RelatedResource(copy));
			}
		}
	}
	
	/**
	 * Constructor for creating a DDMS 2.0 Resource from raw data.
	 * 
	 * <p>This helper constructor merely calls the fully-parameterized version. Attempts to use it with DDMS 3.0 (or
	 * higher) components will fail, because some required attributes are missing.</p>
	 * 
	 * @param topLevelComponents a list of top level components
	 */
	public Resource(List<IDDMSComponent> topLevelComponents) throws InvalidDDMSException {
		this(topLevelComponents, null, null, null, null, null, null, null);
	}
	
	/**
	 * Constructor for creating a DDMS 2.0 Resource from raw data.
	 * 
	 * <p>This helper constructor merely calls the fully-parameterized version. Attempts to use it with DDMS 3.0
	 * (or higher) components will fail, because some required attributes are missing.</p>
	 * 
	 * @param topLevelComponents a list of top level components
	 * @param extensions any extensible attributes (optional)
	 */
	public Resource(List<IDDMSComponent> topLevelComponents, ExtensibleAttributes extensions)
		throws InvalidDDMSException {
		this(topLevelComponents, null, null, null, null, null, null, extensions);
	}

	/**
	 * Constructor for creating a DDMS resource of any version from raw data.
	 * 
	 * <p>This helper constructor merely calls the fully-parameterized version.</p>
	 * 
	 * @param topLevelComponents a list of top level components
	 * @param resourceElement value of the resourceElement attribute (required, starting in DDMS 3.0)
	 * @param createDate the create date as an xs:date (YYYY-MM-DD) (required, starting in DDMS 3.0)
	 * @param desVersion the DES Version as an Integer (required, starting in DDMS 3.0)
	 * @param securityAttributes any security attributes (classification and ownerProducer are required, starting in
	 * DDMS 3.0)
	 */
	public Resource(List<IDDMSComponent> topLevelComponents, Boolean resourceElement, String createDate,
		Integer desVersion, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		this(topLevelComponents, resourceElement, createDate, desVersion, null, securityAttributes, null, null);
	}

	/**
	 * Constructor for creating a DDMS resource of any version from raw data.
	 * 
	 * <p>The other data-driven constructors call this one.</p>
	 * 
	 * <p> Passing the top-level components in as a list is a compromise between a constructor with over twenty
	 * parameters, and the added complexity of a step-by-step factory/builder approach. If any component is not a
	 * top-level component, an InvalidDDMSException will be thrown. </p>
	 * 
	 * <p> The order of different types of components does not matter here (a security component could be the first
	 * component in the list). However, if multiple instances of the same component type exist in the list (such as
	 * multiple identifier components), those components will be stored and output in the order of the list. If only 1
	 * instance can be supported, the last one in the list will be the one used. </p>
	 * 
	 * <p>Starting in DDMS 3.0, resources have additional ISM attributes which did not exist in 2.0. However, the 2.0
	 * schema still allows "any" attributes on the Resource, so the attribute values will be loaded if present. </p>
	 * 
	 * @param topLevelComponents a list of top level components
	 * @param resourceElement value of the resourceElement attribute (required, starting in DDMS 3.0)
	 * @param createDate the create date as an xs:date (YYYY-MM-DD) (required, starting in DDMS 3.0)
	 * @param ismDESVersion the DES Version as an Integer (required, starting in DDMS 3.0)
	 * @param ntkDESVersion the DES Version as an Integer (required, starting in DDMS 4.0)
	 * @param securityAttributes any security attributes (classification and ownerProducer are required, starting in
	 * DDMS 3.0)
	 * @param noticeAttributes any notice attributes (optional, starting in DDMS 4.0)
	 * @param extensions any extensible attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed, or if one of the components
	 * does not belong at the top-level of the Resource.
	 */
	public Resource(List<IDDMSComponent> topLevelComponents, Boolean resourceElement, String createDate,
		Integer ismDESVersion, Integer ntkDESVersion, SecurityAttributes securityAttributes, NoticeAttributes noticeAttributes,
		ExtensibleAttributes extensions) throws InvalidDDMSException {
		try {
			String name = Resource.getName(DDMSVersion.getCurrentVersion());
			if (topLevelComponents == null)
				topLevelComponents = Collections.emptyList();
			Element element = Util.buildDDMSElement(name, null);
			String ismPrefix = PropertyReader.getPrefix("ism");
			String ntkPrefix = PropertyReader.getPrefix("ntk");
			// Attributes
			if (ntkDESVersion != null) {
				_cachedNtkDESVersion = ntkDESVersion;
				Util.addAttribute(element, ntkPrefix, DES_VERSION_NAME, 
					DDMSVersion.getCurrentVersion().getNtkNamespace(), ntkDESVersion.toString());
			}
			if (resourceElement != null) {
				Util.addAttribute(element, ismPrefix, RESOURCE_ELEMENT_NAME, 
					DDMSVersion.getCurrentVersion().getIsmNamespace(), String.valueOf(resourceElement));
			}
			if (ismDESVersion != null) {
				_cachedIsmDESVersion = ismDESVersion;
				Util.addAttribute(element, ismPrefix, DES_VERSION_NAME, 
					DDMSVersion.getCurrentVersion().getIsmNamespace(), ismDESVersion.toString());
			}
			if (!Util.isEmpty(createDate)) {
				try {
					_cachedCreateDate = getFactory().newXMLGregorianCalendar(createDate);
				}
				catch (IllegalArgumentException e) {
					throw new InvalidDDMSException("The ISM:createDate attribute is not in a valid date format.");
				}
				Util.addAttribute(element, ismPrefix, CREATE_DATE_NAME, 
					DDMSVersion.getCurrentVersion().getIsmNamespace(), getCreateDate().toXMLFormat());
			}
			_cachedNoticeAttributes = (noticeAttributes == null ? new NoticeAttributes(null, null, null, null)
				: noticeAttributes);
			_cachedNoticeAttributes.addTo(element);
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);
			_cachedExtensibleAttributes = (extensions == null ? new ExtensibleAttributes((List<Attribute>) null)
				: extensions);
			_cachedExtensibleAttributes.addTo(element);

			for (IDDMSComponent component : topLevelComponents) {
				// Resource Set
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
				else if (component instanceof Creator)
					_cachedCreators.add((Creator) component);
				else if (component instanceof Publisher)
					_cachedPublishers.add((Publisher) component);
				else if (component instanceof Contributor)
					_cachedContributors.add((Contributor) component);
				else if (component instanceof PointOfContact)
					_cachedPointOfContacts.add((PointOfContact) component);
				// Format Set
				else if (component instanceof Format)
					_cachedFormat = (Format) component;
				// Summary Set
				else if (component instanceof SubjectCoverage)
					_cachedSubjectCoverages.add((SubjectCoverage) component);
				else if (component instanceof VirtualCoverage)
					_cachedVirtualCoverages.add((VirtualCoverage) component);
				else if (component instanceof TemporalCoverage)
					_cachedTemporalCoverages.add((TemporalCoverage) component);
				else if (component instanceof GeospatialCoverage)
					_cachedGeospatialCoverages.add((GeospatialCoverage) component);
				else if (component instanceof RelatedResource)
					_cachedRelatedResources.add((RelatedResource) component);
				// Security Set
				else if (component instanceof Security)
					_cachedSecurity = (Security) component;
				// Extensible Layer
				else if (component instanceof ExtensibleElement)
					_cachedExtensibleElements.add((ExtensibleElement) component);
				else
					throw new InvalidDDMSException(component.getName()
						+ " is not a valid top-level component in a ddms:" + name + ".");
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
		_orderedList.addAll(getSubjectCoverages());
		_orderedList.addAll(getVirtualCoverages());
		_orderedList.addAll(getTemporalCoverages());
		_orderedList.addAll(getGeospatialCoverages());
		_orderedList.addAll(getRelatedResources());
		if (getSecurity() != null)
			_orderedList.add(getSecurity());
		_orderedList.addAll(getExtensibleElements());
	}

	/**
	 * Performs a Schematron validation of the DDMS Resource, via the ISO Schematron skeleton stylesheets for XSLT1
	 * or XSLT2 processors. This action can only be performed on a DDMS Resource which is already valid according 
	 * to the DDMS specification.
	 * 
	 * <p>The informational results of this validation are returned to the caller in a list of ValidationMessages of
	 * type "Warning" for reports and "Error" for failed asserts. These messages do NOT affect the validity of the
	 * underlying object model. The locator on the ValidationMessage will be the location attribute from the
	 * successful-report or failed-assert element.</p>
	 * 
	 * <p>Details about ISO Schematron can be found at: http://www.schematron.com/ </p>
	 * 
	 * @param schematronFile the file containing the ISO Schematron constraints. This file is transformed with the ISO
	 * Schematron skeleton files.
	 * @return a list of ValidationMessages
	 * @throws XSLException if there are XSL problems transforming with stylesheets
	 * @throws IOException if there are problems reading or parsing the Schematron file
	 */
	public List<ValidationMessage> validateWithSchematron(File schematronFile) throws XSLException, IOException {
		List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
		XSLTransform schematronTransform = Util.buildSchematronTransform(schematronFile);
		Nodes nodes = schematronTransform.transform(new Document(getXOMElementCopy()));
		Document doc = XSLTransform.toDocument(nodes);
		
		XPathContext context = XPathContext.makeNamespaceContext(doc.getRootElement());
		String svrlNamespace = context.lookup("svrl");
		Nodes outputNodes = doc.query("//svrl:failed-assert | //svrl:successful-report", context);
		for (int i = 0; i < outputNodes.size(); i++) {
			if (outputNodes.get(i) instanceof Element) {
				Element outputElement = (Element) outputNodes.get(i);
				boolean isAssert = "failed-assert".equals(outputElement.getLocalName());
				String text = outputElement.getFirstChildElement("text", svrlNamespace).getValue();
				String locator = outputElement.getAttributeValue("location");
				messages.add(isAssert ? ValidationMessage.newError(text, locator)
					: ValidationMessage.newWarning(text, locator));
			}
		}		
		return (messages);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>1-many identifiers, 1-many titles, 0-1 descriptions, 0-1 dates, 0-1 rights, 0-1 formats, exactly 1
	 * subjectCoverage, and exactly 1 security element must exist.</li>
	 * <li>Starting in DDMS 4.0, 1-many subjectCoverage elements can exist.</li>
	 * <li>At least 1 of creator, publisher, contributor, or pointOfContact must exist.</li>
	 * <li>If this resource has security attributes, the SecurityAttributes on any subcomponents are valid according 
	 * to rollup rules (security attributes are not required in DDMS 2.0).</li>
	 * <li>All child components are using the same version of DDMS.</li>
	 * <li>resourceElement attribute must exist, starting in DDMS 3.0.</li>
	 * <li>createDate attribute must exist and conform to the xs:date date type (YYYY-MM-DD), starting in DDMS 3.0.</li>
	 * <li>ISM DESVersion must exist and be a valid Integer, starting in DDMS 3.0.</li>
	 * <li>ISM DESVersion must exist and be "5" in DDMS 3.1.</li>
	 * <li>NTK DESVersion must exist and be a valid Integer, starting in DDMS 4.0.</li>
	 * <li>NTK DESVersion must exist and be "5" in DDMS 4.0.</li>
	 * <li>A classification is required, starting in DDMS 3.0.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty, starting in DDMS 3.0y.</li>
	 * <li>Only 1 extensible element can exist in DDMS 2.0.</li>
	 * </td></tr></table>
	 * 
	 * @see ISMVocabulary#validateRollup(SecurityAttributes, Set)
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Resource.getName(getDDMSVersion()));

		if (getIdentifiers().size() < 1)
			throw new InvalidDDMSException("At least 1 identifier is required.");
		if (getTitles().size() < 1)
			throw new InvalidDDMSException("At least 1 title is required.");
		if (getCreators().size() + getContributors().size() + getPublishers().size() + getPointOfContacts().size() == 0)
			throw new InvalidDDMSException(
				"At least 1 producer (creator, contributor, publisher, or pointOfContact) is required.");
		Util.requireBoundedDDMSChildCount(getXOMElement(), Description.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Dates.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Rights.getName(getDDMSVersion()), 0, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Format.getName(getDDMSVersion()), 0, 1);
		if (getDDMSVersion().isAtLeast("4.0")) {
			if (getSubjectCoverages().size() < 1)
				throw new InvalidDDMSException("At least 1 subjectCoverage is required.");		
		}
		else
			Util.requireBoundedDDMSChildCount(getXOMElement(), SubjectCoverage.getName(getDDMSVersion()), 1, 1);
		Util.requireBoundedDDMSChildCount(getXOMElement(), Security.getName(getDDMSVersion()), 1, 1);	
		if (!getSecurityAttributes().isEmpty()) {
			Set<SecurityAttributes> childAttributes = new HashSet<SecurityAttributes>();
			for (IDDMSComponent component : getTopLevelComponents()) {
				if (component.getSecurityAttributes() != null && !(component instanceof Security))
					childAttributes.add(component.getSecurityAttributes());
			}
			ISMVocabulary.validateRollup(getSecurityAttributes(), childAttributes);
		}
		
		// Should be reviewed as additional versions of DDMS are supported.
		if ("3.1".equals(getDDMSVersion().getVersion()) && !(new Integer(5).equals(getIsmDESVersion())))
			throw new InvalidDDMSException("The ISM:DESVersion must be 5 in DDMS 3.1 resources.");
		if ("4.0".equals(getDDMSVersion().getVersion()) && !(new Integer(5).equals(getNtkDESVersion())))
			throw new InvalidDDMSException("The ntk:DESVersion must be 5 in DDMS 4.0 resources.");
		if (!getDDMSVersion().isAtLeast("3.0") && getExtensibleElements().size() > 1) {
			throw new InvalidDDMSException("Only 1 extensible element is allowed in DDMS 2.0.");
		}
		if (getDDMSVersion().isAtLeast("3.0")) {
			Util.requireDDMSValue(RESOURCE_ELEMENT_NAME, isResourceElement());
			Util.requireDDMSValue(CREATE_DATE_NAME, getCreateDate());
			Util.requireDDMSValue(DES_VERSION_NAME, getIsmDESVersion());
			Util.requireDDMSValue("security attributes", getSecurityAttributes());
			getSecurityAttributes().requireClassification();
			if (!getCreateDate().getXMLSchemaType().equals(DatatypeConstants.DATE))
				throw new InvalidDDMSException("The createDate must be in the xs:date format (YYYY-MM-DD).");
		}
		
		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>If ddms:resource has no classification, warn about ignoring rollup validation.</li>
	 * <li>Add any warnings from the notice attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (!getNoticeAttributes().isEmpty()) {
			addWarnings(getNoticeAttributes().getValidationWarnings(), true);
		}
		if (getSecurityAttributes().isEmpty()) {
			addWarning("Security rollup validation is being skipped, because no classification exists "
				+ "on the ddms:" + getName() + " itself.");
		}
		super.validateWarnings();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();	
		if (isResourceElement() != null)
			text.append(buildOutput(isHTML, prefix + RESOURCE_ELEMENT_NAME, String.valueOf(isResourceElement()),
				true));
		if (getCreateDate() != null)
			text.append(buildOutput(isHTML, prefix + CREATE_DATE_NAME, getCreateDate().toXMLFormat(), true));
		if (getIsmDESVersion() != null)
			text.append(buildOutput(isHTML, prefix + "ism." + DES_VERSION_NAME, String.valueOf(getIsmDESVersion()), true));
		if (getNtkDESVersion() != null)
			text.append(buildOutput(isHTML, prefix + "ntk." + DES_VERSION_NAME, String.valueOf(getNtkDESVersion()), true));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		text.append(getNoticeAttributes().getOutput(isHTML, prefix));
		text.append(getExtensibleAttributes().getOutput(isHTML, prefix));
		for (IDDMSComponent component : getTopLevelComponents())
			text.append(isHTML ? component.toHTML() : component.toText());
		text.append(buildOutput(isHTML, "extensible.layer", String.valueOf(!getExtensibleElements().isEmpty()), true));
		text.append(buildOutput(isHTML, "ddms.generator", "DDMSence " + PropertyReader.getProperty("version"), true));
		text.append(buildOutput(isHTML, "ddms.version", getDDMSVersion().getVersion(),
			true));
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
			&& Util.nullEquals(getIsmDESVersion(), test.getIsmDESVersion())
			&& Util.nullEquals(getNtkDESVersion(), test.getNtkDESVersion())
			&& getNoticeAttributes().equals(test.getNoticeAttributes())
			&& getExtensibleAttributes().equals(test.getExtensibleAttributes()));
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
		if (getIsmDESVersion() != null)
			result = 7 * result + getIsmDESVersion().hashCode();
		if (getNtkDESVersion() != null)
			result = 7 * result + getNtkDESVersion().hashCode();
		result = 7 * result + getNoticeAttributes().hashCode();
		result = 7 * result + getExtensibleAttributes().hashCode();
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
		return (version.isAtLeast("4.0") ? "resource" : "Resource");
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
	public List<Creator> getCreators() {
		return (Collections.unmodifiableList(_cachedCreators));
	}

	/**
	 * Accessor for a list of all Publisher entities (0-many)
	 */
	public List<Publisher> getPublishers() {
		return (Collections.unmodifiableList(_cachedPublishers));
	}

	/**
	 * Accessor for a list of all Contributor entities (0-many)
	 */
	public List<Contributor> getContributors() {
		return (Collections.unmodifiableList(_cachedContributors));
	}

	/**
	 * Accessor for a list of all PointOfContact entities (0-many)
	 */
	public List<PointOfContact> getPointOfContacts() {
		return (Collections.unmodifiableList(_cachedPointOfContacts));
	}

	/**
	 * Accessor for the Format component (0-1). May return null.
	 */
	public Format getFormat() {
		return (_cachedFormat);
	}

	/**
	 * Accessor for the subjectCoverage component (1-many)
	 */
	public List<SubjectCoverage> getSubjectCoverages() {
		return _cachedSubjectCoverages;
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
	 * Accessor for the RelatedResource components (0-many)
	 */
	public List<RelatedResource> getRelatedResources() {
		return (Collections.unmodifiableList(_cachedRelatedResources));
	}

	/**
	 * Accessor for the security component (exactly 1). May return null but this cannot happen after instantiation.
	 */
	public Security getSecurity() {
		return (_cachedSecurity);
	}
	
	/**
	 * Accessor for the extensible layer elements (0-many in 3.0, 0-1 in 2.0).
	 */
	public List<ExtensibleElement> getExtensibleElements() {
		return (Collections.unmodifiableList(_cachedExtensibleElements));
	}

	/**
	 * Accessor for the resourceElement attribute. This may be null for v2.0 Resource components.
	 */
	public Boolean isResourceElement() {
		String value = getAttributeValue(RESOURCE_ELEMENT_NAME, getDDMSVersion().getIsmNamespace());
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
		return (_cachedCreateDate == null ? null
			: getFactory().newXMLGregorianCalendar(_cachedCreateDate.toXMLFormat()));
	}

	/**
	 * Accessor for the ISM DESVersion attribute. Because this attribute does not exist before DDMS 3.0, the accessor
	 * will return null for v2.0 Resource elements.
	 */
	public Integer getIsmDESVersion() {
		return (_cachedIsmDESVersion);
	}
	
	/**
	 * Accessor for the NTK DESVersion attribute.
	 */
	public Integer getNtkDESVersion() {
		return (_cachedNtkDESVersion);
	}

	/**
	 * Accessor for an ordered list of the components in this Resource. Components which are missing are not represented
	 * in this list (no null entries).
	 */
	public List<IDDMSComponent> getTopLevelComponents() {
		return (Collections.unmodifiableList(_orderedList));
	}
	
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		return (getTopLevelComponents());
	}

	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
	
	/**
	 * Accessor for the Notice Attributes. Will always be non-null even if the attributes are not set.
	 */
	public NoticeAttributes getNoticeAttributes() {
		return (_cachedNoticeAttributes);
	}
	
	/**
	 * Accessor for the extensible attributes. Will always be non-null, even if not set.
	 */
	public ExtensibleAttributes getExtensibleAttributes() {
		return (_cachedExtensibleAttributes);
	}
	
	/**
	 * Accesor for the datatype factory
	 */
	private static DatatypeFactory getFactory() {
		return (Util.getDataTypeFactory());
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -8581492714895157280L;
		private List<Identifier.Builder> _identifiers;
		private List<Title.Builder> _titles;
		private List<Subtitle.Builder> _subtitles;
		private Description.Builder _description;
		private List<Language.Builder> _languages;
		private Dates.Builder _dates;
		private Rights.Builder _rights;
		private List<Source.Builder> _sources;
		private List<Type.Builder> _types;
		private List<Creator.Builder> _creators;
		private List<Contributor.Builder> _contributors;
		private List<Publisher.Builder> _publishers;
		private List<PointOfContact.Builder> _pointOfContacts;
		private Format.Builder _format;
		private List<SubjectCoverage.Builder> _subjectCoverages;
		private List<VirtualCoverage.Builder> _virtualCoverages;
		private List<TemporalCoverage.Builder> _temporalCoverages;
		private List<GeospatialCoverage.Builder> _geospatialCoverages;
		private List<RelatedResource.Builder> _relatedResources;
		private Security.Builder _security;
		private List<ExtensibleElement.Builder> _extensibleElements;

		private String _createDate;
		private Boolean _resourceElement;
		private Integer _ismDESVersion;
		private Integer _ntkDESVersion;
		private NoticeAttributes.Builder _noticeAttributes;
		private SecurityAttributes.Builder _securityAttributes;
		private ExtensibleAttributes.Builder _extensibleAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Resource resource) {
			for (IDDMSComponent component : resource.getTopLevelComponents()) {
				// Resource Set
				if (component instanceof Identifier)
					getIdentifiers().add(new Identifier.Builder((Identifier) component));
				else if (component instanceof Title)
					getTitles().add(new Title.Builder((Title) component));
				else if (component instanceof Subtitle)
					getSubtitles().add(new Subtitle.Builder((Subtitle) component));
				else if (component instanceof Description)
					setDescription(new Description.Builder((Description) component));
				else if (component instanceof Language)
					getLanguages().add(new Language.Builder((Language) component));
				else if (component instanceof Dates)
					setDates(new Dates.Builder((Dates) component));
				else if (component instanceof Rights)
					setRights(new Rights.Builder((Rights) component));
				else if (component instanceof Source)
					getSources().add(new Source.Builder((Source) component));
				else if (component instanceof Type)
					getTypes().add(new Type.Builder((Type) component));
				else if (component instanceof Creator)
					getCreators().add(new Creator.Builder((Creator) component));
				else if (component instanceof Contributor)
					getContributors().add(new Contributor.Builder((Contributor) component));
				else if (component instanceof Publisher)
					getPublishers().add(new Publisher.Builder((Publisher) component));
				else if (component instanceof PointOfContact)
					getPointOfContacts().add(new PointOfContact.Builder((PointOfContact) component));
					
				// Format Set
				else if (component instanceof Format)
					setFormat(new Format.Builder((Format) component));
				// Summary Set
				else if (component instanceof SubjectCoverage)
					getSubjectCoverages().add(new SubjectCoverage.Builder((SubjectCoverage) component));
				else if (component instanceof VirtualCoverage)
					getVirtualCoverages().add(new VirtualCoverage.Builder((VirtualCoverage) component));
				else if (component instanceof TemporalCoverage)
					getTemporalCoverages().add(new TemporalCoverage.Builder((TemporalCoverage) component));
				else if (component instanceof GeospatialCoverage)
					getGeospatialCoverages().add(new GeospatialCoverage.Builder((GeospatialCoverage) component));
				else if (component instanceof RelatedResource)
					getRelatedResources().add(new RelatedResource.Builder((RelatedResource) component));
				// Security Set
				else if (component instanceof Security)
					setSecurity(new Security.Builder((Security) component));
				// Extensible Layer
				else if (component instanceof ExtensibleElement)
					getExtensibleElements().add(new ExtensibleElement.Builder((ExtensibleElement) component));
			}
			if (resource.getCreateDate() != null)
				setCreateDate(resource.getCreateDate().toXMLFormat());
			setResourceElement(resource.isResourceElement());
			setIsmDESVersion(resource.getIsmDESVersion());
			setNtkDESVersion(resource.getNtkDESVersion());
			setSecurityAttributes(new SecurityAttributes.Builder(resource.getSecurityAttributes()));
			setNoticeAttributes(new NoticeAttributes.Builder(resource.getNoticeAttributes()));
			setExtensibleAttributes(new ExtensibleAttributes.Builder(resource.getExtensibleAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Resource commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<IDDMSComponent> topLevelComponents = new ArrayList<IDDMSComponent>();
			for (IBuilder builder : getChildBuilders()) {
				IDDMSComponent component = builder.commit();
				if (component != null)
					topLevelComponents.add(component);
			}
			return (new Resource(topLevelComponents, getResourceElement(), getCreateDate(), getIsmDESVersion(), getNtkDESVersion(),
				getSecurityAttributes().commit(), getNoticeAttributes().commit(), getExtensibleAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getChildBuilders())
				hasValueInList = hasValueInList || !builder.isEmpty();
			return (!hasValueInList
				&& Util.isEmpty(getCreateDate())
				&& getResourceElement() == null
				&& getIsmDESVersion() == null
				&& getNtkDESVersion() == null
				&& getSecurityAttributes().isEmpty()
				&& getNoticeAttributes().isEmpty()
				&& getExtensibleAttributes().isEmpty());
		}
		
		/**
		 * Convenience method to get every child Builder in this Builder.
		 * 
		 * @return a list of IBuilders
		 */
		private List<IBuilder> getChildBuilders() {
			List<IBuilder> list = new ArrayList<IBuilder>();
			list.addAll(getIdentifiers());
			list.addAll(getTitles());
			list.addAll(getSubtitles());
			list.addAll(getLanguages());
			list.addAll(getSources());
			list.addAll(getTypes());
			list.addAll(getCreators());
			list.addAll(getContributors());
			list.addAll(getPublishers());
			list.addAll(getPointOfContacts());
			list.addAll(getSubjectCoverages());
			list.addAll(getVirtualCoverages());
			list.addAll(getTemporalCoverages());
			list.addAll(getGeospatialCoverages());
			list.addAll(getRelatedResources());
			list.addAll(getExtensibleElements());
			list.add(getDescription());
			list.add(getDates());
			list.add(getRights());
			list.add(getFormat());
			list.add(getSecurity());
			return (list);
		}
		
		/**
		 * Builder accessor for the identifiers
		 */
		public List<Identifier.Builder> getIdentifiers() {
			if (_identifiers == null)
				_identifiers = new LazyList(Identifier.Builder.class);
			return _identifiers;
		}
		
		/**
		 * Builder accessor for the titles
		 */
		public List<Title.Builder> getTitles() {
			if (_titles == null)
				_titles = new LazyList(Title.Builder.class);
			return _titles;
		}
		
		/**
		 * Builder accessor for the subtitles
		 */
		public List<Subtitle.Builder> getSubtitles() {
			if (_subtitles == null)
				_subtitles = new LazyList(Subtitle.Builder.class);
			return _subtitles;
		}
		
		/**
		 * Builder accessor for the description
		 */
		public Description.Builder getDescription() {
			if (_description == null)
				_description = new Description.Builder();
			return _description;
		}
		
		/**
		 * Builder accessor for the description
		 */
		public void setDescription(Description.Builder description) {
			_description = description;
		}
		
		/**
		 * Builder accessor for the languages
		 */
		public List<Language.Builder> getLanguages() {
			if (_languages == null)
				_languages = new LazyList(Language.Builder.class);
			return _languages;
		}
		
		/**
		 * Builder accessor for the dates
		 */
		public Dates.Builder getDates() {
			if (_dates == null)
				_dates = new Dates.Builder();
			return _dates;
		}
		
		/**
		 * Builder accessor for the dates
		 */
		public void setDates(Dates.Builder dates) {
			_dates = dates;
		}
		
		/**
		 * Builder accessor for the rights
		 */
		public Rights.Builder getRights() {
			if (_rights == null)
				_rights = new Rights.Builder();
			return _rights;
		}
		
		/**
		 * Builder accessor for the rights
		 */
		public void setRights(Rights.Builder rights) {
			_rights = rights;
		}
		
		/**
		 * Builder accessor for the sources
		 */
		public List<Source.Builder> getSources() {
			if (_sources == null)
				_sources = new LazyList(Source.Builder.class);
			return _sources;
		}
		
		/**
		 * Builder accessor for the types
		 */
		public List<Type.Builder> getTypes() {
			if (_types == null)
				_types = new LazyList(Type.Builder.class);
			return _types;
		}
		
		/**
		 * Convenience accessor for all of the producers. This list does not grow dynamically.
		 */
		public List<IBuilder> getProducers() {
			List<IBuilder> producers = new ArrayList<IBuilder>();
			producers.addAll(getCreators());
			producers.addAll(getContributors());
			producers.addAll(getPublishers());
			producers.addAll(getPointOfContacts());
			return (producers);
		}
		/**
		 * Builder accessor for creators
		 */
		public List<Creator.Builder> getCreators() {
			if (_creators == null)
				_creators = new LazyList(Creator.Builder.class);
			return _creators;
		}
		
		/**
		 * Builder accessor for contributors
		 */
		public List<Contributor.Builder> getContributors() {
			if (_contributors == null)
				_contributors = new LazyList(Contributor.Builder.class);
			return _contributors;
		}
		
		/**
		 * Builder accessor for publishers
		 */
		public List<Publisher.Builder> getPublishers() {
			if (_publishers == null)
				_publishers = new LazyList(Publisher.Builder.class);
			return _publishers;
		}
		
		/**
		 * Builder accessor for points of contact
		 */
		public List<PointOfContact.Builder> getPointOfContacts() {
			if (_pointOfContacts == null)
				_pointOfContacts = new LazyList(PointOfContact.Builder.class);
			return _pointOfContacts;
		}
				
		/**
		 * Builder accessor for the format
		 */
		public Format.Builder getFormat() {
			if (_format == null)
				_format = new Format.Builder();
			return _format;
		}
		
		/**
		 * Builder accessor for the format
		 */
		public void setFormat(Format.Builder format) {
			_format = format;
		}
		
		/**
		 * Builder accessor for the subjectCoverages
		 */
		public List<SubjectCoverage.Builder> getSubjectCoverages() {
			if (_subjectCoverages == null)
				_subjectCoverages = new LazyList(SubjectCoverage.Builder.class);
			return _subjectCoverages;
		}
				
		/**
		 * Builder accessor for the virtualCoverages
		 */
		public List<VirtualCoverage.Builder> getVirtualCoverages() {
			if (_virtualCoverages == null)
				_virtualCoverages = new LazyList(VirtualCoverage.Builder.class);
			return _virtualCoverages;
		}
		
		/**
		 * Builder accessor for the temporalCoverages
		 */
		public List<TemporalCoverage.Builder> getTemporalCoverages() {
			if (_temporalCoverages == null)
				_temporalCoverages = new LazyList(TemporalCoverage.Builder.class);
			return _temporalCoverages;
		}
		
		/**
		 * Builder accessor for the geospatialCoverages
		 */
		public List<GeospatialCoverage.Builder> getGeospatialCoverages() {
			if (_geospatialCoverages == null)
				_geospatialCoverages = new LazyList(GeospatialCoverage.Builder.class);
			return _geospatialCoverages;
		}
		
		/**
		 * Builder accessor for the relatedResources
		 */
		public List<RelatedResource.Builder> getRelatedResources() {
			if (_relatedResources == null)
				_relatedResources = new LazyList(RelatedResource.Builder.class);
			return _relatedResources;
		}
		
		/**
		 * Builder accessor for the security
		 */
		public Security.Builder getSecurity() {
			if (_security == null)
				_security = new Security.Builder();
			return _security;
		}
		
		/**
		 * Builder accessor for the security
		 */
		public void setSecurity(Security.Builder security) {
			_security = security;
		}
		
		/**
		 * Builder accessor for the extensibleElements
		 */
		public List<ExtensibleElement.Builder> getExtensibleElements() {
			if (_extensibleElements == null)
				_extensibleElements = new LazyList(ExtensibleElement.Builder.class);
			return _extensibleElements;
		}
		
		/**
		 * Builder accessor for the createDate attribute
		 */
		public String getCreateDate() {
			return _createDate;
		}
		
		/**
		 * Builder accessor for the createDate attribute
		 */
		public void setCreateDate(String createDate) {
			_createDate = createDate;
		}

		/**
		 * Accessor for the resourceElement attribute
		 */
		public Boolean getResourceElement() {
			return _resourceElement;
		}

		/**
		 * Accessor for the resourceElement attribute
		 */
		public void setResourceElement(Boolean resourceElement) {
			_resourceElement = resourceElement;
		}
		
		/**
		 * Builder accessor for the NTK DESVersion
		 */
		public Integer getNtkDESVersion() {
			return _ntkDESVersion;
		}
		
		/**
		 * Builder accessor for the NTK DESVersion
		 */
		public void setNtkDESVersion(Integer desVersion) {
			_ntkDESVersion = desVersion;
		}
		
		/**
		 * Builder accessor for the ISM DESVersion
		 */
		public Integer getIsmDESVersion() {
			return _ismDESVersion;
		}
		
		/**
		 * Builder accessor for the ISM DESVersion
		 */
		public void setIsmDESVersion(Integer desVersion) {
			_ismDESVersion = desVersion;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}
		
		/**
		 * Builder accessor for the noticeAttributes
		 */
		public NoticeAttributes.Builder getNoticeAttributes() {
			if (_noticeAttributes == null)
				_noticeAttributes = new NoticeAttributes.Builder();
			return _noticeAttributes;
		}
		
		/**
		 * Builder accessor for the noticeAttributes
		 */
		public void setNoticeAttributes(NoticeAttributes.Builder noticeAttributes) {
			_noticeAttributes = noticeAttributes;
		}	
		
		/**
		 * Builder accessor for the extensibleAttributes
		 */
		public ExtensibleAttributes.Builder getExtensibleAttributes() {
			if (_extensibleAttributes == null)
				_extensibleAttributes = new ExtensibleAttributes.Builder();
			return _extensibleAttributes;
		}
		
		/**
		 * Builder accessor for the extensibleAttributes
		 */
		public void setExtensibleAttributes(ExtensibleAttributes.Builder extensibleAttributes) {
			_extensibleAttributes = extensibleAttributes;
		}		
	}
}