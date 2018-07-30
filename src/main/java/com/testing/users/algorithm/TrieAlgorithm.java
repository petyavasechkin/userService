package com.testing.users.algorithm;


import com.testing.users.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.util.*;

@Component
public class TrieAlgorithm implements Algorithm {

    TrieNode rootNode = new TrieNode();
    AppConfig appConfig;

    TrieAlgorithm(@Autowired AppConfig appConfig) {

        this.appConfig = appConfig;

        List<AppConfig.Pattern> patternParts = new ArrayList<>();
        for(AppConfig.Pattern pattern: appConfig.getPatterns()){

            String path = pattern.getPath().replaceAll(appConfig.getBorderSlash(), "");
            String [] parts = path.split(appConfig.getSlash());
            pattern.setParts(parts);
            patternParts.add(pattern);

            TrieNode node = rootNode;
            createTrie(pattern, parts, node);
        }
    }

    @Override
    public Map<String, Map<String, String>> resolveMethod(String path) {

        path = path.replaceAll(appConfig.getBorderSlash(), "");
        String [] parts = path.split(appConfig.getSlash());

        TrieNode node = rootNode;
        ResolveResult res = new ResolveResult();
        resolveHelper(parts, node, res, 0);

        HashMap<String,Map<String,String>> resolveMethodMap = createResolveMethodMap(res);
        return resolveMethodMap;
    }

    private HashMap<String, Map<String, String>> createResolveMethodMap(ResolveResult res) {

        HashMap<String, String> dynPathParts = new HashMap<>();
        Set<Map.Entry<String, String>> entries = res.dynParts.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String updatedKey = entry.getKey().replaceAll("\\*", "");
            dynPathParts.put(updatedKey, entry.getValue());
        }
        HashMap<String, Map<String, String>> resolveMethodMap = new HashMap<>();
        resolveMethodMap.put(res.method, dynPathParts);
        return resolveMethodMap;
    }

    private void resolveHelper(String[] parts, TrieNode node, ResolveResult res, int level) {

        if (level == parts.length) {
            if (node.isTerminal) {
                res.method = node.method;
            }
            return;
        }
        TrieNode child = node.children.get(parts[level]);
        if (child == null) {

            if (node.wildcards.isEmpty()) {
                new ServletException("Path not found");
            } else {
                for (String wildcard : node.wildcards) {
                    child = node.children.get(wildcard);
                    res.dynParts.put(wildcard, parts[level]);
                    resolveHelper(parts, child, res, level + 1);
                    if (res.method != null) break;
                    res.dynParts.remove(wildcard);
                }
            }
        } else {
            resolveHelper(parts, child, res, level + 1);
        }
    }

    private void createTrie(AppConfig.Pattern pattern, String[] parts, TrieNode node) {

        for (int i = 0; i < parts.length; i++) {

            TrieNode child = node.children.get(parts[i]);
            if (child == null) {
                child = new TrieNode();
                child.part = parts[i];
                node.children.put(parts[i], child);
                if(parts[i].contains(appConfig.getWildcard())){
                    node.wildcards.add(parts[i]);
                }
            }
            node = child;
            if (i == parts.length - 1) {
                node.isTerminal = true;
                node.method = pattern.getMethod();
            }
        }
    }


}

class TrieNode {

    boolean isTerminal;
    String part;
    String method;
    ArrayList<String> wildcards = new ArrayList<>();
    HashMap<String, TrieNode> children = new HashMap<>();
}

class ResolveResult{

    String method;
    HashMap<String, String> dynParts = new HashMap<>();
}



