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
 * currentVersion variable which can be set at runtime. All DDMS component constructors which build components from 
 * scratch can then call <code>DDMSVersion.getCurrentVersion()</code> to access various details such as schema 
 * locations and namespace URIs. If no currentVersion has been set, a default will be used, which maps to 
 * <code>buri.ddmsence.ddms.defaultVersion</code> in the properties file. This defaults to 3.0 right now.</p>
 * 
 * <p>
 * The ddmsence.properties file has six properties which can be a comma-separated list:
 * </p>
 * 
 * 
 * <li><code>ddms.supportedVersions</code>: i.e. "2.0,3.0"</li>
 * <li><code>ddms.xmlNamespaces</code>: 
 * i.e. "http://metadata.dod.mil/mdr/ns/DDMS/2.0/,http://metadata.dod.mil/mdr/ns/DDMS/3.0/"</li>
 * <li><code>ddms.xsdLocations</code>: 
 * i.e. "/schemas/2.0/DDMS/2.0/DDMS-v2_0.xsd,/schemas/3.0/DDMS/3.0/DDMS-v3_0.xsd"</li>
 * <li><code>gml.xmlNamespaces</code>: i.e. "http://www.opengis.net/gml,http://www.opengis.net/gml/3.2"</li>
 * <li><code>gml.xsdLocations</code>: 
 * i.e. "/schemas/2.0/DDMS/2.0/DDMS-GML-Profile.xsd,/schemas/3.0/DDMS/3.0/DDMS-GML-Profile.xsd"</li>
 * <li><code>icism.xmlNamespaces</code>: i.e. "urn:us:gov:ic:ism:v2,urn:us:gov:ic:ism"</li>
 *  
 * 
 * <p>
 * The number of items in each property must match the number of supported DDMS versions. The format of an xsdLocation 
 * should follow <code>/schemas/versionNumber/schemaLocationInDDMSDownloadablePackage</code>.
 * </p>
 * 
 * <p>
 * Because DDMS 3.0.1 is syntactically identical to DDMS 3.0, requests for version 3.0.1
 * will simply alias to DDMS 3.0. DDMS 3.0.1 is not set up as a separate batch of schemas and namespaces,
 * since none of the technical artifacts changed (3.0.1 was a documentation release).
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

	private static final Map<String, DDMSVersion> VERSIONS_TO_DETAILS = new HashMap<String, DDMSVersion>();
	static {
		VERSIONS_TO_DETAILS.put("2.0", new DDMSVersion("2.0"));
		VERSIONS_TO_DETAILS.put("3.0", new DDMSVersion("3.0"));
		_currentVersion = getVersionFor(PropertyReader.getProperty("ddms.defaultVersion"));
	}
			
	/**
	 * Private to prevent instantiation
	 * 
	 * @param version the number as shown in ddms.supportedVersions.
	 */
	private DDMSVersion(String version) {
		int index = getSupportVersionsProperty().indexOf(version);
		_version = version;
		_namespace = PropertyReader.getListProperty("ddms.xmlNamespaces").get(index);
		_schema = PropertyReader.getListProperty("ddms.xsdLocations").get(index);
		_gmlNamespace = PropertyReader.getListProperty("gml.xmlNamespaces").get(index);
		_gmlSchema = PropertyReader.getListProperty("gml.xsdLocations").get(index);
		_icismNamespace = PropertyReader.getListProperty("icism.xmlNamespaces").get(index);
	}
	
	/**
	 * Returns a list of supported DDMS versions
	 * 
	 * @return List of string version numbers
	 */
	public static List<String> getSupportedVersions() {
		return Collections.unmodifiableList(getSupportVersionsProperty());
	}
	
	/**
	 * Private accessor for the property containing the supported versions list
	 * 
	 * @return List of String version numbers
	 */
	private static List<String> getSupportVersionsProperty() {
		return (PropertyReader.getListProperty("ddms.supportedVersions"));
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
		return (aliasVersion(ddmsVersion).equals(DDMSVersion.getVersionForNamespace(ddmsElement.getNamespaceURI()).getVersion()));
	}
	
	/**
	 * Checks if the string matches the current version.
	 * 
	 * @param ddmsVersion the version to test
	 * @return true if the current version matches the test version, false otherwise
	 */
	public static boolean isCurrentVersion(String ddmsVersion) {
		return (getCurrentVersion().getVersion().equals(aliasVersion(ddmsVersion)));
	}
	
	/**
	 * Returns the DDMSVersion instance mapped to a particular version number.
	 * 
	 * @param version a version number
	 * @return the instance
	 * @throws UnsupportedVersionException if the version number is not supported
	 */
	public static DDMSVersion getVersionFor(String version) {
		version = aliasVersion(version);
		if (!getSupportVersionsProperty().contains(version))
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
		int index = PropertyReader.getListProperty("ddms.xmlNamespaces").indexOf(ddmsNamespaceURI);
		if (index == -1)
			return (null);
		return (getVersionFor(getSupportVersionsProperty().get(index)));
	}
	
	/**
	 * Returns the DDMSVersion instance mapped to a GML namespace URI
	 * 
	 * @param gmlNamespaceURI the namespace to check
	 * @return a DDMS version or null if it is not mapped to a version
	 */
	public static DDMSVersion getVersionForGmlNamespace(String gmlNamespaceURI) {
		int index = PropertyReader.getListProperty("gml.xmlNamespaces").indexOf(gmlNamespaceURI);
		if (index == -1)
			return (null);
		return (getVersionFor(getSupportVersionsProperty().get(index)));
	}

	/**
	 * Sets the currentVersion which will be used for by DDMS component constructors to determine the namespace and
	 * schema to use.
	 * 
	 * @param version the new version, which must be supported by DDMSence
	 * @throws UnsupportedVersionException if the version is not supported
	 */
	public static synchronized void setCurrentVersion(String version) {
		version = aliasVersion(version);
		if (!getSupportVersionsProperty().contains(version))
			throw new UnsupportedVersionException(version);
		_currentVersion = getVersionFor(version);
	}
	
	/**
	 * Treats version 3.0.1 of DDMS as an alias for DDMS 3.0. 3.0.1 is syntactically identical,
	 * and has the same namespaces and schemas.
	 * 
	 * @param version the raw version
	 * @return 3.0, if the raw version is 3.0.1. Otherwise, the version.
	 */
	private static String aliasVersion(String version) {
		if ("3.0.1".equals(version))
			return ("3.0");
		return (version);
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
		setCurrentVersion(PropertyReader.getProperty("ddms.defaultVersion"));
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
