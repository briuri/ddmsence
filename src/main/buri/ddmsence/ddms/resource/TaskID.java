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

import nu.xom.Element;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.ISMVocabulary;
import buri.ddmsence.ddms.summary.xlink.XLinkAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ddms:taskID.
 * 
 * <p>This element is not a global component, but is being implemented because it has attributes.</p>
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence is stricter than the specification in the following ways:</p>
 * <ul>
 * <li>The child text must not be empty. This rule is codified in the schema, starting in DDMS 5.0.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>ddms:taskingSystem</u>: the tasking system (optional)<br />
 * <u>network</u>: the name of the network, taken from a token list (optional)<br /> (becomes virt: in DDMS 5.0)
 * <u>otherNetwork</u>: an alternate network name (optional)<br /> (goes away in DDMS 5.0)
 * <u>{@link XLinkAttributes}</u>: If set, the xlink:type attribute must have a fixed value of "simple".<br />
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class TaskID extends AbstractBaseComponent {

	private XLinkAttributes _xlinkAttributes = null;

	private static final String FIXED_TYPE = "simple";

	/** The prefix of the network attributes */
	public static final String NO_PREFIX = "";

	/** The namespace of the network attributes */
	public static final String NO_NAMESPACE = "";

	private static final String NETWORK_NAME = "network";
	private static final String OTHER_NETWORK_NAME = "otherNetwork";
	private static final String TASKING_SYSTEM_NAME = "taskingSystem";

	/**
	 * Constructor for creating a component from a XOM Element
	 * 
	 * @param element the XOM element representing this
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public TaskID(Element element) throws InvalidDDMSException {
		try {
			_xlinkAttributes = new XLinkAttributes(element);
			setXOMElement(element, true);
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}

	/**
	 * Constructor for creating a component from raw data
	 * 
	 * @param value the child text (optional)
	 * @param taskingSystem the tasking system (optional)
	 * @param network the network (optional)
	 * @param otherNetwork another network (optional)
	 * @param xlinkAttributes simple xlink attributes
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public TaskID(String value, String taskingSystem, String network, String otherNetwork,
		XLinkAttributes xlinkAttributes) throws InvalidDDMSException {
		try {
			Element element = Util.buildDDMSElement(TaskID.getName(DDMSVersion.getCurrentVersion()), value);
			Util.addDDMSAttribute(element, TASKING_SYSTEM_NAME, taskingSystem);
			if (DDMSVersion.getCurrentVersion().isAtLeast("5.0")) {
				String virtPrefix = PropertyReader.getPrefix("virt");
				String virtNamespace = DDMSVersion.getCurrentVersion().getVirtNamespace();
				Util.addAttribute(element, virtPrefix, NETWORK_NAME, virtNamespace, network);
			}
			else
				Util.addAttribute(element, NO_PREFIX, NETWORK_NAME, NO_NAMESPACE, network);
			Util.addAttribute(element, NO_PREFIX, OTHER_NETWORK_NAME, NO_NAMESPACE, otherNetwork);

			_xlinkAttributes = XLinkAttributes.getNonNullInstance(xlinkAttributes);
			_xlinkAttributes.addTo(element);
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
	 * <li>A child text value is required.</li>
	 * <li>If set, the xlink:type attribute has a value of "simple".</li>
	 * <li>If set, the network attribute must be a valid network token.</li>
	 * <li>The otherNetwork cannot be used after DDMS 4.1.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireDDMSQName(getXOMElement(), TaskID.getName(getDDMSVersion()));
		Util.requireDDMSValue("value", getValue());
		if (!Util.isEmpty(getXLinkAttributes().getType()) && !getXLinkAttributes().getType().equals(FIXED_TYPE))
			throw new InvalidDDMSException("The type attribute must have a fixed value of \"" + FIXED_TYPE + "\".");
		if (!Util.isEmpty(getNetwork()))
			ISMVocabulary.requireValidNetwork(getNetwork());
		if (getDDMSVersion().isAtLeast("5.0") && !Util.isEmpty(getOtherNetwork()))
			throw new InvalidDDMSException("The otherNetwork attribute cannot be used after DDMS 4.1.");
		super.validate();
	}

	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>Include any warnings from the XLink attributes.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (getXLinkAttributes() != null)
			addWarnings(getXLinkAttributes().getValidationWarnings(), true);
		super.validateWarnings();
	}

	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, getName(), suffix);
		StringBuffer text = new StringBuffer();
		text.append(buildOutput(isHTML, localPrefix, getValue()));
		text.append(buildOutput(isHTML, localPrefix + "." + TASKING_SYSTEM_NAME, getTaskingSystem()));
		text.append(buildOutput(isHTML, localPrefix + "." + NETWORK_NAME, getNetwork()));
		text.append(buildOutput(isHTML, localPrefix + "." + OTHER_NETWORK_NAME, getOtherNetwork()));
		text.append(getXLinkAttributes().getOutput(isHTML, localPrefix + "."));
		return (text.toString());
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof TaskID))
			return (false);
		TaskID test = (TaskID) obj;
		return (getValue().equals(test.getValue())
			&& getTaskingSystem().equals(test.getTaskingSystem()) 
			&& getNetwork().equals(test.getNetwork())
			&& getOtherNetwork().equals(test.getOtherNetwork()) 
			&& getXLinkAttributes().equals(test.getXLinkAttributes()));
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		int result = super.hashCode();
		result = 7 * result + getValue().hashCode();
		result = 7 * result + getTaskingSystem().hashCode();
		result = 7 * result + getNetwork().hashCode();
		result = 7 * result + getOtherNetwork().hashCode();
		result = 7 * result + getXLinkAttributes().hashCode();
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
		return ("taskID");
	}

	/**
	 * Accessor for the value of the child text.
	 */
	public String getValue() {
		return (getXOMElement().getValue());
	}

	/**
	 * Accessor for the taskingSystem attribute.
	 */
	public String getTaskingSystem() {
		return (getAttributeValue(TASKING_SYSTEM_NAME));
	}

	/**
	 * Accessor for the network attribute.
	 */
	public String getNetwork() {
		String namespace = getDDMSVersion().isAtLeast("5.0") ? getDDMSVersion().getVirtNamespace() : NO_NAMESPACE;
		return (getAttributeValue(NETWORK_NAME, namespace));
	}

	/**
	 * Accessor for the otherNetwork attribute.
	 */
	public String getOtherNetwork() {
		return (getAttributeValue(OTHER_NETWORK_NAME, NO_NAMESPACE));
	}

	/**
	 * Accessor for the XLink Attributes. Will always be non-null, even if it has no values set.
	 */
	public XLinkAttributes getXLinkAttributes() {
		return (_xlinkAttributes);
	}

	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 1.8.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 4325950371570699184L;
		private String _value;
		private String _taskingSystem;
		private String _network;
		private String _otherNetwork;
		private XLinkAttributes.Builder _xlinkAttributes;

		/**
		 * Empty constructor
		 */
		public Builder() {}

		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(TaskID taskID) {
			setValue(taskID.getValue());
			setTaskingSystem(taskID.getTaskingSystem());
			setNetwork(taskID.getNetwork());
			setOtherNetwork(taskID.getOtherNetwork());
			setXLinkAttributes(new XLinkAttributes.Builder(taskID.getXLinkAttributes()));
		}

		/**
		 * @see IBuilder#commit()
		 */
		public TaskID commit() throws InvalidDDMSException {
			return (isEmpty() ? null : new TaskID(getValue(), getTaskingSystem(), getNetwork(), getOtherNetwork(),
				getXLinkAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			return (Util.isEmpty(getValue())
				&& Util.isEmpty(getTaskingSystem())
				&& Util.isEmpty(getNetwork())
				&& Util.isEmpty(getOtherNetwork())
				&& getXLinkAttributes().isEmpty());				
		}

		/**
		 * Builder accessor for the value
		 */
		public String getValue() {
			return _value;
		}

		/**
		 * Builder accessor for the value
		 */
		public void setValue(String value) {
			_value = value;
		}

		/**
		 * Builder accessor for the taskingSystem
		 */
		public String getTaskingSystem() {
			return _taskingSystem;
		}

		/**
		 * Builder accessor for the taskingSystem
		 */
		public void setTaskingSystem(String taskingSystem) {
			_taskingSystem = taskingSystem;
		}

		/**
		 * Builder accessor for the network
		 */
		public String getNetwork() {
			return _network;
		}

		/**
		 * Builder accessor for the network
		 */
		public void setNetwork(String network) {
			_network = network;
		}

		/**
		 * Builder accessor for the otherNetwork
		 */
		public String getOtherNetwork() {
			return _otherNetwork;
		}

		/**
		 * Builder accessor for the otherNetwork
		 */
		public void setOtherNetwork(String otherNetwork) {
			_otherNetwork = otherNetwork;
		}

		/**
		 * Builder accessor for the XLink Attributes
		 */
		public XLinkAttributes.Builder getXLinkAttributes() {
			if (_xlinkAttributes == null)
				_xlinkAttributes = new XLinkAttributes.Builder();
			return _xlinkAttributes;
		}

		/**
		 * Builder accessor for the XLink Attributes
		 */
		public void setXLinkAttributes(XLinkAttributes.Builder xlinkAttributes) {
			_xlinkAttributes = xlinkAttributes;
		}
	}
}