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
package org.apache.karaf.docker.core.impl;

import org.apache.karaf.docker.api.*;
import org.apache.karaf.docker.core.KarafDocker;

import javax.ws.rs.core.Response;
import java.io.*;
import java.util.*;

public class KarafDockerImpl implements KarafDocker {

    public static final String DOCKER_STORE_FILE = "dockers.properties";

    // TODO add properties to store the current status, container id, etc
    private File storageLocation;

    private Map<String, String> docks = new HashMap<>();

    public File getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(File storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Set<String> docks() {
        return docks.keySet();
    }

    public void bootstrap(String name) {
        if (docks.keySet().contains(name)) {
            throw new IllegalArgumentException("Karaf dock " + name + " already exists");
        }
        // File dockDirectory = new File(storageLocation, name);

        // TODO deal with docker.io hub about images

        // creating the docker.io container
        Docker docker = DockerFactory.createInstance();
        ContainerConfig config = new ContainerConfig();
        config.setTty(true);
        config.setAttachStderr(true);
        config.setAttachStdin(true);
        config.setAttachStdout(true);
        config.setImage("karaf:3.0.3");
        config.setHostname("docker");
        config.setUser("");
        config.setCmd(new String[]{"/bin/karaf"});
        config.setWorkingDir("");
        config.setOpenStdin(true);
        config.setStdinOnce(true);
        Map<String, Map<String, String>> exposedPorts = new HashMap<String, Map<String, String>>();
        Map<String, String> exposedPort = new HashMap<>();
        exposedPorts.put("8101/tcp", exposedPort);
        config.setExposedPorts(exposedPorts);
        CreateContainerResponse response = docker.createContainer(config, name);
        docks.put(name, response.getId());
    }

    public void provision(String name) throws IOException {
        if (docks.keySet().contains(name)) {
            throw new IllegalArgumentException("Karaf dock " + name + " already exists");
        }

        // creating the dock
        File dockDirectory = new File(storageLocation, name);
        dockDirectory.mkdirs();

        File currentKarafBase = new File(System.getProperty("karaf.base"));
        copy(currentKarafBase, dockDirectory);

        // creating the docker.io container
        Docker docker = DockerFactory.createInstance();
        ContainerConfig config = new ContainerConfig();
        config.setTty(true);
        config.setAttachStderr(true);
        config.setAttachStdin(true);
        config.setAttachStdout(true);
        config.setImage("karaf:3.0.3");
        config.setHostname("docker");
        config.setUser("");
        config.setCmd(new String[]{"/bin/karaf"});
        config.setWorkingDir("");
        config.setOpenStdin(true);
        config.setStdinOnce(true);
        Map<String, Map<String, String>> exposedPorts = new HashMap<String, Map<String, String>>();
        Map<String, String> exposedPort = new HashMap<>();
        exposedPorts.put("8101/tcp", exposedPort);
        config.setExposedPorts(exposedPorts);
        CreateContainerResponse response = docker.createContainer(config, "docker-karaf");
        docks.put(name, response.getId());
    }

    public void start(String name) {
        if (!docks.keySet().contains(name)) {
            throw new IllegalArgumentException("Karaf dock " + name + " doesn't exist");
        }

        Docker docker = DockerFactory.createInstance();

        // starting the docker.io container
        HostConfig hostConfig = new HostConfig();
        hostConfig.setPrivileged(false);
        hostConfig.setPublishAllPorts(false);

        // getting the dock
        File dock = new File(storageLocation, name);
        if (dock.exists()) {
            hostConfig.setBinds(new String[]{dock.getAbsolutePath() + ":/opt/apache-karaf"});
        }

        hostConfig.setNetworkMode("bridge");
        hostConfig.setLxcConf(new String[]{});
        Map<String, List<Map<String, String>>> portBindings = new HashMap<String, List<Map<String, String>>>();
        List<Map<String, String>> portBinding = new LinkedList<>();
        Map<String, String> binding = new HashMap<>();
        binding.put("0.0.0.0", "49153");
        portBinding.add(binding);
        portBindings.put("8101/tcp", portBinding);
        hostConfig.setPortBindings(portBindings);
        Response response = docker.startContainer(docks.get(name), hostConfig);
        if (response.getStatus() != 204) {
            throw new IllegalStateException("docker.io container didn't start correctly: " + response.getStatusInfo().toString());
        }
    }

    public void stop(String name) {
        if (!docks.keySet().contains(name)) {
            throw new IllegalArgumentException("Karaf dock " + name + " doesn't exist");
        }
        Docker docker = DockerFactory.createInstance();
        Response response = docker.stopContainer(docks.get(name), 30);
        if (response.getStatus() != 204) {
            throw new IllegalStateException("docker.io container didn't stop correctly: " + response.getStatusInfo().toString());
        }
    }

    public void delete(String name) {
        if (!docks.keySet().contains(name)) {
            throw new IllegalArgumentException("Karaf dock " + name + " doesn't exist");
        }
        Docker docker = DockerFactory.createInstance();
        Response response = docker.removeContainer(docks.get(name), true, true);
        if (response.getStatus() != 204) {
            throw new IllegalStateException("docker.io container has not been removed: " + response.getStatusInfo().toString());
        }
        File dock = new File(storageLocation, name);
        if (dock.exists()) {
            dock.delete();
        }
    }

    private void mkdir(File karafBase, String path) {
        File file = new File(karafBase, path);
        if( !file.exists() ) {
            file.mkdirs();
        }
    }

    private int chmod(File serviceFile, String mode) throws IOException {
        java.lang.ProcessBuilder builder = new java.lang.ProcessBuilder();
        builder.command("chmod", mode, serviceFile.getCanonicalPath());
        java.lang.Process p = builder.start();
        try {
            return p.waitFor();
        } catch (InterruptedException e) {
            throw (IOException) new InterruptedIOException().initCause(e);
        }
    }

    private void copy(File source, File destination) throws IOException {
        if (source.getName().equals("docker")) {
            // ignore inner docker
            return;
        }
        if (source.getName().equals("cache.lock")) {
            // ignore cache.lock file
            return;
        }
        if (source.getName().equals("lock")) {
            // ignore lock file
            return;
        }
        if (source.getName().matches("transaction_\\d+\\.log")) {
            // ignore active txlog files
            return;
        }
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            String[] children = source.list();
            for (String child : children) {
                if (!child.contains("instances") && !child.contains("lib"))
                    copy(new File(source, child), new File(destination, child));
            }
        } else {
            try (
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(destination)
            ) {
                new StreamUtils().copy(in, out);
            }
        }
    }

    class StreamUtils {

        public StreamUtils() {
        }

        public void close(Closeable... closeables) {
            for (Closeable c : closeables) {
                try {
                    if (c != null) {
                        c.close();
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }

        public void close(Iterable<Closeable> closeables) {
            for (Closeable c : closeables) {
                try {
                    if (c != null) {
                        c.close();
                    }
                } catch (IOException e) {
                    // Ignore
                }
            }
        }

        public void copy(final InputStream input, final OutputStream output) throws IOException {
            byte[] buffer = new byte[1024 * 16];
            int n;
            while ((n = input.read(buffer)) > 0) {
                output.write(buffer, 0, n);
            }
            output.flush();
        }

    }


}
