package com.cy.pj.sys.pojo.sys;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.entity.SysLog;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class SerializableTests {
	
	 /**将对象转换为json格式字符串，并对其进行反序列化*/
	 @Test
	 public void testSerializeJsonStr()throws Exception {
		 SysLog log=new SysLog();
		 log.setId(100L);
		 log.setIp("192.168.1.111");
		 log.setUsername("admin");
		 log.setOperation("update");
		 log.setMethod("xxx.deleteObject");
		 log.setParams("1");
		 log.setTime(10l);
		 log.setCreatedTime(new Date());
		 
		 //广义的序列化和反序列化
		 //将对象序列化为json格式的字符串
		 ObjectMapper oMapper=new ObjectMapper();
		 String jsonStr=oMapper.writeValueAsString(log);
		 System.out.println("jsonStr="+jsonStr);
		 System.out.println("序列化ok");
		 //将json格式的字符串反序列化为java对象
		 SysLog newLog=oMapper.readValue(jsonStr, SysLog.class);
		 System.out.println(newLog);
	 }
	 @Test
	 public void testSerializeByte()throws Exception {
		 SysLog log=new SysLog();
		 log.setId(100L);
		 log.setIp("192.168.1.111");
		 log.setUsername("admin");
		 log.setOperation("update");
		 log.setMethod("xxx.deleteObject");
		 log.setParams("1");
		 log.setTime(10l);
		 log.setCreatedTime(new Date());
		 //将log对象序列化
		 ObjectOutputStream out=
		 new ObjectOutputStream(new FileOutputStream(new File("f1.txt")));
		 out.writeObject(log);
		 out.close();
		 System.out.println("序列化ok");
	 }
	 /**反序列化*/
	 @Test
	 public void testDeserializeObject()throws Exception {
		 ObjectInputStream in=
	     new ObjectInputStream(new FileInputStream(new File("f1.txt")));
		 Object obj=in.readObject();
		 System.out.println(obj);
		 in.close();
	 }
}





