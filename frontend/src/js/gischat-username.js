'use strict';

export function getUsername() {
  return localStorage.getItem("gischat.username") || "";
}

export function setUsername(newUserName) {
  return localStorage.setItem("gischat.username", newUserName);
}
