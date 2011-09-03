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
package buri.ddmsence.ddms.summary;

import java.io.Serializable;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:boundingBox.
 *
 * <p>The DDMS documentation has no Text/HTML examples for the output of this component, so a best guess was taken:</p>
 * <ul>
 * <p><b>Suggested Text Output</b><br /><code>
 * boundingBox westBL: value<br />
 * boundingBox eastBL: value<br />
 * boundingBox southBL: value<br />
 * boundingBox northBL: value<br />
 * </code></p>
 * 
 * <p><b>Suggested HTML Output</b><br /><code>
 * &lt;meta name="geospatialCoverage.GeospatialExtent.boundingBox.westBL" content="value" /&gt;<br />
 * &lt;meta name="geospatialCoverage.GeospatialExtent.boundingBox.eastBL" content="value" /&gt;<br />
 * &lt;meta name="geospatialCoverage.GeospatialExtent.boundingBox.southBL" content="value" /&gt;<br />
 * &lt;meta name="geospatialCoverage.GeospatialExtent.boundingBox.northBL" content="value" /&gt;<br />
 * </code>
 * </ul></p>
 *
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:westBL</u>: westbound longitude (required)<br />
 * <u>ddms:eastBL</u>: eastbound longitude (required)<br />
 * <u>ddms:southBL</u>: northbound latitude (required)<br />
 * <u>ddms:northBL</u>: southbound latitude (required)<br />
 * Please note that the case of the nested elements changed starting in DDMS 4.0. Previously, the first letter
 * of each element was capitalized (i.e. WestBL/EastBL/SouthBL/NorthBL).
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#geospatialCoverage_GeospatialExtent_boundingBox<br />
 * <u>Description</u>: A wrapper for elements containing the bounding longitudes and latitudes for describing a 
 * geographic extent.<br />
 * <u>Obligation</u>: Optional in a geospatialCoverage element<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class BoundingBox extends AbstractBaseComponent {

	// Values are cached upon instantiation, so doubles are only generated once.
	private Double _cachedWestBL = null;
	private Double _cachedEastBL = null;
	private Double _cachedSouthBL = null;
	private Double _cachedNorthBL = null;
		
	/** The element name of this component */
	public static final String NAME = "boundingBox";
			
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingBox(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("boundingBox element", element);
			setXOMElement(element, false);
			_cachedWestBL = getChildTextAsDouble(element, getWestBLName());
			_cachedEastBL = getChildTextAsDouble(element, getEastBLName());
			_cachedSouthBL = getChildTextAsDouble(element, getSouthBLName());
			_cachedNorthBL = getChildTextAsDouble(element, getNorthBLName());
			validate();
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param westBL the westbound longitude
	 * @param eastBL the eastbound longitude
	 * @param southBL the southbound latitude
	 * @param northBL the northbound latitude
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public BoundingBox(double westBL, double eastBL, double southBL, double northBL) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(BoundingBox.NAME, null);
			setXOMElement(element, false);
			element.appendChild(Util.buildDDMSElement(getWestBLName(), String.valueOf(westBL)));
			element.appendChild(Util.buildDDMSElement(getEastBLName(), String.valueOf(eastBL)));
			element.appendChild(Util.buildDDMSElement(getSouthBLName(), String.valueOf(southBL)));
			element.appendChild(Util.buildDDMSElement(getNorthBLName(), String.valueOf(northBL)));
			_cachedWestBL = Double.valueOf(westBL);
			_cachedEastBL = Double.valueOf(eastBL);
			_cachedSouthBL = Double.valueOf(southBL);
			_cachedNorthBL = Double.valueOf(northBL);
			validate();
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
	 * <li>A westBL exists.</li>
	 * <li>An eastBL exists.</li>
	 * <li>A southBL exists.</li>
	 * <li>A northBL exists.</li>
	 * <li>westBL and eastBL must be between -180 and 180 degrees.</li>
	 * <li>southBL and northBL must be between -90 and 90 degrees.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */	
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), NAME);
		Util.requireDDMSValue("westbound longitude", getWestBL());
		Util.requireDDMSValue("eastbound longitude", getEastBL());
		Util.requireDDMSValue("southbound latitude", getSouthBL());
		Util.requireDDMSValue("northbound latitude", getNorthBL());
		Util.requireValidLongitude(getWestBL());
		Util.requireValidLongitude(getEastBL());
		Util.requireValidLatitude(getSouthBL());
		Util.requireValidLatitude(getNorthBL());
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		String prefix = GeospatialCoverage.NAME + ".GeospatialExtent." + NAME + ".";
		html.append(buildHTMLMeta(prefix + getWestBLName(), String.valueOf(getWestBL()), true));
		html.append(buildHTMLMeta(prefix + getEastBLName(), String.valueOf(getEastBL()), true));
		html.append(buildHTMLMeta(prefix + getSouthBLName(), String.valueOf(getSouthBL()), true));
		html.append(buildHTMLMeta(prefix + getNorthBLName(), String.valueOf(getNorthBL()), true));
		return (html.toString());
	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine(NAME + " " + getWestBLName(), String.valueOf(getWestBL()), true));
		text.append(buildTextLine(NAME + " " + getEastBLName(), String.valueOf(getEastBL()), true));
		text.append(buildTextLine(NAME + " " + getSouthBLName(), String.valueOf(getSouthBL()), true));
		text.append(buildTextLine(NAME + " " + getNorthBLName(), String.valueOf(getNorthBL()), true));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof BoundingBox))
			return (false);
		BoundingBox test = (BoundingBox) obj;
		return (getWestBL().equals(test.getWestBL()) 
			&& getEastBL().equals(test.getEastBL())
			&& getSouthBL().equals(test.getSouthBL()) 
			&& getNorthBL().equals(test.getNorthBL()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getWestBL().hashCode();
		result = 7 * result + getEastBL().hashCode();
		result = 7 * result + getSouthBL().hashCode();
		result = 7 * result + getNorthBL().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the name of the westbound longitude element, which changed in DDMS 4.0.
	 */
	private String getWestBLName() {
		return (getDDMSVersion().isAtLeast("4.0") ? "westBL" : "WestBL");
	}
	
	/**
	 * Accessor for the name of the eastbound longitude element, which changed in DDMS 4.0.
	 */
	private String getEastBLName() {
		return (getDDMSVersion().isAtLeast("4.0") ? "eastBL" : "EastBL");
	}
	
	/**
	 * Accessor for the name of the southbound latitude element, which changed in DDMS 4.0.
	 */
	private String getSouthBLName() {
		return (getDDMSVersion().isAtLeast("4.0") ? "southBL" : "SouthBL");
	}
	
	/**
	 * Accessor for the name of the northbound latitude element, which changed in DDMS 4.0.
	 */
	private String getNorthBLName() {
		return (getDDMSVersion().isAtLeast("4.0") ? "northBL" : "NorthBL");
	}
	
	/**
	 * Accessor for the westbound longitude.
	 */
	public Double getWestBL() {
		return (_cachedWestBL); 
	}
	
	/**
	 * Accessor for the eastbound longitude.
	 */
	public Double getEastBL() {
		return (_cachedEastBL); 
	}
	
	/**
	 * Accessor for the southbound latitude.
	 */
	public Double getSouthBL() {
		return (_cachedSouthBL); 
	}
	
	/**
	 * Accessor for the northbound latitude.
	 */
	public Double getNorthBL() {
		return (_cachedNorthBL); 
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -2364407215439097065L;
		private Double _westBL;
		private Double _eastBL;
		private Double _southBL;
		private Double _northBL;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(BoundingBox box) {
			setWestBL(box.getWestBL());
			setEastBL(box.getEastBL());
			setSouthBL(box.getSouthBL());
			setNorthBL(box.getNorthBL());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public BoundingBox commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			// Check for existence of values before casting to primitives.
			if (getWestBL() == null || getEastBL() == null || getSouthBL() == null || getNorthBL() == null)
				throw new InvalidDDMSException("A ddms:boundingBox requires two latitude and two longitude values.");
			return (new BoundingBox(getWestBL().doubleValue(), getEastBL().doubleValue(), getSouthBL().doubleValue(),
				getNorthBL().doubleValue()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (getWestBL() == null && getEastBL() == null && getSouthBL() == null && getNorthBL() == null);
		}
		
		/**
		 * Builder accessor for the westbound longitude
		 */
		public Double getWestBL() {
			return _westBL;
		}

		/**
		 * Builder accessor for the westbound longitude
		 */
		public void setWestBL(Double westBL) {
			_westBL = westBL;
		}

		/**
		 * Builder accessor for the eastbound longitude
		 */
		public Double getEastBL() {
			return _eastBL;
		}

		/**
		 * Builder accessor for the eastbound longitude
		 */
		public void setEastBL(Double eastBL) {
			_eastBL = eastBL;
		}

		/**
		 * Builder accessor for the southbound latitude
		 */
		public Double getSouthBL() {
			return _southBL;
		}

		/**
		 * Builder accessor for the southbound latitude
		 */
		public void setSouthBL(Double southBL) {
			_southBL = southBL;
		}

		/**
		 * Builder accessor for the northbound latitude
		 */
		public Double getNorthBL() {
			return _northBL;
		}

		/**
		 * Builder accessor for the northbound latitude
		 */
		public void setNorthBL(Double northBL) {
			_northBL = northBL;
		}
	}
} 