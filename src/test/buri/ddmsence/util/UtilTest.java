/* Copyright 2010 by Brian Uri!
   
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
package buri.ddmsence.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.datatype.DatatypeConstants;

import junit.framework.TestCase;
import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.resource.Identifier;

/**
 * A collection of Util tests.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class UtilTest extends TestCase {
		
	protected static final String TEST_NAMESPACE = DDMSVersion.getCurrentVersion().getNamespace();
	private static final String TEST_DATA_DIR = PropertyReader.getProperty("test.unit.data");
	
	/**
	 * Resets the in-use version of DDMS.
	 */
	protected void setUp() throws Exception {
		DDMSVersion.clearCurrentVersion();
	}

	/**
	 * Resets the in-use version of DDMS.
	 */
	protected void tearDown() throws Exception {
		DDMSVersion.clearCurrentVersion();
	}
	
	public void testGetNonNullStringNull() {
		assertEquals("", Util.getNonNullString(null));
	}
	
	public void testGetNonNullStringValue() {
		assertEquals("Test", Util.getNonNullString("Test"));
	}
	
	public void testGetXsList() {
		List<String> list = new ArrayList<String>();
		list.add("Test");
		list.add("Dog");
		assertEquals("Test Dog", Util.getXsList(list));
	}
	
	public void testGetXsListNull() {
		assertEquals("", Util.getXsList(null));
	}
	
	public void testBooleanHashCodeTrue() {
		assertEquals(1, Util.booleanHashCode(true));
	}
	
	public void testBooleanHashCodeFalse() {
		assertEquals(0, Util.booleanHashCode(false));
	}
	
	public void testIsEmptyNull() {
		assertTrue(Util.isEmpty(null));
	}
	
	public void testIsEmptyWhitespace() {
		assertTrue(Util.isEmpty(" "));
	}
	
	public void testIsEmptyEmptyString() {
		assertTrue(Util.isEmpty(""));
	}
	
	public void testIsEmptyNot() {
		assertFalse(Util.isEmpty("DDMSence"));
	}
	
	public void testGetFirstDDMSChildValueNullParent() {
		try {
			Util.getFirstDDMSChildValue(null, "test");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testGetFirstDDMSChildValueNullChild() {
		try {
			Util.getFirstDDMSChildValue(Util.buildDDMSElement("test", null), null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testGetFirstDDMSChildValueWrongNamespace() {
		Element element = Util.buildElement("ddmsence", "test", "http://ddmsence.urizone.net/", null);
		try {
			Util.getFirstDDMSChildValue(element, "child");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testGetFirstDDMSChildValueIndependentOfCurrentVersion() {
		DDMSVersion.setCurrentVersion("3.0");
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", "childText1"));
		element.appendChild(Util.buildDDMSElement("child", "childText2"));
		DDMSVersion.setCurrentVersion("2.0");
		String value = Util.getFirstDDMSChildValue(element, "child");
		assertEquals("childText1", value);
	}
	
	public void testGetFirstDDMSChildValueNoValue() {
		String value = Util.getFirstDDMSChildValue(Util.buildDDMSElement("test", null), "unknown");
		assertEquals("", value);
	}
	
	public void testGetFirstDDMSChildValueWithValue() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", "childText1"));
		element.appendChild(Util.buildDDMSElement("child", "childText2"));
		String value = Util.getFirstDDMSChildValue(element, "child");
		assertEquals("childText1", value);
	}	
	
	public void testGetDDMSChildValuesNullParent() {
		try {
			Util.getDDMSChildValues(null, "test");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testGetDDMSChildValuesNullChild() {
		try {
			Util.getDDMSChildValues(Util.buildDDMSElement("test", null), null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}

	public void testGetDDMSChildValuesWrongNamespace() {
		Element element = Util.buildElement("ddmsence", "test", "http://ddmsence.urizone.net/", null);
		element.appendChild(Util.buildDDMSElement("child", "child1"));
		element.appendChild(Util.buildDDMSElement("child", "child2"));
		try {
			Util.getDDMSChildValues(element, "child");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
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
	
	public void testGetDDMSChildValuesNoValues() {
		Element element = Util.buildDDMSElement("test", null);
		List<String> values = Util.getDDMSChildValues(element, "unknown");
		assertNotNull(values);
		assertTrue(values.isEmpty());
	}	
	
	public void testGetDDMSChildValuesEmptyValues() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("child", ""));
		List<String> values = Util.getDDMSChildValues(element, "child");
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("", values.get(0));
	}
	
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
    
	public void testRequireDDMSValueNull() {
		try {
			Util.requireDDMSValue("description", null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testRequireDDMSValueEmpty() {
		try {
			Util.requireDDMSValue("description", "");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testRequireDDMSValueNotEmpty() {
		try {
			Util.requireDDMSValue("description", "DDMSence");
		}
		catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}
	}
	
	public void testRequireDDMSDateFormatSuccess() {
		try {
			Util.requireDDMSDateFormat(DatatypeConstants.DATETIME);
		}
		catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}
	}
	
	public void testRequireDDMSDateFormatFailure() {
		try {
			Util.requireDDMSDateFormat(DatatypeConstants.GDAY);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testRequireDDMSQNameSuccess() {
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireDDMSQName(element, DDMSVersion.getCurrentVersion().getNamespace(), "name");
		}
		catch (InvalidDDMSException e) {
			fail("Did not allow valid data.");
		}
	}
	
	public void testRequireDDMSQNameFailure() {
		// Null NamespaceURI = ""
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireDDMSQName(element, null, "name");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
		
		// Bad URI
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireDDMSQName(element, DDMSVersion.getCurrentVersion().getGmlNamespace(), "name");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
		
		// Bad Name
		try {
			Element element = Util.buildDDMSElement("name", null);
			Util.requireDDMSQName(element, DDMSVersion.getCurrentVersion().getNamespace(), "newName");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testRequireValueNull() {
		try {
			Util.requireValue("description", null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testRequireValueEmpty() {
		try {
			Util.requireValue("description", "");
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testRequireValueNotEmpty() {
		try {
			Util.requireValue("description", "DDMSence");
		}
		catch (IllegalArgumentException e) {
			fail("Did not allow valid data.");
		}
	}
	
	public void testRequireBoundedDDMSChildCountNullParent() {
		try {
			Util.requireBoundedDDMSChildCount(null, "test", 0, 1);
			fail("Allowed illegal argument data.");
		}
		catch (InvalidDDMSException e) {
			fail("Allowed illegal argument data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testRequireBoundedDDMSChildCountNullChild() {
		try {
			Util.requireBoundedDDMSChildCount(Util.buildDDMSElement("test", null), null, 0, 1);
			fail("Allowed illegal argument data.");
		}
		catch (InvalidDDMSException e) {
			fail("Allowed illegal argument data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
		
	public void testRequireBoundedDDMSChildCountBounded() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("name", "nameValue"));
		try {
			Util.requireBoundedDDMSChildCount(element, "name", 0, 2);
		}
		catch (InvalidDDMSException e) {
			fail("Prevented valid data.");
		}
	}
	
	public void testRequireBoundedDDMSChildCountExactly1() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("name", "nameValue"));
		try {
			Util.requireBoundedDDMSChildCount(element, "phone", 1, 1);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("Exactly 1 phone element must exist.", e.getMessage());
		}
	}
	
	public void testRequireBoundedDDMSChildCountExactlyX() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("name", "nameValue"));
		try {
			Util.requireBoundedDDMSChildCount(element, "phone", 2, 2);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("Exactly 2 phone elements must exist.", e.getMessage());
		}
	}
	
	public void testRequireBoundedDDMSChildCountNoMoreThan1() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		try {
			Util.requireBoundedDDMSChildCount(element, "phone", 0, 1);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("No more than 1 phone element can exist.", e.getMessage());
		}
	}
	
	public void testRequireBoundedDDMSChildCountNoMoreThanX() {
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		try {
			Util.requireBoundedDDMSChildCount(element, "phone", 0, 2);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("No more than 2 phone elements can exist.", e.getMessage());
		}
	}
	
	public void testRequireBoundedDDMSChildCountGenericUnbounded() {
		Element element = Util.buildDDMSElement("test", null);
		try {
			Util.requireBoundedDDMSChildCount(element, "phone", 1, 5);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			assertEquals("The number of phone elements must be between 1 and 5.", e.getMessage());
		}
	}
	
	public void testRequireBoundedDDMSChildCountWrongNamespace() {
		Element element = Util.buildElement("ddmsence", "test", "http://ddmsence.urizone.net/", null);
		try {
			Util.requireBoundedDDMSChildCount(element, "child", 0, 0);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			fail("Allowed processing of invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testRequireBoundedDDMSChildCountIndependentOfCurrentVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		Element element = Util.buildDDMSElement("test", null);
		element.appendChild(Util.buildDDMSElement("phone", "phoneValue"));
		DDMSVersion.setCurrentVersion("2.0");
		Util.requireBoundedDDMSChildCount(element, "phone", 1, 1);
	}
	
	public void testRequireValidNCNamesNull() throws InvalidDDMSException {
		Util.requireValidNCNames(null);
	}
	
	public void testRequireValidNCNamesValid() throws InvalidDDMSException {
		List<String> names = new ArrayList<String>();
		names.add("test");
		Util.requireValidNCNames(names);
	}
	
	public void testRequireValidNCNamesInvalid() throws InvalidDDMSException {
		List<String> names = new ArrayList<String>();
		names.add("1test");
		try {
			Util.requireValidNCNames(names);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testRequireValidNCNameNull() {
		try {
			Util.requireValidNCName(null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testRequireValidNCNameInvalidName() {
		try {
			Util.requireValidNCName("1TEST");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}		
	}
	
	public void testRequireValidNCNameInvalidNamespace() {
		try {
			Util.requireValidNCName("xmlns:TEST");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}		
	}
	
	public void testRequireValidNCNameValid() throws InvalidDDMSException {
		Util.requireValidNCName("name");
	}
	
	public void testRequireDDMSValidURIValid() throws InvalidDDMSException {
		Util.requireDDMSValidURI("test");
	}

	public void testRequireDDMSValidURIInvalid() {
		try {
			Util.requireDDMSValidURI(":::::");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}	
	}
	
	public void testRequireDDMSValidURINull() throws InvalidDDMSException {
		try {
			Util.requireDDMSValidURI(null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}	
	}

	public void testRequireValidLongitudeNull() {
		try {
			Util.requireValidLongitude(null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}			
	}
	
	public void testRequireValidLongitudeOutOfBounds() {
		try {
			Util.requireValidLongitude(new Double(-181));
			fail("Allowed invalid data.");
			
			Util.requireValidLongitude(new Double(181));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}	
	}
	
	public void testRequireValidLongitudeValid() throws InvalidDDMSException {
		Util.requireValidLongitude(new Double(0));
	}
	
	public void testRequireValidLatitudeNull() {
		try {
			Util.requireValidLatitude(null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}	
	}
	
	public void testRequireValidLatitudeOutOfBounds() {
		try {
			Util.requireValidLatitude(new Double(-91));
			fail("Allowed invalid data.");
			
			Util.requireValidLatitude(new Double(91));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}	
	}
	
	public void testRequireValidLatitudeValid() throws InvalidDDMSException {
		Util.requireValidLatitude(new Double(0));
	}
	
	
	public void testIsBoundedBadRange() {
		try {
			Util.isBounded(0, 10, 0);
			fail("Did not stop on bad range.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testIsBoundedLow() {
		assertFalse(Util.isBounded(0, 1, 3));
	}
	
	public void testIsBoundedHigh() {
		assertFalse(Util.isBounded(4, 1, 3));
	}
	
	public void testIsBoundedMiddle() {
		assertTrue(Util.isBounded(1, 0, 2));
	}
	
	public void testIsBoundedLowEdge() {
		assertTrue(Util.isBounded(1, 1, 3));
	}
	
	public void testIsBoundedHighEdge() {
		assertTrue(Util.isBounded(3, 1, 3));
	}
	
	public void testIsBoundedOnlyOne() {
		assertTrue(Util.isBounded(1, 1, 1));
	}

	public void testListEqualsNullLists() {
		try {
			Util.listEquals(null, null);
			fail("Did not stop on bad data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testListEqualsSameList() {
		List<String> list = new ArrayList<String>();
		list.add("Test");
		assertTrue(Util.listEquals(list, list));		
	}
	
	public void testListEqualsEmptyLists() {
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		assertTrue(Util.listEquals(list1, list2));		
	}
	
	public void testListEqualsSizes() {
		List<String> list1 = new ArrayList<String>();
		list1.add("Test");
		List<String> list2 = new ArrayList<String>();
		list2.add("Test");
		list2.add("Test2");
		assertFalse(Util.listEquals(list1, list2));
	}
	
	public void testListEqualsNullValues() {
		List<String> list1 = new ArrayList<String>();
		list1.add(null);
		list1.add("Test2");
		List<String> list2 = new ArrayList<String>();
		list2.add(null);
		list2.add("Test2");
		assertTrue(Util.listEquals(list1, list2));
	}
	
	public void testListEqualsNullValue() {
		List<String> list1 = new ArrayList<String>();
		list1.add(null);
		list1.add("Test2");
		List<String> list2 = new ArrayList<String>();
		list2.add("Test");
		list2.add("Test2");
		assertFalse(Util.listEquals(list1, list2));
	}
	
	public void testListEqualsDifferentValue() {
		List<String> list1 = new ArrayList<String>();
		list1.add("Test1");
		List<String> list2 = new ArrayList<String>();
		list2.add("Test2");
		assertFalse(Util.listEquals(list1, list2));
	}
			
	public void testXmlEscape() {
		String testString = "<test>\"Brian's DDMSense & DDMS\"</test>";
		assertEquals("&lt;test&gt;&quot;Brian&apos;s DDMSense &amp; DDMS&quot;&lt;/test&gt;", 
			Util.xmlEscape(testString));
	}
	
	public void testCapitalizeEmpty() {
		assertEquals(null, Util.capitalize(null));
	}

	public void testCapitalizeOneChar() {
		assertEquals("A", Util.capitalize("a"));
	}

	public void testCapitalizeNotLetter() {
		assertEquals("123", Util.capitalize("123"));
	}
	
	public void testCapitalizeSuccess() {
		assertEquals("Ddms", Util.capitalize("ddms"));
	}
	
	public void testBuildElementEmptyPrefix() {
		Element element = Util.buildElement(null, "test", "", null);
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals("", element.getNamespaceURI());
		assertEquals("", element.getNamespacePrefix());
		assertEquals("test", element.getQualifiedName());
		
	}
	
	public void testBuildDDMSElement() {
		Element element = Util.buildDDMSElement("test", null);
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals(TEST_NAMESPACE, element.getNamespaceURI());
		assertEquals(Util.DDMS_PREFIX, element.getNamespacePrefix());
	}
	
	public void testBuildDDMSElementNullName() {
		try {
			Util.buildDDMSElement(null, null);
			fail("Method allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}

	public void testBuildDDMSElementChildText() {
		Element element = Util.buildDDMSElement("test", "testValue");
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals("testValue", element.getValue());
		assertEquals(TEST_NAMESPACE, element.getNamespaceURI());
		assertEquals(Util.DDMS_PREFIX, element.getNamespacePrefix());
	}
	
	public void testBuildDDMSElementNoChildText() {
		Element element = Util.buildDDMSElement("test", null);
		assertNotNull(element);
		assertEquals("test", element.getLocalName());
		assertEquals("", element.getValue());
		assertEquals(TEST_NAMESPACE, element.getNamespaceURI());
		assertEquals(Util.DDMS_PREFIX, element.getNamespacePrefix());
	}
	
	public void testBuildAttribute() {
		Attribute attribute = Util.buildAttribute("ddms", "test", DDMSVersion.getCurrentVersion().getNamespace(),
			"testValue");
		assertNotNull(attribute);
		assertEquals("test", attribute.getLocalName());
		assertEquals("testValue", attribute.getValue());
		assertEquals(TEST_NAMESPACE, attribute.getNamespaceURI());
		assertEquals(Util.DDMS_PREFIX, attribute.getNamespacePrefix());
	}
	
	public void testBuildAttributeEmptyValues() {
		Attribute attribute = Util.buildAttribute(null, "test", null, "testValue");
		assertNotNull(attribute);
		assertEquals("test", attribute.getLocalName());
		assertEquals("testValue", attribute.getValue());
		assertEquals("", attribute.getNamespaceURI());
		assertEquals("", attribute.getNamespacePrefix());
	}
	
	public void testBuildDDMSAttribute() {
		Attribute attribute = Util.buildDDMSAttribute("test", "testValue");
		assertNotNull(attribute);
		assertEquals("test", attribute.getLocalName());
		assertEquals("testValue", attribute.getValue());
		assertEquals(TEST_NAMESPACE, attribute.getNamespaceURI());
		assertEquals(Util.DDMS_PREFIX, attribute.getNamespacePrefix());
	}
	
	public void testBuildDDMSAttributeNullName() {
		try {
			Util.buildDDMSAttribute(null, "testValue");
			fail("Method allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testBuildDDMSAttributeNullValue() {
		try {
			Util.buildDDMSAttribute("test", null);
			fail("Method allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testRequireSameVersionSuccess() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("2.0");
		Identifier identifier = new Identifier("Test", "Value");
		Identifier identifier2 = new Identifier("Test", "Value");
		Util.requireSameVersion(identifier, identifier2);
	}
	
	public void testRequireSameVersionFailure() {
		try {
			DDMSVersion.setCurrentVersion("2.0");
			Identifier identifier = new Identifier("Test", "Value");
			DDMSVersion.setCurrentVersion("3.0");
			Identifier identifier2 = new Identifier("Test", "Value");
			Util.requireSameVersion(identifier, identifier2);
			fail("Allowed different versions.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testAddDdmsAttributeEmptyValue() {
		Element element = new Element("test", "http://test.com");
		Util.addDDMSAttribute(element, "testAttribute", null);
		assertNull(element.getAttribute("testAttribute", DDMSVersion.getCurrentVersion().getNamespace()));
	}
	
	public void testAddDdmsAttributeValue() {
		Element element = new Element("test", "http://test.com");
		Util.addDDMSAttribute(element, "testAttribute", "dog");
		Attribute attr = element.getAttribute("testAttribute", DDMSVersion.getCurrentVersion().getNamespace());
		assertEquals("ddms", attr.getNamespacePrefix());
		assertEquals(DDMSVersion.getCurrentVersion().getNamespace(), attr.getNamespaceURI());
		assertEquals("testAttribute", attr.getLocalName());
		assertEquals("dog", element.getAttributeValue("testAttribute", DDMSVersion.getCurrentVersion().getNamespace()));
	}
	
	public void testAddDdmsChildElementEmptyValue() {
		Element element = new Element("test", "http://test.com");
		Util.addDDMSChildElement(element, "child", null);
		assertEquals(0, element.getChildElements().size());
	}
	
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
	
	public void testGetDatatypeFactory() {
		assertNotNull(Util.getDataTypeFactory());
	}
	
	public void testGetAsList() {
		assertTrue(Util.getAsList(null).isEmpty());
		assertTrue(Util.getAsList("").isEmpty());
		List<String> list = Util.getAsList("a b");
		assertEquals(2, list.size());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		list = Util.getAsList("a  b");
		assertEquals(3, list.size());
		assertEquals("a", list.get(0));
		assertEquals("", list.get(1));
		assertEquals("b", list.get(2));
	}
	
	public static List<String> getAsList(String value) {
		if (Util.isEmpty(value))
			return Collections.emptyList();
		return (Arrays.asList(value.split(" ")));
	}

	public void testBuildXmlDocument() throws Exception {
		File testFile = new File(TEST_DATA_DIR + "3.0/", "resource.xml");
		String expectedXmlOutput = new DDMSReader().getDDMSResource(testFile).toXML();
		assertEquals(expectedXmlOutput, Util.buildXmlDocument(new FileInputStream(testFile)).getRootElement().toXML());
	}
	
	public void testBuildXmlDocumentBadFile() throws Exception {
		try {
			Util.buildXmlDocument(null);
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
		
		try {
			Util.buildXmlDocument(new ByteArrayInputStream("Not an XML File".getBytes()));
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			// Good
		}
	}
}
