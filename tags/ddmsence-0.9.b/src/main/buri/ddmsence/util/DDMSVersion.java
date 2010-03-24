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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import buri.ddmsence.ddms.UnsupportedVersionException;

/**
 * Manages the supported versions of DDMS.
 * 
 * <p>
 * This class is the extension point for adding new DDMS versions in the future. Currently, only Pre-Release 3.0 is supported. All DDMS Component
 * constructors use 3.0 as the DDMS version (via <code>DDMSVersion.getDefaultNamespace()</code>).</p>
 * 
 * <p>
 * The ddmsence.properties file has three properties which can be a comma-separated list:
 * </p>
 * 
 * <li>ddms.supportedVersions: i.e. "2.0,3.0"</li>
 * <li>ddms.xmlNamespaces: i.e. "http://metadata.dod.mil/mdr/ns/DDMS/2.0/,http://metadata.dod.mil/mdr/ns/DDMS/3.0/"</li>
 * <li>ddms.xsdLocations: i.e. "/schemas/2.0/DDMS/2.0/DDMS-v2_0.xsd,/schemas/3.0/DDMS/3.0/DDMS-v3_0.xsd"</li>
 * 
 * <p>
 * The number of items in each property must match the number of items in the other two properties. The format of an xsdLocation should
 * follow <code>/schemas/versionNumber/schemaLocationInDDMSDownloadablePackage</code>.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DDMSVersion {
	
	/** The default version of DDMS, used to construct DDMS components when no specific version is mentioned */
	public static final String DEFAULT_VERSION = PropertyReader.getProperty("ddms.defaultVersion");
	private static final List<String> SUPPORTED_DDMS_VERSIONS = PropertyReader.getListProperty("ddms.supportedVersions");
	private static final List<String> DDMS_NAMESPACES = PropertyReader.getListProperty("ddms.xmlNamespaces");
	private static final List<String> DDMS_SCHEMA_LOCATIONS = PropertyReader.getListProperty("ddms.xsdLocations");
	private static final Map<String, String> VERSIONS_TO_NAMESPACES = new HashMap<String, String>();
	private static final Map<String, String> VERSIONS_TO_SCHEMAS = new HashMap<String, String>();
	static {
		for (int i = 0; i < SUPPORTED_DDMS_VERSIONS.size(); i++) {
			VERSIONS_TO_NAMESPACES.put(SUPPORTED_DDMS_VERSIONS.get(i), DDMS_NAMESPACES.get(i));
			VERSIONS_TO_SCHEMAS.put(SUPPORTED_DDMS_VERSIONS.get(i), DDMS_SCHEMA_LOCATIONS.get(i));
		}
	}
	
	/**
	 * Private to prevent instantiation
	 */
	private DDMSVersion() {}
	
	/**
	 * Returns a list of supported DDMS versions
	 * 
	 * @return List of string version numbers
	 */
	public static List<String> getSupportedVersions() {
		return Collections.unmodifiableList(SUPPORTED_DDMS_VERSIONS);
	}
	
	/**
	 * Checks if a version number is among the list of supported DDMS versions
	 * 
	 * @param version	the version to test
	 * @return true if the version is supported
	 */
	public static boolean isSupported(String version) {
		return (getSupportedVersions().contains(version));
	}
	
	/**
	 * Returns the local schema location for the default version of DDMS
	 * 
	 * @return the schema location
	 */
	public static String getDefaultSchema() {
		return (getSchemaFor(DEFAULT_VERSION));
	}
	
	/**
	 * Returns the namespace for the default version of DDMS
	 * 
	 * @return the namespace
	 */
	public static String getDefaultNamespace() {
		return (getNamespaceFor(DEFAULT_VERSION));
	}
	
	/**
	 * Returns the local schema location for a particular version of DDMS. This will be more useful
	 * in the future when multiple versions of DDMS are supported.
	 * 
	 * @param version	the version of DDMS
	 * @return the schema location
	 * @throws UnsupportedVersionException if the version is not supported
	 */
	public static String getSchemaFor(String version) throws UnsupportedVersionException {
		if (!isSupported(version))
			throw new UnsupportedVersionException(version);
		return (VERSIONS_TO_SCHEMAS.get(version));
	}
	
	/**
	 * Returns the namespace for a particular version of DDMS. This will be more useful
	 * in the future when multiple versions of DDMS are supported.
	 * 
	 * @param version	the version of DDMS
	 * @return the namespace
	 * @throws UnsupportedVersionException if the version is not supported
	 */
	public static String getNamespaceFor(String version) throws UnsupportedVersionException {
		if (!isSupported(version))
			throw new UnsupportedVersionException(version);
		return (VERSIONS_TO_NAMESPACES.get(version));
	}
}
