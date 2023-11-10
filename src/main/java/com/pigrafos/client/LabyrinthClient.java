package com.pigrafos.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigrafos.model.FinalResponse;
import com.pigrafos.model.LabyrinthBFS;
import com.pigrafos.model.LabyrinthDFS;
import com.pigrafos.model.LabyrinthGraph;
import com.pigrafos.model.LabyrinthResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.cert.X509Certificate;

public class LabyrinthClient {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LabyrinthClient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCertificates = new TrustManager[] { new InsecureTrustManager() };
        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
        httpClient = HttpClients.custom()
                .setSslcontext(sslContext)
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

    public List<String> checkLabyrinths() throws IOException {
        String url = "https://gtm.delary.dev/labirintos";
        HttpGet request = new HttpGet(url);

        CloseableHttpResponse httpResponse = httpClient.execute(request);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(entity);
            String[] labyrinths = objectMapper.readValue(responseBody, String[].class);

            return Arrays.stream(labyrinths).toList();
        } else {
            throw new IOException("Erro na solicitação: Código de status " + statusCode);
        }
    }

    public LabyrinthResponse startExploration(String user, String labyrinths) throws IOException {
        String url = "https://gtm.delary.dev/iniciar";
        HttpPost request = new HttpPost(url);
        String json = "{\"id\":\"" + user + "\",\"labirinto\":\"" + labyrinths + "\"}";

        final StringEntity myEntity = new StringEntity(json);

        request.setEntity(myEntity);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Accept", "application/json");

        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            return objectMapper.readValue(responseBody, LabyrinthResponse.class);
        } else {
            throw new IOException("Erro na solicitação de exploração: Código de status " + statusCode);
        }
    }

    public LabyrinthResponse move(String user, String labyrinths, int newPosition) throws IOException {
        String url = "https://gtm.delary.dev/movimentar";
        HttpPost request = new HttpPost(url);
        String json = "{\"id\":\"" + user + "\",\"labirinto\":\"" + labyrinths + "\",\"nova_posicao\":" + newPosition
                + "}";

        final StringEntity myEntity = new StringEntity(json);

        request.setEntity(myEntity);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Accept", "application/json");

        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            return objectMapper.readValue(responseBody, LabyrinthResponse.class);
        } else {
            throw new IOException("Erro na solicitação de movimento: Código de status " + statusCode);
        }
    }

    public FinalResponse pathValidator(String user, String labyrinths, List<Integer> todosMovimentos)
            throws IOException {
        String url = "https://gtm.delary.dev/validar_caminho";
        HttpPost request = new HttpPost(url);
        String json = "{\"id\":\"" + user + "\",\"labirinto\":\"" + labyrinths + "\",\"todos_movimentos\":"
                + todosMovimentos + "}";

        final StringEntity myEntity = new StringEntity(json);

        request.setEntity(myEntity);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Accept", "application/json");
        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            return objectMapper.readValue(responseBody, FinalResponse.class);
        } else {
            throw new IOException("Erro na solicitação de movimento: Código de status " + statusCode);
        }
    }

    public List<Integer> findShortestPathbfs(String user, String labyrinths) throws IOException {
        List<LabyrinthResponse> responses = getLabyrinthResponses(user, labyrinths);

        LabyrinthGraph graph = new LabyrinthGraph();
        graph.buildGraph(responses);

        LabyrinthBFS bfs = new LabyrinthBFS(graph);
        int startVertex = responses.get(0).getActualPosition();
        int endVertex = getEndVertex(responses);

        List<Integer> path = bfs.findShortestPath(startVertex, endVertex);

        int apiRequests = responses.size();

        System.out.println("Número de requisições API para BFS: " + apiRequests);

        return path;
    }

    // public List<Integer> findShortestPathDFS(String user, String labyrinths)
    // throws IOException {
    // List<LabyrinthResponse> responses = getLabyrinthResponses(user, labyrinths);

    // LabyrinthGraph graph = new LabyrinthGraph();
    // graph.buildGraph(responses);

    // LabyrinthDFS dfs = new LabyrinthDFS(graph);
    // int startVertex = responses.get(0).getActualPosition();
    // int endVertex = getEndVertex(responses);

    // List<Integer> path = dfs.findExitPath(startVertex, endVertex);

    // int apiRequests = responses.size();

    // System.out.println("Número de requisições API para bfs: " + apiRequests);

    // return path;
    // }

    private List<LabyrinthResponse> getLabyrinthResponses(String user, String labyrinths) throws IOException {
        List<String> labyrinthIds = checkLabyrinths();
        List<LabyrinthResponse> responses = new ArrayList<>();

        for (String labyrinthId : labyrinthIds) {
            LabyrinthResponse response = startExploration(user, labyrinthId);
            responses.add(response);
        }

        return responses;
    }

    private int getEndVertex(List<LabyrinthResponse> responses) {
        for (LabyrinthResponse response : responses) {
            if (response.isFinal()) {
                return response.getActualPosition();
            }
        }

        throw new IllegalStateException("Labirinto não possui uma posição final.");
    }
}