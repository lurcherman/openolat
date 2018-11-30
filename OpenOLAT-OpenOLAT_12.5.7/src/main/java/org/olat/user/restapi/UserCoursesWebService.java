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
package org.olat.user.restapi;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.olat.core.id.Identity;
import org.olat.repository.RepositoryEntry;
import org.olat.repository.RepositoryEntryOrder;
import org.olat.repository.RepositoryManager;
import org.olat.restapi.repository.course.CoursesWebService;
import org.olat.restapi.support.MediaTypeVariants;
import org.olat.restapi.support.vo.CourseVO;
import org.olat.restapi.support.vo.CourseVOes;

/**
 * 
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class UserCoursesWebService {
	
	private final Identity identity;
	
	public UserCoursesWebService(Identity identity) {
		this.identity = identity;
	}
	
	
	/**
	 * Retrieves the list of "My entries" but limited to courses.
	 * @response.representation.200.qname {http://www.example.com}courseVO
	 * @response.representation.200.mediaType application/xml, application/json
	 * @response.representation.200.doc The courses
	 * @response.representation.200.example {@link org.olat.restapi.support.vo.Examples#SAMPLE_COURSEVOes}
	 * @response.representation.401.doc The roles of the authenticated user are not sufficient
	 * @param start The first result
	 * @param limit Max result
	 * @param httpRequest The HTTP request
	 * @param request The REST request
	 * @return The list of my entries
	 */
	@GET
	@Path("my")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getMyCourses(@QueryParam("start") @DefaultValue("0") Integer start,
			@QueryParam("limit") @DefaultValue("25") Integer limit, @Context HttpServletRequest httpRequest,
			@Context Request request) {
		
		RepositoryManager rm = RepositoryManager.getInstance();
		if(MediaTypeVariants.isPaged(httpRequest, request)) {
			List<RepositoryEntry> repoEntries = rm.getLearningResourcesAsStudent(identity, null, start, limit, RepositoryEntryOrder.nameAsc);
			int totalCount= rm.countLearningResourcesAsStudent(identity);

			CourseVO[] vos = toCourseVo(repoEntries);
			CourseVOes voes = new CourseVOes();
			voes.setCourses(vos);
			voes.setTotalCount(totalCount);
			return Response.ok(voes).build();
		} else {
			List<RepositoryEntry> repoEntries = rm.getLearningResourcesAsStudent(identity, null, 0, -1, RepositoryEntryOrder.nameAsc);
			CourseVO[] vos = toCourseVo(repoEntries);
			return Response.ok(vos).build();
		}
	}

	/**
	 * Retrieves the list of "My supervised courses" but limited to courses.
	 * @response.representation.200.qname {http://www.example.com}courseVO
	 * @response.representation.200.mediaType application/xml, application/json
	 * @response.representation.200.doc The courses
	 * @response.representation.200.example {@link org.olat.restapi.support.vo.Examples#SAMPLE_COURSEVOes}
	 * @response.representation.401.doc The roles of the authenticated user are not sufficient
	 * @param start The first result
	 * @param limit Max result
	 * @param httpRequest The HTTP request
	 * @param request The REST request
	 * @return The list of my supervised entries
	 */
	@GET
	@Path("teached")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getTeachedCourses(@QueryParam("start") @DefaultValue("0") Integer start,
			@QueryParam("limit") @DefaultValue("25") Integer limit, @Context HttpServletRequest httpRequest,
			@Context Request request) {
		
		RepositoryManager rm = RepositoryManager.getInstance();
		if(MediaTypeVariants.isPaged(httpRequest, request)) {
			List<RepositoryEntry> repoEntries = rm.getLearningResourcesAsTeacher(identity, start, limit, RepositoryEntryOrder.nameAsc);
			int totalCount= rm.countLearningResourcesAsTeacher(identity);

			CourseVO[] vos = toCourseVo(repoEntries);
			CourseVOes voes = new CourseVOes();
			voes.setCourses(vos);
			voes.setTotalCount(totalCount);
			return Response.ok(voes).build();
		} else {
			List<RepositoryEntry> repoEntries = rm.getLearningResourcesAsTeacher(identity, 0, -1);
			CourseVO[] vos = toCourseVo(repoEntries);
			return Response.ok(vos).build();
		}
	}
	
	/**
	 * Retrieves the list of my favorite courses.
	 * @response.representation.200.qname {http://www.example.com}courseVO
	 * @response.representation.200.mediaType application/xml, application/json
	 * @response.representation.200.doc The courses
	 * @response.representation.200.example {@link org.olat.restapi.support.vo.Examples#SAMPLE_COURSEVOes}
	 * @response.representation.401.doc The roles of the authenticated user are not sufficient
	 * @param start The first result
	 * @param limit Max result
	 * @param httpRequest The HTTP request
	 * @param request The REST request
	 * @return The list of my favorite courses
	 */
	@GET
	@Path("favorite")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getFavoritCourses(@QueryParam("start") @DefaultValue("0") Integer start,
			@QueryParam("limit") @DefaultValue("25") Integer limit, @Context HttpServletRequest httpRequest,
			@Context Request request) {
		
		List<String> courseType = Collections.singletonList("CourseModule");
		RepositoryManager rm = RepositoryManager.getInstance();
		if(MediaTypeVariants.isPaged(httpRequest, request)) {
			List<RepositoryEntry> repoEntries = rm.getFavoritLearningResourcesAsTeacher(identity, courseType, start, limit, RepositoryEntryOrder.nameAsc);
			
			int totalCount;
			if(repoEntries.size() < limit) {
				totalCount = repoEntries.size();
			} else {
				totalCount = rm.countFavoritLearningResourcesAsTeacher(identity, courseType);
			}
			CourseVO[] vos = toCourseVo(repoEntries);
			CourseVOes voes = new CourseVOes();
			voes.setCourses(vos);
			voes.setTotalCount(totalCount);
			return Response.ok(voes).build();
		} else {
			List<RepositoryEntry> repoEntries = rm.getFavoritLearningResourcesAsTeacher(identity, courseType, 0, -1, RepositoryEntryOrder.nameAsc);
			CourseVO[] vos = toCourseVo(repoEntries);
			return Response.ok(vos).build();
		}
	}
	
	private CourseVO[] toCourseVo(List<RepositoryEntry> repoEntries) {
		return CoursesWebService.toCourseVo(repoEntries);
	}
}
