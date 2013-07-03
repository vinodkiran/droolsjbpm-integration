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
import org.drools.compiler.kproject.models.KieSessionModelImpl;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieSessionModel;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KSessionFactoryBean
        implements
        FactoryBean<KieSessionModel>,
        InitializingBean {

    private KieSessionModel kSession;
    private String id;
    private String type;
    private KieBaseModelImpl kBase;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KieBaseModelImpl getKBase() {
        return kBase;
    }

    public void setKBase(KieBaseModelImpl kBase) {
        this.kBase = kBase;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public KieSessionModel getObject() throws Exception {
        return kSession;
    }

    public Class<? extends KieSessionModel> getObjectType() {
        return KieSessionModel.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        kSession = new KieSessionModelImpl(kBase, name);
        kSession.setType(type != null ? KieSessionModel.KieSessionType.valueOf(type.toUpperCase()) : KieSessionModel.KieSessionType.STATEFUL);

        try {
            Map<String, KieSessionModel> rawKieSessionModels = kBase.getRawKieSessionModels();
            rawKieSessionModels.put(kSession.getName(), kSession);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
