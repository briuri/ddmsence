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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import nu.xom.Attribute;
import nu.xom.Element;
import buri.ddmsence.ddms.resource.Organization;
import buri.ddmsence.ddms.resource.Person;
import buri.ddmsence.ddms.resource.Service;
import buri.ddmsence.ddms.resource.Unknown;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.ddms.summary.Category;
import buri.ddmsence.ddms.summary.Keyword;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * Attribute group representing the xs:anyAttribute tag which appears on various DDMS components.
 * 
 * <p>In DDMS 3.0, this attribute group can decorate {@link Organization}, {@link Person}, {@link Service}, 
 * {@link Unknown}, {@link Keyword}, {@link Category}, or the {@link Resource} itself. In DDMS 2.0,
 * this attribute group can only decorate {@link Organization}, {@link Person}, {@link Service}, or
 * the {@link Resource}.</p>
 * 
 * <p>No validation or processing of any kind is performed by DDMSence on extensible attributes, other than the base
 * validation used when loading attributes from an XML file. This class merely exposes a <code>getAttributes()</code> 
 * method which returns a read-only List of XOM Attributes that can be manipulated in business-specific ways.</p>
 * 
 * <p>For example, this ddms:Keyword would have an ExtensibleAttributes instance containing 2 attributes (assuming
 * that the "opensearch" namespace was defined earlier in the file):</p>
 * 
 * <ul><code>
 * &lt;ddms:Keyword ddms:value="xml" opensearch:relevance="95" opensearch:confidence="82" /&gt;
 * </code></ul>
 * 
 * <p>XOM attributes can be created as follows:</p>
 * 
 * <ul><code>
 * Attribute attribute = new Attribute("opensearch:relevance", "http://opensearch.namespace/", "95");
 * Attribute attribute = new Attribute("opensearch:confidence", "http://opensearch.namespace/", "82");
 * </code></ul>
 * 
 * <p>The DDMS documentation does not provide sample HTML/Text output for extensible attributes, so the following
 * approach is used. In general, the HTML/Text output of extensible attributes will be prefixed with the name of the 
 * element being marked. For example:</p>
 * <ul><code>
 * Keyword Opensearch Relevance: 95<br />
 * Keyword Opensearch Confidence: 82<br />
 * &lt;meta name="subject.keyword.opensearch.relevance" content="95" /&gt;<br />
 * &lt;meta name="subject.keyword.opensearch.confidence" content="82" /&gt;<br />
 * </code></ul></p>
 * 
 * <p>Details about the XOM Attribute class can be found at:
 * <i>http://www.xom.nu/apidocs/index.html?nu/xom/Attribute.html</i></p>
 * 
 * @author Brian Uri!
 * @since 1.1.0
 */
public final class ExtensibleAttributes extends AbstractAttributeGroup {
	
	private List<Attribute> _cachedAttributes = null;

	private static final String ICISM_PREFIX = PropertyReader.getProperty("icism.prefix");
	
	private final Set<QName> RESERVED_RESOURCE_NAMES = new HashSet<QName>();
	
	/**
	 * Base constructor
	 * 
	 * <p>Will only load attributes from a different namespace than DDMS (##other)
	 * and will also skip any Resource attributes that are reserved.</p>
	 * 
	 * @param element the XOM element which is decorated with these attributes.
	 */
	public ExtensibleAttributes(Element element) throws InvalidDDMSException {
		super(DDMSVersion.getVersionForNamespace(element.getNamespaceURI()));
		buildReservedNames();
		_cachedAttributes = new ArrayList<Attribute>();
		for (int i = 0; i < element.getAttributeCount(); i++) {
			Attribute attribute = element.getAttribute(i);
			// Skip ddms: attributes.
			if (element.getNamespaceURI().equals(attribute.getNamespaceURI()))
				continue;
			// Skip reserved ICISM attributes
			if (element.getLocalName().equals(Resource.NAME)) {
				QName testName = new QName(attribute.getNamespaceURI(), attribute.getLocalName(), 
					attribute.getNamespacePrefix());
				if (RESERVED_RESOURCE_NAMES.contains(testName))
					continue;
			}
			_cachedAttributes.add(attribute);
		}
		validate();
	}
	
