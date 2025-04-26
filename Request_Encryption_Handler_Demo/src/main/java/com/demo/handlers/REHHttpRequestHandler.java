package com.demo.handlers;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;

public class REHHttpRequestHandler implements ProxyRequestHandler {
    private MontoyaApi _montoya;
    private Logging _logging;

    /**
     * Constructor for the request handler object
     * @param montoyaApi - an instance of the Burp Montoya APIs
     */
    public REHHttpRequestHandler(MontoyaApi montoyaApi) {
        this._montoya = montoyaApi;
        this._logging = montoyaApi.logging();
    }

    /**
     * Handle requests when they are received
     * @param interceptedRequest - An object holding the captured HTTP request
     * @return - an un-modified request
     */
    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        return ProxyRequestReceivedAction.continueWith(interceptedRequest);
    }

    /**
     * Handle the request after it is received, before sent back to the client
     * @param interceptedRequest - An object holding the HTTP request right before it is sent
     * @return - an un-modified request
     */
    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }
}
