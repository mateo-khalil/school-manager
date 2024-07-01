package utec.gestorApp.reporte;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import utec.gestorApp.evento.Evento;
import utec.gestorApp.evento.EventoService;
import utec.gestorApp.usuario.Usuario;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReportGenerator {

    private EventoService eventoService;

    public ReportGenerator(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public void generarReporte(Evento evento, List<Usuario> usuariosInscritos, Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float yPosition = yStart;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float cellMargin = 12f;

                String title = "Reporte del Evento: " + evento.getNombre();
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(title);
                contentStream.newLine();
                yPosition -= 20;

                for (Usuario usuario : usuariosInscritos) {
                    yPosition -= 2 * cellMargin;
                    boolean asistio = eventoService.usuarioAsistio(evento.getId(), usuario.getId());
                    String asistioStr = asistio ? "Sí" : "No";

                    String text = "Nombre: " + usuario.getUsername() + "\n"
                            + "Email: " + usuario.getEmail() + "\n"
                            + "Asistió: " + asistioStr;

                    String[] lines = text.split("\n");
                    for (String line : lines) {
                        contentStream.newLineAtOffset(0, -cellMargin);
                        contentStream.showText(line);
                    }
                }

                contentStream.endText();
                contentStream.close();

                document.save(file);
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}