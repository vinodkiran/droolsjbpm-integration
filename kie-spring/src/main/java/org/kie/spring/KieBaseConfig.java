/*
 * Copyright 2014 JBoss Inc
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

package org.kie.spring;

import org.drools.core.RuleBaseConfiguration;
import org.drools.core.runtime.rule.impl.DefaultConsequenceExceptionHandler;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.DeclarativeAgendaOption;
import org.kie.api.conf.RemoveIdentitiesOption;
import org.kie.api.runtime.rule.ConsequenceExceptionHandler;
import org.kie.internal.conf.*;

import java.io.Serializable;

public class KieBaseConfig implements Serializable {
    protected SequentialAgendaOption sequentialAgenda;

    protected boolean sequential = false;
    protected boolean maintainTms = true;
    protected boolean shadowproxy = true;
    protected boolean removeIdentities= false;
    protected boolean shareAlphaNodes = true;
    protected boolean shareBetaNodes = true;
    protected boolean alphaMemory = false;
    protected boolean indexLeftBetaMemory = true;
    protected boolean indexRightBetaMemory = true;
    protected boolean lrUnlinkingEnabled = false;
    protected boolean declarativeAgendaEnabled = false;

    protected int alphaNodeHashingThreshold = 3;
    protected int compositeKeyDepth = 3;
    protected int permgenThreshold = 90;

    //drools.ruleEngine ?
    protected Class<? extends ConsequenceExceptionHandler> consequenceExceptionHandler = DefaultConsequenceExceptionHandler.class;


    public KieBaseConfig() {
    }

    public boolean isSequential() {
        return sequential;
    }

    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }

    public SequentialAgendaOption getSequentialAgenda() {
        return sequentialAgenda;
    }

    public void setSequentialAgenda(SequentialAgendaOption sequentialAgenda) {
        this.sequentialAgenda = sequentialAgenda;
    }

    public void setSequentialAgenda(String sequentialAgenda) {
        this.sequentialAgenda = SequentialAgendaOption.valueOf(sequentialAgenda);
    }

    public boolean isMaintainTms() {
        return maintainTms;
    }

    public void setMaintainTms(boolean maintainTms) {
        this.maintainTms = maintainTms;
    }

    public boolean isShadowproxy() {
        return shadowproxy;
    }

    public void setShadowproxy(boolean shadowproxy) {
        this.shadowproxy = shadowproxy;
    }

    public boolean isRemoveIdentities() {
        return removeIdentities;
    }

    public void setRemoveIdentities(boolean removeIdentities) {
        this.removeIdentities = removeIdentities;
    }

    public boolean isShareAlphaNodes() {
        return shareAlphaNodes;
    }

    public void setShareAlphaNodes(boolean shareAlphaNodes) {
        this.shareAlphaNodes = shareAlphaNodes;
    }

    public boolean isShareBetaNodes() {
        return shareBetaNodes;
    }

    public void setShareBetaNodes(boolean shareBetaNodes) {
        this.shareBetaNodes = shareBetaNodes;
    }

    public boolean isAlphaMemory() {
        return alphaMemory;
    }

    public void setAlphaMemory(boolean alphaMemory) {
        this.alphaMemory = alphaMemory;
    }

    public int getAlphaNodeHashingThreshold() {
        return alphaNodeHashingThreshold;
    }

    public void setAlphaNodeHashingThreshold(int alphaNodeHashingThreshold) {
        this.alphaNodeHashingThreshold = alphaNodeHashingThreshold;
    }

    public int getCompositeKeyDepth() {
        return compositeKeyDepth;
    }

    public void setCompositeKeyDepth(int compositeKeyDepth) {
        this.compositeKeyDepth = compositeKeyDepth;
    }

    public boolean isIndexLeftBetaMemory() {
        return indexLeftBetaMemory;
    }

    public void setIndexLeftBetaMemory(boolean indexLeftBetaMemory) {
        this.indexLeftBetaMemory = indexLeftBetaMemory;
    }

    public boolean isIndexRightBetaMemory() {
        return indexRightBetaMemory;
    }

    public void setIndexRightBetaMemory(boolean indexRightBetaMemory) {
        this.indexRightBetaMemory = indexRightBetaMemory;
    }

    public boolean isLrUnlinkingEnabled() {
        return lrUnlinkingEnabled;
    }

    public void setLrUnlinkingEnabled(boolean lrUnlinkingEnabled) {
        this.lrUnlinkingEnabled = lrUnlinkingEnabled;
    }

    public boolean isDeclarativeAgendaEnabled() {
        return declarativeAgendaEnabled;
    }

    public void setDeclarativeAgendaEnabled(boolean declarativeAgendaEnabled) {
        this.declarativeAgendaEnabled = declarativeAgendaEnabled;
    }

    public int getPermgenThreshold() {
        return permgenThreshold;
    }

    public void setPermgenThreshold(int permgenThreshold) {
        this.permgenThreshold = permgenThreshold;
    }

    public Class<? extends ConsequenceExceptionHandler> getConsequenceExceptionHandler() {
        return consequenceExceptionHandler;
    }

    public void setConsequenceExceptionHandler(Class<? extends ConsequenceExceptionHandler> consequenceExceptionHandler) {
        this.consequenceExceptionHandler = consequenceExceptionHandler;
    }

    public KieBaseConfiguration getConf(){
        KieBaseConfiguration kieBaseConfiguration = new RuleBaseConfiguration();
        kieBaseConfiguration.setOption(sequentialAgenda);

        kieBaseConfiguration.setOption(sequential?SequentialOption.YES:SequentialOption.NO);
        kieBaseConfiguration.setOption(removeIdentities? RemoveIdentitiesOption.YES:RemoveIdentitiesOption.NO);
        kieBaseConfiguration.setOption(shareAlphaNodes? ShareAlphaNodesOption.YES:ShareAlphaNodesOption.NO);
        kieBaseConfiguration.setOption(shareBetaNodes? ShareBetaNodesOption.YES:ShareBetaNodesOption.NO);
        kieBaseConfiguration.setOption(indexLeftBetaMemory?IndexLeftBetaMemoryOption.YES:IndexLeftBetaMemoryOption.NO);
        kieBaseConfiguration.setOption(indexRightBetaMemory?IndexRightBetaMemoryOption.YES:IndexLeftBetaMemoryOption.NO);
        kieBaseConfiguration.setOption(declarativeAgendaEnabled? DeclarativeAgendaOption.ENABLED:DeclarativeAgendaOption.DISABLED);

        //maintainTms ;
        //shadowproxy ;
        //alphaMemory ;
        //lrUnlinkingEnabled ;

        kieBaseConfiguration.setOption(AlphaThresholdOption.get(alphaNodeHashingThreshold));
        kieBaseConfiguration.setOption(CompositeKeyDepthOption.get(compositeKeyDepth));
        kieBaseConfiguration.setOption(PermGenThresholdOption.get(permgenThreshold));

        if ( consequenceExceptionHandler!= null) {
            kieBaseConfiguration.setOption(ConsequenceExceptionHandlerOption.get(consequenceExceptionHandler));
        }
        return kieBaseConfiguration;
    }
}
