import random
numberList = [111,222,333,444,555]
reslist = []
print(numberList)
for x in numberList:
    reslist.append(random.choice(numberList))
print(reslist)