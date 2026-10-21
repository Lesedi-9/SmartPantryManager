package com.magakwe.smartpantrymanager;

import java.util.List;
import data.PantryItem;
import data.RecipeIngredient;

public class RecipeMatcher {

    // Helper to fix plurals (e.g., "tomatoes" -> "tomato", "eggs" -> "egg")
    public static String normalizeName(String name) {
        if (name == null) return "";
        name = name.trim().toLowerCase();
        if (name.endsWith("s") && name.length() > 3) {
            // Basic singularization: tomatoes -> tomato, eggs -> egg
            if (name.endsWith("oes")) {
                return name.substring(0, name.length() - 2);
            }
            return name.substring(0, name.length() - 1);
        }
        return name;
    }

    // Helper to fix unit strings (e.g., "grams" -> "g", "pieces" -> "piece")
    public static String normalizeUnit(String unit) {
        if (unit == null) return "";
        unit = unit.trim().toLowerCase();
        if (unit.equals("grams") || unit.equals("gram")) return "g";
        if (unit.equals("milliliters") || unit.equals("milliliter") || unit.equals("mls")) return "ml";
        if (unit.equals("pieces") || unit.equals("pc") || unit.equals("pcs")) return "piece";
        if (unit.equals("slices")) return "slice";
        if (unit.equals("cloves")) return "clove";
        if (unit.equals("tablespoons") || unit.equals("tablespoon")) return "tbsp";
        if (unit.equals("teaspoons") || unit.equals("teaspoon")) return "tsp";
        if (unit.equals("cups")) return "cup";
        if (unit.equals("cans")) return "can";
        return unit; // return as-is if we don't recognize it
    }

    // The Core Strict-Matching Logic
    public static boolean isStrictMatch(List<RecipeIngredient> requiredIngredients, List<PantryItem> pantryItems) {
        for (RecipeIngredient req : requiredIngredients) {
            String reqName = normalizeName(req.name);
            String reqUnit = normalizeUnit(req.unit);
            boolean ingredientFound = false;

            for (PantryItem pantryItem : pantryItems) {
                String pName = normalizeName(pantryItem.name);
                String pUnit = normalizeUnit(pantryItem.unit);

                // If the names and units match, we check the quantity
                if (reqName.equals(pName) && reqUnit.equals(pUnit)) {
                    if (pantryItem.quantity >= req.quantity) {
                        ingredientFound = true;
                        break; // Found a match with enough quantity, move to next ingredient
                    }
                }
            }

            // If we loop through the whole pantry and don't find this ingredient (or enough of it)
            // the recipe is a STRICT FAIL.
            if (!ingredientFound) {
                return false;
            }
        }
        // If we checked all ingredients and none failed, it's a match!
        return true;
    }
}
