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

import java.util.List;

import buri.ddmsence.ddms.extensible.ExtensibleAttributes;

/**
 * Identifying interface for a producer entity element (person, organization, service, unknown)
 *  
 * @author Brian Uri!
 * @since 2.0.0
 */
public interface IProducerEntity extends IDDMSComponent {
	
	/**
	 * Accessor for the names of the entity (1 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<String> getNames();
	
	/**
	 * Accessor for the phone numbers of the entity (0 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<String> getPhones();
	
	/**
	 * Accessor for the emails of the entity (0 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<String> getEmails();
	
	/**
	 * Accessor for any extensible attributes on the producer
	 * 
	 * @return the attributes
	 */
	public ExtensibleAttributes getExtensibleAttributes();
	
	/**
	 * Extended HTML output method which adds a prefix to each line.
	 * 
	 * @param prefix the text to add
	 * @return HTML output
	 */
	public String toHTML(String prefix);
	
	/**
	 * Extended Text output method which adds a prefix to each line.
	 * 
	 * @param prefix the text to add
	 * @return Text output
	 */
	public String toText(String prefix);
}
