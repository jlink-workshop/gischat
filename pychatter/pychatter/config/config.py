import os
from types import MappingProxyType

import tomli


def get_config_path() -> str:
    """Get configuration file path."""
    return os.path.abspath(
        os.path.join(
            os.path.dirname(
                os.path.abspath(__file__),
            ),
            "config.toml",
        ),
    )


def load_config_from_file(path: str) -> dict:
    """Load configuration from file on disk."""
    if os.path.isfile(path):
        with open(path, "rb") as f:
            return tomli.load(f)
    else:
        return {}


def load_config_from_env() -> dict:
    """Load configuration from environment variables."""
    cfg = {"openai-api-key": os.getenv("OPENAI_API_KEY")}
    return {k: v for k, v in cfg.items() if v is not None}


def get_config() -> MappingProxyType:
    """Get configuration variables from environment and file.

    Configuration values from environment variables will be given precedence over file
    values.
    """
    path = get_config_path()
    cfg_file = load_config_from_file(path)
    cfg_env = load_config_from_env()
    cfg = {}
    cfg.update(cfg_file)
    cfg.update(cfg_env)
    return MappingProxyType(cfg)
