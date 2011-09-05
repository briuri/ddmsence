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
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import buri.ddmsence.ddms.InvalidDDMSException;

/**
 * A collection of DDMSReader tests.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class DDMSReaderTest extends TestCase {

	private DDMSReader _reader;

	public DDMSReaderTest() throws SAXException {
		_reader = new DDMSReader();
	}

	public void testGetElementNullFile() throws InvalidDDMSException {
		try {
			getReader().getElement((File) null);
			fail("Allowed invalid data.");
		} catch (IOException e) {
			fail("Allowed invalid data.");
		} catch (IllegalArgumentException e) {
			// Good
		}
	}

	public void testGetElementNullString() throws InvalidDDMSException {
		try {
			getReader().getElement((String) null);
			fail("Allowed invalid data.");
		} catch (IOException e) {
			fail("Allowed invalid data.");
		} catch (IllegalArgumentException e) {
			// Good
		}
	}

	public void testGetElementNullInputStream() throws InvalidDDMSException {
		try {
			getReader().getElement((InputStream) null);
			fail("Allowed invalid data.");
		} catch (IOException e) {
			fail("Allowed invalid data.");
		} catch (IllegalArgumentException e) {
			// Good
		}
	}

	public void testGetElementNullReader() throws InvalidDDMSException {
		try {
			getReader().getElement((Reader) null);
			fail("Allowed invalid data.");
		} catch (IOException e) {
			fail("Allowed invalid data.");
		} catch (IllegalArgumentException e) {
			// Good
		}
	}

	public void testGetElementDoesNotExistFile() throws InvalidDDMSException {
		try {
			getReader().getElement(new File("doesnotexist"));
			fail("Allowed invalid data.");
		} catch (IOException e) {
			// Good
		}
	}

	public void testGetElementDoesNotExistString() throws InvalidDDMSException {
		try {
			getReader().getElement("<wrong></wrong>");
			fail("Allowed invalid data.");
		} catch (IOException e) {
			fail("Should have thrown an InvalidDDMSException");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testGetElementDoesNotExistInputStream() throws InvalidDDMSException {
		try {
			getReader().getElement(new FileInputStream(new File("doesnotexist")));
			fail("Allowed invalid data.");
		} catch (IOException e) {
			// Good
		}
	}

	public void testGetElementDoesNotExistReader() throws InvalidDDMSException {
		try {
			getReader().getElement(new FileReader(new File("doesnotexist")));
			fail("Allowed invalid data.");
		} catch (IOException e) {
			// Good
		}
	}

	public void testGetElementNotXML() throws IOException {
		try {
			getReader().getElement(new File("conf/ddmsence.properties"));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testGetElementFileSuccess() throws InvalidDDMSException, IOException {
		getReader().getElement(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml"));
	}

	public void testGetElementStringSuccess() throws InvalidDDMSException, IOException {
		getReader().getElement(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><ddms:language "
				+ " xmlns:ddms=\"http://metadata.dod.mil/mdr/ns/DDMS/3.0/\" "
				+ " ddms:qualifier=\"http://purl.org/dc/elements/1.1/language\" ddms:value=\"en\" />");
	}

	public void testGetElementInputStreamSuccess() throws InvalidDDMSException, IOException {
		getReader().getElement(
			new FileInputStream(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml")));
	}

	public void testGetElementReaderSuccess() throws InvalidDDMSException, IOException {
		getReader()
			.getElement(new FileReader(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml")));
	}

	public void testGetResourceFailure() throws IOException {
		try {
			getReader().getDDMSResource(new File(PropertyReader.getProperty("test.unit.data"), "3.0/rights.xml"));
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testGetResourceSuccessFile() throws InvalidDDMSException, IOException {
		getReader().getDDMSResource(new File(PropertyReader.getProperty("test.unit.data"), "3.0/resource.xml"));
	}

	public void testGetResourceSuccessString() throws InvalidDDMSException, IOException {
		LineNumberReader reader = new LineNumberReader(new FileReader(new File(
			PropertyReader.getProperty("test.unit.data"), "3.0/resource.xml")));
		StringBuffer xmlString = new StringBuffer();
		String nextLine = reader.readLine();
		while (nextLine != null) {
			xmlString.append(nextLine);
			nextLine = reader.readLine();
		}
		getReader().getDDMSResource(xmlString.toString());
	}

	public void testGetResourceSuccessInputStream() throws InvalidDDMSException, IOException {
		getReader().getDDMSResource(
			new FileInputStream(new File(PropertyReader.getProperty("test.unit.data"), "3.0/resource.xml")));
	}

	public void testGetResourceSuccessReader() throws InvalidDDMSException, IOException {
		getReader().getDDMSResource(
			new FileReader(new File(PropertyReader.getProperty("test.unit.data"), "3.0/resource.xml")));
	}

	public void testGetExternalSchemaLocation() {
		String externalLocations = getReader().getExternalSchemaLocations();
		assertEquals(16, externalLocations.split(" ").length);
		assertTrue(externalLocations.contains("http://metadata.dod.mil/mdr/ns/DDMS/2.0/"));
		assertTrue(externalLocations.contains("http://metadata.dod.mil/mdr/ns/DDMS/3.0/"));
		assertTrue(externalLocations.contains("http://www.opengis.net/gml"));
		assertTrue(externalLocations.contains("http://www.opengis.net/gml/3.2"));
	}

	/**
	 * Accessor for the reader
	 */
	private DDMSReader getReader() {
		return _reader;
	}
}
