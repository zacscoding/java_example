package basic;

import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-19
 * @GitHub : https://github.com/zacscoding
 */
public class KakaoPayCreditCardProcessor implements CreditCardProcessor {

    public KakaoPayCreditCardProcessor() {
        SimpleLogger.println("KakaoPayCreditCardProcessor is created : {}", this);
    }

}
