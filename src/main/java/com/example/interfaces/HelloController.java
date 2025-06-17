package com.example.interfaces;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class HelloController {
    @FXML
    private TextField dniTextField;
    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField direccionTextField;
    @FXML
    private TextField localidadTextField;
    @FXML
    private TextField provinciaTextField;
    @FXML
    private DatePicker fechaLlegadaPicker;
    @FXML
    private DatePicker fechaSalidaPicker;
    @FXML
    private Spinner<Integer> numHabitacionesSpinner;
    @FXML
    private ComboBox<String> tipoHabitacionComboBox;
    @FXML
    private CheckBox fumadorCheckBox;
    @FXML
    private RadioButton regimenDesayunoRadio;
    @FXML
    private RadioButton regimenMediaRadio;
    @FXML
    private RadioButton regimenCompletaRadio;

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
    protected void aceptarBoton() {
        String url = "jdbc:mysql://localhost:3306/registros";
        String user = "root";
        String password = "";

        String sql = "INSERT INTO reservas (dni, nombre, direccion, localidad, provincia, fecha_llegada, fecha_salida, num_habitaciones, tipo_habitacion, fumador, regimen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, dniTextField.getText());
                stmt.setString(2, nombreTextField.getText());
                stmt.setString(3, direccionTextField.getText());
                stmt.setString(4, localidadTextField.getText());
                stmt.setString(5, provinciaTextField.getText());
                stmt.setDate(6, java.sql.Date.valueOf(fechaLlegadaPicker.getValue()));
                stmt.setDate(7, java.sql.Date.valueOf(fechaSalidaPicker.getValue()));
                stmt.setInt(8, numHabitacionesSpinner.getValue());
                stmt.setString(9, tipoHabitacionComboBox.getValue());
                stmt.setBoolean(10, fumadorCheckBox.isSelected());

                String regimen = "";
                if (regimenDesayunoRadio.isSelected()) {
                    regimen = "Desayuno";
                } else if (regimenMediaRadio.isSelected()) {
                    regimen = "Media pensión";
                } else if (regimenCompletaRadio.isSelected()) {
                    regimen = "Pensión completa";
                }

                stmt.setString(11, regimen);

                stmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reserva Confirmada");
                alert.setHeaderText(null);
                alert.setContentText("La reserva ha sido guardada con éxito.");
                alert.showAndWait();

                limpiarBoton();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo guardar la reserva.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
