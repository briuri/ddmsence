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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.xslt.XSLException;
import buri.ddmsence.util.DDMSVersion;

/**
 * <p>Tests related to Schematron validation of Resources</p>
 * 
 * @author Brian Uri!
 * @since 1.3.1
 */
public class SchematronValidationTest extends AbstractComponentTestCase {

	private Map<String, Resource> versionToResourceMap;
	
	/**
	 * Constructor
	 */
	public SchematronValidationTest() throws InvalidDDMSException {
		super("resource.xml");
		versionToResourceMap = new HashMap<String, Resource>();
		for (String version : DDMSVersion.getSupportedVersions()) {
			versionToResourceMap.put(version, new Resource(getValidElement(version)));
		}
	}
	
	public void testSchematronValidation() throws InvalidDDMSException, IOException, XSLException {
//		PropertyReader.setProperty("xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
//		PropertyReader.setProperty("xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
		for (String version : DDMSVersion.getSupportedVersions()) {
			String ddmsNamespace = DDMSVersion.getVersionFor(version).getNamespace();
			List<ValidationMessage> messages = versionToResourceMap.get(version).validateWithSchematron(new File("data/test/"
				+ version + "/testSchematron.sch"));
			assertEquals(2, messages.size());
			assertEquals("//*[local-name()='Resource' and namespace-uri()='" + ddmsNamespace + "']", 
				messages.get(0).getLocator());
			assertEquals("A DDMS Resource must have an unknownElement child. This will always fail.", messages.get(0).getText());
			assertEquals(ValidationMessage.ERROR_TYPE, messages.get(0).getType());
			assertEquals("//*[local-name()='Resource' and namespace-uri()='" + ddmsNamespace + "']"
				+ "/*[local-name()='publisher' and namespace-uri()='" + ddmsNamespace + "']"
				+ "/*[local-name()='Person' and namespace-uri()='" + ddmsNamespace + "']"
				+ "/*[local-name()='surname' and namespace-uri()='" + ddmsNamespace + "']", 
				messages.get(1).getLocator());
			assertEquals("Members of the Uri family cannot be publishers.", messages.get(1).getText());
			assertEquals(ValidationMessage.WARNING_TYPE, messages.get(1).getType());
		}
	}
}
