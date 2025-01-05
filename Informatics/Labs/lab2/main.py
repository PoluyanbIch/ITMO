def hamming_code(input_string: str):
    r = [input_string[0], input_string[1], input_string[3]]
    i = [input_string[2], input_string[4], input_string[5], input_string[6]]
    s1 = syndrome(r[0], i[0], i[1], i[3])
    s2 = syndrome(r[1], i[0], i[2], i[3])
    s3 = syndrome(r[2], i[1], i[2], i[3])
    S = s1 + s2 + s3
    err = error(S)
    if err == '':
        return ''.join(i), 'correct'
    if err[0] == 'i':
        i[int(err[1]) - 1] = str(1 - int(i[int(err[1]) - 1]))
    return ''.join(i), err


def syndrome(r: str, i1: str, i2: str, i3: str) -> str:
    return str([r, i1, i2, i3].count('1') % 2)


def error(s: str):
    d = {
        '000': '',
        '001': 'r3',
        '010': 'r2',
        '011': 'i3',
        '100': 'r1',
        '101': 'i2',
        '110': 'i1',
        '111': 'i4'
    }
    return d[s]


def validate_input():
    inp = input('Введите набор из 7 цифр "0" и "1"\n')
    while len(inp) != 7 or bool(set(inp) - {'0', '1'}):
        print('Incorrect input')
        inp = input()
    return inp


if __name__ == '__main__':
    inp = validate_input()
    print(*hamming_code(inp))