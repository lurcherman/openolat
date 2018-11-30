/**
* OLAT - Online Learning and Training<br>
* http://www.olat.org
* <p>
* Licensed under the Apache License, Version 2.0 (the "License"); <br>
* you may not use this file except in compliance with the License.<br>
* You may obtain a copy of the License at
* <p>
* http://www.apache.org/licenses/LICENSE-2.0
* <p>
* Unless required by applicable law or agreed to in writing,<br>
* software distributed under the License is distributed on an "AS IS" BASIS, <br>
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
* See the License for the specific language governing permissions and <br>
* limitations under the License.
* <p>
* Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
* University of Zurich, Switzerland.
* <hr>
* <a href="http://www.openolat.org">
* OpenOLAT - Online Learning and Training</a><br>
* This file has been modified by the OpenOLAT community. Changes are licensed
* under the Apache 2.0 license as the original file.
*/

package org.olat.course.run.preview;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.olat.basesecurity.GroupRoles;
import org.olat.core.CoreSpringFactory;
import org.olat.core.id.Identity;
import org.olat.core.logging.AssertException;
import org.olat.core.manager.BasicManager;
import org.olat.course.export.CourseEnvironmentMapper;
import org.olat.course.groupsandrights.CourseGroupManager;
import org.olat.course.groupsandrights.CourseRights;
import org.olat.group.BusinessGroup;
import org.olat.group.BusinessGroupService;
import org.olat.group.area.BGArea;
import org.olat.group.area.BGAreaManager;
import org.olat.repository.RepositoryEntry;
import org.olat.repository.RepositoryService;
import org.olat.resource.OLATResource;

/**
 * Initial Date:  08.02.2005
 *
 * @author Mike Stock
 */
final class PreviewCourseGroupManager extends BasicManager implements CourseGroupManager {

	private List<BGArea> areas;
	private List<BusinessGroup> groups;
	private RepositoryEntry courseResource;
	private boolean isCoach, isCourseAdmin;
	
	private final BGAreaManager areaManager;
	private final RepositoryService repositoryService;
	private final BusinessGroupService businessGroupService;
	
	/**
	 * @param groups
	 * @param areas
	 * @param isCoach
	 * @param isCourseAdmin
	 */
	public PreviewCourseGroupManager(RepositoryEntry courseResource, List<BusinessGroup> groups,
			List<BGArea> areas, boolean isCoach, boolean isCourseAdmin) {
		this.courseResource = courseResource;
		this.groups = groups;
		this.areas = areas;
		this.isCourseAdmin = isCourseAdmin;
		this.isCoach = isCoach;

		areaManager = CoreSpringFactory.getImpl(BGAreaManager.class);
		repositoryService = CoreSpringFactory.getImpl(RepositoryService.class);
		businessGroupService = CoreSpringFactory.getImpl(BusinessGroupService.class);
	}

	@Override
	public OLATResource getCourseResource() {
		return courseResource.getOlatResource();
	}

