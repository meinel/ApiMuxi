package com.muxi.apirestmuxi.controller;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.muxi.apirestmuxi.enumerado.EnumJson;
import com.muxi.apirestmuxi.model.Message;
import com.muxi.apirestmuxi.model.Terminal;
import com.muxi.apirestmuxi.service.TerminalService;
import com.muxi.apirestmuxi.util.Util;


@RestController // Responsavel para determinar que meu controller trabalha com protocolo HTTP (RestFull)
@RequestMapping(value = "/v1") // Responsavel por mapear, rotas da aplicacao .
public class TerminalController {

	Message response = null;

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
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<Message> salvar(@RequestBody Terminal terminal) {  
		try {
			Util util = new Util();
			Gson gson = new Gson();
			

			String terminaljson = gson.toJson(terminal);
			boolean isvalid = util.validateSchema(terminaljson, EnumJson.SCHEMA.getSchema());
			if(terminal.getLogic() == 0  ) {
				messageStatus("Campo Logic é  obrigatório", HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);

			}
			else if (terminal.getSerial() =="") {
				messageStatus("Campo Serial é obrigatório", HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);

			}
			else if (terminal.getModel() =="") {
				messageStatus("Campo Model é obrigatório", HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
				
			}
			else if (terminal.getVersion() =="") {
				messageStatus("Campo Version é obrigatório", HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
							}

			if(isvalid) {
				service.salvar(terminal);
			}
			else {
				messageStatus("Json passado não é valido", HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.badRequest().body(response);
				
			}

		} catch (Exception e) {
			messageStatus("Ocorreu um erro ao salvar o registro", HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.badRequest().body(response);
		}

		return null;

	}


	private void messageStatus(String msg, int status) {
		Message response = new Message();
		response.setMensagem(msg);
		response.setStatusCode(status);
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
