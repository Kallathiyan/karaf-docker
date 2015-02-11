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
package org.apache.karaf.docker.command;

import org.apache.karaf.docker.core.KarafDocker;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

import java.util.Set;

@Command(scope = "docker", name = "list", description = "List the docks")
public class ListCommand extends OsgiCommandSupport {

    private KarafDocker docker;

    public KarafDocker getDocker() {
        return docker;
    }

    public void setDocker(KarafDocker docker) {
        this.docker = docker;
    }

    public Object doExecute() throws Exception {
        Set<String> docks = docker.docks();
        for (String dock : docks) {
            System.out.println(dock);
        }
        return null;
    }

}
