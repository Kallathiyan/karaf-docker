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
package org.apache.karaf.docker.core;

import java.io.IOException;
import java.util.Set;

/**
 * Karaf Docker service
 */
public interface KarafDocker {

    public Set<String> docks();

    /**
     * Create a fresh Karaf instance in a docker volume, ready to be started in a docker.io container.
     * It's a dock.
     *
     * @param name the name of the dock.
     */
    public void bootstrap(String name);

    /**
     * Clone the current Karaf instance in a docker volume, ready to be started in a docker.io container.
     *
     * @param name the name of the dock.
     */
    public void provision(String name) throws IOException;

    /**
     * Delete a Karaf dock.
     *
     * @param name the name of the dock.
     */
    public void delete(String name);

    /**
     * Start a Karaf dock in a docker.io container.
     *
     * @param name the name of the dock.
     */
    public void start(String name);

    /**
     * Stop a Karaf dock and the corresponding docker.io container.
     *
     * @param name the name of the dock.
     */
    public void stop(String name);

}
