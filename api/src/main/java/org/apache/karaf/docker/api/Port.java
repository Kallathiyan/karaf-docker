package org.apache.karaf.docker.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * docker.io container port
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Port {

    @JsonProperty("PrivatePort")
    private int privatePort;
    @JsonProperty("PublicPort")
    private int publicPort;
    @JsonProperty("Type")
    private String type;

}
