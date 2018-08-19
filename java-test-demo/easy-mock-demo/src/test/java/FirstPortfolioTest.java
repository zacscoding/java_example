import static org.junit.Assert.assertTrue;

import demo.Portfolio;
import demo.Stock;
import demo.StockService;
import java.util.Arrays;
import java.util.List;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-08-19
 * @GitHub : https://github.com/zacscoding
 */
public class FirstPortfolioTest {

    private Portfolio portfolio;
    private StockService stockService;

    @Before
    public void setUp() {
        this.portfolio = new Portfolio();
        this.stockService = EasyMock.createMock(StockService.class);
        this.portfolio.setStockService(stockService);
    }

    @Test
    public void testGetPrice() {
        //Creates a list of stocks to be added to the portfolio
        Stock googleStock = new Stock("1", "Google", 10);
        Stock microsoftStock = new Stock("2", "Microsoft", 100);
        List<Stock> stocks = Arrays.asList(googleStock, microsoftStock);

        //add stocks to the portfolio
        portfolio.setStocks(stocks);

        // mock the behavior of stock service to return the value of various stocks
        EasyMock.expect(stockService.getPrice(googleStock)).andReturn(50.00);
        EasyMock.expect(stockService.getPrice(microsoftStock)).andReturn(1000.00);

        // activate the mock
        EasyMock.replay(stockService);

        double marketValue = portfolio.getMarketValue();
        assertTrue(marketValue == 100500.0D);
    }
}
