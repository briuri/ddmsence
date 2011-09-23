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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:taskingInfo elements</p>
 * 
 * <p> Because a ddms:taskingInfo is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class TaskingInfoTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public TaskingInfoTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a canned fixed value for testing.
	 * 
	 * @return a XOM element representing a valid taskingInfo
	 */
	protected static Element getFixtureElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
		element.addNamespaceDeclaration(PropertyReader.getPrefix("ism"), version.getIsmNamespace());
		SecurityAttributesTest.getFixture(false).addTo(element);
		element.appendChild(RequesterInfoTest.getFixtureElement(true));
		element.appendChild(AddresseeTest.getFixtureElement(true));
		element.appendChild(getDescriptionFixture().getXOMElementCopy());
		element.appendChild(TaskIDTest.getFixtureElement());
		return (element);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static List<RequesterInfo> getRequesterList() {
		try {
			List<RequesterInfo> list = new ArrayList<RequesterInfo>();
			list.add(new RequesterInfo(RequesterInfoTest.getFixtureElement(true)));
			return (list);
		}
		catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static List<Addressee> getAddresseeList() {
		try {
			List<Addressee> list = new ArrayList<Addressee>();
			list.add(new Addressee(AddresseeTest.getFixtureElement(true)));
			return (list);
		}
		catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static Description getDescriptionFixture() {
		try {
			return (new Description("Tasking Info", SecurityAttributesTest.getFixture(false)));
		}
		catch (InvalidDDMSException e) {
			fail("Failed to create fixture: " + e.getMessage());
		}
		return (null);
	}

	/**
	 * Helper method to create a fixture
	 */
	private static TaskID getTaskIDFixture() {
		try {
			return (new TaskID(TaskIDTest.getFixtureElement()));
		}
		catch (InvalidDDMSException e) {
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
	private TaskingInfo testConstructor(boolean expectFailure, Element element) {
		TaskingInfo component = null;
		try {
			component = new TaskingInfo(element);
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
	 * @param requesterInfos list of requestors (optional)
	 * @param addressees list of addressee (optional)
	 * @param description description of tasking (optional)
	 * @param taskID taskID for tasking (required)
	 */
	private TaskingInfo testConstructor(boolean expectFailure, List<RequesterInfo> requesterInfos,
		List<Addressee> addressees, Description description, TaskID taskID) {
		TaskingInfo component = null;
		try {
			component = new TaskingInfo(requesterInfos, addressees, description, taskID, SecurityAttributesTest
				.getFixture(false));
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
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.entityType", "organization"));
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.name", "Name"));
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.classification", "U"));
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.entityType", "organization"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.name", "Name"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.classification", "U"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "taskingInfo.description", "Tasking Info"));
		text.append(buildOutput(isHTML, "taskingInfo.description.classification", "U"));
		text.append(buildOutput(isHTML, "taskingInfo.description.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID", "Task #12345"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.taskingSystem", "MDR"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.network", "NIPRNet"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.otherNetwork", "PBS"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.type", "simple"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.href", "http://en.wikipedia.org/wiki/Tank"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.role", "tank"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.title", "Tank Page"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.arcrole", "arcrole"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.show", "new"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.actuate", "onLoad"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:taskingInfo ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:requesterInfo ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:organization><ddms:name>Name</ddms:name></ddms:organization>");
		xml.append("</ddms:requesterInfo>");
		xml.append("<ddms:addressee ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:organization><ddms:name>Name</ddms:name></ddms:organization>");
		xml.append("</ddms:addressee>");
		xml.append("<ddms:description ISM:classification=\"U\" ISM:ownerProducer=\"USA\">Tasking Info</ddms:description>");
		xml.append("<ddms:taskID xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
		xml.append("ddms:taskingSystem=\"MDR\" network=\"NIPRNet\" otherNetwork=\"PBS\" xlink:type=\"simple\" ");
		xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:arcrole=\"arcrole\" ");
		xml.append("xlink:show=\"new\" xlink:actuate=\"onLoad\">Task #12345</ddms:taskID>");
		xml.append("</ddms:taskingInfo>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getFixtureElement()), DEFAULT_DDMS_PREFIX, TaskingInfo
				.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getFixtureElement());

			// No optional fields
			Element element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			element.appendChild(getTaskIDFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getRequesterList(), getAddresseeList(), getDescriptionFixture(),
				getTaskIDFixture());

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null, getTaskIDFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing taskID
			Element element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
			SecurityAttributesTest.getFixture(false).addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing security attributes
			element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
			element.appendChild(getTaskIDFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing taskID
			testConstructor(WILL_FAIL, null, null, null, null);

			// Missing security attributes
			try {
				new TaskingInfo(null, null, null, new TaskID("test", null, null, null, null), null);
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
			TaskingInfo component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			TaskingInfo dataComponent = testConstructor(WILL_SUCCEED, getRequesterList(), getAddresseeList(),
				getDescriptionFixture(), getTaskIDFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo elementComponent = testConstructor(WILL_SUCCEED, getFixtureElement());
			TaskingInfo dataComponent = testConstructor(WILL_SUCCEED, null, getAddresseeList(),
				getDescriptionFixture(), getTaskIDFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getRequesterList(), null, getDescriptionFixture(),
				getTaskIDFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getRequesterList(), getAddresseeList(), null,
				getTaskIDFixture());
			assertFalse(elementComponent.equals(dataComponent));

			TaskID taskID = new TaskID("Test", null, null, null, null);
			dataComponent = testConstructor(WILL_SUCCEED, getRequesterList(), getAddresseeList(),
				getDescriptionFixture(), taskID);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getRequesterList(), getAddresseeList(), getDescriptionFixture(),
				getTaskIDFixture());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo component = testConstructor(WILL_SUCCEED, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, getRequesterList(), getAddresseeList(), getDescriptionFixture(),
				getTaskIDFixture());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("4.0");
			SecurityAttributes attr = SecurityAttributesTest.getFixture(false);
			DDMSVersion.setCurrentVersion("2.0");
			new TaskingInfo(null, null, null, new TaskID("test", null, null, null, null), attr);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo component = testConstructor(WILL_SUCCEED, getFixtureElement());

			// Equality after Building
			TaskingInfo.Builder builder = new TaskingInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new TaskingInfo.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getRequesterInfos().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());
			builder = new TaskingInfo.Builder();
			builder.getAddressees().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());

			// Validation
			builder = new TaskingInfo.Builder();
			builder.getSecurityAttributes().setClassification("U");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.getTaskID().setValue("test");
			builder.commit();
		}
	}
}
