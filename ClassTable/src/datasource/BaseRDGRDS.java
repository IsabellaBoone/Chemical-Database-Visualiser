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
 * BaseRowDataGatewayRDS
 * @author Isabella Boone, Kim O'Neill
 */
public class BaseRDGRDS implements BaseRDG {
  int baseId, solute;
  String name; 
  double inventory;
  
  /**
   * Create table
   */
  public BaseRDGRDS() {
    
  }
  
  /**
   * Constructor BaseRowDataGateway, search for existing Base via id 
   * @param id
   */
  public BaseRDGRDS(int id) throws SQLException, DatabaseException {
    
    // Select statements
    String getBase = new String("SELECT * FROM Base WHERE baseId = " + id + ";"),
        getChem = new String("SELECT * FROM Chemical INNER JOIN Base ON Chemical.chemicalId = " + id + ";");

    // Get acid information
    Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
    ResultSet rs = statement.executeQuery(getBase);
    rs.next(); // Get result
    this.solute = rs.getInt("solute");

    // Get chemical information
    rs = statement.executeQuery(getChem);
    rs.next();
    this.name = rs.getString("name");
    this.inventory = rs.getDouble("inventory");
    this.baseId = id;

  }
  
  /**
   * AcidRowDataGateway constructor for creating a new base
   * @param id
   * @param solute
   * @param name
   * @param inhabits
   */
  public BaseRDGRDS(int id, int solute, String name, double inventory) {   
    
    
    try {
      // Insert chemical
      PreparedStatement insertChemical = DatabaseManager.getSingleton().getConnection()
          .prepareStatement("INSERT INTO Chemical (chemicalId, name, inventory)" + "VALUES (?, ?, ?);");
      insertChemical.setInt(1, id);
      insertChemical.setString(2, name);
      insertChemical.setDouble(3, inventory);
      
      // Insert Base
      PreparedStatement insertAcid = DatabaseManager.getSingleton().getConnection()
        .prepareStatement("INSERT INTO Base (baseId, solute)" + "VALUES (?, ?);");
      insertAcid.setInt(1, id); // Set acid id
      insertAcid.setInt(2, solute); // set solute id
      
      insertChemical.execute(); // Insert chemical
      insertAcid.execute();  // Insert acid
      
    } catch(SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to insert acid through constructor");
    }
    
    // Set instance variables
    this.baseId = id;
    this.solute = solute;
    this.name = name;
    this.inventory = inventory;
  }
  
  
  /**
   * Delete a base from both chemical and base tables.
   */
  public void delete() {
    String deleteChemical = "DELETE FROM Chemical WHERE ChemicalId = " + baseId + ";",
        deleteBase = "DELETE FROM Base WHERE baseId = " + baseId + ";";
    
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      
      statement.executeUpdate(deleteBase);
      statement.executeUpdate(deleteChemical);
      
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Error deleting base " + baseId);
    }
  }
  
  /**
   * Update the database.
   */
  public void update() {
    String updateChemicalSQL = "UPDATE Chemical SET chemicalId = ?, name = ?, inventory = ? WHERE chemicalID = " + baseId
        + ";", updateBaseSQL = "UPDATE Base SET baseId = ?, solute = ? WHERE baseId = " + baseId + ";";

    try {
      // Chemical
      PreparedStatement chem = DatabaseManager.getSingleton().getConnection().prepareStatement(updateChemicalSQL);
      chem.setInt(1, baseId);
      chem.setString(2, name);
      chem.setDouble(3, inventory);

      chem.execute();
      
      // Base
      PreparedStatement acid = DatabaseManager.getSingleton().getConnection().prepareStatement(updateBaseSQL);
      acid.setInt(1, baseId);
      acid.setInt(2, solute);
      
      acid.execute();
      
      
    } catch(SQLException | DatabaseException e) {
      e.printStackTrace();
      System.out.println("Failed to update base");
    }
  }
  
  /**
   * 
   */
  public List<BaseRDGRDS> findSet(int solute) {
    List<BaseRDGRDS> results = new ArrayList<>(); 
    
    try {
      String sql = "SELECT * FROM Base WHERE solute = " + solute + ";";
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);
      
      while(rs.next()) {
        int sol = rs.getInt("baseId");
        BaseRDGRDS id = new BaseRDGRDS(sol); 
        results.add(id);
      }
      
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
    }
    return results;
  }
  
  /**
   * Get all bases in the database. 
   */
  public List<BaseDTO> getAll() {
    String sql = "SELECT * FROM Base INNER JOIN Chemical WHERE Base.baseId = Chemical.chemicalId;";
    ArrayList<BaseDTO> bases = new ArrayList<BaseDTO>();
    
    try {
      Statement statement = DatabaseManager.getSingleton().getConnection().createStatement();
      ResultSet rs = statement.executeQuery(sql);

      while (rs.next()) {
        bases.add(new BaseDTO(rs.getInt("baseId"), rs.getInt("solute"), rs.getString("name"), rs.getDouble("inventory")));
      }
    } catch (SQLException | DatabaseException e) {
      e.printStackTrace();
    }
    return bases;
  }
  
  /**
   * Get solute.
   */
  @Override
  public int getSolute() {
    return solute;
  }

  /** 
   * Set solute.
   */
  @Override
  public void setSolute(int newSolute) {
    this.solute = newSolute;
  }

  /** 
   * Get name.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Set name.
   */
  @Override
  public void setName(String newName) {
    this.name = newName;
  }

  /**
   * Get inhabits.
   */
  @Override
  public double getInventory() {
    return inventory;
  }

  /**
   * Set inhabits.
   */
  @Override
  public void setInventory(double inventory) {
    this.inventory = inventory;
  }

}
