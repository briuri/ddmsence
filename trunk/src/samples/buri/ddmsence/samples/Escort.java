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
package buri.ddmsence.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nu.xom.Comment;
import nu.xom.Document;
import nu.xom.Serializer;
import buri.ddmsence.ddms.AbstractProducerRole;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.format.Extent;
import buri.ddmsence.ddms.format.Format;
import buri.ddmsence.ddms.resource.Addressee;
import buri.ddmsence.ddms.resource.ApplicationSoftware;
import buri.ddmsence.ddms.resource.Contributor;
import buri.ddmsence.ddms.resource.Creator;
import buri.ddmsence.ddms.resource.Dates;
import buri.ddmsence.ddms.resource.Details;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.resource.Language;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.PointOfContact;
import buri.ddmsence.ddms.resource.ProcessingInfo;
import buri.ddmsence.ddms.resource.Publisher;
import buri.ddmsence.ddms.resource.RecordKeeper;
import buri.ddmsence.ddms.resource.RecordsManagementInfo;
import buri.ddmsence.ddms.resource.RequestorInfo;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Source;
import buri.ddmsence.ddms.resource.SubOrganization;
import buri.ddmsence.ddms.resource.Subtitle;
import buri.ddmsence.ddms.resource.Title;
import buri.ddmsence.ddms.resource.Type;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.summary.BoundingBox;
import buri.ddmsence.ddms.summary.BoundingGeometry;
import buri.ddmsence.ddms.summary.Category;
import buri.ddmsence.ddms.summary.CountryCode;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.FacilityIdentifier;
import buri.ddmsence.ddms.summary.GeographicIdentifier;
import buri.ddmsence.ddms.summary.GeospatialCoverage;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.ddms.summary.Link;
import buri.ddmsence.ddms.summary.NonStateActor;
import buri.ddmsence.ddms.summary.PostalAddress;
import buri.ddmsence.ddms.summary.ProductionMetric;
import buri.ddmsence.ddms.summary.RelatedResource;
import buri.ddmsence.ddms.summary.SubDivisionCode;
import buri.ddmsence.ddms.summary.SubjectCoverage;
import buri.ddmsence.ddms.summary.TemporalCoverage;
import buri.ddmsence.ddms.summary.VerticalExtent;
import buri.ddmsence.ddms.summary.VirtualCoverage;
import buri.ddmsence.ddms.summary.gml.Point;
import buri.ddmsence.ddms.summary.gml.Polygon;
import buri.ddmsence.ddms.summary.gml.Position;
import buri.ddmsence.ddms.summary.gml.SRSAttributes;
import buri.ddmsence.samples.util.IComponentBuilder;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * DDMScort is a step-by-step wizard for building a DDMS Resource from scratch, and then saving it to a file.
 * 
 * <p>
 * I have implemented this wizard as a series of textual prompts, to avoid the overhead of having a full-fledged MVC
 * Swing application (or implementing it as a web application and requiring a server to run). It is not as flashy, but
 * the procedural structuring should make it easier to focus on the important sections of the source code.
 * </p>
 * 
 * <p>Currently, the wizard does not ask for the "Additional" ISM security attributes, such as SCI controls or 
 * SAR Identifiers, or SRS Attributes on individual gml:pos elements. It also does not ask for anything from the 
 * Extensible Layer.</p>
 * 
 * <p>For additional details about this application, please see the tutorial on the Documentation page of the DDMSence
 * website.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class Escort {
	
	private boolean _useDummySecurityAttributes = false;
	private List<IDDMSComponent> _topLevelComponents = new ArrayList<IDDMSComponent>();

	private static final BufferedReader INPUT_STREAM = new BufferedReader(new InputStreamReader(System.in));
	private static final Map<Class<?>, IComponentBuilder> BUILDERS = new HashMap<Class<?>, IComponentBuilder>();
	private static final Set<String> GEOSPATIAL_TYPES = new HashSet<String>();
	static {
		GEOSPATIAL_TYPES.add("geographicIdentifier");
		GEOSPATIAL_TYPES.add("boundingBox");
		GEOSPATIAL_TYPES.add("boundingGeometry");
		GEOSPATIAL_TYPES.add("postalAddress");
		GEOSPATIAL_TYPES.add("verticalExtent");
	}
	
	/**
	 * Entry point
	 * 
	 * @param args no parameters are required
	 */
	public static void main(String[] args) {
		try {
			Escort app = new Escort();
			app.run();
		}
		catch (IOException e) {
			System.err.println("Could not parse input: " + e.getMessage());
			System.exit(1);
		}
	}	
	
	/**
	 * Creates the anonymous builders used in the main loop.
	 */
	public Escort() {
		BUILDERS.put(Identifier.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String qualifier = readString("the qualifier [testQualifier]");
				String value = readString("the value [testValue]");
				return (new Identifier(qualifier, value));
			}		
		});
		BUILDERS.put(Title.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the title text [testTitle]");
				return (new Title(text, buildSecurityAttributes("title")));
			}		
		});
		BUILDERS.put(Subtitle.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the subtitle text [testSubtitle]");
				return (new Subtitle(text, buildSecurityAttributes("subtitle")));
			}		
		});
		BUILDERS.put(Description.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the description text [testDescription]");
				SecurityAttributes attr = buildSecurityAttributes("description");
				return (new Description(text, attr));
			}		
		});
		BUILDERS.put(Language.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String qualifier = readString("the qualifier [testQualifier]");
				String value = readString("the value [testValue]");
				return (new Language(qualifier, value));
			}		
		});
		BUILDERS.put(Dates.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String created = readString("the created date [2010]");
				String posted = readString("the posted date [2010]");
				String validTil = readString("the validTil date [2010]");
				String infoCutOff = readString("the infoCutOff date [2010]");
				String approvedOn = readString("the approvedOn date [2010]");
				String receivedOn = readString("the receivedOn date [2010]");
				return (new Dates(created, posted, validTil, infoCutOff, approvedOn, receivedOn));
			}		
		});
		BUILDERS.put(Rights.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				boolean privacy = confirm("Does this resource contain information protected by the Privacy Act?");
				boolean intellectual = confirm("Does this resource have an intellectual property rights owner?");
				boolean copyright = confirm("Is this resource protected by copyright?");
				return (new Rights(privacy, intellectual, copyright));
			}		
		});
		BUILDERS.put(Source.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String qualifier = readString("the qualifier [testQualifier]");
				String value = readString("the value [testValue]");
				String schemaQualifier = readString("the schema qualifier [testQualifier]");
				String schemHref = readString("the schema href [testValue]");
				return (new Source(qualifier, value, schemaQualifier, schemHref, buildSecurityAttributes("source")));
			}		
		});
		BUILDERS.put(Type.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String description = readString("the description child text [testDescription]");
				String qualifier = readString("the qualifier [testQualifier]");
				String value = readString("the value [testValue]");
				SecurityAttributes attr = buildSecurityAttributes("type");
				return (new Type(description, qualifier, value, attr));
			}		
		});
		BUILDERS.put(ApplicationSoftware.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the applicationSoftware text [testSoftware]");
				return (new ApplicationSoftware(text, buildSecurityAttributes("applicationSoftware")));
			}		
		});
		BUILDERS.put(Details.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the revision recall details text [testDetails]");
				return (new Details(text, buildSecurityAttributes("details")));
			}		
		});
		BUILDERS.put(ProcessingInfo.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the processingInfo text [testText]");
				String posted = readString("the processing date [2010]");
				return (new ProcessingInfo(text, posted, buildSecurityAttributes("processingInfo")));
			}		
		});
		BUILDERS.put(RecordKeeper.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String id = readString("the recordKeeperID [testID]");
				Organization org = (Organization) inputLoop(Organization.class);
				return (new RecordKeeper(id, org));
			}		
		});
		BUILDERS.put(RecordsManagementInfo.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				RecordKeeper recordKeeper = (RecordKeeper) inputLoop(RecordKeeper.class);
				ApplicationSoftware applicationSoftware = (ApplicationSoftware) inputLoop(ApplicationSoftware.class);
				boolean vitalRecordIndicator = confirm("Is this publication categorized as a vital record?");
				return (new RecordsManagementInfo(recordKeeper, applicationSoftware, vitalRecordIndicator));
			}		
		});
		BUILDERS.put(Addressee.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				DDMSVersion version = DDMSVersion.getCurrentVersion();
				String entityType = readString("the entity type [organization]");
				IRoleEntity entity = null;
				if (Person.getName(version).equals(entityType))
					entity = (Person) inputLoop(Person.class);
				else if (Organization.getName(version).equals(entityType))
					entity = (Organization) inputLoop(Organization.class);
				return (new Addressee(entity, buildSecurityAttributes("addressee")));
			}		
		});
		BUILDERS.put(RequestorInfo.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				DDMSVersion version = DDMSVersion.getCurrentVersion();
				String entityType = readString("the entity type [organization]");
				IRoleEntity entity = null;
				if (Person.getName(version).equals(entityType))
					entity = (Person) inputLoop(Person.class);
				else if (Organization.getName(version).equals(entityType))
					entity = (Organization) inputLoop(Organization.class);
				return (new RequestorInfo(entity, buildSecurityAttributes("requestorInfo")));
			}		
		});
		BUILDERS.put(AbstractProducerRole.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				DDMSVersion version = DDMSVersion.getCurrentVersion();
				String producerType = readString("the producer type [creator]");
				String entityType = readString("the entity type [organization]");
				SecurityAttributes attr = buildSecurityAttributes("producer");
								
				IRoleEntity entity = null;
				if (Person.getName(version).equals(entityType))
					entity = (Person) inputLoop(Person.class);
				else if (Organization.getName(version).equals(entityType))
					entity = (Organization) inputLoop(Organization.class);
				else if (Service.getName(version).equals(entityType))
					entity = (Service) inputLoop(Service.class);
				else if (Unknown.getName(version).equals(entityType)) 
					entity = (Unknown) inputLoop(Unknown.class);
				
				if (Creator.getName(version).equals(producerType))
					return (new Creator(entity, null, attr));
				if (Contributor.getName(version).equals(producerType))
					return (new Creator(entity, null, attr));
				if (Publisher.getName(version).equals(producerType))
					return (new Publisher(entity, null, attr));
				if (PointOfContact.getName(version).equals(producerType))
					return (new PointOfContact(entity, null, attr));
				throw new InvalidDDMSException("Unknown producerType: " + producerType);
			}		
		});	
		BUILDERS.put(Person.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numNames = readInt("the number of names this person has [1]");
				if (numNames == 0) {
					println("At least 1 name is required. Defaulting to 1 name.");
					numNames = 1;
				}
				int numPhones = readInt("the number of phone numbers this person has [0]");
				int numEmails = readInt("the number of email addresses this person has [0]");

				List<String> names = new ArrayList<String>();
				for (int i = 0; i < numNames; i++) {
					names.add(readString("entity name #" + (i + 1) + " [testName" + (i + 1) + "]"));
				}
				List<String> phones = new ArrayList<String>();
				for (int i = 0; i < numPhones; i++) {
					phones.add(readString("entity phone number #" + (i + 1) + " [testPhone" + (i + 1) + "]"));
				}
				List<String> emails = new ArrayList<String>();
				for (int i = 0; i < numEmails; i++) {
					emails.add(readString("entity email #" + (i + 1) + " [testEmail" + (i + 1) + "]"));
				}
				String surname = readString("the person surname [testSurname]");
				String userID = readString("the person userID [testID]");
				String affiliation = readString("the person affiliation [testOrg]");
				return (new Person(names, surname, phones, emails, userID, affiliation));
			}		
		});	
		BUILDERS.put(Organization.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numNames = readInt("the number of names this organization has [1]");
				if (numNames == 0) {
					println("At least 1 name is required. Defaulting to 1 name.");
					numNames = 1;
				}
				int numPhones = readInt("the number of phone numbers this organization has [0]");
				int numEmails = readInt("the number of email addresses this organization has [0]");
				int numSubs = readInt("the number of suborganizations to include [0]");
				List<String> names = new ArrayList<String>();
				for (int i = 0; i < numNames; i++) {
					names.add(readString("entity name #" + (i + 1) + " [testName" + (i + 1) + "]"));
				}
				List<String> phones = new ArrayList<String>();
				for (int i = 0; i < numPhones; i++) {
					phones.add(readString("entity phone number #" + (i + 1) + " [testPhone" + (i + 1) + "]"));
				}
				List<String> emails = new ArrayList<String>();
				for (int i = 0; i < numEmails; i++) {
					emails.add(readString("entity email #" + (i + 1) + " [testEmail" + (i + 1) + "]"));
				}
				List<SubOrganization> subOrgs = new ArrayList<SubOrganization>();
				for (int i = 0; i < numSubs; i++) {
					println("* SubOrganization #" + (i + 1));
					subOrgs.add((SubOrganization) inputLoop(SubOrganization.class));
				}		
				String acronym = readString("the Organization acronym [testAcronym]");
				return (new Organization(names, phones, emails, subOrgs, acronym));
			}		
		});	
		BUILDERS.put(SubOrganization.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String value = readString("the value [testSubOrganization]");
				SecurityAttributes attr = buildSecurityAttributes("suborganization");
				return (new SubOrganization(value, attr));
			}
		});
		BUILDERS.put(Service.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numNames = readInt("the number of names this service has [1]");
				if (numNames == 0) {
					println("At least 1 name is required. Defaulting to 1 name.");
					numNames = 1;
				}
				int numPhones = readInt("the number of phone numbers this service has [0]");
				int numEmails = readInt("the number of email addresses this service has [0]");
				List<String> names = new ArrayList<String>();
				for (int i = 0; i < numNames; i++) {
					names.add(readString("entity name #" + (i + 1) + " [testName" + (i + 1) + "]"));
				}
				List<String> phones = new ArrayList<String>();
				for (int i = 0; i < numPhones; i++) {
					phones.add(readString("entity phone number #" + (i + 1) + " [testPhone" + (i + 1) + "]"));
				}
				List<String> emails = new ArrayList<String>();
				for (int i = 0; i < numEmails; i++) {
					emails.add(readString("entity email #" + (i + 1) + " [testEmail" + (i + 1) + "]"));
				}
				return (new Service(names, phones, emails));
			}		
		});
		BUILDERS.put(Unknown.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numNames = readInt("the number of names this unknown entity has [1]");
				if (numNames == 0) {
					println("At least 1 name is required. Defaulting to 1 name.");
					numNames = 1;
				}
				int numPhones = readInt("the number of phone numbers this unknown entity has [0]");
				int numEmails = readInt("the number of email addresses this unknown entity has [0]");
				List<String> names = new ArrayList<String>();
				for (int i = 0; i < numNames; i++) {
					names.add(readString("entity name #" + (i + 1) + " [testName" + (i + 1) + "]"));
				}
				List<String> phones = new ArrayList<String>();
				for (int i = 0; i < numPhones; i++) {
					phones.add(readString("entity phone number #" + (i + 1) + " [testPhone" + (i + 1) + "]"));
				}
				List<String> emails = new ArrayList<String>();
				for (int i = 0; i < numEmails; i++) {
					emails.add(readString("entity email #" + (i + 1) + " [testEmail" + (i + 1) + "]"));
				}
				return (new Unknown(names, phones, emails));
			}		
		});	
		BUILDERS.put(Format.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String mimeType = readString("the mimeType [testType]");
				String qualifier = readString("the extent qualifier [testQualifier]");
				String value = readString("the extent value [testValue]");
				String medium = readString("the medium [testValue]");
				Extent extent = new Extent(qualifier, value);
				return (new Format(mimeType, extent, medium));
			}		
		});
		BUILDERS.put(Keyword.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String keyword = readString("the keyword value [testValue]");
				SecurityAttributes attr = buildSecurityAttributes("keyword");
				return (new Keyword(keyword, attr));
			}
		});
		BUILDERS.put(Category.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String qualifier = readString("the qualifier [testQualifier]");
				String code = readString("the code [testCode]");
				String label = readString("the label [testLabel]");
				return (new Category(qualifier, code, label, buildSecurityAttributes("category")));
			}
		});
		BUILDERS.put(ProductionMetric.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String subject = readString("the subject [testSubject]");
				String coverage = readString("the coverage [testCoverage]");
				return (new ProductionMetric(subject, coverage, buildSecurityAttributes("productionMetric")));
			}
		});
		BUILDERS.put(NonStateActor.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String value = readString("the value [testValue]");
				int order = readInt("the order [1]");
				SecurityAttributes attr = buildSecurityAttributes("nonStateActor");
				return (new NonStateActor(value, Integer.valueOf(order), attr));
			}
		});
		BUILDERS.put(SubjectCoverage.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numKeywords = readInt("the number of keywords to include [1]");
				int numCategories = readInt("the number of categories to include [0]");
				int numMetrics = readInt("the number of productionMetrics to include [0]");
				int numActors = readInt("the number of nonStateActors to include [0]");
				if (numKeywords + numCategories == 0) {
					println("At least 1 keyword or category is required. Defaulting to 1 keyword.");
					numKeywords = 1;
				}
				List<Keyword> keywords = new ArrayList<Keyword>();
				for (int i = 0; i < numKeywords; i++) {
					println("* Keyword #" + (i + 1));
					keywords.add((Keyword) inputLoop(Keyword.class));
				}
				List<Category> categories = new ArrayList<Category>();
				for (int i = 0; i < numCategories; i++) {
					println("* Category #" + (i + 1));
					categories.add((Category) inputLoop(Category.class));
				}
				List<ProductionMetric> metrics = new ArrayList<ProductionMetric>();
				for (int i = 0; i < numMetrics; i++) {
					println("* ProductionMetric #" + (i + 1));
					metrics.add((ProductionMetric) inputLoop(ProductionMetric.class));
				}
				List<NonStateActor> actors = new ArrayList<NonStateActor>();
				for (int i = 0; i < numActors; i++) {
					println("* NonStateActor #" + (i + 1));
					actors.add((NonStateActor) inputLoop(NonStateActor.class));
				}
				return (new SubjectCoverage(keywords, categories, metrics, actors, buildSecurityAttributes("subject")));
			}		
		});
		BUILDERS.put(TemporalCoverage.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String timePeriodName = readString("the time period name [testName]");
				String start = readString("the start date [1950]");
				String end = readString("the end date [1959]");
				return (new TemporalCoverage(timePeriodName, start, end, buildSecurityAttributes("temporalCoverage")));
			}		
		});
		BUILDERS.put(VirtualCoverage.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String address = readString("the address [123.456.789.0]");
				String protocol = readString("the protocol [IP]");
				return (new VirtualCoverage(address, protocol, buildSecurityAttributes("virtualCoverage")));
			}		
		});
		BUILDERS.put(FacilityIdentifier.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String beNumber = readString("the beNumber [1234DD56789]");
				String osuffix = readString("the osuffix [DD123]");
				return (new FacilityIdentifier(beNumber, osuffix));
			}		
		});
		BUILDERS.put(GeographicIdentifier.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numNames = readInt("the number of names to include [1]");
				int numRegions = readInt("the number of regions to include [0]");
				List<String> names = new ArrayList<String>();
				for (int i = 0; i < numNames; i++) {
					names.add(readString("name #" + (i + 1) + " [testName" + (i + 1) + "]"));
				}
				List<String> regions = new ArrayList<String>();
				for (int i = 0; i < numRegions; i++) {
					regions.add(readString("region #" + (i + 1) + " [testRegion" + (i + 1) + "]"));
				}	
				CountryCode code = null;
				if (confirm("Include a countryCode?")) {
					String qualifier = readString("the qualifier [testQualifier]");
					String value = readString("the value [USA]");
					code = new CountryCode(qualifier, value);
				}
				SubDivisionCode subCode = null;
				if (confirm("Include a subDivisionCode?")) {
					String qualifier = readString("the qualifier [testQualifier]");
					String value = readString("the value [USA]");
					subCode = new SubDivisionCode(qualifier, value);
				}
				return (new GeographicIdentifier(names, regions, code, subCode));
			}		
		});
		BUILDERS.put(BoundingBox.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String west = readString("the westbound longitude [0]");
				String east = readString("the eastbound longitude [15]");
				String south = readString("the southbound latitude [0]");
				String north = readString("the northbound latitude [15]");
				return (new BoundingBox(Double.valueOf(west).doubleValue(), Double.valueOf(east).doubleValue(), 
					Double.valueOf(south).doubleValue(), Double.valueOf(north).doubleValue()));
			}
		});
		BUILDERS.put(Position.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String coordsString = readString("the coordinates as a space-delimited string [5.0 6.0 7.0]");
				List<String> strings = Util.getXsListAsList(coordsString);
				List<Double> coords = new ArrayList<Double>();
				for (String string : strings) {
					coords.add(Double.valueOf(string));
				}
				// Skipping SRS Attributes
				return (new Position(coords, null));
			}		
		});
		BUILDERS.put(Polygon.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numPositions = readInt("the number of positions in this polygon [4]");
				if (numPositions < 4) {
					println("At least 4 positions are required for a Polygon. Defaulting to 4.");
					numPositions = 4;
				}
				List<Position> positions = new ArrayList<Position>();
				for (int i = 0; i < numPositions - 1; i++) {
					println("** Position #" + (i + 1));
					positions.add((Position) inputLoop(Position.class));
				}
				println("The final position will be the first coordinate of the Polygon (to close the shape).");
				positions.add(positions.get(0));
				
				String srsName = readString("the Polygon's srsName [testName]");
				int srsDimension = readInt("the Polygon's srsDimension [1]");
				String axisLabels = readString("the Polygon's Axis Labels, as a space-delimited string [x y]");
				String uomLabels = readString("the Polygon's UOM Labels, as a space-delimited string [Meter Meter]");
				String id = readString("the Polygon's gml:id [testId]");
								
				SRSAttributes attr = new SRSAttributes(srsName, new Integer(srsDimension), 
					Util.getXsListAsList(axisLabels), Util.getXsListAsList(uomLabels));
				return (new Polygon(positions, attr, id));
			}		
		});
		BUILDERS.put(Point.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				Position position = (Position) inputLoop(Position.class);
				String srsName = readString("the Point's srsName [testName]");
				int srsDimension = readInt("the Point's srsDimension [1]");
				String axisLabels = readString("the Point's Axis Labels, as a space-delimited string [x y]");
				String uomLabels = readString("the Point's UOM Labels, as a space-delimited string [Meter Meter]");
				String id = readString("the Point's gml:id [testId]");
				
				SRSAttributes attr = new SRSAttributes(srsName, new Integer(srsDimension), 
					Util.getXsListAsList(axisLabels), Util.getXsListAsList(uomLabels));
				return (new Point(position, attr, id));
			}		
		});
		BUILDERS.put(BoundingGeometry.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numPolygons = readInt("the number of polygons to include [0]");
				int numPoints = readInt("the number of points to include [1]");
				if (numPolygons + numPoints == 0) {
					println("At least 1 polygon or point is required. Defaulting to 1 point.");
					numPoints = 1;
				}
				List<Polygon> polygons = new ArrayList<Polygon>();
				for (int i = 0; i < numPolygons; i++) {
					println("* Polygon #" + (i + 1));
					polygons.add((Polygon) inputLoop(Polygon.class));
				}
				List<Point> points = new ArrayList<Point>();
				for (int i = 0; i < numPoints; i++) {
					println("* Point #" + (i + 1));
					points.add((Point) inputLoop(Point.class));
				}
				return (new BoundingGeometry(polygons, points));
			}		
		});
		BUILDERS.put(PostalAddress.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numStreets = readInt("the number of street address lines to include [1]");
				if (numStreets > 6) {
					println("A maximum of 6 street lines can be used. Defaulting to 6.");
					numStreets = 6;
				}
				List<String> streets = new ArrayList<String>();
				for (int i = 0; i < numStreets; i++) {
					streets.add(readString("street #" + (i + 1) + " [testStreet" + (i + 1) + "]"));
				}				
				String city = readString("the city [testCity]");
				boolean hasState = confirm("Does this postal address have a state (instead of a province)?");
				String stateOrProvince = readString("the " + (hasState ? "state [testState]" 
					: "province [testProvince]"));
				String postalCode = readString("the postal code [testCode]");
				CountryCode code = null;
				if (confirm("Include a countryCode?")) {
					String qualifier = readString("the qualifier [testQualifier]");
					String value = readString("the value [USA]");
					code = new CountryCode(qualifier, value);
				}
				return (new PostalAddress(streets, city, stateOrProvince, postalCode, code, hasState));
			}		
		});
		BUILDERS.put(VerticalExtent.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String min = readString("the minimum vertical extent [0]");
				String max = readString("the maximum vertical extent [15]");
				String uom = readString("the unit of measure [Meter]");
				String datum = readString("the datum [MSL]");
				return (new VerticalExtent(Double.valueOf(min).doubleValue(), Double.valueOf(max).doubleValue(), uom,
					datum));
			}
		});
		BUILDERS.put(GeospatialCoverage.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				GeographicIdentifier geoId = null;
				BoundingBox box = null;
				BoundingGeometry geo = null;
				PostalAddress address = null;
				VerticalExtent extent = null;
				if (confirm("Should the geospatialCoverage be defined in terms of a facilityIdentifier?")) {
					geoId = new GeographicIdentifier((FacilityIdentifier) inputLoop(FacilityIdentifier.class));
				} else {
					String type = null;
					while (!GEOSPATIAL_TYPES.contains(type)) {
						type = readString("the coverage element type [geographicIdentifier, boundingBox, "
							+ "boundingGeometry, postalAddress, or verticalExtent]");
					}
					if ("geographicIdentifier".equals(type)) {
						geoId = (GeographicIdentifier) inputLoop(GeographicIdentifier.class);
					} else if ("boundingBox".equals(type)) {
						box = (BoundingBox) inputLoop(BoundingBox.class);
					} else if ("boundingGeometry".equals(type)) {
						geo = (BoundingGeometry) inputLoop(BoundingGeometry.class);
					} else if ("postalAddress".equals(type)) {
						address = (PostalAddress) inputLoop(PostalAddress.class);
					} else if ("verticalExtent".equals(type)) {
						extent = (VerticalExtent) inputLoop(VerticalExtent.class);
					}
				}
				String precedence = readString("the precedence [Primary]");
				int order = readInt("the order []");
				return (new GeospatialCoverage(geoId, box, geo, address, extent, precedence, order,
					buildSecurityAttributes("geospatialCoverage")));
			}
		});
		BUILDERS.put(Link.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String href = readString("the link href [testHref]");
				String role = readString("the link role [testRole]");
				String title = readString("the link title [testTitle]");
				String label = readString("the link label [testValue]");
				return (new Link(href, role, title, label));
			}		
		});
		BUILDERS.put(RelatedResource.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numLinks = readInt("the number of links on this relatedResource [1]");
				if (numLinks == 0) {
					println("At least 1 link is required. Defaulting to 1.");
					numLinks = 1;
				}
				List<Link> links = new ArrayList<Link>();
				for (int i = 0; i < numLinks; i++) {
					println("** Link #" + (i + 1));
					links.add((Link) inputLoop(Link.class));
				}
				String relationship = readString("the relatedResource relationship [testQualifier]");
				String direction = readString("the relatedResource direction [outbound]");
				String qualifier = readString("the relatedResource qualifier [testQualifier]");
				String value = readString("the relatedResource value [testValue]");
				return (new RelatedResource(links, relationship, direction, qualifier, value,buildSecurityAttributes("relatedResource")));
			}		
		});
		BUILDERS.put(Security.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				return (new Security(buildSecurityAttributes("security element")));
			}		
		});
		BUILDERS.put(Resource.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				boolean resourceElement = confirm("Does this tag set the classification for the resource as a whole?");
				String createDate = readString("Resource createDate [2010-04-01]");
				int desVersion = readInt("the Resource DESVersion [2]");
				return (new Resource(getTopLevelComponents(), resourceElement, createDate, new Integer(desVersion), 
					buildSecurityAttributes("resource")));
			}		
		});
	}
	
	/**
	 * The main execution loop of the program
	 */
	private void run() throws IOException {
		println("Escort: a DDMSence Sample\n");
		
		println("This program allows you to build a DDMS 4.0 resource from scratch.");
		println("If you do not know how to answer a question, a suggested valid answer is provided in square brackets.");
		println("However, this is not a default value (hitting Enter will answer the question with an empty string).\n");

		println("In FAST mode, Escort will only ask you to create top-level components which are required for a "
			+ "valid Resource.");
		println("In COMPLETE mode, Escort will let you create all of the top-level components.");
		boolean onlyRequiredComponents = confirm("Would you like to run in FAST mode?");
		_useDummySecurityAttributes = confirm("Would you like to use dummy security attributes, Unclassified/USA, throughout the resource?");
				
		DDMSVersion.setCurrentVersion("4.0");
		
		printHead("ddms:identifier (at least 1 required)");
		getTopLevelComponents().add(inputLoop(Identifier.class));
		while (!onlyRequiredComponents && confirm("Add another ddms:identifier?")) {
			getTopLevelComponents().add(inputLoop(Identifier.class));	
		}
		
		printHead("ddms:title (at least 1 required)");
		getTopLevelComponents().add(inputLoop(Title.class));
		while (!onlyRequiredComponents && confirm("Add another ddms:title?")) {
			getTopLevelComponents().add(inputLoop(Title.class));	
		}
		
		if (!onlyRequiredComponents) {
			printHead("ddms:subtitle (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Subtitle.class));	
				while (confirm("Add another ddms:subtitle?")) {
					getTopLevelComponents().add(inputLoop(Subtitle.class));	
				}
			}
			
			printHead("ddms:description (only 1 allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Description.class));	
			}
			
			printHead("ddms:language (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Language.class));	
				while (confirm("Add another ddms:language?")) {
					getTopLevelComponents().add(inputLoop(Language.class));	
				}
			}
			
			printHead("ddms:dates (only 1 allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Dates.class));	
			}
			
			printHead("ddms:rights (only 1 allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Rights.class));	
			}

			printHead("ddms:source (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Source.class));	
				while (confirm("Add another ddms:source?")) {
					getTopLevelComponents().add(inputLoop(Source.class));	
				}
			}
			
			printHead("ddms:type (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Type.class));	
				while (confirm("Add another ddms:type?")) {
					getTopLevelComponents().add(inputLoop(Type.class));	
				}
			}
		}
				
		printHead("Producers: creator, publisher, contributor, and pointOfContact (at least 1 required)");
		getTopLevelComponents().add(inputLoop(AbstractProducerRole.class));	
		while (!onlyRequiredComponents && confirm("Add another producer?")) {
			getTopLevelComponents().add(inputLoop(AbstractProducerRole.class));	
		}
		
		if (!onlyRequiredComponents) {
			printHead("ddms:format (only 1 allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(Format.class));	
			}
		}
		
		printHead("ddms:subjectCoverage (at least 1 required)");
		getTopLevelComponents().add(inputLoop(SubjectCoverage.class));
		while (!onlyRequiredComponents && confirm("Add another ddms:subjectCoverage?")) {
			getTopLevelComponents().add(inputLoop(SubjectCoverage.class));	
		}
		
		if (!onlyRequiredComponents) {
			printHead("ddms:virtualCoverage (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(VirtualCoverage.class));	
				while (confirm("Add another ddms:virtualCoverage?")) {
					getTopLevelComponents().add(inputLoop(VirtualCoverage.class));	
				}
			}
			
			printHead("ddms:temporalCoverage (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(TemporalCoverage.class));	
				while (confirm("Add another ddms:temporalCoverage?")) {
					getTopLevelComponents().add(inputLoop(TemporalCoverage.class));	
				}
			}
			
			printHead("ddms:geospatialCoverage (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(GeospatialCoverage.class));	
				while (confirm("Add another ddms:geospatialCoverage?")) {
					getTopLevelComponents().add(inputLoop(GeospatialCoverage.class));	
				}
			}
			
			printHead("ddms:relatedResource (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(RelatedResource.class));	
				while (confirm("Add another ddms:relatedResource?")) {
					getTopLevelComponents().add(inputLoop(RelatedResource.class));	
				}
			}
		}
				
		printHead("ddms:security (exactly 1 required)");
		getTopLevelComponents().add(inputLoop(Security.class));
		
		printHead("ddms:Resource Attributes (all required)");
		Resource resource = (Resource) inputLoop(Resource.class);
		println("The DDMS Resource is valid!");
		if (resource.getValidationWarnings().isEmpty())
			println("No warnings were recorded.");
		else {
			println("The following warnings were recorded:");
			for (ValidationMessage warning : resource.getValidationWarnings())
				println("   [WARNING] " + warning.getLocator() + ": " + warning.getText());
		}
		
		printHead("Saving the Resource");
		if (confirm("Would you like to save this file?")) {
			println("This Resource will be saved as XML in the " + PropertyReader.getProperty("sample.data")
				+ " directory.");
			while (true) {
				String filename = readString("a filename");
				if (filename.length() > 0) {
					saveFile(filename, resource);
					break;
				}
			}
			println("\nYou can now open your saved file with the Essentials application.");
		}	
		println("The Escort wizard is now finished.");
	}

	/**
	 * Loops around a builder's input methods until a valid component is created,
	 * and then returns that component
	 * 
	 * @param theClass the class of the component to build
	 * @return a valid component
	 */
	private IDDMSComponent inputLoop(Class<?> theClass) throws IOException {
		IDDMSComponent component = null;
		while (component == null) {
			try {
				component = BUILDERS.get(theClass).build();
			}
			catch (Exception e) {
				printError(e);
			}
		}
		return (component);
	}
	
	/**
	 * Saves the valid resource to a file.
	 * 
	 * @param filename the filename
	 * @throws IOException
	 */
	private void saveFile(String filename, Resource resource) throws IOException {
		FileOutputStream os = null;
		try {
			Document doc = new Document(resource.getXOMElementCopy());
			doc.insertChild(new Comment(" Generated by Escort in DDMSence v" + PropertyReader.getProperty("version")
				+ " "), 0);
			File outputFile = new File(PropertyReader.getProperty("sample.data"), filename);
			os = new FileOutputStream(outputFile);
			Serializer serializer = new Serializer(os);
			serializer.setIndent(3);
			serializer.write(doc);
			println("File saved at \"" + outputFile.getAbsolutePath() + "\".");
		} finally {
			if (os != null)
				os.close();
		}
	}

	/**
	 * Helper method to build a security attributes object
	 * 
	 * @param elementName the name of the element being decorated
	 * @return a valid SecurityAttributes
	 * @throws InvalidDDMSException
	 */
	private SecurityAttributes buildSecurityAttributes(String elementName) throws IOException, InvalidDDMSException {
		String classification = getUseDummySecurityAttributes() ? "U" : readString("the " + elementName
			+ "'s classification [U]");
		String ownerProducers = getUseDummySecurityAttributes() ? "USA" : readString("the " + elementName
			+ "'s ownerProducers as a space-delimited string [USA]");
		return (new SecurityAttributes(classification, Util.getXsListAsList(ownerProducers), null));
	}

	/**
	 * Accepts Y/N from the user.
	 * 
	 * @param text the question to ask
	 * @return a boolean
	 * @throws IOException
	 */
	private boolean confirm(String text) throws IOException {
		while (true) {
			print(text + " [Y/N]: ");
			System.out.flush();
			String result = INPUT_STREAM.readLine();
			result = result.trim();
			if (result.length() > 0) {
				if ("y".equalsIgnoreCase(result) || "n".equalsIgnoreCase(result))
					return ("y".equalsIgnoreCase(result));
			}
		}
	}
	
	/**
	 * Reads a number from the command line.
	 * 
	 * @param name the descriptive name of the value	
	 * @return the value as an int
	 *
	 * @throws IOException
	 */
	private int readInt(String name) throws IOException {
		while (true) {
			String input = readString(name);
			try {
				int value = Integer.parseInt(input);
				if (value >= 0)
					return (value);
				println("A positive integer is required.");
			} catch (NumberFormatException nfe) {
				println("This could not be converted into a number: " + input);
			}
		}
	}

	/**
	 * Reads a string from the command line.
	 * 
	 * @param name the descriptive name of the value
	 * @return the value
	 * 
	 * @throws IOException
	 */
	private String readString(String name) throws IOException {
		while (true) {
			print("Please enter " + name + ": ");
			System.out.flush();
			String result = INPUT_STREAM.readLine();
			result = result.trim();
			return (result);				
		}
	}

	/**
	 * Convenience method to print a title
	 * 
	 * @param title the text of the title
	 */
	private void printHead(String title) {
		println("\n=== " + title + " ===");
	}
	
	/**
	 * Formats an error message
	 * 
	 * @param e the exception
	 */
	private void printError(Exception e) {
		//e.printStackTrace();
		if (e instanceof InvalidDDMSException) {
			InvalidDDMSException ide = (InvalidDDMSException) e;
			String prefix = (Util.isEmpty(ide.getLocator()) ? "" : ide.getLocator() + ": ");
			println("[ERROR] " + prefix + e.getMessage());
		}
		else
			println("[ERROR]: " + e.getMessage());
	}
	
	/**
	 * Convenience method to print a string.
	 * 
	 * @param str the string to print.
	 */
	private void print(String str) {
		System.out.print(str);
	}

	/**
	 * Convenience method to print a string followed by a newline.
	 * 
	 * @param str the string to print.
	 */
	private void println(String str) {
		System.out.println(str);
	}

	/**
	 * Accessor for the top level components
	 */
	private List<IDDMSComponent> getTopLevelComponents() {
		return _topLevelComponents;
	}

	/**
	 * Accessor for the useDummySecurityAttributes flag
	 */
	public boolean getUseDummySecurityAttributes() {
		return _useDummySecurityAttributes;
	}
}