package com.demo.handlers;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.proxy.http.InterceptedResponse;
import burp.api.montoya.proxy.http.ProxyResponseHandler;
import burp.api.montoya.proxy.http.ProxyResponseReceivedAction;
import burp.api.montoya.proxy.http.ProxyResponseToBeSentAction;

public class REHHttpResponseHandler implements ProxyResponseHandler {
    private MontoyaApi _montoya;
    private Logging _logging;

    /**
     * Constructor for the response handler object
     * @param montoyaApi - an instance of the Burp Montoya APIs
     */
    public REHHttpResponseHandler(MontoyaApi montoyaApi) {
        this._montoya = montoyaApi;
        this._logging = montoyaApi.logging();
    }
    /**
     * Handle responses when they are received
     * @param interceptedResponse - An object holding the captured HTTP response
     * @return - an un-modified response
     */
    @Override
    public ProxyResponseReceivedAction handleResponseReceived(InterceptedResponse interceptedResponse) {
        return ProxyResponseReceivedAction.continueWith(interceptedResponse);
    }

    /**
     * Handle the response after it is received, before sent back to the client
     * @param interceptedResponse - An object holding the HTTP response right before it is sent
     * @return - an un-modified response
     */
    @Override
    public ProxyResponseToBeSentAction handleResponseToBeSent(InterceptedResponse interceptedResponse) {
        return ProxyResponseToBeSentAction.continueWith(interceptedResponse);
    }
}
