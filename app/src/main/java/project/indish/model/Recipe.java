package project.indish.model;

import java.util.List;

public class Recipe {


    private String description;
    private String forked;
    private String image;
    private String id;
    private List<Ingredient> ingredient;
    private String name;
    private String notes;
    private List<Step> step;
    private List<String> tag;

    public Recipe(String description, String forked, String image, String id, List<Ingredient> ingredient, String name, String notes, List<Step> step, List<String> tag) {
        this.description = description;
        this.forked = forked;
        this.image = image;
        this.id = id;
        this.ingredient = ingredient;
        this.name = name;
        this.notes = notes;
        this.step = step;
        this.tag = tag;
    }

    public Recipe(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getForked() {
        return forked;
    }

    public void setForked(String forked) {
        this.forked = forked;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Step> getStep() {
        return step;
    }

    public void setStep(List<Step> step) {
        this.step = step;
    }

//    public List<String> getTag() {
//        return tag;
//    }
//
//    public void setTag(List<String> tag) {
//        this.tag = tag;
//    }
}
