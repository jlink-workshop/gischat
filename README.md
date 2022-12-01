# GisChat

Code for the GiScience chat room application.

The user website is [here](https://jlink-workshop.github.io/gischat/).

## Local Build

### Requirements

- Java >= 17 installed and runnable in path, e.g. via [SDKMAN](https://sdkman.io/)
- Chrome browser installed and runnable in path

### Build and Run on the command line

```bash
./gradlew build
java -jar web/build/libs/web.jar
```

The first build can take some time due to Chrome driver installation.

The website should now be accessible on `http://localhost:8080`

### IntelliJ Configuration

> Preferences | Build, Execution, Deployment | Build Tools | Gradle | Build and Run using: `Gradle`

This makes sure that the frontend is built and copied to the web apps resource folder.

## Frontend Development

### Requirements
- Node 19.1.0
- npm 8.19.3
- Chrome browser installed and runnable in path

### Install and Build

```bash
cd frontend
npm install
npm run build
```

### Run Tests with Karma and Jasmine

Install Karma CLI globally:
```bash
npm install -g karma-cli
```

Continuous test run on headless Chrome - watching file changes:
```bash
karma start
```

Single test run on Chrome:
```bash
karma start --single-run --browsers Chrome
```

### IntelliJ Configuration

> Preferences | Languages & Frameworks | JavaScript | Code Quality Tools | ESLint | Automatic ESLint Configuration

This will show ESLint errors and warnings already in the editor.

## Heroku Deployment

### Requirements

- [Heroku](https://www.heroku.com/) account.
- Local installation of [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli).
- Project has been created with `heroku create --stack heroku-22 -a gischat --no-remote`.

### Manual Deployment

```bash
heroku plugins:install java
heroku login
./gradlew build
heroku deploy:jar web/build/libs/web.jar -a gischat
heroku logs --tail
```

Now the web app is accessible on `https://gischat.herokuapp.com`

Access Open API with `https://gischat.herokuapp.com/swagger-ui.html`

### Deployment through GitHub Action

Run action "Deploy App" on https://github.com/jlink-workshop/gischat/actions

### Viewing the Logs

```bash
heroku logs -a gischat --tail
```

### Restarting the App

```bash
heroku restart -a gischat
```

## Feature Toggles

You can switch on and off features by adding the following query parameter 
to the app's URL: `?features=feature1,feature2`.
Leave the list empty to clear all feature toggles.

The state of a feature toggle is saved in the browser's local storage.
That's why you only have to add the `features` query parameter once per browser.



### Current Features

| Feature     | Description                             | Default |
|-------------|-----------------------------------------|---------|
| `pychatter` | Enables the Pychatter Bot Checkbox      | `off`   |