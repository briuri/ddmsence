/* Copyright 2010 - 2013 by Brian Uri!
   
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
package buri.ddmsence.ddms.resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.ApproximableDate;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:dates.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>A dates element can be used with none of the seven date values set.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ddms:acquiredOn</u>: the acquisition date (0-many, starting in DDMS 4.1), implemented as an {@link ApproximableDate}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:created</u>: creation date (optional)<br />
 * <u>ddms:posted</u>: posting date (optional)<br />
 * <u>ddms:validTil</u>: expiration date (optional)<br />
 * <u>ddms:infoCutOff</u>: info cutoff date (optional)<br />
 * <u>ddms:approvedOn</u>: approved for posting date (optional, starting in DDMS 3.1)<br />
 * <u>ddms:receivedOn</u>: received date (optional, starting in DDMS 4.0.1)<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 0.9.b
 */
public final class Dates extends AbstractBaseComponent {

	private List<ApproximableDate> _acquiredOns = null;
	
	private static final String CREATED_NAME = "created";
	private static final String POSTED_NAME = "posted";
	private static final String VALID_TIL_NAME = "validTil";
	private static final String INFO_CUT_OFF_NAME = "infoCutOff";
	private static final String APPROVED_ON_NAME = "approvedOn";
	private static final String RECEIVED_ON_NAME = "receivedOn";
	private static final String ACQUIRED_ON_NAME = "acquiredOn";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Dates(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			_acquiredOns = new ArrayList<ApproximableDate>();
			Elements acquiredOns = element.getChildElements(ACQUIRED_ON_NAME, getNamespace());
			for (int i = 0; i < acquiredOns.size(); i++) {
				_acquiredOns.add(new ApproximableDate(acquiredOns.get(i)));
			}
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data. 
	 * 
	 * @deprecated A new constructor was added for DDMS 4.1 to support ddms:acquiredOn. This constructor is preserved for 
	 * backwards compatibility, but may disappear in the next major release.
	 * 
	 * @param created the creation date (optional)
	 * @param posted the posting date (optional)
	 * @param validTil the expiration date (optional)
	 * @param infoCutOff the info cutoff date (optional)
	 * @param approvedOn the approved on date (optional, starting in DDMS 3.1)
	 * @param receivedOn the received on date (optional, starting in DDMS 4.0.1)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Dates(String created, String posted, String validTil, String infoCutOff, String approvedOn,
		String receivedOn) throws InvalidDDMSException {
		this(null, created, posted, validTil, infoCutOff, approvedOn, receivedOn);
	}
	
	/**
	 * Constructor for creating a component from raw data. 
	 * 
	 * @param acquiredOns the acquisition dates (optional, starting in DDMS 4.1)
	 * @param created the creation date (optional)
	 * @param posted the posting date (optional)
	 * @param validTil the expiration date (optional)
	 * @param infoCutOff the info cutoff date (optional)
	 * @param approvedOn the approved on date (optional, starting in DDMS 3.1)
	 * @param receivedOn the received on date (optional, starting in DDMS 4.0.1)
	 * 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Dates(List<ApproximableDate> acquiredOns, String created, String posted, String validTil, String infoCutOff,
		String approvedOn, String receivedOn) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(Dates.getName(DDMSVersion.getCurrentVersion()), null);
			if (acquiredOns == null)
				acquiredOns = Collections.emptyList();
			_acquiredOns = acquiredOns;
			for (ApproximableDate acquiredOn : acquiredOns)
				element.appendChild(acquiredOn.getXOMElementCopy());

			Util.addDDMSAttribute(element, CREATED_NAME, created);
			Util.addDDMSAttribute(element, POSTED_NAME, posted);
			Util.addDDMSAttribute(element, VALID_TIL_NAME, validTil);
			Util.addDDMSAttribute(element, INFO_CUT_OFF_NAME, infoCutOff);
			Util.addDDMSAttribute(element, APPROVED_ON_NAME, approvedOn);
			Util.addDDMSAttribute(element, RECEIVED_ON_NAME, receivedOn);
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Validates the component.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>The qualified name of the element is correct.</li>
	 * <li>If set, each date attribute adheres to an acceptable date format.</li>
	 * <li>The approvedOn date cannot be used until DDMS 3.1 or later.</li>
	 * <li>The receivedOn date cannot be used until DDMS 4.0.1 or later.</li>
	 * <li>An acquiredOn date cannot be used until DDMS 4.1 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), Dates.getName(getDDMSVersion()));
		if (!Util.isEmpty(getCreatedString()))
			Util.requireDDMSDateFormat(getCreatedString(), getNamespace());
		if (!Util.isEmpty(getPostedString()))
			Util.requireDDMSDateFormat(getPostedString(), getNamespace());
		if (!Util.isEmpty(getValidTilString()))
			Util.requireDDMSDateFormat(getValidTilString(), getNamespace());
		if (!Util.isEmpty(getInfoCutOffString())) 
			Util.requireDDMSDateFormat(getInfoCutOffString(), getNamespace());
		if (!Util.isEmpty(getApprovedOnString()))
			Util.requireDDMSDateFormat(getApprovedOnString(), getNamespace());
		if (!Util.isEmpty(getReceivedOnString()))
			Util.requireDDMSDateFormat(getReceivedOnString(), getNamespace());
		
		// Should be reviewed as additional versions of DDMS are supported.
		if (!getDDMSVersion().isAtLeast("3.1") && !Util.isEmpty(getApprovedOnString())) {
			throw new InvalidDDMSException("This component cannot have an approvedOn date until DDMS 3.1 or later.");
		}
		if (!getDDMSVersion().isAtLeast("4.0.1") && !Util.isEmpty(getReceivedOnString())) {
			throw new InvalidDDMSException("This component cannot have a receivedOn date until DDMS 4.0.1 or later.");
		}
		if (!getDDMSVersion().isAtLeast("4.1") && !getAcquiredOns().isEmpty()) {
			throw new InvalidDDMSException("This component cannot have an acquiredOn date until DDMS 4.1 or later.");
		}

		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>A completely empty ddms:dates element was found.</li>
	 * <li>A ddms:acquiredOn element may cause issues for DDMS 4.0 records.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (Util.isEmpty(getCreatedString())
			&& Util.isEmpty(getPostedString())
			&& Util.isEmpty(getValidTilString())
			&& Util.isEmpty(getInfoCutOffString())
			&& Util.isEmpty(getApprovedOnString())
			&& Util.isEmpty(getReceivedOnString())
			&& getAcquiredOns().isEmpty()) {
			addWarning("A completely empty ddms:dates element was found.");
		}
		if (!getAcquiredOns().isEmpty())
			addDdms40Warning("ddms:acquiredOn element");
		
		super.validateWarnings();
	}
	
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix + ".");
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix, getAcquiredOns()));
		text.append(buildOutput(isHTML, localPrefix + CREATED_NAME, getCreatedString()));
		text.append(buildOutput(isHTML, localPrefix + POSTED_NAME, getPostedString()));
		text.append(buildOutput(isHTML, localPrefix + VALID_TIL_NAME, getValidTilString()));
		text.append(buildOutput(isHTML, localPrefix + INFO_CUT_OFF_NAME, getInfoCutOffString()));
		text.append(buildOutput(isHTML, localPrefix + APPROVED_ON_NAME, getApprovedOnString()));
		text.append(buildOutput(isHTML, localPrefix + RECEIVED_ON_NAME, getReceivedOnString()));
		return (text.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.addAll(getAcquiredOns());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Dates))
			return (false);
		Dates test = (Dates) obj;
		return (getCreatedString().equals(test.getCreatedString())
			&& getPostedString().equals(test.getPostedString())
			&& getValidTilString().equals(test.getValidTilString())
			&& getInfoCutOffString().equals(test.getInfoCutOffString())
			&& getApprovedOnString().equals(test.getApprovedOnString())
			&& getReceivedOnString().equals(test.getReceivedOnString()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getCreatedString().hashCode();
		result = 7 * result + getPostedString().hashCode();
		result = 7 * result + getValidTilString().hashCode();
		result = 7 * result + getInfoCutOffString().hashCode();
		result = 7 * result + getApprovedOnString().hashCode();
		result = 7 * result + getReceivedOnString().hashCode();
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
		return ("dates");
	}
	
	/**
	 * Accessor for the created date (optional). Returns a copy.
	 * 
	 * @deprecated Because DDMS 4.1 added a new allowable date format (ddms:DateHourMinType),
	 * XMLGregorianCalendar is no longer a sufficient representation. This accessor will return
	 * null for dates in the new format. Use <code>getCreatedString()</code> to
	 * access the raw XML format of the date instead.
	 */
	public XMLGregorianCalendar getCreated() {
		try {
			return (getFactory().newXMLGregorianCalendar(getCreatedString()));
		}
		catch (IllegalArgumentException e) {
			return (null);
		}
	}

	/**
	 * Accessor for the created date (optional).
	 */
	public String getCreatedString() {
		return (getAttributeValue(CREATED_NAME));
	}
	
	/**
	 * Accessor for the posted date (optional). Returns a copy.
	 * 
	 * @deprecated Because DDMS 4.1 added a new allowable date format (ddms:DateHourMinType),
	 * XMLGregorianCalendar is no longer a sufficient representation. This accessor will return
	 * null for dates in the new format. Use <code>getPostedString()</code> to
	 * access the raw XML format of the date instead.
	 */
	public XMLGregorianCalendar getPosted() {
		try {
			return (getFactory().newXMLGregorianCalendar(getPostedString()));
		}
		catch (IllegalArgumentException e) {
			return (null);
		}
	}
	
	/**
	 * Accessor for the posted date (optional).
	 */
	public String getPostedString() {
		return (getAttributeValue(POSTED_NAME));
	}

	/**
	 * Accessor for the expiration date (optional). Returns a copy.
	 * 
	 * @deprecated Because DDMS 4.1 added a new allowable date format (ddms:DateHourMinType),
	 * XMLGregorianCalendar is no longer a sufficient representation. This accessor will return
	 * null for dates in the new format. Use <code>getValidTilString()</code> to
	 * access the raw XML format of the date instead.
	 */
	public XMLGregorianCalendar getValidTil() {
		try {
			return (getFactory().newXMLGregorianCalendar(getValidTilString()));
		}
		catch (IllegalArgumentException e) {
			return (null);
		}
	}
	
	/**
	 * Accessor for the expiration date (optional).
	 */
	public String getValidTilString() {
		return (getAttributeValue(VALID_TIL_NAME));
	}

	/**
	 * Accessor for the cutoff date (optional). Returns a copy.
	 * 
	 * @deprecated Because DDMS 4.1 added a new allowable date format (ddms:DateHourMinType),
	 * XMLGregorianCalendar is no longer a sufficient representation. This accessor will return
	 * null for dates in the new format. Use <code>getInfoCutOffString()</code> to
	 * access the raw XML format of the date instead.
	 */
	public XMLGregorianCalendar getInfoCutOff() {
		try {
			return (getFactory().newXMLGregorianCalendar(getInfoCutOffString()));
		}
		catch (IllegalArgumentException e) {
			return (null);
		}
	}
	
	/**
	 * Accessor for the cutoff date (optional).
	 */
	public String getInfoCutOffString() {
		return (getAttributeValue(INFO_CUT_OFF_NAME));
	}
	
	/**
	 * Accessor for the approved on date (optional). Returns a copy.
	 * 
	 * @deprecated Because DDMS 4.1 added a new allowable date format (ddms:DateHourMinType),
	 * XMLGregorianCalendar is no longer a sufficient representation. This accessor will return
	 * null for dates in the new format. Use <code>getApprovedOnString()</code> to
	 * access the raw XML format of the date instead.
	 */
	public XMLGregorianCalendar getApprovedOn() {
		try {
			return (getFactory().newXMLGregorianCalendar(getApprovedOnString()));
		}
		catch (IllegalArgumentException e) {
			return (null);
		}
	}

	/**
	 * Accessor for the approved on date (optional).
	 */
	public String getApprovedOnString() {
		return (getAttributeValue(APPROVED_ON_NAME));
	}
	
	/**
	 * Accessor for the received on date (optional). Returns a copy.
	 * 
	 * @deprecated Because DDMS 4.1 added a new allowable date format (ddms:DateHourMinType),
	 * XMLGregorianCalendar is no longer a sufficient representation. This accessor will return
	 * null for dates in the new format. Use <code>getReceivedOnString()</code> to
	 * access the raw XML format of the date instead.
	 */
	public XMLGregorianCalendar getReceivedOn() {
		try {
			return (getFactory().newXMLGregorianCalendar(getReceivedOnString()));
		}
		catch (IllegalArgumentException e) {
			return (null);
		}
	}
	
	/**
	 * Accessor for the received on date (optional).
	 */
	public String getReceivedOnString() {
		return (getAttributeValue(RECEIVED_ON_NAME));
	}
	
	/**
	 * Accessor for the acquiredOn dates (0-many optional). 
	 */
	public List<ApproximableDate> getAcquiredOns() {
		return (Collections.unmodifiableList(_acquiredOns));
	}
	
	/**
	 * Accesor for the datatype factory
	 */
	private static DatatypeFactory getFactory() {
		return (Util.getDataTypeFactory());
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = -2857638896738260719L;
		private List<ApproximableDate.Builder> _acquiredOns;
		private String _created;
		private String _posted;
		private String _validTil;
		private String _infoCutOff;
		private String _approvedOn;
		private String _receivedOn;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Dates dates) {
			for (ApproximableDate acquiredOn : dates.getAcquiredOns())
				getAcquiredOns().add(new ApproximableDate.Builder(acquiredOn));
			setCreated(dates.getCreatedString());
			setPosted(dates.getPostedString());
			setValidTil(dates.getValidTilString());
			setInfoCutOff(dates.getInfoCutOffString());
			setApprovedOn(dates.getApprovedOnString());
			setReceivedOn(dates.getReceivedOnString());
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Dates commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<ApproximableDate> acquiredOns = new ArrayList<ApproximableDate>();
			for (IBuilder builder : getAcquiredOns()) {
				ApproximableDate component = (ApproximableDate) builder.commit();
				if (component != null)
					acquiredOns.add(component);
			}
			return (new Dates(acquiredOns, getCreated(), getPosted(), getValidTil(),
				getInfoCutOff(), getApprovedOn(), getReceivedOn()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getAcquiredOns())
				hasValueInList = hasValueInList || !builder.isEmpty();
			return (!hasValueInList && Util.isEmpty(getCreated()) && Util.isEmpty(getPosted())
				&& Util.isEmpty(getValidTil()) && Util.isEmpty(getInfoCutOff()) && Util.isEmpty(getApprovedOn()) && Util
				.isEmpty(getReceivedOn()));
		}
		
		/**
		 * Builder accessor for the acquiredOn dates
		 */
		public List<ApproximableDate.Builder> getAcquiredOns() {
			if (_acquiredOns == null)
				_acquiredOns = new LazyList(ApproximableDate.Builder.class);
			return _acquiredOns;
		}
		
		/**
		 * Builder accessor for the created date.
		 */
		public String getCreated() {
			return _created;
		}

		/**
		 * Builder accessor for the created date.
		 */
		public void setCreated(String created) {
			_created = created;
		}

		/**
		 * Builder accessor for the posted date.
		 */
		public String getPosted() {
			return _posted;
		}

		/**
		 * Builder accessor for the posted date.
		 */
		public void setPosted(String posted) {
			_posted = posted;
		}

		/**
		 * Builder accessor for the validTil date.
		 */
		public String getValidTil() {
			return _validTil;
		}

		/**
		 * Builder accessor for the validTil date.
		 */
		public void setValidTil(String validTil) {
			_validTil = validTil;
		}

		/**
		 * Builder accessor for the infoCutOff date.
		 */
		public String getInfoCutOff() {
			return _infoCutOff;
		}

		/**
		 * Builder accessor for the infoCutOff date.
		 */
		public void setInfoCutOff(String infoCutOff) {
			_infoCutOff = infoCutOff;
		}

		/**
		 * Builder accessor for the approvedOn date.
		 */
		public String getApprovedOn() {
			return _approvedOn;
		}

		/**
		 * Builder accessor for the approvedOn date.
		 */
		public void setApprovedOn(String approvedOn) {
			_approvedOn = approvedOn;
		}

		/**
		 * Builder accessor for the receivedOn
		 */
		public String getReceivedOn() {
			return _receivedOn;
		}

		/**
		 * Builder accessor for the receivedOn
		 */
		public void setReceivedOn(String receivedOn) {
			_receivedOn = receivedOn;
		}
	}
}