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

package org.kie.server.services.casemgmt;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.kie.server.api.commands.CommandScript;
import org.kie.server.api.commands.DescriptorCommand;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieServerCommand;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.api.model.ServiceResponsesList;
import org.kie.server.api.model.Wrapped;
import org.kie.server.services.api.KieContainerCommandService;
import org.kie.server.services.api.KieServerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseKieContainerCommandServiceImpl implements KieContainerCommandService {

    private static final Logger logger = LoggerFactory.getLogger(CaseKieContainerCommandServiceImpl.class);

    private KieServerRegistry context;

    private CaseManagementServiceBase caseManagementServiceBase;
    private CaseManagementRuntimeDataServiceBase caseManagementRuntimeDataService;


    public CaseKieContainerCommandServiceImpl(KieServerRegistry context, CaseManagementServiceBase caseManagementServiceBase, CaseManagementRuntimeDataServiceBase caseManagementRuntimeDataService) {

        this.context = context;
        this.caseManagementServiceBase = caseManagementServiceBase;
        this.caseManagementRuntimeDataService = caseManagementRuntimeDataService;
    }

    @Override
    public ServiceResponse<String> callContainer(String containerId, String payload, MarshallingFormat marshallingFormat, String classType) {
        return null;
    }

    @Override
    public ServiceResponsesList executeScript(CommandScript commands, MarshallingFormat marshallingFormat, String classType) {
        List<ServiceResponse<? extends Object>> responses = new ArrayList<ServiceResponse<? extends Object>>();

        for (KieServerCommand command : commands.getCommands()) {
            if (!(command instanceof DescriptorCommand)) {
                logger.warn("Unsupported command '{}' given, will not process it", command.getClass().getName());
                continue;
            }
            try {
                Object result = null;
                Object handler = null;

                DescriptorCommand descriptorCommand = (DescriptorCommand) command;
                // find out the handler to call to process given command
                if ("CaseService".equals(descriptorCommand.getService())) {
                    handler = caseManagementServiceBase;
                } else if ("CaseQueryService".equals(descriptorCommand.getService())) {
                    handler = caseManagementRuntimeDataService;
                } else {
                    throw new IllegalStateException("Unable to find handler for " + descriptorCommand.getService() + " service");
                }

                List<Object> arguments = new ArrayList();
                // process and unwrap arguments
                for (Object arg : descriptorCommand.getArguments()) {
                    logger.debug("Before :: Argument with type {} and value {}", arg.getClass(), arg);
                    if (arg instanceof Wrapped) {
                        arg = ((Wrapped) arg).unwrap();
                    }
                    logger.debug("After :: Argument with type {} and value {}", arg.getClass(), arg);
                    arguments.add(arg);
                }

                if (descriptorCommand.getPayload() != null && !descriptorCommand.getPayload().isEmpty()) {
                    arguments.add(descriptorCommand.getPayload());
                }
                if (descriptorCommand.getMarshallerFormat() != null && !descriptorCommand.getMarshallerFormat().isEmpty()) {
                    arguments.add(descriptorCommand.getMarshallerFormat());
                }

                logger.debug("About to execute {} operation on {} with args {}", descriptorCommand.getMethod(), handler, arguments);
                // process command via reflection and handler
                result = MethodUtils.invokeMethod(handler, descriptorCommand.getMethod(), arguments.toArray());
                logger.debug("Handler {} returned response {}", handler, result);
                // return successful result
                responses.add(new ServiceResponse(ServiceResponse.ResponseType.SUCCESS, "", result));
            } catch (InvocationTargetException e){
                responses.add(new ServiceResponse(ServiceResponse.ResponseType.FAILURE, e.getTargetException().getMessage()));
            } catch (Throwable e) {
                logger.error("Error while processing {} command", command, e);
                // return failure result
                responses.add(new ServiceResponse(ServiceResponse.ResponseType.FAILURE, e.getMessage()));
            }
        }
        logger.debug("About to return responses '{}'", responses);
        return new ServiceResponsesList(responses);
    }
}
