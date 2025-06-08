package petadoptionapp;

// Abstraction - provides general Pet template for specific pet types
public class Pet {
    // Encapsulation - private fields hide internal data from outside access
    private String name;
    private String gender;
    private int age;
    private int months;
    private String imagePath;
    private String description;

    // Encapsulation - public constructor controls object creation and data initialization
    public Pet(String name, String gender, int age, int months, String imagePath, String description) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.months = months;
        this.imagePath = imagePath;
        this.description = description;
    }

    // Encapsulation - public getters provide controlled read access to private fields
    public String getName() { return name; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public int getMonths() { return months; }
    public String getImagePath() { return imagePath; }
    public String getDescription() { return description; }

    // Polymorphism - method can be overridden by subclasses for specific behavior
    public void displayDetails() {
        System.out.println(getName() +
            "\nGender: " + getGender() +
            "\nAge: " + getAge() + " years" +
            (getMonths() > 0 ? " & " + getMonths() + " months" : "") +
            "\nDescription: " + getDescription());
    }
}