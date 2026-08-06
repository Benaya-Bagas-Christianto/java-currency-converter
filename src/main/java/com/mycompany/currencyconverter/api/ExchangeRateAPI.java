package com.mycompany.currencyconverter.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ExchangeRateAPI {

    private static final String API_URL = "https://api.frankfurter.app/latest";

    public static double getExchangeRate(String fromCurrency, String toCurrency) throws Exception {
        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }

        String urlString = API_URL + "?from=" + fromCurrency + "&to=" + toCurrency;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject rates = jsonResponse.getJSONObject("rates");
            return rates.getDouble(toCurrency);
        } else {
            throw new Exception("Failed to fetch data, Response Code: " + responseCode);
        }
    }
}
