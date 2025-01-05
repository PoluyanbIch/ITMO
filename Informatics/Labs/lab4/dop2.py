import re


def yaml2json_with_re(name_input_file: str, name_output_file: str = 'output1.json') -> str:
    with open(name_input_file, 'r', encoding='utf-8') as f:
        inp = f.readlines()
    result = '{\n'
    tabs = 0
    for i in range(len(inp) - 1):
        line = inp[i].lstrip()
        nextline = inp[i + 1].lstrip()
        spl = line.split(':', maxsplit=1)
        key = spl[0].strip()
        var = spl[1].strip()
        if re.match(r'^[А-ЯA-Zа-яa-z()0-9]+\s*:\s*\n$', line):
            result += ('\t' * (tabs + 1) + '"' + key + '":\n' + '\t' * (tabs + 1) + '{\n')
            tabs += 1
        else:
            if re.match(r'^[А-ЯA-Zа-яa-z()0-9]+\s*:\s*\n$', nextline):
                result += ('\t' * (tabs + 1) + '"' + key + '":' + '"' + var + '"\n' + '\t' * (tabs + 1) + '},\n')
                tabs -= 1
            else:
                result += ('\t' * (tabs + 1) + '"' + key + '":' + '"' + var + '",\n')
    lastline = inp[-1].lstrip()
    spl = lastline.split(':', maxsplit=1)
    result += ('\t' * (tabs + 1) + '"' + spl[0].strip() + '":' + '"' + spl[1].strip() + '"' + '\n')
    for i in range(tabs, -1, -1):
        result += ('\t' * i + '}\n')

    with open(name_output_file, 'w', encoding='utf-8') as f:
        f.write(result)

    return result


if __name__ == '__main__':
    yaml2json_with_re('input.yaml')