	/**
	 * Constructor which builds from raw data. Because the parent is not known at this time, will accept
	 * all attributes. The method, addTo() will confirm that the names do not clash with existing or reserved
	 * names on the element.
	 * 
	 * @param attributes a list of extensible attributes
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public ExtensibleAttributes(List<Attribute> attributes) throws InvalidDDMSException {
		super(DDMSVersion.getCurrentVersion());
		if (attributes == null)
			attributes = Collections.emptyList();
		_cachedAttributes = new ArrayList<Attribute>(attributes);
		validate();
	}
		
	/**
	 * Compiles lists of attribute names which should be ignored when creating extensible attributes. In most cases,
	 * this is easy to determine, because namespace="##other" forces all extensible attributes to be in a non-DDMS 
	 * namespace, so the Resource is the only element that might encounter collisions (it has ICISM attributes
	 * that should be ignored). The ICISM attributes are counted as extensible attributes in DDMS 2.0.
	 */
	private void buildReservedNames() {
		DDMSVersion version = DDMSVersion.getVersionFor(getDDMSVersion());
		String namespace = version.getIcismNamespace();
		RESERVED_RESOURCE_NAMES.clear();
		if ("3.0".equals(getDDMSVersion())) {
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, Resource.RESOURCE_ELEMENT_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, Resource.CREATE_DATE_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, Resource.DES_VERSION_NAME, ICISM_PREFIX));			
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.CLASSIFICATION_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.OWNER_PRODUCER_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.SCI_CONTROLS_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.SAR_IDENTIFIER_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.DISSEMINATION_CONTROLS_NAME, 
				ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.FGI_SOURCE_OPEN_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.FGI_SOURCE_PROTECTED_NAME, 
				ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.RELEASABLE_TO_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.NON_IC_MARKINGS_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.CLASSIFIED_BY_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.COMPILATION_REASON_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.DERIVATIVELY_CLASSIFIED_BY_NAME, 
				ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.CLASSIFICATION_REASON_NAME, 
				ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.DERIVED_FROM_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.DECLASS_DATE_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.DECLASS_EVENT_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.DECLASS_EXCEPTION_NAME, ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.TYPE_OF_EXEMPTED_SOURCE_NAME, 
				ICISM_PREFIX));
			RESERVED_RESOURCE_NAMES.add(new QName(namespace, SecurityAttributes.DATE_OF_EXEMPTED_SOURCE_NAME, 
				ICISM_PREFIX));
		}		
	}
	
	/**
	 * Convenience method to add these attributes onto an existing XOM Element
	 * 
	 * @param element the element to decorate
	 */
	public void addTo(Element element) throws InvalidDDMSException {
		DDMSVersion version = DDMSVersion.getVersionFor(getDDMSVersion());
		DDMSVersion elementVersion = DDMSVersion.getVersionForNamespace(element.getNamespaceURI());
		if (version != elementVersion) {
			throw new InvalidDDMSException("These extensible attributes cannot decorate a DDMS component with"
				+ " a different DDMS version.");
		}			
		
		for (Attribute attribute : getAttributes()) {
			if (element.getAttribute(attribute.getLocalName(), attribute.getNamespaceURI()) != null)
				throw new InvalidDDMSException("This element already has an attribute with the name, "
					+ attribute.getQualifiedName() + ".");
			element.addAttribute(attribute);
		}
	}

	/**
	 * Checks if any attributes have been set.
	 * 
	 * @return true if no attributes have values, false otherwise
	 */
	public boolean isEmpty() {
		return (getAttributes().isEmpty());
	}

	/**
	 * Currently, no further validation is performed, other than to confirm the DDMS Version on the underlying element.
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
	}
	
	/**
	 * Creates an HTML representation of these attributes.
	 * 
	 * @param prefix the parent prefix to place in each Meta tag
	 * @return a string representation of the HTML.
	 */
	public String toHTML(String prefix) {
		if (!Util.isEmpty(prefix))
			prefix = prefix + ".";
		StringBuffer html = new StringBuffer();
		for (Attribute attribute : getAttributes()) {
			html.append(Resource.buildHTMLMeta(prefix + attribute.getNamespacePrefix() + "." + attribute.getLocalName(), 
				attribute.getValue(), false));	
		}
		return (html.toString());
	}

	/**
	 * Creates a Text representation of these attributes.
	 * 
	 * @param prefix the parent prefix to place in each line of text
	 * @return a string representation of the Text
	 */
	public String toText(String prefix) {
		if (!Util.isEmpty(prefix))
			prefix = prefix + " ";
		StringBuffer text = new StringBuffer();
		for (Attribute attribute : getAttributes()) {
			text.append(Resource.buildTextLine(prefix + Util.capitalize(attribute.getNamespacePrefix()) + " "
				+ Util.capitalize(attribute.getLocalName()), attribute.getValue(), false));	
		}
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof ExtensibleAttributes))
			return (false);		
		ExtensibleAttributes test = (ExtensibleAttributes) obj;
		// XOM Attribute has no logical equality. Must compare by hand.
		if (getAttributes().size() != test.getAttributes().size())
			return (false);
		for (int i = 0; i < getAttributes().size(); i++) {
			Attribute attr1 = getAttributes().get(i);
			Attribute attr2 = test.getAttributes().get(i);
			if (!attr1.getLocalName().equals(attr2.getLocalName())
				|| !attr1.getNamespaceURI().equals(attr1.getNamespaceURI()))
				return (false);
		}
		return (true);
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		// XOM Attribute has no logical equality. Must calculate by hand.		
		for (Attribute attribute : getAttributes()) {
			result = 7 * result + attribute.getLocalName().hashCode();
			result = 7 * result + attribute.getNamespaceURI().hashCode();
		}
		return (result);
	}	
	
	/**
	 * Accessor for the attributes. Returns a copy.
	 */
	public List<Attribute> getAttributes() {
		return (Collections.unmodifiableList(_cachedAttributes));
	}

}