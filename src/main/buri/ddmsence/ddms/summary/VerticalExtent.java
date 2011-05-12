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
package buri.ddmsence.ddms.summary;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:verticalExtent.
 *
 * <p>
 * DDMSence is stricter than the specification in the following ways:</p><ul>
 * <li>The optional unitOfMeasure and datum on the MinVerticalExtent/MaxVerticalExtent child elements MUST match the 
 * values on the required attributes of the same name on this element. It does not seem logical to specify these 
 * attributes on the parent element and then express the actual values with a different measure. Note that because 
 * DDMSence is giving precedence to the top-level unitOfMeasure and datum attributes, those attributes on the 
 * children are not displayed in HTML/Text. However, they are still rendered in XML, if present in an existing 
 * document.</li>
 * </ul></p>
 * 
 * <p>The above design decision dictates that VerticalDistance (the type behind MinVerticalExtent and MaxVerticalExtent) 
 * does not need to be implemented as a Java class.</p>
 * 
 * <p>The DDMS documentation has no Text/HTML examples for the output of this component, so a best guess was taken:</p>
 * <ul>
 * <p><b>Suggested Text Output</b><br /><code>
 * Vertical Extent Unit of Measure: value<br />
 * Vertical Extent Datum: value<br />
 * Minimum Vertical Extent: value<br />
 * Maximum Vertical Extent: value<br />
 * </code></p>
 * 
 * <p><b>Suggested HTML Output</b><br /><code>
 * &lt;meta name="geospatial.verticalExtent.unitOfMeasure" content="value" /&gt;<br />
 * &lt;meta name="geospatial.verticalExtent.datum" content="value" /&gt;<br />
 * &lt;meta name="geospatial.verticalExtent.minimum" content="value" /&gt;<br />
 * &lt;meta name="geospatial.verticalExtent.maximum" content="value" /&gt;<br />
 * </code>
 * </ul>
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:MinVerticalExtent</u>: minimum extent (required)<br />
 * <u>ddms:MaxVerticalExtent</u>: maximum extent (required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:unitOfMeasure</u>: unit of measure (Meter, Kilometer, Foot, StatuteMile, NauticalMile, Fathom, Inch) 
 * (required) <br />
 * <u>ddms:datum</u>: vertical datum (MSL, AGL, HAE) (required)<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: http://metadata.ces.mil/mdr/irs/DDMS/ddms_categories.htm#VerticalExtent<br />
 * <u>Description</u>: A wrapper for child elements used to describe the vertical extent applicable to the 
 * resource.<br />
 * <u>Obligation</u>: Optional in a geospatialCoverage element<br />
 * <u>Schema Modification Date</u>: 2010-01-25<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class VerticalExtent extends AbstractBaseComponent {

	// Values are cached upon instantiation, so doubles are only generated once.
	private Double _cachedMin;
	private Double _cachedMax;
		
	private static Set<String> VERTICAL_DATUM_TYPES = new HashSet<String>();
	static {
		VERTICAL_DATUM_TYPES.add("MSL");
		VERTICAL_DATUM_TYPES.add("AGL");
		VERTICAL_DATUM_TYPES.add("HAE");
	}

	private static Set<String> LENGTH_MEASURE_TYPES = new HashSet<String>();
	static {
		LENGTH_MEASURE_TYPES.add("Meter");
		LENGTH_MEASURE_TYPES.add("Kilometer");
		LENGTH_MEASURE_TYPES.add("Foot");
		LENGTH_MEASURE_TYPES.add("StatuteMile");
		LENGTH_MEASURE_TYPES.add("NauticalMile");
		LENGTH_MEASURE_TYPES.add("Fathom");
		LENGTH_MEASURE_TYPES.add("Inch");
	}
	
	/** The element name of this component */
	public static final String NAME = "verticalExtent";
	
	private static final String MIN_VERTICAL_EXTENT_NAME = "MinVerticalExtent";
	private static final String MAX_VERTICAL_EXTENT_NAME = "MaxVerticalExtent";
	private static final String DATUM_NAME = "datum";
	private static final String UOM_NAME ="unitOfMeasure";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public VerticalExtent(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("verticalExtent element", element);
			_cachedMin = getChildTextAsDouble(element, MIN_VERTICAL_EXTENT_NAME);
			_cachedMax = getChildTextAsDouble(element, MAX_VERTICAL_EXTENT_NAME);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param minVerticalExtent the minimum (required)
	 * @param maxVerticalExtent the maximum (required)
	 * @param unitOfMeasure the unit of measure (required)
	 * @param datum the datum (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public VerticalExtent(double minVerticalExtent, double maxVerticalExtent, String unitOfMeasure, String datum)
		throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(VerticalExtent.NAME, null);
			Util.addDDMSAttribute(element, UOM_NAME, unitOfMeasure);
			Util.addDDMSAttribute(element, DATUM_NAME, datum);
			element.appendChild(Util.buildDDMSElement(MIN_VERTICAL_EXTENT_NAME, String.valueOf(minVerticalExtent)));
			element.appendChild(Util.buildDDMSElement(MAX_VERTICAL_EXTENT_NAME, String.valueOf(maxVerticalExtent)));
			_cachedMin = Double.valueOf(minVerticalExtent);
			_cachedMax = Double.valueOf(maxVerticalExtent);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
		
	/**
	 * Validates a vertical datum type against the allowed types.
	 * 
	 * @param datumType the type to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateVerticalDatumType(String datumType) throws InvalidDDMSException {
		Util.requireDDMSValue("vertical datum type", datumType);
		if (!VERTICAL_DATUM_TYPES.contains(datumType))
			throw new InvalidDDMSException("The vertical datum type must be one of " + VERTICAL_DATUM_TYPES);
	}
	
	/**
	 * Validates a length measure type against the allowed types.
	 * 
	 * @param lengthType the type to test
	 * @throws InvalidDDMSException if the value is null, empty or invalid.
	 */
	public static void validateLengthMeasureType(String lengthType) throws InvalidDDMSException {
		Util.requireDDMSValue("length measure type", lengthType);
		if (!LENGTH_MEASURE_TYPES.contains(lengthType))
			throw new InvalidDDMSException("The length measure type must be one of " + LENGTH_MEASURE_TYPES);
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>A MinVerticalExtent exists.</li>
	 * <li>A MaxVerticalExtent exists.</li>
	 * <li>A unitOfMeasure exists and has an appropriate value.</li>
	 * <li>A datum exists and has an appropriate value.</li>
	 * <li>If a MinVerticalExtent has unitOfMeasure or datum set, its values match the parent attribute values.</li>
	 * <li>If a MaxVerticalExtent has unitOfMeasure or datum set, its values match the parent attribute values.</li>
	 * <li>The MinVerticalExtent is less than the MaxVerticalExtent.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMSVersion.getVersionFor(getDDMSVersion()).getNamespace(), NAME);
		Util.requireDDMSValue(MIN_VERTICAL_EXTENT_NAME, getMinVerticalExtent());
		Util.requireDDMSValue(MAX_VERTICAL_EXTENT_NAME, getMaxVerticalExtent());
		Util.requireDDMSValue(UOM_NAME, getUnitOfMeasure());
		Util.requireDDMSValue(DATUM_NAME, getDatum());
		validateLengthMeasureType(getUnitOfMeasure());
		validateVerticalDatumType(getDatum());	
		validateInheritedAttributes(getChild(MIN_VERTICAL_EXTENT_NAME));
		validateInheritedAttributes(getChild(MAX_VERTICAL_EXTENT_NAME));
		if (getMaxVerticalExtent().compareTo(getMinVerticalExtent()) < 0)
			throw new InvalidDDMSException("Minimum vertical extent must be less than maximum vertical extent.");
	}
	
	/**
	 * Confirms that the unitOfMeasure and datum on minimum and maximum extent elements matches the parent attribute
	 * values. This is an additional level of logic added by DDMSence.
	 * 
	 * @param extentElement
	 * @throws InvalidDDMSException
	 */
	private void validateInheritedAttributes(Element extentElement) throws InvalidDDMSException {
		String unitOfMeasure = extentElement.getAttributeValue(UOM_NAME, extentElement.getNamespaceURI());
		String datum = extentElement.getAttributeValue(DATUM_NAME, extentElement.getNamespaceURI());
		if (!Util.isEmpty(unitOfMeasure) && !unitOfMeasure.equals(getUnitOfMeasure()))
			throw new InvalidDDMSException("The unitOfMeasure on the " + extentElement.getLocalName()
				+ " element must match the unitOfMeasure on the enclosing verticalExtent element.");
		if (!Util.isEmpty(datum) && !datum.equals(getDatum()))
			throw new InvalidDDMSException("The datum on the " + extentElement.getLocalName()
				+ " element must match the datum on the enclosing verticalExtent element.");
	}
	

	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		html.append(buildHTMLMeta("geospatial.verticalExtent.unitOfMeasure", getUnitOfMeasure(), true));
		html.append(buildHTMLMeta("geospatial.verticalExtent.datum", getDatum(), true));
		html.append(buildHTMLMeta("geospatial.verticalExtent.minimum", String.valueOf(getMinVerticalExtent()), true));
		html.append(buildHTMLMeta("geospatial.verticalExtent.maximum", String.valueOf(getMaxVerticalExtent()), true));
		return (html.toString());
	}
		 
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		text.append(buildTextLine("Vertical Extent Unit of Measure", getUnitOfMeasure(), true));
		text.append(buildTextLine("Vertical Extent Datum", getDatum(), true));
		text.append(buildTextLine("Minimum Vertical Extent", String.valueOf(getMinVerticalExtent()), true));
		text.append(buildTextLine("Maximum Vertical Extent", String.valueOf(getMaxVerticalExtent()), true));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof VerticalExtent))
			return (false);
		VerticalExtent test = (VerticalExtent) obj;
		return (getUnitOfMeasure().equals(test.getUnitOfMeasure()) 
			&& getDatum().equals(test.getDatum())
			&& getMinVerticalExtent().equals(test.getMinVerticalExtent()) 
			&& getMaxVerticalExtent().equals(test.getMaxVerticalExtent()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getUnitOfMeasure().hashCode();
		result = 7 * result + getDatum().hashCode();
		result = 7 * result + getMinVerticalExtent().hashCode();
		result = 7 * result + getMaxVerticalExtent().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the unitOfMeasure attribute
	 */
	public String getUnitOfMeasure() {
		return (getAttributeValue(UOM_NAME)); 
	}
	
	/**
	 * Accessor for the vertical datum attribute
	 */
	public String getDatum() {
		return (getAttributeValue(DATUM_NAME));
	}
	
	/**
	 * Accessor for the minimum extent
	 */
	public Double getMinVerticalExtent() {
		return (_cachedMin); 
	}
	
	/**
	 * Accessor for the maximum extent
	 */
	public Double getMaxVerticalExtent() {
		return (_cachedMax); 
	}
	
	/**
	 * Builder for this DDMS component. The builder should be used when a DDMS record needs to be built up over time,
	 * but validation should not occur until the end. The commit() method attempts to finalize the immutable object
	 * based on the values gathered.
	 * 
	 * <p>The builder approach differs from calling the immutable constructor directly because it treats a Builder
	 * instance with no values provided as "no component" instead of "a component with missing values". For example,
	 * calling a constructor directly with an empty string for a required parameter might throw an InvalidDDMSException,
	 * while calling commit() on a Builder without setting any values would just return null.</p>
	 * 
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder {
		private Double _minVerticalExtent;
		private Double _maxVerticalExtent;
		private String _unitOfMeasure;
		private String _datum;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(VerticalExtent extent) {
			setMinVerticalExtent(extent.getMinVerticalExtent());
			setMaxVerticalExtent(extent.getMaxVerticalExtent());
			setUnitOfMeasure(extent.getUnitOfMeasure());
			setDatum(extent.getDatum());
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. If no values have been provided, a null
		 * instance will be returned instead of a possibly invalid one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public VerticalExtent commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			// Check for existence of values before casting to primitives.
			if (getMinVerticalExtent() == null || getMaxVerticalExtent() == null)
				throw new InvalidDDMSException("A ddms:verticalExtent requires a minimum and maximum extent value.");
			return (new VerticalExtent(getMinVerticalExtent().doubleValue(), getMaxVerticalExtent().doubleValue(),
				getUnitOfMeasure(), getDatum()));
		}
		
		/**
		 * Checks if any values have been provided for this Builder.
		 * 
		 * @return true if every field is empty
		 */
		public boolean isEmpty() {
			return (getMinVerticalExtent() == null && getMaxVerticalExtent() == null
				&& Util.isEmpty(getUnitOfMeasure())
				&& Util.isEmpty(getDatum()));
		}
		
		/**
		 * Builder accessor for the minimum extent
		 */
		public Double getMinVerticalExtent() {
			return _minVerticalExtent;
		}

		/**
		 * Builder accessor for the minimum extent
		 */
		public void setMinVerticalExtent(Double minVerticalExtent) {
			_minVerticalExtent = minVerticalExtent;
		}

		/**
		 * Builder accessor for the maximum extent
		 */
		public Double getMaxVerticalExtent() {
			return _maxVerticalExtent;
		}

		/**
		 * Builder accessor for the maximum extent
		 */
		public void setMaxVerticalExtent(Double maxVerticalExtent) {
			_maxVerticalExtent = maxVerticalExtent;
		}

		/**
		 * Builder accessor for the unitOfMeasure attribute
		 */
		public String getUnitOfMeasure() {
			return _unitOfMeasure;
		}

		/**
		 * Builder accessor for the unitOfMeasure attribute
		 */
		public void setUnitOfMeasure(String unitOfMeasure) {
			_unitOfMeasure = unitOfMeasure;
		}

		/**
		 * Builder accessor for the vertical datum attribute
		 */
		public String getDatum() {
			return _datum;
		}

		/**
		 * Builder accessor for the vertical datum attribute
		 */
		public void setDatum(String datum) {
			_datum = datum;
		}
	}
} 