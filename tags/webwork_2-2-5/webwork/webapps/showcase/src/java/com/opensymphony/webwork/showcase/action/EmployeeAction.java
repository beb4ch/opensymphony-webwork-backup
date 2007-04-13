/* ====================================================================
 * The OpenSymphony Software License, Version 1.1
 *
 * (this license is derived and fully compatible with the Apache Software
 * License - see http://www.apache.org/LICENSE.txt)
 *
 * Copyright (c) 2001-2005 The OpenSymphony Group. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        OpenSymphony Group (http://www.opensymphony.com/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "OpenSymphony" and "The OpenSymphony Group"
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact license@opensymphony.com .
 *
 * 5. Products derived from this software may not be called "OpenSymphony"
 *    or "WebWork", nor may "OpenSymphony" or "WebWork" appear in their
 *    name, without prior written permission of the OpenSymphony Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
package com.opensymphony.webwork.showcase.action;

import com.opensymphony.webwork.showcase.dao.Dao;
import com.opensymphony.webwork.showcase.dao.EmployeeDao;
import com.opensymphony.webwork.showcase.model.Employee;
import com.opensymphony.webwork.showcase.model.Skill;
import com.opensymphony.webwork.showcase.application.TestDataProvider;
import com.opensymphony.xwork.Preparable;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * EmployeeAction.
 *
 * @author <a href="mailto:gielen@it-neering.net">Rene Gielen</a>
 */

public class EmployeeAction extends AbstractCRUDAction implements Preparable {

    private static final Logger log = Logger.getLogger(EmployeeAction.class);

    private Long empId;
    protected EmployeeDao employeeDao;
    private Employee currentEmployee;
    private List selectedSkills;

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public String[] getAvailablePositions() {
        return TestDataProvider.POSITIONS;
    }

    public List getAvailableLevels() {
        return Arrays.asList(TestDataProvider.LEVELS);
    }

    public List getSelectedSkills() {
        return selectedSkills;
    }

    public void setSelectedSkills(List selectedSkills) {
        this.selectedSkills = selectedSkills;
    }

    protected Dao getDao() {
        return employeeDao;
    }

    public void setEmployeeDao(EmployeeDao employeeDao) {
        if (log.isDebugEnabled()) {
            log.debug("EmployeeAction - [setEmployeeDao]: employeeDao injected.");
        }
        this.employeeDao = employeeDao;
    }

    /**
     * This method is called to allow the action to prepare itself.
     *
     * @throws Exception thrown if a system level exception occurs.
     */
    public void prepare() throws Exception {
        Employee preFetched = (Employee) fetch(getEmpId(), getCurrentEmployee());
        if (preFetched != null) {
            setCurrentEmployee(preFetched);
        }
    }

    public String execute() throws Exception {
        if (getCurrentEmployee() != null && getCurrentEmployee().getOtherSkills()!=null) {
            setSelectedSkills(new ArrayList());
            Iterator it = getCurrentEmployee().getOtherSkills().iterator();
            while (it.hasNext()) {
                getSelectedSkills().add(((Skill) it.next()).getName());
            }
        }
        return super.execute();
    }

    public String save() throws Exception {
        if (getCurrentEmployee() != null) {
            setEmpId((Long) employeeDao.merge(getCurrentEmployee()));
            employeeDao.setSkills(getEmpId(), getSelectedSkills());
        }
        return SUCCESS;
    }

}
