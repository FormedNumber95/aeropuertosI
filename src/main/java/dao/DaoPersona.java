package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.ConexionBBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelos.ModeloPersona;

/**
 * Clase DaoPersona.
 */
public class DaoPersona {

	/**
	 * Cargar lista de personas.
	 *
	 * @return the observable list
	 */
	public static ObservableList<ModeloPersona> cargarListaPersonas(){
		ConexionBBDD connection;
        ObservableList<ModeloPersona> listadoDePersonas= FXCollections.observableArrayList();
        try {
			connection = new ConexionBBDD();
			String consulta = "SELECT nombre,apellidos,edad FROM Persona";
			 PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
             ResultSet rs = pstmt.executeQuery();
             while (rs.next()) {
                 String nombre = rs.getString("nombre");
                 String apellidos = rs.getString("apellidos");
                 String edad = rs.getString("edad");
                 ModeloPersona mp = new ModeloPersona(nombre,apellidos,Integer.parseInt(edad));
                 listadoDePersonas.add(mp);

             }
             rs.close();
             pstmt.close();
             connection.CloseConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listadoDePersonas;
	}
	
	/**
	 * Eliminar persona.
	 *
	 * @param p La persona
	 * @return true, if successful
	 */
	public static boolean eliminar(ModeloPersona p) {
		ConexionBBDD connection;
		int exito=0;
			try {
				connection = new ConexionBBDD();
				String id=conseguirID(p, connection);
				String consulta2="DELETE FROM Persona WHERE id=? ";
				PreparedStatement pstmt2=connection.getConnection().prepareStatement(consulta2);
				pstmt2.setString(1, id);
				exito=pstmt2.executeUpdate();
				pstmt2.close();
				connection.CloseConexion();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return exito>0;
		}

	/**
	 * Conseguir ID.
	 *
	 * @param p La persona
	 * @param connection la conexion
	 * @return the id
	 */
	private static String conseguirID(ModeloPersona p, ConexionBBDD connection){
		String consulta1="SELECT id FROM personas.Persona WHERE "
				+ "nombre=? AND apellidos=? AND edad=?";
		try {
			PreparedStatement pstmt=connection.getConnection().prepareStatement(consulta1);
			pstmt.setString(1, p.getNombre());
			pstmt.setString(2, p.getApellidos());
			pstmt.setInt(3, p.getEdad());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String id=rs.getString("id");
				pstmt.close();
				return id;
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Aniadir persona.
	 *
	 * @param persona La persona
	 * @return true, if successful
	 */
	public static boolean aniadir(ModeloPersona persona) {
		ConexionBBDD connection;
		int exito=0;
		try {
			connection = new ConexionBBDD();
			String consulta="INSERT INTO Persona (nombre,apellidos,edad) VALUES (?,?,?)";
			PreparedStatement pstmt;
			pstmt = connection.getConnection().prepareStatement(consulta);
			pstmt.setString(1, persona.getNombre());
			pstmt.setString(2, persona.getApellidos());
			pstmt.setInt(3, persona.getEdad());
			exito=pstmt.executeUpdate();
			pstmt.close();
			connection.CloseConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exito>0;
	}
	
	/**
	 * Modificar persona.
	 *
	 * @param p La persona
	 * @param personaAModificar La persona a modificar
	 * @return true, if successful
	 */
	public static boolean modificar(ModeloPersona p,ModeloPersona personaAModificar) {
		ConexionBBDD connection;
		int exito=0;
		try {
			connection = new ConexionBBDD();
			String id=conseguirID(personaAModificar, connection);
			String consulta="UPDATE personas.Persona SET nombre=?,apellidos=?,edad=? WHERE id=?";
			PreparedStatement pstmt=connection.getConnection().prepareStatement(consulta);
			pstmt.setString(1, p.getNombre());
			pstmt.setString(2, p.getApellidos());
			pstmt.setInt(3, p.getEdad());
			pstmt.setString(4, id);
			exito=pstmt.executeUpdate();
			pstmt.close();
			connection.CloseConexion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exito>0;
	}
	
}
