package com.demo.providers;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpRequestEditor;
import burp.api.montoya.ui.editor.extension.HttpRequestEditorProvider;
import com.demo.editors.REHHttpRequestEditor;

public class REHHttpRequestEditorProvider implements HttpRequestEditorProvider {

    private MontoyaApi _montoya;

    /**
     * Construct a REHHttpRequestEditorProvider
     * @param api - an instance of the Montoya API
     */
    public REHHttpRequestEditorProvider(MontoyaApi api) {
        this._montoya = api;
    }

    /**
     * Returns a newly created HttpRequestEditor for each request.
     * @param editorCreationContext What mode the created editor should implement.
     * @return the newly created editor object
     */
    @Override
    public ExtensionProvidedHttpRequestEditor provideHttpRequestEditor(EditorCreationContext editorCreationContext) {
        return new REHHttpRequestEditor(this._montoya, editorCreationContext.editorMode());
    }
}
