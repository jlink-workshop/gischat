from unittest.mock import patch

import httpx

from pychatter import main


def test_fetch_messages():
    with patch("httpx.get") as mock_request:
        mock_request.return_value = httpx.Response(
            status_code=200,
            content="""
                {
                  "title": "About Everything",
                  "messages": [
                    {
                      "user": "Greg",
                      "text": "How are you?"
                    },
                    {
                      "user": "John",
                      "text": "I am fine."
                    }
                  ],
                  "id": "1"
                }
            """,
        )
        messages = main.fetch_messages("https://www.example.org")
        assert messages == [
            {"user": "Greg", "text": "How are you?"},
            {"user": "John", "text": "I am fine."},
        ]


def test_send_message():
    with patch("httpx.post") as mock_request:
        mock_request.return_value = httpx.Response(
            status_code=200, content="{'time': '11:43:11', 'user': 'Py', 'text': 'Foo'}"
        )
        url = "https://gischat.herokuapp.com/api/chats/1"
        main.send_message(url=url, text="Foo")
        mock_request.assert_called_once_with(
            "https://gischat.herokuapp.com/api/chats/1/messages",
            json={"user": "Py", "text": "Foo"},
        )


def test_build_chat_promt():
    messages = [
        {"user": "Greg", "text": "How are you?"},
        {"user": "John", "text": "I am fine."},
    ]
    prompt = main.build_chat_prompt(messages)
    assert prompt == main.PRE_PROMPT + "\nGreg: How are you?\nJohn: I am fine.\nPy: "


def test_get_stop():
    messages = [
        {"user": "Alfred", "text": "Al"},
        {"user": "Betty", "text": "Best"},
        {"user": "Lucky", "text": "Luke"},
        {"user": "Greg", "text": "How are you?"},
        {"user": "John", "text": "I am fine."},
        {"user": "John", "text": "I am fine in a loop."},
    ]
    stop = main.get_stop(messages)
    assert stop == ["Betty:", "Lucky:", "Greg:", "John:"]


def test_make_completion():
    prompt = "Greg: I like chocolate ice cream!\n"
    with patch("openai.Completion.create") as mock_completion:
        mock_completion.return_value = {
            "choices": [
                {
                    "finish_reason": "stop",
                    "index": 0,
                    "logprobs": None,
                    "text": (
                        "\nI like chocolate ice cream because it is one of my "
                        + "favorite flavors. I also like it because it is very rich "
                        + "and creamy."
                    ),
                }
            ],
            "created": 1662463344,
            "id": "cmpl-5nQwqtcKOBq3BVz2QCQz4m7A8uvJ7",
            "model": "text-davinci-002",
            "object": "text_completion",
            "usage": {"completion_tokens": 27, "prompt_tokens": 7, "total_tokens": 34},
        }
        completion = main.make_completion(prompt, stop=["Greg:"])
        assert completion == (
            "I like chocolate ice cream because it is one of my favorite flavors. I "
            + "also like it because it is very rich and creamy."
        )


def test_dont_make_completion_without_prior_messages():
    prompt = main.build_chat_prompt([])
    stop = main.get_stop([])
    completion = main.make_completion(prompt, stop=stop)
    assert completion is None


# TODO: Add tests run()
