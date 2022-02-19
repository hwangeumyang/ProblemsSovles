package kakaoblind2022.reportresult;

import java.util.*;

class Solution {
    public int[] solution(String[] id_list, String[] report, int k) {
        int memleng = id_list.length;
        int[] answer = new int[memleng];
        Map<String, Integer> ordermap = new HashMap<>();
        Map<String, List<String>> reportedmap = new HashMap<>();
        
        //init
        for(int i=0; i<memleng; ++i) {
            answer[i] = 0;          
            reportedmap.put(id_list[i], new ArrayList<>());
            ordermap.put(id_list[i], i);
        }
        
        //sort and eliminate 'same report'
        Arrays.sort(report);
        ArrayList<String> rptlist = new ArrayList<>(Arrays.asList(report));
        for (int i=rptlist.size()-1; i>0; --i) {
            if(rptlist.get(i).equals(rptlist.get(i-1))) rptlist.remove(i);                        
        }
        
        //reporting
        for(String rpt : rptlist) {
            String[] relation = rpt.split(" ");
            reportedmap.get(relation[1]).add(relation[0]);
            System.out.println(rpt);
        }
        
        //resulting
        for(Map.Entry<String, List<String>> entry : reportedmap.entrySet()) {
            List<String> names = entry.getValue();
            if(names.size()>=k) {
                for(String name : names) {
                    answer[ordermap.get(name)]++;
                }               
            }
        }
        
        return answer;
    }
}