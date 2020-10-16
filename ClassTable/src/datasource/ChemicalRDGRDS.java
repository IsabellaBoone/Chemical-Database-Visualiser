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
 * ChemicalRowDataGatewayRDS 
 * @author Isabella Boone, Kim O'Neill
 *
 */
public class ChemicalRDGRDS implements ChemicalRDG {
  ChemicalDTO chemical;
  
  /**
   * Create table
   */
  public ChemicalRDGRDS() {
    
  }
  
  /**
   * Constructor ChemicalRowDataGatewayRDS, search for existing chemical.
   * @param id to search for
   */
  public ChemicalRDGRDS(int id) throws SQLException, DatabaseException {
    
    // Select statement
    String getChem = new String("SELECT * FROM Chemical WHERE chemicalId = " + id + ";");

    // Get chemical information
    Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
    ResultSet rs = statement.executeQuery(getChem);
    rs.next();

    chemical = new ChemicalDTO(id, rs.getString("name"), rs.getDouble("inventory"));

  }
  
  /**
   * Constructor ChemicalRowDataGatewayRDS, create a new chemical.
   * 
   * @param id
   * @param name
   * @param inhabits
   */
  public ChemicalRDGRDS(int id, String name, double inventory) {

    try {
      // Insert chemical
      PreparedStatement insertChemical = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("INSERT INTO Chemical (chemicalId, name, inventory)" + "VALUES (?, ?, ?);");
      insertChemical.setInt(1, id);
      insertChemical.setString(2, name);
      insertChemical.setDouble(3, inventory);

      insertChemical.execute(); // Insert chemical

    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to insert chemical through constructor");
    }
    
    // Set instance variables
    chemical = new ChemicalDTO(id, name, inventory);
  }

  /**
   * Delete the currently selected chemical from the database. 
   */
  public void delete() {
    String deleteChemical = "DELETE FROM Chemical WHERE ChemicalId = " + chemical.getChemicalId() + ";";
    
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      
      statement.executeUpdate(deleteChemical);
      
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Error deleting chemical " + chemical.getChemicalId());
    }
  }
  
  /**
   * Update the database.
   */
  public void update() {
    String updateChemicalSQL = "UPDATE Chemical SET chemicalId = ?, name = ?, inventory = ? WHERE chemicalID = " + chemical.getChemicalId()
        + ";";

    try {
      // Chemical
      PreparedStatement chem = DatabaseManager.getSingleton().getConnection().prepareStatement(updateChemicalSQL);
      chem.setInt(1, chemical.getChemicalId());
      chem.setString(2, chemical.getName());
      chem.setDouble(3, chemical.getInventory());

      chem.execute();
      
    } catch(SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to update chemical");
    }
  }
  
  /**
   * Set name.
   * @param newName new name to set
   */
  @Override
  public void setName(String newName) {
    chemical.setName(newName);
  }

  /**
   * Set inhabits.
   * @param newInhabits new inhabits to set
   */
  @Override
  public void setInventory(double inventory) {
    chemical.setInventory(inventory);
  }
  
  public ChemicalDTO getChemical() {
    return chemical;
  }

}