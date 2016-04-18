package gd.model;

import java.util.SortedMap;

/**
 * Created by anle on 4/10/16.
 */
public class ContentList {
    String companyId;
    String companyName;
    String industry;
    String size;
    String competitors;
    public SortedMap<String, Integer> reviews;
    public SortedMap<String, Integer> interviews;

    public ContentList(String companyId, String companyName, String industry, String size, String competitors, SortedMap<String, Integer> reviews, SortedMap<String, Integer> interviews) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.industry = industry;
        this.size = size;
        this.reviews = reviews;
        this.interviews = interviews;
        this.competitors = competitors;
    }

    @Override
    public String toString() {
        return "ContentList{" +
                "companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", industry='" + industry + '\'' +
                ", size='" + size + '\'' +
                ", competitors='" + competitors + '\'' +
                ", reviews=" + reviews +
                ", interviews=" + interviews +
                '}';
    }

    public SortedMap<String, Integer> getInterviews() {
        return interviews;
    }

    public void setInterviews(SortedMap<String, Integer> interviews) {
        this.interviews = interviews;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public SortedMap<String, Integer> getReviews() {
        return reviews;
    }

    public void setReviews(SortedMap<String, Integer> reviews) {
        this.reviews = reviews;
    }
}