/**
 * Simple implementation of John Conway's Game of Life.
 * Starts with random seed and plays out on bounded grid
 * (without wrap-around).
 * <p>
 * User interactions:
 * ENTER to evolve
 * 'A' to add a live cell
 * 'N' to display neighbor counts for each cell
 * 'Q' to end game
 * <p>
 * <p>
 * TO DO:
 * I removed much of the implementation; look for
 * "TO DO" throughout the code.
 * <p>
 * Also, you may want to add other interactions, for example:
 * - removing (or killing) a cell
 * - toggling a cell (change its state)
 * - adding a bunch of live cells at random locations in one step
 * <p>
 * Other ideas:
 * - Change the program so that the user can enter the size of the
 * grid and the initial density of live cells, either through
 * console input or as commandline args.
 * - Display the number of live cells and the number of evolutions.
 * <p>
 * <p>
 * ADDED FUNCTIONS:
 * User Interactions:
 * 'M' to add multiple cells in one step
 * 'R' to remove cell
 * 'T' to toggle cell
 */


import java.util.Scanner;

public class GameOfLife {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // user commands
        final String QUIT = "Q";
        final String ADD_CELL = "A";
        final String DISPLAY_NEIGHBOR_COUNTS = "N";
        final String ADD_MULTIPLE_CELLS = "M";
        final String REMOVE_CELL = "R";
        final String TOGGLE_CELL = "T";

        // user input is stored here
        String command;

        // world setup
        final int GRID_HEIGHT = 20 + 2; // '+2' is a border of empty
        final int GRID_WIDTH = 40 + 2;  // cells around the grid -- it makes
        // for simple/easy implementation
        final double SEED_DENSITY = 0.6;

        boolean[][] cells = randomSeed(GRID_HEIGHT, GRID_WIDTH, SEED_DENSITY);

