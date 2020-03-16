import os
count = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
# Function to find the no of samples for each room
def main():
   path="F:/CE DATA/ALL/"
   for filename in os.listdir(path):
      if filename[6] != '_':
         label = filename[4:7]
      else:
         label = filename[4:6]
      if label == 'L1':
         count[0] = count[0]+1
      elif label == 'L2':
         count[1] = count[1]+1
      elif label == 'L3':
         count[2] = count[2]+1
      elif label == 'L4':
         count[3] = count[3]+1
      elif label == 'L5':
         count[4] = count[4]+1
      elif label == 'L6':
         count[5] = count[5]+1
      elif label == 'L7':
         count[6] = count[6]+1
      elif label == 'L8':
         count[7] = count[7]+1
      elif label == 'L9':
         count[8] = count[8]+1
      elif label == 'L10':
         count[9] = count[9]+1
      elif label == 'L11':
         count[10] = count[10]+1
      elif label == 'L12':
         count[11] = count[11]+1
      elif label == 'L13':
         count[12] = count[12]+1
      elif label == 'L14':
         count[13] = count[13]+1
      elif label == 'L15':
         count[14] = count[14]+1
      elif label == 'L16':
         count[15] = count[15]+1
      elif label == 'L17':
         count[16] = count[16]+1
      elif label == 'L18':
         count[17] = count[17]+1
      elif label == 'L19':
         count[18] = count[18]+1
      elif label == 'L20':
         count[19] = count[19]+1
      elif label == 'L21':
         count[20] = count[20]+1
      elif label == 'L22':
         count[21] = count[21]+1
      elif label == 'L23':
         count[22] = count[22]+1
   print(count)
      
# Driver Code
if __name__ == '__main__':
   # Calling main() function
   main()
