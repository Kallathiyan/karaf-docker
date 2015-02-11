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

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import java.util.ArrayList;

/**
 * Create a docker.io client.
 */
public class DockerFactory {

    public final static String DEFAULT_ADDRESS = "http://localhost:2375";

    public static Docker createInstance() {
        return DockerFactory.createInstance(DEFAULT_ADDRESS);
    }

    public static Docker createInstance(String address) {
        ArrayList providers = new ArrayList();
        providers.add(new JacksonJsonProvider());
        return JAXRSClientFactory.create(address, Docker.class, providers);
    }

}
