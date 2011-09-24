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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleAttributesTest;
import buri.ddmsence.ddms.extensible.ExtensibleElement;
import buri.ddmsence.ddms.extensible.ExtensibleElementTest;
import buri.ddmsence.ddms.format.Extent;
import buri.ddmsence.ddms.format.Format;
import buri.ddmsence.ddms.format.FormatTest;
import buri.ddmsence.ddms.resource.Contributor;
import buri.ddmsence.ddms.resource.ContributorTest;
import buri.ddmsence.ddms.resource.Creator;
import buri.ddmsence.ddms.resource.CreatorTest;
import buri.ddmsence.ddms.resource.DatesTest;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.resource.IdentifierTest;
import buri.ddmsence.ddms.resource.Language;
import buri.ddmsence.ddms.resource.LanguageTest;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.PointOfContact;
import buri.ddmsence.ddms.resource.PointOfContactTest;
import buri.ddmsence.ddms.resource.Publisher;
import buri.ddmsence.ddms.resource.PublisherTest;
import buri.ddmsence.ddms.resource.ResourceManagementTest;
import buri.ddmsence.ddms.resource.RightsTest;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Source;
import buri.ddmsence.ddms.resource.SourceTest;
import buri.ddmsence.ddms.resource.Subtitle;
import buri.ddmsence.ddms.resource.SubtitleTest;
import buri.ddmsence.ddms.resource.Title;
import buri.ddmsence.ddms.resource.TitleTest;
import buri.ddmsence.ddms.resource.Type;
import buri.ddmsence.ddms.resource.TypeTest;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.SecurityTest;
import buri.ddmsence.ddms.security.ism.NoticeAttributes;
import buri.ddmsence.ddms.security.ism.NoticeAttributesTest;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.DescriptionTest;
import buri.ddmsence.ddms.summary.GeospatialCoverage;
import buri.ddmsence.ddms.summary.GeospatialCoverageTest;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.ddms.summary.Link;
import buri.ddmsence.ddms.summary.PostalAddress;
import buri.ddmsence.ddms.summary.RelatedResource;
import buri.ddmsence.ddms.summary.RelatedResourceTest;
import buri.ddmsence.ddms.summary.SubjectCoverageTest;
import buri.ddmsence.ddms.summary.TemporalCoverage;
import buri.ddmsence.ddms.summary.TemporalCoverageTest;
import buri.ddmsence.ddms.summary.VirtualCoverage;
import buri.ddmsence.ddms.summary.VirtualCoverageTest;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:resource elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class ResourceTest extends AbstractComponentTestCase {
	private List<IDDMSComponent> TEST_TOP_LEVEL_COMPONENTS;
	private List<IDDMSComponent> TEST_NO_OPTIONAL_COMPONENTS;

	private static final Boolean TEST_RESOURCE_ELEMENT = Boolean.TRUE;
	private static final String TEST_CREATE_DATE = "2010-01-21";

	/**
	 * Constructor
	 */
	public ResourceTest() throws InvalidDDMSException {
		super("resource.xml");
	}

	/**
	 * Regenerates all the components needed in a Resource
	 */
	private void createComponents() throws InvalidDDMSException {
		TEST_TOP_LEVEL_COMPONENTS = new ArrayList<IDDMSComponent>();
		TEST_TOP_LEVEL_COMPONENTS.add(SecurityTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(RelatedResourceTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(ResourceManagementTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(GeospatialCoverageTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(TemporalCoverageTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(VirtualCoverageTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(SubjectCoverageTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(FormatTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(PointOfContactTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(ContributorTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(PublisherTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(CreatorTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(TypeTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(SourceTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(RightsTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(DatesTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(LanguageTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(DescriptionTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(SubtitleTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(TitleTest.getFixture());
		TEST_TOP_LEVEL_COMPONENTS.add(IdentifierTest.getFixture());

		TEST_NO_OPTIONAL_COMPONENTS = new ArrayList<IDDMSComponent>();
		TEST_NO_OPTIONAL_COMPONENTS.add(IdentifierTest.getFixture());
		TEST_NO_OPTIONAL_COMPONENTS.add(TitleTest.getFixture());
		TEST_NO_OPTIONAL_COMPONENTS.add(CreatorTest.getFixture());
		TEST_NO_OPTIONAL_COMPONENTS.add(SubjectCoverageTest.getFixture());
		TEST_NO_OPTIONAL_COMPONENTS.add(SecurityTest.getFixture());
	}

	/**
	 * Creates a stub resource element that is otherwise correct, but leaves resource header attributes off.
	 * 
	 * @return the element
	 * @throws InvalidDDMSException
	 */
	private Element getResourceWithoutHeaderElement() throws InvalidDDMSException {
		Element element = Util.buildDDMSElement(Resource.getName(DDMSVersion.getCurrentVersion()), null);
		element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
		element.appendChild(TitleTest.getFixture().getXOMElementCopy());
		element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
		element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
		element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
		return (element);
	}

	/**
	 * Creates a stub resource element that is otherwise correct, but leaves resource components out.
	 * 
	 * @return the element
	 * @throws InvalidDDMSException
	 */
	private Element getResourceWithoutBodyElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		String ismPrefix = PropertyReader.getPrefix("ism");
		String ismNamespace = version.getIsmNamespace();
		String ntkPrefix = PropertyReader.getPrefix("ntk");
		String ntkNamespace = version.getNtkNamespace();

		Element element = Util.buildDDMSElement(Resource.getName(version), null);
		Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
			.valueOf(TEST_RESOURCE_ELEMENT));
		Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
		Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
			.valueOf(getIsmDESVersion()));
		if (version.isAtLeast("4.0"))
			Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
				.valueOf(getNtkDESVersion()));
		SecurityAttributesTest.getFixture().addTo(element);
		return (element);
	}

	/**
	 * Creates a stub resource element that contains a bunch of pre-DDMS 4.0 relatedResources in different
	 * configurations.
	 * 
	 * @return the element
	 * @throws InvalidDDMSException
	 */
	private Element getResourceWithMultipleRelated() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		if (version.isAtLeast("4.0"))
			return null;
		String ismPrefix = PropertyReader.getPrefix("ism");
		String ismNamespace = version.getIsmNamespace();

		Element element = Util.buildDDMSElement(Resource.getName(version), null);
		Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
			.valueOf(TEST_RESOURCE_ELEMENT));
		Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
		Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
			.valueOf(getIsmDESVersion()));
		SecurityAttributesTest.getFixture().addTo(element);
		element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
		element.appendChild(TitleTest.getFixture().getXOMElementCopy());
		element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
		element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());

		Link link = new Link(new XLinkAttributes("http://en.wikipedia.org/wiki/Tank", "role", null, null));

		// #1: a ddms:relatedResources containing 1 ddms:RelatedResource
		Element rel1 = Util.buildDDMSElement(RelatedResource.getName(version), null);
		Util.addDDMSAttribute(rel1, "relationship", "http://purl.org/dc/terms/references");
		Element innerElement = Util.buildDDMSElement("RelatedResource", null);
		Util.addDDMSAttribute(innerElement, "qualifier", "http://purl.org/dc/terms/URI");
		Util.addDDMSAttribute(innerElement, "value", "http://en.wikipedia.org/wiki/Tank1");
		innerElement.appendChild(link.getXOMElementCopy());
		rel1.appendChild(innerElement);
		element.appendChild(rel1);

		// #2: a ddms:relatedResources containing 3 ddms:RelatedResources
		Element rel2 = Util.buildDDMSElement(RelatedResource.getName(version), null);
		Util.addDDMSAttribute(rel2, "relationship", "http://purl.org/dc/terms/references");
		Element innerElement1 = Util.buildDDMSElement("RelatedResource", null);
		Util.addDDMSAttribute(innerElement1, "qualifier", "http://purl.org/dc/terms/URI");
		Util.addDDMSAttribute(innerElement1, "value", "http://en.wikipedia.org/wiki/Tank2");
		innerElement1.appendChild(link.getXOMElementCopy());
		Element innerElement2 = Util.buildDDMSElement("RelatedResource", null);
		Util.addDDMSAttribute(innerElement2, "qualifier", "http://purl.org/dc/terms/URI");
		Util.addDDMSAttribute(innerElement2, "value", "http://en.wikipedia.org/wiki/Tank3");
		innerElement2.appendChild(link.getXOMElementCopy());
		Element innerElement3 = Util.buildDDMSElement("RelatedResource", null);
		Util.addDDMSAttribute(innerElement3, "qualifier", "http://purl.org/dc/terms/URI");
		Util.addDDMSAttribute(innerElement3, "value", "http://en.wikipedia.org/wiki/Tank4");
		innerElement3.appendChild(link.getXOMElementCopy());
		rel2.appendChild(innerElement1);
		rel2.appendChild(innerElement2);
		rel2.appendChild(innerElement3);
		element.appendChild(rel2);

		element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
		return (element);
	}

	/**
	 * Returns an acceptable DESVersion for the version of DDMS
	 * 
	 * @return a DESVersion
	 */
	private Integer getIsmDESVersion() {
		if (!DDMSVersion.getCurrentVersion().isAtLeast("3.1"))
			return (new Integer(2));
		return (new Integer(5));
	}

	/**
	 * Returns an acceptable DESVersion for the version of DDMS
	 * 
	 * @return a DESVersion
	 */
	private Integer getNtkDESVersion() {
		if (!DDMSVersion.getCurrentVersion().isAtLeast("4.0"))
			return (null);
		return (new Integer(5));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Resource testConstructor(boolean expectFailure, Element element) {
		Resource component = null;
		try {
			component = new Resource(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create a DDMS 3.0 object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param topLevelComponents a list of top level components
	 * @param resourceElement value of the resourceElement attribute (required)
	 * @param createDate the create date as an xs:date (YYYY-MM-DD) (required)
	 * @param ismDESVersion the ISM DES Version as an Integer (required)
	 * @param ntkDESVersion the NTK DES Version as an Integer (required, starting in DDMS 4.0)
	 * @return a valid object
	 */
	private Resource testConstructor(boolean expectFailure, List<IDDMSComponent> topLevelComponents,
		Boolean resourceElement, String createDate, Integer ismDESVersion, Integer ntkDESVersion) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Resource component = null;
		try {
			NoticeAttributes notice = (!version.isAtLeast("4.0") ? null : NoticeAttributesTest.getFixture());
			SecurityAttributes attr = (!version.isAtLeast("3.0") ? null : SecurityAttributesTest.getFixture());
			component = new Resource(topLevelComponents, resourceElement, createDate, ismDESVersion, ntkDESVersion,
				attr, notice, null);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		String resourcePrefix = Resource.getName(version);
		if (version.isAtLeast("3.0")) {
			text.append(buildOutput(isHTML, resourcePrefix + ".resourceElement", "true"));
			text.append(buildOutput(isHTML, resourcePrefix + ".createDate", "2010-01-21"));
		}
		text.append(buildOutput(isHTML, resourcePrefix + ".ism.DESVersion", String.valueOf(getIsmDESVersion())));
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, resourcePrefix + ".ntk.DESVersion", String.valueOf(getNtkDESVersion())));
		}
		if (version.isAtLeast("3.0")) {
			text.append(buildOutput(isHTML, resourcePrefix + ".classification", "U"));
			text.append(buildOutput(isHTML, resourcePrefix + ".ownerProducer", "USA"));
		}
		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, resourcePrefix + ".noticeType", "noticeType"));
			text.append(buildOutput(isHTML, resourcePrefix + ".noticeReason", "noticeReason"));
			text.append(buildOutput(isHTML, resourcePrefix + ".noticeDate", "2011-09-15"));
			text.append(buildOutput(isHTML, resourcePrefix + ".unregisteredNoticeType", "unregisteredNoticeType"));
		}
		text.append(buildOutput(isHTML, "identifier.qualifier", "URI"));
		text.append(buildOutput(isHTML, "identifier.value", "urn:buri:ddmsence:testIdentifier"));
		text.append(buildOutput(isHTML, "title", "DDMSence"));
		text.append(buildOutput(isHTML, "title.classification", "U"));
		text.append(buildOutput(isHTML, "title.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "subtitle", "Version 0.1"));
		text.append(buildOutput(isHTML, "subtitle.classification", "U"));
		text.append(buildOutput(isHTML, "subtitle.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "description", "A transformation service."));
		text.append(buildOutput(isHTML, "description.classification", "U"));
		text.append(buildOutput(isHTML, "description.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "language.qualifier", "http://purl.org/dc/elements/1.1/language"));
		text.append(buildOutput(isHTML, "language.value", "en"));
		text.append(buildOutput(isHTML, "dates.created", "2003"));
		text.append(buildOutput(isHTML, "rights.privacyAct", "true"));
		text.append(buildOutput(isHTML, "rights.intellectualProperty", "true"));
		text.append(buildOutput(isHTML, "rights.copyright", "true"));
		text.append(buildOutput(isHTML, "source.value", "http://www.xmethods.com"));
		text.append(buildOutput(isHTML, "type.qualifier", "DCMITYPE"));
		text.append(buildOutput(isHTML, "type.value", "http://purl.org/dc/dcmitype/Text"));
		text.append(buildOutput(isHTML, "creator.entityType", Organization.getName(version)));
		text.append(buildOutput(isHTML, "creator.name", "DISA"));
		text.append(buildOutput(isHTML, "publisher.entityType", Person.getName(version)));
		text.append(buildOutput(isHTML, "publisher.name", "Brian"));
		text.append(buildOutput(isHTML, "publisher.surname", "Uri"));
		text.append(buildOutput(isHTML, "contributor.entityType", Service.getName(version)));
		text.append(buildOutput(isHTML, "contributor.name", "https://metadata.dod.mil/ebxmlquery/soap"));
		if (version.isAtLeast("3.0")) {
			text.append(buildOutput(isHTML, "pointOfContact.entityType", Unknown.getName(version)));
			text.append(buildOutput(isHTML, "pointOfContact.name", "UnknownEntity"));
		}
		else {
			text.append(buildOutput(isHTML, "pointOfContact.entityType", Person.getName(version)));
			text.append(buildOutput(isHTML, "pointOfContact.name", "Brian"));
			text.append(buildOutput(isHTML, "pointOfContact.surname", "Uri"));
		}

		String formatPrefix = (version.isAtLeast("4.0") ? "format." : "format.Media.");
		String subjectPrefix = (version.isAtLeast("4.0") ? "subjectCoverage." : "subjectCoverage.Subject.");
		String temporalPrefix = (version.isAtLeast("4.0") ? "temporalCoverage." : "temporalCoverage.TimePeriod.");
		String geospatialPrefix = version.isAtLeast("4.0") ? "geospatialCoverage."
			: "geospatialCoverage.GeospatialExtent.";
		String relatedPrefix = (version.isAtLeast("4.0") ? "relatedResource." : "relatedResources.RelatedResource.");

		text.append(buildOutput(isHTML, formatPrefix + "mimeType", "text/xml"));
		text.append(buildOutput(isHTML, subjectPrefix + "keyword", "DDMSence"));
		text.append(buildOutput(isHTML, "virtualCoverage.address", "123.456.789.0"));
		text.append(buildOutput(isHTML, "virtualCoverage.protocol", "IP"));
		text.append(buildOutput(isHTML, temporalPrefix + "name", "Unknown"));
		text.append(buildOutput(isHTML, temporalPrefix + "start", "1979-09-15"));
		text.append(buildOutput(isHTML, temporalPrefix + "end", "Not Applicable"));
		text.append(buildOutput(isHTML, geospatialPrefix + "boundingGeometry.Point.id", "IDValue"));
		text.append(buildOutput(isHTML, geospatialPrefix + "boundingGeometry.Point.srsName",
			"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D"));
		text.append(buildOutput(isHTML, geospatialPrefix + "boundingGeometry.Point.srsDimension", "10"));
		text.append(buildOutput(isHTML, geospatialPrefix + "boundingGeometry.Point.axisLabels", "A B C"));
		text.append(buildOutput(isHTML, geospatialPrefix + "boundingGeometry.Point.uomLabels", "Meter Meter Meter"));
		text.append(buildOutput(isHTML, geospatialPrefix + "boundingGeometry.Point.pos", "32.1 40.1"));
		text.append(buildOutput(isHTML, relatedPrefix + "relationship", "http://purl.org/dc/terms/references"));
		text.append(buildOutput(isHTML, relatedPrefix + "direction", "outbound"));
		text.append(buildOutput(isHTML, relatedPrefix + "qualifier", "http://purl.org/dc/terms/URI"));
		text.append(buildOutput(isHTML, relatedPrefix + "value", "http://en.wikipedia.org/wiki/Tank"));
		text.append(buildOutput(isHTML, relatedPrefix + "link.type", "locator"));
		text.append(buildOutput(isHTML, relatedPrefix + "link.href", "http://en.wikipedia.org/wiki/Tank"));
		text.append(buildOutput(isHTML, relatedPrefix + "link.role", "role"));

		if (version.isAtLeast("4.0")) {
			text.append(buildOutput(isHTML, "resourceManagement.processingInfo", "XSLT Transformation to convert DDMS 2.0 to DDMS 3.1."));
			text.append(buildOutput(isHTML, "resourceManagement.processingInfo.dateProcessed", "2011-08-19"));
			text.append(buildOutput(isHTML, "resourceManagement.processingInfo.classification", "U"));
			text.append(buildOutput(isHTML, "resourceManagement.processingInfo.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, "resourceManagement.classification", "U"));
			text.append(buildOutput(isHTML, "resourceManagement.ownerProducer", "USA"));
		}
		if (version.isAtLeast("3.0"))
			text.append(buildOutput(isHTML, "security.excludeFromRollup", "true"));
		text.append(buildOutput(isHTML, "security.classification", "U"));
		text.append(buildOutput(isHTML, "security.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "extensible.layer", "false"));
		text.append(buildOutput(isHTML, "ddms.generator", "DDMSence " + PropertyReader.getProperty("version")));
		text.append(buildOutput(isHTML, "ddms.version", version.getVersion()));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:").append(Resource.getName(version)).append(" ").append(getXmlnsDDMS());
		if (version.isAtLeast("4.0"))
			xml.append(" ").append(getXmlnsNTK());
		xml.append(" ").append(getXmlnsISM());
		if (version.isAtLeast("4.0"))
			xml.append(" ntk:DESVersion=\"").append(getNtkDESVersion()).append("\"");
		if (version.isAtLeast("3.0"))
			xml.append(" ISM:resourceElement=\"true\"");
		// Adding DESVersion in DDMS 2.0 allows the namespace declaration to definitely be in the Resource element.
		xml.append(" ISM:DESVersion=\"").append(getIsmDESVersion()).append("\"");
		if (version.isAtLeast("3.0"))
			xml.append(" ISM:createDate=\"2010-01-21\"");
		if (version.isAtLeast("4.0"))
			xml.append(" ISM:noticeType=\"noticeType\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ISM:unregisteredNoticeType=\"unregisteredNoticeType\"");
		if (version.isAtLeast("3.0"))
			xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		xml.append(">\n");
		xml.append("\t<ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence:testIdentifier\" />\n");
		xml.append("\t<ddms:title ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DDMSence</ddms:title>\n");
		xml.append("\t<ddms:subtitle ISM:classification=\"U\" ISM:ownerProducer=\"USA\">Version 0.1</ddms:subtitle>\n");
		xml.append("\t<ddms:description ISM:classification=\"U\" ISM:ownerProducer=\"USA\">A transformation service.</ddms:description>\n");
		xml.append("\t<ddms:language ddms:qualifier=\"http://purl.org/dc/elements/1.1/language\" ddms:value=\"en\" />\n");
		xml.append("\t<ddms:dates ddms:created=\"2003\" />\n");
		xml.append("\t<ddms:rights ddms:privacyAct=\"true\" ddms:intellectualProperty=\"true\" ddms:copyright=\"true\" />\n");
		xml.append("\t<ddms:source ddms:value=\"http://www.xmethods.com\" />\n");
		xml.append("\t<ddms:type ddms:qualifier=\"DCMITYPE\" ddms:value=\"http://purl.org/dc/dcmitype/Text\" />\n");
		xml.append("\t<ddms:creator>\n");
		xml.append("\t\t<ddms:").append(Organization.getName(version)).append(">\n");
		xml.append("\t\t\t<ddms:name>DISA</ddms:name>\n");
		xml.append("\t\t</ddms:").append(Organization.getName(version)).append(">\t\n");
		xml.append("\t</ddms:creator>\n");
		xml.append("\t<ddms:publisher>\n");
		xml.append("\t\t<ddms:").append(Person.getName(version)).append(">\n");
		xml.append("\t\t\t<ddms:name>Brian</ddms:name>\n");
		xml.append("\t\t\t<ddms:surname>Uri</ddms:surname>\n");
		xml.append("\t\t</ddms:").append(Person.getName(version)).append(">\t\n");
		xml.append("\t</ddms:publisher>\n");
		xml.append("\t<ddms:contributor>\n");
		xml.append("\t\t<ddms:").append(Service.getName(version)).append(">\n");
		xml.append("\t\t\t<ddms:name>https://metadata.dod.mil/ebxmlquery/soap</ddms:name>\n");
		xml.append("\t\t</ddms:").append(Service.getName(version)).append(">\t\n");
		xml.append("\t</ddms:contributor>\n");
		xml.append("\t<ddms:pointOfContact>\n");
		if (version.isAtLeast("3.0")) {
			xml.append("\t\t<ddms:").append(Unknown.getName(version)).append(">\n");
			xml.append("\t\t\t<ddms:name>UnknownEntity</ddms:name>\n");
			xml.append("\t\t</ddms:").append(Unknown.getName(version)).append(">\t\n");
		}
		else {
			xml.append("\t\t<ddms:").append(Person.getName(version)).append(">\n");
			xml.append("\t\t\t<ddms:name>Brian</ddms:name>\n");
			xml.append("\t\t\t<ddms:surname>Uri</ddms:surname>\n");
			xml.append("\t\t</ddms:").append(Person.getName(version)).append(">\n");
		}
		xml.append("\t</ddms:pointOfContact>\n");
		xml.append("\t<ddms:format>\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t\t<ddms:mimeType>text/xml</ddms:mimeType>\n");
		}
		else {
			xml.append("\t\t<ddms:Media>\n");
			xml.append("\t\t\t<ddms:mimeType>text/xml</ddms:mimeType>\n");
			xml.append("\t\t</ddms:Media>\n");
		}
		xml.append("\t</ddms:format>\n");
		xml.append("\t<ddms:subjectCoverage>\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
		}
		else {
			xml.append("\t\t<ddms:Subject>\n");
			xml.append("\t\t\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
			xml.append("\t\t</ddms:Subject>\n");
		}
		xml.append("\t</ddms:subjectCoverage>\n");
		xml.append("\t<ddms:virtualCoverage ddms:address=\"123.456.789.0\" ddms:protocol=\"IP\" />\n");
		xml.append("\t<ddms:temporalCoverage>\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t\t<ddms:start>1979-09-15</ddms:start>\n");
			xml.append("\t\t<ddms:end>Not Applicable</ddms:end>\n");
		}
		else {
			xml.append("\t\t<ddms:TimePeriod>\n");
			xml.append("\t\t\t<ddms:start>1979-09-15</ddms:start>\n");
			xml.append("\t\t\t<ddms:end>Not Applicable</ddms:end>\n");
			xml.append("\t\t</ddms:TimePeriod>\n");
		}
		xml.append("\t</ddms:temporalCoverage>\n");
		xml.append("\t<ddms:geospatialCoverage>\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t\t<ddms:boundingGeometry>\n");
			xml.append("\t\t\t<gml:Point xmlns:gml=\"").append(version.getGmlNamespace()).append("\" ");
			xml.append(
				"gml:id=\"IDValue\" srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" srsDimension=\"10\" ")
				.append("axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\">\n");
			xml.append("\t\t\t\t<gml:pos>32.1 40.1</gml:pos>\n");
			xml.append("\t\t\t</gml:Point>\n");
			xml.append("\t\t</ddms:boundingGeometry>\n");
		}
		else {
			xml.append("\t\t<ddms:GeospatialExtent>\n");
			xml.append("\t\t\t<ddms:boundingGeometry>\n");
			xml.append("\t\t\t\t<gml:Point xmlns:gml=\"").append(version.getGmlNamespace()).append("\" ");
			xml.append(
				"gml:id=\"IDValue\" srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" srsDimension=\"10\" ")
				.append("axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\">\n");
			xml.append("\t\t\t\t\t<gml:pos>32.1 40.1</gml:pos>\n");
			xml.append("\t\t\t\t</gml:Point>\n");
			xml.append("\t\t\t</ddms:boundingGeometry>\n");
			xml.append("\t\t</ddms:GeospatialExtent>\n");
		}
		xml.append("\t</ddms:geospatialCoverage>\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t<ddms:relatedResource ddms:relationship=\"http://purl.org/dc/terms/references\" ").append(
				"ddms:direction=\"outbound\" ddms:qualifier=\"http://purl.org/dc/terms/URI\" ").append(
				"ddms:value=\"http://en.wikipedia.org/wiki/Tank\">\n");
			xml.append("\t\t<ddms:link xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"locator\" ").append(
				"xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"role\" />\n");
			xml.append("\t</ddms:relatedResource>\n");
		}
		else {
			xml.append("\t<ddms:relatedResources ddms:relationship=\"http://purl.org/dc/terms/references\" ").append(
				"ddms:direction=\"outbound\">\n");
			xml.append("\t\t<ddms:RelatedResource ddms:qualifier=\"http://purl.org/dc/terms/URI\" ").append(
				"ddms:value=\"http://en.wikipedia.org/wiki/Tank\">\n");
			xml.append("\t\t\t<ddms:link xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"locator\" ").append(
				"xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"role\" />\n");
			xml.append("\t\t</ddms:RelatedResource>\n");
			xml.append("\t</ddms:relatedResources>\n");
		}
		if (version.isAtLeast("4.0")) {
			xml.append("\t<ddms:resourceManagement ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
			xml.append("<ddms:processingInfo ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ddms:dateProcessed=\"2011-08-19\">");
			xml.append("XSLT Transformation to convert DDMS 2.0 to DDMS 3.1.</ddms:processingInfo></ddms:resourceManagement>\n");
		}
		xml.append("\t<ddms:security ");
		if (version.isAtLeast("3.0"))
			xml.append("ISM:excludeFromRollup=\"true\" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\" />\n");
		xml.append("</ddms:").append(Resource.getName(version)).append(">");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				Resource.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);

			// More than 1 subjectCoverage
			if (version.isAtLeast("4.0")) {
				element = getResourceWithoutBodyElement();
				element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
				element.appendChild(TitleTest.getFixture().getXOMElementCopy());
				element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
				testConstructor(WILL_SUCCEED, element);
			}
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// All fields
			testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();
			String ismPrefix = PropertyReader.getPrefix("ism");
			String ismNamespace = version.getIsmNamespace();
			String ntkPrefix = PropertyReader.getPrefix("ntk");
			String ntkNamespace = version.getNtkNamespace();

			if (version.isAtLeast("3.0")) {
				// Missing resourceElement
				Element element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);

				// Empty resourceElement
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, "");
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);

				// Invalid resourceElement
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, "aardvark");
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);

				// Missing createDate
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);

				// Invalid createDate
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, "2004");
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);

				// Missing desVersion
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);

				// desVersion not an integer
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, "one");
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);
			}
			if (version.isAtLeast("4.0")) {
				// NTK desVersion not an integer
				Element element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, "one");
				SecurityAttributesTest.getFixture().addTo(element);
				testConstructor(WILL_FAIL, element);
			}

			// At least 1 producer
			Element element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// At least 1 identifier
			element = getResourceWithoutBodyElement();
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// At least 1 title
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 description
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(DescriptionTest.getFixture().getXOMElementCopy());
			element.appendChild(DescriptionTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 dates
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(DatesTest.getFixture().getXOMElementCopy());
			element.appendChild(DatesTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 rights
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(RightsTest.getFixture().getXOMElementCopy());
			element.appendChild(RightsTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 formats
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(FormatTest.getFixture().getXOMElementCopy());
			element.appendChild(FormatTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 resourceManagement
			if (version.isAtLeast("4.0")) {
				element = getResourceWithoutBodyElement();
				element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
				element.appendChild(TitleTest.getFixture().getXOMElementCopy());
				element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(ResourceManagementTest.getFixture().getXOMElementCopy());
				element.appendChild(ResourceManagementTest.getFixture().getXOMElementCopy());
				element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
				testConstructor(WILL_FAIL, element);
			}
			
			// At least 1 subjectCoverage
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 subjectCoverage
			if (!version.isAtLeast("4.0")) {
				element = getResourceWithoutBodyElement();
				element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
				element.appendChild(TitleTest.getFixture().getXOMElementCopy());
				element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
				testConstructor(WILL_FAIL, element);
			}

			// At least 1 security
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 security
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No top level components
			element = getResourceWithoutBodyElement();
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			if (version.isAtLeast("3.0")) {
				// Missing createDate
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, null,
					getIsmDESVersion(), getNtkDESVersion());

				// Invalid createDate
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, "2001",
					getIsmDESVersion(), getNtkDESVersion());

				// Nonsensical createDate
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, "notAnXmlDate",
					getIsmDESVersion(), getNtkDESVersion());

				// Missing desVersion
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, null,
					getNtkDESVersion());
			}
			if (version.isAtLeast("4.0")) {
				// Missing desVersion
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
					getIsmDESVersion(), null);
			}

			// At least 1 producer
			List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(CreatorTest.getFixture());
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// At least 1 identifier
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(IdentifierTest.getFixture());
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// At least 1 title
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(TitleTest.getFixture());
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// At least 1 subjectCoverage
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(SubjectCoverageTest.getFixture());
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// At least 1 security
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(CreatorTest.getFixture());
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// No top level components
			testConstructor(WILL_FAIL, null, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// Non-top-level component
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new Keyword("test", null));
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// base warnings (1 for 2.0, 0 for all others)
			int warnings = version.isAtLeast("3.0") ? 0 : 1;
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(warnings, component.getValidationWarnings().size());

			// Nested warnings
			List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new Format("test", new Extent("test", ""), "test"));
			component = testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());
			warnings = version.isAtLeast("3.0") ? 1 : 2;
			assertEquals(warnings, component.getValidationWarnings().size());

			String resourceName = Resource.getName(version);
			if (!version.isAtLeast("3.0")) {
				String text = "Security rollup validation is being skipped, because no classification exists "
					+ "on the ddms:" + resourceName + " itself.";
				String locator = "ddms:" + resourceName;
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

				text = "A qualifier has been set without an accompanying value attribute.";
				locator = "ddms:" + resourceName + "/ddms:format/ddms:Media/ddms:extent";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(1));
			}
			else {
				String text = "A qualifier has been set without an accompanying value attribute.";
				String locator = (version.isAtLeast("4.0")) ? "ddms:" + resourceName + "/ddms:format/ddms:extent"
					: "ddms:" + resourceName + "/ddms:format/ddms:Media/ddms:extent";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}

			// More nested warnings
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			PostalAddress address = new PostalAddress(element);
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new GeospatialCoverage(null, null, null, address, null, null, null, null));
			component = testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());
			warnings = version.isAtLeast("3.0") ? 1 : 2;
			assertEquals(warnings, component.getValidationWarnings().size());
			if (!version.isAtLeast("3.0")) {
				String text = "Security rollup validation is being skipped, because no classification exists "
					+ "on the ddms:" + resourceName + " itself.";
				String locator = "ddms:" + resourceName;
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

				text = "A completely empty ddms:postalAddress element was found.";
				locator = "ddms:" + resourceName + "/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:postalAddress";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(1));
			}
			else {
				String text = "A completely empty ddms:postalAddress element was found.";
				String locator = (version.isAtLeast("4.0")) ? "ddms:" + resourceName
					+ "/ddms:geospatialCoverage/ddms:postalAddress" : "ddms:" + resourceName
					+ "/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:postalAddress";
				assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Resource dataComponent = (!version.isAtLeast("3.0") ? testConstructor(WILL_SUCCEED,
				TEST_TOP_LEVEL_COMPONENTS, null, null, getIsmDESVersion(), getNtkDESVersion()) : testConstructor(
				WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion()));
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Resource dataComponent = testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, false, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				"1999-10-10", getIsmDESVersion(), getNtkDESVersion());
			assertFalse(elementComponent.equals(dataComponent));

			// Can only use alternate DESVersions in early DDMS versions
			if (!version.isAtLeast("3.1")) {
				dataComponent = testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
					TEST_CREATE_DATE, new Integer(1), getNtkDESVersion());
				assertFalse(elementComponent.equals(dataComponent));
			}

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, getIsmDESVersion(), getNtkDESVersion());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = (!version.isAtLeast("3.0") ? testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, null,
				null, getIsmDESVersion(), getNtkDESVersion()) : testConstructor(WILL_SUCCEED,
				TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion()));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = (!version.isAtLeast("3.0") ? testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, null,
				null, getIsmDESVersion(), getNtkDESVersion()) : testConstructor(WILL_SUCCEED,
				TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion()));
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testRollupTooRestrictive() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			List<String> ownerProducers = new ArrayList<String>();
			ownerProducers.add("USA");
			Organization org = new Organization(Util.getXsListAsList("DISA"), null, null, null, null, null);
			Creator creator = new Creator(org, null, new SecurityAttributes("TS", ownerProducers, null));

			Element element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(creator.getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testSkipRollupIfNotAvailable() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		createComponents();

		List<String> ownerProducers = new ArrayList<String>();
		ownerProducers.add("USA");
		Organization org = new Organization(Util.getXsListAsList("DISA"), null, null, null, null, null);
		Creator creator = new Creator(org, null, new SecurityAttributes("TS", ownerProducers, null));

		Element element = Util.buildDDMSElement(Resource.getName(version), null);
		element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
		element.appendChild(TitleTest.getFixture().getXOMElementCopy());
		element.appendChild(creator.getXOMElementCopy());
		element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
		element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
		testConstructor(WILL_SUCCEED, element);
	}

	public void testRollupWrongSystem() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			if (version.isAtLeast("3.1"))
				continue;
			createComponents();

			List<String> ownerProducers = new ArrayList<String>();
			ownerProducers.add("USA");
			Organization org = new Organization(Util.getXsListAsList("DISA"), null, null, null, null, null);
			Creator creator = new Creator(org, null, new SecurityAttributes("CTSA", ownerProducers, null));

			Element element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(creator.getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		// Security attributes do not exist in 2.0
		new Resource(TEST_TOP_LEVEL_COMPONENTS);

		// But attributes can still be used.
		new Resource(TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
			SecurityAttributesTest.getFixture());
	}

	public void testExtensibleSuccess() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// Extensible attribute added
			ExtensibleAttributes attr = ExtensibleAttributesTest.getFixture();
			if (!version.isAtLeast("3.0"))
				new Resource(TEST_TOP_LEVEL_COMPONENTS, attr);
			else
				new Resource(TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
					getNtkDESVersion(), SecurityAttributesTest.getFixture(), null, attr);
		}
	}

	public void test20ExtensibleElementSize() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		String ismPrefix = PropertyReader.getPrefix("ism");

		// ISM:DESVersion in element
		Element element = getResourceWithoutHeaderElement();
		Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, version.getIsmNamespace(), String
			.valueOf(getIsmDESVersion()));
		Resource component = new Resource(element);
		assertEquals(getIsmDESVersion(), component.getIsmDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(0, component.getExtensibleAttributes().getAttributes().size());

		// ISM:classification in element
		element = getResourceWithoutHeaderElement();
		Util.addAttribute(element, ismPrefix, SecurityAttributes.CLASSIFICATION_NAME, version.getIsmNamespace(), "U");
		component = new Resource(element);
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(0, component.getExtensibleAttributes().getAttributes().size());

		// ddmsence:confidence in element
		element = getResourceWithoutHeaderElement();
		Util.addAttribute(element, "ddmsence", "confidence", "http://ddmsence.urizone.net/", "95");
		component = new Resource(element);
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());
	}

	public void test20ExtensibleDataSizes() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		createComponents();

		// This can be a parameter or an extensible.
		Attribute icAttribute = new Attribute("ISM:DESVersion", version.getIsmNamespace(), "2");
		// This can be a securityAttribute or an extensible.
		Attribute secAttribute = new Attribute("ISM:classification", version.getIsmNamespace(), "U");
		// This can be an extensible.
		Attribute uniqueAttribute = new Attribute("ddmsence:confidence", "http://ddmsence.urizone.net/", "95");
		List<Attribute> exAttr = new ArrayList<Attribute>();

		// Base Case
		Resource component = new Resource(TEST_TOP_LEVEL_COMPONENTS);
		assertNull(component.getIsmDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(0, component.getExtensibleAttributes().getAttributes().size());

		// icAttribute as parameter, uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, getIsmDESVersion(), getNtkDESVersion(), null,
			null, new ExtensibleAttributes(exAttr));
		assertEquals(getIsmDESVersion(), component.getIsmDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());

		// icAttribute and uniqueAttribute as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(icAttribute));
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, new ExtensibleAttributes(exAttr));
		assertNull(component.getIsmDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());

		// secAttribute as securityAttribute, uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, null, null,
			SecurityAttributesTest.getFixture(), null, new ExtensibleAttributes(exAttr));
		assertNull(component.getIsmDESVersion());
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());

		// secAttribute and uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(secAttribute));
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, new ExtensibleAttributes(exAttr));
		assertNull(component.getIsmDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());

		// icAttribute as parameter, secAttribute as securityAttribute, uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, getIsmDESVersion(), getNtkDESVersion(),
			SecurityAttributesTest.getFixture(), null, new ExtensibleAttributes(exAttr));
		assertEquals(getIsmDESVersion(), component.getIsmDESVersion());
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());

		// icAttribute as parameter, secAttribute and uniqueAttribute as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(secAttribute));
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, getIsmDESVersion(), getNtkDESVersion(), null,
			null, new ExtensibleAttributes(exAttr));
		assertEquals(getIsmDESVersion(), component.getIsmDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());

		// secAttribute as securityAttribute, icAttribute and uniqueAttribute as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(icAttribute));
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, null, null,
			SecurityAttributesTest.getFixture(), null, new ExtensibleAttributes(exAttr));
		assertNull(component.getIsmDESVersion());
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());

		// all three as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(icAttribute));
		exAttr.add(new Attribute(secAttribute));
		exAttr.add(new Attribute(uniqueAttribute));
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, new ExtensibleAttributes(exAttr));
		assertNull(component.getIsmDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(3, component.getExtensibleAttributes().getAttributes().size());
	}

	public void testExtensibleDataDuplicates() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// DESVersion in parameter AND extensible.
			try {
				List<Attribute> exAttr = new ArrayList<Attribute>();
				exAttr.add(new Attribute("ISM:DESVersion", version.getIsmNamespace(), "2"));
				new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, getIsmDESVersion(), getNtkDESVersion(),
					SecurityAttributesTest.getFixture(), null, new ExtensibleAttributes(exAttr));
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}

			// classification in securityAttributes AND extensible.
			try {
				List<Attribute> exAttr = new ArrayList<Attribute>();
				exAttr.add(new Attribute("ISM:classification", version.getIsmNamespace(), "U"));
				new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, null, null, SecurityAttributesTest.getFixture(),
					null, new ExtensibleAttributes(exAttr));
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testExtensibleElementElementConstructor() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getFixtureElement());
			Element element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			element.appendChild(component.getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testExtensibleElementOutput() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();
		ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getFixtureElement());

		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(component);
		Resource resource = testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
			getIsmDESVersion(), getNtkDESVersion());
		assertTrue(resource.toHTML().indexOf(buildOutput(true, "extensible.layer", "true")) != -1);
		assertTrue(resource.toText().indexOf(buildOutput(false, "extensible.layer", "true")) != -1);
	}

	public void testWrongVersionExtensibleElementAllowed() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getFixtureElement());
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();

		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(component);
		testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
			getNtkDESVersion());
	}

	public void test20TooManyExtensibleElements() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();

		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		testConstructor(WILL_FAIL, components, null, null, null, null);
	}

	public void testAfter20TooManyExtensibleElements() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();

		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
			getNtkDESVersion());
	}

	public void test20DeclassManualReviewAttribute() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		String ismNamespace = version.getIsmNamespace();

		Element element = getResourceWithoutHeaderElement();
		Util.addAttribute(element, PropertyReader.getPrefix("ism"), SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME,
			ismNamespace, "true");
		SecurityAttributesTest.getFixture().addTo(element);
		Resource resource = testConstructor(WILL_SUCCEED, element);

		// ISM:declassManualReview should not get picked up as an extensible attribute
		assertEquals(0, resource.getExtensibleAttributes().getAttributes().size());
	}

	public void testRelatedResourcesMediation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();
			if (version.isAtLeast("4.0"))
				continue;

			Element element = getResourceWithMultipleRelated();
			Resource resource = testConstructor(WILL_SUCCEED, element);
			assertEquals(4, resource.getRelatedResources().size());
			assertEquals("http://en.wikipedia.org/wiki/Tank1", resource.getRelatedResources().get(0).getValue());
			assertEquals("http://en.wikipedia.org/wiki/Tank2", resource.getRelatedResources().get(1).getValue());
			assertEquals("http://en.wikipedia.org/wiki/Tank3", resource.getRelatedResources().get(2).getValue());
			assertEquals("http://en.wikipedia.org/wiki/Tank4", resource.getRelatedResources().get(3).getValue());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			Resource.Builder builder = new Resource.Builder(component);
			assertEquals(builder.commit(), component);

			// Equality with ExtensibleElement
			builder.getExtensibleElements().add(new ExtensibleElement.Builder());
			builder.getExtensibleElements().get(0).setXml(
				"<ddmsence:extension xmlns:ddmsence=\"http://ddmsence.urizone.net/\">"
					+ "This is an extensible element.</ddmsence:extension>");
			component = builder.commit();
			builder = new Resource.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Resource.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Resource.Builder();
			builder.setCreateDate(TEST_CREATE_DATE);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testBuild20Commit30() throws InvalidDDMSException {
		// Version during building should be 100% irrelevant
		DDMSVersion version = DDMSVersion.setCurrentVersion("2.0");
		Resource.Builder builder = new Resource.Builder();
		builder.setResourceElement(TEST_RESOURCE_ELEMENT);
		builder.setCreateDate(TEST_CREATE_DATE);
		builder.setIsmDESVersion(getIsmDESVersion());
		builder.getSecurityAttributes().setClassification("U");
		builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));

		builder.getIdentifiers().get(0).setQualifier("testQualifier");
		builder.getIdentifiers().get(0).setValue("testValue");
		builder.getTitles().get(0).setValue("testTitle");
		builder.getTitles().get(0).getSecurityAttributes().setClassification("U");
		builder.getTitles().get(0).getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
		builder.getCreators().get(0).setEntityType(Organization.getName(version));
		builder.getCreators().get(0).getOrganization().setNames(Util.getXsListAsList("testName"));
		builder.getSubjectCoverages().get(0).getKeywords().get(0).setValue("keyword");
		builder.getSecurity().getSecurityAttributes().setClassification("U");
		builder.getSecurity().getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
		DDMSVersion.setCurrentVersion("3.0");
		builder.commit();

		// Using a 2.0-specific value
		builder.getSecurity().getSecurityAttributes().setClassification("NS-S");
		try {
			builder.commit();
			fail("Builder allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuild30Commit20() throws InvalidDDMSException {
		// Version during building should be 100% irrelevant
		DDMSVersion version = DDMSVersion.setCurrentVersion("3.0");
		Resource.Builder builder = new Resource.Builder();
		builder.setResourceElement(TEST_RESOURCE_ELEMENT);
		builder.setIsmDESVersion(getIsmDESVersion());
		builder.getSecurityAttributes().setClassification("U");
		builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));

		builder.getIdentifiers().get(0).setQualifier("testQualifier");
		builder.getIdentifiers().get(0).setValue("testValue");
		builder.getTitles().get(0).setValue("testTitle");
		builder.getTitles().get(0).getSecurityAttributes().setClassification("U");
		builder.getTitles().get(0).getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
		builder.getCreators().get(0).setEntityType(Organization.getName(version));
		builder.getCreators().get(0).getOrganization().setNames(Util.getXsListAsList("testName"));
		builder.getSubjectCoverages().get(0).getKeywords().get(0).setValue("keyword");
		builder.getSecurity().getSecurityAttributes().setClassification("U");
		builder.getSecurity().getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));

		DDMSVersion.setCurrentVersion("2.0");
		builder.commit();

		// Using a 3.0-specific value
		builder.getSubjectCoverages().get(0).getSecurityAttributes().setClassification("U");
		builder.getSubjectCoverages().get(0).getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
		try {
			builder.commit();
			fail("Builder allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testLoad30Commit20() throws InvalidDDMSException {
		Resource.Builder builder = new Resource.Builder(new Resource(getValidElement("3.0")));

		// Direct mapping works
		DDMSVersion.setCurrentVersion("3.0");
		builder.commit();

		// Transform back to 2.0 fails on 3.0-specific fields
		DDMSVersion.setCurrentVersion("2.0");
		try {
			builder.commit();
			fail("Builder allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}

		// Wiping of 3.0-specific fields works
		builder.getPointOfContacts().clear();
		builder.commit();
	}

	public void testLoad20Commit30() throws InvalidDDMSException {
		Resource.Builder builder = new Resource.Builder(new Resource(getValidElement("2.0")));

		// Direct mapping works
		DDMSVersion.setCurrentVersion("2.0");
		builder.commit();

		// Transform up to 3.0 fails on 3.0-specific fields
		DDMSVersion.setCurrentVersion("3.0");
		try {
			builder.commit();
			fail("Builder allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}

		// Adding 3.0-specific fields works
		builder.setResourceElement(TEST_RESOURCE_ELEMENT);
		builder.setCreateDate(TEST_CREATE_DATE);
		builder.setIsmDESVersion(getIsmDESVersion());
		builder.getSecurityAttributes().setClassification("U");
		builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
		builder.commit();
	}

	public void testLoad30Commit31() throws InvalidDDMSException {
		Resource.Builder builder = new Resource.Builder(new Resource(getValidElement("3.0")));

		// Direct mapping works
		DDMSVersion.setCurrentVersion("3.0");
		builder.commit();

		// Transform up to 3.1 fails on 3.0-specific fields
		DDMSVersion.setCurrentVersion("3.1");
		try {
			builder.commit();
			fail("Builder allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}

		builder.setIsmDESVersion(new Integer(5));
		builder.commit();
	}

	public void testBuilderEmptiness() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Resource.Builder builder = new Resource.Builder();
			assertTrue(builder.isEmpty());
			builder.getIdentifiers().add(new Identifier.Builder());
			assertTrue(builder.isEmpty());
			builder.getTitles().add(new Title.Builder());
			assertTrue(builder.isEmpty());
			builder.getSubtitles().add(new Subtitle.Builder());
			assertTrue(builder.isEmpty());
			builder.getLanguages().add(new Language.Builder());
			assertTrue(builder.isEmpty());
			builder.getSources().add(new Source.Builder());
			assertTrue(builder.isEmpty());
			builder.getTypes().add(new Type.Builder());
			assertTrue(builder.isEmpty());
			builder.getCreators().add(new Creator.Builder());
			assertTrue(builder.isEmpty());
			builder.getContributors().add(new Contributor.Builder());
			assertTrue(builder.isEmpty());
			builder.getPublishers().add(new Publisher.Builder());
			assertTrue(builder.isEmpty());
			builder.getPointOfContacts().add(new PointOfContact.Builder());
			assertTrue(builder.isEmpty());
			assertEquals(4, builder.getProducers().size());
			builder.getVirtualCoverages().add(new VirtualCoverage.Builder());
			assertTrue(builder.isEmpty());
			builder.getTemporalCoverages().add(new TemporalCoverage.Builder());
			assertTrue(builder.isEmpty());
			builder.getGeospatialCoverages().add(new GeospatialCoverage.Builder());
			assertTrue(builder.isEmpty());
			builder.getRelatedResources().add(new RelatedResource.Builder());
			assertTrue(builder.isEmpty());
			builder.getExtensibleElements().add(new ExtensibleElement.Builder());
			assertTrue(builder.isEmpty());
			builder.getExtensibleElements().get(0).setXml("InvalidXml");
			assertFalse(builder.isEmpty());
		}
	}

	public void testSerializableBuilders() throws Exception {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			Resource.Builder builder = new Resource.Builder(component);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(builder);
			oos.close();
			byte[] serialized = out.toByteArray();
			assertTrue(serialized.length > 0);

			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serialized));
			Resource.Builder unserializedBuilder = (Resource.Builder) ois.readObject();
			assertEquals(component, unserializedBuilder.commit());
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Resource.Builder builder = new Resource.Builder();
			assertNotNull(builder.getIdentifiers().get(1));
			assertNotNull(builder.getTitles().get(1));
			assertNotNull(builder.getSubtitles().get(1));
			assertNotNull(builder.getLanguages().get(1));
			assertNotNull(builder.getSources().get(1));
			assertNotNull(builder.getTypes().get(1));
			assertNotNull(builder.getCreators().get(1));
			assertNotNull(builder.getContributors().get(1));
			assertNotNull(builder.getPublishers().get(1));
			assertNotNull(builder.getPointOfContacts().get(1));
			assertNotNull(builder.getVirtualCoverages().get(1));
			assertNotNull(builder.getTemporalCoverages().get(1));
			assertNotNull(builder.getGeospatialCoverages().get(1));
			assertNotNull(builder.getRelatedResources().get(1));
			assertNotNull(builder.getExtensibleElements().get(1));
		}
	}
}
