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
