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
import buri.ddmsence.ddms.ValidationMessage;
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
		println("If you do not know how to answer a question, a suggested valid answer is provided in square brackets.");
		println("However, this is not a default value (hitting Enter will answer the question with an empty string).\n");

		println("In FAST mode, Escort will only ask you to create top-level components which are required for a valid Resource.");
		println("In COMPLETE mode, Escort will let you create all of the top-level components.");
		boolean onlyRequiredComponents = confirm("Would you like to run in FAST mode?");
		List<IDDMSComponent> topLevelComponents = new ArrayList<IDDMSComponent>();
				
		printHead("ddms:identifier (at least 1 required)");
		topLevelComponents.add(buildIdentifier());
		while (!onlyRequiredComponents && confirm("Add another ddms:identifier?")) {
			topLevelComponents.add(buildIdentifier());	
		}
		
		printHead("ddms:title (at least 1 required)");
		topLevelComponents.add(buildTitle());
		while (!onlyRequiredComponents && confirm("Add another ddms:title?")) {
			topLevelComponents.add(buildTitle());	
		}
		
		if (!onlyRequiredComponents) {
			printHead("ddms:subtitle (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildSubtitle());	
				while (confirm("Add another ddms:subtitle?")) {
					topLevelComponents.add(buildSubtitle());	
				}
			}
			
			printHead("ddms:description (only 1 allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildDescription());	
			}
			
			printHead("ddms:language (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildLanguage());	
				while (confirm("Add another ddms:language?")) {
					topLevelComponents.add(buildLanguage());	
				}
			}
			
			printHead("ddms:dates (only 1 allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildDates());	
			}
			
			printHead("ddms:rights (only 1 allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildRights());	
			}

			printHead("ddms:source (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildSource());	
				while (confirm("Add another ddms:source?")) {
					topLevelComponents.add(buildSource());	
				}
			}
			
			printHead("ddms:type (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildType());	
				while (confirm("Add another ddms:type?")) {
					topLevelComponents.add(buildType());	
				}
			}
		}
				
		printHead("Producers: creator, publisher, contributor, and pointOfContact (at least 1 required)");
		topLevelComponents.add(buildProducer());	
		while (!onlyRequiredComponents && confirm("Add another producer?")) {
			topLevelComponents.add(buildProducer());	
		}
		
		if (!onlyRequiredComponents) {
			printHead("ddms:format (only 1 allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildFormat());	
			}
		}
		
		printHead("ddms:subjectCoverage (exactly 1 required)");
		topLevelComponents.add(buildSubjectCoverage());
		
		if (!onlyRequiredComponents) {
			printHead("ddms:virtualCoverage (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildVirtualCoverage());	
				while (confirm("Add another ddms:virtualCoverage?")) {
					topLevelComponents.add(buildVirtualCoverage());	
				}
			}
			
			printHead("ddms:temporalCoverage (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildTemporalCoverage());	
				while (confirm("Add another ddms:temporalCoverage?")) {
					topLevelComponents.add(buildTemporalCoverage());	
				}
			}
			
			printHead("ddms:geospatialCoverage (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildGeospatialCoverage());	
				while (confirm("Add another ddms:geospatialCoverage?")) {
					topLevelComponents.add(buildGeospatialCoverage());	
				}
			}
			
			printHead("ddms:relatedResources (any number allowed)");
			if (confirm("Include this component?")) {
				topLevelComponents.add(buildRelatedResources());	
				while (confirm("Add another ddms:relatedResources?")) {
					topLevelComponents.add(buildRelatedResources());	
				}
			}
		}
				
		printHead("ddms:security (exactly 1 required)");
		topLevelComponents.add(buildSecurity());
		
		printHead("ddms:Resource Attributes (all required)");
		Resource resource = buildResource(topLevelComponents);
		
		printHead("Saving the Resource");
		println("This Resource will be saved as XML in the " + SAMPLE_DIR + " directory.");
		while (true) {
			String filename = readString("a filename");
			if (filename.length() > 0) {
				saveFile(filename, resource);
				break;
			}
		}
		println("\nYou can now open your saved file with the Essentials application.");
		println("The Escort wizard is now finished.");
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
		return (new SecurityAttributes(classification, Arrays.asList(ownerProducers.split(" ")), null));
	}
	
	/**
	 * Saves the valid resource to a file.
	 * 
	 * @param filename the filename
	 * @throws IOException
	 */
	private void saveFile(String filename, Resource resource) throws IOException {
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
	private IDDMSComponent buildIdentifier() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			try {
				return (new Identifier(qualifier, value));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildTitle() throws IOException {
		while (true) {
			String text = readString("the title text [testTitle]");
			String classification = readString("the title classification [U]");
			String ownerProducers = readString("the title's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Title(text, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildSubtitle() throws IOException {
		while (true) {
			String text = readString("the subtitle text [testTitle]");
			String classification = readString("the subtitle classification [U]");
			String ownerProducers = readString("the subtitle's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Subtitle(text, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildDescription() throws IOException {
		while (true) {
			String text = readString("the description text [testTitle]");
			String classification = readString("the description classification [U]");
			String ownerProducers = readString("the description's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {				
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Description(text, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildLanguage() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			try {
				return (new Language(qualifier, value));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildDates() throws IOException {
		while (true) {
			String created = readString("the created date [2010]");
			String posted = readString("the posted date [2010]");
			String validTil = readString("the validTil date [2010]");
			String infoCutOff = readString("the infoCutOff date [2010]");
			try {
				return (new Dates(created, posted, validTil, infoCutOff));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildRights() throws IOException {
		while (true) {
			boolean privacy = confirm("Does this resource contain information protected by the Privacy Act?");
			boolean intellectual = confirm("Does this resource have an intellectual property rights owner?");
			boolean copyright = confirm("Is this resource protected by copyright?");
			try {
				return (new Rights(privacy, intellectual, copyright));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildSource() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			String schemaQualifier = readString("the schema qualifier [WSDL]");
			String schemHref = readString("the schema href [testValue]");
			String classification = readString("the source classification [U]");
			String ownerProducers = readString("the source's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Source(qualifier, value, schemaQualifier, schemHref, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildType() throws IOException {
		while (true) {
			String qualifier = readString("the qualifier [URI]");
			String value = readString("the value [testValue]");
			try {
				return (new Type(qualifier, value));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	

	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildProducer() throws IOException {
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
				List<String> names = new ArrayList<String>();
				names.add(entityName);
				List<String> phones = new ArrayList<String>();
				phones.add(entityPhone);
				List<String> emails = new ArrayList<String>();
				emails.add(entityEmail);
				
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				if (Person.NAME.equals(entityType)) {
					return (new Person(producerType, surname, names, userID, affiliation, phones, emails, attr));
				}
				else if (Organization.NAME.equals(entityType)) {
					return (new Organization(producerType, names, phones, emails, attr));
				}
				else if (Service.NAME.equals(entityType)) {
					return (new Service(producerType, names, phones, emails, attr));
				}
				else if (Unknown.NAME.equals(entityType)) {
					return (new Unknown(producerType, names, phones, emails, attr));
				}
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildFormat() throws IOException {
		while (true) {
			String mimeType = readString("the mimeType [text/html]");
			String qualifier = readString("the extent qualifier [URI]");
			String value = readString("the extent value [testValue]");
			String medium = readString("the mimeType [digital]");
			try {
				MediaExtent extent = new MediaExtent(qualifier, value);
				return (new Format(mimeType, extent, medium));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildSubjectCoverage() throws IOException {
		println("Sample Limitation: This wizard only supports keywords, not categories.");
		while (true) {
			String keywords = readString("the keywords as a space-delimited string [ddms xml]");
			// Skipping categories
			String classification = readString("the subject classification [U]");
			String ownerProducers = readString("the subject's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {				
				List<Keyword> keywordList = new ArrayList<Keyword>();
				for (String keyword : Arrays.asList(keywords.split(" "))) {
					keywordList.add(new Keyword(keyword));
				}
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new SubjectCoverage(keywordList, null, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}

	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildTemporalCoverage() throws IOException {
		while (true) {
			String timePeriodName = readString("the time period name [the 50s]");
			String start = readString("the start date [1950]");
			String end = readString("the end date [1959]");
			String classification = readString("the temporalCoverage classification [U]");
			String ownerProducers = readString("the temporalCoverage's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {				
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);			
				return (new TemporalCoverage(timePeriodName, start, end, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildVirtualCoverage() throws IOException {
		while (true) {
			String address = readString("the address [123.456.789.0]");
			String protocol = readString("the protocol [IP]");
			String classification = readString("the virtualCoverage classification [U]");
			String ownerProducers = readString("the virtualCoverage's ownerProducers as a space-delimited string [USA AUS]");
			// Skipping the "additional" security attributes.
			try {
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new VirtualCoverage(address, protocol, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildGeospatialCoverage() throws IOException {
		println("Sample Limitation: This wizard only supports geospatialCoverage defined with a facilityIdentifier.");
		while (true) {
			String beNumber = readString("the beNumber [1234DD56789]");
			String osuffix = readString("the osuffix [DD123]");
			String classification = readString("the geospatialCoverage classification [U]");
			String ownerProducers = readString("the geospatialCoverage's ownerProducers as a space-delimited string [USA AUS]");	
			try {				
				FacilityIdentifier facId = new FacilityIdentifier(beNumber, osuffix);
				GeographicIdentifier geoId = new GeographicIdentifier(facId);
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new GeospatialCoverage(geoId, null, null, null, null, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildRelatedResources() throws IOException {
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
				Link link = new Link(href, role, title, label);
				List<Link> links = new ArrayList<Link>();
				links.add(link);
				RelatedResource related = new RelatedResource(links, qualifier, value);
				List<RelatedResource> resources = new ArrayList<RelatedResource>();
				resources.add(related);
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new RelatedResources(resources, relationship, direction, attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid component
	 */
	private IDDMSComponent buildSecurity() throws IOException {
		while (true) {
			String classification = readString("the classification [U]");
			String ownerProducers = readString("the ownerProducers as a space-delimited string [USA AUS]");	
			try {				
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				return (new Security(attr));
			}
			catch (InvalidDDMSException e) {
				printError(e);
			}
		}
	}
	
	/**
	 * Builds a valid DDMS component
	 * 
	 * @return a valid Resource
	 */
	private Resource buildResource(List<IDDMSComponent> topLevelComponents) throws IOException {
		while (true) {
			boolean resourceElement = confirm("Does this tag set the classification for the resource as a whole?");
			String createDate = readString("Resource createDate [2010-03-24]");
			int desVersion = readInt("the Resource DESVersion [2]");
			String classification = readString("the Resource classification [U]");
			String ownerProducers = readString("the Resource's ownerProducers as a space-delimited string [USA AUS]");
			try {				
				SecurityAttributes attr = buildSecurityAttributes(classification, ownerProducers);
				Resource resource = new Resource(topLevelComponents, resourceElement, createDate, new Integer(desVersion), attr);
				println("The DDMS Resource is valid!");
				if (!resource.getValidationWarnings().isEmpty()) {
					println("The following warnings were recorded:");
					for (ValidationMessage warning : resource.getValidationWarnings())
						println("   [WARNING] " + warning.getLocator() + ": " + warning.getText());
				}
				else
					println("No warnings were recorded.");
				return (resource);
			}
			catch (InvalidDDMSException e) {
				printError(e);
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
	private void printError(InvalidDDMSException e) {
		println("[ERROR] " + e.getLocator() + ": " + e.getMessage());
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
}