	@Override
	public RepositoryEntry getCourseEntry() {
		return courseResource;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#hasRight(org.olat.core.id.Identity, java.lang.String)
	 */
	@Override
	public boolean hasRight(Identity identity, String courseRight) {
		if (courseRight.equals(CourseRights.RIGHT_COURSEEDITOR)) {
			return false;
		}
		return false;
	}

	@Override
	public List<String> getRights(Identity identity) {
		return new ArrayList<String>(1);
	}

	@Override
	public boolean isIdentityInGroup(Identity identity, Long groupKey) {
		for(BusinessGroup group:groups) {
			if(groupKey.equals(group.getKey())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#isLearningGroupFull(java.lang.String)
	 */
	public boolean isBusinessGroupFull(Long groupKey){
		for(BusinessGroup group:groups) {
			if(groupKey.equals(group.getKey())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isIdentityInLearningArea(Identity identity, Long areaKey) {
		for(BGArea area:areas) {
			if(areaKey.equals(area.getKey())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean existGroup(String nameOrKey) {
		for(BusinessGroup group:groups) {
			if(nameOrKey.equals(group.getName()) || nameOrKey.equals(group.getKey().toString())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean existArea(String nameOrKey) {
		for(BGArea area:areas) {
			if(nameOrKey.equals(area.getName()) || nameOrKey.equals(area.getKey().toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#isIdentityCourseCoach(org.olat.core.id.Identity)
	 */
	@Override
	public boolean isIdentityCourseCoach(Identity identity) {
		return isCoach;
	}

	@Override
	public boolean isIdentityCourseParticipant(Identity identity) {
		return false;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#isIdentityCourseAdministrator(org.olat.core.id.Identity)
	 */
	@Override
	public boolean isIdentityCourseAdministrator(Identity identity) {
		return isCourseAdmin;
	}

	@Override
	public boolean isIdentityAnyCourseCoach(Identity identity) {
		return isCoach;
	}

	@Override
	public boolean isIdentityAnyCourseAdministrator(Identity identity) {
		return isCourseAdmin;
	}

	@Override
	public boolean isIdentityAnyCourseParticipant(Identity identity) {
		return false;
	}

	@Override
	public boolean hasBusinessGroups() {
		return groups != null && groups.size() > 0;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#getAllLearningGroupsFromAllContexts()
	 */
	@Override
	public List<BusinessGroup> getAllBusinessGroups() {
		return groups;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#getOwnedBusinessGroups(org.olat.core.id.Identity)
	 */
	@Override
	public List<BusinessGroup> getOwnedBusinessGroups(Identity identity) {
		return new ArrayList<>(1);
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#getParticipatingBusinessGroups(org.olat.core.id.Identity)
	 */
	@Override
	public List<BusinessGroup> getParticipatingBusinessGroups(Identity identity) {
		return new ArrayList<>(1);
	}
	
	@Override
	public boolean hasAreas() {
		return areas != null && areas.size() > 0;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#getAllAreasFromAllContexts()
	 */
	@Override
	public List<BGArea> getAllAreas() {
		return areas;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#deleteCourseGroupmanagement()
	 */
	@Override
	public void deleteCourseGroupmanagement() {
		//do nothing in preview
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#getNumberOfMembersFromGroups(java.util.List)
	 */
	@Override
	public List<Integer> getNumberOfMembersFromGroups(List<BusinessGroup> groupList) {
		List<Integer> members = new ArrayList<Integer>();
		for (BusinessGroup group:groups) {
			int numbMembers = businessGroupService.countMembers(group, GroupRoles.participant.name());
			members.add(new Integer(numbMembers));
		}
		return members;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#getUniqueBusinessGroupNames()
	 */
	@Override
	public List<String> getUniqueBusinessGroupNames() {
		List<String> names = new ArrayList<>();
		if(groups != null) {
			for (BusinessGroup group:groups) {
				if (!names.contains(group.getName())) {
					names.add(group.getName().trim());
				}
			}
			Collections.sort(names);
		}
		return names;
	}

	/**
	 * @see org.olat.course.groupsandrights.CourseGroupManager#getUniqueAreaNames()
	 */
	@Override
	public List<String> getUniqueAreaNames() {
		List<String> areaNames = new ArrayList<>();
		if(areas != null) {
			for (BGArea area:areas) {
				if (!areaNames.contains(area.getName())) {
					areaNames.add(area.getName().trim());
				}
			}
			Collections.sort(areaNames);
		}
		return areaNames;
	}

	@Override
	public List<Identity> getCoachesFromBusinessGroups() {
		return businessGroupService.getMembers(groups, GroupRoles.coach.name());
	}

	@Override
	public List<Identity> getCoachesFromAreas() {
		List<BusinessGroup> groupList = areaManager.findBusinessGroupsOfAreas(areas);
		return businessGroupService.getMembers(groupList, GroupRoles.coach.name());
	}

	@Override
	public List<Identity> getParticipantsFromBusinessGroups() {
		return businessGroupService.getMembers(groups, GroupRoles.participant.name());
	}

	@Override
	public List<Identity> getCoachesFromBusinessGroups(List<Long> groupKeys) {
		List<BusinessGroup> bgs = businessGroupService.loadBusinessGroups(groupKeys);
		return businessGroupService.getMembers(bgs, GroupRoles.coach.name());
	}

	@Override
	public List<Identity> getCoachesFromAreas(List<Long> areaKeys) {
		List<BGArea> areaList = areaManager.loadAreas(areaKeys);
		List<BusinessGroup> groupList = areaManager.findBusinessGroupsOfAreas(areaList);
		return businessGroupService.getMembers(groupList, GroupRoles.coach.name());
	}

	@Override
	public List<Identity> getParticipantsFromBusinessGroups(List<Long> groupKeys) {
		return businessGroupService.getMembers(groups, GroupRoles.participant.name());
	}

	@Override
	public List<Identity> getParticipantsFromAreas(List<Long> areaKeys) {
		List<BGArea> areaList = areaManager.loadAreas(areaKeys);
		List<BusinessGroup> groupList = areaManager.findBusinessGroupsOfAreas(areaList);
		return businessGroupService.getMembers(groupList, GroupRoles.participant.name());
	}

	@Override
	public List<Identity> getParticipantsFromAreas() {
		return businessGroupService.getMembers(groups, GroupRoles.participant.name());
	}
	
	@Override
	public List<Identity> getCoaches() {
		return repositoryService.getMembers(getCourseEntry(), GroupRoles.coach.name());
	}

	@Override
	public List<Identity> getParticipants() {
		return repositoryService.getMembers(getCourseEntry(), GroupRoles.participant.name());
	}

	@Override
	public CourseEnvironmentMapper getBusinessGroupEnvironment() {
		throw new AssertException("unsupported");
	}

	@Override
	public void exportCourseBusinessGroups(File fExportDirectory, CourseEnvironmentMapper env,
			boolean runtimeDatas, boolean backwardsCompatible) {
		throw new AssertException("unsupported");
	}

	@Override
	public CourseEnvironmentMapper importCourseBusinessGroups(File fImportDirectory) {
		throw new AssertException("unsupported");
	}

	@Override
	public void archiveCourseGroups(File exportDirectory) {
		//
	}

	@Override
	public List<BusinessGroup> getWaitingListGroups(Identity identity) {
		return new ArrayList<>(1);
	}
}