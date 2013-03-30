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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.DescriptionTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:taskingInfo elements </p>
 * 
 * <p> Because a ddms:taskingInfo is a local component, we cannot load a valid document from a unit test data file. We
 * have to build the well-formed Element ourselves. </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class TaskingInfoTest extends AbstractBaseTestCase {

	/**
	 * Constructor
	 */
	public TaskingInfoTest() {
		super(null);
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static Element getFixtureElement() {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			Element element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
			element.addNamespaceDeclaration(PropertyReader.getPrefix("ddms"), version.getNamespace());
			element.addNamespaceDeclaration(PropertyReader.getPrefix("ism"), version.getIsmNamespace());
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(RequesterInfoTest.getFixtureElement(true));
			element.appendChild(AddresseeTest.getFixtureElement(true));
			element.appendChild(DescriptionTest.getFixture().getXOMElementCopy());
			element.appendChild(TaskIDTest.getFixtureElementNoNetwork());
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
	public static List<TaskingInfo> getFixtureList() {
		try {
			List<TaskingInfo> infos = new ArrayList<TaskingInfo>();
			infos.add(new TaskingInfo(getFixtureElement()));
			return (infos);
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
	private TaskingInfo getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		TaskingInfo component = null;
		try {
			component = new TaskingInfo(element);
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
	 * @param requesterInfos list of requestors (optional)
	 * @param addressees list of addressee (optional)
	 * @param description description of tasking (optional)
	 * @param taskID taskID for tasking (required)
	 */
	private TaskingInfo getInstance(String message, List<RequesterInfo> requesterInfos, List<Addressee> addressees,
		Description description, TaskID taskID) {
		boolean expectFailure = !Util.isEmpty(message);
		TaskingInfo component = null;
		try {
			component = new TaskingInfo(requesterInfos, addressees, description, taskID,
				SecurityAttributesTest.getFixture());
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
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.entityType", "organization"));
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.name", "DISA"));
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.classification", "U"));
		text.append(buildOutput(isHTML, "taskingInfo.requesterInfo.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.entityType", "organization"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.name", "DISA"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.classification", "U"));
		text.append(buildOutput(isHTML, "taskingInfo.addressee.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "taskingInfo.description", "A transformation service."));
		text.append(buildOutput(isHTML, "taskingInfo.description.classification", "U"));
		text.append(buildOutput(isHTML, "taskingInfo.description.ownerProducer", "USA"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID", "Task #12345"));
		text.append(buildOutput(isHTML, "taskingInfo.taskID.taskingSystem", "MDR"));
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
		xml.append("ism:classification=\"U\" ism:ownerProducer=\"USA\">");
		xml.append("<ddms:requesterInfo ism:classification=\"U\" ism:ownerProducer=\"USA\">");
		xml.append("<ddms:organization><ddms:name>DISA</ddms:name></ddms:organization>");
		xml.append("</ddms:requesterInfo>");
		xml.append("<ddms:addressee ism:classification=\"U\" ism:ownerProducer=\"USA\">");
		xml.append("<ddms:organization><ddms:name>DISA</ddms:name></ddms:organization>");
		xml.append("</ddms:addressee>");
		xml.append("<ddms:description ism:classification=\"U\" ism:ownerProducer=\"USA\">A transformation service.</ddms:description>");
		xml.append("<ddms:taskID ");
		xml.append("xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
		xml.append("ddms:taskingSystem=\"MDR\" ");
		xml.append("xlink:type=\"simple\" ");
		xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:arcrole=\"arcrole\" ");
		xml.append("xlink:show=\"new\" xlink:actuate=\"onLoad\">Task #12345</ddms:taskID>");
		xml.append("</ddms:taskingInfo>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getFixtureElement()), DEFAULT_DDMS_PREFIX,
				TaskingInfo.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, getFixtureElement());

			// No optional fields
			Element element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(TaskIDTest.getFixture().getXOMElementCopy());
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			getInstance(SUCCESS, RequesterInfoTest.getFixtureList(), AddresseeTest.getFixtureList(),
				DescriptionTest.getFixture(), TaskIDTest.getFixture());

			// No optional fields
			getInstance(SUCCESS, null, null, null, TaskIDTest.getFixture());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing taskID
			Element element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("taskID is required.", element);

			// Missing security attributes
			element = Util.buildDDMSElement(TaskingInfo.getName(version), null);
			element.appendChild(TaskIDTest.getFixture().getXOMElementCopy());
			getInstance("classification is required.", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Missing taskID
			getInstance("taskID is required.", null, null, null, null);

			// Missing security attributes
			try {
				new TaskingInfo(null, null, null, new TaskID("test", null, null, null, null), null);
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "classification is required.");
			}
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			TaskingInfo component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo elementComponent = getInstance(SUCCESS, getFixtureElement());
			TaskingInfo dataComponent = getInstance(SUCCESS, RequesterInfoTest.getFixtureList(),
				AddresseeTest.getFixtureList(), DescriptionTest.getFixture(),  new TaskID(TaskIDTest.getFixtureElementNoNetwork()));
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo elementComponent = getInstance(SUCCESS, getFixtureElement());
			TaskingInfo dataComponent = getInstance(SUCCESS, null, AddresseeTest.getFixtureList(),
				DescriptionTest.getFixture(), TaskIDTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, RequesterInfoTest.getFixtureList(), null,
				DescriptionTest.getFixture(), TaskIDTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, RequesterInfoTest.getFixtureList(), AddresseeTest.getFixtureList(),
				null, TaskIDTest.getFixture());
			assertFalse(elementComponent.equals(dataComponent));

			TaskID taskID = new TaskID("Test", null, null, null, null);
			dataComponent = getInstance(SUCCESS, RequesterInfoTest.getFixtureList(), AddresseeTest.getFixtureList(),
				DescriptionTest.getFixture(), taskID);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, RequesterInfoTest.getFixtureList(), AddresseeTest.getFixtureList(),
				DescriptionTest.getFixture(), new TaskID(TaskIDTest.getFixtureElementNoNetwork()));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo component = getInstance(SUCCESS, getFixtureElement());
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = getInstance(SUCCESS, RequesterInfoTest.getFixtureList(), AddresseeTest.getFixtureList(),
				DescriptionTest.getFixture(), new TaskID(TaskIDTest.getFixtureElementNoNetwork()));
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testWrongVersion() {
		try {
			DDMSVersion.setCurrentVersion("4.0.1");
			SecurityAttributes attr = SecurityAttributesTest.getFixture();
			DDMSVersion.setCurrentVersion("2.0");
			new TaskingInfo(null, null, null, new TaskID("test", null, null, null, null), attr);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "The DDMS version of the parent");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo component = getInstance(SUCCESS, getFixtureElement());
			TaskingInfo.Builder builder = new TaskingInfo.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo.Builder builder = new TaskingInfo.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getRequesterInfos().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());
			builder = new TaskingInfo.Builder();
			builder.getAddressees().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			TaskingInfo.Builder builder = new TaskingInfo.Builder();
			builder.getSecurityAttributes().setClassification("U");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "taskID is required.");
			}
			builder.getSecurityAttributes().setOwnerProducers(Util.getXsListAsList("USA"));
			builder.getTaskID().setValue("test");
			builder.commit();
		}
	}
}
