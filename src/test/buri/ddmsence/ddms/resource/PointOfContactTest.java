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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.IRoleEntity;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.RoleEntityTest;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:pointOfContact elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class PointOfContactTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public PointOfContactTest() {
		super("pointOfContact.xml");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private PointOfContact testConstructor(boolean expectFailure, Element element) {
		PointOfContact component = null;
		try {
			SecurityAttributesTest.getFixture(false).addTo(element);
			component = new PointOfContact(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Helper method to create a fixture organization to act as an entity
	 */
	private IRoleEntity getEntityFixture() {
		try {
			if ("2.0".equals(DDMSVersion.getCurrentVersion().getVersion()))
				return (new Service(Util.getXsListAsList("https://metadata.dod.mil/ebxmlquery/soap"),
					Util.getXsListAsList("703-882-1000"), Util.getXsListAsList("ddms@fgm.com")));
			return (new Unknown(Util.getXsListAsList("UnknownEntity"), Util.getXsListAsList("703-882-1000"),
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
	private PointOfContact testConstructor(boolean expectFailure, IRoleEntity entity, String pocType) {
		PointOfContact component = null;
		try {
			component = new PointOfContact(entity, pocType, SecurityAttributesTest.getFixture(false));
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
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		StringBuffer text = new StringBuffer();
		text.append(getEntityFixture().getOutput(isHTML, "pointOfContact."));
		if (version.isAtLeast("4.0"))
			text.append(buildOutput(isHTML, "pointOfContact.POCType", "ICD-710"));
		text.append(buildOutput(isHTML, "pointOfContact.classification", "U"));
		text.append(buildOutput(isHTML, "pointOfContact.ownerProducer", "USA"));
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
		xml.append("<ddms:pointOfContact ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM());
		if (version.isAtLeast("4.0"))
			xml.append(" ddms:POCType=\"ICD-710\"");
		xml.append(" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n\t");
		if ("2.0".equals(version.getVersion())) {
			xml.append("<ddms:").append(Service.getName(version)).append(">\n");
			xml.append("\t\t<ddms:name>https://metadata.dod.mil/ebxmlquery/soap</ddms:name>\n");
			xml.append("\t\t<ddms:phone>703-882-1000</ddms:phone>\n");
			xml.append("\t\t<ddms:email>ddms@fgm.com</ddms:email>\n");
			xml.append("\t</ddms:").append(Service.getName(version)).append(">");
		} else {
			xml.append("<ddms:").append(Unknown.getName(version)).append(">\n");
			xml.append("\t\t<ddms:name>UnknownEntity</ddms:name>\n");
			xml.append("\t\t<ddms:phone>703-882-1000</ddms:phone>\n");
			xml.append("\t\t<ddms:email>ddms@fgm.com</ddms:email>\n");
			xml.append("\t</ddms:").append(Unknown.getName(version)).append(">");
		}
		xml.append("\n</ddms:pointOfContact>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				PointOfContact.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(PointOfContact.getName(version), null);
			element.appendChild(getEntityFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			testConstructor(WILL_SUCCEED, getEntityFixture(), null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing entity
			Element element = Util.buildDDMSElement(PointOfContact.getName(version), null);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Missing entity		
			testConstructor(WILL_FAIL, (IRoleEntity) null, null);
		}
	}

	public void testWarnings() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			PointOfContact component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PointOfContact elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			PointOfContact dataComponent = testConstructor(WILL_SUCCEED, getEntityFixture(),
				RoleEntityTest.getPOCType());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PointOfContact elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			PointOfContact dataComponent = testConstructor(WILL_SUCCEED,
				new Service(Util.getXsListAsList("DISA PEO-GES"), Util.getXsListAsList("703-882-1000 703-885-1000"),
					Util.getXsListAsList("ddms@fgm.com")), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PointOfContact component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), RoleEntityTest.getPOCType());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PointOfContact component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getEntityFixture(), RoleEntityTest.getPOCType());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testSecurityAttributes() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			PointOfContact component = new PointOfContact(getEntityFixture(), null,
				SecurityAttributesTest.getFixture(false));
			assertEquals(SecurityAttributesTest.getFixture(false), component.getSecurityAttributes());
		}
	}

	public void testPOCTypeWrongVersion() {
		DDMSVersion.setCurrentVersion("3.1");
		try {
			new PointOfContact(getEntityFixture(), "ICD-710", SecurityAttributesTest.getFixture(false));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			PointOfContact component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			PointOfContact.Builder builder = new PointOfContact.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new PointOfContact.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new PointOfContact.Builder();
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
