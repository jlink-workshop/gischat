import {isFeatureActive, setFeature, clearFeature, clearAllFeatures} from "../src/js/gischat-features.js";

describe("gischat-features.js:", function () {

  beforeEach(() => {
    localStorage.clear();
  });

  it("feature by default not active", () => {
    expect(isFeatureActive("feature1")).toBeFalse();
  });

  it("active feature can be retrieved", () => {
    setFeature("feature1");

    expect(isFeatureActive("feature1")).toBeTrue();

    clearFeature("feature1");

    expect(isFeatureActive("feature1")).toBeFalse();
  });

  it("alle features can be cleared", () => {
    setFeature("feature1");
    setFeature("feature2");

    clearAllFeatures();

    expect(isFeatureActive("feature1")).toBeFalse();
    expect(isFeatureActive("feature2")).toBeFalse();
  });
});
