import {config} from "../src/js/gischat-config.js";

describe("gischat-config.js", () => {
  it("default pollingInterval is 5000", function () {
    expect(config().pollingInterval).toBe(5000);
  });
});

