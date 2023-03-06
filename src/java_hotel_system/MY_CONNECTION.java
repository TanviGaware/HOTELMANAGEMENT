package java_hotel_system;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 1BestCsharp
 */
public class MY_CONNECTION {
    
    // in this class we will make or connection with the mysql database
    // 1 - download the mysql connector -> https://dev.mysql.com/downloads/connector/j/5.1.html
    // 2 - extract the file
    // 3 - add the connector to your project
    // 4 - open xampp
    // 5 - start apache and mysql
    // 6 - go to phpmyadmin -> http://localhost/phpmyadmin/
    // 7 - create the database
    
    // create a function to connect with mysql
    public Connection createConnection()
    {
        Connection connection = null;
        
        MysqlDataSource mds = new MysqlDataSource();
        
        mds.setServerName("localhost");
        mds.setPortNumber(3306);
        mds.setUser("root");
        mds.setPassword("");
        mds.setDatabaseName("java_hotel_db");
        
        try {
            connection = mds.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MY_CONNECTION.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
        
    }
    
}
