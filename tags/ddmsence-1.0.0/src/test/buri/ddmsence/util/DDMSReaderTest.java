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
	
	private static final String TEST_DATA_DIR = PropertyReader.getProperty("test.unit.data");
	
	public DDMSReaderTest() throws SAXException {
		_reader = new DDMSReader(DDMSVersion.getDefaultNamespace(), DDMSVersion.getDefaultSchema());
	}
	
	public void testGetElementNull() throws InvalidDDMSException {
		try {
			getReader().getElement(null);
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			fail("Allowed invalid data.");
		}
		catch (IllegalArgumentException e) {
			// Good
		}
	}
	
	public void testGetElementDoesNotExist() throws InvalidDDMSException {
		try {
			getReader().getElement(new File("doesnotexist"));
			fail("Allowed invalid data.");
		}
		catch (IOException e) {
			// Good
		}
	}
	
	public void testGetElementNotXML() throws IOException {
		try {
			getReader().getElement(new File("conf/ddmsence.properties"));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testGetElementSuccess() throws InvalidDDMSException, IOException {
		getReader().getElement(new File(TEST_DATA_DIR, "rights.xml"));
	}
	
	public void testGetResourceFailure() throws IOException {
		try {
			getReader().getDDMSResource(new File(TEST_DATA_DIR, "rights.xml"));
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testGetResourceSuccess() throws InvalidDDMSException, IOException {
		getReader().getDDMSResource(new File(TEST_DATA_DIR, "resource.xml"));
	}
	
	/**
	 * Accessor for the reader
	 */
	private DDMSReader getReader() {
		return _reader;
	}
}