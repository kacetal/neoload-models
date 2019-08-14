package com.neotys.neoload.model.readers.jmeter.step.controller;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.step.StepConverters;
import com.neotys.neoload.model.v3.project.userpath.*;
import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.SwitchController;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SwitchControllerConverter implements BiFunction<SwitchController, HashTree, List<Step>> {

    //Attributs
    private final StepConverters converter;
    private static final Logger LOGGER = LoggerFactory.getLogger(SwitchControllerConverter.class);

    //Constructor
    public SwitchControllerConverter(final StepConverters converters) {
        this.converter = converters;
    }

    //Methods
    @Override
    public List<Step> apply(SwitchController switchController, HashTree hashTree) {
        final List<Step> containerList = new ArrayList<>();
        containerList.add(convertSwitch(switchController,hashTree));
        LOGGER.info("SwitchController correctly converted");
        EventListenerUtils.readSupportedFunction("SwitchController", "Switch");
        return containerList;
    }
    private ImmutableSwitch convertSwitch(SwitchController switchController, HashTree hashTree) {
        Switch.Builder switchBuilder = Switch.builder()
                .name(switchController.getName())
                .description(switchController.getComment())
                .value(switchController.getSelection());
        for(Object o : hashTree.get(switchController).list()) {
            if (o instanceof Controller){ //To manage the controller like case
                HashTree subtree = new HashTree();
                Controller container = (Controller) o;
                subtree.add(o);subtree.get(o).add(hashTree.get(switchController).get(container));

                if("default".equalsIgnoreCase(container.getName().toLowerCase())){
                    switchBuilder.getDefault(Container.builder()
                            .addAllSteps(converter.convertStep(subtree))
                            .build());
                }else{
                    switchBuilder.addCases(Case.builder()
                            .isBreak(true)
                            .addAllSteps(converter.convertStep(subtree))
                            .value(container.getName())
                            .build());
                }
            } else{
                LOGGER.error("In the first level of a SwitchController node, you can only put Containers");
                EventListenerUtils.readUnsupportedAction("Element not tolerate at the first level of switch node");
            }
        }
        LOGGER.warn("We don't manage if you want to use the switch using the page number and not the page name");
        return switchBuilder.build();
    }
}
