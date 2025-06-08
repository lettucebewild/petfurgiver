package petadoptionapp;

public class Cat extends Pet {
    public Cat(String name, String gender, int age, int months, String imagePath, String description) {
        super(name, gender, age, months, imagePath, description);
    }
    
    @Override
    public void displayDetails() {
        System.out.println(getName() + 
                         "\nGender: " + getGender() + 
                         "\nAge: " + getAge() + " years" + 
                         (getMonths() > 0 ? " & " + getMonths() + " months" : "") +
                         "\nDescription: " + getDescription());
    }
}