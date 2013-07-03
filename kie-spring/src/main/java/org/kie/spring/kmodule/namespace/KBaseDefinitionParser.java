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
package org.kie.spring.kmodule.namespace;


import org.kie.spring.KieSpringUtils;
import org.kie.spring.kmodule.factorybeans.KBaseFactoryBean;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

import static org.kie.spring.namespace.DefinitionParserHelper.emptyAttributeCheck;


public class KBaseDefinitionParser extends AbstractBeanDefinitionParser {

    private static final String ATTRIBUTE_ID = "name";

    @SuppressWarnings("unchecked")
    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(KBaseFactoryBean.class);

        String id = element.getAttribute(ATTRIBUTE_ID);
        emptyAttributeCheck(element.getLocalName(), ATTRIBUTE_ID, id);
        factory.addPropertyValue("kBaseName", id);
        String generatedId = KieSpringUtils.KIE_SPRING_ID_PREFIX+id;
        element.setAttribute("id", generatedId);

        //the following statement is needed to prevent Spring from creating an alias with the name element.
        //as we will introduce another bean with the name.
        element.setAttribute("name", generatedId);
        List<Element> ksessionElements = DomUtils.getChildElementsByTagName(element, "ksession");
        if (ksessionElements != null && ksessionElements.size() > 0) {
            for (Element kbaseElement : ksessionElements){
                BeanDefinitionHolder obj = (BeanDefinitionHolder) parserContext.getDelegate().parsePropertySubElement(kbaseElement, null);
                obj.getBeanDefinition().getPropertyValues().addPropertyValue("kBase", new RuntimeBeanReference(generatedId));
                if (obj.getAliases() != null) {
                    System.out.println(" **************** obj.getAliases() *************" + Arrays.asList(obj.getAliases()));
                }
            }
        }

        return factory.getBeanDefinition();
    }
}
