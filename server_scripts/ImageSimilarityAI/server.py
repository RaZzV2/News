from flask import Flask, jsonify
import base64
from io import BytesIO
from flask import request
from transformers import ViTImageProcessor, ViTForImageClassification
from PIL import Image
import requests
from scipy.spatial import distance
metric = 'cosine'

processor = ViTImageProcessor.from_pretrained('google/vit-base-patch16-224')
model = ViTForImageClassification.from_pretrained('google/vit-base-patch16-224')
app = Flask("server")

@app.route("/identify", methods=['POST'])
def identify():
    im_b64_1 = request.json["im_b64_1"]
    im_bytes_1 = base64.b64decode(im_b64_1)
    im_file_1 = BytesIO(im_bytes_1)
    image_1 = Image.open(im_file_1)

    inputs_1 = processor(images=image_1, return_tensors="pt")
    outputs_1 = model(**inputs_1)

    return jsonify(outputs_1.logits.tolist()[0])