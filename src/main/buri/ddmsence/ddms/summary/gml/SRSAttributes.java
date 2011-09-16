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
package buri.ddmsence.ddms.summary.gml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.AbstractAttributeGroup;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.Resource;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * Attribute group for the four SRS attributes used in the GML profile.
 * 
 * <p>
 * Because the GML-Profile defines these attributes locally inside of attribute groups, they are not in any namespace.
 * Some older examples on the DDMS website inaccurately display the attributes with the gml: prefix.
 * </p>
 *
 * <p>When validating this attribute group, the required/optional nature of the srsName attribute is not checked. Because
 * that limitation depends on the parent element (for example, gml:Point and gml:Polygon require an srsName, but gml:pos 
 * does not), the parent element should be responsible for checking.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>srsName</u>: A URI-based name (optional on gml:pos, required everywhere else)<br />
 * <u>srsDimension</u>: A positive integer dimension (optional)<br />
 * <u>axisLabels</u>: Ordered list of labels for the axes, as a space-delimited list of NCNames (valid XML names without
 * colons) (optional, but if no srsName is set, this should be omitted too)<br />
 * <u>uomLabels</u>: Ordered list of unit of measure (uom) labels for all the axes, as a space-delimited list of NCNames
 * (valid XML names without colons) (required when axisLabels is set)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class SRSAttributes extends AbstractAttributeGroup {

	private String _gmlNamespace = null;
	
	private String _cachedSrsName;
	private Integer _cachedSrsDimension;
	private List<String> _cachedAxisLabels;
	private List<String> _cachedUomLabels;
	
	/** The prefix of the shared attributes */
	public static final String NO_PREFIX = "";
	
	/** The namespace of the shared attributes */
	public static final String NO_NAMESPACE = "";
		
	private static final String SRS_NAME_NAME = "srsName";
	private static final String SRS_DIMENSION_NAME = "srsDimension";
	private static final String AXIS_LABELS_NAME = "axisLabels";
	private static final String UOM_LABELS_NAME = "uomLabels";
	
	/**
	 * Base constructor
	 * 
	 * @param element the XOM element which is decorated with these attributes.
	 */
	public SRSAttributes(Element element) throws InvalidDDMSException {
		_gmlNamespace = element.getNamespaceURI();
		_cachedSrsName = element.getAttributeValue(SRS_NAME_NAME, NO_NAMESPACE);
		String srsDimension = element.getAttributeValue(SRS_DIMENSION_NAME, NO_NAMESPACE);
		if (!Util.isEmpty(srsDimension)) {
			_cachedSrsDimension = Integer.valueOf(srsDimension);
		}
		String axisLabels = element.getAttributeValue(AXIS_LABELS_NAME, NO_NAMESPACE);
		_cachedAxisLabels = new ArrayList<String>();
		if (!Util.isEmpty(axisLabels)) {
			_cachedAxisLabels.addAll(Util.getXsListAsList(axisLabels));
		}
		String uomLabels = element.getAttributeValue(UOM_LABELS_NAME, NO_NAMESPACE);
		_cachedUomLabels = new ArrayList<String>();
		if (!Util.isEmpty(uomLabels)) {
			_cachedUomLabels.addAll(Util.getXsListAsList(uomLabels));
		}
		validate();
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param srsName	the srsName (required if the name is not "pos")
	 * @param srsDimension the srsDimension (optional)
	 * @param axisLabels the axis labels (optional, but should be omitted if no srsName is set)
	 * @param uomLabels the labels for UOM (required when axisLabels is set)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SRSAttributes(String srsName, Integer srsDimension, List<String> axisLabels, List<String> uomLabels)
		throws InvalidDDMSException {
		_gmlNamespace = DDMSVersion.getCurrentVersion().getGmlNamespace();
		if (axisLabels == null)
			axisLabels = Collections.emptyList();
		if (uomLabels == null)
			uomLabels = Collections.emptyList();
		_cachedSrsName = srsName;
		_cachedSrsDimension = srsDimension;
		_cachedAxisLabels = axisLabels;
		_cachedUomLabels = uomLabels;
		validate();
	}
	
	/**
	 * Convenience method to add these attributes onto an existing XOM Element
	 * 
	 * @param element the element to decorate
	 * @throws InvalidDDMSException if the DDMS version of the element is different
	 */
	protected void addTo(Element element) throws InvalidDDMSException {
		if (!getGmlNamespace().equals(element.getNamespaceURI())) {
			throw new InvalidDDMSException("These SRS attributes cannot decorate a DDMS component with"
				+ " a different DDMS version.");
		}	
		Util.addAttribute(element, NO_PREFIX, SRS_NAME_NAME, NO_NAMESPACE, getSrsName());
		if (getSrsDimension() != null)
			Util.addAttribute(element, NO_PREFIX, SRS_DIMENSION_NAME, NO_NAMESPACE, String.valueOf(getSrsDimension()));
		Util.addAttribute(element, NO_PREFIX, AXIS_LABELS_NAME, NO_NAMESPACE, getAxisLabelsAsXsList());
		Util.addAttribute(element, NO_PREFIX, UOM_LABELS_NAME, NO_NAMESPACE, getUomLabelsAsXsList());
	}
	
	/**
	 * Validates the attribute group.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>If the srsName is set, it must be a valid URI.</li>
	 * <li>If the srsDimension is set, it must be positive.</li>
	 * <li>If the srsName is not set, the axisLabels must be not set or empty.</li>
	 * <li>If the axisLabels are not set or empty, the uomLabels must be not set or empty.</li>
	 * <li>Each axisLabel must be a NCName.</li>
	 * <li>Each uomLabel must be a NCName.</li> 
	 * </td></tr></table>
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		if (!Util.isEmpty(getSrsName()))
			Util.requireDDMSValidURI(getSrsName());
		if (getSrsDimension() != null && getSrsDimension().intValue() < 0)
			throw new InvalidDDMSException("The srsDimension must be a positive integer.");
		if (Util.isEmpty(getSrsName()) && !getAxisLabels().isEmpty())
			throw new InvalidDDMSException("The axisLabels attribute can only be used in tandem with an srsName.");
		if (getAxisLabels().isEmpty() && !getUomLabels().isEmpty())
			throw new InvalidDDMSException("The uomLabels attribute can only be used in tandem with axisLabels.");
		Util.requireValidNCNames(getAxisLabels());
		Util.requireValidNCNames(getUomLabels());
		super.validate();
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix);		
		StringBuffer text = new StringBuffer();
		text.append(Resource.buildOutput(isHTML, prefix + "srsName", getSrsName(), false));
		if (getSrsDimension() != null) {
			text.append(Resource.buildOutput(isHTML, prefix + "srsDimension",
				String.valueOf(getSrsDimension()), false));
		}
		text.append(Resource.buildOutput(isHTML, prefix + "axisLabels", getAxisLabelsAsXsList(), false));
		text.append(Resource.buildOutput(isHTML, prefix + "uomLabels", getUomLabelsAsXsList(), false));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof SRSAttributes))
			return (false);
		SRSAttributes test = (SRSAttributes) obj;
		return (getSrsName().equals(test.getSrsName()) 
			&& Util.nullEquals(getSrsDimension(), test.getSrsDimension())
			&& Util.listEquals(getAxisLabels(), test.getAxisLabels()) 
			&& Util.listEquals(getUomLabels(), test.getUomLabels()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = 0; 
		result = 7 * result + getSrsName().hashCode();
		if (getSrsDimension() != null)
			result = 7 * result + getSrsDimension().hashCode();
		result = 7 * result + getAxisLabels().hashCode();
		result = 7 * result + getUomLabels().hashCode();
		return (result);
	}
		
	/**
	 * Accessor for the GML namespace on the enclosing element.
	 */
	public String getGmlNamespace() {
		return (Util.getNonNullString(_gmlNamespace));
	}
	
	/**
	 * Accessor for the srsName.
	 */
	public String getSrsName() {
		return (Util.getNonNullString(_cachedSrsName));
	}
	
	/**
	 * Accessor for the srsDimension. May return null if not set.
	 */
	public Integer getSrsDimension() {
		return (_cachedSrsDimension);
	}
	
	/**
	 * Accessor for the axisLabels. Will return an empty list if not set.
	 */
	public List<String> getAxisLabels() {
		return (Collections.unmodifiableList(_cachedAxisLabels));
	}
	
	/**
	 * Accessor for the String representation of the axisLabels
	 */
	public String getAxisLabelsAsXsList() {
		return (Util.getXsList(getAxisLabels()));
	}
	
	/**
	 * Accessor for the uomLabels. Will return an empty list if not set.
	 */
	public List<String> getUomLabels() {
		return (Collections.unmodifiableList(_cachedUomLabels));
	}
	
	/**
	 * Accessor for the String representation of the uomLabels
	 */
	public String getUomLabelsAsXsList() {
		return (Util.getXsList(getUomLabels()));
	}
	
	/**
	 * Builder for these attributes.
	 * 
	 * <p>This class does not implement the IBuilder interface, because the behavior of commit() is at odds with the
	 * standard commit() method. As an attribute group, an empty attribute group will always be returned instead of
	 * null.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements Serializable {
		private static final long serialVersionUID = 6071979027185230870L;
		private String _srsName;
		private Integer _srsDimension;
		private List<String> _axisLabels;
		private List<String> _uomLabels;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(SRSAttributes attributes) {
			setSrsName(attributes.getSrsName());
			setSrsDimension(attributes.getSrsDimension());
			setAxisLabels(attributes.getAxisLabels());
			setUomLabels(attributes.getUomLabels());
		}
		
		/**
		 * Finalizes the data gathered for this builder instance. Will always return an empty instance instead of
		 * a null one.
		 * 
		 * @throws InvalidDDMSException if any required information is missing or malformed
		 */
		public SRSAttributes commit() throws InvalidDDMSException {
			return (new SRSAttributes(getSrsName(), getSrsDimension(), getAxisLabels(), getUomLabels()));
		}
		
		/**
		 * Checks if any values have been provided for this Builder.
		 * 
		 * @return true if every field is empty
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getSrsName()) && getSrsDimension() == null
				&& Util.containsOnlyEmptyValues(getAxisLabels())
				&& Util.containsOnlyEmptyValues(getUomLabels()));				
		}
		
		/**
		 * Builder accessor for the srsName
		 */
		public String getSrsName() {
			return _srsName;
		}
		
		/**
		 * Builder accessor for the srsName
		 */
		public void setSrsName(String srsName) {
			_srsName = srsName;
		}
		
		/**
		 * Builder accessor for the srsDimension
		 */
		public Integer getSrsDimension() {
			return _srsDimension;
		}
		
		/**
		 * Builder accessor for the srsDimension
		 */
		public void setSrsDimension(Integer srsDimension) {
			_srsDimension = srsDimension;
		}
		
		/**
		 * Builder accessor for the axisLabels
		 */
		public List<String> getAxisLabels() {
			if (_axisLabels == null)
				_axisLabels = new LazyList(String.class);
			return _axisLabels;
		}
		
		/**
		 * Builder accessor for the axisLabels
		 */
		public void setAxisLabels(List<String> axisLabels) {
			_axisLabels = new LazyList(axisLabels, String.class);
		}
		
		/**
		 * Builder accessor for the uomLabels
		 */
		public List<String> getUomLabels() {
			if (_uomLabels == null)
				_uomLabels = new LazyList(String.class);
			return _uomLabels;
		}
		
		/**
		 * Builder accessor for the uomLabels
		 */
		public void setUomLabels(List<String> uomLabels) {
			_uomLabels = new LazyList(uomLabels, String.class);
		}
	}
}