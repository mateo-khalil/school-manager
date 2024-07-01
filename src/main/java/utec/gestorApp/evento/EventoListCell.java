package utec.gestorApp.evento;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import utec.gestorApp.ui.EventoControllerFx;

public class EventoListCell extends ListCell<Evento> {
    private HBox content;
    private Text nameAndType;
    private Button opcionesButton;
    private EventoControllerFx controller;

    public EventoListCell(EventoControllerFx controller) {
        super();
        this.controller = controller;
        nameAndType = new Text();
        opcionesButton = new Button("Opciones");
        opcionesButton.setOnAction(event -> {
            Evento evento = getItem();
            if (evento != null) {
                controller.mostrarOpcionesEvento(evento);
            }
        });
        content = new HBox(nameAndType, opcionesButton);
        content.setSpacing(10);
    }

    @Override
    protected void updateItem(Evento item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            nameAndType.setText(item.getNombre());
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }
}