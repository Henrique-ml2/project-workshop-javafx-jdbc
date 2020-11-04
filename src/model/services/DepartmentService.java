package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	// Agora, irá retornar os Departments do banco de dados
	public List<Department> findAll() {
		return dao.findAll();
	}

}
