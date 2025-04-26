package com.demo.editors;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.extension.EditorMode;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import java.awt.*;

public class REHHttpResponseEditor implements ExtensionProvidedHttpResponseEditor {
    private MontoyaApi _montoya;
    private Logging _logging;

    public REHHttpResponseEditor(MontoyaApi montoyaApi, EditorMode editorMode) {
        this._montoya = montoyaApi;
        this._logging = montoyaApi.logging();
    }

    @Override
    public HttpResponse getResponse() {
        return null;
    }

    @Override
    public void setRequestResponse(HttpRequestResponse httpRequestResponse) {

    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse httpRequestResponse) {
        return false;
    }

    @Override
    public String caption() {
        return "";
    }

    @Override
    public Component uiComponent() {
        return null;
    }

    @Override
    public Selection selectedData() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }
}
