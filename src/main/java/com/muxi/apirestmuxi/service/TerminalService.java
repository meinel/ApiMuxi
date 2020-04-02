package com.muxi.apirestmuxi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.muxi.apirestmuxi.model.Terminal;

@Service // uma classe gerenciavel
public class  TerminalService  {

	Map<Integer, Terminal> terminalmap = null;

	
	public boolean salvar(Terminal terminal) {
		if(terminalmap == null ) {
			terminalmap = new HashMap <Integer, Terminal>();	
		}
		terminalmap.put(terminal.getLogic(),terminal);
		return true;
	}

	//Retorna uma lista de terminais
	public List<Terminal> findAll(){
		List<Terminal> terminais = new ArrayList<Terminal>(terminalmap.values());
		return terminais;
	}

	// Retorna um terminal
	public Terminal findId(Integer logic) {
		Terminal terminal = terminalmap.get(logic);
		return terminal;
	}

	public boolean delete(Integer logic) {
		terminalmap.remove(logic);
		return true;
	}

	public Terminal update(Integer logic, Terminal terminal) {
		return terminalmap.replace(logic, terminal);
	}
}
