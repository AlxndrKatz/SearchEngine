package searchengine.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.StatisticsLemma;
import searchengine.morphology.MorphologyInterface;
import searchengine.model.Page;
import searchengine.model.SitePage;
import searchengine.repositories.PageRepository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class LemmaParser implements LemmaInterface {
    private final PageRepository pageRepository;
    private final MorphologyInterface morphologyInterface;
    private List<StatisticsLemma> statisticsLemmaList;

    @Override
    public void run(SitePage site) {
        statisticsLemmaList = new CopyOnWriteArrayList<>();
        Iterable<Page> pageList = pageRepository.findAll();
        TreeMap<String, Integer> lemmasList = new TreeMap<>();
        for (Page page : pageList) {
            String pageContent = page.getContent();
            String title = HTMLParser.parseHtml(pageContent, "title");
            String body = HTMLParser.parseHtml(pageContent, "body");
            HashMap<String, Integer> titleList = morphologyInterface.getLemmaList(title);
            HashMap<String, Integer> bodyList = morphologyInterface.getLemmaList(body);
            Set<String> allTheWords = new HashSet<>();
            allTheWords.addAll(titleList.keySet());
            allTheWords.addAll(bodyList.keySet());
            for (String word : allTheWords) {
                int frequency = lemmasList.getOrDefault(word, 0) + 1;
                lemmasList.put(word, frequency);
            }
        }
        for (String lemma : lemmasList.keySet()) {
            Integer frequency = lemmasList.get(lemma);
            statisticsLemmaList.add(new StatisticsLemma(lemma, frequency));
        }
    }

    public List<StatisticsLemma> getLemmaList() {
        return statisticsLemmaList;
    }
}