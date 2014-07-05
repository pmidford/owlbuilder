package org.arachb.owlbuilder.lib;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.arachb.arachadmin.AbstractConnection;
import org.arachb.arachadmin.TaxonBean;
import org.arachb.owlbuilder.Owlbuilder;
import org.semanticweb.owlapi.model.OWLObject;

public class Taxon implements AbstractNamedEntity{

	private static Logger log = Logger.getLogger(Taxon.class);
	private final TaxonBean bean;

	public Taxon(TaxonBean b){
		bean = b;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OWLObject generateOWL(Owlbuilder b) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGeneratedId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getGeneratedId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIriString() {
		return bean.getIriString();
	}

	@Override
	public String checkIriString() {
		if (bean.get_available_id() == null){
			throw new IllegalStateException("Term has neither assigned nor generated id");
		}
		else {
			return bean.get_available_id();
		}
	}

	public String getParentSourceId(){
		return bean.getParentSourceId();
	}

	@Override
	public void updateDB(AbstractConnection c) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public String getName() {
		return bean.getName();
	}

}
