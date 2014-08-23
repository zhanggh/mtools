package com.tools.test.aspect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tools.test.commons.service.CommonService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ztools.beans.xml"})
public class AspectTest extends AbstractJUnit4SpringContextTests {
 


	@Autowired
	CommonService comSev;
	
	@Test
    public void aspectTest() {
		comSev.doService();
	}
}