        boolean keepGoing = true;
        do {
            displayGrid(cells);
            command = input.nextLine().trim().toUpperCase();

            if (command.startsWith(QUIT)) {
                keepGoing = false;
            } else if (command.startsWith(ADD_CELL)) {
                System.out.print("row: ");
                int row = input.nextInt();
                System.out.print("col: ");
                int col = input.nextInt();
                input.nextLine();
                addCell(cells, row, col);
            } else if (command.startsWith(DISPLAY_NEIGHBOR_COUNTS)) {
                System.out.print("row: ");
                int row = input.nextInt();
                System.out.print("col: ");
                int col = input.nextInt();
                input.nextLine();
                int count = countNeighbors(cells, row, col);
                System.out.println(count);
                //displayNeighborCounts(cells);
            } else if (command.startsWith(ADD_MULTIPLE_CELLS)) {
                addMultipleCells(cells);
            } else if(command.startsWith(REMOVE_CELL)){
                System.out.print("row: ");
                int row = input.nextInt();
                System.out.print("col: ");
                int col = input.nextInt();
                input.nextLine();
                removeCell(cells, row, col);
            } else if(command.startsWith(TOGGLE_CELL)){
                System.out.print("row: ");
                int row = input.nextInt();
                System.out.print("col: ");
                int col = input.nextInt();
                input.nextLine();
                toggleCell(cells, row, col);
            }else {
                cells = evolve(cells);
            }

        } while (keepGoing);

    }

    private static void toggleCell(boolean[][] cells, int row, int col) {
        if(cells[row][col]){
            cells[row][col] = false;
        }
        else{
            cells[row][col] = true;
        }
    }

    private static void removeCell(boolean[][] cells, int row, int col) {
        cells[row][col] = false;
    }


    /*
       Takes current generation of cells and returns the next generation.
       Cells in top and bottom row and leftmost and rightmost columns of grid
       (i.e. border cells) do not evolve

       TO DO: Right now only the first rule is implemented -- you add
       code for the rest.
    */
    public static boolean[][] evolve(boolean[][] cells) {
        boolean[][] nextGen = new boolean[cells.length][cells[0].length];

        for (int row = 1; row < nextGen.length - 1; row++) {
            for (int col = 1; col < nextGen[0].length - 1; col++) {
                int numNeighbors = countNeighbors(cells, row, col);

                // 1. Any live cell with fewer than two live neighbours dies.
                if (cells[row][col] && numNeighbors < 2) {
                    nextGen[row][col] = false;
                }
                // 2. Any live cell with two or three live neighbours lives
                //   on to the next generation.
                //   TO DO
                if (cells[row][col] && (numNeighbors == 2 || numNeighbors == 3)) {
                    nextGen[row][col] = true;
                }

                // 3. Any live cell with more than three live neighbours dies.
                //   TO DO
                if (cells[row][col] && numNeighbors > 3) {
                    nextGen[row][col] = false;
                }

                // 4. Any dead cell with exactly three live neighbours becomes alive.
                //   TO DO
                if (!cells[row][col] == false && (numNeighbors == 3)) {
                    nextGen[row][col] = true;
                }

            }
        }
        return nextGen;
    }

  /*
     Makes the cell at specified location alive.

     TO DO: Currently the user can turn border cells alive,
            or crash the game by specifying location outside
            the grid. Modify this method to prevent either of
            these from happening; if row or col are either
            border or outside the grid display a message letting
            the user know that the location is out of bounds.
  */

    public static void addCell(boolean[][] grid,
                               int row,
                               int col) {

        if ((row > grid.length) || (col > grid[row].length)) {
            System.out.println("Cell out of boundary!");
        }
        if (row == 0 || col == 0) {
            System.out.println("Cell out of boundary!");
        } else {
            grid[row][col] = true;
        }
    }


    public static void addMultipleCells(boolean[][] grid) {
        Scanner input = new Scanner(System.in);
        System.out.println("How many do you want to add?: ");
        int amount = input.nextInt();
        for (int i = 0; i < amount; i++) {
            System.out.print("#" + (i + 1) + " row: ");
            int row = input.nextInt();
            System.out.print("#" + (i + 1) + " col: ");
            int col = input.nextInt();
            input.nextLine();
            addCell(grid, row, col);
        }
    }

    /*
       Counts and returns the number of live cells (true is live) surrounding
       the cell at specified location.

       If X is the cell at (row, col), its neighbors are shown with 'n':

                    n n n
                    n X n
                    n n n

       Thus the number returned must be in [0, 8]

       TO DO: Right now it just returns 0 for each cell, making all the
              cells die out. Prevent this cell massacre!!!!
    */
    public static int countNeighbors(boolean[][] grid, int row, int col) {
        int num = 0;

        // left row
        if (row > 1) {
            for (int r = row-1; r < row + 2; r++) {
                if (grid[r][col-1] == true) {
                    num++;
                }
            }
        }
        if ((col > 0) || (col < grid[0].length)) {
//      +2 skips cell in the middle to just count neighbours
            for (int r = row-1; r < row + 2; r = r + 2) {
                if (grid[r][col] == true) {
                    num++;
                }
            }
        }
        if (row < grid.length) {
            //right row
            for (int r = row-1; r < row + 2; r++) {
                if (grid[r][col + 1] == true) {
                    num++;
                }
            }
        }


        return num;
    }

    /*
       Returns grid with specified height and width, randomly seeded with
       live cells. The probability that a non-border cell is alive is equal
       to density.
    */
    public static boolean[][] randomSeed(int height,
                                         int width,
                                         double density) {
        boolean[][] grid = new boolean[height][width];

        for (int row = 1; row < grid.length - 1; row++) {
            for (int col = 1; col < grid[0].length - 1; col++) {
                grid[row][col] = Math.random() < density;
            }
        }

        return grid;
    }

    /*
       Prints the grid surrounded by a border. Live cells are represented
       with 'o', dead cells are white space.
    */
    public static void displayGrid(boolean[][] grid) {
        // top border
        displayHorizontalBorder(grid[0].length);

        for (int row = 1; row < grid.length - 1; row++) {
            // left border
            System.out.print("| ");

            // cells on grid
            for (int col = 1; col < grid[0].length - 1; col++) {
                System.out.print(grid[row][col] ? "o " : "  ");
            }

            // right border
            System.out.println("|");
        }

        // bottom border
        displayHorizontalBorder(grid[0].length);
    }

    /*
       Displays the grid with each non-border cell showing the number
       of live neighbors around the cell. Useful for debugging!
    */
    public static void displayNeighborCounts(boolean[][] grid) {
        // top border
        displayHorizontalBorder(grid[0].length);

        for (int row = 1; row < grid.length - 1; row++) {
            // left border
            System.out.print("| ");

            // neighbor counts for each cell
            for (int col = 1; col < grid[0].length - 1; col++) {
                System.out.print(countNeighbors(grid, row, col) + " ");
            }

            // right border
            System.out.println("|");
        }

        // bottom border
        displayHorizontalBorder(grid[0].length);
    }

    /*
       Prints a line that looks like this:

           + - - - - - +

       with the number of dashes depending on width.
   */
    private static void displayHorizontalBorder(int width) {
        System.out.print("+ ");
        for (int i = 1; i < width - 1; i++) {
            System.out.print("- ");
        }
        System.out.println("+");
    }
}
