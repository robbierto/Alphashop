package com.xantrix.webapp.dao;

import java.util.List;

import com.xantrix.webapp.entities.Profili;

public interface ProfiliDao
{
	Profili SelById(long id);
	
	List<Profili> SelByIdFidelity(String IdFidelity);
	
	void Salva(Profili profilo);

	void Elimina(Profili profilo);
	
	void Aggiorna(Profili profilo);
}
