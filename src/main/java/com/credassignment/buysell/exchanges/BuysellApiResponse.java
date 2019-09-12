package com.credassignment.buysell.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuysellApiResponse {
    private String maximum_gain;
    private String buy_date;
    private String sell_date;
}
