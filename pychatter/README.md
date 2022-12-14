# PyChatter

## Requirements

- Python: 3.10, e.g. through [pyenv](https://github.com/pyenv/pyenv)

## Installation

Make sure you're in directory `pychatter`.
Then run the following commands:

```bash
python -m venv venv
source venv/bin/activate
pip install --upgrade pip
pip install --upgrade setuptools
pip install --upgrade build
pip install --editable .
```

## Configuration

PyChatter can be configured using a configuration file or environment variables.

| Configuration Variable Name | Environment Variable Name | Name in Configuration File | Default Value                                       |
| --------------------------- | ------------------------- |----------------------------| --------------------------------------------------- |
| OpenAI API Key              | `OPENAI_API_KEY`          | `openai-api-key`           | `none`                                              |


### Environment Variable

```bash
export OPENAI_API_KEY='sk-...'
```

### Configuration File

The path of the configuration file is `gischat/pychatter/pychatter/config/config.toml`.
A sample configuration file can be found in the same directory: `gischat/pychatter/pychatter/config/sample.config.toml`.
All configuration files in this directory (`config`) will be ignored by Git.
To create a new configuration file simply copy the sample configuration file and change the values.

```bash
cp pychatter/config/sample.config.toml pychatter/config/config.toml
```

Then add a valid value for `open-ai-key` in `pychatter/config/config.toml`.

## Run Tests

```bash
pytest
```

## Usage

To start the API:
```bash
./scripts/start_api.sh
```

Now the API is available on `http://localhost:8081`.
Try out the Open API doc on `http://localhost:8081/docs`.

## Resources

The Google OpenAI GPT-3 model used to respond to text prompts is 'text-davinci-002'.
For more details see: https://beta.openai.com/docs/models/overview

## Heroku Deployment

### Requirements

- [Heroku](https://www.heroku.com/) account.
- Local installation of [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli).
- Project has been created with `heroku create --stack heroku-22 -a gischat-pychatter --no-remote`.

### Manual Deployment

```bash
heroku plugins:install heroku-builds
heroku login
heroku builds:create -a gischat-pychatter
heroku logs -a gischat-pychatter --tail
```

Now the pychatter API is accessible on `https://gischat-pychatter.herokuapp.com`

Access Open API with `https://gischat-pychatter.herokuapp.com/docs`

### Deployment through GitHub Action

Pychatter is automatically deployed on push. For a manual redeploy click 
run action "Deploy Pychatter" on https://github.com/jlink-workshop/gischat/actions

### Viewing the Logs

```bash
heroku logs -a gischat-pychatter --tail
```

### Restarting the App

```bash
heroku restart -a gischat-pychatter
```
