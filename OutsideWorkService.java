package api.tengyun.front.service;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.lyarc.data.mongo.MongoConnFactory;
import com.lyarc.service.ServiceError;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import api.tengyun.MongoDBUtil;

public class OutsideWorkService {
	
	public int reply(Map<String, Object> log) throws UnsupportedEncodingException {
		
		MongoCollection<Document> collection;
		MongoDatabase db = MongoConnFactory.getFactory().getDataBase();
        Document filter = new Document();
		filter.append("corp_id", log.get("corp_id"));
        if(log.containsKey("id")){
        	collection = db.getCollection("c_ty_outside_work");
        	filter.append("id", log.get("id"));
        }else{
        	collection = db.getCollection("c_ty_outside_work_sign");
        	filter.append("id", log.get("sign_id"));
        }
        Document document = collection.find(filter).first();
		List<Document> reply = (List<Document>) document.get("reply");
        int reply_id = 1;
        if (reply != null) {
            if (reply.size() != 0) {
                for (Document obj : reply) {
                    if (obj.getInteger("id") > reply_id) {
                        reply_id = obj.getInteger("id");
                    }
                }
                reply_id++;
            }
        } else {
            reply = new ArrayList<Document>();
        }
        Document newreply = new Document();
        newreply.append("id", reply_id).append("type", log.get("type")).append("content", log.get("content"));
        if(log.containsKey("at")){
        	String[] atPersonsArray = log.get("at").toString().split(",");
        	newreply.append("at", atPersonsArray);
        }
        reply.add(newreply);
        Document update = new Document();
        update.append("reply", reply);
		collection.findOneAndUpdate(filter, new BasicDBObject().append("$set", update));
		return reply_id;
	}
	
	public boolean replyRemove(Map<String, Object> log) throws UnknownHostException, ServiceError {
		
		MongoCollection<Document> collection;
		MongoDatabase db = MongoConnFactory.getFactory().getDataBase();
        Document filter = new Document();
        filter.append("corp_id", log.get("corp_id"));
        if (log.containsKey("id")){
        	collection = db.getCollection("c_ty_outside_work");
        	filter.append("id", log.get("id"));
        }else{
        	collection = db.getCollection("c_ty_outside_work_sign");
        	filter.append("id", log.get("sign_id"));
        }
        Document document = collection.find(filter).first();
        if (document == null) {
            throw ServiceError.Create("该工作日志不存在！", 1);
        }
        List<Document> reply = (List<Document>) document.get("reply");
        if (reply != null) {
            if (reply.size() != 0) {
                boolean ret = false;
                for (int i = 0; i < reply.size(); i++) {
                    Document obj = reply.get(i);
                    if (obj.getInteger("id") == log.get("reply_id")) {
                        reply.remove(i);
                        ret = true;
                    }
                }
                if (!ret) {
                    throw ServiceError.Create("该条评论不存在！", 2);
                }
            } else {
                throw ServiceError.Create("评论列表为空！", 3);
            }
        } else {
            throw ServiceError.Create("评论列表不存在！", 4);
        }
        Document update = new Document();
        update.append("reply", reply);
        collection.updateOne(filter, new BasicDBObject().append("$set", update));
        return true;
	}
	
	public List<Object> signList(int corp_id, int type, Integer emp_id) throws UnknownHostException {
		List<Object> list = new ArrayList<Object>();
		DBCollection c_ty_outside_work_sign = MongoDBUtil.getDBCollection("c_ty_outside_work_sign");
		BasicDBObject query = new BasicDBObject().append("corp_id", corp_id);
		DBCursor cursor;
		if (type == 1){
			query.append("create_person", emp_id);
			cursor = c_ty_outside_work_sign.find(query);
			while (cursor.hasNext()){
				list.add(cursor.next());
			}
		}
		if (type == 2){
			cursor = c_ty_outside_work_sign.find(query);
			while (cursor.hasNext()){
				DBObject record = cursor.next();
				List<BasicDBObject> related_persons = (List<BasicDBObject>) record.get("related_persons");
				for (BasicDBObject person : related_persons){
					if (person.getInt("emp_id") == emp_id){
						list.add(record);
					}
				}
			}
		}
		return list;
	}

}
