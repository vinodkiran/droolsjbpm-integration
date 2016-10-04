/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.spring.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.spring.beans.Person;
import org.kie.spring.beans.SampleBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class KieSpringBasics2Test {

    static ApplicationContext context = null;

    @BeforeClass
    public static void setup() {
        context = new ClassPathXmlApplicationContext("org/kie/spring/beans-internal2.xml");
    }

    @Test
    public void testContext() throws Exception {
        assertNotNull(context);
    }

    @Test
    public void testKieBase() throws Exception {
        KieBase kbase = (KieBase) context.getBean("drl_kiesample3");
        assertNotNull(kbase);
    }

    @Test
    public void testStatelessKieSession() throws Exception {
        StatelessKieSession ksession = (StatelessKieSession) context.getBean("ksession1");
        assertNotNull(ksession);
    }

    @Test
    public void testKieSession() throws Exception {
        KieSession ksession = (KieSession) context.getBean("ksession2");
        assertNotNull(ksession);
    }

    @Test
    public void testKSessionExecution() throws Exception {
        StatelessKieSession ksession = (StatelessKieSession) context.getBean("ksession1");
        assertNotNull(ksession);
        Person person = new Person("HAL", 42);
        person.setHappy(false);
        ksession.execute(person);
        assertTrue(person.isHappy());

        StatelessKieSession ksession2 = (StatelessKieSession) context.getBean("ksession1");
        assertSame( ksession, ksession2 );
    }

    @Test
    public void testPrototypeKSessionExecution() throws Exception {
        StatelessKieSession ksession = (StatelessKieSession) context.getBean("ksession1p");
        assertNotNull(ksession);
        Person person = new Person("HAL", 42);
        person.setHappy(false);
        ksession.execute(person);
        assertTrue(person.isHappy());

        StatelessKieSession ksession2 = (StatelessKieSession) context.getBean("ksession1p");
        assertNotSame( ksession, ksession2 );

        assertNotNull(ksession2);
        person = new Person("HAL", 42);
        person.setHappy(false);
        ksession2.execute(person);
        assertTrue(person.isHappy());
    }

    @Test
    public void testKSessionInjection() throws Exception {
        SampleBean sampleBean = (SampleBean) context.getBean("sampleBean");
        assertNotNull(sampleBean);
        assertNotNull(sampleBean.getKieSession() );
        assertTrue(sampleBean.getKieSession() instanceof StatelessKieSession);
    }

    @AfterClass
    public static void tearDown() {

    }

}
