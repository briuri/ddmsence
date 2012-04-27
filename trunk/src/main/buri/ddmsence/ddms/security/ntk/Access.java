/* Copyright 2010 - 2012 by Brian Uri!
   
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Element;
import nu.xom.Elements;
import buri.ddmsence.AbstractBaseComponent;
import buri.ddmsence.ddms.IBuilder;
import buri.ddmsence.ddms.IDDMSComponent;
import buri.ddmsence.ddms.InvalidDDMSException;
import buri.ddmsence.ddms.security.ism.SecurityAttributes;
import buri.ddmsence.util.DDMSVersion;
import buri.ddmsence.util.LazyList;
import buri.ddmsence.util.PropertyReader;
import buri.ddmsence.util.Util;

/**
 * An immutable implementation of ntk:Access.
 * 
 * <table class="info"><tr class="infoHeader"><th>Strictness</th></tr><tr><td class="infoBody">
 * <p>DDMSence allows the following legal, but nonsensical constructs:</p>
 * <ul>
 * <li>An Access element with no individual, group, or profile information can be used.</li>
 * </ul>
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Nested Elements</th></tr><tr><td class="infoBody">
 * <u>ntk:AccessIndividualList/ntk:AccessIndividual</u>: A list system access info for individuals, implemented 
 * as a list of {@link Individual}<br />
 * <u>ntk:AccessGroupList/ntk:AccessGroup</u>: A list system access info for groups, implemented as a list of 
 * {@link Group}<br />
 * <u>ntk:AccessProfileList</u>: A list system access info for profiles, implemented as a {@link ProfileList}<br />
 * The list of profiles is a full-fledged object because the list might have security attributes. The other two lists
 * are merely Java lists containing the real data.
 * </td></tr></table>
 * 
 * <table class="info"><tr class="infoHeader"><th>Attributes</th></tr><tr><td class="infoBody">
 * <u>{@link SecurityAttributes}</u>:  The classification and ownerProducer attributes are required.
 * </td></tr></table>
 *  
 * @author Brian Uri!
 * @since 2.0.0
 */
public final class Access extends AbstractBaseComponent {
	
	private List<Individual> _individuals = null;
	private List<Group> _groups = null;
	private ProfileList _profileList = null;
	private SecurityAttributes _securityAttributes = null;
	
	private static final String INDIVIDUAL_LIST_NAME = "AccessIndividualList";
	private static final String GROUP_LIST_NAME = "AccessGroupList";
	
