"""
Authors:
    Russo Gabriele
    Paletto Andrea
    Tuninetti Andrè
"""
import json
import random
import threading
from time import sleep, time

import schedule
from instapy import InstaPy, smart_run


with open('sample.json') as json_file:
    data = json.load(json_file)
    username = data['username']
    password = data['password']


def instapy():
    insta_username = username
    insta_password = password

    photo_comments = ['Nice photo! @{}',
                      'I really like your profile! @{}',
                      'Really great content! :thumbsup:',
                      'Just incredible :open_mouth:',
                      'What trick did you use to frame the shot @{}?',
                      'Love your posts @{}',
                      'This looks awesome @{}',
                      'Getting inspired by you. Keep up the good work! @{}',
                      ':raised_hands: Yes!',
                      'I can feel your passion for real! @{} :muscle:']

    # HASHTAG CHE SE PRESENTI IMPEDIRANNO LE INTERAZIONI CON LE FOTO
    avoid = ['sex', 'nude', 'naked', 'porn']

    friends = ['list of friends I do not want to interact with']

    # SOSTITUISCI GLI HASHTAG SOTTO CON QUELLI RELATIVI AI TUOI CONTENUTI
    like_tag_list = ['computer', 'programming', 'mma', 'ufc', 'business', 'hacking', 'photography', 'travelgram',
                     'nature']

    # prevent posts that contain some tags from being skipped
    ignore_list = []

    accounts = ["andre_tuni", "ruckfazor",
                "andreapaletto"]  # SOSTITUISCI USER1,2,3,4 CON UTENTI CHE HANNO PAGINE SIMILI ALLA TUA

    # get a session!
    session = InstaPy(username=insta_username,
                      password=insta_password,
                      headless_browser=False,
                      want_check_browser=False)
    session.login()
    # users = session.target_list("C:\\Users\\Palius\\Desktop\\GITHUB\\IUM-TWEB\\Python_project\\user.txt")
    # session.follow_by_list(users, times=1, sleep_delay=600, interact=False)

    with smart_run(session):
        # settings
        session.set_quota_supervisor(enabled=True,
                                     sleep_after=["likes", "comments_d", "follows", "unfollows", "server_calls_h"],
                                     sleepyhead=True, stochastic_flow=True, notify_me=True,
                                     peak_comments_hourly=211,
                                     peak_comments_daily=1821,
                                     peak_follows_hourly=481,
                                     peak_follows_daily=None,
                                     peak_unfollows_hourly=351,
                                     peak_unfollows_daily=402,
                                     peak_server_calls_hourly=None,
                                     peak_server_calls_daily=4700)
        session.set_relationship_bounds(enabled=True,
                                        max_followers=15000)
        session.set_mandatory_language(enabled=True, character_set=['LATIN'])
        session.set_skip_users(skip_private=True,
                               skip_no_profile_pic=True,
                               no_profile_pic_percentage=100)
        session.set_do_follow(enabled=True, percentage=50)
        session.set_comments(photo_comments)
        session.set_do_comment(enabled=True, percentage=80)
        session.set_do_like(True, percentage=70)
        session.set_do_story(enabled=True, percentage=70, simulate=True)
        session.set_mandatory_language(enabled=True, character_set=['ITALIAN', 'ENGLISH'])

        # sleep rules !important
        session.set_action_delays(enabled=True,
                                  like=3,
                                  comment=3,
                                  follow=3,
                                  unfollow=3,
                                  story=3,
                                  randomize=True, random_range_from=70, random_range_to=140)

        session.set_user_interact(amount=1, randomize=True, percentage=60)
        session.set_do_follow(enabled=True, percentage=100)
        session.set_do_like(enabled=True, percentage=80)

        session.set_dont_include(accounts)
        session.set_dont_like(avoid)
        session.set_ignore_if_contains(ignore_list)

        # activity

        rus_pal_tun_followers = session.grab_followers(username="rus_pal_tun", amount="full", live_match=True,
                                                       store_locally=True)
        session.follow_by_list(rus_pal_tun_followers, times=1, sleep_delay=600, interact=False)

        # find all of the active unfollowers
        all_unfollowers, active_unfollowers = session.pick_unfollowers(username="rus_pal_tun", compare_by="earliest",
                                                                       compare_track="first", live_match=True,
                                                                       store_locally=True, print_out=True)
        # let's unfollow them immediately cos Bernard will be angry if heards about those unfollowers! :D
        session.unfollow_users(amount=84, custom_list_enabled=True, custom_list=active_unfollowers,
                               custom_list_param="all", style="RANDOM", unfollow_after=55 * 60 * 60, sleep_delay=600)

        session.interact_by_comments(usernames=["rus_pal_tun"],
                                     posts_amount=10,
                                     comments_per_post=5,
                                     reply=True,
                                     interact=True,
                                     randomize=True,
                                     media="Photo")
        random.shuffle(like_tag_list)
        session.like_by_tags(like_tag_list, amount=2, interact=True)
        session.interact_by_users(["cristiano"], amount=2, randomize=True, media="Photo")
        session.interact_by_users(rus_pal_tun_followers, amount=5, randomize=True, media='Photo')
        session.interact_by_comments(usernames=rus_pal_tun_followers,
                                     posts_amount=10,
                                     comments_per_post=5,
                                     reply=True,
                                     interact=True,
                                     randomize=True,
                                     media="Photo")
        session.end(threaded_session=True)


