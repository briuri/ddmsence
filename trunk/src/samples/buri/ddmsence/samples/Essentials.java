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
package buri.ddmsence.samples;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.xml.sax.SAXException;

import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.samples.util.AbstractSample;
import buri.ddmsence.samples.util.JavaConvertor;
import buri.ddmsence.util.Util;

/**
 * DDMSsentials is a simple reader application which loads a DDMS Resource from a file, and displays it in four formats.
 * 
 * <ol>
 * <li>The original XML</li>
 * <li>HTML, as defined by the DDMS Specification</li>
 * <li>Text, as defined by the DDMS Specification</li>
 * <li>Java code</li>
 * </ol>
 * 
 * <p>The Java code is intended to be a cut-and-paste example that you can use as a template for building your own components
 * from scratch.</p>
 * 
 * <p>For additional details about this application, please see the tutorial on the Documentation page of the DDMSence website.</p>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class Essentials extends AbstractSample {

	private Resource _resource;
	private static final String INSTRUCTIONS = "<html><center>To begin, select " + OPEN + " from the " + FILE
	+ " menu,<br />and choose an XML file containing a DDMS Resource as the root element.</center></html>";

	/**
	 * Entry point
	 * 
	 * @param args no parameters are required, although a filename containing a DDMS Resource can be passed in.
	 */
	public static void main(String[] args) {
		try {
			Essentials app = new Essentials();
			if (args.length > 0 && !Util.isEmpty(args[0]))
				app.loadFile(new File(args[0]));
			app.setVisible(true);
		}
		catch (SAXException e) {
			System.err.println("Could not initialize the application.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Sets up the UI and DDMSReader (which is base functionality of all sample apps)
	 */
	public Essentials() throws SAXException {
		super("Essentials", new Dimension(900, 600), true);
	}

	/**
	 * Returns instructions to display on the screen initially.
	 */
	protected String getDefaultInstructions() {
		return (INSTRUCTIONS);
	}
	
	/**
	 * Attempts to convert a file into a Resource object and then render it in the frame.
	 * 
	 * <p>
	 * This is the primary location where code interacts with DDMSence. An MVC approach would be more logical here, but
	 * I wanted to minimize the code not directly related to learning about this library.
	 * </p>
	 * 
	 * @param file the file containing a DDMS Resource as a root element
	 */
	private void loadFile(File file) {
		JPanel resultPanel;
		try {
			// The DDMS reader builds a Resource object from the XML in the file.
			_resource = getReader().getDDMSResource(file);
			
			// The four output formats
			String xmlFormat = getResource().toXML();
			String htmlFormat = getResource().toHTML();
			String textFormat = getResource().toText();
			String javaFormat = JavaConvertor.toJavaCode(getResource());
					
			// Render the formats in the Swing GUI
			JPanel htmlTextPanel = new JPanel(new GridLayout(1, 0));
			htmlTextPanel.add(buildLabelledPanel(file.getName() + " in HTML", htmlFormat));
			htmlTextPanel.add(buildLabelledPanel(file.getName() + " in Text", textFormat));
			resultPanel = new JPanel(new GridLayout(0, 1));
			resultPanel.add(buildLabelledPanel(file.getName() + " in XML", xmlFormat));
			resultPanel.add(htmlTextPanel);
			resultPanel.add(buildLabelledPanel(file.getName() + " in Java", javaFormat));
		}
		catch (InvalidDDMSException e) {
			resultPanel = buildErrorPanel("Could not create the DDMS Resource: ", e);
		}
		catch (IOException e) {
			resultPanel = buildErrorPanel("Could not create the DDMS Resource: ", e);
		}
		refreshUI(resultPanel);
	}

	/**
	 * Listens for Open events to show the filechooser. This code does not interact with DDMSence.
	 * 
	 * @param e the actionEvent, fired from the menubar
	 */
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (OPEN.equals(e.getActionCommand())) {
		    JFileChooser chooser = new JFileChooser();
		    FileFilter filter = new FileFilter() {
				public String getDescription() {
					return ("XML files");
				}
				public boolean accept(File pathname) {
					return (pathname.getAbsolutePath().endsWith(".xml"));
				}
			};
		    chooser.setFileFilter(filter);
		    chooser.setCurrentDirectory(new File(SAMPLE_DIR));
		    int returnVal = chooser.showOpenDialog(getFrame());
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    	loadFile(chooser.getSelectedFile());
		    }
		}
	}
	
	/**
	 * Accessor for the currently loaded ddms:Resource
	 */
	private Resource getResource() {
		return (_resource);
	}
}
