package hr.java.production.enumeration;

public enum FilePath {
    CATEGORIES("Josipovic-6/src/main/dat/input/categories.txt"),
    ITEMS("Josipovic-6/src/main/dat/input/items.txt"),
    ADDRESSES("Josipovic-6/src/main/dat/input/addresses.txt"),
    FACTORIES("Josipovic-6/src/main/dat/input/factories.txt"),
    STORES("Josipovic-6/src/main/dat/input/stores.txt"),
    SERIALIZED_FACTORIES("Josipovic-6/src/main/dat/serialized-objects/serialized-factories.txt"),
    SERIALIZED_STORES("Josipovic-6/src/main/dat/serialized-objects/serialized-stores.txt");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
