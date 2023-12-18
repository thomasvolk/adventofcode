#!/usr/bin/env python3

import re

SPELLED = ['one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine']


def calculate(lines):
    values = map(
                lambda n: int(f'{n[0]}{n[-1]}'),
                map(
                    lambda c: re.sub('[a-zA-Z]', '', c).strip(),
                    lines
                )
            )
    return sum(values)


def translate_spelled(lines):
    def translate(name):
        return str(SPELLED.index(name) + 1)

    def translate_line(line):
        numbers = [
            (m.start(), translate(s))
            for s in SPELLED for m in re.finditer(s, line)
        ]
        numbers += [
            (n.start(), n.group())
            for n in re.finditer('[1-9]', line)
        ]
        return ''.join([
            n[1] for n in sorted(numbers, key=lambda e: e[0])
        ])
    return map(translate_line, lines)
    
    
if __name__ == '__main__':
    test_data = """
    1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
""".strip().split('\n')
    assert calculate(test_data) == 142
    with open('day01-input.txt') as f:
        result = calculate(f.readlines())
        print(result)
        assert result == 55477

    test_data_2 = """
    two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
    """.strip().split('\n')
    assert calculate(translate_spelled(test_data_2)) == 281
    with open('day01-input.txt') as f:
        result = calculate(translate_spelled(f.readlines()))
        print(result)
        assert result == 54431
