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
package buri.ddmsence.ddms.summary.gml;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractComponentTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to the SRS attributes on gml: elements</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class SRSAttributesTest extends AbstractComponentTestCase {

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
	 * Returns a canned fixed value attributes object for testing higher-level components.
	 * 
	 * @return SRSAttributes
	 */
	public static SRSAttributes getFixture() throws InvalidDDMSException {
		return (new SRSAttributes(TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, TEST_UOM_LABELS));
	}

	/**
	 * Attempts to build a component from a XOM element.
	 * 
	 * @param expectFailure true if this operation is expected to fail, false otherwise
	 * @param element the element to build from
	 * 
	 * @return a valid object
	 */
	private SRSAttributes testConstructor(boolean expectFailure, Element element) {
		SRSAttributes attributes = null;
		try {
			attributes = new SRSAttributes(element);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
	}

	/**
	 * Helper method to create an object which is expected to be valid.
	 * 
	 * @param expectFailure true if this operation is expected to succeed, false otherwise
	 * @param srsName the srsName (optional)
	 * @param srsDimension the srsDimension (optional)
	 * @param axisLabels the axis labels (optional, but should be omitted if no srsName is set)
	 * @param uomLabels the labels for UOM (required when axisLabels is set)
	 * @return a valid object
	 */
	private SRSAttributes testConstructor(boolean expectFailure, String srsName, Integer srsDimension,
		List<String> axisLabels, List<String> uomLabels) {
		SRSAttributes attributes = null;
		try {
			attributes = new SRSAttributes(srsName, srsDimension, axisLabels, uomLabels);
			checkConstructorSuccess(expectFailure);
		} catch (InvalidDDMSException e) {
			checkConstructorFailure(expectFailure, e);
		}
		return (attributes);
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
			Util.addAttribute(element, SRSAttributes.NO_PREFIX, "srsDimension", SRSAttributes.NO_NAMESPACE, 
				String.valueOf(srsDimension));
		}
		Util.addAttribute(element, SRSAttributes.NO_PREFIX, "axisLabels", SRSAttributes.NO_NAMESPACE, axisLabels);
		Util.addAttribute(element, SRSAttributes.NO_PREFIX, "uomLabels", SRSAttributes.NO_NAMESPACE, uomLabels);
	}

	public void testElementConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			Element element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Position.NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(TEST_UOM_LABELS));
			testConstructor(WILL_SUCCEED, element);

			// No optional fields
			element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Position.NAME, DDMSVersion.getCurrentVersion().getGmlNamespace(),
				null);
			testConstructor(WILL_SUCCEED, element);
		}
	}

	public void testDataConstructorValid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// All fields
			testConstructor(WILL_SUCCEED, TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, TEST_UOM_LABELS);

			// No optional fields
			testConstructor(WILL_SUCCEED, null, null, null, null);
		}
	}

	public void testElementConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// srsName not a URI
			Element element = Util.buildDDMSElement(Position.NAME, null);
			addAttributes(element, INVALID_URI, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(TEST_UOM_LABELS));
			testConstructor(WILL_FAIL, element);

			// axisLabels without srsName
			element = Util.buildDDMSElement(Position.NAME, null);
			addAttributes(element, null, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(TEST_UOM_LABELS));
			testConstructor(WILL_FAIL, element);

			// uomLabels without axisLabels
			element = Util.buildDDMSElement(Position.NAME, null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, null, Util.getXsList(TEST_UOM_LABELS));
			testConstructor(WILL_FAIL, element);

			// Non-NCNames in axisLabels
			List<String> newLabels = new ArrayList<String>(TEST_AXIS_LABELS);
			newLabels.add("1TEST");
			element = Util.buildDDMSElement(Position.NAME, null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(newLabels), 
				Util.getXsList(TEST_UOM_LABELS));
			testConstructor(WILL_FAIL, element);

			// Non-NCNames in uomLabels
			newLabels = new ArrayList<String>(TEST_UOM_LABELS);
			newLabels.add("TEST:TEST");
			element = Util.buildDDMSElement(Position.NAME, null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(newLabels));
			testConstructor(WILL_FAIL, element);

			// Dimension is a positive integer
			element = Util.buildDDMSElement(Position.NAME, null);
			addAttributes(element, TEST_SRS_NAME, new Integer(-1), null, Util.getXsList(TEST_UOM_LABELS));
			testConstructor(WILL_FAIL, element);
		}
	}

	public void testDataConstructorInvalid() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// srsName not a URI
			testConstructor(WILL_FAIL, INVALID_URI, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, TEST_UOM_LABELS);

			// axisLabels without srsName
			testConstructor(WILL_FAIL, null, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, TEST_UOM_LABELS);

			// uomLabels without axisLabels
			testConstructor(WILL_FAIL, TEST_SRS_NAME, TEST_SRS_DIMENSION, null, TEST_UOM_LABELS);

			// Non-NCNames in axisLabels
			List<String> newLabels = new ArrayList<String>(TEST_AXIS_LABELS);
			newLabels.add("TEST:TEST");
			testConstructor(WILL_FAIL, TEST_SRS_NAME, TEST_SRS_DIMENSION, newLabels, TEST_UOM_LABELS);

			// Non-NCNames in uomLabels
			newLabels = new ArrayList<String>(TEST_UOM_LABELS);
			newLabels.add("TEST:TEST");
			testConstructor(WILL_FAIL, TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, newLabels);

			// Dimension is a positive integer
			testConstructor(WILL_FAIL, TEST_SRS_NAME, new Integer(-1), TEST_AXIS_LABELS, TEST_UOM_LABELS);
		}
	}

	public void testWarnings() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			// No warnings
			Element element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Position.NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(TEST_UOM_LABELS));
			SRSAttributes component = testConstructor(WILL_SUCCEED, element);
			assertEquals(0, component.getValidationWarnings().size());
		}
	}

	public void testConstructorEquality() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Element element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Position.NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(TEST_UOM_LABELS));
			SRSAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			SRSAttributes dataAttributes = testConstructor(WILL_SUCCEED, TEST_SRS_NAME, TEST_SRS_DIMENSION,
				TEST_AXIS_LABELS, TEST_UOM_LABELS);

			assertEquals(elementAttributes, dataAttributes);
			assertEquals(elementAttributes.hashCode(), dataAttributes.hashCode());
		}
	}

	public void testConstructorInequalityDifferentValues() {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Element element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Position.NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(TEST_UOM_LABELS));
			SRSAttributes elementAttributes = testConstructor(WILL_SUCCEED, element);
			SRSAttributes dataAttributes = testConstructor(WILL_SUCCEED, DIFFERENT_VALUE, TEST_SRS_DIMENSION,
				TEST_AXIS_LABELS, TEST_UOM_LABELS);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_SRS_NAME, null, TEST_AXIS_LABELS, TEST_UOM_LABELS);
			assertFalse(elementAttributes.equals(dataAttributes));

			List<String> newLabels = new ArrayList<String>(TEST_AXIS_LABELS);
			newLabels.add("NewLabel");
			dataAttributes = testConstructor(WILL_SUCCEED, TEST_SRS_NAME, TEST_SRS_DIMENSION, newLabels,
				TEST_UOM_LABELS);
			assertFalse(elementAttributes.equals(dataAttributes));

			dataAttributes = testConstructor(WILL_SUCCEED, TEST_SRS_NAME, TEST_SRS_DIMENSION, TEST_AXIS_LABELS, null);
			assertFalse(elementAttributes.equals(dataAttributes));
		}
	}

	public void testConstructorInequalityWrongClass() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			Element element = Util.buildElement(PropertyReader.getProperty("gml.prefix"), Position.NAME, 
				DDMSVersion.getCurrentVersion().getGmlNamespace(), null);
			addAttributes(element, TEST_SRS_NAME, TEST_SRS_DIMENSION, Util.getXsList(TEST_AXIS_LABELS), 
				Util.getXsList(TEST_UOM_LABELS));
			SRSAttributes attributes = new SRSAttributes(element);
			Rights wrongComponent = new Rights(true, true, true);
			assertFalse(attributes.equals(wrongComponent));
		}
	}
	
	public void testWrongVersionAttributes() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		SRSAttributes attr = getFixture();
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new Position(PositionTest.TEST_COORDS, attr);
			fail("Allowed different versions.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testBuilder() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SRSAttributes component = getFixture();
			
			// Equality after Building
			SRSAttributes.Builder builder = new SRSAttributes.Builder(component);
			assertEquals(builder.commit(), component);
			
			// Validation
			builder = new SRSAttributes.Builder();
			builder.setSrsDimension(new Integer(-1));
			try {
				builder.commit();
				fail("Builder allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				// Good
			}
			
			// Empty Tests
			builder = new SRSAttributes.Builder();
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
	
	public void testBuilderLazyList() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			SRSAttributes.Builder builder = new SRSAttributes.Builder();
			assertNotNull(builder.getUomLabels().get(1));
			assertNotNull(builder.getAxisLabels().get(1));
		}
	}
}
