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

import java.util.ArrayList;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractBaseTestCase;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleAttributesTest;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * <p> Tests related to underlying methods in the base class for DDMS producer entities </p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class RoleEntityTest extends AbstractBaseTestCase {

	public RoleEntityTest() {
		super(null);
	}

	private static final String TEST_POC_TYPE = "DoD-Dist-B";

	/**
	 * Helper method to generate a pocType for producers
	 */
	public static List<String> getPocTypes() {
		return (DDMSVersion.getCurrentVersion().isAtLeast("4.0.1") ? Util.getXsListAsList(TEST_POC_TYPE) : Util.getXsListAsList(""));
	}

	public void testSharedWarnings() throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getCurrentVersion();

		// Empty phone
		Element entityElement = Util.buildDDMSElement(Organization.getName(version), null);
		entityElement.appendChild(Util.buildDDMSElement("name", "name"));
		entityElement.appendChild(Util.buildDDMSElement("phone", ""));
		Organization component = new Organization(entityElement);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A ddms:phone element was found with no value.", component.getValidationWarnings().get(0)
			.getText());

		// Empty email
		entityElement = Util.buildDDMSElement(Organization.getName(version), null);
		entityElement.appendChild(Util.buildDDMSElement("name", "name"));
		entityElement.appendChild(Util.buildDDMSElement("email", ""));
		component = new Organization(entityElement);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A ddms:email element was found with no value.", component.getValidationWarnings().get(0)
			.getText());
	}

	public void testIndexLevelsStringLists() throws InvalidDDMSException {
		List<String> names = Util.getXsListAsList("Brian BU");
		List<String> phones = Util.getXsListAsList("703-885-1000");
		Person person = new Person(names, "Uri", phones, null, null, null);

		PropertyReader.setProperty("output.indexLevel", "0");
		assertEquals("entityType: person\nname: Brian\nname: BU\nphone: 703-885-1000\nsurname: Uri\n", person.toText());

		PropertyReader.setProperty("output.indexLevel", "1");
		assertEquals("entityType: person\nname[1]: Brian\nname[2]: BU\nphone: 703-885-1000\nsurname: Uri\n", person
			.toText());

		PropertyReader.setProperty("output.indexLevel", "2");
		assertEquals("entityType: person\nname[1]: Brian\nname[2]: BU\nphone[1]: 703-885-1000\nsurname: Uri\n", person
			.toText());
	}

	public void testExtensibleSuccess() throws InvalidDDMSException {
		for (String sVersion : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(sVersion);

			ExtensibleAttributes attr = ExtensibleAttributesTest.getFixture();
			List<String> names = new ArrayList<String>();
			names.add("DISA");
			new Organization(names, null, null, null, null, attr);
		}
	}

	public void testExtensibleFailure() throws InvalidDDMSException {
		// No failure cases to test right now.
		// ISM attributes are at creator/contributor level, so they never clash with extensibles on the entity level.
	}
}