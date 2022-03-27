package com.alexander.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    Connection con;
    String url="jdbc:mysql://localhost:3306/bdcarritocompras";
    String user="root";
    String pass="El_Conde27**";
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            con=DriverManager.getConnection(url,user,pass);
        } catch (Exception e) {
        }
        return con;
    }
}
