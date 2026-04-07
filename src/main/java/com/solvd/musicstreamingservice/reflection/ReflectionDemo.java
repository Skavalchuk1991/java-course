package com.solvd.musicstreamingservice.reflection;

import com.solvd.musicstreamingservice.model.Genre;
import com.solvd.musicstreamingservice.model.Song;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Demonstrates reflection: inspecting and creating objects dynamically.
 */
public class ReflectionDemo {

    public static void run() {
        System.out.println("\n===== REFLECTION DEMO =====\n");

        Class<Song> clazz = Song.class;

        // 1. Fields — name, type, modifiers
        System.out.println("--- Fields ---");
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println("  " + Modifier.toString(field.getModifiers())
                    + " " + field.getType().getSimpleName()
                    + " " + field.getName());
        }

        // 2. Constructors — parameters
        System.out.println("\n--- Constructors ---");
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            System.out.print("  " + Modifier.toString(constructor.getModifiers()) + " Song(");
            Class<?>[] params = constructor.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(params[i].getSimpleName());
            }
            System.out.println(")");
        }

        // 3. Methods — name, return type, parameters, modifiers
        System.out.println("\n--- Methods (declared) ---");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.print("  " + Modifier.toString(method.getModifiers())
                    + " " + method.getReturnType().getSimpleName()
                    + " " + method.getName() + "(");
            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) System.out.print(", ");
                System.out.print(params[i].getSimpleName());
            }
            System.out.println(")");
        }

        // 4. Create object using reflection
        System.out.println("\n--- Create object via reflection ---");
        try {
            Constructor<Song> ctor = clazz.getDeclaredConstructor(
                    int.class, String.class, int.class, String.class, String.class, Genre.class);
            Genre reflectedGenre = new Genre("Electronic", "Electronic music");
            Song reflectedSong = ctor.newInstance(100, "Reflection Song", 180, "DJ Reflect", "Mirror Album", reflectedGenre);
            System.out.println("Created: " + reflectedSong);
        } catch (Exception e) {
            System.out.println("Reflection object creation failed: " + e.getMessage());
        }

        // 5. Call method using reflection
        System.out.println("\n--- Call method via reflection ---");
        try {
            Genre reflectedGenre = new Genre("Electronic", "Electronic music");
            Constructor<Song> ctor = clazz.getDeclaredConstructor(
                    int.class, String.class, int.class, String.class, String.class, Genre.class);
            Song reflectedSong = ctor.newInstance(101, "Dynamic Call", 200, "Reflector", "Meta Album", reflectedGenre);

            Method getMediaInfo = clazz.getDeclaredMethod("getMediaInfo");
            String result = (String) getMediaInfo.invoke(reflectedSong);
            System.out.println("getMediaInfo() via reflection: " + result);

            Method play = clazz.getMethod("play");
            play.invoke(reflectedSong);
        } catch (Exception e) {
            System.out.println("Reflection method call failed: " + e.getMessage());
        }

        // 6. Custom annotation handling
        System.out.println("\n--- Custom Annotation: @JsonField ---");
        Genre annotationGenre = new Genre("Pop", "Popular music");
        Song annotatedSong = new Song(200, "Annotated Song", 240, "Annotation Artist", "Meta Album", annotationGenre);
        String jsonResult = JsonSerializer.serialize(annotatedSong);
        System.out.println("Serialized Song: " + jsonResult);

        System.out.println("\n===== END OF REFLECTION DEMO =====");
    }
}
