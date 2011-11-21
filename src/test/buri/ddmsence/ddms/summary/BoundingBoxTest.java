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
package buri.ddmsence.ddms.summary;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to ddms:boundingBox elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class BoundingBoxTest extends AbstractBaseTestCase {

	private static final double TEST_WEST = 12.3;
	private static final double TEST_EAST = 23.4;
	private static final double TEST_SOUTH = 34.5;
	private static final double TEST_NORTH = 45.6;

	/**
	 * Constructor
	 */
	public BoundingBoxTest() {
		super("boundingBox.xml");
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static BoundingBox getFixture() {
		try {
			return (new BoundingBox(1.1, 2.2, 3.3, 4.4));
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
	private BoundingBox getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		BoundingBox component = null;
		try {
			component = new BoundingBox(element);
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
	 * @param westBL the westbound longitude
	 * @param eastBL the eastbound longitude
	 * @param southBL the southbound latitude
	 * @param northBL the northbound latitude
	 * @return a valid object
	 */
	private BoundingBox getInstance(String message, double westBL, double eastBL, double southBL, double northBL) {
		boolean expectFailure = !Util.isEmpty(message);
		BoundingBox component = null;
		try {
			component = new BoundingBox(westBL, eastBL, southBL, northBL);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (component);
	}

	/**
	 * Helper method to get the name of the westbound longitude element, which changed in DDMS 4.0.1.
	 */
	private String getWestBLName() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? "westBL" : "WestBL");
	}

	/**
	 * Helper method to get the name of the eastbound longitude element, which changed in DDMS 4.0.1.
	 */
	private String getEastBLName() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? "eastBL" : "EastBL");
	}

	/**
	 * Helper method to get the name of the southbound latitude element, which changed in DDMS 4.0.1.
	 */
	private String getSouthBLName() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? "southBL" : "SouthBL");
	}

	/**
	 * Helper method to get the name of the northbound latitude element, which changed in DDMS 4.0.1.
	 */
	private String getNorthBLName() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? "northBL" : "NorthBL");
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) throws InvalidDDMSException {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "boundingBox." + getWestBLName(), String.valueOf(TEST_WEST)));
		text.append(buildOutput(isHTML, "boundingBox." + getEastBLName(), String.valueOf(TEST_EAST)));
		text.append(buildOutput(isHTML, "boundingBox." + getSouthBLName(), String.valueOf(TEST_SOUTH)));
		text.append(buildOutput(isHTML, "boundingBox." + getNorthBLName(), String.valueOf(TEST_NORTH)));
		return (text.toString());
	}

	/**
	 * Returns the expected XML output for this unit test
	 * 
	 * @param preserveFormatting if true, include line breaks and tabs.
	 */
	private String getExpectedXMLOutput(boolean preserveFormatting) {
		StringBuffer xml = new StringBuffer();
		xml.append("<ddms:boundingBox ").append(getXmlnsDDMS()).append(">\n\t");
		xml.append("<ddms:").append(getWestBLName()).append(">").append(TEST_WEST).append("</ddms:").append(
			getWestBLName()).append(">\n\t");
		xml.append("<ddms:").append(getEastBLName()).append(">").append(TEST_EAST).append("</ddms:").append(
			getEastBLName()).append(">\n\t");
		xml.append("<ddms:").append(getSouthBLName()).append(">").append(TEST_SOUTH).append("</ddms:").append(
			getSouthBLName()).append(">\n\t");
		xml.append("<ddms:").append(getNorthBLName()).append(">").append(TEST_NORTH).append("</ddms:").append(
			getNorthBLName()).append(">\n");
		xml.append("</ddms:boundingBox>");
		return (formatXml(xml.toString(), preserveFormatting));
	}

	/**
	 * Helper method to create a XOM element that can be used to test element constructors
	 * 
	 * @param west westBL
	 * @param east eastBL
	 * @param south southBL
	 * @param north northBL
	 * @return Element
	 */
	private Element buildComponentElement(String west, String east, String south, String north) {
		Element element = Util.buildDDMSElement(BoundingBox.getName(DDMSVersion.getCurrentVersion()), null);
		element.appendChild(Util.buildDDMSElement(getWestBLName(), String.valueOf(west)));
		element.appendChild(Util.buildDDMSElement(getEastBLName(), String.valueOf(east)));
		element.appendChild(Util.buildDDMSElement(getSouthBLName(), String.valueOf(south)));
		element.appendChild(Util.buildDDMSElement(getNorthBLName(), String.valueOf(north)));
		return (element);
	}

	public void testNameAndNamespace() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			assertNameAndNamespace(getInstance(SUCCESS, getValidElement(sVersion)), DEFAULT_DDMS_PREFIX, BoundingBox
				.getName(version));
			getInstance(WRONG_NAME_MESSAGE, getWrongNameElementFixture());
		}
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			getInstance(SUCCESS, getValidElement(sVersion));
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			getInstance(SUCCESS, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// Missing values
			Element element = Util.buildDDMSElement(BoundingBox.getName(version), null);
			getInstance("westbound longitude is required.", element);

			// Not Double
			element = buildComponentElement("west", String.valueOf(TEST_EAST), String.valueOf(TEST_SOUTH), String
				.valueOf(TEST_NORTH));
			getInstance("westbound longitude is required.", element);

			// Longitude too small
			element = buildComponentElement("-181", String.valueOf(TEST_EAST), String.valueOf(TEST_SOUTH), String
				.valueOf(TEST_NORTH));
			getInstance("A longitude value must be between", element);

			// Longitude too big
			element = buildComponentElement("181", String.valueOf(TEST_EAST), String.valueOf(TEST_SOUTH), String
				.valueOf(TEST_NORTH));
			getInstance("A longitude value must be between", element);

			// Latitude too small
			element = buildComponentElement(String.valueOf(TEST_WEST), String.valueOf(TEST_EAST), "-91", String
				.valueOf(TEST_NORTH));
			getInstance("A latitude value must be between", element);

			// Latitude too big
			element = buildComponentElement(String.valueOf(TEST_WEST), String.valueOf(TEST_EAST), "91", String
				.valueOf(TEST_NORTH));
			getInstance("A latitude value must be between", element);
		}
	}

	public void testNorthboundLatitudeValiation() {
		// Issue #65
		getInstance("A latitude value must be between", TEST_WEST, TEST_EAST, TEST_SOUTH, -91);
		getInstance("A latitude value must be between", TEST_WEST, TEST_EAST, TEST_SOUTH, 91);
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// Longitude too small
			getInstance("A longitude value must be between", -181, TEST_EAST, TEST_SOUTH, TEST_NORTH);

			// Longitude too big
			getInstance("A longitude value must be between", 181, TEST_EAST, TEST_SOUTH, TEST_NORTH);

			// Latitude too small
			getInstance("A latitude value must be between", TEST_WEST, TEST_EAST, -91, TEST_NORTH);

			// Latitude too big
			getInstance("A latitude value must be between", TEST_WEST, TEST_EAST, 91, TEST_NORTH);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			BoundingBox component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingBox elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			BoundingBox dataComponent = getInstance(SUCCESS, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
			assertEquals(elementComponent, dataComponent);
			assertEquals(elementComponent.hashCode(), dataComponent.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingBox elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			BoundingBox dataComponent = getInstance(SUCCESS, 10, TEST_EAST, TEST_SOUTH, TEST_NORTH);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_WEST, 10, TEST_SOUTH, TEST_NORTH);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_WEST, TEST_EAST, 10, TEST_NORTH);
			assertFalse(elementComponent.equals(dataComponent));

			dataComponent = getInstance(SUCCESS, TEST_WEST, TEST_EAST, TEST_SOUTH, 10);
			assertFalse(elementComponent.equals(dataComponent));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingBox elementComponent = getInstance(SUCCESS, getValidElement(sVersion));
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(elementComponent.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingBox component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());

			component = getInstance(SUCCESS, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
			assertEquals(getExpectedOutput(true), component.toHTML());
			assertEquals(getExpectedOutput(false), component.toText());
		}
	}

	public void testXMLOutput() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingBox component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(getExpectedXMLOutput(true), component.toXML());

			component = getInstance(SUCCESS, TEST_WEST, TEST_EAST, TEST_SOUTH, TEST_NORTH);
			assertEquals(getExpectedXMLOutput(false), component.toXML());
		}
	}

	public void testDoubleEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			BoundingBox component = getInstance(SUCCESS, getValidElement(sVersion));
			assertEquals(component.getWestBL(), Double.valueOf(TEST_WEST));
			assertEquals(component.getEastBL(), Double.valueOf(TEST_EAST));
			assertEquals(component.getSouthBL(), Double.valueOf(TEST_SOUTH));
			assertEquals(component.getNorthBL(), Double.valueOf(TEST_NORTH));
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			BoundingBox component = getInstance(SUCCESS, getValidElement(sVersion));
			BoundingBox.Builder builder = new BoundingBox.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			BoundingBox.Builder builder = new BoundingBox.Builder();
			assertNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setWestBL(TEST_WEST);
			assertFalse(builder.isEmpty());

		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			BoundingBox.Builder builder = new BoundingBox.Builder();
			builder.setEastBL(Double.valueOf(TEST_EAST));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "A ddms:boundingBox requires");
			}
			builder.setWestBL(Double.valueOf(TEST_WEST));
			builder.setNorthBL(Double.valueOf(TEST_NORTH));
			builder.setSouthBL(Double.valueOf(TEST_SOUTH));
			builder.commit();
		}
	}
}
