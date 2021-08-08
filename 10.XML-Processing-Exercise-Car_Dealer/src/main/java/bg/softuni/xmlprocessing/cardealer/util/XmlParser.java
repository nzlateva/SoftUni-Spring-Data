package bg.softuni.xmlprocessing.cardealer.util;

public interface XmlParser {

    <T> void serialize(T entity, String fileName);

    <T> T deserializeFromFile(String fileName, Class<T> tClass);
}