	/**
	 * Constructor for creating a component from a XOM Element
	 *  
	 * @param element the XOM element representing this 
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Access(Element element) throws InvalidDDMSException {
		try {
			setXOMElement(element, false);
			_individuals = new ArrayList<Individual>();
			Element individualList = element.getFirstChildElement(INDIVIDUAL_LIST_NAME, getNamespace());
			if (individualList != null) {
				Elements individuals = individualList.getChildElements();
				for (int i = 0; i < individuals.size(); i++) {
					_individuals.add(new Individual(individuals.get(i)));
				}
			}
			_groups = new ArrayList<Group>();
			Element groupList = element.getFirstChildElement(GROUP_LIST_NAME, getNamespace());
			if (groupList != null) {
				Elements groups = groupList.getChildElements();
				for (int i = 0; i < groups.size(); i++) {
					_groups.add(new Group(groups.get(i)));
				}
			}
			Element profileList = element.getFirstChildElement(ProfileList.getName(getDDMSVersion()), getNamespace());
			if (profileList != null)
				_profileList = new ProfileList(profileList);
			_securityAttributes = new SecurityAttributes(element);
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
	 * @param individuals a list of individuals
	 * @param groups a list of groups
	 * @param profileList the profile list
	 * @param securityAttributes security attributes (required)
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	public Access(List<Individual> individuals, List<Group> groups, ProfileList profileList,
		SecurityAttributes securityAttributes) throws InvalidDDMSException {
		try {
			DDMSVersion version = DDMSVersion.getCurrentVersion();
			String ntkPrefix = PropertyReader.getPrefix("ntk");
			String ntkNamespace = version.getNtkNamespace();
			
			Element element = Util.buildElement(ntkPrefix, Access.getName(version),
				ntkNamespace, null);
			setXOMElement(element, false);
			
			if (individuals == null)
				individuals = Collections.emptyList();
			if (!individuals.isEmpty()) {
				Element individualList = Util.buildElement(ntkPrefix, INDIVIDUAL_LIST_NAME, ntkNamespace, null);
				element.appendChild(individualList);
				for (Individual individual : individuals) {
					individualList.appendChild(individual.getXOMElementCopy());
				}
			}
			if (groups == null)
				groups = Collections.emptyList();
			if (!groups.isEmpty()) {
				Element groupList = Util.buildElement(ntkPrefix, GROUP_LIST_NAME, ntkNamespace, null);
				element.appendChild(groupList);
				for (Group group : groups) {
					groupList.appendChild(group.getXOMElementCopy());
				}
			}			
			if (profileList != null)
				element.appendChild(profileList.getXOMElementCopy());
			
			_individuals = individuals;
			_groups = groups;
			_profileList = profileList;
			_securityAttributes = SecurityAttributes.getNonNullInstance(securityAttributes);
			_securityAttributes.addTo(element);
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
	 * <li>A classification is required.</li>
	 * <li>At least 1 ownerProducer exists and is non-empty.</li>
	 * <li>This component cannot exist until DDMS 4.0.1 or later.</li>
	 * </td></tr></table>
	 * 
	 * @see AbstractBaseComponent#validate()
	 * @throws InvalidDDMSException if any required information is missing or malformed
	 */
	protected void validate() throws InvalidDDMSException {
		Util.requireQName(getXOMElement(), getNamespace(), Access.getName(getDDMSVersion()));
		Util.requireDDMSValue("security attributes", getSecurityAttributes());
		getSecurityAttributes().requireClassification();
		
		// Should be reviewed as additional versions of DDMS are supported.
		requireVersion("4.0.1");
		
		super.validate();
	}
	
	/**
	 * Validates any conditions that might result in a warning.
	 * 
	 * <table class="info"><tr class="infoHeader"><th>Rules</th></tr><tr><td class="infoBody">
	 * <li>No individuals, groups, or profiles are described in this Access element.</li>
	 * </td></tr></table>
	 */
	protected void validateWarnings() {
		if (getIndividuals().isEmpty() && getGroups().isEmpty() && getProfileList() == null)
			addWarning("An ntk:Access element was found with no individual, group, or profile information.");
		super.validateWarnings();
	}
		
	/**
	 * @see AbstractBaseComponent#getOutput(boolean, String, String)
	 */
	public String getOutput(boolean isHTML, String prefix, String suffix) {
		String localPrefix = buildPrefix(prefix, "access", suffix) + ".";
		StringBuffer text = new StringBuffer();		
		text.append(buildOutput(isHTML, localPrefix + "individualList.", getIndividuals()));
		text.append(buildOutput(isHTML, localPrefix + "groupList.", getGroups()));
		if (getProfileList() != null)
			text.append(getProfileList().getOutput(isHTML, localPrefix, ""));
		text.append(getSecurityAttributes().getOutput(isHTML, localPrefix));
		return (text.toString());
	}
		
