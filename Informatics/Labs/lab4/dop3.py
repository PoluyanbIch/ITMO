def get_count_spaces(line: str) -> int:
    return len(line) - len(line.lstrip())


def convert_values(value: str):
    if value.lower() in ['true', 'yes', 'on']:
        return 'true'
    if value.lower() in ['false', 'no', 'off']:
        return 'false'
    try:
        return float(value)
    except ValueError:
        ...
    if int(value) == value:
        return int(value)
    if value == '':
        return value
    return '"' + value + '"'


def yaml2dict(inp: list[str], start_line_index: int = 0, spaces_num_start: int = 0) -> dict:
    result = {}
    i = start_line_index
    while i < len(inp):
        spaces_num = get_count_spaces(inp[i])
        line = inp[i]
        if line.lstrip()[0:2] == '- ':
            spaces_num += 2
            if result != {} and spaces_num == spaces_num_start:
                break
            line = inp[i].lstrip()[2:]
        if spaces_num > spaces_num_start:
            i += 1
            continue
        if spaces_num < spaces_num_start:
            break
        spl = line.strip().split(':', maxsplit=1)
        key = convert_values(spl[0].strip())
        if '[' in spl[1] and ']' in spl[1]:
            var = [convert_values(i) for i in spl[1].strip()[1:-1].split(', ')]
        else:
            var = convert_values(spl[1].strip())
        if var != '':
            result[key] = var
        elif i != len(inp) - 1 and inp[i + 1].lstrip()[0:2] == '- ':
            result[key] = []
            j = i + 1
            while get_count_spaces(inp[j]) != spaces_num:
                if get_count_spaces(inp[j]) == spaces_num + 2 and inp[j].lstrip()[0:2] == '- ':
                    result[key].append(yaml2dict(inp, j, spaces_num + 4))
                j += 1
        else:
            result[key] = yaml2dict(inp, i + 1, spaces_num + 2)
        i += 1
    return result


def dict2json(inp: dict) -> str:
    data = str(inp)
    data = data.replace("'", '')
    return data


def convert(yaml_file_name: str, json_file_name: str = 'output1.json'):
    with open(yaml_file_name, 'r', encoding='utf-8') as inp, open(json_file_name, 'w', encoding='utf-8') as out:
        lines = inp.readlines()
        lines = [i.split('#')[0] for i in lines]
        lines = [i for i in lines if i != '\n' and i != '']
        out.write(dict2json(yaml2dict(lines)))


if __name__ == '__main__':
    convert('etalon.yaml')
