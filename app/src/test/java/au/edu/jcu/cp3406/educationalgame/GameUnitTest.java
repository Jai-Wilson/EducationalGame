package au.edu.jcu.cp3406.educationalgame;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGame(){
        Game game = new Game();
        int index = 0;
        String question = game.getQuestion(index);
        String answer = game.getAnswer(index);

        assertEquals("Chemical formula for Sodium Chloride?", question);
        assertEquals("NaCl", answer);

    }
}