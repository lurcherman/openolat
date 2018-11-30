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
package org.olat.repository.model;

/**
 * 
 * Initial date: 19.08.2014<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class RepositoryEntrySecurity {
	
	private final boolean owner;
	private final boolean canLaunch;
	private final boolean entryAdmin;
	private final boolean readOnly;
	
	private final boolean courseParticipant;
	private final boolean courseCoach;
	private final boolean groupParticipant;
	private final boolean groupCoach;
	private final boolean groupWaiting;
	
	public RepositoryEntrySecurity(boolean entryAdmin, boolean owner,
			boolean courseParticipant, boolean courseCoach,
			boolean groupParticipant, boolean groupCoach,
			boolean groupWaiting, boolean canLaunch, boolean readOnly) {
		this.owner = owner;
		this.canLaunch = canLaunch;
		this.entryAdmin = entryAdmin;
		
		this.courseParticipant = courseParticipant;
		this.courseCoach = courseCoach;
		this.groupParticipant = groupParticipant;
		this.groupCoach = groupCoach;
		this.groupWaiting = groupWaiting;
		this.readOnly = readOnly;
	}
	
	public boolean isOwner() {
		return owner;
	}
	
	public boolean isEntryAdmin() {
		return entryAdmin;
	}
	
	public boolean canLaunch() {
		return canLaunch;
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public boolean isCourseParticipant() {
		return courseParticipant;
	}

	public boolean isCourseCoach() {
		return courseCoach;
	}

	public boolean isGroupParticipant() {
		return groupParticipant;
	}

	public boolean isGroupCoach() {
		return groupCoach;
	}

	public boolean isGroupWaiting() {
		return groupWaiting;
	}
	
	public boolean isMember() {
		return owner || courseParticipant || courseCoach || groupParticipant || groupCoach;
	}
}
