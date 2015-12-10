/* Copyright 2010 - 2016 by Brian Uri!
   
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
   home page is located at https://ddmsence.urizone.net/
 */
package buri.ddmsence.util;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import org.junit.Test;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
import buri.ddmsence.ddms.format.Extent;
import buri.ddmsence.ddms.resource.Identifier;
import buri.ddmsence.ddms.summary.gml.SRSAttributes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * A collection of Util tests.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class UtilTest extends AbstractBaseTestCase {

	protected static final String TEST_NAMESPACE = DDMSVersion.getCurrentVersion().getNamespace();

	public UtilTest() {
		super(null);
	}

	@Test
	public void testPrivateConstructorForCoverage() throws Exception {
		Constructor<Util> constructor = Util.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}
	
	@Test
	public void testToXmlGregorianCalendar() {
		// Standard XML dates
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:00.1-00:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:00.01"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:00.001Z"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:00-00:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:00Z"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:22-00:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:22"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:22Z"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:22.1-00:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:22.01"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57:22.001Z"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04Z"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04-00:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08Z"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-00:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013Z"));
		assertNotNull(Util.toXMLGregorianCalendar("2013"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-00:00"));

		// Custom DDMS dates
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57-00:00"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57"));
		assertNotNull(Util.toXMLGregorianCalendar("2013-08-04T11:57Z"));

		// Other values
		assertNull(Util.toXMLGregorianCalendar(""));
		assertNull(Util.toXMLGregorianCalendar("Not Applicable"));
		assertNull(Util.toXMLGregorianCalendar("Unknown"));
	}
	
	@Test
	public void testGetJSONArray() throws InvalidDDMSException {
		// Double
		List<Double> doubles = new ArrayList<Double>();
		doubles.add(Double.valueOf(1));
		JsonArray array = Util.getJSONArray(doubles);
		assertEquals(1, array.size());
		
		// String
		List<String> strings = new ArrayList<String>();
		strings.add("Test");
		strings.add("Dog");
		array = Util.getJSONArray(strings);
		assertEquals("Test", array.get(0).getAsString());
		assertEquals("Dog", array.get(1).getAsString());
		
		// AbstractBaseComponent
		List<Extent> components = new ArrayList<Extent>();
		components.add(new Extent("qualifier", "value"));
		array = Util.getJSONArray(components);
		assertEquals(1, array.size());
		
		// Unknown
		try {
			List<Long> unknowns = new ArrayList<Long>();
			unknowns.add(Long.valueOf(1));
			Util.getJSONArray(unknowns);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "Unexpected class");
		}
	}
	
	@Test
	public void testaddNonEmptyJsonProperty() throws InvalidDDMSException {
		JsonObject object = new JsonObject();
		
		// AbstractAttributeGroup
		Util.addNonEmptyJsonProperty(object, "attr", new SRSAttributes(null, null, null, null));
		assertFalse(object.has("attr"));
		Util.addNonEmptyJsonProperty(object, "attr", new SRSAttributes("test", null, null, null));
		assertTrue(object.has("attr"));
		
		// Boolean
		Util.addNonEmptyJsonProperty(object, "boolean", (Boolean) null);
		assertFalse(object.has("boolean"));
		Util.addNonEmptyJsonProperty(object, "boolean", Boolean.TRUE);
		assertTrue(object.has("boolean"));

		// Double
		Util.addNonEmptyJsonProperty(object, "double", (Double) null);
		assertFalse(object.has("double"));
		Util.addNonEmptyJsonProperty(object, "double", Double.valueOf(1));
		assertTrue(object.has("double"));
		
		// Integer
		Util.addNonEmptyJsonProperty(object, "integer", (Integer) null);
		assertFalse(object.has("integer"));
		Util.addNonEmptyJsonProperty(object, "integer", Integer.valueOf(1));
		assertTrue(object.has("integer"));
		
		// JsonArray
		JsonArray array = new JsonArray();
		Util.addNonEmptyJsonProperty(object, "array", array);
		assertFalse(object.has("array"));
		array.add(new JsonPrimitive("test"));
		Util.addNonEmptyJsonProperty(object, "array", array);
		assertTrue(object.has("array"));
		
		// JsonObject
		Util.addNonEmptyJsonProperty(object, "object", (JsonObject) null);
		assertFalse(object.has("object"));
		Util.addNonEmptyJsonProperty(object, "object", new JsonObject());
		assertTrue(object.has("object"));
		
		// String
		Util.addNonEmptyJsonProperty(object, "string", "");
		assertFalse(object.has("string"));
		Util.addNonEmptyJsonProperty(object, "string", "test");
		assertTrue(object.has("string"));
		
		// Unknown
		try {
			Util.addNonEmptyJsonProperty(object, "byte", Byte.valueOf("1"));
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "Unexpected class");
		}
	}
		
	@Test
	public void testGetNonNullStringNull() {
		assertEquals("", Util.getNonNullString(null));
	}

	@Test
	public void testGetNonNullStringValue() {
		assertEquals("Test", Util.getNonNullString("Test"));
	}

	@Test
	public void testGetXsList() {
		List<String> list = new ArrayList<String>();
		list.add("Test");
		list.add("Dog");
		assertEquals("Test Dog", Util.getXsList(list));
	}

	@Test
	public void testGetXsListNull() {
		assertEquals("", Util.getXsList(null));
	}

	@Test
	public void testBooleanHashCodeTrue() {
		assertEquals(1, Util.booleanHashCode(true));
	}

	@Test
	public void testBooleanHashCodeFalse() {
		assertEquals(0, Util.booleanHashCode(false));
	}

	@Test
	public void testIsEmptyNull() {
		assertTrue(Util.isEmpty(null));
	}

	@Test
	public void testIsEmptyWhitespace() {
		assertTrue(Util.isEmpty(" "));
	}

	@Test
	public void testIsEmptyEmptyString() {
		assertTrue(Util.isEmpty(""));
	}

	@Test
	public void testIsEmptyNot() {
		assertFalse(Util.isEmpty("DDMSence"));
	}

	@Test
	public void testContainsEmptyStringFalse() {
		assertFalse(Util.containsOnlyEmptyValues(null));
		List<String> list = new ArrayList<String>();
		list.add("dog");
		list.add("");
		list.add(null);
		assertFalse(Util.containsOnlyEmptyValues(list));
	}

	@Test
	public void testContainsEmptyStringTrue() {
		List<String> list = new ArrayList<String>();
		assertTrue(Util.containsOnlyEmptyValues(list));
		list.add("");
		assertTrue(Util.containsOnlyEmptyValues(list));
		list.clear();
		list.add(null);
		assertTrue(Util.containsOnlyEmptyValues(list));
	}

	@Test
	public void testGetFirstDDMSChildValueNullParent() {
		try {
			Util.getFirstDDMSChildValue(null, "test");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "parent element must exist.");
		}
	}

	@Test
	public void testGetFirstDDMSChildValueNullChild() {
		try {
			Util.getFirstDDMSChildValue(Util.buildDDMSElement("test", null), null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "child name must exist.");
		}
	}

	@Test
	public void testGetFirstDDMSChildValueWrongNamespace() {
		Element element = Util.buildElement("ddmsence", "test", "https://ddmsence.urizone.net/", null);
		try {
			Util.getFirstDDMSChildValue(element, "child");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "This method should only be called");
		}
	}

	@Test
	public void testGetFirstDDMSChildValueIndependentOfCurrentVersion() {
		DDMSVersion.setCurrentVersion("3.0");
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", "childText1"));
		element.appendChild(Util.buildDDMSElement("child", "childText2"));
		DDMSVersion.setCurrentVersion("2.0");
		String value = Util.getFirstDDMSChildValue(element, "child");
		assertEquals("childText1", value);
	}

	@Test
	public void testGetFirstDDMSChildValueNoValue() {
		String value = Util.getFirstDDMSChildValue(Util.buildDDMSElement("test", null), "unknown");
		assertEquals("", value);
	}

	@Test
	public void testGetFirstDDMSChildValueWithValue() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", "childText1"));
		element.appendChild(Util.buildDDMSElement("child", "childText2"));
		String value = Util.getFirstDDMSChildValue(element, "child");
		assertEquals("childText1", value);
	}

	@Test
	public void testGetDDMSChildValuesNullParent() {
		try {
			Util.getDDMSChildValues(null, "test");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "parent element must exist.");
		}
	}

	@Test
	public void testGetDDMSChildValuesNullChild() {
		try {
			Util.getDDMSChildValues(Util.buildDDMSElement("test", null), null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "child name must exist.");
		}
	}

	@Test
	public void testGetDDMSChildValuesWrongNamespace() {
		Element element = Util.buildElement("ddmsence", "test", "https://ddmsence.urizone.net/", null);
		element.appendChild(Util.buildDDMSElement("child", "child1"));
		element.appendChild(Util.buildDDMSElement("child", "child2"));
		try {
			Util.getDDMSChildValues(element, "child");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "This method should only be called");
		}
	}

	@Test
	public void testGetDDMSChildValuesIndependentOfCurrentVersion() {
		DDMSVersion.setCurrentVersion("3.0");
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", "child1"));
		element.appendChild(Util.buildDDMSElement("child", "child2"));
		DDMSVersion.setCurrentVersion("2.0");
		List<String> values = Util.getDDMSChildValues(element, "child");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals("child1", values.get(0));
		assertEquals("child2", values.get(1));
	}

	@Test
	public void testGetDDMSChildValuesNoValues() {
		Element element = Util.buildDDMSElement("test", null);
		List<String> values = Util.getDDMSChildValues(element, "unknown");
		assertNotNull(values);
		assertTrue(values.isEmpty());
	}

	@Test
	public void testGetDDMSChildValuesEmptyValues() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", ""));
		List<String> values = Util.getDDMSChildValues(element, "child");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("", values.get(0));
	}

	@Test
	public void testGetDDMSChildValuesWithValues() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", "child1"));
		element.appendChild(Util.buildDDMSElement("child", "child2"));
		List<String> values = Util.getDDMSChildValues(element, "child");
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals("child1", values.get(0));
		assertEquals("child2", values.get(1));
	}

	@Test
	public void testRequireOutputFormat() {
		try {
			Util.requireHTMLText(OutputFormat.JSON);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "This method");
		}
		Util.requireHTMLText(OutputFormat.HTML);
	}
	
	@Test
	public void testRequireDDMSValueNull() {
		try {
			Util.requireDDMSValue("description", null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "description must exist.");
		}
	}

	@Test
	public void testRequireDDMSValueEmpty() {
		try {
			Util.requireDDMSValue("description", "");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "description must exist.");
		}
	}

	@Test
	public void testRequireDDMSValueNotEmpty() {
		try {
			Util.requireDDMSValue("description", "DDMSence");
		}
		catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}
	}

	@Test
	public void testRequireDDMSDateFormatSuccess() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);
			try {
				Util.requireDDMSDateFormat("2012", version.getNamespace());
				Util.requireDDMSDateFormat("2012-01", version.getNamespace());
				Util.requireDDMSDateFormat("2012-01-01", version.getNamespace());
				Util.requireDDMSDateFormat("2012-01-01T01:02:03Z", version.getNamespace());
				Util.requireDDMSDateFormat("2012-01-01T01:02:03+05:00", version.getNamespace());
				Util.requireDDMSDateFormat("2012-01-01T01:02:03.4Z", version.getNamespace());
				Util.requireDDMSDateFormat("2012-01-01T01:02:03.4+05:00", version.getNamespace());
				if (version.isAtLeast("4.1")) {
					Util.requireDDMSDateFormat("2012-01-01T01:02Z", version.getNamespace());
					Util.requireDDMSDateFormat("2012-01-01T01:02+05:00", version.getNamespace());
				}
			}
			catch (InvalidDDMSException e) {
				fail("Did not allow valid data.");
			}
		}
	}

	@Test
	public void testRequireDDMSDateFormatFailure() {
		for (String sVersion : getSupportedVersions()) {
			DDMSVersion version = DDMSVersion.setCurrentVersion(sVersion);

			// Another XSD date type
			try {
				Util.requireDDMSDateFormat("---31", version.getNamespace());
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The date datatype must be");
			}

			// Not a date
			try {
				Util.requireDDMSDateFormat("baboon", version.getNamespace());
				fail("Allowed invalid data.");
			}
			catch (InvalidDDMSException e) {
				expectMessage(e, "The date datatype must be");
			}

			// ddms:DateHourMinType (specifying 4.0.1 here, since you can't tell if it's 4.0.1 or 4.1 by XML namespace)
			if (!version.isAtLeast("4.0.1")) {
				try {
					Util.requireDDMSDateFormat("2012-01-01T01:02Z", version.getNamespace());
					fail("Allowed invalid data.");
				}
				catch (InvalidDDMSException e) {
					expectMessage(e, "The date datatype must be");
				}
				try {
					Util.requireDDMSDateFormat("2012-01-01T01:02+05:00", version.getNamespace());
					fail("Allowed invalid data.");
				}
				catch (InvalidDDMSException e) {
					expectMessage(e, "The date datatype must be");
				}
			}
		}
	}

	@Test
	public void testRequireDDMSQNameSuccess() {
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireDDMSQName(element, "name");
		}
		catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}
	}

	@Test
	public void testRequireDDMSQNameFailure() {
		// Bad URI
		try {
			Element element = Util.buildElement("ic", "name", "urn:us:gov:ic:ism", null);
			Util.requireDDMSQName(element, "name");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Unexpected namespace URI and local name encountered");
		}

		// Bad Name
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireDDMSQName(element, "newName");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Unexpected namespace URI and local name encountered");
		}
	}

	@Test
	public void testRequireQNameSuccess() {
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireQName(element, DDMSVersion.getCurrentVersion().getNamespace(), "name");
		}
		catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}

		// Empty URI
		try {
			Element element = Util.buildElement("", "name", "", null);
			Util.requireQName(element, null, "name");
		}
		catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}
	}

	@Test
	public void testRequireQNameFailure() {
		// Bad URI
		try {
			Element element = Util.buildElement("ic", "name", "urn:us:gov:ic:ism", null);
			Util.requireQName(element, "", "name");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Unexpected namespace URI and local name encountered");
		}

		// Bad Name
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireQName(element, DDMSVersion.getCurrentVersion().getNamespace(), "newName");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Unexpected namespace URI and local name encountered");
		}
	}

	@Test
	public void testRequireValueNull() {
		try {
			Util.requireValue("description", null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "description must exist.");
		}
	}

	@Test
	public void testRequireValueEmpty() {
		try {
			Util.requireValue("description", "");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "description must exist.");
		}
	}

	@Test
	public void testRequireValueNotEmpty() {
		try {
			Util.requireValue("description", "DDMSence");
		}
		catch (IllegalArgumentException e) {
			fail("Did not allow valid data.");
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountNullParent() {
		try {
			Util.requireBoundedChildCount(null, "test", 0, 1);
			fail("Allowed illegal argument data.");
		}
		catch (InvalidDDMSException e) {
			fail("Allowed illegal argument data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "parent element must exist.");
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountNullChild() {
		try {
			Util.requireBoundedChildCount(Util.buildDDMSElement("test", null), null, 0, 1);
			fail("Allowed illegal argument data.");
		}
		catch (InvalidDDMSException e) {
			fail("Allowed illegal argument data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "child name must exist.");
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountBounded() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("name", "nameValue"));
		try {
			Util.requireBoundedChildCount(element, "name", 0, 2);
		}
		catch (InvalidDDMSException e) {
			fail("Prevented valid data.");
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountExactly1() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("name", "nameValue"));
		try {
			Util.requireBoundedChildCount(element, "phone", 1, 1);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("Exactly 1 phone element must exist.", e.getMessage());
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountExactlyX() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("name", "nameValue"));
		try {
			Util.requireBoundedChildCount(element, "phone", 2, 2);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("Exactly 2 phone elements must exist.", e.getMessage());
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountNoMoreThan1() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		try {
			Util.requireBoundedChildCount(element, "phone", 0, 1);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("No more than 1 phone element must exist.", e.getMessage());
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountNoMoreThanX() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		try {
			Util.requireBoundedChildCount(element, "phone", 0, 2);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("No more than 2 phone elements must exist.", e.getMessage());
		}
	}

	@Test
	public void testRequireBoundedDDMSChildCountGenericUnbounded() {
		Element element = Util.buildDDMSElement("test", null);
		try {
			Util.requireBoundedChildCount(element, "phone", 1, 5);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("The number of phone elements must be between 1 and 5.", e.getMessage());
		}
	}

	@Test
	public void testRequireBoundedChildCountIndependentOfCurrentVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		DDMSVersion.setCurrentVersion("2.0");
		Util.requireBoundedChildCount(element, "phone", 1, 1);
	}

	@Test
	public void testRequireValidNCNamesNull() throws InvalidDDMSException {
		Util.requireValidNCNames(null);
	}

	@Test
	public void testRequireValidNCNamesValid() throws InvalidDDMSException {
		List<String> names = new ArrayList<String>();
		names.add("test");
		Util.requireValidNCNames(names);
	}

	@Test
	public void testRequireValidNCNamesInvalid() throws InvalidDDMSException {
		List<String> names = new ArrayList<String>();
		names.add("1test");
		try {
			Util.requireValidNCNames(names);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "\"1test\" is not a valid NCName.");
		}
	}

	@Test
	public void testRequireValidNCNameNull() {
		try {
			Util.requireValidNCName(null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "\"null\" is not a valid NCName.");
		}
	}

	@Test
	public void testRequireValidNCNameInvalidName() {
		try {
			Util.requireValidNCName("1TEST");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "\"1TEST\" is not a valid NCName.");
		}
	}

	@Test
	public void testRequireValidNCNameInvalidNamespace() {
		try {
			Util.requireValidNCName("xmlns:TEST");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "\"xmlns:TEST\" is not a valid NCName.");
		}
	}

	@Test
	public void testRequireValidNCNameValid() throws InvalidDDMSException {
		Util.requireValidNCName("name");
	}

	@Test
	public void testRequireValidNMTokenNull() {
		try {
			Util.requireValidNMToken(null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "\"null\" is not a valid NMTOKEN.");
		}
	}

	@Test
	public void testRequireValidNMTokenValidName() throws InvalidDDMSException {
		Util.requireValidNMToken("1TEST");
	}

	@Test
	public void testRequireValidNMTokenValidNamespace() throws InvalidDDMSException {
		Util.requireValidNMToken("xmlns:TEST");
	}

	@Test
	public void testRequireValidNMTokenValid() throws InvalidDDMSException {
		Util.requireValidNMToken("name");
	}

	@Test
	public void testRequireDDMSValidURIValid() throws InvalidDDMSException {
		Util.requireDDMSValidURI("test");
	}

	@Test
	public void testRequireDDMSValidURIInvalid() {
		try {
			Util.requireDDMSValidURI(":::::");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Invalid URI");
		}
	}

	@Test
	public void testRequireDDMSValidURINull() throws InvalidDDMSException {
		try {
			Util.requireDDMSValidURI(null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "uri must exist.");
		}
	}

	@Test
	public void testRequireValidLongitudeNull() {
		try {
			Util.requireValidLongitude(null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A longitude value must be between");
		}
	}

	@Test
	public void testRequireValidLongitudeOutOfBounds() {
		try {
			Util.requireValidLongitude(new Double(-181));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A longitude value must be between");
		}
		try {
			Util.requireValidLongitude(new Double(181));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A longitude value must be between");
		}
	}

	@Test
	public void testRequireValidLongitudeValid() throws InvalidDDMSException {
		Util.requireValidLongitude(new Double(0));
	}

	@Test
	public void testRequireValidLatitudeNull() {
		try {
			Util.requireValidLatitude(null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A latitude value must be between");
		}
	}

	@Test
	public void testRequireValidLatitudeOutOfBounds() {
		try {
			Util.requireValidLatitude(new Double(-91));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A latitude value must be between");
		}
		try {
			Util.requireValidLatitude(new Double(91));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A latitude value must be between");
		}
	}

	@Test
	public void testRequireValidLatitudeValid() throws InvalidDDMSException {
		Util.requireValidLatitude(new Double(0));
	}

	@Test
	public void testIsBoundedBadRange() {
		try {
			Util.isBounded(0, 10, 0);
			fail("Did not stop on bad range.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "Invalid number range: 10 to 0");
		}
	}

	@Test
	public void testIsBoundedLow() {
		assertFalse(Util.isBounded(0, 1, 3));
	}

	@Test
	public void testIsBoundedHigh() {
		assertFalse(Util.isBounded(4, 1, 3));
	}

	@Test
	public void testIsBoundedMiddle() {
		assertTrue(Util.isBounded(1, 0, 2));
	}

	@Test
	public void testIsBoundedLowEdge() {
		assertTrue(Util.isBounded(1, 1, 3));
	}

	@Test
	public void testIsBoundedHighEdge() {
		assertTrue(Util.isBounded(3, 1, 3));
	}

	@Test
	public void testIsBoundedOnlyOne() {
		assertTrue(Util.isBounded(1, 1, 1));
	}

	@Test
	public void testListEqualsNullLists() {
		try {
			Util.listEquals(null, null);
			fail("Did not stop on bad data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "Null lists cannot be compared.");
		}

		try {
			Util.listEquals(new ArrayList<String>(), null);
			fail("Did not stop on bad data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "Null lists cannot be compared.");
		}
	}

	@Test
	public void testListEqualsSameList() {
		List<String> list = new ArrayList<String>();
		list.add("Test");
		assertTrue(Util.listEquals(list, list));
	}

	@Test
	public void testListEqualsEmptyLists() {
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		assertTrue(Util.listEquals(list1, list2));
	}

	@Test
	public void testListEqualsSizes() {
		List<String> list1 = new ArrayList<String>();
		list1.add("Test");
		List<String> list2 = new ArrayList<String>();
		list2.add("Test");
		list2.add("Test2");
		assertFalse(Util.listEquals(list1, list2));
	}

	@Test
	public void testListEqualsNullValues() {
		List<String> list1 = new ArrayList<String>();
		list1.add(null);
		list1.add("Test2");
		List<String> list2 = new ArrayList<String>();
		list2.add(null);
		list2.add("Test2");
		assertTrue(Util.listEquals(list1, list2));
	}

	@Test
	public void testListEqualsNullValue() {
		List<String> list1 = new ArrayList<String>();
		list1.add(null);
		list1.add("Test2");
		List<String> list2 = new ArrayList<String>();
		list2.add("Test");
		list2.add("Test2");
		assertFalse(Util.listEquals(list1, list2));
	}

	@Test
	public void testListEqualsDifferentValue() {
		List<String> list1 = new ArrayList<String>();
		list1.add("Test1");
		List<String> list2 = new ArrayList<String>();
		list2.add("Test2");
		assertFalse(Util.listEquals(list1, list2));
	}

	@Test
	public void testXmlEscape() {
		String testString = "<test>\"Brian's DDMSense & DDMS\"</test>";
		assertEquals("&lt;test&gt;&quot;Brian&apos;s DDMSense &amp; DDMS&quot;&lt;/test&gt;",
			Util.xmlEscape(testString));
		assertEquals(null, Util.xmlEscape(null));
	}

	@Test
	public void testCapitalizeEmpty() {
		assertEquals(null, Util.capitalize(null));
	}

	@Test
	public void testCapitalizeOneChar() {
		assertEquals("A", Util.capitalize("a"));
	}

	@Test
	public void testCapitalizeNotLetter() {
		assertEquals("123", Util.capitalize("123"));
	}

	@Test
	public void testCapitalizeSuccess() {
		assertEquals("Ddms", Util.capitalize("ddms"));
	}

	@Test
	public void testDecapitalizeEmpty() {
		assertEquals(null, Util.decapitalize(null));
	}

	@Test
	public void testDecapitalizeOneChar() {
		assertEquals("a", Util.decapitalize("A"));
	}

	@Test
	public void testDecapitalizeNotLetter() {
		assertEquals("123", Util.decapitalize("123"));
	}

	@Test
	public void testDecapitalizeSuccess() {
		assertEquals("ddms", Util.decapitalize("Ddms"));
	}
	
	@Test
	public void testBuildElementEmptyPrefix() {
		Element element = Util.buildElement(null, "test", "", null);
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals("", element.getNamespaceURI());
		assertEquals("", element.getNamespacePrefix());
		assertEquals("test", element.getQualifiedName());

	}

	@Test
	public void testBuildDDMSElement() {
		Element element = Util.buildDDMSElement("test", null);
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals(TEST_NAMESPACE, element.getNamespaceURI());
		assertEquals(PropertyReader.getPrefix("ddms"), element.getNamespacePrefix());
	}

	@Test
	public void testBuildDDMSElementNullName() {
		try {
			Util.buildDDMSElement(null, null);
			fail("Method allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "name must exist.");
		}
	}

	@Test
	public void testBuildDDMSElementChildText() {
		Element element = Util.buildDDMSElement("test", "testValue");
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals("testValue", element.getValue());
		assertEquals(TEST_NAMESPACE, element.getNamespaceURI());
		assertEquals(PropertyReader.getPrefix("ddms"), element.getNamespacePrefix());
	}

	@Test
	public void testBuildDDMSElementNoChildText() {
		Element element = Util.buildDDMSElement("test", null);
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals("", element.getValue());
		assertEquals(TEST_NAMESPACE, element.getNamespaceURI());
		assertEquals(PropertyReader.getPrefix("ddms"), element.getNamespacePrefix());
	}

	@Test
	public void testBuildAttribute() {
		Attribute attribute = Util.buildAttribute("ddms", "test", DDMSVersion.getCurrentVersion().getNamespace(),
			"testValue");
		assertNotNull(attribute);
		assertEquals("test", attribute.getLocalName());
		assertEquals("testValue", attribute.getValue());
		assertEquals(TEST_NAMESPACE, attribute.getNamespaceURI());
		assertEquals(PropertyReader.getPrefix("ddms"), attribute.getNamespacePrefix());
	}

	@Test
	public void testBuildAttributeEmptyValues() {
		Attribute attribute = Util.buildAttribute(null, "test", null, "testValue");
		assertNotNull(attribute);
		assertEquals("test", attribute.getLocalName());
		assertEquals("testValue", attribute.getValue());
		assertEquals("", attribute.getNamespaceURI());
		assertEquals("", attribute.getNamespacePrefix());
	}

	@Test
	public void testBuildDDMSAttribute() {
		Attribute attribute = Util.buildDDMSAttribute("test", "testValue");
		assertNotNull(attribute);
		assertEquals("test", attribute.getLocalName());
		assertEquals("testValue", attribute.getValue());
		assertEquals(TEST_NAMESPACE, attribute.getNamespaceURI());
		assertEquals(PropertyReader.getPrefix("ddms"), attribute.getNamespacePrefix());
	}

	@Test
	public void testBuildDDMSAttributeNullName() {
		try {
			Util.buildDDMSAttribute(null, "testValue");
			fail("Method allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "name must exist.");
		}
	}

	@Test
	public void testBuildDDMSAttributeNullValue() {
		try {
			Util.buildDDMSAttribute("test", null);
			fail("Method allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "value must exist.");
		}
	}

	@Test
	public void testRequireSameVersionSuccess() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Identifier identifier = new Identifier("Test", "Value");
		Identifier identifier2 = new Identifier("Test", "Value");
		Util.requireCompatibleVersion(identifier, identifier2);
	}

	@Test
	public void testRequireSameVersionFailure() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			Identifier identifier = new Identifier("Test", "Value");
			DDMSVersion.setCurrentVersion("3.0");
			Identifier identifier2 = new Identifier("Test", "Value");
			Util.requireCompatibleVersion(identifier, identifier2);
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A child component, ddms:identifier");
		}
	}

	@Test
	public void testAddDdmsAttributeEmptyValue() {
		Element element = new Element("test", "http://test.com");
		Util.addDDMSAttribute(element, "testAttribute", null);
		assertNull(element.getAttribute("testAttribute", DDMSVersion.getCurrentVersion().getNamespace()));
	}

	@Test
	public void testAddDdmsAttributeValue() {
		Element element = new Element("test", "http://test.com");
		Util.addDDMSAttribute(element, "testAttribute", "dog");
		Attribute attr = element.getAttribute("testAttribute", DDMSVersion.getCurrentVersion().getNamespace());
		assertEquals("ddms", attr.getNamespacePrefix());
		assertEquals(DDMSVersion.getCurrentVersion().getNamespace(), attr.getNamespaceURI());
		assertEquals("testAttribute", attr.getLocalName());
		assertEquals("dog", element.getAttributeValue("testAttribute", DDMSVersion.getCurrentVersion().getNamespace()));
	}

	@Test
	public void testAddDdmsChildElementEmptyValue() {
		Element element = new Element("test", "http://test.com");
		Util.addDDMSChildElement(element, "child", null);
		assertEquals(0, element.getChildElements().size());
	}

	@Test
	public void testAddDdmsChildElementValue() {
		Element element = new Element("test", "http://test.com");
		Util.addDDMSChildElement(element, "child", "dog");
		assertEquals(1, element.getChildElements().size());
		Element child = element.getFirstChildElement("child", DDMSVersion.getCurrentVersion().getNamespace());
		assertEquals("ddms", child.getNamespacePrefix());
		assertEquals(DDMSVersion.getCurrentVersion().getNamespace(), child.getNamespaceURI());
		assertEquals("child", child.getLocalName());
		assertEquals("dog", child.getValue());
	}

	@Test
	public void testGetDatatypeFactory() {
		assertNotNull(Util.getDataTypeFactory());
	}

	@Test
	public void testGetAsList() {
		assertTrue(Util.getXsListAsList(null).isEmpty());
		assertTrue(Util.getXsListAsList("").isEmpty());
		List<String> list = Util.getXsListAsList("a b");
		assertEquals(2, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		list = Util.getXsListAsList("a      b");
		assertEquals(2, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
	}

	@Test
	public void testBuildXmlDocument() throws Exception {
		File testFile = new File(PropertyReader.getProperty("test.unit.data") + "5.0/", "resource.xml");
		String expectedXmlOutput = new DDMSReader(DDMSVersion.getVersionFor("5.0")).getDDMSResource(testFile).toXML();
		assertEquals(expectedXmlOutput, Util.buildXmlDocument(new FileInputStream(testFile)).getRootElement().toXML());
	}

	@Test
	public void testBuildXmlDocumentBadFile() throws Exception {
		try {
			Util.buildXmlDocument(null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "input stream must exist.");
		}

		try {
			Util.buildXmlDocument(new ByteArrayInputStream("Not an XML File".getBytes()));
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			expectMessage(e, "Content is not allowed in prolog.");
		}
	}

	@Test
	public void testSchematronQueryBinding() throws Exception {
		Document schDocument = Util.buildXmlDocument(new FileInputStream(
			"data/sample/schematron/testPublisherValueXslt1.sch"));
		assertEquals("xslt", Util.getSchematronQueryBinding(schDocument));
		schDocument = Util.buildXmlDocument(new FileInputStream("data/sample/schematron/testPositionValuesXslt2.sch"));
		assertEquals("xslt2", Util.getSchematronQueryBinding(schDocument));
	}
	
	@Test
	public void testCommitXml() throws InvalidDDMSException {
		Util.commitXml("<test />");
	}
	
	@Test
	public void testCommitXmlFailure() {
		try {
			Util.commitXml("notXml");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Could not create a valid element");
		}
	}
}
