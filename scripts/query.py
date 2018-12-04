from numpy import genfromtxt
import numpy as np
import matplotlib.pyplot as plt
import argparse
import csv

parser = argparse.ArgumentParser()
parser.add_argument("-i", "--input_file", help="File name from which data will be pulled", type=str, required=True)
parser.add_argument("-o", "--output_file", help="File name to which the data will be added", type=str, required=True)
parser.add_argument("-f", "--frames", nargs='+', type=int, help="Frame number(s) [1-m] to output", required=True)
parser.add_argument("-t", "--tubes", nargs='+', type=int, help="Tube number(s) [1-n] to output", required=False)
args = parser.parse_args()

data = genfromtxt(args.input_file, delimiter=',')
df = np.transpose(data)

framedata = []

try:
    if args.tubes:
        for n in range(0, len(args.tubes)):
            framedata.append([])
            for f in args.frames:
                framedata[-1].append(data[args.tubes[n] - 1][f])
    else:
        for n in range(0, len(df[0])):
            framedata.append([])
            for f in args.frames:
                framedata[-1].append(data[n][f])

    with open(args.output_file, "w") as f:
        writer = csv.writer(f)
        writer.writerows(framedata)

except IndexError as e:
    print('Tube(s) out of range!')
