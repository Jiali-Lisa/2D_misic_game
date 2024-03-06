import bagel.*;

import java.util.ArrayList;
/**
 * Represents a note in the game.
 */

public class Note extends GameElement {
    private final int CENTER_X = Window.getWidth() / 2;
    private final int CENTER_Y = Window.getHeight() / 2;
    private final double STATIONARY_NOTE_Y = 657;
    private final double NOTE_SPEED = 2.0;
    private final double STARTING_NOTE_Y = 100;
    private final double HOLD_STARTING_NOTE_Y = 24;
    private final int WINDOW_HEIGHT = 768;
    private static int playerScore = 0;

    private Image noteImage;
    private final double x;
    private double y;
    private int score = 0;
    private int scoreFrameCount = 0;
    private final int LASTINGTIME = 480;

    private String scoreMessage = "";

    private String laneType;
    private String noteType;

    private boolean isHold = false;

    private boolean holdStarted = false;

    private boolean checked = false;
    private boolean keyPressed = false;
    private boolean activated = false;

    private final int SPEEDUP = 1;
    private final int SLOWDOWN = -1;

    private final int SCOREFRAME = 30;
    private final int DISTANCE15 = 15;
    private final int DISTANCE50 = 50;
    private final int DISTANCE100 = 100;
    private final int DISTANCE200 = 200;
    private double noteSpeed = NOTE_SPEED;
    private final String NORMAL = "Normal";
    private final String HOLD = "Hold";
    private final String SPEED_UP = "SpeedUp";
    private final String SLOW_DOWN = "SlowDown";
    private final String DOUBLE_SCORE = "DoubleScore";
    private final String BOMB = "Bomb";
    private final String RIGHT = "Right";
    private final String LEFT = "Left";
    private final String UP = "Up";
    private final String SPECIAL = "Special";
    private final String DOWN = "Down";
    private final String PERFECT = "PERFECT";
    private final String GOOD = "GOOD";
    private final String BAD = "BAD";
    private final String MISS = "MISS";
    private final String DOUBLE_SCORE_MESSAGE = "DOUBLE SCORE";
    private final String SPEED_UP_MESSAGE = "SPEED UP";
    private final String SLOW_DOWN_MESSAGE = "SLOW DOWN";
    /**
     * Gets the type of the note.
     *
     * @return noteType The type of the note.
     */
    public String getNoteType() {
        return noteType;
    }
    /**
     * Gets the score message associated with the note.
     *
     * @return scoreMessage The score message.
     */
    public String getScoreMessage() {
        return scoreMessage;
    }
    /**
     * Gets the y-coordinate of the note.
     *
     * @return The y-coordinate of the note.
     */
    public double getY() {
        return y;
    }
    /**
     * Gets the x-coordinate of the note.
     *
     * @return The x-coordinate of the note.
     */
    public double getX() {
        return x;
    }
    /**
     * Sets whether the note is checked for removal.
     *
     * @param checked True if the note is checked, false otherwise.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    /**
     * Sets whether a key was pressed for the note.
     *
     * @param keyPressed True if a key was pressed, false otherwise.
     */
    public void setKeyPressed(boolean keyPressed) {
        this.keyPressed = keyPressed;
    }
    /**
     * Sets the score message associated with the note.
     *
     * @param scoreMessage The new score message.
     */
    public void setScoreMessage(String scoreMessage) {
        this.scoreMessage = scoreMessage;
    }
    /**
     * Gets the player's current score.
     *
     * @return The player's score.
     */
    public static int getPlayerScore() {
        return playerScore;
    }
    /**
     * Sets the player's score.
     *
     * @param playerScore The new player's score.
     */
    public static void setPlayerScore(int playerScore) {
        Note.playerScore = playerScore;
    }
    /**
     * Checks if the note should be removed.
     *
     * @return True if the note should be removed, false otherwise.
     */
    public boolean shouldBeRemoved() {
        return checked;
    }
    /**
     * Gets the type of lane the note is in.
     *
     * @return The type of lane.
     */
    public String getLaneType() {
        return laneType;
    }

    /**
     * Creates a new note with the specified lane type, note type, and frame number.
     *
     * @param laneType    The type of lane.
     * @param noteType    The type of note.
     * @param frameNumber The frame number.
     */
    public Note(String laneType, String noteType, int frameNumber) {
        this.laneType = laneType;
        this.noteType = noteType;
        if (NORMAL.equals(noteType)) {
            noteImage = new Image("res/note" + laneType + ".png");
            y = -(frameNumber * NOTE_SPEED - STARTING_NOTE_Y);
        } else if (HOLD.equals(noteType)) {
            noteImage = new Image("res/holdNote" + laneType + ".png");
            y = -(frameNumber * NOTE_SPEED - HOLD_STARTING_NOTE_Y);
            isHold = true;
        }else if (SPEED_UP.equals(noteType)){
            noteImage = new Image("res/noteSpeedUp.png");
            y = -(frameNumber * NOTE_SPEED - STARTING_NOTE_Y);

        }else if (SLOW_DOWN.equals(noteType)){
            noteImage = new Image("res/noteSlowDown.png");
            y = -(frameNumber * NOTE_SPEED - STARTING_NOTE_Y);

        }else if (DOUBLE_SCORE.equals(noteType)){
            noteImage = new Image("res/note2x.png");
            y = -(frameNumber * NOTE_SPEED - STARTING_NOTE_Y);

        }else if (BOMB.equals(noteType)){
            noteImage = new Image("res/noteBomb.png");
            y = -(frameNumber * NOTE_SPEED - STARTING_NOTE_Y);

        }
        x = getLaneXForNoteType(laneType);
    }

