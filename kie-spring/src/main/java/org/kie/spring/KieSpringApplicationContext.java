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
import org.kie.api.runtime.KieContainer;
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

    protected boolean initialized;
    private Map<ReleaseId, KieContainer> gavs;
    private ReleaseId releaseId;
    KieObjectsResolver objectsResolver = null;

    boolean isInitialized() {
        return initialized;
    }

//    @Override
//    public Object getBean(String name) throws BeansException {
//        try {
//            Object obj = super.getBean(name);
//            System.out.println("************user asked for "+name+", and we found **********"+obj);
//            return obj;
//        } catch ( NoSuchBeanDefinitionException nsbde) {
//            if ( super.containsBean(KieSpringUtils.KIE_SPRING_ID_PREFIX+name)){
//                Object modelBean = super.getBean(KieSpringUtils.KIE_SPRING_ID_PREFIX+name);
//                if (modelBean instanceof KieBaseModel){
//                    System.out.println("************user asked for "+name+", and we found **********"+modelBean);
//                    KieBaseModel kieBaseModel = (KieBaseModel)modelBean;
//                    KieBase kieBase = objectsResolver.resolveKBase(kieBaseModel.getName(), releaseId);
//                    if (kieBase != null) {
//                        getBeanFactory().registerSingleton(kieBaseModel.getName(), kieBase);
//                        return kieBase;
//                    }
//                }
//            }
//        }
//        return null;
//    }

    public void initialize(ReleaseId releaseId) {
        this.releaseId = releaseId;

        System.out.println("************KieSpringApplicationContext::start-initialize******");
        StopWatch sw = new StopWatch();
        sw.start();
        objectsResolver = KieObjectsResolver.get();
        if ( getParentBeanFactory() instanceof ApplicationContext) {
            Map<String, KieBaseModel> kieBaseModelBeans = ((ApplicationContext)getParentBeanFactory()).getBeansOfType(KieBaseModel.class);
            for ( String beanName : kieBaseModelBeans.keySet() ){
                if (beanName.startsWith(KieSpringUtils.KIE_SPRING_ID_PREFIX)) {
                    System.out.println("************inside initialize. Found bean ::"+beanName+"******");
                    Object obj = kieBaseModelBeans.get(beanName);
                    if (obj != null){
                        KieBaseModel kieBaseModel = (KieBaseModel)obj;
                        String kBaseName = kieBaseModel.getName();
                        KieBase kieBase = objectsResolver.resolveKBase(kBaseName, releaseId);
                        if (kieBase != null) {
                            getBeanFactory().registerSingleton(kieBaseModel.getName(), kieBase);
                        }
                    }
                }
            }
        }
        this.initialized = true;
        sw.stop();
        System.out.println("************KieSpringApplicationContext::initialize-completed. Took "+sw.getLastTaskTimeMillis()+" Millis.******");
    }

    public KieSpringApplicationContext(ApplicationContext parent) throws BeansException {
        super(parent);
    }


}
