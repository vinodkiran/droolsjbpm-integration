/*
 * Copyright 2013 JBoss Inc
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

package org.kie.spring;

import org.drools.compiler.kie.builder.impl.AbstractKieModule;
import org.drools.compiler.kproject.models.KieBaseModelImpl;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.StopWatch;

import java.util.Map;

public class KieSpringApplicationContext extends StaticApplicationContext {

    private static final Logger log               = LoggerFactory.getLogger(KieSpringApplicationContext.class);

    protected boolean initialized;
    private ReleaseId releaseId;
    KieObjectsResolver objectsResolver = null;

    boolean isInitialized() {
        return initialized;
    }

    public void initialize(ReleaseId releaseId) {
        if ( isInitialized() ) {
            return;
        }
        this.releaseId = releaseId;

        StopWatch sw = new StopWatch();
        sw.start();
        objectsResolver = KieObjectsResolver.get();
        if ( getParentBeanFactory() instanceof ApplicationContext) {
            Map<String, KieBaseModel> kieBaseModelBeans = ((ApplicationContext)getParentBeanFactory()).getBeansOfType(KieBaseModel.class);
            for ( String beanName : kieBaseModelBeans.keySet() ){
                if (beanName.startsWith(KieSpringUtils.KIE_SPRING_ID_PREFIX)) {
                    Object obj = kieBaseModelBeans.get(beanName);
                    if (obj != null){
                        KieBaseModel kieBaseModel = (KieBaseModel)obj;
                        String kBaseName = kieBaseModel.getName();
                        KieBase kieBase = objectsResolver.resolveKBase(kBaseName, releaseId);
                        if (kieBase != null) {
                            getBeanFactory().registerSingleton(kBaseName, kieBase);
                        }
                    }
                }
            }
            Map<String, KieSessionModel> kieSessionBeans = ((ApplicationContext)getParentBeanFactory()).getBeansOfType(KieSessionModel.class);
            for ( String beanName : kieSessionBeans.keySet() ){
                if (beanName.startsWith(KieSpringUtils.KIE_SPRING_ID_PREFIX)) {
                    Object obj = kieSessionBeans.get(beanName);
                    if (obj != null){
                        KieSessionModel kieSessionModel = (KieSessionModel)obj;
                        String kieSessionModelName = kieSessionModel.getName();
                        Object kieSession = objectsResolver.resolveKSession(kieSessionModelName, releaseId);
                        if (kieSession != null) {
                            getBeanFactory().registerSingleton(kieSessionModelName, kieSession);
                        }
                    }
                }
            }
        }
        this.initialized = true;
        sw.stop();
        log.debug("KieSpringApplicationContext::Initialized. Took "+sw.getLastTaskTimeMillis()+" Millis.");
    }

    public KieSpringApplicationContext(ApplicationContext parent) throws BeansException {
        super(parent);
    }


}
