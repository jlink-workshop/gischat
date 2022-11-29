import {getUsername, setUsername} from "../src/js/gischat-username.js";

describe("gischat-username.js:", function () {

  beforeEach(() => {
    localStorage.clear();
  });

  it("default username is ''", () => {
    expect(getUsername()).toBe("");
  });

  it("set username can be retrieved", () => {
    setUsername("Francis");

    expect(getUsername()).toBe("Francis");
  });
});
