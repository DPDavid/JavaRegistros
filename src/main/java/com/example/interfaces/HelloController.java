package com.example.interfaces;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class HelloController {
    //Atributos FXML
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
        //Establece el spinner con valores de 1 a 10, comenzando en 1
        numHabitacionesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        //Si se marca la casilla de fumador, muestra una alerta informativa
        fumadorCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "En virtud de la Ley de Sanidad se informa a los clientes de que solo podrán fumar en las habitaciones reservadas para tal fin.");
                alert.show();
            }
        });
    }


    @FXML
    //Funcion para limpiar el formulario
    protected void limpiarBoton() {
        //Borra los campos de texto
        dniTextField.clear();
        nombreTextField.clear();
        direccionTextField.clear();
        localidadTextField.clear();
        provinciaTextField.clear();

        //Borra las fechas seleccionadas
        fechaLlegadaPicker.setValue(null);
        fechaSalidaPicker.setValue(null);

        //Restaura a 1 el valor del spinner
        numHabitacionesSpinner.getValueFactory().setValue(1);

        //Limpia la selección y checkbox
        tipoHabitacionComboBox.getSelectionModel().clearSelection();
        fumadorCheckBox.setSelected(false);

        //Deselecciona todos los radio buttons
        regimenCompletaRadio.setSelected(false);
        regimenDesayunoRadio.setSelected(false);
        regimenMediaRadio.setSelected(false);
    }

    @FXML
    //Funcion para cerrar el programa
    protected void cancelarBoton() {
        Platform.exit();
    }

    @FXML
    //Funcion para enviar los datos del formulario
    protected void aceptarBoton() {
        //Conexion con la base de datos
        String url = "jdbc:mysql://localhost:3306/registros";
        String user = "root";
        String password = "";

        //Consulta sql para introducir los datos
        String sql = "INSERT INTO reservas (dni, nombre, direccion, localidad, provincia, fecha_llegada, fecha_salida, num_habitaciones, tipo_habitacion, fumador, regimen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                //Insertar los valores del formulario en la sentencia sql
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

                //Determina el regimen
                String regimen = "";
                if (regimenDesayunoRadio.isSelected()) {
                    regimen = "Desayuno";
                } else if (regimenMediaRadio.isSelected()) {
                    regimen = "Media pensión";
                } else if (regimenCompletaRadio.isSelected()) {
                    regimen = "Pensión completa";
                }

                stmt.setString(11, regimen);

                //Ejecuta el update de la consulta
                stmt.executeUpdate();

                //Muestra la confirmación
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reserva Confirmada");
                alert.setHeaderText(null);
                alert.setContentText("La reserva ha sido guardada con éxito.");
                alert.showAndWait();

                //Limpia los campos del formulario
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
