package com.pigrafos.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigrafos.model.FinalResponse;
import com.pigrafos.model.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;

public class Client {
    private static final String BASE_URL = "https://gtm.delary.dev";
    private static final String LABYRINTH_PATH = "/labirintos";
    private static final String START_PATH = "/iniciar";
    private static final String MOVE_PATH = "/movimentar";
    private static final String VALIDATE_PATH = "/validar_caminho";

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String ACCEPT_JSON = "application/json";

    private static final int HTTP_OK = 200;

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Client() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCertificates = new TrustManager[]{new InsecureTrustManager()};
        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

        httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .build();
    }

    private static class InsecureTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }

    private HttpResponse executeRequest(HttpUriRequest request) throws IOException {
        try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HTTP_OK) {
                return httpResponse;
            } else {
                throw new IOException("Erro na solicitação: Código de status " + statusCode);
            }
        }
    }

    private String getResponseBody(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }

    private String buildJsonString(String user, String lab, String additionalParams) {
        return String.format("{\"id\":\"%s\",\"labirinto\":\"%s\",%s}", user, lab, additionalParams);
    }

    public List<String> checkLabs() throws IOException {
        String url = BASE_URL + LABYRINTH_PATH;
        HttpGet request = new HttpGet(url);

        HttpResponse httpResponse = executeRequest(request);
        String responseBody = getResponseBody(httpResponse);

        return Arrays.asList(objectMapper.readValue(responseBody, String[].class));
    }

    public Response apiStart(String user, String lab) throws IOException {
        String url = BASE_URL + START_PATH;
        HttpPost request = new HttpPost(url);
        String json = buildJsonString(user, lab, "");

        request.setEntity(new StringEntity(json));
        request.setHeader("Content-type", CONTENT_TYPE_JSON);
        request.setHeader("Accept", ACCEPT_JSON);

        HttpResponse response = executeRequest(request);
        String responseBody = getResponseBody(response);

        return objectMapper.readValue(responseBody, Response.class);
    }

    public Response apiMove(String user, String lab, int newPosition) throws IOException {
        String url = BASE_URL + MOVE_PATH;
        HttpPost request = new HttpPost(url);
        String json = buildJsonString(user, lab, "\"nova_posicao\":" + newPosition);

        request.setEntity(new StringEntity(json));
        request.setHeader("Content-type", CONTENT_TYPE_JSON);
        request.setHeader("Accept", ACCEPT_JSON);

        HttpResponse response = executeRequest(request);
        String responseBody = getResponseBody(response);

        return objectMapper.readValue(responseBody, Response.class);
    }

    public FinalResponse apiValided(String user, String lab, Object object) throws IOException {
        String url = BASE_URL + VALIDATE_PATH;
        HttpPost request = new HttpPost(url);
        String json = buildJsonString(user, lab, "\"todos_movimentos\":" + object);

        request.setEntity(new StringEntity(json));
        request.setHeader("Content-type", CONTENT_TYPE_JSON);
        request.setHeader("Accept", ACCEPT_JSON);

        HttpResponse response = executeRequest(request);
        String responseBody = getResponseBody(response);

        return objectMapper.readValue(responseBody, FinalResponse.class);
    }

    public void close() throws IOException {
        httpClient.close();
    }
}
