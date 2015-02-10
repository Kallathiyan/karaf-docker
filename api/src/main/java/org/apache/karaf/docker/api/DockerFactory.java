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
