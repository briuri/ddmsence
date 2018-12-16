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
package buri.ddmsence.ddms;

/**
 * Enumeration of output formats. XML is not an enumeration value, as we rely on XOM for XML output.
 * 
 * <ul>
 * <li><b>HTML</b>: HTML meta tags, in accordance with the suggested HTML output from the DDMS Specification.</li>
 * <li><b>JSON</b>: JSON data, which generally follows the naming conventions of the HTML/Text output, rather than
 *    being a straight XML-to-JSON conversion.</li>
 * <li><b>TEXT</b>: Text name-value pairings, in accordance with the suggested Text output from the DDMS Specification.</li>
 * </ul>
 * 
 * @author Brian Uri!
 * @since 2.3.0
 */
public enum OutputFormat {
	HTML, JSON, TEXT
}