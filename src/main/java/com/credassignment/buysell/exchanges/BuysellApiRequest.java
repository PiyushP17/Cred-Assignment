package com.credassignment.buysell.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuysellApiRequest {
    @NotNull
    private String start_date;
    @NotNull
    private String end_date;
}
