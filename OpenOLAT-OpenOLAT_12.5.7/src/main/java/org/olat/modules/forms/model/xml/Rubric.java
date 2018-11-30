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
package org.olat.modules.forms.model.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Initial date: 7 déc. 2016<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class Rubric extends AbstractElement {

	private static final long serialVersionUID = -8486210445435845568L;
	
	private SliderType sliderType;
	private List<Slider> sliders = new ArrayList<>();
	private List<StepLabel> stepLabels = new ArrayList<>();
	
	private int start;
	private int end;
	private int steps;
	
	@Override
	public String getType() {
		return "formrubric";
	}

	public SliderType getSliderType() {
		return sliderType;
	}

	public void setSliderType(SliderType sliderType) {
		this.sliderType = sliderType;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public List<StepLabel> getStepLabels() {
		return stepLabels;
	}

	public void setStepLabels(List<StepLabel> stepLabels) {
		this.stepLabels = stepLabels;
	}

	public List<Slider> getSliders() {
		return sliders;
	}

	public void setSliders(List<Slider> sliders) {
		this.sliders = sliders;
	}

	public enum SliderType {
		discrete,
		discrete_slider,
		continuous	
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj instanceof Rubric) {
			Rubric rubric = (Rubric)obj;
			return getId() != null && getId().equals(rubric.getId());
		}
		return super.equals(obj);
	}

}
