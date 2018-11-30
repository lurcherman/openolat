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

package org.olat.registration;

import org.olat.basesecurity.BaseSecurityManager;
import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.form.flexible.FormItemContainer;
import org.olat.core.gui.components.form.flexible.elements.TextElement;
import org.olat.core.gui.components.form.flexible.impl.FormBasicController;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.Event;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.id.Identity;
import org.olat.core.util.Util;
import org.olat.login.auth.OLATAuthManager;
import org.olat.user.ChangePasswordForm;
import org.olat.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * 
 * @author Sabina Jeger
 */
public class PwChangeForm extends FormBasicController {

	private TextElement newpass1;
	private TextElement newpass2; // confirm
	
	private final TemporaryKey tempKey;
	private Identity identityToChange;
	
	@Autowired
	private RegistrationManager rm;
	@Autowired
	private UserManager userManager;
	@Autowired
	private BaseSecurityManager securityManager;
	@Autowired
	private OLATAuthManager olatAuthenticationSpi;
	
	public PwChangeForm(UserRequest ureq, WindowControl wControl, Identity identityToChange, TemporaryKey tempKey) {
		super(ureq, wControl, null, Util.createPackageTranslator(ChangePasswordForm.class, ureq.getLocale()));
		this.identityToChange = identityToChange;
		this.tempKey = tempKey;
		initForm(ureq);
	}
	
	public PwChangeForm(UserRequest ureq, WindowControl wControl, TemporaryKey tempKey) {
		super(ureq, wControl, null, Util.createPackageTranslator(ChangePasswordForm.class, ureq.getLocale()));
		this.tempKey = tempKey;
		initForm(ureq);
	}

	@Override
	public boolean validateFormLogic(UserRequest ureq) {
		boolean allOk = super.validateFormLogic(ureq);
		
		newpass1.clearError();
		if (!userManager.syntaxCheckOlatPassword(newpass1.getValue())) {
			newpass1.setErrorKey("form.checkPassword", null);
			allOk &= false;
		} else if(!olatAuthenticationSpi.checkCredentialHistory(getIdentityToChange(), newpass1.getValue())) {
			newpass1.setErrorKey("form.checkPassword.history", null);
			allOk &= false;
		}
		
		// validate that both passwords are the same
		newpass2.clearError();
		if (!newpass1.getValue().equals(newpass2.getValue())) {
			newpass2.setErrorKey("form.password.error.nomatch", null);
			allOk &= false;
		}
		
		return allOk;
	}

	@Override
	protected void formOK(UserRequest ureq) {
		Identity identToChange = getIdentityToChange();
		changeIdentity(identToChange);
		fireEvent (ureq, Event.DONE_EVENT);
	}

	private Identity getIdentityToChange() {
		Identity identToChange;
		if(tempKey != null) {
			identToChange = securityManager.loadIdentityByKey(tempKey.getIdentityKey());
			rm.deleteTemporaryKeyWithId(tempKey.getRegistrationKey());	
		} else {
			identToChange = identityToChange;
		}
		return identToChange;
	}

	private void changeIdentity(Identity identToChange) {
		if(identToChange != null && !saveFormData(identToChange)) {
			showError("password.failed");
		}
	}

	/**
	 * Saves the form data in the user object and the database
	 * 
	 * @param s The identity to change the password.
	 */
	private boolean saveFormData(Identity s) {
		return olatAuthenticationSpi.changePasswordByPasswordForgottenLink(s, newpass1.getValue());	
	}

	@Override
	protected void initForm(FormItemContainer formLayout, Controller listener, UserRequest ureq) {
		setFormTitle("form.password.enter.new");
		newpass1 = uifactory.addPasswordElement("newpass1",  "form.password.new1", 128, "", formLayout);
		newpass1.setAutocomplete("new-password");
		newpass2 = uifactory.addPasswordElement("newpass2",  "form.password.new2", 128, "", formLayout);
		newpass2.setAutocomplete("new-password");
		uifactory.addFormSubmitButton("submit", formLayout);
	}

	@Override
	protected void doDispose() {
		//
	}
}