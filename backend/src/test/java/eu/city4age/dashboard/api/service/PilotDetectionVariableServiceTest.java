package eu.city4age.dashboard.api.service;

import java.math.BigDecimal;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import eu.city4age.dashboard.api.ApplicationTest;
import eu.city4age.dashboard.api.config.ObjectMapperFactory;
import eu.city4age.dashboard.api.exceptions.JsonMappingExceptionUD;
import eu.city4age.dashboard.api.jpa.DetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.DetectionVariableTypeRepository;
import eu.city4age.dashboard.api.jpa.PilotDetectionVariableRepository;
import eu.city4age.dashboard.api.jpa.PilotRepository;
import eu.city4age.dashboard.api.jpa.TypicalPeriodRepository;
import eu.city4age.dashboard.api.jpa.UserInRoleRepository;
import eu.city4age.dashboard.api.jpa.UserInSystemRepository;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariable;
import eu.city4age.dashboard.api.pojo.domain.DetectionVariableType;
import eu.city4age.dashboard.api.pojo.domain.Pilot;
import eu.city4age.dashboard.api.pojo.domain.Pilot.PilotCode;
import eu.city4age.dashboard.api.pojo.domain.PilotDetectionVariable;
import eu.city4age.dashboard.api.pojo.json.ConfigureDailyMeasuresDeserializer;
import eu.city4age.dashboard.api.pojo.domain.TypicalPeriod;
import eu.city4age.dashboard.api.pojo.domain.UserInRole;
import eu.city4age.dashboard.api.pojo.domain.UserInSystem;
import eu.city4age.dashboard.api.rest.PilotDetectionVariableService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class)
public class PilotDetectionVariableServiceTest {
	
	static protected Logger logger = LogManager.getLogger(PilotDetectionVariableServiceTest.class);
	
	@Autowired
	DetectionVariableRepository detectionVariableRepository;
	
	@Mock
	DetectionVariableRepository detectionVariableRepositoryMock;
	
	@Autowired
	DetectionVariableTypeRepository detectionVariableTypeRepository;
	
	@Mock
	DetectionVariableTypeRepository detectionVariableTypeRepositoryMock;
	
	@Autowired
	PilotRepository pilotRepository;
	
	@Mock
	PilotRepository pilotRepositoryMock;
	
	@Autowired
	UserInSystemRepository userInSystemRepository;
	
	@Mock
	UserInSystemRepository userInSystemRepositoryMock;
	
	@Autowired
	UserInRoleRepository userInRoleRepository;
	
	@Mock
	UserInRoleRepository userInRoleRepositoryMock;
	
	@Autowired
	TypicalPeriodRepository typicalPeriodRepository;
	
	@Autowired
	PilotDetectionVariableRepository pilotDetectionVariableRepository;
	
	@Mock
	PilotDetectionVariableRepository pilotDetectionVariableRepositoryMock;
	
	@Before
    public void setUp() {
		MockitoAnnotations.initMocks(this);
    }
	@Spy
	@InjectMocks
	PilotDetectionVariableService pilotDetectionVariableService;
	
