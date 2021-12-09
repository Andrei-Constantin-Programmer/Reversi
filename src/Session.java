import java.io.*;

/**
 * The session contains all of the information of two players and the current board that they are playing.
 *
 * @author Andrei Constantin
 * @version 16-04-2021
 */
public class Session implements Serializable
{

    private SessionPlayer player1, player2;
    private GameBoard gameBoard;
    private String status;

    /**
     * Constructor for a Session object.
     * @param player1 The first player
     * @param player2 The second player
     * @param activeGameBoard The current board
     * @param status The status of the game in-progress
     */
    public Session(SessionPlayer player1, SessionPlayer player2, GameBoard activeGameBoard, String status)
    {
        this.player1 = player1;
        this.player2 = player2;
        gameBoard = activeGameBoard;
        this.status = status;
    }

    /**
     * Get Player 1.
     * @return Player 1
     */
    public SessionPlayer getPlayer1()
    {
        return player1;
    }

    /**
     * Get Player 2.
     * @return Player 2
     */
    public SessionPlayer getPlayer2()
    {
        return player2;
    }

    /**
     * Get the current game board.
     * @return The game board.
     */
    public GameBoard getGameBoard()
    {
        return gameBoard;
    }

    /**
     * Get the status of the game in-progress.
     * @return The status
     */
    public String getStatus(){ return status; }

    /**
     * Save the session to a file with the specified name.
     * @param fileName The name of the file where the session will be saved
     * @throws SaveSessionException
     */
    public void saveToFile(String fileName) throws SaveSessionException
    {
        createSavesFolder();

        String saveString = "./saves/"+fileName+".rev";
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(saveString, false))) {
            outStream.writeObject(this);
        }
        catch (IOException e) {

            throw new SaveSessionException("Could not save to file.");
        }
    }

    /**
     * Save the session to a file with a predefined name.
     * @throws SaveSessionException
     */
    public void autoSave() throws SaveSessionException {
        saveToFile("AUTO " + this.player1.getName() + "-" + this.player2.getName());
    }

    /**
     * Quick save the given session. It overrides the existing quick save.
     * @param session The session to quick save
     * @throws SaveSessionException
     */
    public static void quickSave(Session session) throws SaveSessionException
    {
        session.saveToFile("QUICK");
    }

    /**
     * Load a session from the given file and replace the current session with the loaded one (if possible).
     * @param fileName The name of the file from which to load the session
     * @return The loaded session
     * @throws LoadSessionException
     */
    public static Session loadFromFile(String fileName) throws LoadSessionException
    {
        try(ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileName)))
        {
            return (Session) inStream.readObject();
        }
        catch(Exception ex) {
            throw new LoadSessionException("Could not load the file.");
        }
    }

    /**
     * Create a saves folder if it does not exist.
     */
    public static void createSavesFolder()
    {
        File directory = new File("./saves/");
        if(!directory.exists())
            directory.mkdir();
    }

    /**
     * Change the game board to a new game board.
     * @param newGameBoard The new game board
     */
    public void setGameBoard(GameBoard newGameBoard)
    {
        gameBoard = newGameBoard;
    }

    /**
     * Change the status to a new status.
     * @param newStatus The new status
     */
    public void setStatus(String newStatus)
    {
        status = newStatus;
    }

    /**
     * Change player 1 to a new player.
     * @param player1 The new player 1
     */
    public void setPlayer1(SessionPlayer player1) {
        this.player1 = player1;
    }

    /**
     * Change player 2 to a new player.
     * @param player2 The new player 2
     */
    public void setPlayer2(SessionPlayer player2) {
        this.player2 = player2;
    }
}
