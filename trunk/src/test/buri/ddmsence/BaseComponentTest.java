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
package buri.ddmsence;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import nu.xom.Element;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.resource.Language;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to underlying methods in the base class for DDMS components</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class BaseComponentTest extends TestCase {

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

	public void testSelfEquality() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);
		assertEquals(rights, rights);
	}

	public void testToString() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);
		assertEquals(rights.toString(), rights.toXML());
	}

	public void testVersion() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);
		assertEquals(DDMSVersion.getCurrentVersion().getNamespace(), rights.getNamespace());
	}

	public void testCustomPrefix() throws InvalidDDMSException {
		String namespace = DDMSVersion.getCurrentVersion().getNamespace();
		Element element = Util.buildElement("customPrefix", Language.getName(DDMSVersion.getCurrentVersion()),
			namespace, null);
		Util.addAttribute(element, "customPrefix", "qualifier", namespace, "testQualifier");
		Util.addAttribute(element, "customPrefix", "value", namespace, "en");
		new Language(element);
	}

	public void testNullChecks() throws InvalidDDMSException {
		AbstractBaseComponent component = new AbstractBaseComponent() {
			public String getOutput(boolean isHTML, String prefix) {
				return null;
			}
		};
		assertEquals("", component.getName());
		assertEquals("", component.getNamespace());
		assertEquals("", component.getPrefix());
		assertEquals("", component.toXML());
	}

	public void testAttributeWarnings() throws InvalidDDMSException {
		AbstractBaseComponent component = new AbstractBaseComponent() {
			public String getOutput(boolean isHTML, String prefix) {
				return null;
			}

			protected String getLocatorSuffix() {
				return ("locatorSuffix");
			}
		};
		List<ValidationMessage> warnings = new ArrayList<ValidationMessage>();
		warnings.add(ValidationMessage.newWarning("test", "locator"));
		component.addWarnings(warnings, true);
		assertEquals("//locator", component.getValidationWarnings().get(0).getLocator());

	}
}
