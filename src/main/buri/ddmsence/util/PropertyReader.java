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
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Utility class for dealing with the property file.
 * 
 * <p>
 * Properties in DDMSence are found in the <code>ddmsence.properties</code> file. All properties are prefixed with
 * "buri.ddmsence.", so <code>getProperty</code> calls should be performed with just the property suffix.
 * </p>
 * 
 * <p>
 * The Property Reader supports several custom properties, which can be specified at runtime in a 
 * <code>ddmsence-config.properties</code> file. If the file exists in the classpath, the values from that file will
 * be used. Only a subset of the DDMSence properties are allowed to be configured:
 * </p>
 * 
 * <ul>
 * <li><code>buri.ddmsence.ddms.prefix</code>: The namespace prefix for components in the DDMS namespace.</li>
 * <li><code>buri.ddmsence.gml.prefix</code>: The namespace prefix for components in the GML namespace.</li>
 * <li><code>buri.ddmsence.icism.prefix</code>: The namespace prefix for components in the ICISM namespace.</li>
 * <li><code>buri.ddmsence.xlink.prefix</code>: The namespace prefix for components in the xlink namespace.</li>
 * <li><code>buri.ddmsence.sample.data</code>: The default location for Sample Application data.</li>
 * </ul> 
 * 
 * <p>
 * Changing a namespace prefix will affect both components created from scratch and components loaded from XML files.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class PropertyReader {
	private Properties _properties = new Properties();
	
	private static final String PROPERTIES_FILE = "ddmsence.properties";
	private static final String CUSTOM_PROPERTIES_FILE = "ddmsence-config.properties";
	private static final String PROPERTIES_PREFIX = "buri.ddmsence.";
	private static final String UNDEFINED_PROPERTY = "Undefined Property: ";
	private static final String NOT_NUMBER_PROPERTY = "Not a Number: ";
	
	private static final Set<String> CUSTOM_PROPERTIES = new HashSet<String>();
	static {
		CUSTOM_PROPERTIES.add(PROPERTIES_PREFIX + "ddms.prefix");
		CUSTOM_PROPERTIES.add(PROPERTIES_PREFIX + "gml.prefix");
		CUSTOM_PROPERTIES.add(PROPERTIES_PREFIX + "icism.prefix");
		CUSTOM_PROPERTIES.add(PROPERTIES_PREFIX + "xlink.prefix");
		CUSTOM_PROPERTIES.add(PROPERTIES_PREFIX + "sample.data");
		CUSTOM_PROPERTIES.add(PROPERTIES_PREFIX + "xml.transform.TransformerFactory");
	};
	
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
				loadCustomProperties();
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Could not load the properties file: " + e.getMessage());
		}
	}
	
	/**
	 * Searches for the ddmsence-config.properties file and loads specifically
	 * defined custom properties, if available.
	 * 
	 * @throws IOException
	 */
	private void loadCustomProperties() throws IOException {
		InputStream is = getLoader().getResourceAsStream(CUSTOM_PROPERTIES_FILE);
		if (is == null)
			return;
		Properties aProperties = new Properties();
		aProperties.load(is);
		is.close();
		for (String customKey : CUSTOM_PROPERTIES) {
			String customProperty = aProperties.getProperty(customKey);
			if (customProperty != null) {
				_properties.put(customKey, customProperty);
			}
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
