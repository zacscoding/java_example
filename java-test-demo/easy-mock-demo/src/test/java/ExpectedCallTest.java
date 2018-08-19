import static org.junit.Assert.assertTrue;

import demo.CalculatorService;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author zacconding
 * @Date 2018-08-19
 * @GitHub : https://github.com/zacscoding
 */
// @RunWith attaches a runner with the test class to initialize the test data
@RunWith(EasyMockRunner.class)
public class ExpectedCallTest {


    // @TestSubject annotation is used to identify class which is going to use the mock object
    @TestSubject
    MathApplication mathApplication = new MathApplication();

    // @Mock annotation is used to create the mock object to be injected
    @Mock
    CalculatorService calcService;

    @Test
    public void testAdd() {
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);
        calcService.serviceUsed();
        EasyMock.expectLastCall().times(1);
        //activate the mock
        EasyMock.replay(calcService);

        assertTrue(mathApplication.add(10.0, 20.0) == 30.0);

        //verify call to calcService is made or not
        EasyMock.verify(calcService);
    }


    public static class MathApplication {

        private CalculatorService calculatorService;

        public void setCalculatorService(CalculatorService calculatorService) {
            this.calculatorService = calculatorService;
        }

        public double add(double input1, double input2) {
            calculatorService.serviceUsed();
            return calculatorService.add(input1, input2);
        }

        public double subtract(double input1, double input2) {
            return calculatorService.subtract(input1, input2);
        }

        public double multiply(double input1, double input2) {
            return calculatorService.multiply(input1, input2);
        }

        public double divide(double input1, double input2) {
            return calculatorService.divide(input1, input2);
        }
    }
}
