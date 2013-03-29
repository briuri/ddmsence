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
import buri.ddmsence.ddms.security.ism.SecurityAttributesTest;
import buri.ddmsence.ddms.summary.Link;
import buri.ddmsence.ddms.summary.LinkTest;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributesTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:revisionRecall elements </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RevisionRecallTest extends AbstractBaseTestCase {

	private static final Integer TEST_REVISION_ID = Integer.valueOf(1);
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
	 */
	public static Element getTextFixtureElement() {
		DDMSVersion version = DDMSVersion.getCurrentVersion();
		Element element = new Element(new RevisionRecallTest().getValidElement(version.getVersion()));
		element.removeChildren();
		element.appendChild(TEST_VALUE);
		return (element);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static RevisionRecall getTextFixture() {
		try {
			return (new RevisionRecall(getTextFixtureElement()));
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
	private RevisionRecall getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		RevisionRecall component = null;
		try {
			component = new RevisionRecall(element);
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
	 * @param links associated links (optional)
	 * @param details associated details (optional)
	 * @param revisionID integer ID for this recall (required)
	 * @param revisionType type of revision (required)
	 * @param network the network (optional)
	 * @param otherNetwork another network (optional)
	 * @param xlinkAttributes simple xlink attributes (optional)
	 */
	private RevisionRecall getInstance(String message, List<Link> links, List<Details> details, Integer revisionID,
		String revisionType, String network, String otherNetwork, XLinkAttributes xlinkAttributes) {
		boolean expectFailure = !Util.isEmpty(message);
		RevisionRecall component = null;
		try {
			component = new RevisionRecall(links, details, revisionID, revisionType, network, otherNetwork,
				xlinkAttributes, SecurityAttributesTest.getFixture());
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
	 * @param value child text (optional)
	 * @param revisionID integer ID for this recall (required)
	 * @param revisionType type of revision (required)
	 * @param network the network (optional)
	 * @param otherNetwork another network (optional)
	 * @param xlinkAttributes simple xlink attributes (optional)
	 */
	private RevisionRecall getInstance(String message, String value, Integer revisionID, String revisionType,
		String network, String otherNetwork, XLinkAttributes xlinkAttributes) {
		boolean expectFailure = !Util.isEmpty(message);
		RevisionRecall component = null;
		try {
			component = new RevisionRecall(value, revisionID, revisionType, network, otherNetwork, xlinkAttributes,
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

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX,
				RevisionRecall.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// All fields (links)
			getInstance(SUCCESS, getValidElement(sVersion));

			// All fields (text)
			getInstance(SUCCESS, getTextFixtureElement());

			// No optional fields (links)
			Element element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(LinkTest.getLocatorFixture(true).getXOMElementCopy());
			getInstance(SUCCESS, element);

			// No optional fields (text)
			element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// All fields (links)
			getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());

			// All fields (text)
			getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());

			// No optional fields (links)
			getInstance(SUCCESS, null, null, TEST_REVISION_ID, TEST_REVISION_TYPE, null, null, null);

			// No optional fields (text)
			getInstance(SUCCESS, null, TEST_REVISION_ID, TEST_REVISION_TYPE, null, null, null);
		}
	}

	public void testElementConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Wrong type of XLinkAttributes (locator)
			Element element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			XLinkAttributesTest.getLocatorFixture().addTo(element);
			getInstance("revision ID is required.", element);

			// Both text AND links/details, text first
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(LinkTest.getLocatorFixture(true).getXOMElementCopy());
			getInstance("A ddms:revisionRecall element cannot have both child text and nested elements.", element);

			// Both text AND links/details, text last
			element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			element.appendChild(LinkTest.getLocatorFixture(true).getXOMElementCopy());
			element.appendChild(TEST_VALUE);
			getInstance("A ddms:revisionRecall element cannot have both child text and nested elements.", element);

			// Links without security attributes
			element = Util.buildDDMSElement(RevisionRecall.getName(version), null);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			Link link = new Link(XLinkAttributesTest.getLocatorFixture(), null);
			element.appendChild(link.getXOMElementCopy());
			getInstance("classification is required.", element);

			// Bad revisionID
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", "one");
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("revision ID is required.", element);

			// Missing revisionID
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("revision ID is required.", element);

			// Missing revisionType
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("The revisionType attribute must be one of", element);

			// Bad revisionType
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", "MISTAKE");
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("The revisionType attribute must be one of", element);

			// Bad network
			element = Util.buildDDMSElement(RevisionRecall.getName(version), TEST_VALUE);
			Util.addAttribute(element, "", "network", "", "PBS");
			Util.addDDMSAttribute(element, "revisionID", TEST_REVISION_ID.toString());
			Util.addDDMSAttribute(element, "revisionType", TEST_REVISION_TYPE);
			SecurityAttributesTest.getFixture().addTo(element);
			getInstance("The network attribute must be one of", element);
		}
	}

	public void testDataConstructorInvalid() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Wrong type of XLinkAttributes (locator)
			getInstance("The type attribute must have a fixed value", LinkTest.getLocatorFixtureList(true),
				DetailsTest.getFixtureList(), TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getLocatorFixture());

			// Links without security attributes
			Link link = new Link(XLinkAttributesTest.getLocatorFixture(), null);
			List<Link> linkList = new ArrayList<Link>();
			linkList.add(link);
			getInstance("classification is required.", linkList, DetailsTest.getFixtureList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getLocatorFixture());

			// Missing revisionID
			getInstance("revision ID is required.", LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				null, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());

			// Missing revisionType
			getInstance("The revisionType attribute must be one of", LinkTest.getLocatorFixtureList(true),
				DetailsTest.getFixtureList(), TEST_REVISION_ID, null, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());

			// Bad revisionType
			getInstance("The revisionType attribute must be one of", LinkTest.getLocatorFixtureList(true),
				DetailsTest.getFixtureList(), TEST_REVISION_ID, "MISTAKE", TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());

			// Bad network
			getInstance("The network attribute must be one of", LinkTest.getLocatorFixtureList(true),
				DetailsTest.getFixtureList(), TEST_REVISION_ID, TEST_REVISION_TYPE, "PBS", TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());
		}
	}

	public void testWarnings() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// No warnings
			RevisionRecall component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			RevisionRecall dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true),
				DetailsTest.getFixtureList(), TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());

			// text
			elementComponent = getInstance(SUCCESS, getTextFixtureElement());
			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			RevisionRecall dataComponent = getInstance(SUCCESS, null, DetailsTest.getFixtureList(), TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), null, TEST_REVISION_ID,
				TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				Integer.valueOf(2), TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				TEST_REVISION_ID, "ADMINISTRATIVE REVISION", TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				TEST_REVISION_ID, TEST_REVISION_TYPE, "SIPRNet", TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, "DoD-Dist-B",
				XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, null);
			assertFalse(elementComponent.equals(dataComponent));

			// text
			elementComponent = getInstance(SUCCESS, getTextFixtureElement());
			dataComponent = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, Integer.valueOf(2), TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, "ADMINISTRATIVE REVISION", TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, "SIPRNet",
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				"DoD-Dist-B", XLinkAttributesTest.getResourceFixture());
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, null);
			assertFalse(elementComponent.equals(dataComponent));

		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RevisionRecall elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true, true), component.toHTML());
			assertEquals(getExpectedOutput(true, false), component.toText());

			component = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedOutput(true, true), component.toHTML());
			assertEquals(getExpectedOutput(true, false), component.toText());

			// text
			component = getInstance(SUCCESS, getTextFixtureElement());
			assertEquals(getExpectedOutput(false, true), component.toHTML());
			assertEquals(getExpectedOutput(false, false), component.toText());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedOutput(false, true), component.toHTML());
			assertEquals(getExpectedOutput(false, false), component.toText());
		}
	}

	public void testXMLOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// links
			RevisionRecall component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, LinkTest.getLocatorFixtureList(true), DetailsTest.getFixtureList(),
				TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK,
				XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			// text
			component = getInstance(SUCCESS, getTextFixtureElement());
			assertEquals(getExpectedXMLOutput(false), component.toXML());

			component = getInstance(SUCCESS, TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK,
				TEST_OTHER_NETWORK, XLinkAttributesTest.getResourceFixture());
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testWrongVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("4.0.1");
		XLinkAttributes attr = XLinkAttributesTest.getResourceFixture();
		DDMSVersion.setCurrentVersion("2.0");
		// Cross version attributes are allowed, because the version is not set until they are added onto an element.
		new RevisionRecall(TEST_VALUE, TEST_REVISION_ID, TEST_REVISION_TYPE, TEST_NETWORK, TEST_OTHER_NETWORK, attr,
			SecurityAttributesTest.getFixture());
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			// Equality after Building (links)
			RevisionRecall component = getInstance(SUCCESS, getValidElement(sVersion));
			RevisionRecall.Builder builder = new RevisionRecall.Builder(component);
			assertEquals(component, builder.commit());

			// Equality after Building (text)
			component = getInstance(SUCCESS, getTextFixtureElement());
			builder = new RevisionRecall.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RevisionRecall.Builder builder = new RevisionRecall.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.getLinks().get(2).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());

			builder = new RevisionRecall.Builder();
			assertTrue(builder.isEmpty());
			builder.getDetails().get(2).getSecurityAttributes().setClassification("U");
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			RevisionRecall.Builder builder = new RevisionRecall.Builder();
			builder.setRevisionID(TEST_REVISION_ID);
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The revisionType attribute must be one of");
			}
			builder.setRevisionType(TEST_REVISION_TYPE);
			builder.commit();
		}
	}
}
