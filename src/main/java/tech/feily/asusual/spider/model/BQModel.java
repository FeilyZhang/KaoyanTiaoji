package tech.feily.asusual.spider.model;

public class BQModel {

    private InfoModel infoModel;
    private CampusModel campusModel;
    
    public void setInfoModel(InfoModel infoModel) {
        this.infoModel = infoModel;
    }
    
    public void setCampusModel(CampusModel campusModel) {
        this.campusModel = campusModel;
    }
    
    public InfoModel getInfoModel() {
        return infoModel;
    }
    
    public CampusModel getCampusModel() {
        return campusModel;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(infoModel.toString());
        sb.append(campusModel.toString());
        return sb.toString();
    }
    
}
