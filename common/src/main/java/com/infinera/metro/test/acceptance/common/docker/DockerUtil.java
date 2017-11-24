package com.infinera.metro.test.acceptance.common.docker;

import com.google.common.collect.ImmutableMap;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.AttachedNetwork;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.NetworkSettings;

import java.util.Map;

public enum DockerUtil {
    DOCKER_UTIL;

    private DockerClient dockerClient;

    DockerUtil() {
        try {
            dockerClient = DefaultDockerClient.fromEnv().build();
        } catch (DockerCertificateException e) {
            e.printStackTrace();
        }
    }
    public String getContainerIpAddress(String containerName) throws DockerCertificateException, DockerException, InterruptedException {
        if(containerName == null ||
            dockerClient.inspectContainer(containerName) == null ||
                dockerClient.inspectContainer("dnam-mainserver").networkSettings() == null ||
                    dockerClient.inspectContainer("dnam-mainserver").networkSettings().networks() == null ) {
            throw new RuntimeException(String.format("Failed to retrieve ipAdders for container with name %s." +
                " Either containerName, ContainerInfo, NetworkSettings or Networks was null", containerName));
        }
        final ContainerInfo info = dockerClient.inspectContainer(containerName);
        final NetworkSettings networkSettings = info.networkSettings();
        final ImmutableMap<String, AttachedNetwork> networks = networkSettings.networks();

        return networks.entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(attachedNetwork -> attachedNetwork.aliases().contains(containerName))
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .ipAddress();
    }
}