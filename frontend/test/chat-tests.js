import {createTextElement, toggleBotCheckbox} from "../src/js/gischat.js";

describe("gischat.js:", function () {

  it("displays multi line message with line breaks", () => {
    const textElement = createTextElement("", "line1\nline2");

    expect(
        textElement.querySelector("span.messageText").innerHTML
    ).toBe("line1<br>line2");
  });

  it("enable the checkbox if pychatter status is active", () => {
    const checkbox = document.createElement('checkbox');
    toggleBotCheckbox({"status":"active"}, checkbox);

    expect(checkbox.checked).toBeTrue();
  });

  it("disable the checkbox if pychatter status is sleeping", () => {
    const checkbox = document.createElement('checkbox');
    toggleBotCheckbox({"status":"sleeping"}, checkbox);

    expect(checkbox.checked).toBeFalse();
  });
});
