package com.example.interfaces;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {
    @FXML private TextField dniTextField;
    @FXML private TextField nombreTextField;
    @FXML private TextField direccionTextField;
    @FXML private TextField localidadTextField;
    @FXML private TextField provinciaTextField;
    @FXML private DatePicker fechaLlegadaPicker;
    @FXML private DatePicker fechaSalidaPicker;
    @FXML private Spinner<Integer> numHabitacionesSpinner;
    @FXML private ComboBox<String> tipoHabitacionComboBox;
    @FXML private CheckBox fumadorCheckBox;
    @FXML private RadioButton regimenDesayunoRadio;
    @FXML private RadioButton regimenMediaRadio;
    @FXML private RadioButton regimenCompletaRadio;

    @FXML
    protected void initialize() {
        numHabitacionesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        fumadorCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "En virtud de la Ley de Sanidad se informa a los clientes de que solo podrán fumar en las habitaciones reservadas para tal fin.");
                alert.show();
            }
        });
    }


    @FXML
    protected void limpiarBoton() {
        dniTextField.clear();
        nombreTextField.clear();
        direccionTextField.clear();
        localidadTextField.clear();
        provinciaTextField.clear();
        fechaLlegadaPicker.setValue(null);
        fechaSalidaPicker.setValue(null);
        numHabitacionesSpinner.getValueFactory().setValue(1);
        tipoHabitacionComboBox.getSelectionModel().clearSelection();
        fumadorCheckBox.setSelected(false);

        regimenCompletaRadio.setSelected(false);
        regimenDesayunoRadio.setSelected(false);
        regimenMediaRadio.setSelected(false);
    }
    @FXML
    protected void cancelarBoton() {
        Platform.exit();
    }

    @FXML
    protected void aceptarBoton(){

    }
}