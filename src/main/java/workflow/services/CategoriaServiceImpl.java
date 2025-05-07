package workflow.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import workflow.entidades.Categoria;
import workflow.repository.CategoriaRepository;
@Service
public class CategoriaServiceImpl implements CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public Categoria alta(Categoria categoria) {
		
		try {
            return categoriaRepository.save(categoria);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public Categoria modificar(Categoria categoria) {
		
		try {
            if (categoriaRepository.existsById(categoria.getIdCategoria())) {
                return categoriaRepository.save(categoria);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public int eliminar(int idCategoria) {
		
		try {
            if (categoriaRepository.existsById(idCategoria)) {
                categoriaRepository.deleteById(idCategoria);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

	@Override
	public Categoria buscarUna(int idCategoria) {
		
		return categoriaRepository.findById(idCategoria).orElse(null);
	}

	@Override
	public List<Categoria> buscarTodas() {
		
		return categoriaRepository.findAll();
	}

}
