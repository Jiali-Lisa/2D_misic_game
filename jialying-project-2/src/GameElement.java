import bagel.Input;
/**
 * Represents a game element in the ShadowDance game.
 * This is an abstract base class for various game elements such as notes, enemies, and guardians.
 */
public abstract class GameElement {
    /**
     * Update the state and behavior of the game element based on user input.
     *
     * @param input The input object for user interactions.
     */
    public abstract void update(Input input);
}