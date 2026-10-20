package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String steps;

    public Recipe(String name, String steps) {
        this.name = name;
        this.steps = steps;
    }
}