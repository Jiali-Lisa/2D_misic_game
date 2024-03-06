import bagel.Image;
import bagel.Input;
/**
 * Represents a lane in the ShadowDance game.
 * Lanes are used to organize and display notes for the player to interact with.
 */
public class Lane extends GameElement {
    private String laneType;
    private Image laneImage;
    private double x;
    private double y;
    private final int YCOORDINATE = 384;
    private final String RIGHT = "Right";
    private final String LEFT = "Left";
    private final String UP = "Up";
    private final String SPECIAL = "Special";
    private final String DOWN = "Down";
    /**
     * Get the type of the lane.
     *
     * @return The lane type.
     */
    public String getLaneType() {
        return laneType;
    }
    /**
     * Get the X-coordinate of the lane.
     *
     * @return The X-coordinate.
     */
    public double getX() {
        return x;
    }
    /**
     * Create a new Lane object with the specified lane type and X-coordinate.
     *
     * @param laneType The type of the lane (e.g., Right, Left, Up, Down, Special).
     * @param x        The X-coordinate of the lane.
     */
    public Lane(String laneType, double x) {
        this.laneType = laneType;
        // Initialize lane image based on laneType
        if (LEFT.equals(laneType)) {
            laneImage = new Image("res/laneLeft.png");
        } else if (RIGHT.equals(laneType)) {
            laneImage = new Image("res/laneRight.png");
        } else if (UP.equals(laneType)) {
            laneImage = new Image("res/laneUp.png");
        } else if (DOWN.equals(laneType)) {
            laneImage = new Image("res/laneDown.png");
        }else if (SPECIAL.equals(laneType)) {
            laneImage = new Image("res/laneSpecial.png");
        }
        this.x = x;
        this.y = YCOORDINATE;

    }
    /**
     * Update the state of the lane, including drawing it on the screen if the game is not over.
     *
     * @param input The input object for user interactions.
     */
    public void update(Input input){
        // only draw lane when game is not over
        if (!ShadowDance.getIsGameOver()){
            laneImage.draw(x, y);
        }
    }

}
