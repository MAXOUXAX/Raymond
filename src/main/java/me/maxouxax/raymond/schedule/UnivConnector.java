package me.maxouxax.raymond.schedule;

import me.maxouxax.raymond.Raymond;
import me.maxouxax.raymond.config.UnivConfig;
import me.maxouxax.supervisor.Supervisor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UnivConnector {

    private final HttpClient httpClient;
    private final Raymond raymond;
    private final Logger logger;
    private final UnivConfig univConfig;
    private String currentToken;
    private String refreshToken;

    public UnivConnector() {
        this.httpClient = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
        this.raymond = Raymond.getInstance();
        this.logger = Supervisor.getInstance().getLogger();
        this.univConfig = raymond.getConfig().getUnivConfig();
    }

    public void connect() throws IOException, URISyntaxException, InterruptedException {
        logger.info("Connecting to the Univ GQL API");
        String authObject = "username=" + univConfig.getUsername() + "&" +
                "password=" + URLEncoder.encode(univConfig.getPassword(), StandardCharsets.UTF_8) + "&";

        logger.info("Getting authentification ticket...");
        String authTicket = getTicket(univConfig.getAuthUrl(), "/login?service=" + univConfig.getDataUrl() + "/login", authObject);
        logger.info("Got authentification ticket: " + authTicket);

        logger.info("Getting authentification token...");
        JSONObject jsonObject = new JSONObject("{\"operationName\":\"casAuth\",\"variables\":{\"token\":" + authTicket + "},\"query\":\"query casAuth($token: String!) {\\n  casAuth(token: $token)\\n}\\n\"}");
        JSONObject authToken = makeGQLRequest(jsonObject);
        logger.info("Got authentification token, storing it...");

        JSONArray tokenArray = authToken.getJSONObject("data").getJSONArray("casAuth");
        this.currentToken = tokenArray.getString(0);
        this.refreshToken = tokenArray.getString(1);
    }

    public JSONObject makeGQLRequest(JSONObject query) throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(univConfig.getDataUrl() + "/graphql");

        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json");

        if (currentToken != null && refreshToken != null) {
            builder = builder
                    .header("x-refresh-token", refreshToken)
                    .header("x-token", currentToken);
        }

        HttpRequest gqlRequest = builder.POST(HttpRequest.BodyPublishers.ofString(query.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(gqlRequest, HttpResponse.BodyHandlers.ofString());

        return new JSONObject(response.body());
    }

    public String getTicket(String baseUrl, String endpoint, String credentials) throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(baseUrl + endpoint);
        HttpRequest loginPageRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();

        HttpResponse<String> loginPageResponse = httpClient.send(loginPageRequest, HttpResponse.BodyHandlers.ofString());
        String executionToken = loginPageResponse.body().split("<input type=\"hidden\" name=\"execution\" value=\"")[1].split("\"/>")[0];

        String formData = credentials + "&execution=" + executionToken + "&_eventId=submit";

        HttpRequest authRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = httpClient.send(authRequest, HttpResponse.BodyHandlers.ofString());

        Optional<String> location = response.headers().firstValue("Location");
        return location.map(s -> s.split("ticket=")[1]).orElse(null);
    }

    public UnivConfig getUnivConfig() {
        return univConfig;
    }

}
