/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.docker.api;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Docker factory test.
 */
public class DockerFactoryTest {

    private Docker docker;

    @Before
    public void setup() {
        docker = DockerFactory.createInstance();
    }

    @Test
    public void testDockerInfo() throws Exception {
        Info info = docker.info();
        System.out.println("Info:");
        System.out.println("\tDriver: " + info.getDriver());
        System.out.println("\tDriver Status: " + info.getDriverStatus());
        System.out.println("\tExecution Driver: " + info.getExecutionDriver());
        System.out.println("\tIndex Server Address: "  + info.getIndexServerAddress());
        System.out.println("\tInit Path: " + info.getInitPath());
        System.out.println("\tInit SHA1: " + info.getInitSha1());
        System.out.println("\tKernel Version: " + info.getKernelVersion());
        System.out.println("\tContainers: " + info.getContainers());
        System.out.println("\tImages: " + info.getImages());
        System.out.println("\tNFD: " + info.getNfd());
        System.out.println("\tNGoRoutines: " + info.getNgoroutines());
        System.out.println("\tMemory Limit enabled: " + info.isMemoryLimit());
        System.out.println("\tSwap Limit enabled: " + info.isSwapLimit());
    }

    @Test
    public void testDockerContainer() throws Exception {
        List<Container> containers = docker.containers();
        assertEquals(0, containers.size());
        System.out.println("Creating docker-karaf container ...");
        ContainerConfig config = new ContainerConfig();
        config.setTty(true);
        config.setAttachStderr(false);
        config.setAttachStdin(false);
        config.setAttachStdout(false);
        config.setImage("karaf:3.0.2");
        config.setUser("karaf");
        CreateContainerResponse response = docker.createContainer(config, "docker-karaf");
        System.out.println("\tContainer ID: " + response.getId());
        System.out.println("\t(Warnings: " + response.getWarnings() + ")");
        System.out.println("Starting docker-karaf container ...");
        HostConfig hostConfig = new HostConfig();
        hostConfig.setPrivileged(false);
        hostConfig.setPublishAllPorts(false);
        Response startResponse = docker.startContainer(response.getId(), hostConfig);
        System.out.println("\tStatus: " + startResponse.getStatus());
        assertEquals(204, startResponse.getStatus());
        containers = docker.containers();
        assertEquals(1, containers.size());
        System.out.println("Stopping docker-karaf container ...");
        Response stopResponse = docker.stopContainer(response.getId(), 30);
        System.out.println("\tStatus: " + stopResponse.getStatus());
        assertEquals(204, stopResponse.getStatus());
        System.out.println("Removing docker-karaf container ...");
        Response removeResponse = docker.removeContainer(response.getId(), true, true);
        System.out.println("\tStatus: " + removeResponse.getStatus());
        assertEquals(204, removeResponse.getStatus());
    }

}
