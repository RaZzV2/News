FROM huggingface/transformers-pytorch-gpu
RUN pip install flask langchain sentence_transformers
COPY server.py /server.py
ENV FLASK_APP=server.py
ENV FLASK_RUN_PORT=5000
CMD flask run --host=0.0.0.0
EXPOSE 5000
