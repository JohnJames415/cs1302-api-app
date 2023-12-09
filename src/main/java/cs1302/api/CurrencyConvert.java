package cs1302.api;

import com.google.gson.annotations.SerializedName;

public class CurrencyConvert {
    @SerializedName("old_amount")
    double oldAmount;

    @SerializedName("old_currency")
    String oldCurrency;

    @SerializedName("new_currency")
    String newCurrency;

    @SerializedName("new_amount")
    double newAmount;

    String error;
}
