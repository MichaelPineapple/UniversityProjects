from Tkinter import *
import Tkinter, tkFileDialog, tkMessageBox
from subprocess import Popen, PIPE
import random
import time

class Attribute:
#{
    name = "[null]"
    positive = "[null]"
    negative = "[null]"
    id = 0

    def __init__(self, _name, _pos, _neg, _id):
    #{
        self.name = _name
        self.positive = _pos
        self.negative = _neg
        self.id = _id
    #}
#}

class Preference:
#{
    equation = ""
    penalty = 0
    def __init__(self, _equ, _penalty):
    #{
        self.equation = _equ
        self.penalty = _penalty
    #}
#}

def doNothing(): xxx=0

def loadFile(x):
#{
    handle = tkFileDialog.askopenfile(title="Select File")
    if (not (handle is None)):
    #{
        x.delete(1.0, END)
        x.insert(END, handle.read())
    #}
#}

def parseAttribute(a):
#{
    walk = ""
    name = ""
    pos = ""
    neg = ""
    for c in a:
    #{
        if (c == ':'):
        #{
            name = walk
            walk = ""
        #}
        elif (c == ','):
        #{
            pos = walk
            walk = ""
        #}
        elif (c == ' '): doNothing()
        else: walk += c
    #}
    neg = walk
    if (name.__len__() > 0 and pos.__len__() > 0 and neg.__len__() > 0):
    #{
        global attributeList
        global attributeIndex
        attributeList.append(Attribute(name, pos, neg, attributeIndex))
        attributeIndex += 1
    #}
#}

def parsePreference(p):
#{
    equ = ""
    walk = ""
    pMode = False
    for c in p:
    #{
        if (c == ' '):
        #{
            if (walk == "AND"): equ += "0\n"
            elif (walk == "OR"): equ += ""
            elif (walk == "NOT"): equ += "-"
            else: equ += str(findAttribute(walk))+" "
            walk = ""
        #}
        elif (c == ','): pMode == True
        else: walk += c
    #}
    gay = 0
    equ2 = ""
    for c in equ:
    #{
        if (c == '-'):
        #{
            gay += 1
            if (gay > 2): gay = 1
        #}
        else:
        #{
            if (gay == 1): equ2 += "-"
            gay = 0
            equ2 += c
        #}
    #}
    equ2 += "0"
    global preferencesList
    pen = int(walk)
    preferencesList.append(Preference(equ2, pen))
#}

def parseConstraint(p):
#{
    equ = ""
    walk = ""
    for c in p:
    #{
        if (c == ' '):
        #{
            if (walk == "AND"): equ += "0 "
            elif (walk == "OR"): equ += ""
            elif (walk == "NOT"): equ += "-"
            else: equ += str(findAttribute(walk))+" "
            walk = ""
        #}
        else: walk += c
    #}
    gay = 0
    equ2 = ""
    for c in equ:
    #{
        if (c == '-'):
        #{
            gay += 1
            if (gay > 2): gay = 1
        #}
        else:
        #{
            if (gay == 1): equ2 += "-"
            gay = 0
            equ2 += c
        #}
    #}
    equ2 += "0"
    global constraintsList
    constraintsList.append(equ2)
#}

def findAttribute(name):
#{
    exp = 0
    global attributeList
    for i in attributeList:
    #{
        if (name == i.positive): exp = i.id
        elif (name == i.negative): exp = -i.id
    #}
    return exp
#}

def findAttribute2(id):
#{
    exp = ""
    global attributeList
    for i in attributeList:
    #{
        if (abs(id) == i.id):
        #{
            if (id < 0): exp = i.negative
            else: exp = i.positive
        #}
    #}
    return exp
#}

def calculatePenalty(obj):
#{
    exp = 0
    global preferencesList
    for p in preferencesList:
    #{
        if (obj not in getClaspObjects("p cnf " + str(attributeList.__len__()) + " " + str(p.equation.count('0')) +"\n"+ p.equation)): exp = p.penalty
    #}
    return exp
#}

def convertBackToEng(var):
#{
    var += " "
    exp = ""
    walk = ""

    for c in var:
    #{
        if (c == ' '):
        #{
            exp += findAttribute2(int(walk)) + ","
            walk = ""
        #}
        else: walk += c
    #}

    return exp
#}

def getClaspObjects(var):
#{
    open("claspData.txt", "w").write(var)
    process = Popen(["clasp", "0", "claspData.txt"], stdout=PIPE, stderr=PIPE)
    out, err = process.communicate()
    objList = []
    tmp = out.splitlines()
    for line in tmp:
    #{
        if (line[0] == 'v'): objList.append(line[2:-2])
    #}
    return objList
#}

