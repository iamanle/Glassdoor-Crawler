package gd.model;

/**
 * Created by anle on 4/10/16.
 */
public class Company {
    String id;
    String name;
    String industry;
    String size;
    String competitors;

    public Company() {
    }

    public Company(String id, String name, String industry, String size, String competitors) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.size = size;
        this.competitors= competitors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCompetitors() {
        return competitors;
    }

    public void setCompetitors(String competitors) {
        this.competitors = competitors;
    }

}