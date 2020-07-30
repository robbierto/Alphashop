package com.xantrix.webapp.views;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.xantrix.webapp.domain.Articoli;

import java.awt.Color;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class ArticoliPdfView extends MyAbstractPdfView
{
	private String fileName;
	
	public ArticoliPdfView()
	{
		fileName = "prodotti.pdf";
		isLandScape = false;
	    mrgTop = 15;
	    mrgBottom = 15;
	    mrgLeft = 12;
	    mrgRight = 12;
	}
	
	public ArticoliPdfView(String NomeFile)
	{
		fileName = NomeFile;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final String Titolo = "Elenco Articoli";
		 
		//Impostazione del nome del file
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
           
        List<Articoli> articoli = (List<Articoli>) model.get("Articoli");
      
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10);
      
 
        //impostazione  del colore e tipo di font
        Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setColor(Color.WHITE);
 
        // impostazioni dell'intestazione
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
 
        // Intestazione del documento
        cell.setPhrase(new Phrase("Codice", font));
        table.addCell(cell);
 
        cell.setPhrase(new Phrase("Descrizione", font));
        table.addCell(cell);
 
        cell.setPhrase(new Phrase("Prezzo", font));
        table.addCell(cell);
 
        cell.setPhrase(new Phrase("UM", font));
        table.addCell(cell);
 
        cell.setPhrase(new Phrase("Categoria", font));
        table.addCell(cell);
 
        for(Articoli articolo : articoli)
        {
            table.addCell(articolo.getCodArt());
            table.addCell(articolo.getDescrizione());
            table.addCell(articolo.getPrezzo().toString());
            table.addCell(articolo.getUm());
            table.addCell(articolo.getDesFamAss());
        } 
 
        document.addTitle(Titolo);
        document.add(new Paragraph("Documento Creato il " + LocalDate.now()));
        document.setPageCount(0);
     
        document.add(table);
    } 
}
