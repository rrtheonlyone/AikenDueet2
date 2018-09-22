from flask import Flask, jsonify, request
from flask_restful import Resource, Api, reqparse

from primesum import primeSum
from imagegps import ImageEXIF

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
		return json_data





api.add_resource(PrimeSum, '/primesum')
api.add_resource(PhotoGps, '/imagesGPS')

if __name__ == '__main__':
    app.run(debug=True)