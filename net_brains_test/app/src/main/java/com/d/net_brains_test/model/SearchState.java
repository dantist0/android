package com.d.net_brains_test.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 06.05.2018.
 */

public class SearchState implements Serializable{
    public List<SearchResponse.Result> search_results = new ArrayList<>();
    public boolean error = false;
    public int page = 0;
}
