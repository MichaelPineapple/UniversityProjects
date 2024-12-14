//Michael Wilson
//10-28-17
//Assignment 4

import java.lang.String;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Assignment4
{ 
   public static void main(String[] args) throws FileNotFoundException
   {
	   ArrayList<Vehicle> vehicleList = new ArrayList<>();
	   
      // import file
      Scanner fileScanner = new Scanner(new File(args[0]));

      // Read vehicl records from file
      while (fileScanner.hasNextLine())
      {
         String line = fileScanner.nextLine();

         if (line.contains("vehicle"))
         {
            String ownerName = fileScanner.nextLine();
            String address = fileScanner.nextLine();
            String phone = fileScanner.nextLine();
            String email = fileScanner.nextLine();
            vehicleList.add(new Vehicle(ownerName, address, phone, email));
         }
         else if (line.contains("foreign car"))
         {
            String ownerName = fileScanner.nextLine();
            String address = fileScanner.nextLine();
            String phone = fileScanner.nextLine();
            String email = fileScanner.nextLine();
            String color = fileScanner.nextLine();
            boolean convertable = fileScanner.nextLine().contains("true");
            String countryOfManufacture = fileScanner.nextLine();
            float importDuty = Float.valueOf(fileScanner.nextLine());
            vehicleList.add(new Foreign_Car(ownerName, address, phone, email, color, convertable, countryOfManufacture, importDuty));
         }
         else if (line.contains("american car"))
         {
            String ownerName = fileScanner.nextLine();
            String address = fileScanner.nextLine();
            String phone = fileScanner.nextLine();
            String email = fileScanner.nextLine();
            String color = fileScanner.nextLine();
            boolean convertable = fileScanner.nextLine().contains("true");
            boolean madeInDetroit = fileScanner.nextLine().contains("true");
            boolean unionShop = fileScanner.nextLine().contains("true");
            vehicleList.add(new American_Car(ownerName, address, phone, email, color, convertable, madeInDetroit, unionShop));
         }
         else if (line.contains("car"))
         {
            String ownerName = fileScanner.nextLine();
            String address = fileScanner.nextLine();
            String phone = fileScanner.nextLine();
            String email = fileScanner.nextLine();
            String color = fileScanner.nextLine();
            boolean convertable = fileScanner.nextLine().contains("true");
            vehicleList.add(new Car(ownerName, address, phone, email, color, convertable));
         }
         else if (line.contains("truck"))
         {
            String ownerName = fileScanner.nextLine();
            String address = fileScanner.nextLine();
            String phone = fileScanner.nextLine();
            String email = fileScanner.nextLine();
            float tons = Float.valueOf(fileScanner.nextLine());
            float cost = Float.valueOf(fileScanner.nextLine());
            String dateProduced = fileScanner.nextLine();
            vehicleList.add(new Truck(ownerName, address, phone, email, tons, cost, dateProduced));
         }
         else if (line.contains("bicycle"))
         {
            String ownerName = fileScanner.nextLine();
            String address = fileScanner.nextLine();
            String phone = fileScanner.nextLine();
            String email = fileScanner.nextLine();
            int numberOfSpeeds = Integer.valueOf(fileScanner.nextLine());
            vehicleList.add(new Bicycle(ownerName, address, phone, email, numberOfSpeeds));

         }

      }
      
      fileScanner.close();

      // display vehicle records

      System.out.println("\n - Vehicle List - \n");
      
	   printAll(vehicleList);
	   
      // sort and re-display records
      
      System.out.println("\n - Sorted Vehicle List - \n");
      
	   sort(vehicleList);
      
      printRecordCount(vehicleList);
      
      System.out.println("\n - List Of Only Bikes And Trucks - \n");
      
      printBikesAndTrucks(vehicleList);
      
      System.out.println("\n - List Of Vehicles In Area Code 987 - \n");
      
      printVehiclesFrom987AreaCode(vehicleList);
   }
   
   /*
    * prints date for all vehicle objects in a list of vehicles
    * list - list of vehicles
    */
   public static void printAll(ArrayList<Vehicle> list)
   {
	   for (int i = 0; i < list.size(); i++) System.out.println(list.get(i).toString());
   }
   
   /*
    * prints the amount of vehicles in a vehicle list
    * list - list of vehicles
    */
   public static void printRecordCount(ArrayList<Vehicle> list)
   {
	   System.out.println("Number of records: "+list.size());
   }
   
   /*
    * prints only the bikes and trucks from a list of vehicles
    * list - list of vehicles
    */
   public static void printBikesAndTrucks(ArrayList<Vehicle> list)
   {
	   for (int i = 0; i < list.size(); i++) if (list.get(i) instanceof Bicycle || list.get(i) instanceof Truck) System.out.println(list.get(i).toString());
   }
   
   /*
    * sorts a list of vehicles by email alphabetically and then prints the list
    * list - list of vehicles
    */
   public static void sort(ArrayList<Vehicle> list)
   {
	   for (int i = 0; i < list.size(); i++)
	   {
		   for (int j = 0; j < list.size(); j++)
		   {
			   if (list.get(i).getEmail().compareTo(list.get(j).getEmail()) < 0)
			   {
				   Vehicle temp = list.get(j);
				   list.set(j, list.get(i));
				   list.set(i, temp);
			   }
		   }
	   }
	   
	   printAll(list);
   }
   
   /*
    * prints all the vehicles from the area code 987
    * list - list of vehicles
    */
   public static void printVehiclesFrom987AreaCode(ArrayList<Vehicle> list)
   {
      for (int i = 0; i < list.size(); i++)
	   {
         if (list.get(i).getPhone().contains("(987)")) System.out.println(list.get(i).toString());
      }
   }
   
}


