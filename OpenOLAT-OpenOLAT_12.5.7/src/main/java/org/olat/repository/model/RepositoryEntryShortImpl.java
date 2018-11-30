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

import org.olat.core.commons.persistence.PersistentObject;
import org.olat.repository.RepositoryEntryShort;
import org.olat.resource.OLATResource;

/**
 * 
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 */
public class RepositoryEntryShortImpl extends PersistentObject implements RepositoryEntryShort {

	private static final long serialVersionUID = 4599683374800325931L;
	
	private String displayName;
	private String description;
	private OLATResource resource;
	private int access;
	private int statusCode;
	private boolean membersOnly;
	
	
	@Override
	public String getDisplayname() {
		return displayName;
	}
	
	public void setDisplayname(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getResourceType() {
		return resource.getResourceableTypeName();
	}


	public OLATResource getOlatResource() {
		return resource;
	}

	public void setOlatResource(OLATResource resource) {
		this.resource = resource;
	}

	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}

	@Override
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isMembersOnly() {
		return membersOnly;
	}

	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}

	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof RepositoryEntryShortImpl) {
			RepositoryEntryShortImpl other = (RepositoryEntryShortImpl) obj;
			return getKey().equals(other.getKey());
		}
		return false;
	}
}