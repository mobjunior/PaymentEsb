/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Utilities.Logs;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 *
 * @author Prime
 */
public class Database {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultset = null;
    
        public Database() throws ClassNotFoundException, SQLException {
        connection = getConnection();
    }

    public Connection getConnection(){
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:/jboss/datasources/PaymentGateway");
           // Logging.applicationLog(Utilities.logPreString()+"Got Datasource", "", "3");
            connection = dataSource.getConnection();
           // Logging.applicationLog(Utilities.logPreString()+"Got Connection", "", "3");
            return connection;
        } catch (NamingException ex) {
           // Logging.applicationLog(Utilities.logPreString()+"NamingException Cause: "+ex.getMessage(), "", "3");
            //
          //  MyLogger.log("Radiuslogs/Database", "Database - getConnection() " + ex.getMessage(), "exceptions.log");
            return null;
        } catch (SQLException ex) {
           // MyLogger.log("Radiuslogs/Database", "Database - getConnection() " + ex.getMessage(), "exceptions.log");
           // Logging.applicationLog(Utilities.logPreString()+"SQLException Cause: "+ex.getMessage(), "", "3");
            return null;
        }
    }
    
    public int insert(String query){
        try {
            int result = 0;
            statement = connection.createStatement();
            result = statement.executeUpdate(query);
            
            return result;
        } catch (SQLException ex) {
//            MyLogger.log("Radiuslogs/Database", "Database - insert() " + ex.getMessage(), "exceptions.log");
            return 0;
        } finally {
            closeConnection();
        }
    }
    
    public ResultSet select(String query){
        try {
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);
            return resultset;
        } catch (SQLException ex) {
//            MyLogger.log("Radiuslogs/Database", "Database - select() " + ex.getMessage(), "exceptions.log");
          //  Logging.applicationLog(Utilities.logPreString()+"SQLException Cause: "+ex.getMessage(), "", "3");
            return null;
        } finally {
          //  closeConnection();
        }
    }
    
    public int count(String query){
        try {
            int result = 0;
            statement = connection.createStatement();
            resultset = statement.executeQuery(query);
            
            while(resultset.next()){
                result++;
            }
            
            return result;
        } catch (SQLException ex) {
//            MyLogger.log("Radiuslogs/Database", "Database - count() " + ex.getMessage(), "exceptions.log");
            return 0;
        } finally {
           // closeConnection();
        }

    }
    
    public int update(String query){
        try {
            int result = 0;
            statement = connection.createStatement();
            result = statement.executeUpdate(query);
            
            return result;
        } catch (SQLException ex) {
//            MyLogger.log("Radiuslogs/Database", "Database - update() " + ex.getMessage(), "exceptions.log");
            return 0;
        } finally {
            closeConnection();
        }
    }
    
    public int delete(String query){
        try {
            int result = 0;
            statement = connection.createStatement();
            result = statement.executeUpdate(query);
            
            return result;
        } catch (SQLException ex) {
//            MyLogger.log("Radiuslogs/Database", "Database - delete() " + ex.getMessage(), "exceptions.log");
            return 0;
        } finally {
            closeConnection();
        }
    }
    
    public void closeConnection(){
        try {

            if(statement != null){
                statement.close();
            }

            if(resultset != null){
                resultset.close();
            }     
            if(connection != null){
                connection.close();  
            }
        } catch (SQLException ex) {
//            MyLogger.log("Radiuslogs/Database", "Database - closeConnection() " + ex.getMessage(), "exceptions.log");
        }
    }
    
    
}