def calculateOutput():
#{
    #try:
    #{
        global rootFrame
        loadingLbl = Label(rootFrame, text="Calculating...")
        loadingLbl.grid(row=6, column=0, padx=5)
        root.update()

        aStr = Abox.get(1.0, END).strip()
        pStr = Pbox.get(1.0, END).strip()
        cStr = Cbox.get(1.0, END).strip()

        global attributeIndex
        global attributeList
        global preferencesList
        global constraintsList
        attributeList = []
        preferencesList = []
        constraintsList = []
        attributeIndex = 1


        if (aStr.__len__()>0):
        #{
            tmp = aStr.splitlines()
            for line in tmp: parseAttribute(line)

            if (pStr.__len__() > 0):
            #{
                tmp = pStr.splitlines()
                for line in tmp: parsePreference(line)
            #}

            if (cStr.__len__() > 0):
            #{
                tmp = cStr.splitlines()
                for line in tmp: parseConstraint(line + " ")
            #}

            boi = ""
            for q in constraintsList: boi += "\n" + q
            lines = 0
            for c in boi:
            #{
                if (c == '0'): lines += 1
            #}

            if (lines < 1):
            #{
                lines = 1
                boi = "\n0"
            #}

            claspDataStr = "p cnf " + str(attributeList.__len__()) + " " + str(lines) + boi
            open("claspData.txt", "w").write(claspDataStr)
            process = Popen(["clasp", "0", "claspData.txt"], stdout=PIPE, stderr=PIPE)
            out, err = process.communicate()
            #tkMessageBox.showinfo("Clasp", out)
            if (err.__len__() > 0): tkMessageBox.showerror("Clasp Error", "An error occured, please check your input.\n\n" + err)
            else:
            #{
                sat = True
                outputStr = ""
                if ("UNSATISFIABLE" in out):
                #{
                    tkMessageBox.showinfo("OUTPUT", "Unsatisfiable")
                    sat = False
                #}
                elif ("SATISFIABLE" in out): outputStr += "Satisfiable"
                else: outputStr += "Unknown Satisfiablity"

                if (sat):
                #{

                    objList = []

                    tmp = out.splitlines()
                    for line in tmp:
                    #{
                        if (line[0] == 'v'): objList.append(line[2:-2])
                    #}

                    random.seed(time.time())

                    if (objList.__len__() > 1):
                    #{
                        obj1 = ""
                        obj2 = ""
                        while (obj1 == obj2):
                        #{
                            obj1 = objList[random.randint(0, objList.__len__()-1)]
                            obj2 = objList[random.randint(0, objList.__len__()-1)]
                        #}

                        pen1 = calculatePenalty(obj1)
                        pen2 = calculatePenalty(obj2)
                        op = '='
                        if (pen1 > pen2): op = '-->'
                        elif (pen1 < pen2): op = '<--'

                        minObj = objList[0]
                        for x in objList:
                        #{
                            if (calculatePenalty(x) < calculatePenalty(minObj)): minObj = x
                        #}
                        minPenalty = calculatePenalty(minObj)

                        outputStr += "\n\nTWO OBJECTS:\n{"+convertBackToEng(obj1)+"("+str(pen1)+")} "+op+" {"+convertBackToEng(obj2)+"("+str(pen2)+")}"
                        outputStr += "\n\nOPTIMAL OBJECT:\n{"+convertBackToEng(minObj)+"("+str(minPenalty)+")}"
                        outputStr += "\n\nALL OPTIMAL OBJECTS:"
                        for x in objList:
                        #{
                            if (calculatePenalty(x) == minPenalty): outputStr += "\n{"+convertBackToEng(x)+"("+str(minPenalty)+")}"
                        #}


                    #}
                    else: outputStr += "\nNot enought objects."

                    win = Toplevel()
                    msg = Text(win, width=150, height=40, borderwidth=1, relief=SOLID);
                    msg.insert(INSERT, outputStr)
                    msg.config(state=DISABLED)
                    msg.pack()
                    #tkMessageBox.showinfo("OUTPUT", outputStr)

                #}

            #}
        #}
        else: tkMessageBox.showerror("Alert", "No attributes found.")

        loadingLbl.destroy()
        root.update()
    #}
    #except: tkMessageBox.showerror("Error", "An error occured, please check your input.")
#}





#main()
#{
attributeIndex = 0
attributeList = []
preferencesList = []
constraintsList = []

winTitle = "AI Project 3 (N01223646)"
col = 0
pad = 5
boxBorder = 1
boxWidth = 70
boxHeight = 10
boxRelief = SOLID
boxSpan = 1
root = Tk()
root.title(winTitle)
rootFrame = Frame(root, padx=pad, pady=pad)
row = 0
txt = "Attributes"
Label(rootFrame, text=txt+":").grid(row=row, column=col, sticky=W, padx=pad)
Button(rootFrame, text="Load "+txt+" File", command=lambda: loadFile(Abox)).grid(row=row, column=col, sticky=E, padx=pad)
Abox = Text(rootFrame, width=boxWidth, height=boxHeight, borderwidth=boxBorder, relief=boxRelief); Abox.grid(row=row+1, column=col, columnspan=boxSpan, padx=pad, pady=pad)
Abox.insert(INSERT, "appetizer: soup, salad\nentree: beef, fish\ndrink: beer, wine\ndessert: cake, ice-cream")
row = 2
txt = "Constraints"
Label(rootFrame, text=txt+":").grid(row=row, column=col, sticky=W, padx=pad)
Button(rootFrame, text="Load "+txt+" File", command=lambda: loadFile(Cbox)).grid(row=row, column=col, sticky=E, padx=pad)
Cbox = Text(rootFrame, width=boxWidth, height=boxHeight, borderwidth=boxBorder, relief=boxRelief); Cbox.grid(row=row+1, column=col, columnspan=boxSpan, padx=pad, pady=pad)
Cbox.insert(INSERT, "NOT soup OR NOT beer\nNOT soup OR NOT wine")
row = 4
txt = "Preferences"
Label(rootFrame, text=txt+":").grid(row=row, column=col, sticky=W, padx=pad)
Button(rootFrame, text="Load "+txt+" File", command=lambda: loadFile(Pbox)).grid(row=row, column=col, sticky=E, padx=pad)
Pbox = Text(rootFrame, width=boxWidth, height=boxHeight, borderwidth=boxBorder, relief=boxRelief); Pbox.grid(row=row+1, column=col, columnspan=boxSpan, padx=pad, pady=pad)
Pbox.insert(INSERT, "fish AND wine, 10\nwine OR cake, 6\nbeer AND beer OR beef AND NOT soup, 7")
row = 6
Button(rootFrame, text="Calculate Output", command=lambda: calculateOutput()).grid(row=row, column=col, sticky=W, padx=pad)
rootFrame.pack()
root.mainloop()
#}
