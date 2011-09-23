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
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ntk:AccessGroup elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class GroupTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public GroupTest() {
		super("accessGroup.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Group getFixture() {
		try {
			return (new Group(SystemNameTest.getFixture(), GroupValueTest.getFixtureList(), SecurityAttributesTest
				.getFixture()));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static List<Group> getFixtureList() {
		List<Group> list = new ArrayList<Group>();
		list.add(GroupTest.getFixture());
		return (list);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private Group testConstructor(boolean expectFailure, Element element) {
		Group component = null;
		try {
			component = new Group(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
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
	private Group testConstructor(boolean expectFailure, SystemName systemName, List<GroupValue> values) {
		Group component = null;
		try {
			component = new Group(systemName, values, SecurityAttributesTest.getFixture());
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(SystemNameTest.getFixture().getOutput(isHTML, "group."));
		text.append(GroupValueTest.getFixtureList().get(0).getOutput(isHTML, "group."));
		text.append(buildOutput(isHTML, "group.classification", "U"));
		text.append(buildOutput(isHTML, "group.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ntk:AccessGroup ").append(getXmlnsNTK()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">\n");
		xml.append("\t<ntk:AccessSystemName ISM:classification=\"U\" ISM:ownerProducer=\"USA\">DIAS</ntk:AccessSystemName>\n");
		xml.append("\t<ntk:AccessGroupValue ISM:classification=\"U\" ISM:ownerProducer=\"USA\">WISE/RODCA</ntk:AccessGroupValue>\n");
		xml.append("</ntk:AccessGroup>\n");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_NTK_PREFIX, Group
				.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, SystemNameTest.getFixture(), GroupValueTest.getFixtureList());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			String ntkPrefix = PropertyReader.getPrefix("ntk");

			// Missing systemName
			Element element = Util.buildElement(ntkPrefix, Group.getName(version), version.getNtkNamespace(), null);
			for (GroupValue value : GroupValueTest.getFixtureList())
				element.appendChild(value.getXOMElementCopy());
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing groupValue
			element = Util.buildElement(ntkPrefix, Group.getName(version), version.getNtkNamespace(), null);
			element.appendChild(SystemNameTest.getFixture().getXOMElementCopy());
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing security attributes
			element = Util.buildElement(ntkPrefix, Group.getName(version), version.getNtkNamespace(), null);
			element.appendChild(SystemNameTest.getFixture().getXOMElementCopy());
			for (GroupValue value : GroupValueTest.getFixtureList())
				element.appendChild(value.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing systemName
			testConstructor(WILL_FAIL, null, GroupValueTest.getFixtureList());

			// Missing groupValue
			testConstructor(WILL_FAIL, SystemNameTest.getFixture(), null);

			// Missing security attributes
			try {
				new Group(SystemNameTest.getFixture(), GroupValueTest.getFixtureList(), null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			Group component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Group elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Group dataComponent = testConstructor(WILL_SUCCEED, SystemNameTest.getFixture(), GroupValueTest
				.getFixtureList());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Group elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Group dataComponent = testConstructor(WILL_SUCCEED, new SystemName("MDR", null, null, null,
				SecurityAttributesTest.getFixture()), GroupValueTest.getFixtureList());
			assertFalse(elementComponent.equals(dataComponent));

			List<GroupValue> list = new ArrayList<GroupValue>();
			list.add(new GroupValue("newUser", null, null, null, SecurityAttributesTest.getFixture()));
			dataComponent = testConstructor(WILL_SUCCEED, SystemNameTest.getFixture(), list);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Group component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, SystemNameTest.getFixture(), GroupValueTest.getFixtureList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Group component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = testConstructor(WILL_SUCCEED, SystemNameTest.getFixture(), GroupValueTest.getFixtureList());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			Group component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			Group.Builder builder = new Group.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new Group.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getGroupValues().get(0);
			assertTrue(builder.isEmpty());
			builder.getGroupValues().get(1).setValue("TEST");
			assertFalse(builder.isEmpty());

			// Validation
			builder = new Group.Builder();
			builder.getSecurityAttributes().setClassification("U");
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.getSystemName().setValue("value");
			builder.getSystemName().getSecurityAttributes().setClassification("U");
			builder.getSystemName().getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));

			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			Group.Builder builder = new Group.Builder();
			assertNotNull(builder.getGroupValues().get(1));
		}
	}
}
