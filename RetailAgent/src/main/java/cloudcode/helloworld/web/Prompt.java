package cloudcode.helloworld.web;

/* Copyright 2022 Google LLC
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
*/


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Example POJO to demonstrate Spring Cloud GCP Spring Data Firestore operations. */

public class Prompt {
private String prompt;
private String response;
private String address;
private String sessiontext;

public String getSessiontext() {
    return sessiontext;
}
public void setSessiontext(String sessiontext) {
    this.sessiontext = sessiontext;
}
public String getAddress() {
    return address;
}
public void setAddress(String address) {
    this.address = address;
}
public String getResponse() {
    return response;
}
public void setResponse(String response) {
    this.response = response;
}

public String getPrompt() {
    return prompt;
}
public void setPrompt(String prompt) {
    this.prompt = prompt;
}

 
}