import re
import json


def main(s: str):
    pattern = r'[А-Я]\w+(?:-[А-Я]\w+)* (?:[А-Я]\.){1,2}(?:\W|$|\s)'
    return ' '.join(sorted(map(lambda i: i.split()[0], re.findall(pattern, s))))


inp = input('Введите данные: ')
res = main(inp)
print(res)

my_json = {}
my_json['answers'] = [res]

with open('result.json', 'w', encoding='utf-8') as f:
    dumped_json = json.dumps(my_json, ensure_ascii=False)
    f.write(dumped_json)