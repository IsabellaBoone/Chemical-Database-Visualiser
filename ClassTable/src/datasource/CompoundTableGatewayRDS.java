package datasource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CompoundTableGatewayRDS extends CompoundTableDataGateway {

  @Override
  public void createTableCompound() {
    String dropTable = "DROP TABLE IF EXISTS Compound;";
    String createTable = "CREATE TABLE Compound" + "(" + "compoundId INT NOT NULL, " + "madeOf VARCHAR(20), "
        + "FOREIGN KEY(compoundId) REFERENCES Chemical(chemicalId)" + ");"; // Not sure how to store madeOf (list of
                                                                            // elements)

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

  @Override
  public void insert(int compoundId, String madeOf) {

    try {
      
      PreparedStatement insert = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("INSERT INTO Compound (compoundId, madeOf)" + "VALUES (?, ?);");
      insert.setInt(1, compoundId);
      insert.setString(2, madeOf);
      
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (DatabaseException e) {
      e.printStackTrace();
    }

  }

  @Override
  public String getMadeOf(int id) {
    String sql = "";
    sql = "SELECT * FROM Compound WHERE compoundId = " + id + ";";
    String madeOf = "";
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);
      rs.next();
      madeOf = rs.getString(0);
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
    }
    return madeOf;
  }

  @Override
  public String getName(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getInhabits(int id) {
    // TODO Auto-generated method stub
    return null;
  }
}
