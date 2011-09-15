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
package buri.ddmsence.ddms.security.ntk;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ntk:AccessIndividual elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class IndividualTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public IndividualTest() {
		super("accessIndividual.xml");
	}

	/**
	 * Helper method to create a fixture
	 */
	private static SystemName getSystemNameFixture() {
		try {
			return (new SystemName("DIAS", null, null, null, SecurityAttributesTest.getFixture(false)));
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static List<IndividualValue> getIndividualValueFixture() {
		List<IndividualValue> list = new ArrayList<IndividualValue>();
		try {
			list.add(new IndividualValue("user_2321889:Doe_John_H", null, null, null, SecurityAttributesTest
				.getFixture(false)));
			return (list);
		} catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Individual testConstructor(boolean expectFailure, Element element) {
		Individual component = null;
		try {
			component = new Individual(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param systemName the system (required)
	 * @param values the values (1 required)
	 */
	private Individual testConstructor(boolean expectFailure, SystemName systemName, List<IndividualValue> values) {
		Individual component = null;
		try {
			component = new Individual(systemName, values, SecurityAttributesTest.getFixture(false));
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(getSystemNameFixture().getOutput(isHTML, "individual."));
		text.append(getIndividualValueFixture().get(0).getOutput(isHTML, "individual."));
		text.append(buildOutput(isHTML, "individual.classification", "U"));
		text.append(buildOutput(isHTML, "individual.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ntk:AccessIndividual ").append(getXmlnsNTK()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml.append("\t<ntk:AccessIndividualValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\">user_2321889:Doe_John_H</ntk:AccessIndividualValue>\n");
		xml.append("</ntk:AccessIndividual>\n");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(Individual.getName(version), component.getName());
			assertEquals(PropertyReader.getProperty("ntk.prefix"), component.getPrefix());
			assertEquals(PropertyReader.getProperty("ntk.prefix") + ":" + Individual.getName(version),
				component.getQualifiedName());

			// Wrong name/namespace
			Element element = Util.buildDDMSElement("wrongName", null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(versionString));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// All fields
			testConstructor(WILL_SUCCEED, getSystemNameFixture(), getIndividualValueFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);
			String ntkPrefix = PropertyReader.getProperty("ntk.prefix");

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing systemName
			Element element = Util
				.buildElement(ntkPrefix, Individual.getName(version), version.getNtkNamespace(), null);
			for (IndividualValue value : getIndividualValueFixture())
				element.appendChild(value.getXOMElementCopy());
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing individualValue
			element = Util.buildElement(ntkPrefix, Individual.getName(version), version.getNtkNamespace(), null);
			element.appendChild(getSystemNameFixture().getXOMElementCopy());
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing security attributes
			element = Util.buildElement(ntkPrefix, Individual.getName(version), version.getNtkNamespace(), null);
			element.appendChild(getSystemNameFixture().getXOMElementCopy());
			for (IndividualValue value : getIndividualValueFixture())
				element.appendChild(value.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// Missing systemName
			testConstructor(WILL_FAIL, null, getIndividualValueFixture());

			// Missing individualValue
			testConstructor(WILL_FAIL, getSystemNameFixture(), null);

			// Missing security attributes
			try {
				new Individual(getSystemNameFixture(), getIndividualValueFixture(), null);
				fail("Allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			// No warnings
			Individual component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Individual dataComponent = testConstructor(WILL_SUCCEED, getSystemNameFixture(),
				getIndividualValueFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Individual dataComponent = testConstructor(WILL_SUCCEED, new SystemName("MDR", null, null, null,
				SecurityAttributesTest.getFixture(false)), getIndividualValueFixture());
			assertFalse(elementComponent.equals(dataComponent));

			List<IndividualValue> list = new ArrayList<IndividualValue>();
			list.add(new IndividualValue("newUser", null, null, null, SecurityAttributesTest.getFixture(false)));
			dataComponent = testConstructor(WILL_SUCCEED, getSystemNameFixture(), list);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual elementComponent = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(true), component.toHTML());

			component = testConstructor(WILL_SUCCEED, getSystemNameFixture(), getIndividualValueFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
		}
	}

	public void testTextOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getSystemNameFixture(), getIndividualValueFixture());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual component = testConstructor(WILL_SUCCEED, getValidElement(versionString));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = testConstructor(WILL_SUCCEED, getSystemNameFixture(), getIndividualValueFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(versionString);

			if (!version.isAtLeast("4.0"))
				continue;

			Individual component = testConstructor(WILL_SUCCEED, getValidElement(versionString));

			// Equality after Building
			Individual.Builder builder = new Individual.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Individual.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getIndividualValues().get(0);
			assertTrue(builder.isEmpty());
			builder.getIndividualValues().get(1).setValue("TEST");
			assertFalse(builder.isEmpty());

			// Validation
			builder = new Individual.Builder();
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.getSystemName().setValue("value");
			builder.getSystemName().getSecurityAttributes().setClassification("U");
			builder.getSystemName().getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));

			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			} catch (InvalidDDMSException e) {
				// Good
			}

			// Skip empty builders

		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String versionString : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(versionString);
			Individual.Builder builder = new Individual.Builder();
			assertNotNull(builder.getIndividualValues().get(1));
		}
	}
}
