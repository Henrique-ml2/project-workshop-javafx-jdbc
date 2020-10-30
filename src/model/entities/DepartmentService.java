// Classe de serviço responsável por fornecer serviços/operações relacionadas a Department
// Fornecer dados para o Controller trabalhar

// Padrão MVC - está no campo do Model, juntamente com a Entitie Department

package model.entities;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
	
	public List<Department> findAll() {
		
		// MOCK - Mocar os dados - dados de mentirinha e não do banco de dados
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Books"));
		list.add(new Department(2, "Computers"));
		list.add(new Department(3, "Eletronics"));
		return list;
	}

}
