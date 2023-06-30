from elasticsearch import Elasticsearch
import numpy as np

es = Elasticsearch(
    ['http://localhost:9200'],
    basic_auth=('elastic', 'Abcabc123!')
)

# generate a random dense vector of dim 1000
vector = np.random.rand(1000)

# index a document with the dense vector
doc = {
    "vector": {
        "type": "dense_vector",
        "dims": 1000,
        "values": vector.tolist()
    }
}

index_name = "my_index"
doc_id = "my_doc_id"

es.index(index=index_name, id=doc_id, body=doc)
