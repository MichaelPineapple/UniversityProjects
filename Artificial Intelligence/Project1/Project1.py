"""
AI - PROJECT 1

A program which will read state data from a .csv file and display information to the user.
The user can sort the states by either name or population.
The user can also find specific state data by searching for it by name.

Author: Michael Wilson
Version: 1.0
Email: n01223646@unf.edu
Data: 1-24-19
"""

# STATE CLASS
class State:

    """
    This class stores information about a single state object and provides useful functions for manipulating and accessing said data.
    """

    def __init__(self, _name, _cap, _abb, _region, _pop, _seats):

        """
        Initializes the State class

        :_name: State name
        :_cap: State capital
        :_abb: State abbreviation
        :_region: State region
        :_pop: State population
        :_seats: State's number of US House seats
        :return: Nothing
        :raise: Nothing
        """

        self.name = _name
        self.capital = _cap
        self.abbreviation = _abb
        self.region = _region
        self.population = _pop
        self.houseSeats = _seats

    name = ""
    capital = ""
    region = ""
    abbreviation = ""
    population = 0
    houseSeats = 0

    def __gt__(self):

        """
        Gets the state name for comparison with other states

        :return: The state's name
        :raise: Nothing
        """

        return self.name

    def __str__(self):

        """
        Returns all the state information in a single string

        :return: All state information in one string
        """

        return "State Name: " + self.name + "\nCapital City: " + self.capital + "\nRegion: " + self.region + "\nAbbreviation: " + \
               self.abbreviation + "\nPopulation: " + str(self.population) + "\nUS House Seats: " +  str(self.houseSeats)

    def __setName__(self, x):

        """
        Sets the state name

        :x: The new name
        :return: Nothing
        :raise: Nothing
        """

        self.name = x

    def __getName__(self):

        """
        Returns the state name

        :return: The state name
        :raise: Nothing
        """

        return self.name



    def __setCapital__(self, x):

        """
        Sets the state capital

        :x: The new capital
        :return: Nothing
        :raise: Nothing
        """

        self.capital = x

    def __getCapital__(self):

        """
        Returns the state capital

        :return: The state capital
        :raise: Nothing
        """

        return self.capital



    def __setAbbreviation__(self, x):

        """
        Sets the state abbreviation

        :x: The new abbreviation
        :return: Nothing
        :raise: Nothing
        """

        self.abbreviation = x

    def __getAbbreviation__(self):

        """
        Returns the state abbreviation

        :return: The state abbreviation
        :raise: Nothing
        """

        return self.abbreviation



    def __setRegion__(self, x):

        """
        Sets the state region

        :x: The new region
        :return: Nothing
        :raise: Nothing
        """

        self.region = x

    def __getRegion__(self):

        """
        Returns the state region

        :return: The state region
        :raise: Nothing
        """

        return self.region


    def __setPopulation__(self, x):

        """
        Sets the state population

        :x: The new population
        :return: Nothing
        :raise: Nothing
        """

        self.population = x

    def __getPopulation__(self):

        """
        Returns the state population

        :return: The state population
        :raise: Nothing
        """

        return self.population



    def __setHouseSeats__(self, x):

        """
        Sets the state's number of US House seats

        :x: The new number of US House seats
        :return: Nothing
        :raise: Nothing
        """

        self.houseSeats = x

    def __getHouseSeats__(self):

        """
        Returns the state's number of US House seats

        :return: The state's number of US House seats
        :raise: Nothing
        """

        return self.houseSeats

# END STATE CLASS

# READ FILE DATA
stateList = []
file = open('States.csv', "r");
lineNum = 0
for line in file:
    if (lineNum > 0):
        stateName = "[null]"
        stateCap = "[null]"
        stateAbbr = "[null]"
        stateRegion = "[null]"
        statePop = "[null]"
        stateSeats = "[null]"
        commaNum = 0
        walk = ""
        for ch in line:
            if (ch == ',' or ch == '\n'):
                if (commaNum == 0): stateName = walk
                elif (commaNum == 1): stateCap = walk
                elif (commaNum == 2): stateAbbr = walk
                elif (commaNum == 3): statePop = int(walk)
                elif (commaNum == 4): stateRegion = walk
                elif (commaNum == 5): stateSeats = int(walk)
                else: print "error reading file!"
                commaNum += 1
                walk = ""
            else: walk += ch
        stateList.append(State(stateName, stateCap, stateAbbr, stateRegion, statePop, stateSeats))
    lineNum += 1
