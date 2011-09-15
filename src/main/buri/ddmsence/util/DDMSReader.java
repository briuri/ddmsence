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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;

/**
 * Reader class which loads an XML file containing DDMS information and converts it into XOM elements.
 * 
 * <p>
 * This parsing performs schema validation against a local set of DDMS/ISM schemas.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DDMSReader {

	private XMLReader _reader;
	
	private static final String PROP_XERCES_VALIDATION = "http://xml.org/sax/features/validation";
	private static final String PROP_XERCES_SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
	private static final String PROP_XERCES_EXTERNAL_LOCATION =
		"http://apache.org/xml/properties/schema/external-schemaLocation";
	
	/**
	 * Constructor
	 * 
	 * <p>Schemas are loaded in reverse order, so the latest, greatest copy is always first to be looked for.</p>
	 * 
	 * Creates a DDMSReader which can process various versions of DDMS and GML
	 */
	public DDMSReader() throws SAXException {
		_reader = XMLReaderFactory.createXMLReader(PropertyReader.getProperty("xmlReader.class"));
		StringBuffer schemas = new StringBuffer();
		List<String> versions = new ArrayList<String>(DDMSVersion.getSupportedVersions());
		Collections.reverse(versions);
		for (String versionString : versions) {
			DDMSVersion version = DDMSVersion.getVersionFor(versionString);
			String xsd = getLocalSchemaLocation(version.getSchema());
			schemas.append(version.getNamespace()).append(" ").append(xsd).append(" ");
			xsd = getLocalSchemaLocation(version.getGmlSchema());
			schemas.append(version.getGmlNamespace()).append(" ").append(xsd).append(" ");
			if (!Util.isEmpty(version.getNtkSchema())) {
				xsd = getLocalSchemaLocation(version.getNtkSchema());
				schemas.append(version.getNtkNamespace()).append(" ").append(xsd).append(" ");
			}
		}
		getReader().setFeature(PROP_XERCES_VALIDATION, true);
		getReader().setFeature(PROP_XERCES_SCHEMA_VALIDATION, true);
		getReader().setProperty(PROP_XERCES_EXTERNAL_LOCATION, schemas.toString().trim());
	}
	
	/**
	 * Returns the full path to a local schema copy, based on the relative location from the
	 * properties file. The full path will have spaces escaped as %20, to resolve Issue 50
	 * in the DDMSence Issue Tracker.
	 * 
	 * @param schemaLocation the relative schema location as specified in the properties file
	 * @return the full path to the schema (generally this is in the JAR file)
	 * @throws IllegalArgumentException if the schema could not be found.
	 */
	private String getLocalSchemaLocation(String schemaLocation) {
		URL xsd = getClass().getResource(schemaLocation);
		if (xsd == null)
			throw new IllegalArgumentException("Unable to load a local copy of the schema for validation.");
		String fullPath = xsd.toExternalForm().replaceAll(" ", "%20");		
		return (fullPath);
	}

	/**
	 * Creates a XOM element representing the root XML element in the file.
	 * 
	 * <p>The implementation of this method delegates to the Reader-based overloaded method.</p>
	 * 
	 * @param file the file containing the XML document
	 * @return a XOM element representing the root node in the document
	 */
	public Element getElement(File file) throws IOException, InvalidDDMSException {
		Util.requireValue("file", file);
		return (getElement(new FileReader(file)));
	}
	
	/**
	 * Creates a XOM element representing the root XML element in a string representation of an XML document.
	 * 
	 * <p>The implementation of this method delegates to the Reader-based overloaded method.</p>
	 * 
	 * @param xml a string containing the XML document
	 * @return a XOM element representing the root node in the document 
	 */
	public Element getElement(String xml) throws IOException, InvalidDDMSException {
		Util.requireValue("XML string", xml);
		return (getElement(new StringReader(xml)));
	}
	
	/**
	 * Creates a XOM element representing the root XML element in an input stream.
	 * 
	 * <p>The implementation of this method delegates to the Reader-based overloaded method.</p>
	 * 
	 * @param inputStream a stream mapping to an XML document
	 * @return a XOM element representing the root node in the document 
	 */
	public Element getElement(InputStream inputStream) throws IOException, InvalidDDMSException {
		Util.requireValue("input stream", inputStream);
		return (getElement(new InputStreamReader(inputStream)));
	}
		
	/**
	 * Creates a XOM element representing the root XML element in a reader.
	 * 
	 * @param reader a reader mapping to an XML document
	 * @return a XOM element representing the root node in the document
	 */
	public Element getElement(Reader reader) throws IOException, InvalidDDMSException {
		Util.requireValue("reader", reader);
		try {
			Builder builder = new Builder(getReader(), true);
			Document doc = builder.build(reader);
			return (doc.getRootElement());
		}
		catch (ParsingException e) {
			throw new InvalidDDMSException(e);
		}
	}
	
	/**
	 * Creates a DDMS resource based on the contents of a file, and also sets the DDMSVersion based on the namespace
	 * URIs in the file).
	 * 
	 * @param file the file containing the DDMS Resource.
	 * @return a DDMS Resource
	 * @throws InvalidDDMSException if the component could not be built
	 */
	public Resource getDDMSResource(File file) throws IOException, InvalidDDMSException {
		return (buildResource(getElement(file)));
	}
	
	/**
	 * Creates a DDMS resource based on the contents of a string representation of an XML document, and also sets the
	 * DDMSVersion based on the namespace URIs in the document).
	 * 
	 * @param xml the string representation of the XML DDMS Resource
	 * @return a DDMS Resource
	 * @throws InvalidDDMSException if the component could not be built
	 */
	public Resource getDDMSResource(String xml) throws IOException, InvalidDDMSException {
		return (buildResource(getElement(xml)));
	}
	
	/**
	 * Creates a DDMS resource based on the contents of an input stream, and also sets the DDMSVersion based on the
	 * namespace URIs in the document).
	 * 
	 * @param inputStream the input stream wrapped around an XML DDMS Resource
	 * @return a DDMS Resource
	 * @throws InvalidDDMSException if the component could not be built
	 */
	public Resource getDDMSResource(InputStream inputStream) throws IOException, InvalidDDMSException {
		return (buildResource(getElement(inputStream)));
	}
	
	/**
	 * Creates a DDMS resource based on the contents of a reader, and also sets the DDMSVersion based on the namespace
	 * URIs in the document).
	 * 
	 * @param reader the reader wrapped around an XML DDMS Resource
	 * @return a DDMS Resource
	 * @throws InvalidDDMSException if the component could not be built
	 */
	public Resource getDDMSResource(Reader reader) throws IOException, InvalidDDMSException {
		return (buildResource(getElement(reader)));
	}
	
	/**
	 * Shared helper method to build a DDMS Resource from a XOM Element
	 * 
	 * @param xomElement
	 * @return a DDMS Resource
	 * @throws InvalidDDMSException if the component could not be built
	 */
	protected Resource buildResource(Element xomElement) throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion(DDMSVersion.getVersionForNamespace(xomElement.getNamespaceURI())
			.getVersion());
		return (new Resource(xomElement));
	}
		
	/**
	 * Returns the external schema locations for debugging. The returned string will contain a space-delimited set
	 * of XMLNamespace/SchemaLocation pairs.
	 * 
	 * @return the string containing all schema locations
	 */
	public String getExternalSchemaLocations() {
		try {
			return ((String) getReader().getProperty(PROP_XERCES_EXTERNAL_LOCATION));
		}
		catch (SAXException e) {
			throw new IllegalStateException(PROP_XERCES_EXTERNAL_LOCATION
				+ " is not supported or recognized for this XMLReader.");
		}
	}
	
	/**
	 * Accessor for the reader
	 */
	private XMLReader getReader() {
		return _reader;
	}
}