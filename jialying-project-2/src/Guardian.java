import bagel.Image;
import bagel.Input;
/**
 * Represents the guardian in the game.
 */
public class Guardian extends GameElement{
    private final Image GUARDIANIMAGE = new Image("res/guardian.png");
    private double x;
    private double y;
    private double FIXEDX = 800;
    private double FIXEDY = 600;
    /**
     * Creates a new guardian.
     */
    public Guardian(){
        this.x = FIXEDX;
        this.y = FIXEDY;
    }
    /**
     * Updates the guardian's state and appearance.
     *
     * @param input The input object for user interactions.
     */
    public void update(Input input) {
        if (!ShadowDance.getIsGameOver()){
            GUARDIANIMAGE.draw(FIXEDX, FIXEDY);
        }

    }
}