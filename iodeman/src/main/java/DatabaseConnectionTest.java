/**
 * 
 */

import java.sql.* ; 
public class DatabaseConnectionTest {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/";
    //  Database credentials
    static final String USER = "root";
    static final String PASS = "root";

    public DatabaseConnectionTest(){
        Connection con = null;
        Statement stmt = null;
        try{
            Class.forName(JDBC_DRIVER);
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        try{
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static void main(String[] args) {
    	new DatabaseConnectionTest();
    }
}