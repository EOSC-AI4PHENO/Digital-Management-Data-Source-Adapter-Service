package com.siseth.adapter.export.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class JsonToExcelConverter {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Method to convert json Data to excel file
     *
     * @param jsonData - jsonData in node form
     * @return file
     */
    public ByteArrayOutputStream jsonDataToExcelFile(JsonNode jsonData) {
        try {
            Workbook workbook = new XSSFWorkbook();

            //Iterating over the each sheets
            Iterator<String> sheetItr = jsonData.fieldNames();
            while (sheetItr.hasNext()) {

                // create the workbook sheet
                String sheetName = sheetItr.next();
                Sheet sheet = workbook.createSheet(sheetName);

                ArrayNode sheetData = (ArrayNode) jsonData.get(sheetName);
                ArrayList<String> headers = new ArrayList<>();

                //Creating cell style for header to make it bold
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);

                //creating the header into the sheet
                Row header = sheet.createRow(0);
                if (sheetData.get(0) != null) {
                    Iterator<String> it = sheetData.get(0).fieldNames();
                    int headerIdx = 0;
                    while (it.hasNext()) {
                        String headerName = it.next();
                        headers.add(headerName);
                        Cell cell = header.createCell(headerIdx++);
                        cell.setCellValue(headerName);
                        //apply the bold style to headers
                        cell.setCellStyle(headerStyle);
                    }


                    //Iterating over the each row data and writing into the sheet
                    for (int i = 0; i < sheetData.size(); i++) {
                        ObjectNode rowData = (ObjectNode) sheetData.get(i);
                        Row row = sheet.createRow(i + 1);
                        for (int j = 0; j < headers.size(); j++) {
                            String value = rowData.get(headers.get(j)).asText();
                            row.createCell(j).setCellValue(value);
                        }
                    }

                    /*
                     * automatic adjust data in column using autoSizeColumn, autoSizeColumn should
                     * be made after populating the data into the excel. Calling before populating
                     * data will not have any effect.
                     */
                    for (int i = 0; i < headers.size(); i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
            }
            /* close the workbook and bos */
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                workbook.write(bos);
            } finally {
                bos.close();
                workbook.close();
            }
            return bos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public JsonNode getJsonNodeFromString(String jsonString) {
        try {
            return mapper.readValue(jsonString, JsonNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}