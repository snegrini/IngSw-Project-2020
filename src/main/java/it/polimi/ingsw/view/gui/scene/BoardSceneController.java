package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class BoardSceneController implements ViewGuiController {

    private View view;

    @FXML
    private GridPane boardGrid;

    @FXML
    public void initialize() {
        boardGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onSpaceClick(event));
    }

    private void onSpaceClick(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer row = GridPane.getRowIndex(clickedNode);
        Integer col = GridPane.getColumnIndex(clickedNode);

        System.out.print("Row: " + row);
        System.out.println(", Col: " + col);
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }
}
