package tech.feily.asusual.spider.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import tech.feily.asusual.spider.model.CampusModel;

public class CampusUtils {

    @SuppressWarnings("resource")
    public static List<CampusModel> initCampuses(String file, List<CampusModel> campuses) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(file)));
            String strline = null;
            while ((strline = br.readLine()) != null) {
                CampusModel campusModel = new CampusModel();
                campusModel.setName(strline.split("=")[0]);
                campusModel.setUrl(strline.split("=")[1]);
                campuses.add(campusModel);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return campuses;
    }
}
