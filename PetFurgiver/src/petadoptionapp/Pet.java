package petadoptionapp;

public class Pet {
    private String name;
    private String gender;
    private int age;
    private int months;
    private String imagePath;
    private String description;

    public Pet(String name, String gender, int age, int months, String imagePath, String description) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.months = months;
        this.imagePath = imagePath;
        this.description = description;
    }

    // Getters
    public String getName() { return name; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public int getMonths() { return months; }
    public String getImagePath() { return imagePath; }
    public String getDescription() { return description; }

    public void displayDetails() {
        System.out.println(getName() + 
                         "\nGender: " + getGender() + 
                         "\nAge: " + getAge() + " years" + 
                         (getMonths() > 0 ? " & " + getMonths() + " months" : "") +
                         "\nDescription: " + getDescription());
    }
}