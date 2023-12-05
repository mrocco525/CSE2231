import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * JUnit test fixture for {@code ExpressionEvaluator}'s {@code valueOfExpr}
 * static method.
 *
 * @author Put your name here
 *
 */
public final class ExpressionEvaluatorTest {

    @Test
    public void testExample() {
        StringBuilder exp = new StringBuilder(
                "281/7/2-1-5*(15-(14-1))+((1))+20=30!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(30, value);
        assertEquals("=30!", exp.toString());
    }

    @Test
    public void testSimpleAdd() {
        StringBuilder exp = new StringBuilder("1+1=2!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(2, value);
        assertEquals("=2!", exp.toString());
    }

    @Test
    public void testSimpleSubtraction() {
        StringBuilder exp = new StringBuilder("2-1=1!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(1, value);
        assertEquals("=1!", exp.toString());
    }

    @Test
    public void testSimpleMultiplication() {
        StringBuilder exp = new StringBuilder("1*1=1!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(1, value);
        assertEquals("=1!", exp.toString());
    }

    @Test
    public void testSimpleDivision() {
        StringBuilder exp = new StringBuilder("2/2=1!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(1, value);
        assertEquals("=1!", exp.toString());
    }

    @Test
    public void testSimpleParenthesesMult() {
        StringBuilder exp = new StringBuilder("(1+1)*3=6!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(6, value);
        assertEquals("=6!", exp.toString());
    }

    @Test
    public void testSimpleParenthesesDiv() {
        StringBuilder exp = new StringBuilder("(6+6)/2=6!");
        int value = ExpressionEvaluator.valueOfExpr(exp);
        assertEquals(6, value);
        assertEquals("=6!", exp.toString());
    }

}
