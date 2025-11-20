package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.PlanningDTO;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.dto.SeanceDTO;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class PdfGenerationService {

    public byte[] generatePdf(PlanningDTO planning) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
        Paragraph title = new Paragraph(planning.getTitle(), titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        Font dateFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.DARK_GRAY);
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);

        for (Map.Entry<LocalDate, List<SeanceDTO>> entry : planning.getSchedule().entrySet()) {
            Paragraph dateParagraph = new Paragraph(entry.getKey().format(dateFormatter), dateFont);
            dateParagraph.setSpacingBefore(15);
            dateParagraph.setSpacingAfter(10);
            document.add(dateParagraph);

            PdfPTable table = new PdfPTable(4); // 4 columns: Heure, Enseignement, Enseignant, Salle
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2f, 3f, 3f, 2f});

            // Add table headers
            Stream.of("Heure", "Enseignement", "Enseignant", "Salle").forEach(headerTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(Color.GRAY);
                header.setBorderWidth(1);
                header.setPhrase(new Phrase(headerTitle, headerFont));
                table.addCell(header);
            });

            for (SeanceDTO seance : entry.getValue()) {
                table.addCell(seance.getHeureDebut() + " - " + seance.getHeureFin());
                table.addCell(seance.getEcNom());
                table.addCell(seance.getEnseignantNom());
                table.addCell(seance.getSalleNom());
            }
            document.add(table);
        }

        document.close();
        return baos.toByteArray();
    }
}
