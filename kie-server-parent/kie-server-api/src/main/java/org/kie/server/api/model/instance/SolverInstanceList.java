/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.server.api.model.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.kie.server.api.model.KieContainerResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="solvers")
@XStreamAlias( "solvers" )
@XmlAccessorType (XmlAccessType.FIELD)
public class SolverInstanceList {

    @XmlElement(name="solver")
    @XStreamImplicit(itemFieldName = "solver")
    private List<SolverInstance> solvers;

    public SolverInstanceList() {
        super();
        solvers = new ArrayList<SolverInstance>();
    }

    public SolverInstanceList(List<SolverInstance> solvers) {
        super();
        this.solvers = solvers;
    }
    
    public List<SolverInstance> getContainers() {
        return solvers;
    }
    
    public void setContainers(List<SolverInstance> solvers) {
        this.solvers = solvers;
    }

}
