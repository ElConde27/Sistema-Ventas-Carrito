package com.alexander.modelo;

import com.alexander.config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClienteDAO {

    Connection con;
    Conexion cn = new Conexion();
    PreparedStatement ps;
    ResultSet rs;

    public Cliente Validar(String email, String pass) {
        String sql="select * from cliente where Email=? and Password=?";
        Cliente c=new Cliente();
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while (rs.next()) {
                c.setId(rs.getInt(1));
                c.setDni(rs.getString(2));
                c.setNombres(rs.getString(3));
                c.setDireccion(rs.getString(4));
                c.setEmail(rs.getString(5));
                c.setPass(rs.getString(6));
            }
        } catch (Exception e) {
        }
        return c;        
    }
    public int AgregarCliente(Cliente c) {
        String sql="INSERT INTO cliente (Dni, Nombres, Direccion, Email, Password)values(?,?,?,?,?)";        
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,c.getDni());
            ps.setString(2, c.getNombres());
            ps.setString(3, c.getDireccion());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getPass());
            ps.executeUpdate();
        } catch (Exception e) {
        }
        return 1;        
    }
}
