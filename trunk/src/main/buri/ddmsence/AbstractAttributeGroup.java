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
package buri.ddmsence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;

/**
 * Top-level base class for attribute groups, such as {@link SecurityAttributes}.
 * 
 * <p>Extensions of this class are generally expected to be immutable. It is assumed that after the constructor on 
 * a component has been called, the component will be well-formed and valid.</p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public abstract class AbstractAttributeGroup {

	private String _xmlNamespace = null;
	private List<ValidationMessage> _warnings = null;
	
	/**
	 * Constructor which stores the XML namespace of the enclosing element
	 */
	public AbstractAttributeGroup(String xmlNamespace) {
		_xmlNamespace = xmlNamespace;
	}
	
	/**
	 * Accessor for the DDMS namespace on the enclosing element.
	 */
	public DDMSVersion getDDMSVersion() {
		return (DDMSVersion.getVersionForNamespace(_xmlNamespace));
	}
	
	/**
	 * Base validation case for attribute groups.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>No rules are validated at this level. Extending classes may have additional rules.</li>
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {}
		
	/**
	 * Compares the DDMS version of these attributes to another DDMS version
	 * 
	 * @param version the version to test
	 * @throws InvalidDDMSException if the versions do not match
	 */
	protected void validateSameVersion(DDMSVersion version) throws InvalidDDMSException {
		if (!getDDMSVersion().equals(version)) {
			throw new InvalidDDMSException(
				"These attributes cannot decorate a component with a different DDMS version.");
		}
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
	
	/**
	 * Outputs to HTML or Text with a prefix at the beginning of each meta tag or line.
	 * 
	 * @param isHTML true for HTML, false for Text
	 * @param prefix the prefix to add
	 * @param suffix the suffix to add
	 * @return the HTML or Text output
	 */
	public abstract String getOutput(boolean isHTML, String prefix, String suffix);
}
