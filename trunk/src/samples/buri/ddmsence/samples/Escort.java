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
import buri.ddmsence.ddms.IProducerEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.format.Format;
import buri.ddmsence.ddms.format.Extent;
import buri.ddmsence.ddms.resource.Contributor;
import buri.ddmsence.ddms.resource.Creator;
import buri.ddmsence.ddms.resource.Dates;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.resource.Language;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.PointOfContact;
import buri.ddmsence.ddms.resource.Publisher;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Source;
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
import buri.ddmsence.ddms.summary.PostalAddress;
import buri.ddmsence.ddms.summary.RelatedResource;
import buri.ddmsence.ddms.summary.RelatedResources;
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
 * <p>Currently, the wizard does not ask for the "Additional" ICISM security attributes, such as SCI controls or 
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
				String classification = readString("the title classification [U]");
				String ownerProducers = readString("the title's ownerProducers as a space-delimited string [USA]");
				return (new Title(text, buildSecurityAttributes(classification, ownerProducers)));
			}		
		});
		BUILDERS.put(Subtitle.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the subtitle text [testSubtitle]");
				String classification = readString("the subtitle classification [U]");
				String ownerProducers = readString("the subtitle's ownerProducers as a space-delimited string [USA]");
				return (new Subtitle(text, buildSecurityAttributes(classification, ownerProducers)));
			}		
		});
		BUILDERS.put(Description.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the description text [testDescription]");
				String classification = readString("the description classification [U]");
				String ownerProducers = readString("the description's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
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
				String classification = readString("the source classification [U]");
				String ownerProducers = readString("the source's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Source(qualifier, value, schemaQualifier, schemHref, attr));
			}		
		});
		BUILDERS.put(Type.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String description = readString("the description child text [testDescription]");
				String qualifier = readString("the qualifier [testQualifier]");
				String value = readString("the value [testValue]");
				String classification = readString("the type classification [U]");
				String ownerProducers = readString("the type's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Type(description, qualifier, value, attr));
			}		
		});
		BUILDERS.put(AbstractProducerRole.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String producerType = readString("the producer type [creator]");
				String entityType = readString("the entity type [Organization]");
				int numNames = readInt("the number of names this producer has [1]");
				int numPhones = readInt("the number of phone numbers this producer has [0]");
				int numEmails = readInt("the number of email addresses this producer has [0]");
				if (numNames == 0) {
					println("At least 1 name is required. Defaulting to 1 name.");
					numNames = 1;
				}
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
				String surname = null;
				String userID = null;
				String affiliation = null;
				if (Person.getName(DDMSVersion.getCurrentVersion()).equals(entityType)) {
					surname = readString("the Person surname [testSurname]");
					userID = readString("the Person userID [testID]");
					affiliation = readString("the Person affiliation [testOrg]");
				}
				String classification = readString("the producer classification [U]");
				String ownerProducers = readString("the producer's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				
				IProducerEntity entity = null;
				if (Person.getName(DDMSVersion.getCurrentVersion()).equals(entityType))
					entity = new Person(producerType, surname, names, userID, affiliation, phones, emails);
				else if (Organization.getName(DDMSVersion.getCurrentVersion()).equals(entityType))
					entity = new Organization(producerType, names, phones, emails);
				else if (Service.getName(DDMSVersion.getCurrentVersion()).equals(entityType))
					entity = new Service(producerType, names, phones, emails);
				else 
					entity = new Unknown(producerType, names, phones, emails);
				
				if (Creator.getName(DDMSVersion.getCurrentVersion()).equals(producerType))
					return (new Creator(entity, null, attr));
				if (Contributor.getName(DDMSVersion.getCurrentVersion()).equals(producerType))
					return (new Creator(entity, null, attr));
				if (Publisher.getName(DDMSVersion.getCurrentVersion()).equals(producerType))
					return (new Publisher(entity, null, attr));
				if (PointOfContact.getName(DDMSVersion.getCurrentVersion()).equals(producerType))
					return (new PointOfContact(entity, null, attr));
				throw new InvalidDDMSException("Unknown producerType: " + producerType);
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
				String classification = readString("the keyword's classification [U]");
				String ownerProducers = readString("the keyword's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Keyword(keyword, attr));
			}
		});
		BUILDERS.put(Category.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String qualifier = readString("the qualifier [testQualifier]");
				String code = readString("the code [testCode]");
				String label = readString("the label [testLabel]");
				String classification = readString("the category's classification [U]");
				String ownerProducers = readString("the category's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Category(qualifier, code, label, attr));
			}
		});
		BUILDERS.put(SubjectCoverage.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numKeywords = readInt("the number of keywords to include [1]");
				int numCategories = readInt("the number of categories to include [0]");
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
				String classification = readString("the subject classification [U]");
				String ownerProducers = readString("the subject's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new SubjectCoverage(keywords, categories, attr));
			}		
		});
		BUILDERS.put(TemporalCoverage.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String timePeriodName = readString("the time period name [testName]");
				String start = readString("the start date [1950]");
				String end = readString("the end date [1959]");
				String classification = readString("the temporalCoverage classification [U]");
				String ownerProducers = 
					readString("the temporalCoverage's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);			
				return (new TemporalCoverage(timePeriodName, start, end, attr));
			}		
		});
		BUILDERS.put(VirtualCoverage.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String address = readString("the address [123.456.789.0]");
				String protocol = readString("the protocol [IP]");
				String classification = readString("the virtualCoverage classification [U]");
				String ownerProducers = 
					readString("the virtualCoverage's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new VirtualCoverage(address, protocol, attr));
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
					code = new CountryCode("geographicIdentifier", qualifier, value);
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
								
				SRSAttributes attr = new SRSAttributes(srsName, new Integer(srsDimension), Util.getXsListAsList(axisLabels), 
					Util.getXsListAsList(uomLabels));
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
				
				SRSAttributes attr = new SRSAttributes(srsName, new Integer(srsDimension), Util.getXsListAsList(axisLabels), 
					Util.getXsListAsList(uomLabels));
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
					code = new CountryCode("postalAddress", qualifier, value);
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
				}
				else {
					String type = null;
					while (!GEOSPATIAL_TYPES.contains(type)) {
						type = readString("the coverage element type [geographicIdentifier, boundingBox, "
							+ "boundingGeometry, postalAddress, or verticalExtent]");
					}
					if ("geographicIdentifier".equals(type)) {
						geoId = (GeographicIdentifier) inputLoop(GeographicIdentifier.class);
					}
					else if ("boundingBox".equals(type)) {
						box = (BoundingBox) inputLoop(BoundingBox.class);
					}
					else if ("boundingGeometry".equals(type)) {
						geo = (BoundingGeometry) inputLoop(BoundingGeometry.class);
					}
					else if ("postalAddress".equals(type)) {
						address = (PostalAddress) inputLoop(PostalAddress.class);
					}
					else if ("verticalExtent".equals(type)) {
						extent = (VerticalExtent) inputLoop(VerticalExtent.class);
					}
				}
				String classification = readString("the geospatialCoverage classification [U]");
				String ownerProducers = 
					readString("the geospatialCoverage's ownerProducers as a space-delimited string [USA]");	
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new GeospatialCoverage(geoId, box, geo, address, extent, attr));
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
				int numLinks = readInt("the number of links on this RelatedResource [1]");
				if (numLinks == 0) {
					println("At least 1 link is required. Defaulting to 1.");
					numLinks = 1;
				}
				List<Link> links = new ArrayList<Link>();
				for (int i = 0; i < numLinks; i++) {
					println("** Link #" + (i + 1));
					links.add((Link) inputLoop(Link.class));
				}
				String qualifier = readString("the RelatedResource qualifier [testQualifier]");
				String value = readString("the RelatedResource value [testValue]");
				return (new RelatedResource(links, qualifier, value));
			}		
		});
		BUILDERS.put(RelatedResources.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				int numRelated = readInt("the number of separate RelatedResource elements to include [1]");
				if (numRelated == 0) {
					println("At least 1 resource is required. Defaulting to 1.");
					numRelated = 1;
				}
				List<RelatedResource> resources = new ArrayList<RelatedResource>();
				for (int i = 0; i < numRelated; i++) {
					println("** RelatedResource #" + (i + 1));
					resources.add((RelatedResource) inputLoop(RelatedResource.class));
				}
				String relationship = readString("the relatedResources relationship [testQualifier]");
				String direction = readString("the relatedResources direction [outbound]");
				String classification = readString("the relatedResources classification [U]");
				String ownerProducers = 
					readString("the relatedResources ownerProducers as a space-delimited string [USA]");	
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new RelatedResources(resources, relationship, direction, attr));
			}		
		});
		BUILDERS.put(Security.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String classification = readString("the classification [U]");
				String ownerProducers = readString("the ownerProducers as a space-delimited string [USA]");	
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Security(attr));
			}		
		});
		BUILDERS.put(Resource.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				boolean resourceElement = confirm("Does this tag set the classification for the resource as a whole?");
				String createDate = readString("Resource createDate [2010-04-01]");
				int desVersion = readInt("the Resource DESVersion [2]");
				String classification = readString("the Resource classification [U]");
				String ownerProducers = readString("the Resource's ownerProducers as a space-delimited string [USA]");
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Resource(getTopLevelComponents(), resourceElement, createDate, new Integer(desVersion), 
					attr));
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
		
		printHead("ddms:subjectCoverage (exactly 1 required)");
		getTopLevelComponents().add(inputLoop(SubjectCoverage.class));
		
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
			
			printHead("ddms:relatedResources (any number allowed)");
			if (confirm("Include this component?")) {
				getTopLevelComponents().add(inputLoop(RelatedResources.class));	
				while (confirm("Add another ddms:relatedResources?")) {
					getTopLevelComponents().add(inputLoop(RelatedResources.class));	
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
			println("This Resource will be saved as XML in the " + PropertyReader.getProperty("sample.data") + " directory.");
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
			doc.insertChild(new Comment(" Generated by Escort in DDMSence v" + PropertyReader.getProperty("version") + " "), 0);
			File outputFile = new File(PropertyReader.getProperty("sample.data"), filename);
			os = new FileOutputStream(outputFile);
			Serializer serializer = new Serializer(os);
			serializer.setIndent(3);
			serializer.write(doc);
			println("File saved at \"" + outputFile.getAbsolutePath() + "\".");
		}
		finally {
			if (os != null)
				os.close();
		}
	}

	/**
	 * Helper method to build a security attributes object
	 * 
	 * @param classification the classification
	 * @param ownerProducers a space-delimited list of ownerProducers
	 * @return a valid SecurityAttributes
	 * @throws InvalidDDMSException
	 */
	private SecurityAttributes buildSecurityAttributes(String classification, String ownerProducers)
		throws InvalidDDMSException {
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
		e.printStackTrace();
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
}