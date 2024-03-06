import bagel.*;
import bagel.util.Vector2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;



/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2023
 * Please enter your name below
 * @author Jiali Ying
 */
public class ShadowDance extends AbstractGame  {
    private double shortestDistance = 0;

    private int counter = 0;
    private final int RX = 900;
    private final int UPX = 500;
    private final int LXY = 100;
    private final double ORIGINX = 800;
    private final double ORIGINY = 600;
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final String SPECIAL = "Special";
    private final String CLEAR = "CLEAR!";
    private final String TRY_AGAIN = "TRY AGAIN";
    private final String LANE = "Lane";
    private final int ENEMYFRAME= 600;
    private int enemyFrameCounter = 0;
    private final Font titleFont;
    private final Font instructionFont;
    private int level;
    private int targetScore = 0;
    private static boolean isGameOver = false;
    private static boolean isGameStarted = false;
    private int sameFrameCounter = 0;
    private Track currentTrack;
    private final Vector2 START = new Vector2(800, 600);
    private Vector2 targetPosition;
    private final int CENTER_X = Window.getWidth() / 2;
    private static int doubleScoreCheck = 1;
    private static int doubleScoreFrameCount = 0;
    private List<GameElement> gameElements;
    private static List<Lane> lanes = new ArrayList<>();
    private static List<Note> notes = new ArrayList<>();
    private static List<Projectile> projectiles= new ArrayList<>();
    // in case multiple double scores work together
    private static List<Integer> doubleScore = new ArrayList<>();
    private static List<Enemy> enemies = new ArrayList<>();
    private static int speed = 2;
    private Guardian guardian;

    private String[] csvFileNames = {
            "res/level1.csv",
            "res/level2.csv",
            "res/level3.csv"
    };


    /**
     * Getter for the doubleScoreCheck.
     * doubleScoreCheck - if double score is activated or not
     * @return doubleScoreCheck The current doubleScoreCheck value.
     */
    public static int getDoubleScoreCheck() {
        return doubleScoreCheck;
    }
    /**
     * Setter for the doubleScoreCheck.
     *
     * @param doubleScoreCheck The new value for doubleScoreCheck.
     */
    public static void setDoubleScoreCheck(int doubleScoreCheck) {
        ShadowDance.doubleScoreCheck = doubleScoreCheck;
    }
    /**
     * Getter for the game speed.
     *
     * @return The current game speed.
     */
    public static int getSpeed() {
        return speed;
    }
    /**
     * Getter for the game speed.
     *
     * @return speed The current game speed.
     */
    public static void setSpeed(int speed) {
        ShadowDance.speed = speed;
    }
    /**
     * Getter for the list of notes in the game.
     *
     * @return notes A list of Note objects.
     */
    public static List<Note> getNotes() {
        return notes;
    }

    public ShadowDance(){

        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        titleFont = new Font("res/FSO8BITR.ttf", 64);
        instructionFont = new Font("res/FSO8BITR.ttf", 24);
        guardian = new Guardian();

    }
    /**
     * Getter for isGameOver, check if the game is over.
     *
     * @return isGameOver A boolean.
     */
    public static boolean getIsGameOver() {
        return isGameOver;
    }
    /**
     * Getter for DoubleScore, get the list.
     *
     * @return doubleScore A list of doubleScore values.
     */
    public static List<Integer> getDoubleScore() {
        return doubleScore;
    }
    /**
     * Setter for DoubleScore, get the list.
     *
     * @param doubleScore A list of doubleScore values.
     */
    public static void setDoubleScore(List<Integer> doubleScore) {
        ShadowDance.doubleScore = doubleScore;
    }
    /**
     * Getter for the game start status.
     *
     * @return True if the game has started, false otherwise.
     */
    public static boolean isIsGameStarted() {
        return isGameStarted;
    }

    /**
     * Getter for the list of lanes in the game.
     *
     * @return lanes A list of Lane objects.
     */
    public static List<Lane> getLanes() {
        return lanes;
    }
    /**
     * Getter for the list of enemies in the game.
     *
     * @return A list of Enemy objects.
     */
    public static List<Enemy> getEnemies() {
        return enemies;
    }


    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     */


    private List<GameElement> loadGameElements(String csvFileName) {
        List<GameElement> elements = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(LANE)) {
                    elements.add(new Lane(parts[1], Double.parseDouble(parts[2])));
                    lanes.add(new Lane(parts[1], Double.parseDouble(parts[2])));
                }else{
                    elements.add(new Note(parts[0], parts[1], Integer.parseInt(parts[2])));
                    notes.add(new Note(parts[0], parts[1], Integer.parseInt(parts[2])));


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return elements;
    }

    private int countDistinctLaneTypes() {
        List<String> distinctLaneTypes = lanes.stream()
                .map(Lane::getLaneType)
                .distinct()
                .collect(Collectors.toList());

        return distinctLaneTypes.size();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!isGameStarted && (input.wasPressed(Keys.NUM_1) || input.wasPressed(Keys.NUM_2) || input.wasPressed(Keys.NUM_3))) {
            if (input.wasPressed(Keys.NUM_1)){
                currentTrack = new Track("res/track1.wav");
                level = 1;
                gameElements = loadGameElements(csvFileNames[0]);
            }else if (input.wasPressed(Keys.NUM_2)){
                currentTrack = new Track("res/track2.wav");
                level = 2;
                gameElements = loadGameElements(csvFileNames[1]);
            }else if (input.wasPressed(Keys.NUM_3)){
                level = 3;
                gameElements = loadGameElements(csvFileNames[2]);
                currentTrack = new Track("res/track3.wav");
            }
            currentTrack.start();


        }

