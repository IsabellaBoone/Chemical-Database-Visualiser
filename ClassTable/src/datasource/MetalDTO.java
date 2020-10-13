package datasource;

public class MetalDTO {
  int metalId, dissolvedById; 
  String name;
  double inventory;
  
  public MetalDTO(int metalId, int dissolvedById, String name, double inventory) {
    this.metalId = metalId;
    this.dissolvedById = dissolvedById; 
    this.name = name;
    this.inventory = inventory;
  }
  
  public int getMetalId() { 
    return metalId;
  }
  
  public int getDissolvedById() {
    return dissolvedById; 
  }
  
  public String getName() {
    return name;
  }
  
  public double getInventory() {
    return inventory;
  }
  
  public void setMetalId(int metalId) {
    this.metalId = metalId;
  }
  
  public void setDissolvedById(int dissolvedById) {
    this.dissolvedById = dissolvedById;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setInventory(double inventory) {
    this.inventory = inventory; 
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + dissolvedById;
    long temp;
    temp = Double.doubleToLongBits(inventory);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + metalId;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MetalDTO other = (MetalDTO) obj;
    if (dissolvedById != other.dissolvedById)
      return false;
    if (Double.doubleToLongBits(inventory) != Double.doubleToLongBits(other.inventory))
      return false;
    if (metalId != other.metalId)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
  
  
}
