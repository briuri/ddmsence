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

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
 * This parsing performs schema validation against a local set of DDMS/ICISM schemas.
 * </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DDMSReader {

	private XMLReader _reader;
	
	private static final String XML_READER_CLASS = PropertyReader.getProperty("xmlReader.class");
	private static final String PROP_XERCES_VALIDATION = "http://xml.org/sax/features/validation";
	private static final String PROP_XERCES_SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
	private static final String PROP_XERCES_EXTERNAL_LOCATION =
		"http://apache.org/xml/properties/schema/external-schemaLocation";
	
	/**
	 * Constructor
	 * 
	 * Creates a DDMSReader for the currently in-use version of DDMS.
	 */
	public DDMSReader() throws SAXException {
		this(DDMSVersion.getCurrentNamespace(), DDMSVersion.getCurrentSchema());
	}
	
	/**
	 * Constructor
	 * 
	 * <p>
	 * Generally, the parameter-less constructor is the simplest way to read a DDMS resource file.
	 * </p>
	 * 
	 * <p>
	 * By allowing the namespaceURI and schemaLocation to be passed in in this constructor,
	 * this DDMSReader can be used for non-Resource XML files. For example, the gml:Point unit tests
	 * create a DDMSReader that validates against the GML-Profile.xsd.
	 * </p>
	 * 
	 * @param namespaceURI the namespace URI for the schema file
	 * @param schemaLocation the local location of the schema file
	 */
	public DDMSReader(String namespaceURI, String schemaLocation) throws SAXException {
		Util.requireValue("schema location", schemaLocation);
		_reader = XMLReaderFactory.createXMLReader(XML_READER_CLASS);
		URL xsd = getClass().getResource(schemaLocation);
		if (xsd == null)
			throw new IllegalArgumentException("Unable to load a local copy of the schema for validation.");
		String externalLocation = namespaceURI + " " + xsd.toExternalForm();
		
		getReader().setFeature(PROP_XERCES_VALIDATION, true);
		getReader().setFeature(PROP_XERCES_SCHEMA_VALIDATION, true);
		getReader().setProperty(PROP_XERCES_EXTERNAL_LOCATION, externalLocation);
	}
	
	/**
	 * Accepts a file name and returns the root element in that file.
	 * 
	 * @param file the file containing the DDMS elements.
	 * @return a XOM element representing the DDMS component read from the root node of the file. 
	 */
	public Element getElement(File file) throws IOException, InvalidDDMSException {
		Util.requireValue("file", file);
		if (!file.exists())
			throw new IOException("Could not load file: " + file.getAbsolutePath());
		Builder builder = new Builder(getReader(), true);
		try {
			Document doc = builder.build(file);
			return (doc.getRootElement());
		}
		catch (ParsingException e) {
			throw new InvalidDDMSException(e);
		}
	}
	
	/**
	 * Accepts a file name and returns the DDMS Resource identified by the root element in that file.
	 * 
	 * @param file the file containing the DDMS Resource.
	 * @return a XOM element representing the DDMS component read from the root node of the file.
	 * @throws InvalidDDMSException if the component could not be built 
	 */
	public Resource getDDMSResource(File file) throws IOException, InvalidDDMSException {
		return (new Resource(getElement(file)));
	}
	
	/**
	 * Accessor for the reader
	 */
	private XMLReader getReader() {
		return _reader;
	}
}