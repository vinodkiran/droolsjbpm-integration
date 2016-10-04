/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.kie.server.api.model.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "query-definition")
public class QueryDefinition {

    @XmlElement(name="query-name")
    private String name;
    @XmlElement(name="query-source")
    private String source;
    @XmlElement(name="query-expression")
    private String expression;
    @XmlElement(name="query-target")
    private String target;

    public QueryDefinition() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public static class Builder {

        private QueryDefinition definition = new QueryDefinition();

        public QueryDefinition build() {
            return definition;
        }

        public Builder name(String name) {
            definition.setName(name);

            return this;
        }

        public Builder source(String source) {
            definition.setSource(source);

            return this;
        }

        public Builder expression(String expression) {
            definition.setExpression(expression);

            return this;
        }

        public Builder target(String target) {
            definition.setTarget(target);

            return this;
        }
    }
}
