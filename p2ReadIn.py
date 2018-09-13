##Author: Haonan Tian##
import re
import os
import codecs
import re
import math
import glob
import bs4
from bs4 import BeautifulSoup
import porterStemming
from porterStemming import PorterStemmer
import math

############################Stage I##################################
##The following functions are help functions and function of parseFirstRound
def loadFile():  ##Read in all sgm files in a diectory and send them to first round parsing
    counter = 0
    data_stack1 = []
    for fileName in glob.glob('*.sgm'):
        parseFirstRound(fileName,data_stack1)
        print('Finish loading file ', counter)
        counter += 1
    return data_stack1

def parseFirstRound(fileName,storeList):  ##parse to find the articles with only single topics
    with codecs.open(fileName, "r",encoding='utf-8', errors='ignore') as fin:
        read_data = fin.read()
    temp_stack =[]
    soup = BeautifulSoup(read_data,"html.parser")
    temp_stack = soup.find_all("reuters")
    for items in temp_stack:
        if (len(items.topics) == 1):
            storeList.append(items)

##The following functions are help functions and function of parseSecondRound 
def addInList(item,topicList):  ##Create and add items to a topicList. Check if a topic is already in the list and do corresponding operations 
    if topicList != []:
        for key in topicList:
            if (item == key):
                topicList[key] += 1
                return 
        topicList[item] = 1
        return
    else:
        topicList[item] = 1
        return

def refineTopicList(topicList): ##Return a frequencyList with the most frequent 20 topics   
    tempList = topicList
    print('length of the topicList here is: ' + str(len(topicList)))
    frequency = {}
    while len(frequency) < 20:
        maxVal = 0
        maxKey = ''
        for key in tempList:
            if tempList[key] > maxVal:
                maxVal = tempList[key]
                maxKey = key
        frequency[maxKey] = maxVal       
        print(maxVal)
        tempList.pop(maxKey,None)
    print('length of the frequencyList is: ' + str(len(frequency)))
    return frequency

def checkInList(item,frequencyList): ##Check is a topic is included in the frequent topic list 
    for key in frequencyList:
        if (key == item):
            return True
    return False
        

def parseSecondRound(inputList,topicList):  ## Find the article whose topic is top 20 most frequent topics Q: is the top 20 fron the topics we get?
    outputList = []
    for items in inputList:
        newSoup = BeautifulSoup(str(items),"html.parser") 
        if (newSoup == None) or not checkInList(newSoup.topics.d.string,topicList) or (newSoup.body == None):
            inputList.remove(items)
        else:
            tempStore = []
            tempStore.append(newSoup.reuters['newid'])
            tempStore.append(newSoup.topics.d.string)
            tempStore.append(newSoup.body.string)
            outputList.append(tempStore)
    print("Lenth is !!!: " + str(len(outputList)))
    return outputList

def printForResults1(inputList,fileName):  ##Print out function for sorted list for first stage 
    fout = open(fileName,'w')
    for items in inputList:
        for entries in items:
            fout.write(str(entries) + '\t')
        fout.write('\n\n')
    fout.close()

#################################Stage II##########################################       
##The following functions are designed to perform text cleanning task for further clustring analysis
def stage2Processing1(inputList): ##Eliminate non-AsciII characters from texts
    outputList = inputList
    for items in outputList:
        result = ''
        outStr = ''
        outStr = (c for c in items[2] if (0 < ord(c) < 127))
        result = ''.join(outStr)
        items[2] = result
        items[2] = items[2].lower()   ##Convert to lower case
        items[2] = re.sub('[^0-9a-zA-Z]+', ' ', items[2])  ##Replace any non alphanumeric characters with space
        items[2] = items[2].split(' ') ##Split to tokens with space as its deliminater
    return outputList

def checkIfOnlyDigit(token): ##Check is a string contains only digits
    for char in token:
        if char.isalpha():
            return False
    return True

def constructStopList(inputFile): ##Take in a stoplist text file and return a list of stop words 
    stack = []
    with open(inputFile,'r') as fin:
        for line in fin:
            line = line.rstrip()
            line = line.split(' ')
            for items in line:
                stack.append(items)
    return stack

def checkInStopList(token,stopList): ##Check if a token is a stop word
    for items in stopList:
        if token == items:
            return True
    return False

