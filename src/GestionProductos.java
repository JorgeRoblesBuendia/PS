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
import java.sql.*;
import java.util.Random;

public class GestionProductos {

    // Conexión a la BD
    private static final String URL = "jdbc:mysql://belbr9kwb1stmqbvm6si-mysql.services.clever-cloud.com:3306/belbr9kwb1stmqbvm6si?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String USER = "uakgprfg2wghdbl8";
    private static final String PASSWORD = "GbNuYm3g8kkcG1jRi14n";

    // Modelo de tabla
    private static DefaultTableModel modeloTabla;

    // Campos para entrada de datos
    private static JTextField nombreText, precioCompraText, precioVentaText, stockText, stockMinimoText, idCategoriaText, idProveedorText, codigoBarrasText, fechaVencimientoText, buscarText;
    private static JTable tabla;

    public static void main(String[] args) {
        // Crear ventana
        JFrame frame = new JFrame("Gestión de Productos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // Colocar componentes
        colocarComponentes(panel);

        // Cargar datos desde la BD
        cargarDatosDesdeBD();

        frame.setVisible(true);
    }

    private static void colocarComponentes(JPanel panel) {
        JLabel tituloLabel = new JLabel("Gestión de Productos");
        tituloLabel.setBounds(300, 10, 300, 25);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(tituloLabel);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(20, 50, 100, 25);
        panel.add(nombreLabel);

        nombreText = new JTextField(20);
        nombreText.setBounds(120, 50, 160, 25);
        panel.add(nombreText);

        JLabel precioCompraLabel = new JLabel("Precio Compra:");
        precioCompraLabel.setBounds(20, 90, 100, 25);
        panel.add(precioCompraLabel);

        precioCompraText = new JTextField(20);
        precioCompraText.setBounds(120, 90, 160, 25);
        panel.add(precioCompraText);

        JLabel precioVentaLabel = new JLabel("Precio Venta:");
        precioVentaLabel.setBounds(20, 130, 100, 25);
        panel.add(precioVentaLabel);

        precioVentaText = new JTextField(20);
        precioVentaText.setBounds(120, 130, 160, 25);
        panel.add(precioVentaText);

        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setBounds(20, 170, 100, 25);
        panel.add(stockLabel);

        stockText = new JTextField(20);
        stockText.setBounds(120, 170, 160, 25);
        panel.add(stockText);

        JLabel stockMinimoLabel = new JLabel("Stock Mínimo:");
        stockMinimoLabel.setBounds(20, 210, 100, 25);
        panel.add(stockMinimoLabel);

        stockMinimoText = new JTextField(20);
        stockMinimoText.setBounds(120, 210, 160, 25);
        panel.add(stockMinimoText);

        JLabel idCategoriaLabel = new JLabel("ID Categoría:");
        idCategoriaLabel.setBounds(20, 250, 100, 25);
        panel.add(idCategoriaLabel);

        idCategoriaText = new JTextField(20);
        idCategoriaText.setBounds(120, 250, 160, 25);
        panel.add(idCategoriaText);

        JLabel idProveedorLabel = new JLabel("ID Proveedor:");
        idProveedorLabel.setBounds(20, 290, 100, 25);
        panel.add(idProveedorLabel);

        idProveedorText = new JTextField(20);
        idProveedorText.setBounds(120, 290, 160, 25);
        panel.add(idProveedorText);

        JLabel codigoBarrasLabel = new JLabel("Código Barras:");
        codigoBarrasLabel.setBounds(20, 330, 100, 25);
        panel.add(codigoBarrasLabel);

        codigoBarrasText = new JTextField(20);
        codigoBarrasText.setBounds(120, 330, 160, 25);
        panel.add(codigoBarrasText);

        JLabel fechaVencimientoLabel = new JLabel("Fecha Vencimiento:");
        fechaVencimientoLabel.setBounds(20, 370, 120, 25);
        panel.add(fechaVencimientoLabel);

        fechaVencimientoText = new JTextField(20);
        fechaVencimientoText.setBounds(150, 370, 130, 25);
        panel.add(fechaVencimientoText);

        // Botones
        JButton agregarButton = new JButton("Agregar");
        agregarButton.setBounds(320, 50, 120, 25);
        panel.add(agregarButton);

        JButton actualizarButton = new JButton("Actualizar");
        actualizarButton.setBounds(320, 90, 120, 25);
        panel.add(actualizarButton);

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.setBounds(320, 130, 120, 25);
        panel.add(eliminarButton);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.setBounds(320, 170, 120, 25);
        panel.add(buscarButton);

        JLabel buscarLabel = new JLabel("Buscar:");
        buscarLabel.setBounds(320, 210, 60, 25);
        panel.add(buscarLabel);

        buscarText = new JTextField(20);
        buscarText.setBounds(380, 210, 160, 25);
        panel.add(buscarText);

        // Tabla
        String[] columnas = {"ID", "Nombre", "Precio Compra", "Precio Venta", "Stock", "Stock Minimo", "ID Categoria", "ID Proveedor", "Código Barras", "Fecha Vencimiento"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBounds(20, 420, 740, 130);
        panel.add(scrollPane);

        // Acciones de los botones
        agregarButton.addActionListener(e -> agregarProducto());
        actualizarButton.addActionListener(e -> actualizarProducto());
        eliminarButton.addActionListener(e -> eliminarProducto());
        buscarButton.addActionListener(e -> buscarProducto());
    }

    // Cargar datos desde la BD
    private static void cargarDatosDesdeBD() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM Productos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getDouble("precioCompra"),
                        rs.getDouble("precioVenta"),
                        rs.getInt("stock"),
                        rs.getInt("stockMinimo"),
                        rs.getInt("idCategoria"),
                        rs.getInt("idProveedor"),
                        rs.getString("codigoBarras"),
                        rs.getDate("fechaVencimiento")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar productos: " + e.getMessage());
        }
    }

