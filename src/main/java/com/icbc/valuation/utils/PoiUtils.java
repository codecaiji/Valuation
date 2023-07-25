package com.icbc.valuation.utils;

import com.icbc.valuation.model.SheetData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class PoiUtils implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(PoiUtils.class);
    private static final String outputFileName = "output.xlsx";
    private Workbook workbook;

    public PoiUtils() {
        workbook = new XSSFWorkbook();
    }
    public PoiUtils(InputStream fileInputStream) throws IOException {
        workbook = new XSSFWorkbook(fileInputStream);
    }

    public List<SheetData> upload() {
        List<SheetData> uploadData = new LinkedList<>();
        for (int sheetNo = 0; sheetNo < workbook.getNumberOfSheets(); sheetNo++) {
            Sheet sheet = workbook.getSheetAt(sheetNo);
            SheetData uploadSheetData = new SheetData();

            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                Row firstRow = rowIterator.next();
                for (Cell cell : firstRow) {
                    uploadSheetData.getParamsName().add(cell.getStringCellValue());
                }
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, Object> entryScores = new HashMap<>();
                for (int cellNo = 0; cellNo < row.getLastCellNum(); cellNo++) {
                    Cell cell = row.getCell(cellNo);
                    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        row.createCell(cellNo).setCellValue(0);
                    }
                    row.getCell(cellNo).setCellType(Cell.CELL_TYPE_STRING);
                    entryScores.put(uploadSheetData.getParamsName().get(cellNo), row.getCell(cellNo)
                            .getStringCellValue());
                    //.getNumericCellValue());
                }
                uploadSheetData.getParamsScores().add(entryScores);
            }
            if (CollectionUtils.isNotEmpty(uploadSheetData.getParamsScores())) {
                uploadData.add(uploadSheetData);
            }
        }
        return uploadData;
    }

    public void output(List<SheetData> outputList) {
        try (OutputStream fileOut = new FileOutputStream(outputFileName)) {
            outputWorkbook(outputList);
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void output(HttpServletResponse response, List<SheetData> outputList) {
        try {
            outputWorkbook(outputList);
            workbook.write(response.getOutputStream());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void outputWorkbook(List<SheetData> outputList) {
        for (SheetData outputSheet : outputList) {
            List<String> outputNames = outputSheet.getParamsName();
            Sheet sheet = workbook.createSheet();
            //第一行输出属性名
            Row row = sheet.createRow(0);
            for (int outputNameNo = 0; outputNameNo < outputNames.size(); outputNameNo++) {
                Cell cell = row.createCell(outputNameNo);
                cell.setCellValue(outputNames.get(outputNameNo));
            }

            //之后输出属性值
            List<Map<String, Object>> outputScores = outputSheet.getParamsScores();
            for (int outputRowNo = 1; outputRowNo <= outputScores.size(); outputRowNo++) {
                row = sheet.createRow(outputRowNo);
                for (int outputNameNo = 0; outputNameNo < outputNames.size(); outputNameNo++) {
                    if (Objects.nonNull(outputScores.get(outputRowNo - 1).get(outputNames.get(outputNameNo)))) {
                        Cell cell = row.createCell(outputNameNo);
                        cell.setCellValue(Double.parseDouble(outputScores.get(outputRowNo - 1)
                                .get(outputNames.get(outputNameNo)).toString()
                        ));
                    }
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                logger.error("Close PoiUtils instance failed", e);
                throw new IOException("Close PoiUtils instance failed", e);
            }
        }
    }
}
