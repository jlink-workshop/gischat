'use strict';

export function isFeatureActive(featureName) {
  let feature = normalize(featureName);
  return getFeatures().includes(feature);
}

export function setFeature(featureName) {
  let feature = normalize(featureName);
  let features = getFeatures();
  if (features.includes(feature)) {
    return;
  }
  features.push(feature);
  writeFeatures(features);
}

export function clearFeature(featureName) {
  let feature = normalize(featureName);
  let features = getFeatures();
  features = features.filter(f => f !== feature);
  writeFeatures(features);

}

export function clearAllFeatures() {
  localStorage.removeItem(FEATURES_KEY);
}

const FEATURES_KEY = "gischat.features";

function writeFeatures(features) {
  localStorage.setItem(FEATURES_KEY, JSON.stringify(features));
}

function getFeatures() {
  return JSON.parse(localStorage.getItem(FEATURES_KEY) || "[]");
}

function normalize(featureName) {
  return featureName.trim().toLowerCase();
}
