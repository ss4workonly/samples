package samples.server;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
interface XRepository extends CrudRepository<Company, Long> {
    @Cacheable("company")
    Company findByKey(Integer key);
    @Cacheable("companies")
    List<Company> findDistinctByCreatedAfterAndKeywords_ValueIn(Date created, List<String> keywords);
    List<Company> findDistinctByCreatedBefore(Date created);
}
