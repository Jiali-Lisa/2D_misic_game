import bagel.Image;
import bagel.Input;
import java.util.Random;
/**
 * Represents an enemy in the game.
 */
public class Enemy extends GameElement{
    private final Image ENEMYIMAGE = new Image("res/enemy.png");

    private double x;
    private double y;
    private double COLLIDEDISTANCE = 104;

    private final int LEFTLIMIT = 100;
    private final int RIGHTLIMIT = 900;
    private final int ENEMYSPEED = 1;
    private boolean isMovingRight;
    private final String NORMAL = "Normal";
    /**
     * Creates a new enemy character at the specified coordinates.
     *
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     */
    public Enemy(double x, double y){
        Random random = new Random();
        this.x = x;
        this.y = y;
        isMovingRight = random.nextBoolean();
    }
    /**
     * Gets the x-coordinate of the enemy.
     *
     * @return The x-coordinate of the enemy.
     */
    public double getX() {
        return x;
    }
    /**
     * Gets the y-coordinate of the enemy.
     *
     * @return The y-coordinate of the enemy.
     */
    public double getY() {
        return y;
    }
    /**
     * Updates the enemy's state check collide, if collide, set checked for note to be true to let it disappear.
     *
     * @param input The input object for user interactions.
     */
    // update and check collide, if collide, set checked for note to be true to let it disappear
    public void update(Input input) {
        for (Enemy enemy: ShadowDance.getEnemies()){
            if (!ShadowDance.getIsGameOver()) {
                ENEMYIMAGE.draw(x, y);
                for (Note note: ShadowDance.getNotes()){
                    if (Math.sqrt((y - note.getY())*(y - note.getY()) + (x - note.getX())*(x - note.getX())) <= COLLIDEDISTANCE
                    && note.getNoteType().equals(NORMAL)){
                        note.setChecked(true);
                    }

                }
            }
        }

        if (isMovingRight) {
            x += ENEMYSPEED;
        } else {
            x -= ENEMYSPEED;
        }
        if (x <= LEFTLIMIT || x >= RIGHTLIMIT) {
            isMovingRight = !isMovingRight;
        }

    }
}