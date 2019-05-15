/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GraficoTienda;

import java.sql.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import tienda.musica.Cliente;
import tienda.musica.Conexion;

/**
 * FXML Controller class
 *
 * @author DAW
 */
public class MenuClientesController implements Initializable {

    @FXML
    private TextField text_id;
    @FXML
    private TextField text_nombre;
    @FXML
    private TextField text_apellido1;
    @FXML
    private TextField text_apellido2;
    @FXML
    private TableColumn<Cliente, Integer> col_id;
    @FXML
    private TableColumn<Cliente, String> col_nombre;
    @FXML
    private TableColumn<Cliente, String> col_apellido1;
    @FXML
    private TableColumn<Cliente, String> col_apellido2;
    @FXML
    private TableColumn<Cliente, String> col_valoracion;
    @FXML
    private TableColumn<Cliente, String> col_descrip;
    @FXML
    private Button b_modificar;
    @FXML
    private Button b_anadir;
    @FXML
    private Button b_borrar;
    @FXML
    private Button b_vaciar;
    @FXML
    private Button b_guardar;
    
    private ObservableList <Cliente> tvClientes;
    @FXML
    private TableView<Cliente> tablaClientes;
    
    private final ListChangeListener <Cliente> selectorClientes = new ListChangeListener <Cliente>()
    {
        @Override
        public void onChanged (ListChangeListener.Change<? extends Cliente> c)
        {
            escribirClienteSel();
        }
    };

    /**
     * Inicializa la clase controladora.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        tvClientes = FXCollections.observableArrayList(); 
        Cliente.rellenarTabla(tvClientes);
        tablaClientes.setItems(tvClientes);
        asociarValores(); 
        
        tvClientes = tablaClientes.getSelectionModel().getSelectedItems();
        tvClientes.addListener(selectorClientes);
    }
           
    public void escribirClienteSel(){
        
        final Cliente cliente = obtenerTabla();
        Integer posicionCliente = tvClientes.indexOf(cliente);
        
        if (cliente != null)
        {
            String idCliente = Integer.toString(cliente.getId());
            
            text_id.setText(idCliente);
            text_nombre.setText(cliente.getNombre());
            text_apellido1.setText(cliente.getApellido1());
            text_apellido2.setText(cliente.getApellido2());
        }   
    }  
    
    public Cliente obtenerTabla()
    {
       if (tablaClientes != null)
       {
           List <Cliente> tabla = tablaClientes.getSelectionModel().getSelectedItems();
           
           if (tabla.size() == 1)
           {
               final Cliente clienteSel = tabla.get(0);
               return clienteSel;
           }
       }

        return null;
       
    }
            
    public void asociarValores()
    {
        col_id.setCellValueFactory(new PropertyValueFactory <Cliente, Integer>("id"));
        col_nombre.setCellValueFactory(new PropertyValueFactory <Cliente, String>("nombre"));
        col_apellido1.setCellValueFactory(new PropertyValueFactory <Cliente, String>("apellido1"));
        col_apellido2.setCellValueFactory(new PropertyValueFactory <Cliente, String>("apellido2"));
    }
    
    public Integer idMaximo()
    {
        Integer idMax = null;
        try
        {
            Conexion conexion = new Conexion();
            Connection con = conexion.conectar();
            PreparedStatement stmt = null;
            ResultSet rs = null;

            stmt = con.prepareStatement ("SELECT MAX(Id) FROM Clientes");
            rs = stmt.executeQuery();
            rs.next();
            
            idMax = rs.getInt(1) +1;
        }
        
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return idMax;
    }

    @FXML
    private void modificar(ActionEvent event) {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        botonesPrinInvisibles();
        
        try
        {
             
        }
        
        catch (Exception ex)
        {
            
        }
    }

    @FXML
    private void anadirCliente(ActionEvent event) 
    {
        botonesPrinInvisibles();
        text_id.setEditable(false);
        b_guardar.setVisible(true);
        b_guardar.setDisable(false);
        b_vaciar.setVisible(true);
        b_vaciar.setDisable(false);
        
        //if trabajador.administrador = true; else alert
        try
        {
            String idMax = Integer.toString(idMaximo());
            text_id.setText(idMax);
            text_nombre.setText(null);
            text_apellido1.setText(null);
            text_apellido2.setText(null);    
        }
        
        catch (Exception ex)
        {
            System.out.println("hbd");
        }
    }

    @FXML
    private void eliminarCliente(ActionEvent event) {
    }
    
    public void botonesPrinVisibles()
    {
        b_modificar.setVisible(true);
        b_anadir.setVisible(true);
        b_borrar.setVisible(true);
    }
    
    public void botonesPrinInvisibles()
    {
        b_modificar.setVisible(false);
        b_anadir.setVisible(false);
        b_borrar.setVisible(false);
    }

    @FXML
    private void vaciarFormulario(ActionEvent event) {
        
        String idMax = Integer.toString(idMaximo());
        text_id.setText(idMax);
        text_nombre.setText(null);
        text_apellido1.setText(null);
        text_apellido2.setText(null);
    }

    @FXML
    private void guardarCambiosModificar(ActionEvent event) 
    {
        Conexion conexion = new Conexion();
        Connection con = conexion.conectar();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try
        {
            Integer id = idMaximo();
            String nombre = text_nombre.getText();
            String apellido1 = text_apellido1.getText();
            String apellido2 = text_apellido2.getText();

            stmt = con.prepareStatement("INSERT INTO Clientes (Id, Nombre, Apellido1, Apellido2) VALUES (?, ?, ?, ?)");
            stmt.setInt (1, id);
            stmt.setString(2, nombre);
            stmt.setString(3, apellido1);
            stmt.setString(4, apellido2);
            stmt.executeUpdate();
            
            alertaInsercionCompletada();
            
        }
        
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public void alertaInsercionCompletada()
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle("INSERCIÓN COMPLEATADA");
        alert.setHeaderText(null);
        alert.setContentText("La inserción a la base de datos se ha realizado correctamente.");
        alert.showAndWait();
    }
    
    public void alertaErrorInsercion()
    {
        Alert alert = new Alert (Alert.AlertType.ERROR);
        alert.setTitle("ERROR AL INSERTAR");
        alert.setHeaderText(null);
        alert.setContentText("Ha ocurrido un error al insertar los datos.");
        alert.showAndWait();
    }
    
}
