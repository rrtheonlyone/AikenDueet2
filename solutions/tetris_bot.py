#!/usr/bin/python

from field import Field
from optimizer import Optimizer
from tetromino import Tetromino


def tetris_solver(seq):
    field = Field()
    next_tetromino = None
    out = []
    for i in seq:
        current_tetromino = Tetromino.create(i)
        opt = Optimizer.get_optimal_drop(field, current_tetromino)
        rotation = opt['tetromino_rotation']
        column = opt['tetromino_column']

        out.append(rotation*10 + column)

        current_tetromino.rotate(rotation)
        field.drop(current_tetromino, column)

    return out


