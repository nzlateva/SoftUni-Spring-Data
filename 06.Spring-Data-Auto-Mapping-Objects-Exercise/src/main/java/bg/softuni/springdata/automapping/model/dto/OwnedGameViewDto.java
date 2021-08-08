package bg.softuni.springdata.automapping.model.dto;

public class OwnedGameViewDto {

    private String title;

    public OwnedGameViewDto() {
    }

    public OwnedGameViewDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
