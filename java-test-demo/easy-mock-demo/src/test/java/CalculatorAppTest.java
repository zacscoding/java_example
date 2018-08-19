import static org.junit.Assert.assertTrue;

import demo.CalculatorApp;
import demo.CalculatorService;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author zacconding
 * @Date 2018-08-19
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(EasyMockRunner.class)
public class CalculatorAppTest {

    // @TestSubject annotation is used to identify class which is going to use the mock object
    @TestSubject
    CalculatorApp calcApp = new CalculatorApp();

    //@Mock annotation is used to create the mock object to be injected
    @Mock
    CalculatorService calculatorService;

    @Before
    public void setUp() {
        //add the behavior of calc service to add | subtract | multiply | divide two numbers
        EasyMock.expect(calculatorService.add(20.0, 10.0)).andReturn(30.0D);
        EasyMock.expect(calculatorService.subtract(20.0, 10.0)).andReturn(10.0D);
        EasyMock.expect(calculatorService.multiply(20.0, 10.0)).andReturn(200.0D);
        EasyMock.expect(calculatorService.divide(20.0, 10.0)).andReturn(2.0D);

        //activate the mock
        EasyMock.replay(calculatorService);
    }

    @Test
    public void testAdd() {
        assertTrue("Failed to add", calcApp.add(20.0, 10.0) == 30.0D);
    }

    @Test
    public void testSubtract() {
        assertTrue("Failed to subtract", calcApp.subtract(20.0, 10.0) == 10.0D);
    }

    @Test
    public void testMultiply() {
        assertTrue("Failed to multiply", calcApp.multiply(20.0, 10.0) == 200.0D);
    }

    @Test
    public void testDivide() {
        assertTrue("Failed to divide", calcApp.divide(20.0, 10.0) == 2.0D);
    }
}
