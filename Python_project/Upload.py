"""
Authors:
    Russo Gabriele
    Paletto Andrea
    Tuninetti Andrè
"""
import json
import random
from time import sleep
from selenium import webdriver
import os
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC


# questo script serve per effettuare l'upload di una foto su instagram con la versione desktop
# apriamo un altra scheda di firefox con useragent android per poter sbloccare l'utilizzo della versione per telefono di instagram
# utilizziamo un script scritto in autoit per automatizzare la finestra di caricamento file
#
with open('sample.json') as json_file:
    data = json.load(json_file)
    username =data['username']
    password = data['password']
    script_path = data['path']
    memes_path = data['path_memes']
    memes_send_path = data['path_memes_sent']

def Upload_photo():

    frasi = (
        "GoodMorning vietnam", "bella raga giorno di paga ", "beautiful day ", "I wanna be famous", "#follow4follow",
        "#follow for new beautiful photo")
    profile = webdriver.FirefoxProfile()
    profile.set_preference("general.useragent.override",
                           "Mozilla/5.0 (Linux; Android 9.0; MI 8 SE) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.119 Mobile Safari/537.36")

    browser = webdriver.Firefox(profile)
    wait = WebDriverWait(browser, 10)
    browser.get('https://www.instagram.com/')
    wait.until(
        EC.element_to_be_clickable(
            (By.XPATH, '/html/body/div[1]/section/main/article/div/div/div/div[2]/button'))).click()

    browser.find_element_by_name("username").send_keys(username)
    browser.find_element_by_name("password").send_keys(password)
    wait.until(EC.element_to_be_clickable(
        (By.XPATH, '/html/body/div[1]/section/main/article/div/div/div/form/div[1]/div[6]/button/div'))).click()

    wait.until(EC.element_to_be_clickable((By.XPATH, '/html/body/div[1]/section/main/div/div/div/button'))).click()

    wait.until(EC.element_to_be_clickable((By.XPATH, "/html/body/div[1]/section/nav[2]/div/div/div["
                                                     "2]/div/div/div[3]"))).click()
    # random choice sceglie un file random nella cartella da postare

    img_to_upload = random.choice(os.listdir(memes_path))

    path1 = script_path
    path2 = memes_path + img_to_upload
    os.system(path1 + " " + path2)
    wait.until(EC.element_to_be_clickable(
        (By.XPATH, '/html/body/div[1]/section/div[1]/header/div/div[2]/button'))).click()

    browser.find_element_by_xpath("/html/body/div[1]/section/div[2]/section[1]/div[1]/textarea").send_keys(
        random.choice(frasi))

    wait.until(EC.element_to_be_clickable(
        (By.XPATH, '/html/body/div[1]/section/div[1]/header/div/div[2]/button'))).click()
    os.rename(memes_path+ img_to_upload,
              memes_send_path + img_to_upload)
    sleep(7)
    browser.close()
