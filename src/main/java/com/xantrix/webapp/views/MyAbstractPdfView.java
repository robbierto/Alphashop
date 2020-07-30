package com.xantrix.webapp.views;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public abstract class MyAbstractPdfView extends AbstractView
{
	protected Rectangle pageSize = PageSize.A4;
	protected boolean isLandScape = true;
	
	//Margini della pagina
	protected float mrgLeft = 10;
	protected float mrgRight = 10;
	protected float mrgTop = 10;
	protected float mrgBottom = 10;
	
	public MyAbstractPdfView()
	{
		setContentType("application/pdf");
	}
	
	@Override
	protected boolean generatesDownloadContent()
	{
		return true;
	}
	
	@Override
	protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{

		// IE workaround: write into byte array first.
		ByteArrayOutputStream baos = createTemporaryOutputStream();

		pageSize = (isLandScape) ? pageSize.rotate() : pageSize;
		
		// Preferenze del documento
		Document document = new Document(pageSize, mrgLeft, mrgRight, mrgTop, mrgBottom);
		
		PdfWriter writer = PdfWriter.getInstance(document, baos);
		prepareWriter(model, writer, request);
		buildPdfMetadata(model, document, request);

		// Creazione del file PDF
		document.open();
		buildPdfDocument(model, document, writer, request, response);
		document.close();

		// Flush to HTTP response.
		writeToResponse(response, baos);
	}
	
	protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
			throws DocumentException
	{
		writer.setViewerPreferences(getViewerPreferences());
	}
	
	protected int getViewerPreferences()
	{
		return  PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
	}
	
	protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request)
	{
	}
	
	protected abstract void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception;
}
