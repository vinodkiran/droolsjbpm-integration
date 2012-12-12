/*
 * Copyright 2010 JBoss Inc
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

package org.drools.grid.remote;

import org.drools.command.impl.GenericCommand;
import org.kie.KieBaseConfiguration;
import org.kie.KnowledgeBase;
import org.kie.KnowledgeBaseFactory;
import org.kie.command.Context;

public class NewKnowledgeBaseRemoteCommand
    implements
    GenericCommand<KnowledgeBase> {

    private String kbaseConfId;
    
    public NewKnowledgeBaseRemoteCommand() {
        System.out.println(this);
    }

    public NewKnowledgeBaseRemoteCommand(String kbaseConfId) {
        this.kbaseConfId = kbaseConfId;
    }

    public KnowledgeBase execute(Context context) {
        KnowledgeBase kbase = null;
        KieBaseConfiguration kconf = null;
        
        if(kbaseConfId != null){
           kconf  = (KieBaseConfiguration) context.getContextManager().getContext( "__TEMP__" ).get(kbaseConfId);
        }
        if ( kconf == null ) {
            kbase = KnowledgeBaseFactory.newKnowledgeBase();
        } else {
            kbase = KnowledgeBaseFactory.newKnowledgeBase( kconf );
            
        }
        return kbase;
    }

}
