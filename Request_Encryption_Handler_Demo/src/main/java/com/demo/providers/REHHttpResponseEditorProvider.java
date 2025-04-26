package com.demo.providers;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.extension.EditorCreationContext;
import burp.api.montoya.ui.editor.extension.ExtensionProvidedHttpResponseEditor;
import burp.api.montoya.ui.editor.extension.HttpResponseEditorProvider;
import com.demo.editors.REHHttpResponseEditor;

public class REHHttpResponseEditorProvider implements HttpResponseEditorProvider {

    private MontoyaApi _montoya;

    /**
     * Construct a REHHttpResponseEditorProvider
     * @param api - an instance of the Montoya API
     */
    public REHHttpResponseEditorProvider(MontoyaApi api) {
        this._montoya = api;
    }

    /**
     * Returns a newly created ExtensionProvidedHttpResponseEditor tab for each response.
     * @param editorCreationContext Context details about the editor.
     * @return the newly created editor object
     */
    @Override
    public ExtensionProvidedHttpResponseEditor provideHttpResponseEditor(EditorCreationContext editorCreationContext) {
        return new REHHttpResponseEditor(this._montoya, editorCreationContext.editorMode());
    }
}
