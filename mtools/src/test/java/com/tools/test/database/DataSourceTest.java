package com.tools.test.database;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

import com.mtools.core.plugin.auth.MenuPlugin;
import com.mtools.core.plugin.db.CoreDao;
import com.mtools.core.plugin.entity.MenuInfo;
import com.mtools.core.plugin.entity.PageInfo;
import com.mtools.core.plugin.helper.AIPGException;
import com.mtools.core.plugin.helper.XStreamIg;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ztools.testbeans.xml"})
public class DataSourceTest extends AbstractJUnit4SpringContextTests {
 
	public  Log log= LogFactory.getLog(this.getClass());
	@Resource
	CoreDao dao;

	@Resource
	MenuPlugin menuPlugin;
	@Test
    public void queryTest() {
		dao.getSeqMys("test");
	}
     
}