class Vehicle
{
	private String 
		ownerName = "[null]",
		address = "[null]",
		phone = "[null]",
		email = "[null]";
	
	public Vehicle(String _ownerName, String _address, String _phone, String _email)
	{
		ownerName = _ownerName;
		address = _address;
		phone = _phone;
		email = _email;
	}
	
	public String getOwnerName()
	{
		return ownerName;
	}
	public String getEmail()
	{
		return email;
	}
	public String getAddress()
	{
		return address;
	}
	public String getPhone()
	{
		return phone;
	}
	
	public void setOwnerName(String str)
	{
		ownerName = str;
	}
	public void setEmail(String str)
	{
		email = str;
	}
	public void setAddress(String str)
	{
		address = str;
	}
	public void setPhone(String str)
	{
		phone = str;
	}
	
   @Override
   public String toString()
   {
      return new String("\n"+ownerName+"\n"+address+"\n"+phone+"\n"+email+"\n");
   }

}

class Car extends Vehicle
{
	private String color = "[null]";
	private boolean convertable = false; 
	
	public Car(String _ownerName, String _address, String _phone, String _email, String _color, boolean _convertable)
	{
		super(_ownerName, _address, _phone, _email);
		color = _color;
		convertable = _convertable;
	}
	
	public String getColor()
	{
		return color;
	}
	public boolean getConvertable()
	{
		return convertable;
	}
	
	public void setColor(String str)
	{
		color = str;
	}
	public void setConvertable(boolean _bool)
	{
		convertable = _bool;
	}
	
   @Override
   public String toString()
   {
	   return new String("Car"+super.toString()+color+"\n"+convertable+"\n");
   }

}

class American_Car extends Car
{
	private boolean madeInDetroit = false, unionShop = false;
	
	public American_Car(String _ownerName, String _address, String _phone, String _email, String _color, boolean _convertable, boolean _madeInDetroit, boolean _unionShop)
	{
		super(_ownerName, _address, _phone, _email, _color, _convertable);
		madeInDetroit = _madeInDetroit;
		unionShop = _unionShop;
	}
	
	public boolean getMadeInDetroit()
	{
		return madeInDetroit;
	}
	public boolean getUnionShop()
	{
		return unionShop;
	}
	
	public void setMadeInDetroit(boolean _bool)
	{
		madeInDetroit = _bool;
	}
	public void setUnionShop(boolean _bool)
	{
		unionShop = _bool;
	}
	
   @Override
   public String toString()
   {
	   return new String("American "+super.toString()+madeInDetroit+"\n"+unionShop+"\n");
   }

}

class Foreign_Car extends Car
{
	private String countryOfManufacture = "[null]";
	private float importDuty = 0.0f;
	
	public Foreign_Car(String _ownerName, String _address, String _phone, String _email, String _color, boolean _convertable, String _countryOfManufacture, float _importDuty)
	{
		super(_ownerName, _address, _phone, _email, _color, _convertable);
		countryOfManufacture = _countryOfManufacture;
		importDuty = _importDuty;
	}
	
	public String getCountryOfManufacture()
	{
		return countryOfManufacture;
	}
	public float getImportDuty()
	{
		return importDuty;
	}
	
	public void setCountryOfManufacture(String str)
	{
		countryOfManufacture = str;
	}
	public void setImportDuty(float val)
	{
		importDuty = val;
	}
	
   @Override
   public String toString()
   {
	   return new String("Foreign "+super.toString()+countryOfManufacture+"\n"+importDuty+"\n");
   }

}

class Truck extends Vehicle
{
	private float tons = 0.0f, cost = 0.0f;
	private String datePurchased = "[null]";
	
	public Truck(String _ownerName, String _address, String _phone, String _email, float _tons, float _cost, String _datePurchased)
	{
		super(_ownerName, _address, _phone, _email);
		tons = _tons;
		cost = _cost;
		datePurchased = _datePurchased;
	}
	
	public float getTons()
	{
		return tons;
	}
	public float getCost()
	{
		return cost;
	}
	public String getDatPurchased()
	{
		return datePurchased;
	}
	
	public void setTons(float val)
	{
		tons = val;
	}
	public void setCost(float val)
	{
		cost = val;
	}
	public void setDatePurchased(String str)
	{
		datePurchased = str;
	}
	
   @Override
   public String toString()
   {
	   return new String("Truck"+super.toString()+tons+"\n"+cost+"\n"+datePurchased+"\n");
   }

}

class Bicycle extends Vehicle
{
	private int numberOfSpeeds = 0;
	
	public Bicycle(String _ownerName, String _address, String _phone, String _email, int _numberOfSpeeds)
	{
		super(_ownerName, _address, _phone, _email);
		numberOfSpeeds = _numberOfSpeeds;
	}
	
	public int getNumberOfSpeeds()
	{
		return numberOfSpeeds;
	}
	
	public void setNumberOfSpeeds(int val)
	{
		numberOfSpeeds = val;
	}
	
	@Override
	public String toString()
	{
		return new String("Bicycle"+super.toString()+numberOfSpeeds+"\n");
	}
}

