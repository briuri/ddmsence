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

import nu.xom.Element;

import buri.ddmsence.ddms.UnsupportedVersionException;

/**
 * Manages the supported versions of DDMS.
 * 
 * <p>
 * This class is the extension point for supporting new DDMS versions in the future. DDMSVersion maintains a static 
 * currentVersion variable which can be set at runtime. All DDMS component constructors which build components from scratch
 * can then call <code>DDMSVersion.getCurrentVersion()</code> to access various details such as schema locations and namespace
 * URIs. If no currentVersion has been set, a default will be used, which maps to 
 * <code>buri.ddmsence.ddms.defaultVersion</code> in the properties file. This defaults to 3.0 right now.</p>
 * 
 * <p>
 * The ddmsence.properties file has six properties which can be a comma-separated list:
 * </p>
 * 
 * 
 * <li><code>ddms.supportedVersions</code>: i.e. "2.0,3.0"</li>
 * <li><code>ddms.xmlNamespaces</code>: i.e. "http://metadata.dod.mil/mdr/ns/DDMS/2.0/,http://metadata.dod.mil/mdr/ns/DDMS/3.0/"</li>
 * <li><code>ddms.xsdLocations</code>: i.e. "/schemas/2.0/DDMS/2.0/DDMS-v2_0.xsd,/schemas/3.0/DDMS/3.0/DDMS-v3_0.xsd"</li>
 * <li><code>gml.xmlNamespaces</code>: i.e. "http://www.opengis.net/gml,http://www.opengis.net/gml/3.2"</li>
 * <li><code>gml.xsdLocations</code>: i.e. "/schemas/2.0/DDMS/2.0/DDMS-GML-Profile.xsd,/schemas/3.0/DDMS/3.0/DDMS-GML-Profile.xsd"</li>
 * <li><code>icism.xmlNamespaces</code>: i.e. "urn:us:gov:ic:ism:v2,urn:us:gov:ic:ism"</li>
 *  
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
	
	private String _version;
	private String _namespace;
	private String _schema;
	private String _gmlNamespace;
	private String _gmlSchema;
	private String _icismNamespace;
	
	private static DDMSVersion _currentVersion;
	private static final String DEFAULT_VERSION_NUMBER = PropertyReader.getProperty("ddms.defaultVersion");
	private static final List<String> SUPPORTED_DDMS_VERSIONS = PropertyReader.getListProperty("ddms.supportedVersions");
	private static final List<String> DDMS_NAMESPACES = PropertyReader.getListProperty("ddms.xmlNamespaces");
	private static final List<String> DDMS_SCHEMA_LOCATIONS = PropertyReader.getListProperty("ddms.xsdLocations");
	private static final List<String> GML_NAMESPACES = PropertyReader.getListProperty("gml.xmlNamespaces");
	private static final List<String> GML_SCHEMA_LOCATIONS = PropertyReader.getListProperty("gml.xsdLocations");
	private static final List<String> ICISM_NAMESPACES = PropertyReader.getListProperty("icism.xmlNamespaces");
	
	/** Details concerning DDMS 2.0 */
	public static final DDMSVersion DDMS_2_0 = new DDMSVersion("2.0");
	
	/** Details concerning DDMS 3.0 */
	public static final DDMSVersion DDMS_3_0 = new DDMSVersion("3.0");
	
	private static final Map<String, DDMSVersion> VERSIONS_TO_DETAILS = new HashMap<String, DDMSVersion>();
	static {
		VERSIONS_TO_DETAILS.put("2.0", DDMS_2_0);
		VERSIONS_TO_DETAILS.put("3.0", DDMS_3_0);
		_currentVersion = getVersionFor(DEFAULT_VERSION_NUMBER);
	}
			
	/**
	 * Private to prevent instantiation
	 * 
	 * @param version the number as shown in ddms.supportedVersions.
	 */
	private DDMSVersion(String version) {
		if (!SUPPORTED_DDMS_VERSIONS.contains(version))
			throw new UnsupportedVersionException(version);
		int index = SUPPORTED_DDMS_VERSIONS.indexOf(version);
		_version = version;
		_namespace = DDMS_NAMESPACES.get(index);
		_schema = DDMS_SCHEMA_LOCATIONS.get(index);
		_gmlNamespace = GML_NAMESPACES.get(index);
		_gmlSchema = GML_SCHEMA_LOCATIONS.get(index);
		_icismNamespace = ICISM_NAMESPACES.get(index);
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
	 * Checks if some DDMS element matches some DDMS version
	 * 
	 * @param ddmsVersion the version expected
	 * @param ddmsElement the element containing the namespaceURI to test.
	 * @return true if the element is in the correct version's namespace, false otherwise
	 */
	public static boolean isVersion(String ddmsVersion, Element ddmsElement) {
		Util.requireValue("test version", ddmsVersion);
		Util.requireValue("test element", ddmsElement);
		return (ddmsVersion.equals(DDMSVersion.getVersionForNamespace(ddmsElement.getNamespaceURI()).getVersion()));
	}
	
	/**
	 * Checks if the string matches the current version.
	 * 
	 * @param ddmsVersion the version to test
	 * @return true if the current version matches the test version, false otherwise
	 */
	public static boolean isCurrentVersion(String ddmsVersion) {
		return (getCurrentVersion().getVersion().equals(ddmsVersion));
	}
	
	/**
	 * Returns the DDMSVersion instance mapped to a particular version number.
	 * 
	 * @param version a version number
	 * @return the instance
	 * @throws UnsupportedVersionException if the version number is not supported
	 */
	public static DDMSVersion getVersionFor(String version) {
		if (!SUPPORTED_DDMS_VERSIONS.contains(version))
			throw new UnsupportedVersionException(version);
		return (VERSIONS_TO_DETAILS.get(version));
	}
		
	/**
	 * Returns the DDMSVersion instance mapped to a DDMS namespace URI
	 * 
	 * @param ddmsNamespaceURI the namespace to check
	 * @return a DDMS version or null if it is not mapped to a version
	 */
	public static DDMSVersion getVersionForNamespace(String ddmsNamespaceURI) {
		int index = DDMS_NAMESPACES.indexOf(ddmsNamespaceURI);
		if (index == -1)
			return (null);
		return (getVersionFor(SUPPORTED_DDMS_VERSIONS.get(index)));
	}
	
	/**
	 * Returns the DDMSVersion instance mapped to a GML namespace URI
	 * 
	 * @param gmlNamespaceURI the namespace to check
	 * @return a DDMS version or null if it is not mapped to a version
	 */
	public static DDMSVersion getVersionForGmlNamespace(String gmlNamespaceURI) {
		int index = GML_NAMESPACES.indexOf(gmlNamespaceURI);
		if (index == -1)
			return (null);
		return (getVersionFor(SUPPORTED_DDMS_VERSIONS.get(index)));
	}

	/**
	 * Sets the currentVersion which will be used for by DDMS component constructors to
	 * determine the namespace and schema to use.
	 * 
	 * @param version the new version, which must be supported by DDMSence
	 * @throws UnsupportedVersionException if the version is not supported
	 */
	public static synchronized void setCurrentVersion(String version) {
		if (!SUPPORTED_DDMS_VERSIONS.contains(version))
			throw new UnsupportedVersionException(version);
		_currentVersion = getVersionFor(version);
	}
	
	/**
	 * Accessor for the current version. If not set, returns the default from the properties file.
	 */
	public static DDMSVersion getCurrentVersion() {
		return (_currentVersion);
	}
	
	/**
	 * Resets the current version to the default value.
	 */
	public static void clearCurrentVersion() {
		setCurrentVersion(DEFAULT_VERSION_NUMBER);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return (getVersion());
	}
	
	/**
	 * Accessor for the version number
	 */
	public String getVersion() {
		return _version;
	}
	
	/**
	 * Accessor for the DDMS namespace
	 */
	public String getNamespace() {
		return _namespace;
	}

	/**
	 * Accessor for the DDMS schema location
	 */
	public String getSchema() {
		return _schema;
	}

	/**
	 * Accessor for the gml namespace
	 */
	public String getGmlNamespace() {
		return _gmlNamespace;
	}

	/**
	 * Accessor for the gml schema location
	 */
	public String getGmlSchema() {
		return _gmlSchema;
	}
	
	/**
	 * Accessor for the ICISM namespace
	 */
	public String getIcismNamespace() {
		return _icismNamespace;
	}
}
