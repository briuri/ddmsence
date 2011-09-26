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
import java.util.List;
import java.util.Map;

import nu.xom.Comment;
import nu.xom.Document;
import nu.xom.Serializer;
import buri.ddmsence.AbstractProducerRole;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.metacard.MetacardInfo;
import buri.ddmsence.ddms.resource.Contributor;
import buri.ddmsence.ddms.resource.Creator;
import buri.ddmsence.ddms.resource.Dates;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.PointOfContact;
import buri.ddmsence.ddms.resource.Publisher;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.SubOrganization;
import buri.ddmsence.ddms.resource.Title;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.summary.Category;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.ddms.summary.NonStateActor;
import buri.ddmsence.ddms.summary.ProductionMetric;
import buri.ddmsence.ddms.summary.SubjectCoverage;
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
 * <p>Currently, the wizard only walks through a minimally representative set of DDMS components. DDMS 4.0 introduces a lot of
 * depth and reuse (especially related to the new ddms:metacardInfo element) which gets very confusing when using
 * text prompts.</p>
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
		BUILDERS.put(MetacardInfo.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				DDMSVersion version = DDMSVersion.getCurrentVersion();
				List<IDDMSComponent> components = new ArrayList<IDDMSComponent>();
				println("A minimal metacardInfo consist of an identifier, dates, and a publisher.");
				components.add((Identifier) inputLoop(Identifier.class));
				components.add((Dates) inputLoop(Dates.class));

				String entityType = readString("the publisher entity type [organization]");
				IRoleEntity entity = null;
				if (Person.getName(version).equals(entityType))
					entity = (Person) inputLoop(Person.class);
				else if (Organization.getName(version).equals(entityType))
					entity = (Organization) inputLoop(Organization.class);
				else if (Service.getName(version).equals(entityType))
					entity = (Service) inputLoop(Service.class);
				else if (Unknown.getName(version).equals(entityType)) 
					entity = (Unknown) inputLoop(Unknown.class);
				components.add(new Publisher(entity, null, buildSecurityAttributes("publisher")));
				return (new MetacardInfo(components, buildSecurityAttributes("subject")));
			}		
		});
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
		BUILDERS.put(Description.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				String text = readString("the description text [testDescription]");
				SecurityAttributes attr = buildSecurityAttributes("description");
				return (new Description(text, attr));
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
		BUILDERS.put(Security.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				return (new Security(null, null, buildSecurityAttributes("security element")));
			}		
		});
		BUILDERS.put(Resource.class, new IComponentBuilder() {
			public IDDMSComponent build() throws IOException, InvalidDDMSException {
				boolean resourceElement = confirm("Does this tag set the classification for the resource as a whole?");
				String createDate = readString("Resource createDate [2010-04-01]");
				int ismDESVersion = readInt("the Resource ISM:DESVersion [7]");
				int ntkDESVersion = readInt("the Resource ntk:DESVersion [5]");
				return (new Resource(getTopLevelComponents(), resourceElement, createDate, null, new Integer(ismDESVersion), new Integer(ntkDESVersion), 
					buildSecurityAttributes("resource"), null, null));
			}		
		});
	}
	
	/**
	 * The main execution loop of the program
	 */
	private void run() throws IOException {
		println("Escort: a DDMSence Sample\n");
		
		println("This program allows you to build a DDMS 4.0 resource from scratch using a");
		println("representative subset of possible components. Suggested valid examples are");
		println("provided in square brackets for each prompt. However, this is not a default");
		println("value (hitting Enter will answer the prompt with an empty string).\n");

		_useDummySecurityAttributes = confirm("Would you like to save time by using dummy security attributes, Unclassified/USA, throughout the resource?");
				
		DDMSVersion.setCurrentVersion("4.0");
		
		printHead("ddms:metacardInfo (exactly 1 required)");
		getTopLevelComponents().add(inputLoop(MetacardInfo.class));
		
		printHead("ddms:identifier (at least 1 required)");
		getTopLevelComponents().add(inputLoop(Identifier.class));
		
		printHead("ddms:title (at least 1 required)");
		getTopLevelComponents().add(inputLoop(Title.class));
			
		printHead("ddms:description (only 1 allowed)");
		if (confirm("Include this component?")) {
			getTopLevelComponents().add(inputLoop(Description.class));	
		}
		
		printHead("ddms:dates (only 1 allowed)");
		if (confirm("Include this component?")) {
			getTopLevelComponents().add(inputLoop(Dates.class));	
		}
		
		printHead("Producers: creator, publisher, contributor, and pointOfContact (at least 1 required)");
		getTopLevelComponents().add(inputLoop(AbstractProducerRole.class));	
		
		printHead("ddms:subjectCoverage (at least 1 required)");
		getTopLevelComponents().add(inputLoop(SubjectCoverage.class));
				
		printHead("ddms:security (exactly 1 required)");
		getTopLevelComponents().add(inputLoop(Security.class));
		
		printHead("ddms:resource Attributes (all required)");
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