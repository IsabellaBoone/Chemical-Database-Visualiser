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
class TestBase extends DatabaseTest {

  @Test
  void testGetName() throws SQLException, DatabaseException {
    BaseRowDataGateway base = new BaseRowDataGatewayRDS();
    ChemicalRowDataGateway chem = new ChemicalRowDataGatewayRDS();
    
    base.dropAllTables();
    chem.createTable();
    base.createTable();
    
    BaseRowDataGateway
        base1 = new BaseRowDataGatewayRDS(1, 2, "basename1", "baseinhabits1"),
        base2 = new BaseRowDataGatewayRDS(2, 4, "basename2", "baseinhabits2"),
        base3 = new BaseRowDataGatewayRDS(3, 6, "basename3", "baseinhabits3"),
        base1_fetch = new BaseRowDataGatewayRDS(1), base2_fetch = new BaseRowDataGatewayRDS(2),
        base3_fetch = new BaseRowDataGatewayRDS(3);

    // Testing to see if new gateways can properly fetch
    assertEquals("basename1", base1_fetch.getName());
    assertEquals("basename2", base2_fetch.getName());
    assertEquals("basename3", base3_fetch.getName());

    base.dropAllTables();
  }
  
  @Test
  void testGetInhabits() throws SQLException, DatabaseException {
    BaseRowDataGateway initialize = new BaseRowDataGatewayRDS(),
        base1 = new BaseRowDataGatewayRDS(1, 2, "basename1", "baseihabits1"),
        base2 = new BaseRowDataGatewayRDS(2, 4, "basename2", "baseihabits2"),
        base3 = new BaseRowDataGatewayRDS(3, 6, "basename3", "baseihabits3"),
        base1_fetch = new BaseRowDataGatewayRDS(1), base2_fetch = new BaseRowDataGatewayRDS(2),
        base3_fetch = new BaseRowDataGatewayRDS(3);

    // Testing to see if new gateways can properly fetch
    assertEquals("baseihabits1", base1_fetch.getInhabits());
    assertEquals("baseihabits2", base2_fetch.getInhabits());
    assertEquals("baseihabits3", base3_fetch.getInhabits());

    initialize.dropAllTables();
  }
  
  @Test
  void testGetSolute() throws SQLException, DatabaseException {
    BaseRowDataGateway initialize = new BaseRowDataGatewayRDS(),
        base1 = new BaseRowDataGatewayRDS(1, 2, "basename1", "baseihabits1"),
        base2 = new BaseRowDataGatewayRDS(2, 4, "basename2", "baseihabits2"),
        base3 = new BaseRowDataGatewayRDS(3, 6, "basename3", "baseihabits3"),
        base1_fetch = new BaseRowDataGatewayRDS(1), base2_fetch = new BaseRowDataGatewayRDS(2),
        base3_fetch = new BaseRowDataGatewayRDS(3);

    // Testing to see if new gateways can properly fetch
    assertEquals(2, base1_fetch.getSolute());
    assertEquals(4, base2_fetch.getSolute());
    assertEquals(6, base3_fetch.getSolute());

    initialize.dropAllTables();
  }
  
  @Test
  void testUpdate() throws SQLException, DatabaseException {
    BaseRowDataGateway initialize = new BaseRowDataGatewayRDS();
    initialize.dropAllTables();
    
    BaseRowDataGateway
        base_setter = new BaseRowDataGatewayRDS(1, 2, "basename1", "baseinhabits1"),
        base_getter = new BaseRowDataGatewayRDS(1);
    
    // Test solute
    assertEquals(2, base_getter.getSolute());
    base_setter.setSolute(3);
    base_setter.update();
    base_getter = new BaseRowDataGatewayRDS(1);
    assertEquals(3, base_getter.getSolute());
    
    // Test name
    assertEquals("basename1", base_getter.getName());
    base_setter.setName("basename2");
    base_setter.update();
    base_getter = new BaseRowDataGatewayRDS(1);
    assertEquals("basename2", base_getter.getName());
    
    // Test inhabits
    assertEquals("baseinhabits1", base_getter.getInhabits());
    base_setter.setInhabits("baseinhabits2");
    base_setter.update();
    base_getter = new BaseRowDataGatewayRDS(1);
    assertEquals("baseinhabits2", base_getter.getInhabits());
    
    initialize.dropAllTables();
  }
  
  @Test
  void testDelete() {
    BaseRowDataGateway createBase = new BaseRowDataGatewayRDS(),
        base = new BaseRowDataGatewayRDS(1, 2, "chemname1", "cheminhabits1");
    
    // Ensure it has been added 
    assertEquals("chemname1", base.getName());
    assertEquals("cheminhabits1", base.getInhabits());
    
    base.delete();
    
    try { 
      base = new BaseRowDataGatewayRDS(1);
      fail("");
    } catch(DatabaseException | SQLException e) {
      assertTrue(true); 
    }
    
    createBase.dropAllTables();
  }
  
  @Test
  void testGetSet() {
    BaseRowDataGateway createBase = new BaseRowDataGatewayRDS();
    createBase.dropAllTables();
    ChemicalRowDataGateway createChemical = new ChemicalRowDataGatewayRDS();
    BaseRowDataGateway acid1 = new BaseRowDataGatewayRDS(1, 15, "chemicalname1", "inhabits1");
    BaseRowDataGateway acid2 = new BaseRowDataGatewayRDS(2, 15, "chemicalname2", "inhabits2");
    
    BaseRowDataGateway getter = new BaseRowDataGatewayRDS();
    List<BaseRowDataGatewayRDS> acidGet = getter.findSet(15);
    
    assertEquals("chemicalname1", acidGet.get(0).getName());
    assertEquals("chemicalname2", acidGet.get(1).getName());
    
    BaseRowDataGateway acid4 = new BaseRowDataGatewayRDS(4, 32, "chemicalname4", "inhabits4");
    BaseRowDataGateway acid6 = new BaseRowDataGatewayRDS(6, 32, "chemicalname6", "inhabits6");
    
    acidGet = getter.findSet(32);
    
    assertEquals("chemicalname4", acidGet.get(0).getName());
    assertEquals("chemicalname6", acidGet.get(1).getName());

    createBase.dropAllTables();
  }
}
