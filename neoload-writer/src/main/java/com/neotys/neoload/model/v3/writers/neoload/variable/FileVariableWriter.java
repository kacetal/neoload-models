package com.neotys.neoload.model.v3.writers.neoload.variable;

import com.google.common.collect.ImmutableList;
import com.neotys.neoload.model.v3.project.variable.FileVariable;
import com.neotys.neoload.model.v3.util.RegExpUtils;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileVariableWriter extends VariableWriter {

	public static final String VARIABLE_DIRECTORY = "variables"; 
	
	public static final String DATA_FILE_BASE_NAME = "Variable";
	public static final String DATA_FILE_BASE_EXT = ".csv";
	
	public static final String XML_TAG_NAME = "variable-file";
    public static final String XML_ATTR_FILENAME = "filename";
    public static final String XML_ATTR_OFFSET = "offset";

    public static final String XML_ATTR_USE_FIRST_LINE = "useFirstLine";
    public static final String XML_ATTR_DELIMITER = "delimiters";
    
    public static final String XML_TAG_COLOMN = "column";
    public static final String XML_COLOMN_ATTR_NAME = "name";
    public static final String XML_COLOMN_ATTR_NUMBER = "number";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FileVariableWriter.class);
    
	public FileVariableWriter(FileVariable variable) {
		super(variable);
	}
	
	
	@Override
	public void writeXML(final Document document, final org.w3c.dom.Element currentElement, final String outputFolder) {
		org.w3c.dom.Element xmlVariable = document.createElement(XML_TAG_NAME);
		super.writeXML(xmlVariable) ;

		FileVariable theFileVariable = (FileVariable) element;
		xmlVariable.setAttribute(XML_ATTR_DELIMITER, theFileVariable.getDelimiter());
		xmlVariable.setAttribute(XML_ATTR_USE_FIRST_LINE, Boolean.toString(theFileVariable.isFirstLineColumnNames()));
		xmlVariable.setAttribute(XML_ATTR_OFFSET, Integer.toString(theFileVariable.getStartFromLine()));
		
		xmlVariable.setAttribute(XML_ATTR_FILENAME, theFileVariable.getPath());

		//generate Column nodes
		List<String> cols = theFileVariable.getColumnNames();
		if(cols.isEmpty() && theFileVariable.isFirstLineColumnNames()) {
			// read the first line and get the columns names
			try {
				cols = getColumnsFromFile(theFileVariable.getPath(), theFileVariable.getDelimiter());
			}catch(IOException e) {
				LOGGER.warn("Cannot read columns in file variable "+theFileVariable.getPath(), e);
			}
		}
		int counter = 0;
		for(String columnName : cols) {
			org.w3c.dom.Element xmlColumn = document.createElement(XML_TAG_COLOMN);
			xmlColumn.setAttribute(XML_COLOMN_ATTR_NAME, columnName);
			xmlColumn.setAttribute(XML_COLOMN_ATTR_NUMBER, Integer.toString(counter++));
			xmlVariable.appendChild(xmlColumn);
		}
		writeDescription(document, xmlVariable);
		
		currentElement.appendChild(xmlVariable);
	}

	static List<String> getColumnsFromFile(String fileName, String columnsDelimiter) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			return getColumsFromFirstLine(stream.findFirst(), columnsDelimiter);
		}
	}

	static List<String> getColumsFromFirstLine(Optional<String> firstLine, String columnsDelimiter) {
        return firstLine.map(s -> Arrays.stream(s.split(RegExpUtils.escape(columnsDelimiter))).map(String::trim).collect(Collectors.toList())).orElseGet(ImmutableList::of);
    }
	
	
	//generate the file
	static String dumpDataInFile(File folder, String variableName, List<String> columnsNames, String delimiter, String [][] data) {
		if(folder == null || !folder.isDirectory()) {
			if (folder == null) {
				LOGGER.error("the output folder does not exists");
			} else {
				LOGGER.error("the folder \"" + folder.getAbsolutePath() + "\" does not exists");
			}
			return null;
		}
		
		boolean notFound = true;
		int numFile = 0;
		File dataFile = new File(folder.getAbsolutePath() + File.separator + VARIABLE_DIRECTORY);
		if (! dataFile.exists()) {
			dataFile.mkdir();
		}
		
		//find file path
		while (notFound) {
			numFile++;
			dataFile = new File(folder.getAbsolutePath() +
					File.separator + VARIABLE_DIRECTORY +
					File.separator + DATA_FILE_BASE_NAME +
					"_" + variableName +
					Integer.toString(numFile) + DATA_FILE_BASE_EXT);
			if(!dataFile.exists()) {
				notFound = false;
			}
		}
		
		try(Writer writer = Files.newBufferedWriter(Paths.get(dataFile.getPath()));
				CSVWriter csvWriter = new CSVWriter(writer,
				delimiter.charAt(0),
				CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER,
				CSVWriter.DEFAULT_LINE_END);
	        ) {
			
			String[] headerRecord = new String[columnsNames.size()];
			columnsNames.toArray(headerRecord);
			
			csvWriter.writeNext(headerRecord);
			Arrays.asList(data).stream().forEach(csvWriter::writeNext);
			
		} catch (IOException e) {
			LOGGER.error("An error occured while writing the parameter File \"" + dataFile.getAbsolutePath() + "\":\n" + e);
		}
		
		return VARIABLE_DIRECTORY + File.separator + dataFile.getName();
	}
	
}