	private static final ObjectMapper objectMapper = ObjectMapperFactory.create();
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateConfigurationService () throws Exception {
		
		/*
		 * test scenario no1: configuration which has 2 geriatric factor groups; (num of rows expected: 2)
		 * one of those groups have two geriatric factors, other has one; (num of rows expected: 3)
		 * geriatric factors from first group: one has 2 geriatric subfactors, other has 1 (num of rows expected: 3)
		 * geriatric factor from second group has 1 geriatric subfactor (num of rows expected: 1)
		 * each subfactors has 2 measures (one monthly one daily) (those 2 measures are same for every GES) (num of rows expected: 8)
		 * daily measure has 2 nuis (num of rows expected: 8); 
		 * number of rows expected in db after service = 25
		 */
		String input1 = "{\"configurations\": [{\"name\": \"ovl_var\", \"level\": 0, \"pilotCode\": \"ATH\", \"username\": \"username\", \"password\": \"password\", \"groups\": [{\"name\": \"gfg_var1\", \"level\": 1, \"weight\": 0.6, \"formula\": \"\", \"factors\": [{\"name\": \"gef_var1\", \"level\": 2, \"weight\": 0.3, \"formula\": \"\", \"subFactors\": [{\"name\": \"ges_var1\", \"level\": 3, \"weight\": 0.35, \"formula\": \"\", \"measures\": [{\"name\": \"mea_var1\", \"level\": 4, \"weight\": 0.2}, {\"name\": \"mea_var2\", \"level\": 4, \"weight\": 0.8, \"nuis\": [{\"name\": \"nui_var1\", \"formula\": \"nui1\", \"weight\": 0.5}, {\"name\": \"nui_var2\", \"formula\": \"nui2\", \"weight\": 0.5}]}]},{\"name\": \"ges_var2\", \"level\": 3, \"weight\": 0.65, \"formula\": \"\", \"measures\": [{\"name\": \"mea_var1\", \"level\": 4, \"weight\": 0.3}, {\"name\": \"mea_var2\", \"level\": 4, \"weight\": 0.7, \"nuis\": [{\"name\": \"nui_var1\", \"formula\": \"nui1\", \"weight\": 0.5}, {\"name\": \"nui_var2\", \"formula\": \"nui2\", \"weight\": 0.5}]}]}]}, {\"name\": \"gef_var2\", \"level\": 2, \"weight\": 0.7, \"formula\":\"\", \"subFactors\": [{\"name\": \"ges_var3\", \"level\": 3, \"weight\": 1, \"formula\": \"\", \"measures\": [{\"name\": \"mea_var1\", \"level\": 4, \"weight\": 0.1}, {\"name\": \"mea_var2\", \"level\": 4, \"weight\": 0.9, \"nuis\": [{\"name\": \"nui_var1\", \"formula\": \"nui1\", \"weight\": 0.5}, {\"name\": \"nui_var2\", \"formula\": \"nui2\", \"weight\": 0.5}]}]}]}]}, {\"name\": \"gfg_var2\", \"level\": 1, \"weight\": 0.4, \"formula\": \"\", \"factors\": [{\"name\": \"gef_var3\", \"level\": 2, \"weight\": 1, \"formula\": \"\", \"subFactors\": [{\"name\": \"ges_var4\", \"level\": 3, \"weight\": 1, \"formula\": \"\", \"measures\": [{\"name\": \"mea_var1\", \"level\": 4, \"weight\": 0.25}, {\"name\": \"mea_var2\", \"level\": 4, \"weight\": 0.75, \"nuis\": [{\"name\": \"nui_var1\", \"formula\": \"nui1\", \"weight\": 0.5}, {\"name\": \"nui_var2\", \"formula\": \"nui2\", \"weight\": 0.5}]}]}]}]}]}]}"
				;
		logger.info("input: " + input1);
		Pilot pil = new Pilot ();
		pil.setPilotCode(PilotCode.ATH);
		pil = pilotRepository.save(pil);
		
		Mockito.when(pilotRepositoryMock.findByPilotCode(pil.getPilotCode())).thenReturn(pilotRepository.findByPilotCode(pil.getPilotCode()));
		
		UserInSystem uis = new UserInSystem ();
		uis.setUsername("username");
		uis.setPassword("password");
		uis = userInSystemRepository.save(uis);
		
		UserInRole uir = new UserInRole ();
		uir.setPilot(pil);
		uir.setPilotCode(PilotCode.ATH);
		uir.setUserInSystem(uis);
		uir = userInRoleRepository.save(uir);
		
		//logger.info("uis.id: " + uis.getId() + " ui");
		
		UserInRole resultUIR = userInRoleRepository.findBySystemUsernameAndPassword(uis.getUsername(), uis.getPassword());
		//logger.info("resultUIR.id: " + resultUIR.getId());
		Mockito.when(userInRoleRepositoryMock.findBySystemUsernameAndPassword(uis.getUsername(), uis.getPassword())).thenReturn(userInRoleRepository.findBySystemUsernameAndPassword(uis.getUsername(), uis.getPassword()));
		
		TypicalPeriod typical_period_day = new TypicalPeriod ();
		typical_period_day.setTypicalPeriod(eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.DAY.getDbName());
		typical_period_day = typicalPeriodRepository.save(typical_period_day);
		
		TypicalPeriod typical_period_month = new TypicalPeriod ();
		typical_period_month.setTypicalPeriod(eu.city4age.dashboard.api.pojo.enu.TypicalPeriod.MONTH.getDbName());
		typical_period_month = typicalPeriodRepository.save(typical_period_month);
		
		DetectionVariableType ovl_type = DetectionVariableType.OVL;
		DetectionVariableType gfg_type = DetectionVariableType.GFG;
		DetectionVariableType gef_type = DetectionVariableType.GEF;
		DetectionVariableType ges_type = DetectionVariableType.GES;
		DetectionVariableType mea_type = DetectionVariableType.MEA;
		DetectionVariableType nui_type = DetectionVariableType.NUI;
		
		detectionVariableTypeRepository.save(ovl_type); detectionVariableTypeRepository.save(gfg_type); detectionVariableTypeRepository.save(gef_type); detectionVariableTypeRepository.save(ges_type); detectionVariableTypeRepository.save(mea_type); detectionVariableTypeRepository.save(nui_type);
		
		DetectionVariable ovl = new DetectionVariable ();
		ovl.setDetectionVariableName("ovl_var");
		ovl.setDetectionVariableType(ovl_type);
		ovl = detectionVariableRepository.save(ovl);
		
		DetectionVariable gfg1 = new DetectionVariable ();
		gfg1.setDetectionVariableName("gfg_var1");
		gfg1.setDetectionVariableType(gfg_type);
		gfg1 = detectionVariableRepository.save(gfg1);
		
		DetectionVariable gfg2 = new DetectionVariable ();
		gfg2.setDetectionVariableName("gfg_var2");
		gfg2.setDetectionVariableType(gfg_type);
		gfg2 = detectionVariableRepository.save(gfg2);
		
		DetectionVariable gef1 = new DetectionVariable ();
		gef1.setDetectionVariableName("gef_var1");
		gef1.setDetectionVariableType(gef_type);
		gef1 = detectionVariableRepository.save(gef1);
		
		DetectionVariable gef2 = new DetectionVariable ();
		gef2.setDetectionVariableName("gef_var2");
		gef2.setDetectionVariableType(gef_type);
		gef2 = detectionVariableRepository.save(gef2);
		
		DetectionVariable gef3 = new DetectionVariable ();
		gef3.setDetectionVariableName("gef_var3");
		gef3.setDetectionVariableType(gef_type);
		gef3 = detectionVariableRepository.save(gef3);
		
		DetectionVariable ges1 = new DetectionVariable ();
		ges1.setDetectionVariableName("ges_var1");
		ges1.setDetectionVariableType(ges_type);
		ges1 = detectionVariableRepository.save(ges1);
		
		DetectionVariable ges2 = new DetectionVariable ();
		ges2.setDetectionVariableName("ges_var2");
		ges2.setDetectionVariableType(ges_type);
		ges2 = detectionVariableRepository.save(ges2);
		
		DetectionVariable ges3 = new DetectionVariable ();
		ges3.setDetectionVariableName("ges_var3");
		ges3.setDetectionVariableType(ges_type);
		ges3 = detectionVariableRepository.save(ges3);
		
		DetectionVariable ges4 = new DetectionVariable ();
		ges4.setDetectionVariableName("ges_var4");
		ges4.setDetectionVariableType(ges_type);
		ges4 = detectionVariableRepository.save(ges4);
		
		DetectionVariable mea1 = new DetectionVariable ();
		mea1.setDetectionVariableName("mea_var1");
		mea1.setDetectionVariableType(mea_type);
		mea1.setDefaultTypicalPeriod(typical_period_month.getTypicalPeriod());
		mea1 = detectionVariableRepository.save(mea1);
		
		DetectionVariable mea2 = new DetectionVariable ();
		mea2.setDetectionVariableName("mea_var2");
		mea2.setDetectionVariableType(mea_type);
		mea2.setDefaultTypicalPeriod(typical_period_day.getTypicalPeriod());
		mea2 = detectionVariableRepository.save(mea2);
		
		DetectionVariable nui1 = new DetectionVariable ();
		nui1.setDetectionVariableName("nui_var1");
		nui1.setDetectionVariableType(nui_type);
		nui1 = detectionVariableRepository.save(nui1);
		
		DetectionVariable nui2 = new DetectionVariable ();
		nui2.setDetectionVariableName("nui_var2");
		nui2.setDetectionVariableType(nui_type);
		nui2 = detectionVariableRepository.save(nui2);
		
		PilotDetectionVariable mockPDV = new PilotDetectionVariable ();
		mockPDV.setDerivationWeight(new BigDecimal (0.5));
		mockPDV.setDerivedDetectionVariable(nui1);
		mockPDV.setDetectionVariable(mea2);
		mockPDV.setFormula(nui1.getDetectionVariableName());
		mockPDV.setPilotCode(pil.getPilotCode());
		
		Mockito.when(pilotDetectionVariableRepositoryMock.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pil.getPilotCode(), mea2.getId(), nui1.getId())).thenReturn(mockPDV);
		Mockito.when(pilotDetectionVariableRepositoryMock.findOneByPilotCodeAndDetectionVariableIdAndDerivedDetectionVariableId(pil.getPilotCode(), mea2.getId(), nui2.getId())).thenReturn(mockPDV);
		Mockito.when(pilotDetectionVariableRepositoryMock.count()).thenReturn(new Long(25));
		
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(ovl.getDetectionVariableName(), ovl.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(ovl.getDetectionVariableName(),  ovl.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(gfg1.getDetectionVariableName(), gfg1.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(gfg1.getDetectionVariableName(),  gfg1.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(gfg2.getDetectionVariableName(), gfg2.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(gfg2.getDetectionVariableName(),  gfg2.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(gef1.getDetectionVariableName(), gef1.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(gef1.getDetectionVariableName(),  gef1.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(gef2.getDetectionVariableName(), gef2.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(gef2.getDetectionVariableName(),  gef2.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(gef3.getDetectionVariableName(), gef3.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(gef3.getDetectionVariableName(),  gef3.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(ges1.getDetectionVariableName(), ges1.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(ges1.getDetectionVariableName(),  ges1.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(ges2.getDetectionVariableName(), ges2.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(ges2.getDetectionVariableName(),  ges2.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(ges3.getDetectionVariableName(), ges3.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(ges3.getDetectionVariableName(),  ges3.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(ges4.getDetectionVariableName(), ges4.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(ges4.getDetectionVariableName(),  ges4.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(mea1.getDetectionVariableName(), mea1.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(mea1.getDetectionVariableName(),  mea1.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(mea2.getDetectionVariableName(), mea2.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(mea2.getDetectionVariableName(),  mea2.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(nui1.getDetectionVariableName(), nui1.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(nui1.getDetectionVariableName(),  nui1.getDetectionVariableType()));
		Mockito.when(detectionVariableRepositoryMock.findByDetectionVariableNameAndDetectionVariableType(nui2.getDetectionVariableName(), nui2.getDetectionVariableType())).thenReturn(detectionVariableRepository.findByDetectionVariableNameAndDetectionVariableType(nui2.getDetectionVariableName(),  nui2.getDetectionVariableType()));
		
		Response response = pilotDetectionVariableService.updateConfigurationService(input1);
		
		String responseString = (String) response.getEntity();
						
		Assert.assertTrue(responseString.contains("Number of rows for Pilot: ATH\n\tafter configuration update done on:"));
		Assert.assertTrue(responseString.contains("Number of inserted rows in configuration is: 25\n\tNumber of updated rows in configuration is: 0\n\tNumber of inactive rows in configuration is: 0\n\tNumber of rows in DB after update for this pilot is: 0\n\tNumber of rows in db after configuration update is: 25"));
		
		logger.info("response.entity: " + response.getEntity());	
		
	}

}
