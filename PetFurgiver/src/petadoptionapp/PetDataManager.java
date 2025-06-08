package petadoptionapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Centralized data management class for all pets in the adoption system.
 * This class handles loading, storing, and providing access to pet data.
 */
public class PetDataManager {
    private static PetDataManager instance;
    private ArrayList<Pet> allPets;

    // Private constructor for singleton pattern
    private PetDataManager() {
        initializePetData();
    }

    // Singleton pattern to ensure only one instance exists
    public static PetDataManager getInstance() {
        if (instance == null) {
            instance = new PetDataManager();
        }
        return instance;
    }

    /**
     * Initialize all pet data - add your pets here!
     */
    private void initializePetData() {
        allPets = new ArrayList<>();
        addDogs();
        addCats();
    }

    /**
     * Add all dog data here
     */
    private void addDogs() {
        allPets.addAll(Arrays.asList(
            // Male Dogs
            new Dog("Alexis", "Male", 2, 0,
                "/dog_images/dog_alexis.png",
                buildPetDescription("Male", "2 years old", "Brown", "Aspin",
                    "One eye surgically removed due to a past injury. Currently under medication maintenance but stable and responding well.",
                    "Neutered", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Alexis was found in a vacant lot in Marikina City, alone and injured. Despite his early hardships, he remains incredibly gentle and affectionate.")),

            new Dog("Arian", "Male", 1, 2,
                "/dog_images/dog_arian.png",
                buildPetDescription("Male", "1 year & 2 months", "Brown and White", "Basenji",
                    "Allergic to Royal Canin dry dog food",
                    "Neutered", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "He was rescued as a malnourished stray pup searching for food and shelter. With love and care, he's now healthy, happy, and ready for his forever home.")),

            new Dog("Billie", "Male", 1, 0,
                "/dog_images/dog_billie.png",
                buildPetDescription("Male", "1 year old", "Black and White", "Border Collie",
                    "Left eye has cataract",
                    "Not yet", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Billie was rescued from neglect but has blossomed into a lively, intelligent dog. Despite having a cataract in one eye, he's full of energy and love.")),

            new Dog("Brisket", "Male", 0, 10,
                "/dog_images/dog_brisket.png",
                buildPetDescription("Male", "10 months", "Off white and Gray", "Maltese",
                    "No health issues",
                    "Not yet", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Brisket was rescued after being abandoned in a box near a busy road, scared and hungry. Now safe and healthy, he's a cheerful pup ready for a loving home.")),

            new Dog("Brix", "Male", 0, 11,
                "/dog_images/dog_brix.png",
                buildPetDescription("Male", "11 months", "White", "Shih Tzu",
                    "Underbite and has injury in left foot (still recovering)",
                    "Not yet", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Brix was found limping near a roadside, likely abandoned, and was gently rescued by a kind passerby. He is now safe and recovering well from his foot injury.")),

            new Dog("Bruno", "Male", 3, 0,
                "/dog_images/dog_bruno.png",
                buildPetDescription("Male", "3 years old", "Brown and Black", "Belgian Malinois",
                    "No health issues",
                    "Neutered", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Bruno was rescued after being spotted wandering alone near a construction site, hungry and scared. He was safely brought in and has since regained his strength and confidence.")),

            new Dog("Brutos", "Male", 1, 0,
                "/dog_images/dog_brutos.png",
                buildPetDescription("Male", "1 year old", "Brown and White", "Aspin",
                    "No health issues",
                    "Neutered", "Not specified",
                    "Brutos was rescued from the streets after being seen scavenging for food near a marketplace. He quickly adapted to care and is now thriving in a safe environment.")),

            new Dog("Frankie", "Male", 0, 7,
                "/dog_images/dog_frankie.png",
                buildPetDescription("Male", "7 months", "Multi-colored", "Chihuahua",
                    "Has epilepsy",
                    "Not yet", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Frankie was rescued after being abandoned outside a veterinary clinic, trembling and alone. Despite his epilepsy, he is now receiving the care he needs and continues to show a loving spirit.")),

            // Female Dogs
            new Dog("Alusha", "Female", 2, 0,
                "/dog_images/dog_alusha.png",
                buildPetDescription("Female", "2 years old", "Light Brown", "Golden Retriever",
                    "No health issues",
                    "Spayed", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Alusha was rescued from a backyard breeder who could no longer care for her and her littermates. She was the smallest of the group but full of energy and love.")),

            new Dog("Andy", "Female", 0, 5,
                "/dog_images/dog_andy.png",
                buildPetDescription("Female", "5 months old", "Black", "Dachshund",
                    "No health issues",
                    "Not yet", "8-in-1 & Oral Deworm",
                    "Andy was found wandering alone near a marketplace, likely abandoned due to his breed's health maintenance needs. A kind passerby alerted rescuers just in time.")),

            new Dog("Biscoff", "Female", 0, 2,
                "/dog_images/dog_biscoff.png",
                buildPetDescription("Female", "2 months old", "Brown and White", "Corgi",
                    "No health issues",
                    "Not yet", "(1) 8-in-1 & Oral Deworm",
                    "Biscoff was discovered inside a cardboard box left outside a veterinary clinic. Despite her young age, she showed remarkable resilience and playfulness.")),

            new Dog("Bleu", "Female", 0, 3,
                "/dog_images/dog_bleu.png",
                buildPetDescription("Female", "3 months old", "Black Brown", "Rottweiler",
                    "Has allergy to Nutri chunks dry dog food",
                    "Not yet", "8-in-1 & Oral Deworm",
                    "Bleu was surrendered by a family who could not manage her dietary needs. She was malnourished and itchy but is now recovering in foster care.")),

            new Dog("Cassie", "Female", 0, 5,
                "/dog_images/dog_cassie.png",
                buildPetDescription("Female", "5 months", "Black", "Labrador",
                    "Allergic to Aozi dry and wet dog food",
                    "Not yet", "8-in-1 & Oral Deworm",
                    "Cassie was rescued from a cramped cage at an overrun shelter. She had been overlooked due to her allergies and black fur, but she's now thriving.")),

            new Dog("Chichay", "Female", 1, 0,
                "/dog_images/dog_chichay.png",
                buildPetDescription("Female", "1 year old", "Chocolate Brown", "Labrador",
                    "Deaf due to abuse",
                    "Spayed", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Chichay was found chained and abused in a backyard; her deafness is a lasting result of the trauma she endured. She has since blossomed into a gentle and loyal companion.")),

            new Dog("Chiwa", "Female", 1, 0,
                "/dog_images/dog_chiwa.png",
                buildPetDescription("Female", "1 year old", "Brown", "Chihuahua",
                    "Lactose Intolerance to any milk replacer",
                    "Spayed", "Anti-rabies, 8-in-1 & Oral Deworm",
                    "Chiwa was rescued from a hoarding situation where she was living with over 20 dogs. Malnourished and shy, she's slowly gaining confidence and trust."))
        ));
    }

    /**
     * Add all cat data here
     */
    private void addCats() {
        allPets.addAll(Arrays.asList(
            // Male Cats
            new Cat("Ash", "Male", 2, 0,
                "/cat_images/cat_ash.png",
                buildPetDescription("Male", "2 years old", "Blue cat", "British Shorthair",
                    "Asthma",
                    "Neutered", "Deworm, 4-in-1, Anti-rabies",
                    "Ash was adopted from a shelter by a caring family who continues to manage his asthma with love and attention.")),

            new Cat("Choknat", "Male", 1, 0,
                "/cat_images/cat_choknat.png",
                buildPetDescription("Male", "1 year old", "Black gray", "Philippine Street Cat",
                    "Allergic to wet food",
                    "Not yet", "Deworm, 4-in-1",
                    "Choknat was found wandering in a local neighborhood and was adopted by a compassionate rescuer who gave him a safe home.")),

            new Cat("Frank", "Male", 2, 0,
                "/cat_images/cat_frank.png",
                buildPetDescription("Male", "2 years old", "Black and Gray", "Philippine Street Cat and Persian",
                    "No health issues",
                    "Neutered", "Deworm, 4-in-1, Anti-rabies",
                    "Frank was born to a stray mother and was adopted by a family who embraced his unique Persian mix and playful personality.")),

            new Cat("Jobet", "Male", 1, 0,
                "/cat_images/cat_jobet.png",
                buildPetDescription("Male", "1 year old", "Ginger white", "Philippine Street Cat",
                    "No health issues",
                    "Not yet", "Deworm, 4-in-1, Anti-rabies",
                    "Jobet was rescued from the streets as a kitten and welcomed into a warm and loving household.")),

            new Cat("Kitty", "Male", 0, 2,
                "/cat_images/cat_kitty.png",
                buildPetDescription("Male", "2 months old", "Ginger", "Philippine Street Cat",
                    "No health issues",
                    "Not yet", "Deworm",
                    "Kitty was rescued as a stray kitten and taken in by a kind-hearted animal lover who gave him a safe and loving home.")),

            new Cat("Pipoy", "Male", 0, 2,
                "/cat_images/cat_pipoy.png",
                buildPetDescription("Male", "2 months old", "Ginger", "Persian",
                    "No health issues",
                    "Not yet", "Deworm, 4-in-1",
                    "Pipoy was adopted from a friend's accidental litter and quickly became the adored baby of his new family.")),

            new Cat("Toffee", "Male", 0, 3,
                "/cat_images/cat_toffee.png",
                buildPetDescription("Male", "3 months old", "Black and gray", "Philippine street cat",
                    "No health issues",
                    "Not yet", "Deworm, 4-in-1",
                    "Toffee was taken in by a kind-hearted individual after being found alone near a market as a tiny kitten.")),

            new Cat("Riley", "Male", 2, 0,
                "/cat_images/cat_riley.png",
                buildPetDescription("Male", "2 years old", "Black and White", "Persian",
                    "Paralyzed lower feet due to abuse",
                    "Neutered", "Deworm, 4-in-1, Anti-rabies",
                    "Riley was rescued from an abusive situation that left him paralyzed, and he was adopted by a devoted caregiver who provides him with constant love and support.")),

            // Female Cats
            new Cat("Osang", "Female", 0, 3,
                "/cat_images/cat_osang.png",
                buildPetDescription("Female", "3 months old", "Ginger", "Persian Ragdoll",
                    "No health issues",
                    "Not yet", "Deworm",
                    "Osang was adopted from a neighbor whose cat unexpectedly gave birth, and she quickly became the youngest member of her new family.")),

            new Cat("Mimay", "Female", 2, 0,
                "/cat_images/cat_mimay.png",
                buildPetDescription("Female", "2 years old", "Ginger white", "Philippine Street Cat",
                    "No right eye due to accident",
                    "Spayed", "Deworm, 4-in-1, Anti-rabies",
                    "Mimay was found injured on the streets and lovingly adopted after surviving an accident that took her right eye.")),

            new Cat("Mimi", "Female", 2, 0,
                "/cat_images/cat_mimi.png",
                buildPetDescription("Female", "2 years old", "Black Gray and White", "Philippine Street Cat",
                    "Allergic to any Monello cat food",
                    "Spayed", "Deworm, 4-in-1, Anti-rabies",
                    "Mimi was rescued from the streets as a young stray and adopted into a caring home that helped her recover and thrive.")),

            new Cat("Nene", "Female", 0, 11,
                "/cat_images/cat_nene.png",
                buildPetDescription("Female", "11 months old", "Blue cat", "British shorthair",
                    "No health issues",
                    "Spayed", "Deworm, 4-in-1, Anti-rabies",
                    "Nene was adopted from a trusted breeder and has grown up in a comfortable and affectionate environment.")),

            new Cat("Tisay", "Female", 0, 4,
                "/cat_images/cat_tisay.png",
                buildPetDescription("Female", "4 months old", "Ginger white", "Philippine Street Cat",
                    "Difficulty to walk due to abuse",
                    "Not yet", "Deworm, 4-in-1",
                    "Tisay was saved from an abusive situation and adopted by a compassionate rescuer who is helping her heal and learn to trust again.")),

            new Cat("Sassa", "Female", 4, 0,
                "/cat_images/cat_sassa.png",
                buildPetDescription("Female", "4 years old", "Black", "Philippine Street Cat",
                    "Difficulty to jump due to old age",
                    "Spayed", "Deworm, 4-in-1, Anti-rabies",
                    "Sassa was adopted as a senior stray and now enjoys a peaceful, loving home where she can age gracefully.")),

            new Cat("Siopao", "Female", 3, 0,
                "/cat_images/cat_siopao.png",
                buildPetDescription("Female", "3 years old", "Multi-colored", "Philippine Street Cat",
                    "No right eye, surgically removed",
                    "Spayed", "Deworm, 4-in-1, Anti-rabies",
                    "Siopao was rescued after an accident left her blind in one eye, and she was adopted into a nurturing home that continues to care for her special needs."))
        ));
    }

    /**
     * Helper method to build consistent pet descriptions
     */
    private String buildPetDescription(String gender, String age, String color, String breed,
            String healthStatus, String spayedNeutered, String vaccinations, String description) {
        return String.format(
            "<html><head><style>" +
            "body { font-family: 'Arial Nova Cond', Arial, sans-serif; font-size: 17px; margin: 0; padding: 0; }" +
            "p { margin: 2px 0; padding: 0; line-height: 1.2; }" +
            "br { line-height: 0.5; }" +
            "</style></head><body>" +
            "<p><b><font color='#060644'>Gender:</font></b> <font color='black'>%s</font></p>" +
            "<p><b><font color='#060644'>Age:</font></b> <font color='black'>%s</font></p>" +
            "<p><b><font color='#060644'>Color:</font></b> <font color='black'>%s</font></p>" +
            "<p><b><font color='#060644'>Breed:</font></b> <font color='black'>%s</font></p><br/>" +
            "<p><b><font color='#060644'>Health Status:</font></b> <font color='black'>%s</font></p>" +
            "<p><b><font color='#060644'>Spayed/Neutered:</font></b> <font color='black'>%s</font></p>" +
            "<p><b><font color='#060644'>Vaccinations & Deworm:</font></b> <font color='black'>%s</font></p><br/>" +
            "<p><b><font color='#060644'>Description:</font></b> <font color='black'>%s</font></p>" +
            "</body></html>",
            gender, age, color, breed, healthStatus, spayedNeutered, vaccinations, description
        );
    }

    // ========== EXISTING METHODS ==========

    public ArrayList<Pet> getAllPets() {
        return new ArrayList<>(allPets);
    }

    public ArrayList<Pet> getDogs() {
        return allPets.stream()
                .filter(pet -> pet instanceof Dog)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Pet> getCats() {
        return allPets.stream()
                .filter(pet -> pet instanceof Cat)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addPet(Pet pet) {
        if (pet != null && findPetByName(pet.getName()) == null) {
            allPets.add(pet);
        }
    }

    public boolean removePet(Pet pet) {
        return allPets.remove(pet);
    }

    public Pet findPetByName(String name) {
        return allPets.stream()
                .filter(pet -> pet.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<Pet> getPetsByGender(String gender) {
        if ("All".equalsIgnoreCase(gender)) {
            return getAllPets();
        }
        return allPets.stream()
                .filter(pet -> pet.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // ========== ENHANCED METHODS ==========

    /**
     * Search pets by multiple criteria
     */
    public ArrayList<Pet> searchPets(String name, String gender, String type, Integer minAge, Integer maxAge) {
        return allPets.stream()
                .filter(pet -> name == null || pet.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(pet -> gender == null || "All".equalsIgnoreCase(gender) || pet.getGender().equalsIgnoreCase(gender))
                .filter(pet -> type == null || "All".equalsIgnoreCase(type) || 
                        (pet instanceof Dog && "Dog".equalsIgnoreCase(type)) ||
                        (pet instanceof Cat && "Cat".equalsIgnoreCase(type)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get pets with special needs
     */
    public ArrayList<Pet> getSpecialNeedsPets() {
        return allPets.stream()
                .filter(pet -> !pet.getDescription().toLowerCase().contains("no health issues"))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get pets by breed
     */
    public ArrayList<Pet> getPetsByBreed(String breed) {
        return allPets.stream()
                .filter(pet -> pet.getDescription().toLowerCase().contains(breed.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get pets ready for adoption (vaccinated and spayed/neutered)
     */
    public ArrayList<Pet> getAdoptionReadyPets() {
        return allPets.stream()
                .filter(pet -> {
                    String desc = pet.getDescription().toLowerCase();
                    return (desc.contains("spayed") || desc.contains("neutered")) &&
                           (desc.contains("anti-rabies") || desc.contains("4-in-1"));
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int getTotalPetCount() {
        return allPets.size();
    }

    public int getDogCount() {
        return (int) allPets.stream().filter(pet -> pet instanceof Dog).count();
    }

    public int getCatCount() {
        return (int) allPets.stream().filter(pet -> pet instanceof Cat).count();
    }
}