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
package buri.ddmsence.samples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
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
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.FacilityIdentifier;
import buri.ddmsence.ddms.summary.GeographicIdentifier;
import buri.ddmsence.ddms.summary.GeospatialCoverage;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.ddms.summary.Link;
import buri.ddmsence.ddms.summary.RelatedResource;
import buri.ddmsence.ddms.summary.RelatedResources;
import buri.ddmsence.ddms.summary.SubjectCoverage;
import buri.ddmsence.ddms.summary.TemporalCoverage;
import buri.ddmsence.ddms.summary.VirtualCoverage;
import buri.ddmsence.util.PropertyReader;

/**
 * DDMScort is a step-by-step wizard for building a DDMS Resource from scratch, and then saving it to a file.
 * 
 * <p>
 * I have implemented this wizard as a series of textual prompts, to avoid the overhead of having a full-fledged MVC
 * Swing application (or implementing it as a web application and requiring a server to run). It is not as flashy, but
 * the procedural structuring should make it easier to focus on the important sections of the source code.
 * </p>
 * 
 * <p>To simplify the wizard, some optional sections are skipped. I may add them in eventually, but I feel like
 * there are enough examples right now:</p>
 * 
 * <ul>
 * 		<li>"Additional" ICISM attributes</li>
 * 		<li>Additional names, phones, and emails in a producer element.</li>
 * 		<li>Categories in a subjectCoverage element.</li>
 * 		<li>GeospatialCoverage is only FacilityIdentifier-based.</li>
 * 		<li>Multiple links or RelatedResource elements in a relatedResoures element.</li>
 * </ul>
 * 
 * <p>
 * For additional details about this application, please see the tutorial on the Documentation page of the DDMSence
 * website.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class Escort {

	private static final BufferedReader INPUT_STREAM = new BufferedReader(new InputStreamReader(System.in));
	private static final String SAMPLE_DIR = PropertyReader.getProperty("sample.data");
	
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
	 * Empty constructor
	 */
	public Escort() {}
	
	/**
	 * The main execution loop of the program
	 */
	private void run() throws IOException {
		println("Escort: a DDMSence Sample\n");
		
		println("This program allows you to build a DDMS resource from scratch.");
		println("If you do not know how to answer a question, a suggested valid answer is provided in square brackets.\nHowever, this is not a default value (hitting Enter will answer the question with an empty string).\n\n");
		
		println("Starting a new ddms:Resource...");
		Resource resource;
		List<IDDMSComponent> topLevelComponents = new ArrayList<IDDMSComponent>();
		
		println("\nAt least 1 ddms:identifier is required.");
		topLevelComponents.add(buildIdentifier());
		while (confirm("Add another ddms:identifier?")) {
			topLevelComponents.add(buildIdentifier());	
		}
		
		println("\nAt least 1 ddms:title is required.");
		topLevelComponents.add(buildTitle());
		while (confirm("Add another ddms:title?")) {
			topLevelComponents.add(buildTitle());	
		}
		
		println("\nddms:subtitle is optional.");
		if (confirm("Add an optional ddms:subtitle?")) {
			topLevelComponents.add(buildSubtitle());	
			while (confirm("Add another ddms:subtitle?")) {
				topLevelComponents.add(buildSubtitle());	
			}
		}
		
		println("\nddms:description is optional.");
		if (confirm("Add the optional ddms:description?")) {
			topLevelComponents.add(buildDescription());	
		}
		
		println("\nddms:language is optional.");
		if (confirm("Add an optional ddms:language?")) {
			topLevelComponents.add(buildLanguage());	
			while (confirm("Add another ddms:language?")) {
				topLevelComponents.add(buildLanguage());	
			}
		}
		
		println("\nddms:dates is optional.");
		if (confirm("Add the optional ddms:dates?")) {
			topLevelComponents.add(buildDates());	
		}
		
		println("\nddms:rights is optional.");
		if (confirm("Add the optional ddms:rights?")) {
			topLevelComponents.add(buildRights());	
		}

		println("\nddms:source is optional.");
		if (confirm("Add an optional ddms:source?")) {
			topLevelComponents.add(buildSource());	
			while (confirm("Add another ddms:source?")) {
				topLevelComponents.add(buildSource());	
			}
		}
		
		println("\nddms:type is optional.");
		if (confirm("Add an optional ddms:type?")) {
			topLevelComponents.add(buildType());	
			while (confirm("Add another ddms:type?")) {
				topLevelComponents.add(buildType());	
			}
		}
		
		println("\nAt least one producer (creator, publisher, contributor, pointOfContact) is required.");
		topLevelComponents.add(buildProducer());	
		while (confirm("Add another producer?")) {
			topLevelComponents.add(buildProducer());	
		}
		
		println("\nddms:format is optional.");
		if (confirm("Add the optional ddms:format?")) {
			topLevelComponents.add(buildFormat());	
		}
		
		println("\nddms:subjectCoverage is required.");
		topLevelComponents.add(buildSubjectCoverage());
		
		println("\nddms:virtualCoverage is optional.");
		if (confirm("Add an optional ddms:virtualCoverage?")) {
			topLevelComponents.add(buildVirtualCoverage());	
			while (confirm("Add another ddms:virtualCoverage?")) {
				topLevelComponents.add(buildVirtualCoverage());	
			}
		}
		
		println("\nddms:temporalCoverage is optional.");
		if (confirm("Add an optional ddms:temporalCoverage?")) {
			topLevelComponents.add(buildTemporalCoverage());	
			while (confirm("Add another ddms:temporalCoverage?")) {
				topLevelComponents.add(buildTemporalCoverage());	
			}
		}
		
		println("\nddms:geospatialCoverage is optional.");
		if (confirm("Add an optional ddms:geospatialCoverage?")) {
			topLevelComponents.add(buildGeospatialCoverage());	
			while (confirm("Add another ddms:geospatialCoverage?")) {
				topLevelComponents.add(buildGeospatialCoverage());	
			}
		}
		
		println("\nddms:relatedResources is optional.");
		if (confirm("Add an optional ddms:relatedResources?")) {
			topLevelComponents.add(buildRelatedResources());	
			while (confirm("Add another ddms:relatedResources?")) {
				topLevelComponents.add(buildRelatedResources());	
			}
		}
		
		println("\nddms:security is required.");
		topLevelComponents.add(buildSecurity());

		println("\nYou are now done with top-level components. All that remains is the Resource attributes.");
		while (true) {
			boolean resourceElement = confirm("Does this tag set the classification for the resource as a whole?");
			String createDate = readString("Resource createDate [2010-03-24]");
			int desVersion = readInt("the Resource DESVersion [2]");
			String classification = readString("the Resource classification [U]");
			String ownerProducers = readString("the Resource's ownerProducers as a space-delimited string [USA AUS]");
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				resource = new Resource(topLevelComponents, resourceElement, createDate, new Integer(desVersion), attr);
				println("The DDMS Resource is valid!");
				break;
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
		
		println("\nThis Resource will be saved as XML in the " + SAMPLE_DIR + " directory.");
		while (true) {
			String filename = readString("a filename");
			if (filename.length() > 0) {
				saveFile(filename, resource);
				break;
			}
		}
		println("\nYou can now open your saved file with the Essentials application.");
	}
	
	/**
	 * Saves the valid resource to a file.
	 * 
	 * @param filename the filename
	 * @throws IOException
	 */
	private static void saveFile(String filename, Resource resource) throws IOException {
		BufferedWriter writer = null;
		try {
			File outputFile = new File(SAMPLE_DIR, filename);
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(resource.toXML());
			println("File saved at " + outputFile.getAbsolutePath() + " .");
		}
		finally {
			if (writer != null)
				writer.close();
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildIdentifier() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			try {
				println("Validating...");
				Identifier component = new Identifier(qualifier, value);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildTitle() throws IOException {
		while (true) {
			String text = readString("the title text [testTitle]");
			String classification = readString("the title classification [U]");
			String ownerProducers = readString("the title's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				Title component = new Title(text, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildSubtitle() throws IOException {
		while (true) {
			String text = readString("the subtitle text [testTitle]");
			String classification = readString("the subtitle classification [U]");
			String ownerProducers = readString("the subtitle's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				Subtitle component = new Subtitle(text, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildDescription() throws IOException {
		while (true) {
			String text = readString("the description text [testTitle]");
			String classification = readString("the description classification [U]");
			String ownerProducers = readString("the description's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				Description component = new Description(text, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildLanguage() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			try {
				println("Validating...");
				Language component = new Language(qualifier, value);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildDates() throws IOException {
		while (true) {
			String created = readString("the created date [2010]");
			String posted = readString("the posted date [2010]");
			String validTil = readString("the validTil date [2010]");
			String infoCutOff = readString("the infoCutOff date [2010]");
			try {
				println("Validating...");
				Dates component = new Dates(created, posted, validTil, infoCutOff);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildRights() throws IOException {
		while (true) {
			boolean privacy = confirm("Does this resource contain information protected by the Privacy Act?");
			boolean intellectual = confirm("Does this resource have an intellectual property rights owner?");
			boolean copyright = confirm("Is this resource protected by copyright?");
			try {
				println("Validating...");
				Rights component = new Rights(privacy, intellectual, copyright);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildSource() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			String schemaQualifier = readString("the schema qualifier [WSDL]");
			String schemHref = readString("the schema href [testValue]");
			String classification = readString("the source classification [U]");
			String ownerProducers = readString("the source's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				Source component = new Source(qualifier, value, schemaQualifier, schemHref, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildType() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			try {
				println("Validating...");
				Type component = new Type(qualifier, value);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildProducer() throws IOException {
		println("Sample Limitation: This wizard only allows one name, phone number, and email per producer.");
		while (true) {
			String surname = null;
			String userID = null;
			String affiliation = null;
			String producerType = readString("the producer type [creator]");
			String entityType = readString("the entity type [Organization]");
			String entityName = readString("the entity name [John]");
			String entityPhone = readString("the entity phone [703-885-1000]");
			String entityEmail = readString("the entity email [ddms@fgm.com]");
			// Skipping the multiple names/phones/emails allowed in a record.
			String classification = readString("the producer classification [U]");
			String ownerProducers = readString("the producer's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			if (Person.NAME.equals(entityType)) {
				surname = readString("the Person surname [Smith]");
				userID = readString("the Person userID [123]");
				affiliation = readString("the Person affiliation [DISA]");
			}
			try {
				println("Validating...");
				List<String> names = new ArrayList<String>();
				names.add(entityName);
				List<String> phones = new ArrayList<String>();
				phones.add(entityPhone);
				List<String> emails = new ArrayList<String>();
				emails.add(entityEmail);
				
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				if (Person.NAME.equals(entityType)) {
					Person person = new Person(producerType, surname, names, userID, affiliation, phones, emails, attr);
					println("This component is valid.");
					return (person);
				}
				else if (Organization.NAME.equals(entityType)) {
					Organization org = new Organization(producerType, names, phones, emails, attr);
					println("This component is valid.");
					return (org);
				}
				else if (Service.NAME.equals(entityType)) {
					Service service = new Service(producerType, names, phones, emails, attr);
					println("This component is valid.");
					return (service);
				}
				else if (Unknown.NAME.equals(entityType)) {
					Unknown unknown = new Unknown(producerType, names, phones, emails, attr);
					println("This component is valid.");
					return (unknown);
				}
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildFormat() throws IOException {
		while (true) {
			String mimeType = readString("the mimeType [text/html]");
			String qualifier = readString("the extent qualifier [URI]");
			String value = readString("the extent value [testValue]");
			String medium = readString("the mimeType [digital]");
			try {
				println("Validating...");
				MediaExtent extent = new MediaExtent(qualifier, value);
				Format component = new Format(mimeType, extent, medium);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildSubjectCoverage() throws IOException {
		println("Sample Limitation: This wizard only supports keywords, not categories.");
		while (true) {
			String keywords = readString("the keywords as a space-delimited string [ddms xml]");
			// Skipping categories
			String classification = readString("the subject classification [U]");
			String ownerProducers = readString("the subject's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				println("Validating...");
				List<Keyword> keywordList = new ArrayList<Keyword>();
				for (String keyword : Arrays.asList(keywords.split(" "))) {
					keywordList.add(new Keyword(keyword));
				}
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				SubjectCoverage component = new SubjectCoverage(keywordList, null, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}

	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildTemporalCoverage() throws IOException {
		while (true) {
			String timePeriodName = readString("the time period name [the 50s]");
			String start = readString("the start date [1950]");
			String end = readString("the end date [1959]");
			String classification = readString("the temporalCoverage classification [U]");
			String ownerProducers = readString("the temporalCoverage's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				TemporalCoverage component = new TemporalCoverage(timePeriodName, start, end, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildVirtualCoverage() throws IOException {
		while (true) {
			String address = readString("the address [123.456.789.0]");
			String protocol = readString("the protocol [IP]");
			String classification = readString("the virtualCoverage classification [U]");
			String ownerProducers = readString("the virtualCoverage's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				VirtualCoverage component = new VirtualCoverage(address, protocol, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildGeospatialCoverage() throws IOException {
		println("Sample Limitation: This wizard only supports geospatialCoverage defined with a facilityIdentifier.");
		while (true) {
			String beNumber = readString("the beNumber [1234DD56789]");
			String osuffix = readString("the osuffix [DD123]");
			String classification = readString("the geospatialCoverage classification [U]");
			String ownerProducers = readString("the geospatialCoverage's ownerProducers as a space-delimited string [USA AUS]");	
			try {
				println("Validating...");
				FacilityIdentifier facId = new FacilityIdentifier(beNumber, osuffix);
				GeographicIdentifier geoId = new GeographicIdentifier(facId);
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				GeospatialCoverage component = new GeospatialCoverage(geoId, null, null, null, null, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildRelatedResources() throws IOException {
		println("Sample Limitation: This wizard only supports a relatedResources element containing a single related resource. That resource contains a single link.");
		
		while (true) {
			// Only allowing 1 link in a RelatedResource
			// Only allowing 1 RelatedResource in a RelatedResources
			String href = readString("the link href [http://ddmsence.urizone.net/]");
			String role = readString("the link role [testValue]");
			String title = readString("the link title [testValue]");
			String label = readString("the link label [testValue]");
			String qualifier = readString("the RelatedResource qualifier [URI]");
			String value = readString("the RelatedResource value [testValue]");
			
			String relationship = readString("the relationship [URI]");
			String direction = readString("the direction [outbound]");
			String classification = readString("the relatedResources classification [U]");
			String ownerProducers = readString("the relatedResources ownerProducers as a space-delimited string [USA AUS]");	
			try {
				println("Validating...");
				Link link = new Link(href, role, title, label);
				List<Link> links = new ArrayList<Link>();
				links.add(link);
				RelatedResource related = new RelatedResource(links, qualifier, value);
				List<RelatedResource> resources = new ArrayList<RelatedResource>();
				resources.add(related);
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				RelatedResources component = new RelatedResources(resources, relationship, direction, attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private static IDDMSComponent buildSecurity() throws IOException {
		while (true) {
			String classification = readString("the classification [U]");
			String ownerProducers = readString("the ownerProducers as a space-delimited string [USA AUS]");	
			try {
				println("Validating...");
				SecurityAttributes attr = new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null);
				Security component = new Security(attr);
				println("This component is valid.");
				return (component);
			}
			catch (InvalidDDMSException e) {
				println(e.getMessage());
			}
		}
	}
	
	/**
	 * Accepts Y/N from the user.
	 * 
	 * @param text the question to ask
	 * @return a boolean
	 * @throws IOException
	 */
	private static boolean confirm(String text) throws IOException {
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
	private static int readInt(String name) throws IOException {
		while (true) {
			String input = readString(name);
			try {
				return Integer.parseInt(input);
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
	private static String readString(String name) throws IOException {
		while (true) {
			print("Please enter " + name + ": ");
			System.out.flush();
			String result = INPUT_STREAM.readLine();
			result = result.trim();
			return (result);				
		}
	}

	/**
	 * Convenience method to print a string.
	 * 
	 * @param str the string to print.
	 */
	private static void print(String str) {
		System.out.print(str);
	}

	/**
	 * Convenience method to print a string followed by a newline.
	 * 
	 * @param str the string to print.
	 */
	private static void println(String str) {
		System.out.println(str);
	}
}
