package petadoptionapp;

// Inheritance - Dog class inherits from Pet class to reuse common attributes
public class Dog extends Pet {
    
    // Encapsulation - public constructor controls object creation
    // Inheritance - calls parent constructor using super()
    public Dog(String name, String gender, int age, int months, String imagePath, String description) {
        super(name, gender, age, months, imagePath, description);
    }

    // Polymorphism - overrides parent's displayDetails method with Dog-specific implementation
    @Override
    public void displayDetails() {
        System.out.println(getName() +
            "\nGender: " + getGender() +
            "\nAge: " + getAge() + " years" +
            (getMonths() > 0 ? " & " + getMonths() + " months" : "") +
            "\nDescription: " + getDescription());
    }
}