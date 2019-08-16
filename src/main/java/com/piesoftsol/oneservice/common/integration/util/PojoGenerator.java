package com.piesoftsol.oneservice.common.integration.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

public class PojoGenerator {
	
	public static BeanMap generateBean(Object mapObject) {
	    BeanGenerator generator = new BeanGenerator();
	    if(mapObject instanceof JSONObject) {
	    	JSONObject json = (JSONObject)mapObject;
		    Iterator<String> keys = json.keys();
	
		    while (keys.hasNext()) {
		        Object key = keys.next();
		        generator.addProperty(key.toString(), Object.class);
		        
		    }
	
		    Object result = generator.create();
		    BeanMap bean = BeanMap.create(result);
		    keys = json.keys();
	
		    while (keys.hasNext()) {
		        Object key = keys.next();
		        Object value = json.get(key.toString());
		        if(value instanceof JSONArray) {
		        	List<Object> newmap = new ArrayList<Object>();
		        	for (Object o : json.getJSONArray(key.toString())) {
		        		if(o instanceof JSONObject) {
			        		BeanMap beanMap = PojoGenerator.generateBean((JSONObject)o);
			        		newmap.add(beanMap);
		        		}
		        		else {
		        			newmap.add(o.toString());
		        		}
					}
		        	
		        	bean.put(key, newmap);
		        	
		        }else if(value instanceof JSONObject) {
		        	BeanMap beanMap = PojoGenerator.generateBean((JSONObject)value);
		        	bean.put(key, beanMap);
		        }else {
		        	bean.put(key, value);
		        }
		    }
	
		    return bean;
		}else {
			return null;
		}
	}
}
