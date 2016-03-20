#!/usr/bin/env python3
import Pyro4
import sys
import os

if __name__ == "__main__":
    log_path = "/home/revant/Documents/SISTER/RPC/NewRPC/var/log"
    if len(sys.argv) > 1:
        log_path = sys.argv[1]

    servers = []
    if len(sys.argv) > 2:
        for host in sys.argv[2:]:
            servers.append("PYRONAME:example.LogParser@%s" % host)

    proxies = []
    for server in servers:
        proxy = Pyro4.Proxy(server)
        proxies.append(proxy)


    # log_parser = Pyro4.Proxy("PYRONAME:example.LogParser")
    results = []
    counter = 0
    category_counter = dict()
    total_server = len(proxies)
    for file_name in os.listdir(log_path):
        if file_name.startswith("secure"):
            proxy = proxies[counter]
            async = proxy._pyroAsync()

            logfile = open(log_path + "/" + file_name)
            result = async.parse_log(logfile.read())
            logfile.close()

            results.append(result)
            counter = (counter + 1) % total_server

    for result in results:
        category_map = result.value
        for key, value in category_map.items():
            if key in category_counter:
                category_counter[key] += value
            else:
                category_counter[key] = value

    for key, value in category_counter.items():
        print(key + " -> " + str(value))
