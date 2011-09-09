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
package buri.ddmsence.ddms;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import nu.xom.Element;
import buri.ddmsence.util.DDMSReader;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;

/**
 * Base class for test cases involving components.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public abstract class AbstractComponentTestCase extends TestCase {

	private String _type;

	private static Map<String, Element> _elementMap = new HashMap<String, Element>();

	protected static final String TEST_ID = "IDValue";

	protected static final boolean WILL_SUCCEED = false;
	protected static final boolean WILL_FAIL = true;
	protected static final String INVALID_URI = ":::::";
	protected static final String DIFFERENT_VALUE = "Different";

	/**
	 * Resets the in-use version of DDMS.
	 */
	protected void setUp() throws Exception {
		DDMSVersion.clearCurrentVersion();
	}

	/**
	 * Resets the in-use version of DDMS.
	 */
	protected void tearDown() throws Exception {
		DDMSVersion.clearCurrentVersion();
	}

	/**
	 * Constructor
	 * 
	 * @param validDocumentFile the filename to load. One of these will be loaded for each supporting version.
	 */
	public AbstractComponentTestCase(String validDocumentFile) {
		_type = validDocumentFile;
		if (validDocumentFile == null)
			return;
		try {
			DDMSReader reader = null;
			for (String versionString : DDMSVersion.getSupportedVersions()) {
				if (getValidElement(versionString) == null) {
					if (reader == null)
						reader = new DDMSReader();
					File file = new File(PropertyReader.getProperty("test.unit.data") + versionString,
						validDocumentFile);
					if (file.exists()) {
						Element element = reader.getElement(file);
						synchronized (_elementMap) {
							_elementMap.put(getType() + ":" + versionString, element);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot run tests without valid DDMSReader and valid unit test object.", e);
		}

	}

	/**
	 * Should be called after a successful constructor execution. If the constructor was expected to fail, but it
	 * succeeded, the test will fail.
	 * 
	 * @param expectFailure true if the constructor was expected to fail.
	 */
	protected static void checkConstructorSuccess(boolean expectFailure) {
		if (expectFailure)
			fail("Constructor allowed invalid data.");
	}

	/**
	 * Should be called after a failed constructor execution. If the constructor was expected to succeed, but it failed,
	 * the test will fail.
	 * 
	 * @param expectFailure true if the constructor was expected to fail.
	 * @param exception the exception that occurred
	 */
	protected static void checkConstructorFailure(boolean expectFailure, InvalidDDMSException exception) {
		if (!expectFailure)
			fail("Constructor failed on valid data: " + exception.getMessage());
	}

	/**
	 * Helper method to confirm that a warning message is correct.
	 * 
	 * @param text the text of the message
	 * @param locator the locator text of the message
	 * @param message the ValidationMessage to test
	 */
	protected void assertWarningEquality(String text, String locator, ValidationMessage message) {
		assertEquals(ValidationMessage.newWarning(text, locator), message);
	}
	
	/**
	 * Helper method to confirm that a warning message is correct.
	 * 
	 * @param text the text of the message
	 * @param locator the locator text of the message
	 * @param message the ValidationMessage to test
	 */
	protected void assertErrorEquality(String text, String locator, ValidationMessage message) {
		assertEquals(ValidationMessage.newError(text, locator), message);
	}
	
	/**
	 * Strips tabs and new lines from XML output where appropriate. The unit test samples in the XML files have tabs and
	 * new lines, but the default implementation of XOM toXML() returns XML on a single line.
	 * 
	 * @param string the original string
	 * @param preserveFormatting true to retain tabs and new lines, false to strip them.
	 * @return the modified string
	 */
	protected static String formatXml(String string, boolean preserveFormatting) {
		if (!preserveFormatting) {
			string = string.replaceAll("\t", "");
			string = string.replaceAll("\n", "");
		}
		return (string);
	}

	/**
	 * Accessor for a valid DDMS XOM Element constructed from the root element of an XML file, which can be used in
	 * testing as a "correct base case".
	 */
	public Element getValidElement(String version) {
		return (_elementMap.get(getType() + ":" + version));
	}

	/**
	 * Accessor for the local identifier for the type of component being tested
	 */
	private String getType() {
		return (_type);
	}

	/**
	 * Convenience method to check that DDMS Version is 3.1 or greater
	 */
	protected boolean isDDMS31OrGreater() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("3.1"));
	}

	/**
	 * Convenience method to check that DDMS Version is 4.0 or greater
	 */
	protected boolean isDDMS40OrGreater() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0"));
	}
}
