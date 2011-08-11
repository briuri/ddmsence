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
package buri.ddmsence.ddms.format;

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
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
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#format <br />
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
		try {
			Util.requireDDMSValue("format element", element);
			String ddmsNamespace = element.getNamespaceURI();
			Element mediaElement = element.getFirstChildElement(MEDIA_NAME, ddmsNamespace);
			if (mediaElement != null) {
				Element mimeTypeElement = mediaElement.getFirstChildElement(MIME_TYPE_NAME, ddmsNamespace);
				if (mimeTypeElement != null)
					_cachedMimeType = mimeTypeElement.getValue();
				Element extentElement = mediaElement.getFirstChildElement(MediaExtent.NAME, ddmsNamespace);
				if (extentElement != null)
					_cachedExtent = new MediaExtent(extentElement);
				Element mediumElement = mediaElement.getFirstChildElement(MEDIUM_NAME, ddmsNamespace);
				if (mediumElement != null)
					_cachedMedium = mediumElement.getValue();
			}
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
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
		try {
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
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
			
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>A mimeType exists, and is not empty.</li>
	 * <li>Exactly 1 mimeType, 0-1 extents, and 0-1 mediums exist.</li>
	 * <li>If an extent exists, it is using the same version of DDMS.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
		Element mediaElement = getChild(MEDIA_NAME);
		Util.requireDDMSValue("Media element", mediaElement);
		Util.requireDDMSValue(MIME_TYPE_NAME, getMimeType());
		Util.requireBoundedDDMSChildCount(mediaElement, MIME_TYPE_NAME, 1, 1);
		Util.requireBoundedDDMSChildCount(mediaElement, MediaExtent.NAME, 0, 1);
		Util.requireBoundedDDMSChildCount(mediaElement, MEDIUM_NAME, 0, 1);
		if (getExtent() != null)
			Util.requireCompatibleVersion(this, getExtent());

		validateWarnings();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A ddms:medium element was found with no value.</li>
	 * <li>Include any validation warnings from the MediaExtent child.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		Element mediaElement = getChild(MEDIA_NAME);
		if (Util.isEmpty(getMedium())
				&& mediaElement.getChildElements(MEDIUM_NAME, mediaElement.getNamespaceURI()).size() == 1)
			addWarning("A ddms:medium element was found with no value.");
		if (getExtent() != null) {
			addWarnings(getExtent().getValidationWarnings(), false);
		}
	}
	
	/**
	 * @see AbstractBaseComponent#getLocatorSuffix()
	 */
	protected String getLocatorSuffix() {
		return (ValidationMessage.ELEMENT_PREFIX + getXOMElement().getNamespacePrefix() + ":" + MEDIA_NAME);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("format.Media.mimeType", getMimeType(), true));
		if (getExtent() != null)
			html.append(getExtent().toHTML());
		html.append(buildHTMLMeta("format.Media.medium", getMedium(), false));
		return (html.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("format.Media.mimeType", getMimeType(), true));
		if (getExtent() != null)
			text.append(getExtent().toText());
		text.append(buildTextLine("format.Media.medium", getMedium(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Format))
			return (false);
		Format test = (Format) obj;
		boolean isEqual = getMimeType().equals(test.getMimeType()) 
			&& Util.nullEquals(getExtent(), test.getExtent())
			&& getMedium().equals(test.getMedium());
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
	 * Accessor for the mimeType element child text. Will return an empty string if not set, but that cannot occur after
	 * instantiation.
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
	 * Convenience accessor for the extent qualifier. Returns an empty string if there is not extent.
	 */
	public String getExtentQualifier() {
		return (getExtent() == null ? "" : getExtent().getQualifier());
	}

	/**
	 * Convenience accessor for the extent value. Returns an empty string if there is not extent.
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
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 7851044806424206976L;
		private String _mimeType;
		private MediaExtent.Builder _extent;
		private String _medium;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Format format) {
			setMimeType(format.getMimeType());
			if (format.getExtent() != null)
				setExtent(new MediaExtent.Builder(format.getExtent()));
			setMedium(format.getMedium());			
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Format commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new Format(getMimeType(), getExtent().commit(), getMedium()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getMimeType()) && Util.isEmpty(getMedium()) && getExtent().isEmpty());
		}
		
		/**
		 * Builder accessor for the mimeType element child text.
		 */
		public String getMimeType() {
			return _mimeType;
		}

		/**
		 * Builder accessor for the mimeType element child text.
		 */
		public void setMimeType(String mimeType) {
			_mimeType = mimeType;
		}

		/**
		 * Builder accessor for the mediaExtent element.
		 */
		public MediaExtent.Builder getExtent() {
			if (_extent == null)
				_extent = new MediaExtent.Builder();
			return _extent;
		}

		/**
		 * Builder accessor for the mediaExtent element.
		 */
		public void setExtent(MediaExtent.Builder extent) {
			_extent = extent;
		}

		/**
		 * Builder accessor for the medium element child text.
		 */
		public String getMedium() {
			return _medium;
		}

		/**
		 * Builder accessor for the medium element child text.
		 */
		public void setMedium(String medium) {
			_medium = medium;
		}
	}	
} 