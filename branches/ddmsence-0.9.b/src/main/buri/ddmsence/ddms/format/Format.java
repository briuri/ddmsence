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
package buri.ddmsence.ddms.format;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:format.
 * 
 * <p>
 * A format element contains a locally defined Media construct. This Media construct is a container for the mimeType,
 * extent, and medium of a resource. It exists only inside of a ddms:format parent, so it is not implemented as a Java
 * object.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>A non-empty mimeType value is required.</li>
 * </ul>
 * 
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A medium element can be used with no child text.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:mimeType</u>: the MIME type (exactly 1 required)<br />
 * <u>ddms:extent</u>: the format extent (0-1 optional), implemented as a {@link MediaExtent}<br />
 * <u>ddms:medium</u>: the physical medium (0-1 optional)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Format <br />
 * <u>Description</u>: A format is the physical or digital manifestation of the resource.<br />
 * <u>Obligation</u>: Optional<br />
 * <u>Schema Modification Date</u>: 2003-05-16<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Format extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private String _cachedMimeType;
	private MediaExtent _cachedExtent;
	private String _cachedMedium;
	
	/** The element name of this component */
	public static final String NAME = "format";
	
	private static final String MEDIA_NAME = "Media";
	private static final String MIME_TYPE_NAME = "mimeType";
	private static final String MEDIUM_NAME = "medium";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Format(Element element) throws InvalidDDMSException {
		Util.requireDDMSValue("format element", element);
		Element mediaElement = element.getFirstChildElement(MEDIA_NAME, element.getNamespaceURI());
		if (mediaElement != null) {
			Element mimeTypeElement = mediaElement.getFirstChildElement(MIME_TYPE_NAME, element.getNamespaceURI());
			if (mimeTypeElement != null)
				_cachedMimeType = mimeTypeElement.getValue();
			Element extentElement = mediaElement.getFirstChildElement(MediaExtent.NAME, element.getNamespaceURI());
			if (extentElement != null)
				_cachedExtent = new MediaExtent(extentElement);
			Element mediumElement = mediaElement.getFirstChildElement(MEDIUM_NAME, element.getNamespaceURI());
			if (mediumElement != null)
				_cachedMedium = mediumElement.getValue();
		}
		setXOMElement(element, true);
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param mimeType the mimeType element (required)
	 * @param extent the extent element (may be null)
	 * @param medium the medium element (may be null)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Format(String mimeType, MediaExtent extent, String medium) throws InvalidDDMSException {
		Element mediaElement = Util.buildDDMSElement(MEDIA_NAME, null);
		Util.addDDMSChildElement(mediaElement, MIME_TYPE_NAME, mimeType);
		if (extent != null)
			mediaElement.appendChild(extent.getXOMElementCopy());
		Util.addDDMSChildElement(mediaElement, MEDIUM_NAME, medium);
		Element element = Util.buildDDMSElement(Format.NAME, null);
		element.appendChild(mediaElement);
		_cachedMimeType = mimeType;
		_cachedExtent = extent;
		_cachedMedium = medium;
		setXOMElement(element, true);
	}
			
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A mimeType exists, and is not empty.</li>
	 * <li>Exactly 1 mimeType, 0-1 extents, and 0-1 mediums exist.</li>
	 * <li>If set, the extent is a valid component.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public void validate() throws InvalidDDMSException {
		super.validate();
		Element mediaElement = getChild(MEDIA_NAME);
		Util.requireDDMSValue("Media element", mediaElement);
		Util.requireDDMSValue(MIME_TYPE_NAME, getMimeType());
		Util.requireBoundedDDMSChildCount(mediaElement, MIME_TYPE_NAME, 1, 1);
		Util.requireBoundedDDMSChildCount(mediaElement, MediaExtent.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(mediaElement, MEDIUM_NAME, 0, 1);
		if (getExtent() != null)
			getExtent().validate();
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("format.media", getMimeType(), true));
		if (getExtent() != null)
			html.append(getExtent().toHTML());
		html.append(buildHTMLMeta("format.medium", getMedium(), false));
		return (html.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Media format", getMimeType(), true));
		if (getExtent() != null)
			text.append(getExtent().toText());
		text.append(buildTextLine("Medium", getMedium(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Format))
			return (false);
		Format test = (Format) obj;
		boolean isEqual = getMimeType().equals(test.getMimeType()) &&
			Util.nullEquals(getExtent(), test.getExtent()) && 
			getMedium().equals(test.getMedium());
		return (isEqual);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getMimeType().hashCode();
		if (getExtent() != null)
			result = 7 * result + getExtent().hashCode();
		result = 7 * result + getMedium().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the mimeType element child text. Will return an empty string if not set, 
	 * but that cannot occur after instantiation.
	 */
	public String getMimeType() {
		return (Util.getNonNullString(_cachedMimeType));
	}

	/**
	 * Accessor for the extent
	 */
	public MediaExtent getExtent() {
		return (_cachedExtent);
	}

	/**
	 * Convenience accessor for the extent qualifier. Returns an empty string if there is not 
	 * extent.
	 */
	public String getExtentQualifier() {
		return (getExtent() == null ? "" : getExtent().getQualifier());
	}
	
	/**
	 * Convenience accessor for the extent value. Returns an empty string if there is not 
	 * extent.
	 */
	public String getExtentValue() {
		return (getExtent() == null ? "" : getExtent().getValue());
	}
	
	/**
	 * Accessor for the medium element child text
	 */
	public String getMedium() {
		return (Util.getNonNullString(_cachedMedium));
	}
	
} 