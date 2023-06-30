import requests
import json
import base64
import hashlib
import numpy as np
import ast
from firebase_admin import credentials, messaging, initialize_app
from firebase_admin import db as firebase_db
from datetime import datetime

from elasticsearch import Elasticsearch

es = Elasticsearch(
    ['http://localhost:9200'],
    basic_auth=('elastic', 'Abcabc123!')
)

mapping = { "mappings": {
    "properties": {
      "author": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "category": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "content": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "country": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "description": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "featureVector": {
          "type": "dense_vector",
          "dims": 1000,
          "index": True,
          "similarity": "cosine"
      },
       "textFeatureVector": {
          "type": "dense_vector",
          "dims": 768,
          "index": True,
          "similarity": "cosine"
      },
      "hash": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "source": {
        "properties": {
          "id": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "name": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "title": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "url": {
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

cred = credentials.Certificate('google-services.json')
config = {
    'databaseURL': 'https://news-ca9ff-default-rtdb.europe-west1.firebasedatabase.app/'
}
initialize_app(cred, config)

ref = firebase_db.reference()

def save_notification(uid, title, body):
    current_datetime = datetime.now()
    formatted_datetime = current_datetime.strftime("%Y-%m-%d %H:%M:%S")
    notifications_ref = ref.child('notifications').child(uid).child(formatted_datetime)

    notifications_ref.set({
        'title': title,
        'body': body,
    })

users_ref = ref.child('users')
users_dict = users_ref.get()
tokens = []
for user_dict in users_dict.values():
    if 'token' in user_dict:
        tokens.append(user_dict['token'])
    else:
        pass


if not es.indices.exists(index='news_articles', body=mapping):
    es.indices.create(index='news_articles', body=mapping)

url = "http://localhost:5000/identify"
urlText = "http://localhost:7000/text_identify"

countries = ["us", "ro", "uk", "fr", "de", "ca", "it", "jp", "in"]
categories = ["business", "entertainment" "general", "health", "science", "sports", "technology"]

api_endpoint = "https://newsapi.org/v2/top-headlines"

for country in countries:
    for category in categories:
        api_params = {
            "country": country,
            "category": category,
            "apiKey": "f6180008bfc8495cb8bb10b0db72cbb0"
        }
        response = requests.get(api_endpoint, params=api_params)
        articles = response.json()["articles"]
        for article in articles:
            article["hash"] = hashlib.md5(json.dumps(article, sort_keys = True).encode("utf-8")).hexdigest()
            article["country"] = country
            payload = {"text": article["title"]}
            response = requests.post(urlText, json=payload)
            float_list = ast.literal_eval(response.content.decode('utf-8'))
            article["textFeatureVector"] = np.array(float_list).tolist()
            try:
                  response = requests.get(article["urlToImage"])
                  base64_image = base64.b64encode(response.content)
                  payload = {"im_b64_1": base64_image.decode()}
                  response = requests.post(url, json=payload)
                  float_list = ast.literal_eval(response.content.decode('utf-8'))
                  article["featureVector"] = np.array(float_list).tolist()
            except:
                   pass
            article["category"] = category
            try:
              es.index(index="news_articles", document=article, pipeline="news_pipe")
            except:
                pass
message = messaging.MulticastMessage(
notification=messaging.Notification(
title='New articles available!',
body='Check out the latest news on our app!'),
tokens=tokens)
response = messaging.send_multicast(message)
print('Successfully sent message to', response.success_count, 'devices.')

for uid in tokens:
    save_notification(uid, 'New articles available!', 'Check out the latest news on our app!')