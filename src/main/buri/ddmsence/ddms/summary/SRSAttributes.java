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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import buri.ddmsence.ddms.AbstractAttributeGroup;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
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
		super(DDMSVersion.getVersionForGmlNamespace(element.getNamespaceURI()));
		_cachedSrsName = element.getAttributeValue(SRS_NAME_NAME, NO_NAMESPACE);
		String srsDimension = element.getAttributeValue(SRS_DIMENSION_NAME, NO_NAMESPACE);
		if (!Util.isEmpty(srsDimension)) {
			_cachedSrsDimension = Integer.valueOf(srsDimension);
		}
		String axisLabels = element.getAttributeValue(AXIS_LABELS_NAME, NO_NAMESPACE);
		_cachedAxisLabels = new ArrayList<String>();
		if (!Util.isEmpty(axisLabels)) {
			_cachedAxisLabels.addAll(Util.getAsList(axisLabels));
		}
		String uomLabels = element.getAttributeValue(UOM_LABELS_NAME, NO_NAMESPACE);
		_cachedUomLabels = new ArrayList<String>();
		if (!Util.isEmpty(uomLabels)) {
			_cachedUomLabels.addAll(Util.getAsList(uomLabels));
		}
		validate();
	}
	
	/**
	 * Constructor which builds from raw data.
	 * 
	 * @param srsName	the srsName (required if the name is not Position.NAME)
	 * @param srsDimension the srsDimension (optional)
	 * @param axisLabels the axis labels (optional, but should be omitted if no srsName is set)
	 * @param uomLabels the labels for UOM (required when axisLabels is set)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SRSAttributes(String srsName, Integer srsDimension, List<String> axisLabels, List<String> uomLabels)
		throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion());
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
	 */
	protected void addTo(Element element) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getVersionFor(getDDMSVersion());
		DDMSVersion elementVersion = DDMSVersion.getVersionForGmlNamespace(element.getNamespaceURI());
		if (version != elementVersion) {
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
		super.validate();
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
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof SRSAttributes))
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
		int result = super.hashCode(); 
		result = 7 * result + getSrsName().hashCode();
		if (getSrsDimension() != null)
			result = 7 * result + getSrsDimension().hashCode();
		result = 7 * result + getAxisLabels().hashCode();
		result = 7 * result + getUomLabels().hashCode();
		return (result);
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
}