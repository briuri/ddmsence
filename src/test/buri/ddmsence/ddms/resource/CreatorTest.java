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
 * <p>Tests related to ddms:creator elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class CreatorTest extends AbstractComponentTestCase {

	private static final String TEST_POC_TYPE = "ICD-710";

	/**
	 * Constructor
	 */
	public CreatorTest() {
		super("creator.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Creator testConstructor(boolean expectFailure, Element element) {
		Creator component = null;
		try {
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = new Creator(element);
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
			return (new Person(Creator.getName(DDMSVersion.getCurrentVersion()), "Uri",
				Util.getXsListAsList("Brian BU"), "123", "DISA", Util.getXsListAsList("703-885-1000"),
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
	private Creator testConstructor(boolean expectFailure, IProducerEntity entity, String pocType) {
		Creator component = null;
		try {
			component = new Creator(entity, pocType, SecurityAttributesTest.getFixture(false));
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
		String creatorName = Creator.getName(DDMSVersion.getCurrentVersion());
		html.append(getEntityFixture().toHTML());
		if (isDDMS40OrGreater()) {
			html.append("<meta name=\"").append(creatorName).append(".POCType\" content=\"ICD-710\" />\n");
		}
		html.append("<meta name=\"").append(creatorName).append(".classification\" content=\"U\" />\n");
		html.append("<meta name=\"").append(creatorName).append(".ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		String creatorName = Creator.getName(DDMSVersion.getCurrentVersion());
		text.append(getEntityFixture().toText());
		if (isDDMS40OrGreater()) {
			text.append("POCType: ICD-710\n");
		}
		text.append(creatorName).append(" classification: U\n");
		text.append(creatorName).append(" ownerProducer: USA\n");
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
		xml.append("<ddms:creator xmlns:ddms=\"").append(version.getNamespace()).append("\" ");
		xml.append("xmlns:ISM=\"").append(version.getIsmNamespace()).append("\"");
		if (isDDMS40OrGreater()) {
			xml.append(" ddms:POCType=\"ICD-710\"");
		}
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n\t<ddms:")
			.append(Person.getName(version)).append(">\n");
		xml.append("\t\t<ddms:name>Brian</ddms:name>\n");
		xml.append("\t\t<ddms:name>BU</ddms:name>\n");
		xml.append("\t\t<ddms:surname>Uri</ddms:surname>\n");
		xml.append("\t\t<ddms:userID>123</ddms:userID>\n");
		xml.append("\t\t<ddms:affiliation>DISA</ddms:affiliation>\n");
		xml.append("\t\t<ddms:phone>703-885-1000</ddms:phone>\n");
		xml.append("\t\t<ddms:email>ddms@fgm.com</ddms:email>\n");
		xml.append("\t</ddms:").append(Person.getName(version)).append(">\n</ddms:creator>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Creator component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Creator.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Creator.getName(version),
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
			Element element = Util.buildDDMSElement(Creator.getName(version), null);
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
			Element element = Util.buildDDMSElement(Creator.getName(version), null);
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
			Creator component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Creator elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Creator dataComponent = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Creator elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Creator dataComponent = testConstructor(
				WILL_SUCCEED,
				new Service(Creator.getName(version), Util.getXsListAsList("DISA PEO-GES"), Util
					.getXsListAsList("703-882-1000 703-885-1000"), Util.getXsListAsList("ddms@fgm.com")), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Creator elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Creator component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Creator component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Creator component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), getPOCTypeFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Creator component = new Creator(getEntityFixture(), null, SecurityAttributesTest.getFixture(false));
			assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());
		}
	}

	public void testPOCTypeWrongVersion() {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new Creator(getEntityFixture(), TEST_POC_TYPE, SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			Creator component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Creator.Builder builder = new Creator.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Creator.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new Creator.Builder();
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
