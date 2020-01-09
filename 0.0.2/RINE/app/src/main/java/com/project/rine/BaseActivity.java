package com.project.rine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class BaseActivity extends AppCompatActivity {

    final static int JSON_MODE_DIRECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public String encrypt(String plainText, BigInteger n, BigInteger e) {
        /*
        * @par plainText: String, waiting to be encrypted
        * @par n: BigInteger, dst friend's key_n
        * @par e: BigInteger, dst friend's key_e
        *
        * @result: String, a String of numbers, encrypted by RSA algorithm,
        *   using plainText, "utf-8", n and e.
        * */
        //SharedPreferences pref = getSharedPreferences("keys", MODE_PRIVATE);
//        String nString = pref.getString("n", "");
//        String eString = pref.getString("e", "");
//        if (nString.equals("")) {
//            Log.e("RINE_error", "No n in preference file!");
//        }
//        BigInteger n = new BigInteger(nString);
//        BigInteger e = new BigInteger(eString);
        return Encrypter.encrypt(plainText, n, e);
    }

    public void genkey() {
        /*
        * function: generate keys using in RSA algorithm, save them in
        *   "keys" SharedPreferences.
        * */
        SharedPreferences pref0 = getSharedPreferences("keys", MODE_PRIVATE);
        String nString = pref0.getString("n", "");
        if (!nString.equals("")) {
            Log.d("RINE_debug", "keys already exists.");
            return;
            //Log.d("RINE_debug", "But I do not return.");
        }
        Random random = new Random();
        final BigInteger one = new BigInteger("1");
        final BigInteger zero = new BigInteger("0");
        Log.d("RINE_debug", "generating keys...");
        BigInteger p = BigInteger.probablePrime(1024, random);
        BigInteger q = BigInteger.probablePrime(1024, random);
        Log.d("RINE_debug", "generated p and q.");
        Log.d("RINE_keys", "p = "+p.toString());
        gV.xLog("RINE_keys", "p.length ="+p.toString().length());
        gV.xLog("RINE_keys", "p.bitCount ="+p.bitCount());
        Log.d("RINE_keys", "q = "+q.toString());
        gV.xLog("RINE_keys", "q.length ="+q.toString().length());
        gV.xLog("RINE_keys", "q.bitCount ="+q.bitCount());
        BigInteger n = p.multiply(q);
        BigInteger phiN = (p.subtract(one)).multiply(q.subtract(one));
        BigInteger e = BigInteger.probablePrime(10, random);
        Log.d("RINE_keys", "e = "+e.toString());
        final BigInteger cPhiN = new BigInteger(phiN.toString());
        phiN = phiN.add(one);
        BigInteger d = new BigInteger("1");
        while (!(phiN.mod(e).equals(zero))) {
            phiN = phiN.add(cPhiN);
        }
        gV.xLog("RINE_debug", "n.bitCount="+
                n.bitCount());
        d = phiN.divide(e);
        Log.d("RINE_keys", "d = "+d.toString());
        Log.d("RINE_debug", "generated e and d.");
        Log.d("RINE_debug", "de mod phiN == 1:"+
                d.multiply(e).mod(cPhiN).equals(one));
        SharedPreferences.Editor pref = getSharedPreferences("keys",
                MODE_PRIVATE).edit();
        pref.putString("n", n.toString());
        pref.putString("d", d.toString());
        pref.putString("e", e.toString());
        pref.apply();
        Log.d("RINE_debug", "keys written in PS.");
        Log.d("RINE_keys", "n = "+n.toString());
    }

    public String decrypt(String cipherText) {
        /*
        * @par cipherText: String, a String of numbers encrypted from function
        *   "encrypt". using RSA algorithm: n and d to decrypt. Use "utf-8" to encode.
        *
        * @result: String, plain text.
        * */
        SharedPreferences pref = getSharedPreferences("keys", MODE_PRIVATE);
        String nString = pref.getString("n", "");
        String dString = pref.getString("d", "");
        if (nString.equals("")) {
            Log.e("RINE_error", "No n in preference file!");
        }
        BigInteger n = new BigInteger(nString);
        BigInteger d = new BigInteger(dString);
        return Encrypter.decrypt(cipherText, n, d);
    }

    public void get(final String method) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    String urlString = "http://152.136.159.228:3000/"+method;
                    URL url = new URL(urlString);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(20000);
                    conn.setConnectTimeout(20000);
                    conn.setRequestMethod("GET");
                    InputStream in = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    getCallback(response.toString());
                }catch (Exception err) {
                    Log.e("RINE_error", err.toString());
                }finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    public void getCallback(String res) {
        gV.xLog("RINE_debug", "Get Received response:"+res);
    }
    public void post(final String method, final String jsonString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    String urlString = "http://152.136.159.228:3000/"+method;
                    URL url = new URL(urlString);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(20000);
                    conn.setConnectTimeout(20000);
                    conn.setRequestMethod("POST");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonString);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode=conn.getResponseCode(); // To Check for 200
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        gV.xLog("RINE_debug", "responseCode error:"+responseCode);
                    }

                    InputStream in = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    postCallback(response.toString());
                }catch (Exception err) {
                    Log.e("RINE_error", err.toString());
                }finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
    public void postCallback(String res) {
        gV.xLog("RINE_debug", "Post Response get:"+res);
    }
    public String parseJSONString(String jsonData, int mode, String params) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            if (mode == JSON_MODE_DIRECT) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String text = jsonObject.getString(params);
                    if (!text.equals("")) {
                        return text;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
