import json
import yaml


def parse(name_input_file: str, name_output_file='output1.json'):
    with open(name_input_file, 'r', encoding='utf-8') as inp, open(name_output_file, 'w', encoding='utf-8') as out:
        return json.dump(yaml.safe_load(inp), out, ensure_ascii=False)


if __name__ == "__main__":
    parse('input.yaml')