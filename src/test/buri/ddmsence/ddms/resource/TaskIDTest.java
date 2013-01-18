/* Copyright 2010 - 2013 by Brian Uri!
   
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
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:taskID elements </p>
 * 
 * <p> Because a ddms:taskID is a local component, we cannot load a valid document from a unit test data file. We have
 * to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class TaskIDTest extends AbstractBaseTestCase {

	private static final String TEST_TASKING_SYSTEM = "MDR";
	private static final String TEST_VALUE = "Task #12345";
	private static final String TEST_NETWORK = "NIPRNet";
	private static final String TEST_OTHER_NETWORK = "PBS";

	/**
	 * Constructor
	 */
	public TaskIDTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Element getFixtureElement() {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();

			Element element = Util.buildDDMSElement(TaskID.getName(version), TEST_VALUE);
			element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
			element.addNamespaceDeclaration(PropertyReader.getPrefix("xlink"), version.getXlinkNamespace());
			Util.addDDMSAttribute(element, "taskingSystem", TEST_TASKING_SYSTEM);
			Util.addAttribute(element, "", "network", "", TEST_NETWORK);
			Util.addAttribute(element, "", "otherNetwork", "", TEST_OTHER_NETWORK);
			XLinkAttributesTest.getSimpleFixture().addTo(element);
			return (element);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static TaskID getFixture() {
		try {
			return (new TaskID(TaskIDTest.getFixtureElement()));
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private TaskID getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		TaskID component = null;
		try {
			component = new TaskID(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param value the child text (optional) link href (required)
	 * @param taskingSystem the tasking system (optional)
	 * @param network the network (optional)
	 * @param otherNetwork another network (optional)
	 * @param attributes the xlink attributes (optional)
	 */
	private TaskID getInstance(String message, String value, String taskingSystem, String network, String otherNetwork,
		XLinkAttributes attributes) {
		boolean expectFailure = !Util.isEmpty(message);
		TaskID component = null;
		try {
			component = new TaskID(value, taskingSystem, network, otherNetwork, attributes);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "taskID", TEST_VALUE));
		text.append(buildOutput(isHTML, "taskID.taskingSystem", TEST_TASKING_SYSTEM));
		text.append(buildOutput(isHTML, "taskID.network", TEST_NETWORK));
		text.append(buildOutput(isHTML, "taskID.otherNetwork", TEST_OTHER_NETWORK));
		text.append(XLinkAttributesTest.getSimpleFixture().getOutput(isHTML, "taskID."));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:taskID ").append(getXmlnsDDMS()).append(" ");
		xml.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\" ddms:taskingSystem=\"MDR\" ");
		xml.append("network=\"NIPRNet\" otherNetwork=\"PBS\" xlink:type=\"simple\" ");
		xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"tank\" xlink:title=\"Tank Page\" ");
		xml.append("xlink:arcrole=\"arcrole\" xlink:show=\"new\" xlink:actuate=\"onLoad\">Task #12345</ddms:taskID>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement()), DEFAULT_DDMS_PREFIX, TaskID
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getFixtureElement());

			// No optional fields
			Element element = Util.buildDDMSElement(TaskID.getName(version), TEST_VALUE);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, TEST_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest
				.getSimpleFixture());

			// No optional fields
			getInstance(SUCCESS, TEST_VALUE, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Wrong type of XLinkAttributes (locator)
			Element element = Util.buildDDMSElement(TaskID.getName(version), TEST_VALUE);
			XLinkAttributesTest.getLocatorFixture().addTo(element);
			getInstance("The type attribute must have a fixed value", element);

			// Missing value
			element = Util.buildDDMSElement(TaskID.getName(version), null);
			getInstance("value is required.", element);

			// Bad network
			element = Util.buildDDMSElement(TaskID.getName(version), TEST_VALUE);
			Util.addAttribute(element, "", "network", "", "PBS");
			getInstance("The network attribute must be one of", element);

		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Wrong type of XLinkAttributes
			getInstance("The type attribute must have a fixed value", TEST_VALUE, null, null, null, XLinkAttributesTest
				.getLocatorFixture());

			// Missing value
			getInstance("value is required.", null, null, null, null, null);

			// Bad network
			getInstance("The network attribute must be one of", TEST_VALUE, null, "PBS", null, null);
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			TaskID component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID elementComponent = getInstance(SUCCESS, getFixtureElement());
			TaskID dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getSimpleFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID elementComponent = getInstance(SUCCESS, getFixtureElement());
			TaskID dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getSimpleFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, DIFFERENT_VALUE, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getSimpleFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_TASKING_SYSTEM, "SIPRNet", TEST_OTHER_NETWORK,
				XLinkAttributesTest.getSimpleFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK, DIFFERENT_VALUE,
				XLinkAttributesTest.getSimpleFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK, TEST_OTHER_NETWORK,
				null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID elementComponent = getInstance(SUCCESS, getFixtureElement());
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getSimpleFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getSimpleFixture());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("4.0.1");
			XLinkAttributes attr = XLinkAttributesTest.getSimpleFixture();
			DDMSVersion.setCurrentVersion("2.0");
			new TaskID(TEST_VALUE, TEST_TASKING_SYSTEM, TEST_NETWORK, TEST_OTHER_NETWORK, attr);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "These attributes cannot decorate");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID component = getInstance(SUCCESS, getFixtureElement());
			TaskID.Builder builder = new TaskID.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID.Builder builder = new TaskID.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setValue(TEST_VALUE);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskID.Builder builder = new TaskID.Builder();
			builder.setTaskingSystem(TEST_TASKING_SYSTEM);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "value is required.");
			}
			builder.setValue(TEST_VALUE);
			builder.commit();
		}
	}
}
