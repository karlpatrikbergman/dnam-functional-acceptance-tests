package com.infinera.metro.test.acceptance.appdriver.dnam;

import com.infinera.metro.test.acceptance.appdriver.api.RemoteServiceAccessData;
import com.infinera.metro.test.acceptance.appdriver.dnam.rmi.RmiSessionFactory;
import lombok.Getter;
import se.transmode.tnm.rmiclient.server.rmiserver.AbstractRequest;
import se.transmode.tnm.rmiclient.server.rmiserver.AbstractResponse;
import se.transmode.tnm.rmiclient.server.rmiserver.Session;

import java.rmi.RemoteException;


public abstract class DnamRmiClient<T extends AbstractRequest, V extends AbstractResponse> {
    @Getter
    protected final Session session;

    protected DnamRmiClient(RemoteServiceAccessData rmiRemoteService) {
        RmiSessionFactory rmiSessionFactory = new RmiSessionFactory(rmiRemoteService.getIpAddress(), rmiRemoteService.getPort());
        this.session = rmiSessionFactory.getSession();
    }

    protected V process(T request) {
        try {
            @SuppressWarnings("unchecked")
            V response = (V) session.process(request);
            if (response == null ||
                response.getReturnCode() != AbstractResponse.RESPONSE_OK) {
                final String responseString = (response == null) ? "null" : response.toString();
                throw new RuntimeException("Retrieved an erroneous response for request: "
                    .concat(request.toString()
                    .concat("\n")
                    .concat("Response: ")
                    .concat(responseString)));
            }
            return response;
        } catch (RemoteException e) {
            throw new RuntimeException("Failed to perform node request"
                .concat(request.toString())
                .concat("Exception: ")
                .concat(e.getMessage()));
        }
    }
}
