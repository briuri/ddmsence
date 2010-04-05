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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.ddms.AbstractBaseComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.ValidationMessage;
import buri.ddmsence.ddms.security.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:subjectCoverage.
 * 
 * <p>
 * A subjectCoverage element contains a locally defined Subject construct. This construct is a container for the
 * keywords and categories of a resource. It exists only inside of a ddms:format parent, so it is not implemented as a
 * Java object.
 * </p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>Duplicate keywords or categories can be used.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:category</u>: a category (0-many optional), implemented as a {@link Category}<br />
 * <u>ddms:keyword</u>: a keyword (0-many optional), implemented as a {@link Keyword}<br />
 * <p>At least 1 of category or keyword must be used.</p>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * This class is decorated with ICISM {@link SecurityAttributes}, starting in DDMS v3.0. The classification and
 * ownerProducer attributes are optional.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>DDMS Information</th></tr><tr><td class="infoBody">
 * <u>Link</u>: https://metadata.dod.mil/mdr/irs/DDMS/ddms_categories.htm#Subject<br />
 * <u>Description</u>: Subject keyword(s)/categories that characterize the subject matter of a resource.<br />
 * <u>Obligation</u>: Mandatory<br />
 * <u>Schema Modification Date</u>: 2010-01-26<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class SubjectCoverage extends AbstractBaseComponent {
	
	// Values are cached upon instantiation, so XOM elements do not have to be traversed when calling getters.
	private List<Keyword> _cachedKeywords;
	private List<Category> _cachedCategories;
	private SecurityAttributes _cachedSecurityAttributes = null;
	
	/** The element name of this component */
	public static final String NAME = "subjectCoverage";
	
	private static final String SUBJECT_NAME = "Subject";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SubjectCoverage(Element element) throws InvalidDDMSException {
		try {
			Util.requireDDMSValue("subjectCoverage element", element);
			Element subjectElement = element.getFirstChildElement(SUBJECT_NAME, element.getNamespaceURI());
			_cachedKeywords = new ArrayList<Keyword>();
			_cachedCategories = new ArrayList<Category>();
			if (subjectElement != null) {
				Elements keywords = subjectElement.getChildElements(Keyword.NAME, subjectElement.getNamespaceURI());
				for (int i = 0; i < keywords.size(); i++) {
					_cachedKeywords.add(new Keyword(keywords.get(i)));
				}
				Elements categories = subjectElement.getChildElements(Category.NAME, subjectElement.getNamespaceURI());
				for (int i = 0; i < categories.size(); i++) {
					_cachedCategories.add(new Category(categories.get(i)));
				}
			}
			_cachedSecurityAttributes = new SecurityAttributes(element);
			setXOMElement(element, true);
		} catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param keywords list of keywords
	 * @param categories list of categories
	 * @param securityAttributes any security attributes (optional)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public SubjectCoverage(List<Keyword> keywords, List<Category> categories, SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			if (keywords == null)
				keywords = Collections.emptyList();
			if (categories == null)
				categories = Collections.emptyList();
			Element subjectElement = Util.buildDDMSElement(SUBJECT_NAME, null);
			for (Keyword keyword : keywords) {
				subjectElement.appendChild(keyword.getXOMElementCopy());
			}
			for (Category category : categories) {
				subjectElement.appendChild(category.getXOMElementCopy());
			}
			Element element = Util.buildDDMSElement(SubjectCoverage.NAME, null);
			element.appendChild(subjectElement);

			_cachedKeywords = keywords;
			_cachedCategories = categories;
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
	 * <li>At least 1 of "Keyword" or "Category" must exist.</li>
	 * <li>The SecurityAttributes do not exist in DDMS v2.0.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		super.validate();
		Util.requireDDMSQName(getXOMElement(), DDMS_PREFIX, NAME);
		Element subjectElement = getChild(SUBJECT_NAME);
		Util.requireDDMSValue("Subject element", subjectElement);
		int count = subjectElement.getChildElements(Keyword.NAME, subjectElement.getNamespaceURI()).size()
			+ subjectElement.getChildElements(Category.NAME, subjectElement.getNamespaceURI()).size();
		if (count < 1)
			throw new InvalidDDMSException("At least 1 keyword or category must exist.");
		
		Set<Keyword> uniqueKeywords = new HashSet<Keyword>(getKeywords());
		if (uniqueKeywords.size() != getKeywords().size())
			addWarning("1 or more keywords have the same value.");
		Set<Category> uniqueCategories = new HashSet<Category>(getCategories());
		if (uniqueCategories.size() != getCategories().size())
			addWarning("1 or more categories have the same value.");
				
		for (Keyword keyword : getKeywords()) {
			Util.requireSameVersion(this, keyword);
			addWarnings(keyword.getValidationWarnings(), false);
		}
		for (Category category : getCategories()) {
			Util.requireSameVersion(this, category);
			addWarnings(category.getValidationWarnings(), false);
		}
		if (DDMSVersion.isVersion("2.0", getXOMElement()) && !getSecurityAttributes().isEmpty()) {
			throw new InvalidDDMSException("Security attributes can only be applied to this component in DDMS v3.0 or later.");
		}
		addWarnings(getSecurityAttributes().getValidationWarnings(), true);
	}
	
	/**
	 * @see AbstractBaseComponent#getLocatorSuffix()
	 */
	protected String getLocatorSuffix() {
		return (ValidationMessage.ELEMENT_PREFIX + DDMS_PREFIX + ":" + SUBJECT_NAME);
	}
	
	/**
	 * @see AbstractBaseComponent#toHTML()
	 */
	public String toHTML() {
		StringBuffer html = new StringBuffer();
		for (Keyword keyword : getKeywords())
			html.append(keyword.toHTML());
		for (Category category : getCategories())
			html.append(category.toHTML());
		html.append(getSecurityAttributes().toHTML("subject"));
		return (html.toString());

	}
	
	/**
	 * @see AbstractBaseComponent#toText()
	 */
	public String toText() {
		StringBuffer text = new StringBuffer();
		for (Keyword keyword : getKeywords())
			text.append(keyword.toText());
		for (Category category : getCategories())
			text.append(category.toText());
		text.append(getSecurityAttributes().toText("Subject"));
		return (text.toString());
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof SubjectCoverage))
			return (false);
		SubjectCoverage test = (SubjectCoverage) obj;
		return (Util.listEquals(getKeywords(), test.getKeywords()) &&
			Util.listEquals(getCategories(), test.getCategories()) &&
			getSecurityAttributes().equals(test.getSecurityAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getKeywords().hashCode();
		result = 7 * result + getCategories().hashCode();
		result = 7 * result + getSecurityAttributes().hashCode();
		return (result);
	}
	
	/**
	 * Accessor for the keywords (0 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<Keyword> getKeywords() {
		return (Collections.unmodifiableList(_cachedKeywords));
	}
	
	/**
	 * Accessor for the categories (0 to many).
	 * 
	 * @return unmodifiable List
	 */
	public List<Category> getCategories() {
		return (Collections.unmodifiableList(_cachedCategories));
	}
	
	/**
	 * Accessor for the Security Attributes. Will be null in DDMS 2.0, and non-null in DDMS 3.0.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_cachedSecurityAttributes);
	}
} 