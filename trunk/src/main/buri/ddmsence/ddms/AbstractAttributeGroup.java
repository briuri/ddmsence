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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.ddms.summary.SRSAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * Top-level base class for attribute groups, specifically {@link SRSAttributes}, {@link SecurityAttributes}, and
 * {@link ExtensibleAttributes}.
 * 
 * <p>Extensions of this class are generally expected to be immutable. It is assumed that after the constructor on 
 * a component has been called, the component will be well-formed and valid.</p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public abstract class AbstractAttributeGroup {

	private String _ddmsVersion;
	private List<ValidationMessage> _warnings;
	
	/**
	 * Superconstructor sets the DDMS Version
	 * 
	 * @param version the currently in-use version of DDMS
	 */
	public AbstractAttributeGroup(DDMSVersion version) {
		_ddmsVersion = (version == null ? null : version.getVersion());
	}
	
	/**
	 * Base validation case for attribute groups.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The DDMS Version is not null.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSValue("vresion", getDDMSVersion());
	}
		
	/**
	 * Returns a list of any warning messages that occurred during validation. Warnings do not prevent a valid component
	 * from being formed.
	 * 
	 * @return a list of warnings
	 */
	public List<ValidationMessage> getValidationWarnings() {
		return (Collections.unmodifiableList(getWarnings()));
	}

	/**
	 * Accessor for the DDMS Version
	 */
	public String getDDMSVersion() {
		return (_ddmsVersion);
	}
	
	/**
	 * Accessor for the list of validation warnings.
	 * 
	 * <p>
	 * This is the private copy that should be manipulated during validation. Lazy initialization.
	 * </p>
	 * 
	 * @return an editable list of warnings
	 */
	protected List<ValidationMessage> getWarnings() {
		if (_warnings == null)
			_warnings = new ArrayList<ValidationMessage>();
		return (_warnings);
	}	
}
