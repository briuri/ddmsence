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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.IllegalNameException;
import nu.xom.NamespaceConflictException;
import buri.ddmsence.ddms.InvalidDDMSException;

/** 
 * A collection of static utility methods.
 *
 * @author Brian Uri!
 * @since 0.9.b
 */
public class Util {
	
	/** The DDMS prefix, as defined in the ddms.prefix property */
	public static final String DDMS_PREFIX = PropertyReader.getProperty("ddms.prefix");
	
    private static final int DISPLAY_TRACE_DEPTH = PropertyReader.getIntProperty("stackTrace.depth");
	private static final LinkedHashMap<String, String> XML_SPECIAL_CHARS = new LinkedHashMap<String, String>();
	static {
		XML_SPECIAL_CHARS.put("&", "&amp;");
		XML_SPECIAL_CHARS.put("\"", "&quot;");
		XML_SPECIAL_CHARS.put("'", "&apos;");
		XML_SPECIAL_CHARS.put("<", "&lt;");
		XML_SPECIAL_CHARS.put(">", "&gt;");
	}

	private static Set<QName> DATE_DATATYPES = new HashSet<QName>();
	static {
		DATE_DATATYPES.add(DatatypeConstants.DATE);
		DATE_DATATYPES.add(DatatypeConstants.DATETIME);
		DATE_DATATYPES.add(DatatypeConstants.GYEARMONTH);
		DATE_DATATYPES.add(DatatypeConstants.GYEAR);
	}
		
	private static DatatypeFactory _factory;
	static {
		try {
			_factory = DatatypeFactory.newInstance();
		}
		catch (DatatypeConfigurationException e) {
			throw new RuntimeException("Could not load DatatypeFactory for date conversion.", e);
		}
	}
	
	/**
	 * Private to prevent instantiation.
	 */
	private Util() {}
		
	/**
	 * Accesor for the datatype factory
	 */
	public static DatatypeFactory getDataTypeFactory() {
		return (_factory);
	}
	
	/**
	 * Returns an empty string in place of a null one.
	 * 
	 * @param string the string to convert, if null
	 * @return an empty string if the string is null, or the string untouched
	 */
	public static String getNonNullString(String string) {
		return (string == null ? "" : string);
	}
	
	/**
	 * Converts a list of objects into a space-delimited xs:list, using the object's toString() implementation
	 * 
	 * @param list the list to convert
	 * @return a space-delimited string, or empty string if the list was empty.
	 */
	public static String getXsList(List<?> list) {
		if (list == null)
			return ("");
		StringBuffer buffer = new StringBuffer();
		for (Object string : list) {
			buffer.append(string).append(" ");
		}
		return (buffer.toString().trim());
	}
	
	/**
	 * Returns an int value for a boolean, for use in a hashCode function.
	 * 
	 * @param b	the boolean
	 * @return 1 for true and 0 for false
	 */
	public static int booleanHashCode(boolean b) {
		return (b ? 1 : 0);
	}
	
	/**
	 * Checks if a String value is empty. An empty string is defined as one that is null, contains only whitespace, or
	 * has length 0.
	 * 
	 * @param value the value to check.
	 * @return a boolean, true if the value is null or zero-length, false otherwise
	 */
	public static boolean isEmpty(String value) {
		return (value == null || value.trim().length() == 0);
	}
		
    /**
     * Extracts an Exception's stack trace for string manipulation.
     * 
     * @param throwable		the exception to get the trace of
     * @return the stack trace as a String 
     */
    public static String getStackTrace(Throwable throwable) {
    	if (throwable == null)
    		return ("");
    	StringBuffer buffer = new StringBuffer(getStackTraceHelper(throwable));
    	Throwable cause = throwable.getCause();
		while (cause != null) {
			buffer.append("Caused by ").append(getStackTraceHelper(cause));
			cause = cause.getCause();
		}
		return buffer.toString();
	}	
	
    /**
     * Gets the child text of the first child element matching the name in the default DDMS namespace.
     * 
     * @param parent the parent element
     * @param name the name of the child element
     * @return the child text of the first discovered child element
     */
    public static String getFirstDDMSChildValue(Element parent, String name) {
    	Util.requireValue("parent element", parent);
    	Util.requireValue("child name", name);
    	Element child = parent.getFirstChildElement(name, DDMSVersion.getDefaultNamespace());
    	return (child == null ? "" : child.getValue());
    }
    
