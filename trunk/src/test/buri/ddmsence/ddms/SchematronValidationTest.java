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
import buri.ddmsence.util.PropertyReader;

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
	
	public void testSchematronValidationXslt1() throws InvalidDDMSException, IOException, XSLException {
		String[] supportedXslt1Processors = new String[] {"org.apache.xalan.processor.TransformerFactoryImpl",
			"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", "net.sf.saxon.TransformerFactoryImpl"};
		for (String processor : supportedXslt1Processors) {
			PropertyReader.setProperty("xml.transform.TransformerFactory", processor);
			for (String version : DDMSVersion.getSupportedVersions()) {
				String ddmsNamespace = DDMSVersion.getVersionFor(version).getNamespace();
				List<ValidationMessage> messages = versionToResourceMap.get(version).validateWithSchematron(new File("data/test/"
					+ version + "/testSchematronXslt1.sch"));
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
	
	public void testSchematronValidationXslt2() throws InvalidDDMSException, IOException, XSLException {
		String[] supportedXslt1Processors = new String[] {"net.sf.saxon.TransformerFactoryImpl"};
		for (String processor : supportedXslt1Processors) {
			PropertyReader.setProperty("xml.transform.TransformerFactory", processor);
			for (String version : DDMSVersion.getSupportedVersions()) {
				String ddmsNamespace = DDMSVersion.getVersionFor(version).getNamespace();
				String gmlNamespace = DDMSVersion.getVersionFor(version).getGmlNamespace();
				List<ValidationMessage> messages = versionToResourceMap.get(version).validateWithSchematron(new File("data/test/"
					+ version + "/testSchematronXslt2.sch"));
				assertEquals(1, messages.size());
				assertEquals("//*:Resource[namespace-uri()='" + ddmsNamespace + "'][1]/*:geospatialCoverage[namespace-uri()='"
					+ ddmsNamespace + "'][1]/*:GeospatialExtent[namespace-uri()='" + ddmsNamespace + "'][1]/*:boundingGeometry[namespace-uri()='"
					+ ddmsNamespace + "'][1]/*:Point[namespace-uri()='" + gmlNamespace + "'][1]/*:pos[namespace-uri()='"
					+ gmlNamespace + "'][1]", messages.get(0).getLocator());
				assertEquals("The second coordinate in a gml:pos element must be 40.2 degrees.", messages.get(0).getText());
				assertEquals(ValidationMessage.ERROR_TYPE, messages.get(0).getType());
			}
		}
	}
}
