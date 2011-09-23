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
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.Link;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to ddms:revisionRecall elements</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RevisionRecallTest extends AbstractComponentTestCase {

	private static final Integer TEST_REVISION_ID = new Integer(1);
	private static final String TEST_REVISION_TYPE = "ADMINISTRATIVE RECALL";
	private static final String TEST_VALUE = "Description of Recall";
	private static final String TEST_NETWORK = "NIPRNet";
	private static final String TEST_OTHER_NETWORK = "PBS";

	/**
	 * Constructor
	 */
	public RevisionRecallTest() {
		super("revisionRecall.xml");
		removeSupportedVersions("2.0 3.0 3.1");
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @return a XOM element representing a valid element
	 */
	protected Element getTextFixtureElement() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = new Element(getValidElement(version.getVersion()));
		element.removeChildren();
		element.appendChild(TEST_VALUE);
		return (element);
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @return a XOM element representing a valid element
	 */
	protected List<Link> getLinkList() {
		List<Link> links = new ArrayList<Link>();
		links.add(getLinkFixture());
		return (links);
	}

	/**
	 * Returns a fixture object for testing.
	 * 
	 * @return a XOM element representing a valid element
	 */
	protected Link getLinkFixture() {
		try {
			return (new Link(XLinkAttributesTest.getLocatorFixture(), SecurityAttributesTest.getFixture()));
		}
		catch (InvalidDDMSException e) {
			e.printStackTrace();
			fail("Could not create fixture.");
		}
		return (null);
	}

	/**
	 * Returns a canned fixed value for testing.
	 * 
	 * @return a XOM element representing a valid element
	 */
	protected List<Details> getDetailsList() {
		List<Details> links = new ArrayList<Details>();
		links.add(getDetailsFixture());
		return (links);
	}

	/**
	 * Returns a canned fixed value for testing.
	 * 
	 * @return a XOM element representing a valid element
	 */
	protected Details getDetailsFixture() {
		try {
			return (new Details("Details", SecurityAttributesTest.getFixture()));
		}
		catch (InvalidDDMSException e) {
			e.printStackTrace();
			fail("Could not create fixture.");
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
	private RevisionRecall testConstructor(boolean expectFailure, Element element) {
		RevisionRecall component = null;
		try {
			component = new RevisionRecall(element);
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
	 * @param links associated links (optional)
	 * @param details associated details (optional)
	 * @param revisionID integer ID for this recall (required)
	 * @param revisionType type of revision (required)
	 * @param network the network (optional)
	 * @param otherNetwork another network (optional)
	 * @param xlinkAttributes simple xlink attributes (optional)
	 */
	private RevisionRecall testConstructor(boolean expectFailure, List<Link> links, List<Details> details,
		Integer revisionID, String revisionType, String network, String otherNetwork, XLinkAttributes xlinkAttributes) {
		RevisionRecall component = null;
		try {
			component = new RevisionRecall(links, details, revisionID, revisionType, network, otherNetwork,
				xlinkAttributes, SecurityAttributesTest.getFixture());
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
	 * @param value child text (optional)
	 * @param revisionID integer ID for this recall (required)
	 * @param revisionType type of revision (required)
	 * @param network the network (optional)
	 * @param otherNetwork another network (optional)
	 * @param xlinkAttributes simple xlink attributes (optional)
	 */
	private RevisionRecall testConstructor(boolean expectFailure, String value, Integer revisionID,
		String revisionType, String network, String otherNetwork, XLinkAttributes xlinkAttributes) {
		RevisionRecall component = null;
		try {
			component = new RevisionRecall(value, revisionID, revisionType, network, otherNetwork, xlinkAttributes,
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
	private String getExpectedOutput(boolean hasLinks, boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		if (!hasLinks)
			text.append(buildOutput(isHTML, "revisionRecall", TEST_VALUE));
		text.append(buildOutput(isHTML, "revisionRecall.revisionID", "1"));
		text.append(buildOutput(isHTML, "revisionRecall.revisionType", "ADMINISTRATIVE RECALL"));
		text.append(buildOutput(isHTML, "revisionRecall.network", "NIPRNet"));
		text.append(buildOutput(isHTML, "revisionRecall.otherNetwork", "PBS"));
		if (hasLinks) {
			text.append(buildOutput(isHTML, "revisionRecall.link.type", "locator"));
			text.append(buildOutput(isHTML, "revisionRecall.link.href", "http://en.wikipedia.org/wiki/Tank"));
			text.append(buildOutput(isHTML, "revisionRecall.link.role", "tank"));
			text.append(buildOutput(isHTML, "revisionRecall.link.title", "Tank Page"));
			text.append(buildOutput(isHTML, "revisionRecall.link.label", "tank"));
			text.append(buildOutput(isHTML, "revisionRecall.link.classification", "U"));
			text.append(buildOutput(isHTML, "revisionRecall.link.ownerProducer", "USA"));
			text.append(buildOutput(isHTML, "revisionRecall.details", "Details"));
			text.append(buildOutput(isHTML, "revisionRecall.details.classification", "U"));
			text.append(buildOutput(isHTML, "revisionRecall.details.ownerProducer", "USA"));
		}
		text.append(XLinkAttributesTest.getResourceFixture().getOutput(isHTML, "revisionRecall."));
		text.append(SecurityAttributesTest.getFixture().getOutput(isHTML, "revisionRecall."));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 */
	private String getExpectedXMLOutput(boolean hasLinks) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:revisionRecall ").append(getXmlnsDDMS()).append(
			" xmlns:xlink=\"http://www.w3.org/1999/xlink\" ");
		xml.append(getXmlnsISM()).append(" ");
		xml.append("ddms:revisionID=\"1\" ddms:revisionType=\"ADMINISTRATIVE RECALL\" ");
		xml.append("network=\"NIPRNet\" otherNetwork=\"PBS\" ");
		xml.append("xlink:type=\"resource\" xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:label=\"tank\" ");
		xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\">");

		if (hasLinks) {
			xml.append("<ddms:link xlink:type=\"locator\" xlink:href=\"http://en.wikipedia.org/wiki/Tank\" ");
			xml.append("xlink:role=\"tank\" xlink:title=\"Tank Page\" xlink:label=\"tank\" ");
			xml.append("ISM:classification=\"U\" ISM:ownerProducer=\"USA\" />");
			xml.append("<ddms:details ISM:classification=\"U\" ISM:ownerProducer=\"USA\">Details</ddms:details>");
		}
		else
			xml.append(TEST_VALUE);
		xml.append("</ddms:revisionRecall>");

		return (xml.toString());
	}

	public void testNameAndNamespace() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(testConstructor(WILL_SUCCEED, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				RevisionRecall.getName(version));
			testConstructor(WILL_FAIL, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields (links)
			testConstructor(WILL_SUCCEED, getValidElement(sVersion));

			// All fields (text)
			testConstructor(WILL_SUCCEED, getTextFixtureElement());

			// No optional fields (links)
			Element element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(getLinkFixture().getXOMElementCopy());
			testConstructor(WILL_SUCCEED, element);

			// No optional fields (text)
			element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields (links)
			testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), TEST_REVISION_ID, TEST_REVISION_TYPE,
				TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());

			// All fields (text)
			testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());

			// No optional fields (links)
			testConstructor(WILL_SUCCEED, null, null, TEST_REVISION_ID, TEST_REVISION_TYPE, null, null, null);

			// No optional fields (text)
			testConstructor(WILL_SUCCEED, null, TEST_REVISION_ID, TEST_REVISION_TYPE, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Wrong type of XLinkAttributes (locator)
			Element element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			XLinkAttributesTest.getLocatorFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Both text AND links/details, text first
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(getLinkFixture().getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Both text AND links/details, text last
			element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(getLinkFixture().getXOMElementCopy());
			element.appendChild(TEST_VALUE);
			testConstructor(WILL_FAIL, element);

			// Links without security attributes
			element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			Link link = new Link(XLinkAttributesTest.getLocatorFixture(), null);
			element.appendChild(link.getXOMElementCopy());
			testConstructor(WILL_FAIL, element);

			// Bad revisionID
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", "one");
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing revisionID
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Missing revisionType
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Bad revisionType
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", "MISTAKE");
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);

			// Bad network
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addAttribute(element, "", "network", "", "PBS");
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Wrong type of XLinkAttributes (locator)
			testConstructor(WILL_FAIL, getLinkList(), getDetailsList(), TEST_REVISION_ID, TEST_REVISION_TYPE,
				TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getLocatorFixture());

			// Links without security attributes
			Link link = new Link(XLinkAttributesTest.getLocatorFixture(), null);
			List<Link> linkList = new ArrayList<Link>();
			linkList.add(link);
			testConstructor(WILL_FAIL, linkList, getDetailsList(), TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getLocatorFixture());

			// Missing revisionID
			testConstructor(WILL_FAIL, getLinkList(), getDetailsList(), null, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());

			// Missing revisionType
			testConstructor(WILL_FAIL, getLinkList(), getDetailsList(), TEST_REVISION_ID, null, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());

			// Bad revisionType
			testConstructor(WILL_FAIL, getLinkList(), getDetailsList(), TEST_REVISION_ID, "MISTAKE", TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());

			// Bad network
			testConstructor(WILL_FAIL, getLinkList(), getDetailsList(), TEST_REVISION_ID, TEST_REVISION_TYPE, "PBS",
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			RevisionRecall component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			RevisionRecall dataComponent = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(),
				TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest
					.getResourceFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// text
			elementComponent = testConstructor(WILL_SUCCEED, getTextFixtureElement());
			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE,
				TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			RevisionRecall dataComponent = testConstructor(WILL_SUCCEED, null, getDetailsList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinkList(), null, TEST_REVISION_ID, TEST_REVISION_TYPE,
				TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), new Integer(2),
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), TEST_REVISION_ID,
				"ADMINISTRATIVE REVISION", TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, "SIPRNet", TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, "ABC", XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, null);
			assertFalse(elementComponent.equals(dataComponent));

			// text
			elementComponent = testConstructor(WILL_SUCCEED, getTextFixtureElement());
			dataComponent = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE,
				TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, new Integer(2), TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, "ADMINISTRATIVE REVISION",
				TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, "SIPRNet",
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE,
				TEST_NETWORK, "ABC", XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE,
				TEST_NETWORK, TEST_OTHER_NETWORK, null);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RevisionRecall elementComponent = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true, true), component.toHTML());
			assertEquals(getExpectedOutput(true, false), component.toText());

			component = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedOutput(true, true), component.toHTML());
			assertEquals(getExpectedOutput(true, false), component.toText());

			// text
			component = testConstructor(WILL_SUCCEED, getTextFixtureElement());
			assertEquals(getExpectedOutput(false, true), component.toHTML());
			assertEquals(getExpectedOutput(false, false), component.toText());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedOutput(false, true), component.toHTML());
			assertEquals(getExpectedOutput(false, false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = testConstructor(WILL_SUCCEED, getLinkList(), getDetailsList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			// text
			component = testConstructor(WILL_SUCCEED, getTextFixtureElement());
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = testConstructor(WILL_SUCCEED, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void test20Usage() {
		try {
			DDMSVersion.setCurrentVersion("4.0");
			XLinkAttributes attr = XLinkAttributesTest.getSimpleFixture();
			DDMSVersion.setCurrentVersion("2.0");
			new RevisionRecall(TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK,
				attr, SecurityAttributesTest.getFixture());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testBuilder() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Equality after Building (links)
			RevisionRecall component = testConstructor(WILL_SUCCEED, getValidElement(sVersion));
			RevisionRecall.Builder builder = new RevisionRecall.Builder(component);
			assertEquals(builder.commit(), component);

			// Equality after Building (text)
			component = testConstructor(WILL_SUCCEED, getTextFixtureElement());
			builder = new RevisionRecall.Builder(component);
			assertEquals(builder.commit(), component);

			// Empty case
			builder = new RevisionRecall.Builder();
			assertNull(builder.commit());

			// Emptiness
			builder = new RevisionRecall.Builder();
			assertTrue(builder.isEmpty());
			builder.getLinks().get(2).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());
			builder = new RevisionRecall.Builder();
			assertTrue(builder.isEmpty());
			builder.getDetails().get(2).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());

			// Validation
			builder = new RevisionRecall.Builder();
			builder.setRevisionID(TEST_REVISION_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			builder.setRevisionType(TEST_REVISION_TYPE);
			builder.commit();
		}
	}
}
