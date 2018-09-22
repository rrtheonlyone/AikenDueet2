#!/usr/bin/python

import numpy as np
import time
from collections import defaultdict
from functools import cmp_to_key

class Tetromino():

    TYPES = [' ', 'I', 'O', 'T', 'S', 'Z', 'J', 'L']
    TYPES_D = {
        ' ': 0,
        'I': 1,
        'O': 2,
        'T': 3,
        'S': 4,
        'Z': 5,
        'J': 6,
        'L': 7
    }

    def __init__(self, state):
        self.state = np.array(state, dtype=np.uint8, copy=True)

    @staticmethod
    def ITetromino():
        return Tetromino(
            [
                [1],
                [1],
                [1],
                [1]
            ]
        )

    @staticmethod
    def OTetromino():
        return Tetromino(
            [
                [2, 2],
                [2, 2]
            ]
        )

    @staticmethod
    def TTetromino():
        return Tetromino(
            [
                [3, 3, 3],
                [0, 3, 0]
            ]
        )

    @staticmethod
    def STetromino():
        return Tetromino(
            [
                [0, 4, 4],
                [4, 4, 0]
            ]
        )

    @staticmethod
    def ZTetromino():
        return Tetromino(
            [
                [5, 5, 0],
                [0, 5, 5]
            ]
        )

    @staticmethod
    def JTetromino():
        return Tetromino(
            [
                [0, 6],
                [0, 6],
                [6, 6]
            ]
        )

    @staticmethod
    def LTetromino():
        return Tetromino(
            [
                [7, 0],
                [7, 0],
                [7, 7]
            ]
        )

    @staticmethod
    def create(letter):
        if letter.upper() not in Tetromino.TYPES[1:]:
            raise ValueError('No Tetromino of type {}'.format(letter))
        return getattr(Tetromino, '{}Tetromino'.format(letter.upper()))()

    def __str__(self):
        return str(np.vectorize(Tetromino.TYPES.__getitem__)(self.state))

    def __getitem__(self, key):
        return self.state[key]

    def copy(self):
        return Tetromino(self.state)

    def flat(self):
        return self.state.flat

    def width(self):
        return self.state.shape[1]

    def height(self):
        return self.state.shape[0]

    def rotate(self, change):
        while change < 0:
            change += 4
        change = (change % 4)
        if change == 0:
            return None
        elif change == 1:
            self.rotate_right()
        elif change == 2:
            self.flip()
        elif change == 3:
            self.rotate_left()

    def rotate_right(self):
        self.state = np.rot90(self.state, 3)
        return self

    def rotate_left(self):
        self.state = np.rot90(self.state, 1)
        return self

    def flip(self):
        self.state = np.rot90(self.state, 2)
        return self


class Optimizer():

    @staticmethod
    def get_optimal_drop(field, tetromino):
        piece = tetromino.state[0][0]
        if piece == 0:
            piece = tetromino.state[0][1]
        if piece == 2:
            rotations = [tetromino]
        elif piece == 1 or piece == 5 or piece == 4:
            rotations = [tetromino, tetromino.copy().rotate_right()]
        else:
            rotations = [
                tetromino,
                tetromino.copy().rotate_right(),
                tetromino.copy().flip(),
                tetromino.copy().rotate_left(),
            ]
        drops = []
        for rotation, tetromino_ in enumerate(rotations):
            for column in range(Field.WIDTH):
                try:
                    f = field.copy()
                    row = f.drop(tetromino_, column)
                    drops.append({
                        'field': f,
                        'field_gaps': f.count_gaps(),
                        'field_height': f.heights(),
                        'tetromino_rotation': rotation,
                        'tetromino_column': column,
                        'tetromino_row': row
                    })
                except:
                    continue

        # First, we pick out all the drops that will produce the least
        # amount of gaps.
        lowest_gaps = min([drop['field_gaps'] for drop in drops])
        drops = list(filter(
            lambda drop: drop['field_gaps'] == lowest_gaps, drops))
        # Next we sort for the ones with the lowest field height.
        lowest_height = min([sum(drop['field_height']) for drop in drops])
        drops = list(filter(
            lambda drop: sum(drop['field_height']) == lowest_height, drops))
        # Finally, we sort for the ones that drop the tetromino in the lowest
        # row. Since rows increase from top to bottom, we use max() instead.
        lowest_row = max([drop['tetromino_row'] for drop in drops])
        drops = list(filter(
            lambda drop: drop['tetromino_row'] == lowest_row, drops))
        assert len(drops) > 0
        return drops[0]


