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

import java.net.URISyntaxException;

/**
 * Exception class for attempts to generate invalid DDMS components.
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public class InvalidDDMSException extends Exception {
	
	private static final long serialVersionUID = -183915550465140589L;
	
	/**
	 * @see Exception#Exception(String)
	 */
	public InvalidDDMSException(String message) {
		super(message);
	}
	
	/**
	 * @see Exception#Exception(Throwable)
	 */
	public InvalidDDMSException(Throwable nested) {
		super(nested);
	}
	
	/**
	 * Handles nested URISyntaxExceptions
	 * 
	 * @param e	the exception
	 */
	public InvalidDDMSException(URISyntaxException e) {
		super("Invalid URI: " + e.getMessage(), e);
	}	
}
