package flore.cristi.project.model.entity;

public class WeatherEntity {

    private String main;
    private String description;
    private long clouds;
    private double temp;
    private double feels_like;

    public WeatherEntity(String main, String description, long clouds, double temp, double feels_like) {
        this.main = main;
        this.description = description;
        this.clouds = clouds;
        this.temp = temp;
        this.feels_like = feels_like;
    }

    public WeatherEntity() {}
    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getClouds() {
        return clouds;
    }

    public void setClouds(long clouds) {
        this.clouds = clouds;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", clouds='" + clouds + '\'' +
                ", temp=" + temp +
                ", feels_like=" + feels_like +
                '}';
    }
}
