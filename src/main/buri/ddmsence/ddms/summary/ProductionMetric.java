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
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:productionMetric.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The subject and coverage attributes must be non-empty.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:subject</u>: A method of categorizing the subject of a document in a fashion understandable by DDNI-A. (required)<br />
 * <u>ddms:coverage</u>: A method of categorizing the coverage of a document in a fashion understandable by DDNI-A (required)<br />
 * This class is also decorated with ISM {@link SecurityAttributes}, starting in DDMS 4.0. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Description</u>: A categorization scheme whose values and use are defined by DDNI-A.<br />
 * <u>Obligation</u>: Optional in a SubjectCoverage.<br />
 * <u>Schema Modification Date</u>: 2011-08-31<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class ProductionMetric extends AbstractBaseComponent {

	private SecurityAttributes _cachedSecurityAttributes = null;
	
	private static final String SUBJECT_NAME = "subject";
	private static final String COVERAGE_NAME = "coverage";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ProductionMetric(Element element) throws InvalidDDMSException {
		try {
			_cachedSecurityAttributes = new SecurityAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data.
	 *  
	 * @param subject a method of categorizing the subject of a document in a fashion understandable by DDNI-A (required)
	 * @param coverage a method of categorizing the coverage of a document in a fashion understandable by DDNI-A (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ProductionMetric(String subject, String coverage, SecurityAttributes securityAttributes)
		throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(ProductionMetric.getName(DDMSVersion.getCurrentVersion()), null);
			Util.addDDMSAttribute(element, SUBJECT_NAME, subject);
			Util.addDDMSAttribute(element, COVERAGE_NAME, coverage);
			_cachedSecurityAttributes = (securityAttributes == null ? new SecurityAttributes(null, null, null)
				: securityAttributes);
			_cachedSecurityAttributes.addTo(element);
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
	 * <li>A subject exists and is not empty.</li>
	 * <li>A coverage exists and is not empty.</li>
	 * <li>This component cannot be used until DDMS 4.0 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), ProductionMetric.getName(getDDMSVersion()));
		Util.requireDDMSValue("subject attribute", getSubject());
		Util.requireDDMSValue("coverage attribute", getCoverage());
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0");
		
		super.validate();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String)
	 */
	public String getOutput(boolean isHTML, String prefix) {
		prefix = Util.getNonNullString(prefix) + getName() + ".";
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, prefix + SUBJECT_NAME, getSubject(), true));
		text.append(buildOutput(isHTML, prefix + COVERAGE_NAME, getCoverage(), true));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix));
		return (text.toString());
	}
		
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof ProductionMetric))
			return (false);
		ProductionMetric test = (ProductionMetric) obj;
		return (getSubject().equals(test.getSubject()) 
			&& getCoverage().equals(test.getCoverage()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getSubject().hashCode();
		result = 7 * result + getCoverage().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("productionMetric");
	}
	
	/**
	 * Accessor for the subject attribute.
	 */
	public String getSubject() {
		return (getAttributeValue(SUBJECT_NAME));
	}
	
	/**
	 * Accessor for the coverage attribute.
	 */
	public String getCoverage() {
		return (getAttributeValue(COVERAGE_NAME));
	}
		
	/**
	 * Accessor for the Security Attributes. Will always be non-null, even if it has no values set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
		
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -9012648230977148516L;
		private String _subject;
		private String _coverage;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(ProductionMetric metric) {
			setSubject(metric.getSubject());
			setCoverage(metric.getCoverage());
			setSecurityAttributes(new SecurityAttributes.Builder(metric.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public ProductionMetric commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new ProductionMetric(getSubject(), getCoverage(), 
				getSecurityAttributes().commit()));
		}
		
		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getSubject()) && Util.isEmpty(getCoverage()) && getSecurityAttributes().isEmpty());
		}

		/**
		 * Builder accessor for the subject attribute
		 */
		public String getSubject() {
			return _subject;
		}

		/**
		 * Builder accessor for the subject attribute
		 */
		public void setSubject(String subject) {
			_subject = subject;
		}

		/**
		 * Builder accessor for the coverage attribute
		 */
		public String getCoverage() {
			return _coverage;
		}

		/**
		 * Builder accessor for the coverage attribute
		 */
		public void setCoverage(String coverage) {
			_coverage = coverage;
		}

		/**
		 * Builder accessor for the Security Attributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the Security Attributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}
	}
} 