package webTest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import app.service.RoadService;





public class SpringTest {

	
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("Beans.xml");
		//
		System.out.println("test");
		// ������newһ��RoadServiceʵ��������ͨ��spring������ȡBean��ʵ����
		RoadService rs = ctx.getBean("roadService",RoadService.class);
		
	    System.out.println(rs.getRoadName());
	}

}