def stage2Processing2(inputList,stopList): ##Function for operations on tokens
    outputList = []
    for items in inputList:
        tempList = []
        tempList.append(items[0])
        tempList.append(items[1])
        stringList = []
        for tokens in items[2]:
            if tokens == '':   ##Remove empty 
                continue
            if (checkIfOnlyDigit(tokens)): ##Remove string with only digits
                continue
            if (checkInStopList(tokens,stopList)):  ##Remove stop words 
                continue
            stringList.append(tokens)
        tempList.append(stringList)
        '''print(str(tempList))
        exit(0)'''
        outputList.append(tempList)
    return outputList
'''def stage2Processing2(inputList,stopList):
    counter = 0;
    while counter < len(inputList):
        counter2 = 0
        while counter2 < len(inputList[counter][2])'''
'''def addInTokenList(token,tokenList): ##Check if a token is already in the frequency list and perform corresponding operations
    if tokenList == {}:
        tokenList[token] = 1
        return tokenList
    for key in tokenList:
        if key == token:
            tokenList[key] += 1
            return tokenList
    tokenList[key] = 1
    return tokenList'''

def checkInDic(item,dic): ##Check if an element is the key for the dictionary
    for key in dic:
        if item == key:
            return True
    return False

'''def constructTokenList(tokenList): ##Contruct a token list for frequency counting
    outputList = {}
    for items in tokenList:
        if checkInDic(items,outputList):
            outputList[items] += 1
        else:
            outputList[items] = 1
    return outputList'''

def constructTokenList(stack):
    globalToken = {}
    for articles in stack:
        for items in articles[2]:
            if checkInDic(items,globalToken):
                globalToken[items] += 1
            else:
                globalToken[items] = 1
    return globalToken

def reOrderTokenList(globalToken):
    outputToken = {}
    for key in globalToken:
        if globalToken[key] >= 5:
            outputToken[key] = globalToken[key]
    return outputToken

'''def reOrderTokenList(tokenList): ##Reorder the list to eliminate tokens with frequency less than 5
    keyList = []
    for key in tokenList:
        if tokenList[key] >= 5:
            keyList.append(key)
    return keyList'''
            
def checkInFrequency(item,tokenList):##Check if one token is included in the frequent tokenlist
    for key in tokenList:
        if key == item:
            return True
    return False

def stemming(inputList):
    parser = PorterStemmer()
    outputList = []
    for items in inputList:
        tempList = []
        tempList.append(items[0])
        tempList.append(items[1])
        stringList = []
        for tokens in items[2]:
            stringList.append(parser.stem(tokens,0,len(tokens)-1)) ##Using portStemming package
        tempList.append(stringList)
        outputList.append(tempList)
    return outputList

def stage2Processing3(inputList,frequentList): ##This function is designed to perform the Stemming process
    outputList2 = []
    checkList = []
    for key in frequentList:
        checkList.append(key)
    for elements in inputList:
        tempList2 = []
        tempList2.append(elements[0])
        tempList2.append(elements[1])
        stringList2 = []
        for tokens in elements[2]:
            if checkInFrequency(tokens,checkList):
                stringList2.append(tokens)
        tempList2.append(stringList2)
        outputList2.append(tempList2)
    return outputList2

def secondParse(stack):
    outputList = []
    for item in stack:
        if (item[2]!=[]):
            outputList.append(item)
    return outputList
            
def printForResults2(inputList, fileName): ##Print out function for stage2
    fout = open(fileName,'w')
    for items in inputList:
        for entry in items:
            fout.write(str(entry)+'\t')
        fout.write('\n\n')
    fout.close()


##=====================================Stage III===========================================
##The functions in this stage is designed to convert each item to vertors
def writeClassList(stack,fileName):  ##make class-file
    fout = open(fileName,'w')
    for articles in stack:
        fout.write(str(articles[0])+","+str(articles[1]))
        fout.write('\n')
    fout.close()

def makeReference(frequentList):  ##Makle lebal references 
    lebalDic = {}
    counter1 = 1
    for key in frequentList:
        lebalDic[key] = counter1
        counter1 += 1
    return lebalDic

def checkInTemp(temp,item):  ##Check if an item is in the lebal list 
    for key in temp:
        if key == item:
            return True
    return False

def constructMethodI(stack,lebalList):  ##Adapt the first method to construct list
    outputList = []
    for articles in stack:
        tempList = [articles[0]]
        temp = {}
        for items in articles[2]:
            lebal = lebalList[items]
            if checkInTemp(temp,lebal):
                temp[lebal] += 1
            else:
                temp[lebal] = 1
        tempList.append(temp)
        outputList.append(tempList)
    return outputList