    /**
     * Gets the child text of any child elements in the default DDMS namespace and returns them as a list.
     * 
     * @param parent the parent element
     * @param name the name of the child element
     * @return a List of strings, where each string is child text of matching elements
     */
    public static List<String> getDDMSChildValues(Element parent, String name) {
    	Util.requireValue("parent element", parent);
    	Util.requireValue("child name", name);
     	List<String> childTexts = new ArrayList<String>();
		Elements childElements = parent.getChildElements(name, DDMSVersion.getDefaultNamespace());
		for (int i = 0; i < childElements.size(); i++) {
			childTexts.add(childElements.get(i).getValue());
		}
		return (childTexts);
    }
    
	/**
	 * Asserts that a value required for DDMS is not null or empty.
	 * 
	 * @param description	a descriptive name of the value
	 * @param value			the value to check
	 * @throws InvalidDDMSException if the value is null or empty
	 */
	public static void requireDDMSValue(String description, Object value) throws InvalidDDMSException {
		if (value == null || (value instanceof String && isEmpty((String) value)))
			throw new InvalidDDMSException(description + " is required.");
	}
		
	/**
	 * Asserts that a date format is one of the 4 types accepted by DDMS.
	 * 
	 * @param dateQName	the QName of the datatype
	 * @throws InvalidDDMSException if the value is invalid. Does nothing if value is null.
	 */
	public static void requireDDMSDateFormat(QName dateQName) throws InvalidDDMSException {
		if (dateQName != null) {
			if (!DATE_DATATYPES.contains(dateQName))
				throw new InvalidDDMSException("The date datatype must be one of " + DATE_DATATYPES);
		}
	}
	
	/**
	 * Asserts that a value required, for general cases.
	 * 
	 * @param description	a descriptive name of the value
	 * @param value			the value to check
	 * @throws IllegalArgumentException if the value is null or empty
	 */
	public static void requireValue(String description, Object value) {
		if (value == null || (value instanceof String && isEmpty((String) value)))
			throw new IllegalArgumentException(description + " is required.");
	}
	
	/**
	 * Checks that the number of child elements with the given name in the default DDMS namespace are bounded.
	 * 
	 * @param parent		the parent element
	 * @param childName		the local name of the DDMS child
	 * @param lowBound		the lowest value the number can be
	 * @param highBound		the highest value the number can be
	 * @throws InvalidDDMSException if the number is out of bounds
	 */
	public static void requireBoundedDDMSChildCount(Element parent, String childName, int lowBound, int highBound)
		throws InvalidDDMSException {
		Util.requireValue("parent element", parent);
		Util.requireValue("child name", childName);
		int childCount = parent.getChildElements(childName, DDMSVersion.getDefaultNamespace()).size();
		if (!isBounded(childCount, lowBound, highBound)) {
			StringBuffer error = new StringBuffer();
			if (lowBound == highBound) {
				error.append("Exactly ").append(highBound).append(" ").append(childName).append(" element");
				if (highBound != 1)
					error.append("s");
				error.append(" must exist.");
			}
			else if (lowBound == 0) {
				error.append("No more than ").append(highBound).append(" ").append(childName).append(" element");
				if (highBound != 1)
					error.append("s");
				error.append(" can exist.");
			}
			else {
				error.append("The number of ").append(childName).append(" elements must be between ").append(lowBound)
						.append(" and ").append(highBound).append(".");
			}
			throw new InvalidDDMSException(error.toString());
		}
	}
	
	/**
	 * Validates that a list of strings contains NCNames. This method uses the built-in Verifier in XOM by attempting to
	 * create a new Element with the test string as a local name (Local names must be NCNames).
	 * 
	 * @param names a list of names to check
	 * @throws InvalidDDMSException if any name is not an NCName.
	 */
	public static void requireValidNCNames(List<String> names) throws InvalidDDMSException {
		if (names == null)
			names = Collections.emptyList();
		for (String name : names) {
			requireValidNCName(name);
		}
	}
	
