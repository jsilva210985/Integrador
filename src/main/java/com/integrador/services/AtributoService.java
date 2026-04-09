package com.integrador.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.integrador.models.Atributo;
import com.integrador.models.Usuario;
import com.integrador.repositories.AtributoRepository;
import com.integrador.repositories.UsuarioRepository;
@Service
public class AtributoService {

	@Autowired AtributoRepository atributoRepository;
	@Autowired UsuarioRepository usuarioRepository;
	public List<Atributo> getByTipo(String tipo){
		return atributoRepository.findByTipo(tipo);
	}
	public Map<String,String> getByTipoInMap(String tipo){
		Map<String,String> values = new HashMap<String,String>();
		List<Atributo> list = getByTipo(tipo);
		for (Atributo a : list) {
			values.put(a.getLlave().trim(), a.getValor().trim());
		}
		return values;
	}
	public Usuario findUsuario(String usuario) {
		return usuarioRepository.findByUsuario(usuario);
	}
}
