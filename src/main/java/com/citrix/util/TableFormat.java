package com.citrix.util;

import java.util.ArrayList;
import java.util.List;

public class TableFormat {

    private final StringBuilder banner;
    private final List<String> headers;
    private final List<List<String>> table;
    private final List<Integer> maxLength;
    private int rows, cols;

    /*
     * Constructor that needs two arraylist
     * 1: The headersIs is one list containing strings with the columns headers
     * 2: The content is an matrix of Strings build up with ArrayList containing the content
     *
     * Call the printTable method to print the table
     */

    public TableFormat(StringBuilder banner, List<String> headersIn, List<List<String>> content) {
        this.banner = banner;
        this.headers = headersIn;
        this.maxLength = new ArrayList<>();
        //Set headers length to maxLength at first
        for (String header : headers) {
            maxLength.add(header.length());
        }
        this.table = content;
        calcMaxLengthAll();
    }

    @Override
    public String toString() {
        //Take out the
        StringBuilder sb = new StringBuilder(banner);
        StringBuilder sbRowSep = new StringBuilder();
        StringBuilder padded = new StringBuilder();
        int rowLength = 0;
        String rowSeperator;

//        sb.append("\n");
        //Create padding string containing just containing spaces
        int TABLEPADDING = 0;
        for (int i = 0; i < TABLEPADDING; i++) {
            padded.append(" ");
        }

        //Create the rowSeperator
        for (Integer aMaxLength : maxLength) {
            sbRowSep.append("|");
            for (int j = 0; j < aMaxLength + (TABLEPADDING * 2); j++) {
                char SEPARATOR_CHAR = '-';
                sbRowSep.append(SEPARATOR_CHAR);
            }
        }
        sbRowSep.append("|");
        rowSeperator = sbRowSep.toString();

        sb.append(rowSeperator);
        sb.append("\n");
        //HEADERS
        sb.append("|");
        for (int i = 0; i < headers.size(); i++) {
            sb.append(padded);
            sb.append(headers.get(i));
            //Fill up with empty spaces
            for (int k = 0; k < (maxLength.get(i) - headers.get(i).length()); k++) {
                sb.append(" ");
            }
            sb.append(padded);
            sb.append("|");
        }
        sb.append("\n");
        sb.append(rowSeperator);
        sb.append("\n");

        //BODY
        for (List<String> tempRow : table) {
            //New row
            sb.append("|");
            for (int j = 0; j < tempRow.size(); j++) {
                sb.append(padded);
                sb.append(tempRow.get(j));
                //Fill up with empty spaces
                for (int k = 0; k < (maxLength.get(j) - tempRow.get(j).length()); k++) {
                    sb.append(" ");
                }
                sb.append(padded);
                sb.append("|");
            }
            sb.append("\n");
            sb.append(rowSeperator);
            sb.append("\n");
        }

        return sb.toString();
    }

    /*
     * Fills maxLength with the length of the longest word
     * in each column
     *
     * This will only be used if the user don't send any data
     * in first init
     */
    private void calcMaxLengthAll() {
        for (List<String> temp : table) {
            for (int j = 0; j < temp.size(); j++) {
                //If the table content was longer then current maxLength - update it
                if (temp.get(j).length() > maxLength.get(j)) {
                    maxLength.set(j, temp.get(j).length());
                }
            }
        }
    }
}