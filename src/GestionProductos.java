/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author brayan
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.sql.*;
import java.util.Random;
 

public class GestionProductos {

    // URL de conexión a Clever Cloud (brayan)
   // private static final String URL = "jdbc:mysql://biqivxhkta76airtzqfb-mysql.services.clever-cloud.com:3306/biqivxhkta76airtzqfb?useSSL=false";
    //private static final String USER = "u3c1pw84rqzgvrhu";
    //private static final String PASSWORD = "3BzUet9fLujycjKuy1k8";
    
    //jorge
    private static final String URL = "jdbc:mysql://belbr9kwb1stmqbvm6si-mysql.services.clever-cloud.com:3306/belbr9kwb1stmqbvm6si?useSSL=false";
    private static final String USER = "uakgprfg2wghdbl8";
    private static final String PASSWORD = "GbNuYm3g8kkcG1jRi14n";



    // Modelo para la tabla
    private static DefaultTableModel modeloTabla;

    // Campos de entrada
    private static JTextField nombreText, precioText, cantidadText, descripcionText, buscarText;
    private static JComboBox<String> categoriaBox;
    private static JTable tabla;

    public static void main(String[] args) {
        // Crear ventana
        JFrame frame = new JFrame("Productos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // Etiqueta de título
        JLabel tituloLabel = new JLabel("VENTANA PRODUCTOS");
        tituloLabel.setBounds(250, 10, 200, 25);
        tituloLabel.setForeground(Color.BLUE);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(tituloLabel);

        // Colocar componentes
        colocarComponentes(panel);

        // Cargar datos desde la base de datos
        cargarDatosDesdeBD();

        frame.setVisible(true);
    }

    private static void colocarComponentes(JPanel panel) {
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(20, 50, 100, 25);
        panel.add(nombreLabel);

        nombreText = new JTextField(20);
        nombreText.setBounds(120, 50, 160, 25);
        panel.add(nombreText);

        JLabel categoriaLabel = new JLabel("Categoría:");
        categoriaLabel.setBounds(20, 90, 100, 25);
        panel.add(categoriaLabel);

        String[] categorias = {"","bebidas", "frituras", "galletas", "Hogar", "Otros"};
        categoriaBox = new JComboBox<>(categorias);
        categoriaBox.setBounds(120, 90, 160, 25);
        panel.add(categoriaBox);

        JLabel precioLabel = new JLabel("Precio:");
        precioLabel.setBounds(20, 130, 100, 25);
        panel.add(precioLabel);

        precioText = new JTextField(20);
        precioText.setBounds(120, 130, 160, 25);
        panel.add(precioText);

        JLabel cantidadLabel = new JLabel("Cantidad:");
        cantidadLabel.setBounds(20, 170, 100, 25);
        panel.add(cantidadLabel);

        cantidadText = new JTextField(20);
        cantidadText.setBounds(120, 170, 160, 25);
        panel.add(cantidadText);

        JLabel descripcionLabel = new JLabel("Descripción:");
        descripcionLabel.setBounds(20, 210, 100, 25);
        panel.add(descripcionLabel);

        descripcionText = new JTextField(50);
        descripcionText.setBounds(120, 210, 160, 25);
        panel.add(descripcionText);

        JLabel buscarLabel = new JLabel("Buscar:");
        buscarLabel.setBounds(320, 210, 100, 25);
        panel.add(buscarLabel);

        buscarText = new JTextField(20);
        buscarText.setBounds(370, 210, 160, 25);
        panel.add(buscarText);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.setBounds(540, 210, 100, 25);
        panel.add(buscarButton);

        JButton agregarButton = new JButton("Registrar");
        agregarButton.setBounds(320, 50, 120, 25);
        panel.add(agregarButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.setBounds(320, 90, 120, 25);
        panel.add(actualizarButton);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.setBounds(320, 130, 120, 25);
        panel.add(eliminarButton);

        String[] columnas = {"ID", "Nombre", "Categoría", "Precio", "Cantidad", "Código Barras", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBounds(20, 260, 640, 180);
        panel.add(scrollPane);

        // Acciones de los botones
        agregarButton.addActionListener(e -> agregarProducto());
        actualizarButton.addActionListener(e -> actualizarProducto());
        eliminarButton.addActionListener(e -> eliminarProducto());
        buscarButton.addActionListener(e -> buscarProducto());
    }

    // Método para cargar datos desde la base de datos
    private static void cargarDatosDesdeBD() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM productos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("codigoBarras"),
                        rs.getString("descripcion")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage());
        }
    }

