import functions_framework
from google.cloud import dialogflowcx_v3beta1 as dialogflow
import argparse
import uuid

from google.cloud.dialogflowcx_v3beta1.services.agents import AgentsClient
from google.cloud.dialogflowcx_v3beta1.services.sessions import SessionsClient
from google.cloud.dialogflowcx_v3beta1.types import session

@functions_framework.http
def hello_get(request):
    request_json = request.get_json(silent=True)
    print(f"Request JSON: {request_json}")
    if request_json and 'project_id' in request_json and 'location_id' in request_json and 'agent_id' in request_json and 'texts' in request_json and 'language_code' in request_json:
        project_id = request_json['project_id']
        location_id = request_json['location_id']
        agent_id = request_json['agent_id']
        texts = request_json['texts']
        language_code = request_json['language_code']
        return run_sample(project_id, location_id, agent_id, texts, language_code)
    else:
        return 'Missing required parameters in request JSON', 400

def run_sample(project_id, location_id, agent_id, texts, language_code):
    session_id = texts[0].split(":_+_:")[0];
    return detect_intent_texts(agent_id, session_id, texts, language_code)


def detect_intent_texts(agent, session_id, texts, language_code):
    """Returns the result of detect intent with texts as inputs.
    Using the same `session_id` between requests allows continuation
    of the conversation."""
    session_path = f"{agent}/sessions/{session_id}"
    client_options = None
    agent_components = AgentsClient.parse_agent_path(agent)
    location_id = agent_components["location"]
    if location_id != "global":
        api_endpoint = f"{location_id}-dialogflow.googleapis.com:443"
        print(f"API Endpoint: {api_endpoint}\n")
        client_options = {"api_endpoint": api_endpoint}
    session_client = SessionsClient(client_options=client_options)

    for text in texts:
        text_input = session.TextInput(text=text.split(":_+_:")[1])
        query_input = session.QueryInput(text=text_input, language_code=language_code)
        request = session.DetectIntentRequest(
            session=session_path, query_input=query_input
        )
        response = session_client.detect_intent(request=request)

        print("=" * 20)
        print(f"Query text: {response.query_result.text}")
        response_messages = [
            " ".join(msg.text.text) for msg in response.query_result.response_messages
        ]
        print(f"Response text: {' '.join(response_messages)}\n")
        resultprint = f"Response text: {' '.join(response_messages)}\n"

    return resultprint
