/**
 * Main driver class for Duke.
 */
public class Duke {
    protected Storage storage;
    protected Ui ui;
    protected TaskList arrayOfTasks;

    /**
     * Instantiates Duke object.
     * @param path File path giving the location of the input file.
     */
    public Duke(String path) {
        this.storage = new Storage(path);
        this.ui = new Ui();
    }

    /**
     * Loads input file and starts parsing.
     * @return void
     */
    public void execute() throws DukeException {
        try {
            TaskList inputArray = new TaskList(storage.load());
            arrayOfTasks = inputArray;
        } catch (DukeException error) {
            arrayOfTasks = new TaskList();
            throw new DukeException("Unable to load data from 'duke.txt'. Please ensure that you have" +
                                    " a 'data' folder that contains 'duke.txt' in project directory.");
        }

        ui.printWelcomeMessage();

        boolean isDone = false;
        Command parsedCommand;
        String command = ui.parseInput();

        while (command != null && !isDone) {
            try {
                parsedCommand = Parser.parse(command);
                isDone = parsedCommand.exitCheck();
                parsedCommand.runCommand(arrayOfTasks, ui, storage);
            } catch (DukeException error) {
                System.err.println(error);
            }
            if (isDone) {
                // Do nothing.
            } else {
                command = ui.parseInput();
            }
        }

        ui.exitProgram();

        if (!storage.isStorageChanged()) {
            // Do nothing.
        } else {
            try {
                storage.saveToDisk(arrayOfTasks);
                ui.printBorder();
                System.out.println("Tasks have been successfully saved to duke.txt!");
            } catch (DukeException error) { 
                System.err.println("Tasks cannot be saved to specified file path('data/duke.txt'). Please ensure " +
                                   "that there is a 'data' folder containing 'duke.txt' and try again.");
            }
        }
        ui.printByeMessage();
    }

    /**
     * Instantiates and run Duke program.
     * @param args CMD arguments
     * @return void
     */
    public static void main(String[] args) throws DukeException {
        Duke runDuke = new Duke("data/duke.txt");
        runDuke.execute();
    }
 }
