package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.NoteCahierTexte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CahierTextePdfService {

    @Autowired
    private NoteCahierTexteService noteCahierTexteService;

    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(102, 126, 234);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Génère un PDF du cahier de texte avec les notes validées
     */
    public byte[] genererPdfCahierTexte(Long enseignantId) {
        List<NoteCahierTexte> notes;

        if (enseignantId != null) {
            notes = noteCahierTexteService.getNotesByEnseignant(enseignantId)
                    .stream()
                    .filter(NoteCahierTexte::isEstValide)
                    .toList();
        } else {
            notes = noteCahierTexteService.getAllNotes()
                    .stream()
                    .filter(NoteCahierTexte::isEstValide)
                    .toList();
        }

        return genererPdf(notes, enseignantId);
    }

    private byte[] genererPdf(List<NoteCahierTexte> notes, Long enseignantId) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // En-tête du document
            ajouterEntete(document, enseignantId, notes);

            // Tableau des notes
            if (!notes.isEmpty()) {
                ajouterTableauNotes(document, notes);
            } else {
                document.add(new Paragraph("Aucune note validée trouvée.")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(50));
            }

            // Pied de page avec signatures
            ajouterSignatures(document);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage(), e);
        }

        return baos.toByteArray();
    }

    private void ajouterEntete(Document document, Long enseignantId, List<NoteCahierTexte> notes) {
        // Titre principal
        Paragraph titre = new Paragraph("UNIVERSITE ASSANE SECK DE ZIGUINCHOR")
                .setBold()
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titre);

        Paragraph sousTitre = new Paragraph("CAHIER DE TEXTE")
                .setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(sousTitre);

        // Informations sur l'enseignant si spécifié
        if (enseignantId != null && !notes.isEmpty()) {
            NoteCahierTexte premiereNote = notes.get(0);
            if (premiereNote.getEnseignant() != null) {
                Paragraph enseignantInfo = new Paragraph("Enseignant: " +
                        premiereNote.getEnseignant().getNom() + " " +
                        premiereNote.getEnseignant().getPrenom())
                        .setFontSize(11)
                        .setTextAlignment(TextAlignment.LEFT);
                document.add(enseignantInfo);
            }
        }

        // Date de génération
        Paragraph dateGeneration = new Paragraph("Généré le: " +
                LocalDateTime.now().format(DATETIME_FORMATTER))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(20);
        document.add(dateGeneration);

        // Ligne de séparation
        document.add(new Paragraph("─".repeat(80))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15));
    }

    private void ajouterTableauNotes(Document document, List<NoteCahierTexte> notes) {
        // Créer le tableau
        Table table = new Table(UnitValue.createPercentArray(new float[]{15, 15, 25, 25, 20}))
                .setWidth(UnitValue.createPercentValue(100));

        // En-têtes du tableau
        String[] headers = {"Date", "EC/Module", "Titre", "Contenu", "Objectifs"};
        for (String header : headers) {
            Cell cell = new Cell()
                    .add(new Paragraph(header).setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(HEADER_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5);
            table.addHeaderCell(cell);
        }

        // Données des notes
        for (NoteCahierTexte note : notes) {
            // Date
            String dateStr = "-";
            if (note.getSeance() != null && note.getSeance().getDateSeance() != null) {
                dateStr = note.getSeance().getDateSeance().format(DATE_FORMATTER);
            }
            table.addCell(creerCellule(dateStr));

            // Module/EC
            String moduleStr = "-";
            if (note.getSeance() != null && note.getSeance().getModule() != null) {
                moduleStr = note.getSeance().getModule().getNom();
            }
            table.addCell(creerCellule(moduleStr));

            // Titre
            table.addCell(creerCellule(note.getTitre() != null ? note.getTitre() : "-"));

            // Contenu (limité)
            String contenu = note.getContenu() != null ? note.getContenu() : "-";
            if (contenu.length() > 100) {
                contenu = contenu.substring(0, 97) + "...";
            }
            table.addCell(creerCellule(contenu));

            // Objectifs
            String objectifs = note.getObjectifsPedagogiques() != null ? note.getObjectifsPedagogiques() : "-";
            if (objectifs.length() > 80) {
                objectifs = objectifs.substring(0, 77) + "...";
            }
            table.addCell(creerCellule(objectifs));
        }

        document.add(table);

        // Statistiques
        document.add(new Paragraph("\nTotal: " + notes.size() + " note(s) validée(s)")
                .setFontSize(10)
                .setItalic()
                .setMarginTop(10));
    }

    private Cell creerCellule(String texte) {
        return new Cell()
                .add(new Paragraph(texte).setFontSize(9))
                .setPadding(4);
    }

    private void ajouterSignatures(Document document) {
        document.add(new Paragraph("\n\n"));

        // Zone de signatures
        Table signatures = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(50);

        // Signature enseignant
        Cell cellEnseignant = new Cell()
                .add(new Paragraph("Signature de l'Enseignant").setBold().setFontSize(11))
                .add(new Paragraph("\n\n\n"))
                .add(new Paragraph("Date: ____/____/________"))
                .setBorder(null)
                .setTextAlignment(TextAlignment.CENTER);
        signatures.addCell(cellEnseignant);

        // Signature chef de département
        Cell cellChef = new Cell()
                .add(new Paragraph("Visa du Chef de Département").setBold().setFontSize(11))
                .add(new Paragraph("\n\n\n"))
                .add(new Paragraph("Date: ____/____/________"))
                .setBorder(null)
                .setTextAlignment(TextAlignment.CENTER);
        signatures.addCell(cellChef);

        document.add(signatures);

        // Pied de page
        document.add(new Paragraph("\n─".repeat(80))
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Document généré automatiquement - UASZ Gestion des Enseignements")
                .setFontSize(8)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER));
    }
}
