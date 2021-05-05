
package pearrewards.connection;

import java.sql.Date;
import java.sql.SQLException;


public interface IConnection {
    
    
    public void checkDBexist() throws SQLException;
    
    
    public boolean checkUserExist(String username) throws SQLException;
    
    
    public void createUser(String username) throws SQLException;
    
    
    public Date getUserDate(String username) throws SQLException;
    
        
    public boolean updateDate(String username);
    
    
    public int getNumRewards(String username);
    
    
    public void incrementNumRewards(String username);
    
    
    public void resetNumRewards(String username);
    
    
    public void incrementReedemRewards(String username);
    
    
    public void resetReedemRewards(String username);
    
    
    public int getReedemRewards(String username);
    
}
