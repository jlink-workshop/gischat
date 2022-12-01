// eslint-disable-next-line no-undef
module.exports = {
  "plugins": ["jasmine"],
  "env": {
    "jasmine": true,
    "browser": true,
    "es2021": true
  },
  "extends": [
    "eslint:recommended",
    "plugin:jasmine/recommended"
  ],
  "parserOptions": {
    "ecmaVersion": "latest",
    "sourceType": "module"
  },
  "rules": {}
}