	/**
	 * @see AbstractBaseComponent#getNestedComponents()
	 */
	protected List<IDDMSComponent> getNestedComponents() {
		List<IDDMSComponent> list = new ArrayList<IDDMSComponent>();
		list.addAll(getIndividuals());
		list.addAll(getGroups());
		list.add(getProfileList());
		return (list);
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Access))
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
		return ("Access");
	}
	
	/**
	 * Accessor for the individuals
	 */
	public List<Individual> getIndividuals() {
		return (Collections.unmodifiableList(_individuals));
	}

	/**
	 * Accessor for the groups
	 */
	public List<Group> getGroups() {
		return (Collections.unmodifiableList(_groups));
	}

	/**
	 * Accessor for the profileList
	 */
	public ProfileList getProfileList() {
		return _profileList;
	}

	/**
	 * Accessor for the Security Attributes. Will always be non-null even if the attributes are not set.
	 */
	public SecurityAttributes getSecurityAttributes() {
		return (_securityAttributes);
	}
	
	/**
	 * Builder for this DDMS component.
	 * 
	 * @see IBuilder
	 * @author Brian Uri!
	 * @since 2.0.0
	 */
	public static class Builder implements IBuilder, Serializable {
		private static final long serialVersionUID = 7851044806424206976L;
		private List<Individual.Builder> _individuals;
		private List<Group.Builder> _groups;
		private ProfileList.Builder _profileList;
		private SecurityAttributes.Builder _securityAttributes;
		
		/**
		 * Empty constructor
		 */
		public Builder() {}
		
		/**
		 * Constructor which starts from an existing component.
		 */
		public Builder(Access access) {
			for (Individual individual : access.getIndividuals())
				getIndividuals().add(new Individual.Builder(individual));
			for (Group group : access.getGroups())
				getGroups().add(new Group.Builder(group));
			if (access.getProfileList() != null)
				setProfileList(new ProfileList.Builder(access.getProfileList()));
			setSecurityAttributes(new SecurityAttributes.Builder(access.getSecurityAttributes()));
		}
		
		/**
		 * @see IBuilder#commit()
		 */
		public Access commit() throws InvalidDDMSException {
			if (isEmpty())
				return (null);
			List<Individual> individuals = new ArrayList<Individual>();
			for (IBuilder builder : getIndividuals()) {
				Individual component = (Individual) builder.commit();
				if (component != null)
					individuals.add(component);
			}
			List<Group> groups = new ArrayList<Group>();
			for (IBuilder builder : getGroups()) {
				Group component = (Group) builder.commit();
				if (component != null)
					groups.add(component);
			}

			return (new Access(individuals, groups, getProfileList().commit(), getSecurityAttributes().commit()));
		}

		/**
		 * @see IBuilder#isEmpty()
		 */
		public boolean isEmpty() {
			boolean hasValueInList = false;
			for (IBuilder builder : getIndividuals())
				hasValueInList = hasValueInList || !builder.isEmpty();
			for (IBuilder builder : getGroups())
				hasValueInList = hasValueInList || !builder.isEmpty();			
			return (!hasValueInList && getProfileList().isEmpty() && getSecurityAttributes().isEmpty());
		}
				

		/**
		 * Builder accessor for the individuals
		 */
		public List<Individual.Builder> getIndividuals() {
			if (_individuals == null)
				_individuals = new LazyList(Individual.Builder.class);
			return _individuals;
		}

		/**
		 * Builder accessor for the groups
		 */
		public List<Group.Builder> getGroups() {
			if (_groups == null)
				_groups = new LazyList(Group.Builder.class);
			return _groups;
		}

		/**
		 * Builder accessor for the profileList
		 */
		public ProfileList.Builder getProfileList() {
			if (_profileList == null)
				_profileList = new ProfileList.Builder();
			return _profileList;
		}

		/**
		 * Builder accessor for the profileList
		 */
		public void setProfileList(ProfileList.Builder profileList) {
			_profileList = profileList;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public SecurityAttributes.Builder getSecurityAttributes() {
			if (_securityAttributes == null)
				_securityAttributes = new SecurityAttributes.Builder();
			return _securityAttributes;
		}
		
		/**
		 * Builder accessor for the securityAttributes
		 */
		public void setSecurityAttributes(SecurityAttributes.Builder securityAttributes) {
			_securityAttributes = securityAttributes;
		}

	}
} 