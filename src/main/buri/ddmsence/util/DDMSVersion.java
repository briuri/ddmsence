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
import java.util.List;

import buri.ddmsence.ddms.UnsupportedVersionException;

/**
 * Manages the supported versions of DDMS.
 * 
 * <p>
 * This class is the extension point for supporting new DDMS versions in the future. DDMSVersion maintains a static 
 * currentVersion variable which can be set at runtime. All DDMS component constructors which build components from scratch
 * can then call <code>DDMSVersion.getCurrentVersion()</code> or <code>DDMSVersion.getCurrentNamespace()</code> to determine
 * what namespace/DDMS version to use. If no currentVersion has been set, a default will be used, which maps to 
 * <code>buri.ddmsence.ddms.defaultVersion</code> in the properties file. This defaults to 3.0 right now.</p>
 * 
 * <p>
 * The ddmsence.properties file has five properties which can be a comma-separated list:
 * </p>
 * 
 * <li>ddms.supportedVersions: i.e. "2.0,3.0"</li>
 * <li>ddms.xmlNamespaces: i.e. "http://metadata.dod.mil/mdr/ns/DDMS/2.0/,http://metadata.dod.mil/mdr/ns/DDMS/3.0/"</li>
 * <li>ddms.xsdLocations: i.e. "/schemas/2.0/DDMS/2.0/DDMS-v2_0.xsd,/schemas/3.0/DDMS/3.0/DDMS-v3_0.xsd"</li>
 * <li>gml.xmlNamespaces: i.e. "http://www.opengis.net/gml,http://www.opengis.net/gml/3.2"</li>
 * <li>gml.xsdLocations: i.e. "/schemas/2.0/DDMS/2.0/DDMS-GML-Profile.xsd,/schemas/3.0/DDMS/3.0/DDMS-GML-Profile.xsd"</li> 
 * 
 * <p>
 * The number of items in each property must match the number of supported DDMS versions. The format of an xsdLocation should
 * follow <code>/schemas/versionNumber/schemaLocationInDDMSDownloadablePackage</code>.
 * </p>
 * 
 * <p>
 * This class is intended for use in a single-threaded environment.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DDMSVersion {
	
	private static String _currentVersion;
	
	/** The default version of DDMS, used to construct DDMS components when no specific version is mentioned */
	public static final String DEFAULT_VERSION = PropertyReader.getProperty("ddms.defaultVersion");
	
	private static final List<String> SUPPORTED_DDMS_VERSIONS = PropertyReader.getListProperty("ddms.supportedVersions");
	private static final List<String> DDMS_NAMESPACES = PropertyReader.getListProperty("ddms.xmlNamespaces");
	private static final List<String> DDMS_SCHEMA_LOCATIONS = PropertyReader.getListProperty("ddms.xsdLocations");
	private static final List<String> GML_NAMESPACES = PropertyReader.getListProperty("gml.xmlNamespaces");
	private static final List<String> GML_SCHEMA_LOCATIONS = PropertyReader.getListProperty("gml.xsdLocations");
		
	/**
	 * Private to prevent instantiation
	 */
	private DDMSVersion() {}
	
	/**
	 * Allows a reverse lookup from a DDMS namespace URI to a DDMS version
	 * 
	 * @param ddmsNamespaceURI the namespace to check
	 * @return a ddms version or empty string if it is not valid.
	 */
	public static String getVersionFor(String ddmsNamespaceURI) {
		int index = DDMS_NAMESPACES.indexOf(ddmsNamespaceURI);
		return (index >= 0 ? SUPPORTED_DDMS_VERSIONS.get(index) : "");
	}
	
	/**
	 * Allows a reverse lookup from a DDMS namespace URI to a GML namespace URI
	 * 
	 * @param ddmsNamespaceURI the namespace to check
	 * @return a gml namepaces or empty string if it is not valid.
	 */
	public static String getGmlNamespaceFor(String ddmsNamespaceURI) {
		int index = DDMS_NAMESPACES.indexOf(ddmsNamespaceURI);
		return (index >= 0 ? GML_NAMESPACES.get(index) : "");
	}
	
	/**
	 * Sets the currentVersion which will be used for by DDMS component constructors to
	 * determine the namespace and schema to use.
	 * 
	 * @param version the new version, which must be supported by DDMSence
	 * @throws UnsupportedVersionException if the version is not supported
	 */
	public static synchronized void setCurrentVersion(String version) {
		if (!isSupported(version))
			throw new UnsupportedVersionException(version);
		_currentVersion = version;
	}
	
	/**
	 * Accessor for the current version. If not set, returns the default from the properties file.
	 */
	public static String getCurrentVersion() {
		return (Util.isEmpty(_currentVersion) ? DEFAULT_VERSION : _currentVersion);
	}
	
	/**
	 * Resets the current version to the default value.
	 */
	public static void clearCurrentVersion() {
		setCurrentVersion(DEFAULT_VERSION);
	}
	
	/**
	 * Accessor for the current version's index. This can be used to look up namespaces and schemas.
	 */
	private static int getCurrentVersionIndex() {
		return (SUPPORTED_DDMS_VERSIONS.indexOf(getCurrentVersion()));
	}
	
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
	 * Returns the local schema location mapping to the currently in-use version of DDMS
	 * 
	 * @return the schema location
	 */
	public static String getCurrentSchema() throws UnsupportedVersionException {
		return (DDMS_SCHEMA_LOCATIONS.get(getCurrentVersionIndex()));
	}
	
	/**
	 * Returns the namespace mapping to the currently in-use version of DDMS
	 * 
	 * @return the namespace
	 */
	public static String getCurrentNamespace() throws UnsupportedVersionException {
		return (DDMS_NAMESPACES.get(getCurrentVersionIndex()));
	}
	
	/**
	 * Returns the local GML Profile schema location mapping to the currently in-use version of DDMS
	 * 
	 * @return the schema location
	 */
	public static String getCurrentGmlSchema() throws UnsupportedVersionException {
		return (GML_SCHEMA_LOCATIONS.get(getCurrentVersionIndex()));
	}
	
	/**
	 * Returns the GML namespace mapping to the currently in-use version of DDMS
	 * 
	 * @return the namespace
	 */
	public static String getCurrentGmlNamespace() throws UnsupportedVersionException {
		return (GML_NAMESPACES.get(getCurrentVersionIndex()));
	}
}
