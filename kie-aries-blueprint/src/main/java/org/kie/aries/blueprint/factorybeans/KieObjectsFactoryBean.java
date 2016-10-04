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
package org.kie.aries.blueprint.factorybeans;

import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.impl.EnvironmentFactory;
import org.drools.core.marshalling.impl.ClassObjectMarshallingStrategyAcceptor;
import org.drools.persistence.jpa.KnowledgeStoreServiceImpl;
import org.drools.persistence.jpa.marshaller.JPAPlaceholderResolverStrategy;
import org.kie.api.KieBase;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.marshalling.ObjectMarshallingStrategy;
import org.kie.api.persistence.jpa.KieStoreServices;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.EnvironmentName;
import org.kie.aries.blueprint.helpers.JPAPlaceholderResolverStrategyHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KieObjectsFactoryBean {

    static final ImportInjector importInjector = new ImportInjector();

    public static Object fetchKBase(String id, ReleaseId releaseId, KBaseOptions kbaseOptions) {
        return new KieBaseResolver(releaseId, id);
    }

    public static Object fetchKContainer(ReleaseId releaseId){
        return new KieContainerResolver(releaseId);
    }

    public static Object createKieSessionRef(String id, ReleaseId releaseId, List<KieListenerAdaptor> listeners, List<KieLoggerAdaptor> loggers, List<?> commands){
        return new KieSessionRefResolver( releaseId, id, listeners, loggers, commands );
    }

    public static Object createKieSession(String id, ReleaseId releaseId, List<KieListenerAdaptor> listeners, List<KieLoggerAdaptor> loggers, List<?> commands, KSessionOptions kSessionOptions){
        return new KieSessionResolver( releaseId, listeners, loggers, commands, kSessionOptions );
    }

    public static KieStoreServices createKieStore() throws Exception {
        return new KnowledgeStoreServiceImpl();
    }

    public static ReleaseId createReleaseId(String id, String groupId, String artifactId, String version){
        return new ReleaseIdImpl(groupId, artifactId, version);
    }

    public static Object createImport(String releaseIdName, ReleaseId releaseId, boolean enableScanner, long scannerInterval) {
        return new KieImportResolver( releaseIdName, releaseId, enableScanner, scannerInterval );
    }

    public static Object createImportedKieSession( String ksessionName ) {
        KieImportSessionResolver resolver = new KieImportSessionResolver( ksessionName );
        importInjector.registerSessionResolver( ksessionName, resolver );
        return resolver;
    }

    public static Object createImportedKieBase( String kbaseName ) {
        KieImportBaseResolver resolver = new KieImportBaseResolver( kbaseName );
        importInjector.registerBaseResolver( kbaseName, resolver );
        return resolver;
    }

    public static Object createImportedKieScanner( String kscannerName ) {
        KieImportScannerResolver resolver = new KieImportScannerResolver( kscannerName );
        importInjector.registerScannerResolver( kscannerName, resolver );
        return resolver;
    }

    public static Environment createEnvironment(String id, HashMap<String, Object> parameters, List<Object> marshallingStrategies){
        Environment environment = EnvironmentFactory.newEnvironment();
        if ( parameters != null) {
            for (String key : parameters.keySet()){
                environment.set(key, parameters.get(key));
            }
        }
        for (int i=0; i<marshallingStrategies.size(); i++){
            Object object = marshallingStrategies.get(i);
            if ( object instanceof JPAPlaceholderResolverStrategyHelper) {
                JPAPlaceholderResolverStrategy jpaPlaceholderResolverStrategy;
                Environment refEnv = ((JPAPlaceholderResolverStrategyHelper)object).getEnvironment();
                if ( refEnv == null) {
                    jpaPlaceholderResolverStrategy = new JPAPlaceholderResolverStrategy(environment);
                } else {
                    jpaPlaceholderResolverStrategy = new JPAPlaceholderResolverStrategy(refEnv);
                }
                marshallingStrategies.set(i, jpaPlaceholderResolverStrategy);
                break;
            }
        }
        if ( marshallingStrategies != null){
            environment.set(EnvironmentName.OBJECT_MARSHALLING_STRATEGIES, marshallingStrategies.toArray(new ObjectMarshallingStrategy[]{}));
        }
        return environment;
    }

    public static ClassObjectMarshallingStrategyAcceptor createDefaultAcceptor(){
        return ClassObjectMarshallingStrategyAcceptor.DEFAULT;
    }

    public static class ImportInjector {
        private Map<String, KieImportSessionResolver> sessionResolvers = new HashMap<String, KieImportSessionResolver>();
        private Map<String, KieImportBaseResolver> baseResolvers = new HashMap<String, KieImportBaseResolver>();
        private Map<String, KieImportScannerResolver> scannerResolvers = new HashMap<String, KieImportScannerResolver>();

        public void registerSessionResolver( String name, KieImportSessionResolver resolver ) {
            sessionResolvers.put( name, resolver );
        }

        public void registerBaseResolver( String name, KieImportBaseResolver resolver ) {
            baseResolvers.put( name, resolver );
        }

        public void registerScannerResolver( String name, KieImportScannerResolver resolver ) {
            scannerResolvers.put( name, resolver );
        }

        public void wireSession(String name, Object kieSession) {
            KieImportSessionResolver resolver = sessionResolvers.get(name);
            if (resolver != null) {
                resolver.setSession( kieSession );
            }
        }

        public void wireBase(String name, KieBase kieBase ) {
            KieImportBaseResolver resolver = baseResolvers.get(name);
            if (resolver != null) {
                resolver.setBase( kieBase );
            }
        }

        public void wireScanner(String name, KieScanner kieScanner ) {
            KieImportScannerResolver resolver = scannerResolvers.get(name);
            if (resolver != null) {
                resolver.setScanner( kieScanner );
            }
        }
    }
}
