package data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PantryItem.class, Recipe.class, RecipeIngredient.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PantryItemDao pantryItemDao();
    public abstract RecipeDao recipeDao();

    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "smart_pantry_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                RecipeDao dao = INSTANCE.recipeDao();

                // 1. Tomato Pasta
                long id1 = dao.insertRecipe(new Recipe("Tomato Pasta", "1. Boil pasta. 2. Fry tomatoes and garlic. 3. Mix together."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id1, "pasta", 200, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id1, "tomato", 2, "pieces"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id1, "garlic", 2, "cloves"));

                // 2. Tuna Sandwich
                long id2 = dao.insertRecipe(new Recipe("Tuna Sandwich", "1. Mix tuna and mayo. 2. Toast bread. 3. Add tuna mix, lettuce, and cucumber."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id2, "bread", 2, "slice"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id2, "tuna", 1, "can"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id2, "mayonnaise", 2, "tbsp"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id2, "lettuce", 2, "piece"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id2, "cucumber", 0.5, "piece"));

                // 3. Tiramisu Overnight Oats
                long id3 = dao.insertRecipe(new Recipe("Tiramisu Overnight Oats", "1. Mix oats, milk, yogurt, coffee, and honey in a jar. 2. Top with cocoa powder. 3. Refrigerate overnight."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id3, "oats", 50, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id3, "milk", 150, "ml"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id3, "yogurt", 50, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id3, "coffee", 30, "ml"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id3, "honey", 1, "tbsp"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id3, "cocoa powder", 1, "tbsp"));

                // 4. Creamy Pasta
                long id4 = dao.insertRecipe(new Recipe("Creamy Pasta", "1. Boil pasta. 2. Melt butter and fry garlic. 3. Add cream and cheese. 4. Mix with pasta."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id4, "pasta", 200, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id4, "cream", 150, "ml"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id4, "cheese", 50, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id4, "garlic", 2, "clove"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id4, "butter", 1, "tbsp"));

                // 5. French Toast
                long id5 = dao.insertRecipe(new Recipe("French Toast", "1. Whisk eggs, milk, and cinnamon. 2. Dip bread in mixture. 3. Fry in butter until golden."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id5, "bread", 4, "slice"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id5, "egg", 2, "piece"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id5, "milk", 100, "ml"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id5, "cinnamon", 1, "tsp"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id5, "butter", 1, "tbsp"));

                // 6. Tuna Salad
                long id6 = dao.insertRecipe(new Recipe("Tuna Salad", "1. Open tuna. 2. Mix with mayo and chopped onion."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id6, "tuna", 1, "can"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id6, "mayonnaise", 2, "tbsp"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id6, "onion", 1, "piece"));

                // 7. Mac and Cheese
                long id7 = dao.insertRecipe(new Recipe("Mac and Cheese", "1. Boil macaroni. 2. Melt cheese with milk. 3. Mix."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id7, "macaroni", 200, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id7, "cheese", 100, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id7, "milk", 100, "ml"));

                // 8. Chicken Stir Fry
                long id8 = dao.insertRecipe(new Recipe("Chicken Stir Fry", "1. Cook chicken. 2. Add veggies and soy sauce. 3. Serve."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id8, "chicken", 200, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id8, "carrot", 1, "piece"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id8, "soy sauce", 2, "tbsp"));

                // 9. Garlic Bread
                long id9 = dao.insertRecipe(new Recipe("Garlic Bread", "1. Mix butter and garlic. 2. Spread on bread. 3. Bake."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id9, "bread", 4, "slices"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id9, "butter", 50, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id9, "garlic", 2, "cloves"));

                // 10. Pancakes
                long id10 = dao.insertRecipe(new Recipe("Pancakes", "1. Mix flour, milk, egg. 2. Fry in pan."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id10, "flour", 1, "cup"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id10, "milk", 1, "cup"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id10, "egg", 1, "piece"));

                // 11. Fruit Smoothie
                long id11 = dao.insertRecipe(new Recipe("Fruit Smoothie", "1. Blend all ingredients together."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id11, "banana", 1, "piece"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id11, "milk", 200, "ml"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id11, "honey", 1, "tbsp"));

                // 12. Grilled Cheese
                long id12 = dao.insertRecipe(new Recipe("Grilled Cheese", "1. Butter bread. 2. Put cheese inside. 3. Grill."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id12, "bread", 2, "slices"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id12, "cheese", 50, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id12, "butter", 1, "tbsp"));

                // 13. Vegetable Soup
                long id13 = dao.insertRecipe(new Recipe("Vegetable Soup", "1. Boil water. 2. Add chopped veggies. 3. Simmer."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id13, "water", 500, "ml"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id13, "carrot", 2, "pieces"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id13, "onion", 1, "piece"));

                // 14. Beef Tacos
                long id14 = dao.insertRecipe(new Recipe("Beef Tacos", "1. Cook beef. 2. Put in taco shells with cheese."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id14, "beef", 250, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id14, "taco shells", 3, "pieces"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id14, "cheese", 50, "g"));

                // 15. Avocado Toast
                long id15 = dao.insertRecipe(new Recipe("Avocado Toast", "1. Toast bread. 2. Mash avocado on top."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id15, "bread", 2, "slices"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id15, "avocado", 1, "piece"));

                // 16. Beef Stew
                long id16 = dao.insertRecipe(new Recipe("Beef Stew", "1. Brown beef in a pot. 2. Add chopped veggies. 3. Pour in broth and simmer for 1 hour."));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id16, "beef", 500, "g"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id16, "potato", 3, "piece"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id16, "carrot", 2, "piece"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id16, "onion", 1, "piece"));
                dao.insertRecipeIngredient(new RecipeIngredient((int)id16, "beef broth", 500, "ml"));

            });
        }
    };
}
