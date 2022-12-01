import os
from types import MappingProxyType
from unittest.mock import patch

from pychatter.config import config


def test_load_config_from_file():
    path = config.get_config_path()
    cfg = config.load_config_from_file(path)
    assert isinstance(cfg, dict)


def test_load_config_from_env_set(monkeypatch):
    monkeypatch.setenv("OPENAI_API_KEY", "foo")
    cfg = config.load_config_from_env()
    assert cfg == {"openai-api-key": "foo"}


def test_load_config_from_env_unset(monkeypatch):
    if os.getenv("OPENAI_API_KEY") is not None:
        monkeypatch.delenv("OPENAI_API_KEY")
    cfg = config.load_config_from_env()
    assert cfg == {}


def test_get_config_set_env(monkeypatch):
    monkeypatch.setenv("OPENAI_API_KEY", "foo")
    cfg = config.get_config()
    assert cfg == MappingProxyType({"openai-api-key": "foo"})


def test_get_config_unset_env(monkeypatch):
    if os.getenv("OPENAI_API_KEY") is not None:
        monkeypatch.delenv("OPENAI_API_KEY")
    with patch("pychatter.config.config.get_config_path") as mock_request:
        mock_request.return_value = ""
        cfg = config.get_config()
        assert cfg == MappingProxyType({})
