import {createTextElement} from "../src/js/gischat.js";

describe("gischat.js:", function () {

  it("displays multi line message with line breaks", () => {
    const textElement = createTextElement("", "line1\nline2");

    expect(
        textElement.querySelector("span.messageText").innerHTML
    ).toBe("line1<br>line2");
  });
});
