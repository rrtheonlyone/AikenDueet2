from flask import Flask, jsonify, request
from flask_restful import Resource, Api, reqparse

from python.primesum import primeSum
from python.imagegps import ImageEXIF
from python.sortinggame import fifteenpuzzle

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



api.add_resource(PrimeSum, '/prime-sum')
api.add_resource(PhotoGps, '/imagesGPS')
api.add_resource(SortingGame, '/sorting-game')

if __name__ == '__main__':
    app.run(debug=True)