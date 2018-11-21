package eu.city4age.dashboard.api.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.city4age.dashboard.api.ApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class VmvFilteringRepositoryTest {
	
	@Autowired
	private VmvFilteringRepository vmvFilteringRepository;
	
	@Test
	public void findCountOfVmvFilteringTest() {
		
		int result = vmvFilteringRepository.findCountOfVmvFiltering();
		
		System.out.println("result: " + result);
		
	}

}