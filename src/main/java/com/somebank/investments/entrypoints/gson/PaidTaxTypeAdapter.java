package com.somebank.investments.entrypoints.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.somebank.investments.entities.PaidTax;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaidTaxTypeAdapter extends TypeAdapter<PaidTax> {

    public static final int TWO_DECIMAL_PLACES = 2;

    @Override
    public void write(JsonWriter out, PaidTax paidTax) throws IOException {
        out.beginObject();
        out.name("tax");
        BigDecimal decimalTax = paidTax.tax();
        decimalTax = decimalTax.setScale(TWO_DECIMAL_PLACES, RoundingMode.HALF_UP);
        out.value(decimalTax);
        out.endObject();
    }

    @Override
    public PaidTax read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return new PaidTax(BigDecimal.valueOf(in.nextDouble()));
    }
}
