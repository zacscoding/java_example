package demo;

import java.util.List;

/**
 * https://www.tutorialspoint.com/easymock/easymock_first_application.htm
 */
public class Portfolio {

    private StockService stockService;
    private List<Stock> stocks;

    public double getMarketValue() {
        double marketValue = 0.0D;

        for (Stock stock : stocks) {
            marketValue += stockService.getPrice(stock) * stock.getQuantity();
        }

        return marketValue;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public StockService getStockService() {
        return stockService;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}
