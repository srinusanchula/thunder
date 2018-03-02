package com.citrix.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class InputWriter {
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter(InputReader.INPUT_CSV);
        write(fileWriter);
        fileWriter.close();
        System.out.println("Write successful.");
    }

    private static void write(Writer writer) {
        PrintWriter printWriter = new PrintWriter(writer);
        int loop = 100;
        for (int i = 1; i <= loop; i++) {
            printWriter.printf("user%d,password,%d", i, i);
            if (i < loop)
                printWriter.println();
        }
    }
}
