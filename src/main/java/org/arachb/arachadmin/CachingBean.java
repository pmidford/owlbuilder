package org.arachb.arachadmin;



public interface CachingBean  {

	void cache();

	/**
	 * Less an update, but more a warning if multiple copies of 'cached' beans are floating
	 * around - inconsistency is a likely outcome
	 */
	void updatecache();

}
