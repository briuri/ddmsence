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
 * <p>Tests related to ddms:contributor elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ContributorTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public ContributorTest() {
		super("contributor.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Contributor testConstructor(boolean expectFailure, Element element) {
		Contributor component = null;
		try {
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = new Contributor(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create a fixture organization to act as an entity
	 */
	private IProducerEntity getEntityFixture() {
		try {
			return (new Organization(Contributor.NAME, Util.getXsListAsList("DISA PEO-GES"),
				Util.getXsListAsList("703-882-1000 703-885-1000"), Util.getXsListAsList("ddms@fgm.com")));
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
	 */
	private Contributor testConstructor(boolean expectFailure, IProducerEntity entity) {
		Contributor component = null;
		try {
			component = new Contributor(entity, SecurityAttributesTest.getFixture(false));
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
		html.append("<meta name=\"").append(Contributor.NAME).append(".classification\" content=\"U\" />\n");
		html.append("<meta name=\"").append(Contributor.NAME).append(".ownerProducer\" content=\"USA\" />\n");
		return (html.toString());
	}

	/**
	 * Returns the expected Text output for this unit test
	 */
	private String getExpectedTextOutput() {
		StringBuffer text = new StringBuffer();
		text.append(getEntityFixture().toText());
		text.append(Contributor.NAME).append(" classification: U\n");
		text.append(Contributor.NAME).append(" ownerProducer: USA\n");
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:contributor xmlns:ddms=\"").append(
			DDMSVersion.getCurrentVersion().getNamespace()).append("\" ");
		xml.append("xmlns:ICISM=\"").append(DDMSVersion.getCurrentVersion().getIsmNamespace()).append(
			"\" ICISM:classification=\"U\" ICISM:ownerProducer=\"USA\">\n\t<ddms:Organization>\n");
		xml.append("\t\t<ddms:name>DISA</ddms:name>\n");
		xml.append("\t\t<ddms:name>PEO-GES</ddms:name>\n");
		xml.append("\t\t<ddms:phone>703-882-1000</ddms:phone>\n");
		xml.append("\t\t<ddms:phone>703-885-1000</ddms:phone>\n");
		xml.append("\t\t<ddms:email>ddms@fgm.com</ddms:email>\n");
		xml.append("\t</ddms:Organization>\n</ddms:contributor>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(Contributor.NAME, component.getName());
			assertEquals(PropertyReader.getProperty("ddms.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ddms.prefix") + ":" + Contributor.NAME, component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(version));

			// No optional fields
			Element element = Util.buildDDMSElement(Contributor.NAME, null);
			element.appendChild(getEntityFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, getEntityFixture());
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing entity
			Element element = Util.buildDDMSElement(Contributor.NAME, null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// Missing entity		
			testConstructor(WILL_FAIL, (IProducerEntity) null);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Contributor dataComponent = testConstructor(WILL_SUCCEED, getEntityFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Contributor dataComponent = testConstructor(WILL_SUCCEED, new Service(Contributor.NAME, Util.getXsListAsList("DISA PEO-GES"),
				Util.getXsListAsList("703-882-1000 703-885-1000"), Util.getXsListAsList("ddms@fgm.com")));
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor elementComponent = testConstructor(WILL_SUCCEED, getValidElement(version));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedHTMLOutput(), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getEntityFixture());
			assertEquals(getExpectedHTMLOutput(), component.toHTML());
		}
	}

	public void testTextOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedTextOutput(), component.toText());

			component = testConstructor(WILL_SUCCEED, getEntityFixture());
			assertEquals(getExpectedTextOutput(), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(version));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getEntityFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor component = new Contributor(getEntityFixture(), SecurityAttributesTest.getFixture(false));
			assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Contributor component = testConstructor(WILL_SUCCEED, getValidElement(version));
			
			// Equality after Building
			Contributor.Builder builder = new Contributor.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Empty case
			builder = new Contributor.Builder();
			assertNull(builder.commit());
			
			// Validation
			builder = new Contributor.Builder();
			builder.setEntityType(Organization.NAME);
			builder.getOrganization().setPhones(Util.getXsListAsList("703-885-1000"));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}
}
