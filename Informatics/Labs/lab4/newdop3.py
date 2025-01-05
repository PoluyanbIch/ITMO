def get_count_spaces(line: str) -> int:
    if line.lstrip()[0] == '-':
        return len(line) - len(line.lstrip()) + 2
    return len(line) - len(line.lstrip())


def convert_values(value: str):
    if value.lower() in ['true', 'yes', 'on', 'y']:
        return 'true'
    if value.lower() in ['false', 'no', 'off', 'n']:
        return 'false'
    try:
        return float(value)
    except ValueError:
        ...
    try:
        if int(value) == value:
            return int(value)
    except ValueError:
        ...
    if value == '':
        return value
    if '\\' in value:
        new_value = ''
        i = 0
        while i < len(value):
            if value[i] != '\\':
                new_value += value[i]
            else:
                new_value += '\\'
                i += 1
            i += 1
        value = new_value
    if value[0] == '"' and value[-1] == '"':
        return value
    return '"' + value + '"'


def object_parser(inp: list[str], start_line_index: int, spaces_num_start: int) -> dict:
    result = {}
    i = start_line_index
    while i < len(inp) and ((get_count_spaces(inp[i]) >= spaces_num_start and '-' not in inp[i]) or i == start_line_index):
        line = inp[i]
        spl = line.strip().split(':')
        if line.strip()[0:2] == '- ':
            key = convert_values(spl[0].strip()[2:])
        else:
            key = convert_values(spl[0].strip())
        var = spl[1].strip()
        if var == '':
            result[key] = object_parser(inp, i + 1, get_count_spaces(inp[i]))
            i += 1
            while i < len(inp) and get_count_spaces(inp[i]) > get_count_spaces(inp[start_line_index]):
                i += 1
            i -= 1
        else:
            var = convert_values(var)
            result[key] = var
        i += 1
    return result


def yaml2dict(inp: list[str], start_line_index: int = 0, spaces_num_start: int = 0):
    result = {}
    i = start_line_index
    while i < len(inp):
        spaces_num = get_count_spaces(inp[i])
        line = inp[i]
        spl = line.strip().split(':', 1)

        if line.strip()[0:2] == '- ':
            result = []
            spaces_num_start = get_count_spaces(line)
            while get_count_spaces(inp[i]) >= spaces_num_start:
                line = inp[i]
                if line.strip()[0] == '-':
                    if ':' not in line and line.strip() != '-':
                        result.append(convert_values(line.strip()[2:]))
                    elif ':' in line:
                        # object
                        result.append(object_parser(inp, i, get_count_spaces(inp[i])))
                        i += 1
                        while get_count_spaces(inp[i]) >= spaces_num and inp[i].strip()[0] != '-':
                            i += 1
                        i -= 1
                    elif line.strip() == '-':
                        result.append(yaml2dict(inp, i + 1, get_count_spaces(line) + 4))
                        i += 1
                        while i < len(inp) and get_count_spaces(inp[i]) > spaces_num:
                            i += 1
                        i -= 1
                i += 1
            return result

        if len(spl) == 2 and spl[1] != '' and '[' not in line:
            # pair
            key = convert_values(spl[0].strip())
            var = convert_values(spl[1].strip())
            result[key] = var
        elif len(spl) == 2 and spl[1] != '' and '[' in line:
            # list
            key = convert_values(spl[0].strip())
            var = [convert_values(i) for i in spl[1].strip()[1:-1].split(', ')]
            result[key] = var
        elif len(spl) == 2 and spl[1] == '' and i + 1 < len(inp) and inp[i + 1].lstrip()[0:2] == '- ':
            # array
            key = convert_values(spl[0].strip())
            var = yaml2dict(inp, i + 1, spaces_num + 4)
            result[key] = var
            i += 1
            while i < len(inp) and get_count_spaces(inp[i]) != spaces_num:
                i += 1
            i -= 1
        else:
            key = convert_values(spl[0].strip())
            result[key] = object_parser(inp, i + 1, spaces_num + 2)
            i += 1
            while i < len(inp) and get_count_spaces(inp[i]) != spaces_num:
                i += 1
            i -= 1
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
        print(dict2json(yaml2dict(lines)), file=out)


if __name__ == '__main__':
    convert('etalon.yaml')
