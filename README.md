# Musinity

Deep learning generates music.  
  
Musinity is an app that generates music based on an artist or genre. I used a time distrbuted dense autoencoder made in Tensorflow/Keras to generate music.  
  
Currently, the app only generates ragtime music based on 100+ songs, some of which are listed in the app. Each square in the home section represents a genre/artist that the app can potentially generate music for. When a square is tapped on, a "Generate Music" button appears and some songs the model is based on. Future improvements would include models that generate other types of music.  
  
The model (MusinityNet) takes 120 numbers sampled from a Gaussian distribution and decodes them into 16 measures. I used a queue to keep track of the current measure playing. If users want to skip, the app either goes back to the beginning or end of the 16 measures.  

The PlayerFragment class is in charge of playing the music. It has a MusicGenerator (the model) in charge of generating measures and a NotePlayer class in charge of playing notes. I used a SoundPool to play the sounds, but created the NotePlayer class to reduce code density in the fragment class.  

Here's a short gif for app demonstration:  
