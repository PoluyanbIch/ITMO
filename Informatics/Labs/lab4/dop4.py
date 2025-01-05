from datetime import datetime
import main
import dop1
import dop2
import newdop3


if __name__ == '__main__':
    input_file = 'input.yaml'
    start = datetime.now()
    print('Start first algorithm 1000 times')
    for i in range(1000):
        _ = main.yaml2json(input_file)
    print(f'First algorithm end, time need {(datetime.now() - start).total_seconds()} sec')

    start = datetime.now()
    print('Start second algorithm 1000 times')
    for i in range(1000):
        _ = dop1.parse(input_file)
    print(f'Second algorithm end, time need {(datetime.now() - start).total_seconds()} sec')

    start = datetime.now()
    print('Start third algorithm 1000 times')
    for i in range(1000):
        _ = dop2.yaml2json_with_re(input_file)
    print(f'Third algorithm end, time need {(datetime.now() - start).total_seconds()} sec')

    start = datetime.now()
    print('Start forth algorithm 1000 times')
    for i in range(1000):
        _ = newdop3.convert(input_file)
    print(f'Forth algorithm end, time need {(datetime.now() - start).total_seconds()} sec')