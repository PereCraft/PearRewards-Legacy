
package pearrewards.connection;

import pearrewards.persistence.ConfigurationFile;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;



class ConnectionMySQL implements IConnection {
    
    private Connection connection;
    private String username;
    private String password;
    private String url;

    public ConnectionMySQL(ConfigurationFile config) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        
        this.username = config.getConfig().getString("connect_db.mysql.username");
        this.password = config.getConfig().getString("connect_db.mysql.password");
        this.url = "jdbc:mysql://localhost:3306/" + 
                config.getConfig().getString("connect_db.mysql.database");
    }
    
    // GETTER E SETTER //

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    // PRENDO TABELLA DB //
    
    @Override
    public void checkDBexist() throws SQLException {

        connection = (Connection)DriverManager.getConnection(url, username, password);
        String sql = "CREATE TABLE IF NOT EXISTS PearRewards(ID int NOT NULL AUTO_INCREMENT, "
                + "Username varchar(20) NOT NULL, Date DATE, NumRewards int NOT NULL, "
                + "ReedemRewards int NOT NULL, PRIMARY KEY (ID))";
                
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }
    
    @Override
    public boolean checkUserExist(String username) throws SQLException{
        String sql = "SELECT ID FROM PearRewards WHERE Username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        
        ResultSet res = stmt.executeQuery();
        return res.next();
    }
    
    @Override
    public void createUser(String username) throws SQLException {
        
        LocalDate date = LocalDate.now();
        
        String sql = "INSERT INTO PearRewards (Username, Date, NumRewards, ReedemRewards) VALUES (?, ?, 1, 0)";        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        
        stmt.executeUpdate();                
    }
    
    @Override
    public Date getUserDate(String username) throws SQLException {
                
        String sql = "SELECT Date FROM PearRewards WHERE Username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        
        ResultSet res = stmt.executeQuery();
        if(res.next()) {
            return res.getDate("Date");
        } else {
            throw new SQLException();
        }    
    }
    
    @Override
    public boolean updateDate(String username) {
        
        try {
                String sql = "UPDATE PearRewards SET Date = ? WHERE Username = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                stmt.setString(2, username);

                stmt.executeUpdate();
                return true;
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            return false;
        }
        
    }
    
    @Override
    public int getNumRewards(String username) {
        
        try { 
            String sql = "SELECT NumRewards FROM PearRewards WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            ResultSet res = stmt.executeQuery();
            
            if(res.next()) {
                return res.getInt("NumRewards");
            } else {
                System.err.println("[PearRewards] Error while taking NumRewards");
                return 1;
            }        
            
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            return 1;
        }
        
    }
    
    @Override
    public void incrementNumRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET NumRewards = NumRewards+1 WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public void resetNumRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET NumRewards = 1 WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public void incrementReedemRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET ReedemRewards = ? WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, getNumRewards(username));
            stmt.setString(2, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public void resetReedemRewards(String username) {
        
        try {
            String sql = "UPDATE PearRewards SET ReedemRewards = 0 WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            stmt.executeUpdate();
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
        }
        
    }
    
    @Override
    public int getReedemRewards(String username) {
        
        try { 
            String sql = "SELECT ReedemRewards FROM PearRewards WHERE Username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            
            ResultSet res = stmt.executeQuery();
            if(res.next()) {
                return res.getInt("ReedemRewards");
            } else {
                System.err.println("[PearRewards] Error while taking NumRewards");
                return 1;
            }  
        } catch(SQLException ex) {
            System.err.println("[PearRewards] Error: " + ex.getMessage());
            return 1;
        }
        
    }
    
}
