package cloudcode.helloworld.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.cloud.dialogflow.cx.v3.DetectIntentRequest;
import com.google.cloud.dialogflow.cx.v3.QueryResult;

import java.util.UUID;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

/** Defines a controller to handle HTTP requests */
@RestController
public final class HelloWorldController {

  private static String project;
  private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

  /**
   * Create an endpoint for the landing page
   *
   * @return the index view template
   */
  @GetMapping("/")
  public ModelAndView helloWorld(ModelMap map, Prompt prompt) {
   // String responseFromObj = prompt.getResponse();
    String addressFromObj = prompt.getAddress();
    String sessionFromObj = prompt.getSessiontext();
    String responseFromObj = null;
    if(responseFromObj != null){
      map.addAttribute("address", addressFromObj);
      map.addAttribute("session", sessionFromObj);
      map.addAttribute("response", responseFromObj);
    } else{
      String projectId = "*****";
      String locationId = "us-central1";
      String agentId = "***";
      String languageCode = "en";
      String address = projectId + ":" + locationId + ":" + agentId + ":" + languageCode;
      String session = address + ":" + UUID.randomUUID().toString();
      map.addAttribute("address", address);
      map.addAttribute("sessiontext", session);
    }
   
    return new ModelAndView("index", map);
  }

  
  /**
   * Create an endpoint for the landing page with user input text
   *
   * @return the index view template
   * @throws IOException
   * 
   */
  @GetMapping("/chat")
  public ModelAndView chat(ModelMap map, Prompt prompt) throws Exception {
    String sessionId = prompt.getSessiontext();
    String address = prompt.getAddress();
    String userPrompt = prompt.getPrompt();
    String projectId = address.split(":")[0].toString();
    String locationId = address.split(":")[1].toString();
    String agentId = address.split(":")[2].toString();
    String languageCode = address.split(":")[3].toString();
    String text = userPrompt;
    List<String> texts = List.of(userPrompt);
    DetectIntent di = new DetectIntent();
    String responseFromObj = di.invokeDetectIntentFunction(projectId, locationId, agentId, sessionId, text, languageCode);

    map.addAttribute("address", address);
    map.addAttribute("sessiontext", sessionId);
    map.addAttribute("response", responseFromObj);

    return new ModelAndView("index", map);
  }

}
