package abmi.bis.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import abmi.bis.batch.dao.ConnectionDao;

@Service("dBService")
public class DBServiceImpl implements DBService {

	@Autowired
	private ConnectionDao conDao;
	
	@Override
	public Boolean isConnected() {
		return conDao.isConnected();
	}

}
