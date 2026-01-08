package py.com.hidraulica.caacupe.service;

import py.com.hidraulica.caacupe.domain.Factura;
import py.com.hidraulica.caacupe.domain.FacturaItem;
import py.com.hidraulica.caacupe.domain.enums.TipoFactura;
import py.com.hidraulica.caacupe.exception.BusinessException;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class FacturaPdfService {

	private static final String EMPRESA_NOMBRE = "Hidráulica Nuestra Sra. De Caacupe";

	private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private static final Locale LOCALE_PY = new Locale("es", "PY");

	public enum PdfFormat {
		A4, TICKET
	}

	public byte[] generarPdf(Factura factura) {
		return generarPdf(factura, PdfFormat.TICKET);
	}

	public byte[] generarPdf(Factura factura, PdfFormat format) {
		if (factura.getTipo() == TipoFactura.FISCAL) {
			throw new BusinessException("PDF Fiscal aún no implementado. Use NO_FISCAL por el momento.");
		}
		if (format == null)
			format = PdfFormat.TICKET;
		return switch (format) {
		case A4 -> generarNoFiscalA4(factura);
		case TICKET -> generarNoFiscalTicket(factura);
		};
	}

	private byte[] generarNoFiscalA4(Factura f) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
		PdfWriter.getInstance(doc, baos);
		doc.open();

		Font h1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
		Font h2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
		Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10);
		Font small = FontFactory.getFont(FontFactory.HELVETICA, 9);

		Paragraph empresa = new Paragraph(EMPRESA_NOMBRE, h2);
		empresa.setAlignment(Element.ALIGN_CENTER);
		doc.add(empresa);

		Paragraph title = new Paragraph("FACTURA NO FISCAL", h1);
		title.setAlignment(Element.ALIGN_CENTER);
		doc.add(title);
		doc.add(Chunk.NEWLINE);

		PdfPTable meta = new PdfPTable(new float[] { 2, 3, 2, 3 });
		meta.setWidthPercentage(100);

		addMeta(meta, "N°", f.getNumero() != null ? f.getNumero() : String.valueOf(f.getId()), h2, normal);
		addMeta(meta, "Fecha", f.getFecha() != null ? FECHA_FMT.format(f.getFecha()) : "-", h2, normal);
		addMeta(meta, "Cliente", f.getCliente() != null ? safe(f.getCliente().getNombreRazonSocial()) : "-", h2,
				normal);
		addMeta(meta, "RUC/CI", f.getCliente() != null ? safe(f.getCliente().getRuc()) : "-", h2, normal);
		doc.add(meta);
		doc.add(Chunk.NEWLINE);

		PdfPTable items = new PdfPTable(new float[] { 1.2f, 5.0f, 2.0f, 2.0f, 2.2f });
		items.setWidthPercentage(100);
		addHeader(items, "Cant.", h2);
		addHeader(items, "Descripción", h2);
		addHeader(items, "P.Unit", h2);
		addHeader(items, "Tipo", h2);
		addHeader(items, "Total", h2);

		BigDecimal total = BigDecimal.ZERO;

		if (f.getItems() != null) {
			for (FacturaItem it : f.getItems()) {
				BigDecimal cant = nz(it.getCantidad());
				BigDecimal pu = nz(it.getPrecioUnitario());
				BigDecimal linea = it.getTotalLinea() != null ? it.getTotalLinea() : pu.multiply(cant);

				addCell(items, fmtQty(cant), normal, Element.ALIGN_RIGHT);
				addCell(items, safe(it.getDescripcion()), normal, Element.ALIGN_LEFT);
				addCell(items, fmtMoney(pu), normal, Element.ALIGN_RIGHT);
				addCell(items, it.getTipo() != null ? it.getTipo().name() : "-", small, Element.ALIGN_CENTER);
				addCell(items, fmtMoney(linea), normal, Element.ALIGN_RIGHT);

				total = total.add(linea);
			}
		}

		doc.add(items);
		doc.add(Chunk.NEWLINE);

		PdfPTable tot = new PdfPTable(new float[] { 7.2f, 2.8f });
		tot.setWidthPercentage(100);
		PdfPCell blank = new PdfPCell(new Phrase(""));
		blank.setBorder(Rectangle.NO_BORDER);
		tot.addCell(blank);

		PdfPCell totalLbl = new PdfPCell(new Phrase("TOTAL", h2));
		totalLbl.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalLbl.setPadding(8);
		tot.addCell(totalLbl);

		tot.addCell(blank);

		PdfPCell totalVal = new PdfPCell(new Phrase(fmtMoney(total), h2));
		totalVal.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalVal.setPadding(8);
		tot.addCell(totalVal);

		doc.add(tot);

		doc.add(Chunk.NEWLINE);
		Paragraph note = new Paragraph("Documento sin valor fiscal. Gracias por su preferencia.", small);
		note.setAlignment(Element.ALIGN_CENTER);
		doc.add(note);

		doc.close();
		return baos.toByteArray();
	}

	private byte[] generarNoFiscalTicket(Factura f) {

		final float width = 226.77f; // ~80mm
		final float marginTop = 10f, marginBottom = 10f, marginLeft = 10f, marginRight = 10f;

		// Fuentes (las mismas que usás al dibujar)
		Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
		Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
		Font normal = FontFactory.getFont(FontFactory.HELVETICA, 9);
		Font small = FontFactory.getFont(FontFactory.HELVETICA, 8);

		// 1) Calculamos alto dinámico
		float height = calcularAltoTicket(f);

		Rectangle page = new Rectangle(width, height);
		Document doc = new Document(page, marginLeft, marginRight, marginTop, marginBottom);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(doc, baos);
		doc.open();

		Paragraph emp = new Paragraph(EMPRESA_NOMBRE, title);
		emp.setAlignment(Element.ALIGN_CENTER);
		doc.add(emp);

		Paragraph nf = new Paragraph("FACTURA NO FISCAL", bold);
		nf.setAlignment(Element.ALIGN_CENTER);
		doc.add(nf);

		doc.add(linea());
		doc.add(new Paragraph("N°: " + (f.getNumero() != null ? f.getNumero() : String.valueOf(f.getId())), normal));
		doc.add(new Paragraph("Fecha: " + (f.getFecha() != null ? FECHA_FMT.format(f.getFecha()) : "-"), normal));

		if (f.getCliente() != null) {
			doc.add(new Paragraph("Cliente: " + safe(f.getCliente().getNombreRazonSocial()), normal));
			if (f.getCliente().getRuc() != null && !f.getCliente().getRuc().isBlank()) {
				doc.add(new Paragraph("RUC/CI: " + safe(f.getCliente().getRuc()), normal));
			}
		}

		doc.add(linea());

		BigDecimal total = BigDecimal.ZERO;

		if (f.getItems() != null) {
			for (FacturaItem it : f.getItems()) {
				BigDecimal cant = nz(it.getCantidad());
				BigDecimal pu = nz(it.getPrecioUnitario());
				BigDecimal linea = it.getTotalLinea() != null ? it.getTotalLinea() : pu.multiply(cant);

				// 1) descripción (puede wrap)
				  for (String line : wrapText(safe(it.getDescripcion()), 28)) {
				    doc.add(new Paragraph(line, bold));
				  }

				  // 2) fila detalle (izq) + total (der)
				  PdfPTable row = new PdfPTable(new float[] { 2.2f, 1.0f });
				  row.setWidthPercentage(100);

				PdfPCell left = new PdfPCell(new Phrase(fmtQty(cant) + " x " + fmtMoney(pu), normal));
				left.setBorder(Rectangle.NO_BORDER);
				left.setHorizontalAlignment(Element.ALIGN_LEFT);
				left.setPadding(0);

				PdfPCell right = new PdfPCell(new Phrase(fmtMoney(linea), normal));
				right.setBorder(Rectangle.NO_BORDER);
				right.setHorizontalAlignment(Element.ALIGN_RIGHT);
				right.setPadding(0);

				row.addCell(left);
				row.addCell(right);

				doc.add(row);

				doc.add(new Paragraph(" ", small)); // espacio entre items
				total = total.add(linea);
			}

		}

		doc.add(linea());

		PdfPTable tot = new PdfPTable(new float[] { 2.2f, 1.0f });
		tot.setWidthPercentage(100);

		PdfPCell lbl = new PdfPCell(new Phrase("TOTAL", bold));
		lbl.setBorder(Rectangle.NO_BORDER);
		lbl.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPCell val = new PdfPCell(new Phrase(fmtMoney(total), title));
		val.setBorder(Rectangle.NO_BORDER);
		val.setHorizontalAlignment(Element.ALIGN_RIGHT);

		tot.addCell(lbl);
		tot.addCell(val);
		doc.add(tot);

		doc.add(linea());

		Paragraph note = new Paragraph("Documento sin valor fiscal.", small);
		note.setAlignment(Element.ALIGN_CENTER);
		doc.add(note);

		Paragraph thanks = new Paragraph("Gracias por su preferencia.", small);
		thanks.setAlignment(Element.ALIGN_CENTER);
		doc.add(thanks);

		doc.close();
		return baos.toByteArray();
	}

	private static Paragraph linea() {
		Paragraph p = new Paragraph("--------------------------------", FontFactory.getFont(FontFactory.HELVETICA, 8));
		p.setAlignment(Element.ALIGN_CENTER);
		return p;
	}

	private static BigDecimal nz(BigDecimal v) {
		return v == null ? BigDecimal.ZERO : v;
	}

	private static void addMeta(PdfPTable t, String k, String v, Font fk, Font fv) {
		PdfPCell c1 = new PdfPCell(new Phrase(k, fk));
		c1.setBorder(Rectangle.NO_BORDER);
		c1.setPadding(2);
		PdfPCell c2 = new PdfPCell(new Phrase(v != null ? v : "-", fv));
		c2.setBorder(Rectangle.NO_BORDER);
		c2.setPadding(2);
		t.addCell(c1);
		t.addCell(c2);
	}

	private static void addHeader(PdfPTable t, String txt, Font f) {
		PdfPCell c = new PdfPCell(new Phrase(txt, f));
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setBackgroundColor(new java.awt.Color(240, 240, 240));
		c.setPadding(6);
		t.addCell(c);
	}

	private static void addCell(PdfPTable t, String txt, Font f, int align) {
		PdfPCell c = new PdfPCell(new Phrase(txt != null ? txt : "-", f));
		c.setHorizontalAlignment(align);
		c.setPadding(6);
		t.addCell(c);
	}

	private static String safe(String s) {
		return s == null ? "-" : s;
	}

	// si se quiere usar mas adelando con Dolar
