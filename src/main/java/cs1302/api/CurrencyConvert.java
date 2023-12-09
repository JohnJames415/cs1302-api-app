package cs1302.api;

import com.google.gson.annotations.SerializedName;


/**
 * This class will be used by Gson to take the data from API calls
 * and assign it to the listed variables.
 */
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