    // Método para agregar producto
    private static void agregarProducto() {
        String nombre = nombreText.getText();
        String categoria = (String) categoriaBox.getSelectedItem();
        String precioStr = precioText.getText();
        String cantidadStr = cantidadText.getText();
        String descripcion = descripcionText.getText();
        String codigoBarras = generarCodigoBarras();

        if (nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "INSERT INTO productos (nombre, categoria, precio, cantidad, codigoBarras, descripcion) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, categoria);
            stmt.setDouble(3, precio);
            stmt.setInt(4, cantidad);
            stmt.setString(5, codigoBarras);
            stmt.setString(6, descripcion);
            stmt.executeUpdate();

            modeloTabla.addRow(new Object[]{nombre, categoria, precio, cantidad, codigoBarras, descripcion});
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "Producto registrado con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar producto: " + e.getMessage());
        }
    }

    // Método para actualizar producto
    private static void actualizarProducto() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombre = nombreText.getText();
            String categoria = (String) categoriaBox.getSelectedItem();
            double precio = Double.parseDouble(precioText.getText());
            int cantidad = Integer.parseInt(cantidadText.getText());
            String descripcion = descripcionText.getText();

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "UPDATE productos SET nombre=?, categoria=?, precio=?, cantidad=?, descripcion=? WHERE idProducto=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombre);
                stmt.setString(2, categoria);
                stmt.setDouble(3, precio);
                stmt.setInt(4, cantidad);
                stmt.setString(5, descripcion);
                stmt.setInt(6, idProducto);
                stmt.executeUpdate();

                modeloTabla.setValueAt(nombre, filaSeleccionada, 1);
                modeloTabla.setValueAt(categoria, filaSeleccionada, 2);
                modeloTabla.setValueAt(precio, filaSeleccionada, 3);
                modeloTabla.setValueAt(cantidad, filaSeleccionada, 4);
                modeloTabla.setValueAt(descripcion, filaSeleccionada, 6);
                limpiarCampos();
                JOptionPane.showMessageDialog(null, "Producto actualizado con éxito.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un producto para actualizar.");
        }
    }

    // Método para eliminar producto
    private static void eliminarProducto() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "DELETE FROM productos WHERE idProducto=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idProducto);
                stmt.executeUpdate();
                modeloTabla.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un producto para eliminar.");
        }
    }

    // Método para buscar productos
    private static void buscarProducto() {
        String nombreBuscar = buscarText.getText().trim();
        if (nombreBuscar.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Introduce un nombre para buscar.");
            return;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nombreBuscar + "%");
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel modeloFiltrado = new DefaultTableModel(new String[]{"ID", "Nombre", "Categoría", "Precio", "Cantidad", "Código Barras", "Descripción"}, 0);
            while (rs.next()) {
                modeloFiltrado.addRow(new Object[]{
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getString("codigoBarras"),
                        rs.getString("descripcion")
                });
            }
            tabla.setModel(modeloFiltrado);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
        }
    }

    // Generar código de barras aleatorio
    private static String generarCodigoBarras() {
        Random rand = new Random();
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            codigo.append(rand.nextInt(10));
        }
        return codigo.toString();
    }

    // Limpiar campos después de las operaciones
    private static void limpiarCampos() {
        nombreText.setText("");
        precioText.setText("");
        cantidadText.setText("");
        descripcionText.setText("");
        buscarText.setText("");
        categoriaBox.setSelectedIndex(0);
    }
}