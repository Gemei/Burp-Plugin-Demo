package com.demo.editors;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.ContentType;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.Selection;
import burp.api.montoya.ui.editor.RawEditor;
import burp.api.montoya.ui.editor.extension.EditorMode;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpRequestEditor;
import com.demo.helpers.REHConstants;
import com.demo.helpers.CryptoHelper;
import org.json.JSONObject;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class REHHttpRequestEditor implements ExtensionProvidedHttpRequestEditor {
    private final MontoyaApi montoya;
    private final Logging logging;
    private HttpRequestResponse reqResp;
    private final RawEditor editor;

    public REHHttpRequestEditor(MontoyaApi montoyaApi, EditorMode editorMode) {
        this.montoya = montoyaApi;
        this.logging = montoyaApi.logging();
        this.editor = this.montoya.userInterface().createRawEditor();
    }

    @Override
    public HttpRequest getRequest() {
        byte[] body;
        if (this.editor.isModified()) {
            CryptoHelper cryptoHelper = new CryptoHelper(this.montoya);
            String originalBody = this.reqResp.request().body().toString();

            String editorContents = new String(this.editor.getContents().getBytes(), StandardCharsets.UTF_8);
            JSONObject editorJson = new JSONObject(editorContents);
            JSONObject encryptedJson = cryptoHelper.encrypt(editorJson);

            this.logging.logToOutput("------------------------------------------");
            this.logging.logToOutput("OLD BODY: %s".formatted(originalBody));

            String encryptedJsonString = encryptedJson.toString();
            String base64Encoded = Base64.getEncoder().encodeToString(encryptedJsonString.getBytes(StandardCharsets.UTF_8));

            JSONObject wrappedJson = new JSONObject();
            wrappedJson.put("enc_payload", base64Encoded);

            this.logging.logToOutput("New body data: %s".formatted(wrappedJson));
            this.logging.logToOutput("------------------------------------------");

            body = wrappedJson.toString().getBytes(StandardCharsets.UTF_8);
        } else {
            body = this.reqResp.request().body().getBytes();
        }

        if (body == null || body.length == 0) {
            this.logging.logToError("[-] getRequest: The selected editor body is empty/null.");
            body = this.reqResp.request().body().getBytes();
        }

        return this.reqResp.request().withBody(ByteArray.byteArray(body));
    }

    @Override
    public void setRequestResponse(HttpRequestResponse httpRequestResponse) {
        this.reqResp = httpRequestResponse;

        CryptoHelper cryptoHelper = new CryptoHelper(this.montoya);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();

        JSONObject jsonBody = new JSONObject(this.reqResp.request().bodyToString());
        this.logging.logToOutput("[*] encrypted JSON Body: %s".formatted(jsonBody));

        String encryptedData = jsonBody.getString(REHConstants.ENC_PARAMETER_REQ);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        JSONObject encryptedJson = new JSONObject(decodedString);
        JSONObject decryptedJson = cryptoHelper.decrypt(encryptedJson);

        try {
            outstream.write(decryptedJson.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            this.logging.logToError("[-] Stream write error.");
            throw new RuntimeException(e);
        }

        this.editor.setContents(ByteArray.byteArray(outstream.toByteArray()));
    }

    @Override
    public boolean isEnabledFor(HttpRequestResponse httpRequestResponse) {
        if (httpRequestResponse == null || httpRequestResponse.request() == null) {
            return false;
        }
        if (httpRequestResponse.request().httpVersion() == null) {
            return false;
        }
        if (httpRequestResponse.request().contentType() != ContentType.JSON) {
            return false;
        }
        if (httpRequestResponse.request().body().indexOf(REHConstants.ENC_PARAMETER_REQ) == -1) {
            return false;
        }
        if (httpRequestResponse.request().body() == null || httpRequestResponse.request().body().length() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public String caption() {
        return REHConstants.CAPTION;
    }

    @Override
    public Component uiComponent() {
        return this.editor.uiComponent();
    }

    @Override
    public Selection selectedData() {
        return this.editor.selection().get();
    }

    @Override
    public boolean isModified() {
        return this.editor.isModified();
    }
}
