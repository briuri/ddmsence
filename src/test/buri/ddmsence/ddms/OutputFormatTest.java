/* Copyright 2010 - 2016 by Brian Uri!
   
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

import junit.framework.TestCase;

import org.junit.Test;

/**
 * <p> Tests related to OutputFormat </p>
 * 
 * @author Brian Uri!
 * @since 2.3.0
 */
public class OutputFormatTest extends TestCase {

	@Test
	public void testValueOf() {
		// Hack to get coverage of bytecode, by calling valueOf one time.
		assertEquals(OutputFormat.HTML, OutputFormat.valueOf("HTML"));
	}
}
