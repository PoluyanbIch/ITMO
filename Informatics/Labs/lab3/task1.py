import re


def main(s: str):
    pattern = r':<{|'
    return len(re.findall(pattern, s))