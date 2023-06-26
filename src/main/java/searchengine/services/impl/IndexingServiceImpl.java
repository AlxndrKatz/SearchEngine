package searchengine.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.SitePage;
import searchengine.model.Status;
import searchengine.parsers.IndexInterface;
import searchengine.parsers.IndexingSite;
import searchengine.parsers.LemmaInterface;
import searchengine.repositories.IndexRepository;
import searchengine.repositories.LemmaRepository;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.services.IndexingService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndexingServiceImpl implements IndexingService {
    private static final int cpuCoreCount = Runtime.getRuntime().availableProcessors();
    private ExecutorService executorService;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository searchIndexRepository;
    private final LemmaInterface lemmaParserInterface;
    private final IndexInterface indexParserInterface;
    private final SitesList sitesList;

    @Override
    public boolean urlIndexing(String url) {
        if (urlCheck(url)) {
            log.info("Start reindexing site - " + url);
            executorService = Executors.newFixedThreadPool(cpuCoreCount);
            executorService.submit(new IndexingSite(pageRepository, siteRepository,
                    lemmaRepository, searchIndexRepository,
                    lemmaParserInterface, indexParserInterface, url, sitesList));
            executorService.shutdown();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean indexingAll() {
        if (isIndexingActive()) {
            log.debug("Indexing already started");
            return false;
        } else {
            List<Site> siteList = sitesList.getSites();
            executorService = Executors.newFixedThreadPool(cpuCoreCount);
            for (Site site : siteList) {
                String url = site.getUrl();
                SitePage sitePage = new SitePage();
                sitePage.setName(site.getName());
                log.info("Parsing site: " + site.getName());
                executorService.submit(new IndexingSite(pageRepository, siteRepository,
                        lemmaRepository, searchIndexRepository,
                        lemmaParserInterface, indexParserInterface, url, sitesList));
            }
            executorService.shutdown();
        }
        return true;
    }

    @Override
    public boolean stopIndexing() {
        if (isIndexingActive()) {
            log.info("Indexing stopped");
            executorService.shutdownNow();
            return true;
        } else {
            log.info("Indexing was not stopped because it was not started");
            return false;
        }
    }

    private boolean isIndexingActive() {
        siteRepository.flush();
        Iterable<SitePage> siteList = siteRepository.findAll();
        for (SitePage site : siteList) {
            if (site.getStatus() == Status.INDEXING) {
                return true;
            }
        }
        return false;
    }

    private boolean urlCheck(String url) {
        List<Site> urlList = sitesList.getSites();
        for (Site site : urlList) {
            if (site.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }
}