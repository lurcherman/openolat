/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.group.ui.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.olat.core.gui.components.form.flexible.elements.FormLink;
import org.olat.core.id.Identity;
import org.olat.group.BusinessGroupManagedFlag;
import org.olat.group.BusinessGroupShort;
import org.olat.user.UserPropertiesRow;
import org.olat.user.propertyhandlers.UserPropertyHandler;

/**
 * 
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 */
public class MemberView extends UserPropertiesRow {
	
	private Date firstTime;
	private Date lastTime;
	private final CourseMembership membership = new CourseMembership();
	private List<BusinessGroupShort> groups;
	private String onlineStatus;
	private FormLink toolsLink, chatLink;
	
	public MemberView(Identity identity, List<UserPropertyHandler> userPropertyHandlers, Locale locale) {
		super(identity, userPropertyHandlers, locale);
	}

	public FormLink getToolsLink() {
		return toolsLink;
	}

	public void setToolsLink(FormLink toolsLink) {
		this.toolsLink = toolsLink;
	}

	public FormLink getChatLink() {
		return chatLink;
	}

	public void setChatLink(FormLink chatLink) {
		this.chatLink = chatLink;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public CourseMembership getMembership() {
		return membership;
	}

	public List<BusinessGroupShort> getGroups() {
		return groups;
	}

	public void setGroups(List<BusinessGroupShort> groups) {
		this.groups = groups;
	}

	public void addGroup(BusinessGroupShort group) {
		if(group == null) return;
		if(groups == null) {
			groups = new ArrayList<BusinessGroupShort>(3);
		}
		groups.add(group);
	}
	
	public boolean isFullyManaged() {
		if(membership != null && !membership.isManagedMembersRepo() &&
				(membership.isRepoOwner() || membership.isRepoTutor() || membership.isRepoParticipant())) {
			return false;
		}

		if(groups != null) {
			for(BusinessGroupShort group:groups) {
				if(!BusinessGroupManagedFlag.isManaged(group.getManagedFlags(), BusinessGroupManagedFlag.membersmanagement)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Date getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Date firstTime) {
		if(firstTime == null) return;
		if(this.firstTime == null || this.firstTime.compareTo(firstTime) > 0) {
			this.firstTime = firstTime;
		}
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		if(lastTime == null) return;
		if(this.lastTime == null || this.lastTime.compareTo(lastTime) < 0) {
			this.lastTime = lastTime;
		}
	}

	@Override
	public int hashCode() {
		return getIdentityKey() == null ? 2878 : getIdentityKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj instanceof MemberView) {
			MemberView member = (MemberView)obj;
			return getIdentityKey() != null && getIdentityKey().equals(member.getIdentityKey());
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("memberView[identityKey=").append(getIdentityKey() == null ? "" : getIdentityKey())
			.append(":login=").append(getIdentityName()).append("]");
		return sb.toString();
	}
}