package com.credassignment.buysell.Controller;

import com.credassignment.buysell.exchanges.BuysellApiRequest;
import com.credassignment.buysell.exchanges.BuysellApiResponse;
import com.credassignment.buysell.services.BuysellService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@Scope("request")
public class BuysellController {

    public static final String CALCULATE_API = "/v1/calculate";

    @Autowired
    private BuysellService buySellService;

    // API URI: /v1/qeats
    // Method: POST
    // Request Body Format :
    //  {
    //    "start_date" : "2019-01-01",
    //    "end_date" : "2019-01-10"
    //  }
    // Success Output :
    // Output the maximum gain, buy date and sell date.
    // Response Body Contains:
    //  {
    //    "maximum_gain": 251.01,
    //    "buy_date": "2019-01-03",
    //    "sell_date": "2019-01-06"
    //  }

    /**
     * @param apiRequest - Contains API Request Body
     * @return - ResponseEntity - application/JSON Format
     */
    @PostMapping(CALCULATE_API)
    public ResponseEntity<BuysellApiResponse> getResponse(
            @RequestBody BuysellApiRequest apiRequest) {
        /* requesting service method getApiResponse */

        log.info("BuySellRequest called with {}",apiRequest);
        BuysellApiResponse apiResponse = buySellService
                .getApiResponse(apiRequest);

        /* Return response from API, application/json format */
        return ResponseEntity.ok().body(apiResponse);
    }
}
