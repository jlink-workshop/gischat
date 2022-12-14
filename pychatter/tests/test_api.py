from unittest.mock import patch, PropertyMock

import httpx
import pytest
from fastapi import Response
from fastapi.testclient import TestClient

from pychatter.api import app, BotRequestModel, unsubscribe
from pychatter.api import subscribe


def mock_completion_fixture():
    return {
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


def mock_request_fixture():
    return httpx.Response(
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


def mock_response_fixture():
    return httpx.Response(
        status_code=200, content="{'time': '11:43:11', 'user': 'Py', 'text': 'Foo'}"
    )


@patch("httpx.post")
@patch("openai.Completion.create")
@patch("httpx.get")
def test_send_request(mock_request, mock_completion, mock_response):
    mock_request.return_value = mock_request_fixture()
    mock_completion.return_value = mock_completion_fixture()
    mock_response.return_value = mock_response_fixture()
    with TestClient(app) as client:
        response: Response = client.post(
            "/bots/send", json={"url": "https://gischat.herokuapp.com/api/chats/1"}
        )
        assert response.status_code == 200
        assert response.headers["content-type"] == "application/json"
        assert response.json() == {
            "url": "https://gischat.herokuapp.com/api/chats/1",
            "id": None,
        }
        mock_response.assert_called_once_with(
            "https://gischat.herokuapp.com/api/chats/1/messages",
            json={
                "user": "Py",
                "text": (
                    "I like chocolate ice cream because it is one of my favorite "
                    + "flavors. I also like it because it is very rich and creamy."
                ),
            },
        )


@patch("httpx.post")
@patch("openai.Completion.create")
@patch("httpx.get")
def test_subscribe_request(mock_request, mock_completion, mock_response):
    mock_request.return_value = mock_request_fixture()
    mock_completion.return_value = mock_completion_fixture()
    mock_response.return_value = mock_response_fixture()
    with TestClient(app) as client:
        response: Response = client.post(
            "/bots/subscribe", json={"url": "https://gischat.herokuapp.com/api/chats/1"}
        )
        assert response.status_code == 200
        assert response.headers["content-type"] == "application/json"
        json = response.json()
        assert json["url"] == "https://gischat.herokuapp.com/api/chats/1"
        assert isinstance(json["id"], str)


# TODO: Add test for status request


# TODO: Add tests for unsubscribe request

@pytest.mark.asyncio
@patch("apscheduler.schedulers.background.BackgroundScheduler.add_job")
async def test_subscribe(mock_add_job):
    bot_request = BotRequestModel(url="https://gischat.app/api/chats/4711")
    type(mock_add_job.return_value).id = PropertyMock(return_value="42")

    response = await subscribe(bot_request)
    mock_add_job.assert_called_once()
    assert response == {"url": "https://gischat.app/api/chats/4711", "id": "42"}


@pytest.mark.asyncio
@patch("apscheduler.schedulers.background.BackgroundScheduler.get_jobs")
@patch("apscheduler.schedulers.background.BackgroundScheduler.remove_all_jobs")
async def test_unsubscribe(mock_get_jobs, mock_remove_all_jobs):
    mock_get_jobs.return_value = []
    response = await unsubscribe()

    mock_remove_all_jobs.assert_called_once()
    assert response == {"count": 0}
