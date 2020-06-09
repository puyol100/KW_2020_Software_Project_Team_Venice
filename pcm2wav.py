import wave
from pydub import AudioSegment

def pcm2wav( pcm_data, wav_file, channels=1, bit_depth=16, sampling_rate=44100 ):

    # Check if the options are valid.
	if bit_depth % 8 !=0:
		raise ValueError("bit_depth "+str(bit_depth)+" must be a multiple of 8.")       
    # Read the .pcm file as a binary file and store the data to pcm_data
	obj2write = wave.open(wav_file,'wb')
	obj2write.setnchannels(channels)
	obj2write.setsampwidth(bit_depth//8)
	obj2write.setframerate(sampling_rate)
	obj2write.writeframes(pcm_data)
	obj2write.close()
	
	
	