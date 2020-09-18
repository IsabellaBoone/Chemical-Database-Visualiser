package ClassTable.src.datasource;

import java.sql.Statement;

public class BaseTableDataGateway {
  
  public void createTableBase() {
    String dropTable = "DROP TABLE IF EXISTS Base;";
    String createTable = "CREATE TABLE Base" + "(" + "chemicalID INT NOT NULL, "
        + "solute VARCHAR(20) " + ");"; // Not sure how to store solute, is a chemical
    
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      // Drop the table if exists first
      statement.executeUpdate(dropTable);
      // Create new Monitorings Table
      statement.executeUpdate(createTable);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
