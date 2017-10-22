package api.tengyun.back.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.jfinal.plugin.activerecord.Record;
import com.lyarc.data.mongo.MongoConnFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import api.tengyun.MongoDBUtil;
import api.tengyun.front.service.ServiceCommon;

public class UpdateLogService {


	public int add(Map<String, Object> log) {

		 MongoDatabase db = MongoConnFactory.getFactory().getDataBase();
         MongoCollection<Document> c_log_updatelog = db.getCollection("c_log_updatelog");
         Document row = new Document();
		 int id = ServiceCommon.getSequence("c_log_updatelog");
		 row.append("id", id);
		 row.append("title", log.get("title"));
		 row.append("version", log.get("version"));
		 row.append("sys_type", log.get("sys_type"));
		 row.append("type", log.get("type"));
		 row.append("content", log.get("content"));
		 row.append("remark", log.get("remark"));
		 row.append("update_time", new Date());
		 c_log_updatelog.insertOne(row);
		 return id;
	    }
	
	public List<Object> queryList(int sys_type, String type, int page, int rows) throws UnknownHostException{
		
		List<Object> list = new ArrayList<Object>();
		DBCollection userCollection =MongoDBUtil.getDBCollection("c_log_updatelog");
		BasicDBObject query = new BasicDBObject();
		query.append("sys_type", sys_type).append("type", type);
		DBCursor cursor = userCollection.find(query).skip((page - 1) * rows).limit(rows);
		while (cursor.hasNext()){
			list.add(cursor.next());
		}
		return list;
	}
	
	public Record getDetail(int id) throws UnknownHostException{
		DBCollection userCollection = MongoDBUtil.getDBCollection("c_log_updatelog");
		BasicDBObject query = new BasicDBObject();
		query.append("id", id);
		DBObject result = userCollection.findOne(query);
		Record item=new Record();
		item.set("id", id).set("title", result.get("title")).set("version", result.get("version"))
			.set("sys_type", result.get("sys_type")).set("type", result.get("type"))
			.set("content", result.get("content")).set("remark", result.get("remark"))
			.set("update_time", result.get("update_time"));
		return item;
	}
}