def constructMethodII(stack): ##Adapt the second method to construct list BASED ON METHOD1
    outputList = []
    for lis in stack:
        newLis = []
        newLis = lis
        for key in newLis[1]:
            frequency = newLis[1][key]
            newFrequency = 1 + (float)(math.sqrt(frequency))
            newLis[1][key] = newFrequency
        outputList.append(newLis)
    return outputList

def constructMethodIII(stack):  ##Adapt the third method to construct list BASED ON METHOD1
    outputList = []
    for lis in stack:
        newLis = []
        newLis = lis
        for key in newLis[1]:
            frequency = newLis[1][key]
            newFrequency = (float)(1 + math.log2(frequency))
            newLis[1][key] = newFrequency
        outputList.append(newLis)
    return outputList

def unify(vectorList):  ##Unify vectors for further comparison
    vectorList2 = []
    vectorList2 = vectorList
    for vectors in vectorList2:
        sumValue = 0.0
        for items in vectors[1]:
            sumValue = sumValue + vectors[1][items] * vectors[1][items]
        sumValue = math.sqrt(sumValue)
        for items2 in vectors[1]:
            newValue = 0.0
            newValue = (float)(vectors[1][items2]/sumValue)
            vectors[1][items2] = newValue
    return vectorList2

def sortedVectors(unifiedList):
    newList = []
    newList = unifiedList
    for articles in newList:
        newLis = []
        newLis = sorted(articles[1].keys())
        newDic = {}
        for item in newLis:
            newDic[item] = articles[1][item]
        articles[1] = newDic
    return newList

def printForResults3(inputList,fileName):  ##make input-file
    fout = open(fileName,'w')
    for lis in inputList:
        fout.write(str(lis[0])+",")
        for items in lis[1]:
            fout.write(str(items) + ',' + str(lis[1][items]) + ',')
        fout.write('\n')
    fout.close()
    

def main():
    topicList = {}
    level1Stack = []
    level1Stack = loadFile()
    print(len(level1Stack))
    for items in level1Stack:
        addInList(items.topics.d.string,topicList)
    frequencyList = {}
    frequencyList = refineTopicList(topicList)
    topicList = frequencyList
    print('length of the topicList is: ' + str(len(topicList)))
    for key in topicList:
        print(topicList[key])
    level2Stack = []
    level2Stack = parseSecondRound(level1Stack,topicList)
    ##printForResults1(level2Stack,"outputForLevel1.txt")  ##To print out the result uncomment this line
    level2Stack = stage2Processing1(level2Stack)
    stopList = constructStopList("stoplist.txt")
    level2Stack = stage2Processing2(level2Stack,stopList)
    level2Stack = stemming(level2Stack)
    level3Stack = []
    globalToken = {}
    frequentList = {}
    globalToken = constructTokenList(level2Stack)
    frequentList = reOrderTokenList(globalToken)
    level3Stack = stage2Processing3(level2Stack,frequentList)
    #level3Stack = secondParse(level3Stack)
    #print(len(level3Stack))
    ##printForResults2(level3Stack,"outputForStage3.txt")
    writeClassList(level3Stack,"class-file.txt")
    lebalDic = {}
    lebalDic = makeReference(frequentList)
    outputMethod1 = []
    outputMethod1 = constructMethodI(level3Stack,lebalDic)
    printForResults3(outputMethod1,"stage3method1.txt")
    outputMethod2 = []
    outputMethod2 = constructMethodII(outputMethod1)
    printForResults3(outputMethod2,"stage3method2.txt")
    outputMethod3 = []
    outputMethod3 = constructMethodIII(outputMethod1)
    printForResults3(outputMethod3,"stage3method3.txt")
    unified1 = unify(outputMethod1)
    unified2 = unify(outputMethod2)
    unified3 = unify(outputMethod3)
    printForResults3(unified1,"unified1.txt")
    printForResults3(unified2,"unified2.txt")
    printForResults3(unified3,"unified3.txt")
    sorted1 = sortedVectors(unified1)
    sorted2 = sortedVectors(unified2)
    sorted3 = sortedVectors(unified3)
    printForResults3(sorted1,"input-fileM1.txt")
    print("yes\n")
    printForResults3(sorted2,"input-fileM2.txt")
    print("yes\n")
    printForResults3(sorted3,"inout-fileM3.txt")
    print("yes\n")
    return 

if __name__ == '__main__':
    main()
