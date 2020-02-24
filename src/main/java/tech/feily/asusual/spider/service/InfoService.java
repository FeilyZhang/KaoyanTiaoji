package tech.feily.asusual.spider.service;

import java.util.List;

import tech.feily.asusual.spider.model.InfoModel;

public interface InfoService {

    public abstract void add(InfoModel info);
    public abstract void update(InfoModel info);
    public abstract List<InfoModel> selectAll();
    
}
