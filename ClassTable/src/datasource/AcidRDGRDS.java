package datasource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseException;
import database.DatabaseManager;

/**
 * AcidRowDataGatewayRDS 
 * @author Isabella Boone, Kim O'Neill
 */
public class AcidRDGRDS implements AcidRDG {
  private AcidDTO acid;
  
  /**
   * Empty constructor
   */
  public AcidRDGRDS() {
    
  }
  
  /**
   * Constructor used to find an existing Acid.
   * @param id of the acid to find
   */
  public AcidRDGRDS(int id) throws SQLException, DatabaseException {
    int solute; 

    // Statements to find existing acid/chemical and collect their information.
    String getAcid = new String("SELECT * FROM Acid WHERE acidId = " + id + ";"),
        getChem = new String("SELECT * FROM Chemical WHERE chemicalId = " + id + ";");

    // Get acid information
    Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
    ResultSet rs = statement.executeQuery(getAcid);
    rs.next(); // Get result
    solute = rs.getInt("solute");

    // Get chemical information
    rs = statement.executeQuery(getChem);
    rs.next();
    acid = new AcidDTO(id, solute, rs.getString("name"), rs.getDouble("inventory"));

  }
  
  /**
   * AcidRowDataGateway constructor for creating a new acid.
   * @param id of acid to insert
   * @param solute of acid to insert
   * @param name of acid to insert
   * @param inhabits of acid to insert
   */
  public AcidRDGRDS(int id, int solute, String name, double inventory) {
    
    try {
      // Insert chemical 
      PreparedStatement insertChemical = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("INSERT INTO Chemical (chemicalId, name, inventory)" + " VALUES (?, ?, ?);");
      insertChemical.setInt(1, id);
      insertChemical.setString(2, name);
      insertChemical.setDouble(3, inventory);
      
      // Insert Acid
      PreparedStatement insertAcid = DatabaseManager.getSingleton().getConnection()
        .prepareStatement("INSERT INTO Acid (acidId, solute)" + " VALUES (?, ?);");
      insertAcid.setInt(1, id); // Set acid id
      insertAcid.setInt(2, solute); // set solute id
      
      insertChemical.execute(); // Insert chemical
      insertAcid.execute();  // Insert acid
      
      acid = new AcidDTO(id, solute, name, inventory);
      
    } catch(SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to insert acid through constructor");
    }
  }
	
	/**
	 * Delete an acid from both acid and chemical tables
	 */
  @Override
  public void delete() {
    String deleteChemical = "DELETE FROM Chemical WHERE ChemicalId = " + acid.getAcidId() + ";",
        deleteAcid = "DELETE FROM Acid WHERE AcidId = " + acid.getAcidId() + ";";
    
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
      statement.executeUpdate(deleteAcid);
      statement.executeUpdate(deleteChemical);
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
      
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Error deleting acid " + acid.getAcidId());
    }
    
    acid = null;
  }

  /**
   * Update the database.
   */
  @Override
  public void update() {
    String updateChemicalSQL = "UPDATE Chemical SET chemicalId = ?, name = ?, inventory = ? WHERE chemicalID = " + acid.getAcidId() + ";";
    String updateAcidSQL = "UPDATE Acid SET acidId = ?, solute = ? WHERE acidId = " + acid.getAcidId() + ";";
    
    try {
      // Chemical
      PreparedStatement chem = DatabaseManager.getSingleton().getConnection().prepareStatement(updateChemicalSQL);
      chem.setInt(1, acid.getAcidId());
      chem.setString(2, acid.getName());
      chem.setDouble(3, acid.getInventory());

      chem.execute();
      
      // Acid
      PreparedStatement acid = DatabaseManager.getSingleton().getConnection().prepareStatement(updateAcidSQL);
      acid.setInt(1, this.acid.getAcidId());
      acid.setInt(2, this.acid.getSoluteId());
      
      acid.execute();
      
      
    } catch(SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to update acid");
    }

  }
  
  /**
   * Find a set of ids that are dissolved by the same solute
   * @param solute
   * @return
   */
  public List<AcidRDGRDS> findSet(int solute) {
    List<AcidRDGRDS> results = new ArrayList<>();
    try {
      String sql = "SELECT * FROM Acid WHERE solute = "+ solute + ";";
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);
      
      while(rs.next()) {
        int sol = rs.getInt("acidId");
        AcidRDGRDS id = new AcidRDGRDS(sol);
        results.add(id);
      }
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();

    }
    return results;
  }

  /**
   * Set solute
   */
  @Override
  public void setSolute(int newSolute) {
    acid.setSoluteId(newSolute);
  }

  /**
   * Set name
   */
  @Override
  public void setName(String newName) {
    acid.setName(newName);
  }

  /**
   * Set inhabits
   */
  @Override
  public void setInventory(double inventory) {
    acid.setInventory(inventory);
  }
  
  public AcidDTO getAcid() {
    return acid;
  }
	
}