package com.example.exhibitionguide;

public class Exhibition {
    private String name, description;
    private int exhibitionID;

    public static final Exhibition[] exhibitions = {
            new Exhibition("Art Gallery", "Visit the museum with our tour guide", R.drawable.art_gallery),
            new Exhibition("WWI Exhibition", "Explore world war I exhibition", R.drawable.wwi),
            new Exhibition("Exploring the space", "Explore the universe with our experts", R.drawable.visit_space),
            new Exhibition("Visual show", "Some interesting visual show for entertainment", R.drawable.visual_show)
    };

    private Exhibition(String name, String description, int exhibitionID){
        this.name = name;
        this.description = description;
        this.exhibitionID = exhibitionID;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public int getExhibitionID(){
        return this.exhibitionID;
    }
}
