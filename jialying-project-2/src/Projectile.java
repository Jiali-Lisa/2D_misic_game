import bagel.*;
import bagel.util.Vector2;
import java.lang.Math;
import java.util.List;


/**
 * Represents a projectile in the game.
 */
public class Projectile extends GameElement {
    private final Image PROJECTILEIMAGE = new Image("res/arrow.png");


    private double x, y;
    private boolean isActive = true;
    private static final double SPEED = 6.0;
    private Vector2 position;
    private Vector2 target;
    private Vector2 direction;
    private final int COLLIDE = 62;

    private final double ORIGINX = 800;
    private final double ORIGINY = 600;
    /**
     * Creates a new projectile.
     *
     * @param position The initial position of the projectile.
     * @param target   The target position for the projectile.
     */
    public Projectile(Vector2 position, Vector2 target) {
        this.position = position;
        this.target = target;

    }
    private double magnitude;
    private double unitX;
    private double unitY = y / magnitude;
    /**
     * Updates the projectile's position and checks for collisions.
     *
     * @param input The input object for user interactions.
     */
    public void update(Input input) {
        if (!(position.x < 0 || position.x > Window.getWidth() || position.y < 0 || position.y > Window.getHeight())
                && isActive
                && !collidesWith(ShadowDance.getEnemies())){
            x = position.x;
            y = position.y;
            if (!ShadowDance.getIsGameOver()){
                if (target.x - ORIGINX > 0){
                    PROJECTILEIMAGE.draw(x, y, new DrawOptions().setRotation(Math.toRadians(270 - Math.toDegrees(Math.atan2((target.x - ORIGINX), (target.y - ORIGINY)))+180)));

                }else{
                    PROJECTILEIMAGE.draw(x, y, new DrawOptions().setRotation(Math.toRadians(-180-(Math.toDegrees(Math.atan2((target.x - ORIGINX), (target.y - ORIGINY)))+90))));
                }
            }
            position = calculateVelocity(position.x, position.y);
        }else {
            isActive = false;
        }
    }
    /**
     * Checks for collisions between the projectile and enemies.
     *
     * @param enemies The list of enemies to check for collisions with.
     * @return True if a collision occurred, false otherwise.
     */
    public boolean collidesWith(List<Enemy> enemies) {
        for (Enemy enemy : enemies){
            if (Math.sqrt((enemy.getX() - position.x)*(enemy.getX() - position.x) + (enemy.getY() - position.y)*(enemy.getY() - position.y)) <= COLLIDE){
                enemies.remove(enemy);
                isActive = false;
                return true;
            }
        }
        return false;
    }

    //calculate the next position
    private Vector2 calculateVelocity(double startX, double startY) {
        direction = new Vector2(target.x - ORIGINX, target.y - ORIGINY);
        magnitude = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        unitX = Math.abs(direction.x / magnitude);
        unitY = Math.abs(direction.y / magnitude);
        double velocityX;
        double velocityY = startY - SPEED * unitY;
        if (direction.x > 0){
            velocityX = startX + SPEED * unitX;
        }else{
            velocityX = startX - SPEED * unitX;
        }
        return new Vector2(velocityX, velocityY);
    }
}