file.close()

sortMode = False

# FUNCTIONS
def __report__():

    """
    Prints data for all the states to the console.

    :return: Nothing
    :raise: Nothing
    """

    print ""
    strFormat = "%-20s%-20s%-20s%-20s%-20s%-20s"
    print(strFormat %("State Name", "Capital City", "Abbr", "Population", "Region", "US House Seats"))
    print "------------------------------------------------------------------------------------------------------------------"
    for s in stateList:
        print(strFormat %(s.__getName__(),s.__getCapital__(),s.__getAbbreviation__(),str(s.__getPopulation__()),s.__getRegion__(),str(s.__getHouseSeats__())))

def __sortByName__():
    
    """
    Sorts all states by name using quick sort.

    :return: Nothing
    :raise: Nothing
    """

    __quickSort__(stateList, 0, len(stateList)-1)
    global sortMode
    sortMode = True
    print "\n[States sorted by name]"

def __quickSort__(_list, _minIndex, _maxIndex):

    """
    A recursive quick sort function.

    :_list: A list of State objects to be sorted
    :_minIndex: The minimum index of the state list to be sorted
    :_maxIndex: The maximum index of the state list to be sorted
    :return: Nothing
    :raise: Nothing
    """

    if _minIndex < _maxIndex:
        i = (_minIndex - 1)
        pivot = _list[_maxIndex].__getName__()
        for j in range(_minIndex, _maxIndex):
            if _list[j].__getName__() <= pivot:
                i = i + 1
                _list[i], _list[j] = _list[j], _list[i]
        _list[i + 1], _list[_maxIndex] = _list[_maxIndex], _list[i + 1]
        pi = (i + 1)
        __quickSort__(_list, _minIndex, pi - 1)
        __quickSort__(_list, pi + 1, _maxIndex)

def __sortByPop__():
    
    """
    Sorts all states by population using radix sort.

    :return: Nothing
    :raise: Nothing
    """
    
    global sortMode
    sortMode = False
    kek = 10
    max = False
    temp = -1
    cur = 1
    while not max:
        max = True
        tempList = [list() for _ in range(kek)]
        for i in stateList:
            temp = i.__getPopulation__() / cur
            tempList[temp % kek].append(i)
            if (max and temp > 0): max = False
        a = 0
        for b in range(kek):
            jucc = tempList[b]
            for i in jucc:
                stateList[a] = i
                a += 1
        cur *= kek
    print "\n[States sorted by population]"

def __find__():
    
    """
    Searches for a particular state in the state list using a user provided key.
    If the list is sorted by name, the search function will use binary search. Else, sequential search will be used.

    :return: Nothing
    :raise: Nothing
    """
    
    key = raw_input("Enter the state name: ")
    temp = None
    if sortMode:
        b = len(stateList) - 1
        a = 0
        while a <= b and temp == None:
            midpoint = (a + b) // 2
            if (stateList[midpoint].__getName__() == key):
                temp = stateList[midpoint]
            else:
                if (key < stateList[midpoint].__getName__()):
                    b = midpoint - 1
                else:
                    a = midpoint + 1
        print "\n[Binary Search]\n"
    else:
        for s in stateList:
            if s.__getName__() == key: temp = s
        print "\n[Sequential Search]\n"

    if (temp == None): print "State '" + key + "' not found."
    else:
        print "State Name:      "+temp.__getName__()
        print "Capital City:    "+temp.__getCapital__()
        print "Abbr:            "+temp.__getAbbreviation__()
        print "Population:      "+str(temp.__getPopulation__())
        print "Region:          "+temp.__getRegion__()
        print "US House Seats:  "+str(temp.__getHouseSeats__())


def __quit__():
    
    """
    Quits the program.

    :return: Nothing
    :raise: Nothing
    """
    
    print "Goodbye..."
    exit(0)

# MAIN LOOP
while 1:
    print "\n1. Print a state report\n2. Sort by State name\n3. Sort by Population\n4. Find and print a given state\n5. Quit\n"

    try: user = input("Enter your choice: ")
    except: user = -1

    if (user == 1): __report__()
    elif (user == 2): __sortByName__()
    elif (user == 3): __sortByPop__()
    elif (user == 4): __find__()
    elif (user == 5): __quit__()
    else: print "Invalid Input! Try Again..."