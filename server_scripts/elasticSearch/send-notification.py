from firebase_admin import credentials, messaging, initialize_app
from firebase_admin import db as firebase_db

# Initialize the app with the appropriate credentials
cred = credentials.Certificate('google-services.json')
config = {
    'databaseURL': 'https://news-ca9ff-default-rtdb.europe-west1.firebasedatabase.app/'
}
initialize_app(cred, config)

# Get all the user tokens from your Realtime Database
ref = firebase_db.reference()
users_ref = ref.child('users')
users_dict = users_ref.get()
tokens = []
for user_dict in users_dict.values():
    if 'token' in user_dict:
        tokens.append(user_dict['token'])
    else:
        # Add code to handle users without a token
        pass

# Create a message to be sent
message = messaging.MulticastMessage(
    notification=messaging.Notification(
        title='New articles available!',
        body='Check out the latest news on our app!'
    ),
    tokens=tokens
)

# Send the message
response = messaging.send_multicast(message)

print('Successfully sent message to', response.success_count, 'devices.')
