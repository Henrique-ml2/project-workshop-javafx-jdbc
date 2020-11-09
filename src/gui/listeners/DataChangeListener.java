/* 
	// Interface que permite um objeto "escutar" um evento de outro objeto
	// Esse outro objeto quando alterar os dados ele irá emitir o evento
	// E o objeto que receber o Listener/Observer vai fazer alguma ação quando esse evento for recebido
	
	
	
	
	// ------  ATUALIZAR A LISTA <TableView> DE Departments QUANDO SOFRER ALTERAÇÃO  ------  //

	Padrão de Projeto Observer (ideia de evento) 
	- programação de alto nível
	- forma de comunicar objetos (classes) de maneira altamente desacoplacada o objeto (classe) SUBJECT não conhece o objeto (classe) OBSERVER 
	- muito utilizado em qualquer aplicação que lida com eventos, seja eventos de tela, servidor, etc


	Esta etapa é para a lista ser atualizada quando tiver alguma alteração nela
	Ou seja, quando for salvo OU atualizado algum Department
	
	Para fazer isso é preciso:

	1) A View de DepartmentFormController (SUBJECT) salvar OU atualizar a <TableView> de Departments
	2) Quando a View de DepartmentFormController emitir um evento a View DepartmentListController (OBSERVER) receberá esse evento
	3) A listinha de Departments <TableView> tableViewDepartment atualizará automaticamente

	- (SUBJECT): classe que emite um evento
	- (OBSERVER): classe que recebe um evento
*/
package gui.listeners;

public interface DataChangeListener {

	void onDataChanged();
}
