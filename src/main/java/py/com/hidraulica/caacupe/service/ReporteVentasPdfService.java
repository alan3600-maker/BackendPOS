package py.com.hidraulica.caacupe.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import py.com.hidraulica.caacupe.domain.Venta;
import py.com.hidraulica.caacupe.domain.enums.MedioPago;
import py.com.hidraulica.caacupe.repository.VentaRepository;

@Service
public class ReporteVentasPdfService {

  private final VentaRepository ventaRepo;

  public ReporteVentasPdfService(VentaRepository ventaRepo) {
    this.ventaRepo = ventaRepo;
  }

  public byte[] pdfVentas(OffsetDateTime desde, OffsetDateTime hasta, String titulo) {
    List<Venta> ventas = ventaRepo.findConfirmadasForReporte(desde, hasta);

    try {
      Document doc = new Document(PageSize.A4);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(doc, baos);
      doc.open();

      Font h1 = new Font(Font.HELVETICA, 14, Font.BOLD);
      Font h2 = new Font(Font.HELVETICA, 10, Font.BOLD);
      Font normal = new Font(Font.HELVETICA, 9, Font.NORMAL);
      Font small = new Font(Font.HELVETICA, 8, Font.NORMAL);

      doc.add(new Paragraph("Hidráulica Caacupé", h1));
      doc.add(new Paragraph(titulo, h2));
      doc.add(new Paragraph("Generado: " + OffsetDateTime.now().toString(), small));
      doc.add(new Paragraph(" "));

      PdfPTable table = new PdfPTable(new float[] { 1.2f, 2.2f, 3.2f, 2.0f, 2.0f });
      table.setWidthPercentage(100);
      addHeader(table, "ID", h2);
      addHeader(table, "Fecha", h2);
      addHeader(table, "Cliente", h2);
      addHeader(table, "Medio", h2);
      addHeader(table, "Total", h2);

      BigDecimal total = BigDecimal.ZERO;
      EnumMap<MedioPago, BigDecimal> porMedio = new EnumMap<>(MedioPago.class);
      for (MedioPago mp : MedioPago.values()) porMedio.put(mp, BigDecimal.ZERO);

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      for (Venta v : ventas) {
        total = total.add(nz(v.getTotal()));
        // si hay cobros, agregamos al resumen por medio
        if (v.getCobros() != null) {
          v.getCobros().forEach(c -> porMedio.put(c.getMedioPago(), porMedio.get(c.getMedioPago()).add(nz(c.getMonto()))));
        }

        addCell(table, String.valueOf(v.getId()), normal, Element.ALIGN_LEFT);
        addCell(table, v.getFecha() != null ? dtf.format(v.getFecha()) : "-", normal, Element.ALIGN_LEFT);
        addCell(table, v.getCliente() != null ? v.getCliente().getNombreRazonSocial() : "-", normal, Element.ALIGN_LEFT);

        String medio = "-";
        if (v.getCobros() != null && !v.getCobros().isEmpty()) {
          // si es mixto, queda MIXTO; si hay uno solo, ese medio
          if (v.getCobros().size() == 1) {
            medio = String.valueOf(v.getCobros().get(0).getMedioPago());
          } else {
            medio = "MIXTO";
          }
        }
        addCell(table, medio, normal, Element.ALIGN_LEFT);
        addCell(table, fmtMoney(nz(v.getTotal())), normal, Element.ALIGN_RIGHT);
      }

      doc.add(table);
      doc.add(new Paragraph(" "));

      PdfPTable resumen = new PdfPTable(new float[] { 3f, 2f });
      resumen.setWidthPercentage(50);
      resumen.setHorizontalAlignment(Element.ALIGN_LEFT);

      addHeader(resumen, "Resumen", h2);
      addHeader(resumen, "Monto", h2);

      for (MedioPago mp : MedioPago.values()) {
        addCell(resumen, mp.name(), normal, Element.ALIGN_LEFT);
        addCell(resumen, fmtMoney(porMedio.get(mp)), normal, Element.ALIGN_RIGHT);
      }
      addCell(resumen, "TOTAL", h2, Element.ALIGN_LEFT);
      addCell(resumen, fmtMoney(total), h2, Element.ALIGN_RIGHT);

      doc.add(resumen);

      doc.close();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Error generando PDF de ventas", e);
    }
  }

  public static OffsetDateTime startOfDay(LocalDate date) {
    ZoneId zone = ZoneId.systemDefault();
    return date.atStartOfDay(zone).toOffsetDateTime();
  }

  public static OffsetDateTime endOfDay(LocalDate date) {
    ZoneId zone = ZoneId.systemDefault();
    return date.plusDays(1).atStartOfDay(zone).toOffsetDateTime().minusNanos(1);
  }

  private static void addHeader(PdfPTable t, String txt, Font f) {
    PdfPCell c = new PdfPCell(new Phrase(txt, f));
    c.setHorizontalAlignment(Element.ALIGN_CENTER);
    c.setPadding(6);
    t.addCell(c);
  }

  private static void addCell(PdfPTable t, String txt, Font f, int align) {
    PdfPCell c = new PdfPCell(new Phrase(txt != null ? txt : "-", f));
    c.setHorizontalAlignment(align);
    c.setPadding(5);
    t.addCell(c);
  }

  private static BigDecimal nz(BigDecimal v) {
    return v != null ? v : BigDecimal.ZERO;
  }

  private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

  private static String fmtMoney(BigDecimal v) {
    return MONEY.format(nz(v));
  }
}
