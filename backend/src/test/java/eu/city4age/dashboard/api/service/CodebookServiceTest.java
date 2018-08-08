package eu.city4age.dashboard.api.service;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.jpa.RiskStatusRepository;
import eu.city4age.dashboard.api.jpa.RoleRepository;
import eu.city4age.dashboard.api.pojo.domain.CrProfile;
import eu.city4age.dashboard.api.pojo.domain.RiskStatus;
import eu.city4age.dashboard.api.pojo.domain.Role;
import eu.city4age.dashboard.api.rest.CodebookEndpoint;
import eu.city4age.dashboard.api.rest.UserEndpoint;

/*
 * author: marina.andric
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class CodebookServiceTest {

	static protected Logger logger = LogManager.getLogger(CodebookEndpoint.class);

	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Autowired
	private RiskStatusRepository riskStatusRepository;

	@Mock
	private RiskStatusRepository riskStatusRepositoryMock;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Mock
	private RoleRepository roleRepositoryMock;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Mock
	private SessionFactory sessionFactoryMock;
	
	@Spy
	@InjectMocks
	CodebookEndpoint codebookService = new CodebookEndpoint();

    @Before
    public void setUp() {
     MockitoAnnotations.initMocks(this);
    }
	
	@Test
	@Transactional
	@Rollback(true)
	public void getAllRiskStatusTest() throws Exception {
		
		RiskStatus rs = new RiskStatus();
		rs.setRiskStatus('W');
		riskStatusRepository.save(rs);
		
		List<RiskStatus> riskStatus = riskStatusRepository.findAll();
		Mockito.when(riskStatusRepositoryMock.findAll()).thenReturn(riskStatus);
		
		Response response = codebookService.getAllRiskStatus();
		List<RiskStatus> result = objectMapper.readerFor(List.class).readValue(response.getEntity().toString());
		Assert.assertEquals(result.size(), 1);
		Assert.assertEquals("[{riskStatus=W}]", result.toString());
	}
	@Test
	@Transactional
	@Rollback(true)
	public void getAllRolesForStakeholderAbbrTest() throws IOException {
		
		
		Role role = new Role();
		role.setRoleAbbreviation("cg");
		role.setStakeholderAbbreviation("cgs");		
		roleRepository.save(role);
		
		List<Role> roles = roleRepository.findByStakeholderAbbreviation(role.getStakeholderAbbreviation());
		Mockito.when(roleRepositoryMock.findByStakeholderAbbreviation(role.getStakeholderAbbreviation())).thenReturn(roles);
		
		Response response = codebookService.getAllRolesForStakeholderAbbr(role.getStakeholderAbbreviation());
		List<Role> result = objectMapper.readerFor(List.class).readValue(response.getEntity().toString());
		Assert.assertEquals(result.size(), 1);
		Assert.assertEquals("[{id=" + role.getId() + ", roleAbbreviation=cg, stakeholderAbbreviation=cgs}]", result.toString());
	}
	
}
