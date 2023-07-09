public class Main {
    public static String[][] board;
    public static String[] moves;
    public static int score = 0;
    public static int currentPositionY; // Stores the current vertical location of "*"
    public static int currentPositionX; // Stores the current horizontal location of "*"
    public static int rowSize; // Stores the row size of board to calculate mod
    public static int columnSize; // Stores the column size of board to calculate mod
    public static boolean hole = false; // Checks the white ball fell into the hole or not
    public static String boardFile; // For arguments
    public static String moveFile; // For arguments

    public static void main(String[] args) {
        boardFile = args[0];
        moveFile = args[1];

        moves = File.readFile(moveFile)[0].split(" ");
        board = setBoard();

        File.writeFile("output.txt", "Game board:", false, true);
        boardShow();

        setPosition();
        game();

        File.writeFile("output.txt", "Your output is:", true, true);
        boardShow();

        if (hole)
            File.writeFile("output.txt", "Game Over!", true, true);
        File.writeFile("output.txt", "Score: " + score, true, true);
    }

    public static String[][] setBoard() { // Creates the 2d array board
        String[] lines = File.readFile(boardFile);

        rowSize = lines.length;
        columnSize = lines[0].split(" ").length;

        String[][] board = new String[rowSize][columnSize]; // Creates a new (rowSize x columnSize) 2d array

        for (int i = 0; i < lines.length; i++) {
            board[i] = lines[i].split(" "); // Splits the line for 2d array
        }
        return board;
    }

    public static void boardShow() { // Prints the board
        for (String[] strings : board) {
            for (String string : strings) {
                File.writeFile("output.txt", string + " ", true, false);
            }
            File.writeFile("output.txt", "", true, true);
        }
        File.writeFile("output.txt", "", true, true);
    }

    public static void setPosition() { // Gets the position of white ball before the game starts
        for (int row = 0; row < rowSize; row++) {
            for (int column = 0; column < columnSize; column++) {
                if (board[row][column].equals("*")) {
                    currentPositionX = column;
                    currentPositionY = row;
                }
            }
        }
    }

    // Loops through the moves array and makes the moves.
    // If there is a wall where the white ball wants to move, the white ball will
    // move in the opposite direction. (1 step to move in the opposite direction).
    // If there is a hole (H), the white ball becomes space and the loop ends.
    // According to the willChange variable, score is changed.
    // Math.floorMod methods always provides to positive mod like in Python.The %
    // operator can provide negative number.
    // For example, -1 % 5 = -1 Math.floorMod(-1,5) = 4
    // If the white ball try to get out of the board, Math.floorMod makes our job
    // easier and the white ball appears from the other side.
    public static void game() {
        File.writeFile("output.txt", "Your movement is:", true, true);

        loop: for (String move : moves) {
            File.writeFile("output.txt", move + " ", true, false);
            String willChange = "";
            switch (move) {
                case "L":
                    willChange = board[currentPositionY][Math.floorMod(currentPositionX - 1, columnSize)];
                    switch (willChange) {
                        case "W":
                            willChange = board[currentPositionY][Math.floorMod(currentPositionX + 1, columnSize)];
                            moveRight(willChange);
                            break;
                        case "H":
                            hole = true;
                            board[currentPositionY][currentPositionX] = " ";
                            break loop;
                        default:
                            moveLeft(willChange);
                            break;
                    }
                    break;
                case "R":
                    willChange = board[currentPositionY][Math.floorMod(currentPositionX + 1, columnSize)];
                    switch (willChange) {
                        case "W":
                            willChange = board[currentPositionY][Math.floorMod(currentPositionX - 1, columnSize)];
                            moveLeft(willChange);
                            break;
                        case "H":
                            hole = true;
                            board[currentPositionY][currentPositionX] = " ";
                            break loop;
                        default:
                            moveRight(willChange);
                            break;
                    }
                    break;
                case "U":
                    willChange = board[Math.floorMod(currentPositionY - 1, rowSize)][currentPositionX];
                    switch (willChange) {
                        case "W":
                            willChange = board[Math.floorMod(currentPositionY + 1, rowSize)][currentPositionX];
                            moveDown(willChange);
                            break;
                        case "H":
                            hole = true;
                            board[currentPositionY][currentPositionX] = " ";
                            break loop;
                        default:
                            moveUp(willChange);
                            break;
                    }
                    break;
                case "D":
                    willChange = board[Math.floorMod(currentPositionY + 1, rowSize)][currentPositionX];
                    switch (willChange) {
                        case "W":
                            willChange = board[Math.floorMod(currentPositionY - 1, rowSize)][currentPositionX];
                            moveUp(willChange);
                            break;
                        case "H":
                            hole = true;
                            board[currentPositionY][currentPositionX] = " ";
                            break loop;
                        default:
                            moveDown(willChange);
                            break;
                    }
                    break;
            }
            switch (willChange) {
                case "R":
                    score += 10;
                    break;
                case "Y":
                    score += 5;
                    break;
                case "B":
                    score -= 5;
                    break;
            }
            board[currentPositionY][currentPositionX] = "*";
        }
        File.writeFile("output.txt", "\n", true, true);
    }

    // Move* methods swaps the character with the new one and swapped character is
    // R,Y or B, it changes with X.
    public static void moveLeft(String current) {
        board[currentPositionY][currentPositionX] = current;
        if (current.equals("R") || current.equals("Y") || current.equals("B"))
            board[currentPositionY][currentPositionX] = "X";
        currentPositionX = Math.floorMod(currentPositionX - 1, columnSize);
    }

    public static void moveRight(String current) {
        board[currentPositionY][currentPositionX] = current;
        if (current.equals("R") || current.equals("Y") || current.equals("B"))
            board[currentPositionY][currentPositionX] = "X";
        currentPositionX = Math.floorMod(currentPositionX + 1, columnSize);
    }

    public static void moveUp(String current) {
        board[currentPositionY][currentPositionX] = current;
        if (current.equals("R") || current.equals("Y") || current.equals("B"))
            board[currentPositionY][currentPositionX] = "X";
        currentPositionY = Math.floorMod(currentPositionY - 1, rowSize);
    }

    public static void moveDown(String current) {
        board[currentPositionY][currentPositionX] = current;
        if (current.equals("R") || current.equals("Y") || current.equals("B"))
            board[currentPositionY][currentPositionX] = "X";
        currentPositionY = Math.floorMod(currentPositionY + 1, rowSize);
    }
}