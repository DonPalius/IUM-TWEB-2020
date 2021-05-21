"""
Authors:
    Russo Gabriele
    Paletto Andrea
    Tuninetti Andrè
"""
import json
import os
import praw
import requests  # to get image from the web
import shutil  # to save it locally
import random

with open('sample.json') as json_file:
    data = json.load(json_file)
    dynpath =data['path_memes']
    password = data['reddit_password']
    username = data ['reddit_username']
    clientId = data ['client_id']
    clientSecret = data ['client_secret']
def reddit_download(path=dynpath):

    reddit = praw.Reddit(client_id=clientId,
                         client_secret=clientSecret,
                         username=username,
                         password= password,
                         user_agent='RusPalTun')
    if not os.path.isdir(path) :
        risposta = input("folder not found. Do you want to create it ? [y/N] ")
        if risposta == "y" :
            os.mkdir(path)
        else :
            print("exiting...")
            return

    a = ['itookapicture', 'pics', 'art', 'eyebleach', 'aww', 'RateMyPicture', 'DigitalArt','ProgrammerHumor']
    r = random.randint(0, len(a) - 1)
    print(a[r])
    subreddit = reddit.subreddit(a[r])
    hot_python = subreddit.hot(limit=10)
    for submission in hot_python:
        if not submission.over_18:
            r = requests.get(submission.url, stream=True)
            filename = submission.url.split("/")[-1]
            if r.status_code == 200:
            # Set decode_content value to True, otherwise the downloaded image file's size will be zero.
                r.raw.decode_content = True

            # Open a local file with wb ( write binary ) permission.
                with open(path + filename, "wb") as f:
                    shutil.copyfileobj(r.raw, f)

                if filename.endswith(('.jpg', 'jpeg', 'png')):
                    print('Image sucessfully Downloaded: ', filename)
                else:
                    os.remove(path + filename)
                    print("foto eliminata")

        else:
            print('Image Couldn\'t be retreived')
