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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Rest Docker.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface Docker {

    @GET @Path("/info")
    Info info();

    @GET @Path("/containers/json")
    List<Container> containers(@QueryParam("all") boolean showAll);

    @POST @Path("/containers/create")
    CreateContainerResponse createContainer(ContainerConfig config, @QueryParam("name") String name);

    @DELETE @Path("/containers/{id}")
    Response removeContainer(@PathParam("id") String id, @QueryParam("v") boolean removeVolumes, @QueryParam("force") boolean force);

    @POST @Path("/containers/{id}/start")
    Response startContainer(@PathParam("id") String id, HostConfig config);

    @POST @Path("/containers/{id}/stop")
    Response stopContainer(@PathParam("id") String id, @QueryParam("t") int timeToWait);

}
