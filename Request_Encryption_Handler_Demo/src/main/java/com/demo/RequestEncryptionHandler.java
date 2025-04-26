package com.demo;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;
import burp.api.montoya.logging.Logging;
import com.demo.providers.REHHttpRequestEditorProvider;
import com.demo.handlers.REHHttpRequestHandler;
import com.demo.handlers.REHHttpResponseHandler;
import com.demo.helpers.REHConstants;
import com.demo.providers.REHHttpResponseEditorProvider;

/**
 * Class to hold main logic for RequestEncryptionHandler extension
 */
public class RequestEncryptionHandler implements BurpExtension, ExtensionUnloadingHandler {
    private MontoyaApi _montoya;
    private Logging logging;

    /**
     * Setup function that gets called on extension startup. Register all required handlers, providers, etc.
     * @param api The api implementation to access the functionality of burp suite.
     */
    @Override
    public void initialize(MontoyaApi api) {
        this._montoya = api;
        this._montoya.extension().setName(REHConstants.EXTENSION_NAME);
        this.logging = this._montoya.logging();

        // Request/Response Editor Providers
        REHHttpRequestEditorProvider requestEditorProvider = new REHHttpRequestEditorProvider(this._montoya);
        REHHttpResponseEditorProvider responseEditorProvider = new REHHttpResponseEditorProvider(this._montoya);
        this._montoya.userInterface().registerHttpRequestEditorProvider(requestEditorProvider);
        this._montoya.userInterface().registerHttpResponseEditorProvider(responseEditorProvider);

        // Request/Response Handlers
        REHHttpRequestHandler httpRequestHandler = new REHHttpRequestHandler(this._montoya);
        REHHttpResponseHandler httpResponseHandler = new REHHttpResponseHandler(this._montoya);
        this._montoya.proxy().registerRequestHandler(httpRequestHandler);
        this._montoya.proxy().registerResponseHandler(httpResponseHandler);

        this._montoya.extension().registerUnloadingHandler(this);
        this.logging.logToOutput(REHConstants.LOADED_LOG_MSG);
    }

    /**
     * Gets called when the extension is unloaded. No concurrency used, no need to clean up threads.
     */
    @Override
    public void extensionUnloaded() {
        this.logging.logToOutput(REHConstants.UNLOADED_LOG_MSG);
    }
}
