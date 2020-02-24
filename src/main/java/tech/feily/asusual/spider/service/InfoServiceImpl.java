package tech.feily.asusual.spider.service;

import java.util.List;

import tech.feily.asusual.spider.dao.InfoDao;
import tech.feily.asusual.spider.dao.InfoDaoImpl;
import tech.feily.asusual.spider.model.InfoModel;

public class InfoServiceImpl implements InfoService {

    InfoDao dao = new InfoDaoImpl();
    
    public void add(InfoModel info) {
        dao.add(info);
    }

    public void update(InfoModel info) {
        dao.update(info);
    }

    public List<InfoModel> selectAll() {
        return dao.selectAll();
    }

}
