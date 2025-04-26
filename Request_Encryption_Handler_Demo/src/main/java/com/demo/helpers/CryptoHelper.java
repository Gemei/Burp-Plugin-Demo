package com.demo.helpers;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import org.json.*;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Crypto helper class to handle encryption and decryption.
 */
public class CryptoHelper {

    private final MontoyaApi montoya;
    private final Logging logging;

    /**
     * Creates a new instance of the CryptoHelper class
     * @param api - the Burp callback functions object
     */
    public CryptoHelper(MontoyaApi api) {
        this.montoya = api;
        this.logging = this.montoya.logging();
    }

    /**
     * Function to encrypt JSON Data
     *
     * @param jsonData the input JSON to encrypt
     * @return the encrypted JSONObject
     */
    public JSONObject encrypt(JSONObject jsonData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(REHConstants.KEY.getBytes(StandardCharsets.UTF_8), REHConstants.KEY_ALGORITHM);

            byte[] iv = new byte[12];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(REHConstants.TRANSFORMATION_AES);
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);

            byte[] plaintext = jsonData.toString().getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(plaintext);

            JSONObject encryptedJson = new JSONObject();
            encryptedJson.put("iv", bytesToJsonArray(iv));
            encryptedJson.put("ciphertext", bytesToJsonArray(ciphertext));

            this.logging.logToOutput("Input JSON: %s".formatted(jsonData.toString()));
            this.logging.logToOutput("Encrypted JSON: %s".formatted(encryptedJson.toString()));

            return encryptedJson;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Function to decrypt an encrypted JSON
     *
     * @param encryptedJson the input encrypted JSON Object
     * @return the decrypted JSONObject
     */
    public JSONObject decrypt(JSONObject encryptedJson) {
        try {
            this.logging.logToOutput("Encrypted JSON: %s".formatted(encryptedJson));
            byte[] iv = jsonArrayToBytes(encryptedJson.getJSONArray("iv"));
            byte[] ciphertext = jsonArrayToBytes(encryptedJson.getJSONArray("ciphertext"));

            SecretKeySpec keySpec = new SecretKeySpec(REHConstants.KEY.getBytes(StandardCharsets.UTF_8), REHConstants.KEY_ALGORITHM);

            Cipher cipher = Cipher.getInstance(REHConstants.TRANSFORMATION_AES);
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

            byte[] plaintextBytes = cipher.doFinal(ciphertext);
            String plaintext = new String(plaintextBytes, StandardCharsets.UTF_8);

            this.logging.logToOutput("Decrypted JSON: %s".formatted(plaintext));

            return new JSONObject(plaintext);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Helper function to convert JSONArray to byte array
     *
     * @param array JSONArray
     * @return byte[] array
     */
    private byte[] jsonArrayToBytes(JSONArray array) {
        byte[] bytes = new byte[array.length()];
        for (int i = 0; i < array.length(); i++) {
            bytes[i] = (byte) array.getInt(i);
        }
        return bytes;
    }

    /**
     * Helper function to convert byte array to JSONArray
     *
     * @param bytes byte array
     * @return JSONArray
     */
    private JSONArray bytesToJsonArray(byte[] bytes) {
        JSONArray jsonArray = new JSONArray();
        for (byte b : bytes) {
            jsonArray.put((int) b & 0xFF);  // Convert byte to unsigned int
        }
        return jsonArray;
    }
}