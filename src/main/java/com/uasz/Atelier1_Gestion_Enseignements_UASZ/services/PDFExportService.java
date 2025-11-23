package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.Seance;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PDFExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * US38 - Générer un emploi du temps hebdomadaire en PDF
     */
    public byte[] generateEmploiDuTempsPDFSemaine(java.util.List<Seance> seances, LocalDate dateDebut, String titre) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate()); // Format paysage
            PdfWriter.getInstance(document, baos);

            document.open();

            // Titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD);
            Paragraph title = new Paragraph(titre, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Période
            LocalDate dateFin = dateDebut.with(DayOfWeek.SATURDAY);
            Font periodFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph period = new Paragraph(
                    "Semaine du " + dateDebut.format(DATE_FORMATTER) + " au " + dateFin.format(DATE_FORMATTER),
                    periodFont
            );
            period.setAlignment(Element.ALIGN_CENTER);
            period.setSpacingAfter(15);
            document.add(period);

            // Créer le tableau (6 colonnes pour lundi à samedi)
            PdfPTable table = new PdfPTable(7); // 1 colonne pour les heures + 6 jours
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 3, 3, 3, 3, 3, 3});

            // En-têtes du tableau
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD);
            addHeaderCell(table, "Heure", headerFont);

            LocalDate currentDate = dateDebut.with(DayOfWeek.MONDAY);
            for (int i = 0; i < 6; i++) {
                String dayName = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH);
                addHeaderCell(table, dayName.substring(0, 1).toUpperCase() + dayName.substring(1) + "\n" +
                        currentDate.format(DateTimeFormatter.ofPattern("dd/MM")), headerFont);
                currentDate = currentDate.plusDays(1);
            }

            // Grouper les séances par jour
            Map<LocalDate, java.util.List<Seance>> seancesParJour = seances.stream()
                    .collect(Collectors.groupingBy(Seance::getDateSeance));

            // Trouver toutes les heures uniques
            Set<String> heures = new TreeSet<>();
            for (Seance seance : seances) {
                heures.add(seance.getHeureDebut().format(TIME_FORMATTER) + " - " + seance.getHeureFin().format(TIME_FORMATTER));
            }

            // Remplir le tableau
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            for (String heure : heures) {
                PdfPCell heureCell = new PdfPCell(new Phrase(heure, cellFont));
                heureCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                heureCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                heureCell.setBackgroundColor(new Color(230, 230, 230));
                table.addCell(heureCell);

                currentDate = dateDebut.with(DayOfWeek.MONDAY);
                for (int i = 0; i < 6; i++) {
                    LocalDate finalCurrentDate = currentDate;
                    String finalHeure = heure;

                    java.util.List<Seance> seancesJour = seancesParJour.getOrDefault(finalCurrentDate, Collections.emptyList())
                            .stream()
                            .filter(s -> (s.getHeureDebut().format(TIME_FORMATTER) + " - " + s.getHeureFin().format(TIME_FORMATTER)).equals(finalHeure))
                            .collect(Collectors.toList());

                    if (!seancesJour.isEmpty()) {
                        Seance seance = seancesJour.get(0);
                        String cellContent = seance.getEc().getLibelle() + "\n" +
                                seance.getEnseignant().getPrenom() + " " + seance.getEnseignant().getNom() + "\n" +
                                "Salle: " + seance.getSalle().getLibelle();

                        PdfPCell cell = new PdfPCell(new Phrase(cellContent, cellFont));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setPadding(5);
                        cell.setBackgroundColor(new Color(220, 240, 255));
                        table.addCell(cell);
                    } else {
                        table.addCell("");
                    }

                    currentDate = currentDate.plusDays(1);
                }
            }

            document.add(table);

            // Pied de page
            document.add(new Paragraph("\n"));
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8);
            Paragraph footer = new Paragraph("Généré le " + LocalDate.now().format(DATE_FORMATTER), footerFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            document.add(footer);

            document.close();

            return baos.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    /**
     * US38 - Générer un emploi du temps par enseignant en PDF
     */
    public byte[] generateEmploiDuTempsEnseignantPDF(java.util.List<Seance> seances, String nomEnseignant, LocalDate dateDebut, LocalDate dateFin) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);

            document.open();

            // Titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Font.BOLD);
            Paragraph title = new Paragraph("Emploi du temps - " + nomEnseignant, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15);
            document.add(title);

            // Période
            Font periodFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Paragraph period = new Paragraph(
                    "Période : " + dateDebut.format(DATE_FORMATTER) + " - " + dateFin.format(DATE_FORMATTER),
                    periodFont
            );
            period.setAlignment(Element.ALIGN_CENTER);
            period.setSpacingAfter(15);
            document.add(period);

            // Tableau des séances
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 2, 3, 2, 2});

            // En-têtes
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD);
            addHeaderCell(table, "Date", headerFont);
            addHeaderCell(table, "Heure", headerFont);
            addHeaderCell(table, "Cours", headerFont);
            addHeaderCell(table, "Salle", headerFont);
            addHeaderCell(table, "Durée", headerFont);

            // Contenu
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            seances.sort(Comparator.comparing(Seance::getDateSeance).thenComparing(Seance::getHeureDebut));

            for (Seance seance : seances) {
                table.addCell(new Phrase(seance.getDateSeance().format(DATE_FORMATTER), cellFont));
                table.addCell(new Phrase(seance.getHeureDebut().format(TIME_FORMATTER) + " - " +
                        seance.getHeureFin().format(TIME_FORMATTER), cellFont));
                table.addCell(new Phrase(seance.getEc().getLibelle(), cellFont));
                table.addCell(new Phrase(seance.getSalle().getLibelle(), cellFont));
                table.addCell(new Phrase(seance.getDuree() + "h", cellFont));
            }

            document.add(table);

            // Footer
            document.add(new Paragraph("\n"));
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8);
            Paragraph footer = new Paragraph("Total : " + seances.size() + " séances | Généré le " +
                    LocalDate.now().format(DATE_FORMATTER), footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            return baos.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        Font whiteFont = new Font(font);
        whiteFont.setColor(Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, whiteFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(new Color(26, 94, 52)); // Vert UASZ
        cell.setPadding(8);
        table.addCell(cell);
    }
}
