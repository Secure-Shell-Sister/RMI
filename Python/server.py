import Pyro4
import re

class LogParser(object):
    def __init__(self):
        super(LogParser, self).__init__
        self.matcher = re.compile('\[\d+\]: (.+?) (for|from)')
    def parse_log(self, log):
        print("Get a new parsing job!")
        lines = log.split('\n')
        category_counter = dict()
        matches = self.matcher.finditer(log)
        file_length = len(log.splitlines())
        matches_length = 0
        for match in matches:
            matches_length += 1
            key_string = ''
            if match.group(2) == 'for':
                key_string = match.group(1)
            else:
                separated_string = match.group(1).split(' ')
                if len(separated_string) > 1:
                    key_string = (' ').join(separated_string[:-1])
                else:
                    key_string = separated_string[0]

            if key_string in category_counter:
                category_counter[key_string] += 1
            else:
                category_counter[key_string] = 1

        category_counter["Unidentified"] = file_length - matches_length
        return category_counter

daemon = Pyro4.Daemon(port=8080, host="0.0.0.0")
ns = Pyro4.locateNS()
parser = LogParser()
uri = daemon.register(parser)
ns.register("example.LogParser", uri)

daemon.requestLoop()
