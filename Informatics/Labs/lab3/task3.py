import re
import json


def main(s: str):
    pattern = r"[+-]?\d+(\.0)?"
    s = s.split()
    for i in range(len(s)):
        if re.fullmatch(pattern, s[i]):
            s[i] = str((int(float(s[i])) ** 2) * 4 - 7)
    return ' '.join(s)


inp = input('Введите данные: ')
res = main(inp)
print(res)

my_json = {}
my_json['answers'] = [res]

with open('result.json', 'w', encoding='utf-8') as f:
    dumped_json = json.dumps(my_json, ensure_ascii=False)
    f.write(dumped_json)