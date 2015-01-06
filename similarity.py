from nltk.corpus import wordnet as wn
import sys
for i in range(0,len(sys.argv)/2):
    w1=wn.synsets(sys.argv[i*2+1])[0]
    w2=wn.synsets(sys.argv[i*2+2])[0]
    print w1.path_similarity(w2)
