import time
from pygame import mixer


mixer.init()
mixer.music.load("956_STDY_EXT_STND_AN-GS-AUS_HT_CUT.mp3")
mixer.music.play()
while mixer.music.get_busy():  # wait for music to finish playing
    time.sleep(1)