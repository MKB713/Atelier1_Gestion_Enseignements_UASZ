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
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PdfGenerationService {

    public byte[] generatePdf(List<Seance> seances, String title) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
        Paragraph docTitle = new Paragraph(title, titleFont);
        docTitle.setAlignment(Paragraph.ALIGN_CENTER);
        docTitle.setSpacingAfter(20);
        document.add(docTitle);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
        Font dateFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.DARK_GRAY);
        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
        Font contentFont = new Font(Font.HELVETICA, 9);


        // Group Seance entities by dateSeance
        Map<LocalDate, List<Seance>> schedule = seances.stream()
                .collect(Collectors.groupingBy(Seance::getDateSeance));


        // Sort dates to ensure chronological order
        schedule.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            LocalDate date = entry.getKey();
            List<Seance> seancesOfDay = entry.getValue().stream().sorted((s1, s2) -> s1.getHeureDebut().compareTo(s2.getHeureDebut())).collect(Collectors.toList());

            Paragraph dateParagraph = new Paragraph(date.format(dateFormatter), dateFont);
            dateParagraph.setSpacingBefore(15);
            dateParagraph.setSpacingAfter(10);
            try {
                document.add(dateParagraph);
            } catch (DocumentException e) {
                System.err.println("Error adding date paragraph: " + e.getMessage());
                // Decide whether to rethrow or just log
            }

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

            for (Seance seance : seancesOfDay) {
                table.addCell(new Phrase(seance.getHeureDebut() + " - " + seance.getHeureFin(), contentFont));
                table.addCell(new Phrase((seance.getRepartition() != null && seance.getRepartition().getEc() != null) ? seance.getRepartition().getEc().getLibelle() : "N/A", contentFont));
                table.addCell(new Phrase((seance.getRepartition() != null && seance.getRepartition().getEnseignant() != null) ? (seance.getRepartition().getEnseignant().getPrenom() + " " + seance.getRepartition().getEnseignant().getNom()) : "N/A", contentFont));
                table.addCell(new Phrase((seance.getSalle() != null) ? seance.getSalle().getLibelle() : "N/A", contentFont));
            }
            try {
                document.add(table);
            } catch (DocumentException e) {
                System.err.println("Error adding table: " + e.getMessage());
                // Decide whether to rethrow or just log
            }
        });

        document.close();
        return baos.toByteArray();
    }


    public byte[] generatePdf(PlanningDTO planning) throws DocumentException {
        // Original implementation, kept for compatibility if still used by PlanningController@GetMapping("/pdf")
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
        Font contentFont = new Font(Font.HELVETICA, 9);


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
                table.addCell(new Phrase(seance.getHeureDebut() + " - " + seance.getHeureFin(), contentFont));
                table.addCell(new Phrase(seance.getEcLibelle(), contentFont));
                table.addCell(new Phrase(seance.getEnseignantNom(), contentFont));
                table.addCell(new Phrase(seance.getSalleNom(), contentFont));
            }
            document.add(table);
        }

        document.close();
        return baos.toByteArray();
    }
}
