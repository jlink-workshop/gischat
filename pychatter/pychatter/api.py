import httpx
from apscheduler.schedulers.background import BackgroundScheduler
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

from pychatter.main import run

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class BotRequestModel(BaseModel):
    url: str = Field(
        title="URL",
        description="MapChat API URL of a chat room",
        example="https://gischat.herokuapp.com/api/chats/1",
    )


class BotResponseModel(BotRequestModel):
    id: str | None = Field(
        title="ID",
        description="",
        example="a75338e11f9e4798bc42551b3a201b86",
    )


@app.on_event("startup")
def init_scheduler():
    global scheduler
    scheduler = BackgroundScheduler(job_defaults={'max_instances': 3})
    scheduler.start()


@app.get("/hello")
async def root() -> dict:
    """Provide simple endpoint for testing."""
    return {"msg": "Hello World"}


@app.post("/bots/send", response_model=BotResponseModel)
async def send(bot: BotRequestModel):
    """Send a message from a bot to the given chat room once."""
    run(bot.url)
    return BotResponseModel(url=bot.url, id=None)


@app.post("/bots/subscribe", response_model=BotResponseModel)
async def subscribe(bot: BotRequestModel):
    """Create a chat bot which subscribes to the given chat room.

    The bot will fetch messages of the chat in a regular interval and reacts on
    mentioning of the bots name (Py).
    """
    job = scheduler.add_job(
        run,
        "interval",
        seconds=5,
        args=[bot.url],
        kwargs={"on_mention": True},
    )
    return BotResponseModel(url=bot.url, id=job.id)


@app.get("/bots/status")
async def status():
    """Verify if the chat bot is listening to any channels.
    """
    if len(scheduler.get_jobs()) > 0:
        return {"status": "active"}
    return {"status": "sleeping"}


@app.post("/bots/unsubscribe")
async def unsubscribe():
    """Unsubscribe a chatbot. It will currently unsubscribe all bots."""
    # TODO: This method should take a bot id as parameter and return an unsubscribe model

    count = len(scheduler.get_jobs())
    scheduler.remove_all_jobs()
    return {"count": count}


if __name__ == "__main__":
    url = "http://0.0.0.0:8080/bots/send"
    parameters = {
        "name": "Py",
        "url": "https://gischat.herokuapp.com/api/chats/1",
    }
    response = httpx.post(url, json=parameters)
    print(response.json())
