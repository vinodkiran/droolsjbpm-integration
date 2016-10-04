/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package org.kie.server.integrationtests.drools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.ServiceResponse;

import static org.junit.Assert.*;
import org.kie.server.integrationtests.shared.KieServerAssert;
import org.kie.server.integrationtests.shared.KieServerDeployer;

/**
 * Test used for verification of spreadsheet decision table processing.
 */
public class SpreadsheetIntegrationTest extends DroolsKieServerBaseIntegrationTest {

    private static ReleaseId releaseId = new ReleaseId("org.kie.server.testing", "spreadsheet", "1.0.0-SNAPSHOT");

    private static final String CONTAINER_ID = "spreadsheet";
    private static final String PERSON_CLASS_NAME = "org.kie.server.testing.Person";
    private static final String PERSON_OUT_IDENTIFIER = "person";
    private static final String PERSON_CAN_BUY_ALCOHOL_FIELD = "canBuyAlcohol";
    private static final Integer PERSON_AGE = new Integer(25);

    private static ClassLoader kjarClassLoader;

    @BeforeClass
    public static void buildAndDeployArtifacts() {
        KieServerDeployer.buildAndDeployCommonMavenParent();
        KieServerDeployer.buildAndDeployMavenProject(ClassLoader.class.getResource("/kjars-sources/spreadsheet").getFile());

        kjarClassLoader = KieServices.Factory.get().newKieContainer(releaseId).getClassLoader();

        createContainer(CONTAINER_ID, releaseId);
    }

    @Override
    protected void addExtraCustomClasses(Map<String, Class<?>> extraClasses) throws Exception {
        extraClasses.put(PERSON_CLASS_NAME, Class.forName(PERSON_CLASS_NAME, true, kjarClassLoader));
    }

    @Test
    public void testExecuteSpreadsheetRule() {
        Object person = createInstance(PERSON_CLASS_NAME, PERSON_AGE);
        List<Command<?>> commands = new ArrayList<Command<?>>();
        BatchExecutionCommand batchExecution = commandsFactory.newBatchExecution(commands);

        commands.add(commandsFactory.newInsert(person, PERSON_OUT_IDENTIFIER));
        commands.add(commandsFactory.newFireAllRules());

        ServiceResponse<ExecutionResults> response = ruleClient.executeCommandsWithResults(CONTAINER_ID, batchExecution);
        KieServerAssert.assertSuccess(response);
        ExecutionResults results = response.getResult();
        Object value = results.getValue(PERSON_OUT_IDENTIFIER);
        assertEquals(Boolean.TRUE, valueOf(value, PERSON_CAN_BUY_ALCOHOL_FIELD));
    }
}
