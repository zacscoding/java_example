package demo;

import demo.util.GsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * https://www.tutorialspoint.com/easymock/easymock_first_application.htm
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    private String stockId;
    private String name;
    private int quantity;

    @Override
    public String toString() {
        return GsonUtil.toString(this);
    }
}
