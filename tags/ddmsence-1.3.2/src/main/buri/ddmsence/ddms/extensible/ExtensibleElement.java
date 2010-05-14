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
package buri.ddmsence.ddms.extensible;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;

/**
 * An immutable implementation of an element which might fulfill the xs:any space in the Extensible Layer.
 * 
 * <p>In DDMS 3.0, zero to many of these elements may appear in a ddms:Resource and can live in any other
 * namespace besides the DDMS namespace. In DDMS 2.0, only one of these is allowed.</p>
 * 
 * <p>No validation or processing of any kind is performed by DDMSence on extensible attributes, other than the base
 * validation used when loading attributes from an XML file. This class merely exposes a <code>getXOMElementCopy()</code> 
 * method which returns a XOM Element that can be manipulated in business-specific ways.</p>
 * 
 * <p>XOM elements can be created as follows:</p>
 * 
 * <ul><code>
 * Element element = new Element("ddmsence:extension", "http://ddmsence.urizone.net/");<br />
 * element.appendChild("This will be the child text.");
 * </code></ul>
 * 
 * <p>Because it is impossible to cover all of the HTML/Text output cases for ExtensibleElements, DDMSence
 * will simply print out the existence of extensible elements:</p>
 * <ul><code>
 * Extensible Layer: true<br />
 * &lt;meta name="extensible.layer" content="true" /&gt;<br />
 * </code></ul></p>
 * 
 * <p>Details about the XOM Element class can be found at:
 * <i>http://www.xom.nu/apidocs/index.html?nu/xom/Element.html</i></p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public final class ExtensibleElement extends AbstractBaseComponent {
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ExtensibleElement(Element element) throws InvalidDDMSException {
		super(element);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The namespace cannot be the DDMS namespace.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		DDMSVersion version = DDMSVersion.getVersionForNamespace(getXOMElement().getNamespaceURI());
		if (version != null)
			throw new InvalidDDMSException("Extensible elements cannot be defined in the DDMS namespace.");
	}

	/**
	 * @see IDDMSComponent#toHTML()
	 */
	public String toHTML() {
		return ("");
	}

	/**
	 * @see IDDMSComponent#toText()
	 */
	public String toText() {
		return ("");
	}

	/**
	 * ExtensibleElements are not tied to any particular version.
	 * 
	 * @see IDDMSComponent#getDDMSVersion()
	 */
	public String getDDMSVersion() {
		return ("?");
	}
	
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof ExtensibleElement))
			return (false);
		ExtensibleElement test = (ExtensibleElement) obj;
		return (getXOMElement().toXML().equals(test.getXOMElement().toXML()));
	}
	
	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getXOMElement().toXML().hashCode();
		return (result);
	}
} 