package api.tengyun.back.controller;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.lyarc.controller.BaseController;

import api.tengyun.back.service.UpdateLogService;

public class UpdateLogController extends BaseController {
    private UpdateLogService service = enhance(UpdateLogService.class);   
    
    
    public void add() {
    	Map<String, Object> log = new HashMap<String,Object>();
    	if(StrKit.notBlank(getPara("title"))){
            log.put("title", getPara("title"));
        }
    	if(StrKit.notBlank(getPara("version"))){
            log.put("version", getPara("version"));
        }
        if(getParaToInt("sys_type")!=0){
            log.put("sys_type", getParaToInt("sys_type"));
        }
        if(StrKit.notBlank(getPara("type"))){
            log.put("type", getPara("type"));
        }
        if(StrKit.notBlank(getPara("content"))){
            log.put("content", getPara("content"));
        }
        if(StrKit.notBlank(getPara("remark"))){
            log.put("remark", getPara("remark"));
        }
    	renderApiRult(service.add(log));
	}
    
    public void queryList() throws UnknownHostException {
    	int sys_type = getParaToInt("sys_type");
    	String type = getPara("type");
    	int page = getParaToInt("page");
    	int rows = getParaToInt("rows");
    	renderApiRult(service.queryList(sys_type, type, page, rows));		
	}
    
    public void getDetail() throws UnknownHostException{
    	int id = getParaToInt("id");
    	renderApiRult(service.getDetail(id));
    }
    
}
