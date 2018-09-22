from google.cloud import vision_v1
from google.cloud.vision_v1 import types

def process_array():
	return 0

def handwriting_solve():

	with io.open('../python/test.png', 'rb') as image_file:
		content = image_file.read()

	image = types.Image(content=imageData.read())
	client = vision_v1.ImageAnnotatorClient()
	response = client.text_detection(image=image)

	raw_data = response.text_annotations[0].description
	return raw_data


def detect_input(raw):
	res = []

	for image in raw:
		process_array(image)
		res.append(handwriting_solve())

	return res

# print(handwriting_solve())