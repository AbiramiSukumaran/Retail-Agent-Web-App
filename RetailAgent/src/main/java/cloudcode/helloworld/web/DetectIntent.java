package cloudcode.helloworld.web;

import com.google.cloud.dialogflow.cx.v3.DetectIntentRequest;
import com.google.cloud.dialogflow.cx.v3.DetectIntentResponse;
import com.google.cloud.dialogflow.cx.v3.QueryInput;
import com.google.cloud.dialogflow.cx.v3.QueryResult;
import com.google.cloud.dialogflow.cx.v3.SessionName;
import com.google.cloud.dialogflow.cx.v3.SessionsClient;
import com.google.cloud.dialogflow.cx.v3.SessionsSettings;
import com.google.cloud.dialogflow.cx.v3.TextInput;
import com.google.api.core.ApiFuture;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.List;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.StringConcatFactory;
import java.io.ByteArrayInputStream;
import com.google.api.gax.rpc.ApiException;
import com.google.common.collect.Maps;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.api.client.util.Key;

import com.google.gson.Gson;


public class DetectIntent {

    public String invokeDetectIntentFunction(String projectId, String locationId, String agentId, String sessionId, String texts, String languageCode) throws Exception{
        // Create a Gson object for JSON serialization
        Gson gson = new Gson();   

          //invoke the REST endpoint https://us-central1-abis-345004.cloudfunctions.net/detectintent_python with JSON request body jsonRequest
        String functionUrl = "https://us-central1-*****.cloudfunctions.net/detectintent_python"; // Replace placeholders
        String data = "{\"project_id\": \"" + projectId + "\",\"location_id\": \"" + locationId + "\",\"agent_id\": \"" + agentId + "\",\"texts\": [\""+ sessionId + ":_+_:" + texts +"\"],\"language_code\": \"en-us\"}"; // JSON data (if applicable)
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(functionUrl))
                .POST(HttpRequest.BodyPublishers.ofString(data)) 
                .header("Content-Type", "application/json") // For JSON data
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
      }
      
    public String detectIntent(String projectId, String locationId, String agentId, String sessionId, List<String> texts, String languageCode) throws IOException, ApiException {
        SessionsSettings sessionsSettings =
         SessionsSettings.newBuilder()
             .setEndpoint(locationId + "-dialogflow.googleapis.com:443")
             .build();
        try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {
            SessionName session =
            SessionName.ofProjectLocationAgentSessionName(projectId, locationId, agentId, sessionId);
            System.out.println("Session Path: " + session.toString());

            StringBuilder responseBuilder = new StringBuilder();

            // Process each text input asynchronously
            for (String text : texts) {
                TextInput textInput = TextInput.newBuilder().setText(text).build();
                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).setLanguageCode(languageCode).build();
                DetectIntentRequest request = DetectIntentRequest.newBuilder().setSession(session.toString()).setQueryInput(queryInput).build();
                // Send the request asynchronously
                ApiFuture<DetectIntentResponse> futureResponse = sessionsClient.detectIntentCallable().futureCall(request);        
                // Retrieve the response and handle errors
                try {
                    DetectIntentResponse response = futureResponse.get();
                    QueryResult queryResult = response.getQueryResult();
                    responseBuilder.append("====================\n");
                    responseBuilder.append(String.format("Query Text: '%s'\n", queryResult.getText()));
                    responseBuilder.append(String.format("Detected Intent: %s (confidence: %f)\n",
                            queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence()));
                } catch (Exception e) {
                    System.err.println("Error detecting intent: " + e.getMessage());
                    responseBuilder.append("Error detecting intent.\n"); // Append error message to the response
                }
            }
            return responseBuilder.toString(); // Return the combined response
        }
    }
 
}