        if (!isGameStarted){
            int TITLE_X = 220;
            int TITLE_Y = 250;
            titleFont.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            int instructionX = TITLE_X + 100;
            int instructionY = TITLE_Y + 190;
            instructionFont.drawString("SELECT LEVELS WITH", instructionX, instructionY);
            instructionFont.drawString("NUMBER KEYS", instructionX, instructionY + 40);
            instructionFont.drawString("1    2    3", instructionX+85, instructionY + 100);
            if (input.wasPressed(Keys.NUM_1) || input.wasPressed(Keys.NUM_2) || input.wasPressed(Keys.NUM_3)){
                isGameStarted = true;
            }
        }else {
            /* Game has started */
            updateGameElements(input);
            checkWinLoseConditions();
        }

        if (isGameOver) {
            /* only print win/lose words after game over and let score disappear */

            if (level == 1){
                targetScore = 150;
            }else if (level == 2){
                targetScore = 400;
            }else if (level == 3){
                targetScore = 350;
            }

            if (Note.getPlayerScore() >= targetScore) {
                String text = CLEAR;
                double textWidth = titleFont.getWidth(text);
                titleFont.drawString(text, CENTER_X - textWidth / 2, 300);
            } else {
                String text = TRY_AGAIN;
                double textWidth = titleFont.getWidth(text);
                titleFont.drawString(text, CENTER_X - textWidth / 2, 300);
            }String instrcution = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
            double instrcutionWidth = instructionFont.getWidth(instrcution);
            instructionFont.drawString(instrcution, CENTER_X - instrcutionWidth / 2, 500);


        }
        if (isGameOver && input.wasPressed(Keys.SPACE)){
            isGameOver = false;
            Note.setPlayerScore(0);
            isGameStarted = false;
            lanes.clear();
        }

    }

    private void checkWinLoseConditions() {
        if (counter == (notes.size())) {
            isGameOver = true;
        }
    }

    // update everything hear(notes, lanes...)
    // also add elements
    // all adding are done in ShadowDance
    private void updateGameElements(Input input) {
        // put all doubleScore in an arraylist in case two are activated at the samwe time
        for (Integer score:doubleScore){
            score--;
            if (doubleScoreFrameCount < 0){
                doubleScore.remove(score);
            }

        }
        if (doubleScore.isEmpty()){
            doubleScoreCheck = 1;
        }else{
            doubleScoreCheck = 2 * doubleScore.size();
        }

        if (level == 3){
            enemyFrameCounter ++;
            if (enemyFrameCounter == ENEMYFRAME){
                Random random = new Random();
                enemies.add(new Enemy(random.nextDouble() * (RX - LXY) + 100, random.nextDouble() * (UPX - LXY) + 100));
                enemyFrameCounter = 0;
            }
            for (Enemy enemy : enemies) {
                enemy.update(input);
            }


            guardian.update(input);

            if (input.wasPressed(Keys.LEFT_SHIFT) && !enemies.isEmpty() && !isGameOver) {
                // create new enemy
                for (Enemy enemy : enemies) {
                    double distance = Math.sqrt((enemy.getX() - ORIGINX)*(enemy.getX() - ORIGINX) + (enemy.getY() - ORIGINY)*(enemy.getY() - ORIGINY));
                    if (shortestDistance == 0){
                        shortestDistance = distance;
                        targetPosition = new Vector2(enemy.getX(), enemy.getY());
                    } else if (distance < shortestDistance) {
                        targetPosition = new Vector2(enemy.getX(), enemy.getY());
                        shortestDistance = distance;
                    }
                }
                shortestDistance = 0;
                Projectile projectile = new Projectile(START, targetPosition);
                projectiles.add(projectile);

            }
            for (Projectile projectile : projectiles) {
                projectile.update(input);
            }
        }


        for (Lane lane : lanes){
            lane.update(input);
        }
        if (isGameStarted) {
            /* check every note in one frame through the for loop
             * counter is used for identify which note should be applied in one frame
             * when there is input
             * if an input is applied to the note
             * counter ++ to set the next note to be the only one that can apply input
             * shouldBeRemoved checks if current note has been applied any input
             */
            for (int i  = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                if (i == counter){
                    int numberOfDistinctLaneTypes = countDistinctLaneTypes();
                    // special notes can be ignored sometimes, can check with notes together
                    if(notes.get(i).getLaneType().equals(SPECIAL)){
                        note.setKeyPressed(false);
                        i++;
                    }

                    /* in case there are notes that have the same frame*/
                    for (int j = i; j < (numberOfDistinctLaneTypes + i) && j < notes.size(); j++){
                        Note nextNote = notes.get(j);
                        if (nextNote.getY() == note.getY()){
                            nextNote.setKeyPressed(false);
                            sameFrameCounter ++;
                        }
                    }
                    note.setKeyPressed(false);
                    i += sameFrameCounter;
                    sameFrameCounter = 0;
                }else {
                    note.setKeyPressed(true);
                }

            }
            for (int i  = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                note.update(input);
            }

            if (counter < notes.size() && notes.get(counter).shouldBeRemoved()){
                counter++;
            }
            for (Lane lane : lanes){
                lane.update(input);
            }
            if (!isGameOver){
                Font scoreFont = new Font("res/FSO8BITR.ttf", 30);
                scoreFont.drawString("SCORE " + Note.getPlayerScore(), 35, 35);
            }

        }
    }




}
