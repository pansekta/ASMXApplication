package com.example.sekta.asmxapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import org.ksoap2.transport.HttpsTransportSE;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.HostnameVerifier;


/**
 * Created by Sekta on 2015-10-31.
 */
public class SSLConnection {

    private static String NAMESPACE = "http://tempuri.org/";
    private static String HOST = "10.0.2.2";
    private static int PORT = 443;
    private static String FILE = "/Jackson/WebService.asmx";
    private static int TIMEOUT = 10000;
    private static String SOAP_ACTION = "http://tempuri.org/";

    private Context context;

    public SSLConnection(Context context) {
        this.context = context;
    }

    public boolean SecurePostLogin(String login, String password, String webMethName) throws Exception {
        boolean resTxt = false;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo loginProperty = new PropertyInfo();
        // Set Name
        loginProperty.setName("login");
        // Set Value
        loginProperty.setValue(login);
        // Set dataType
        loginProperty.setType(String.class);
        // Add the property to request object
        request.addProperty(loginProperty);

        PropertyInfo passwordProperty = new PropertyInfo();
        // Set Name
        passwordProperty.setName("password");
        // Set Value
        passwordProperty.setValue(password);
        // Set dataType
        passwordProperty.setType(String.class);
        // Add the property to request object
        request.addProperty(passwordProperty);
        // Create envelope

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);

        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        HttpsTransportSE transport = new HttpsTransportSE(HOST, PORT, FILE, TIMEOUT);
        ((HttpsServiceConnectionSE) transport.getServiceConnection()).setSSLSocketFactory(getSSLSocketFactory());


        try {
            // Invole web service
            transport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = Boolean.valueOf(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = false;
        }

        return resTxt;
    }

    private SSLSocketFactory getSSLSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager assetManager = context.getAssets();
        InputStream caInput = new BufferedInputStream(assetManager.open("cajackson.cer"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, tmf.getTrustManagers(), null);
        return context.getSocketFactory();
    }
}