class Field():

    WIDTH = 10
    HEIGHT = 22

    def __init__(self, state=None):
        if state is not None:
            self.state = np.array(state, dtype=np.uint8, copy=True)
        else:
            self.state = np.full((Field.HEIGHT, Field.WIDTH), 0, dtype=np.uint8)

    def __str__(self):
        BAR = '   |' + ' '.join(map(str, range(Field.WIDTH))) + '|\n'
        field = np.vectorize(Tetromino.TYPES.__getitem__)(self.state)
        FIELD = '\n'.join(['{:2d} |'.format(i) +
            ' '.join(row) + '|' for i, row in enumerate(field)])
        return BAR + FIELD + '\n' + BAR

    def _test_tetromino(self, tetromino, row, column):
        """
        Tests to see if a tetromino can be placed at the specified row and
        column. It performs the test with the bottom left corner of the
        tetromino at the specified row and column.
        """
        r, c = row - tetromino.height(), column + tetromino.width()
        if column < 0 or c > Field.WIDTH:
            return False
        if r < 0 or row >= Field.HEIGHT:
            return False
        for s, t in zip(self.state[r + 1:row + 1, column:c].flat, tetromino.flat()):
            if s and t:
                return False
        return True

    def _place_tetromino(self, tetromino, row, column):
        """
        Place a tetromino at the specified row and column.
        The bottom left corner of the tetromino will be placed at the specified
        row and column. This function does not perform checks and will overwrite
        filled spaces in the field.
        """
        r, c = row - tetromino.height(), column + tetromino.width()
        if column < 0 or c > Field.WIDTH:
            return False
        if r < 0 or row >= Field.HEIGHT:
            return False
        for tr, sr in enumerate(range(r + 1, row + 1)):
            for tc, sc, in enumerate(range(column, c)):
                if tetromino[tr][tc]:
                    self.state[sr][sc] = tetromino[tr][tc]

    def _get_tetromino_drop_row(self, tetromino, column):
        """
        Given a tetromino and a column, return the row that the tetromino
        would end up in if it were dropped in that column.
        Assumes the leftmost column of the tetromino will be aligned with the
        specified column.
        """
        if column < 0 or column + tetromino.width() > Field.WIDTH:
            return -1
        last_fit = -1
        for row in range(tetromino.height(), Field.HEIGHT):
            if self._test_tetromino(tetromino, row, column):
                last_fit = row
            else:
                return last_fit
        return last_fit

    def _line_clear(self):
        """
        Checks and removes all filled lines.
        """
        non_filled = np.array([not row.all() and row.any() for row in self.state])
        if non_filled.any():
            tmp = self.state[non_filled]
            self.state.fill(0)
            self.state[Field.HEIGHT - tmp.shape[0]:] = tmp

    def copy(self):
        """
        Returns a shallow copy of the field.
        """
        return Field(self.state)

    def drop(self, tetromino, column):
        """
        Drops a tetromino in the specified column.
        The leftmost column of the tetromino will be aligned with the specified
        column.
        Returns the row it was dropped in for computations.
        """
        assert column + tetromino.width() <= Field.WIDTH
        row = self._get_tetromino_drop_row(tetromino, column)
        assert row != -1
        self._place_tetromino(tetromino, row, column)
        self._line_clear()
        return row

    def count_gaps(self):
        """
        Check each column one by one to make sure there are no gaps in the
        column.
        """
        gaps = 0
        for col in self.state.T:
            begin = False
            for space in col:
                if space != Tetromino.TYPES_D[' ']:
                    begin = True
                elif begin:
                    gaps += 1
            begin = False
        return gaps

    def heights(self):
        h = Field.HEIGHT
        myList = []
        for i in self.state.T:
            k = np.nonzero(i)
            if len(k[0]) != 0:
                myList.append(h - np.min(k))
            else:
                myList.append(0)
        out = np.asarray(myList)
        return out

    def max_height(self):
        """
        Returns the height on the field of the highest placed tetromino on the
        field.
        """
        return np.max(self.heights())

    def avg_height(self):
        return np.mean(self.heights())

    def dev_height(self):
        return np.std(self.heights())

    def rating(self, weights):
        factors = np.array([
            self.count_gaps(),
            self.max_height(),
            self.avg_height(),
            self.dev_height()
        ])
        return (factors * weights).sum()


if __name__ == '__main__':
    field = Field()
    seq = input()
    next_tetromino = None
    out = []
    starttime = time.clock()
    try:
        for i in seq:
            currtime = time.clock()
            
            if (currtime - starttime) > 19.9:
                break
            current_tetromino = Tetromino.create(i)
            opt = Optimizer.get_optimal_drop(field, current_tetromino)
            rotation = opt['tetromino_rotation']
            column = opt['tetromino_column']
    
            out.append(rotation*10 + column)

            current_tetromino.rotate(rotation)
            field.drop(current_tetromino, column)
            print (field)

    except:
        pass
    print (out + [0] * (len(seq) - len(out)))
