package com.example.sekta.asmxapplication;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;

public class WebService {

    private static String NAMESPACE = "http://tempuri.org/";
    private static String URL = "http://192.168.56.1/Jackson/WebService.asmx";
    private static String SOAP_ACTION = "http://tempuri.org/";

    public static String invokeHelloWorldWS(String name, String webMethName) {
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo celsiusPI = new PropertyInfo();
        // Set Name
        celsiusPI.setName("name");
        // Set Value
        celsiusPI.setValue(name);
        // Set dataType
        celsiusPI.setType(String.class);
        // Add the property to request object
        request.addProperty(celsiusPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Error occured";
        }

        return resTxt;
    }

    public static boolean invokeLogin(String login, String password, String webMethName) {
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
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
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
}
