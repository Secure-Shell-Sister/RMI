#Python RMI

This implementation uses Pyro4. To install it run:

```bash
pip install pyro4
```
The server code uses nameserver for object discovery.

To run the nameserver simply run:
```bash
pyro4-ns
```
in separate terminal

##Client

The parameter for the client are:

```bash
client.py [path_to_log_folder] [host1, host2, ...]
```
