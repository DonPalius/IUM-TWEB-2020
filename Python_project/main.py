from reddit import reddit_download
from Instascript import instapy
from Upload import Upload_photo






if __name__ == "__main__":
    while True:
        risposta = input("Which operation do you want to perform? [r:reddit, u:Upload, i:instapy, q:quit] ")
        if risposta.lower() == "r":
            reddit_download()
        elif risposta.lower() == "u":
            Upload_photo()
        elif risposta.lower() == "i":
            instapy()
        elif risposta.lower() == "q":
            break
        else:
            print("Command not found")

    print("Exiting ...")


