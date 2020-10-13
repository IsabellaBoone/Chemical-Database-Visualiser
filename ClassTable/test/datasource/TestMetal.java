package datasource;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author kimberlyoneill
 *
 */
class TestMetal {

  @Test
  static void testGetName() {    
    MetalRDG metalGet1 = new MetalRDGRDS(31);
    MetalRDG metalGet2 = new MetalRDGRDS(32);
    MetalRDG metalGet3 = new MetalRDGRDS(33);
    MetalRDG metalGet4 = new MetalRDGRDS(34);
    
    assertEquals("metalname1", metalGet1.getName());
    assertEquals("metalname2", metalGet2.getName());
    assertEquals("metalname3", metalGet3.getName());
    assertEquals("metalname4", metalGet4.getName());
  }

  @Test
  static void testGetInhabits() {    
    MetalRDG metalGet1 = new MetalRDGRDS(31);
    MetalRDG metalGet2 = new MetalRDGRDS(32);
    MetalRDG metalGet3 = new MetalRDGRDS(33);
    MetalRDG metalGet4 = new MetalRDGRDS(34);
    
    assertEquals(1.1, metalGet1.getInventory(), 0.1);
    assertEquals(1.2, metalGet2.getInventory(), 0.1);
    assertEquals(1.3, metalGet3.getInventory(), 0.1);
    assertEquals(1.4, metalGet4.getInventory(), 0.1);
  }

  @Test
  static void testGetDissolvedBy() {    
    MetalRDG metalGet1 = new MetalRDGRDS(31);
    MetalRDG metalGet2 = new MetalRDGRDS(32);
    MetalRDG metalGet3 = new MetalRDGRDS(33);
    MetalRDG metalGet4 = new MetalRDGRDS(34);
    
    assertEquals(1, metalGet1.getDissolvedBy());
    assertEquals(2, metalGet2.getDissolvedBy());
    assertEquals(3, metalGet3.getDissolvedBy());
    assertEquals(4, metalGet4.getDissolvedBy());
  }

  @Test
  static void testGetSet() {    
    MetalRDG metal = new MetalRDGRDS(); 
    List<MetalRDGRDS> metalGet = metal.findSet(5);
    
    assertEquals("metalname5", metalGet.get(0).getName());
    assertEquals("metalname6", metalGet.get(1).getName());
  }
  
  static void testAll() {
    insertMetals();
    testGetName();
    testGetInhabits();
    testGetDissolvedBy();
    testGetSet(); 
  }
  
  private static void insertMetals() {
    MetalRDG 
    metal = new MetalRDGRDS(31, 1, "metalname1", 1.1);
    metal = new MetalRDGRDS(32, 2, "metalname2", 1.2);
    metal = new MetalRDGRDS(33, 3, "metalname3", 1.3);
    metal = new MetalRDGRDS(34, 4, "metalname4", 1.4);
    metal = new MetalRDGRDS(35, 5, "metalname5", 1.5);
    metal = new MetalRDGRDS(36, 5, "metalname6", 1.6);
  }
}
