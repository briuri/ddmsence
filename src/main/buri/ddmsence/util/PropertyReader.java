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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Utility class for dealing with the property file.
 * 
 * <p>
 * Properties in DDMSence are found in the <code>ddmsence.properties</code> file. All properties are prefixed with
 * "buri.ddmsence.", so <code>getProperty</code> calls should be performed with just the property suffix.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PropertyReader {
	private Properties _properties = new Properties();
	
	private static final String PROPERTIES_FILE = "ddmsence.properties";
	private static final String PROPERTIES_PREFIX = "buri.ddmsence.";
	private static final String UNDEFINED_PROPERTY = "Undefined Property: ";
	private static final String NOT_NUMBER_PROPERTY = "Not a Number: ";
	
	private static final PropertyReader INSTANCE = new PropertyReader();
    
	/**
	 * Private to prevent instantiation
	 */
	private PropertyReader() {
		InputStream is = getLoader().getResourceAsStream(PROPERTIES_FILE);
		try {
			if (is != null) {
				Properties aProperties = new Properties();
				aProperties.load(is);
				is.close();
				_properties.putAll(aProperties);
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Could not load the properties file: " + e.getMessage());
		}
	}
		
	/**
	 * Locates a property and returns it. Assumes that the property is required.
	 * 
	 * @param name		the simple name of the property, without "buri.ddmsence."
	 * @return the property specified
	 * @throws IllegalArgumentException if the property does not exist.
	 */
	public static String getProperty(String name) {
		String value = INSTANCE.getProperties().getProperty(PROPERTIES_PREFIX + name);
		if (value == null)
			throw new IllegalArgumentException(UNDEFINED_PROPERTY + PROPERTIES_PREFIX + name);
		return (value);
	}

	/**
	 * Locates a property and returns it as an int
	 * 
	 * @param name		the simple name of the property, without "buri.ddmsence."
	 * @return the property specified
	 * @throws IllegalArgumentException if the property does not exist or is not a number.
	 */
	public static int getIntProperty(String name) {
		String value = getProperty(name);
		try {
			int intValue = Integer.valueOf(value);
			return (intValue);
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException(NOT_NUMBER_PROPERTY + value);
		}
	}
	
	/**
	 * Locates a list property and returns it as a List
	 * 
	 * @param name		the simple name of the property, without "buri.ddmsence."
	 * @return the property specified
	 * @throws IllegalArgumentException if the property does not exist
	 */
	public static List<String> getListProperty(String name) {
		String value = getProperty(name);
		String[] values = value.split(",");
		List<String> listValues = Arrays.asList(values);
		return (Collections.unmodifiableList(listValues));
	}	
	
	/**
	 * Generate a ClassLoader to be used to load resources
	 * 
	 * @return a ClassLoader
	 */
	private static ClassLoader getLoader() {
		return new FindClassLoader().getClass().getClassLoader();
	}

	/**
	 * Stub to load classes.
	 */
	private static class FindClassLoader {
		public FindClassLoader() {}
	} 
	
	/**
	 * Accessor for properties object.
	 */
	private Properties getProperties() {
		return (_properties);
	}
}
