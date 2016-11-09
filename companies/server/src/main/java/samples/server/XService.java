package samples.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
class XService {
    private final static int UPDATE_WEEKLY = 7*24*60*60*1000;
    final static int BACK_TO_THREE_MONTHS = 3;

    @Autowired
    private XRepository repository;

    @Scheduled(fixedRate = UPDATE_WEEKLY)
    @Transactional
    private void updateRepository() {
        final Date from = Date.from(LocalDateTime.now().minusMonths(BACK_TO_THREE_MONTHS).atZone(ZoneId.systemDefault()).toInstant());
        final Set<Integer> all = Downloader.getSitemaps().flatMap(Downloader::getCompanies).collect(Collectors.toSet());
        final Set<Integer> before = repository.findDistinctByCreatedBefore(from).stream().map(Company::getKey).collect(Collectors.toSet());

        Set<Integer> after = new HashSet<Integer>(all);
        after.removeAll(before);
        after.forEach(k -> {
            Company company = repository.findByKey(k);
            if (company == null) {
                company = new Company(k);
            }
            company.addKeywords(Downloader.getKeywords(k));
            repository.save(company);
        });
    }

    @Transactional(readOnly = true)
    List<Integer> getCompanies(Date created, List<String> keywords) {
        return repository.findDistinctByCreatedAfterAndKeywords_ValueIn(created, keywords).stream().map(Company::getKey).collect(Collectors.toList());
    }
}
