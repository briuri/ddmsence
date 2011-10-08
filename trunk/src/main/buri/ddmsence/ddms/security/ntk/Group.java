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
package buri.ddmsence.ddms.security.ntk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.AbstractAccessEntity;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ntk:AccessGroup.
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ntk:AccessSystemName</u>: The system described by this access record (exactly 1 required), implemented as a 
 * {@link SystemName}<br />
 * <u>ntk:AccessGroupValue</u>: The value used to describe the group (1-to-many required), implemented as a 
 * {@link GroupValue}<br />
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are required.
 * </td></tr></table>
 * 
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class Group extends AbstractAccessEntity {
	
	private List<GroupValue> _groupValues = null;
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Group(Element element) throws InvalidDDMSException {
		super(element);
		try {
			Elements values = element.getChildElements(GroupValue.getName(getDDMSVersion()), getNamespace());
			_groupValues = new ArrayList<GroupValue>();
			for (int i = 0; i < values.size(); i++) {
				_groupValues.add(new GroupValue(values.get(i)));
			}			
			validate();
		}
		catch (InvalidDDMSException e) {
			e.setLocator(getQualifiedName());
			throw (e);
		}
	}
	
	/**
	 * Constructor for creating a component from raw data
	 *  
	 * @param systemName the system name (required)
	 * @param groupValues the list of values (at least 1 required)
	 * @param securityAttributes security attributes (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Group(SystemName systemName, List<GroupValue> groupValues,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		super(Group.getName(DDMSVersion.getCurrentVersion()), systemName, securityAttributes);
		try {
			if (groupValues == null)
				groupValues = Collections.emptyList();
			for (GroupValue value : groupValues) {
				getXOMElement().appendChild(value.getXOMElementCopy());
			}
			_groupValues = groupValues;
			validate();
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
	 * <li>At least 1 group value is required.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getNamespace(), Group.getName(getDDMSVersion()));
		if (getGroupValues().isEmpty())
			throw new InvalidDDMSException("At least one group value is required.");
		
		super.validate();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		prefix = Util.getNonNullString(prefix) + "group.";
		StringBuffer text = new StringBuffer();
		if (getSystemName() != null)
			text.append(getSystemName().getOutput(isHTML, prefix, ""));
		for (GroupValue value : getGroupValues())
			text.append(value.getOutput(isHTML, prefix, ""));
		text.append(getSecurityAttributes().getOutput(isHTML, prefix, ""));
		return (text.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = super.getNestedComponents();
		list.addAll(getGroupValues());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Group))
			return (false);
		return (true);		
	}

	/**
	 * Accessor for the element name of this component, based on the version of DDMS used
	 * 
	 * @param version the DDMSVersion
	 * @return an element name
	 */
	public static String getName(DDMSVersion version) {
		Util.requireValue("version", version);
		return ("AccessGroup");
	}

	/**
	 * Accessor for the list of group values (1-many)
	 */
	public List<GroupValue> getGroupValues() {
		return (Collections.unmodifiableList(_groupValues));
	}
			
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder extends AbstractAccessEntity.Builder {
		private static final long serialVersionUID = 7851044806424206976L;
		private List<GroupValue.Builder> _groupValues;
		
		/**
		 * Empty constructor
		 */
		public Builder() {
			super();			
		}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Group group) {
			super(group);
			for (GroupValue value : group.getGroupValues())
				getGroupValues().add(new GroupValue.Builder(value));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Group commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<GroupValue> values = new ArrayList<GroupValue>();
			for (IBuilder builder : getGroupValues()) {
				GroupValue component = (GroupValue) builder.commit();
				if (component != null)
					values.add(component);
			}
			return (new Group(getSystemName().commit(), values, getSecurityAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getGroupValues())
				hasValueInList = hasValueInList || !builder.isEmpty();
			return (!hasValueInList && super.isEmpty());
		}

		/**
		 * Builder accessor for the values
		 */
		public List<GroupValue.Builder> getGroupValues() {
			if (_groupValues == null)
				_groupValues = new LazyList(GroupValue.Builder.class);
			return _groupValues;
		}
	}
} 