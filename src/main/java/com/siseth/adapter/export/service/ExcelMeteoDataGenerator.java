package com.siseth.adapter.export.service;

import com.siseth.adapter.feign.weatherStation.dto.source.response.MeteoDataDTO;
import com.siseth.adapter.feign.weatherStation.dto.source.response.WeatherStationDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelMeteoDataGenerator {

    public byte[] generateMeteoRaportExcel(WeatherStationDTO weatherStationDTO, List<MeteoDataDTO> meteoDataDTOS) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Station Data");
            int rowNum = 0;
            // Create the first table (header for ImageSource table)
            Row firstTableHeaderRow = sheet.createRow(rowNum);
            String[] firstTableHeaders = {"StationId", "ObservationStationId", "Name", "TercCode",
                    "WktLocation", "Latitude", "Longitude", "OwnerId", "StationType", "Active"};
            for (int i = 0; i < firstTableHeaders.length; i++) {
                Cell cell = firstTableHeaderRow.createCell(i);
                cell.setCellValue(firstTableHeaders[i]);
                cell.setCellStyle(getHeaderCellStyle(workbook));
            }
            rowNum++;
            // Create line for ImageSource table
            Row currentRow = sheet.createRow(rowNum);
            String[] firstTableDataRow = weatherStationDTO.getDataRow();
            for (int i = 0; i < firstTableDataRow.length; i++) {
                Cell cell = currentRow.createCell(i);
                cell.setCellValue(firstTableDataRow[i]);
            }
            rowNum++;
            rowNum++;
            // Create the first table (header for Meteo table)
            Row secondTableHeaderRow = sheet.createRow(rowNum);
            String[] secondTableHeaders = {"Station Id", "Measurement Date", "Air Temperature", "Relative Humidity",
                    "Insolation", "Wind Speed", "Wind Direction", "Air Pressure", "Precipitation", "Dew Point Temperature"};
            for (int i = 0; i < secondTableHeaders.length; i++) {
                Cell cell = secondTableHeaderRow.createCell(i);
                cell.setCellValue(secondTableHeaders[i]);
                cell.setCellStyle(getBoldCellStyle(workbook));
            }
            rowNum++;
            // Create line for MeteoData table
            for (MeteoDataDTO meteoDataDTO : meteoDataDTOS) {
                currentRow = sheet.createRow(rowNum);
                String[] secondTableDataRow = meteoDataDTO.getDataRow();
                for (int i = 0; i < secondTableDataRow.length; i++) {
                    Cell cell = currentRow.createCell(i);
                    cell.setCellValue(secondTableDataRow[i]);
                }
                rowNum++;
            }

            // Auto-size columns
            for (int colIndex = 0; colIndex <= secondTableHeaders.length; colIndex++) {
                sheet.autoSizeColumn(colIndex);
            }

            // Convert the Excel workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            System.err.println("Error occurred while generating Excel: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error occurred while generating the document - please contact the helpdesk.");
        }
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle getBoldCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

}
