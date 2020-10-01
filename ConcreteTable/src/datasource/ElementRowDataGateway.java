package datasource;

public interface ElementRowDataGateway {
	
	public int getElementID();
	
	public String getName();
	
	public String getInhabits();
	
	public int getAtomicNumber();
	
	public double getAtomicMass();
	
	public void setName(String s);
	
	public void setInhabits(String s);
	
	public void setAtomicNumber(int i);
	
	public void setAtomicMass(double d);
	
	public boolean persist();
	
	public boolean delete();
}
