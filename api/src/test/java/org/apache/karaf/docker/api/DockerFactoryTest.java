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
        System.out.println("\tNGoRoutines" + info.getNgoroutines());
        System.out.println("\tMemory Limit enabled: " + info.isMemoryLimit());
        System.out.println("\tSwap Limit enabled: " + info.isSwapLimit());
    }

}
