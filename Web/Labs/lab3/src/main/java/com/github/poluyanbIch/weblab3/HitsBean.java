package com.github.poluyanbIch.weblab3;

import com.github.poluyanbIch.weblab3.model.Hit;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean(name = "hitsBean")
@SessionScoped
public class HitsBean implements Serializable {
    @ManagedProperty("#{hitRepository}")
    private HitRepository hitRepository;
    private List<Hit> hits;

    public HitsBean() {}

    @PostConstruct
    public void init() {
        hits = hitRepository.getAll();
        if (hits == null) {
            hits = new ArrayList<>();
        }
    }
    public void clear() {
        try {
            hitRepository.clear();
            hits = new ArrayList<>();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addHit(Hit hit) {
        try {
            hitRepository.save(hit);
            hits = hitRepository.getAll();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateHitsAsJson() {

        return "[" + hits.stream()
                .map(Hit::toJson)
                .collect(Collectors.joining(",")) + "]";
    }
    public String formatCell(String content, int maxLength, int maxVisible, int minEnd) {
        if (content == null) return "";
        if (content.length() > maxLength) {
            String start = content.substring(0, Math.min(maxVisible, content.length()));
            String end = content.substring(Math.max(content.length() - minEnd, 0));
            return start + "..." + end;
        }
        return content;
    }

    public String formatCell(String content) {
        return formatCell(content, 7, 5, 2);
    }


    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public void setHitRepository(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    public HitRepository getHitRepository() {
        return hitRepository;
    }
}
