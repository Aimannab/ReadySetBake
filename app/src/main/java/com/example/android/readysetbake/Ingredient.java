package com.example.android.readysetbake;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aiman Nabeel on 04/06/2018.
 */

/**************************************************************************************************************
 *    This code has been adapted from the following source:
 *    Title: Udacity-Advanced-Developer-Nanodegree-Baking-App-2017
 *    Author: nikosvaggalis
 *    Date: 2017
 *    Code version: N/A
 *    Availability: https://github.com/nikosvaggalis/Udacity-Advanced-Developer-Nanodegree-Baking-App-2017.git
 **************************************************************************************************************/

public class Ingredient implements Parcelable {

    private Double quantity;
    private String measure;
    private String ingredient;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    protected Ingredient(Parcel in) {
        quantity = in.readByte() == 0x00 ? null : in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(quantity);
        }
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
