package com.credassignment.buysell.services;

import com.credassignment.buysell.exchanges.BuysellApiRequest;
import com.credassignment.buysell.exchanges.BuysellApiResponse;
import java.util.Map;

public interface BuysellService {

    /**
     *
     * @param start_Date - start date provided in the POST request
     * @param end_Date - end date provided in the POST request
     * @return coinDeskResponse - JSON response returned by Coin Desk API.
     */
    String callCoinDeskApi(String start_Date, String end_Date);


    /**
     *
     * @param apiRequest - Request Body containing start_date and end_date
     * @return - BuysellApiResponse - Response containing maximum_gain,
     *                                buy_date and sell_date.
     */
    BuysellApiResponse getApiResponse(BuysellApiRequest apiRequest);


    /**
     *
     * @param mapDatePrice - HashMap containing dates as key and prices as values {date:Price}
     * @return - resultant response in form of HashMap.
     */
    Map<String,String> calculateProfit(Map<String, String> mapDatePrice);
}
