public class Duke {
    public Storage storage;
    public Ui ui;
    public TaskList arrayOfTasks;

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
    public void execute() {
        try {
            TaskList inputArray = new TaskList(storage.load());
            arrayOfTasks = inputArray;
        } catch (DukeException error) {
            System.out.println("File cannot be loaded from the specified file path. Please try again!");
            arrayOfTasks = new TaskList();
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
                System.err.println("File cannot be saved to the specified file path. Please try again!");
            }
        }
        ui.printByeMessage();
    }

    /**
     * Instantiates and run Duke program.
     * @param args CMD arguments
     * @return void
     */
    public static void main(String[] args) {
        Duke runDuke = new Duke("data/duke.txt");
        runDuke.execute();
    }
 }
