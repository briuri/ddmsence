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

import junit.framework.TestCase;
import nu.xom.Element;
import buri.ddmsence.ddms.extensible.ExtensibleAttributes;
import buri.ddmsence.ddms.extensible.ExtensibleAttributesTest;
import buri.ddmsence.ddms.resource.Contributor;
import buri.ddmsence.ddms.resource.OrganizationX;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to underlying methods in the base class for DDMS producer entities</p>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public class ProducerEntityTest extends TestCase {

	public void testValidateParentTypeSuccess() throws InvalidDDMSException {
		// TODO: Replace with constant.
		OrganizationX.validateParentType(Contributor.NAME);
	}

	public void testValidateProducerTypeFailure() {
		try {
			OrganizationX.validateParentType("editor");
			fail("Allowed invalid data.");
		} catch (InvalidDDMSException e) {
			// Good
		}
	}

	public void testSharedWarnings() throws InvalidDDMSException {
		// Empty phone
		Element entityElement = Util.buildDDMSElement(OrganizationX.NAME, null);
		entityElement.appendChild(Util.buildDDMSElement("name", "name"));
		entityElement.appendChild(Util.buildDDMSElement("phone", ""));
		OrganizationX component = new OrganizationX(Contributor.NAME, entityElement);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A ddms:phone element was found with no value.", 
			component.getValidationWarnings().get(0).getText());

		// Empty email
		entityElement = Util.buildDDMSElement(OrganizationX.NAME, null);
		entityElement.appendChild(Util.buildDDMSElement("name", "name"));
		entityElement.appendChild(Util.buildDDMSElement("email", ""));
		component = new OrganizationX(Contributor.NAME, entityElement);
		assertEquals(1, component.getValidationWarnings().size());
		assertEquals(ValidationMessage.WARNING_TYPE, component.getValidationWarnings().get(0).getType());
		assertEquals("A ddms:email element was found with no value.", 
			component.getValidationWarnings().get(0).getText());
	}
	
	public void testExtensibleSuccess() throws InvalidDDMSException {
		for (String version : DDMSVersion.getSupportedVersions()) {
			DDMSVersion.setCurrentVersion(version);
			
			ExtensibleAttributes attr = ExtensibleAttributesTest.getFixture();
			List<String> names = new ArrayList<String>();
			names.add("DISA");
			new OrganizationX(Contributor.NAME, names, null, null, attr);
		}
	}
	
	public void testExtensibleFailure() throws InvalidDDMSException {
		// No failure cases to test right now.
		// ICISM attributes are at creator/contributor level, so they never clash with extensibles on the entity level.
	}
}
