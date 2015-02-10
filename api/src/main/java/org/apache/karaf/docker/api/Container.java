package org.apache.karaf.docker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * docker.io container
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Container {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("Command")
    private String command;
    @JsonProperty("Created")
    private long created;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Ports")
    private List<Port> ports;
    @JsonProperty("SizeRw")
    private long sizeRw;
    @JsonProperty("SizeRootFS")
    private long sizeRootFs;


}