    // Método para agregar producto
    private static void agregarProducto() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Productos (nombre, precioCompra, precioVenta, stock, stockMinimo, idCategoria, idProveedor, codigoBarras, fechaVencimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreText.getText());
            stmt.setDouble(2, Double.parseDouble(precioCompraText.getText()));
            stmt.setDouble(3, Double.parseDouble(precioVentaText.getText()));
            stmt.setInt(4, Integer.parseInt(stockText.getText()));
            stmt.setInt(5, Integer.parseInt(stockMinimoText.getText()));
            stmt.setInt(6, Integer.parseInt(idCategoriaText.getText()));
            stmt.setInt(7, Integer.parseInt(idProveedorText.getText()));
            stmt.setString(8, codigoBarrasText.getText());
            stmt.setString(9, fechaVencimientoText.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
            modeloTabla.setRowCount(0);
            cargarDatosDesdeBD();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar producto: " + e.getMessage());
        }
    }

    // Método para actualizar producto
    private static void actualizarProducto() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idProducto = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "UPDATE Productos SET nombre=?, precioCompra=?, precioVenta=?, stock=?, stockMinimo=?, idCategoria=?, idProveedor=?, codigoBarras=?, fechaVencimiento=? WHERE idProducto=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nombreText.getText());
                stmt.setDouble(2, Double.parseDouble(precioCompraText.getText()));
                stmt.setDouble(3, Double.parseDouble(precioVentaText.getText()));
                stmt.setInt(4, Integer.parseInt(stockText.getText()));
                stmt.setInt(5, Integer.parseInt(stockMinimoText.getText()));
                stmt.setInt(6, Integer.parseInt(idCategoriaText.getText()));
                stmt.setInt(7, Integer.parseInt(idProveedorText.getText()));
                stmt.setString(8, codigoBarrasText.getText());
                stmt.setString(9, fechaVencimientoText.getText());
                stmt.setInt(10, idProducto);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
                modeloTabla.setRowCount(0);
                cargarDatosDesdeBD();
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
                String sql = "DELETE FROM Productos WHERE idProducto=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idProducto);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
                modeloTabla.setRowCount(0);
                cargarDatosDesdeBD();
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
            String sql = "SELECT * FROM Productos WHERE nombre LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nombreBuscar + "%");
            ResultSet rs = stmt.executeQuery();

            modeloTabla.setRowCount(0);
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getDouble("precioCompra"),
                        rs.getDouble("precioVenta"),
                        rs.getInt("stock"),
                        rs.getInt("stockMinimo"),
                        rs.getInt("idCategoria"),
                        rs.getInt("idProveedor"),
                        rs.getString("codigoBarras"),
                        rs.getDate("fechaVencimiento")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar producto: " + e.getMessage());
        }
    }
}