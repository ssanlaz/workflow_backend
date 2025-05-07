package workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import workflow.entidades.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{

}
