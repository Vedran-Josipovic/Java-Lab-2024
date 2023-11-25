package hr.java.production.utility;

import hr.java.production.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final String CATEGORIES_TEXT_FILE_NAME = "Josipovic-6/src/main/dat/categories.txt";

    /**
     *
     * Za razliku od ScannerInputProcessor.inputCategories ne provjeravaju se duplikati.
     * Možda uvesti to po ID-u idk.
     */
    public List<Category> inputCategories() {
        List<Category> categories = new ArrayList<>();
        File file = new File(CATEGORIES_TEXT_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Optional<String> idOptional;
            while ((idOptional = Optional.ofNullable(reader.readLine())).isPresent()) {
                Optional<Category> newCategoryOptional;

                Long id = Long.parseLong(idOptional.get());
                String name = reader.readLine();
                String description = reader.readLine();

                newCategoryOptional = Optional.of(new Category(id, name, description));
                newCategoryOptional.ifPresent(categories::add);
            }
        } catch (FileNotFoundException e) {
            String msg = "Couldn't locate file at specified location [" + CATEGORIES_TEXT_FILE_NAME + "]";
            logger.error(msg, e);
            System.out.println(msg);
        } catch (IOException e) {
            String msg = "IO Exception occurred.";
            logger.error(msg, e);
            System.out.println(msg);
        }


        return categories;
    }


}
