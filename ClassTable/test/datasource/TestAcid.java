package datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author Isabella Boone
 * 
 */
class TestAcid extends DatabaseTest {
  
  @Test
  void testGetName() throws SQLException, DatabaseException {
    AcidRDG acid = new AcidRDGRDS();
    acid.dropAllTables();
    acid.createTable();
    
    AcidRDG
        acid1 = new AcidRDGRDS(99, 2, "acidname1", 1.1),
        acid2 = new AcidRDGRDS(8, 4, "acidname2", 1.2),
        acid3 = new AcidRDGRDS(63, 6, "acidname3", 1.3),
        acid1_fetch = new AcidRDGRDS(99), 
        acid2_fetch = new AcidRDGRDS(8),
        acid3_fetch = new AcidRDGRDS(63);

    // Tests
    assertEquals("acidname1", acid1_fetch.getName());
    assertEquals("acidname2", acid2_fetch.getName());
    assertEquals("acidname3", acid3_fetch.getName());

    // Drop acid and chemical tables to get rid of anything we added to the database.
    acid.dropAllTables();
  }
  
  @Test
  void testGetInhabits() throws SQLException, DatabaseException {
    ChemicalRDG resetChem = new ChemicalRDGRDS();
    AcidRDG resetAcid = new AcidRDGRDS(),
        acid1 = new AcidRDGRDS(56, 2, "acidname1", 1.1),
        acid2 = new AcidRDGRDS(22, 4, "acidname2", 1.1),
        acid3 = new AcidRDGRDS(38, 6, "acidname3", 1.3),
        acid1_fetch = new AcidRDGRDS(56), 
        acid2_fetch = new AcidRDGRDS(22),
        acid3_fetch = new AcidRDGRDS(38);

    // Tests
    assertEquals(1.1, acid1_fetch.getInventory(), 0.1);
    assertEquals(1.2, acid2_fetch.getInventory(), 0.1);
    assertEquals(1.3, acid3_fetch.getInventory(), 0.1);

    // Drop acid and chemical tables to get rid of anything we added to the database.
    resetAcid.dropAllTables();
  }
  
  @Test
  void testGetSolute() throws SQLException, DatabaseException {
    AcidRDG acid = new AcidRDGRDS();
    acid.dropAllTables();
    acid.createTable();
    AcidRDG
        acid1 = new AcidRDGRDS(1, 2, "acidname1", 1.1),
        acid2 = new AcidRDGRDS(2, 4, "acidname2", 1.1),
        acid3 = new AcidRDGRDS(3, 6, "acidname3", 1.2),
        acid1_fetch = new AcidRDGRDS(1), 
        acid2_fetch = new AcidRDGRDS(2),
        acid3_fetch = new AcidRDGRDS(3);

    // Test
    assertEquals(2, acid1_fetch.getSolute());
    assertEquals(4, acid2_fetch.getSolute());
    assertEquals(6, acid3_fetch.getSolute());

    acid.dropAllTables();
  }
  
  @Test
  void testDelete() {
    AcidRDG initialize = new AcidRDGRDS(),
        acid = new AcidRDGRDS(1, 2, "chemname1", 1.1);
    
    // Ensure it has been added
    assertEquals("chemname1", acid.getName());
    assertEquals(1.1, acid.getInventory(), 0.1);
    
    acid.delete();
    
    try { 
      acid = new AcidRDGRDS(1);
      fail("");
    } catch(DatabaseException | SQLException e) {
      assertTrue(true); 
    }
  }
  
  @Test
  void testUpdate() throws SQLException, DatabaseException {
    AcidRDG acid = new AcidRDGRDS();
    acid.dropAllTables();
    acid.createTable();
    
    AcidRDG
        acid_setter = new AcidRDGRDS(1, 2, "acidname1", 1.1),
        acid_getter = new AcidRDGRDS(1);
    
    // Test solute
    assertEquals(2, acid_getter.getSolute());
    acid_setter.setSolute(3);
    acid_setter.update();
    acid_getter = new AcidRDGRDS(1);
    assertEquals(3, acid_getter.getSolute());
    
    // Test name
    assertEquals("acidname1", acid_getter.getName());
    acid_setter.setName("acidname2");
    acid_setter.update();
    acid_getter = new AcidRDGRDS(1);
    assertEquals("acidname2", acid_getter.getName());
    
    // Test inhabits
    assertEquals(1.1, acid_getter.getInventory(), 0.1);
    acid_setter.setInventory(1.2);
    acid_setter.update();
    acid_getter = new AcidRDGRDS(1);
    assertEquals(1.2, acid_getter.getInventory(), 0.1);
    
    acid.dropAllTables();
  }
  
  @Test
  void testGetSet() {
    AcidRDG createAcid = new AcidRDGRDS();
    createAcid.dropAllTables();
    ChemicalRDG createChemical = new ChemicalRDGRDS();
    createAcid.createTable();
    AcidRDG acid1 = new AcidRDGRDS(1, 15, "chemicalname1", 1.1);
    AcidRDG acid2 = new AcidRDGRDS(2, 15, "chemicalname2", 1.2);
    
    AcidRDG getter = new AcidRDGRDS();
    List<AcidRDGRDS> acidGet = getter.findSet(15);
    
    assertEquals("chemicalname1", acidGet.get(0).getName());
    assertEquals("chemicalname2", acidGet.get(1).getName());
    
    AcidRDG acid4 = new AcidRDGRDS(4, 32, "chemicalname4", 1.3);
    AcidRDG acid6 = new AcidRDGRDS(6, 32, "chemicalname6", 1.4);
    
    acidGet = getter.findSet(32);
    
    assertEquals("chemicalname4", acidGet.get(0).getName());
    assertEquals("chemicalname6", acidGet.get(1).getName());

  }
}
