from numpy import genfromtxt
import numpy as np
import matplotlib.pyplot as plt
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("-i", "--input_file", help="File name from which data will be pulled", type=str, required=True)
parser.add_argument("-o", "--output_file", help="File name to which the data will be added", type=str, required=True)
parser.add_argument("-t","--tubes", nargs='+', type=int, help="Tube number(s) [1-n] to graph", required=False)
args = parser.parse_args()

data=genfromtxt(args.input_file ,delimiter=',')
df = np.transpose(data)

try:
    if args.tubes:
        for n in range(0, len(args.tubes)):
            plt.plot(data[args.tubes[n] - 1],label="Tubule "+str(args.tubes[n]))
    else:
        for n in range(0, len(df[0])):
            plt.plot(data[n], label="Tubule " + str(n + 1))
    plt.xlabel('Time')
    plt.ylabel('Length')
    plt.title('Microtubule Length Over Time')
    plt.legend()
    plt.savefig(args.output_file)
    plt.show()

except IndexError as e:
    print('Tube(s) out of range!')
