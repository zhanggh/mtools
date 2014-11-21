package com.ztools.db;

import com.mtools.core.plugin.db.CoreDao;

public class DBUtilTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		 CoreDao dao = null;
		 String sequenceName="SEQ_ROLE";
		 String value = (String) dao.getSimpleObj("select _nextval(?) ", String.class, sequenceName);
	}

}