    private double getLaneXForNoteType(String laneType) {
        /* Find the matching lane and return its x-coordinate from the lanes list */
        for (Lane lane : ShadowDance.getLanes()) {
            if (lane.getLaneType().equals(laneType)) {
                return lane.getX();
            }
        }
        return 0;
    }
    /**
     * Separate note, hold note, special notes
     * For special notes, effects are applied to all so variables like doubleFrameScoreCheck are in ShadowDance
     * @param input The input object for user interactions.
     */
    public void update(Input input){
        if (ShadowDance.isIsGameStarted()) {
            y += ShadowDance.getSpeed();
            if (!checked){
                // check if a note has been applied with input
                if (!keyPressed) {
                    //check if input has been applied in one frame
                    if (!isHold) {
                        if (((laneType.equals(RIGHT) && input.wasPressed(Keys.RIGHT))
                                || (laneType.equals(LEFT) && input.wasPressed(Keys.LEFT))
                                || (laneType.equals(UP) && input.wasPressed(Keys.UP))
                                || (laneType.equals(DOWN) && input.wasPressed(Keys.DOWN)))
                                && !keyPressed) {
                            // a key was pressed and keyPressed checks it will only apply once in a frame
                            double distance = Math.abs(y - STATIONARY_NOTE_Y);
                            if (noteType.equals(BOMB)){
                                if (!activated){
                                    if (distance <= DISTANCE50) {
                                        activated = true;
                                        for (Note note : ShadowDance.getNotes()) {
                                            if (note.laneType.equals(laneType) && note.y >= STARTING_NOTE_Y) {
                                                note.checked = true;
                                            }
                                        }
                                    }
                                }checked = true;
                            }else{
                                if (distance <= DISTANCE15) {
                                    score = 10 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(PERFECT);
                                    setUp();
                                } else if (distance <= DISTANCE50) {
                                    score = 5 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(GOOD);
                                    setUp();
                                } else if (distance <= DISTANCE100) {
                                    score = -1 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(BAD);
                                    setUp();
                                } else if (distance <= DISTANCE200){
                                    score = -5 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(MISS);
                                    setUp();
                                }playerScore += score;
                            }
                        } else if (laneType.equals(SPECIAL) && input.wasReleased(Keys.SPACE)){
                            double distance = Math.abs(y - STATIONARY_NOTE_Y);
                            if (noteType.equals(BOMB) && !activated){
                                if (distance <= DISTANCE50){
                                    activated = true;
                                    checked = true;
                                    for (Note note : ShadowDance.getNotes()){
                                        if (note.laneType.equals(laneType) && note.y >= STARTING_NOTE_Y){
                                            note.checked = true;
                                            note.activated = true;
                                        }
                                    }
                                }else if (y >= WINDOW_HEIGHT + noteImage.getHeight() / 2){
                                    checked = true;
                                }
                            }else if (noteType.equals(DOUBLE_SCORE) && !activated){
                                if (distance <= DISTANCE50){
                                    ShadowDance.setDoubleScoreCheck(ShadowDance.getDoubleScoreCheck()*2);
                                    ArrayList<Integer> list = (ArrayList<Integer>) ShadowDance.getDoubleScore();
                                    list.add(LASTINGTIME);
                                    ShadowDance.setDoubleScore(list);
                                    activated = true;
                                    setUp();
                                    setScoreMessage(DOUBLE_SCORE_MESSAGE);
                                }else if (y >= WINDOW_HEIGHT + noteImage.getHeight() / 2){
                                    checked = true;
                                }
                            }else if (noteType.equals(SPEED_UP) && !activated){
                                if (distance <= DISTANCE50){
                                    ShadowDance.setSpeed(ShadowDance.getSpeed() + SPEEDUP);
                                    activated = true;
                                    setUp();
                                    setScoreMessage(SPEED_UP_MESSAGE);
                                    score += 15 * ShadowDance.getDoubleScoreCheck();
                                    playerScore += score;
                                }else if (y >= WINDOW_HEIGHT + noteImage.getHeight() / 2){
                                    checked = true;
                                }
                            }else if (noteType.equals(SLOW_DOWN) && !activated){
                                if (distance <= DISTANCE50){
                                    ShadowDance.setSpeed(ShadowDance.getSpeed() + SLOWDOWN);
                                    setUp();
                                    setScoreMessage(SLOW_DOWN_MESSAGE);
                                    score += 15 * ShadowDance.getDoubleScoreCheck();
                                    playerScore += score;
                                }else if (y >= WINDOW_HEIGHT + noteImage.getHeight() / 2){
                                    checked = true;
                                }

                            }


                        }else if (y >= WINDOW_HEIGHT + noteImage.getHeight() / 2) {
                            if (noteType.equals(NORMAL) || noteType.equals(HOLD)){
                                score = -5 * ShadowDance.getDoubleScoreCheck();
                                setScoreMessage(MISS);
                                playerScore += score;
                                scoreFrameCount = SCOREFRAME;
                            }
                            checked = true;
                        }
                        if (y < WINDOW_HEIGHT + noteImage.getHeight() / 2) {
                            noteImage.draw(x, y);
                        }
                    }else{
                        if (holdStarted) {
                            noteImage.draw(x, y);
                            // finish holding
                            if (((laneType.equals(RIGHT) && input.wasReleased(Keys.RIGHT))
                                    || (laneType.equals(LEFT) && input.wasReleased(Keys.LEFT))
                                    || (laneType.equals(UP) && input.wasReleased(Keys.UP))
                                    || (laneType.equals(DOWN) && input.wasReleased(Keys.DOWN)))
                                    && !keyPressed) {
                                double top = y - 82;
                                double distance = Math.abs(top - STATIONARY_NOTE_Y);
                                if (distance <= DISTANCE15) {
                                    score = 10 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(PERFECT);
                                } else if (distance <= DISTANCE50) {
                                    score = 5 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(GOOD);
                                } else if (distance <= DISTANCE100) {
                                    score = -1 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(BAD);
                                } else{
                                    score = -5 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(MISS);
                                }
                                playerScore += score;
                                setUp();
                                holdStarted = false;


                            } else if (y - 82 >= WINDOW_HEIGHT + noteImage.getHeight() / 2) {
                                score = -5 * ShadowDance.getDoubleScoreCheck();
                                setScoreMessage(MISS);
                                playerScore += score;
                                setUp();
                                holdStarted = false;

                            }

                        } else if (!holdStarted) {
                            // hold is coming
                            if (((laneType.equals(RIGHT) && input.wasPressed(Keys.RIGHT))
                                    || (laneType.equals(LEFT) && input.wasPressed(Keys.LEFT))
                                    || (laneType.equals(UP) && input.wasPressed(Keys.UP))
                                    || (laneType.equals(DOWN) && input.wasPressed(Keys.DOWN)))
                                    && !keyPressed) {
                                double bottom = y + 82;
                                double distance = Math.abs(bottom - STATIONARY_NOTE_Y);
                                if (distance <= DISTANCE15) {
                                    score = 10 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(PERFECT);
                                    playerScore += score;
                                    scoreFrameCount = SCOREFRAME;
                                    holdStarted = true;
                                    checked = false;
                                } else if (distance <= DISTANCE50) {
                                    score = 5 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(GOOD);
                                    playerScore += score;
                                    scoreFrameCount = SCOREFRAME;
                                    holdStarted = true;
                                    checked = false;
                                } else if (distance <= DISTANCE100) {
                                    score = -1 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(BAD);
                                    playerScore += score;
                                    scoreFrameCount = SCOREFRAME;
                                    holdStarted = true;
                                    checked = false;
                                } else if (distance <= DISTANCE200){
                                    score = -5 * ShadowDance.getDoubleScoreCheck();
                                    setScoreMessage(MISS);
                                    playerScore += score;
                                    scoreFrameCount = SCOREFRAME;
                                    holdStarted = true;
                                    checked = false;
                                }

                            } else if (y + 82 >= WINDOW_HEIGHT + noteImage.getHeight() / 2) {
                                score = -5 * ShadowDance.getDoubleScoreCheck();
                                setScoreMessage(MISS);
                                playerScore += score;
                                holdStarted = false;
                                setUp();
                            }
                            noteImage.draw(x, y);
                        }
                    }
                }else{
                    noteImage.draw(x, y);
                }

            }
            Font scoreFont = new Font("res/FSO8BITR.ttf", 40);
            double textWidth = scoreFont.getWidth(getScoreMessage());
            if (scoreFrameCount > 0 && !ShadowDance.getIsGameOver()){
                scoreFont.drawString(getScoreMessage(), CENTER_X - textWidth / 2, CENTER_Y);
                scoreFrameCount -= (int) NOTE_SPEED;
                if (!isHold){
                    checked = true;// && !(ShadowDance.getCounter() == (ShadowDance.getNotes().size()))
                }

            }else{
                setScoreMessage("");
            }
        }

    }

    /**
     * Set checked and scoreFrameCount
     */
    public void setUp(){
        checked = true;
        scoreFrameCount = SCOREFRAME;

    }
}