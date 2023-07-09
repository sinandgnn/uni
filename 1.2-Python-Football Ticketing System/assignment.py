from sys import argv
from string import ascii_uppercase


def writeOutput(write):  # to write the outputs to the output.txt file
    print(write, end="")
    file.write(write)


category = dict()  # all categories will be on this dictionary.


def create(categoryName, rowsColumns):  # to create a new category
    if categoryName in category:  # if there is a category in the category dictionary which name is "categoryName", it gives an error message
        writeOutput(
            f"Warning: Cannot create the category for the second time. The stadium has already {categoryName}\n")
    else:
        # given number of rows and number of columns are splitted from "x" first of them is row, and other is column
        rows, columns = int(rowsColumns.split("x")[0]), int(
            rowsColumns.split("x")[1])
        # a new dictionary which name is "categoryName" is created inside the category dictionary
        category[categoryName] = dict()
        # columns of the category are named with the numbers, and key's name is " " (only one whitespace)
        category[categoryName][" "] = tuple(range(columns))

        # rows of the category are named with the characters of the English alphabet
        for i in ascii_uppercase[:rows]:
            # each row has "X" in all columns because seats are haven't sold yet
            category[categoryName][i] = list("X"*columns)
        writeOutput(
            f"The category '{categoryName}' having {rows*columns} seats has been created\n")


def checkCategory(categoryName, seat, letter, number):  # to check the given category
    # how many columns are in the given category
    numberOfColumns = len(category[categoryName][' '])

    if numberOfColumns <= number and letter not in category[categoryName]:
        writeOutput(
            f"Error: The category '{categoryName}' has less row and column than the specified index {seat}!\n")
    elif numberOfColumns <= number:
        writeOutput(
            f"Error: The category '{categoryName}' has less column than the specified index {seat}!\n")
    elif letter not in category[categoryName]:
        writeOutput(
            f"Error: The category '{categoryName}' has less row than the specified index {seat}!\n")
    else:
        return True


def sell(customerName, paymentType, categoryName, seats):  # to sell tickets
    # student = "S" | full = "F" | season(else) = "T"
    payment = "S" if paymentType == "student" else (
        "F" if paymentType == "full" else "T")

    for seat in seats:  # "seats" is a list of seats/seat and this list has at least 1 element
        letter = seat[0]  # first index of seat is letter

        if "-" not in seat:  # if "seat" is not a "seat ranges (B2-5, D6-18)"
            # first index of seat is letter and the rest are numbers (B*1*, D*18*)
            number = int(seat[1:])
            if checkCategory(categoryName, seat, letter, number):
                if category[categoryName][letter][number] != "X":  # if the seat is already sold
                    writeOutput(
                        f"Warning: The seat {seat} cannot be sold to {customerName} since it was already sold!\n")
                else:
                    # "X" is changed depending on the payment type for this seat  (letternumber)
                    category[categoryName][letter][number] = payment
                    writeOutput(
                        f"Success: {customerName} has bought {seat} at {categoryName}\n")
        else:
            # "seat" are splitted from "-" (D11-18 => ["D11","18"]) first of them of first index is letter, the rest is first seat (fromSeat)
            fromSeat = int(seat.split("-")[0][1:])
            # second index is last seat (toSeat)
            toSeat = int(seat.split("-")[1])

            if checkCategory(categoryName, seat, letter, toSeat):
                # if all of this seats (fromSeat to toSeat) are available
                if category[categoryName][letter][fromSeat:toSeat+1].count("X") == len(category[categoryName][letter][fromSeat:toSeat+1]):
                    for number in range(fromSeat, toSeat+1):
                        # "X" is changed depending on the payment type for this seat (letternumber)
                        category[categoryName][letter][number] = payment
                    writeOutput(
                        f"Success: {customerName} has bought {seat} at {categoryName}\n")
                else:
                    writeOutput(
                        f"Warning: The seats {seat} cannot be sold to {customerName} due some of them have already been sold\n")


def cancel(categoryName, seats):  # to cancel an existing ticket
    for seat in seats:  # "seats" argument is a list of seats and this list has at least 1 element
        letter = seat[0]  # first index of seat is "letter" (*B*1, *D*7)
        number = int(seat[1:])  # and the rest are numbers (B*1*, D*13*)

        if checkCategory(categoryName, seat, letter, number):
            if category[categoryName][letter][number] != "X":  # if the seat is been sold before
                # payment type is changed to "X" for this seat (letternumber)
                category[categoryName][letter][number] = "X"
                writeOutput(
                    f"Success: The seat {seat} at '{categoryName}' has been canceled and now ready to sell again\n")
            else:
                writeOutput(
                    f"Error: The seat {seat} at '{categoryName}' has already been free! Nothing to cancel\n")


def show(categoryName):  # to visualize the current layout of the specified category
    writeOutput(f"Printing category layout of {categoryName}\n")

    # category dictionary is reversed because "category" must have "A" at the bottom | 'dict' object is not reversible in Python 3.6.8, so that list() function is used
    for i in reversed(list(category[categoryName])):
        # if key's name is not " " (this means it is not a numbers row)
        if i != " ":
            writeOutput(f"{i} ")
            writeOutput("  ".join(category[categoryName][i]))
        else:
            for j in category[categoryName][i]:
                writeOutput(f"{j:>3}")
        writeOutput("\n")


def balance(categoryName):  # to show the number of tickets and the total revenue in a certain category
    # how many "students" are in the sold seats
    student = sum([i.count("S") for i in category[categoryName].values()])
    # how many "full" are in the sold seats
    full = sum([i.count("F") for i in category[categoryName].values()])
    # how many "season" are in the sold seats
    season = sum([i.count("T") for i in category[categoryName].values()])
    total = student*10 + full*20 + season*250  # total
    output = f"category report of '{categoryName}'"

    writeOutput(f"{output}\n{'-'*len(output)}\n")
    writeOutput(
        f"Sum of students = {student}, Sum of full pay = {full}, Sum of season ticket = {season}, and Revenues = {total} Dollars\n")


file = open("output.txt", "w")
with open(argv[1], "r") as reader:
    for line in reader:
        word = line.split()  # each line is splitted from spaces
        command = word[0]  # first of them is command
        if command == "CREATECATEGORY":
            # word1=categoryName, #word2=numberOfRowsXnumberOfColumns
            create(word[1], word[2])
        elif command == "SELLTICKET":
            # word1=customerName, word2=full-student-season, word3=categoryName, word4=seat*
            sell(word[1], word[2], word[3], word[4:])
        elif command == "CANCELTICKET":
            cancel(word[1], word[2:])  # word1=categoryName, word2=seat*
        elif command == "SHOWCATEGORY":
            show(word[1])  # word1=categoryName
        elif command == "BALANCE":
            balance(word[1])  # word1=categoryName
file.close()
