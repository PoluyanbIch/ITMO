import bson
import dop3

if __name__ == '__main__':
    with open('output.bson', 'wb') as out, open('input.yaml', 'r', encoding='utf-8') as inp:
        bson_data = bson.dumps(dop3.yaml2dict(inp.readlines()))
        out.write(bson_data)