'use strict';

import configuration from './config.js';

const defaultConfiguration = {
  pollingInterval: 5000,
  pychatterUrl: '<must be provided by server app>',
};

export function config() {
  let merged = {...defaultConfiguration, ...configuration};
  return merged;
}
