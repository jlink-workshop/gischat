'use strict';

import {getChat, getChatbotStatus, postChatMessage, subscribeChatbot, unsubscribeChatbot} from "./gischat-api.js";
import {config} from "./gischat-config.js";
import {getUsername, setUsername} from "./gischat-username.js";
import {clearAllFeatures, isFeatureActive, setFeature} from "./gischat-features.js";

export function onLoad() {
  evalFeatureToggles(location);
  toggleDependentPreHook();
  route(location.pathname);
}

export async function onSendMessage() {
  let userName = document.getElementById("userName").value;
  let messageText = document.getElementById("messageText").value;
  let newMessage = await postChatMessage("1", userName, messageText);
  document.getElementById("messageText").value = "";
  let messagesElement = document.getElementById("messages");
  addMessageTo(newMessage, messagesElement);
  return false;
}


export function onUserNameChange(event) {
  let newUserName = event.target.value;
  setUsername(newUserName);
}

export async function onChangeChatbotEnabled(event) {
  let chatbotCheckbox = document.getElementById("chatbotEnabled");
  chatbotCheckbox.disabled = true;
  if (event.target.checked) {
    await subscribePychatterBot();
    chatbotCheckbox.disabled = false;
  } else {
    await unsubscribePychatterBot();
    chatbotCheckbox.disabled = false;
  }
}

async function subscribePychatterBot() {
  await subscribeChatbot(1);
}

async function unsubscribePychatterBot() {
  await unsubscribeChatbot(1);
}

function evalFeatureToggles(location) {
  const urlParams = new URLSearchParams(location.search);
  const featuresParam = urlParams.get('features');
  if (featuresParam != null) {
    clearAllFeatures();
    const features = featuresParam.split(',').map(f => f.trim()).filter(f => f !== "");
    features.forEach(f => setFeature(f));
  }
}

function toggleDependentPreHook() {
  if (isFeatureActive("pychatter")) {
    document.getElementById("feature-pychatter").style.visibility = "visible";
  }
}

function initializeUserName() {
  document.getElementById("userName").value = getUsername();
}

function route(pathname) {
  if (pathname === "" || pathname === "/" || pathname === "/index.html") {
    location.pathname = "/chat/1";
    return;
  }
  if (pathname.startsWith("/chat/")) {
    let chatId = pathname.substring(6)
    loadChat(chatId);
    initializeUserName();
    pollChat(chatId);
    pollStatus(chatId);
    return;
  }
  setErrorMessage("Unknown route: " + pathname);
}

async function loadChat(chatId) {
  let chatResponse = await getChat(chatId);
  fillInChat(chatResponse);
  chatResponse = await getChatbotStatus(chatId);
  toggleBotCheckbox(chatResponse, document.getElementById('chatbotEnabled'));
}

function pollChat(chatId) {
  setInterval(async () => {
        let chatResponse = await getChat(chatId);
        fillInChat(chatResponse);
      },
      config().pollingInterval
  );
}

function pollStatus(chatId) {
  setInterval(async () => {
        let chatResponse = await getChatbotStatus(chatId);
        console.log("polling pychatter status:", chatResponse);
        toggleBotCheckbox(chatResponse, document.getElementById('chatbotEnabled'));
      },
      config().pollingInterval
  );
}

function fillInChat(chatResponse) {
  setChatTitle(chatResponse.title);
  setChatMessages(chatResponse.messages)
}

function setChatTitle(title) {
  let titleElement = document.getElementById("chatTitle");
  titleElement.textContent = title;
}

function setChatMessages(messages) {
  let messagesElement = document.getElementById("messages");
  messagesElement.replaceChildren();
  messages.forEach(message => addMessageTo(message, messagesElement))
}

function addMessageTo(message, parent) {
  let messageElements = document.createDocumentFragment();
  let userElement = createUserElement(message.user);
  let textElement = createTextElement(message.time, message.text);

  messageElements.append(userElement, textElement);
  parent.append(messageElements);
  scrollToBottom(parent);
}

function createUserElement(user) {
  let userElement = document.createElement("div");
  userElement.classList.add("user");
  userElement.append(document.createTextNode(user))
  return userElement;
}

export function createTextElement(time, text) {
  let textElement = document.createElement("div");
  textElement.classList.add("message");
  let timeElement = document.createElement("span");
  timeElement.classList.add("messageTime");
  timeElement.append(document.createTextNode(time));
  textElement.append(timeElement);
  const messageElement = document.createElement("span");
  messageElement.classList.add("messageText");
  const lines = text.split('\n');
  lines.forEach((line, index) => {
    if (index > 0) {
      messageElement.append(document.createElement("br"));
    }
    const lineElement = document.createTextNode(line);
    messageElement.append(lineElement);
  });
  textElement.append(messageElement);
  return textElement;
}

function scrollToBottom(div) {
  div.scrollTop = div.scrollHeight - div.clientHeight;
}

function setErrorMessage(message) {
  let errorElement = document.getElementById("error");
  errorElement.textContent = message;
}

export function toggleBotCheckbox(status, checkbox) {
  checkbox.checked = status.status === 'active';
}

