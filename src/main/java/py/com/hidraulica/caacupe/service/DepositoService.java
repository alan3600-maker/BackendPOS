package py.com.hidraulica.caacupe.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import py.com.hidraulica.caacupe.domain.Deposito;
import py.com.hidraulica.caacupe.dto.DepositoDto;
import py.com.hidraulica.caacupe.dto.PageResponse;
import py.com.hidraulica.caacupe.exception.NotFoundException;
import py.com.hidraulica.caacupe.repository.DepositoRepository;

@Service
public class DepositoService implements CrudService<Deposito, Long> {
	private final DepositoRepository repo;

	public DepositoService(DepositoRepository repo) {
		this.repo = repo;
	}

	@Override
	public Deposito create(Deposito e) {
		return repo.save(e);
	}

	@Override
	public Deposito get(Long id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException("Deposito no encontrado: " + id));
	}

	@Override
	public List<Deposito> list() {
		return repo.findAll();
	}

	@Override
	public Deposito update(Long id, Deposito e) {
		var cur = get(id);
		cur.setNombre(e.getNombre());
		// si tu entidad tiene más campos, setéalos acá también
		return repo.save(cur);
	}

	@Override
	public void delete(Long id) {
		if (!repo.existsById(id)) {
			throw new NotFoundException("Deposito no encontrado: " + id);
		}
		repo.deleteById(id);
	}

	public DepositoDto toDto(Deposito d) {
		return new DepositoDto(d.getId(), d.getNombre());
	}

	@Transactional(readOnly = true)
	public PageResponse<DepositoDto> searchDto(String q, int page, int size, String sortBy, String dir) {
		// similar a Cliente/Producto: whitelist sortBy y PageRequest
		// implementar spec por nombre like(q)
		if (size <= 0)
			size = 10;
		if (page < 0)
			page = 0;

		// whitelist campos ordenables (según tu entidad)
		Set<String> allowed = Set.of("id", "nombre" );
		if (!StringUtils.hasText(sortBy) || !allowed.contains(sortBy))
			sortBy = "id";

		Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		String s = StringUtils.hasText(q) ? q.trim() : null;

		Page<Deposito> p = repo.search(s, pageable);

		var content = p.getContent().stream().map(this::toDto).toList();
		return new PageResponse<>(content, p.getTotalElements(), p.getTotalPages(), p.getNumber(), p.getSize());
	}
}
