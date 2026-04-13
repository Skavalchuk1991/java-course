package com.solvd.musicstreamingservice.reflection;

import com.solvd.musicstreamingservice.model.Genre;
import com.solvd.musicstreamingservice.model.Song;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Demonstrates reflection: inspecting and creating objects dynamically.
 */
public class ReflectionDemo {

    private static final Logger LOGGER = LogManager.getLogger(ReflectionDemo.class);

    public static void run() {
        LOGGER.info("\n===== REFLECTION DEMO =====\n");

        Class<Song> clazz = Song.class;

        // 1. Fields — name, type, modifiers
        LOGGER.info("--- Fields ---");
        for (Field field : clazz.getDeclaredFields()) {
            LOGGER.info("  {} {} {}",
                    Modifier.toString(field.getModifiers()),
                    field.getType().getSimpleName(),
                    field.getName());
        }

        // 2. Constructors — parameters
        LOGGER.info("\n--- Constructors ---");
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            StringBuilder sb = new StringBuilder();
            sb.append("  ").append(Modifier.toString(constructor.getModifiers())).append(" Song(");
            Class<?>[] params = constructor.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(params[i].getSimpleName());
            }
            sb.append(")");
            LOGGER.info(sb.toString());
        }

        // 3. Methods — name, return type, parameters, modifiers
        LOGGER.info("\n--- Methods (declared) ---");
        for (Method method : clazz.getDeclaredMethods()) {
            StringBuilder sb = new StringBuilder();
            sb.append("  ").append(Modifier.toString(method.getModifiers()))
                    .append(" ").append(method.getReturnType().getSimpleName())
                    .append(" ").append(method.getName()).append("(");
            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(params[i].getSimpleName());
            }
            sb.append(")");
            LOGGER.info(sb.toString());
        }

        // 4. Create object using reflection
        LOGGER.info("\n--- Create object via reflection ---");
        try {
            Constructor<Song> ctor = clazz.getDeclaredConstructor(
                    int.class, String.class, int.class, String.class, String.class, Genre.class);
            Genre reflectedGenre = new Genre("Electronic", "Electronic music");
            Song reflectedSong = ctor.newInstance(100, "Reflection Song", 180, "DJ Reflect", "Mirror Album", reflectedGenre);
            LOGGER.info("Created: {}", reflectedSong);
        } catch (Exception e) {
            LOGGER.error("Reflection object creation failed: {}", e.getMessage());
        }

        // 5. Call method using reflection
        LOGGER.info("\n--- Call method via reflection ---");
        try {
            Genre reflectedGenre = new Genre("Electronic", "Electronic music");
            Constructor<Song> ctor = clazz.getDeclaredConstructor(
                    int.class, String.class, int.class, String.class, String.class, Genre.class);
            Song reflectedSong = ctor.newInstance(101, "Dynamic Call", 200, "Reflector", "Meta Album", reflectedGenre);

            Method getMediaInfo = clazz.getDeclaredMethod("getMediaInfo");
            String result = (String) getMediaInfo.invoke(reflectedSong);
            LOGGER.info("getMediaInfo() via reflection: {}", result);

            Method play = clazz.getMethod("play");
            play.invoke(reflectedSong);
        } catch (Exception e) {
            LOGGER.error("Reflection method call failed: {}", e.getMessage());
        }

        // 6. Custom annotation handling
        LOGGER.info("\n--- Custom Annotation: @JsonField ---");
        Genre annotationGenre = new Genre("Pop", "Popular music");
        Song annotatedSong = new Song(200, "Annotated Song", 240, "Annotation Artist", "Meta Album", annotationGenre);
        String jsonResult = JsonSerializer.serialize(annotatedSong);
        LOGGER.info("Serialized Song: {}", jsonResult);

        LOGGER.info("\n===== END OF REFLECTION DEMO =====");
    }
}
