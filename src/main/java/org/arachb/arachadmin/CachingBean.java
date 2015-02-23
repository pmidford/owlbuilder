package org.arachb.arachadmin;

import java.util.HashMap;
import java.util.Map;


public abstract class CachingBean implements BeanBase {

	private static final Map<Integer, CachingBean> cache = new HashMap<Integer, CachingBean>();
	

	static boolean isCached(int id){
		return cache.containsKey(id);
	}
	
	static CachingBean getCached(int id){
		assert(cache.containsKey(id));  //shouldn't be calling this w/o checking first
		return cache.get(id);
	}

	static void cache(CachingBean cb){
		final int id = cb.getId();
		cache.put(id, cb);
	}

	

}
