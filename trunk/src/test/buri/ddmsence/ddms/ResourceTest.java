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
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleAttributesTest;
import buri.ddmsence.ddms.extensible.ExtensibleElement;
import buri.ddmsence.ddms.extensible.ExtensibleElementTest;
import buri.ddmsence.ddms.format.Format;
import buri.ddmsence.ddms.format.MediaExtent;
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
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.ddms.security.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.BoundingGeometry;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.GeospatialCoverage;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.ddms.summary.Link;
import buri.ddmsence.ddms.summary.Point;
import buri.ddmsence.ddms.summary.Position;
import buri.ddmsence.ddms.summary.PositionTest;
import buri.ddmsence.ddms.summary.PostalAddress;
import buri.ddmsence.ddms.summary.RelatedResource;
import buri.ddmsence.ddms.summary.RelatedResources;
import buri.ddmsence.ddms.summary.SRSAttributes;
import buri.ddmsence.ddms.summary.SRSAttributesTest;
import buri.ddmsence.ddms.summary.SubjectCoverage;
import buri.ddmsence.ddms.summary.TemporalCoverage;
import buri.ddmsence.ddms.summary.VirtualCoverage;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:Resource elements</p>
 * 
 * <p>Assumes that unit testing on individual components is done separately.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class ResourceTest extends AbstractComponentTestCase {

	private Identifier TEST_IDENTIFIER;
	private Title TEST_TITLE;
	private Subtitle TEST_SUBTITLE;
	private Description TEST_DESCRIPTION;
	private Language TEST_LANGUAGE;
	private Dates TEST_DATES;
	private Rights TEST_RIGHTS;
	private Source TEST_SOURCE;
	private Type TEST_TYPE;
	private IProducer TEST_CREATOR;
	private IProducer TEST_PUBLISHER;
	private IProducer TEST_CONTRIBUTOR;
	private IProducer TEST_POC;
	private Format TEST_FORMAT;
	private SubjectCoverage TEST_SUBJECT;
	private VirtualCoverage TEST_VIRTUAL;
	private TemporalCoverage TEST_TEMPORAL;
	private GeospatialCoverage TEST_GEOSPATIAL;
	private RelatedResources TEST_RELATED;
	private Security TEST_SECURITY;
	private SRSAttributes TEST_SRS_ATTRIBUTES;
	private List<IDDMSComponent> TEST_TOP_LEVEL_COMPONENTS;
	private List<IDDMSComponent> TEST_NO_OPTIONAL_COMPONENTS;

	private static final Boolean TEST_RESOURCE_ELEMENT = Boolean.TRUE;
	private static final Integer TEST_DES_VERSION = new Integer(2);
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
		TEST_IDENTIFIER = new Identifier("URI", "urn:buri:ddmsence:testIdentifier");
		TEST_TITLE = new Title("DDMSence", SecurityAttributesTest.getFixture(false));
		TEST_SUBTITLE = new Subtitle("Version 0.1", SecurityAttributesTest.getFixture(false));
		TEST_DESCRIPTION = new Description("A transformation service.", SecurityAttributesTest.getFixture(false));
		TEST_LANGUAGE = new Language("http://purl.org/dc/elements/1.1/language", "en");
		TEST_DATES = new Dates("2003", null, null, null);
		TEST_RIGHTS = new Rights(true, true, true);
		TEST_SOURCE = new Source(null, "http://www.xmethods.com", null, null, null);
		TEST_TYPE = new Type("DCMITYPE", "http://purl.org/dc/dcmitype/Text");
		TEST_CREATOR = new Organization("creator", getAsList("DISA"), null, null, null);
		TEST_PUBLISHER = new Person("publisher", "Uri", getAsList("Brian"), null, null, null, null, null);
		TEST_CONTRIBUTOR = new Service(Organization.CONTRIBUTOR_NAME,
			getAsList("https://metadata.dod.mil/ebxmlquery/soap"), null, null, null);
		TEST_POC = (DDMSVersion.isCurrentVersion("2.0") ?
			new Person("pointOfContact", "Uri", getAsList("Brian"), null, null, null, null, null)
			: new Unknown("pointOfContact", getAsList("Unknown Entity"), null, null, null));
		TEST_FORMAT = new Format("text/xml", null, null);

		List<Keyword> keywords = new ArrayList<Keyword>();
		keywords.add(new Keyword("DDMSence"));
		TEST_SUBJECT = new SubjectCoverage(keywords, null, null);
		TEST_VIRTUAL = new VirtualCoverage("123.456.789.0", "IP", null);
		TEST_TEMPORAL = new TemporalCoverage(null, "1979-09-15", "Not Applicable", null);

		TEST_SRS_ATTRIBUTES = SRSAttributesTest.getFixture();
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(new Position(PositionTest.TEST_COORDS, null), TEST_SRS_ATTRIBUTES, TEST_ID));
		TEST_GEOSPATIAL = new GeospatialCoverage(null, null, new BoundingGeometry(null, points), null, null, null);

		List<Link> links = new ArrayList<Link>();
		links.add(new Link("http://en.wikipedia.org/wiki/Tank", null, null, null));
		List<RelatedResource> resources = new ArrayList<RelatedResource>();
		resources.add(new RelatedResource(links, "http://purl.org/dc/terms/URI", "http://en.wikipedia.org/wiki/Tank"));
		TEST_RELATED = new RelatedResources(resources, "http://purl.org/dc/terms/references", "outbound", null);
		TEST_SECURITY = new Security(SecurityAttributesTest.getFixture(false));

		TEST_TOP_LEVEL_COMPONENTS = new ArrayList<IDDMSComponent>();
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_SECURITY);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_RELATED);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_GEOSPATIAL);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_TEMPORAL);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_VIRTUAL);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_SUBJECT);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_FORMAT);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_POC);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_CONTRIBUTOR);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_PUBLISHER);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_CREATOR);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_TYPE);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_SOURCE);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_RIGHTS);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_DATES);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_LANGUAGE);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_DESCRIPTION);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_SUBTITLE);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_TITLE);
		TEST_TOP_LEVEL_COMPONENTS.add(TEST_IDENTIFIER);

		TEST_NO_OPTIONAL_COMPONENTS = new ArrayList<IDDMSComponent>();
		TEST_NO_OPTIONAL_COMPONENTS.add(TEST_IDENTIFIER);
		TEST_NO_OPTIONAL_COMPONENTS.add(TEST_TITLE);
		TEST_NO_OPTIONAL_COMPONENTS.add(TEST_CREATOR);
		TEST_NO_OPTIONAL_COMPONENTS.add(TEST_SUBJECT);
		TEST_NO_OPTIONAL_COMPONENTS.add(TEST_SECURITY);
	}

	/**
	 * Helper method to wrap a string in a list, to quickly make lists consisting of only 1 item.
	 * 
	 * @param string the string
	 * @return the list
	 */
	private List<String> getAsList(String string) {
		List<String> list = new ArrayList<String>();
		list.add(string);
		return (list);
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
		} catch (InvalidDDMSException e) {
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
	 * @param desVersion the DES Version as an Integer (required)
	 * @return a valid object
	 */
	private Resource testConstructor(boolean expectFailure, List<IDDMSComponent> topLevelComponents,
		Boolean resourceElement, String createDate, Integer desVersion) {
		Resource component = null;
		try {
			SecurityAttributes attr = (DDMSVersion.isCurrentVersion("2.0") ? null 
				: SecurityAttributesTest.getFixture(false));
			component = new Resource(topLevelComponents, resourceElement, createDate, desVersion, attr);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML output for this unit test
	 */
	private String getExpectedHTMLOutput() {
		StringBuffer html = new StringBuffer();
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			html.append("<meta name=\"security.resourceElement\" content=\"true\" />\n");
			html.append("<meta name=\"security.createDate\" content=\"2010-01-21\" />\n");
			html.append("<meta name=\"security.DESVersion\" content=\"2\" />\n");
			html.append("<meta name=\"security.classification\" content=\"U\" />\n");
			html.append("<meta name=\"security.ownerProducer\" content=\"USA\" />\n");
		}
		else {
			// Adding the optional ICISM tag allows the namespace declaration to definitely be in the Resource element.
			html.append("<meta name=\"security.DESVersion\" content=\"2\" />\n");
		}
		html.append("<meta name=\"identifier.qualifier\" content=\"URI\" />\n");
		html.append("<meta name=\"identifier.value\" content=\"urn:buri:ddmsence:testIdentifier\" />\n");
		html.append("<meta name=\"title\" content=\"DDMSence\" />\n");
		html.append("<meta name=\"title.classification\" content=\"U\" />\n");
		html.append("<meta name=\"title.ownerProducer\" content=\"USA\" />\n");
		html.append("<meta name=\"subtitle\" content=\"Version 0.1\" />\n");
		html.append("<meta name=\"subtitle.classification\" content=\"U\" />\n");
		html.append("<meta name=\"subtitle.ownerProducer\" content=\"USA\" />\n");
		html.append("<meta name=\"description\" content=\"A transformation service.\" />\n");
		html.append("<meta name=\"description.classification\" content=\"U\" />\n");
		html.append("<meta name=\"description.ownerProducer\" content=\"USA\" />\n");
		html.append("<meta name=\"language.qualifier\" content=\"http://purl.org/dc/elements/1.1/language\" />\n");
		html.append("<meta name=\"language\" content=\"en\" />\n");
		html.append("<meta name=\"date.created\" content=\"2003\" />\n");
		html.append("<meta name=\"rights.privacy\" content=\"true\" />\n");
		html.append("<meta name=\"rights.intellectualproperty\" content=\"true\" />\n");
		html.append("<meta name=\"rights.copy\" content=\"true\" />\n");
		html.append("<meta name=\"source.value\" content=\"http://www.xmethods.com\" />\n");
		html.append("<meta name=\"type.qualifier\" content=\"DCMITYPE\" />\n");
		html.append("<meta name=\"type.value\" content=\"http://purl.org/dc/dcmitype/Text\" />\n");
		html.append("<meta name=\"creator.entityType\" content=\"Organization\" />\n");
		html.append("<meta name=\"creator.name\" content=\"DISA\" />\n");
		html.append("<meta name=\"publisher.entityType\" content=\"Person\" />\n");
		html.append("<meta name=\"publisher.name\" content=\"Brian\" />\n");
		html.append("<meta name=\"publisher.surname\" content=\"Uri\" />\n");
		html.append("<meta name=\"contributor.entityType\" content=\"Service\" />\n");
		html.append("<meta name=\"contributor.name\" content=\"https://metadata.dod.mil/ebxmlquery/soap\" />\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			html.append("<meta name=\"pointOfContact.entityType\" content=\"Unknown\" />\n");
			html.append("<meta name=\"pointOfContact.name\" content=\"Unknown Entity\" />\n");
		}
		else {
			html.append("<meta name=\"pointOfContact.entityType\" content=\"Person\" />\n");
			html.append("<meta name=\"pointOfContact.name\" content=\"Brian\" />\n");
			html.append("<meta name=\"pointOfContact.surname\" content=\"Uri\" />\n");
		}
		html.append("<meta name=\"format.media\" content=\"text/xml\" />\n");
		html.append("<meta name=\"subject.keyword\" content=\"DDMSence\" />\n");
		html.append("<meta name=\"virtual.address\" content=\"123.456.789.0\" />\n");
		html.append("<meta name=\"virtual.networkProtocol\" content=\"IP\" />\n");
		html.append("<meta name=\"temporal.TimePeriod\" content=\"Unknown\" />\n");
		html.append("<meta name=\"temporal.DateStart\" content=\"1979-09-15\" />\n");
		html.append("<meta name=\"temporal.DateEnd\" content=\"Not Applicable\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.id\" content=\"IDValue\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.type\" content=\"Point\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsName\" ")
			.append("content=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.srsDimension\" content=\"10\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.axisLabels\" content=\"A B C\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.uomLabels\" content=\"Meter Meter Meter\" />\n");
		html.append("<meta name=\"geospatial.boundingGeometry.position\" content=\"32.1 40.1\" />\n");
		html.append("<meta name=\"RelatedResources.relationship\" ")
			.append("content=\"http://purl.org/dc/terms/references\" />\n");
		html.append("<meta name=\"RelatedResources.direction\" content=\"outbound\" />\n");
		html.append("<meta name=\"RelatedResource.qualifier\" content=\"http://purl.org/dc/terms/URI\" />\n");
		html.append("<meta name=\"RelatedResource.value\" content=\"http://en.wikipedia.org/wiki/Tank\" />\n");
		html.append("<meta name=\"Link.type\" content=\"locator\" />\n");
		html.append("<meta name=\"Link.href\" content=\"http://en.wikipedia.org/wiki/Tank\" />\n");
		if (!DDMSVersion.isCurrentVersion("2.0"))
			html.append("<meta name=\"security.excludeFromRollup\" content=\"true\" />\n");
		html.append("<meta name=\"security.classification\" content=\"U\" />\n");
		html.append("<meta name=\"security.ownerProducer\" content=\"USA\" />\n");
		html.append("<meta name=\"extensible.layer\" content=\"false\" />\n");
		html.append("<meta name=\"ddms.generator\" content=\"DDMSence ").append(PropertyReader.getProperty("version")).append("\" />\n");
		html.append("<meta name=\"ddms.version\" content=\"").append(DDMSVersion.getCurrentVersion().getVersion())
			.append("\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			text.append("ResourceElement: true\n");
			text.append("Create Date: 2010-01-21\n");
			text.append("DES Version: 2\n");
			text.append("Classification: U\n");
			text.append("ownerProducer: USA\n");
		}
		else {
			// Adding the optional ICISM tag allows the namespace declaration to definitely be in the Resource element.
			text.append("DES Version: 2\n");
		}
		text.append("Identifier qualifier: URI\n");
		text.append("Identifier value: urn:buri:ddmsence:testIdentifier\n");
		text.append("Title: DDMSence\n");
		text.append("Title Classification: U\n");
		text.append("Title ownerProducer: USA\n");
		text.append("Subtitle: Version 0.1\n");
		text.append("Subtitle Classification: U\n");
		text.append("Subtitle ownerProducer: USA\n");
		text.append("Description: A transformation service.\n");
		text.append("Description Classification: U\n");
		text.append("Description ownerProducer: USA\n");
		text.append("Language qualifier: http://purl.org/dc/elements/1.1/language\n");
		text.append("Language: en\n");
		text.append("Date Created: 2003\n");
		text.append("Privacy Act: true\n");
		text.append("Intellectual Property Rights: true\n");
		text.append("Copyright: true\n");
		text.append("Source Value: http://www.xmethods.com\n");
		text.append("Type qualifier: DCMITYPE\n");
		text.append("Type: http://purl.org/dc/dcmitype/Text\n");
		text.append("Creator EntityType: Organization\n");
		text.append("Name: DISA\n");
		text.append("Publisher EntityType: Person\n");
		text.append("Name: Brian\n");
		text.append("Surname: Uri\n");
		text.append("Contributor EntityType: Web Service\n");
		text.append("Name: https://metadata.dod.mil/ebxmlquery/soap\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			text.append("PointOfContact EntityType: Unknown\n");
			text.append("Name: Unknown Entity\n");
		}
		else {
			text.append("PointOfContact EntityType: Person\n");
			text.append("Name: Brian\n");
			text.append("Surname: Uri\n");			
		}
		text.append("Media format: text/xml\n");
		text.append("Keyword: DDMSence\n");
		text.append("Virtual address: 123.456.789.0\n");
		text.append("Network Protocol: IP\n");
		text.append("Time Period: Unknown\n");
		text.append("Date Start: 1979-09-15\n");
		text.append("Date End: Not Applicable\n");
		text.append("Geospatial Geometry ID: IDValue\n");
		text.append("Geospatial Geometry Type: Point\n");
		text.append("Geospatial Geometry SRS Name: http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\n");
		text.append("Geospatial Geometry SRS Dimension: 10\n");
		text.append("Geospatial Geometry Axis Labels: A B C\n");
		text.append("Geospatial Geometry Unit of Measure Labels: Meter Meter Meter\n");
		text.append("Geospatial Geometry Position: 32.1 40.1\n");
		text.append("Related Resources relationship: http://purl.org/dc/terms/references\n");
		text.append("Related Resources direction: outbound\n");
		text.append("Related Resource qualifier: http://purl.org/dc/terms/URI\n");
		text.append("Related Resource value: http://en.wikipedia.org/wiki/Tank\n");
		text.append("Link type: locator\n");
		text.append("Link href: http://en.wikipedia.org/wiki/Tank\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			text.append("Exclude From Rollup: true\n");
		}
		text.append("Classification: U\n");
		text.append("ownerProducer: USA\n");
		text.append("Extensible Layer: false\n");
		text.append("DDMS Generator: DDMSence ").append(PropertyReader.getProperty("version")).append("\n");
		text.append("DDMS Version: ").append(DDMSVersion.getCurrentVersion().getVersion()).append("\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:Resource xmlns:ddms=\"").append(DDMSVersion.getCurrentVersion().getNamespace())
			.append("\" xmlns:ICISM=\"");
		xml.append(DDMSVersion.getCurrentVersion().getIcismNamespace()).append("\"");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			xml.append(" ICISM:resourceElement=\"true\" ICISM:DESVersion=\"2\" ICISM:createDate=\"2010-01-21\" ")
				.append("ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\"");
		}
		else {
			// Adding the optional ICISM tag allows the namespace declaration to definitely be in the Resource element.
			xml.append(" ICISM:DESVersion=\"2\"");
		}
		xml.append(">\n");
		xml.append("\t<ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence:testIdentifier\" />\n");
		xml.append("\t<ddms:title ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">DDMSence</ddms:title>\n");
		xml.append("\t<ddms:subtitle ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">Version 0.1</ddms:subtitle>\n");
		xml.append("\t<ddms:description ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">A transformation service.</ddms:description>\n");
		xml.append("\t<ddms:language ddms:qualifier=\"http://purl.org/dc/elements/1.1/language\" ddms:value=\"en\" />\n");
		xml.append("\t<ddms:dates ddms:created=\"2003\" />\n");
		xml.append("\t<ddms:rights ddms:privacyAct=\"true\" ddms:intellectualProperty=\"true\" ddms:copyright=\"true\" />\n");
		xml.append("\t<ddms:source ddms:value=\"http://www.xmethods.com\" />\n");
		xml.append("\t<ddms:type ddms:qualifier=\"DCMITYPE\" ddms:value=\"http://purl.org/dc/dcmitype/Text\" />\n");
		xml.append("\t<ddms:creator>\n");
		xml.append("\t\t<ddms:Organization>\n");
		xml.append("\t\t\t\t<ddms:name>DISA</ddms:name>\n");
		xml.append("\t\t</ddms:Organization>\t\n");
		xml.append("\t</ddms:creator>\n");
		xml.append("\t<ddms:publisher>\n");
		xml.append("\t\t<ddms:Person>\n");
		xml.append("\t\t\t<ddms:name>Brian</ddms:name>\n");
		xml.append("\t\t\t<ddms:surname>Uri</ddms:surname>\n");
		xml.append("\t\t</ddms:Person>\t\n");
		xml.append("\t</ddms:publisher>\n");
		xml.append("\t<ddms:contributor>\n");
		xml.append("\t\t<ddms:Service>\n");
		xml.append("\t\t\t<ddms:name>https://metadata.dod.mil/ebxmlquery/soap</ddms:name>\n");
		xml.append("\t\t</ddms:Service>\t\n");
		xml.append("\t</ddms:contributor>\n");
		xml.append("\t<ddms:pointOfContact>\n");
		if (!DDMSVersion.isCurrentVersion("2.0")) {
			xml.append("\t\t<ddms:Unknown>\n");
			xml.append("\t\t\t<ddms:name>Unknown Entity</ddms:name>\n");
			xml.append("\t\t</ddms:Unknown>\t\n");
		}
		else {
			xml.append("\t\t<ddms:Person>\n");
			xml.append("\t\t\t<ddms:name>Brian</ddms:name>\n");
			xml.append("\t\t\t<ddms:surname>Uri</ddms:surname>\n");
			xml.append("\t\t</ddms:Person>\n");
		}
		xml.append("\t</ddms:pointOfContact>\n");
		xml.append("\t<ddms:format>\n");
		xml.append("\t\t<ddms:Media>\n");
		xml.append("\t\t\t<ddms:mimeType>text/xml</ddms:mimeType>\n");
		xml.append("\t\t</ddms:Media>\n");
		xml.append("\t</ddms:format>\n");
		xml.append("\t<ddms:subjectCoverage>\n");
		xml.append("\t\t<ddms:Subject>\n");
		xml.append("\t\t\t<ddms:keyword ddms:value=\"DDMSence\" />\n");
		xml.append("\t\t</ddms:Subject>\n");
		xml.append("\t</ddms:subjectCoverage>\n");
		xml.append("\t<ddms:virtualCoverage ddms:address=\"123.456.789.0\" ddms:protocol=\"IP\" />\n");
		xml.append("\t<ddms:temporalCoverage>\n");
		xml.append("\t\t<ddms:TimePeriod>\n");
		xml.append("\t\t\t<ddms:start>1979-09-15</ddms:start>\n");
		xml.append("\t\t\t<ddms:end>Not Applicable</ddms:end>\n");
		xml.append("\t\t</ddms:TimePeriod>\n");
		xml.append("\t</ddms:temporalCoverage>\n");
		xml.append("\t<ddms:geospatialCoverage>\n");
		xml.append("\t\t<ddms:GeospatialExtent>\n");
		xml.append("\t\t\t<ddms:boundingGeometry>\n");
		xml.append("\t\t\t\t<gml:Point xmlns:gml=\"").append(DDMSVersion.getCurrentVersion().getGmlNamespace())
			.append("\" ");
		xml.append("srsName=\"http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D\" srsDimension=\"10\" ")
			.append("axisLabels=\"A B C\" uomLabels=\"Meter Meter Meter\" gml:id=\"IDValue\">\n");
		xml.append("\t\t\t\t\t<gml:pos>32.1 40.1</gml:pos>\n");
		xml.append("\t\t\t\t</gml:Point>\n");
		xml.append("\t\t\t</ddms:boundingGeometry>\n");
		xml.append("\t\t</ddms:GeospatialExtent>\n");
		xml.append("\t</ddms:geospatialCoverage>\n");
		xml.append("\t<ddms:relatedResources ddms:relationship=\"http://purl.org/dc/terms/references\" ")
			.append("ddms:direction=\"outbound\">\n");
		xml.append("\t\t<ddms:RelatedResource ddms:qualifier=\"http://purl.org/dc/terms/URI\" ")
			.append("ddms:value=\"http://en.wikipedia.org/wiki/Tank\">\n");
		xml.append("\t\t\t<ddms:link xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"locator\" ")
			.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" />\n");
		xml.append("\t\t</ddms:RelatedResource>\n");
		xml.append("\t</ddms:relatedResources>\n");
		xml.append("\t<ddms:security ");
		if (!DDMSVersion.isCurrentVersion("2.0"))
			xml.append("ICISM:excludeFromRollup=\"true\" ");
		xml.append("ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\" />\n");
		xml.append("</ddms:Resource>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Resource.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Resource.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// No optional fields
			Element element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			// All fields
			testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				TEST_DES_VERSION);

			// No optional fields
			testConstructor(WILL_SUCCEED, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				TEST_DES_VERSION);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			if (!DDMSVersion.isCurrentVersion("2.0")) {
				// Missing resourceElement
				Element element = Util.buildDDMSElement(Resource.NAME, null);
				Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
					String.valueOf(TEST_DES_VERSION));
				SecurityAttributesTest.getFixture(false).addTo(element);
				element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
				element.appendChild(TEST_TITLE.getXOMElementCopy());
				element.appendChild(TEST_CREATOR.getXOMElementCopy());
				element.appendChild(TEST_SUBJECT.getXOMElementCopy());
				element.appendChild(TEST_SECURITY.getXOMElementCopy());
				testConstructor(WILL_FAIL, element);

				// Empty resourceElement
				element = Util.buildDDMSElement(Resource.NAME, null);
				Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, "");
				Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
					String.valueOf(TEST_DES_VERSION));
				SecurityAttributesTest.getFixture(false).addTo(element);
				element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
				element.appendChild(TEST_TITLE.getXOMElementCopy());
				element.appendChild(TEST_CREATOR.getXOMElementCopy());
				element.appendChild(TEST_SUBJECT.getXOMElementCopy());
				element.appendChild(TEST_SECURITY.getXOMElementCopy());
				testConstructor(WILL_FAIL, element);

				// Invalid resourceElement
				element = Util.buildDDMSElement(Resource.NAME, null);
				Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, "aardvark");
				Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
					String.valueOf(TEST_DES_VERSION));
				SecurityAttributesTest.getFixture(false).addTo(element);
				element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
				element.appendChild(TEST_TITLE.getXOMElementCopy());
				element.appendChild(TEST_CREATOR.getXOMElementCopy());
				element.appendChild(TEST_SUBJECT.getXOMElementCopy());
				element.appendChild(TEST_SECURITY.getXOMElementCopy());
				testConstructor(WILL_FAIL, element);

				// Missing createDate
				element = Util.buildDDMSElement(Resource.NAME, null);
				Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, DDMSVersion
					.getCurrentVersion().getIcismNamespace(), String.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, 
					icNamespace, String.valueOf(TEST_DES_VERSION));
				SecurityAttributesTest.getFixture(false).addTo(element);
				element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
				element.appendChild(TEST_TITLE.getXOMElementCopy());
				element.appendChild(TEST_CREATOR.getXOMElementCopy());
				element.appendChild(TEST_SUBJECT.getXOMElementCopy());
				element.appendChild(TEST_SECURITY.getXOMElementCopy());
				testConstructor(WILL_FAIL, element);

				// Invalid createDate
				element = Util.buildDDMSElement(Resource.NAME, null);
				Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
					String.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, 
					icNamespace, "2004");
				Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, 
					icNamespace, String.valueOf(TEST_DES_VERSION));
				SecurityAttributesTest.getFixture(false).addTo(element);
				element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
				element.appendChild(TEST_TITLE.getXOMElementCopy());
				element.appendChild(TEST_CREATOR.getXOMElementCopy());
				element.appendChild(TEST_SUBJECT.getXOMElementCopy());
				element.appendChild(TEST_SECURITY.getXOMElementCopy());
				testConstructor(WILL_FAIL, element);

				// Missing desVersion
				element = Util.buildDDMSElement(Resource.NAME, null);
				Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
					String.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, 
					icNamespace, TEST_CREATE_DATE);
				SecurityAttributesTest.getFixture(false).addTo(element);
				element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
				element.appendChild(TEST_TITLE.getXOMElementCopy());
				element.appendChild(TEST_CREATOR.getXOMElementCopy());
				element.appendChild(TEST_SUBJECT.getXOMElementCopy());
				element.appendChild(TEST_SECURITY.getXOMElementCopy());
				testConstructor(WILL_FAIL, element);

				// desVersion not an integer
				element = Util.buildDDMSElement(Resource.NAME, null);
				Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
					String.valueOf(TEST_RESOURCE_ELEMENT));
				Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
				Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, "one");
				SecurityAttributesTest.getFixture(false).addTo(element);
				element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
				element.appendChild(TEST_TITLE.getXOMElementCopy());
				element.appendChild(TEST_CREATOR.getXOMElementCopy());
				element.appendChild(TEST_SUBJECT.getXOMElementCopy());
				element.appendChild(TEST_SECURITY.getXOMElementCopy());
				testConstructor(WILL_FAIL, element);
			}
			
			// At least 1 producer
			Element element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// At least 1 identifier
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// At least 1 title
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 description
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_DESCRIPTION.getXOMElementCopy());
			element.appendChild(TEST_DESCRIPTION.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 dates
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_DATES.getXOMElementCopy());
			element.appendChild(TEST_DATES.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 rights
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_RIGHTS.getXOMElementCopy());
			element.appendChild(TEST_RIGHTS.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 formats
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_FORMAT.getXOMElementCopy());
			element.appendChild(TEST_FORMAT.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// At least 1 subjectCoverage
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 subjectCoverage
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// At least 1 security
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No more than 1 security
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// No top level components
			element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace, 
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace, 
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			if (!DDMSVersion.isCurrentVersion("2.0")) {
				// Missing createDate
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, null, TEST_DES_VERSION);

				// Invalid createDate
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, "2001", TEST_DES_VERSION);

				// Nonsensical createDate
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, "notAnXmlDate", TEST_DES_VERSION);

				// Missing desVersion
				testConstructor(WILL_FAIL, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, null);
			}
			// At least 1 producer
			List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(TEST_CREATOR);
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);

			// At least 1 identifier
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(TEST_IDENTIFIER);
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);

			// At least 1 title
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(TEST_TITLE);
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);

			// At least 1 subjectCoverage
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(TEST_SUBJECT);
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);

			// At least 1 security
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.remove(TEST_SECURITY);
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);

			// No top level components
			testConstructor(WILL_FAIL, null, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);

			// Non-top-level component
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new Keyword("test"));
			testConstructor(WILL_FAIL, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();

			// base warnings (1 for 2.0, 0 for 3.0)
			int warnings = DDMSVersion.isCurrentVersion("2.0") ? 1 : 0;
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(warnings, component.getValidationWarnings().size());

			// Nested warnings
			List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new Format("test", new MediaExtent("test", ""), "test"));
			component = testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				TEST_DES_VERSION);
			warnings = DDMSVersion.isCurrentVersion("2.0") ? 2 : 1;
			assertEquals(warnings, component.getValidationWarnings().size());

			if (DDMSVersion.isCurrentVersion("2.0")) {
				assertEquals("Security rollup validation is being skipped, because no classification exists "
					+ "on the ddms:Resource itself.", component.getValidationWarnings().get(0).getText());
				assertEquals("A qualifier has been set without an accompanying value attribute.", 
					component.getValidationWarnings().get(1).getText());
				assertEquals("/ddms:Resource", component.getValidationWarnings().get(0).getLocator());
				assertEquals("/ddms:Resource/ddms:format/ddms:Media/ddms:extent", 
					component.getValidationWarnings().get(1).getLocator());
			} else {
				assertEquals("A qualifier has been set without an accompanying value attribute.", 
					component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:Resource/ddms:format/ddms:Media/ddms:extent", 
					component.getValidationWarnings().get(0).getLocator());
			}
			
			// More nested warnings
			Element element = Util.buildDDMSElement(PostalAddress.NAME, null);
			PostalAddress address = new PostalAddress(element);
			components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
			components.add(new GeospatialCoverage(null, null, null, address, null, null));
			component = testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
				TEST_DES_VERSION);
			warnings = DDMSVersion.isCurrentVersion("2.0") ? 2 : 1;
			assertEquals(warnings, component.getValidationWarnings().size());
			if (DDMSVersion.isCurrentVersion("2.0")) {
				assertEquals("Security rollup validation is being skipped, because no classification exists "
					+ "on the ddms:Resource itself.", component.getValidationWarnings().get(0).getText());
				assertEquals("A completely empty ddms:postalAddress element was found.", 
					component.getValidationWarnings().get(1).getText());
				assertEquals("/ddms:Resource", component.getValidationWarnings().get(0).getLocator());
				assertEquals("/ddms:Resource/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:postalAddress",
					component.getValidationWarnings().get(1).getLocator());
			} else {
				assertEquals("A completely empty ddms:postalAddress element was found.", 
					component.getValidationWarnings().get(0).getText());
				assertEquals("/ddms:Resource/ddms:geospatialCoverage/ddms:GeospatialExtent/ddms:postalAddress",
					component.getValidationWarnings().get(0).getLocator());
			}
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			Resource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Resource dataComponent = ("2.0".equals(version) ? testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS,
				null, null, TEST_DES_VERSION) : testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS,
				TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION));
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();

			Resource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Resource dataComponent = testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, false, TEST_CREATE_DATE,
				TEST_DES_VERSION);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				"1999-10-10", TEST_DES_VERSION);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, new Integer(1));
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_NO_OPTIONAL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, TEST_DES_VERSION);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			Resource elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = ("2.0".equals(version) ? testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, null, null,
				TEST_DES_VERSION) : testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, TEST_DES_VERSION));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = ("2.0".equals(version) ? testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, null, null,
				TEST_DES_VERSION) : testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, TEST_DES_VERSION));
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = ("2.0".equals(version) ? testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, null, null,
				TEST_DES_VERSION) : testConstructor(WILL_SUCCEED, TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT,
				TEST_CREATE_DATE, TEST_DES_VERSION));
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testRollupTooRestrictive() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			List<String> ownerProducers = new ArrayList<String>();
			ownerProducers.add("USA");
			Organization org = new Organization("creator", getAsList("DISA"), null, null, new SecurityAttributes("TS",
				ownerProducers, null));

			Element element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace,
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace,
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(org.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}
	
	public void testSkipRollupIfNotAvailable() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		
		List<String> ownerProducers = new ArrayList<String>();
		ownerProducers.add("USA");
		Organization org = new Organization("creator", getAsList("DISA"), null, null, new SecurityAttributes("TS",
			ownerProducers, null));
		
		Element element = Util.buildDDMSElement(Resource.NAME, null);
		element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
		element.appendChild(TEST_TITLE.getXOMElementCopy());
		element.appendChild(org.getXOMElementCopy());
		element.appendChild(TEST_SUBJECT.getXOMElementCopy());
		element.appendChild(TEST_SECURITY.getXOMElementCopy());
		testConstructor(WILL_SUCCEED, element);
	}

	public void testRollupWrongSystem() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			List<String> ownerProducers = new ArrayList<String>();
			ownerProducers.add("USA");
			Organization org = new Organization("creator", getAsList("DISA"), null, null, 
				new SecurityAttributes("CTSA", ownerProducers, null));

			Element element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace,
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace,
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(org.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testRollupManualReviewWarning() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			
			List<String> ownerProducers = new ArrayList<String>();
			ownerProducers.add("USA");
			Organization org = new Organization("creator", getAsList("DISA"), null, null, 
				new SecurityAttributes("CTS-B", ownerProducers, null));

			Element element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace,
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace,
				String.valueOf(TEST_DES_VERSION));

			SecurityAttributes parentAttr = new SecurityAttributes("CTS-BALK", ownerProducers, null);
			parentAttr.addTo(element);

			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(new Title("DDMSence", parentAttr).getXOMElementCopy());
			element.appendChild(org.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(new Security(parentAttr).getXOMElementCopy());
			Resource resource = testConstructor(WILL_SUCCEED, element);
			assertEquals(1, resource.getValidationWarnings().size());
			assertEquals("A security classification from the set [R, CTS-B, or CTS-BALK] is being used. "
				+ "Please review your ddms:Resource and confirm that security rollup is being handled correctly.",
				resource.getValidationWarnings().get(0).getText());
			assertEquals("/ddms:Resource", resource.getValidationWarnings().get(0).getLocator());

		}
	}
	
	public void test20Usage() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		// Security attributes do not exist in 2.0
		new Resource(TEST_TOP_LEVEL_COMPONENTS);
		
		// But attributes can still be used.
		new Resource(TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION, 
			SecurityAttributesTest.getFixture(false));
	}
	
	public void testExtensibleSuccess() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			// Extensible attribute added
			ExtensibleAttributes attr = ExtensibleAttributesTest.getFixture();
			if ("2.0".equals(version))
				new Resource(TEST_TOP_LEVEL_COMPONENTS, attr);
			else
				new Resource(TEST_TOP_LEVEL_COMPONENTS, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION,
					SecurityAttributesTest.getFixture(false), attr);
		}		
	}
	
	public void test20ExtensibleElementSize() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		String icPrefix = PropertyReader.getProperty("icism.prefix");
		
		// ICISM:DESVersion in element
		Element element = Util.buildDDMSElement(Resource.NAME, null);
		Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, 
			DDMSVersion.getCurrentVersion().getIcismNamespace(), String.valueOf(TEST_DES_VERSION));
		element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
		element.appendChild(TEST_TITLE.getXOMElementCopy());
		element.appendChild(TEST_CREATOR.getXOMElementCopy());
		element.appendChild(TEST_SUBJECT.getXOMElementCopy());
		element.appendChild(TEST_SECURITY.getXOMElementCopy());
		Resource component = new Resource(element);
		assertEquals(TEST_DES_VERSION, component.getDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(0, component.getExtensibleAttributes().getAttributes().size());
		
		// ICISM:classification in element
		element = Util.buildDDMSElement(Resource.NAME, null);
		Util.addAttribute(element, icPrefix, SecurityAttributes.CLASSIFICATION_NAME, 
			DDMSVersion.getCurrentVersion().getIcismNamespace(), "U");
		element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
		element.appendChild(TEST_TITLE.getXOMElementCopy());
		element.appendChild(TEST_CREATOR.getXOMElementCopy());
		element.appendChild(TEST_SUBJECT.getXOMElementCopy());
		element.appendChild(TEST_SECURITY.getXOMElementCopy());
		component = new Resource(element);
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(0, component.getExtensibleAttributes().getAttributes().size());
		
		// ddmsence:confidence in element
		element = Util.buildDDMSElement(Resource.NAME, null);
		Util.addAttribute(element, "ddmsence", "confidence", "http://ddmsence.urizone.net/", "95");
		element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
		element.appendChild(TEST_TITLE.getXOMElementCopy());
		element.appendChild(TEST_CREATOR.getXOMElementCopy());
		element.appendChild(TEST_SUBJECT.getXOMElementCopy());
		element.appendChild(TEST_SECURITY.getXOMElementCopy());
		component = new Resource(element);
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());
	}
	
	public void test20ExtensibleDataSizes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		
		// This can be a parameter or an extensible.
		Attribute icAttribute = new Attribute("ICISM:DESVersion", DDMSVersion.getCurrentVersion().getIcismNamespace(),
			"2");
		// This can be a securityAttribute or an extensible.
		Attribute secAttribute = new Attribute("ICISM:classification", 
			DDMSVersion.getCurrentVersion().getIcismNamespace(), "U");
		// This can be an extensible.
		Attribute uniqueAttribute = new Attribute("ddmsence:confidence", "http://ddmsence.urizone.net/", "95");
		List<Attribute> exAttr = new ArrayList<Attribute>();
		
		// Base Case
		Resource component = new Resource(TEST_TOP_LEVEL_COMPONENTS);
		assertNull(component.getDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(0, component.getExtensibleAttributes().getAttributes().size());
		
		// icAttribute as parameter, uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, TEST_DES_VERSION, null, 
			new ExtensibleAttributes(exAttr));
		assertEquals(TEST_DES_VERSION, component.getDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());
		
		// icAttribute and uniqueAttribute as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(icAttribute));
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, new ExtensibleAttributes(exAttr));
		assertNull(component.getDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());
		
		// secAttribute as securityAttribute, uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, null, SecurityAttributesTest.getFixture(false),
			new ExtensibleAttributes(exAttr));
		assertNull(component.getDESVersion());
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());
		
		// secAttribute and uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(secAttribute));
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, new ExtensibleAttributes(exAttr));
		assertNull(component.getDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());
		
		// icAttribute as parameter, secAttribute as securityAttribute, uniqueAttribute as extensibleAttribute
		exAttr.clear();
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, TEST_DES_VERSION, 
			SecurityAttributesTest.getFixture(false), new ExtensibleAttributes(exAttr));
		assertEquals(TEST_DES_VERSION, component.getDESVersion());
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(1, component.getExtensibleAttributes().getAttributes().size());
		
		// icAttribute as parameter, secAttribute and uniqueAttribute as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(secAttribute));
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, TEST_DES_VERSION, null, 
			new ExtensibleAttributes(exAttr));
		assertEquals(TEST_DES_VERSION, component.getDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());
		
		// secAttribute as securityAttribute, icAttribute and uniqueAttribute as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(icAttribute));	
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, null, 
			SecurityAttributesTest.getFixture(false), new ExtensibleAttributes(exAttr));
		assertNull(component.getDESVersion());
		assertFalse(component.getSecurityAttributes().isEmpty());
		assertEquals(2, component.getExtensibleAttributes().getAttributes().size());
		
		// all three as extensibleAttributes
		exAttr.clear();
		exAttr.add(new Attribute(icAttribute));
		exAttr.add(new Attribute(secAttribute));
		exAttr.add(new Attribute(uniqueAttribute));		
		component = new Resource(TEST_TOP_LEVEL_COMPONENTS, new ExtensibleAttributes(exAttr));
		assertNull(component.getDESVersion());
		assertTrue(component.getSecurityAttributes().isEmpty());
		assertEquals(3, component.getExtensibleAttributes().getAttributes().size());
	}
	
	public void testExtensibleDataDuplicates() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			
			// DESVersion in parameter AND extensible.
			try {
				List<Attribute> exAttr = new ArrayList<Attribute>();
				exAttr.add(new Attribute("ICISM:DESVersion", DDMSVersion.getCurrentVersion().getIcismNamespace(), 
					"2"));
				new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, TEST_DES_VERSION, 
					SecurityAttributesTest.getFixture(false), new ExtensibleAttributes(exAttr));
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			
			// classification in securityAttributes AND extensible.
			try {
				List<Attribute> exAttr = new ArrayList<Attribute>();
				exAttr.add(new Attribute("ICISM:classification", 
					DDMSVersion.getCurrentVersion().getIcismNamespace(), "U"));
				new Resource(TEST_TOP_LEVEL_COMPONENTS, null, null, null, 
					SecurityAttributesTest.getFixture(false), new ExtensibleAttributes(exAttr));
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
	
	public void testExtensibleElementElementConstructor() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			createComponents();
			String icPrefix = PropertyReader.getProperty("icism.prefix");
			String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
			ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getElementFixture());
		
			Element element = Util.buildDDMSElement(Resource.NAME, null);
			Util.addAttribute(element, icPrefix, Resource.RESOURCE_ELEMENT_NAME, icNamespace,
				String.valueOf(TEST_RESOURCE_ELEMENT));
			Util.addAttribute(element, icPrefix, Resource.CREATE_DATE_NAME, icNamespace, TEST_CREATE_DATE);
			Util.addAttribute(element, icPrefix, Resource.DES_VERSION_NAME, icNamespace,
				String.valueOf(TEST_DES_VERSION));
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
			element.appendChild(TEST_TITLE.getXOMElementCopy());
			element.appendChild(TEST_CREATOR.getXOMElementCopy());
			element.appendChild(TEST_SUBJECT.getXOMElementCopy());
			element.appendChild(TEST_SECURITY.getXOMElementCopy());
			element.appendChild(component.getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}
	
	public void testExtensibleElementOutput() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();
		ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getElementFixture());
		
		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(component);
		Resource resource = testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE,
			TEST_DES_VERSION);
		assertTrue(resource.toHTML().indexOf("<meta name=\"extensible.layer\" content=\"true\" />") != -1);
		assertTrue(resource.toText().indexOf("Extensible Layer: true\n") != -1);
	}
	
	public void testWrongVersionExtensibleElementAllowed() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		ExtensibleElement component = new ExtensibleElement(ExtensibleElementTest.getElementFixture());
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();
		
		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(component);
		testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);
	}
	
	public void test20TooManyExtensibleElements() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		
		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(new ExtensibleElement(ExtensibleElementTest.getElementFixture()));
		components.add(new ExtensibleElement(ExtensibleElementTest.getElementFixture()));
		
		testConstructor(WILL_FAIL, components, null, null, null);
	}
	
	public void test30TooManyExtensibleElements() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		createComponents();
		
		List<IDDMSComponent> components = new ArrayList<IDDMSComponent>(TEST_NO_OPTIONAL_COMPONENTS);
		components.add(new ExtensibleElement(ExtensibleElementTest.getElementFixture()));
		components.add(new ExtensibleElement(ExtensibleElementTest.getElementFixture()));
		
		testConstructor(WILL_SUCCEED, components, TEST_RESOURCE_ELEMENT, TEST_CREATE_DATE, TEST_DES_VERSION);
	}
	
	public void test20DeclassManualReviewAttribute() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		createComponents();
		String icNamespace = DDMSVersion.getCurrentVersion().getIcismNamespace();
	
		Element element = Util.buildDDMSElement(Resource.NAME, null);
		Util.addAttribute(element, PropertyReader.getProperty("icism.prefix"), SecurityAttributes.DECLASS_MANUAL_REVIEW_NAME, icNamespace, "true");
		SecurityAttributesTest.getFixture(false).addTo(element);
		element.appendChild(TEST_IDENTIFIER.getXOMElementCopy());
		element.appendChild(TEST_TITLE.getXOMElementCopy());
		element.appendChild(TEST_CREATOR.getXOMElementCopy());
		element.appendChild(TEST_SUBJECT.getXOMElementCopy());
		element.appendChild(TEST_SECURITY.getXOMElementCopy());
		Resource resource = testConstructor(WILL_SUCCEED, element);

		// ICISM:declassManualReview should not get picked up as an extensible attribute
		assertEquals(0, resource.getExtensibleAttributes().getAttributes().size());
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
			// Equality after Building
			Resource.Builder builder = new Resource.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Equality with ExtensibleElement
			builder.getExtensibleElements().add(new ExtensibleElement.Builder());
			builder.getExtensibleElements().get(0).setXml("<ddmsence:extension xmlns:ddmsence=\"http://ddmsence.urizone.net/\">This is an extensible element.</ddmsence:extension>");
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
	
	public void testBuilderEmptiness() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			
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
			builder.getProducers().add(new Organization.Builder());
			assertTrue(builder.isEmpty());
			builder.getVirtualCoverages().add(new VirtualCoverage.Builder());
			assertTrue(builder.isEmpty());
			builder.getTemporalCoverages().add(new TemporalCoverage.Builder());
			assertTrue(builder.isEmpty());
			builder.getGeospatialCoverages().add(new GeospatialCoverage.Builder());
			assertTrue(builder.isEmpty());
			builder.getRelatedResources().add(new RelatedResources.Builder());
			assertTrue(builder.isEmpty());
			builder.getExtensibleElements().add(new ExtensibleElement.Builder());
			assertTrue(builder.isEmpty());
			builder.getExtensibleElements().get(0).setXml("InvalidXml");
			assertFalse(builder.isEmpty());
		}
	}
	
	public void testSerializableBuilders() throws Exception {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Resource component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
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
}
