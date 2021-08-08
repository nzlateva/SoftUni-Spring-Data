package bg.softuni.springdata.automapping.model.dto;

import java.math.BigDecimal;

public class GameViewDto {

    private String title;
    private String trailer;
    private String imageThumbnail;
    private Double size;
    private BigDecimal price;
    private String description;
    private String releaseDate;

    public GameViewDto() {
    }

    public GameViewDto(String title, String trailer, String imageThumbnail, Double size, BigDecimal price, String description, String releaseDate) {
        this.title = title;
        this.trailer = trailer;
        this.imageThumbnail = imageThumbnail;
        this.size = size;
        this.price = price;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Game{");
        sb.append("title='").append(title).append('\'');
        sb.append(", trailer='").append(trailer).append('\'');
        sb.append(", imageThumbnail='").append(imageThumbnail).append('\'');
        sb.append(", size=").append(size);
        sb.append(", price=").append(price);
        sb.append(", description='").append(description).append('\'');
        sb.append(", releaseDate='").append(releaseDate).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
