with open("/home/daniel/PycharmProjects/ModelBuilder/ds.txt", "r") as f:
    lines = f.readlines();
    print(lines[0])
with open("/home/daniel/PycharmProjects/ModelBuilder/ds.txt", "w") as f:
    newLines = []
    for line in lines:
        line = line.replace("\n", '')
        #print(line.split("\t"))
        newLine_broken = line.split("\t")
        newLine_broken.reverse()
        print(newLine_broken)
        newLines.append(("\t".join(newLine_broken))+"\n")
    f.writelines(newLines)