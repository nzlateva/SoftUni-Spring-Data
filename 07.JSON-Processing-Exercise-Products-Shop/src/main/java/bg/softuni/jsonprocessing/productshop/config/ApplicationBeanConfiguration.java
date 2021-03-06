package bg.softuni.jsonprocessing.productshop.config;

import bg.softuni.jsonprocessing.productshop.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<User, String> converter = new Converter<User, String>() {
            @Override
            public String convert(MappingContext<User, String> mappingContext) {
                return mappingContext.getSource() == null
                        ? null
                        : mappingContext.getSource().getFirstName() == null
                        ? mappingContext.getSource().getLastName()
                        : String.format("%s %s",
                        mappingContext.getSource().getFirstName(),
                        mappingContext.getSource().getLastName());
            }
        };

        modelMapper.addConverter(converter);

        return modelMapper;
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

}
