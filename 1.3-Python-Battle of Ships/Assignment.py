from string import ascii_uppercase
import sys


def writeOutput(write):  # to write the outputs to the battleship.out file
    print(write, end="")
    file.write(write)


leftColumn = list()
rightColumn = list()

def writeColumn(left, right):  # to print the outputs in two columns to the battleship.out file
    for first, second in zip(left, right):
        if second == "":  # if there is only one column
            writeOutput(f"{first}\n")
        else:
            lenFirst = len(first.expandtabs(4))
            # calculates how many tabs are needed
            space = ((28 - lenFirst)//4) if (lenFirst %
                                             4) == 0 else ((28 - lenFirst)//4 + 1)
            tab = "\t" * space
            writeOutput(f"{first}{tab}{second}\n")


def columnAppend(first, second=""):  # to add a new text to the columns
    if type(first) is str:  # if the text has only one line
        leftColumn.append(first)
        rightColumn.append(second)
    else:  # if the text has more than one line (it is a list of text)
        leftColumn.extend(first)
        rightColumn.extend(second)
    leftColumn.append("")  # an empty line
    rightColumn.append("")  # an empty line


# board of player and hidden board of player will be in this dictionary
board = {"Player1": {}, "Player2": {},
         "Player1hidden": {}, "Player2hidden": {}}

def boardCreator(player, file):  # to create a board and a hidden board
    board[player][" "] = tuple(ascii_uppercase[:10])  # alphabet line
    board[player].update({i: list("-"*10) for i in range(1, 11)})
    for number, line in enumerate(file.readlines(), start=1):
        line = line.rstrip("\n").split(";")
        column = 0

        for letter in line:
            if letter != "":  # if letter is not empty, that show us there is a ship
                # the position is changed
                board[player][number][column] = letter
            column += 1

    board[player+"hidden"][" "] = tuple(ascii_uppercase[:10])
    # to create a line with ten times "-", these are not changed because it is in hidden board
    board[player+"hidden"].update({i: list("-"*10) for i in range(1, 11)})


# all ships will be in this dictionary
shipsComplex = {"Player1": {}, "Player2": {}}

def shipFunction(player):
    # first list is alphabet row
    for row, line in enumerate(list(board[player].values())[1:], start=1):
        for column, ship in enumerate(line):
            if ship != "-":
                # ship added to shipsComplex dictionary
                shipsComplex[player][row, column] = ship

    for shipLetter in ("C", "B", "D", "S", "P"):
        number = 1
        if shipLetter == "C":
            howManyShips = 5
        elif shipLetter == "B":
            howManyShips = 4
        elif shipLetter == "D" or shipLetter == "S":
            howManyShips = 3
        elif shipLetter == "P":
            howManyShips = 2

        for ship in shipsComplex[player]:
            if shipsComplex[player][ship] == shipLetter:
                if checkRow(player, ship, howManyShips, howManyShips, number, shipLetter):
                    number += 1
                elif checkColumn(player, ship, howManyShips, howManyShips, number, shipLetter):
                    number += 1


# all ships will be "categorized" in this dictionary
shipsCategorized = {"Player1": {}, "Player2": {}}

# this function checks the position to the right of the current position of the ships, and grouping the ships using recursive function
def checkRow(player, ship, times, shipsCount, number, shipLetter):
    try:
        # B B B B B to avoid the error in this case
        # B                 P P P
        # B                 P
        # B
        if shipLetter == shipsComplex[player][(ship[0], ship[1]+times)]:
            return checkColumn(player, ship, times, shipsCount, number, shipLetter)
    except:
        pass
    while times > 0:
        times -= 1
        if ship in shipsComplex[player]:
            return checkRow(player, (ship[0], ship[1]+1), times, shipsCount, number, shipLetter)
        else:
            return False

    while shipsCount > 0:
        # if the class of the ship added is not in the dictionary
        if shipLetter not in shipsCategorized[player]:
            shipsCategorized[player][shipLetter] = dict()
            shipsCategorized[player][shipLetter][number] = [
                (ship[0], ship[1]-shipsCount)]
        else:
            # if the position added is the first part of the ship
            if number not in shipsCategorized[player][shipLetter]:
                # creates a new list in shipsCategorized dictionary
                shipsCategorized[player][shipLetter][number] = list()
            shipsCategorized[player][shipLetter][number].append(
                (ship[0], ship[1]-shipsCount))  # and the ship is added
        shipsComplex[player][(ship[0], ship[1]-shipsCount)] = "X"
        shipsCount -= 1

    return True


# This function checks the position to the down of the current position of the ships, and grouping the ships using recursive function
def checkColumn(player, ship, times, shipsCount, number, shipLetter):
    while times > 0:
        times -= 1
        if ship in shipsComplex[player]:
            return checkColumn(player, (ship[0]+1, ship[1]), times, shipsCount, number, shipLetter)
        else:
            return False

    while shipsCount > 0:
        # if the class of the ship added is not in the dictionary
        if shipLetter not in shipsCategorized[player]:
            shipsCategorized[player][shipLetter] = dict()
            shipsCategorized[player][shipLetter][number] = [
                (ship[0]-shipsCount, ship[1])]
        else:
            # if the position added is the first part of the ship
            if number not in shipsCategorized[player][shipLetter]:
                # creates a new list in shipsCategorized dictionary
                shipsCategorized[player][shipLetter][number] = list()
            shipsCategorized[player][shipLetter][number].append(
                (ship[0]-shipsCount, ship[1]))
        shipsComplex[player][(ship[0]-shipsCount), ship[1]] = "X"
        shipsCount -= 1

    return True


def counter(player, letter):
    result = 0

    for ship in shipsCategorized[player][letter].values():
        if ship != []:  # if the ship have not sunk yet
            result += 1

    return result


def countShips(player):
    carrier = counter(player, "C")
    battleship = counter(player, "B")
    destroyer = counter(player, "D")
    submarine = counter(player, "S")
    patrolboat = counter(player, "P")

    return carrier, battleship, destroyer, submarine, patrolboat


# according to the move in the input file, the position on the opposing player's board is hit
def game(player, otherPlayer, round):
    columnAppend(f"{player}'s Move")
    columnAppend(f"Round : {round}", "Grid Size: 10x10")
    columnAppend(showGame("Player1"), showGame("Player2"))
    while len(moves[player]) != 0:
        try:
            move = moves[player][0]
            del moves[player][0]
            if "," not in move:
                raise IndexError
            number, letter = move.split(",")
            if number == "" or letter == "":  # if x or y cordinate is missed
                raise IndexError
            number, letter = int(number), str(letter)
            column = ascii_uppercase.index(letter)
            assert 0 < number <= 10
            assert letter in ascii_uppercase[0:10]
        except IndexError:
            columnAppend(f"IndexError: The given input is incorrect: {move}")
            columnAppend(f"The next move is being played for {player}.")
            round += 1
        except ValueError:
            columnAppend(f"ValueError: The given input is incorrect: {move}")
            columnAppend(f"The next move is being played for {player}.")
            round += 1
        except AssertionError:
            columnAppend(f"AssertionError: Invalid Operation: {move}")
            columnAppend(f"The next move is being played for {player}.")
            round += 1
        else:
            columnAppend(f"Enter your move: {move}")
            number_letter = board[otherPlayer][number][column]

            if number_letter != "-":  # it means that there is a ship at the point
                for ships in shipsCategorized[otherPlayer][number_letter].values():
                    # to delete the ship that was hit from the shipsCategorized dictionary
                    if (number, column) in ships:
                        ships.remove((number, column))
                board[otherPlayer+"hidden"][number][column] = "X"
                board[otherPlayer][number][column] = "X"
            else:
                board[otherPlayer+"hidden"][number][column] = "O"
                board[otherPlayer][number][column] = "O"
            break


def showGame(player, hidden=True):
    output = list()
    if hidden:
        output.append(f"{player}'s Hidden Board")
        for i in board[player+'hidden']:
            output.append(f"{i:<2}{' '.join(board[player+'hidden'][i])}")
    else:
        output.append(f"{player}'s Board")
        for i in board[player]:
            output.append(f"{i:<2}{' '.join(board[player][i])}")

    carrier, battleship, destroyer, submarine, patrolboat = countShips(player)
    output.append("")
    output.append(f"Carrier\t\t{' '.join('X'*(1-carrier) + '-'*(carrier))}")
    output.append(
        f"Battleship\t{' '.join('X'*(2-battleship) + '-'*(battleship))}")
    output.append(
        f"Destroyer\t{' '.join('X'*(1-destroyer) + '-'*(destroyer))}")
    output.append(
        f"Submarine\t{' '.join('X'*(1-submarine) + '-'*(submarine))}")
    output.append(
        f"Patrol Boat\t{' '.join('X'*(4-patrolboat) + '-'*(patrolboat))}")

    return output


def openFile(number):
    try:
        return open(sys.argv[number], "r")  # try to open the file
    except:
        # if not, add filename to errorFiles list
        errorFiles.append(sys.argv[number])


try:
    file = open("Battleship.out", "w")
    errorFiles = list()
    player1txt, player2txt = openFile(1), openFile(2)
    player1in, player2in = openFile(3), openFile(4)
    if len(errorFiles) != 0:
        raise IOError
    moves = {"Player1": [], "Player2": []}
    moves["Player1"] = player1in.read().rstrip(";").split(";")
    moves["Player2"] = player2in.read().rstrip(";").split(";")
    boardCreator("Player1", player1txt)
    boardCreator("Player2", player2txt)
    shipFunction("Player1")
    shipFunction("Player2")
    columnAppend(f"Battle of Ships Game")
    round = 1
    while len(moves["Player1"]) != 0 and len(moves["Player2"]) != 0:
        game("Player1", "Player2", round)
        game("Player2", "Player1", round)
        round += 1

    if sum(countShips("Player1")) == 0:
        columnAppend("Player2 Wins!")
    elif sum(countShips("Player2")) == 0:
        columnAppend("Player1 Wins!")
    elif sum(countShips("Player1")) == 0 and sum(countShips("Player2")) == 0:
        columnAppend("It is a Draw!")
    else:
        columnAppend("Something went wrong, no winner.")

    columnAppend("Final Information")
    columnAppend(showGame("Player1", False), showGame("Player2", False))
except IOError:
    columnAppend(
        f"IOError: input file(s) {', '.join(errorFiles)} is/are not reachable.")
except IndexError:
    columnAppend("IndexError: number of input files less than expected.")
except:
    columnAppend("kaBOOM: run for your life!")
else:
    player1txt.close()
    player2txt.close()
    player1in.close()
    player2in.close()
finally:
    writeColumn(leftColumn, rightColumn)
    file.close()