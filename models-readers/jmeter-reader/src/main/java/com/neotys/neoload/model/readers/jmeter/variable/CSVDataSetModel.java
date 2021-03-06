package com.neotys.neoload.model.readers.jmeter.variable;

import com.neotys.neoload.model.readers.jmeter.EventListenerUtils;
import com.neotys.neoload.model.readers.jmeter.variable.ImmutableCSVDataSetModel.Builder;
import com.neotys.neoload.model.v3.project.variable.Variable.OutOfValue;
import com.neotys.neoload.model.v3.project.variable.Variable.Scope;
import org.immutables.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Value.Immutable
@Value.Style(validationMethod = Value.Style.ValidationMethod.NONE)
abstract class CSVDataSetModel {

    //Attributs
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVDataSetModel.class);

    abstract boolean getRecycle();

    abstract boolean getStopThread();

    abstract boolean getIsFirstLineColumnNames();

    abstract String getShareMode();

    abstract String getPath();

    abstract String getDelimiter();

    abstract String getFileName();

    abstract String getName();

    //Methods
    static Builder builder() {
        return ImmutableCSVDataSetModel.builder();
    }

    OutOfValue computeOutOfValue() {
        if (getStopThread() && !getRecycle()) {
            return OutOfValue.STOP;
        } else {
            return OutOfValue.CYCLE;
        }
    }

    Optional<Scope> computeScope() {
        if ("shareMode.group".equals(getShareMode())) {
            LOGGER.warn("This Parameter can't be converted at 100%");
            EventListenerUtils.readSupportedParameterWithWarn("FileVariable", "Scope", "ShareMode", "Can't be 100% converted");
            return of(Scope.GLOBAL);
        } else if ("shareMode.all".equals(getShareMode())) {
            return of(Scope.GLOBAL);
        } else if ("shareMode.thread".equals(getShareMode())) {
            return of(Scope.LOCAL);
        } else {
            LOGGER.error("Share mode parameter not supported");
            EventListenerUtils.readUnsupportedParameter("CSVDataSet", "String ", "ShareMode Parameter");
            return empty();
        }
    }


}
