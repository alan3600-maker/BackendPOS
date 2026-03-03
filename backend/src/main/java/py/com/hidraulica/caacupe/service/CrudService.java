package py.com.hidraulica.caacupe.service;

import java.util.List;

public interface CrudService<T, ID> {
  T create(T entity);
  T get(ID id);
  List<T> list();
  T update(ID id, T entity);
  void delete(ID id);
}
