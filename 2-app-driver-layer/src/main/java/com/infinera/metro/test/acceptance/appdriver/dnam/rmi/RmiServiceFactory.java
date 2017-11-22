package com.infinera.metro.test.acceptance.appdriver.dnam.rmi;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Slf4j
public class RmiServiceFactory {

    private final RmiConfig rmiConfig;

    public RmiServiceFactory(RmiConfig rmiConfig) {
        this.rmiConfig = rmiConfig;
    }

    public <T> T lookupRemoteService(Class<T> ofClass) {
        return lookupRemoteService(ofClass, ofClass.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    public <T> T lookupRemoteService(Class<T> ofClass, String name) {
        log.debug("Attempting to lookup RMI stub for {}...", name);

        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        String serviceUrl = String.format("rmi://%s:%d/%s", rmiConfig.getHostname(), rmiConfig.getRmiPort(), name);
        factory.setServiceUrl(serviceUrl);
        factory.setServiceInterface(ofClass);
        factory.setRefreshStubOnConnectFailure(true);
        factory.setLookupStubOnStartup(false);
        factory.afterPropertiesSet();
        T svc = (T) factory.getObject();
        log.info("Created {}", serviceUrl);
        return svc;
    }

//    @ConfigurationProperties(prefix = "tnm.mainserver")
    @Data
    public static class RmiConfig {
        private String hostname;
        private int rmiPort;
    }

}
