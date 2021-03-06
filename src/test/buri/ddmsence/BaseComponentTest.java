/* Copyright 2010 - 2019 by Brian Uri!
   
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
package buri.ddmsence;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;

import org.junit.Test;

import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.OutputFormat;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.format.Extent;
import buri.ddmsence.ddms.resource.Creator;
import buri.ddmsence.ddms.resource.Language;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Rights;
import buri.ddmsence.ddms.security.Security;
import buri.ddmsence.ddms.security.SecurityTest;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

import com.google.gson.JsonObject;

/**
 * <p> Tests related to underlying methods in the base class for DDMS components </p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class BaseComponentTest extends AbstractBaseTestCase {

	public BaseComponentTest() {
		super(null);
	}

	@Test
	public void testBuildIndexInvalidInputBounds() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);

		// Bad total
		try {
			rights.buildIndex(0, 0);
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "The total must be at least 1");
		}

		// Low index
		try {
			rights.buildIndex(-1, 1);
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "The index is not properly bounded");
		}

		// High index
		try {
			rights.buildIndex(2, 2);
		}
		catch (IllegalArgumentException e) {
			expectMessage(e, "The index is not properly bounded");
		}
	}

	@Test
	public void testBuildIndexValidInputBounds() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);

		// Good total
		rights.buildIndex(0, 1);

		// Good index
		rights.buildIndex(1, 2);
	}

	@Test
	public void testBuildIndexLevel0() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);

		PropertyReader.setProperty("output.indexLevel", "0");
		String index = rights.buildIndex(0, 1);
		assertEquals("", index);
		index = rights.buildIndex(2, 4);
		assertEquals("", index);

		PropertyReader.setProperty("output.indexLevel", "unknown");
		index = rights.buildIndex(0, 1);
		assertEquals("", index);
		index = rights.buildIndex(2, 4);
		assertEquals("", index);
	}

	@Test
	public void testBuildIndexLevel1() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);

		PropertyReader.setProperty("output.indexLevel", "1");
		String index = rights.buildIndex(0, 1);
		assertEquals("", index);
		index = rights.buildIndex(2, 4);
		assertEquals("[3]", index);
	}

	@Test
	public void testBuildIndexLevel2() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);

		PropertyReader.setProperty("output.indexLevel", "2");
		String index = rights.buildIndex(0, 1);
		assertEquals("[1]", index);
		index = rights.buildIndex(2, 4);
		assertEquals("[3]", index);
	}

	@Test
	public void testBuildOutput() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);
		List<IDDMSComponent> objectList = new ArrayList<IDDMSComponent>();
		objectList.add(rights);
		assertEquals("rights.privacyAct: true\nrights.intellectualProperty: true\nrights.copyright: true\n",
			rights.buildHTMLTextOutput(OutputFormat.TEXT, "", objectList));

		List<String> stringList = new ArrayList<String>();
		stringList.add("Text");
		assertEquals("name: Text\n", rights.buildHTMLTextOutput(OutputFormat.TEXT, "name", stringList));

		List<Double> otherList = new ArrayList<Double>();
		otherList.add(Double.valueOf(2.0));
		assertEquals("name: 2.0\n", rights.buildHTMLTextOutput(OutputFormat.TEXT, "name", otherList));
	}

	@Test
	public void testOutputJSONPrettyPrint() throws InvalidDDMSException {
		Extent extent = new Extent("a", "z");
		PropertyReader.setProperty("output.json.prettyPrint", "false");
		assertEquals("{\"qualifier\":\"a\",\"value\":\"z\"}", extent.toJSON());
		PropertyReader.setProperty("output.json.prettyPrint", "true");
		assertEquals("{\n  \"qualifier\": \"a\",\n  \"value\": \"z\"\n}", extent.toJSON());
	}
	
	@Test
	public void testOutputJSONInline() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("4.1");
		Security security = SecurityTest.getFixture();
		PropertyReader.setProperty("output.json.inlineAttributes", "false");
		assertEquals("{\"excludeFromRollup\":true,\"securityAttributes\":{\"classification\":\"U\",\"ownerProducer\":[\"USA\"]}}", security.toJSON());
		PropertyReader.setProperty("output.json.inlineAttributes", "true");
		assertEquals("{\"excludeFromRollup\":true,\"classification\":\"U\",\"ownerProducer\":[\"USA\"]}", security.toJSON());
	}
	
	@Test
	public void testSelfEquality() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);
		assertEquals(rights, rights);
	}

	@Test
	public void testToString() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);
		assertEquals(rights.toString(), rights.toXML());
	}

	@Test
	public void testVersion() throws InvalidDDMSException {
		Rights rights = new Rights(true, true, true);
		assertEquals(DDMSVersion.getCurrentVersion().getNamespace(), rights.getNamespace());
	}

	@Test
	public void testCustomPrefix() throws InvalidDDMSException {
		String namespace = DDMSVersion.getCurrentVersion().getNamespace();
		Element element = Util.buildElement("customPrefix", Language.getName(DDMSVersion.getCurrentVersion()),
			namespace, null);
		Util.addAttribute(element, "customPrefix", "qualifier", namespace, "testQualifier");
		Util.addAttribute(element, "customPrefix", "value", namespace, "en");
		new Language(element);
	}

	@Test
	public void testNullChecks() throws InvalidDDMSException {
		AbstractBaseComponent component = new AbstractBaseComponent() {
			public JsonObject getJSONObject() {
				return null;
			}
			public String getHTMLTextOutput(OutputFormat format, String prefix, String suffix) {
				return null;
			}
		};
		assertEquals("", component.getName());
		assertEquals("", component.getNamespace());
		assertEquals("", component.getPrefix());
		assertEquals("", component.toXML());
	}

	@Test
	public void testAttributeWarnings() throws InvalidDDMSException {
		AbstractBaseComponent component = new AbstractBaseComponent() {
			public JsonObject getJSONObject() {
				return null;
			}
			
			public String getHTMLTextOutput(OutputFormat format, String prefix, String suffix) {
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

	@Test
	public void testSameVersion() throws InvalidDDMSException {
		DDMSVersion.setCurrentVersion("3.0");
		Organization org = new Organization(Util.getXsListAsList("DISA"), null, null, null, null, null);
		DDMSVersion.setCurrentVersion("2.0");
		try {
			new Creator(org, null, null);
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			expectMessage(e, "A child component, ddms:Organization");
		}
	}
}