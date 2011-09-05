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
package buri.ddmsence.ddms.resource;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.IProducerEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:publisher elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class PublisherTest extends AbstractComponentTestCase {

	private static final String TEST_POC_TYPE = "ICD-710";

	/**
	 * Constructor
	 */
	public PublisherTest() {
		super("publisher.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Publisher testConstructor(boolean expectFailure, Element element) {
		Publisher component = null;
		try {
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = new Publisher(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper methdo to insert a POCType when testing 4.0.
	 */
	private String getPOCTypeFixture() {
		return (isDDMS40OrGreater() ? TEST_POC_TYPE : "");
	}

	/**
	 * Helper method to create a fixture organization to act as an entity
	 */
	private IProducerEntity getEntityFixture() {
		try {
			return (new Service(Publisher.getName(DDMSVersion.getCurrentVersion()),
				Util.getXsListAsList("https://metadata.dod.mil/ebxmlquery/soap"), Util.getXsListAsList("703-882-1000"),
				Util.getXsListAsList("ddms@fgm.com")));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param entity the producer entity
	 * @param pocType the POCType (DDMS 4.0 or later)
	 */
	private Publisher testConstructor(boolean expectFailure, IProducerEntity entity, String pocType) {
		Publisher component = null;
		try {
			component = new Publisher(entity, pocType, SecurityAttributesTest.getFixture(false));
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
		html.append(getEntityFixture().toHTML());
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		if (isDDMS40OrGreater()) {
			html.append("<meta name=\"").append(Publisher.getName(version))
				.append(".POCType\" content=\"ICD-710\" />\n");
		}
		html.append("<meta name=\"").append(Publisher.getName(version)).append(".classification\" content=\"U\" />\n");
		html.append("<meta name=\"").append(Publisher.getName(version)).append(".ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append(getEntityFixture().toText());
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		if (isDDMS40OrGreater()) {
			text.append("POCType: ICD-710\n");
		}
		text.append(Publisher.getName(version)).append(" classification: U\n");
		text.append(Publisher.getName(version)).append(" ownerProducer: USA\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		xml.append("<ddms:publisher xmlns:ddms=\"").append(version.getNamespace()).append("\" ");
		xml.append("xmlns:ICISM=\"").append(version.getIsmNamespace()).append("\"");
		if (isDDMS40OrGreater()) {
			xml.append(" ddms:POCType=\"ICD-710\"");
		}
		xml.append(" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">\n\t<ddms:").append(Service.getName(version)).append(">\n");
		xml.append("\t\t<ddms:name>https://metadata.dod.mil/ebxmlquery/soap</ddms:name>\n");
		xml.append("\t\t<ddms:phone>703-882-1000</ddms:phone>\n");
		xml.append("\t\t<ddms:email>ddms@fgm.com</ddms:email>\n");
		xml.append("\t</ddms:").append(Service.getName(version)).append(">\n</ddms:publisher>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Publisher component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Publisher.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Publisher.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// No optional fields
			Element element = Util.buildDDMSElement(Publisher.getName(version), null);
			element.appendChild(getEntityFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// All fields
			testConstructor(WILL_SUCCEED, getEntityFixture(), null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			// Missing entity
			Element element = Util.buildDDMSElement(Publisher.getName(version), null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// Missing entity		
			testConstructor(WILL_FAIL, (IProducerEntity) null, null);
		}
	}

	public void testWarnings() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			// No warnings
			Publisher component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Publisher elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Publisher dataComponent = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Publisher elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Publisher dataComponent = testConstructor(
				WILL_SUCCEED,
				new Service(Publisher.getName(version), Util.getXsListAsList("DISA PEO-GES"), Util
					.getXsListAsList("703-882-1000 703-885-1000"), Util.getXsListAsList("ddms@fgm.com")), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Publisher elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Publisher component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Publisher component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Publisher component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Publisher component = new Publisher(getEntityFixture(), null, SecurityAttributesTest.getFixture(false));
			assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());
		}
	}

	public void testPOCTypeWrongVersion() {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Publisher(getEntityFixture(), TEST_POC_TYPE, SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Publisher component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Publisher.Builder builder = new Publisher.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Publisher.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Publisher.Builder();
			builder.setEntityType(Person.getName(version));
			builder.getOrganization().setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
