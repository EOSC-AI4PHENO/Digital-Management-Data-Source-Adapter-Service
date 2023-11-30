package com.siseth.adapter.export.service;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.export.service.entity.ImageDataAndAnalysis;
import com.siseth.adapter.export.service.entity.PhotoDataExcelObject;
import com.siseth.adapter.feign.analysis.dto.AnalysisParameterResDTO;
import com.siseth.adapter.feign.analysis.dto.AnalysisResDTO;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class ExcelPhotoDataGenerator {

    public File generatePhotoRaportExcel(AnalysisResDTO analysis, ImageSource imageSource, PhotoDataExcelObject photoDataExcelObject, List<AnalysisParameterResDTO> parameters) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Image soruce data");
            int rowNum = 0;
            // Create the first table (header for ImageSource table)
            Row firstTableHeaderRow = sheet.createRow(rowNum);
            String[] firstTableHeaders = {"Name", "IP", "PORT", "STATION ID", "Latitude", "Longitude", "Algorithm name", "Description"};
            for (int i = 0; i < firstTableHeaders.length; i++) {
                Cell cell = firstTableHeaderRow.createCell(i);
                cell.setCellValue(firstTableHeaders[i]);
                cell.setCellStyle(getHeaderCellStyle(workbook));
            }
            rowNum++;
            // Create line for ImageSource table
            Row currentRow = sheet.createRow(rowNum);
            String[] firstTableDataRow = getFirstTableHeaderData(imageSource,analysis);
            for (int i = 0; i < firstTableDataRow.length; i++) {
                Cell cell = currentRow.createCell(i);
                cell.setCellValue(firstTableDataRow[i]);
            }
            rowNum++;
            rowNum++;
            // Create the first table (header for photo data table)
            Row secondTableHeaderRow = sheet.createRow(rowNum);
            String[] secondTableHeaders = getSecondTableHeaderData(parameters);
            for (int i = 0; i < secondTableHeaders.length; i++) {
                Cell cell = secondTableHeaderRow.createCell(i);
                cell.setCellValue(secondTableHeaders[i]);
                cell.setCellStyle(getBoldCellStyle(workbook));
            }
            rowNum++;
            List<ImageDataAndAnalysis> imageDataAndAnalyses = photoDataExcelObject.getImageDataAndAnalyses();
            for (ImageDataAndAnalysis imageDataAndAnalysis: imageDataAndAnalyses) {
                currentRow = sheet.createRow(rowNum);
                String[] secondTableDataRow = imageDataAndAnalysis.getDataRow(parameters);
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
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            workbook.write(outputStream);
//            workbook.close();
//
//            return outputStream.toByteArray();
            return createFileByWorkBook(workbook, UUID.randomUUID().toString());
        } catch (Exception e) {
            System.err.println("Error occurred while generating Excel: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error occurred while generating the document - please contact the helpdesk.");
        }
    }

    @SneakyThrows
    private File createFileByWorkBook(Workbook workbook, String fileName){
        File file =  File.createTempFile(fileName,".xlsx");
        file.deleteOnExit();
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return file;
    }

    private String[] getFirstTableHeaderData(ImageSource imageSource, AnalysisResDTO analysis) {
        String[] imageSourceDataRowPart = imageSource.getDataRow();
        String[] analysisDataRowPart = analysis.getDataRow();

        String[] result = new String[imageSourceDataRowPart.length + analysisDataRowPart.length];

        System.arraycopy(imageSourceDataRowPart, 0, result, 0, imageSourceDataRowPart.length);
        System.arraycopy(analysisDataRowPart, 0, result, imageSourceDataRowPart.length, analysisDataRowPart.length);
        return result;
    }


    private String[] getSecondTableHeaderData( List<AnalysisParameterResDTO> parameters) {
        String[] staticPartOfHeadersForPhotoTable ={"Source Id", "Photo Id", "Photo name", "Directory", "Date of Picture"};
        String[] parametersPartOfHeadersForPhotoTable = parameters.stream().map(AnalysisParameterResDTO::getDesc).toArray(String[]::new);

        int combinedLength = staticPartOfHeadersForPhotoTable.length + parametersPartOfHeadersForPhotoTable.length;
        String[] combinedArray = new String[combinedLength];
        System.arraycopy(staticPartOfHeadersForPhotoTable, 0, combinedArray, 0, staticPartOfHeadersForPhotoTable.length);
        System.arraycopy(parametersPartOfHeadersForPhotoTable, 0, combinedArray, staticPartOfHeadersForPhotoTable.length, parametersPartOfHeadersForPhotoTable.length);

        return combinedArray;
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
