package bg.softuni.xmlprocessing.exercise.util;

public interface XmlParser {

    <T> String serialize(T entity);

    <T> void serialize(T entity, String fileName);

    <T> T deserialize(String data, Class<T> tClass);

    <T> T deserializeFromFile(String fileName, Class<T> tClass);
}
