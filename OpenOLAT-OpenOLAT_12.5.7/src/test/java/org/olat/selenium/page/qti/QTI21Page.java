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
package org.olat.selenium.page.qti;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.olat.selenium.page.NavigationPage;
import org.olat.selenium.page.graphene.OOGraphene;
import org.olat.selenium.page.repository.RepositoryAccessPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * 
 * Initial date: 06.05.2016<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class QTI21Page {
	
	private final By toolsMenu = By.cssSelector("ul.o_sel_repository_tools");
	private final By settingsMenu = By.cssSelector("ul.o_sel_course_settings");
	
	private WebDriver browser;
	
	private QTI21Page(WebDriver browser) {
		this.browser = browser;
	}
	
	public static QTI21Page getQTI21Page(WebDriver browser) {
		By mainBy = By.id("o_main_wrapper");
		OOGraphene.waitElement(mainBy, 5, browser);
		WebElement main = browser.findElement(mainBy);
		Assert.assertTrue(main.isDisplayed());
		return new QTI21Page(browser);
	}
	
	public QTI21Page start() {
		By startBy = By.cssSelector("a.o_sel_start_qti21assessment");
		WebElement startButton = browser.findElement(startBy);
		startButton.click();
		OOGraphene.waitBusy(browser);
		By mainBy = By.cssSelector("div.qtiworks.o_assessmenttest");
		OOGraphene.waitElement(mainBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnCourseAttempts(int numOfAttemtps) {
		By attemptBy = By.xpath("//div[contains(@class,'o_course_run')]//table//tr[contains(@class,'o_attempts')]//td[text()[contains(.,'" + numOfAttemtps + "')]]");
		OOGraphene.waitElement(attemptBy, 5, browser);
		WebElement attemptEl = browser.findElement(attemptBy);
		Assert.assertTrue(attemptEl.isDisplayed());
		return this;
	}
	
	public QTI21Page startTestPart() {
		By startBy = By.xpath("//button[contains(@onclick,'advanceTestPart')]");
		browser.findElement(startBy).click();
		OOGraphene.waitBusy(browser);
		By menuBy = By.id("o_qti_menu");
		OOGraphene.waitElement(menuBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentItem() {
		By assessmentItemBy = By.cssSelector("div.qtiworks.o_assessmentitem.o_assessmenttest");
		OOGraphene.waitElement(assessmentItemBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentItem(String title) {
		By itemTitleBy = By.xpath("//div[@class='o_assessmentitem_wrapper']/h4[contains(normalize-space(.),'" + title + "')]");
		OOGraphene.waitElement(itemTitleBy, 5, browser);
		return this;
	}
	
	public QTI21Page selectItem(String title) {
		By itemBy = By.xpath("//div[@id='o_qti_menu']//li[contains(@class,'o_qti_menu_item')]//a[span[contains(normalize-space(.),'" + title + "')]]");
		OOGraphene.waitElement(itemBy, 5, browser);
		browser.findElement(itemBy).click();
		OOGraphene.waitBusy(browser);
		return this;
	}
	
	/**
	 * Check the answer of a single choice.
	 * @param answer The answer
	 * @return Itself
	 */
	public QTI21Page answerSingleChoice(String answer) {
		By choiceBy = By.xpath("//tr[contains(@class,'choiceinteraction')][td[contains(@class,'choiceInteraction')][label[text()[contains(normalize-space(.),'" + answer + "')]]]]/td[contains(@class,'control')]/input[@type='radio']");
		browser.findElement(choiceBy).click();
		return this;
	}
	
	/**
	 * Check the answer of a single choice. The answer is wrapped in a P tag.
	 * @param answer The answer
	 * @return Itself
	 */
	public QTI21Page answerSingleChoiceWithParagraph(String answer) {
		By choiceBy = By.xpath("//tr[contains(@class,'choiceinteraction')][td[contains(@class,'choiceInteraction')][label/p[contains(normalize-space(text()),'" + answer + "')]]]/td[contains(@class,'control')]/input[@type='radio']");
		browser.findElement(choiceBy).click();
		return this;
	}

	public QTI21Page answerMultipleChoice(String... answers) {
		for(String answer:answers) {
			By choiceBy = By.xpath("//tr[contains(@class,'choiceinteraction')][td[contains(@class,'choiceInteraction')][label/p[contains(text(),'" + answer + "')]]]/td[contains(@class,'control')]/input[@type='checkbox']");
			browser.findElement(choiceBy).click();
		}
		return this;
	}
	
	public QTI21Page deselectAnswerMultipleChoice(String... answers) {
		for(String answer:answers) {
			By choiceBy = By.xpath("//tr[contains(@class,'choiceinteraction')][td[contains(@class,'choiceInteraction')][label/p[contains(text(),'" + answer + "')]]]/td[contains(@class,'control')]/input[@type='checkbox']");
			OOGraphene.check(browser.findElement(choiceBy), Boolean.FALSE);
		}
		return this;
	}
	
	/**
	 * Select the key of the inline choice interaction.
	 * 
	 * @param key The key to select
	 * @return Itself
	 */
	public QTI21Page answerInlineChoice(String key) {
		By inlineBy = By.xpath("//span[@class='inlineChoiceInteraction']/select");
		OOGraphene.waitElement(inlineBy, browser);
		WebElement inlineEl = browser.findElement(inlineBy);
		new Select(inlineEl).selectByValue(key);
		return this;
	}
	
	public QTI21Page answerHotspot(String shape) {
		OOGraphene.waitElement(By.className("hotspotInteraction"), browser);
		By areaBy = By.xpath("//div[contains(@class,'hotspotInteraction')]//map/area[@shape='" + shape + "']");
		List<WebElement> elements = browser.findElements(areaBy);
		Assert.assertEquals("Hotspot of shape " + shape, 1, elements.size()); 
		elements.get(0).click();
		return this;
	}
	
	/**
	 * Select the area with the specified data-qti-id.
	 * @param id The id save in data-qti-id
	 * @return Itself
	 */
	public QTI21Page answerGraphicOrderById(String id) {
		OOGraphene.waitElement(By.className("graphicOrderInteraction"), browser);
		By areaBy = By.xpath("//div[contains(@class,'graphicOrderInteraction')]//map/area[@data-qti-id='" + id + "']");
		List<WebElement> elements = browser.findElements(areaBy);
		Assert.assertEquals("Hotspot with data-qti-id " + id, 1, elements.size()); 
		elements.get(0).click();
		return this;
	}
	
	public QTI21Page answerHottext(int index) {
		OOGraphene.waitElement(By.className("hottextInteraction"), browser);
		By checkBy = By.xpath("//div[contains(@class,'hottextInteraction')]//p/span[@class='hottext'][" + index + "]/input[@type='checkbox']");
		browser.findElement(checkBy).click();
		return this;
	}

	public QTI21Page answerCorrectKPrim(String... choices) {
		for(String choice:choices) {
			By incorrectBy = By.xpath("//tr[td/p[contains(text(),'" + choice + "')]]/td[contains(@class,'o_qti_item_kprim_input o_qti_item_kprim_input_wrong')]/input[@type='checkbox']");
			WebElement incorrectEl = browser.findElement(incorrectBy);
			OOGraphene.check(incorrectEl, Boolean.FALSE);
			
			By correctBy = By.xpath("//tr[td/p[contains(text(),'" + choice + "')]]/td[contains(@class,'o_qti_item_kprim_input o_qti_item_kprim_input_correct')]/input[@type='checkbox']");
			WebElement correctEl = browser.findElement(correctBy);
			OOGraphene.check(correctEl, Boolean.TRUE);
		}
		return this;
	}
	
	public QTI21Page answerIncorrectKPrim(String... choices) {
		for(String choice:choices) {
			By correctBy = By.xpath("//tr[td/p[contains(text(),'" + choice + "')]]/td[contains(@class,'o_qti_item_kprim_input o_qti_item_kprim_input_correct')]/input[@type='checkbox']");
			WebElement correctEl = browser.findElement(correctBy);
			OOGraphene.check(correctEl, Boolean.FALSE);
			
			By incorrectBy = By.xpath("//tr[td/p[contains(text(),'" + choice + "')]]/td[contains(@class,'o_qti_item_kprim_input o_qti_item_kprim_input_wrong')]/input[@type='checkbox']");
			WebElement incorrectEl = browser.findElement(incorrectBy);
			OOGraphene.check(incorrectEl, Boolean.TRUE);
		}
		return this;
	}
	
	/**
	 * Fill the gap entry based on its response id.
	 * 
	 * @param text The answer
	 * @param responseId The identifier of the text entry
	 * @return Itself
	 */
	public QTI21Page answerGapText(String text, String responseId) {
		By gapBy = By.xpath("//span[contains(@class,'textEntryInteraction')]/input[@type='text'][contains(@name,'" + responseId + "')]");
		WebElement gapEl = browser.findElement(gapBy);
		gapEl.clear();
		gapEl.sendKeys(text);
		return this;
	}
	
	/**
	 * 
	 * @param text The answer
	 * @param placeholder The placeholder to found the right gap
	 * @return Itself
	 */
	public QTI21Page answerGapTextWithPlaceholder(String text, String placeholder) {
		By gapBy = By.xpath("//span[contains(@class,'textEntryInteraction')]/input[@type='text'][@placeholder='" + placeholder + "']");
		WebElement gapEl = browser.findElement(gapBy);
		gapEl.clear();
		gapEl.sendKeys(text);
		return this;
	}
	
	/**
	 * The method check text within a &lt;p&gt; element.
	 * 
	 * @param source Text in the source
	 * @param target Text in the target
	 * @param match Select or deselect
	 * @return Itself
	 */
	public QTI21Page answerMatch(String source, String target, boolean match) {
		By matchBy = By.xpath("//div[contains(@class,'matchInteraction')]/table//tr[th/p[contains(text(),'" + source + "')]]/td[count(//div[contains(@class,'matchInteraction')]/table//tr/th[p[contains(text(),'" + target + "')]]/preceding-sibling::th)]/input");
		WebElement matchEl = browser.findElement(matchBy);
		OOGraphene.check(matchEl, match);
		return this;
	}
	
	public QTI21Page answerMatch(String source, TrueFalse target, boolean match) {
		By matchBy = By.xpath("//div[contains(@class,'matchInteraction')]/table//tr[td/p[contains(text(),'" + source + "')]]/td[" + (target.ordinal() + 1) + "]/input");
		WebElement matchEl = browser.findElement(matchBy);
		OOGraphene.check(matchEl, match);
		return this;
	}
	
	public QTI21Page answerMatchDropSourceToTarget(String source, String target) {
		By sourceBy = By.xpath("//li[contains(@class,'o_match_dnd_source')]/p[contains(text(),'" + source + "')]");
		OOGraphene.waitElement(sourceBy, 5, browser);
		WebElement sourceEl = browser.findElement(sourceBy);
		By targetBy = By.xpath("//li[contains(@class,'o_match_dnd_target')]/div[@class='clearfix']/p[contains(text(),'" + target + "')]");
		WebElement targetEl = browser.findElement(targetBy);
		new Actions(browser)
			.moveToElement(sourceEl, 30, 30)
			.clickAndHold()
			.moveToElement(targetEl, 30, 30)
			.release()
			.build()
			.perform();

		By sourceDroppedBy = By.xpath("//ul[contains(@class,'o_match_dnd_target_drop_zone')]/li[contains(@class,'o_match_dnd_source')]/p[contains(text(),'" + source + "')]");
		OOGraphene.waitElement(sourceDroppedBy, 5, browser);
		return this;
	}
	
	public QTI21Page answerMatchDropTargetToTarget(String source, String target) {
		By sourceDroppedBy = By.xpath("//ul[contains(@class,'o_match_dnd_target_drop_zone')]/li[contains(@class,'o_match_dnd_source')]/p[contains(text(),'" + source + "')]");
		WebElement sourceEl = browser.findElement(sourceDroppedBy);
		By targetBy = By.xpath("//li[contains(@class,'o_match_dnd_target')]/div[@class='clearfix']/p[contains(text(),'" + target + "')]");
		WebElement targetEl = browser.findElement(targetBy);
		new Actions(browser)
			.moveToElement(sourceEl, 30, 30)
			.clickAndHold()
			.moveToElement(targetEl, 30, 30)
			.release()
			.build()
			.perform();

		OOGraphene.waitElement(sourceDroppedBy, 5, browser);
		return this;
	}
	
	public QTI21Page answerMatchDetarget(String source) {
		By sourceDroppedBy = By.xpath("//ul[contains(@class,'o_match_dnd_target_drop_zone')]/li[contains(@class,'o_match_dnd_source')]/p[contains(text(),'" + source + "')]");
		browser.findElement(sourceDroppedBy).click();
		return this;
	}
	
	public QTI21Page answerAssociate(String source, int index, boolean left) {
		By itemBy = By.xpath("//div[contains(@class,'o_associate_items')]/div[contains(@class,'o_associate_item')][contains(text(),'" + source + "')]");
		WebElement itemEl = browser.findElement(itemBy);
		By targetBy = By.xpath("//div[@class='association'][" + index + "]/div[contains(@class,'association_box')][contains(@class,'" + (left ? "left" : "right") + "')]");
		WebElement targetEl = browser.findElement(targetBy);
		new Actions(browser)
			.moveToElement(itemEl, 10, 10)
			.clickAndHold()
			.moveToElement(targetEl, 10, 10)
			.release()
			.build()
			.perform();
		return this;
	}
	
	public QTI21Page answerGraphicAssociate(String id) {
		OOGraphene.waitElement(By.className("graphicAssociateInteraction"), browser);
		By areaBy = By.xpath("//div[contains(@class,'graphicAssociateInteraction')]//map/area[@data-qti-id='" + id + "']");
		List<WebElement> elements = browser.findElements(areaBy);
		Assert.assertEquals("Area by " + id, 1, elements.size()); 
		elements.get(0).click();
		return this;
	}
	
	public QTI21Page answerOrderDropItem(String source) {
		By sourceBy = By.xpath("//li[contains(@class,'o_assessmentitem_order_item')][text()[contains(.,'" + source + "')]]");
		OOGraphene.waitElement(sourceBy, 5, browser);
		WebElement sourceEl = browser.findElement(sourceBy);
		By targetBy = By.xpath("//div[@class='orderInteraction']//div[contains(@class,'target')]/ul");
		WebElement targetEl = browser.findElement(targetBy);
		new Actions(browser)
			.moveToElement(sourceEl, 30, 30)
			.clickAndHold()
			.moveToElement(targetEl, 30, 30)
			.release()
			.build()
			.perform();

		By sourceDroppedBy = By.xpath("//div[@class='orderInteraction']//div[contains(@class,'target')]/ul/li[text()[contains(.,'" + source + "')]]");
		OOGraphene.waitElement(sourceDroppedBy, 5, browser);
		return this;
	}
	
	/**
	 * Select the gap match
	 * 
	 * @param source The row to check
	 * @param target Text in the target
	 * @param match Select or deselect
	 * @return Itself
	 */
	public QTI21Page answerGapMatch(int row, String target, boolean match) {
		By matchBy = By.xpath("//div[contains(@class,'gapMatchInteraction')]/table//tr[th[contains(text(),'" + row + "')]]/td[count(//div[contains(@class,'gapMatchInteraction')]/table//tr/th[text()[contains(.,'" + target + "')]]/preceding-sibling::th)]/input");
		WebElement matchEl = browser.findElement(matchBy);
		OOGraphene.check(matchEl, match);
		return this;
	}
	
	/**
	 * Use the click and drop.
	 * 
	 * @param item The identifier of the item
	 * @param gap The identifier of the gap
	 * @return Itself
	 */
	public QTI21Page answerGraphicGapClick(String item, String gap) {
		By sourceBy = By.xpath("//div[contains(@class,'gap_container')]/div[contains(@class,'o_gap_item')][@data-qti-id='" + item + "']");
		OOGraphene.waitElement(sourceBy, 5, browser);
		WebElement sourceEl = browser.findElement(sourceBy);
		sourceEl.click();
		By targetBy = By.xpath("//div[@class='graphicGapMatchInteraction']//map/area[@data-qti-id='" + gap + "']");
		WebElement targetEl = browser.findElement(targetBy);
		targetEl.click();
		return this;
	}
	
	/**
	 * Select the point based on coordinates.
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return Itself
	 */
	public QTI21Page answerSelectPoint(int x, int y) {
		By canvasBy = By.xpath("//div[contains(@class,'selectPointInteraction')]/div/canvas");
		WebElement canvasEl = browser.findElement(canvasBy);
		new Actions(browser)
			.moveToElement(canvasEl, x, y)
			.click()
			.build()
			.perform();
		return this;
	}
	
	/**
	 * Select the object by its index (start with 0) and
	 * move it on the image and the specified coodinates.
	 * 
	 * @param index The index of the object
	 * @param x The x target coordinate
	 * @param y The y target coordinate
	 * @return Itself
	 */
	public QTI21Page answerPositionObject(int index, int x, int y) {
		By itemBy = By.xpath("//div[contains(@class,'positionObjectStage')]//div[@id='object-item-" + index + "']");
		OOGraphene.waitElement(itemBy, browser);
		WebElement itemEl = browser.findElement(itemBy);
		By targetBy = By.xpath("//div[@class='positionObjectStage']//img[contains(@id,'qtiworks_id_container_')]");
		WebElement targetEl = browser.findElement(targetBy);
		new Actions(browser)
			.moveToElement(itemEl, 5, 5)
			.clickAndHold()
			.moveToElement(targetEl, x, y)
			.release()
			.build()
			.perform();
		return this;
	}
	
	/**
	 * Select the point based on coordinates.
	 * 
	 * @param x The x coordinate
	 * @return Itself
	 */
	public QTI21Page answerVerticalSlider(int val) {
		By sliderBy = By.xpath("//div[contains(@class,'sliderInteraction')]/div[contains(@class,'sliderVertical')]/div[contains(@class,'sliderWidget')]");
		OOGraphene.waitElement(sliderBy, browser);
		WebElement sliderEl = browser.findElement(sliderBy);
		Dimension size = sliderEl.getSize();
		float height = (size.getHeight() / 100f) * val;
		float scaledY = size.getHeight() - height;
		
		new Actions(browser)
			.moveToElement(sliderEl, 5, Math.round(scaledY))
			.click()
			.build()
			.perform();
		
		
		By valueBy = By.xpath("//div[contains(@class,'sliderInteraction')]/div[contains(@class,'sliderVertical')]/div[contains(@class,'sliderValue')]/span[text()='" + val + "']");
		OOGraphene.waitElement(valueBy, browser);
		return this;
	}
	
	public QTI21Page answerUpload(File file) {
		By inputBy = By.cssSelector(".uploadInteraction input[type='file']");
		OOGraphene.uploadFile(inputBy, file, browser);
		return this;
	}
	
	public QTI21Page answerEssay(String text) {
		By inputBy = By.cssSelector(".extendedTextInteraction textarea");
		WebElement essayEl = browser.findElement(inputBy);
		essayEl.clear();
		essayEl.sendKeys(text);
		return this;
	}
	
	/**
	 * Draw a line of the house.
	 * @return Itself
	 */
	public QTI21Page answerDrawing() {
		By drawingBy = By.xpath("//div[contains(@class,'drawingInteraction')]//canvas[@id='tmp_canvas']");
		WebElement drawingEl = browser.findElement(drawingBy);
		
		new Actions(browser)
			.moveToElement(drawingEl, 30, 30)
			.clickAndHold()
			.moveByOffset(260, 100)
			.release()
			.build()
			.perform();
		
		OOGraphene.waitingALittleBit();
		return this;
	}
	
	public QTI21Page saveAnswer() {
		By saveAnswerBy = By.cssSelector("button.o_sel_assessment_item_submit");
		browser.findElement(saveAnswerBy).click();
		OOGraphene.waitBusy(browser);
		return this;
	}
	
	public QTI21Page saveAnswerMoveAndScrollTop() {
		By saveAnswerBy = By.cssSelector("button.o_sel_assessment_item_submit");
		OOGraphene.click(saveAnswerBy, browser);
		OOGraphene.waitBusy(browser);
		OOGraphene.scrollTop(browser);
		return this;
	}
	
	public QTI21Page nextAnswer() {
		By nextAnswerBy = By.cssSelector("button.o_sel_next_question");
		browser.findElement(nextAnswerBy).click();
		OOGraphene.waitBusy(browser);
		return this;
	}
	
	/**
	 * Check if the feedback with the specified title is visible.
	 * 
	 * @param title Title of the feedback
	 * @return Itself
	 */
	public QTI21Page assertFeedback(String title) {
		By feedbackBy = By.xpath("//div[contains(@class,'modalFeedback')]/h4[contains(text(),'" + title + "')]");
		OOGraphene.waitElement(feedbackBy, 5, browser);
		return this;
	}
	
	/**
	 * Check if the feedback with the specified content is visible.
	 * 
	 * @param text Text of the feedback.
	 * @return Itself
	 */
	public QTI21Page assertFeedbackText(String text) {
		By feedbackBy = By.xpath("//div[contains(@class,'modalFeedback')][text()[contains(normalize-space(.),'" + text + "')]]");
		OOGraphene.waitElement(feedbackBy, 5, browser);
		return this;
	}
	
	/**
	 * Check if an inline feedback with the specified text
	 * is visible.
	 * 
	 * @param text The text of the inline feedback
	 * @return Itself
	 */
	public QTI21Page assertFeedbackInline(String text) {
		By feedbackBy = By.xpath("//span[contains(@class,'feedbackInline')][text()[contains(normalize-space(.),'" + text + "')]]");
		OOGraphene.waitElement(feedbackBy, 5, browser);
		return this;
	}
	
	/**
	 * Check that no inline feedback with the specified text
	 * is visible.
	 * 
	 * @param text The text of the inline feedback
	 * @return Itself
	 */
	public QTI21Page assertNoFeedbackInline(String text) {
		By feedbackBy = By.xpath("//span[contains(@class,'feedbackInline')][text()[contains(normalize-space(.),'" + text + "')]]");
		OOGraphene.waitElementDisappears(feedbackBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertNoFeedback(String title) {
		By feedbackBy = By.xpath("//div[contains(@class,'modalFeedback')]/h4[contains(text(),'" + title + "')]");
		OOGraphene.waitElementDisappears(feedbackBy, 5, browser);
		return this;
	}
	
	/**
	 * 
	 * @param text The text of the feedback
	 * @return Itself
	 */
	public QTI21Page assertNoFeedbackText(String text) {
		By feedbackBy = By.xpath("//div[contains(@class,'modalFeedback')][text()[contains(normalize-space(.),'\" + text + \"')]]");
		OOGraphene.waitElementDisappears(feedbackBy, 5, browser);
		return this;
	}
	
	/**
	 * Check that there are no feedbacks visible.
	 * 
	 * @return Itself
	 */
	public QTI21Page assertNoFeedback() {
		By feedbackBy = By.xpath("//div[contains(@class,'modalFeedback')]/h4");
		OOGraphene.waitElementDisappears(feedbackBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertCorrectSolution(String title) {
		By feedbackBy = By.xpath("//div[contains(@class,'modalFeedback')]/h4/a[contains(text(),'" + title + "')]");
		OOGraphene.waitElement(feedbackBy, 5, browser);
		return this;
	}
	
	public QTI21Page hint() {
		By hintBy = By.cssSelector("a.o_sel_assessment_item_hint");
		browser.findElement(hintBy).click();
		OOGraphene.waitBusy(browser);
		return this;
	}
	
	public QTI21Page endTestPart() {
		return endTest();
	}
	
	public QTI21Page suspendTest() {
		By suspendBy = By.cssSelector("a.o_sel_suspend_test");
		browser.findElement(suspendBy).click();
		OOGraphene.waitBusy(browser);
		confirm();
		OOGraphene.waitAndCloseBlueMessageWindow(browser);
		return this;
	}
	
	public QTI21Page endTest() {
		By endBy = By.cssSelector("a.o_sel_end_testpart");
		OOGraphene.clickAndWait(endBy, browser);
		confirm();
		return this;
	}
	
	public QTI21Page closeTest() {
		By closeBy = By.cssSelector("a.o_sel_close_test");
		OOGraphene.waitElement(closeBy, 5, browser);
		browser.findElement(closeBy).click();
		OOGraphene.waitBusy(browser);
		confirm();
		return this;
	}
	
	/**
	 * Close the report with the assessment results.
	 * 
	 * @return Itself
	 */
	public QTI21Page closeAssessmentResults() {
		By closeBy = By.cssSelector("a.o_sel_close_results");
		OOGraphene.waitElement(closeBy, 5, browser);
		browser.findElement(closeBy).click();
		OOGraphene.waitBusy(browser);
		return this;
	}
	
	public QTI21Page showAssessmentResults() {
		By showBy = By.cssSelector("a.o_qti_show_assessment_results");
		OOGraphene.waitElement(showBy, 5, browser);
		browser.findElement(showBy).click();
		OOGraphene.waitBusy(browser);
		
		By hideBy = By.cssSelector("a.o_qti_hide_assessment_results");
		OOGraphene.waitElement(hideBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertHiddenSection() {
		By sectionBy = By.cssSelector("li.o_assessmentsection.o_qti_menu_item>header>h4");
		List<WebElement> sectionEls = browser.findElements(sectionBy);
		Assert.assertEquals(0, sectionEls.size());
		return this;
	}
	
	/**
	 * This check specifically if the metadata of the test are visible.
	 * 
	 * @return Itself
	 */
	public QTI21Page assertOnAssessmentResults() {
		By resultsBy = By.cssSelector("div.o_sel_results_details");
		OOGraphene.waitElement(resultsBy, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentTestResults() {
		try {
			By resultsBy = By.cssSelector("div.o_sel_results_details");
			OOGraphene.waitElement(resultsBy, browser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * This check specifically if the metadata of the test are visible.
	 * 
	 * @param timeout
	 * @return Itself
	 */
	public QTI21Page assertOnAssessmentResults(int timeout) {
		By resultsBy = By.cssSelector("div.o_sel_results_details");
		OOGraphene.waitElement(resultsBy, timeout, browser);
		return this;
	}
	
	public QTI21Page assertOnCourseAssessmentTestScore(int score) {
		By resultsBy = By.xpath("//div[contains(@class,'o_personal')]//tr[contains(@class,'o_score')]/td[contains(text(),'" + score + "')]");
		OOGraphene.waitElement(resultsBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnCourseAssessmentTestWaitingCorrection() {
		By resultsBy = By.xpath("//div[contains(@class,'o_personal')]//tr[contains(@class,'o_score')]/td/span[@id='o_score_in_review']");
		OOGraphene.waitElement(resultsBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentTestScore(int score) {
		By resultsBy = By.xpath("//div[contains(@class,'o_sel_results_details')]//tr[contains(@class,'o_sel_assessmenttest_scores')]/td/div/span[contains(@class,'o_sel_assessmenttest_score')][contains(text(),'" + score + "')]");
		OOGraphene.waitElement(resultsBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentItemScore(String title, int score) {
		By resultsBy = By.xpath("//div[contains(@class,'o_qti_item')][div/h4[text()[contains(.,'" + title + "')]]]//tr[contains(@class,'o_sel_assessmentitem_score')]//span[@class='o_sel_assessmentitem_score'][contains(text(),'" + score + "')]");
		OOGraphene.waitElement(resultsBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentTestScore(String score) {
		By resultsBy = By.xpath("//div[contains(@class,'o_sel_results_details')]//tr[contains(@class,'o_sel_assessmenttest_scores')]/td/div/span[contains(@class,'o_sel_assessmenttest_score')][contains(text(),'" + score + "')]");
		OOGraphene.waitElement(resultsBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentTestPassed() {
		By notPassedBy = By.cssSelector("div.o_sel_results_details tr.o_qti_stateinfo.o_passed");
		OOGraphene.waitElement(notPassedBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentTestNotPassed() {
		By notPassedBy = By.cssSelector("div.o_sel_results_details tr.o_qti_stateinfo.o_failed");
		OOGraphene.waitElement(notPassedBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentTestMaxScore(int score) {
		By resultsBy = By.xpath("//div[contains(@class,'o_sel_results_details')]//tr[contains(@class,'o_sel_assessmenttest_scores')]/td/div/span[contains(@class,'o_sel_assessmenttest_maxscore')][contains(text(),'" + score + "')]");
		OOGraphene.waitElement(resultsBy, 5, browser);
		return this;
	}
	
	/**
	 * 
	 * @param name The name of the file without extension
	 * @return Itself
	 */
	public QTI21Page assertOnAssessmentResultUpload(String name) {
		By uploadBy = By.xpath("//div[contains(@class,'o_assessment_test_results')]//div[contains(@class,'uploadInteraction')]/a[contains(@href,'" + name + "')]");
		OOGraphene.waitElement(uploadBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentResultEssay(String text) {
		By uploadBy = By.xpath("//div[contains(@class,'o_assessment_test_results')]//div[contains(@class,'extendedTextInteraction')]/div[contains(text(),'" + text + "')]");
		OOGraphene.waitElement(uploadBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnDrawing() {
		By drawingBy = By.className("drawingInteraction");
		OOGraphene.waitElement(drawingBy, 5, browser);
		return this;
	}
	
	public QTI21Page assertOnAssessmentTestFeedback(String feedback) {
		By feedbackBy = By.xpath("//div[contains(@class,'o_info')]/h3[contains(text(),'" + feedback + "')]");
		OOGraphene.waitElement(feedbackBy, 5, browser);
		List<WebElement> feedbackEls = browser.findElements(feedbackBy);
		Assert.assertEquals(1, feedbackEls.size());
		return this;
	}
	
	/**
	 * Check if the assessment terminated message is visible.
	 * 
	 * @return Itself
	 */
	public QTI21Page assertOnAssessmentTestTerminated() {
		By terminatedBy = By.cssSelector("div.o_sel_assessment_test_terminated");
		OOGraphene.waitElement(terminatedBy, 5, browser);
		return this;
	}
	
	/**
	 * Check if the assessment terminated message is visible.
	 * 
	 * @return Itself
	 */
	public QTI21Page assertOnAssessmentTestTerminated(int timeout) {
		By terminatedBy = By.cssSelector("div.o_sel_assessment_test_terminated");
		OOGraphene.waitElement(terminatedBy, timeout, browser);
		return this;
	}
	
	/**
	 * Yes in a dialog box controller.
	 */
	private void confirm() {
		OOGraphene.waitBusyAndScrollTop(browser);
		// confirm
		By confirmButtonBy = By.cssSelector("div.modal-dialog div.modal-footer a");
		OOGraphene.waitElement(confirmButtonBy, 5, browser);
		List<WebElement> buttonsEl = browser.findElements(confirmButtonBy);
		buttonsEl.get(0).click();
		OOGraphene.waitBusy(browser);
	}
	
	public QTI21EditorPage edit() {
		if(!browser.findElement(toolsMenu).isDisplayed()) {
			openToolsMenu();
		}

		By editBy = By.xpath("//ul[contains(@class,'o_sel_repository_tools')]//a[contains(@onclick,'edit.cmd')]");
		browser.findElement(editBy).click();
		OOGraphene.waitBusy(browser);
		QTI21EditorPage editor = new QTI21EditorPage(browser);
		editor.assertOnEditor();
		return editor;
	}
	
	public QTI21OptionsPage options() {
		if(!browser.findElement(settingsMenu).isDisplayed()) {
			openSettingsMenu();
		}
		
		By optionsBy = By.cssSelector("ul.o_sel_course_settings a.o_sel_qti_resource_options");
		OOGraphene.waitElement(optionsBy, browser);
		browser.findElement(optionsBy).click();
		OOGraphene.waitBusy(browser);
		return new QTI21OptionsPage(browser);
	}
	
	/**
	 * Open the access configuration
	 * 
	 * @return
	 */
	public RepositoryAccessPage accessConfiguration() {
		if(!browser.findElement(settingsMenu).isDisplayed()) {
			openSettingsMenu();
		}
		By accessConfigBy = By.cssSelector("a.o_sel_course_access");
		browser.findElement(accessConfigBy).click();
		OOGraphene.waitBusy(browser);

		By mainId = By.id("o_main_container");
		OOGraphene.waitElement(mainId, 5, browser);
		return new RepositoryAccessPage(browser);
	}
	
	/**
	 * Click the editor link in the tools drop-down
	 * @return
	 */
	public QTI21Page openToolsMenu() {
		By toolsMenuCaret = By.cssSelector("a.o_sel_repository_tools");
		browser.findElement(toolsMenuCaret).click();
		OOGraphene.waitElement(toolsMenu, browser);
		return this;
	}
	
	public QTI21Page openSettingsMenu() {
		By settingsMenuCaret = By.cssSelector("a.o_sel_course_settings");
		OOGraphene.waitElement(settingsMenuCaret, browser);
		browser.findElement(settingsMenuCaret).click();
		OOGraphene.waitElement(settingsMenu, browser);
		return this;
	}
	
	public QTI21Page clickToolbarBack() {
		OOGraphene.closeBlueMessageWindow(browser);
		browser.findElement(NavigationPage.toolbarBackBy).click();
		OOGraphene.waitBusy(browser);
		return QTI21Page.getQTI21Page(browser);
	}
	
	public enum TrueFalse {
		unanswered,
		right,
		wrong
	}
}