package com.somebank.investments.entrypoints.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.somebank.investments.entities.OperationType;

import java.io.IOException;

public class OperationTypeAdapter extends TypeAdapter<OperationType> {
    @Override
    public void write(JsonWriter out, OperationType operationType) throws IOException {
        out.value(operationType.getValue());
    }

    @Override
    public OperationType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        return OperationType.fromString(in.nextString());
    }
}