//	private static String fmtMoney(BigDecimal v) {
//		NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_PY);
//		nf.setMinimumFractionDigits(0);
//		nf.setMaximumFractionDigits(2);
//		return nf.format(v == null ? BigDecimal.ZERO : v);
//	}

	private static String fmtMoney(BigDecimal v) {
		  NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_PY);
		  nf.setMinimumFractionDigits(0);
		  nf.setMaximumFractionDigits(2);
		  return "Gs. " + nf.format(v == null ? BigDecimal.ZERO : v);
		}

	private static String fmtQty(BigDecimal v) {
		NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_PY);
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(3);
		return nf.format(v == null ? BigDecimal.ZERO : v);
	}

	/**
	 * Ajustes finos (si ves mucho aire o corte)
	 * 
	 * Si te queda muy largo, bajá perLine de 12f a 11f.
	 * 
	 * Si te queda cortado, subí el colchón +40f a +60f.
	 * 
	 * Si tu impresora usa fuente más grande/pequeña, ajustamos charsPerLine (por
	 * defecto 28 está bien para 80mm con margen 10).
	 * 
	 * @param f
	 * @return
	 */
	private float calcularAltoTicket(Factura f) {
		// Aproximación práctica (suficiente para impresoras reales)
		// Unidades: puntos PDF. 72 pt = 1 pulgada.

		final float baseHeader = 170f; // empresa + título + metadatos + separadores
		final float footer = 90f; // total + separadores + textos
		final float perLine = 12f; // alto por línea aprox en font 9
		final int charsPerLine = 28; // aprox para 80mm con margen y font 9

		float itemsHeight = 0f;

		if (f.getItems() != null) {
			for (FacturaItem it : f.getItems()) {
				String desc = it.getDescripcion() == null ? "" : it.getDescripcion().trim();

				// líneas de descripción según largo
				int descLines = wrapText(desc, charsPerLine).size();

				// Por cada ítem:
				// - descLines (descripción)
				// - 1 línea detalle "cant x pu = total"
				// - 1 línea tipo (si existe)
				// - 1 línea en blanco
				int extraLines = 1 /* detalle */ + 1 /* blank */;

				itemsHeight += (descLines + extraLines) * perLine;
			}
		}

		// Mínimo para evitar tickets muy “cortitos”
		float minHeight = 380f;

		float total = baseHeader + itemsHeight + footer;

		// Un “colchón” para que nunca corte por poquito (muy común)
		total += 40f;

		return Math.max(total, minHeight);
	}

	private static java.util.List<String> wrapText(String text, int maxChars) {
		if (text == null)
			return java.util.List.of("-");
		text = text.trim();
		if (text.isEmpty())
			return java.util.List.of("-");

		java.util.List<String> lines = new java.util.ArrayList<>();
		String[] words = text.split("\\s+");
		StringBuilder line = new StringBuilder();

		for (String w : words) {
			if (line.length() == 0) {
				line.append(w);
			} else if (line.length() + 1 + w.length() <= maxChars) {
				line.append(' ').append(w);
			} else {
				lines.add(line.toString());
				line.setLength(0);

				// palabra muy larga: la cortamos
				while (w.length() > maxChars) {
					lines.add(w.substring(0, maxChars));
					w = w.substring(maxChars);
				}
				line.append(w);
			}
		}
		if (line.length() > 0)
			lines.add(line.toString());
		return lines;
	}

}
