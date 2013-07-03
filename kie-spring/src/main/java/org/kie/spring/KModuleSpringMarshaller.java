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

import org.kie.api.builder.model.KieModuleModel;
import org.kie.spring.kmodule.factorybeans.KModuleFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class KModuleSpringMarshaller {

    private static KieSpringApplicationListener kieSpringApplicationListener;

    public static KieModuleModel fromXML(java.io.File kModuleFile){
        System.out.println("**KModuleSpringMarshaller::fromXML(java.io.File kModuleFile)");
        ApplicationContext context = new ClassPathXmlApplicationContext(kModuleFile.getAbsolutePath());
        return null;
    }

    public static KieModuleModel fromXML(java.net.URL kModuleUrl){
        System.out.println("**KModuleSpringMarshaller::fromXML(URL kModuleUrl)::"+kModuleUrl.getPath()+" ** "+kModuleUrl.getFile());
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext();
        kieSpringApplicationListener = new KieSpringApplicationListener();
        context.addApplicationListener(kieSpringApplicationListener);
//        context.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
//            @Override
//            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//                KieModuleModel kModel = beanFactory.getBean(KieModuleModel.class);
//                System.out.println("*********KModuleSpringMarshaller::postProcessBeanFactory == "+kModel);
//            }
//        });
        context.setConfigLocation(kModuleUrl.toExternalForm());
        context.refresh();
        context.registerShutdownHook();
        return kieSpringApplicationListener.getKieModuleModel();
    }
}
