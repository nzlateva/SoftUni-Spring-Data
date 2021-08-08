package bg.softuni.xmlprocessing.cardealer.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        Converter<String, LocalDateTime> converterToLocalDateTime = new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {
                return mappingContext.getSource() == null
                        ? null
                        : LocalDateTime.parse(
                        mappingContext.getSource(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            }
        };

        Converter<LocalDateTime, String> converterToString = new Converter<LocalDateTime, String>() {
            @Override
            public String convert(MappingContext<LocalDateTime, String> mappingContext) {
                return mappingContext.getSource() == null
                        ? null
                        : mappingContext.getSource()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            }
        };

        modelMapper.addConverter(converterToLocalDateTime);
        modelMapper.addConverter(converterToString);

        return modelMapper;
    }

}
