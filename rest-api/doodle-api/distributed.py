from http.client import HTTPException
from typing import List

from fastapi import FastAPI, Body
from pydantic import BaseModel

app = FastAPI()


class Vote(BaseModel):
    option: int
    username: str


class Poll(BaseModel):
    votes: List[Vote] = []


polls = {
    # "poll1": {
    #     "votes": [
    #         {
    #             "option": 1,
    #             "username": "user1"
    #         },
    #         {
    #             "option": 2,
    #             "username": "user2"
    #         }
    #     ]
    # }
}


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/polls")
async def get_polls():
    return {polls}


@app.get("/polls/{poll_name}")
async def get_poll(poll_name: str):
    return polls.get(poll_name)


@app.post("/polls")
async def create_poll(poll_name: str):
    polls[poll_name] = Poll()
    return {"message": f"Poll '{poll_name}' created."}


@app.delete("/polls/{poll_name}")
async def delete_poll(poll_name: str):
    if poll_name in polls:
        del polls[poll_name]
        return {"message": f"Poll '{poll_name}' deleted."}
    else:
        raise HTTPException(status_code=404, detail="Poll not found.")


@app.delete("/polls/{poll_name}/{vote_index}")
async def delete_vote(poll_name: str, vote_index: int):
    if poll_name in polls and vote_index < len(polls[poll_name].votes):
        del polls[poll_name].votes[vote_index]
        return {"message": f"Vote {vote_index} deleted from poll '{poll_name}'."}
    else:
        raise HTTPException(status_code=404, detail="Poll not found.")


@app.post("/polls/{poll_name}")
async def add_vote(poll_name: str, vote: Vote = Body()):
    if poll_name in polls:
        polls[poll_name].votes.append(vote)

        return {"message": f"Vote added to poll '{poll_name}'."}
    else:
        raise HTTPException(status_code=404, detail="Poll not found.")


@app.put("/polls/{poll_name}/{vote_index}")
async def update_vote(poll_name: str, vote_index: int, vote: Vote = Body()):
    if poll_name in polls and vote_index < len(polls[poll_name].votes):
        polls[poll_name].votes[vote_index] = vote
        return {"message": f"Vote {vote_index} updated in poll '{poll_name}'."}
    else:
        raise HTTPException(status_code=404, detail="Poll not found.")


@app.put("/polls/{poll_name}")
async def update_poll_name(poll_name: str):
    if poll_name in polls:
        poll = Poll(poll_name)
        poll.votes = polls[poll_name].votes
        del polls[poll_name]
        return {"message": f"Poll '{poll_name}' updated."}
    else:
        raise HTTPException(status_code=404, detail="Poll not found.")
