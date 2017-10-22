package api.tengyun.front.controller;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.lyarc.controller.BaseController;
import com.lyarc.service.ServiceError;

import api.tengyun.LoginUser;
import api.tengyun.UserHolder;
import api.tengyun.front.service.EmployeeService;
import api.tengyun.front.service.OutsideWorkService;

public class OutsideWorkController extends BaseController {
	private OutsideWorkService service = enhance(OutsideWorkService.class);
	
	public void reply() throws UnsupportedEncodingException {
		Map<String, Object> log = new HashMap<String,Object>();
		log.put("corp_id", getParaToInt("corp_id"));
		log.put("type", getParaToInt("type"));
		log.put("content", getPara("content"));
        if(getParaToInt("id") != null){
            log.put("id", getParaToInt("id"));
        }else{
            log.put("sign_id", getParaToInt("sign_id"));
        }
        if(StrKit.notBlank(getPara("at"))){
            log.put("at", getPara("at"));
        }
    	renderApiRult(service.reply(log));
	}
	
	public void replyRemove() throws UnknownHostException, ServiceError {
		Map<String, Object> log = new HashMap<String,Object>();
		log.put("corp_id", getParaToInt("corp_id"));
		log.put("reply_id", getParaToInt("reply_id"));
        if(getParaToInt("id") != null){
            log.put("id", getParaToInt("id"));
        }else{
            log.put("sign_id", getParaToInt("sign_id"));
        }
		renderApiRult(service.replyRemove(log));
	}
	
	public void signList() throws UnknownHostException, Exception {
		int corp_id = getParaToInt("corp_id");
		int type = getParaToInt("type");
		LoginUser user = UserHolder.getUser();
		Map<String, Object> emp = EmployeeService.getEmp(corp_id, user.getUser_id());
		Integer emp_id = (Integer) emp.get("emp_id");
		renderApiRult(service.signList(corp_id, type, emp_id));
	}
}
