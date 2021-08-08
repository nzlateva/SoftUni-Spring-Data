package bg.softuni.springdata.automapping.config;

import bg.softuni.springdata.automapping.model.dto.AddGameDto;
import bg.softuni.springdata.automapping.model.entity.Game;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        Converter<String, LocalDate> toLocalDateConverter = new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                return mappingContext.getSource() == null
                        ? null
                        : LocalDate.parse(mappingContext.getSource(),
                        DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        };

        Converter<LocalDate, String> toStringConverter = new Converter<LocalDate, String>() {
            @Override
            public String convert(MappingContext<LocalDate, String> mappingContext) {
                return mappingContext.getSource() == null
                        ? null
                        : mappingContext.getSource().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        };

        modelMapper.addConverter(toLocalDateConverter);
        modelMapper.addConverter(toStringConverter);

        modelMapper.typeMap(AddGameDto.class, Game.class)
                .addMappings(mapper -> mapper.map(
                        AddGameDto::getThumbnailUrl, Game::setImageThumbnail));

        modelMapper.typeMap(Game.class, AddGameDto.class)
                .addMappings(mapper -> mapper.map(
                        Game::getImageThumbnail, AddGameDto::setThumbnailUrl));


        return modelMapper;
    }
}
