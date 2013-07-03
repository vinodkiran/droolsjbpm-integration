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

package org.kie.spring.kmodule.factorybeans;

import org.drools.compiler.kproject.models.KieBaseModelImpl;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.kie.api.KieBase;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.spring.KieObjectsResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class KBaseFactoryBean
        implements
        FactoryBean<KieBaseModel>,
        InitializingBean {

    private KieBaseModelImpl kBase;
    private String id;
    private KieModuleModelImpl kModule;
    private String kBaseName;


    public String getKBaseName() {
        return kBaseName;
    }

    public void setKBaseName(String name) {
        this.kBaseName = name;
    }

    public KieModuleModelImpl getKModule() {
        return kModule;
    }

    public void setKModule(KieModuleModelImpl kModule) {
        this.kModule = kModule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public KieBaseModelImpl getObject() throws Exception {
        return kBase;
    }

    public Class<? extends KieBaseModel> getObjectType() {
        return KieBaseModelImpl.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        kBase = new KieBaseModelImpl();
        kBase.setKModule(kModule);
        kBase.setName(kBaseName);
        kModule.getRawKieBaseModels().put( kBase.getName(), kBase );
    }

}
