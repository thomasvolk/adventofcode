#!/usr/bin/env python3

import re
import functools
import operator


def parse(lines):
    def parse_line(line):
        m = re.match('Game ([0-9]+): (.*)', line)
        game_id = m.group(1)
        turns = [
            {
                c: int(n)
                for n, c in [
                    d.strip().split(' ')
                    for d in t.split(',')
                ]
            }
            for t in m.group(2).split(';')
        ]
        return {'id': int(game_id), 'turns': turns}
    return map(parse_line, lines)


def calculate_all_valid_games(lines, **cubes):
    def count_fits(game):
        for t in game['turns']:
            for color, count in cubes.items():
                n = t.get(color)
                if n and n > count:
                    return False
        return True

    games = parse(lines)
    games_survived = [
        game['id']
        for game in games
        if count_fits(game)
    ]
    return sum(games_survived)


def calculate_min_setup_power(lines):
    def none_save_max(n, current):
        if not current:
            return n
        if not n:
            return current
        return max(current, n)

    def power(game):
        cube_counts = []
        for color in ['red', 'green', 'blue']:
            current = None
            for t in game['turns']:
                current = none_save_max(t.get(color), current)
            cube_counts.append(current)
        return functools.reduce(operator.mul, filter(operator.truth, cube_counts), 1)

    games = parse(lines)
    return sum(map(power, games))


if __name__ == '__main__':
    test_data = """
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
""".strip().split('\n')
    result = calculate_all_valid_games(test_data, red=12, green=13, blue=14)
    assert result == 8
    result = calculate_min_setup_power(test_data)
    assert result == 2286
    with open('day02-input.txt') as f:
        result = calculate_all_valid_games(f.readlines(),  red=12, green=13, blue=14)
        print(result)
        assert result == 1734

    with open('day02-input.txt') as f:
        result = calculate_min_setup_power(f.readlines())
        print(result)
        assert result == 70387
