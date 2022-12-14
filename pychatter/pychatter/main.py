import logging

import httpx
import openai

from pychatter.config import config


PRE_PROMPT = "Py is an annoying chatbot that tends to avoid answering questions."


def run(url, on_mention=False):
    """Fetch messages, make completion and send a message to given chat room.

    Args:
        url: GisChat API URL of a chat room (E.g. https://gischat.herokuapp.com/chat/1).
        on_mention: Only send message to the chat room if bot name got mentioned.
    """
    messages = fetch_messages(url)
    if not should_answer(messages[-1], on_mention):
        return
    prompt = build_chat_prompt(messages)
    stop = get_stop(messages)
    completion = make_completion(prompt, stop)
    if completion:
        logging.info(completion)
        send_message(url=url, text=completion)


def fetch_messages(url: str) -> list:
    """Fetch all chat messages."""
    response = httpx.get(url)
    chat = response.json()
    return chat["messages"]


def send_message(url: str, text: str):
    httpx.post(url + "/messages", json={"user": "Py", "text": text})


def build_chat_prompt(messages: list) -> str | None:
    """Build a prompt representing a chat as text to generate completion.

    Args:
        messages (list): Each messages is a dictionary with the keys 'user' and 'text'.
    """
    if not messages:
        return None
    prompt = [
        "{user}: {text}".format(user=msg["user"], text=msg["text"]) for msg in messages
    ]
    prompt.insert(0, PRE_PROMPT)
    prompt.append("Py: ")
    return "\n".join(prompt)


def get_stop(messages) -> list:
    """Get the last active user names as stop sequences.

    Stop is an array with up to 4 sequences where the openai API will stop generating
    further tokens.
    """
    users = [msg["user"] for msg in messages]
    users_unique = list(dict.fromkeys(users))
    users_last_active = users_unique[-4:]
    return [user + ":" for user in users_last_active]


def make_completion(prompt: str, stop: list) -> str | None:
    """Make a completion for a given prompt."""
    if not prompt:
        return None
    conf = config.get_config()
    openai.api_key = conf["openai-api-key"]
    response = openai.Completion.create(
        engine="text-davinci-003",
        prompt=prompt,
        temperature=0.7,
        max_tokens=512,
        frequency_penalty=1.5,
        presence_penalty=1.0,
        stop=stop,
    )
    return response["choices"][0]["text"].strip()


def should_answer(message, on_mention):
    return (not on_mention or message["text"].find("Py") != -1) and message["user"] != "Py"


if __name__ == "__main__":
    # answer = make_completion("Frank: What's your name?\nPy:", ["Frank:"])
    # print(answer)
    run("https://gischat.herokuapp.com/api/chats/1")
