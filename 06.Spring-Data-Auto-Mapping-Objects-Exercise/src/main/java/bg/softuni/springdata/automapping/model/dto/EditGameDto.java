package bg.softuni.springdata.automapping.model.dto;

public class EditGameDto {

    private Long id;
    private String[] values;

    public EditGameDto() {
    }

    public EditGameDto(Long id, String... values) {
        this.id = id;
        this.values = values;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
