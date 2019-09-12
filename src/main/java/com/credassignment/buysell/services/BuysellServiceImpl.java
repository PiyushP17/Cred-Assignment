package com.credassignment.buysell.services;

import com.credassignment.buysell.exchanges.BuysellApiRequest;
import com.credassignment.buysell.exchanges.BuysellApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BuysellServiceImpl implements  BuysellService {

    // Usage of CoinDesk API
    // CoinDesk API call Method: GET
    // URI example:
    // https://api.coindesk.com/v1/bpi/historical/close.json?currency=USD&start=2019-01-01&end=2019-01-10
    // Query params for CoinDesk API: startDate, endDate
    //
    // CoinDesk API Response :
    //  {
    //    "bpi": {
    //             "2019-01-01": 3869.47,
    //             "2019-01-02": 3941.2167,
    //             "2019-01-03": 3832.155,
    //             "2019-01-04": 3863.6267,
    //             "2019-01-05": 3835.5983,
    //             "2019-01-06": 4083.165,
    //             "2019-01-07": 4041.4583,
    //             "2019-01-08": 4029.9917,
    //             "2019-01-09": 4028.2917,
    //             "2019-01-10": 3669.5825
    //          },
    //    "disclaimer": "This data was produced from the CoinDesk Bitcoin Price
    //                   Index. BPI value data returned as USD.",
    //    "time": {
    //    "updated": "Jan 11, 2019 00:03:00 UTC",
    //    "updatedISO": "2019-01-11T00:03:00+00:00"
    //    }
    //  }

    @Override
    public synchronized String callCoinDeskApi(String start_Date, String end_Date) {

        /* RestTemplate to call CoinDesk Api */
        RestTemplate restTemplate = new RestTemplate();
        String coinDeskUrl
                = "https://api.coindesk.com/v1/bpi/historical/close.json?currency=USD&start="
                + start_Date + "&end=" + end_Date;
        final String coinDeskResponse = restTemplate.getForObject(coinDeskUrl, String.class);

        /* Return String JSON Response from CoinDesk API */
        return coinDeskResponse;
    }

    @Override
    public synchronized BuysellApiResponse getApiResponse(BuysellApiRequest apiRequest) {

        BuysellApiResponse resultantResponse = new BuysellApiResponse();
        String jsonResponseCoinDesk = callCoinDeskApi(apiRequest.getStart_date(), apiRequest.getEnd_date());
        try {
            /* Convert Json to HashMap and extracting only relevant info dates and prices*/
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponseCoinDesk);
            JsonNode bpi = rootNode.path("bpi");
            Map<String,String> mapDatePrice = mapper.convertValue(bpi, Map.class);
            // Call calculateProfit() to get maximum gains and dates range.
            Map<String, String> responseResult = calculateProfit(mapDatePrice);

            resultantResponse.setMaximum_gain(responseResult.get("maximum_gain"));
            resultantResponse.setBuy_date(responseResult.get("buy_date"));
            resultantResponse.setSell_date(responseResult.get("sell_date"));

            /* Return resultant api response containing
                maximum_gain, buy_date, sell_date
             */
            return resultantResponse;

        } catch (IOException e) {
            e.printStackTrace();
            return resultantResponse;
        }
    }

    @Override
    public synchronized Map<String,String> calculateProfit(Map<String, String> mapDatePrice) {

        /* This method incorporates the logic to calculate maximum gain.
           Returns a HashMap containing the response result in the form of map.
         */
        Map<String, String> result = new HashMap<>(); // holds the final result
        Map<Integer, String> buy = new HashMap<>();  //holds the buy date
        Map<Integer, String> sell = new HashMap<>(); // holds the sell date
        Map<Integer,Double> profit = new HashMap<>(); // holds the profit for date ranges
        String buy_date = "";
        String sell_date = "";
        double minPrice = Double.MAX_VALUE;
        double maxGain = 0.0;
        Iterator resultIterator = mapDatePrice.entrySet().iterator();
        int count = 0;
        while (resultIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) resultIterator.next();
            double price = Double.parseDouble(mapElement.getValue().toString());
            if (price < minPrice) {
                minPrice = price;
                buy_date = mapElement.getKey().toString();
            } else if ((price - minPrice) > maxGain) {
                maxGain = price - minPrice;
                sell_date = mapElement.getKey().toString();
            }

            if (sell_date != "" && buy_date != "") {
                buy.put(count, buy_date);
                sell.put(count, sell_date);
                profit.put(count, (Math.round(maxGain * 10000d)/10000d));
                count++;
            }
        }

        Double maxProfit = Double.MIN_VALUE;
        /* Use SimpleDateFormat to parse buyDate and sellDate */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long daysDiff = 0L;
        long minDiffDays = Long.MAX_VALUE; // Holds min days difference between buy and sell date
        for(int var=0; var<count; var++) {
            try {
                Date buyDate = simpleDateFormat.parse(buy.get(var));
                Date sellDate = simpleDateFormat.parse(sell.get(var));
                long diff = buyDate.getTime() - sellDate.getTime();
                daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if (daysDiff < minDiffDays && maxProfit <= profit.get(var)) {
                    maxGain = profit.get(var);
                    maxProfit = maxGain;
                    buy_date = buy.get(var);
                    sell_date = sell.get(var);
                    minDiffDays = daysDiff;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        result.put("maximum_gain",String.valueOf(maxGain));
        result.put("buy_date", buy_date);
        result.put("sell_date",sell_date);

        /* Return resultant result in form of HashMap */
        return result;
    }
}
