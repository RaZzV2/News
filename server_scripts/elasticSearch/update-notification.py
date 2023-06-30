import requests
import json
import base64
import hashlib
import numpy as np
import ast
from firebase_admin import credentials, messaging, initialize_app
from firebase_admin import db as firebase_db
from datetime import datetime

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


message = messaging.MulticastMessage(
notification=messaging.Notification(
title='New features available!',
body='Check out the latest updates on our app!'),
tokens=tokens)
response = messaging.send_multicast(message)
print('Successfully sent message to', response.success_count, 'devices.')

for uid in tokens:
    save_notification(uid, 'New features available!', 'Check out the latest updates on our app!')