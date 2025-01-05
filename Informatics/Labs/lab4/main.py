def yaml2json(name_input_file: str, name_output_file='output1') -> str:
    with open(name_input_file, 'r', encoding='utf-8') as f:
        inp = f.readlines()
    result = '{\n'
    prev_tabs = 0
    for line in inp:
        line = line.rstrip()
        key = line.split(':', 1)[0].strip()
        tabs = 1
        for i in range(0, len(line), 2):
            if line[i] == ' ':
                tabs += 1
            else:
                break
        if prev_tabs == tabs:
            result += ',\n'
        elif tabs > prev_tabs and prev_tabs != 0:
            result += '\n'
        elif tabs < prev_tabs:
            result += '\n'
            for i in range(prev_tabs - 1, tabs - 1, -1):
                if i == tabs:
                    result += f'{i * "    "}{"},"}'
                else:
                    result += f'{i * "    "}{"}"}'
                result += '\n'
        if line.split(':', 1)[1] == '':
            result += f'{tabs * "    "}"{key}": {"{"}'
        else:
            var = line.split(':', 1)[1].strip()
            result += f'{tabs * "    "}"{key}": "{var}"'
        prev_tabs = tabs

    result += '\n'
    for i in range(prev_tabs - 1, -1, -1):
        result += f'{i * "    "}{"}"}'
        result += '\n'

    with open(f'{name_output_file}.json', 'w', encoding='utf-8') as f:
        f.write(result)

    return result


if __name__ == '__main__':
    yaml2json('input.yaml')