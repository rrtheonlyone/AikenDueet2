from flask import Flask, jsonify, request
from flask_restful import Resource, Api, reqparse

from python.primesum import primeSum
from python.imagegps import ImageEXIF
from python.sortinggame import fifteenpuzzle
from python.linearregression import linearRegression
from python.skilltree import skillTree
from python.tetris import tetris_solver
from python.handwriting import handwriting_solve

from machinelearning.res import pre_process

import random

app = Flask(__name__)
api = Api(app)

class PrimeSum(Resource):
    def get(self):
        return "This is the prime sum problem answer"

    def post(self):
        json_data = request.get_json(force=True)
        string_value = json_data['input']

        n = int(string_value)
        return primeSum(n)


class PhotoGps(Resource):
	def get(self):
		return "This is the photo gps problem answer"

	def post(self):
		json_data = request.get_json(force=True)
		return ImageEXIF(json_data)

class SortingGame(Resource):
    def get(self):
        return "This is the sorting game problem answer"

    def post(self):
        json_data = request.get_json(force=True)
        game_data = json_data['puzzle']
        puzzle = []

        gridSize = len(game_data)

        for i in game_data:
            for j in i:
                square = j
                if square == 0:
                    square = gridSize*gridSize
                square -= 1
                puzzle.append(square)

        puzzle = tuple(puzzle)


        res = fifteenpuzzle(gridSize, puzzle)
        answer_json = {"result": res}
        return jsonify(answer_json)

class Handwriting(Resource):
    def get(self):
        return "This is the answer to the handwriting problem"

    def post(self):
        json_data = request.get_json(force=True)
        res = pre_process(json_data["question"])
        answer_json = {"answer": res}
        return jsonify(answer_json)

class LinearRegression(Resource):
    def get(self):
        return "This is the answer to the linear regression problem"

    def post(self):
        json_data = request.get_json(force=True)
        res = linearRegression(json_data["input"], json_data["output"], json_data["question"])
        answer_json = {"answer" : res}
        return jsonify(answer_json)

class SkillTree(Resource):
    def get(self):
        return "This is the answer to the skill tree problem"
    
    def post(self):
        json_data = request.get_json(force=True)
        return jsonify(skillTree(json_data))

class Tetris(Resource):
    def get(self):
        return "This is the answer to the tetris problem"

    def post(self):
        json_data = request.get_json(force=True)
        return tetris_solver(json_data["tetrominoSequence"])

api.add_resource(PrimeSum, '/prime-sum')
api.add_resource(PhotoGps, '/imagesGPS')
api.add_resource(Handwriting, '/machine-learning/question-2')
api.add_resource(LinearRegression, '/machine-learning/question-1')
api.add_resource(SkillTree, '/skill-tree')
api.add_resource(Tetris, '/tetris')

if __name__ == '__main__':
    app.run(debug=True)





