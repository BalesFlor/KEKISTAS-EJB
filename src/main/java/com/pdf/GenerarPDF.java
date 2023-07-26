package com.pdf;

import com.grsc.logica.ejb.UsuarioBean;
import com.grsc.modelo.entities.Reclamo;
import com.grsc.modelo.entities.Usuarios;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;

public class GenerarPDF {
    
    public void generar(String tit, String parrafo, List<Reclamo> listaReclamos, String rutaImg) {
        
        Paragraph titulo = new Paragraph(tit);
        Document documento = new Document();
        FileOutputStream archivo;
        
        try {
            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Reporte_Alumnos.pdf"));
            documento.open();
            titulo.setAlignment(1);
            
            Image img = null;
            try{
                img = Image.getInstance(rutaImg);
                img.scaleAbsolute(150, 100);
                img.setAbsolutePosition(415, 750);
                
            }catch(Exception e){
            }
            
            documento.add(img);
            documento.add(titulo);
            documento.add(new Paragraph(parrafo));
            
            documento.add(Chunk.NEWLINE);
            
            PdfPTable tabla = new PdfPTable(6);
            tabla.setWidthPercentage(100);
            
            PdfPCell fecha = new PdfPCell(new Phrase("Fecha/Hora")); 
            fecha.setBackgroundColor(BaseColor.CYAN);
            PdfPCell nombre = new PdfPCell(new Phrase("Usuario")); 
            nombre.setBackgroundColor(BaseColor.CYAN);
            PdfPCell tituloReclamo = new PdfPCell(new Phrase("Titulo")); 
            tituloReclamo.setBackgroundColor(BaseColor.CYAN);
            PdfPCell detalle = new PdfPCell(new Phrase("Detalle")); 
            detalle.setBackgroundColor(BaseColor.CYAN);
            PdfPCell estado = new PdfPCell(new Phrase("Estado")); 
            estado.setBackgroundColor(BaseColor.CYAN);
            PdfPCell evento = new PdfPCell(new Phrase("Evento Relacionado")); 
            evento.setBackgroundColor(BaseColor.CYAN);

            tabla.addCell(fecha);
            tabla.addCell(nombre);
            tabla.addCell(tituloReclamo);
            tabla.addCell(detalle);
            tabla.addCell(estado);
            tabla.addCell(evento);
            
            UsuarioBean userBean = new UsuarioBean();
            
            for(Reclamo rec: listaReclamos){
                tabla.addCell(rec.getFechaHora().toString());
                tabla.addCell(userBean.buscarUsuario(rec.getIdUsuario().getIdUsuario()).getNombre1() + " " 
                        + userBean.buscarUsuario(rec.getIdUsuario().getIdUsuario()).getApellido1());
                tabla.addCell(rec.getTitulo());
                tabla.addCell(rec.getDetalle());
                tabla.addCell(rec.getIdEstadoPeticion().getNomEstado());
                tabla.addCell(rec.getIdEvento().getTitulo());
            }
            documento.add(tabla);
            documento.add(new Paragraph("Fecha: "+ Calendar.getInstance().getTime().toString()));
            
            documento.close();
             JOptionPane.showMessageDialog(null, "Archivo PDF creado correctamente!",
                    "Éxito!", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch(DocumentException e){
            System.err.println(e.getMessage());        
        }
    }

}
