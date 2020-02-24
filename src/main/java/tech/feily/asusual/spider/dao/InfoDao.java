package tech.feily.asusual.spider.dao;

import java.util.List;

import tech.feily.asusual.spider.model.InfoModel;

public interface InfoDao {

    public abstract void add(InfoModel info);
    public abstract void update(InfoModel info);
    public abstract List<InfoModel> selectAll();
    
}
