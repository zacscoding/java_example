package demo;

/**
 * @author zacconding
 * @Date 2018-08-19
 * @GitHub : https://github.com/zacscoding
 */
public interface CalculatorService {

    double add(double input1, double input2);

    double subtract(double input1, double input2);

    double multiply(double input1, double input2);

    double divide(double input1, double input2);

    void serviceUsed();
}