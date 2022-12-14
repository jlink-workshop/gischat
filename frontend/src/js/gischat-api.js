'use strict';

import {config} from "./gischat-config.js";

export async function getChat(chatId) {
  try {
    return await getJson("/api/chats/" + chatId)
  } catch (error) {
    return {
      title: `<Unknown chat with id ${chatId}>`,
      messages: []
    };
  }
}

export async function postChatMessage(chatId, user, text) {
  let path = "api/chats/" + chatId + "/messages";
  let data = {
    user: user,
    text: text
  }
  return await postJson(path, data);
}

export async function subscribeChatbot(chatId) {
  let chatApiUrl = baseUrl() + "/api/chats/" + chatId;
  let chatbotRequest = {
    url: chatApiUrl,
  }
  let path = pychatterUrl() + "/bots/subscribe/";
  console.log("Trying to subscribe chatbot on " + path, chatbotRequest);
  return await postJson(path, chatbotRequest);
}

export async function unsubscribeChatbot() {
  let path = pychatterUrl() + "/bots/unsubscribe/";
  console.log("Trying to unsubscribe chatbot on " + path);
  return await postJson(path);
}

export async function getChatbotStatus() {
  let path = pychatterUrl() + "/bots/status/";
  console.log("Checking if chat bot is listening " + path);
  return await getJson(path);
}

function baseUrl() {
  return location.protocol + "//" + location.host;
}

function pychatterUrl() {
  return config().pychatterUrl;
}

async function postJson(path, data) {
  let body = data != null ? JSON.stringify(data) : null;
  const response = await fetch(path, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: body
  });
  return response.json();
}

async function getJson(path) {
  return (await fetch(path)).json();
}

