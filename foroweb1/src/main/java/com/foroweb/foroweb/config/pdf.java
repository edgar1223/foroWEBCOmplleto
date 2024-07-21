package com.foroweb.foroweb.config;
import com.foroweb.foroweb.service.FileStorageService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;

@Component
public class pdf {
    @Autowired
    private FileStorageService fileStorageService;

    public String convertToPdf(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path pdfFilePath;
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            pdfFilePath = convertWordToPdf(file);
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            pdfFilePath = convertExcelToPdf(file);
        } else if (fileName.endsWith(".txt")) {
            pdfFilePath = convertTextToPdf(file);
        }
        else {
            if(fileName.endsWith("pdf")){
                return fileStorageService.storeFile(file);
            }else {
                throw new IllegalArgumentException("Formato de archivo no soportado para conversión a PDF");
            }
        }

        // Utilizar el servicio de almacenamiento para guardar el archivo PDF y devolver una ruta relativa
        return fileStorageService.storeFile2(pdfFilePath);
    }

    // Métodos específicos para cada tipo de conversión
    private Path convertWordToPdf(MultipartFile file) throws IOException {
        File pdfFile = File.createTempFile(file.getOriginalFilename(), ".pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile);
             InputStream is = file.getInputStream()) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            if (file.getOriginalFilename().endsWith(".doc")) {
                HWPFDocument doc = new HWPFDocument(is);
                WordExtractor we = new WordExtractor(doc);
                document.add(new Paragraph(we.getText()));
            } else if (file.getOriginalFilename().endsWith(".docx")) {
                XWPFDocument docx = new XWPFDocument(is);
                XWPFWordExtractor we = new XWPFWordExtractor(docx);
                document.add(new Paragraph(we.getText()));
            }
            document.close();
        }
        return pdfFile.toPath();
    }

    private Path convertExcelToPdf(MultipartFile file) throws IOException {
        File pdfFile = File.createTempFile(file.getOriginalFilename(), ".pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile);
             InputStream is = file.getInputStream()) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);
            sheet.forEach(row -> {
                row.forEach(cell -> {
                    try {
                        document.add(new Paragraph(cell.toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            document.close();
        }
        return pdfFile.toPath();
    }

    private Path convertTextToPdf(MultipartFile file) throws IOException {
        File pdfFile = File.createTempFile(file.getOriginalFilename(), ".pdf");
        try (FileOutputStream fos = new FileOutputStream(pdfFile);
             InputStream is = file.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            String line;
            while ((line = br.readLine()) != null) {
                document.add(new Paragraph(line));
            }
            document.close();
        }
        return pdfFile.toPath();
    }


}
