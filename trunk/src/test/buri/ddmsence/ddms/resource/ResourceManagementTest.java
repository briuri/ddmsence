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

import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:resourceManagement elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ResourceManagementTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public ResourceManagementTest() {
		super("resourceManagement.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private ResourceManagement testConstructor(boolean expectFailure, Element element) {
		ResourceManagement component = null;
		try {
			component = new ResourceManagement(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (component);
	}

	public static void main(String[] args) throws Exception {
		DDMSVersion.setCurrentVersion("4.0");
		Element element = Util.buildDDMSElement("resourceManagement", null);
		element.appendChild(RecordsManagementInfoTest.getFixture().getXOMElementCopy());
		element.appendChild(RevisionRecallTest.getTextFixtureElement());
		element.appendChild(TaskingInfoTest.getFixtureElement());
		element.appendChild(ProcessingInfoTest.getFixture().getXOMElementCopy());
		SecurityAttributesTest.getFixture().addTo(element);
		System.out.println(element.toXML());
	}
	
	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param recordsManagementInfo records management info (optional)
	 * @param revisionRecall information about revision recalls (optional)
	 * @param taskingInfos list of tasking info (optional)
	 * @param processingInfos list of processing info (optional)
	 */
	private ResourceManagement testConstructor(boolean expectFailure, RecordsManagementInfo recordsManagementInfo,
		RevisionRecall revisionRecall, List<TaskingInfo> taskingInfos, List<ProcessingInfo> processingInfos) {
		ResourceManagement component = null;
		try {
			component = new ResourceManagement(recordsManagementInfo, revisionRecall, taskingInfos, processingInfos,
				SecurityAttributesTest.getFixture());
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
		text.append(RecordsManagementInfoTest.getFixture().getOutput(isHTML, "resourceManagement."));
		text.append(RevisionRecallTest.getTextFixture().getOutput(isHTML, "resourceManagement."));
		text.append(TaskingInfoTest.getFixtureList().get(0).getOutput(isHTML, "resourceManagement."));
		text.append(ProcessingInfoTest.getFixtureList().get(0).getOutput(isHTML, "resourceManagement."));
		text.append(buildOutput(isHTML, "resourceManagement.classification", "U"));
		text.append(buildOutput(isHTML, "resourceManagement.ownerProducer", "USA"));		
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput() {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:resourceManagement ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:recordsManagementInfo ddms:vitalRecordIndicator=\"true\">");
		xml.append("<ddms:recordKeeper><ddms:recordKeeperID>#289-99202.9</ddms:recordKeeperID>");
		xml.append("<ddms:organization><ddms:name>DISA</ddms:name></ddms:organization></ddms:recordKeeper>");
		xml.append("<ddms:applicationSoftware ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("IRM Generator 2L-9</ddms:applicationSoftware>");
		xml.append("</ddms:recordsManagementInfo>");
		xml.append("<ddms:revisionRecall xmlns:xlink=\"http://www.w3.org/1999/xlink\" ddms:revisionID=\"1\" ");
		xml.append("ddms:revisionType=\"ADMINISTRATIVE RECALL\" network=\"NIPRNet\" otherNetwork=\"PBS\" ");
		xml.append("xlink:type=\"resource\" xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:label=\"tank\" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">Description of Recall</ddms:revisionRecall>");
		xml.append("<ddms:taskingInfo ISM:classification=\"U\" ISM:ownerProducer=\"USA\"><ddms:requesterInfo ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:organization><ddms:name>DISA</ddms:name></ddms:organization></ddms:requesterInfo>");
		xml.append("<ddms:addressee ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:organization><ddms:name>DISA</ddms:name></ddms:organization></ddms:addressee>");
		xml.append("<ddms:description ISM:classification=\"U\" ISM:ownerProducer=\"USA\">A transformation service.");
		xml.append("</ddms:description><ddms:taskID xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
		xml.append("ddms:taskingSystem=\"MDR\" network=\"NIPRNet\" otherNetwork=\"PBS\" xlink:type=\"simple\" ");
		xml.append("xlink:href=\"http://en.wikipedia.org/wiki/Tank\" xlink:role=\"tank\" xlink:title=\"Tank Page\" ");
		xml.append("xlink:arcrole=\"arcrole\" xlink:show=\"new\" xlink:actuate=\"onLoad\">Task #12345</ddms:taskID>");
		xml.append("</ddms:taskingInfo><ddms:processingInfo ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ");
		xml.append("ddms:dateProcessed=\"2011-08-19\">XSLT Transformation to convert DDMS 2.0 to DDMS 3.1.");
		xml.append("</ddms:processingInfo></ddms:resourceManagement>");
		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				ResourceManagement.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(ResourceManagement.getName(version), null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, RecordsManagementInfoTest.getFixture(), RevisionRecallTest.getTextFixture(), 
				TaskingInfoTest.getFixtureList(), ProcessingInfoTest.getFixtureList());

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Too many recordsManagementInfo elements
			Element element = Util.buildDDMSElement(ResourceManagement.getName(version), null);
			element.appendChild(RecordsManagementInfoTest.getFixture().getXOMElementCopy());
			element.appendChild(RecordsManagementInfoTest.getFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Too many revisionRecall elements
			element = Util.buildDDMSElement(ResourceManagement.getName(version), null);
			element.appendChild(RevisionRecallTest.getTextFixtureElement());
			element.appendChild(RevisionRecallTest.getTextFixtureElement());
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Incorrect version of security attributes
			DDMSVersion.setCurrentVersion("2.0");
			SecurityAttributes attributes = SecurityAttributesTest.getFixture();
			DDMSVersion.setCurrentVersion(sVersion);
			try {
				new ResourceManagement(null, null, null, null, attributes);
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
			ResourceManagement component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ResourceManagement elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			ResourceManagement dataComponent = testConstructor(WILL_SUCCEED, RecordsManagementInfoTest.getFixture(),
				RevisionRecallTest.getTextFixture(), TaskingInfoTest.getFixtureList(), ProcessingInfoTest
					.getFixtureList());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ResourceManagement elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			ResourceManagement dataComponent = testConstructor(WILL_SUCCEED, null, RevisionRecallTest.getTextFixture(),
				TaskingInfoTest.getFixtureList(), ProcessingInfoTest.getFixtureList());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, RecordsManagementInfoTest.getFixture(), null, TaskingInfoTest
				.getFixtureList(), ProcessingInfoTest.getFixtureList());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, RecordsManagementInfoTest.getFixture(), RevisionRecallTest
				.getTextFixture(), null, ProcessingInfoTest.getFixtureList());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, RecordsManagementInfoTest.getFixture(), RevisionRecallTest
				.getTextFixture(), TaskingInfoTest.getFixtureList(), null);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ResourceManagement component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, RecordsManagementInfoTest.getFixture(), RevisionRecallTest.getTextFixture(), 
				TaskingInfoTest.getFixtureList(), ProcessingInfoTest.getFixtureList());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ResourceManagement component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(), component.toXML());

			component = testConstructor(WILL_SUCCEED, RecordsManagementInfoTest.getFixture(), RevisionRecallTest.getTextFixture(), 
				TaskingInfoTest.getFixtureList(), ProcessingInfoTest.getFixtureList());
			assertEquals(getExpectedXMLOutput(), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ResourceManagement component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			ResourceManagement.Builder builder = new ResourceManagement.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new ResourceManagement.Builder();
			assertNull(builder.commit());
			

			// Emptiness
			builder = new ResourceManagement.Builder();
			assertTrue(builder.isEmpty());
			builder.getTaskingInfos().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());
			builder = new ResourceManagement.Builder();
			builder.getProcessingInfos().get(1).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());
			
			// Validation
			builder = new ResourceManagement.Builder();
			builder.getSecurityAttributes().setClassification("ABC");
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			builder.getSecurityAttributes().setClassification("U");
			builder.commit();
		}
	}
}
