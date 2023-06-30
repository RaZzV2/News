from langchain.embeddings import HuggingFaceEmbeddings
import requests
from PIL import Image
from transformers import pipeline
import base64
from io import BytesIO
from flask import Flask, jsonify, request

embeddings = HuggingFaceEmbeddings(model_name="all-mpnet-base-v2")


app = Flask(__name__)

@app.route("/text_identify", methods=['POST'])
def text_identify():
    text = request.json["text"]
    featuresVector = embeddings.embed_query(text)
    return featuresVector
