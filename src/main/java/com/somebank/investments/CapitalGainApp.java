package com.somebank.investments;


import com.somebank.investments.entrypoints.CapitalGainOperationsProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CapitalGainApp {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            StringBuilder inputLines = new StringBuilder();
            String line;
            while ( (line = reader.readLine()) != null && !line.isEmpty() ) {
                inputLines.append(line).append("\n");
            }
            CapitalGainOperationsProcessor processor = new CapitalGainOperationsProcessor();
            System.out.println(processor.process(inputLines.toString().trim()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}