	/**
	 * Validates that a string is an NCName. This method uses the built-in Verifier in XOM by attempting to
	 * create a new Element with the test string as a local name (Local names must be NCNames).
	 * 
	 * @param name the name to check
	 * @throws InvalidDDMSException if the name is not an NCName.
	 */
	public static void requireValidNCName(String name) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("name", name);
			new Element(name);
		}
		catch (IllegalNameException e) {
			throw new InvalidDDMSException("\"" + name + "\" is not a valid NCName.");
		}
		catch (NamespaceConflictException e) {
			throw new InvalidDDMSException("\"" + name + "\" is not a valid NCName.");
		}
	}
	
	/**
	 * Checks that a string is a valid URI.
	 * 
	 * @param uri	the string to test
	 * @throws InvalidDDMSException if the string cannot be built into a URI
	 */
	public static void requireDDMSValidURI(String uri) throws InvalidDDMSException {
		Util.requireValue("uri", uri);
		try {
			new URI(uri);
		}
		catch (URISyntaxException e) {
			throw new InvalidDDMSException(e);
		}
	}
		
	/**
	 * Validates a longitude value
	 * 
	 * @param value the value to test
	 * @throws InvalidDDMSException
	 */
	public static void requireValidLongitude(Double value) throws InvalidDDMSException {
		if (value == null || (new Double(-180)).compareTo(value) > 0 || (new Double(180)).compareTo(value) < 0)
			throw new InvalidDDMSException("A longitude value must be between -180 and 180 degrees: " + value);
	}
	
	/**
	 * Validates a latitude value
	 * 
	 * @param value the value to test
	 * @throws InvalidDDMSException
	 */
	public static void requireValidLatitude(Double value) throws InvalidDDMSException {
		if (value == null || (new Double(-90)).compareTo(value) > 0 || (new Double(90)).compareTo(value) < 0)
			throw new InvalidDDMSException("A latitude value must be between -90 and 90 degrees: " + value);
	}
	
	/**
	 * Checks that a number is between two values, inclusive
	 * 
	 * @param testCount	the number to evaluate
	 * @param lowBound the lowest value the number can be
	 * @param highBound the highest value the number can be
	 * @return true if the number is bounded, false otherwise
	 * @throws IllegalArgumentException if the range is invalid.
	 */
	public static boolean isBounded(int testCount, int lowBound, int highBound) {
		if (lowBound > highBound)
			throw new IllegalArgumentException("Invalid number range: " + lowBound + " to " + highBound);
		return (testCount >= lowBound && testCount <= highBound);
	}
	
	/**
	 * Checks if two lists of Objects are identical. Returns true if the lists are the same length and each indexed
	 * string also exists at the same index in the other list.
	 * 
	 * @param list1 the first list
	 * @param list2 the second list
	 * @return true if the lists are of equal size and contain the same objects, false otherwise.
	 * @throws IllegalArgumentException if one of the lists is null
	 */
	public static boolean listEquals(List<?> list1, List<?> list2) {
		if (list1 == null || list2 == null)
			throw new IllegalArgumentException("Null lists cannot be compared.");
		if (list1 == list2)
			return (true);
		if (list1.size() != list2.size())
			return (false);
		for (int i = 0; i < list1.size(); i++) {
			Object value1 = list1.get(i);
			Object value2 = list2.get(i);
			if (!nullEquals(value1, value2))
				return (false);
		}
		return (true);
	}
	
	/**
	 * Checks object equality when the objects could possible be null.
	 * 
	 * @param obj1 the first object
	 * @param obj2 the second object
	 * @return true if both objects are null or obj1 equals obj2, false otherwise
	 */
	public static boolean nullEquals(Object obj1, Object obj2) {
		return (obj1 == null ? obj2 == null : obj1.equals(obj2));
	}
	
	/**
	 * Replaces XML special characters - '&', '<', '>', '\'', '"' 
	 * 
	 * @param input the string to escape.
	 * @return escaped String
	 */
	public static String xmlEscape(String input) {
		if (input != null) {
			for (Iterator<String> iterator = XML_SPECIAL_CHARS.keySet().iterator(); iterator.hasNext();) {
				String pattern = iterator.next();
				input = Pattern.compile(pattern).matcher(input).replaceAll((String) XML_SPECIAL_CHARS.get(pattern));
			}
		}
		return input;
	}
	
	/**
	 * Capitalizes the first letter of a String. Silently does nothing if the string is null, empty, or not a letter.
	 * 
	 * @param string	the string to capitalize
	 * @return the capitalized string
	 */
	public static String capitalize(String string) {
		if (isEmpty(string))
			return (string);
		if (string.length() == 1)
			return (string.toUpperCase());
		return (string.substring(0, 1).toUpperCase() + string.substring(1, string.length()));
	}	
	
	/**
	 * Helper method to add a ddms attribute to an element. Will not add the attribute if the value
	 * is empty or null.
	 * 
	 * @param element the element to decorate
	 * @param attributeName the name of the attribute (will be within the DDMS namespace)
	 * @param attributeValue the value of the attribute
	 */
	public static void addDDMSAttribute(Element element, String attributeName, String attributeValue) {
		addAttribute(element, DDMS_PREFIX, attributeName, DDMSVersion.getDefaultNamespace(), attributeValue);
	}
	
	/**
	 * Helper method to add an attribute to an element. Will not add the attribute if the value
	 * is empty or null.
	 * 
	 * @param element the element to decorate
	 * @param prefix the prefix to use (without a trailing colon)
	 * @param attributeName the name of the attribute
	 * @param namespaceURI the namespace this attribute is in
	 * @param attributeValue the value of the attribute
	 */
	public static void addAttribute(Element element, String prefix, String attributeName, String namespaceURI, 
		String attributeValue) {
		if (!Util.isEmpty(attributeValue))
			element.addAttribute(Util.buildAttribute(prefix, attributeName, namespaceURI, attributeValue));
	}
	
	/**
	 * Helper method to add a ddms child element to an element. Will not add if the value
	 * is empty or null.
	 * 
	 * @param element the element to decorate
	 * @param childName the name of the child (will be within the DDMS namespace)
	 * @param childValue the value of the attribute
	 */
	public static void addDDMSChildElement(Element element, String childName, String childValue) {
		if (!Util.isEmpty(childValue))
			element.appendChild(Util.buildDDMSElement(childName, childValue));
	}
	
	/**
	 * Convenience method to create an element in the default DDMS namespace with some child text. 
	 * The resultant element will use the DDMS prefix and
	 * have no attributes or children (yet).
	 * 
	 * @param name the local name of the element
	 * @param childText the text of the element (optional)
	 */
	public static Element buildDDMSElement(String name, String childText) {
		return (buildElement(DDMS_PREFIX, name, DDMSVersion.getDefaultNamespace(), childText));
	}
	
	/**
	 * Convenience method to create an element in a namespace with some child text. 
	 * The resultant element will use a custom prefix and have no attributes or children (yet).
	 * 
	 * @param prefix the prefix to use (without a trailing colon)
	 * @param name the local name of the element
	 * @param namespaceURI the namespace this element is in
	 * @param childText the text of the element (optional)
	 */
	public static Element buildElement(String prefix, String name, String namespaceURI, String childText) {
		Util.requireValue("name", name);
		prefix = (Util.isEmpty(prefix) ? "" : prefix + ":");
		Element element = new Element(prefix + name, namespaceURI);
		if (!Util.isEmpty(childText))
			element.appendChild(childText);
		return (element);
	}
	
	/**
	 * Convenience method to create an attribute in the default DDMS namespace. The resultant attribute will use the DDMS prefix
	 * and have the provided value.
	 * 
	 * @param name the local name of the attribute
	 * @param value the value of the attribute
	 */
	public static Attribute buildDDMSAttribute(String name, String value) {
		return (buildAttribute(DDMS_PREFIX, name, DDMSVersion.getDefaultNamespace(), value));
	} 
	
	/**
	 * Convenience method to create an attribute in a namespace. 
	 * 
	 * @param prefix the prefix to use (without a trailing colon)
	 * @param name the local name of the attribute
	 * @param namespaceURI the namespace this attribute is in
	 * @param value the value of the attribute
	 */
	public static Attribute buildAttribute(String prefix, String name, String namespaceURI, String value) {
		requireValue("name", name);
		requireValue("value", value);
		prefix = (Util.isEmpty(prefix) ? "" : prefix + ":");
		if (namespaceURI == null)
			namespaceURI = "";
		return (new Attribute(prefix + name, namespaceURI, value));
	}   

	/**
     * Gets a stack trace as a string. Does not look into nested causes.
     * 
     * @param throwable		the exception to parse
     * @return String 		the stack trace as a string
     */
    private static String getStackTraceHelper(Throwable throwable) {
    	String lineSeparator = System.getProperty("line.separator");
		final StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw, true));
		String[] lines = sw.toString().split(lineSeparator);
		int depth = Math.min(lines.length, DISPLAY_TRACE_DEPTH);
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < depth; i++) {
			buffer.append(lines[i]).append(lineSeparator);
		}
		if (depth < lines.length) {
			buffer.append("\t... ").append(lines.length - depth).append(" more").append(lineSeparator);
		}
		return buffer.toString();
	}
 
}