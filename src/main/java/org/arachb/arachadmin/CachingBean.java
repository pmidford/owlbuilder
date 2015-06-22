package org.arachb.arachadmin;



public interface CachingBean  {

	/**
	 * Caches the object in a id cache and possibly a source_id cache
	 * Eventually the integer id should go away
	 */
	void cache();

	/**
	 * Less an update, but more a warning if multiple copies of 'cached' beans are floating
	 * around - inconsistency is a likely outcome
	 */
	void updatecache();

}
