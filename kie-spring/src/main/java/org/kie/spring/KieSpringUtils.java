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

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public final class KieSpringUtils {
    public static final String KIE_SPRING_ID_PREFIX = "~kmodule~";

    private KieSpringUtils(){ }

    private static Map<KieModuleModel, KieSpringApplicationContext> kieModuleApplicationContextMap = new HashMap<KieModuleModel, KieSpringApplicationContext>();
    private static Map<ReleaseId, KieModuleModel> kieModuleModelReleaseIdMap = new HashMap<ReleaseId, KieModuleModel>();

    static void addKieModuleSpringContext(KieModuleModel kieModule, ApplicationContext context){
        kieModuleApplicationContextMap.put(kieModule, new KieSpringApplicationContext(context));
    }

    public static void setReleaseIdForKieModuleModel(ReleaseId releaseId, KieModuleModel kieModuleModel){
        kieModuleModelReleaseIdMap.put(releaseId, kieModuleModel);
    }

    public static ApplicationContext getSpringContext(ReleaseId releaseId) {
        System.out.println("****************getSpringContext**********");
        if ( releaseId == null) {
            if ( kieModuleModelReleaseIdMap.size() == 1) {
                releaseId = kieModuleModelReleaseIdMap.keySet().iterator().next();
                KieModuleModel kieModuleModel = kieModuleModelReleaseIdMap.get(releaseId);
                KieSpringApplicationContext context = kieModuleApplicationContextMap.get(kieModuleModel);
                if ( context != null ){
                    context.initialize(releaseId);
                }
                return context;
            }
            KieServices ks = KieServices.Factory.get();
            KieContainerImpl kContainer = (KieContainerImpl) ks.getKieClasspathContainer();
            kContainer.getKieProject();
            releaseId = kContainer.getReleaseId();
        }
        KieModuleModel kieModuleModel = kieModuleModelReleaseIdMap.get(releaseId);
        if ( kieModuleModel != null ) {
            KieSpringApplicationContext context = kieModuleApplicationContextMap.get(kieModuleModel);
            if ( context != null ){
                context.initialize(releaseId);
            }
            return context;
        }
        return null;
    }

    public static ApplicationContext getDefaultSpringContext() {
        return getSpringContext(null);
    }

    static KieServices ks;
    static {
        //this forces the KieModules to be loaded up.
        ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
    }
}
