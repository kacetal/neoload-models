package com.neotys.neoload.model.readers.jmeter.step.thread;

import com.neotys.neoload.model.listener.TestEventListener;
import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.v3.project.population.Population;
import com.neotys.neoload.model.v3.project.population.UserPathPolicy;
import com.neotys.neoload.model.v3.project.scenario.PopulationPolicy;
import com.neotys.neoload.model.v3.project.userpath.UserPath;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.threads.ThreadGroup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class ConvertThreadGroupResultTest {

    @Before
    public void before()   {
        TestEventListener spy = spy(new TestEventListener());
        EventListenerUtils.setEventListener(spy);
    }

    @Test
    public void testGetters(){
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("Test Thread");
        threadGroup.setScheduler(false);
        threadGroup.setDelay(1);
        threadGroup.setRampUp(25);
        threadGroup.setNumThreads(10);
        LoopController loopController = new LoopController();
        loopController.setLoops(0);
        threadGroup.setSamplerController(loopController);

        UserPath.Builder userPath = UserPath
                .builder()
                .name("user")
                .description("path");

        UserPathPolicy.Builder userPathPolicy = UserPathPolicy
                .builder()
                .name("userpolicy")
                .description("test");


        Population.Builder population = Population.builder()
                .name("population")
                .addUserPaths(userPathPolicy.build());


        PopulationPolicy populationPolicy = PopulationPolicyConverter.convert(threadGroup);


        ConvertThreadGroupResult convertThreadGroupResult = new ConvertThreadGroupResult(userPath.build(),population.build(),populationPolicy,null);

        assertEquals(convertThreadGroupResult.getPopulationPolicy(),populationPolicy);
        assertEquals(convertThreadGroupResult.getPopulation(),population.build());
        assertEquals(convertThreadGroupResult.getUserPath(),userPath.build());

    }

}