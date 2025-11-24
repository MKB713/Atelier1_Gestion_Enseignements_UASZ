package com.uasz.Atelier1_Gestion_Enseignements_UASZ.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.uasz.Atelier1_Gestion_Enseignements_UASZ.entities.NoteCahierTexte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CahierTextePdfService {

    @Autowired
    private NoteCahierTexteService noteCahierTexteService;

    private static final Color HEADER_COLOR = new Color(102, 126, 234);
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
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // En-tête du document
            ajouterEntete(document, enseignantId, notes);

            // Tableau des notes
            if (!notes.isEmpty()) {
                ajouterTableauNotes(document, notes);
            } else {
                Font font = FontFactory.getFont(FontFactory.HELVETICA, 12);
                Paragraph p = new Paragraph("Aucune note validée trouvée.", font);
                p.setAlignment(Element.ALIGN_CENTER);
                p.setSpacingBefore(50);
                document.add(p);
            }

            // Pied de page avec signatures
            ajouterSignatures(document);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage(), e);
        }

        return baos.toByteArray();
    }

    private void ajouterEntete(Document document, Long enseignantId, List<NoteCahierTexte> notes) throws DocumentException {
        // Titre principal
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Font.BOLD);
        Paragraph titre = new Paragraph("UNIVERSITE ASSANE SECK DE ZIGUINCHOR", titleFont);
        titre.setAlignment(Element.ALIGN_CENTER);
        document.add(titre);

        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Font.BOLD);
        Paragraph sousTitre = new Paragraph("CAHIER DE TEXTE", subtitleFont);
        sousTitre.setAlignment(Element.ALIGN_CENTER);
        sousTitre.setSpacingAfter(10);
        document.add(sousTitre);

        // Informations sur l'enseignant si spécifié
        if (enseignantId != null && !notes.isEmpty()) {
            NoteCahierTexte premiereNote = notes.get(0);
            if (premiereNote.getEnseignant() != null) {
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
                Paragraph enseignantInfo = new Paragraph("Enseignant: " +
                        premiereNote.getEnseignant().getNom() + " " +
                        premiereNote.getEnseignant().getPrenom(), normalFont);
                enseignantInfo.setAlignment(Element.ALIGN_LEFT);
                document.add(enseignantInfo);
            }
        }

        // Date de génération
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Paragraph dateGeneration = new Paragraph("Généré le: " +
                LocalDateTime.now().format(DATETIME_FORMATTER), dateFont);
        dateGeneration.setAlignment(Element.ALIGN_RIGHT);
        dateGeneration.setSpacingAfter(20);
        document.add(dateGeneration);

        // Ligne de séparation
        Font separatorFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph separator = new Paragraph("─────────────────────────────────────────────────────────────────────────────────", separatorFont);
        separator.setAlignment(Element.ALIGN_CENTER);
        separator.setSpacingAfter(15);
        document.add(separator);
    }

    private void ajouterTableauNotes(Document document, List<NoteCahierTexte> notes) throws DocumentException {
        // Créer le tableau avec 5 colonnes
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{15, 15, 25, 25, 20});

        // En-têtes du tableau
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD);
        headerFont.setColor(Color.WHITE);
        String[] headers = {"Date", "EC/Module", "Titre", "Contenu", "Objectifs"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(HEADER_COLOR);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Données des notes
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        for (NoteCahierTexte note : notes) {
            // Date
            String dateStr = "-";
            if (note.getSeance() != null && note.getSeance().getDateSeance() != null) {
                dateStr = note.getSeance().getDateSeance().format(DATE_FORMATTER);
            }
            table.addCell(creerCellule(dateStr, cellFont));

            // Module/EC
            String ecStr = "-";
            if (note.getSeance() != null && note.getSeance().getEc() != null) {
                ecStr = note.getSeance().getEc().getLibelle();
            }
            table.addCell(creerCellule(ecStr, cellFont));

            // Titre
            table.addCell(creerCellule(note.getTitre() != null ? note.getTitre() : "-", cellFont));

            // Contenu (limité)
            String contenu = note.getContenu() != null ? note.getContenu() : "-";
            if (contenu.length() > 100) {
                contenu = contenu.substring(0, 97) + "...";
            }
            table.addCell(creerCellule(contenu, cellFont));

            // Objectifs
            String objectifs = note.getObjectifsPedagogiques() != null ? note.getObjectifsPedagogiques() : "-";
            if (objectifs.length() > 80) {
                objectifs = objectifs.substring(0, 77) + "...";
            }
            table.addCell(creerCellule(objectifs, cellFont));
        }

        document.add(table);

        // Statistiques
        Font statsFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Font.ITALIC);
        Paragraph stats = new Paragraph("\nTotal: " + notes.size() + " note(s) validée(s)", statsFont);
        stats.setSpacingBefore(10);
        document.add(stats);
    }

    private PdfPCell creerCellule(String texte, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, font));
        cell.setPadding(4);
        return cell;
    }

    private void ajouterSignatures(Document document) throws DocumentException {
        document.add(new Paragraph("\n\n"));

        // Zone de signatures
        PdfPTable signatures = new PdfPTable(2);
        signatures.setWidthPercentage(100);
        signatures.setSpacingBefore(50);

        Font signatureFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD);
        Font dateSignatureFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        // Signature enseignant
        PdfPCell cellEnseignant = new PdfPCell();
        cellEnseignant.setBorder(Rectangle.NO_BORDER);
        cellEnseignant.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph enseignantTitle = new Paragraph("Signature de l'Enseignant", signatureFont);
        enseignantTitle.setAlignment(Element.ALIGN_CENTER);
        cellEnseignant.addElement(enseignantTitle);
        cellEnseignant.addElement(new Paragraph("\n\n\n"));

        Paragraph enseignantDate = new Paragraph("Date: ____/____/________", dateSignatureFont);
        enseignantDate.setAlignment(Element.ALIGN_CENTER);
        cellEnseignant.addElement(enseignantDate);

        signatures.addCell(cellEnseignant);

        // Signature chef de département
        PdfPCell cellChef = new PdfPCell();
        cellChef.setBorder(Rectangle.NO_BORDER);
        cellChef.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph chefTitle = new Paragraph("Visa du Chef de Département", signatureFont);
        chefTitle.setAlignment(Element.ALIGN_CENTER);
        cellChef.addElement(chefTitle);
        cellChef.addElement(new Paragraph("\n\n\n"));

        Paragraph chefDate = new Paragraph("Date: ____/____/________", dateSignatureFont);
        chefDate.setAlignment(Element.ALIGN_CENTER);
        cellChef.addElement(chefDate);

        signatures.addCell(cellChef);

        document.add(signatures);

        // Pied de page
        Font separatorFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph separator = new Paragraph("\n─────────────────────────────────────────────────────────────────────────────────", separatorFont);
        separator.setAlignment(Element.ALIGN_CENTER);
        document.add(separator);

        Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, Font.ITALIC);
        Paragraph footer = new Paragraph("Document généré automatiquement - UASZ Gestion des Enseignements", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
}
