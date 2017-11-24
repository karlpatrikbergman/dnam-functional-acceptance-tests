package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Value
@Slf4j
class RmiServiceFactory {

    private final String ipAddress;
    private final int port;

    <T> T lookupRemoteService(Class<T> ofClass) {
        return lookupRemoteService(ofClass, ofClass.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    <T> T lookupRemoteService(Class<T> ofClass, String name) {
        log.debug("Attempting to lookup RMI stub for {}...", name);

        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        String serviceUrl = String.format("rmi://%s:%d/%s", ipAddress, port, name);
        factory.setServiceUrl(serviceUrl);
        factory.setServiceInterface(ofClass);
        factory.setRefreshStubOnConnectFailure(true);
        factory.setLookupStubOnStartup(false);
        factory.afterPropertiesSet();
        T svc = (T) factory.getObject();
        log.info("Created {}", serviceUrl);
        return svc;
    }
}
