/* Copyright 2010 - 2016 by Brian Uri!
   
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
   home page is located at https://ddmsence.urizone.net/
 */
package buri.ddmsence.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;

import org.junit.Test;
import org.xml.sax.SAXException;

import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.InvalidDDMSException;

/**
 * A collection of DDMSReader tests.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DDMSReaderTest extends AbstractBaseTestCase {

	public DDMSReaderTest() throws SAXException {
		super(null);
	}

	@Test
	public void testGetElementNullFile() throws InvalidDDMSException {
		try {
			getReader(null).getElement((File) null);
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "file must exist.");
		}
	}

	@Test
	public void testGetElementNullString() throws InvalidDDMSException {
		try {
			getReader(null).getElement((String) null);
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "XML string must exist.");
		}
	}

	@Test
	public void testGetElementNullInputStream() throws InvalidDDMSException {
		try {
			getReader(null).getElement((InputStream) null);
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "input stream must exist.");
		}
	}

	@Test
	public void testGetElementNullReader() throws InvalidDDMSException {
		try {
			getReader(null).getElement((Reader) null);
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "reader must exist.");
		}
	}

	@Test
	public void testGetElementDoesNotExistFile() throws InvalidDDMSException {
		try {
			getReader(null).getElement(new File("doesnotexist"));
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			expectMessage(e, "doesnotexist (The system cannot find the file specified)");
		}
	}

	@Test
	public void testGetElementDoesNotExistString() throws InvalidDDMSException {
		try {
			getReader(null).getElement("<wrong></wrong>");
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			fail("Should have thrown an InvalidDDMSException");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "nu.xom.ValidityException");
		}
	}

	@Test
	public void testGetElementDoesNotExistInputStream() throws InvalidDDMSException {
		try {
			getReader(null).getElement(new FileInputStream(new File("doesnotexist")));
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			expectMessage(e, "doesnotexist (The system cannot find the file specified)");
		}
	}

	@Test
	public void testGetElementDoesNotExistReader() throws InvalidDDMSException {
		try {
			getReader(null).getElement(new FileReader(new File("doesnotexist")));
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			expectMessage(e, "doesnotexist (The system cannot find the file specified)");
		}
	}

	@Test
	public void testGetElementNotXML() throws IOException {
		try {
			getReader(null).getElement(new File("conf/ddmsence.properties"));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "nu.xom.ParsingException");
		}
	}

	@Test
	public void testGetElementFileSuccess() throws InvalidDDMSException, IOException {
		getReader("3.0").getElement(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml"));
	}

	@Test
	public void testGetElementStringSuccess() throws InvalidDDMSException, IOException {
		getReader("3.0").getElement(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><ddms:language "
				+ " xmlns:ddms=\"http://metadata.dod.mil/mdr/ns/DDMS/3.0/\" "
				+ " ddms:qualifier=\"http://purl.org/dc/elements/1.1/language\" ddms:value=\"en\" />");
	}

	@Test
	public void testGetElementInputStreamSuccess() throws InvalidDDMSException, IOException {
		getReader("3.0").getElement(
			new FileInputStream(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml")));
	}

	@Test
	public void testGetElementReaderSuccess() throws InvalidDDMSException, IOException {
		getReader("3.0").getElement(new FileReader(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml")));
	}

	@Test
	public void testGetResourceFailure() throws IOException {
		try {
			getReader("3.0").getDDMSResource(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml"));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "Unexpected namespace URI and local name encountered");
		}
	}

	@Test
	public void testGetResourceSuccessFile() throws InvalidDDMSException, IOException {
		DDMSVersion.setCurrentVersion("3.0");
		getReader("3.0").getDDMSResource(new File(PropertyReader.getProperty("test.unit.data"), "3.0/resource.xml"));
	}

	@Test
	public void testGetResourceSuccessString() throws InvalidDDMSException, IOException {
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new FileReader(new File(PropertyReader.getProperty("test.unit.data"),
				"3.0/resource.xml")));
			StringBuffer xmlString = new StringBuffer();
			String nextLine = reader.readLine();
			while (nextLine != null) {
				xmlString.append(nextLine);
				nextLine = reader.readLine();
			}
			getReader("3.0").getDDMSResource(xmlString.toString());
		}
		finally {
			if (reader != null)
				reader.close();
		}
	}

	@Test
	public void testGetResourceSuccessInputStream() throws InvalidDDMSException, IOException {
		getReader("3.0").getDDMSResource(
			new FileInputStream(new File(PropertyReader.getProperty("test.unit.data"), "3.0/resource.xml")));
	}

	@Test
	public void testGetResourceSuccessReader() throws InvalidDDMSException, IOException {
		getReader("3.0").getDDMSResource(
			new FileReader(new File(PropertyReader.getProperty("test.unit.data"), "3.0/resource.xml")));
	}

	@Test
	public void testGetExternalSchemaLocation() {
		String externalLocations = getReader("3.0").getExternalSchemaLocations();
		assertEquals(4, externalLocations.split(" ").length);
		assertTrue(externalLocations.contains("http://metadata.dod.mil/mdr/ns/DDMS/3.0/"));
		assertTrue(externalLocations.contains("http://www.opengis.net/gml"));
	}

	@Test
	public void testGetLocalSchemaLocation() {
		assertTrue(getReader("5.0").getLocalSchemaLocation("/schemas/5.0/DDMS/ddms.xsd").contains("/schemas/5.0/"));
		try {
			getReader("5.0").getLocalSchemaLocation("UnknownSchema");
			fail("Allowed unknown schema.");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "Unable to load");
		}
	}
	
	@Test
	public void testTypeSubstitutionExtensibleLayer() throws InvalidDDMSException, IOException {
		File metacard = new File(PropertyReader.getProperty("test.unit.data"), "2.0/DDMSENCE-223.xml");
		try {
			getReader("2.0").getDDMSResource(new FileReader(metacard));
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "nu.xom.ValidityException: cvc-elt.4.2");
		}
		
		DDMSReader reader = getReader("2.0");
		reader.addExternalSchemaLocation("https://ddmsence.atlassian.net/browse/DDMSENCE-223",
			PropertyReader.getProperty("test.unit.data") + "2.0/DDMSENCE-223.xsd");
		reader.getDDMSResource(new FileReader(metacard));
	}
	
	@Test
	public void testAlreadyKnownXmlNamespace() {
		DDMSReader reader = getReader("2.0");
		try {
			reader.addExternalSchemaLocation("http://metadata.dod.mil/mdr/ns/DDMS/2.0/", "somewhereElse");
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "XML Namespace has already been set");
		}
	}
	
	/**
	 * Accessor for the reader
	 */
	private DDMSReader getReader(String sVersion) {
		DDMSVersion version = (sVersion != null) ? DDMSVersion.getVersionFor(sVersion) : DDMSVersion.getCurrentVersion();
		try {
			return (new DDMSReader(version));
		}
		catch (SAXException e) {
			fail("Could not instantiate a reader.");
		}
		return (null);
	}
}
