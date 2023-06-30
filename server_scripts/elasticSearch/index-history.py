from elasticsearch import Elasticsearch

es = Elasticsearch(
    ['http://localhost:9200'],
    basic_auth=('elastic', 'Abcabc123!')
)

mapping = { "mappings": {
  "properties": {
    "date": {
      "type": "date"
    },
    "query": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword",
          "ignore_above": 256
        }
      }
    },
    "uid": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword",
          "ignore_above": 256
        }
      }
    }
  }
}
}

es.indices.create(index='input_history', body= mapping)
