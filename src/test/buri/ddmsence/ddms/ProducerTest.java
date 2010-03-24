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
package buri.ddmsence.ddms;

import junit.framework.TestCase;
import nu.xom.Element;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.util.Util;

/**
 * <p>Tests related to underlying methods in the base class for DDMS Producers</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class ProducerTest extends TestCase {
	
	public void testValidateProducerTypeSuccess() throws InvalidDDMSException {
		Organization.validateProducerType(Organization.CONTRIBUTOR_NAME);
	}
	
	public void testValidateProducerTypeFailure() {
		try {
			Organization.validateProducerType("editor");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	public void testValidateProducerEntityTypeSuccess() throws InvalidDDMSException {
		Organization.validateProducerEntityType(Organization.NAME);
	}
	
	public void testValidateProducerEntityTypeFailure() {
		try {
			Organization.validateProducerEntityType("Corporation");
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
	
	private Element getBigEntity() {
		Element element = Util.buildDDMSElement(Organization.NAME, null);
		element.appendChild(Util.buildDDMSElement(Organization.NAME_NAME, "org1"));
		
		Element element2 = Util.buildDDMSElement(Organization.NAME, null);
		element2.appendChild(Util.buildDDMSElement(Organization.NAME_NAME, "org2"));
		
		Element producerElement = Util.buildDDMSElement("creator", null);
		producerElement.appendChild(element);
		producerElement.appendChild(element2);
		
		return (producerElement);
	}

	public void testInvalidTooManyEntities() {
		try {
			new Organization(getBigEntity());
			fail("Allowed invalid data.");
		}
		catch (InvalidDDMSException e) {
			// Good
		}
	}
}
