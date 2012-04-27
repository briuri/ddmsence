/* Copyright 2010 - 2012 by Brian Uri!
   
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
package buri.ddmsence.ddms.summary.gml;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to the SRS attributes on gml: elements </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SRSAttributesTest extends AbstractBaseTestCase {

	protected static final String TEST_SRS_NAME = "http://metadata.dod.mil/mdr/ns/GSIP/crs/WGS84E_2D";
	protected static final Integer TEST_SRS_DIMENSION = 10;
	protected static final List<String> TEST_AXIS_LABELS = new ArrayList<String>();
	static {
		TEST_AXIS_LABELS.add("A");
		TEST_AXIS_LABELS.add("B");
		TEST_AXIS_LABELS.add("C");
	}
	protected static final List<String> TEST_UOM_LABELS = new ArrayList<String>();
	static {
		TEST_UOM_LABELS.add("Meter");
		TEST_UOM_LABELS.add("Meter");
		TEST_UOM_LABELS.add("Meter");
	}

	/**
	 * Constructor
	 */
	public SRSAttributesTest() {
		super(null);
	}

	/**
	 * Returns a fixture object for testing.
	 */
	public static SRSAttributes getFixture() {
		try {
			return (new SRSAttributes(TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, TEST_UOM_LABELS));
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
	private SRSAttributes getInstance(String message, Element element) {
		boolean expectFailure = !Util.isEmpty(message);
		SRSAttributes attributes = null;
		try {
			attributes = new SRSAttributes(element);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (attributes);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param message an expected error message. If empty, the constructor is expected to succeed.
	 * @param srsName the srsName (optional)
	 * @param srsDimension the srsDimension (optional)
	 * @param axisLabels the axis labels (optional, but should be omitted if no srsName is set)
	 * @param uomLabels the labels for UOM (required when axisLabels is set)
	 * @return a valid object
	 */
	private SRSAttributes getInstance(String message, String srsName, Integer srsDimension, List<String> axisLabels,
		List<String> uomLabels) {
		boolean expectFailure = !Util.isEmpty(message);
		SRSAttributes attributes = null;
		try {
			attributes = new SRSAttributes(srsName, srsDimension, axisLabels, uomLabels);
			checkConstructorSuccess(expectFailure);
		}
		catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
			expectMessage(e, message);
		}
		return (attributes);
	}

	/**
	 * Returns the expected HTML or Text output for this unit test
	 */
	private String getExpectedOutput(boolean isHTML) {
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, "srsName", TEST_SRS_NAME));
		text.append(buildOutput(isHTML, "srsDimension", String.valueOf(TEST_SRS_DIMENSION)));
		text.append(buildOutput(isHTML, "axisLabels", Util.getXsList(TEST_AXIS_LABELS)));
		text.append(buildOutput(isHTML, "uomLabels", Util.getXsList(TEST_UOM_LABELS)));
		return (text.toString());
	}

	/**
	 * Helper method to add srs attributes to a XOM element. The element is not validated.
	 * 
	 * @param element element
	 * @param srsName the srsName (optional)
	 * @param srsDimension the srsDimension (optional)
	 * @param axisLabels the axis labels (optional, but should be omitted if no srsName is set)
	 * @param uomLabels the labels for UOM (required when axisLabels is set)
	 */
	private void addAttributes(Element element, String srsName, Integer srsDimension, String axisLabels,
		String uomLabels) {
		Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsName", SRSAttributes.NO_NAMESPACE, srsName);
		if (srsDimension != null) {
			Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsDimension", SRSAttributes.NO_NAMESPACE, String
				.valueOf(srsDimension));
		}
		Util.addAttribute(element, SRSAttributes.NO_PREFIX, "axisLabels", SRSAttributes.NO_NAMESPACE, axisLabels);
		Util.addAttribute(element, SRSAttributes.NO_PREFIX, "uomLabels", SRSAttributes.NO_NAMESPACE, uomLabels);
	}

	public void testElementConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Position.getName(version), version
				.getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			getInstance(SUCCESS, element);

			// No optional fields
			element = Util.buildElement(PropertyReader.getPrefix("gml"), Position.getName(version), version
				.getGmlNamespace(), null);
			getInstance(SUCCESS, element);
		}
	}

	public void testDataConstructorValid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// All fields
			getInstance(SUCCESS, TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, TEST_UOM_LABELS);

			// No optional fields
			getInstance(SUCCESS, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// srsName not a URI
			Element element = Util.buildDDMSElement(Position.getName(version), null);
			addAttributes(element, INVALID_URI, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			getInstance("Invalid URI", element);

			// axisLabels without srsName
			element = Util.buildDDMSElement(Position.getName(version), null);
			addAttributes(element, null, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			getInstance("The axisLabels attribute can only be used", element);

			// uomLabels without axisLabels
			element = Util.buildDDMSElement(Position.getName(version), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, null, Util.getXsList(TEST_UOM_LABELS));
			getInstance("The uomLabels attribute can only be used", element);

			// Non-NCNames in axisLabels
			List<String> newLabels = new ArrayList<String>(TEST_AXIS_LABELS);
			newLabels.add("1TEST");
			element = Util.buildDDMSElement(Position.getName(version), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(newLabels), Util
				.getXsList(TEST_UOM_LABELS));
			getInstance("\"1TEST\" is not a valid NCName.", element);

			// Non-NCNames in uomLabels
			newLabels = new ArrayList<String>(TEST_UOM_LABELS);
			newLabels.add("TEST:TEST");
			element = Util.buildDDMSElement(Position.getName(version), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(newLabels));
			getInstance("\"TEST:TEST\" is not a valid NCName.", element);

			// Dimension is a positive integer
			element = Util.buildDDMSElement(Position.getName(version), null);
			addAttributes(element, TEST_SRS_NAME, Integer.valueOf(-1), null, Util.getXsList(TEST_UOM_LABELS));
			getInstance("The srsDimension must be a positive integer.", element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			// srsName not a URI
			getInstance("Invalid URI", INVALID_URI, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, TEST_UOM_LABELS);

			// axisLabels without srsName
			getInstance("The axisLabels attribute can only be used", null, TEST_SRS_DIMENSION, TEST_AXIS_LABELS,
				TEST_UOM_LABELS);

			// uomLabels without axisLabels
			getInstance("The uomLabels attribute can only be used", TEST_SRS_NAME, TEST_SRS_DIMENSION, null,
				TEST_UOM_LABELS);

			// Non-NCNames in axisLabels
			List<String> newLabels = new ArrayList<String>(TEST_AXIS_LABELS);
			newLabels.add("TEST:TEST");
			getInstance("\"TEST:TEST\" is not a valid NCName.", TEST_SRS_NAME, TEST_SRS_DIMENSION, newLabels,
				TEST_UOM_LABELS);

			// Non-NCNames in uomLabels
			newLabels = new ArrayList<String>(TEST_UOM_LABELS);
			newLabels.add("TEST:TEST");
			getInstance("\"TEST:TEST\" is not a valid NCName.", TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS,
				newLabels);

			// Dimension is a positive integer
			getInstance("The srsDimension must be a positive integer.", TEST_SRS_NAME, Integer.valueOf(-1),
				TEST_AXIS_LABELS, TEST_UOM_LABELS);
		}
	}

	public void testWarnings() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			// No warnings
			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Position.getName(version), version
				.getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			SRSAttributes component = getInstance(SUCCESS, element);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Position.getName(version), version
				.getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			SRSAttributes elementAttributes = getInstance(SUCCESS, element);
			SRSAttributes dataAttributes = getInstance(SUCCESS, TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS,
				TEST_UOM_LABELS);

			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Position.getName(version), version
				.getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			SRSAttributes elementAttributes = getInstance(SUCCESS, element);
			SRSAttributes dataAttributes = getInstance(SUCCESS, DIFFERENT_VALUE, TEST_SRS_DIMENSION, TEST_AXIS_LABELS,
				TEST_UOM_LABELS);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = getInstance(SUCCESS, TEST_SRS_NAME, null, TEST_AXIS_LABELS, TEST_UOM_LABELS);
			assertFalse(elementAttributes.equals(dataAttributes));

			List<String> newLabels = new ArrayList<String>(TEST_AXIS_LABELS);
			newLabels.add("NewLabel");
			dataAttributes = getInstance(SUCCESS, TEST_SRS_NAME, TEST_SRS_DIMENSION, newLabels, TEST_UOM_LABELS);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = getInstance(SUCCESS, TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, null);
			assertFalse(elementAttributes.equals(dataAttributes));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Position.getName(version), version
				.getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			SRSAttributes attributes = new SRSAttributes(element);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(attributes.equals(wrongComponent));
		}
	}

	public void testHTMLTextOutput() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), Position.getName(version), version
				.getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), Util
				.getXsList(TEST_UOM_LABELS));
			SRSAttributes attributes = new SRSAttributes(element);
			assertEquals(getExpectedOutput(true), attributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false), attributes.getOutput(false, ""));

			SRSAttributes dataAttributes = getInstance(SUCCESS, TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS,
				TEST_UOM_LABELS);
			assertEquals(getExpectedOutput(true), dataAttributes.getOutput(true, ""));
			assertEquals(getExpectedOutput(false), dataAttributes.getOutput(false, ""));
		}
	}

	public void testAddTo() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			SRSAttributes component = getFixture();

			Element element = Util.buildElement(PropertyReader.getPrefix("gml"), "sample", version.getGmlNamespace(),
				null);
			component.addTo(element);
			SRSAttributes output = new SRSAttributes(element);
			assertEquals(component, output);
		}
	}

	public void testGetNonNull() throws InvalidDDMSException {
		SRSAttributes component = new SRSAttributes(null, null, null, null);
		SRSAttributes output = SRSAttributes.getNonNullInstance(null);
		assertEquals(component, output);

		output = SRSAttributes.getNonNullInstance(getFixture());
		assertEquals(getFixture(), output);
	}

	public void testWrongVersionAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		SRSAttributes attr = getFixture();
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new Position(PositionTest.TEST_COORDS, attr);
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "These attributes cannot decorate");
		}
	}

	public void testBuilderEquality() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SRSAttributes component = getFixture();
			SRSAttributes.Builder builder = new SRSAttributes.Builder(component);
			assertEquals(component, builder.commit());
		}
	}

	public void testBuilderIsEmpty() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SRSAttributes.Builder builder = new SRSAttributes.Builder();
			assertNotNull(builder.commit());
			assertTrue(builder.isEmpty());
			builder.setSrsName(TEST_SRS_NAME);
			assertFalse(builder.isEmpty());

			builder = new SRSAttributes.Builder();
			builder.setSrsDimension(TEST_SRS_DIMENSION);
			assertFalse(builder.isEmpty());

			builder = new SRSAttributes.Builder();
			builder.getUomLabels().add(null);
			builder.getUomLabels().add("label");
			assertFalse(builder.isEmpty());

			builder = new SRSAttributes.Builder();
			builder.getAxisLabels().add(null);
			builder.getAxisLabels().add("label");
			assertFalse(builder.isEmpty());
		}
	}

	public void testBuilderValidation() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			SRSAttributes.Builder builder = new SRSAttributes.Builder();
			builder.setSrsDimension(Integer.valueOf(-1));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The srsDimension must be a positive integer.");
			}
			builder.setSrsDimension(Integer.valueOf(1));
			builder.commit();
		}
	}

	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);
			SRSAttributes.Builder builder = new SRSAttributes.Builder();
			assertNotNull(builder.getUomLabels().get(1));
			assertNotNull(builder.getAxisLabels().get(1));
		}
	}
}
