package com.sumitra.resume.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PdfGenerator {

    private static final float MARGIN = 50;
    private static final float FONT_SIZE = 11;
    private static final float LINE_HEIGHT = 14;
    private static final float PAGE_WIDTH = 595;
    private static final float MAX_WIDTH = PAGE_WIDTH - (2 * MARGIN);

    public byte[] createPdfFromText(String title, String content) {
        try (PDDocument document = new PDDocument()) {
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            List<String> lines = new ArrayList<>();
            if (title != null && !title.isBlank()) {
                lines.add(title);
                lines.add("");
            }
            for (String paragraph : content.split("\\R")) {
                lines.addAll(wrapText(paragraph, font, MAX_WIDTH));
                lines.add("");
            }

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream stream = new PDPageContentStream(document, page);
            float y = page.getMediaBox().getHeight() - MARGIN;

            for (String line : lines) {
                if (y < MARGIN) {
                    stream.close();
                    page = new PDPage();
                    document.addPage(page);
                    stream = new PDPageContentStream(document, page);
                    y = page.getMediaBox().getHeight() - MARGIN;
                }

                boolean isTitleLine = title != null && line.equals(title);
                stream.beginText();
                stream.setFont(isTitleLine ? boldFont : font, isTitleLine ? 14 : FONT_SIZE);
                stream.newLineAtOffset(MARGIN, y);
                stream.showText(sanitize(line));
                stream.endText();
                y -= LINE_HEIGHT;
            }

            stream.close();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            document.save(output);
            return output.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private List<String> wrapText(String text, PDType1Font font, float maxWidth) throws IOException {
        if (text == null || text.isBlank()) {
            return List.of("");
        }

        List<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder current = new StringBuilder();

        for (String word : words) {
            String candidate = current.isEmpty() ? word : current + " " + word;
            float width = font.getStringWidth(candidate) / 1000 * FONT_SIZE;
            if (width > maxWidth && !current.isEmpty()) {
                lines.add(current.toString());
                current = new StringBuilder(word);
            } else {
                current = new StringBuilder(candidate);
            }
        }

        if (!current.isEmpty()) {
            lines.add(current.toString());
        }

        return lines;
    }

    private String sanitize(String text) {
        return text.replaceAll("[\\u0000-\\u001F]", " ").trim();
    }
}
