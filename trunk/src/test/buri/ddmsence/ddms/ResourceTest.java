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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleAttributesTest;
import buri.ddmsence.ddms.extensible.ExtensibleElement;
import buri.ddmsence.ddms.extensible.ExtensibleElementTest;
import buri.ddmsence.ddms.format.Extent;
import buri.ddmsence.ddms.format.Format;
import buri.ddmsence.ddms.format.FormatTest;
import buri.ddmsence.ddms.metacard.MetacardInfoTest;
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
 * <p>
 * Tests related to ddms:resource elements
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class ResourceTest extends AbstractBaseTestCase {
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
		TEST_TOP_LEVEL_COMPONENTS.add(MetacardInfoTest.getFixture());

		TEST_NO_OPTIONAL_COMPONENTS = new ArrayList<IDDMSComponent>();
		TEST_NO_OPTIONAL_COMPONENTS.add(MetacardInfoTest.getFixture());
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
		if (DDMSVersion.getCurrentVersion().isAtLeast("4.0"))
			element.appendChild(MetacardInfoTest.getFixture().getXOMElementCopy());
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
		if (!DDMSVersion.getCurrentVersion().isAtLeast("4.0"))
			return (new Integer(5));
		return (new Integer(7));
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
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Resource getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		Resource component = null;
		try {
			component = new Resource(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to create a DDMS 3.0 object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param topLevelComponents a list of top level components
	 * @param resourceElement value of the resourceElement attribute (required)
	 * @param createDate the create date as an xs:date (YYYY-MM-DD) (required)
	 * @param ismDESVersion the ISM DES Version as an Integer (required)
	 * @param ntkDESVersion the NTK DES Version as an Integer (required, starting in DDMS 4.0)
	 * @return a valid object
	 */
	private Resource getInstance(String message, List<IDDMSComponent> topLevelComponents, Boolean resourceElement,
		String createDate, Integer ismDESVersion, Integer ntkDESVersion) {
		boolean expectFailure = !Util.isEmpty(message);
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
			expectMessage(e, message);
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
			text.append(buildOutput(isHTML, resourcePrefix + ".noticeType", "ABC"));
			text.append(buildOutput(isHTML, resourcePrefix + ".noticeReason", "noticeReason"));
			text.append(buildOutput(isHTML, resourcePrefix + ".noticeDate", "2011-09-15"));
			text.append(buildOutput(isHTML, resourcePrefix + ".unregisteredNoticeType", "unregisteredNoticeType"));
			text.append(buildOutput(isHTML, "metacardInfo.identifier.qualifier", "URI"));
			text.append(buildOutput(isHTML, "metacardInfo.identifier.value", "urn:buri:ddmsence:testIdentifier"));
			text.append(buildOutput(isHTML, "metacardInfo.dates.created", "2003"));
			text.append(buildOutput(isHTML, "metacardInfo.publisher.entityType", "person"));
			text.append(buildOutput(isHTML, "metacardInfo.publisher.name", "Brian"));
			text.append(buildOutput(isHTML, "metacardInfo.publisher.surname", "Uri"));
			text.append(buildOutput(isHTML, "metacardInfo.classification", "U"));
			text.append(buildOutput(isHTML, "metacardInfo.ownerProducer", "USA"));
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
			text.append(buildOutput(isHTML, "resourceManagement.processingInfo",
				"XSLT Transformation to convert DDMS 2.0 to DDMS 3.1."));
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
		if (version.isAtLeast("4.0")) {
			xml.append(" ISM:noticeType=\"ABC\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ");
			xml.append("ISM:unregisteredNoticeType=\"unregisteredNoticeType\"");
		}
		if (version.isAtLeast("3.0"))
			xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\"");
		xml.append(">\n");
		if (version.isAtLeast("4.0")) {
			xml.append("\t<ddms:metacardInfo ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
			xml.append("<ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence:testIdentifier\" />");
			xml.append("<ddms:dates ddms:created=\"2003\" /><ddms:publisher><ddms:person><ddms:name>Brian</ddms:name>");
			xml.append("<ddms:surname>Uri</ddms:surname></ddms:person></ddms:publisher></ddms:metacardInfo>\n");
		}
		xml.append("\t<ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence:testIdentifier\" />\n");
		xml.append("\t<ddms:title ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DDMSence</ddms:title>\n");
		xml.append("\t<ddms:subtitle ISM:classification=\"U\" ISM:ownerProducer=\"USA\">Version 0.1</ddms:subtitle>\n");
		xml.append("\t<ddms:description ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("A transformation service.</ddms:description>\n");
		xml.append("\t<ddms:language ddms:qualifier=\"http://purl.org/dc/elements/1.1/language\" ");
		xml.append("ddms:value=\"en\" />\n");
		xml.append("\t<ddms:dates ddms:created=\"2003\" />\n");
		xml.append("\t<ddms:rights ddms:privacyAct=\"true\" ddms:intellectualProperty=\"true\" ");
		xml.append("ddms:copyright=\"true\" />\n");
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
			xml.append("gml:id=\"IDValue\" srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" ");
			xml.append("srsDimension=\"10\" axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\">\n");
			xml.append("\t\t\t\t<gml:pos>32.1 40.1</gml:pos>\n");
			xml.append("\t\t\t</gml:Point>\n");
			xml.append("\t\t</ddms:boundingGeometry>\n");
		}
		else {
			xml.append("\t\t<ddms:GeospatialExtent>\n");
			xml.append("\t\t\t<ddms:boundingGeometry>\n");
			xml.append("\t\t\t\t<gml:Point xmlns:gml=\"").append(version.getGmlNamespace()).append("\" ");
			xml.append("gml:id=\"IDValue\" srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" ");
			xml.append("srsDimension=\"10\" axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\">\n");
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
			xml.append("<ddms:processingInfo ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ");
			xml.append("ddms:dateProcessed=\"2011-08-19\">");
			xml.append("XSLT Transformation to convert DDMS 2.0 to DDMS 3.1.</ddms:processingInfo>");
			xml.append("</ddms:resourceManagement>\n");
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

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, Resource
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// All fields
			getInstance(SUCCESS, getValidElement(sVersion));

			// No optional fields
			Element element = getResourceWithoutBodyElement();
			if (version.isAtLeast("4.0"))
				element.appendChild(MetacardInfoTest.getFixture().getXOMElementCopy());
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance(SUCCESS, element);

			// More than 1 subjectCoverage
			if (version.isAtLeast("4.0")) {
				element = getResourceWithoutBodyElement();
				element.appendChild(MetacardInfoTest.getFixture().getXOMElementCopy());
				element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
				element.appendChild(TitleTest.getFixture().getXOMElementCopy());
				element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
				getInstance(SUCCESS, element);
			}
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// All fields
			getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());

			// No optional fields
			getInstance(SUCCESS, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
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
				if (version.isAtLeast("4.0")) {
					Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
						.valueOf(getNtkDESVersion()));
				}
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance("resourceElement is required.", element);

				// Empty resourceElement
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, "");
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				if (version.isAtLeast("4.0")) {
					Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
						.valueOf(getNtkDESVersion()));
				}
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance("resourceElement is required.", element);

				// Invalid resourceElement
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, "aardvark");
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				if (version.isAtLeast("4.0")) {
					Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
						.valueOf(getNtkDESVersion()));
				}
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance("resourceElement is required.", element);

				// Missing createDate
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				if (version.isAtLeast("4.0")) {
					Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
						.valueOf(getNtkDESVersion()));
				}
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance("createDate is required.", element);

				// Invalid createDate
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, "2004");
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				if (version.isAtLeast("4.0")) {
					Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
						.valueOf(getNtkDESVersion()));
				}
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance("The createDate must be in the xs:date format", element);

				// Missing desVersion
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				if (version.isAtLeast("4.0")) {
					Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
						.valueOf(getNtkDESVersion()));
				}
				SecurityAttributesTest.getFixture().addTo(element);
				String message = "";
				if ("3.0".equals(version.getVersion()))
					message = "DESVersion is required.";
				else if (version.isAtLeast("3.1"))
					message = "The ISM:DESVersion must be";
				getInstance(message, element);

				// desVersion not an integer
				element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, "one");
				if (version.isAtLeast("4.0")) {
					Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, String
						.valueOf(getNtkDESVersion()));
				}
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance(message, element);
			}
			if (version.isAtLeast("4.0")) {
				// NTK desVersion not an integer
				Element element = getResourceWithoutHeaderElement();
				Util.addAttribute(element, ismPrefix, Resource.RESOURCE_ELEMENT_NAME, ismNamespace, String
					.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, ismPrefix, Resource.DES_VERSION_NAME, ismNamespace, String
					.valueOf(getIsmDESVersion()));
				Util.addAttribute(element, ismPrefix, Resource.CREATE_DATE_NAME, ismNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, ntkPrefix, Resource.DES_VERSION_NAME, ntkNamespace, "one");
				SecurityAttributesTest.getFixture().addTo(element);
				getInstance("The ntk:DESVersion must be", element);
			}

			// At least 1 producer
			Element element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			getInstance("Exactly 1 security element must exist.", element);

			// At least 1 identifier
			element = getResourceWithoutBodyElement();
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance("At least 1 identifier is required.", element);

			// At least 1 title
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance("At least 1 title is required.", element);

			// No more than 1 description
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(DescriptionTest.getFixture().getXOMElementCopy());
			element.appendChild(DescriptionTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance("No more than 1 description element can exist.", element);

			// No more than 1 dates
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(DatesTest.getFixture().getXOMElementCopy());
			element.appendChild(DatesTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance("No more than 1 dates element can exist.", element);

			// No more than 1 rights
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(RightsTest.getFixture().getXOMElementCopy());
			element.appendChild(RightsTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance("No more than 1 rights element can exist.", element);

			// No more than 1 formats
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(FormatTest.getFixture().getXOMElementCopy());
			element.appendChild(FormatTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance("No more than 1 format element can exist.", element);

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
				getInstance("No more than 1 resourceManagement", element);
			}

			// At least 1 subjectCoverage
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			String message = version.isAtLeast("4.0") ? "At least 1 subjectCoverage is required."
				: "Exactly 1 subjectCoverage element must exist.";
			getInstance(message, element);

			// No more than 1 subjectCoverage
			if (!version.isAtLeast("4.0")) {
				element = getResourceWithoutBodyElement();
				element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
				element.appendChild(TitleTest.getFixture().getXOMElementCopy());
				element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
				element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
				getInstance("Exactly 1 subjectCoverage element must exist.", element);
			}

			// At least 1 security
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			getInstance("Exactly 1 security element must exist.", element);

			// No more than 1 security
			element = getResourceWithoutBodyElement();
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			getInstance("Extensible elements cannot be defined", element);

			// No top level components
			element = getResourceWithoutBodyElement();
			getInstance("At least 1 identifier is required.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			if (version.isAtLeast("3.0")) {
				// Missing createDate
				getInstance("createDate is required.", TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, null,
					getIsmDESVersion(), getNtkDESVersion());

				// Invalid createDate
				getInstance("The createDate must be in the xs:date format", TEST_NO_OPTIONAL_COMPONENTS,
					TEST_RESOURCE_ELEMENT, "2001", getIsmDESVersion(), getNtkDESVersion());

				// Nonsensical createDate
				getInstance("The ISM:createDate attribute is not in a valid date format.", TEST_NO_OPTIONAL_COMPONENTS,
					TEST_RESOURCE_ELEMENT, "notAnXmlDate", getIsmDESVersion(), getNtkDESVersion());

				String message = "";
				if ("3.0".equals(version.getVersion()))
					message = "DESVersion is required.";
				else if (version.isAtLeast("3.1"))
					message = "The ISM:DESVersion must be";

				// Missing desVersion
				getInstance(message, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, null,
					getNtkDESVersion());
			}
			if (version.isAtLeast("4.0")) {
				// Missing desVersion
				getInstance("The ntk:DESVersion must be", TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT,
					TEST_CREATE_DATE, getIsmDESVersion(), null);
			}

			// At least 1 producer
			List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(CreatorTest.getFixture());
			getInstance("At least 1 producer", components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// At least 1 identifier
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(IdentifierTest.getFixture());
			getInstance("At least 1 identifier is required.", components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());

			// At least 1 title
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(TitleTest.getFixture());
			getInstance("At least 1 title is required.", components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());

			// At least 1 subjectCoverage
			String message = version.isAtLeast("4.0") ? "At least 1 subjectCoverage is required."
				: "Exactly 1 subjectCoverage element must exist.";
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(SubjectCoverageTest.getFixture());
			getInstance(message, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// At least 1 security
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(SecurityTest.getFixture());
			getInstance("Exactly 1 security element must exist.", components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());

			// No top level components
			getInstance("At least 1 identifier is required.", null, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());

			// Non-top-level component
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new Keyword("test", null));
			getInstance("keyword is not a valid top-level component in a resource.", components, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, getIsmDESVersion(), getNtkDESVersion());
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			// No warnings
			Resource component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());

			// Nested warnings
			List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new Format("test", new Extent("test", ""), "test"));
			component = getInstance(SUCCESS, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());
			assertEquals(1, component.getValidationWarnings().size());

			String resourceName = Resource.getName(version);
			String text = "A qualifier has been set without an accompanying value attribute.";
			String locator = (version.isAtLeast("4.0")) ? "ddms:" + resourceName + "/ddms:format/ddms:extent" : "ddms:"
				+ resourceName + "/ddms:format/ddms:Media/ddms:extent";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));

			// More nested warnings
			Element element = Util.buildDDMSElement(PostalAddress.getName(version), null);
			PostalAddress address = new PostalAddress(element);
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new GeospatialCoverage(null, null, null, address, null, null, null, null));
			component = getInstance(SUCCESS, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());
			assertEquals(1, component.getValidationWarnings().size());

			text = "A completely empty ddms:postalAddress element was found.";
			locator = (version.isAtLeast("4.0")) ? "ddms:" + resourceName
				+ "/ddms:geospatialCoverage/ddms:postalAddress" : "ddms:" + resourceName
				+ "/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:postalAddress";
			assertWarningEquality(text, locator, component.getValidationWarnings().get(0));
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Resource dataComponent = (!version.isAtLeast("3.0") ? getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS, null,
				null, getIsmDESVersion(), getNtkDESVersion()) : getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS,
				TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(), getNtkDESVersion()));
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Resource dataComponent = getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS, false, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, "1999-10-10",
				getIsmDESVersion(), getNtkDESVersion());
			assertFalse(elementComponent.equals(dataComponent));

			// Can only use alternate DESVersions in early DDMS versions
			if (!version.isAtLeast("3.1")) {
				dataComponent = getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
					TEST_CREATE_DATE, new Integer(1), getNtkDESVersion());
				assertFalse(elementComponent.equals(dataComponent));
			}

			dataComponent = getInstance(SUCCESS, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				getIsmDESVersion(), getNtkDESVersion());
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = (!version.isAtLeast("3.0") ? getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS, null, null,
				getIsmDESVersion(), getNtkDESVersion()) : getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS,
				TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(), getNtkDESVersion()));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			Resource component = getInstance(SUCCESS, getValidElement(sVersion));

			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = (!version.isAtLeast("3.0") ? getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS, null, null,
				getIsmDESVersion(), getNtkDESVersion()) : getInstance(SUCCESS, TEST_TOP_LEVEL_COMPONENTS,
				TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(), getNtkDESVersion()));
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testWrongVersionSecurityAttributes() throws InvalidDDMSException {
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

			// IsmDESVersion in parameter AND extensible.
			try {
				List<Attribute> exAttr = new ArrayList<Attribute>();
				exAttr.add(new Attribute("ISM:DESVersion", version.getIsmNamespace(), "2"));
				new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, getIsmDESVersion(), getNtkDESVersion(),
					SecurityAttributesTest.getFixture(), null, new ExtensibleAttributes(exAttr));
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The extensible attribute with the name, ISM:DESVersion");
			}

			// NtkDESVersion in parameter AND extensible.
			if (version.isAtLeast("4.0")) {
				try {
					List<Attribute> exAttr = new ArrayList<Attribute>();
					exAttr.add(new Attribute("ntk:DESVersion", version.getNtkNamespace(), "2"));
					new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, getIsmDESVersion(), getNtkDESVersion(),
						SecurityAttributesTest.getFixture(), null, new ExtensibleAttributes(exAttr));
					fail("Allowed invalid data.");
				}
				catch (InvalidDDMSException e) {
					expectMessage(e, "The extensible attribute with the name, ntk:DESVersion");
				}
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
				expectMessage(e, "The extensible attribute with the name, ISM:classification");
			}
		}
	}

	public void testExtensibleElementElementConstructor() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();

			ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getFixtureElement());
			Element element = getResourceWithoutBodyElement();
			if (version.isAtLeast("4.0"))
				element.appendChild(MetacardInfoTest.getFixture().getXOMElementCopy());
			element.appendChild(IdentifierTest.getFixture().getXOMElementCopy());
			element.appendChild(TitleTest.getFixture().getXOMElementCopy());
			element.appendChild(CreatorTest.getFixture().getXOMElementCopy());
			element.appendChild(SubjectCoverageTest.getFixture().getXOMElementCopy());
			element.appendChild(SecurityTest.getFixture().getXOMElementCopy());
			element.appendChild(component.getXOMElementCopy());
			getInstance(SUCCESS, element);
		}
	}

	public void testExtensibleElementOutput() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();
		ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getFixtureElement());

		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(component);
		Resource resource = getInstance(SUCCESS, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
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
		getInstance(SUCCESS, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
			getNtkDESVersion());
	}

	public void test20TooManyExtensibleElements() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();

		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		getInstance("Only 1 extensible element is allowed in DDMS 2.0.", components, null, null, null, null);
	}

	public void testAfter20TooManyExtensibleElements() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();

		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		components.add(new ExtensibleElement(ExtensibleElementTest.getFixtureElement()));
		getInstance(SUCCESS, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
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
		Resource resource = getInstance(SUCCESS, element);

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
			Resource resource = getInstance(SUCCESS, element);
			assertEquals(4, resource.getRelatedResources().size());
			assertEquals("http://en.wikipedia.org/wiki/Tank1", resource.getRelatedResources().get(0).getValue());
			assertEquals("http://en.wikipedia.org/wiki/Tank2", resource.getRelatedResources().get(1).getValue());
			assertEquals("http://en.wikipedia.org/wiki/Tank3", resource.getRelatedResources().get(2).getValue());
			assertEquals("http://en.wikipedia.org/wiki/Tank4", resource.getRelatedResources().get(3).getValue());
		}
	}

	public void testOrderConstraints() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			createComponents();
			if (!version.isAtLeast("4.0"))
				continue;

			// Valid orders
			List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_TOP_LEVEL_COMPONENTS);
			components.add(SubjectCoverageTest.getFixture(1));
			components.add(GeospatialCoverageTest.getFixture(2));
			components.add(SubjectCoverageTest.getFixture(3));
			getInstance(SUCCESS, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, getIsmDESVersion(),
				getNtkDESVersion());

			// Duplicate orders
			components = new ArrayList<IDDMSComponent>(TEST_TOP_LEVEL_COMPONENTS);
			components.add(SubjectCoverageTest.getFixture(1));
			components.add(GeospatialCoverageTest.getFixture(1));
			components.add(SubjectCoverageTest.getFixture(3));
			getInstance("The ddms:order attributes throughout this resource", components, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, getIsmDESVersion(), getNtkDESVersion());

			// Skipped orders
			components = new ArrayList<IDDMSComponent>(TEST_TOP_LEVEL_COMPONENTS);
			components.add(SubjectCoverageTest.getFixture(1));
			components.add(GeospatialCoverageTest.getFixture(3));
			components.add(SubjectCoverageTest.getFixture(4));
			getInstance("The ddms:order attributes throughout this resource", components, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, getIsmDESVersion(), getNtkDESVersion());
		}

	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Resource component = getInstance(SUCCESS, getValidElement(sVersion));

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
				expectMessage(e, "At least 1 identifier is required.");
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
			expectMessage(e, "NS-S is not a valid enumeration token");
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
			expectMessage(e, "Security attributes cannot be applied");
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
			expectMessage(e, "The Unknown element cannot be used");
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
			expectMessage(e, "resourceElement is required.");
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
			expectMessage(e, "The ISM:DESVersion must be");
		}

		// Adding 3.1-specific fields works
		builder.setIsmDESVersion(new Integer(5));
		builder.commit();
	}

	public void testLoad31Commit40() throws InvalidDDMSException {
		Resource.Builder builder = new Resource.Builder(new Resource(getValidElement("3.1")));

		// Direct mapping works
		DDMSVersion.setCurrentVersion("3.1");
		builder.commit();

		// Transform up to 4.0 fails on 3.1-specific fields
		DDMSVersion.setCurrentVersion("4.0");
		try {
			builder.commit();
			fail("Builder allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The ISM:DESVersion must be");
		}

		// Adding 4.0-specific fields works
		builder.setNtkDESVersion(new Integer(5));
		builder.setIsmDESVersion(new Integer(7));
		builder.getMetacardInfo().getIdentifiers().get(0).setQualifier("qualifier");
		builder.getMetacardInfo().getIdentifiers().get(0).setValue("value");
		builder.getMetacardInfo().getDates().setCreated("2011-09-25");
		builder.getMetacardInfo().getPublishers().get(0).setEntityType("organization");
		builder.getMetacardInfo().getPublishers().get(0).getOrganization().setNames(Util.getXsListAsList("DISA"));
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
			Resource component = getInstance(SUCCESS, getValidElement(sVersion));

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
