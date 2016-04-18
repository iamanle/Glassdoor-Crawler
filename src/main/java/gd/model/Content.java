package gd.model;

/**
 * Created by anle on 4/5/16.
 */
public class Content implements Comparable{
    String companyId;
    String companyName;
    String industry;
    String size;
    String date;

    public Content(String id, String name, String industry, String size, String date){
        this.companyId = id;
        this.companyName = name;
        this.industry = industry;
        this.size = size;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        if (!companyId.equals(content.companyId)) return false;
        return date.equals(content.date);

    }

    @Override
    public int hashCode() {
        int result = companyId.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Content{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", industry='" + industry + '\'' +
                ", size='" + size + '\'' +
                ", date='" + date + '\'' +
                '}';
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(Object o) {
        return this.getDate().compareTo(((Content)o).getDate());
    }
}
