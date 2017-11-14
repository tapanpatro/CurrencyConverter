package com.omniroid.currencyconverter.model;

/**
 * Created by tapanpatro on 14/11/17.
 */

public class CurrencyFirebaseData {


    String spinnerFrom;
    float spinnerFromValue;
    String spinnerTo;
    float spinnerToValue;

    public CurrencyFirebaseData() {
    }

    public CurrencyFirebaseData(String spinnerFrom, float spinnerFromValue, String spinnerTo, float spinnerToValue) {
        this.spinnerFrom = spinnerFrom;
        this.spinnerFromValue = spinnerFromValue;
        this.spinnerTo = spinnerTo;
        this.spinnerToValue = spinnerToValue;
    }


    public String getSpinnerFrom() {
        return spinnerFrom;
    }

    public void setSpinnerFrom(String spinnerFrom) {
        this.spinnerFrom = spinnerFrom;
    }

    public float getSpinnerFromValue() {
        return spinnerFromValue;
    }

    public void setSpinnerFromValue(float spinnerFromValue) {
        this.spinnerFromValue = spinnerFromValue;
    }

    public String getSpinnerTo() {
        return spinnerTo;
    }

    public void setSpinnerTo(String spinnerTo) {
        this.spinnerTo = spinnerTo;
    }

    public float getSpinnerToValue() {
        return spinnerToValue;
    }

    public void setSpinnerToValue(float spinnerToValue) {
        this.spinnerToValue = spinnerToValue;
    }
}
