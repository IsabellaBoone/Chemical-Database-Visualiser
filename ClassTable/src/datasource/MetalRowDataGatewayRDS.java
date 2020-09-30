package datasource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author kimberlyoneill
 *
 */
public class MetalRowDataGatewayRDS implements MetalRowDataGateway {

  private int metalId;
  private int dissolvedById;
  private String name;
  private String inhabits;

  /**
   * empty constructor drops the table and recreates it
   */
  public MetalRowDataGatewayRDS() {
    createTableMetal();
  }

  /**
   * finds a metal
   * 
   * @param id
   */
  public MetalRowDataGatewayRDS(int id) {
    this.createTableMetal();
    this.metalId = id;

    String sqlChem = "SELECT * FROM Chemical INNER JOIN Metal ON Chemical.chemicalId = " + id + ";";
    String sqlElement = "SELECT * FROM Metal where metalId = " + id + ";";
    try {

      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sqlElement);
      rs.next();
      this.dissolvedById = rs.getInt("dissolvedBy");

      rs = statement.executeQuery(sqlChem);
      rs.next();
      this.name = rs.getString("name");
      this.inhabits = rs.getString("inhabits");

    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("No entry with id " + id);
    }

  }

  /**
   * constructor to create a metal
   * 
   * @param id
   * @param dissolvedById
   * @param name
   * @param inhabits
   */
  public MetalRowDataGatewayRDS(int id, int dissolvedById, String name, String inhabits) {
    this.createTableMetal();
    try {
      PreparedStatement insertChemical = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("INSERT INTO Chemical (chemicalId, name, inhabits)" + "VALUES (?, ?, ?);");
      insertChemical.setInt(1, id);
      insertChemical.setString(2, name);
      insertChemical.setString(3, inhabits);
      PreparedStatement insert = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("INSERT INTO Metal (metalId, dissolvedBy)" + "VALUES (?, ?);");

      insert.setInt(1, id);
      insert.setInt(2, dissolvedById);

      insertChemical.execute();
      insert.execute();
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to insert");
    }
  }

  @Override
  public void createTableMetal() {
    String createTable = "CREATE TABLE IF NOT EXISTS Metal" + "(" + "metalId INT NOT NULL, " + "dissolvedBy INT,"
        + "FOREIGN KEY(dissolvedBy) REFERENCES Acid(acidId)," + "FOREIGN KEY(metalId) REFERENCES Chemical(chemicalId)"
        + ");";

    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      // Drop the table if exists first
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
      // Create new Monitorings Table
      statement.executeUpdate(createTable);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * drop metal table if it exists
   */
  @Override
  public void dropTableMetal() {
    String dropTable = "DROP TABLE IF EXISTS Metal;";
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
      statement.executeUpdate(dropTable);
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Error dropping metal table");
    }
  }

  /**
   * Drop the chemical table if it exists.
   */

  @Override
  public void dropTableChemical() {
    String dropTable = "DROP TABLE IF EXISTS Chemical;";
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
      statement.executeUpdate(dropTable);
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Error dropping chemical table");
    }
  }

  /**
   * Drop metal and all tables connected (metal & chemical)
   */
  @Override
  public void dropAllTables() {
    dropTableMetal();
    dropTableChemical();
  }

  /**
   * Deletes the metal
   */
  @Override
  public void delete() {

    String sqlMetal = "DELETE FROM Metal WHERE metalId = " + this.metalId + ";";
    String sqlChem = "DELETE FROM Chemical WHERE chemicalId = " + this.metalId + ";";
    try {

      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      statement.executeUpdate(sqlMetal);
      statement.executeUpdate(sqlChem);

    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Problem deleting Metal with id " + this.metalId);
    }
  }

  /**
   * updates metal with given values
   */
  @Override
  public void update(int id, int dissolvedById, String name, String inhabits) {
    try {
      PreparedStatement updateMetal = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("UPDATE Element SET atomicNumber = ?, atomicMass = ? WHERE elementId = ?;");
      updateMetal.setInt(1, dissolvedById);
      updateMetal.setInt(2, id);

      PreparedStatement updateChemical = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("UPDATE Chemical SET name = ?, inhabits = ? WHERE chemicalId = ?;");
      updateChemical.setString(1, name);
      updateChemical.setString(2, inhabits);
      updateChemical.setInt(3, id);

      updateMetal.execute();
      updateChemical.execute();
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to update");
    }

  }

  /**
   * finds all the metals dissolved by an Acid
   * 
   * @param dissolvedById
   *          an Acid
   * @return list of MetalRowDataGatewayRDS that contain the metals dissolved by
   *         the given acid
   */
  public List<MetalRowDataGatewayRDS> findSet(int dissolvedById) {
    List<MetalRowDataGatewayRDS> results = new ArrayList<>();
    try {
      String sql = "SELECT * FROM Metal WHERE dissolvedBy = " + dissolvedById + ";";
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);
      while (rs.next()) {
        MetalRowDataGatewayRDS metalRDS = new MetalRowDataGatewayRDS(rs.getInt("metalId"));
        results.add(metalRDS);
      }
    } catch (SQLException | DatabaseException e) {

    }
    return results;
  }
  
  /**
   * getter for name
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * getter for inhabits
   */
  @Override
  public String getInhabits() {
    return this.inhabits;
  }

  /**
   * getter for name
   */
  @Override
  public int getDissolvedBy() {
    return this.dissolvedById;
  }

}
