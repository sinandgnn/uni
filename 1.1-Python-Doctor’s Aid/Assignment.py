"""to clear doctors_aid_outputs.txt file"""
with open("doctors_aid_outputs.txt", "w") as file:
    file.write("")


"""to save all patients in a list"""
patientList = [
    ["Patient", "Diagnosis", "Disease", "Disease", "Treatment",
        "Treatment"],  # for first row of the table
    ["Name", "Accuracy", "Name", "Incidence", "Name",
        "Risk"],  # for second row of the table
    ["", "", "", "", "", ""]  # for third row of the table
]


"""to create a new patient"""
def create(patientName, diagnosisAccuracy, diseaseName, diseaseIncidence, treatmentName, treatmentRisk):
    duplication = False

    for row in patientList:
        if row[0] == patientName:
            duplication = True  # duplication is True because the patient is in list already

    if duplication:  # if duplication is true, this code runs
        writeOutput(
            f"Patient {patientName} cannot be recorded due to duplication.\n")
    else:
        patientList.append([patientName,
                            "{:.2f}".format(
                                round(float(diagnosisAccuracy)*100, 2))+"%",  # xx.xx%
                            diseaseName,
                            diseaseIncidence,
                            treatmentName,
                            str(int(float(treatmentRisk)*100))+"%"])  # xx%
        writeOutput(f"Patient {patientName} is recorded.\n")


"""to delete a exist patient"""
def remove(name):
    absence = True

    for row in patientList:
        if row[0] == name:
            absence = False  # absence is false because the patient is in list
            patientList.remove(row)
            writeOutput(f"Patient {name} is removed.\n")

    if absence:  # if absence is not false, this code runs
        writeOutput(f"Patient {name} cannot be removed due to absence.\n")


"""to show all patients in a table"""
def list():
    maxColumn = []
    """for loop below calculates the length of the longest word for each column"""
    for i in range(6):  # range(6) because we have 6 column
        columns = []
        for j in range(len(patientList)):  # check all patients on the list
            # length of all words in a column
            columns.append(len(patientList[j][i]))
        # length of the longest word in a column
        maxColumn.append(max(columns))

    spaces = []
    """for loop below calculates how many space have to be for each column"""
    for i in maxColumn:
        space = (i//4+1 if i % 4 != 0 else i//4)*4
        spaces.append(space)

    """for loop below prints the list using calculated spaces"""
    counter = 1  # in third row, have to write "-" only, for this we have a counter
    for row in patientList:
        c = []
        for i in range(6):  # range(6) because of we have 6 column
            tabs = spaces[i]-len(row[i])
            # calculated how many "\t" should be in each column for each row
            c.append(tabs//4+1 if tabs % 4 != 0 else tabs//4)
        tab = "\t"  # because f-string expression part cannot include a backslash
        if counter == 3:  # for third row "-"
            writeOutput(
                f"{'-'*c[0]*4}{'-'*c[1]*4}{'-'*c[2]*4}{'-'*c[3]*4}{'-'*c[4]*4}{'-'*9}\n")
        else:
            writeOutput(
                f"{row[0]}{tab*c[0]}{row[1]}{tab*c[1]}{row[2]}{tab*c[2]}{row[3]}{tab*c[3]}{row[4]}{tab*c[4]}{row[5]}\n")
        counter += 1


"""to calculate patient’s probability of having the disease"""
def returnProbability(name):
    for row in patientList:
        if row[0] == name:
            inTurkey = int(row[3].split("/")[0])
            accuracy = round(float(row[1][0:-1])/100, 4)
            probability = round(
                (inTurkey*accuracy) / ((inTurkey*accuracy)+(99950*(1-accuracy)))*100, 2)
            return (probability)


"""to give outout patient’s probability of having the disease"""
def probability(name):
    absence = True

    for row in patientList:
        if row[0] == name:
            writeOutput(
                f"Patient {name} has a probability of {returnProbability(name)}% of having {row[2].lower()}.\n")
            absence = False  # absence is false because the patient is in list

    if absence:  # if absence is not false, this code runs
        writeOutput(
            f"Probability for {name} cannot be calculated due to absence.\n")


"""to give output to the patient’s recommendation for a particular treatment"""
def recommendation(name):
    absence = True

    for row in patientList:
        if row[0] == name:
            risk = int(row[5][:-1])  # treatment risk of the patient
            if risk > returnProbability(name):
                writeOutput(
                    f"System suggests {name} NOT to have the treatment.\n")
            else:
                writeOutput(f"System suggests {name} to have the treatment.\n")
            absence = False  # absence is false because the patient is in list
            break

    if absence:  # if absence is not false, this 3code runs
        writeOutput(
            f"Recommendation for {name} cannot be calculated due to absence.\n")


"""to write the output to the output file"""
def writeOutput(write):
    with open("doctors_aid_outputs.txt", "a", encoding='utf-8') as f:
        f.write(write)


"""to read input file and call functions"""
def readInput():
    with open("doctors_aid_inputs.txt", "r", encoding='utf-8') as inputFile:
        for line in inputFile:
            # each line is split by space and the first index is "command"
            command = line.split()[0]

            if command == "list":
                list()
            else:
                sep = line.split(", ")
                # first index of sep is "create (name)", first index split by space ["create","(name)"], the second index of this splited index is patientName
                patientName = sep[0].split()[1]

                if command == "create":
                    create(patientName,  # Name
                           sep[1],  # Diagnosis Accuracy 0.xxx
                           sep[2],  # Disease NameFF
                           sep[3],  # Disease Incidence
                           sep[4],  # Treatment Name
                           sep[5])  # Treatment Risk 0.xx
                elif command == "remove":
                    remove(patientName)
                elif command == "recommendation":
                    recommendation(patientName)
                elif command == "probability":
                    probability(patientName)


readInput()
