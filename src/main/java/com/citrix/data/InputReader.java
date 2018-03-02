package com.citrix.data;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InputReader {
    public static final String INPUT_CSV = "./src/main/resources/input.csv";

    public static List<Input> read() {
        List<Input> inputList = null;

        try {
            MappingIterator<Input> inputMappingIterator = new CsvMapper().readerWithTypedSchemaFor(Input.class).readValues(new File(INPUT_CSV));
            inputList = inputMappingIterator.readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputList;
    }
}
