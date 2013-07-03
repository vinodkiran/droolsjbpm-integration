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

import org.drools.core.util.StringUtils;
import org.kie.spring.KieSpringUtils;
import org.kie.spring.kmodule.factorybeans.KSessionFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import static org.kie.spring.namespace.DefinitionParserHelper.emptyAttributeCheck;


public class KSessionDefinitionParser extends AbstractBeanDefinitionParser {

    private static final String ATTRIBUTE_ID = "name";
    private static final String ATTRIBUTE_TYPE = "type";

    @SuppressWarnings("unchecked")
    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(KSessionFactoryBean.class);

        String id = element.getAttribute(ATTRIBUTE_ID);
        emptyAttributeCheck(element.getLocalName(), ATTRIBUTE_ID, id);
        factory.addPropertyValue(ATTRIBUTE_ID, id);

        String generatedId = KieSpringUtils.KIE_SPRING_ID_PREFIX+id;
        element.setAttribute("id", generatedId);

        //the following statement is needed to prevent Spring from creating an alias with the name element.
        //as we will introduce another bean with the name.
        element.setAttribute("name", generatedId);

        String type = element.getAttribute(ATTRIBUTE_TYPE);
        if (StringUtils.isEmpty(type)){
            type = "stateful";
        }
        factory.addPropertyValue(ATTRIBUTE_TYPE, type);
        return factory.getBeanDefinition();
    }
}
