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
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        System.out.println("Creating docker-karaf container ...");
        ContainerConfig config = new ContainerConfig();
        config.setTty(true);
        config.setAttachStderr(true);
        config.setAttachStdin(true);
        config.setAttachStdout(true);
        config.setImage("karaf:3.0.3");
        config.setHostname("docker");
        config.setUser("");
        config.setCmd(new String[]{"/bin/bash"});
        config.setWorkingDir("");
        config.setOpenStdin(true);
        config.setStdinOnce(true);
        Map<String, Map<String, String>> exposedPorts = new HashMap<String, Map<String, String>>();
        Map<String, String> exposedPort = new HashMap<>();
        exposedPorts.put("8101/tcp", exposedPort);
        config.setExposedPorts(exposedPorts);
        CreateContainerResponse response = docker.createContainer(config, "docker-karaf");
        System.out.println("\tContainer ID: " + response.getId());
        System.out.println("\t(Warnings: " + response.getWarnings() + ")");
        System.out.println("Starting docker-karaf container ...");
        HostConfig hostConfig = new HostConfig();
        hostConfig.setPrivileged(false);
        hostConfig.setPublishAllPorts(false);
        hostConfig.setBinds(new String[]{"/opt/apache-maven-3.2.5:/opt/maven"});
        hostConfig.setNetworkMode("bridge");
        hostConfig.setLxcConf(new String[]{});
        Map<String, List<Map<String, String>>> portBindings = new HashMap<String, List<Map<String, String>>>();
        List<Map<String, String>> portBinding = new LinkedList<>();
        Map<String, String> binding = new HashMap<>();
        binding.put("0.0.0.0", "8101");
        portBinding.add(binding);
        portBindings.put("8101/tcp", portBinding);
        hostConfig.setPortBindings(portBindings);
        Response startResponse = docker.startContainer(response.getId(), hostConfig);
        System.out.println("\tStatus: " + startResponse.getStatus());
        assertEquals(204, startResponse.getStatus());
        List<Container> containers = docker.containers(true);
        assertTrue(containers.size() >= 1);
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
