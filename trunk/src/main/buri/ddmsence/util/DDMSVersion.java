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
package buri.ddmsence.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Element;
import buri.ddmsence.ddms.UnsupportedVersionException;
import buri.ddmsence.ddms.security.ISMVocabulary;

/**
 * Manages the supported versions of DDMS.
 * 
 * <p>
 * This class is the extension point for supporting new DDMS versions in the future. DDMSVersion maintains a static 
 * currentVersion variable which can be set at runtime. All DDMS component constructors which build components from 
 * scratch can then call <code>DDMSVersion.getCurrentVersion()</code> to access various details such as schema 
 * locations and namespace URIs. If no currentVersion has been set, a default will be used, which maps to 
 * <code>buri.ddmsence.ddms.defaultVersion</code> in the properties file. This defaults to 3.1 right now.</p>
 * 
 * <p>
 * The ddmsence.properties file has a property, <code>ddms.supportedVersions</code> which can be a comma-separated list of version
 * numbers. Each of these token values then has a set of six properties which identify the namespace and schema locations for each
 * DDMS version:
 * </p>
 * 
 * <li><code>&lt;versionNumber&gt;.ddms.xmlNamespace</code>: i.e. "http://metadata.dod.mil/mdr/ns/DDMS/2.0/"</li>
 * <li><code>&lt;versionNumber&gt;.ddms.xsdLocation</code>: i.e. "/schemas/2.0/DDMS/DDMS-v2_0.xsd"</li>
 * <li><code>&lt;versionNumber&gt;.gml.xmlNamespace</code>: i.e. "http://www.opengis.net/gml"</li>
 * <li><code>&lt;versionNumber&gt;.gml.xsdLocation</code>: i.e. "/schemas/2.0/DDMS/DDMS-GML-Profile.xsd"</li>
 * <li><code>&lt;versionNumber&gt;.ism.cveLocation</code>: i.e. "/schemas/2.0/ISM/CVE/"</li>
 * <li><code>&lt;versionNumber&gt;.ism.xmlNamespace</code>: i.e. "urn:us:gov:ic:ism:v2"</li>
 * <li><code>&lt;versionNumber&gt;.xlink.xmlNamespace</code>: i.e. "http://www.w3.org/1999/xlink"</li>
 * 
 * <p>
 * The format of an xsdLocation should generally follow
 * <code>/schemas/&lt;versionNumber&gt;/schemaLocationInDataDirectory</code>.
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
	private String _ismCveLocation;
	private String _ismNamespace;
	private String _xlinkNamespace;
	
	private static DDMSVersion _currentVersion;

	private static final Map<String, DDMSVersion> VERSIONS_TO_DETAILS = new HashMap<String, DDMSVersion>();
	static {
		VERSIONS_TO_DETAILS.put("2.0", new DDMSVersion("2.0"));
		VERSIONS_TO_DETAILS.put("3.0", new DDMSVersion("3.0"));
		VERSIONS_TO_DETAILS.put("3.1", new DDMSVersion("3.1"));
		_currentVersion = getVersionFor(PropertyReader.getProperty("ddms.defaultVersion"));
	}
			
	/**
	 * Private to prevent instantiation
	 * 
	 * @param version the number as shown in ddms.supportedVersions.
	 */
	private DDMSVersion(String version) {
		int index = getSupportedVersionsProperty().indexOf(version);
		_version = version;
		_namespace = getSupportedDDMSNamespacesProperty().get(index);
		_schema = PropertyReader.getProperty(version + ".ddms.xsdLocation");
		_gmlNamespace = PropertyReader.getProperty(version + ".gml.xmlNamespace");
		_gmlSchema = PropertyReader.getProperty(version + ".gml.xsdLocation");
		_ismCveLocation = PropertyReader.getProperty(version + ".ism.cveLocation");
		_ismNamespace = PropertyReader.getProperty(version + ".ism.xmlNamespace");
		_xlinkNamespace = PropertyReader.getProperty(version + ".xlink.xmlNamespace");
	}
	
	/**
	 * Returns a list of supported DDMS versions
	 * 
	 * @return List of string version numbers
	 */
	public static List<String> getSupportedVersions() {
		return Collections.unmodifiableList(getSupportedVersionsProperty());
	}
	
	/**
	 * Private accessor for the property containing the supported versions list
	 * 
	 * @return List of String version numbers
	 */
	private static List<String> getSupportedVersionsProperty() {
		return (PropertyReader.getListProperty("ddms.supportedVersions"));
	}
	
	/**
	 * Private accessor for the property containing the supported DDMS XML namespace list
	 * 
	 * @return List of String version numbers
	 */
	private static List<String> getSupportedDDMSNamespacesProperty() {
		List<String> supportedNamespaces = new ArrayList<String>();
		for (String version : getSupportedVersionsProperty()) {
			supportedNamespaces.add(PropertyReader.getProperty(version + ".ddms.xmlNamespace"));
		}
		return (supportedNamespaces);
	}
	
	/**
	 * Checks if an XML namespace is included in the list of supported XML namespaces for DDMS
	 * 
	 * @param xmlNamespace the namespace to test
	 * @return true if the namespace is supported
	 */
	public static boolean isSupportedDDMSNamespace(String xmlNamespace) {
		return (getSupportedDDMSNamespacesProperty().contains(xmlNamespace));
	}
	
	/**
	 * Checks if some element in a DDMS XML namespace is compatible with some DDMS version, based on the XML namespace.
	 * 
	 * @param ddmsVersion the version expected
	 * @param ddmsElement the element containing the namespaceURI to test. This only works on DDMS namespaces, not GML or IC.
	 * @return true if the element is in the correct version's namespace, false otherwise
	 */
	public static boolean isCompatibleWithVersion(String ddmsVersion, Element ddmsElement) {
		Util.requireValue("test version", ddmsVersion);
		Util.requireValue("test element", ddmsElement);
		DDMSVersion version = DDMSVersion.getVersionFor(ddmsVersion);
		return (version.getNamespace().equals(ddmsElement.getNamespaceURI()));
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
		if (!getSupportedVersionsProperty().contains(version))
			throw new UnsupportedVersionException(version);
		return (VERSIONS_TO_DETAILS.get(version));
	}
		
	/**
	 * Returns the DDMSVersion instance mapped to a particular XML namespace. If the
	 * namespace is shared by multiple versions of DDMS, the most recent will be
	 * returned.
	 * 
	 * @param namespace the DDMS XML namespace
	 * @return the instance
	 * @throws UnsupportedVersionException if the version number is not supported
	 */
	public static DDMSVersion getVersionForDDMSNamespace(String namespace) {
		List<DDMSVersion> versions = new ArrayList<DDMSVersion>(VERSIONS_TO_DETAILS.values());
		Collections.reverse(versions);
		for (DDMSVersion version : versions) {
			if (version.getNamespace().equals(namespace))
				return (version);
		}
		throw new UnsupportedVersionException("for XML namespace " + namespace);
	}
	
	/**
	 * Sets the currentVersion which will be used for by DDMS component constructors to determine the namespace and
	 * schema to use. Also updates the ISMVersion on the ISMVocabulary class, which is used to determine
	 * which set of IC CVEs to validate with (V2 vs. V5).
	 * 
	 * @param version the new version, which must be supported by DDMSence
	 * @throws UnsupportedVersionException if the version is not supported
	 */
	public static synchronized void setCurrentVersion(String version) {
		version = aliasVersion(version);
		if (!getSupportedVersionsProperty().contains(version))
			throw new UnsupportedVersionException(version);
		_currentVersion = getVersionFor(version);
		ISMVocabulary.setDDMSVersion(getCurrentVersion());
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
	 * Accessor for the ISM CVE location
	 */
	public String getIsmCveLocation() {
		return _ismCveLocation;
	}
	
	/**
	 * Accessor for the ISM namespace
	 */
	public String getIsmNamespace() {
		return _ismNamespace;
	}
	
	/**
	 * Accessor for the xlink namespace
	 */
	public String getXlinkNamespace() {
		return _xlinkNamespace;
	}
}
