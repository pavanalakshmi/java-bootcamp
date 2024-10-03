package test;

import basicspractice.Calculator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalculatorTest {
    private Calculator calculator;

    @Before
    public void setUp(){
        calculator = new Calculator();
    }
    @Test
    public void testAdd(){
        int result = calculator.add(2,3);
        assertEquals(5, result);
        assertTrue(5==result);
    }
}
