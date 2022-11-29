Some npm commands require shell access, therefore a shell must be set:

```
npm config set script-shell "C:\\Program Files\\git\\bin\\bash.exe"
```

This can be undone through:
```
npm config delete script-shell
```

See https://stackoverflow.com/questions/23243353/how-to-set-shell-for-npm-run-scripts-in-windows