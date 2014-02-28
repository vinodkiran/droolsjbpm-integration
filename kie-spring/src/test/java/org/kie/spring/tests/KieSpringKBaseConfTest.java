/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
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
import org.junit.runner.RunWith;
import org.kie.internal.conf.AlphaThresholdOption;
import org.kie.internal.conf.SequentialAgendaOption;
import org.kie.internal.conf.SequentialOption;
import org.kie.spring.KieBaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@ContextConfiguration({"classpath:/org/kie/spring/kbase-sequential.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class KieSpringKBaseConfTest {

    @Autowired
    ApplicationContext context = null;

    @Autowired
    KieBaseConfig kieBaseSpringConfiguration = null;


    @BeforeClass
    public static void setup() {

    }

    @Test
    public void testContext() throws Exception {
        assertNotNull(context);
    }

    @Test
    public void testKieBaseSpringConfiguration() throws Exception {
        assertNotNull(kieBaseSpringConfiguration);
    }

    @Test
    public void testConfSequential() throws Exception {
        assertTrue(kieBaseSpringConfiguration.isSequential());
        SequentialOption option = kieBaseSpringConfiguration.getConf().getOption(SequentialOption.class);
        assertNotNull(option);

        assertTrue(option.isSequential());
    }

    @Test
    public void testConfSequentialAgenda() throws Exception {
        assertNotNull(kieBaseSpringConfiguration.getSequentialAgenda());
        SequentialAgendaOption option = kieBaseSpringConfiguration.getConf().getOption(SequentialAgendaOption.class);
        assertNotNull(option);

        assertTrue(option.compareTo(SequentialAgendaOption.SEQUENTIAL)==0);
    }

    @Test
    public void testAlphaNodeHashingThreshold() throws Exception {
        assertEquals(2, kieBaseSpringConfiguration.getAlphaNodeHashingThreshold());
        AlphaThresholdOption option = kieBaseSpringConfiguration.getConf().getOption(AlphaThresholdOption.class);
        assertNotNull(option);

        assertEquals(2, option.getThreshold());
    }

    @AfterClass
    public static void tearDown() {

    }

}
