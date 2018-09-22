import io
import os

# Imports the Google Cloud client library
from google.cloud import vision
from google.cloud.vision import types


def process_array(arr):

	return 0

def handwriting_solve():
	
	os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "./python/sap-datahub-trial-ea8b51039449.json"
	
	# Instantiates a client
	client = vision.ImageAnnotatorClient()

	with io.open('./python/123.png', 'rb') as image_file:
		content = image_file.read()

	image = types.Image(content=content)

	# Performs label detection on the image file
	response = client.text_detection(image=image)
	print(response)
	return []


def detect_input(raw):
	res = []
	n = []

	for image in raw:
		n.append(object)
		process_array(image)
	
	res.append(handwriting_solve())

	return res

# print(handwriting_solve())