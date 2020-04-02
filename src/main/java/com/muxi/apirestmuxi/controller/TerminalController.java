package com.muxi.apirestmuxi.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.muxi.apirestmuxi.enumerado.EnumJson;
import com.muxi.apirestmuxi.model.Terminal;
import com.muxi.apirestmuxi.service.TerminalService;
import com.muxi.apirestmuxi.util.Util;


@RestController // Responsavel para determinar que meu controller trabalha com protocolo HTTP (RestFull)
@RequestMapping(value = "/v1") // Responsavel por mapear, rotas da aplicacao .
public class TerminalController {

	
	@Autowired // ingetando servico
	TerminalService service;

	@PostMapping(path = "init", consumes = {"text/html; charset=utf-8"}, produces = {MediaType.APPLICATION_JSON_VALUE})     
	public Terminal Conversor (@RequestBody String param){

		String [] dados = param.split(";");

		Terminal terminal = new Terminal();

		terminal.setLogic(Integer.parseInt(dados[0]));
		terminal.setSerial(dados[1]);
		terminal.setModel(dados[2]);
		terminal.setSam(Integer.parseInt(dados[3]));
		terminal.setPtid(dados[4]);
		terminal.setPlat(Integer.parseInt(dados[5]));
		terminal.setVersion(dados[6]);
		terminal.setMxr(Integer.parseInt(dados[7]));
		terminal.setMfx(Integer.parseInt(dados[8]));
		terminal.setVerfm(dados[9]);

		return terminal;


	}
	

	@PostMapping(path = "terminal", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})  
	//@ResponseStatus(HttpStatus.CREATED)
	public String salvar(@RequestBody Terminal terminal) {  
		try {
			Util util = new Util();
			Gson gson = new Gson();

			String terminaljson = gson.toJson(terminal);
			boolean isvalid = util.validateSchema(terminaljson, EnumJson.SCHEMA.getSchema());
			if(terminal.getLogic() == 0  ) {
				return ("Campo Logic é  obrigatório");
			}
			else if (terminal.getSerial() =="") {
				return("Campo Serial é obrigatório");
			}
			else if (terminal.getModel() =="") {
				return("Campo Model é obrigatório");
			}
			else if (terminal.getVersion() =="") {
				return("Campo Version é obrigatório");
			}

			if(isvalid) {
				service.salvar(terminal);
			}
			else {
				return ("Json passado não é valido");
			}

		} catch (Exception e) {
			return ("Ocorreu um erro ao salvar o registro");
		}
		
		return null;

	}

	@GetMapping(path = "listar", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE}) 
	public  List<Terminal> findAll() {
		try {
			return service.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}

	@GetMapping(path = "/terminal/id/{id}")
	public Terminal findId(@PathVariable(name = "id", required = true) Integer logic) {
		try {
			return service.findId(logic);
		} catch (Exception e) {
			return null;
		}

	}

	@DeleteMapping(path = "/terminal/delete/{id}")
	public boolean delete(@PathVariable(name = "id", required = true) Integer logic) {
		try {
			service.delete(logic);
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	@PutMapping(path = "terminal/update/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Terminal update(@PathVariable(name = "id", required = true) Integer logic, @RequestBody Terminal terminal) {
		try {
			return service.update(logic, terminal);
		} catch (Exception e) {
			return null;
		}

	}

}
