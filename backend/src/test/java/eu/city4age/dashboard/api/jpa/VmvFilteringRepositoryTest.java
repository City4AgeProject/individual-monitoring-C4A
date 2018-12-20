package eu.city4age.dashboard.api.jpa;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.pojo.domain.VmvFiltering;

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

	@Test
	public void findFilterTypeByVmvIdTest() {
		
		List<Long> vmvId = new ArrayList<Long>();
		vmvId.add(98L);
		
		List<VmvFiltering> result = vmvFilteringRepository.findFilterTypeByVmvId(vmvId);
		
		System.out.println("filter type: " + result.get(0).getFilterType());
		System.out.println("result: " + result.size());
		
	}

}