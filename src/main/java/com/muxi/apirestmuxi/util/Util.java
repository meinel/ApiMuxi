package com.muxi.apirestmuxi.util;



import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.report.ProcessingReport;
import com.github.fge.jsonschema.util.JsonLoader;

public class Util {
	
	public boolean validateSchema(String Json, String Schema) {
		
		try {
		JsonNode schemaNode = JsonLoader.fromString(Schema);
		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		JsonSchema schema = factory.getJsonSchema(schemaNode);
		JsonNode dataNode   = JsonLoader.fromString(Json);
		ProcessingReport report = schema.validate(dataNode);
				return report.isSuccess();
		} catch (ProcessingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return false;
	}

}
