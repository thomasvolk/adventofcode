#!/usr/bin/env python3
import functools
import operator
import re


class Number:
    def __init__(self, number: str, x: int, y: int):
        self.x = x
        self.y = y
        self.value = number

    @property
    def neighbours(self):
        def left_neighbours(x, y):
            return [
                (x - 1, y - 1),
                (x - 1, y),
                (x - 1, y + 1),
                (x, y - 1),
                (x, y + 1)
            ]

        def mid_neighbours(mid_positions):
            return [
                (p[0], y)
                for p in mid_positions
                for y in [p[1] - 1, p[1] + 1]
            ]

        def right_neighbours(x, y):
            return [
                (x, y - 1),
                (x, y + 1),
                (x + 1, y - 1),
                (x + 1, y),
                (x + 1, y + 1)
            ]

        positions = [(x, self.y) for x in range(self.x, self.x + len(self))]
        return (left_neighbours(*positions[0])
                + mid_neighbours(positions[1:-1])
                + right_neighbours(*positions[-1]))

    def __len__(self):
        return len(self.value)

    def __int__(self):
        return int(self.value)

    def __repr__(self):
        return f"Number({self.x},{self.y} - {self.value} {self.neighbours})"


def parse_numbers(lines):
    def get_numbers(y, line):
        for number in re.finditer('([0-9]+)', line):
            value = number.group(0)
            x = number.start()
            yield Number(value, x, y)

    return [n for i, l in enumerate(lines) for n in get_numbers(i, l)]


def parse_symbols(lines):
    def get_symbols(y, line):
        for x, character in enumerate(line):
            if character not in "0123456789.":
                yield x, y

    return [s for i, l in enumerate(lines) for s in get_symbols(i, l.strip())]


def calculate_missing_part(lines):
    symbols = parse_symbols(lines)

    def has_symbol_neighbour(number):
        for neighbour in number.neighbours:
            if neighbour in symbols:
                return True
        return False

    numbers = parse_numbers(lines)
    numbers_with_symbol_neighbours = filter(has_symbol_neighbour, numbers)
    return sum(map(int, numbers_with_symbol_neighbours))


def calculate_gears(lines):
    def get_gear_symbols(y, line):
        for x, character in enumerate(line):
            if character == '*':
                yield x, y

    numbers = parse_numbers(lines)

    def gear_ratio(gear):
        gear_numbers = list(filter(lambda n: gear in n.neighbours, numbers))
        if len(gear_numbers) == 2:
            return functools.reduce(operator.mul, map(int, gear_numbers))
        return 0

    gear_symbols = [g for i, l in enumerate(lines) for g in get_gear_symbols(i, l.strip())]

    return sum(map(gear_ratio, gear_symbols))


if __name__ == '__main__':
    test_data = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
""".strip().split('\n')
    result = calculate_missing_part(test_data)
    assert result == 4361

    with open('day03-input.txt') as f:
        result = calculate_missing_part(f.readlines())
        print(result)
        assert result == 528799

    result = calculate_gears(test_data)
    assert result == 467835

    with open('day03-input.txt') as f:
        result = calculate_gears(f.readlines())
        print(result)
        assert result == 84907174
