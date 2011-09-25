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
package buri.ddmsence.ddms.metacard;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractComponentTestCase;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.ContributorTest;
import buri.ddmsence.ddms.resource.CreatorTest;
import buri.ddmsence.ddms.resource.Dates;
import buri.ddmsence.ddms.resource.DatesTest;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.resource.IdentifierTest;
import buri.ddmsence.ddms.resource.LanguageTest;
import buri.ddmsence.ddms.resource.PointOfContactTest;
import buri.ddmsence.ddms.resource.ProcessingInfoTest;
import buri.ddmsence.ddms.resource.Publisher;
import buri.ddmsence.ddms.resource.PublisherTest;
import buri.ddmsence.ddms.resource.RecordsManagementInfo;
import buri.ddmsence.ddms.resource.RecordsManagementInfoTest;
import buri.ddmsence.ddms.resource.RevisionRecall;
import buri.ddmsence.ddms.resource.RevisionRecallTest;
import buri.ddmsence.ddms.security.NoticeList;
import buri.ddmsence.ddms.security.NoticeListTest;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.Description;
import buri.ddmsence.ddms.summary.DescriptionTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:metacardInfo elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class MetacardInfoTest extends AbstractComponentTestCase {

	/**
	 * Constructor
	 */
	public MetacardInfoTest() {
		super("metacardInfo.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static MetacardInfo getFixture() {
		try {
			return (DDMSVersion.getCurrentVersion().isAtLeast("4.0") ? new MetacardInfo(getRequiredChildComponents(),
				SecurityAttributesTest.getFixture()) : null);
		}
		catch (InvalidDDMSException e) {
			fail("Could not create fixture: " + e.getMessage());
		}
		return (null);
	}
	
	/**
	 * Returns a list of child components for testing.
	 */
	private static List<IDDMSComponent> getRequiredChildComponents() {
		List<IDDMSComponent> childComponents = new ArrayList<IDDMSComponent>();
		childComponents.add(IdentifierTest.getFixture());
		childComponents.add(DatesTest.getFixture());
		childComponents.add(PublisherTest.getFixture());
		return (childComponents);
	}
	
	/**
	 * Returns a list of child components for testing.
	 */
	private static List<IDDMSComponent> getChildComponents() {
		List<IDDMSComponent> childComponents = new ArrayList<IDDMSComponent>();
		childComponents.add(IdentifierTest.getFixture());
		childComponents.add(DatesTest.getFixture());
		childComponents.add(PublisherTest.getFixture());
		childComponents.add(ContributorTest.getFixture());
		childComponents.add(CreatorTest.getFixture());
		childComponents.add(PointOfContactTest.getFixture());
		childComponents.add(DescriptionTest.getFixture());
		childComponents.add(ProcessingInfoTest.getFixture());
		childComponents.add(RevisionRecallTest.getTextFixture());
		childComponents.add(RecordsManagementInfoTest.getFixture());
		childComponents.add(NoticeListTest.getFixture());
		return (childComponents);
	}
	
	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private MetacardInfo testConstructor(boolean expectFailure, Element element) {
		MetacardInfo component = null;
		try {
			component = new MetacardInfo(element);
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
	 * @param childComponents a list of child components to be put into this instance (required)
	 */
	private MetacardInfo testConstructor(boolean expectFailure, List<IDDMSComponent> childComponents) {
		MetacardInfo component = null;
		try {
			component = new MetacardInfo(childComponents, SecurityAttributesTest.getFixture());
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
		for (IDDMSComponent component : getChildComponents())
			text.append(component.getOutput(isHTML, "metacardInfo."));
		text.append(buildOutput(isHTML, "metacardInfo.classification", "U"));
		text.append(buildOutput(isHTML, "metacardInfo.ownerProducer", "USA"));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:metacardInfo ").append(getXmlnsDDMS()).append(" ").append(getXmlnsISM()).append(" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ddms:identifier ddms:qualifier=\"URI\" ddms:value=\"urn:buri:ddmsence:testIdentifier\" />");
		xml.append("<ddms:dates ddms:created=\"2003\" />");
		xml.append("<ddms:publisher><ddms:person><ddms:name>Brian</ddms:name><ddms:surname>Uri</ddms:surname></ddms:person></ddms:publisher>");
		xml.append("<ddms:contributor><ddms:service><ddms:name>https://metadata.dod.mil/ebxmlquery/soap</ddms:name></ddms:service></ddms:contributor>");
		xml.append("<ddms:creator><ddms:organization><ddms:name>DISA</ddms:name></ddms:organization></ddms:creator>");
		xml.append("<ddms:pointOfContact><ddms:unknown><ddms:name>UnknownEntity</ddms:name></ddms:unknown></ddms:pointOfContact>");
		xml.append("<ddms:description ISM:classification=\"U\" ISM:ownerProducer=\"USA\">A transformation service.</ddms:description>");
		xml.append("<ddms:processingInfo ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ddms:dateProcessed=\"2011-08-19\">");
		xml.append("XSLT Transformation to convert DDMS 2.0 to DDMS 3.1.</ddms:processingInfo>");
		xml.append("<ddms:revisionRecall xmlns:xlink=\"http://www.w3.org/1999/xlink\" ddms:revisionID=\"1\" ");
		xml.append("ddms:revisionType=\"ADMINISTRATIVE RECALL\" network=\"NIPRNet\" otherNetwork=\"PBS\" ");
		xml.append("xlink:type=\"resource\" xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:label=\"tank\" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">Description of Recall</ddms:revisionRecall>");
		xml.append("<ddms:recordsManagementInfo ddms:vitalRecordIndicator=\"true\">");
		xml.append("<ddms:recordKeeper><ddms:recordKeeperID>#289-99202.9</ddms:recordKeeperID>");
		xml.append("<ddms:organization><ddms:name>DISA</ddms:name></ddms:organization></ddms:recordKeeper>");
		xml.append("<ddms:applicationSoftware ISM:classification=\"U\" ISM:ownerProducer=\"USA\">IRM Generator 2L-9</ddms:applicationSoftware>");
		xml.append("</ddms:recordsManagementInfo>");
		xml.append("<ddms:noticeList ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ISM:Notice ISM:noticeType=\"ABC\" ISM:noticeReason=\"noticeReason\" ISM:noticeDate=\"2011-09-15\" ");
		xml.append("ISM:unregisteredNoticeType=\"unregisteredNoticeType\" ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");
		xml.append("<ISM:NoticeText ISM:classification=\"U\" ISM:ownerProducer=\"USA\" ISM:pocType=\"DoD-Dist-B\">noticeText</ISM:NoticeText>");
		xml.append("</ISM:Notice></ddms:noticeList></ddms:metacardInfo>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				MetacardInfo.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// No optional fields
			Element element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getRequiredChildComponents())
				element.appendChild(component.getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields
			testConstructor(WILL_SUCCEED, getChildComponents());
			
			// Null components
			List<IDDMSComponent> components = getChildComponents();
			components.add(null);
			testConstructor(WILL_SUCCEED, components);		
			
			// No optional fields
			testConstructor(WILL_SUCCEED, getRequiredChildComponents());
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Missing identifier
			Element element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getRequiredChildComponents()) {
				if (component instanceof Identifier)
					continue;
				element.appendChild(component.getXOMElementCopy());
			}
			testConstructor(WILL_FAIL, element);
			
			// Missing publisher
			element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getRequiredChildComponents()) {
				if (component instanceof Publisher)
					continue;
				element.appendChild(component.getXOMElementCopy());
			}
			testConstructor(WILL_FAIL, element);
			
			// Missing dates
			element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getRequiredChildComponents()) {
				if (component instanceof Dates)
					continue;
				element.appendChild(component.getXOMElementCopy());
			}
			testConstructor(WILL_FAIL, element);
			
			// Too many dates
			element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getRequiredChildComponents()) {
				element.appendChild(component.getXOMElementCopy());
				if (component instanceof Dates)
					element.appendChild(component.getXOMElementCopy());
			}			
			testConstructor(WILL_FAIL, element);
			
			// Too many descriptions
			element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getChildComponents()) {
				element.appendChild(component.getXOMElementCopy());
				if (component instanceof Description)
					element.appendChild(component.getXOMElementCopy());
			}
			System.out.println(element.toXML());
			testConstructor(WILL_FAIL, element);
			
			// Too many revisionRecalls
			element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getChildComponents()) {
				element.appendChild(component.getXOMElementCopy());
				if (component instanceof RevisionRecall)
					element.appendChild(component.getXOMElementCopy());
			}
			testConstructor(WILL_FAIL, element);
			
			// Too many recordsManagementInfos
			element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getChildComponents()) {
				element.appendChild(component.getXOMElementCopy());
				if (component instanceof RecordsManagementInfo)
					element.appendChild(component.getXOMElementCopy());
			}
			testConstructor(WILL_FAIL, element);
			
			// Too many noticeLists
			element = Util.buildDDMSElement(MetacardInfo.getName(version), null);
			for (IDDMSComponent component : getChildComponents()) {
				element.appendChild(component.getXOMElementCopy());
				if (component instanceof NoticeList)
					element.appendChild(component.getXOMElementCopy());
			}
			testConstructor(WILL_FAIL, element);
			
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No components
			testConstructor(WILL_FAIL, (List) null);
			
			// Weird component
			List<IDDMSComponent> components = getChildComponents();
			components.add(LanguageTest.getFixture());
			testConstructor(WILL_FAIL, components);
			
			// Missing identifier
			components = getChildComponents();
			components.remove(IdentifierTest.getFixture());
			testConstructor(WILL_FAIL, components);
			
			// Missing publisher
			components = getChildComponents();
			components.remove(PublisherTest.getFixture());
			testConstructor(WILL_FAIL, components);
			
			// Missing dates
			components = getChildComponents();
			components.remove(DatesTest.getFixture());
			testConstructor(WILL_FAIL, components);
			
			// Incorrect version of security attributes
			DDMSVersion.setCurrentVersion("2.0");
			SecurityAttributes attributes = SecurityAttributesTest.getFixture();
			DDMSVersion.setCurrentVersion(sVersion);
			try {
				new MetacardInfo(getChildComponents(), attributes);
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
			MetacardInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			MetacardInfo elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			MetacardInfo dataComponent = testConstructor(WILL_SUCCEED, getChildComponents());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}
	
	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			MetacardInfo elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			
			List<IDDMSComponent> components = getChildComponents();
			components.add(IdentifierTest.getFixture());
			MetacardInfo dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(DatesTest.getFixture());
			components.add(new Dates(null, null, null, null, null, null));
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(CreatorTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(ContributorTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(PointOfContactTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.add(PublisherTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(DescriptionTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(ProcessingInfoTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(RevisionRecallTest.getTextFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(RecordsManagementInfoTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
			
			components = getChildComponents();
			components.remove(NoticeListTest.getFixture());
			dataComponent = testConstructor(WILL_SUCCEED, components);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}
	
	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			MetacardInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = testConstructor(WILL_SUCCEED, getChildComponents());
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			MetacardInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getChildComponents());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			MetacardInfo component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// Equality after Building
			MetacardInfo.Builder builder = new MetacardInfo.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new MetacardInfo.Builder();
			assertNull(builder.commit());

			// Validation
			builder = new MetacardInfo.Builder(component);
			builder.getIdentifiers().clear();
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			builder.getIdentifiers().get(0).setQualifier("test");
			builder.getIdentifiers().get(0).setValue("test");
			builder.commit();
		}
	}
}
