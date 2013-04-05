/* Copyright 2010 - 2013 by Brian Uri!
   
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


/**
 * Identifying interface for a TSPI shape element, which may be used to fill a boundingGeometry element.
 * 
 * @author Brian Uri!
 * @since 2.2.0
 */
public interface ITspiShape extends IDDMSComponent {
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public abstract String getOutput(boolean isHTML, String prefix, String suffix);
}
